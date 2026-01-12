package com.unloop.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class UnloopApplication : Application() {
    
    companion object {
        const val CHANNEL_ID = "unloop_notifications"
        const val CHANNEL_NAME = "Unloop Notifications"
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        
        // Apply theme settings
        val prefs = com.unloop.app.data.PreferencesManager(this)
        kotlinx.coroutines.MainScope().launch {
            prefs.darkTheme.collect { isDark ->
                androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
                    if (isDark) androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES 
                    else androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifications when songs are skipped"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
