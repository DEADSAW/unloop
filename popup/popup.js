/**
 * Unloop - Popup JavaScript
 * Handles UI interactions and settings management
 */

// ═══════════════════════════════════════════════════════════════
// DOM ELEMENTS
// ═══════════════════════════════════════════════════════════════

const elements = {
  mainToggle: document.getElementById('mainToggle'),
  statusBanner: document.getElementById('statusBanner'),
  statusText: document.getElementById('statusText'),
  
  // Stats
  songsListened: document.getElementById('songsListened'),
  skipsCount: document.getElementById('skipsCount'),
  freshnessScore: document.getElementById('freshnessScore'),
  historyCount: document.getElementById('historyCount'),
  
  // Mode radios
  modeRadios: document.querySelectorAll('input[name="mode"]'),
  
  // Settings panels
  memoryFadeSettings: document.getElementById('memoryFadeSettings'),
  semiStrictSettings: document.getElementById('semiStrictSettings'),
  artistSmartSettings: document.getElementById('artistSmartSettings'),
  
  // Sliders
  memoryFadeDays: document.getElementById('memoryFadeDays'),
  memoryFadeValue: document.getElementById('memoryFadeValue'),
  songsBeforeRepeat: document.getElementById('songsBeforeRepeat'),
  songsBeforeRepeatValue: document.getElementById('songsBeforeRepeatValue'),
  maxArtistPerSession: document.getElementById('maxArtistPerSession'),
  maxArtistPerSessionValue: document.getElementById('maxArtistPerSessionValue'),
  
  // Buttons
  exportBtn: document.getElementById('exportBtn'),
  importBtn: document.getElementById('importBtn'),
  clearBtn: document.getElementById('clearBtn'),
  importFile: document.getElementById('importFile'),
  
  // Toast
  toast: document.getElementById('toast'),
  toastMessage: document.getElementById('toastMessage')
};

// ═══════════════════════════════════════════════════════════════
// STATE
// ═══════════════════════════════════════════════════════════════

let currentData = null;

// ═══════════════════════════════════════════════════════════════
// INITIALIZATION
// ═══════════════════════════════════════════════════════════════

document.addEventListener('DOMContentLoaded', init);

async function init() {
  await loadData();
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

// ═══════════════════════════════════════════════════════════════
// UI UPDATES
// ═══════════════════════════════════════════════════════════════

function updateUI() {
  if (!currentData) return;

  // Toggle state
  elements.mainToggle.checked = currentData.enabled;
  updateStatusBanner(currentData.enabled);

  // Stats
  elements.songsListened.textContent = formatNumber(currentData.stats.listened);
  elements.skipsCount.textContent = formatNumber(currentData.stats.skipped);
  elements.historyCount.textContent = formatNumber(currentData.historyCount);
  
  // Calculate freshness score
  const total = currentData.stats.listened + currentData.stats.skipped;
  const freshness = total > 0 
    ? Math.round((currentData.stats.listened / total) * 100) 
    : 100;
  elements.freshnessScore.textContent = freshness + '%';

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
  elements.memoryFadeDays.value = currentData.settings.memoryFadeDays;
  elements.memoryFadeValue.textContent = formatDays(currentData.settings.memoryFadeDays);
  
  elements.songsBeforeRepeat.value = currentData.settings.songsBeforeRepeat;
  elements.songsBeforeRepeatValue.textContent = currentData.settings.songsBeforeRepeat + ' songs';
  
  elements.maxArtistPerSession.value = currentData.settings.maxArtistPerSession;
  elements.maxArtistPerSessionValue.textContent = currentData.settings.maxArtistPerSession + ' songs';
}

function updateStatusBanner(enabled) {
  if (enabled) {
    elements.statusBanner.classList.remove('disabled');
    elements.statusText.textContent = 'Discovery Mode Active';
  } else {
    elements.statusBanner.classList.add('disabled');
    elements.statusText.textContent = 'Discovery Mode Paused';
  }
}

function updateModeSettings(mode) {
  // Hide all
  elements.memoryFadeSettings.style.display = 'none';
  elements.semiStrictSettings.style.display = 'none';
  elements.artistSmartSettings.style.display = 'none';

  // Show relevant
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
  elements.memoryFadeDays.addEventListener('input', handleMemoryFadeChange);
  elements.memoryFadeDays.addEventListener('change', saveSettings);
  
  elements.songsBeforeRepeat.addEventListener('input', handleSongsBeforeRepeatChange);
  elements.songsBeforeRepeat.addEventListener('change', saveSettings);
  
  elements.maxArtistPerSession.addEventListener('input', handleMaxArtistChange);
  elements.maxArtistPerSession.addEventListener('change', saveSettings);

  // Action buttons
  elements.exportBtn.addEventListener('click', handleExport);
  elements.importBtn.addEventListener('click', () => elements.importFile.click());
  elements.importFile.addEventListener('change', handleImport);
  elements.clearBtn.addEventListener('click', handleClear);
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

function handleMemoryFadeChange() {
  const days = parseInt(elements.memoryFadeDays.value);
  elements.memoryFadeValue.textContent = formatDays(days);
  currentData.settings.memoryFadeDays = days;
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
      showToast('History exported successfully', 'success');
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
          showToast(`Imported ${response.imported} songs`, 'success');
          await loadData();
          updateUI();
        } else {
          showToast('Import failed: ' + response.error, 'error');
        }
      });
    } catch (err) {
      showToast('Invalid file format', 'error');
    }
  };
  reader.readAsText(file);
  
  // Reset file input
  e.target.value = '';
}

async function handleClear() {
  if (!confirm('Are you sure you want to clear all listening history? This cannot be undone.')) {
    return;
  }

  chrome.runtime.sendMessage({ type: 'CLEAR_HISTORY' }, async (response) => {
    if (response.success) {
      showToast('History cleared', 'success');
      await loadData();
      updateUI();
    }
  });
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
