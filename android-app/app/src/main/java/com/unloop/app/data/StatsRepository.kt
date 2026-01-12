package com.unloop.app.data

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.unloop.app.StatsWidgetProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

/**
 * Repository for comprehensive listening statistics
 */
class StatsRepository private constructor(private val context: Context) {

    companion object {
        @Volatile
        private var instance: StatsRepository? = null

        fun getInstance(context: Context): StatsRepository {
            return instance ?: synchronized(this) {
                instance ?: StatsRepository(context.applicationContext).also { instance = it }
            }
        }
    }
    
    private val database = UnloopDatabase.getDatabase(context)
    private val dao = database.songDao()
    
    // Session tracking
    private var sessionStartTime = System.currentTimeMillis()
    private var sessionSongsDiscovered = 0
    private var sessionLoopsAvoided = 0
    private var sessionListenTimeMs = 0L
    
    // Performance: Debounce widget updates (max once per 2 seconds)
    private var lastWidgetUpdateTime = 0L
    private val WIDGET_UPDATE_DEBOUNCE_MS = 2000L
    
    /**
     * Get comprehensive listening statistics
     */
    suspend fun getStats(): ListeningStats = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val todayStart = getStartOfDay(now)
        val weekStart = getStartOfWeek(now)
        val monthStart = getStartOfMonth(now)
        
        val totalSongs = dao.getTotalSongCount()
        val totalArtists = dao.getUniqueArtistCount()
        val totalLoops = dao.getTotalLoopsAvoided() ?: 0
        
        val totalListenTime = dao.getTotalListenTime() ?: 0L
        val youtubeListenTime = dao.getYoutubeListenTime() ?: 0L
        val spotifyListenTime = dao.getSpotifyListenTime() ?: 0L
        
        val youtubePlays = dao.getYoutubePlays() ?: 0
        val spotifyPlays = dao.getSpotifyPlays() ?: 0
        
        val newSongsToday = dao.getSongsDiscoveredSince(todayStart)
        val newSongsThisWeek = dao.getSongsDiscoveredSince(weekStart)
        val newSongsThisMonth = dao.getSongsDiscoveredSince(monthStart)
        val newArtistsToday = dao.getArtistsDiscoveredSince(todayStart)
        val newArtistsThisWeek = dao.getArtistsDiscoveredSince(weekStart)
        
        val topArtists = dao.getTopArtists(10)
        val topSongs = dao.getTopSongs(10)
        val recentSongs = dao.getRecentSongs(20)
        
        // Calculate health scores
        val freshnessScore = calculateFreshnessScore(newSongsThisWeek, totalSongs)
        val varietyScore = calculateVarietyScore(topArtists, totalSongs)
        val intelligenceScore = calculateIntelligenceScore(totalSongs, totalLoops)
        
        ListeningStats(
            totalSongsDiscovered = totalSongs,
            totalLoopsAvoided = totalLoops,
            totalArtistsDiscovered = totalArtists,
            totalListenTimeMs = totalListenTime,
            youtubeListenTimeMs = youtubeListenTime,
            spotifyListenTimeMs = spotifyListenTime,
            youtubePlays = youtubePlays,
            spotifyPlays = spotifyPlays,
            newSongsToday = newSongsToday,
            newSongsThisWeek = newSongsThisWeek,
            newSongsThisMonth = newSongsThisMonth,
            newArtistsToday = newArtistsToday,
            newArtistsThisWeek = newArtistsThisWeek,
            sessionSongs = sessionSongsDiscovered,
            sessionLoopsAvoided = sessionLoopsAvoided,
            sessionListenTimeMs = sessionListenTimeMs,
            topArtists = topArtists,
            topSongs = topSongs,
            recentSongs = recentSongs,
            freshnessScore = freshnessScore,
            intelligenceScore = intelligenceScore,
            varietyScore = varietyScore
        )
    }
    
    /**
     * Get daily stats for charts
     */
    suspend fun getDailyStatsForChart(days: Int): List<DailyStats> {
        return dao.getRecentDailyStats(days)
    }
    
    fun getDailyStatsFlow(days: Int): Flow<List<DailyStats>> {
        return dao.getRecentDailyStatsFlow(days)
    }
    
    /**
     * Record a new song play
     */
    /**
     * Check if session shouldnbe reset (10 mins inactivity)
     */
    fun checkSessionTimeout() {
        val now = System.currentTimeMillis()
        if (now - sessionStartTime > 10 * 60 * 1000 && sessionListenTimeMs == 0L) {
            // Initial start
        } else if (now - lastActivityTime > 10 * 60 * 1000) {
            resetSession()
        }
        lastActivityTime = now
    }
    
    private var lastActivityTime = System.currentTimeMillis()

    /**
     * Increment play count for a song (Start of play)
     */
    suspend fun incrementPlayCount(
        songId: String,
        title: String,
        artist: String,
        platform: String
    ) = withContext(Dispatchers.IO) {
        checkSessionTimeout()
        
        var song = dao.getSongById(songId)
        val now = System.currentTimeMillis()
        
        if (song == null) {
            // New song discovered
            song = Song(
                id = songId,
                title = title,
                artist = artist,
                platform = platform,
                playCount = 1,
                firstPlayedAt = now,
                lastPlayedAt = now,
                totalListenTimeMs = 0
            )
            sessionSongsDiscovered++
            updateDailyStats(platform, 0, isNewSong = true)
        } else {
            song.playCount++
            song.lastPlayedAt = now
            updateDailyStats(platform, 0, isNewSong = false)
        }
        
        // Update platform-specific counts
        when {
            platform.contains("youtube") -> song.youtubePlayCount++
            platform.contains("spotify") -> song.spotifyPlayCount++
        }
        
        song.affectionScore = song.calculateAffectionScore()
        dao.insertSong(song)
    }

    /**
     * Record listening time for a song (End of play)
     */
    suspend fun recordListenTime(songId: String, durationMs: Long) = withContext(Dispatchers.IO) {
        if (durationMs <= 0) return@withContext
        
        val song = dao.getSongById(songId) ?: return@withContext
        song.totalListenTimeMs += durationMs
        
        when {
            song.platform.contains("youtube") -> song.youtubeListenTimeMs += durationMs
            song.platform.contains("spotify") -> song.spotifyListenTimeMs += durationMs
        }
        
        dao.updateSong(song)
        
        sessionListenTimeMs += durationMs
        updateDailyStats(song.platform, durationMs, isNewSong = false)
        triggerWidgetUpdate()
    }
    
    /**
     * Record a loop avoided (song was auto-skipped)
     */
    suspend fun recordLoopAvoided(songId: String) = withContext(Dispatchers.IO) {
        checkSessionTimeout()
        val song = dao.getSongById(songId) ?: return@withContext
        song.loopsAvoided++
        song.skipCount++
        dao.updateSong(song)
        
        sessionLoopsAvoided++
        
        // Update daily stats
        val today = getTodayString()
        var dailyStats = dao.getDailyStats(today) ?: DailyStats(date = today)
        dailyStats.loopsAvoided++
        dao.insertDailyStats(dailyStats)
        triggerWidgetUpdate()
    }
    
    /**
     * Reset session stats
     */
    fun resetSession() {
        CoroutineScope(Dispatchers.IO).launch {
            sessionStartTime = System.currentTimeMillis()
            sessionSongsDiscovered = 0
            sessionLoopsAvoided = 0
            sessionListenTimeMs = 0L
            triggerWidgetUpdate()
        }
    }
    
    private suspend fun updateDailyStats(platform: String, listenTimeMs: Long, isNewSong: Boolean) {
        val today = getTodayString()
        var dailyStats = dao.getDailyStats(today) ?: DailyStats(date = today)
        
        if (isNewSong) {
            dailyStats.songsDiscovered++
        }
        
        dailyStats.listenTimeMs += listenTimeMs
        
        when {
            platform.contains("youtube") -> dailyStats.youtubePlays++
            platform.contains("spotify") -> dailyStats.spotifyPlays++
        }
        
        dao.insertDailyStats(dailyStats)
    }
    
    private fun calculateFreshnessScore(newSongsThisWeek: Int, totalSongs: Int): Float {
        if (totalSongs == 0) return 0f
        // Freshness is high when we're discovering lots of new songs
        return (newSongsThisWeek.toFloat() / maxOf(totalSongs, 1) * 100).coerceIn(0f, 100f)
    }
    
    private fun calculateVarietyScore(topArtists: List<ArtistStats>, totalSongs: Int): Float {
        if (totalSongs == 0 || topArtists.isEmpty()) return 0f
        // Variety is high when top artist doesn't dominate
        val topArtistRatio = topArtists.first().songCount.toFloat() / totalSongs
        return ((1f - topArtistRatio) * 100).coerceIn(0f, 100f)
    }
    
    private fun calculateIntelligenceScore(totalSongs: Int, loopsAvoided: Int): Float {
        // Intelligence grows with more data
        val dataScore = minOf(totalSongs / 100f, 0.5f) // Max 50 from songs
        val loopScore = minOf(loopsAvoided / 50f, 0.5f) // Max 50 from loops
        return ((dataScore + loopScore) * 100).coerceIn(0f, 100f)
    }
    
    private fun getTodayString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }
    
    private fun getStartOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    private fun getStartOfWeek(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = getStartOfDay(timestamp)
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        return calendar.timeInMillis
    }
    
    private fun getStartOfMonth(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = getStartOfDay(timestamp)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return calendar.timeInMillis
    }
    
    private fun triggerWidgetUpdate() {
        // Debounce: max once per 2 seconds to prevent excessive updates
        val now = System.currentTimeMillis()
        if (now - lastWidgetUpdateTime < WIDGET_UPDATE_DEBOUNCE_MS) return
        lastWidgetUpdateTime = now
        
        try {
            val intent = Intent(context, StatsWidgetProvider::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(
                ComponentName(context, StatsWidgetProvider::class.java)
            )
            // Only update if there are widgets on screen
            if (ids.isNotEmpty()) {
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                context.sendBroadcast(intent)
            }
        } catch (e: Exception) {
            // Silently ignore widget errors
        }
    }
    
    /**
     * Lightweight stats for widget - only essential data, minimal queries
     */
    fun getWidgetStats(): WidgetStats {
        return WidgetStats(
            sessionListenTimeMs = sessionListenTimeMs,
            sessionLoopsAvoided = sessionLoopsAvoided,
            totalListenTimeMs = sessionListenTimeMs // Use session as proxy to avoid DB query
        )
    }
}

data class WidgetStats(
    val sessionListenTimeMs: Long,
    val sessionLoopsAvoided: Int,
    val totalListenTimeMs: Long
)
