package com.unloop.app.service

import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.unloop.app.data.StatsRepository
import kotlinx.coroutines.*

/**
 * OPTIMIZED Service for music notification detection
 * - Minimal logging
 * - Better song ID (no hash collisions)
 * - Debounced processing
 */
class MusicNotificationListenerService : NotificationListenerService() {
    
    companion object {
        private const val TAG = "MusicNotif"
        
        // Fast lookup with HashSet
        private val MUSIC_PACKAGES = hashSetOf(
            "com.google.android.youtube",
            "com.google.android.apps.youtube.music",
            "com.spotify.music",
            "app.revanced.android.youtube",
            "app.revanced.android.apps.youtube.music"
        )
        
        const val ACTION_SONG_DETECTED = "com.unloop.app.SONG_DETECTED"
        const val EXTRA_SONG_ID = "song_id"
        const val EXTRA_SONG_TITLE = "song_title"
        const val EXTRA_SONG_ARTIST = "song_artist"
        const val EXTRA_PLATFORM = "platform"
    }
    
    // Reuse single coroutine scope - avoid creating new scopes
    private var serviceJob: Job? = null
    private val serviceScope by lazy { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
    
    private var currentSongId: String? = null
    private var lastBroadcastTime: Long = 0
    private val statsRepository by lazy { StatsRepository.getInstance(this) }
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Started")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceJob?.cancel()
        serviceScope.cancel()
    }
    
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Fast package check with HashSet
        if (sbn.packageName !in MUSIC_PACKAGES) return
        
        val extras = sbn.notification.extras
        
        // Extract title - most notifications have this
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()?.trim()
        if (title.isNullOrBlank()) return
        
        // Extract artist with fallbacks
        val artist = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()?.trim()
            ?: extras.getCharSequence(Notification.EXTRA_INFO_TEXT)?.toString()?.trim()
            ?: extras.getCharSequence(Notification.EXTRA_SUB_TEXT)?.toString()?.trim()
            ?: return
        
        if (artist.isBlank()) return
        
        val platform = getPlatformName(sbn.packageName)
        
        // Better song ID: use sanitized title+artist (no hash collisions!)
        val songId = "${platform}_${title.lowercase().take(50)}_${artist.lowercase().take(30)}"
        
        // Skip if same song (avoid duplicate broadcasts)
        if (songId == currentSongId) return
        
        // Debounce: max 1 broadcast per 500ms
        val now = System.currentTimeMillis()
        if (now - lastBroadcastTime < 500) return
        
        currentSongId = songId
        lastBroadcastTime = now
        
        // Broadcast immediately (no coroutine needed)
        sendBroadcast(Intent(ACTION_SONG_DETECTED).apply {
            putExtra(EXTRA_SONG_ID, songId)
            putExtra(EXTRA_SONG_TITLE, title)
            putExtra(EXTRA_SONG_ARTIST, artist)
            putExtra(EXTRA_PLATFORM, platform)
            setPackage(packageName)
        })
    }
    
    private fun getPlatformName(packageName: String): String {
        return when (packageName) {
            "com.google.android.youtube", "app.revanced.android.youtube" -> "yt"
            "com.google.android.apps.youtube.music", "app.revanced.android.apps.youtube.music" -> "ytm"
            "com.spotify.music" -> "sp"
            else -> "x"
        }
    }
    
    override fun onListenerConnected() {
        Log.d(TAG, "Connected")
    }
    
    override fun onListenerDisconnected() {
        Log.d(TAG, "Disconnected")
    }
}
