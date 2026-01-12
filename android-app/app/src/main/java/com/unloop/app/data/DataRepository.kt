package com.unloop.app.data

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class BackupData(
    val songs: List<Song> = emptyList(),
    val skipHistory: List<SkipHistoryEntry> = emptyList(),
    val version: Int = 1,
    val timestamp: Long = System.currentTimeMillis()
)

class DataRepository(private val context: Context) {
    
    private val db = UnloopDatabase.getDatabase(context)
    private val gson = Gson()
    
    suspend fun exportData(uri: Uri): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val songs = db.songDao().getAllSongsList()
            val history = db.songDao().getAllSkipHistory()
            val backup = BackupData(songs, history)
            
            context.contentResolver.openOutputStream(uri)?.use { output ->
                output.write(gson.toJson(backup).toByteArray())
            }
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    suspend fun importData(uri: Uri): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                val json = input.bufferedReader().readText()
                val backup = gson.fromJson(json, BackupData::class.java)
                
                if (backup.songs.isNotEmpty()) {
                    db.songDao().insertSongs(backup.songs)
                }
                if (backup.skipHistory.isNotEmpty()) {
                    db.songDao().insertSkipHistoryList(backup.skipHistory)
                }
            }
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
