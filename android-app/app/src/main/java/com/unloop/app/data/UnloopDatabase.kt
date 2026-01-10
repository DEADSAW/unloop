package com.unloop.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Song::class, DailyStats::class, SkipHistoryEntry::class], 
    version = 3, 
    exportSchema = false
)
abstract class UnloopDatabase : RoomDatabase() {
    
    abstract fun songDao(): SongDao
    
    companion object {
        @Volatile
        private var INSTANCE: UnloopDatabase? = null
        
        fun getDatabase(context: Context): UnloopDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UnloopDatabase::class.java,
                    "unloop_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
