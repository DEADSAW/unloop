package com.unloop.app.service;

/**
 * Service to handle accessibility events for skipping songs
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0090\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u0000 W2\u00020\u0001:\u0001WB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020\bH\u0002J.\u0010,\u001a\u00020*2\u0006\u0010-\u001a\u00020\b2\u0006\u0010.\u001a\u00020\b2\u0006\u0010/\u001a\u00020\b2\u0006\u00100\u001a\u00020\bH\u0082@\u00a2\u0006\u0002\u00101J\u0010\u00102\u001a\u0002032\u0006\u00104\u001a\u000205H\u0002J\u0010\u00106\u001a\u0002032\u0006\u00107\u001a\u00020\bH\u0002J(\u00108\u001a\u0004\u0018\u0001052\u0006\u00109\u001a\u0002052\u0006\u0010:\u001a\u00020\b2\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\b0<H\u0002J \u0010=\u001a\u0004\u0018\u0001052\u0006\u00109\u001a\u0002052\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\b0<H\u0002J\u0010\u0010>\u001a\u00020*2\u0006\u0010+\u001a\u00020\bH\u0002J\u0012\u0010?\u001a\u00020*2\b\u0010@\u001a\u0004\u0018\u00010AH\u0016J\b\u0010B\u001a\u00020*H\u0016J\b\u0010C\u001a\u00020*H\u0016J\b\u0010D\u001a\u00020*H\u0016J\b\u0010E\u001a\u00020*H\u0014J\u0010\u0010F\u001a\u00020*2\u0006\u00100\u001a\u00020\bH\u0002J\b\u0010G\u001a\u00020*H\u0002J\u001c\u0010H\u001a\u000e\u0012\u0004\u0012\u000203\u0012\u0004\u0012\u00020\b0I2\u0006\u0010/\u001a\u00020\bH\u0002J\u001c\u0010J\u001a\u000e\u0012\u0004\u0012\u000203\u0012\u0004\u0012\u00020\b0I2\u0006\u0010K\u001a\u00020LH\u0002J\u001c\u0010M\u001a\u000e\u0012\u0004\u0012\u000203\u0012\u0004\u0012\u00020\b0I2\u0006\u0010K\u001a\u00020LH\u0002J*\u0010N\u001a\u000e\u0012\u0004\u0012\u000203\u0012\u0004\u0012\u00020\b0I2\u0006\u0010K\u001a\u00020L2\u0006\u0010/\u001a\u00020\bH\u0082@\u00a2\u0006\u0002\u0010OJ\u001c\u0010P\u001a\u000e\u0012\u0004\u0012\u000203\u0012\u0004\u0012\u00020\b0I2\u0006\u0010K\u001a\u00020LH\u0002J\u0010\u0010Q\u001a\u0002032\u0006\u00100\u001a\u00020\bH\u0002J\b\u0010R\u001a\u000203H\u0002J\b\u0010S\u001a\u000203H\u0002J\u0010\u0010T\u001a\u0002032\u0006\u00100\u001a\u00020\bH\u0002J\u0010\u0010U\u001a\u00020*2\u0006\u0010/\u001a\u00020\bH\u0002J\u0010\u0010V\u001a\u00020*2\u0006\u00100\u001a\u00020\bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\f\u001a\u00020\r8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u000e\u0010\u000fR\u0010\u0010\u0012\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\b0\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010$\u001a\u00020%8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b(\u0010\u0011\u001a\u0004\b&\u0010\'\u00a8\u0006X"}, d2 = {"Lcom/unloop/app/service/UnloopAccessibilityService;", "Landroid/accessibilityservice/AccessibilityService;", "()V", "SKIP_DEBOUNCE_MS", "", "audioManager", "Landroid/media/AudioManager;", "currentSkipPlatform", "", "currentSkipSongArtist", "currentSkipSongId", "currentSkipSongTitle", "database", "Lcom/unloop/app/data/UnloopDatabase;", "getDatabase", "()Lcom/unloop/app/data/UnloopDatabase;", "database$delegate", "Lkotlin/Lazy;", "lastProcessedSongId", "lastSkipSongId", "lastSkipTime", "lastSongStartTime", "mainHandler", "Landroid/os/Handler;", "mediaSessionManager", "Landroid/media/session/MediaSessionManager;", "newSongsSinceLastRepeat", "", "prefs", "Lcom/unloop/app/data/PreferencesManager;", "recentArtists", "", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "songReceiver", "Landroid/content/BroadcastReceiver;", "statsRepository", "Lcom/unloop/app/data/StatsRepository;", "getStatsRepository", "()Lcom/unloop/app/data/StatsRepository;", "statsRepository$delegate", "broadcastSkipEvent", "", "reason", "checkAndSkipIfNeeded", "songId", "title", "artist", "platform", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clickNode", "", "node", "Landroid/view/accessibility/AccessibilityNodeInfo;", "findAndClickButton", "resourceId", "findNodeByClassName", "root", "className", "keywords", "", "findNodeByContentDescription", "logSkipToHistory", "onAccessibilityEvent", "event", "Landroid/view/accessibility/AccessibilityEvent;", "onCreate", "onDestroy", "onInterrupt", "onServiceConnected", "performSkip", "performSwipeGesture", "shouldSkipArtistSmart", "Lkotlin/Pair;", "shouldSkipMemoryFade", "song", "Lcom/unloop/app/data/Song;", "shouldSkipSemiStrict", "shouldSkipSmartAuto", "(Lcom/unloop/app/data/Song;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "shouldSkipStrict", "skipViaAccessibilityButton", "skipViaGenericNextButton", "skipViaMediaKeyEvent", "skipViaMediaSession", "trackNewSong", "triggerSkip", "Companion", "app_debug"})
public final class UnloopAccessibilityService extends android.accessibilityservice.AccessibilityService {
    private com.unloop.app.data.PreferencesManager prefs;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "UnloopAccessibility";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_SONG_SKIPPED = "com.unloop.app.SONG_SKIPPED";
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> SPOTIFY_NEXT_BUTTONS = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> YTM_NEXT_BUTTONS = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> YOUTUBE_NEXT_BUTTONS = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> SKIP_KEYWORDS = null;
    private static boolean isEnabled = true;
    @org.jetbrains.annotations.NotNull()
    private static com.unloop.app.data.DiscoveryMode currentMode = com.unloop.app.data.DiscoveryMode.SMART_AUTO;
    private static int memoryFadeHours = 168;
    private static int newSongsRequired = 10;
    private static int maxArtistRepeat = 3;
    @org.jetbrains.annotations.Nullable()
    private static com.unloop.app.service.UnloopAccessibilityService instance;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler mainHandler = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy database$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy statsRepository$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String lastProcessedSongId;
    private long lastSongStartTime = 0L;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<java.lang.String> recentArtists;
    private int newSongsSinceLastRepeat = 0;
    @org.jetbrains.annotations.Nullable()
    private android.media.session.MediaSessionManager mediaSessionManager;
    @org.jetbrains.annotations.Nullable()
    private android.media.AudioManager audioManager;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String currentSkipSongId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String currentSkipSongTitle;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String currentSkipSongArtist;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String currentSkipPlatform;
    private long lastSkipTime = 0L;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String lastSkipSongId;
    private final long SKIP_DEBOUNCE_MS = 2000L;
    @org.jetbrains.annotations.NotNull()
    private final android.content.BroadcastReceiver songReceiver = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.unloop.app.service.UnloopAccessibilityService.Companion Companion = null;
    
    public UnloopAccessibilityService() {
        super();
    }
    
    private final com.unloop.app.data.UnloopDatabase getDatabase() {
        return null;
    }
    
    private final com.unloop.app.data.StatsRepository getStatsRepository() {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    protected void onServiceConnected() {
    }
    
    @java.lang.Override()
    public void onAccessibilityEvent(@org.jetbrains.annotations.Nullable()
    android.view.accessibility.AccessibilityEvent event) {
    }
    
    @java.lang.Override()
    public void onInterrupt() {
    }
    
    private final java.lang.Object checkAndSkipIfNeeded(java.lang.String songId, java.lang.String title, java.lang.String artist, java.lang.String platform, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object shouldSkipSmartAuto(com.unloop.app.data.Song song, java.lang.String artist, kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Boolean, java.lang.String>> $completion) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Boolean, java.lang.String> shouldSkipStrict(com.unloop.app.data.Song song) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Boolean, java.lang.String> shouldSkipMemoryFade(com.unloop.app.data.Song song) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Boolean, java.lang.String> shouldSkipSemiStrict(com.unloop.app.data.Song song) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Boolean, java.lang.String> shouldSkipArtistSmart(java.lang.String artist) {
        return null;
    }
    
    private final void trackNewSong(java.lang.String artist) {
    }
    
    /**
     * Optimized skip trigger with minimal overhead
     */
    private final void triggerSkip(java.lang.String platform) {
    }
    
    /**
     * Optimized skip - try methods in order, no logging
     */
    private final void performSkip(java.lang.String platform) {
    }
    
    /**
     * Skip via KEYCODE_MEDIA_NEXT - most reliable method!
     * This is the same signal that headphone buttons send
     */
    private final boolean skipViaMediaKeyEvent() {
        return false;
    }
    
    /**
     * Skip using MediaSession transport controls - most reliable method
     */
    private final boolean skipViaMediaSession(java.lang.String platform) {
        return false;
    }
    
    /**
     * Skip by clicking known button IDs for each platform
     */
    private final boolean skipViaAccessibilityButton(java.lang.String platform) {
        return false;
    }
    
    /**
     * Find any button with "next" or "skip" in its description
     */
    private final boolean skipViaGenericNextButton() {
        return false;
    }
    
    /**
     * Find a node by class name that also matches keywords in content description
     */
    private final android.view.accessibility.AccessibilityNodeInfo findNodeByClassName(android.view.accessibility.AccessibilityNodeInfo root, java.lang.String className, java.util.List<java.lang.String> keywords) {
        return null;
    }
    
    /**
     * Find a node by content description containing any of the keywords
     */
    private final android.view.accessibility.AccessibilityNodeInfo findNodeByContentDescription(android.view.accessibility.AccessibilityNodeInfo root, java.util.List<java.lang.String> keywords) {
        return null;
    }
    
    /**
     * Click a node, trying parent if node isn't directly clickable
     */
    private final boolean clickNode(android.view.accessibility.AccessibilityNodeInfo node) {
        return false;
    }
    
    private final boolean findAndClickButton(java.lang.String resourceId) {
        return false;
    }
    
    private final void performSwipeGesture() {
    }
    
    private final void broadcastSkipEvent(java.lang.String reason) {
    }
    
    private final void logSkipToHistory(java.lang.String reason) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\f\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010$\u001a\u0004\u0018\u00010\u0012R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u001a\u0010\u001e\u001a\u00020\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u001b\"\u0004\b \u0010\u001dR\u001a\u0010!\u001a\u00020\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u001b\"\u0004\b#\u0010\u001d\u00a8\u0006%"}, d2 = {"Lcom/unloop/app/service/UnloopAccessibilityService$Companion;", "", "()V", "ACTION_SONG_SKIPPED", "", "SKIP_KEYWORDS", "", "SPOTIFY_NEXT_BUTTONS", "TAG", "YOUTUBE_NEXT_BUTTONS", "YTM_NEXT_BUTTONS", "currentMode", "Lcom/unloop/app/data/DiscoveryMode;", "getCurrentMode", "()Lcom/unloop/app/data/DiscoveryMode;", "setCurrentMode", "(Lcom/unloop/app/data/DiscoveryMode;)V", "instance", "Lcom/unloop/app/service/UnloopAccessibilityService;", "isEnabled", "", "()Z", "setEnabled", "(Z)V", "maxArtistRepeat", "", "getMaxArtistRepeat", "()I", "setMaxArtistRepeat", "(I)V", "memoryFadeHours", "getMemoryFadeHours", "setMemoryFadeHours", "newSongsRequired", "getNewSongsRequired", "setNewSongsRequired", "getInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final boolean isEnabled() {
            return false;
        }
        
        public final void setEnabled(boolean p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.unloop.app.data.DiscoveryMode getCurrentMode() {
            return null;
        }
        
        public final void setCurrentMode(@org.jetbrains.annotations.NotNull()
        com.unloop.app.data.DiscoveryMode p0) {
        }
        
        public final int getMemoryFadeHours() {
            return 0;
        }
        
        public final void setMemoryFadeHours(int p0) {
        }
        
        public final int getNewSongsRequired() {
            return 0;
        }
        
        public final void setNewSongsRequired(int p0) {
        }
        
        public final int getMaxArtistRepeat() {
            return 0;
        }
        
        public final void setMaxArtistRepeat(int p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.unloop.app.service.UnloopAccessibilityService getInstance() {
            return null;
        }
    }
}