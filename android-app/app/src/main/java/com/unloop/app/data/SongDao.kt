package com.unloop.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    
    // Basic queries
    @Query("SELECT * FROM songs ORDER BY lastPlayedAt DESC")
    fun getAllSongs(): Flow<List<Song>>
    
    @Query("SELECT * FROM songs WHERE id = :songId")
    suspend fun getSongById(songId: String): Song?

    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' ORDER BY lastPlayedAt DESC")
    fun searchSongs(query: String): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE (title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%') AND isWhitelisted = 1 ORDER BY lastPlayedAt DESC")
    fun searchWhitelistedSongs(query: String): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE (title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%') AND isBlacklisted = 1 ORDER BY lastPlayedAt DESC")
    fun searchBlacklistedSongs(query: String): Flow<List<Song>>
    
    @Query("SELECT * FROM songs ORDER BY lastPlayedAt DESC LIMIT :limit")
    suspend fun getRecentSongs(limit: Int): List<Song>
    
    @Query("SELECT * FROM songs WHERE artist = :artist ORDER BY lastPlayedAt DESC LIMIT :limit")
    suspend fun getRecentSongsByArtist(artist: String, limit: Int): List<Song>
    
    // Counts
    @Query("SELECT COUNT(*) FROM songs")
    suspend fun getTotalSongCount(): Int
    
    @Query("SELECT COUNT(*) FROM songs")
    fun getTotalSongCountFlow(): Flow<Int>
    
    @Query("SELECT COUNT(DISTINCT artist) FROM songs")
    suspend fun getUniqueArtistCount(): Int
    
    @Query("SELECT COUNT(DISTINCT artist) FROM songs")
    fun getUniqueArtistCountFlow(): Flow<Int>
    
    @Query("SELECT SUM(loopsAvoided) FROM songs")
    suspend fun getTotalLoopsAvoided(): Int?
    
    @Query("SELECT SUM(loopsAvoided) FROM songs")
    fun getTotalLoopsAvoidedFlow(): Flow<Int?>
    
    @Query("SELECT SUM(skipCount) FROM songs")
    suspend fun getTotalSkipCount(): Int?
    
    // Listening time
    @Query("SELECT SUM(totalListenTimeMs) FROM songs")
    suspend fun getTotalListenTime(): Long?
    
    @Query("SELECT SUM(totalListenTimeMs) FROM songs")
    fun getTotalListenTimeFlow(): Flow<Long?>
    
    @Query("SELECT SUM(youtubeListenTimeMs) FROM songs")
    suspend fun getYoutubeListenTime(): Long?
    
    @Query("SELECT SUM(spotifyListenTimeMs) FROM songs")
    suspend fun getSpotifyListenTime(): Long?
    
    // Platform stats
    @Query("SELECT SUM(youtubePlayCount) FROM songs")
    suspend fun getYoutubePlays(): Int?
    
    @Query("SELECT SUM(spotifyPlayCount) FROM songs")
    suspend fun getSpotifyPlays(): Int?
    
    // Time-based queries
    @Query("SELECT COUNT(*) FROM songs WHERE firstPlayedAt >= :since")
    suspend fun getSongsDiscoveredSince(since: Long): Int
    
    @Query("SELECT COUNT(DISTINCT artist) FROM songs WHERE firstPlayedAt >= :since")
    suspend fun getArtistsDiscoveredSince(since: Long): Int
    
    // Top artists
    @Query("""
        SELECT artist as name, 
               COUNT(*) as songCount, 
               SUM(playCount) as totalPlayCount, 
               SUM(totalListenTimeMs) as totalListenTimeMs,
               SUM(loopsAvoided) as loopsAvoided
        FROM songs 
        GROUP BY artist 
        ORDER BY totalPlayCount DESC 
        LIMIT :limit
    """)
    suspend fun getTopArtists(limit: Int): List<ArtistStats>
    
    // Top songs by play count
    @Query("SELECT * FROM songs ORDER BY playCount DESC LIMIT :limit")
    suspend fun getTopSongs(limit: Int): List<Song>
    
    // Whitelist/Blacklist
    @Query("SELECT * FROM songs WHERE isWhitelisted = 1")
    fun getWhitelistedSongs(): Flow<List<Song>>
    
    @Query("SELECT * FROM songs WHERE isBlacklisted = 1")
    fun getBlacklistedSongs(): Flow<List<Song>>
    
    // CRUD operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<Song>)
    
    @Query("SELECT * FROM songs")
    suspend fun getAllSongsList(): List<Song>
    
    @Update
    suspend fun updateSong(song: Song)
    
    @Delete
    suspend fun deleteSong(song: Song)
    
    @Query("DELETE FROM songs")
    suspend fun deleteAllSongs()
    
    @Query("UPDATE songs SET isWhitelisted = :isWhitelisted WHERE id = :songId")
    suspend fun setWhitelisted(songId: String, isWhitelisted: Boolean)
    
    @Query("UPDATE songs SET isBlacklisted = :isBlacklisted WHERE id = :songId")
    suspend fun setBlacklisted(songId: String, isBlacklisted: Boolean)
    
    // Daily stats
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyStats(stats: DailyStats)
    
    @Query("SELECT * FROM daily_stats WHERE date = :date")
    suspend fun getDailyStats(date: String): DailyStats?
    
    @Query("SELECT * FROM daily_stats ORDER BY date DESC LIMIT :days")
    suspend fun getRecentDailyStats(days: Int): List<DailyStats>
    
    @Query("SELECT * FROM daily_stats ORDER BY date DESC LIMIT :days")
    fun getRecentDailyStatsFlow(days: Int): Flow<List<DailyStats>>
    
    // Skip history
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkipHistory(entry: SkipHistoryEntry)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkipHistoryList(entries: List<SkipHistoryEntry>)
    
    @Query("SELECT * FROM skip_history ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentSkipHistory(limit: Int): List<SkipHistoryEntry>

    @Query("SELECT * FROM skip_history")
    suspend fun getAllSkipHistory(): List<SkipHistoryEntry>
    
    @Query("SELECT * FROM skip_history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentSkipHistoryFlow(limit: Int): Flow<List<SkipHistoryEntry>>
    
    @Query("SELECT COUNT(*) FROM skip_history")
    suspend fun getTotalSkipHistoryCount(): Int
    
    @Query("DELETE FROM skip_history")
    suspend fun clearSkipHistory()
}
