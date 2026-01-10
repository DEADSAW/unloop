package com.unloop.app.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

/**
 * Repository for comprehensive listening statistics
 */
class StatsRepository(context: Context) {
    
    private val database = UnloopDatabase.getDatabase(context)
    private val dao = database.songDao()
    
    // Session tracking
    private var sessionStartTime = System.currentTimeMillis()
    private var sessionSongsDiscovered = 0
    private var sessionLoopsAvoided = 0
    private var sessionListenTimeMs = 0L
    
    /**
     * Get comprehensive listening statistics
     */
    suspend fun getStats(): ListeningStats {
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
        
        return ListeningStats(
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
    suspend fun recordSongPlay(
        songId: String,
        title: String,
        artist: String,
        platform: String,
        listenTimeMs: Long
    ) {
        var song = dao.getSongById(songId)
        
        if (song == null) {
            // New song discovered
            song = Song(
                id = songId,
                title = title,
                artist = artist,
                platform = platform,
                playCount = 1,
                firstPlayedAt = System.currentTimeMillis(),
                lastPlayedAt = System.currentTimeMillis(),
                totalListenTimeMs = listenTimeMs
            )
            sessionSongsDiscovered++
        } else {
            song.playCount++
            song.lastPlayedAt = System.currentTimeMillis()
            song.totalListenTimeMs += listenTimeMs
        }
        
        // Update platform-specific stats
        when {
            platform.contains("youtube") -> {
                song.youtubePlayCount++
                song.youtubeListenTimeMs += listenTimeMs
            }
            platform.contains("spotify") -> {
                song.spotifyPlayCount++
                song.spotifyListenTimeMs += listenTimeMs
            }
        }
        
        song.affectionScore = song.calculateAffectionScore()
        dao.insertSong(song)
        
        // Update session stats
        sessionListenTimeMs += listenTimeMs
        
        // Update daily stats
        updateDailyStats(platform, listenTimeMs, isNewSong = song.playCount == 1)
    }
    
    /**
     * Record a loop avoided (song was auto-skipped)
     */
    suspend fun recordLoopAvoided(songId: String) {
        val song = dao.getSongById(songId) ?: return
        song.loopsAvoided++
        song.skipCount++
        dao.updateSong(song)
        
        sessionLoopsAvoided++
        
        // Update daily stats
        val today = getTodayString()
        var dailyStats = dao.getDailyStats(today) ?: DailyStats(date = today)
        dailyStats.loopsAvoided++
        dao.insertDailyStats(dailyStats)
    }
    
    /**
     * Reset session stats
     */
    fun resetSession() {
        sessionStartTime = System.currentTimeMillis()
        sessionSongsDiscovered = 0
        sessionLoopsAvoided = 0
        sessionListenTimeMs = 0L
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
}
