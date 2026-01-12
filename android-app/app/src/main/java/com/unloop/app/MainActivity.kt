package com.unloop.app

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.unloop.app.data.StatsRepository
import com.unloop.app.data.UnloopDatabase
import com.unloop.app.databinding.ActivityMainBinding
import com.unloop.app.service.MusicNotificationListenerService
import com.unloop.app.service.UnloopAccessibilityService
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val database by lazy { UnloopDatabase.getDatabase(this) }
    private val statsRepository by lazy { StatsRepository.getInstance(this) }
    
    private val songSkippedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            refreshStats()
            Toast.makeText(this@MainActivity, "⏭️ Skipped!", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        checkPermissions()
        registerReceivers()
        refreshStats()
    }
    
    override fun onResume() {
        super.onResume()
        checkPermissions()
        refreshStats()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        try { unregisterReceiver(songSkippedReceiver) } catch (e: Exception) {}
    }
    
    private fun setupUI() {
        // Settings button
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Main toggle
        binding.switchEnabled.isChecked = UnloopAccessibilityService.isEnabled
        binding.switchEnabled.setOnCheckedChangeListener { _, isChecked ->
            UnloopAccessibilityService.isEnabled = isChecked
            binding.statusText.text = if (isChecked) "Auto-Skip Active" else "Auto-Skip Paused"
            binding.textModeDescription.text = if (isChecked) 
                "Skipping songs you've heard before" 
            else 
                "Tap to enable"
        }
        
        // Permission buttons
        binding.btnGrantAccessibility.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
        
        binding.btnGrantNotification.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
        
        binding.btnGrantBattery.setOnClickListener {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = android.net.Uri.parse("package:$packageName")
            startActivity(intent)
        }
        
        // Skip history button
        binding.btnSkipHistory.setOnClickListener {
            showSkipHistoryDialog()
        }
        
        // Analytics button (was Export)
        binding.btnExportData.setOnClickListener {
            startActivity(Intent(this, AnalyticsActivity::class.java))
        }
        
        // Clear data button
        binding.btnClearData.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Clear History?")
                .setMessage("This will delete all your listening data.")
                .setPositiveButton("Clear") { _, _ ->
                    lifecycleScope.launch {
                        database.songDao().deleteAllSongs()
                        refreshStats()
                        Toast.makeText(this@MainActivity, "History cleared", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
    
    private fun checkPermissions() {
        val accessibilityEnabled = isAccessibilityServiceEnabled()
        val notificationEnabled = isNotificationListenerEnabled()
        
        binding.cardAccessibilityPermission.visibility = 
            if (accessibilityEnabled) View.GONE else View.VISIBLE
        binding.cardNotificationPermission.visibility = 
            if (notificationEnabled) View.GONE else View.VISIBLE
            
        val batteryOptDisabled = isBatteryOptimizationDisabled()
        binding.cardBatteryPermission.visibility = 
            if (batteryOptDisabled) View.GONE else View.VISIBLE
    }
    
    private fun isBatteryOptimizationDisabled(): Boolean {
        val pm = getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
        return pm.isIgnoringBatteryOptimizations(packageName)
    }
    
    private fun isAccessibilityServiceEnabled(): Boolean {
        val service = "${packageName}/${UnloopAccessibilityService::class.java.canonicalName}"
        val enabledServices = Settings.Secure.getString(
            contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabledServices.contains(service)
    }
    
    private fun isNotificationListenerEnabled(): Boolean {
        val flat = Settings.Secure.getString(
            contentResolver, "enabled_notification_listeners"
        ) ?: return false
        return flat.contains(ComponentName(this, MusicNotificationListenerService::class.java).flattenToString())
    }
    
    private fun registerReceivers() {
        val filter = IntentFilter(UnloopAccessibilityService.ACTION_SONG_SKIPPED)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(songSkippedReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(songSkippedReceiver, filter)
        }
    }
    
    private fun refreshStats() {
        lifecycleScope.launch {
            try {
                val dao = database.songDao()
                val songCount = dao.getTotalSongCount()
                val artistCount = dao.getUniqueArtistCount()
                val loopsAvoided = dao.getTotalLoopsAvoided() ?: 0
                val listenTimeMs = dao.getTotalListenTime() ?: 0L
                
                // Format listen time
                val hours = listenTimeMs / 3600000
                val mins = (listenTimeMs % 3600000) / 60000
                val listenTimeStr = if (hours > 0) "${hours}h ${mins}m" else "${mins}m"
                
                runOnUiThread {
                    binding.textSongCount.text = songCount.toString()
                    binding.textArtistCount.text = artistCount.toString()
                    binding.textLoopsAvoided.text = loopsAvoided.toString()
                    binding.textListenTime.text = listenTimeStr
                }
            } catch (e: Exception) {
                // Ignore stats errors
            }
        }
    }
    
    private fun showSkipHistoryDialog() {
        lifecycleScope.launch {
            try {
                val history = database.songDao().getRecentSkipHistory(30)
                
                if (history.isEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "No skips yet!", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                
                val items = history.map { entry ->
                    val timeAgo = getTimeAgo(entry.timestamp)
                    "${entry.songTitle} • $timeAgo"
                }
                
                runOnUiThread {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("⏭️ Recent Skips")
                        .setItems(items.toTypedArray(), null)
                        .setPositiveButton("OK", null)
                        .show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error loading history", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun getTimeAgo(timestamp: Long): String {
        val diff = System.currentTimeMillis() - timestamp
        val mins = diff / 60000
        val hours = diff / 3600000
        val days = diff / 86400000
        
        return when {
            mins < 1 -> "Just now"
            mins < 60 -> "${mins}m ago"
            hours < 24 -> "${hours}h ago"
            else -> "${days}d ago"
        }
    }
}
