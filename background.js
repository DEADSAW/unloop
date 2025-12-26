/**
 * Unloop - Background Service Worker
 * Handles storage, messaging, and extension lifecycle
 */

// ═══════════════════════════════════════════════════════════════
// DEFAULT SETTINGS
// ═══════════════════════════════════════════════════════════════

const DEFAULT_SETTINGS = {
  mode: 'strict',
  memoryFadeHours: 72,       // Hours instead of days (72h = 3 days default)
  songsBeforeRepeat: 5,
  maxArtistPerSession: 3,
  theme: 'dark',             // dark | light | auto
  minimalMode: false         // Minimal UI toggle
};

const DEFAULT_STATS = {
  listened: 0,
  skipped: 0,
  firstInstall: Date.now(),
  totalListeningSeconds: 0
};

const DEFAULT_SESSION = {
  songs: [],
  artists: [],
  startedAt: Date.now()
};

const DEFAULT_ACHIEVEMENTS = {
  loops25: false,
  loops50: false,
  loops100: false,
  loops250: false,
  loops500: false,
  intel50: false,
  intel70: false,
  intel85: false,
  intel95: false,
  explorer500: false
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
      blacklist: [],
      achievements: DEFAULT_ACHIEVEMENTS
    });
    
    // Session storage (resets on browser restart)
    chrome.storage.session.set({
      sessionData: DEFAULT_SESSION
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

    case 'TRACK_SESSION_SONG':
      trackSessionSong(message.title, message.artist).then(sendResponse);
      return true;

    case 'GET_SESSION_DATA':
      getSessionData().then(sendResponse);
      return true;

    case 'RESET_SESSION':
      resetSession().then(sendResponse);
      return true;

    case 'CHECK_ACHIEVEMENTS':
      checkAchievements(message.stats, message.intelligence).then(sendResponse);
      return true;

    case 'EXPORT_CSV':
      exportCsv(message.songHistory).then(sendResponse);
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
        songHistory: data.songHistory || {},
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

/**
 * Normalize artist name for consistent counting
 * Fixes issues with artist discovery showing 0
 */
function normalizeArtist(name) {
  if (!name) return 'Unknown';
  
  return name
    .toLowerCase()
    .replace(/,/g, '')
    .replace(/&/g, 'and')
    .replace(/\s+/g, ' ')
    .trim();
}

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
// SESSION TRACKING FUNCTIONS
// ═══════════════════════════════════════════════════════════════

async function trackSessionSong(title, artist) {
  return new Promise((resolve) => {
    chrome.storage.session.get(['sessionSongs'], (result) => {
      let sessionSongs = result.sessionSongs || [];
      
      // Track unique songs by title|artist key
      const songKey = `${title}|${artist}`;
      if (!sessionSongs.includes(songKey)) {
        sessionSongs.push(songKey);
      }
      
      chrome.storage.session.set({ sessionSongs }, () => {
        resolve({ 
          success: true, 
          count: sessionSongs.length
        });
      });
    });
  });
}

async function getSessionData() {
  return new Promise((resolve) => {
    chrome.storage.session.get(['sessionSongs'], (result) => {
      resolve({ 
        songs: result.sessionSongs || [],
        count: (result.sessionSongs || []).length
      });
    });
  });
}

async function resetSession() {
  return new Promise((resolve) => {
    chrome.storage.session.set({ sessionSongs: [] }, () => {
      resolve({ success: true });
    });
  });
}

// ═══════════════════════════════════════════════════════════════
// ACHIEVEMENTS SYSTEM
// ═══════════════════════════════════════════════════════════════

async function checkAchievements(stats, intelligence) {
  return new Promise((resolve) => {
    chrome.storage.local.get(['achievements'], (result) => {
      const achievements = result.achievements || DEFAULT_ACHIEVEMENTS;
      const newAchievements = [];
      
      // Loop milestones
      if (!achievements.loops25 && stats.skipped >= 25) {
        achievements.loops25 = true;
        newAchievements.push({
          id: 'loops25',
          message: '🎉 Nice! You\'ve avoided 25 boring repeats',
          emoji: '🎉'
        });
      }
      
      if (!achievements.loops50 && stats.skipped >= 50) {
        achievements.loops50 = true;
        newAchievements.push({
          id: 'loops50',
          message: '🌟 Awesome! 50 repeats dodged successfully',
          emoji: '🌟'
        });
      }
      
      if (!achievements.loops100 && stats.skipped >= 100) {
        achievements.loops100 = true;
        newAchievements.push({
          id: 'loops100',
          message: '🎊 Incredible! 100 loops prevented',
          emoji: '🎊'
        });
      }
      
      if (!achievements.loops250 && stats.skipped >= 250) {
        achievements.loops250 = true;
        newAchievements.push({
          id: 'loops250',
          message: '🏆 Amazing! 250 repeats avoided – you\'re a discovery master',
          emoji: '🏆'
        });
      }
      
      if (!achievements.loops500 && stats.skipped >= 500) {
        achievements.loops500 = true;
        newAchievements.push({
          id: 'loops500',
          message: '💎 Legendary! 500 loops prevented – absolute champion',
          emoji: '💎'
        });
      }
      
      // Intelligence milestones
      if (!achievements.intel50 && intelligence >= 50) {
        achievements.intel50 = true;
        newAchievements.push({
          id: 'intel50',
          message: '🧠 I\'m starting to understand your taste',
          emoji: '🧠'
        });
      }
      
      if (!achievements.intel70 && intelligence >= 70) {
        achievements.intel70 = true;
        newAchievements.push({
          id: 'intel70',
          message: '😎 I know you pretty well now',
          emoji: '😎'
        });
      }
      
      if (!achievements.intel85 && intelligence >= 85) {
        achievements.intel85 = true;
        newAchievements.push({
          id: 'intel85',
          message: '🔥 We\'re totally in sync with your music taste',
          emoji: '🔥'
        });
      }
      
      if (!achievements.intel95 && intelligence >= 95) {
        achievements.intel95 = true;
        newAchievements.push({
          id: 'intel95',
          message: '✨ I understand you better than you understand yourself',
          emoji: '✨'
        });
      }
      
      // Save achievements
      chrome.storage.local.set({ achievements }, () => {
        resolve({ 
          success: true, 
          newAchievements,
          achievements
        });
      });
    });
  });
}

// ═══════════════════════════════════════════════════════════════
// CSV EXPORT
// ═══════════════════════════════════════════════════════════════

async function exportCsv(songHistory) {
  return new Promise((resolve) => {
    // CSV Header
    const headers = ['Platform', 'Title', 'Artist', 'Plays', 'Skips', 'First Played', 'Last Played'];
    const rows = [headers.join(',')];
    
    // Convert song history to CSV rows
    Object.keys(songHistory).forEach(videoId => {
      const song = songHistory[videoId];
      
      const escapeCsv = (str) => {
        if (!str) return '""';
        str = String(str);
        if (str.includes(',') || str.includes('"') || str.includes('\\n')) {
          return `"${str.replace(/"/g, '""')}"`;
        }
        return str;
      };
      
      const row = [
        escapeCsv(song.platform || 'Unknown'),
        escapeCsv(song.title || 'Unknown'),
        escapeCsv(song.artist || 'Unknown'),
        song.plays || 0,
        song.skips || 0,
        song.firstPlayed ? new Date(song.firstPlayed).toISOString() : '',
        song.lastPlayed ? new Date(song.lastPlayed).toISOString() : ''
      ];
      
      rows.push(row.join(','));
    });
    
    const csvContent = rows.join('\\n');
    resolve({ 
      success: true, 
      csv: csvContent,
      totalSongs: Object.keys(songHistory).length
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
