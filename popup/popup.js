/**
 * Unloop - Popup JavaScript
 * Handles UI interactions and settings management
 */

// ═══════════════════════════════════════════════════════════════
// DOM ELEMENTS
// ═══════════════════════════════════════════════════════════════

const elements = {
  mainToggle: document.getElementById('mainToggle'),
  
  // Emotional Status Header
  statusHeader: document.getElementById('statusHeader'),
  statusIcon: document.getElementById('statusIcon'),
  statusMode: document.getElementById('statusMode'),
  statusSubtext: document.getElementById('statusSubtext'),
  
  // Health Grid
  freshnessValue: document.getElementById('freshnessValue'),
  intelligenceValue: document.getElementById('intelligenceValue'),
  varietyValue: document.getElementById('varietyValue'),
  
  // Intelligence Badge (Smart Auto)
  intelligenceBadge: document.getElementById('intelligenceBadge'),
  badgeEmoji: document.getElementById('badgeEmoji'),
  badgeText: document.getElementById('badgeText'),
  badgeFill: document.getElementById('badgeFill'),
  
  // Recent Wins (Smart Auto)
  recentWins: document.getElementById('recentWins'),
  winsList: document.getElementById('winsList'),
  
  // Graphs Section (Smart Auto)
  graphsSection: document.getElementById('graphsSection'),
  learningGraph: document.getElementById('learningGraph'),
  
  // Stats
  songsListened: document.getElementById('songsListened'),
  skipsCount: document.getElementById('skipsCount'),
  artistsCount: document.getElementById('artistsCount'),
  historyCount: document.getElementById('historyCount'),
  sessionSongsCount: document.getElementById('sessionSongsCount'),
  totalListeningTime: document.getElementById('totalListeningTime'),
  
  // Current Song
  currentSongInfo: document.getElementById('currentSongInfo'),
  whitelistBtn: document.getElementById('whitelistBtn'),
  blacklistBtn: document.getElementById('blacklistBtn'),
  
  // Mode radios
  modeRadios: document.querySelectorAll('input[name="mode"]'),
  
  // Settings panels
  memoryFadeSettings: document.getElementById('memoryFadeSettings'),
  semiStrictSettings: document.getElementById('semiStrictSettings'),
  artistSmartSettings: document.getElementById('artistSmartSettings'),
  
  // Sliders
  memoryFadeHours: document.getElementById('memoryFadeHours'),
  memoryFadeHint: document.getElementById('memoryFadeHint'),
  songsBeforeRepeat: document.getElementById('songsBeforeRepeat'),
  songsBeforeRepeatValue: document.getElementById('songsBeforeRepeatValue'),
  maxArtistPerSession: document.getElementById('maxArtistPerSession'),
  maxArtistPerSessionValue: document.getElementById('maxArtistPerSessionValue'),
  
  // Buttons
  exportBtn: document.getElementById('exportBtn'),
  importBtn: document.getElementById('importBtn'),
  clearBtn: document.getElementById('clearBtn'),
  importFile: document.getElementById('importFile'),
  downloadCsvBtn: document.getElementById('downloadCsvBtn'),
  
  // Customization
  themeRadios: document.querySelectorAll('input[name="theme"]'),
  minimalModeToggle: document.getElementById('minimalModeToggle'),
  customizationSection: document.getElementById('customizationSection'),
  
  // Toast
  toast: document.getElementById('toast'),
  toastMessage: document.getElementById('toastMessage')
};

// ═══════════════════════════════════════════════════════════════
// STATE
// ═══════════════════════════════════════════════════════════════

let currentData = null;
let currentVideoInfo = null;

// ═══════════════════════════════════════════════════════════════
// INITIALIZATION
// ═══════════════════════════════════════════════════════════════

document.addEventListener('DOMContentLoaded', init);

async function init() {
  await loadData();
  await getCurrentSong();
  setupEventListeners();
  updateUI();
}

async function loadData() {
  return new Promise((resolve) => {
    chrome.runtime.sendMessage({ type: 'GET_ALL_DATA' }, (response) => {
      currentData = response;
      resolve();
    });
  });
}

async function getCurrentSong() {
  // Get current tab
  const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
  
  if (tab && (tab.url?.includes('youtube.com') || tab.url?.includes('music.youtube.com'))) {
    try {
      const response = await chrome.tabs.sendMessage(tab.id, { type: 'GET_CURRENT_VIDEO' });
      if (response && response.videoId) {
        currentVideoInfo = response;
      }
    } catch (e) {
      // Content script not ready
      currentVideoInfo = null;
    }
  }
}

// ═══════════════════════════════════════════════════════════════
// UI UPDATES
// ═══════════════════════════════════════════════════════════════

function updateUI() {
  if (!currentData) return;

  // Toggle state
  elements.mainToggle.checked = currentData.enabled;
  updateEmotionalStatus();

  // Health Grid
  updateHealthGrid();

  // Stats - support both old and new structures
  const historyData = currentData.history || currentData.songHistory || {};
  const uniqueSongs = Object.keys(historyData).length;
  
  // Songs Explored (unique songs)
  elements.songsListened.textContent = formatNumber(uniqueSongs);
  
  // Loops Prevented (skips count)
  const loopsCount = currentData.stats.skipped || currentData.stats.loopsPrevented || 0;
  elements.skipsCount.textContent = formatNumber(loopsCount);
  
  // Total history count
  elements.historyCount.textContent = formatNumber(uniqueSongs);
  
  // Artist count - use stats.totalArtists if available, otherwise calculate
  const artistCount = currentData.stats?.totalArtists || countUniqueArtists();
  if (elements.artistsCount) {
    elements.artistsCount.textContent = formatNumber(artistCount);
  }

  // Current song display
  updateCurrentSongDisplay();

  // Mode selection
  const currentMode = currentData.settings.mode;
  elements.modeRadios.forEach(radio => {
    radio.checked = radio.value === currentMode;
    if (radio.checked) {
      radio.closest('.mode-option').style.borderColor = 'var(--accent)';
    } else {
      radio.closest('.mode-option').style.borderColor = '';
    }
  });

  // Show/hide mode-specific settings
  updateModeSettings(currentMode);

  // Slider values
  elements.memoryFadeHours.value = currentData.settings.memoryFadeHours || 72;
  updateMemoryFadeHint(currentData.settings.memoryFadeHours || 72);
  
  elements.songsBeforeRepeat.value = currentData.settings.songsBeforeRepeat;
  elements.songsBeforeRepeatValue.textContent = currentData.settings.songsBeforeRepeat + ' songs';
  
  elements.maxArtistPerSession.value = currentData.settings.maxArtistPerSession;
  elements.maxArtistPerSessionValue.textContent = currentData.settings.maxArtistPerSession + ' songs';
}

function updateEmotionalStatus() {
  const isEnabled = currentData.enabled;
  const mode = currentData.settings.mode;
  
  if (!isEnabled) {
    elements.statusIcon.textContent = '⏸️';
    elements.statusMode.textContent = 'Unloop: OFF';
    elements.statusSubtext.textContent = 'Paused';
    elements.statusHeader.style.animation = 'none';
    return;
  }
  
  // Emotional messages based on mode
  const modeMessages = {
    'smart-auto': {
      icon: '✨',
      mode: 'Smart Auto: ON',
      subtext: "I've got this 🎧"
    },
    'strict': {
      icon: '🛡️',
      mode: 'Strict Mode: ON',
      subtext: 'No loops, just vibes'
    },
    'memory-fade': {
      icon: '⏳',
      mode: 'Memory Fade: ON',
      subtext: 'Time heals repeats'
    },
    'semi-strict': {
      icon: '⚖️',
      mode: 'Semi-Strict: ON',
      subtext: 'Balanced freshness'
    },
    'artist-smart': {
      icon: '🎭',
      mode: 'Artist Smart: ON',
      subtext: 'Keeping things interesting'
    }
  };
  
  const msg = modeMessages[mode] || modeMessages['strict'];
  elements.statusIcon.textContent = msg.icon;
  elements.statusMode.textContent = msg.mode;
  elements.statusSubtext.textContent = msg.subtext;
  elements.statusHeader.style.animation = 'heartbeatGlow 3s ease-in-out infinite';
}

function updateHealthGrid() {
  // Freshness (0-100%)
  const total = currentData.stats.listened + currentData.stats.skipped;
  const freshness = total > 0 
    ? Math.round((currentData.stats.listened / total) * 100) 
    : 100;
  elements.freshnessValue.textContent = freshness + '%';
  
  // Intelligence (0-100 from Smart Auto learning) - FIXED: Use callback
  calculateIntelligence((intelligence) => {
    elements.intelligenceValue.textContent = intelligence;
  });
  
  // Variety (artist count or "Strong"/"Growing"/"Low")
  const artistCount = countUniqueArtists();
  let varietyText;
  if (artistCount >= 30) varietyText = 'Strong';
  else if (artistCount >= 15) varietyText = 'Growing';
  else if (artistCount >= 5) varietyText = 'Fair';
  else varietyText = 'Low';
  elements.varietyValue.textContent = varietyText;
}

function calculateIntelligence(callback) {
  chrome.storage.local.get(['learningLog', 'songHistory', 'history', 'stats'], (result) => {
    // First check if we have smartScore in stats (new system)
    if (result.stats && result.stats.smartScore) {
      callback(Math.round(result.stats.smartScore));
      return;
    }
    
    // Fallback to old calculation
    const learningLog = result.learningLog || [];
    const historyData = result.history || result.songHistory || {};
    const historySize = Object.keys(historyData).length;
    
    if (learningLog.length === 0 && historySize === 0) {
      callback(0);
      return;
    }
    
    // Base score from songs learned (0-40)
    const baseScore = Math.min(historySize / 5, 40);
    
    // Event score from learning events (0-40)
    const eventScore = Math.min(
      learningLog.reduce((sum, log) => sum + (log.effectScore || 1), 0) / 10,
      40
    );
    
    // Stability bonus (0-20)
    const recent50 = learningLog.slice(-50);
    const protectionEvents = recent50.filter(log => 
      log.eventType === 'skip_protected' || log.eventType === 'artist_protected'
    ).length;
    const stabilityBonus = Math.min(protectionEvents, 20);
    
    callback(Math.round(Math.min(baseScore + eventScore + stabilityBonus, 100)));
  });
}

function countUniqueArtists() {
  // Check both old (songHistory) and new (history) data structures
  const historyData = currentData?.history || currentData?.songHistory || {};
  
  if (Object.keys(historyData).length === 0) return 0;
  
  const normalizeArtist = (name) => {
    if (!name || name === 'Unknown') return null;
    return name.toLowerCase().replace(/,/g, '').replace(/&/g, 'and').replace(/\s+/g, ' ').trim();
  };
  
  const artists = new Set();
  Object.values(historyData).forEach(song => {
    const normalized = normalizeArtist(song.artist);
    if (normalized) artists.add(normalized);
  });
  
  return artists.size;
}

function updateStatusBanner(enabled) {
  // Legacy function - replaced by updateEmotionalStatus
  updateEmotionalStatus();
}

function updateCurrentSongDisplay() {
  if (currentVideoInfo && currentVideoInfo.title) {
    elements.currentSongInfo.innerHTML = `
      <div class="current-song-title">${currentVideoInfo.title}</div>
      <div class="current-song-artist">${currentVideoInfo.artist || currentVideoInfo.channel || 'Unknown artist'}</div>
    `;
    elements.currentSongInfo.querySelector('.current-song-title')?.classList.add('active');
    elements.whitelistBtn.disabled = false;
    elements.blacklistBtn.disabled = false;
  } else {
    elements.currentSongInfo.innerHTML = '<div class="current-song-text">Open YouTube/YouTube Music to see current song</div>';
    elements.whitelistBtn.disabled = true;
    elements.blacklistBtn.disabled = true;
  }
}

function updateMemoryFadeHint(hours) {
  const days = Math.floor(hours / 24);
  const remainingHours = hours % 24;
  
  let timeText = '';
  if (days > 0 && remainingHours > 0) {
    timeText = `${days} day${days > 1 ? 's' : ''} ${remainingHours}h`;
  } else if (days > 0) {
    timeText = `${days} day${days > 1 ? 's' : ''}`;
  } else {
    timeText = `${hours} hour${hours > 1 ? 's' : ''}`;
  }
  
  elements.memoryFadeHint.textContent = `After ${timeText}, songs can be played again`;
}

function updateModeSettings(mode) {
  // Hide all
  elements.memoryFadeSettings.style.display = 'none';
  elements.semiStrictSettings.style.display = 'none';
  elements.artistSmartSettings.style.display = 'none';

  // Show/hide Smart Auto features
  const isSmartAuto = mode === 'smart-auto';
  elements.intelligenceBadge.style.display = isSmartAuto ? 'block' : 'none';
  elements.recentWins.style.display = isSmartAuto ? 'block' : 'none';
  elements.graphsSection.style.display = isSmartAuto ? 'block' : 'none';
  
  if (isSmartAuto) {
    updateIntelligenceBadge();
    updateRecentWins();
    loadGraphs();
    return;
  }

  // Show relevant settings for manual modes
  switch (mode) {
    case 'memory-fade':
      elements.memoryFadeSettings.style.display = 'block';
      break;
    case 'semi-strict':
      elements.semiStrictSettings.style.display = 'block';
      break;
    case 'artist-smart':
      elements.artistSmartSettings.style.display = 'block';
      break;
  }
}

function updateIntelligenceBadge() {
  chrome.storage.local.get(['learningLog', 'songHistory'], (result) => {
    const learningLog = result.learningLog || [];
    const historySize = Object.keys(result.songHistory || {}).length;
    
    let intelligence = 0;
    if (learningLog.length > 0 || historySize > 0) {
      const baseScore = Math.min(historySize / 5, 40);
      const eventScore = Math.min(
        learningLog.reduce((sum, log) => sum + (log.effectScore || 1), 0) / 10,
        40
      );
      const recent50 = learningLog.slice(-50);
      const protectionEvents = recent50.filter(log => 
        log.eventType === 'skip_protected' || log.eventType === 'artist_protected'
      ).length;
      const stabilityBonus = Math.min(protectionEvents, 20);
      intelligence = Math.round(Math.min(baseScore + eventScore + stabilityBonus, 100));
    }
    
    // Badge states with emotional emojis
    let emoji, text;
    if (intelligence < 25) {
      emoji = '🐣';
      text = 'Still Learning';
    } else if (intelligence < 50) {
      emoji = '🧠';
      text = 'Getting Smarter';
    } else if (intelligence < 75) {
      emoji = '😎';
      text = 'Understands You Well';
    } else {
      emoji = '🔥';
      text = 'Elite Taste Guardian';
    }
    
    elements.badgeEmoji.textContent = emoji;
    elements.badgeText.textContent = text;
    elements.badgeFill.style.width = intelligence + '%';
  });
}

function updateRecentWins() {
  chrome.storage.local.get(['recentWins', 'learningLog'], (result) => {
    // Try new recentWins structure first
    let wins = [];
    
    if (result.recentWins && result.recentWins.length > 0) {
      // New structure - direct wins array
      wins = result.recentWins.slice(0, 5).map(win => win.text);
    } else {
      // Fallback to old learningLog structure
      const learningLog = result.learningLog || [];
      const today = new Date().toDateString();
      
      // Count protections today
      const todayProtections = learningLog.filter(log => {
        const logDate = new Date(log.timestamp).toDateString();
        return logDate === today && (log.eventType === 'skip_protected' || log.eventType === 'artist_protected');
      }).length;
      
      const annoyingBlocked = learningLog.filter(log => 
        log.eventType === 'annoying_blocked'
      ).length;
      
      const artistProtected = learningLog.filter(log => 
        log.eventType === 'artist_protected'
      ).length;
      
      // Build wins list with emotional messages
      if (todayProtections > 0) {
        wins.push(`🎯 Protected you ${todayProtections} time${todayProtections > 1 ? 's' : ''} today`);
      }
      if (annoyingBlocked > 0) {
        wins.push(`🚫 Blocked ${annoyingBlocked} annoying repeat${annoyingBlocked > 1 ? 's' : ''}`);
      }
      if (artistProtected > 2) {
        wins.push(`🎭 Avoided artist spam ${artistProtected} times`);
      }
      if (learningLog.length > 10) {
        wins.push(`🌱 Learning from your taste`);
      }
    }
    
    if (wins.length === 0) {
      wins.push('🌿 Just getting started...');
    }
    
    elements.winsList.innerHTML = wins.map(win => 
      `<div class="win-item">${win}</div>`
    ).join('');
  });
}

function loadGraphs() {
  chrome.storage.local.get(['learningLog', 'songHistory'], (result) => {
    const learningLog = result.learningLog || [];
    const songHistory = result.songHistory || {};
    
    if (learningLog.length === 0) {
      // No data yet
      return;
    }
    
    // Group learning events by day
    const eventsByDay = {};
    learningLog.forEach(log => {
      const date = new Date(log.timestamp).toLocaleDateString('en-US', { 
        month: 'short', 
        day: 'numeric' 
      });
      eventsByDay[date] = (eventsByDay[date] || 0) + (log.effectScore || 1);
    });
    
    // Get last 7 days of data
    const dates = Object.keys(eventsByDay).slice(-7);
    const scores = dates.map(date => eventsByDay[date]);
    
    // Create learning growth graph
    const ctx = elements.learningGraph.getContext('2d');
    
    // Destroy existing chart if it exists
    if (window.learningChart) {
      window.learningChart.destroy();
    }
    
    window.learningChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: dates,
        datasets: [{
          label: 'Learning Activity',
          data: scores,
          borderColor: '#6366f1',
          backgroundColor: 'rgba(99, 102, 241, 0.1)',
          borderWidth: 2,
          tension: 0.4,
          fill: true,
          pointRadius: 4,
          pointHoverRadius: 6,
          pointBackgroundColor: '#6366f1',
          pointBorderColor: '#fff',
          pointBorderWidth: 2
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          },
          tooltip: {
            backgroundColor: 'rgba(0, 0, 0, 0.8)',
            padding: 12,
            titleColor: '#fff',
            bodyColor: '#fff',
            borderColor: '#6366f1',
            borderWidth: 1,
            displayColors: false
          }
        },
        scales: {
          x: {
            grid: {
              display: false
            },
            ticks: {
              color: '#888',
              font: {
                size: 10
              }
            }
          },
          y: {
            beginAtZero: true,
            grid: {
              color: 'rgba(255, 255, 255, 0.05)'
            },
            ticks: {
              color: '#888',
              font: {
                size: 10
              }
            }
          }
        }
      }
    });
  });
}

/**
 * Legacy function - kept for compatibility
 * Calculate and update Smart Auto Intelligence Score
 * Based on learning log events and history
 */
function updateIntelligenceScore() {
  chrome.storage.local.get(['learningLog', 'songHistory'], (result) => {
    const learningLog = result.learningLog || [];
    const songHistory = result.songHistory || {};
    
    // Calculate intelligence score (0-100)
    const historySize = Object.keys(songHistory).length;
    const logSize = learningLog.length;
    
    // Base score from songs learned (0-40 points)
    const baseScore = Math.min(historySize / 5, 40);
    
    // Learning events weight (0-40 points)
    const eventScore = Math.min(learningLog.reduce((sum, event) => {
      return sum + (event.effectScore || 1);
    }, 0) / 10, 40);
    
    // Stability/accuracy bonus (0-20 points)
    const recentEvents = learningLog.slice(-50);
    const protectionEvents = recentEvents.filter(e => 
      e.event === 'skip_protected' || e.event === 'artist_protected'
    ).length;
    const stabilityBonus = Math.min(protectionEvents, 20);
    
    // Total score
    let score = Math.round(baseScore + eventScore + stabilityBonus);
    score = Math.min(Math.max(score, 0), 100);
    
    // Update UI
    elements.intelligenceScore.textContent = score;
    elements.intelligenceFill.style.width = score + '%';
    
    // Animate number
    elements.intelligenceScore.classList.add('score-update');
    setTimeout(() => elements.intelligenceScore.classList.remove('score-update'), 500);
    
    // Update message based on score
    let message = '';
    if (score < 40) {
      message = 'Still learning... 🐣';
    } else if (score < 70) {
      message = 'Getting smart! 🧠';
    } else if (score < 90) {
      message = 'Really knows you 😎';
    } else {
      message = 'Elite level! 🔥';
    }
    
    elements.intelligenceMessage.textContent = message;
  });
}

// ═══════════════════════════════════════════════════════════════
// EVENT LISTENERS
// ═══════════════════════════════════════════════════════════════

function setupEventListeners() {
  // Main toggle
  elements.mainToggle.addEventListener('change', handleToggle);

  // Mode selection
  elements.modeRadios.forEach(radio => {
    radio.addEventListener('change', handleModeChange);
  });

  // Sliders
  elements.memoryFadeHours.addEventListener('input', handleMemoryFadeHoursChange);
  elements.memoryFadeHours.addEventListener('change', saveSettings);
  
  elements.songsBeforeRepeat.addEventListener('input', handleSongsBeforeRepeatChange);
  elements.songsBeforeRepeat.addEventListener('change', saveSettings);
  
  elements.maxArtistPerSession.addEventListener('input', handleMaxArtistChange);
  elements.maxArtistPerSession.addEventListener('change', saveSettings);

  // Action buttons
  elements.exportBtn.addEventListener('click', handleExport);
  elements.importBtn.addEventListener('click', () => elements.importFile.click());
  elements.importFile.addEventListener('change', handleImport);
  elements.clearBtn.addEventListener('click', handleClear);
  
  // Whitelist/Blacklist buttons
  elements.whitelistBtn.addEventListener('click', handleWhitelist);
  elements.blacklistBtn.addEventListener('click', handleBlacklist);
  
  // Memory fade preset buttons
  document.querySelectorAll('.preset-btn').forEach(btn => {
    btn.addEventListener('click', (e) => {
      const hours = parseInt(e.target.dataset.hours);
      elements.memoryFadeHours.value = hours;
      currentData.settings.memoryFadeHours = hours;
      updateMemoryFadeHint(hours);
      saveSettings();
      showToast(`Set to ${formatHours(hours)}`, 'success');
    });
  });
}

// ═══════════════════════════════════════════════════════════════
// EVENT HANDLERS
// ═══════════════════════════════════════════════════════════════

async function handleToggle() {
  const enabled = elements.mainToggle.checked;
  updateStatusBanner(enabled);
  
  chrome.runtime.sendMessage({ 
    type: 'TOGGLE_ENABLED', 
    enabled 
  }, (response) => {
    if (response.success) {
      currentData.enabled = enabled;
      showToast(enabled ? 'Discovery Mode enabled' : 'Discovery Mode paused', 'success');
    }
  });
}

async function handleModeChange(e) {
  const mode = e.target.value;
  currentData.settings.mode = mode;
  
  updateModeSettings(mode);
  
  // Update visual selection
  elements.modeRadios.forEach(radio => {
    if (radio.checked) {
      radio.closest('.mode-option').style.borderColor = 'var(--accent)';
    } else {
      radio.closest('.mode-option').style.borderColor = '';
    }
  });
  
  await saveSettings();
  showToast(`Switched to ${getModeDisplayName(mode)} mode`, 'success');
}

function handleMemoryFadeHoursChange() {
  let hours = parseInt(elements.memoryFadeHours.value);
  
  // Validate input
  if (isNaN(hours) || hours < 1) {
    hours = 1;
    elements.memoryFadeHours.value = 1;
  } else if (hours > 8760) { // Max 1 year
    hours = 8760;
    elements.memoryFadeHours.value = 8760;
  }
  
  currentData.settings.memoryFadeHours = hours;
  updateMemoryFadeHint(hours);
}

function handleSongsBeforeRepeatChange() {
  const count = parseInt(elements.songsBeforeRepeat.value);
  elements.songsBeforeRepeatValue.textContent = count + ' songs';
  currentData.settings.songsBeforeRepeat = count;
}

function handleMaxArtistChange() {
  const count = parseInt(elements.maxArtistPerSession.value);
  elements.maxArtistPerSessionValue.textContent = count + ' songs';
  currentData.settings.maxArtistPerSession = count;
}

async function saveSettings() {
  chrome.runtime.sendMessage({ 
    type: 'UPDATE_SETTINGS', 
    settings: currentData.settings 
  });
}

async function handleExport() {
  chrome.runtime.sendMessage({ type: 'EXPORT_DATA' }, (response) => {
    if (response.success) {
      const dataStr = JSON.stringify(response.data, null, 2);
      const blob = new Blob([dataStr], { type: 'application/json' });
      const url = URL.createObjectURL(blob);
      
      const a = document.createElement('a');
      a.href = url;
      a.download = `unloop-backup-${new Date().toISOString().split('T')[0]}.json`;
      a.click();
      
      URL.revokeObjectURL(url);
      showToast('✅ History backed up successfully', 'success');
    }
  });
}

async function handleImport(e) {
  const file = e.target.files[0];
  if (!file) return;

  const reader = new FileReader();
  reader.onload = (event) => {
    try {
      const data = JSON.parse(event.target.result);
      
      chrome.runtime.sendMessage({ 
        type: 'IMPORT_DATA', 
        data 
      }, async (response) => {
        if (response.success) {
          showToast(`✅ Restored ${response.imported} songs from backup`, 'success');
          await loadData();
          updateUI();
        } else {
          showToast('❌ Import failed: ' + response.error, 'error');
        }
      });
    } catch (err) {
      showToast('❌ Invalid backup file', 'error');
    }
  };
  reader.readAsText(file);
  
  // Reset file input
  e.target.value = '';
}

async function handleClear() {
  if (!confirm('Clear all history? This starts your discovery journey fresh 🌱')) {
    return;
  }

  chrome.runtime.sendMessage({ type: 'CLEAR_HISTORY' }, async (response) => {
    if (response.success) {
      showToast('✨ Fresh start! Ready to discover', 'success');
      await loadData();
      updateUI();
    }
  });
}

async function handleWhitelist() {
  // Send intent to content script - it owns the current track
  const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
  if (!tab) {
    showToast('No active tab found', 'error');
    return;
  }
  
  try {
    chrome.tabs.sendMessage(tab.id, { type: 'FAVORITE_CURRENT_SONG' });
    // Toast handled by content script
  } catch (error) {
    showToast('Could not reach page', 'error');
  }
}

async function handleBlacklist() {
  // Send intent to content script - it owns the current track
  const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
  if (!tab) {
    showToast('No active tab found', 'error');
    return;
  }
  
  try {
    chrome.tabs.sendMessage(tab.id, { type: 'BLOCK_CURRENT_SONG' });
    // Toast handled by content script
  } catch (error) {
    showToast('Could not reach page', 'error');
  }
}

// ═══════════════════════════════════════════════════════════════
// UTILITY FUNCTIONS
// ═══════════════════════════════════════════════════════════════

function formatNumber(num) {
  if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + 'M';
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K';
  }
  return String(num);
}

function formatHours(hours) {
  if (hours < 24) {
    return `${hours} hour${hours !== 1 ? 's' : ''}`;
  } else if (hours < 168) {
    const days = Math.floor(hours / 24);
    const remainingHours = hours % 24;
    if (remainingHours === 0) {
      return `${days} day${days !== 1 ? 's' : ''}`;
    }
    return `${days}d ${remainingHours}h`;
  } else if (hours < 720) {
    const weeks = Math.floor(hours / 168);
    return `${weeks} week${weeks !== 1 ? 's' : ''}`;
  } else {
    const months = Math.floor(hours / 720);
    return `${months} month${months !== 1 ? 's' : ''}`;
  }
}

function formatDays(days) {
  if (days < 14) {
    return days + ' days';
  } else if (days < 60) {
    return Math.round(days / 7) + ' weeks';
  } else if (days < 365) {
    return Math.round(days / 30) + ' months';
  } else {
    return '1 year';
  }
}

function getModeDisplayName(mode) {
  const names = {
    'strict': 'Strict',
    'memory-fade': 'Memory Fade',
    'semi-strict': 'Semi-Strict',
    'artist-smart': 'Artist Smart'
  };
  return names[mode] || mode;
}

function showToast(message, type = 'success') {
  elements.toastMessage.textContent = message;
  elements.toast.className = 'toast ' + type;
  elements.toast.classList.add('show');
  
  setTimeout(() => {
    elements.toast.classList.remove('show');
  }, 2500);
}

// ═══════════════════════════════════════════════════════════════
// NEW FEATURES
// ═══════════════════════════════════════════════════════════════

// Session Tracking
async function updateSessionData() {
  chrome.runtime.sendMessage({ type: 'GET_SESSION_DATA' }, (session) => {
    if (session && elements.sessionSongsCount) {
      elements.sessionSongsCount.textContent = formatNumber(session.count || 0);
    }
  });
}

// CSV Export
async function downloadCsv() {
  if (!currentData || !currentData.songHistory || Object.keys(currentData.songHistory).length === 0) {
    showToast('❌ No songs in history to export', 'error');
    return;
  }
  
  chrome.runtime.sendMessage(
    { type: 'EXPORT_CSV', songHistory: currentData.songHistory },
    (response) => {
      if (response && response.success) {
        // Create download
        const blob = new Blob([response.csv], { type: 'text/csv;charset=utf-8;' });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `unloop-history-${new Date().toISOString().split('T')[0]}.csv`;
        link.click();
        URL.revokeObjectURL(url);
        showToast(`📊 Exported ${response.totalSongs} songs to CSV`, 'success');
      } else {
        showToast('❌ Export failed', 'error');
      }
    }
  );
}

// Achievements Check
function checkAchievements() {
  if (!currentData) return;
  
  calculateIntelligence((intelligence) => {
    chrome.runtime.sendMessage({
      type: 'CHECK_ACHIEVEMENTS',
      stats: currentData.stats,
      intelligence: intelligence
    }, (response) => {
      if (response && response.newAchievements && response.newAchievements.length > 0) {
        // Show achievement toasts
        response.newAchievements.forEach((achievement, index) => {
          setTimeout(() => {
            showToast(achievement.message, 'success');
          }, index * 3000); // Stagger by 3 seconds
        });
      }
    });
  });
}

// Theme Management
function applyTheme(theme) {
  if (theme === 'light') {
    document.body.classList.add('light');
  } else {
    document.body.classList.remove('light');
  }
  
  // Save to settings
  if (currentData && currentData.settings) {
    currentData.settings.theme = theme;
    chrome.runtime.sendMessage({
      type: 'UPDATE_SETTINGS',
      settings: currentData.settings
    });
  }
}

// Minimal Mode
function toggleMinimalMode(enabled) {
  const elementsToHide = [
    elements.intelligenceBadge,
    elements.recentWins,
    elements.graphsSection
    // NOTE: customizationSection intentionally NOT hidden so toggle stays accessible!
  ];
  
  elementsToHide.forEach(el => {
    if (el) {
      el.style.display = enabled ? 'none' : '';
    }
  });
  
  // Simplify health grid
  if (elements.healthGrid) {
    elements.healthGrid.style.display = enabled ? 'none' : '';
  }
  
  // Save to settings
  if (currentData && currentData.settings) {
    currentData.settings.minimalMode = enabled;
    chrome.runtime.sendMessage({
      type: 'UPDATE_SETTINGS',
      settings: currentData.settings
    });
  }
}

// Fix Artist Counting with Normalization
function countUniqueArtists(songHistory) {
  if (!songHistory) return 0;
  
  const normalizeArtist = (name) => {
    if (!name) return 'unknown';
    return name
      .toLowerCase()
      .replace(/,/g, '')
      .replace(/&/g, 'and')
      .replace(/\s+/g, ' ')
      .trim();
  };
  
  const artistSet = new Set();
  Object.values(songHistory).forEach(song => {
    if (song.artist) {
      const normalized = normalizeArtist(song.artist);
      artistSet.add(normalized);
    }
  });
  
  return artistSet.size;
}

// Update the original updateUI to use the new counting
async function updateUIWithEnhancements() {
  // Reload fresh data
  await loadData();
  
  await updateSessionData();
  checkAchievements();
  loadListeningTime();
  
  // Fix artist count display with fresh data
  if (currentData && currentData.songHistory) {
    const artistCount = countUniqueArtists();
    if (elements.artistsCount) {
      elements.artistsCount.textContent = formatNumber(artistCount);
    }
  }
  
  // Apply theme
  if (currentData && currentData.settings && currentData.settings.theme) {
    applyTheme(currentData.settings.theme);
    const themeRadio = document.querySelector(`input[name="theme"][value="${currentData.settings.theme}"]`);
    if (themeRadio) themeRadio.checked = true;
  }
  
  // Apply minimal mode
  if (currentData && currentData.settings && currentData.settings.minimalMode) {
    toggleMinimalMode(true);
    if (elements.minimalModeToggle) elements.minimalModeToggle.checked = true;
  }
}

// Add event listeners for new features
function setupEnhancedEventListeners() {
  // CSV Export
  if (elements.downloadCsvBtn) {
    elements.downloadCsvBtn.addEventListener('click', downloadCsv);
  }
  
  // Theme selector
  if (elements.themeRadios) {
    elements.themeRadios.forEach(radio => {
      radio.addEventListener('change', (e) => {
        applyTheme(e.target.value);
      });
    });
  }
  
  // Minimal mode toggle
  if (elements.minimalModeToggle) {
    elements.minimalModeToggle.addEventListener('change', (e) => {
      toggleMinimalMode(e.target.checked);
    });
  }
}

// Format time duration
function formatTime(seconds) {
  const h = Math.floor(seconds / 3600);
  const m = Math.floor((seconds % 3600) / 60);
  const s = seconds % 60;

  if (h > 0) return `${h}h ${m}m`;
  if (m > 0) return `${m}m ${s}s`;
  return `${s}s`;
}

// Load and display total listening time
async function loadListeningTime() {
  if (currentData && currentData.stats) {
    const totalSeconds = currentData.stats.totalListeningSeconds || 0;
    if (elements.totalListeningTime) {
      elements.totalListeningTime.textContent = totalSeconds > 0 ? formatTime(totalSeconds) : '--';
    }
  }
}

// Call enhanced functions on init
document.addEventListener('DOMContentLoaded', () => {
  setupEnhancedEventListeners();
  setTimeout(updateUIWithEnhancements, 1000);
  setTimeout(loadListeningTime, 500);
});
