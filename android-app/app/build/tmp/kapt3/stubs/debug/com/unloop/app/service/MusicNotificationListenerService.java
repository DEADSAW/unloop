package com.unloop.app.service;

/**
 * OPTIMIZED Service for music notification detection
 * - Minimal logging
 * - Better song ID (no hash collisions)
 * - Debounced processing
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0004H\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\u0017H\u0016J\b\u0010\u0019\u001a\u00020\u0017H\u0016J\b\u0010\u001a\u001a\u00020\u0017H\u0016J\u0010\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u001dH\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\u000e\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u001f"}, d2 = {"Lcom/unloop/app/service/MusicNotificationListenerService;", "Landroid/service/notification/NotificationListenerService;", "()V", "currentSongId", "", "lastBroadcastTime", "", "serviceJob", "Lkotlinx/coroutines/Job;", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "getServiceScope", "()Lkotlinx/coroutines/CoroutineScope;", "serviceScope$delegate", "Lkotlin/Lazy;", "statsRepository", "Lcom/unloop/app/data/StatsRepository;", "getStatsRepository", "()Lcom/unloop/app/data/StatsRepository;", "statsRepository$delegate", "getPlatformName", "packageName", "onCreate", "", "onDestroy", "onListenerConnected", "onListenerDisconnected", "onNotificationPosted", "sbn", "Landroid/service/notification/StatusBarNotification;", "Companion", "app_debug"})
public final class MusicNotificationListenerService extends android.service.notification.NotificationListenerService {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "MusicNotif";
    @org.jetbrains.annotations.NotNull()
    private static final java.util.HashSet<java.lang.String> MUSIC_PACKAGES = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_SONG_DETECTED = "com.unloop.app.SONG_DETECTED";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_SONG_ID = "song_id";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_SONG_TITLE = "song_title";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_SONG_ARTIST = "song_artist";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_PLATFORM = "platform";
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job serviceJob;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy serviceScope$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String currentSongId;
    private long lastBroadcastTime = 0L;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy statsRepository$delegate = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.unloop.app.service.MusicNotificationListenerService.Companion Companion = null;
    
    public MusicNotificationListenerService() {
        super();
    }
    
    private final kotlinx.coroutines.CoroutineScope getServiceScope() {
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
    public void onNotificationPosted(@org.jetbrains.annotations.NotNull()
    android.service.notification.StatusBarNotification sbn) {
    }
    
    private final java.lang.String getPlatformName(java.lang.String packageName) {
        return null;
    }
    
    @java.lang.Override()
    public void onListenerConnected() {
    }
    
    @java.lang.Override()
    public void onListenerDisconnected() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/unloop/app/service/MusicNotificationListenerService$Companion;", "", "()V", "ACTION_SONG_DETECTED", "", "EXTRA_PLATFORM", "EXTRA_SONG_ARTIST", "EXTRA_SONG_ID", "EXTRA_SONG_TITLE", "MUSIC_PACKAGES", "Ljava/util/HashSet;", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}