/**
 * ═══════════════════════════════════════════════════════════════════════════
 * UNLOOP - CONTENT SCRIPT (MAIN ENGINE)
 * ═══════════════════════════════════════════════════════════════════════════
 * 
 * This is the brain of the extension. It:
 * 1) Detects when YouTube/YouTube Music/Spotify song changes
 * 2) Extracts videoId + artist info
 * 3) Checks whitelist → if yes → PLAY
 * 4) Checks blacklist → if yes → SKIP
 * 5) Checks history and applies mode rules:
 *    - Strict: Skip if ever played
 *    - Memory Fade: Skip if played within X days
 *    - Semi-Strict: Skip if not enough new songs since last play
 *    - Artist Smart: Skip if same artist played too recently
 * 6) Shows toast notifications
 * 7) Updates statistics
 * 
 * DATA STRUCTURE:
 * songHistory[trackId] = {
 *   platform: "Spotify" | "YouTube Music" | "YouTube",
 *   title: string,
 *   artist: string,
 *   totalPlays: number,
 *   totalSkips: number,
 *   totalListeningSeconds: number,
 *   firstPlayed: timestamp,
 *   lastPlayed: timestamp,
 *   plays: number,  // For CSV export
 *   skips: number,  // For CSV export
 *   avgListenDuration: number,
 *   quickSkipCount: number
 * }
 * 
 * All data stored in chrome.storage.local (private, offline, free)
 * Session data in chrome.storage.session (resets on browser close)
 * ═══════════════════════════════════════════════════════════════════════════
 */

(function() {
  'use strict';

  // ═══════════════════════════════════════════════════════════════
  // EXTENSION CONTEXT GUARDS (Crash Protection)
  // ═══════════════════════════════════════════════════════════════

  /**
   * Fast check if extension context is still valid
   * Prevents "Extension context invalidated" errors
   */
  function isExtensionAlive() {
    return !!chrome?.runtime?.id;
  }

  /**
   * Session management for SPA navigation safety
   * Prevents stale async operations from completing after page changes
   */
  let songProcessToken = 0;
  let sessionKilled = false;

  function killSession() {
    sessionKilled = true;
    songProcessToken++;
    console.debug('[Unloop] Session refreshed - cancelling old operations');
  }

  // ═══════════════════════════════════════════════════════════════
  // CONFIGURATION
  // ═══════════════════════════════════════════════════════════════
  
  const CONFIG = {
    CHECK_INTERVAL: 1000,         // How often to check for song changes (ms)
    SKIP_DELAY: 400,              // Delay before skipping (feels more natural)
    TOAST_DURATION: 2500,         // How long toast shows (ms)
    DEBUG: true                   // Console logging
  };

  // ═══════════════════════════════════════════════════════════════
  // STATE MANAGEMENT
  // ═══════════════════════════════════════════════════════════════
  
  const state = {
    currentVideoId: null,
    lastProcessedId: null,
    isProcessing: false,
    enabled: true,
    currentStartTime: null,
    currentTrackId: null,
    settings: {
      mode: 'strict',
      memoryFadeHours: 72,      // Use hours for precise control
      songsBeforeRepeat: 5,
      maxArtistPerSession: 3
    },
    whitelist: [],
    blacklist: [],
    sessionData: {
      recentSongs: [],        // Last N songs this session
      artistPlayCount: {},    // Artist -> play count this session
      newSongsSinceRepeat: 0, // Counter for semi-strict mode
      recentArtists: []       // Last 20 artists for Smart Auto
    },
    // Smart Auto listening behavior tracking
    listening: {
      startTime: null,
      currentVideoId: null,
      duration: 0
    }
  };

  // ═══════════════════════════════════════════════════════════════
  // EXTENSION CONTEXT VALIDATION
  // ═══════════════════════════════════════════════════════════════

  /**
   * Quick context check - faster than isExtensionValid()
   * Use this in hot paths and before async completions
   */
  function isContextAlive() {
    return !!chrome?.runtime?.id;
  }

  function isExtensionValid() {
    // Check if extension context is still alive
    return !!(chrome && chrome.runtime && chrome.runtime.id);
  }

  function isContextInvalidatedError(error) {
    return error && error.message && 
           (error.message.includes('Extension context invalidated') ||
            error.message.includes('Extension context'));
  }

  // Session cancellation tracking for navigation safety
  // (Consolidated - single instance only)
  // Token is already defined at top with isExtensionAlive guards

  /**
   * Safely call Chrome APIs with fallback on context invalidation
   * This prevents crashes when YouTube Music navigation kills the extension context
   */
  function safeChrome(fn, fallback = null) {
    try {
      if (!isExtensionValid()) {
        console.warn('[Unloop] Extension context lost before API call');
        return fallback;
      }
      return fn();
    } catch (error) {
      if (isContextInvalidatedError(error)) {
        console.warn('[Unloop] Extension context invalidated during API call, returning fallback');
        return fallback;
      }
      // Re-throw unexpected errors
      throw error;
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // LOGGING
  // ═══════════════════════════════════════════════════════════════

  function log(...args) {
    if (CONFIG.DEBUG && isExtensionValid()) {
      console.log('%c[Unloop]', 'color: #6366f1; font-weight: bold;', ...args);
    }
  }

  function logSkip(reason) {
    if (CONFIG.DEBUG && isExtensionValid()) {
      console.log('%c[Unloop] ⏭️ SKIP:', 'color: #ef4444; font-weight: bold;', reason);
    }
  }

  function logAllow(reason) {
    if (CONFIG.DEBUG && isExtensionValid()) {
      console.log('%c[Unloop] ✅ ALLOW:', 'color: #22c55e; font-weight: bold;', reason);
    }
  }

  // Debug function to show current mode state
  function logModeState() {
    if (CONFIG.DEBUG && isExtensionValid()) {
      console.log('%c[Unloop] 📊 MODE STATE:', 'color: #a855f7; font-weight: bold;', {
        mode: state.settings.mode,
        newSongsSinceRepeat: state.sessionData.newSongsSinceRepeat,
        recentSongs: state.sessionData.recentSongs.length,
        recentArtists: state.sessionData.recentArtists,
        artistPlayCount: state.sessionData.artistPlayCount
      });
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // LISTENING TIME TRACKING
  // ═══════════════════════════════════════════════════════════════

  async function commitListeningTime(trackId) {
    if (!trackId || !state.currentStartTime) return;
    
    const seconds = Math.floor((Date.now() - state.currentStartTime) / 1000);
    if (seconds < 3) return; // Ignore very short plays
    
    try {
      const result = await chrome.storage.local.get(['songHistory', 'stats']);
      const history = result.songHistory || {};
      const stats = result.stats || {};
      
      if (history[trackId]) {
        // Update song's listening time
        history[trackId].totalListeningSeconds = 
          (history[trackId].totalListeningSeconds || 0) + seconds;
        
        // Update global total
        stats.totalListeningSeconds = (stats.totalListeningSeconds || 0) + seconds;
        
        await chrome.storage.local.set({ songHistory: history, stats });
      }
    } catch (error) {
      console.debug('[Unloop] Error committing listening time:', error);
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // PLATFORM DETECTION
  // ═══════════════════════════════════════════════════════════════

  function detectPlatform() {
    const hostname = window.location.hostname;
    if (hostname.includes('open.spotify.com')) return 'Spotify';
    if (hostname.includes('music.youtube.com')) return 'YouTube Music';
    if (hostname.includes('youtube.com')) return 'YouTube';
    return 'Unknown';
  }

  function logSkip(reason) {
    console.log('%c[Unloop] ⏭️ SKIP:', 'color: #ef4444; font-weight: bold;', reason);
  }

  function logAllow(reason) {
    console.log('%c[Unloop] ✅ ALLOW:', 'color: #22c55e; font-weight: bold;', reason);
  }

  // ═══════════════════════════════════════════════════════════════
  // SPOTIFY-SPECIFIC DETECTION (2025 Working Selectors)
  // ═══════════════════════════════════════════════════════════════

  /**
   * Extract Spotify track information
   * Uses stable data-testid selectors that work in 2025
   */
  function getSpotifyTrack() {
    if (!isSpotify()) return null;
    
    try {
      const title = document.querySelector('[data-testid="context-item-info-title"]')?.innerText;
      const artist = document.querySelector('[data-testid="context-item-info-artist"]')?.innerText;

      const link = document.querySelector('[data-testid="context-item-info-title"] a');
      const trackId = link?.href?.split("/")?.pop()?.split("?")[0];

      if (!title || !artist || !trackId) return null;

      return { 
        trackId: `spotify:${trackId}`, 
        title: title.trim(), 
        artist: artist.trim() 
      };
    } catch (e) {
      log('Spotify track detection error:', e);
      return null;
    }
  }

  /**
   * Start Spotify song change watcher
   * Uses MutationObserver on now-playing widget
   */
  function startSpotifyWatcher() {
    log('[Spotify] Starting watcher...');

    const player = document.querySelector('[data-testid="now-playing-widget"]');
    if (!player) {
      log('[Spotify] Player not ready, retrying in 1s...');
      setTimeout(startSpotifyWatcher, 1000);
      return;
    }

    let lastTrack = null;

    const observer = new MutationObserver(() => {
      if (!isExtensionAlive() || sessionKilled) return;
      
      const info = getSpotifyTrack();
      if (!info) return;

      if (lastTrack !== info.trackId) {
        lastTrack = info.trackId;
        log('[Spotify] Song changed:', info);
        
        // Update state and trigger processing
        state.currentVideoId = info.trackId;
        processSong();
      }
    });

    observer.observe(player, { 
      childList: true, 
      subtree: true,
      attributes: true 
    });
    
    log('[Spotify] Watcher attached successfully');
  }

  /**
   * Skip current track on Spotify
   */
  function spotifySkip() {
    log('[Spotify] Skipping track...');
    const skipBtn = document.querySelector('[data-testid="control-button-skip-forward"]');
    if (skipBtn) {
      skipBtn.click();
      return true;
    }
    log('[Spotify] Skip button not found');
    return false;
  }

  // ═══════════════════════════════════════════════════════════════
  // SPOTIFY-SPECIFIC DETECTION (2025 Working Selectors)
  // ═══════════════════════════════════════════════════════════════

  /**
   * Extract Spotify track information
   * Uses stable data-testid selectors that work in 2025
   */
  function getSpotifyTrack() {
    if (!isSpotify()) return null;
    
    try {
      const title = document.querySelector('[data-testid="context-item-info-title"]')?.innerText;
      const artist = document.querySelector('[data-testid="context-item-info-artist"]')?.innerText;

      const link = document.querySelector('[data-testid="context-item-info-title"] a');
      const trackId = link?.href?.split("/")?.pop()?.split("?")[0];

      if (!title || !artist || !trackId) return null;

      return { 
        trackId: `spotify:${trackId}`, 
        title: title.trim(), 
        artist: artist.trim() 
      };
    } catch (e) {
      log('Spotify track detection error:', e);
      return null;
    }
  }

  /**
   * Start Spotify song change watcher
   * Uses MutationObserver on now-playing widget
   */
  function startSpotifyWatcher() {
    log('[Spotify] Starting watcher...');

    const player = document.querySelector('[data-testid="now-playing-widget"]');
    if (!player) {
      log('[Spotify] Player not ready, retrying in 1s...');
      setTimeout(startSpotifyWatcher, 1000);
      return;
    }

    let lastTrack = null;

    const observer = new MutationObserver(() => {
      if (!isExtensionAlive() || sessionKilled) return;
      
      const info = getSpotifyTrack();
      if (!info) return;

      if (lastTrack !== info.trackId) {
        lastTrack = info.trackId;
        log('[Spotify] Song changed:', info);
        
        // Update state and trigger processing
        state.currentVideoId = info.trackId;
        processSong();
      }
    });

    observer.observe(player, { 
      childList: true, 
      subtree: true,
      attributes: true 
    });
    
    log('[Spotify] Watcher attached successfully ✓');
  }

  // ═══════════════════════════════════════════════════════════════
  // PLATFORM DETECTION
  // ═══════════════════════════════════════════════════════════════

  function isYouTubeMusic() {
    return window.location.hostname === 'music.youtube.com';
  }

  function isYouTube() {
    return window.location.hostname === 'www.youtube.com';
  }

  function isSpotify() {
    return window.location.hostname === 'open.spotify.com';
  }

  function getPlatform() {
    if (isSpotify()) return 'Spotify Web';
    return isYouTubeMusic() ? 'YouTube Music' : 'YouTube';
  }

  // ═══════════════════════════════════════════════════════════════
  // VIDEO ID EXTRACTION
  // ═══════════════════════════════════════════════════════════════

  function getVideoId() {
    // Spotify: Use dedicated track getter
    if (isSpotify()) {
      const track = getSpotifyTrack();
      return track?.trackId || null;
    }
    
    // YouTube/YouTube Music: Extract video ID
    // Method 1: URL parameter (works for both platforms)
    const urlParams = new URLSearchParams(window.location.search);
    const videoId = urlParams.get('v');
    if (videoId) return videoId;

    // Method 2: YouTube Music player data
    if (isYouTubeMusic()) {
      try {
        const player = document.querySelector('ytmusic-player-bar');
        if (player && player.__data?.playerResponse?.videoDetails?.videoId) {
          return player.__data.playerResponse.videoDetails.videoId;
        }
      } catch (e) {}
    }

    return null;
  }

  // ═══════════════════════════════════════════════════════════════
  // SONG METADATA EXTRACTION
  // ═══════════════════════════════════════════════════════════════

  function getSongInfo() {
    let title = null;
    let artist = null;
    let channel = null;

    if (isSpotify()) {
      // Use dedicated Spotify track getter
      const track = getSpotifyTrack();
      if (track) {
        title = track.title;
        artist = track.artist;
      }
    } else if (isYouTubeMusic()) {
      // YouTube Music selectors
      const titleEl = document.querySelector('.title.ytmusic-player-bar');
      const artistEl = document.querySelector('.byline.ytmusic-player-bar a') ||
                       document.querySelector('.subtitle.ytmusic-player-bar yt-formatted-string a');
      
      title = titleEl?.textContent?.trim();
      artist = artistEl?.textContent?.trim();
    } else {
      // Regular YouTube selectors
      const titleEl = document.querySelector('h1.ytd-video-primary-info-renderer') ||
                      document.querySelector('h1.ytd-watch-metadata yt-formatted-string');
      const channelEl = document.querySelector('#channel-name a') ||
                        document.querySelector('ytd-video-owner-renderer #channel-name yt-formatted-string a');
      
      title = titleEl?.textContent?.trim();
      channel = channelEl?.textContent?.trim();
      artist = channel; // Use channel as artist for regular YouTube
    }

    return { title, artist, channel };
  }

  // ═══════════════════════════════════════════════════════════════
  // TOAST NOTIFICATIONS
  // ═══════════════════════════════════════════════════════════════

  function showToast(message, type = 'info', icon = '🎵') {
    // Remove existing toast
    const existing = document.querySelector('.unloop-toast');
    if (existing) existing.remove();

    // Create toast element
    const toast = document.createElement('div');
    toast.className = `unloop-toast ${type}`;
    toast.innerHTML = `
      <span class="unloop-toast-icon">${icon}</span>
      <span class="unloop-toast-message">${message}</span>
      <span class="unloop-toast-badge">Unloop</span>
      <div class="unloop-toast-progress">
        <div class="unloop-toast-progress-bar"></div>
      </div>
    `;

    document.body.appendChild(toast);

    // Trigger animation
    requestAnimationFrame(() => {
      toast.classList.add('show');
    });

    // Auto remove
    setTimeout(() => {
      toast.classList.remove('show');
      setTimeout(() => toast.remove(), 400);
    }, CONFIG.TOAST_DURATION);
  }

  // ═══════════════════════════════════════════════════════════════
  // SKIP FUNCTIONALITY
  // ═══════════════════════════════════════════════════════════════

  function skipToNext() {
    log('Attempting to skip to next track...');

    let nextButton = null;

    if (isSpotify()) {
      // Spotify Web skip button
      nextButton = document.querySelector('[data-testid="control-button-skip-forward"]') ||
                   document.querySelector('button[aria-label*="Next"]');
      
      if (nextButton) {
        nextButton.click();
        log('✅ Clicked Spotify next button');
        return true;
      }
      
      // Fallback: MediaSession API
      if ('mediaSession' in navigator && navigator.mediaSession.setActionHandler) {
        try {
          navigator.mediaSession.setActionHandler('nexttrack', () => {});
          log('✅ Triggered MediaSession next');
          return true;
        } catch (e) {}
      }
    } else if (isYouTubeMusic()) {
      // YouTube Music next button selectors
      nextButton = document.querySelector('.next-button') ||
                   document.querySelector('tp-yt-paper-icon-button.next-button') ||
                   document.querySelector('[aria-label="Next"]') ||
                   document.querySelector('.ytmusic-player-bar .next-button');
    } else {
      // Regular YouTube next button
      nextButton = document.querySelector('.ytp-next-button') ||
                   document.querySelector('a.ytp-next-button');
    }

    if (nextButton) {
      nextButton.click();
      log('✅ Clicked next button');
      return true;
    }

    // Fallback: keyboard shortcut (Shift+N)
    log('⚠️ No button found, trying keyboard shortcut...');
    document.dispatchEvent(new KeyboardEvent('keydown', {
      key: 'N',
      code: 'KeyN',
      shiftKey: true,
      bubbles: true
    }));
    return true;
  }

  // ═══════════════════════════════════════════════════════════════
  // STORAGE OPERATIONS
  // ═══════════════════════════════════════════════════════════════

  async function loadSettings() {
    if (!isExtensionValid()) return;
    
    try {
      return new Promise((resolve) => {
        safeChrome(
          () => chrome.storage.local.get(['enabled', 'settings', 'whitelist', 'blacklist'], (result) => {
            if (chrome.runtime.lastError) {
              log('Storage error:', chrome.runtime.lastError);
              resolve();
              return;
            }
            state.enabled = result.enabled !== false;
            state.settings = { ...state.settings, ...result.settings };
            state.whitelist = result.whitelist || [];
            state.blacklist = result.blacklist || [];
            resolve();
          }),
          null
        );
        // If safeChrome returns null (context invalidated), resolve anyway
        setTimeout(() => resolve(), 100);
      });
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error('[Unloop] Settings load error:', error);
      }
    }
  }

  async function getHistory() {
    if (!isExtensionValid()) return {};
    
    try {
      return new Promise((resolve) => {
        const result = safeChrome(
          () => chrome.storage.local.get(['songHistory'], (result) => {
            if (chrome.runtime.lastError) {
              resolve({});
              return;
            }
            resolve(result.songHistory || {});
          }),
          {}
        );
        if (result === null) resolve({});
      });
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error('[Unloop] History fetch error:', error);
      }
      return {};
    }
  }

  async function saveSong(videoId, songInfo, listenDuration = 0) {
    if (!isExtensionValid()) return;
    
    try {
      const history = await getHistory();
      const existing = history[videoId];
      
      // Calculate Smart Auto learning metrics
      const totalPlays = (existing?.totalPlays || 0) + 1;
      const totalSkips = existing?.totalSkips || 0;
      const prevAvgDuration = existing?.avgListenDuration || 0;
      
      // Weighted moving average for listen duration
      const newAvgDuration = prevAvgDuration > 0 
        ? (prevAvgDuration * 0.7 + listenDuration * 0.3)
        : listenDuration;
      
      history[videoId] = {
        timestamp: Date.now(),
        lastPlayed: Date.now(),
        firstPlayed: existing?.firstPlayed || Date.now(),
        title: songInfo.title || 'Unknown',
        artist: songInfo.artist || 'Unknown',
        platform: songInfo.platform || detectPlatform(),
        playCount: (existing?.playCount || 0) + 1,
        plays: totalPlays,  // For CSV export
        skips: totalSkips,  // For CSV export
        totalListeningSeconds: existing?.totalListeningSeconds || 0,  // For listening time tracking
        // Smart Auto learning data
        totalPlays: totalPlays,
        totalSkips: totalSkips,
        avgListenDuration: newAvgDuration,
        quickSkipCount: existing?.quickSkipCount || 0,  // Skips within 20 seconds
        lastListenDuration: listenDuration
      };

      // Update stats
      return new Promise((resolve) => {
        if (!isExtensionValid()) {
          resolve();
          return;
        }
        
        safeChrome(
          () => chrome.storage.local.get(['stats'], (result) => {
            if (chrome.runtime.lastError || !isExtensionValid()) {
              resolve();
              return;
            }
            
            const stats = result.stats || { listened: 0, skipped: 0 };
            stats.listened = (stats.listened || 0) + 1;
            
            safeChrome(
              () => chrome.storage.local.set({ 
                songHistory: history,
                stats: stats
              }, () => {
                if (chrome.runtime.lastError) {
                  log('Save error:', chrome.runtime.lastError);
                }
                resolve();
              }),
              null
            );
          }),
          null
        );
      });
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error('[Unloop] Save song error:', error);
      }
    }
  }

  /**
   * Update song's lastPlayed timestamp for allowed repeats
   * Used by memory-fade, semi-strict, artist-smart when they allow a repeat
   */
  async function updateSongPlayed(videoId, songInfo) {
    if (!isExtensionValid()) return;
    
    try {
      const history = await getHistory();
      const existing = history[videoId];
      
      if (!existing) {
        // Song doesn't exist, treat as new
        return saveSong(videoId, songInfo);
      }
      
      // Update lastPlayed and increment play count
      history[videoId] = {
        ...existing,
        lastPlayed: Date.now(),
        playCount: (existing.playCount || 0) + 1,
        plays: (existing.plays || 0) + 1,
        totalPlays: (existing.totalPlays || 0) + 1
      };
      
      return new Promise((resolve) => {
        if (!isExtensionValid()) {
          resolve();
          return;
        }
        
        safeChrome(
          () => chrome.storage.local.set({ songHistory: history }, () => {
            if (chrome.runtime.lastError) {
              log('Update song error:', chrome.runtime.lastError);
            }
            log(`📊 Updated "${songInfo.title}" - plays: ${history[videoId].totalPlays}`);
            resolve();
          }),
          null
        );
      });
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error('[Unloop] Update song error:', error);
      }
    }
  }

  async function incrementSkipCount() {
    if (!isExtensionValid()) return;
    
    try {
      return new Promise((resolve) => {
        safeChrome(
          () => chrome.storage.local.get(['stats'], (result) => {
            if (chrome.runtime.lastError || !isExtensionValid()) {
              resolve();
              return;
            }
            
            const stats = result.stats || { listened: 0, skipped: 0 };
            stats.skipped = (stats.skipped || 0) + 1;
            
            safeChrome(
              () => chrome.storage.local.set({ stats }, () => {
                if (chrome.runtime.lastError) {
                  log('Skip count error:', chrome.runtime.lastError);
                }
                resolve();
              }),
              null
            );
          }),
          null
        );
      });
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error('[Unloop] Skip count error:', error);
      }
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // CORE DATA ENGINE - FIXES ALL STORAGE ISSUES
  // ═══════════════════════════════════════════════════════════════

  /**
   * ✅ CORE FIX: Update song state consistently
   * This function is called EVERY time a song is detected
   * Fixes: Artist Discovery, Stats, Smart Learning, Everything
   */
  async function updateSongState(platform, trackId, title, artist) {
    if (!isContextAlive() || sessionKilled) return;
    
    try {
      const result = await chrome.storage.local.get(['history', 'stats']);
      const history = result.history || {};
      const stats = result.stats || {
        totalUniqueSongs: 0,
        totalArtists: 0,
        totalListeningSeconds: 0,
        loopsPrevented: 0,
        smartScore: 50
      };

      // Initialize or update song record
      if (!history[trackId]) {
        history[trackId] = {
          platform: platform,
          title: title,
          artist: artist,
          plays: 0,
          skips: 0,
          listeningSeconds: 0,
          firstPlayed: Date.now(),
          lastPlayed: Date.now()
        };
      }

      // Increment play count
      history[trackId].plays++;
      history[trackId].lastPlayed = Date.now();

      // Update stats
      stats.totalUniqueSongs = Object.keys(history).length;

      // Calculate unique artists (with normalization)
      const normalizeArtist = (name) => {
        if (!name || name === 'Unknown') return null;
        return name.toLowerCase().replace(/,/g, '').replace(/&/g, 'and').replace(/\s+/g, ' ').trim();
      };
      
      const artistSet = new Set();
      Object.values(history).forEach(song => {
        const normalized = normalizeArtist(song.artist);
        if (normalized) artistSet.add(normalized);
      });
      stats.totalArtists = artistSet.size;

      // Save to storage
      await chrome.storage.local.set({ history, stats });
      
      log(`✅ Data updated: ${stats.totalUniqueSongs} songs, ${stats.totalArtists} artists`);
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error('[Unloop] updateSongState error:', error);
      }
    }
  }

  /**
   * ✅ Add win to recent wins log
   * Called when: loop prevented, smart decision, unique unlock
   */
  async function addWin(text) {
    if (!isContextAlive() || sessionKilled) return;
    
    try {
      const result = await chrome.storage.local.get(['recentWins']);
      const recentWins = result.recentWins || [];
      
      recentWins.unshift({
        text: text,
        time: Date.now()
      });
      
      // Keep only last 10 wins
      await chrome.storage.local.set({ 
        recentWins: recentWins.slice(0, 10) 
      });
      
      log(`🏆 Win logged: ${text}`);
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error('[Unloop] addWin error:', error);
      }
    }
  }

  /**
   * ✅ Adjust smart learning score
   * good = true: user liked our decision
   * good = false: user didn't like our decision
   */
  async function adjustSmartScore(good) {
    if (!isContextAlive() || sessionKilled) return;
    
    try {
      const result = await chrome.storage.local.get(['stats']);
      const stats = result.stats || { smartScore: 50 };
      
      stats.smartScore = stats.smartScore || 50;
      
      if (good) {
        stats.smartScore += 2;
        log('📈 Smart score +2');
      } else {
        stats.smartScore -= 1;
        log('📉 Smart score -1');
      }
      
      // Clamp between 10-100
      stats.smartScore = Math.max(10, Math.min(100, stats.smartScore));
      
      await chrome.storage.local.set({ stats });
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error('[Unloop] adjustSmartScore error:', error);
      }
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // BLOCK & FAVORITE FUNCTIONS (PRODUCTION-GRADE)
  // ═══════════════════════════════════════════════════════════════

  /**
   * 🔒 Safe storage toggle - NEVER overwrites, always merges
   * This is the key to reliable persistent storage
   */
  async function toggleStore(key, id) {
    if (!isContextAlive()) return false;
    
    try {
      const data = await chrome.storage.local.get([key]);
      const map = data[key] || {};
      
      // Toggle state
      if (map[id]) {
        delete map[id];  // Remove if exists
      } else {
        map[id] = true;  // Add if doesn't exist
      }
      
      await chrome.storage.local.set({ [key]: map });
      
      return map[id] === true;  // Return new state
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error(`[Unloop] toggleStore error for ${key}:`, error);
      }
      return false;
    }
  }

  /**
   * 🚫 Toggle block for current song
   * - If not blocked → block + skip immediately
   * - If blocked → unblock
   */
  async function toggleBlockCurrentSong() {
    if (!isContextAlive() || sessionKilled) return;
    
    const trackId = state.currentVideoId || getVideoId();
    if (!trackId) {
      showToast('No song detected', 'error', '❌');
      return;
    }
    
    try {
      const isNowBlocked = await toggleStore('blockedSongs', trackId);
      
      if (isNowBlocked) {
        showToast('🚫 Blocked & Skipping', 'skip', '🚫');
        log(`🚫 Blocked song: ${trackId}`);
        
        // Immediately skip
        setTimeout(() => {
          if (isContextAlive()) {
            skipToNext();
          }
        }, 300);
      } else {
        showToast('✅ Unblocked', 'saved', '✅');
        log(`✅ Unblocked song: ${trackId}`);
      }
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error('[Unloop] toggleBlock error:', error);
      }
    }
  }

  /**
   * ❤️ Toggle favorite for current song
   * - If not favorited → add to favorites
   * - If favorited → remove from favorites
   */
  async function toggleFavoriteCurrentSong() {
    if (!isContextAlive() || sessionKilled) return;
    
    const trackId = state.currentVideoId || getVideoId();
    if (!trackId) {
      showToast('No song detected', 'error', '❌');
      return;
    }
    
    try {
      const isNowFavorited = await toggleStore('favoriteSongs', trackId);
      
      if (isNowFavorited) {
        showToast('❤️ Added to Favorites', 'saved', '💜');
        await addWin('Favorited a song ❤️');
        log(`❤️ Favorited song: ${trackId}`);
      } else {
        showToast('💔 Removed from Favorites', 'skip', '💔');
        log(`💔 Unfavorited song: ${trackId}`);
      }
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error('[Unloop] toggleFavorite error:', error);
      }
    }
  }

  /**
   * 🚫 Block current song - immediately skips and never plays again
   * (Legacy function - calls toggle)
   */
  async function blockCurrentSong(trackId) {
    if (!isContextAlive() || sessionKilled) return;
    await toggleBlockCurrentSong();
  }

  /**
   * ❤️ Favorite current song - protected from auto-skip
   * (Legacy function - calls toggle)
   */
  async function favoriteCurrentSong(trackId) {
    if (!isContextAlive() || sessionKilled) return;
    await toggleFavoriteCurrentSong();
  }

  /**
   * Check if song is blocked - if yes, skip immediately
   * Returns true if blocked (should abort processing)
   */
  async function checkIfBlocked(trackId) {
    if (!isContextAlive() || sessionKilled) return false;
    
    try {
      const result = await chrome.storage.local.get(['blockedSongs']);
      const blockedSongs = result.blockedSongs || {};
      
      if (blockedSongs[trackId]) {
        showToast('Blocked song skipped 🚫', 'skip', '🚫');
        
        setTimeout(() => {
          if (isContextAlive()) {
            skipToNext();
          }
        }, CONFIG.SKIP_DELAY);
        
        log(`🚫 Auto-skipped blocked song: ${trackId}`);
        return true;
      }
      
      return false;
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error('[Unloop] Check blocked error:', error);
      }
      return false;
    }
  }

  /**
   * Check if song is favorited
   * Returns true if favorited (should never skip)
   */
  async function checkIfFavorited(trackId) {
    if (!isContextAlive() || sessionKilled) return false;
    
    try {
      const result = await chrome.storage.local.get(['favoriteSongs']);
      const favoriteSongs = result.favoriteSongs || {};
      
      return favoriteSongs[trackId] === true;
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error('[Unloop] Check favorited error:', error);
      }
      return false;
    }
  }

  /**
   * Track when user skips a song quickly (< 20 seconds)
   * This indicates strong dislike for Smart Auto learning
   * CANCELLATION-SAFE: Guards against context invalidation during async
   */
  async function trackQuickSkip(videoId) {
    if (!isContextAlive() || sessionKilled) return;
    
    try {
      const history = await getHistory();
      
      // Check context after async operation
      if (!isContextAlive() || sessionKilled) {
        log('Quick skip tracking cancelled - context lost');
        return;
      }
      
      if (history[videoId]) {
        history[videoId].quickSkipCount = (history[videoId].quickSkipCount || 0) + 1;
        history[videoId].totalSkips = (history[videoId].totalSkips || 0) + 1;
        
        // Update storage with context guard
        return new Promise((resolve) => {
          if (!isContextAlive()) {
            resolve();
            return;
          }
          
          safeChrome(
            () => chrome.storage.local.set({ songHistory: history }, () => {
              // Guard completion - don't resolve if context died
              if (!isContextAlive()) {
                log('Quick skip storage cancelled - context invalidated');
                return;
              }
              
              if (chrome.runtime.lastError) {
                log('Quick skip tracking error:', chrome.runtime.lastError);
              } else {
                log(`Tracked quick skip for ${videoId}`);
              }
              resolve();
            }),
            null
          );
        });
      }
    } catch (error) {
      if (isContextInvalidatedError(error)) {
        log('Ignored context invalidation in quick skip tracking');
        return;
      }
      console.error('[Unloop] Quick skip tracking error:', error);
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // LEARNING EVENT TRACKING (For Intelligence Score)
  // ═══════════════════════════════════════════════════════════════

  /**
   * Track meaningful learning events for Smart Auto intelligence calculation
   * Events: skip_protected, artist_protected, loved_detected, annoying_blocked
   */
  async function trackLearningEvent(eventType, effectScore = 1) {
    if (!isContextAlive() || sessionKilled) return;
    
    try {
      return new Promise((resolve) => {
        safeChrome(
          () => chrome.storage.local.get(['learningLog'], (result) => {
            if (!isContextAlive()) {
              resolve();
              return;
            }
            
            const learningLog = result.learningLog || [];
            
            // Add event
            learningLog.push({
              timestamp: Date.now(),
              event: eventType,
              effectScore: effectScore,
              platform: getPlatform()
            });
            
            // Keep last 500 events (prevent bloat)
            if (learningLog.length > 500) {
              learningLog.shift();
            }
            
            safeChrome(
              () => chrome.storage.local.set({ learningLog }, () => {
                if (!isContextAlive()) return;
                log(`📊 Learning event: ${eventType} (+${effectScore})`);
                resolve();
              }),
              null
            );
          }),
          null
        );
      });
    } catch (error) {
      if (isContextInvalidatedError(error)) {
        log('Learning event tracking cancelled');
      }
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // SMART AUTO LEARNING ENGINE
  // ═══════════════════════════════════════════════════════════════

  /**
   * Calculate how much user likes this song (0-1 scale)
   * Based on listen duration, play/skip ratio, and behavior patterns
   */
  function calculateAffectionScore(songData) {
    const avgDuration = songData.avgListenDuration || 0;
    const totalPlays = songData.totalPlays || 1;
    const totalSkips = songData.totalSkips || 0;
    const quickSkips = songData.quickSkipCount || 0;
    
    // Normalize average duration (assume 0-1 scale, where 1 = full listen)
    const durationWeight = avgDuration * 0.6;
    
    // Play vs skip ratio
    const playRatio = totalPlays / (totalPlays + totalSkips + 1);
    const playWeight = playRatio * 0.3;
    
    // Penalty for skips
    const skipRatio = totalSkips / (totalPlays + totalSkips + 1);
    const skipPenalty = skipRatio * 0.1;
    
    // Extra penalty for quick skips (user HATES this song)
    const quickSkipPenalty = Math.min(quickSkips * 0.05, 0.3);
    
    const score = Math.max(0, Math.min(1, 
      durationWeight + playWeight - skipPenalty - quickSkipPenalty
    ));
    
    return score;
  }

  /**
   * Calculate dynamic cooldown based on affection score
   * Loved songs = shorter cooldown, disliked = longer
   */
  function calculateSmartCooldown(affectionScore, songData) {
    const totalSkips = songData.totalSkips || 0;
    const quickSkips = songData.quickSkipCount || 0;
    
    // Base cooldown formula: 4-164 hours based on affection
    let cooldownHours = 4 + (1 - affectionScore) * 160;
    
    // Hard block for repeatedly disliked songs
    if (totalSkips >= 3 || quickSkips >= 2) {
      const hardBlockDays = Math.min(totalSkips * 7, 90);
      cooldownHours = Math.max(cooldownHours, hardBlockDays * 24);
    }
    
    return cooldownHours;
  }

  /**
   * Smart Auto decision engine - the brain that learns
   */
  async function smartAutoDecision(videoId, songInfo, songData, hoursSinceLastPlay) {
    const artist = songInfo.artist?.toLowerCase();
    
    // RULE 1: Session repeat protection (instant repeat = bad UX)
    const recentlyPlayedInSession = state.sessionData.recentSongs
      .slice(-5)
      .some(s => s.videoId === videoId);
    
    if (recentlyPlayedInSession) {
      logSkip('Smart Auto - just played in session 🎧');
      await trackLearningEvent('skip_protected', 2); // Learning event
      return { 
        skip: true, 
        reason: 'smart-session-repeat',
        message: 'Skipped — heard too recently 🎧'
      };
    }
    
    // RULE 2: Hard dislike protection (user clearly hates this)
    const totalSkips = songData.totalSkips || 0;
    const quickSkips = songData.quickSkipCount || 0;
    
    if (totalSkips >= 3) {
      logSkip(`Smart Auto - user dislikes (${totalSkips} skips) 🙈`);
      await trackLearningEvent('annoying_blocked', 3); // Strong learning signal
      return { 
        skip: true, 
        reason: 'smart-dislike',
        message: 'Skipped — you usually skip this 🙈'
      };
    }
    
    if (quickSkips >= 2) {
      logSkip('Smart Auto - repeatedly skipped fast ⚡');
      await trackLearningEvent('annoying_blocked', 3);
      return { 
        skip: true, 
        reason: 'smart-quick-skip',
        message: 'Protecting your sanity 💪'
      };
    }
    
    // RULE 3: Artist diversity shield
    if (artist) {
      const recentArtistPlays = state.sessionData.recentArtists
        .slice(-10)
        .filter(a => a.toLowerCase() === artist)
        .length;
      
      const avgDuration = songData.avgListenDuration || 0;
      
      // If artist played 3+ times recently AND user doesn't love this song
      if (recentArtistPlays >= 3 && avgDuration < 0.75) {
        logSkip(`Smart Auto - too much ${songInfo.artist} 😎`);
        await trackLearningEvent('artist_protected', 2); // Learning event
        return { 
          skip: true, 
          reason: 'smart-artist-diversity',
          message: `Keeping artist variety 😎`
        };
      }
    }
    
    // RULE 4: Time-based cooldown with affection score
    const affectionScore = calculateAffectionScore(songData);
    const cooldownHours = calculateSmartCooldown(affectionScore, songData);
    
    if (hoursSinceLastPlay < cooldownHours) {
      const hoursLeft = Math.round(cooldownHours - hoursSinceLastPlay);
      logSkip(`Smart Auto - cooldown (${hoursLeft}h left, affection: ${affectionScore.toFixed(2)})`);
      await trackLearningEvent('skip_protected', 1);
      return { 
        skip: true, 
        reason: 'smart-cooldown',
        message: 'Skipped — heard too recently 🎧'
      };
    }
    
    // RULE 5: Allow if all checks passed
    logAllow(`Smart Auto - approved (affection: ${affectionScore.toFixed(2)}) ✨`);
    
    // Track loved songs (high affection)
    if (affectionScore > 0.75) {
      await trackLearningEvent('loved_detected', 1);
    }
    
    return { 
      skip: false, 
      reason: 'smart-approved',
      message: `Smart choice ✨`
    };
  }

  // ═══════════════════════════════════════════════════════════════
  // DECISION ENGINE - THE BRAIN (UNIFIED MODE ENGINE)
  // ═══════════════════════════════════════════════════════════════

  /**
   * MASTER MODE ENGINE - Every song passes through this
   * This is the single source of truth for skip/allow decisions
   */
  async function shouldSkipSong(videoId, songInfo) {
    const mode = state.settings.mode || 'strict';
    log(`🧠 Mode Engine: ${mode.toUpperCase()} processing "${songInfo.title}" by ${songInfo.artist}`);
    
    // Step 1: Check if extension is enabled
    if (!state.enabled) {
      logAllow('Extension disabled');
      return { skip: false, reason: 'disabled' };
    }

    // Step 2: Check WHITELIST → Always allow
    if (state.whitelist.includes(videoId)) {
      logAllow('Song is whitelisted ❤️');
      return { skip: false, reason: 'whitelist' };
    }

    // Step 3: Check BLACKLIST → Always skip
    if (state.blacklist.includes(videoId)) {
      logSkip('Song is blacklisted 🚫');
      return { skip: true, reason: 'blacklist' };
    }

    // Step 4: Check history
    const history = await getHistory();
    const songData = history[videoId];

    // If never played → Save and allow (for ALL modes)
    if (!songData) {
      log('✨ NEW SONG - First time playing!');
      return { skip: false, reason: 'new' };
    }

    // Step 5: Song exists in history → Apply mode-specific rules
    log(`📊 Song played before: ${songData.totalPlays || 1}x, last: ${new Date(songData.lastPlayed).toLocaleString()}`);
    
    // Calculate time since last play
    const hoursSinceLastPlay = (Date.now() - songData.lastPlayed) / (1000 * 60 * 60);
    const artist = songInfo.artist?.toLowerCase()?.trim();

    // ═══════════════════════════════════════════════════════════════
    // MODE-SPECIFIC LOGIC
    // ═══════════════════════════════════════════════════════════════
    
    switch (mode) {
      
      // ─────────────────────────────────────────────────────────
      case 'smart-auto':
        log('🤖 Smart Auto analyzing...');
        return await smartAutoDecision(videoId, songInfo, songData, hoursSinceLastPlay);

      // ─────────────────────────────────────────────────────────
      case 'strict':
        // STRICT: Never allow repeats - if song exists in history, skip it
        log(`🛡️ STRICT: Song exists in history → SKIP`);
        return { skip: true, reason: 'strict', message: 'Strict mode: No repeats allowed' };

      // ─────────────────────────────────────────────────────────
      case 'memory-fade':
        // MEMORY FADE: Allow if enough time has passed
        const fadeHours = state.settings.memoryFadeHours || 72;
        log(`⏳ MEMORY FADE: ${Math.round(hoursSinceLastPlay)}h elapsed, threshold: ${fadeHours}h`);
        
        if (hoursSinceLastPlay >= fadeHours) {
          log(`⏳ Memory faded! (${Math.round(hoursSinceLastPlay)}h > ${fadeHours}h) → ALLOW`);
          return { skip: false, reason: 'memory-fade-allowed', message: `Memory faded after ${fadeHours}h` };
        }
        
        const hoursLeft = Math.round(fadeHours - hoursSinceLastPlay);
        log(`⏳ Memory still fresh (${hoursLeft}h remaining) → SKIP`);
        return { skip: true, reason: 'memory-fade', message: `Memory fade: ${hoursLeft}h until allowed` };

      // ─────────────────────────────────────────────────────────
      case 'semi-strict':
        // SEMI-STRICT: Allow if enough NEW songs played since last repeat
        const requiredSongs = state.settings.songsBeforeRepeat || 5;
        const newSongs = state.sessionData.newSongsSinceRepeat || 0;
        log(`⚖️ SEMI-STRICT: ${newSongs} new songs played, need ${requiredSongs}`);
        
        if (newSongs >= requiredSongs) {
          log(`⚖️ Played enough new songs! (${newSongs} >= ${requiredSongs}) → ALLOW`);
          // Reset counter when we allow a repeat
          state.sessionData.newSongsSinceRepeat = 0;
          return { skip: false, reason: 'semi-strict-allowed', message: `Allowed after ${newSongs} new songs` };
        }
        
        const songsNeeded = requiredSongs - newSongs;
        log(`⚖️ Need ${songsNeeded} more new songs → SKIP`);
        return { skip: true, reason: 'semi-strict', message: `Need ${songsNeeded} more new songs` };

      // ─────────────────────────────────────────────────────────
      case 'artist-smart':
        // ARTIST SMART: Prevent artist spam
        log(`🎭 ARTIST SMART: Checking artist "${artist}"`);
        
        if (!artist || artist === 'unknown') {
          log('🎭 Unknown artist, allowing...');
          return { skip: false, reason: 'artist-unknown' };
        }
        
        // Check if same artist in last 3 songs
        const last3Artists = state.sessionData.recentArtists.slice(-3).map(a => a?.toLowerCase()?.trim());
        if (last3Artists.includes(artist)) {
          log(`🎭 Artist "${artist}" played in last 3 songs → SKIP`);
          return { skip: true, reason: 'artist-smart', message: `${songInfo.artist} played too recently` };
        }
        
        // Check session limit
        const artistPlays = state.sessionData.artistPlayCount[artist] || 0;
        const maxArtist = state.settings.maxArtistPerSession || 3;
        
        if (artistPlays >= maxArtist) {
          log(`🎭 Artist "${artist}" hit session limit (${artistPlays}/${maxArtist}) → SKIP`);
          return { skip: true, reason: 'artist-limit', message: `${songInfo.artist} reached session limit` };
        }
        
        // Also check if this specific song was played in last 10
        const last10Songs = state.sessionData.recentSongs.slice(-10).map(s => s.videoId);
        if (last10Songs.includes(videoId)) {
          log(`🎭 Song played in last 10 → SKIP`);
          return { skip: true, reason: 'recent-repeat', message: 'Song played too recently' };
        }
        
        log(`🎭 Artist check passed → ALLOW`);
        return { skip: false, reason: 'artist-smart-allowed' };

      // ─────────────────────────────────────────────────────────
      default:
        log(`⚠️ Unknown mode "${mode}", defaulting to strict`);
        return { skip: true, reason: 'default-strict' };
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // MAIN PROCESSING LOOP
  // ═══════════════════════════════════════════════════════════════

  async function processSong() {
    // CRITICAL: Check if runtime is still valid before any async operations
    if (!isContextAlive() || sessionKilled) {
      console.debug('[Unloop] Session cancelled safely - no action needed');
      return;
    }
    
    // Generate token for this processing run (debouncing)
    const token = ++songProcessToken;
    
    // Check if extension is still valid
    if (!isExtensionValid()) return;
    if (state.isProcessing) return;
    
    const videoId = getVideoId();
    if (!videoId) return;
    
    // Track listening duration for Smart Auto (when song changes)
    if (videoId !== state.lastProcessedId && state.listening.currentVideoId) {
      const listenDuration = (Date.now() - state.listening.startTime) / 1000; // seconds
      const prevVideoId = state.listening.currentVideoId;
      
      // If user listened < 20 seconds, count as quick skip
      if (listenDuration < 20 && state.settings.mode === 'smart-auto') {
        await trackQuickSkip(prevVideoId);
      }
      
      log(`Listened to previous song for ${Math.round(listenDuration)}s`);
    }
    
    if (videoId === state.lastProcessedId) return;

    state.isProcessing = true;
    state.currentVideoId = videoId;
    
    // Start tracking listening time
    state.listening.startTime = Date.now();
    state.listening.currentVideoId = videoId;

    try {
      // Reload settings (might have changed)
      await loadSettings();
      
      // Check if this processing run is stale
      if (token !== songProcessToken) {
        log('Stale song processing aborted (newer request pending)');
        state.isProcessing = false;
        return;
      }
      
      // Re-check context after async operation
      if (!isContextAlive() || sessionKilled) {
        console.debug('[Unloop] Session cancelled during settings load');
        state.isProcessing = false;
        return;
      }

      const songInfo = getSongInfo();
      log(`Processing: "${songInfo.title}" by ${songInfo.artist} [${videoId}]`);

      // 🚫 FIRST PRIORITY: Check if song is blocked
      const isBlocked = await checkIfBlocked(videoId);
      if (isBlocked) {
        state.isProcessing = false;
        return;
      }

      // ❤️ SECOND PRIORITY: Check if favorited (auto-allow)
      const isFavorited = await checkIfFavorited(videoId);
      if (isFavorited) {
        log('❤️ Favorite song detected - auto-allowing');
        await addWin('Protected favorite ❤️');
        showToast('Playing favorite ❤️', 'saved', '💜');
        
        // Update state and continue
        const platform = detectPlatform();
        await updateSongState(platform, videoId, songInfo.title, songInfo.artist);
        state.lastProcessedId = videoId;
        state.isProcessing = false;
        return;
      }

      // ✅ CORE FIX: Update song state EVERY time we detect a song
      // This fixes Artist Discovery, Stats, Smart Learning
      const platform = detectPlatform();
      await updateSongState(platform, videoId, songInfo.title, songInfo.artist);

      // Make decision
      const decision = await shouldSkipSong(videoId, songInfo);
      
      // Check if this processing run is stale
      if (token !== songProcessToken) {
        log('Stale song processing aborted after decision');
        state.isProcessing = false;
        return;
      }
      
      // Re-check context after async decision
      if (!isContextAlive() || sessionKilled) {
        console.debug('[Unloop] Session cancelled during decision');
        state.isProcessing = false;
        return;
      }

      if (decision.skip) {
        // SKIP THE SONG
        await incrementSkipCount();
        
        // ✅ Log win for recent wins display
        const winMessage = decision.message || `Prevented repeat: ${songInfo.title?.substring(0, 30)}`;
        await addWin(winMessage);
        
        // ✅ Adjust smart score (skip = good decision)
        if (state.settings.mode === 'smart-auto') {
          await adjustSmartScore(true);
        }
        
        // Check if stale
        if (token !== songProcessToken) {
          state.isProcessing = false;
          return;
        }
        
        // Check context before showing toast
        if (!isContextAlive() || sessionKilled) {
          state.isProcessing = false;
          return;
        }
        
        // Show toast with Smart Auto custom messages
        let toastMessage = decision.message || 'Skipped — already played 🎧';
        
        // Fallback messages for non-Smart Auto modes
        if (!decision.message) {
          if (decision.reason === 'blacklist') {
            toastMessage = 'Skipped — blacklisted 🚫';
          } else if (decision.reason === 'artist-smart' || decision.reason === 'artist-limit') {
            toastMessage = `Skipped — too much ${songInfo.artist} 🎤`;
          }
        }
        
        showToast(toastMessage, 'skip', '⏭️');

        // Skip after short delay
        setTimeout(() => {
          if (chrome?.runtime?.id) {
            skipToNext();
          }
        }, CONFIG.SKIP_DELAY);

      } else {
        // ALLOW THE SONG
        
        // Commit listening time for previous song
        if (state.currentTrackId && state.currentTrackId !== videoId) {
          await commitListeningTime(state.currentTrackId);
        }
        
        // Start tracking new song
        state.currentTrackId = videoId;
        state.currentStartTime = Date.now();
        
        // Handle based on reason
        const isNewSong = decision.reason === 'new';
        const isAllowedRepeat = ['memory-fade-allowed', 'semi-strict-allowed', 'artist-smart-allowed'].includes(decision.reason);
        
        if (isNewSong) {
          // New song - save it to history
          await saveSong(videoId, songInfo);
          
          // ✅ Log win for new discovery
          await addWin(`New discovery: ${songInfo.artist}`);
          
          // ✅ Smart score +1 for good decision (allowing new song)
          if (state.settings.mode === 'smart-auto') {
            await adjustSmartScore(true);
          }
          
          // Increment new songs counter (for semi-strict mode)
          state.sessionData.newSongsSinceRepeat++;
          log(`📊 New songs since repeat: ${state.sessionData.newSongsSinceRepeat}`);
          
        } else if (isAllowedRepeat) {
          // Allowed repeat - update lastPlayed but don't count as "new"
          await updateSongPlayed(videoId, songInfo);
          log(`📊 Allowed repeat for: ${songInfo.title}`);
        }
        
        // Track session data for ALL allowed songs
        if (songInfo.title && songInfo.artist) {
          chrome.runtime.sendMessage({
            type: 'TRACK_SESSION_SONG',
            title: songInfo.title,
            artist: songInfo.artist
          }).catch(() => {});
        }
        
        // Check if stale
        if (token !== songProcessToken) {
          state.isProcessing = false;
          return;
        }
        
        // Check context after save
        if (!isContextAlive() || sessionKilled) {
          state.isProcessing = false;
          return;
        }
        
        // Update session tracking for ALL allowed songs
        state.sessionData.recentSongs.push({ videoId, ...songInfo });
        if (state.sessionData.recentSongs.length > 50) {
          state.sessionData.recentSongs.shift();
        }
        
        // Track artist plays for ALL allowed songs
        if (songInfo.artist) {
          const artist = songInfo.artist.toLowerCase().trim();
          state.sessionData.artistPlayCount[artist] = 
            (state.sessionData.artistPlayCount[artist] || 0) + 1;
          
          // Track recent artists
          state.sessionData.recentArtists.push(songInfo.artist);
          if (state.sessionData.recentArtists.length > 20) {
            state.sessionData.recentArtists.shift();
          }
          
          log(`📊 Artist "${artist}" played ${state.sessionData.artistPlayCount[artist]}x this session`);
        }

        // Show appropriate toast
        if (isNewSong) {
          const discoveryMessage = decision.message || `New discovery: ${songInfo.title?.substring(0, 30) || 'Unknown'}`;
          showToast(discoveryMessage, 'saved', '✨');
        } else if (decision.reason === 'whitelist') {
          showToast('Playing favorite ❤️', 'whitelist', '💜');
        } else if (isAllowedRepeat) {
          showToast(decision.message || 'Repeat allowed ✓', 'saved', '🔄');
        }
      }

      state.lastProcessedId = videoId;
      
      // Debug: Log current mode state after processing
      logModeState();

    } catch (error) {
      console.error('[Unloop] Error processing song:', error);
    } finally {
      state.isProcessing = false;
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // EVENT LISTENERS & OBSERVERS
  // ═══════════════════════════════════════════════════════════════

  // Store observer references for cleanup
  let urlObserver = null;
  let playerObserver = null;

  function setupObservers() {
    // Disconnect existing observers before creating new ones
    if (urlObserver) {
      urlObserver.disconnect();
      urlObserver = null;
    }
    if (playerObserver) {
      playerObserver.disconnect();
      playerObserver = null;
    }

    // Watch for URL changes (YouTube is a SPA)
    let lastUrl = location.href;
    
    urlObserver = new MutationObserver(() => {
      if (location.href !== lastUrl) {
        lastUrl = location.href;
        log('URL changed, checking for new song...');
        
        // Cleanup and restart observers on navigation
        if (playerObserver) {
          playerObserver.disconnect();
          playerObserver = null;
        }
        
        // Reset processing state on navigation
        state.isProcessing = false;
        state.lastProcessedId = null;
        
        setTimeout(() => {
          if (chrome?.runtime?.id) {
            processSong();
          }
        }, 500);
      }
    });

    urlObserver.observe(document.body, {
      childList: true,
      subtree: true
    });

    // YouTube Music: Watch player bar for changes
    if (isYouTubeMusic()) {
      const waitForPlayer = setInterval(() => {
        if (!chrome?.runtime?.id) {
          clearInterval(waitForPlayer);
          return;
        }
        
        const playerBar = document.querySelector('ytmusic-player-bar');
        if (playerBar) {
          clearInterval(waitForPlayer);
          
          // Disconnect existing player observer if any
          if (playerObserver) {
            playerObserver.disconnect();
          }
          
          playerObserver = new MutationObserver(() => {
            if (chrome?.runtime?.id) {
              processSong();
            }
          });
          
          playerObserver.observe(playerBar, {
            childList: true,
            subtree: true,
            attributes: true,
            attributeFilter: ['title', 'src']
          });
          
          log('Player bar observer attached');
        }
      }, 1000);
    }

    // Spotify: Watch now playing widget for track changes
    if (isSpotify()) {
      const waitForSpotify = setInterval(() => {
        if (!chrome?.runtime?.id) {
          clearInterval(waitForSpotify);
          return;
        }
        
        const nowPlaying = document.querySelector('[data-testid="now-playing-widget"]');
        if (nowPlaying) {
          clearInterval(waitForSpotify);
          
          // Disconnect existing player observer if any
          if (playerObserver) {
            playerObserver.disconnect();
          }
          
          playerObserver = new MutationObserver(() => {
            if (chrome?.runtime?.id) {
              processSong();
            }
          });
          
          playerObserver.observe(nowPlaying, {
            childList: true,
            subtree: true,
            attributes: true
          });
          
          log('Spotify now-playing observer attached');
        }
      }, 1000);
    }

    // YouTube: Watch video player
    if (isYouTube()) {
      const waitForVideo = setInterval(() => {
        if (!chrome?.runtime?.id) {
          clearInterval(waitForVideo);
          return;
        }
        
        const video = document.querySelector('video');
        if (video) {
          clearInterval(waitForVideo);
          
          video.addEventListener('loadeddata', () => {
            if (chrome?.runtime?.id) {
              log('Video loaded');
              setTimeout(() => {
                if (chrome?.runtime?.id) {
                  processSong();
                }
              }, 300);
            }
          });
          
          log('Video element observer attached');
        }
      }, 1000);
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // MESSAGE LISTENER (From Popup/Background)
  // ═══════════════════════════════════════════════════════════════

  safeChrome(
    () => chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
      if (!isExtensionValid()) return false;
      
      try {
        switch (message.type) {
          case 'GET_CURRENT_VIDEO':
            sendResponse({
              videoId: state.currentVideoId,
              ...getSongInfo()
            });
            break;

          case 'SETTINGS_UPDATED':
            loadSettings().then(() => {
              log('Settings reloaded');
              if (isExtensionValid()) {
                sendResponse({ success: true });
              }
            }).catch((error) => {
              if (!isContextInvalidatedError(error)) {
                console.error('[Unloop] Settings update error:', error);
              }
            });
            return true;

          case 'TOGGLE_ENABLED':
            state.enabled = message.enabled;
            showToast(
              message.enabled ? 'Discovery Mode ON' : 'Discovery Mode OFF',
              message.enabled ? 'saved' : 'skip',
              message.enabled ? '🎵' : '⏸️'
            );
            sendResponse({ success: true });
            break;

          case 'ADD_CURRENT_TO_WHITELIST':
            if (state.currentVideoId && isExtensionValid()) {
              safeChrome(
                () => chrome.runtime.sendMessage({
                  type: 'ADD_TO_WHITELIST',
                  videoId: state.currentVideoId
                }, () => {
                  if (chrome.runtime.lastError) return;
                  loadSettings();
                  showToast('Added to favorites ❤️', 'whitelist', '💜');
                }),
                null
              );
            }
            sendResponse({ success: true });
            break;

        case 'ADD_CURRENT_TO_BLACKLIST':
          if (state.currentVideoId && isExtensionValid()) {
            safeChrome(
              () => chrome.runtime.sendMessage({
                type: 'ADD_TO_BLACKLIST',
                videoId: state.currentVideoId
              }, () => {
                if (chrome.runtime.lastError) return;
                loadSettings();
                showToast('Added to blacklist 🚫', 'blacklist', '⛔');
              }),
              null
            );
          }
          sendResponse({ success: true });
          break;

        case 'BLOCK_CURRENT_SONG':
          // Content script owns the track - no trackId needed from popup
          if (isExtensionValid()) {
            toggleBlockCurrentSong();
          }
          sendResponse({ success: true });
          break;

        case 'FAVORITE_CURRENT_SONG':
          // Content script owns the track - no trackId needed from popup
          if (isExtensionValid()) {
            toggleFavoriteCurrentSong();
          }
          sendResponse({ success: true });
          break;

        default:
          sendResponse({ error: 'Unknown message' });
      }
    } catch (error) {
      if (!isContextInvalidatedError(error)) {
        console.error('[Unloop] Message handler error:', error);
      }
    }
    return true;
    }),
    null
  );

  // ═══════════════════════════════════════════════════════════════
  // INITIALIZATION
  // ═══════════════════════════════════════════════════════════════

  async function init() {
    if (!chrome?.runtime?.id) {
      console.warn('[Unloop] Cannot initialize - extension context invalid');
      return;
    }
    
    log(`Initializing on ${getPlatform()}...`);
    
    await loadSettings();
    
    if (!chrome?.runtime?.id) {
      console.warn('[Unloop] Context lost during initialization');
      return;
    }
    
    setupObservers();
    
    // Start periodic checking with context validation
    setInterval(() => {
      if (chrome?.runtime?.id) {
        processSong();
      }
    }, CONFIG.CHECK_INTERVAL);
    
    // Initial check after page loads
    setTimeout(() => {
      if (chrome?.runtime?.id) {
        processSong();
      }
    }, 1500);
    
    log(`Ready! Mode: ${state.settings.mode}, Enabled: ${state.enabled}`);
    
    // Show welcome toast on first song
    showToast(`Unloop active on ${getPlatform()}`, 'info', '∞');
    
    // Initialize Spotify watcher if on Spotify
    if (isSpotify()) {
      startSpotifyWatcher();
    }
  }

  // Set up navigation cancellation listeners
  window.addEventListener('yt-navigate-start', () => {
    commitListeningTime(state.currentTrackId);
    killSession();
  });
  window.addEventListener('popstate', () => {
    commitListeningTime(state.currentTrackId);
    killSession();
  });
  window.addEventListener('beforeunload', () => {
    commitListeningTime(state.currentTrackId);
    killSession();
  });
  
  // Reset session flag after navigation completes
  window.addEventListener('yt-navigate-finish', () => {
    setTimeout(() => {
      sessionKilled = false;
      log('Session reactivated after navigation');
    }, 500);
  });
  
  // Commit listening time when tab loses visibility
  document.addEventListener('visibilitychange', () => {
    if (document.hidden && state.currentTrackId) {
      commitListeningTime(state.currentTrackId);
      // Restart tracking when visible again
      state.currentStartTime = Date.now();
    }
  });

  // Start when DOM is ready
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

})();
