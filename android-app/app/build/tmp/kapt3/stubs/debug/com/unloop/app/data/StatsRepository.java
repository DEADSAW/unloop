package com.unloop.app.data;

/**
 * Repository for comprehensive listening statistics
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u0000 A2\u00020\u0001:\u0001AB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\u000fH\u0002J\u0018\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u000fH\u0002J\u001e\u0010\u0018\u001a\u00020\u00132\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001a2\u0006\u0010\u0015\u001a\u00020\u000fH\u0002J\u0006\u0010\u001c\u001a\u00020\u001dJ\u001a\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020 0\u001a0\u001f2\u0006\u0010!\u001a\u00020\u000fJ\u001c\u0010\"\u001a\b\u0012\u0004\u0012\u00020 0\u001a2\u0006\u0010!\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010#J\u0010\u0010$\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\u0006H\u0002J\u0010\u0010&\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\u0006H\u0002J\u0010\u0010\'\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\u0006H\u0002J\u000e\u0010(\u001a\u00020)H\u0086@\u00a2\u0006\u0002\u0010*J\b\u0010+\u001a\u00020,H\u0002J\u0006\u0010-\u001a\u00020.J.\u0010/\u001a\u00020\u001d2\u0006\u00100\u001a\u00020,2\u0006\u00101\u001a\u00020,2\u0006\u00102\u001a\u00020,2\u0006\u00103\u001a\u00020,H\u0086@\u00a2\u0006\u0002\u00104J\u001e\u00105\u001a\u00020\u001d2\u0006\u00100\u001a\u00020,2\u0006\u00106\u001a\u00020\u0006H\u0086@\u00a2\u0006\u0002\u00107J\u0016\u00108\u001a\u00020\u001d2\u0006\u00100\u001a\u00020,H\u0086@\u00a2\u0006\u0002\u00109J\u0006\u0010:\u001a\u00020\u001dJ\b\u0010;\u001a\u00020\u001dH\u0002J&\u0010<\u001a\u00020\u001d2\u0006\u00103\u001a\u00020,2\u0006\u0010=\u001a\u00020\u00062\u0006\u0010>\u001a\u00020?H\u0082@\u00a2\u0006\u0002\u0010@R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006B"}, d2 = {"Lcom/unloop/app/data/StatsRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "WIDGET_UPDATE_DEBOUNCE_MS", "", "dao", "Lcom/unloop/app/data/SongDao;", "database", "Lcom/unloop/app/data/UnloopDatabase;", "lastActivityTime", "lastWidgetUpdateTime", "sessionListenTimeMs", "sessionLoopsAvoided", "", "sessionSongsDiscovered", "sessionStartTime", "calculateFreshnessScore", "", "newSongsThisWeek", "totalSongs", "calculateIntelligenceScore", "loopsAvoided", "calculateVarietyScore", "topArtists", "", "Lcom/unloop/app/data/ArtistStats;", "checkSessionTimeout", "", "getDailyStatsFlow", "Lkotlinx/coroutines/flow/Flow;", "Lcom/unloop/app/data/DailyStats;", "days", "getDailyStatsForChart", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getStartOfDay", "timestamp", "getStartOfMonth", "getStartOfWeek", "getStats", "Lcom/unloop/app/data/ListeningStats;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTodayString", "", "getWidgetStats", "Lcom/unloop/app/data/WidgetStats;", "incrementPlayCount", "songId", "title", "artist", "platform", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "recordListenTime", "durationMs", "(Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "recordLoopAvoided", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "resetSession", "triggerWidgetUpdate", "updateDailyStats", "listenTimeMs", "isNewSong", "", "(Ljava/lang/String;JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class StatsRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.unloop.app.data.StatsRepository instance;
    @org.jetbrains.annotations.NotNull()
    private final com.unloop.app.data.UnloopDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final com.unloop.app.data.SongDao dao = null;
    private long sessionStartTime;
    private int sessionSongsDiscovered = 0;
    private int sessionLoopsAvoided = 0;
    private long sessionListenTimeMs = 0L;
    private long lastWidgetUpdateTime = 0L;
    private final long WIDGET_UPDATE_DEBOUNCE_MS = 2000L;
    private long lastActivityTime;
    @org.jetbrains.annotations.NotNull()
    public static final com.unloop.app.data.StatsRepository.Companion Companion = null;
    
    private StatsRepository(android.content.Context context) {
        super();
    }
    
    /**
     * Get comprehensive listening statistics
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getStats(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.unloop.app.data.ListeningStats> $completion) {
        return null;
    }
    
    /**
     * Get daily stats for charts
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getDailyStatsForChart(int days, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.unloop.app.data.DailyStats>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.unloop.app.data.DailyStats>> getDailyStatsFlow(int days) {
        return null;
    }
    
    /**
     * Check if session shouldnbe reset (10 mins inactivity)
     */
    public final void checkSessionTimeout() {
    }
    
    /**
     * Increment play count for a song (Start of play)
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object incrementPlayCount(@org.jetbrains.annotations.NotNull()
    java.lang.String songId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String artist, @org.jetbrains.annotations.NotNull()
    java.lang.String platform, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Record listening time for a song (End of play)
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object recordListenTime(@org.jetbrains.annotations.NotNull()
    java.lang.String songId, long durationMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Record a loop avoided (song was auto-skipped)
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object recordLoopAvoided(@org.jetbrains.annotations.NotNull()
    java.lang.String songId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Reset session stats
     */
    public final void resetSession() {
    }
    
    private final java.lang.Object updateDailyStats(java.lang.String platform, long listenTimeMs, boolean isNewSong, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final float calculateFreshnessScore(int newSongsThisWeek, int totalSongs) {
        return 0.0F;
    }
    
    private final float calculateVarietyScore(java.util.List<com.unloop.app.data.ArtistStats> topArtists, int totalSongs) {
        return 0.0F;
    }
    
    private final float calculateIntelligenceScore(int totalSongs, int loopsAvoided) {
        return 0.0F;
    }
    
    private final java.lang.String getTodayString() {
        return null;
    }
    
    private final long getStartOfDay(long timestamp) {
        return 0L;
    }
    
    private final long getStartOfWeek(long timestamp) {
        return 0L;
    }
    
    private final long getStartOfMonth(long timestamp) {
        return 0L;
    }
    
    private final void triggerWidgetUpdate() {
    }
    
    /**
     * Lightweight stats for widget - only essential data, minimal queries
     */
    @org.jetbrains.annotations.NotNull()
    public final com.unloop.app.data.WidgetStats getWidgetStats() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/unloop/app/data/StatsRepository$Companion;", "", "()V", "instance", "Lcom/unloop/app/data/StatsRepository;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.unloop.app.data.StatsRepository getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}