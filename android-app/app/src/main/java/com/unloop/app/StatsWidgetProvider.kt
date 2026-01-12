package com.unloop.app

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.unloop.app.data.StatsRepository
import com.unloop.app.data.WidgetStats

/**
 * OPTIMIZED Widget Provider - uses lightweight in-memory stats, no DB queries
 */
class StatsWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateAllWidgets(context, appWidgetManager, appWidgetIds)
    }
    
    companion object {
        private var statsRepo: StatsRepository? = null

        fun updateAllWidgets(context: Context, manager: AppWidgetManager, ids: IntArray) {
            if (ids.isEmpty()) return
            
            if (statsRepo == null) {
                statsRepo = StatsRepository.getInstance(context)
            }
            
            // Use lightweight synchronous stats - NO database queries!
            val stats = statsRepo!!.getWidgetStats()
            
            for (id in ids) {
                updateAppWidget(context, manager, id, stats)
            }
        }

        private fun updateAppWidget(context: Context, manager: AppWidgetManager, id: Int, stats: WidgetStats) {
            val views = RemoteViews(context.packageName, R.layout.widget_stats)
            
            // Session Time
            val sessionMins = stats.sessionListenTimeMs / 60000
            val sessionTimeStr = if (sessionMins >= 60) {
                 "${sessionMins / 60}h ${sessionMins % 60}m"
            } else {
                "${sessionMins}m"
            }
            views.setTextViewText(R.id.widgetSessionTime, sessionTimeStr)
            
            // Loops (Session)
            views.setTextViewText(R.id.widgetLoops, "${stats.sessionLoopsAvoided}")
            
            // Total Time
            val totalMins = stats.totalListenTimeMs / 60000
            val totalTimeStr = "${totalMins / 60}h ${totalMins % 60}m"
            views.setTextViewText(R.id.widgetTotalTime, context.getString(R.string.widget_total_time, totalTimeStr))
            
            // Click to open app
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.textTitle, pendingIntent)
            
            manager.updateAppWidget(id, views)
        }
    }
}
