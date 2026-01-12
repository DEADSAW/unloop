package com.unloop.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "unloop_settings")

/**
 * Optimized preferences manager using DataStore
 * Caches values for fast access, persists asynchronously
 */
class PreferencesManager(private val context: Context) {
    
    companion object {
        // Keys
        private val MODE_KEY = stringPreferencesKey("discovery_mode")
        private val ENABLED_KEY = booleanPreferencesKey("is_enabled")
        private val SKIP_THRESHOLD_KEY = intPreferencesKey("skip_threshold")
        private val MEMORY_FADE_DAYS_KEY = intPreferencesKey("memory_fade_days")
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        
        // Defaults
        const val DEFAULT_SKIP_THRESHOLD = 1
        const val DEFAULT_MEMORY_FADE_DAYS = 7
    }
    
    // Flows for reactive updates
    val discoveryMode: Flow<DiscoveryMode> = context.dataStore.data.map { prefs ->
        val modeStr = prefs[MODE_KEY] ?: DiscoveryMode.STRICT.name
        try { DiscoveryMode.valueOf(modeStr) } catch (e: Exception) { DiscoveryMode.STRICT }
    }
    
    val isEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[ENABLED_KEY] ?: true
    }
    
    val skipThreshold: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[SKIP_THRESHOLD_KEY] ?: DEFAULT_SKIP_THRESHOLD
    }
    
    val memoryFadeDays: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[MEMORY_FADE_DAYS_KEY] ?: DEFAULT_MEMORY_FADE_DAYS
    }
    
    val darkTheme: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[DARK_THEME_KEY] ?: true
    }
    
    // Setters
    suspend fun setDiscoveryMode(mode: DiscoveryMode) {
        context.dataStore.edit { prefs ->
            prefs[MODE_KEY] = mode.name
        }
    }
    
    suspend fun setEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ENABLED_KEY] = enabled
        }
    }
    
    suspend fun setSkipThreshold(threshold: Int) {
        context.dataStore.edit { prefs ->
            prefs[SKIP_THRESHOLD_KEY] = threshold.coerceIn(1, 10)
        }
    }
    
    suspend fun setMemoryFadeDays(days: Int) {
        context.dataStore.edit { prefs ->
            prefs[MEMORY_FADE_DAYS_KEY] = days.coerceIn(1, 365)
        }
    }
    
    suspend fun setDarkTheme(dark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_THEME_KEY] = dark
        }
    }
}
