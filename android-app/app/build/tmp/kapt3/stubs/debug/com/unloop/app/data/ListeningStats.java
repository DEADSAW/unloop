package com.unloop.app.data;

/**
 * Comprehensive listening statistics
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\r\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\bA\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u00f3\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0007\u0012\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015\u0012\u000e\b\u0002\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u0015\u0012\u000e\b\u0002\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00180\u0015\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u001b\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u001b\u0012\b\b\u0002\u0010\u001d\u001a\u00020\u001b\u00a2\u0006\u0002\u0010\u001eJ\t\u0010E\u001a\u00020\u0003H\u00c6\u0003J\t\u0010F\u001a\u00020\u0003H\u00c6\u0003J\t\u0010G\u001a\u00020\u0003H\u00c6\u0003J\t\u0010H\u001a\u00020\u0003H\u00c6\u0003J\t\u0010I\u001a\u00020\u0003H\u00c6\u0003J\t\u0010J\u001a\u00020\u0003H\u00c6\u0003J\t\u0010K\u001a\u00020\u0003H\u00c6\u0003J\t\u0010L\u001a\u00020\u0007H\u00c6\u0003J\u000f\u0010M\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015H\u00c6\u0003J\u000f\u0010N\u001a\b\u0012\u0004\u0012\u00020\u00180\u0015H\u00c6\u0003J\u000f\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00180\u0015H\u00c6\u0003J\t\u0010P\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Q\u001a\u00020\u001bH\u00c6\u0003J\t\u0010R\u001a\u00020\u001bH\u00c6\u0003J\t\u0010S\u001a\u00020\u001bH\u00c6\u0003J\t\u0010T\u001a\u00020\u0003H\u00c6\u0003J\t\u0010U\u001a\u00020\u0007H\u00c6\u0003J\t\u0010V\u001a\u00020\u0007H\u00c6\u0003J\t\u0010W\u001a\u00020\u0007H\u00c6\u0003J\t\u0010X\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Y\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Z\u001a\u00020\u0003H\u00c6\u0003J\u00f7\u0001\u0010[\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00072\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00072\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u00152\u000e\b\u0002\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u00152\u000e\b\u0002\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00180\u00152\b\b\u0002\u0010\u001a\u001a\u00020\u001b2\b\b\u0002\u0010\u001c\u001a\u00020\u001b2\b\b\u0002\u0010\u001d\u001a\u00020\u001bH\u00c6\u0001J\u0013\u0010\\\u001a\u00020]2\b\u0010^\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010_\u001a\u00020\u0003H\u00d6\u0001J\t\u0010`\u001a\u00020aH\u00d6\u0001R\u0011\u0010\u001a\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\u001c\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010 R\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010#R\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010#R\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010#R\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010#R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00180\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010*\u001a\u00020\u001b8F\u00a2\u0006\u0006\u001a\u0004\b+\u0010 R\u0011\u0010\u0013\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010-R\u0011\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010#R\u0011\u0010\u0011\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010#R\u0011\u00100\u001a\u00020\u001b8F\u00a2\u0006\u0006\u001a\u0004\b1\u0010 R\u0011\u0010\t\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010-R\u0011\u00103\u001a\u00020\u001b8F\u00a2\u0006\u0006\u001a\u0004\b4\u0010 R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010#R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010)R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010)R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010#R\u0011\u00109\u001a\u00020\u001b8F\u00a2\u0006\u0006\u001a\u0004\b:\u0010 R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010-R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010#R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010#R\u0011\u0010\u001d\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010 R\u0011\u0010?\u001a\u00020\u001b8F\u00a2\u0006\u0006\u001a\u0004\b@\u0010 R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010-R\u0011\u0010B\u001a\u00020\u001b8F\u00a2\u0006\u0006\u001a\u0004\bC\u0010 R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010#\u00a8\u0006b"}, d2 = {"Lcom/unloop/app/data/ListeningStats;", "", "totalSongsDiscovered", "", "totalLoopsAvoided", "totalArtistsDiscovered", "totalListenTimeMs", "", "youtubeListenTimeMs", "spotifyListenTimeMs", "youtubePlays", "spotifyPlays", "newSongsToday", "newSongsThisWeek", "newSongsThisMonth", "newArtistsToday", "newArtistsThisWeek", "sessionSongs", "sessionLoopsAvoided", "sessionListenTimeMs", "topArtists", "", "Lcom/unloop/app/data/ArtistStats;", "topSongs", "Lcom/unloop/app/data/Song;", "recentSongs", "freshnessScore", "", "intelligenceScore", "varietyScore", "(IIIJJJIIIIIIIIIJLjava/util/List;Ljava/util/List;Ljava/util/List;FFF)V", "getFreshnessScore", "()F", "getIntelligenceScore", "getNewArtistsThisWeek", "()I", "getNewArtistsToday", "getNewSongsThisMonth", "getNewSongsThisWeek", "getNewSongsToday", "getRecentSongs", "()Ljava/util/List;", "sessionListenMinutes", "getSessionListenMinutes", "getSessionListenTimeMs", "()J", "getSessionLoopsAvoided", "getSessionSongs", "spotifyListenHours", "getSpotifyListenHours", "getSpotifyListenTimeMs", "spotifyPercentage", "getSpotifyPercentage", "getSpotifyPlays", "getTopArtists", "getTopSongs", "getTotalArtistsDiscovered", "totalListenHours", "getTotalListenHours", "getTotalListenTimeMs", "getTotalLoopsAvoided", "getTotalSongsDiscovered", "getVarietyScore", "youtubeListenHours", "getYoutubeListenHours", "getYoutubeListenTimeMs", "youtubePercentage", "getYoutubePercentage", "getYoutubePlays", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
public final class ListeningStats {
    private final int totalSongsDiscovered = 0;
    private final int totalLoopsAvoided = 0;
    private final int totalArtistsDiscovered = 0;
    private final long totalListenTimeMs = 0L;
    private final long youtubeListenTimeMs = 0L;
    private final long spotifyListenTimeMs = 0L;
    private final int youtubePlays = 0;
    private final int spotifyPlays = 0;
    private final int newSongsToday = 0;
    private final int newSongsThisWeek = 0;
    private final int newSongsThisMonth = 0;
    private final int newArtistsToday = 0;
    private final int newArtistsThisWeek = 0;
    private final int sessionSongs = 0;
    private final int sessionLoopsAvoided = 0;
    private final long sessionListenTimeMs = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.unloop.app.data.ArtistStats> topArtists = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.unloop.app.data.Song> topSongs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.unloop.app.data.Song> recentSongs = null;
    private final float freshnessScore = 0.0F;
    private final float intelligenceScore = 0.0F;
    private final float varietyScore = 0.0F;
    
    public ListeningStats(int totalSongsDiscovered, int totalLoopsAvoided, int totalArtistsDiscovered, long totalListenTimeMs, long youtubeListenTimeMs, long spotifyListenTimeMs, int youtubePlays, int spotifyPlays, int newSongsToday, int newSongsThisWeek, int newSongsThisMonth, int newArtistsToday, int newArtistsThisWeek, int sessionSongs, int sessionLoopsAvoided, long sessionListenTimeMs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.unloop.app.data.ArtistStats> topArtists, @org.jetbrains.annotations.NotNull()
    java.util.List<com.unloop.app.data.Song> topSongs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.unloop.app.data.Song> recentSongs, float freshnessScore, float intelligenceScore, float varietyScore) {
        super();
    }
    
    public final int getTotalSongsDiscovered() {
        return 0;
    }
    
    public final int getTotalLoopsAvoided() {
        return 0;
    }
    
    public final int getTotalArtistsDiscovered() {
        return 0;
    }
    
    public final long getTotalListenTimeMs() {
        return 0L;
    }
    
    public final long getYoutubeListenTimeMs() {
        return 0L;
    }
    
    public final long getSpotifyListenTimeMs() {
        return 0L;
    }
    
    public final int getYoutubePlays() {
        return 0;
    }
    
    public final int getSpotifyPlays() {
        return 0;
    }
    
    public final int getNewSongsToday() {
        return 0;
    }
    
    public final int getNewSongsThisWeek() {
        return 0;
    }
    
    public final int getNewSongsThisMonth() {
        return 0;
    }
    
    public final int getNewArtistsToday() {
        return 0;
    }
    
    public final int getNewArtistsThisWeek() {
        return 0;
    }
    
    public final int getSessionSongs() {
        return 0;
    }
    
    public final int getSessionLoopsAvoided() {
        return 0;
    }
    
    public final long getSessionListenTimeMs() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.unloop.app.data.ArtistStats> getTopArtists() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.unloop.app.data.Song> getTopSongs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.unloop.app.data.Song> getRecentSongs() {
        return null;
    }
    
    public final float getFreshnessScore() {
        return 0.0F;
    }
    
    public final float getIntelligenceScore() {
        return 0.0F;
    }
    
    public final float getVarietyScore() {
        return 0.0F;
    }
    
    public final float getTotalListenHours() {
        return 0.0F;
    }
    
    public final float getYoutubeListenHours() {
        return 0.0F;
    }
    
    public final float getSpotifyListenHours() {
        return 0.0F;
    }
    
    public final float getSessionListenMinutes() {
        return 0.0F;
    }
    
    public final float getYoutubePercentage() {
        return 0.0F;
    }
    
    public final float getSpotifyPercentage() {
        return 0.0F;
    }
    
    public ListeningStats() {
        super();
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int component10() {
        return 0;
    }
    
    public final int component11() {
        return 0;
    }
    
    public final int component12() {
        return 0;
    }
    
    public final int component13() {
        return 0;
    }
    
    public final int component14() {
        return 0;
    }
    
    public final int component15() {
        return 0;
    }
    
    public final long component16() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.unloop.app.data.ArtistStats> component17() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.unloop.app.data.Song> component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.unloop.app.data.Song> component19() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final float component20() {
        return 0.0F;
    }
    
    public final float component21() {
        return 0.0F;
    }
    
    public final float component22() {
        return 0.0F;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final long component4() {
        return 0L;
    }
    
    public final long component5() {
        return 0L;
    }
    
    public final long component6() {
        return 0L;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final int component8() {
        return 0;
    }
    
    public final int component9() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.unloop.app.data.ListeningStats copy(int totalSongsDiscovered, int totalLoopsAvoided, int totalArtistsDiscovered, long totalListenTimeMs, long youtubeListenTimeMs, long spotifyListenTimeMs, int youtubePlays, int spotifyPlays, int newSongsToday, int newSongsThisWeek, int newSongsThisMonth, int newArtistsToday, int newArtistsThisWeek, int sessionSongs, int sessionLoopsAvoided, long sessionListenTimeMs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.unloop.app.data.ArtistStats> topArtists, @org.jetbrains.annotations.NotNull()
    java.util.List<com.unloop.app.data.Song> topSongs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.unloop.app.data.Song> recentSongs, float freshnessScore, float intelligenceScore, float varietyScore) {
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