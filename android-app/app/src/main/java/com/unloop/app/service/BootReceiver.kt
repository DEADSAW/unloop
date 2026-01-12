package com.unloop.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Receiver that starts Unloop services when device boots up
 * This ensures the app is ready to skip songs immediately after restart
 */
class BootReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "BootReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON") {
            
            Log.d(TAG, "Device booted - Unloop ready!")
            
            // The AccessibilityService and NotificationListenerService 
            // are automatically restored by the system if previously enabled.
            // We just log that boot completed.
            
            // Initialize the notification channel early
            NotificationHelper.init(context)
        }
    }
}
