/**
 * Unloop - Content Script
 * Runs on YouTube and YouTube Music pages
 * Detects songs, checks history, auto-skips repeats
 */

(function() {
  'use strict';

  // ═══════════════════════════════════════════════════════════════
  // CONFIGURATION
  // ═══════════════════════════════════════════════════════════════
  
  const CONFIG = {
    CHECK_INTERVAL: 1000,        // How often to check for new songs (ms)
    SKIP_DELAY: 300,             // Delay before skipping (ms) - feels more natural
    DEBUG: false                  // Set to true for console logs
  };

  // ═══════════════════════════════════════════════════════════════
  // STATE
  // ═══════════════════════════════════════════════════════════════
  
  let currentVideoId = null;
  let lastCheckedId = null;
  let isEnabled = true;
  let settings = {
    mode: 'strict',              // strict, memory-fade, semi-strict, artist-smart
    memoryFadeDays: 180,         // For memory-fade mode
    songsBeforeRepeat: 5,        // For semi-strict mode
    maxArtistPerSession: 3       // For artist-smart mode
  };
  let sessionArtistCount = {};   // Track artist plays this session
  let sessionSongQueue = [];     // Track recent songs for semi-strict

  // ═══════════════════════════════════════════════════════════════
  // UTILITY FUNCTIONS
  // ═══════════════════════════════════════════════════════════════

  function log(...args) {
    if (CONFIG.DEBUG) {
      console.log('[Unloop]', ...args);
    }
  }

  function isYouTubeMusic() {
    return window.location.hostname === 'music.youtube.com';
  }

  function isYouTube() {
    return window.location.hostname === 'www.youtube.com';
  }

  // ═══════════════════════════════════════════════════════════════
  // VIDEO ID EXTRACTION
  // ═══════════════════════════════════════════════════════════════

  function getVideoId() {
    // Try URL first
    const urlParams = new URLSearchParams(window.location.search);
    let videoId = urlParams.get('v');
    
    if (videoId) return videoId;

    // For YouTube Music, check the player
    if (isYouTubeMusic()) {
      // Check URL path for /watch
      const match = window.location.pathname.match(/\/watch\/([^/?]+)/);
      if (match) return match[1];
      
      // Try to get from player
      const player = document.querySelector('ytmusic-player');
      if (player && player.__data && player.__data.playerResponse) {
        return player.__data.playerResponse.videoDetails?.videoId;
      }
    }

    return null;
  }

  // ═══════════════════════════════════════════════════════════════
  // ARTIST EXTRACTION (For Artist-Smart Mode)
  // ═══════════════════════════════════════════════════════════════

  function getArtistName() {
    if (isYouTubeMusic()) {
      // YouTube Music artist selectors
      const artistEl = document.querySelector(
        'ytmusic-player-bar .byline a, ' +
        '.ytmusic-player-bar .subtitle a, ' +
        'ytmusic-player-bar .content-info-wrapper .subtitle yt-formatted-string a'
      );
      if (artistEl) return artistEl.textContent.trim().toLowerCase();
    } else if (isYouTube()) {
      // Regular YouTube channel/artist
      const channelEl = document.querySelector(
        '#channel-name a, ' +
        'ytd-video-owner-renderer #channel-name yt-formatted-string a, ' +
        '.ytd-channel-name a'
      );
      if (channelEl) return channelEl.textContent.trim().toLowerCase();
    }
    return null;
  }

  function getSongTitle() {
    if (isYouTubeMusic()) {
      const titleEl = document.querySelector(
        'ytmusic-player-bar .title, ' +
        '.ytmusic-player-bar .content-info-wrapper .title'
      );
      if (titleEl) return titleEl.textContent.trim();
    } else if (isYouTube()) {
      const titleEl = document.querySelector(
        'h1.ytd-video-primary-info-renderer, ' +
        'h1.ytd-watch-metadata yt-formatted-string'
      );
      if (titleEl) return titleEl.textContent.trim();
    }
    return null;
  }

  // ═══════════════════════════════════════════════════════════════
  // SKIP FUNCTIONALITY
  // ═══════════════════════════════════════════════════════════════

  function skipToNext() {
    log('Skipping to next track...');

    if (isYouTubeMusic()) {
      // YouTube Music next button
      const nextBtn = document.querySelector(
        '.next-button, ' +
        'tp-yt-paper-icon-button.next-button, ' +
        '.ytmusic-player-bar tp-yt-paper-icon-button[title="Next"],' +
        'ytmusic-player-bar .next-button'
      );
      if (nextBtn) {
        nextBtn.click();
        log('Clicked YT Music next button');
        return true;
      }
    } else if (isYouTube()) {
      // Regular YouTube next button
      const nextBtn = document.querySelector(
        '.ytp-next-button, ' +
        'a.ytp-next-button'
      );
      if (nextBtn) {
        nextBtn.click();
        log('Clicked YouTube next button');
        return true;
      }
    }

    // Fallback: Try keyboard shortcut
    const video = document.querySelector('video');
    if (video) {
      const event = new KeyboardEvent('keydown', {
        key: 'N',
        code: 'KeyN',
        shiftKey: true,
        bubbles: true
      });
      document.dispatchEvent(event);
      log('Sent Shift+N keyboard shortcut');
      return true;
    }

    log('Could not find skip button');
    return false;
  }

  // ═══════════════════════════════════════════════════════════════
  // STORAGE OPERATIONS
  // ═══════════════════════════════════════════════════════════════

  async function getHistory() {
    return new Promise((resolve) => {
      chrome.storage.local.get(['songHistory', 'stats'], (result) => {
        resolve({
          history: result.songHistory || {},
          stats: result.stats || { listened: 0, skipped: 0 }
        });
      });
    });
  }

  async function saveSong(videoId, artist, title) {
    const { history, stats } = await getHistory();
    
    history[videoId] = {
      timestamp: Date.now(),
      artist: artist || 'unknown',
      title: title || 'Unknown Title',
      playCount: (history[videoId]?.playCount || 0) + 1
    };
    
    stats.listened = (stats.listened || 0) + 1;

    return new Promise((resolve) => {
      chrome.storage.local.set({ 
        songHistory: history, 
        stats: stats 
      }, resolve);
    });
  }

  async function incrementSkipCount() {
    const { stats } = await getHistory();
    stats.skipped = (stats.skipped || 0) + 1;
    
    return new Promise((resolve) => {
      chrome.storage.local.set({ stats: stats }, resolve);
    });
  }

  async function getSettings() {
    return new Promise((resolve) => {
      chrome.storage.local.get(['settings', 'enabled'], (result) => {
        if (result.settings) {
          settings = { ...settings, ...result.settings };
        }
        isEnabled = result.enabled !== false; // Default to true
        resolve({ settings, isEnabled });
      });
    });
  }

  // ═══════════════════════════════════════════════════════════════
  // MODE LOGIC
  // ═══════════════════════════════════════════════════════════════

  async function shouldSkipSong(videoId) {
    if (!isEnabled) return false;
    
    const { history } = await getHistory();
    const songData = history[videoId];
    
    if (!songData) {
      // New song - never heard before
      return false;
    }

    const artist = getArtistName();
    const daysSinceLastPlay = (Date.now() - songData.timestamp) / (1000 * 60 * 60 * 24);

    switch (settings.mode) {
      case 'strict':
        // Never play same song again
        return true;

      case 'memory-fade':
        // Allow after X days
        return daysSinceLastPlay < settings.memoryFadeDays;

      case 'semi-strict':
        // Allow if enough new songs played since
        if (sessionSongQueue.length >= settings.songsBeforeRepeat) {
          const recentSongs = sessionSongQueue.slice(-settings.songsBeforeRepeat);
          if (!recentSongs.includes(videoId)) {
            return false; // Played enough new songs, allow repeat
          }
        }
        return true;

      case 'artist-smart':
        // Prevent artist spam
        if (artist) {
          const artistPlays = sessionArtistCount[artist] || 0;
          if (artistPlays >= settings.maxArtistPerSession) {
            log(`Artist ${artist} hit limit (${artistPlays}/${settings.maxArtistPerSession})`);
            return true;
          }
        }
        // Also check if song was played recently (within session)
        return sessionSongQueue.slice(-10).includes(videoId);

      default:
        return true;
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // MAIN CHECK LOOP
  // ═══════════════════════════════════════════════════════════════

  async function checkCurrentSong() {
    await getSettings();
    
    if (!isEnabled) return;

    const videoId = getVideoId();
    if (!videoId || videoId === lastCheckedId) return;

    lastCheckedId = videoId;
    log('New song detected:', videoId);

    const shouldSkip = await shouldSkipSong(videoId);
    
    if (shouldSkip) {
      log('Song already in history, skipping...');
      await incrementSkipCount();
      
      // Small delay for smoother UX
      setTimeout(() => {
        skipToNext();
      }, CONFIG.SKIP_DELAY);
    } else {
      // New song - save it
      const artist = getArtistName();
      const title = getSongTitle();
      
      await saveSong(videoId, artist, title);
      log('Saved new song:', title, 'by', artist);

      // Update session tracking
      sessionSongQueue.push(videoId);
      if (sessionSongQueue.length > 50) {
        sessionSongQueue.shift(); // Keep last 50
      }

      if (artist) {
        sessionArtistCount[artist] = (sessionArtistCount[artist] || 0) + 1;
      }
    }

    currentVideoId = videoId;
  }

  // ═══════════════════════════════════════════════════════════════
  // MUTATION OBSERVER (Detect DOM Changes)
  // ═══════════════════════════════════════════════════════════════

  function setupObserver() {
    // Watch for URL changes (YouTube is a SPA)
    let lastUrl = location.href;
    
    const observer = new MutationObserver(() => {
      if (location.href !== lastUrl) {
        lastUrl = location.href;
        log('URL changed, checking song...');
        setTimeout(checkCurrentSong, 500);
      }
    });

    observer.observe(document.body, {
      childList: true,
      subtree: true
    });

    // Also watch for player state changes
    if (isYouTubeMusic()) {
      const playerObserver = new MutationObserver(() => {
        checkCurrentSong();
      });

      const waitForPlayer = setInterval(() => {
        const playerBar = document.querySelector('ytmusic-player-bar');
        if (playerBar) {
          clearInterval(waitForPlayer);
          playerObserver.observe(playerBar, {
            childList: true,
            subtree: true,
            attributes: true
          });
          log('Player bar observer attached');
        }
      }, 1000);
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // MESSAGE LISTENER (From Popup/Background)
  // ═══════════════════════════════════════════════════════════════

  chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    if (message.type === 'GET_STATUS') {
      sendResponse({
        enabled: isEnabled,
        currentVideoId: currentVideoId,
        mode: settings.mode
      });
    } else if (message.type === 'SETTINGS_UPDATED') {
      getSettings();
      sendResponse({ success: true });
    } else if (message.type === 'TOGGLE_ENABLED') {
      isEnabled = message.enabled;
      sendResponse({ success: true });
    }
    return true;
  });

  // ═══════════════════════════════════════════════════════════════
  // INITIALIZATION
  // ═══════════════════════════════════════════════════════════════

  async function init() {
    log('Unloop initializing on', window.location.hostname);
    
    await getSettings();
    setupObserver();
    
    // Start checking
    setInterval(checkCurrentSong, CONFIG.CHECK_INTERVAL);
    
    // Initial check
    setTimeout(checkCurrentSong, 1000);
    
    log('Unloop initialized. Mode:', settings.mode, 'Enabled:', isEnabled);
  }

  // Start when DOM is ready
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

})();
