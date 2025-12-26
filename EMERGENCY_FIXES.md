# 🚨 EMERGENCY FIXES & DEBUGGING

## 🔥 Quick Reset (If Extension Seems Broken)

### Method 1: Clear Storage (Safe Reset)
1. Open Chrome DevTools on extension popup (F12 or right-click → Inspect)
2. Go to **Console** tab
3. Run these commands:

```javascript
chrome.storage.local.clear(() => console.log('Local storage cleared'))
chrome.storage.session.clear(() => console.log('Session storage cleared'))
```

4. Close and reopen popup
5. Extension will reinitialize with defaults

### Method 2: Inspect Service Worker
1. Go to `chrome://extensions/`
2. Find **Unloop**
3. Click **"Inspect views: service worker"**
4. In console, run:

```javascript
chrome.storage.local.get(null, console.log)  // See all data
chrome.storage.session.get(null, console.log)  // See session data
```

5. Manually clear if needed:

```javascript
chrome.storage.local.clear()
chrome.storage.session.clear()
```

---

## 🐛 Common Issues & Fixes

### Issue 1: "Artist Discovery shows 0"
**Cause**: No songs in history OR artist field missing

**Fix**:
1. Play 5-10 songs on YouTube Music or Spotify
2. Let each play for at least 5 seconds
3. Reload popup
4. Should now show count

**Check data**:
```javascript
chrome.storage.local.get(['songHistory'], (data) => {
  console.log('Songs:', Object.keys(data.songHistory || {}).length);
  console.log('First song:', Object.values(data.songHistory || {})[0]);
});
```

If songs have no `artist` field → bug, report it.

---

### Issue 2: "Session Songs shows 0"
**Cause**: Session storage not tracking OR browser just started

**Fix**:
1. Session resets on browser close (this is intentional)
2. Play a new song
3. Wait 2 seconds
4. Reload popup
5. Should increment

**Check session**:
```javascript
chrome.storage.session.get(['sessionSongs'], console.log);
```

---

### Issue 3: "Total Listening Time shows --"
**Cause**: No listening time accumulated yet OR not being tracked

**Fix**:
1. Play a song for 30+ seconds
2. Skip to next song (this commits the time)
3. Reload popup
4. Should show time

**Check stats**:
```javascript
chrome.storage.local.get(['stats'], (data) => {
  console.log('Total seconds:', data.stats?.totalListeningSeconds || 0);
});
```

---

### Issue 4: "CSV Export says 'No songs in history'"
**Cause**: songHistory is empty

**Fix**:
1. Play songs until Artist Discovery shows > 0
2. Then try CSV export again
3. CSV will have Platform, Title, Artist, Plays, Skips, Timestamps

**Check history**:
```javascript
chrome.storage.local.get(['songHistory'], (data) => {
  const count = Object.keys(data.songHistory || {}).length;
  console.log(`${count} songs ready for export`);
  if (count > 0) {
    console.log('Sample:', Object.values(data.songHistory)[0]);
  }
});
```

---

### Issue 5: "Minimal Mode stuck ON (can't find toggle)"
**Cause**: Toggle was hidden by minimal mode itself (oops!)

**Fix**: This has been fixed - toggle now ALWAYS visible in Customize section

**Emergency override**:
```javascript
chrome.storage.local.get(['settings'], (data) => {
  data.settings.minimalMode = false;
  chrome.storage.local.set({ settings: data.settings }, () => {
    console.log('Minimal mode disabled');
    location.reload();
  });
});
```

---

### Issue 6: "Stats not updating in real-time"
**Cause**: Popup needs reload to fetch latest data

**Fix**:
- Close and reopen popup to see latest stats
- Or add auto-refresh (not implemented yet)

---

### Issue 7: "Extension doesn't detect songs on Spotify"
**Cause**: Content script not loaded OR Spotify changed their HTML

**Check**:
1. Open Spotify Web (`open.spotify.com`)
2. Open DevTools (F12)
3. Check Console for `[Unloop]` messages
4. If none → content script not running
5. Reload the tab

**Manual test**:
```javascript
// In Spotify tab console
document.querySelector('[data-testid="now-playing-widget"]')
// Should return element if Spotify page loaded
```

---

### Issue 8: "Achievements not appearing"
**Cause**: Not reaching milestones OR achievement check not running

**Test manually**:
```javascript
chrome.storage.local.get(['achievements', 'stats'], (data) => {
  console.log('Stats:', data.stats);
  console.log('Achievements:', data.achievements);
  
  // Manually trigger (dev testing only)
  data.stats.skipped = 25;  // Force milestone
  chrome.storage.local.set({ stats: data.stats });
});
```

Then skip one more song → should trigger 25 loops achievement.

---

## 🔍 Data Inspection Commands

### See Everything:
```javascript
chrome.storage.local.get(null, (data) => {
  console.log('=== LOCAL STORAGE ===');
  console.log('Enabled:', data.enabled);
  console.log('Settings:', data.settings);
  console.log('Stats:', data.stats);
  console.log('Songs:', Object.keys(data.songHistory || {}).length);
  console.log('Whitelist:', data.whitelist?.length || 0);
  console.log('Blacklist:', data.blacklist?.length || 0);
  console.log('Achievements:', data.achievements);
});

chrome.storage.session.get(null, (data) => {
  console.log('=== SESSION STORAGE ===');
  console.log('Session Songs:', data.sessionSongs?.length || 0);
});
```

### Export Data for Backup:
```javascript
chrome.storage.local.get(null, (data) => {
  const json = JSON.stringify(data, null, 2);
  const blob = new Blob([json], { type: 'application/json' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = `unloop-backup-${Date.now()}.json`;
  a.click();
});
```

### Restore from Backup:
1. Use Import button in popup
2. Select your backup JSON file
3. Data will merge (not overwrite)

---

## ✅ Verification Checklist

After fixes, test these:

- [ ] Play song → Artist Discovery increments
- [ ] Play song → This Session increments
- [ ] Play 30s → Total Listening Time shows time
- [ ] Skip song → Loops Prevented increments
- [ ] CSV Export → Downloads file with data
- [ ] Minimal Mode toggle → Can turn ON and OFF
- [ ] Theme toggle → Dark/Light switches properly
- [ ] Close browser → Session resets to 0
- [ ] Reopen browser → Total time persists

---

## 🆘 Still Broken?

1. Check Console for errors (red text)
2. Copy error message
3. Check if content script loaded:
   - `chrome://extensions` → Unloop → Details
   - "Content scripts: 3" should show
4. Verify permissions granted (YouTube, Spotify domains)
5. Try disabling/re-enabling extension
6. Last resort: Remove and reinstall extension

---

## 📊 Expected Data After 1 Hour

**stats:**
```javascript
{
  listened: 15,      // 15 songs played
  skipped: 3,        // 3 repeats prevented
  totalListeningSeconds: 900,  // 15 minutes (15 * 60)
  firstInstall: 1234567890
}
```

**songHistory:** (15 entries)
```javascript
{
  "abc123": {
    platform: "Spotify",
    title: "Song Name",
    artist: "Artist Name",
    totalPlays: 1,
    totalSkips: 0,
    totalListeningSeconds: 60,
    firstPlayed: 1234567890,
    lastPlayed: 1234567890,
    plays: 1,
    skips: 0
  },
  // ... 14 more songs
}
```

**sessionSongs:** (15 entries)
```javascript
["Song 1|Artist 1", "Song 2|Artist 2", ...]
```

**achievements:**
```javascript
{
  loops25: false,  // Not yet (only 3 skips)
  loops50: false,
  // ... etc
}
```

---

## 💡 Pro Tips

1. **Open popup while music playing** → Live updates work better
2. **Let songs play 5+ seconds** → Ensures proper tracking
3. **Skip naturally** → Don't spam skip (< 3s ignored)
4. **Check session after 5 songs** → Should show 5
5. **Export CSV weekly** → Backup your data
6. **Reset session manually** → Not implemented yet, but coming

---

You're all set! Extension should now work perfectly. 🚀
