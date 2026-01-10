package com.unloop.app.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a song in the listening history with comprehensive analytics
 */
@Entity(
    tableName = "songs",
    indices = [
        Index(value = ["artist"]),
        Index(value = ["lastPlayedAt"]),
        Index(value = ["platform"])
    ]
)
data class Song(
    @PrimaryKey
    val id: String,  // Unique identifier (e.g., "youtube_videoId" or "spotify_trackId")
    
    val title: String,
    val artist: String,
    val platform: String,  // "youtube", "youtube_music", "spotify"
    
    // Play statistics
    var playCount: Int = 0,
    var skipCount: Int = 0,
    var quickSkipCount: Int = 0,  // Skips under 20 seconds
    var loopsAvoided: Int = 0,    // Times this song was auto-skipped by Unloop
    
    // Timing
    var firstPlayedAt: Long = System.currentTimeMillis(),
    var lastPlayedAt: Long = System.currentTimeMillis(),
    var totalListenTimeMs: Long = 0,
    
    // Per-platform stats
    var youtubePlayCount: Int = 0,
    var spotifyPlayCount: Int = 0,
    var youtubeListenTimeMs: Long = 0,
    var spotifyListenTimeMs: Long = 0,
    
    // Lists
    var isWhitelisted: Boolean = false,
    var isBlacklisted: Boolean = false,
    
    // Smart Auto mode data
    var affectionScore: Float = 0.5f  // 0.0 (dislike) to 1.0 (love)
) {
    /**
     * Calculate average listen duration as a percentage of song length
     */
    fun avgListenDuration(): Float {
        if (playCount == 0) return 0f
        val avgSongLengthMs = 210_000L
        val avgListenMs = totalListenTimeMs / playCount
        return (avgListenMs.toFloat() / avgSongLengthMs).coerceIn(0f, 1f)
    }
    
    /**
     * Calculate affection score based on listening behavior
     */
    fun calculateAffectionScore(): Float {
        if (playCount == 0) return 0.5f
        
        val listenWeight = avgListenDuration() * 0.6f
        val totalInteractions = playCount + skipCount
        val playRatio = if (totalInteractions > 0) playCount.toFloat() / totalInteractions else 0.5f
        val ratioWeight = playRatio * 0.3f
        val quickSkipPenalty = if (playCount > 0) {
            (quickSkipCount.toFloat() / playCount) * 0.1f
        } else 0f
        
        return (listenWeight + ratioWeight - quickSkipPenalty).coerceIn(0f, 1f)
    }
    
    fun calculateSmartCooldownHours(): Float {
        if (skipCount >= 3 || quickSkipCount >= 2) {
            return 90 * 24f
        }
        return 4f + (1f - affectionScore) * 160f
    }
    
    fun isOnCooldown(cooldownHours: Float): Boolean {
        val cooldownMs = (cooldownHours * 60 * 60 * 1000).toLong()
        return System.currentTimeMillis() - lastPlayedAt < cooldownMs
    }
}

/**
 * Comprehensive listening statistics
 */
data class ListeningStats(
    // Core counts
    val totalSongsDiscovered: Int = 0,
    val totalLoopsAvoided: Int = 0,
    val totalArtistsDiscovered: Int = 0,
    
    // Listening time
    val totalListenTimeMs: Long = 0,
    val youtubeListenTimeMs: Long = 0,
    val spotifyListenTimeMs: Long = 0,
    
    // Play counts per platform
    val youtubePlays: Int = 0,
    val spotifyPlays: Int = 0,
    
    // Discovery metrics
    val newSongsToday: Int = 0,
    val newSongsThisWeek: Int = 0,
    val newSongsThisMonth: Int = 0,
    val newArtistsToday: Int = 0,
    val newArtistsThisWeek: Int = 0,
    
    // Session stats
    val sessionSongs: Int = 0,
    val sessionLoopsAvoided: Int = 0,
    val sessionListenTimeMs: Long = 0,
    
    // Top items
    val topArtists: List<ArtistStats> = emptyList(),
    val topSongs: List<Song> = emptyList(),
    val recentSongs: List<Song> = emptyList(),
    
    // Health scores
    val freshnessScore: Float = 0f,      // % of new songs vs repeats
    val intelligenceScore: Float = 0f,   // How well AI knows your taste
    val varietyScore: Float = 0f         // Artist diversity
) {
    val totalListenHours: Float get() = totalListenTimeMs / 3600000f
    val youtubeListenHours: Float get() = youtubeListenTimeMs / 3600000f
    val spotifyListenHours: Float get() = spotifyListenTimeMs / 3600000f
    val sessionListenMinutes: Float get() = sessionListenTimeMs / 60000f
    
    val youtubePercentage: Float get() = if (totalListenTimeMs > 0) 
        (youtubeListenTimeMs.toFloat() / totalListenTimeMs) * 100 else 0f
    val spotifyPercentage: Float get() = if (totalListenTimeMs > 0) 
        (spotifyListenTimeMs.toFloat() / totalListenTimeMs) * 100 else 0f
}

/**
 * Artist statistics
 */
data class ArtistStats(
    val name: String,
    val songCount: Int,
    val totalPlayCount: Int,
    val totalListenTimeMs: Long,
    val loopsAvoided: Int
) {
    val listenHours: Float get() = totalListenTimeMs / 3600000f
}

/**
 * Daily stats for charts
 */
@Entity(tableName = "daily_stats")
data class DailyStats(
    @PrimaryKey
    val date: String,  // Format: "2024-01-15"
    var songsDiscovered: Int = 0,
    var loopsAvoided: Int = 0,
    var listenTimeMs: Long = 0,
    var youtubePlays: Int = 0,
    var spotifyPlays: Int = 0
)

/**
 * Skip history entry for logging all skips
 */
@Entity(tableName = "skip_history")
data class SkipHistoryEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val songId: String,
    val songTitle: String,
    val songArtist: String,
    val platform: String,
    val reason: String,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Discovery mode options
 */
enum class DiscoveryMode {
    SMART_AUTO,
    STRICT,
    MEMORY_FADE,
    SEMI_STRICT,
    ARTIST_SMART
}

/**
 * Settings data class
 */
data class UnloopSettings(
    val isEnabled: Boolean = true,
    val mode: DiscoveryMode = DiscoveryMode.SMART_AUTO,
    val memoryFadeHours: Int = 168,
    val newSongsRequired: Int = 10,
    val maxArtistRepeat: Int = 3,
    val isDarkMode: Boolean = true,
    val showNotifications: Boolean = true
)
