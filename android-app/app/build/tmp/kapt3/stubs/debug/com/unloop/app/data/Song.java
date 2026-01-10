package com.unloop.app.data;

/**
 * Represents a song in the listening history with comprehensive analytics
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\bE\b\u0087\b\u0018\u00002\u00020\u0001B\u00b1\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\b\b\u0002\u0010\n\u001a\u00020\b\u0012\b\b\u0002\u0010\u000b\u001a\u00020\b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\r\u0012\b\b\u0002\u0010\u000f\u001a\u00020\r\u0012\b\b\u0002\u0010\u0010\u001a\u00020\b\u0012\b\b\u0002\u0010\u0011\u001a\u00020\b\u0012\b\b\u0002\u0010\u0012\u001a\u00020\r\u0012\b\b\u0002\u0010\u0013\u001a\u00020\r\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\u0002\u0010\u0019J\u0006\u0010A\u001a\u00020\u0018J\u0006\u0010B\u001a\u00020\u0018J\u0006\u0010C\u001a\u00020\u0018J\t\u0010D\u001a\u00020\u0003H\u00c6\u0003J\t\u0010E\u001a\u00020\rH\u00c6\u0003J\t\u0010F\u001a\u00020\rH\u00c6\u0003J\t\u0010G\u001a\u00020\bH\u00c6\u0003J\t\u0010H\u001a\u00020\bH\u00c6\u0003J\t\u0010I\u001a\u00020\rH\u00c6\u0003J\t\u0010J\u001a\u00020\rH\u00c6\u0003J\t\u0010K\u001a\u00020\u0015H\u00c6\u0003J\t\u0010L\u001a\u00020\u0015H\u00c6\u0003J\t\u0010M\u001a\u00020\u0018H\u00c6\u0003J\t\u0010N\u001a\u00020\u0003H\u00c6\u0003J\t\u0010O\u001a\u00020\u0003H\u00c6\u0003J\t\u0010P\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Q\u001a\u00020\bH\u00c6\u0003J\t\u0010R\u001a\u00020\bH\u00c6\u0003J\t\u0010S\u001a\u00020\bH\u00c6\u0003J\t\u0010T\u001a\u00020\bH\u00c6\u0003J\t\u0010U\u001a\u00020\rH\u00c6\u0003J\u00bd\u0001\u0010V\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\b2\b\b\u0002\u0010\u000b\u001a\u00020\b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\r2\b\b\u0002\u0010\u0010\u001a\u00020\b2\b\b\u0002\u0010\u0011\u001a\u00020\b2\b\b\u0002\u0010\u0012\u001a\u00020\r2\b\b\u0002\u0010\u0013\u001a\u00020\r2\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u00152\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u00c6\u0001J\u0013\u0010W\u001a\u00020\u00152\b\u0010X\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010Y\u001a\u00020\bH\u00d6\u0001J\u000e\u0010Z\u001a\u00020\u00152\u0006\u0010[\u001a\u00020\u0018J\t\u0010\\\u001a\u00020\u0003H\u00d6\u0001R\u001a\u0010\u0017\u001a\u00020\u0018X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u001a\u0010\f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001fR\u001a\u0010\u0016\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010%\"\u0004\b&\u0010\'R\u001a\u0010\u0014\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010%\"\u0004\b(\u0010\'R\u001a\u0010\u000e\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010!\"\u0004\b*\u0010#R\u001a\u0010\u000b\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\u001fR\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b0\u0010,\"\u0004\b1\u0010.R\u001a\u0010\n\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b2\u0010,\"\u0004\b3\u0010.R\u001a\u0010\t\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b4\u0010,\"\u0004\b5\u0010.R\u001a\u0010\u0013\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b6\u0010!\"\u0004\b7\u0010#R\u001a\u0010\u0011\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b8\u0010,\"\u0004\b9\u0010.R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010\u001fR\u001a\u0010\u000f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b;\u0010!\"\u0004\b<\u0010#R\u001a\u0010\u0012\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b=\u0010!\"\u0004\b>\u0010#R\u001a\u0010\u0010\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b?\u0010,\"\u0004\b@\u0010.\u00a8\u0006]"}, d2 = {"Lcom/unloop/app/data/Song;", "", "id", "", "title", "artist", "platform", "playCount", "", "skipCount", "quickSkipCount", "loopsAvoided", "firstPlayedAt", "", "lastPlayedAt", "totalListenTimeMs", "youtubePlayCount", "spotifyPlayCount", "youtubeListenTimeMs", "spotifyListenTimeMs", "isWhitelisted", "", "isBlacklisted", "affectionScore", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IIIIJJJIIJJZZF)V", "getAffectionScore", "()F", "setAffectionScore", "(F)V", "getArtist", "()Ljava/lang/String;", "getFirstPlayedAt", "()J", "setFirstPlayedAt", "(J)V", "getId", "()Z", "setBlacklisted", "(Z)V", "setWhitelisted", "getLastPlayedAt", "setLastPlayedAt", "getLoopsAvoided", "()I", "setLoopsAvoided", "(I)V", "getPlatform", "getPlayCount", "setPlayCount", "getQuickSkipCount", "setQuickSkipCount", "getSkipCount", "setSkipCount", "getSpotifyListenTimeMs", "setSpotifyListenTimeMs", "getSpotifyPlayCount", "setSpotifyPlayCount", "getTitle", "getTotalListenTimeMs", "setTotalListenTimeMs", "getYoutubeListenTimeMs", "setYoutubeListenTimeMs", "getYoutubePlayCount", "setYoutubePlayCount", "avgListenDuration", "calculateAffectionScore", "calculateSmartCooldownHours", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "isOnCooldown", "cooldownHours", "toString", "app_debug"})
@androidx.room.Entity(tableName = "songs", indices = {@androidx.room.Index(value = {"artist"}), @androidx.room.Index(value = {"lastPlayedAt"}), @androidx.room.Index(value = {"platform"})})
public final class Song {
    @androidx.room.PrimaryKey()
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String artist = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String platform = null;
    private int playCount;
    private int skipCount;
    private int quickSkipCount;
    private int loopsAvoided;
    private long firstPlayedAt;
    private long lastPlayedAt;
    private long totalListenTimeMs;
    private int youtubePlayCount;
    private int spotifyPlayCount;
    private long youtubeListenTimeMs;
    private long spotifyListenTimeMs;
    private boolean isWhitelisted;
    private boolean isBlacklisted;
    private float affectionScore;
    
    public Song(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String artist, @org.jetbrains.annotations.NotNull()
    java.lang.String platform, int playCount, int skipCount, int quickSkipCount, int loopsAvoided, long firstPlayedAt, long lastPlayedAt, long totalListenTimeMs, int youtubePlayCount, int spotifyPlayCount, long youtubeListenTimeMs, long spotifyListenTimeMs, boolean isWhitelisted, boolean isBlacklisted, float affectionScore) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getArtist() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPlatform() {
        return null;
    }
    
    public final int getPlayCount() {
        return 0;
    }
    
    public final void setPlayCount(int p0) {
    }
    
    public final int getSkipCount() {
        return 0;
    }
    
    public final void setSkipCount(int p0) {
    }
    
    public final int getQuickSkipCount() {
        return 0;
    }
    
    public final void setQuickSkipCount(int p0) {
    }
    
    public final int getLoopsAvoided() {
        return 0;
    }
    
    public final void setLoopsAvoided(int p0) {
    }
    
    public final long getFirstPlayedAt() {
        return 0L;
    }
    
    public final void setFirstPlayedAt(long p0) {
    }
    
    public final long getLastPlayedAt() {
        return 0L;
    }
    
    public final void setLastPlayedAt(long p0) {
    }
    
    public final long getTotalListenTimeMs() {
        return 0L;
    }
    
    public final void setTotalListenTimeMs(long p0) {
    }
    
    public final int getYoutubePlayCount() {
        return 0;
    }
    
    public final void setYoutubePlayCount(int p0) {
    }
    
    public final int getSpotifyPlayCount() {
        return 0;
    }
    
    public final void setSpotifyPlayCount(int p0) {
    }
    
    public final long getYoutubeListenTimeMs() {
        return 0L;
    }
    
    public final void setYoutubeListenTimeMs(long p0) {
    }
    
    public final long getSpotifyListenTimeMs() {
        return 0L;
    }
    
    public final void setSpotifyListenTimeMs(long p0) {
    }
    
    public final boolean isWhitelisted() {
        return false;
    }
    
    public final void setWhitelisted(boolean p0) {
    }
    
    public final boolean isBlacklisted() {
        return false;
    }
    
    public final void setBlacklisted(boolean p0) {
    }
    
    public final float getAffectionScore() {
        return 0.0F;
    }
    
    public final void setAffectionScore(float p0) {
    }
    
    /**
     * Calculate average listen duration as a percentage of song length
     */
    public final float avgListenDuration() {
        return 0.0F;
    }
    
    /**
     * Calculate affection score based on listening behavior
     */
    public final float calculateAffectionScore() {
        return 0.0F;
    }
    
    public final float calculateSmartCooldownHours() {
        return 0.0F;
    }
    
    public final boolean isOnCooldown(float cooldownHours) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final long component10() {
        return 0L;
    }
    
    public final long component11() {
        return 0L;
    }
    
    public final int component12() {
        return 0;
    }
    
    public final int component13() {
        return 0;
    }
    
    public final long component14() {
        return 0L;
    }
    
    public final long component15() {
        return 0L;
    }
    
    public final boolean component16() {
        return false;
    }
    
    public final boolean component17() {
        return false;
    }
    
    public final float component18() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    public final int component5() {
        return 0;
    }
    
    public final int component6() {
        return 0;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final int component8() {
        return 0;
    }
    
    public final long component9() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.unloop.app.data.Song copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String artist, @org.jetbrains.annotations.NotNull()
    java.lang.String platform, int playCount, int skipCount, int quickSkipCount, int loopsAvoided, long firstPlayedAt, long lastPlayedAt, long totalListenTimeMs, int youtubePlayCount, int spotifyPlayCount, long youtubeListenTimeMs, long spotifyListenTimeMs, boolean isWhitelisted, boolean isBlacklisted, float affectionScore) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}