package com.unloop.app

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.unloop.app.data.DiscoveryMode
import com.unloop.app.data.PreferencesManager
import com.unloop.app.databinding.ActivitySettingsBinding
import com.unloop.app.service.UnloopAccessibilityService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

import com.unloop.app.data.DataRepository
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: PreferencesManager
    private val dataRepository by lazy { DataRepository(this) }
    
    private val exportLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        uri?.let { performExport(it) }
    }
    
    private val importLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { performImport(it) }
    }
    
    private fun performExport(uri: Uri) {
        lifecycleScope.launch {
            Toast.makeText(this@SettingsActivity, R.string.msg_backing_up, Toast.LENGTH_SHORT).show()
            val result = dataRepository.exportData(uri)
            if (result.isSuccess) {
                Toast.makeText(this@SettingsActivity, R.string.msg_backup_success, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@SettingsActivity, R.string.msg_backup_fail, Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun performImport(uri: Uri) {
        lifecycleScope.launch {
            Toast.makeText(this@SettingsActivity, R.string.msg_restoring, Toast.LENGTH_SHORT).show()
            val result = dataRepository.importData(uri)
            if (result.isSuccess) {
                Toast.makeText(this@SettingsActivity, R.string.msg_restore_success, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@SettingsActivity, R.string.msg_restore_fail, Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        prefs = PreferencesManager(this)
        
        setupUI()
        loadSettings()
    }
    
    private fun setupUI() {
        // Back button
        binding.btnBack.setOnClickListener { finish() }
        
        // Mode selection
        binding.radioGroupMode.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                R.id.radioStrict -> DiscoveryMode.STRICT
                R.id.radioThreshold -> DiscoveryMode.SEMI_STRICT
                R.id.radioTimeBased -> DiscoveryMode.MEMORY_FADE
                R.id.radioSmart -> DiscoveryMode.SMART_AUTO
                else -> DiscoveryMode.STRICT
            }
            
            lifecycleScope.launch {
                prefs.setDiscoveryMode(mode)
                UnloopAccessibilityService.currentMode = mode
            }
            
            // Show/hide relevant settings
            binding.layoutThreshold.visibility = 
                if (checkedId == R.id.radioThreshold) View.VISIBLE else View.GONE
            binding.layoutTimeBased.visibility = 
                if (checkedId == R.id.radioTimeBased) View.VISIBLE else View.GONE
        }
        
        // Threshold slider
        binding.seekBarThreshold.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val threshold = progress + 1
                binding.textThresholdValue.text = "Skip after hearing $threshold times"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val threshold = (seekBar?.progress ?: 0) + 1
                lifecycleScope.launch {
                    prefs.setSkipThreshold(threshold)
                }
            }
        })
        
        // Memory fade slider
        binding.seekBarFade.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val days = progress + 1
                binding.textFadeValue.text = "Allow after $days days"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val days = (seekBar?.progress ?: 0) + 1
                lifecycleScope.launch {
                    prefs.setMemoryFadeDays(days)
                }
            }
        })
        
        // Theme toggle
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                prefs.setDarkTheme(isChecked)
            }
        }
        
        // Library button
        binding.btnLibrary.setOnClickListener {
            startActivity(android.content.Intent(this, LibraryActivity::class.java))
        }
        
        // Backup/Restore
        binding.btnBackup.setOnClickListener {
            exportLauncher.launch("unloop_backup_${System.currentTimeMillis()}.json")
        }
        
        binding.btnRestore.setOnClickListener {
            importLauncher.launch(arrayOf("application/json"))
        }
    }
    
    private fun loadSettings() {
        lifecycleScope.launch {
            // Load mode
            val mode = prefs.discoveryMode.first()
            val radioId = when (mode) {
                DiscoveryMode.STRICT -> R.id.radioStrict
                DiscoveryMode.SEMI_STRICT -> R.id.radioThreshold
                DiscoveryMode.MEMORY_FADE -> R.id.radioTimeBased
                DiscoveryMode.SMART_AUTO -> R.id.radioSmart
                else -> R.id.radioStrict
            }
            binding.radioGroupMode.check(radioId)
            
            // Load threshold
            val threshold = prefs.skipThreshold.first()
            binding.seekBarThreshold.progress = threshold - 1
            binding.textThresholdValue.text = "Skip after hearing $threshold times"
            
            // Load fade days
            val days = prefs.memoryFadeDays.first()
            binding.seekBarFade.progress = days - 1
            binding.textFadeValue.text = "Allow after $days days"
            
            // Load theme
            binding.switchTheme.isChecked = prefs.darkTheme.first()
        }
    }
}
