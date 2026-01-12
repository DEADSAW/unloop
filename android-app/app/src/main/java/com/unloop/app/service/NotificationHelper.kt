package com.unloop.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.unloop.app.MainActivity
import com.unloop.app.R

/**
 * Optimized notification helper - reuses builder for efficiency
 */
object NotificationHelper {
    
    private const val CHANNEL_ID = "unloop_status"
    private const val NOTIFICATION_ID = 1001
    
    private var notificationManager: NotificationManager? = null
    private var builder: NotificationCompat.Builder? = null
    
    fun init(context: Context) {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createChannel()
        builder = createBuilder(context)
    }
    
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Unloop Status",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows skip status and song info"
                setShowBadge(false)
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
    
    private fun createBuilder(context: Context): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
    }
    
    fun showActive(context: Context) {
        val b = builder ?: createBuilder(context).also { builder = it }
        b.setContentTitle("∞ Unloop Active")
        b.setContentText("Listening for songs...")
        notificationManager?.notify(NOTIFICATION_ID, b.build())
    }
    
    fun showSkipped(songTitle: String, artist: String) {
        builder?.let { b ->
            b.setContentTitle("⏭️ Skipped: $songTitle")
            b.setContentText("by $artist")
            notificationManager?.notify(NOTIFICATION_ID, b.build())
        }
    }
    
    fun showNowPlaying(songTitle: String, artist: String, isNew: Boolean) {
        builder?.let { b ->
            val icon = if (isNew) "✨" else "🎵"
            b.setContentTitle("$icon $songTitle")
            b.setContentText("by $artist")
            notificationManager?.notify(NOTIFICATION_ID, b.build())
        }
    }
    
    fun showPaused() {
        builder?.let { b ->
            b.setContentTitle("⏸️ Unloop Paused")
            b.setContentText("Tap to resume")
            notificationManager?.notify(NOTIFICATION_ID, b.build())
        }
    }
    
    fun dismiss() {
        notificationManager?.cancel(NOTIFICATION_ID)
    }
}
