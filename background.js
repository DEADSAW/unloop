/**
 * Unloop - Background Service Worker
 * Handles storage, messaging, and extension lifecycle
 */

// ═══════════════════════════════════════════════════════════════
// DEFAULT SETTINGS
// ═══════════════════════════════════════════════════════════════

const DEFAULT_SETTINGS = {
  mode: 'strict',
  memoryFadeDays: 180,
  songsBeforeRepeat: 5,
  maxArtistPerSession: 3
};

const DEFAULT_STATS = {
  listened: 0,
  skipped: 0,
  firstInstall: Date.now()
};

// ═══════════════════════════════════════════════════════════════
// INSTALLATION HANDLER
// ═══════════════════════════════════════════════════════════════

chrome.runtime.onInstalled.addListener((details) => {
  if (details.reason === 'install') {
    // First time install
    chrome.storage.local.set({
      enabled: true,
      settings: DEFAULT_SETTINGS,
      stats: DEFAULT_STATS,
      songHistory: {},
      whitelist: [],
      blacklist: []
    });
    
    console.log('[Unloop] Extension installed! Welcome to Discovery Mode.');
  } else if (details.reason === 'update') {
    // Extension updated - preserve data, update settings if needed
    chrome.storage.local.get(['settings'], (result) => {
      const currentSettings = result.settings || {};
      const updatedSettings = { ...DEFAULT_SETTINGS, ...currentSettings };
      chrome.storage.local.set({ settings: updatedSettings });
    });
    
    console.log('[Unloop] Extension updated to version', chrome.runtime.getManifest().version);
  }
});

// ═══════════════════════════════════════════════════════════════
// MESSAGE HANDLERS
// ═══════════════════════════════════════════════════════════════

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  switch (message.type) {
    case 'GET_ALL_DATA':
      getAllData().then(sendResponse);
      return true;

    case 'UPDATE_SETTINGS':
      updateSettings(message.settings).then(sendResponse);
      return true;

    case 'TOGGLE_ENABLED':
      toggleEnabled(message.enabled).then(sendResponse);
      return true;

    case 'CLEAR_HISTORY':
      clearHistory().then(sendResponse);
      return true;

    case 'EXPORT_DATA':
      exportData().then(sendResponse);
      return true;

    case 'IMPORT_DATA':
      importData(message.data).then(sendResponse);
      return true;

    case 'ADD_TO_WHITELIST':
      modifyList('whitelist', 'add', message.videoId).then(sendResponse);
      return true;

    case 'REMOVE_FROM_WHITELIST':
      modifyList('whitelist', 'remove', message.videoId).then(sendResponse);
      return true;

    case 'ADD_TO_BLACKLIST':
      modifyList('blacklist', 'add', message.videoId).then(sendResponse);
      return true;

    case 'REMOVE_FROM_BLACKLIST':
      modifyList('blacklist', 'remove', message.videoId).then(sendResponse);
      return true;

    default:
      sendResponse({ error: 'Unknown message type' });
      return false;
  }
});

// ═══════════════════════════════════════════════════════════════
// STORAGE FUNCTIONS
// ═══════════════════════════════════════════════════════════════

async function getAllData() {
  return new Promise((resolve) => {
    chrome.storage.local.get(null, (data) => {
      resolve({
        enabled: data.enabled !== false,
        settings: { ...DEFAULT_SETTINGS, ...data.settings },
        stats: data.stats || DEFAULT_STATS,
        historyCount: Object.keys(data.songHistory || {}).length,
        whitelist: data.whitelist || [],
        blacklist: data.blacklist || []
      });
    });
  });
}

async function updateSettings(newSettings) {
  return new Promise((resolve) => {
    chrome.storage.local.get(['settings'], (result) => {
      const settings = { ...result.settings, ...newSettings };
      chrome.storage.local.set({ settings }, () => {
        // Notify content scripts
        notifyContentScripts({ type: 'SETTINGS_UPDATED', settings });
        resolve({ success: true, settings });
      });
    });
  });
}

async function toggleEnabled(enabled) {
  return new Promise((resolve) => {
    chrome.storage.local.set({ enabled }, () => {
      // Notify content scripts
      notifyContentScripts({ type: 'TOGGLE_ENABLED', enabled });
      resolve({ success: true, enabled });
    });
  });
}

async function clearHistory() {
  return new Promise((resolve) => {
    chrome.storage.local.set({ 
      songHistory: {},
      stats: { ...DEFAULT_STATS, firstInstall: Date.now() }
    }, () => {
      resolve({ success: true });
    });
  });
}

async function exportData() {
  return new Promise((resolve) => {
    chrome.storage.local.get(null, (data) => {
      const exportPayload = {
        version: chrome.runtime.getManifest().version,
        exportDate: new Date().toISOString(),
        songHistory: data.songHistory || {},
        stats: data.stats || DEFAULT_STATS,
        settings: data.settings || DEFAULT_SETTINGS,
        whitelist: data.whitelist || [],
        blacklist: data.blacklist || []
      };
      resolve({ success: true, data: exportPayload });
    });
  });
}

async function importData(importedData) {
  return new Promise((resolve) => {
    if (!importedData || !importedData.songHistory) {
      resolve({ success: false, error: 'Invalid import data' });
      return;
    }

    chrome.storage.local.get(['songHistory', 'stats'], (currentData) => {
      // Merge histories
      const mergedHistory = {
        ...currentData.songHistory,
        ...importedData.songHistory
      };

      // Merge stats
      const mergedStats = {
        listened: (currentData.stats?.listened || 0) + (importedData.stats?.listened || 0),
        skipped: (currentData.stats?.skipped || 0) + (importedData.stats?.skipped || 0),
        firstInstall: Math.min(
          currentData.stats?.firstInstall || Date.now(),
          importedData.stats?.firstInstall || Date.now()
        )
      };

      chrome.storage.local.set({
        songHistory: mergedHistory,
        stats: mergedStats,
        whitelist: importedData.whitelist || [],
        blacklist: importedData.blacklist || []
      }, () => {
        resolve({ 
          success: true, 
          imported: Object.keys(importedData.songHistory).length,
          total: Object.keys(mergedHistory).length
        });
      });
    });
  });
}

async function modifyList(listName, action, videoId) {
  return new Promise((resolve) => {
    chrome.storage.local.get([listName], (result) => {
      let list = result[listName] || [];
      
      if (action === 'add' && !list.includes(videoId)) {
        list.push(videoId);
      } else if (action === 'remove') {
        list = list.filter(id => id !== videoId);
      }

      chrome.storage.local.set({ [listName]: list }, () => {
        resolve({ success: true, list });
      });
    });
  });
}

// ═══════════════════════════════════════════════════════════════
// HELPER FUNCTIONS
// ═══════════════════════════════════════════════════════════════

function notifyContentScripts(message) {
  chrome.tabs.query({ 
    url: ['*://www.youtube.com/*', '*://music.youtube.com/*'] 
  }, (tabs) => {
    tabs.forEach(tab => {
      chrome.tabs.sendMessage(tab.id, message).catch(() => {
        // Tab might not have content script loaded
      });
    });
  });
}

// ═══════════════════════════════════════════════════════════════
// BADGE UPDATES (Optional visual feedback)
// ═══════════════════════════════════════════════════════════════

// Update badge with skip count periodically
setInterval(async () => {
  const data = await getAllData();
  if (data.stats.skipped > 0) {
    const badgeText = data.stats.skipped > 999 ? '999+' : String(data.stats.skipped);
    chrome.action.setBadgeText({ text: badgeText });
    chrome.action.setBadgeBackgroundColor({ color: '#6366f1' });
  }
}, 30000);

console.log('[Unloop] Background service worker started');
