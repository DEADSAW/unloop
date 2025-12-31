/**
 * Unloop Desktop - Renderer Process
 * Author: Sangam
 * UI logic and data visualization
 */

const { ipcRenderer } = require('electron');
const AnalyticsEngine = require('./smart-engine');

// ═══════════════════════════════════════════════════════════════
// ANALYTICS ENGINE INITIALIZATION
// ═══════════════════════════════════════════════════════════════

const analytics = new AnalyticsEngine();

// ═══════════════════════════════════════════════════════════════
// STATE MANAGEMENT
// ═══════════════════════════════════════════════════════════════

let state = {
  enabled: true,
  settings: {
    mode: 'strict',
    memoryFadeHours: 72,
    songsBeforeRepeat: 5,
    maxArtistPerSession: 3,
    theme: 'dark',
    minimalMode: false,
    desktopNotifications: true,
    soundEffects: false,
    alwaysOnTop: false,
    startWithWindows: false
  },
  stats: {
    listened: 0,
    skipped: 0,
    totalListeningSeconds: 0,
    totalUniqueSongs: 0,
    totalArtists: 0,
    loopsPrevented: 0,
    smartScore: 50
  },
  history: {},
  recentActivity: []
};

// ═══════════════════════════════════════════════════════════════
// INITIALIZATION
// ═══════════════════════════════════════════════════════════════

async function init() {
  await loadState();
  setupEventListeners();
  updateUI();
  startPeriodicUpdates();
}

async function loadState() {
  try {
    const enabled = await ipcRenderer.invoke('store-get', 'enabled', true);
    const settings = await ipcRenderer.invoke('store-get', 'settings', state.settings);
    const stats = await ipcRenderer.invoke('store-get', 'stats', state.stats);
    const history = await ipcRenderer.invoke('store-get', 'history', {});
    
    state.enabled = enabled;
    state.settings = { ...state.settings, ...settings };
    state.stats = { ...state.stats, ...stats };
    state.history = history;
    
    console.log('State loaded:', state);
  } catch (error) {
    console.error('Error loading state:', error);
  }
}

async function saveState() {
  try {
    await ipcRenderer.invoke('store-set', 'enabled', state.enabled);
    await ipcRenderer.invoke('store-set', 'settings', state.settings);
    await ipcRenderer.invoke('store-set', 'stats', state.stats);
    await ipcRenderer.invoke('store-set', 'history', state.history);
  } catch (error) {
    console.error('Error saving state:', error);
  }
}

// ═══════════════════════════════════════════════════════════════
// EVENT LISTENERS
// ═══════════════════════════════════════════════════════════════

function setupEventListeners() {
  // Navigation
  document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', () => {
      const page = item.dataset.page;
      switchPage(page);
    });
  });

  // Master toggle
  const masterToggle = document.getElementById('masterToggle');
  if (masterToggle) {
    masterToggle.checked = state.enabled;
    masterToggle.addEventListener('change', async (e) => {
      state.enabled = e.target.checked;
      await saveState();
      updateStatusCard();
      showNotification('Unloop', `Discovery mode ${state.enabled ? 'enabled' : 'disabled'}`);
    });
  }

  // Theme switcher
  document.querySelectorAll('.theme-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const theme = btn.dataset.theme;
      applyTheme(theme);
    });
  });

  // Settings
  setupSettingsListeners();
  
  // History
  setupHistoryListeners();
  
  // Listen for IPC messages
  ipcRenderer.on('toggle-enabled', (event, enabled) => {
    state.enabled = enabled;
    if (masterToggle) masterToggle.checked = enabled;
    updateUI();
  });
}

function setupSettingsListeners() {
  const modeSelect = document.getElementById('modeSelect');
  const themeSelect = document.getElementById('themeSelect');
  const minimalMode = document.getElementById('minimalMode');
  const desktopNotifications = document.getElementById('desktopNotifications');
  const soundEffects = document.getElementById('soundEffects');
  const alwaysOnTop = document.getElementById('alwaysOnTop');
  const startWithWindows = document.getElementById('startWithWindows');
  const saveSettings = document.getElementById('saveSettings');
  const resetSettings = document.getElementById('resetSettings');

  // Load current settings
  if (modeSelect) modeSelect.value = state.settings.mode;
  if (themeSelect) themeSelect.value = state.settings.theme;
  if (minimalMode) minimalMode.checked = state.settings.minimalMode;
  if (desktopNotifications) desktopNotifications.checked = state.settings.desktopNotifications;
  if (soundEffects) soundEffects.checked = state.settings.soundEffects;
  if (alwaysOnTop) alwaysOnTop.checked = state.settings.alwaysOnTop;
  if (startWithWindows) startWithWindows.checked = state.settings.startWithWindows;

  // Mode change - show/hide conditional settings
  if (modeSelect) {
    modeSelect.addEventListener('change', () => {
      updateModeSettings(modeSelect.value);
    });
    updateModeSettings(modeSelect.value);
  }

  // Save settings
  if (saveSettings) {
    saveSettings.addEventListener('click', async () => {
      state.settings.mode = modeSelect?.value || 'strict';
      state.settings.theme = themeSelect?.value || 'dark';
      state.settings.minimalMode = minimalMode?.checked || false;
      state.settings.desktopNotifications = desktopNotifications?.checked || true;
      state.settings.soundEffects = soundEffects?.checked || false;
      state.settings.alwaysOnTop = alwaysOnTop?.checked || false;
      state.settings.startWithWindows = startWithWindows?.checked || false;

      // Get mode-specific settings
      const memoryFadeHours = document.getElementById('memoryFadeHours');
      const songsBeforeRepeat = document.getElementById('songsBeforeRepeat');
      const maxArtistPerSession = document.getElementById('maxArtistPerSession');

      if (memoryFadeHours) state.settings.memoryFadeHours = parseInt(memoryFadeHours.value);
      if (songsBeforeRepeat) state.settings.songsBeforeRepeat = parseInt(songsBeforeRepeat.value);
      if (maxArtistPerSession) state.settings.maxArtistPerSession = parseInt(maxArtistPerSession.value);

      await saveState();
      updateUI();
      showNotification('Settings Saved', 'Your preferences have been updated');
    });
  }

  // Reset settings
  if (resetSettings) {
    resetSettings.addEventListener('click', async () => {
      if (confirm('Reset all settings to default?')) {
        state.settings = {
          mode: 'strict',
          memoryFadeHours: 72,
          songsBeforeRepeat: 5,
          maxArtistPerSession: 3,
          theme: 'dark',
          minimalMode: false,
          desktopNotifications: true,
          soundEffects: false,
          alwaysOnTop: false,
          startWithWindows: false
        };
        await saveState();
        location.reload();
      }
    });
  }
}

function updateModeSettings(mode) {
  const memoryFadeSetting = document.getElementById('memoryFadeSetting');
  const semiStrictSetting = document.getElementById('semiStrictSetting');
  const artistSmartSetting = document.getElementById('artistSmartSetting');

  // Hide all
  if (memoryFadeSetting) memoryFadeSetting.style.display = 'none';
  if (semiStrictSetting) semiStrictSetting.style.display = 'none';
  if (artistSmartSetting) artistSmartSetting.style.display = 'none';

  // Show relevant
  if (mode === 'memory-fade' && memoryFadeSetting) {
    memoryFadeSetting.style.display = 'flex';
  } else if (mode === 'semi-strict' && semiStrictSetting) {
    semiStrictSetting.style.display = 'flex';
  } else if (mode === 'artist-smart' && artistSmartSetting) {
    artistSmartSetting.style.display = 'flex';
  }
}

function setupHistoryListeners() {
  const exportBtn = document.getElementById('exportBtn');
  const clearHistoryBtn = document.getElementById('clearHistoryBtn');
  const historySearch = document.getElementById('historySearch');

  if (exportBtn) {
    exportBtn.addEventListener('click', exportHistory);
  }

  if (clearHistoryBtn) {
    clearHistoryBtn.addEventListener('click', async () => {
      if (confirm('Clear all listening history? This cannot be undone.')) {
        state.history = {};
        state.stats.totalUniqueSongs = 0;
        await saveState();
        updateUI();
        showNotification('History Cleared', 'All listening history has been removed');
      }
    });
  }

  if (historySearch) {
    historySearch.addEventListener('input', (e) => {
      filterHistory(e.target.value);
    });
  }
}

// ═══════════════════════════════════════════════════════════════
// UI UPDATES
// ═══════════════════════════════════════════════════════════════

function updateUI() {
  updateDashboard();
  updateHistory();
  updateStats();
  updateStatusCard();
}

function updateStatusCard() {
  const statusText = document.getElementById('statusText');
  const currentMode = document.getElementById('currentMode');

  if (statusText) {
    statusText.textContent = state.enabled ? 'Active & Protecting' : 'Paused';
  }

  if (currentMode) {
    const modeNames = {
      'strict': 'Strict Discovery',
      'memory-fade': 'Memory Fade',
      'semi-strict': 'Semi-Strict',
      'artist-smart': 'Artist Smart'
    };
    currentMode.textContent = modeNames[state.settings.mode] || 'Strict Discovery';
  }
}

function updateDashboard() {
  // Update stats with animation
  animateNumber('songsPlayed', state.stats.listened);
  animateNumber('loopsPrevented', state.stats.loopsPrevented || state.stats.skipped);
  animateNumber('artistsFound', state.stats.totalArtists);
  
  const listenTime = document.getElementById('listenTime');
  if (listenTime) listenTime.textContent = formatListeningTime(state.stats.totalListeningSeconds);

  // Update health metrics
  updateHealthMetrics();
  
  // Update recent activity
  updateRecentActivity();
  
  // Show smart insights
  showSmartInsights();
  
  // Show time-based suggestion
  showSmartSuggestion();
}

function updateHealthMetrics() {
  const totalSongs = state.stats.listened + state.stats.skipped;
  
  // Freshness: % of unique songs vs total plays
  const freshness = totalSongs > 0 ? Math.min(100, (state.stats.totalUniqueSongs / totalSongs) * 100) : 0;
  
  // Discovery Score: using analytics engine
  const discoveryScore = analytics.calculateDiscoveryScore(state.stats, state.history);
  state.stats.smartScore = discoveryScore;
  
  // Variety: based on artist diversity
  const variety = state.stats.totalArtists > 0 ? Math.min(100, state.stats.totalArtists * 2) : 0;

  setHealthBar('freshnessBar', 'freshnessValue', freshness);
  setHealthBar('intelligenceBar', 'intelligenceValue', intelligence);
  setHealthBar('varietyBar', 'varietyValue', variety);
}

function setHealthBar(barId, valueId, percentage) {
  const bar = document.getElementById(barId);
  const value = document.getElementById(valueId);
  
  if (bar) bar.style.width = `${percentage}%`;
  if (value) value.textContent = `${Math.round(percentage)}%`;
}

function updateRecentActivity() {
  const activityList = document.getElementById('recentActivity');
  if (!activityList) return;

  const historyArray = Object.entries(state.history)
    .map(([id, data]) => ({ id, ...data }))
    .sort((a, b) => (b.lastPlayed || 0) - (a.lastPlayed || 0))
    .slice(0, 10);

  if (historyArray.length === 0) {
    activityList.innerHTML = `
      <div class="activity-item empty">
        <span class="activity-icon">🎧</span>
        <span class="activity-text">Start playing music to see activity</span>
      </div>
    `;
    return;
  }

  activityList.innerHTML = historyArray.map(song => `
    <div class="activity-item">
      <span class="activity-icon">${song.totalSkips > 0 ? '⏭️' : '✅'}</span>
      <span class="activity-text">
        <strong>${escapeHtml(song.title || 'Unknown')}</strong> by ${escapeHtml(song.artist || 'Unknown')}
        <br>
        <small>${formatTimeAgo(song.lastPlayed)}</small>
      </span>
    </div>
  `).join('');
}

function updateHistory() {
  const historyList = document.getElementById('historyList');
  const totalSongs = document.getElementById('totalSongs');
  const totalPlays = document.getElementById('totalPlays');
  const mostPlayed = document.getElementById('mostPlayed');

  if (!historyList) return;

  const historyArray = Object.entries(state.history)
    .map(([id, data]) => ({ id, ...data }))
    .sort((a, b) => (b.totalPlays || 0) - (a.totalPlays || 0));

  if (totalSongs) totalSongs.textContent = historyArray.length;
  if (totalPlays) totalPlays.textContent = historyArray.reduce((sum, s) => sum + (s.totalPlays || 0), 0);
  if (mostPlayed && historyArray.length > 0) {
    mostPlayed.textContent = `${historyArray[0].title || 'Unknown'}`;
  }

  if (historyArray.length === 0) {
    historyList.innerHTML = `
      <div class="empty-state">
        <span class="empty-icon">📭</span>
        <p>No history yet. Start listening to music!</p>
      </div>
    `;
    return;
  }

  historyList.innerHTML = historyArray.map(song => `
    <div class="history-item">
      <div class="history-info">
        <div class="history-title">${escapeHtml(song.title || 'Unknown')}</div>
        <div class="history-artist">${escapeHtml(song.artist || 'Unknown')}</div>
      </div>
      <div class="history-stats">
        <span>Plays: ${song.totalPlays || 0}</span>
        <span>Skips: ${song.totalSkips || 0}</span>
      </div>
    </div>
  `).join('');
}

function updateStats() {
  updateTopArtists();
  updateTopSongs();
}

function updateTopArtists() {
  const topArtists = document.getElementById('topArtists');
  if (!topArtists) return;

  const artistMap = {};
  Object.values(state.history).forEach(song => {
    const artist = song.artist || 'Unknown';
    if (!artistMap[artist]) {
      artistMap[artist] = 0;
    }
    artistMap[artist] += song.totalPlays || 0;
  });

  const sortedArtists = Object.entries(artistMap)
    .sort((a, b) => b[1] - a[1])
    .slice(0, 10);

  if (sortedArtists.length === 0) {
    topArtists.innerHTML = '<div class="empty-state-small">No data yet</div>';
    return;
  }

  topArtists.innerHTML = sortedArtists.map(([artist, plays], index) => `
    <div class="top-item">
      <span class="top-rank">#${index + 1}</span>
      <span class="top-name">${escapeHtml(artist)}</span>
      <span class="top-value">${plays} plays</span>
    </div>
  `).join('');
}

function updateTopSongs() {
  const topSongs = document.getElementById('topSongs');
  if (!topSongs) return;

  const sortedSongs = Object.values(state.history)
    .sort((a, b) => (b.totalPlays || 0) - (a.totalPlays || 0))
    .slice(0, 10);

  if (sortedSongs.length === 0) {
    topSongs.innerHTML = '<div class="empty-state-small">No data yet</div>';
    return;
  }

  topSongs.innerHTML = sortedSongs.map((song, index) => `
    <div class="top-item">
      <span class="top-rank">#${index + 1}</span>
      <span class="top-name">${escapeHtml(song.title || 'Unknown')}</span>
      <span class="top-value">${song.totalPlays || 0} plays</span>
    </div>
  `).join('');
}

// ═══════════════════════════════════════════════════════════════
// NAVIGATION
// ═══════════════════════════════════════════════════════════════

function switchPage(pageName) {
  // Update nav items
  document.querySelectorAll('.nav-item').forEach(item => {
    item.classList.toggle('active', item.dataset.page === pageName);
  });

  // Update pages
  document.querySelectorAll('.page').forEach(page => {
    page.classList.toggle('active', page.id === `${pageName}-page`);
  });
}

// ═══════════════════════════════════════════════════════════════
// THEME
// ═══════════════════════════════════════════════════════════════

function applyTheme(theme) {
  if (theme === 'auto') {
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    theme = prefersDark ? 'dark' : 'light';
  }

  document.documentElement.setAttribute('data-theme', theme);
  state.settings.theme = theme;
  saveState();
}

// ═══════════════════════════════════════════════════════════════
// UTILITIES
// ═══════════════════════════════════════════════════════════════

function formatNumber(num) {
  if (num >= 1000000) return (num / 1000000).toFixed(1) + 'M';
  if (num >= 1000) return (num / 1000).toFixed(1) + 'K';
  return num.toString();
}

function formatListeningTime(seconds) {
  const hours = Math.floor(seconds / 3600);
  if (hours >= 24) {
    return Math.floor(hours / 24) + 'd';
  }
  return hours + 'h';
}

function formatTimeAgo(timestamp) {
  if (!timestamp) return 'Never';
  const seconds = Math.floor((Date.now() - timestamp) / 1000);
  
  if (seconds < 60) return 'Just now';
  if (seconds < 3600) return Math.floor(seconds / 60) + 'm ago';
  if (seconds < 86400) return Math.floor(seconds / 3600) + 'h ago';
  return Math.floor(seconds / 86400) + 'd ago';
}

function escapeHtml(text) {
  const div = document.createElement('div');
  div.textContent = text;
  return div.innerHTML;
}

function showNotification(title, body) {
  if (state.settings.desktopNotifications && Notification.permission === 'granted') {
    new Notification(title, { body, icon: '../assets/icon.png' });
  }
}

function exportHistory() {
  const csv = generateHistoryCSV();
  const blob = new Blob([csv], { type: 'text/csv' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = `unloop-history-${Date.now()}.csv`;
  a.click();
  URL.revokeObjectURL(url);
  showNotification('Export Complete', 'History exported successfully');
}

function generateHistoryCSV() {
  const headers = ['Title', 'Artist', 'Platform', 'Total Plays', 'Total Skips', 'First Played', 'Last Played'];
  const rows = Object.values(state.history).map(song => [
    song.title || 'Unknown',
    song.artist || 'Unknown',
    song.platform || 'Unknown',
    song.totalPlays || 0,
    song.totalSkips || 0,
    new Date(song.firstPlayed || 0).toISOString(),
    new Date(song.lastPlayed || 0).toISOString()
  ]);

  return [headers, ...rows]
    .map(row => row.map(cell => `"${cell}"`).join(','))
    .join('\n');
}

function filterHistory(query) {
  // TODO: Implement history filtering
  console.log('Filtering history:', query);
}

function startPeriodicUpdates() {
  setInterval(() => {
    updateRecentActivity();
  }, 30000); // Update every 30 seconds
}

// ═══════════════════════════════════════════════════════════════
// ADVANCED FEATURES
// ═══════════════════════════════════════════════════════════════

function animateNumber(elementId, targetValue) {
  const element = document.getElementById(elementId);
  if (!element) return;
  
  const currentValue = parseInt(element.textContent) || 0;
  const increment = Math.ceil((targetValue - currentValue) / 20);
  
  let current = currentValue;
  const interval = setInterval(() => {
    current += increment;
    if ((increment > 0 && current >= targetValue) || (increment < 0 && current <= targetValue)) {
      current = targetValue;
      clearInterval(interval);
    }
    element.textContent = formatNumber(current);
  }, 30);
}

function showSmartInsights() {
  const insights = analytics.generateInsights(state.stats, state.history);
  
  // Add insights section if not exists
  let insightsSection = document.getElementById('smartInsights');
  if (!insightsSection) {
    const activitySection = document.querySelector('.activity-section');
    if (activitySection) {
      insightsSection = document.createElement('div');
      insightsSection.id = 'smartInsights';
      insightsSection.className = 'insights-section';
      insightsSection.innerHTML = '<h3 class="section-title">📊 Insights</h3><div class="insights-grid" id="insightsGrid"></div>';
      activitySection.parentNode.insertBefore(insightsSection, activitySection);
    }
  }
  
  const insightsGrid = document.getElementById('insightsGrid');
  if (insightsGrid && insights.length > 0) {
    insightsGrid.innerHTML = insights.map(insight => `
      <div class="insight-card ${insight.type}">
        <span class="insight-icon">${insight.icon}</span>
        <div class="insight-content">
          <div class="insight-title">${insight.title}</div>
          <div class="insight-message">${insight.message}</div>
        </div>
      </div>
    `).join('');
  }
}

function showSmartSuggestion() {
  const prediction = analytics.predictNextPreference(state.history);
  const statusText = document.getElementById('statusText');
  
  if (statusText && state.enabled) {
    statusText.innerHTML = `Active & Protecting <span style="opacity: 0.8; font-size: 12px; display: block; margin-top: 4px;">${prediction.suggestion}</span>`;
  }
}

// Request notification permission
if (Notification.permission === 'default') {
  Notification.requestPermission();
}

// ═══════════════════════════════════════════════════════════════
// AUDIO LISTENER INTEGRATION
// ═══════════════════════════════════════════════════════════════

let audioListener = null;
let loopsDetectedCount = 0;

function initAudioListener() {
  audioListener = new AudioListener();
  
  const startBtn = document.getElementById('startListeningBtn');
  const stopBtn = document.getElementById('stopListeningBtn');
  const clearBtn = document.getElementById('clearDataBtn');
  
  // Callbacks for audio detection
  audioListener.onSongDetected = (data) => {
    updateListenerStats();
    addDetectionLog('detection', `Audio detected - Energy: ${data.energy}`, data.timestamp);
  };
  
  audioListener.onLoopDetected = (data) => {
    loopsDetectedCount++;
    updateListenerStats();
    addDetectionLog('loop', `🔁 ${data.message}`, data.timestamp, data.similarity);
    
    // Show notification
    if (state.settings.desktopNotifications) {
      new Notification('Loop Detected!', {
        body: `Song is repeating (${data.similarity}% match)`,
        icon: 'icon.png'
      });
    }
  };
  
  startBtn.addEventListener('click', async () => {
    const result = await audioListener.startListening();
    
    if (result.success) {
      startBtn.style.display = 'none';
      stopBtn.style.display = 'flex';
      updateListenerStatus(true);
      addDetectionLog('info', '✅ Audio listening started successfully');
      
      // Update stats periodically
      setInterval(() => {
        if (audioListener.isListening) {
          updateListenerStats();
        }
      }, 500);
    } else {
      alert(`Failed to start listening: ${result.message}\n\nPlease allow microphone access in your system settings.`);
      addDetectionLog('error', `❌ ${result.message}`);
    }
  });
  
  stopBtn.addEventListener('click', () => {
    const result = audioListener.stopListening();
    startBtn.style.display = 'flex';
    stopBtn.style.display = 'none';
    updateListenerStatus(false);
    addDetectionLog('info', '⏸️ Audio listening stopped');
  });
  
  clearBtn.addEventListener('click', () => {
    audioListener.clearFingerprints();
    loopsDetectedCount = 0;
    updateListenerStats();
    clearDetectionLog();
    addDetectionLog('info', '🗑️ Data cleared');
  });
}

function updateListenerStatus(isOnline) {
  const statusIndicator = document.getElementById('listenerStatus');
  const statusDot = statusIndicator.querySelector('.status-dot');
  const statusText = statusIndicator.querySelector('.status-text');
  
  if (isOnline) {
    statusDot.classList.remove('offline');
    statusDot.classList.add('online');
    statusText.textContent = 'Listening...';
  } else {
    statusDot.classList.remove('online');
    statusDot.classList.add('offline');
    statusText.textContent = 'Offline';
  }
}

function updateListenerStats() {
  if (!audioListener) return;
  
  const status = audioListener.getStatus();
  const volume = Math.round(status.currentVolume * 2); // Scale to 0-200
  
  // Update volume bar
  document.getElementById('volumeLevel').style.width = `${Math.min(volume, 100)}%`;
  document.getElementById('volumePercent').textContent = `${Math.min(volume, 100)}%`;
  
  // Update samples
  document.getElementById('samplesCollected').textContent = status.fingerprintsCollected;
  
  // Update loops
  document.getElementById('loopsDetected').textContent = loopsDetectedCount;
}

function addDetectionLog(type, message, timestamp = Date.now(), similarity = null) {
  const logContainer = document.getElementById('detectionLog');
  
  // Remove empty state
  const emptyState = logContainer.querySelector('.empty');
  if (emptyState) {
    emptyState.remove();
  }
  
  const logItem = document.createElement('div');
  logItem.className = `log-item ${type}`;
  
  const icon = type === 'loop' ? '🔁' : type === 'detection' ? '🎵' : type === 'info' ? 'ℹ️' : '❌';
  const time = new Date(timestamp).toLocaleTimeString();
  
  logItem.innerHTML = `
    <span class="log-icon">${icon}</span>
    <span class="log-text">${message}</span>
    ${similarity ? `<span class="log-similarity">${similarity}%</span>` : ''}
    <span class="log-time">${time}</span>
  `;
  
  // Add at the top
  logContainer.insertBefore(logItem, logContainer.firstChild);
  
  // Keep only last 50 logs
  const logs = logContainer.querySelectorAll('.log-item');
  if (logs.length > 50) {
    logs[logs.length - 1].remove();
  }
}

function clearDetectionLog() {
  const logContainer = document.getElementById('detectionLog');
  logContainer.innerHTML = '<div class="log-item empty"><span class="log-icon">👂</span><span class="log-text">Listening for audio...</span></div>';
}

// Initialize audio listener when page loads
setTimeout(() => {
  if (typeof AudioListener !== 'undefined') {
    initAudioListener();
  }
}, 1000);

