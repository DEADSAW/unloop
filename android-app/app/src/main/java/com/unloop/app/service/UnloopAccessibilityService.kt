package com.unloop.app.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Path
import android.media.AudioManager
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.unloop.app.data.DiscoveryMode
import com.unloop.app.data.SkipHistoryEntry
import com.unloop.app.data.Song
import com.unloop.app.data.StatsRepository
import com.unloop.app.data.UnloopDatabase
import kotlinx.coroutines.*

import com.unloop.app.data.PreferencesManager
import kotlinx.coroutines.flow.first

/**
 * Service to handle accessibility events for skipping songs
 */
class UnloopAccessibilityService : AccessibilityService() {

    private lateinit var prefs: PreferencesManager
    
    companion object {
        private const val TAG = "UnloopAccessibility"
        const val ACTION_SONG_SKIPPED = "com.unloop.app.SONG_SKIPPED"
        
        // Multiple button ID options for each platform (apps update their IDs)
        private val SPOTIFY_NEXT_BUTTONS = listOf(
            "com.spotify.music:id/btn_next",
            "com.spotify.music:id/next_button",
            "com.spotify.music:id/skip_next_button",
            "com.spotify.music:id/player_controls_next"
        )
        
        private val YTM_NEXT_BUTTONS = listOf(
            "com.google.android.apps.youtube.music:id/player_next_button",
            "com.google.android.apps.youtube.music:id/next_button",
            "com.google.android.apps.youtube.music:id/player_control_next",
            "com.google.android.apps.youtube.music:id/next",
            "com.google.android.apps.youtube.music:id/skip_next",
            "com.google.android.apps.youtube.music:id/player_controls_next_button",
            "com.google.android.apps.youtube.music:id/topbar_menu_next"
        )
        
        private val YOUTUBE_NEXT_BUTTONS = listOf(
            "com.google.android.youtube:id/player_next_button",
            "com.google.android.youtube:id/next_button",
            "com.google.android.youtube:id/player_control_next",
            "com.google.android.youtube:id/next",
            "com.google.android.youtube:id/autonav_toggle_button",
            "com.google.android.youtube:id/player_overflow_button"
        )
        
        // Keywords for content description search (YouTube-specific)
        private val SKIP_KEYWORDS = listOf(
            "next", "skip", "forward", 
            "next video", "next track", "skip ahead",
            "skip to next", "next song", "skip ad",
            "player next", "skip intro"
        )
        
        var isEnabled = true
        var currentMode = DiscoveryMode.SMART_AUTO
        var memoryFadeHours = 168
        var newSongsRequired = 10
        var maxArtistRepeat = 3
        
        private var instance: UnloopAccessibilityService? = null
        fun getInstance(): UnloopAccessibilityService? = instance
    }
    
    // Optimized coroutine scope - use Default for CPU work, IO only when needed
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val mainHandler = Handler(Looper.getMainLooper())
    private val database by lazy { UnloopDatabase.getDatabase(this) }
    private val statsRepository by lazy { StatsRepository.getInstance(this) }
    
    private var lastProcessedSongId: String? = null
    private var lastSongStartTime: Long = 0
    private var recentArtists = mutableListOf<String>()
    private var newSongsSinceLastRepeat = 0
    
    // MediaSessionManager for direct skip control
    private var mediaSessionManager: MediaSessionManager? = null
    private var audioManager: AudioManager? = null
    
    // Current song info for skip logging
    private var currentSkipSongId: String? = null
    private var currentSkipSongTitle: String? = null
    private var currentSkipSongArtist: String? = null
    private var currentSkipPlatform: String? = null
    
    // Debouncing - prevent duplicate skips
    private var lastSkipTime: Long = 0
    private var lastSkipSongId: String? = null
    private val SKIP_DEBOUNCE_MS = 2000L // 2 second cooldown between skips
    
    private val songReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == MusicNotificationListenerService.ACTION_SONG_DETECTED) {
                val songId = intent.getStringExtra(MusicNotificationListenerService.EXTRA_SONG_ID) ?: return
                val title = intent.getStringExtra(MusicNotificationListenerService.EXTRA_SONG_TITLE) ?: return
                val artist = intent.getStringExtra(MusicNotificationListenerService.EXTRA_SONG_ARTIST) ?: return
                val platform = intent.getStringExtra(MusicNotificationListenerService.EXTRA_PLATFORM) ?: return
                
                Log.d(TAG, "Song detected broadcast received: $title by $artist on $platform")
                
                // Store current song info for skip logging
                currentSkipSongId = songId
                currentSkipSongTitle = title
                currentSkipSongArtist = artist
                currentSkipPlatform = platform
                
                serviceScope.launch {
                    checkAndSkipIfNeeded(songId, title, artist, platform)
                }
            }
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Initialize managers for skip control
        mediaSessionManager = getSystemService(Context.MEDIA_SESSION_SERVICE) as? MediaSessionManager
        audioManager = getSystemService(Context.AUDIO_SERVICE) as? AudioManager
        prefs = PreferencesManager(this)
        
        // Initialize notification
        NotificationHelper.init(this)
        NotificationHelper.showActive(this)
        
        val filter = IntentFilter(MusicNotificationListenerService.ACTION_SONG_DETECTED)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(songReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(songReceiver, filter)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        instance = null
        try {
            unregisterReceiver(songReceiver)
        } catch (e: Exception) {
            // Ignore
        }
        serviceScope.cancel()
    }
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Accessibility service connected - READY FOR AUTO-SKIP")
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Using broadcast receiver for song detection
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted")
    }
    
    private suspend fun checkAndSkipIfNeeded(songId: String, title: String, artist: String, platform: String) {
        if (!isEnabled || songId == lastProcessedSongId) return
        
        // 1. Record time for PREVIOUS song (if valid and not skipped)
        val now = System.currentTimeMillis()
        if (lastProcessedSongId != null && lastSongStartTime > 0) {
             val duration = now - lastSongStartTime
             // Cap at 20 mins to avoid idle time counting errors
             if (duration < 20 * 60 * 1000) {
                 statsRepository.recordListenTime(lastProcessedSongId!!, duration)
             }
        }
        
        lastProcessedSongId = songId
        
        val dao = database.songDao()
        val song = dao.getSongById(songId)
        
        // Whitelist Check
        if (song?.isWhitelisted == true) {
            statsRepository.incrementPlayCount(songId, title, artist, platform)
            NotificationHelper.showNowPlaying(title, artist, isNew = false)
            lastSongStartTime = now
            return
        }

        // Blacklist Check
        if (song?.isBlacklisted == true) {
            statsRepository.recordLoopAvoided(songId)
            triggerSkip(platform)
            lastSongStartTime = 0
            return
        }
        
        // New Song (Unknown)
        if (song == null) {
            statsRepository.incrementPlayCount(songId, title, artist, platform)
            NotificationHelper.showNowPlaying(title, artist, isNew = true)
            lastSongStartTime = now
            return
        }
        
        // KNOWN SONG - Check Mode Logic
        val mode = prefs.discoveryMode.first()
        var shouldSkip = false
        var skipReason = "Repeat"

        when (mode) {
            DiscoveryMode.STRICT -> {
                shouldSkip = true
                skipReason = "Strict Mode"
            }
            DiscoveryMode.SEMI_STRICT -> { // Threshold
                val threshold = prefs.skipThreshold.first()
                if (song.playCount >= threshold) {
                    shouldSkip = true
                    skipReason = "Played ${song.playCount}/${threshold} times"
                }
            }
            DiscoveryMode.MEMORY_FADE -> { // Time-based
                val daysLimit = prefs.memoryFadeDays.first()
                val msLimit = daysLimit * 24 * 60 * 60 * 1000L
                val timeSince = System.currentTimeMillis() - song.lastPlayedAt
                if (timeSince < msLimit) {
                    shouldSkip = true
                    skipReason = "Played recently (< $daysLimit days)"
                }
            }
            DiscoveryMode.SMART_AUTO -> {
                val (skip, reason) = shouldSkipSmartAuto(song, artist)
                shouldSkip = skip
                skipReason = reason
            }
            else -> shouldSkip = true
        }
        
        if (shouldSkip) {
            NotificationHelper.showSkipped(title, artist)
            statsRepository.recordLoopAvoided(songId)
            triggerSkip(platform)
            lastSongStartTime = 0
        } else {
            // Allowed to play - update stats
            statsRepository.incrementPlayCount(songId, title, artist, platform)
            NotificationHelper.showNowPlaying(title, artist, isNew = false)
            lastSongStartTime = now
        }
    }
    
    private suspend fun shouldSkipSmartAuto(song: Song, artist: String): Pair<Boolean, String> {
        val dao = database.songDao()
        val recentSongs = dao.getRecentSongs(5)
        
        if (recentSongs.any { it.id == song.id }) {
            return Pair(true, "Heard too recently")
        }
        
        if (song.skipCount >= 3 || song.quickSkipCount >= 2) {
            return Pair(true, "You usually skip this")
        }
        
        val artistCount = recentArtists.count { it.equals(artist, ignoreCase = true) }
        if (artistCount >= 3 && song.avgListenDuration() < 0.75f) {
            return Pair(true, "Keeping artist variety")
        }
        
        val cooldownHours = song.calculateSmartCooldownHours()
        if (song.isOnCooldown(cooldownHours)) {
            return Pair(true, "Still on cooldown")
        }
        
        return Pair(false, "Smart choice ✨")
    }
    
    private fun shouldSkipStrict(song: Song): Pair<Boolean, String> {
        return if (song.playCount > 0) {
            Pair(true, "Already heard before")
        } else {
            Pair(false, "First time playing")
        }
    }
    
    private fun shouldSkipMemoryFade(song: Song): Pair<Boolean, String> {
        val cooldownMs = memoryFadeHours * 60 * 60 * 1000L
        val timeSinceLastPlay = System.currentTimeMillis() - song.lastPlayedAt
        
        return if (timeSinceLastPlay < cooldownMs) {
            val hoursRemaining = (cooldownMs - timeSinceLastPlay) / (60 * 60 * 1000)
            Pair(true, "${hoursRemaining}h cooldown left")
        } else {
            Pair(false, "Cooldown expired")
        }
    }
    
    private fun shouldSkipSemiStrict(song: Song): Pair<Boolean, String> {
        return if (song.playCount > 0 && newSongsSinceLastRepeat < newSongsRequired) {
            Pair(true, "Discover ${newSongsRequired - newSongsSinceLastRepeat} more first")
        } else {
            if (song.playCount > 0) {
                newSongsSinceLastRepeat = 0
            }
            Pair(false, "Quota met")
        }
    }
    
    private fun shouldSkipArtistSmart(artist: String): Pair<Boolean, String> {
        val consecutiveCount = recentArtists.takeLastWhile { it.equals(artist, ignoreCase = true) }.size
        
        return if (consecutiveCount >= maxArtistRepeat) {
            Pair(true, "Too many $artist in a row")
        } else {
            Pair(false, "Artist variety maintained")
        }
    }
    
    private fun trackNewSong(artist: String) {
        recentArtists.add(artist)
        if (recentArtists.size > 10) {
            recentArtists.removeAt(0)
        }
    }
    
    /**
     * Optimized skip trigger with minimal overhead
     */
    private fun triggerSkip(platform: String) {
        val now = SystemClock.elapsedRealtime()
        val songId = currentSkipSongId
        
        // Debounce same song (1 second)
        if (songId == lastSkipSongId && now - lastSkipTime < 1000L) return
        
        lastSkipTime = now
        lastSkipSongId = songId
        
        // Quick skip - minimal delay
        mainHandler.postDelayed({
            performSkip(platform)
        }, 150)
    }
    
    /**
     * Optimized skip - try methods in order, no logging
     */
    private fun performSkip(platform: String) {
        // Try skip methods in order of reliability
        if (skipViaMediaKeyEvent()) return
        if (skipViaMediaSession(platform)) return
        if (skipViaAccessibilityButton(platform)) return
        if (skipViaGenericNextButton()) return
        performSwipeGesture()
    }
    
    /**
     * Skip via KEYCODE_MEDIA_NEXT - most reliable method!
     * This is the same signal that headphone buttons send
     */
    private fun skipViaMediaKeyEvent(): Boolean {
        try {
            val am = audioManager ?: return false
            
            // Send KEY_DOWN event
            val eventDown = KeyEvent(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_MEDIA_NEXT,
                0
            )
            am.dispatchMediaKeyEvent(eventDown)
            
            // Send KEY_UP event
            val eventUp = KeyEvent(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                KeyEvent.ACTION_UP,
                KeyEvent.KEYCODE_MEDIA_NEXT,
                0
            )
            am.dispatchMediaKeyEvent(eventUp)
            
            Log.d(TAG, "Dispatched KEYCODE_MEDIA_NEXT via AudioManager")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to dispatch media key event", e)
        }
        return false
    }
    
    /**
     * Skip using MediaSession transport controls - most reliable method
     */
    private fun skipViaMediaSession(platform: String): Boolean {
        try {
            val targetPackage = when (platform) {
                "spotify" -> "com.spotify.music"
                "youtube_music" -> "com.google.android.apps.youtube.music"
                "youtube" -> "com.google.android.youtube"
                else -> return false
            }
            
            Log.d(TAG, "Attempting MediaSession skip for $targetPackage")
            
            val controllers = mediaSessionManager?.getActiveSessions(
                ComponentName(this, MusicNotificationListenerService::class.java)
            )
            
            if (controllers.isNullOrEmpty()) {
                Log.w(TAG, "No active media sessions found")
                return false
            }
            
            Log.d(TAG, "Found ${controllers.size} active sessions: ${controllers.map { it.packageName }}")
            
            for (controller in controllers) {
                Log.d(TAG, "Checking controller: ${controller.packageName}")
                if (controller.packageName == targetPackage) {
                    val state = controller.playbackState
                    Log.d(TAG, "Playback state: ${state?.state}")
                    
                    // Be more lenient with playback states for YouTube
                    if (state != null && state.state != PlaybackState.STATE_NONE && 
                                          state.state != PlaybackState.STATE_STOPPED &&
                                          state.state != PlaybackState.STATE_ERROR) {
                        // Send skip to next command
                        controller.transportControls.skipToNext()
                        Log.d(TAG, "✅ Sent skipToNext via MediaController for $targetPackage")
                        return true
                    } else {
                        Log.w(TAG, "Playback state not suitable for skip: ${state?.state}")
                    }
                }
            }
            
            // Fallback: Try any music controller if exact match fails
            for (controller in controllers) {
                if (controller.packageName.contains("youtube") || controller.packageName.contains("spotify")) {
                    val state = controller.playbackState
                    if (state?.state == PlaybackState.STATE_PLAYING) {
                        controller.transportControls.skipToNext()
                        Log.d(TAG, "✅ Sent skipToNext via fallback controller: ${controller.packageName}")
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "MediaSession skip failed", e)
        }
        return false
    }
    
    /**
     * Skip by clicking known button IDs for each platform
     */
    private fun skipViaAccessibilityButton(platform: String): Boolean {
        val buttonIds = when (platform) {
            "spotify" -> SPOTIFY_NEXT_BUTTONS
            "youtube_music" -> YTM_NEXT_BUTTONS
            "youtube" -> YOUTUBE_NEXT_BUTTONS
            else -> return false
        }
        
        for (buttonId in buttonIds) {
            if (findAndClickButton(buttonId)) {
                return true
            }
        }
        return false
    }
    
    /**
     * Find any button with "next" or "skip" in its description
     */
    private fun skipViaGenericNextButton(): Boolean {
        try {
            val rootNode = rootInActiveWindow ?: run {
                Log.w(TAG, "rootInActiveWindow is null - accessibility may not be enabled")
                return false
            }
            
            Log.d(TAG, "Searching for next button using ${SKIP_KEYWORDS.size} keywords...")
            
            // Search for buttons with next/skip text or content description
            val result = findNodeByContentDescription(rootNode, SKIP_KEYWORDS)
            if (result != null) {
                Log.d(TAG, "Found potential next button: ${result.contentDescription ?: result.text}")
                val clicked = clickNode(result)
                result.recycle()
                rootNode.recycle()
                if (clicked) return true
            }
            
            // Fallback: Try finding ImageButton in player area (common for YouTube)
            Log.d(TAG, "Trying ImageButton class-based search...")
            val imageButtonResult = findNodeByClassName(rootNode, "android.widget.ImageButton", SKIP_KEYWORDS)
            if (imageButtonResult != null) {
                Log.d(TAG, "Found ImageButton: ${imageButtonResult.contentDescription}")
                val clicked = clickNode(imageButtonResult)
                imageButtonResult.recycle()
                rootNode.recycle()
                if (clicked) return true
            }
            
            rootNode.recycle()
        } catch (e: Exception) {
            Log.e(TAG, "Generic button search failed", e)
        }
        return false
    }
    
    /**
     * Find a node by class name that also matches keywords in content description
     */
    private fun findNodeByClassName(root: AccessibilityNodeInfo, className: String, keywords: List<String>): AccessibilityNodeInfo? {
        val queue = mutableListOf(root)
        
        while (queue.isNotEmpty()) {
            val node = queue.removeAt(0)
            
            if (node.className?.toString() == className) {
                val description = node.contentDescription?.toString()?.lowercase() ?: ""
                for (keyword in keywords) {
                    if (description.contains(keyword)) {
                        if (node.isClickable || node.isEnabled) {
                            return node
                        }
                    }
                }
            }
            
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.add(it) }
            }
        }
        return null
    }
    
    /**
     * Find a node by content description containing any of the keywords
     */
    private fun findNodeByContentDescription(root: AccessibilityNodeInfo, keywords: List<String>): AccessibilityNodeInfo? {
        val queue = mutableListOf(root)
        
        while (queue.isNotEmpty()) {
            val node = queue.removeAt(0)
            
            val description = node.contentDescription?.toString()?.lowercase() ?: ""
            val text = node.text?.toString()?.lowercase() ?: ""
            
            for (keyword in keywords) {
                if (description.contains(keyword) || text.contains(keyword)) {
                    if (node.isClickable || node.isEnabled) {
                        return node
                    }
                }
            }
            
            // Add children to queue
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.add(it) }
            }
        }
        
        return null
    }
    
    /**
     * Click a node, trying parent if node isn't directly clickable
     */
    private fun clickNode(node: AccessibilityNodeInfo): Boolean {
        // Try clicking the node directly
        if (node.isClickable) {
            val result = node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            Log.d(TAG, "Clicked node directly: $result")
            return result
        }
        
        // Try clicking parent
        var parent = node.parent
        var depth = 0
        while (parent != null && depth < 5) {
            if (parent.isClickable) {
                val result = parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                parent.recycle()
                Log.d(TAG, "Clicked parent node at depth $depth: $result")
                return result
            }
            val oldParent = parent
            parent = parent.parent
            oldParent.recycle()
            depth++
        }
        parent?.recycle()
        
        return false
    }
    
    private fun findAndClickButton(resourceId: String): Boolean {
        try {
            val rootNode = rootInActiveWindow ?: run {
                Log.w(TAG, "rootInActiveWindow is null when looking for $resourceId")
                return false
            }
            
            val nodes = rootNode.findAccessibilityNodeInfosByViewId(resourceId)
            if (nodes.isNotEmpty()) {
                val node = nodes[0]
                val clicked = clickNode(node)
                if (clicked) {
                    Log.d(TAG, "Clicked next button: $resourceId")
                }
                nodes.forEach { it.recycle() }
                rootNode.recycle()
                return clicked
            } else {
                Log.d(TAG, "Button not found: $resourceId")
            }
            
            rootNode.recycle()
        } catch (e: Exception) {
            Log.e(TAG, "Error clicking button $resourceId", e)
        }
        return false
    }
    
    private fun performSwipeGesture() {
        try {
            val displayMetrics = resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val screenHeight = displayMetrics.heightPixels
            
            // Swipe from right to left (skip to next gesture)
            val path = Path().apply {
                moveTo(screenWidth * 0.8f, screenHeight * 0.5f)
                lineTo(screenWidth * 0.2f, screenHeight * 0.5f)
            }
            
            val gesture = GestureDescription.Builder()
                .addStroke(GestureDescription.StrokeDescription(path, 0, 150))
                .build()
            
            dispatchGesture(gesture, object : GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription?) {
                    Log.d(TAG, "Swipe gesture completed successfully")
                }
                
                override fun onCancelled(gestureDescription: GestureDescription?) {
                    Log.w(TAG, "Swipe gesture was cancelled")
                }
            }, null)
            
            Log.d(TAG, "Performed swipe gesture to skip")
        } catch (e: Exception) {
            Log.e(TAG, "Error performing gesture", e)
        }
    }
    
    private fun broadcastSkipEvent(reason: String) {
        val intent = Intent("com.unloop.app.SONG_SKIPPED").apply {
            putExtra("reason", reason)
            setPackage(packageName)
        }
        sendBroadcast(intent)
        Log.d(TAG, "Broadcast SONG_SKIPPED event with reason: $reason")
        
        // Log skip to history database
        logSkipToHistory(reason)
    }
    
    private fun logSkipToHistory(reason: String) {
        val songId = currentSkipSongId ?: return
        val title = currentSkipSongTitle ?: return
        val artist = currentSkipSongArtist ?: return
        val platform = currentSkipPlatform ?: return
        
        serviceScope.launch {
            try {
                val entry = SkipHistoryEntry(
                    songId = songId,
                    songTitle = title,
                    songArtist = artist,
                    platform = platform,
                    reason = reason
                )
                database.songDao().insertSkipHistory(entry)
                Log.d(TAG, "Logged skip to history: $title - $reason")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to log skip to history", e)
            }
        }
    }
}
