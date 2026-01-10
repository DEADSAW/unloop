package com.unloop.app.data;

/**
 * Repository for comprehensive listening statistics
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\fH\u0002J\u0018\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\fH\u0002J\u001e\u0010\u0015\u001a\u00020\u00102\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0012\u001a\u00020\fH\u0002J\u001a\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u00170\u001a2\u0006\u0010\u001c\u001a\u00020\fJ\u001c\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001b0\u00172\u0006\u0010\u001c\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u001eJ\u0010\u0010\u001f\u001a\u00020\n2\u0006\u0010 \u001a\u00020\nH\u0002J\u0010\u0010!\u001a\u00020\n2\u0006\u0010 \u001a\u00020\nH\u0002J\u0010\u0010\"\u001a\u00020\n2\u0006\u0010 \u001a\u00020\nH\u0002J\u000e\u0010#\u001a\u00020$H\u0086@\u00a2\u0006\u0002\u0010%J\b\u0010&\u001a\u00020\'H\u0002J\u0016\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\'H\u0086@\u00a2\u0006\u0002\u0010+J6\u0010,\u001a\u00020)2\u0006\u0010*\u001a\u00020\'2\u0006\u0010-\u001a\u00020\'2\u0006\u0010.\u001a\u00020\'2\u0006\u0010/\u001a\u00020\'2\u0006\u00100\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u00101J\u0006\u00102\u001a\u00020)J&\u00103\u001a\u00020)2\u0006\u0010/\u001a\u00020\'2\u0006\u00100\u001a\u00020\n2\u0006\u00104\u001a\u000205H\u0082@\u00a2\u0006\u0002\u00106R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00067"}, d2 = {"Lcom/unloop/app/data/StatsRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "dao", "Lcom/unloop/app/data/SongDao;", "database", "Lcom/unloop/app/data/UnloopDatabase;", "sessionListenTimeMs", "", "sessionLoopsAvoided", "", "sessionSongsDiscovered", "sessionStartTime", "calculateFreshnessScore", "", "newSongsThisWeek", "totalSongs", "calculateIntelligenceScore", "loopsAvoided", "calculateVarietyScore", "topArtists", "", "Lcom/unloop/app/data/ArtistStats;", "getDailyStatsFlow", "Lkotlinx/coroutines/flow/Flow;", "Lcom/unloop/app/data/DailyStats;", "days", "getDailyStatsForChart", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getStartOfDay", "timestamp", "getStartOfMonth", "getStartOfWeek", "getStats", "Lcom/unloop/app/data/ListeningStats;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTodayString", "", "recordLoopAvoided", "", "songId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "recordSongPlay", "title", "artist", "platform", "listenTimeMs", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "resetSession", "updateDailyStats", "isNewSong", "", "(Ljava/lang/String;JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class StatsRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.unloop.app.data.UnloopDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final com.unloop.app.data.SongDao dao = null;
    private long sessionStartTime;
    private int sessionSongsDiscovered = 0;
    private int sessionLoopsAvoided = 0;
    private long sessionListenTimeMs = 0L;
    
    public StatsRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
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
     * Record a new song play
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object recordSongPlay(@org.jetbrains.annotations.NotNull()
    java.lang.String songId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String artist, @org.jetbrains.annotations.NotNull()
    java.lang.String platform, long listenTimeMs, @org.jetbrains.annotations.NotNull()
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
}