# 🤖 GitHub Copilot Prompts for Smart Auto Enhancements

## 📋 How to Use These Prompts

1. Copy the prompt for the feature you want to add
2. Paste it at the **top** of the relevant file (as a comment)
3. Let GitHub Copilot generate the code
4. Review, test, and refine
5. Delete the prompt comment after implementation

---

## 🎯 Phase 1: Core Improvements (Already Implemented ✅)

These are **done** and working in the current codebase:

- ✅ Affection score calculation
- ✅ Dynamic cooldown system
- ✅ Listen duration tracking
- ✅ Quick skip detection
- ✅ Five-rule decision engine
- ✅ Smart Auto UI with gradient
- ✅ Intelligent toast messages
- ✅ Session artist tracking

---

## 🚀 Phase 2: Advanced Features (Copy-Paste Ready)

### 1️⃣ Persist Session Data Across Browser Restarts

**File**: `content.js` (near initialization section)

```javascript
/*
Add session data persistence to chrome.storage.local.

When extension initializes:
- Load sessionData from storage (recentSongs, recentArtists, artistPlayCount)
- If not found, use empty defaults

When song is processed:
- Save current sessionData to storage after updating
- Debounce saves to avoid excessive writes (use 5-second delay)

This allows Smart Auto to maintain artist diversity tracking even after browser restart.
*/
```

---

### 2️⃣ Intelligence Level Indicator

**File**: `popup.js` (in updateUI section)

```javascript
/*
Add intelligence level calculation and display in popup.

Calculate based on:
- Total songs learned (from history length)
- Learning phases:
  * 0-50 songs = "Learning" (badge: orange)
  * 51-150 songs = "Smart" (badge: blue)
  * 151-500 songs = "Advanced" (badge: purple)
  * 500+ songs = "Expert" (badge: gold with star)

Display in status banner:
"Smart Auto: [Intelligence Level Badge]"

Update after every data load.
*/
```

**File**: `popup.html` (in status banner section)

```html
<!-- Add intelligence indicator after status text
<div class="intelligence-badge" id="intelligenceBadge">
  <span class="badge-icon">🧠</span>
  <span class="badge-text">Learning</span>
</div>
-->
```

**File**: `popup.css` (add badge styling)

```css
/*
Style .intelligence-badge with:
- Inline-flex, gap: 4px
- Padding: 4px 8px
- Border-radius: 12px
- Font-size: 10px
- Background gradient based on level
- Smooth color transitions
*/
```

---

### 3️⃣ Top Loved/Disliked Songs Display

**File**: `popup.html` (new section before footer)

```html
<!--
Add collapsible "Your Taste Profile" section with two lists:

1. Most Loved Songs (top 5)
   - Show title, artist
   - Display affection score as hearts (🧡🧡🧡🧡🧡)
   - Calculated from avgListenDuration + playCount

2. Most Disliked Songs (top 5)
   - Show title, artist
   - Display skip count
   - Show "Block status" (X days remaining)

Include toggle button to expand/collapse section.
-->
```

**File**: `popup.js` (add calculation function)

```javascript
/*
Function: calculateTopSongs(history)

Returns object with:
- loved: Array of top 5 songs by affection score
- disliked: Array of top 5 songs by skip count + quick skips

Each entry: { videoId, title, artist, affectionScore, skipCount, cooldownRemaining }

Sort loved by: affectionScore DESC
Sort disliked by: (totalSkips * 2 + quickSkips * 3) DESC

Display in popup UI with formatted data.
*/
```

---

### 4️⃣ Learning Graph Visualization

**File**: `popup.html` (new section)

```html
<!--
Add mini canvas element for learning progress graph:
<canvas id="learningGraph" width="300" height="100"></canvas>

Shows:
- X-axis: Time (last 30 days)
- Y-axis: Intelligence score (cumulative learning)
- Line graph with gradient fill
- Animated on load
-->
```

**File**: `popup.js` (add graph drawing)

```javascript
/*
Function: drawLearningGraph(canvasId, historyData)

Calculate daily intelligence scores:
- Score = songs learned that day * affectionScore average
- Cumulative over time

Use Canvas 2D API to draw:
- Gradient background (purple to transparent)
- Smooth bezier curve
- Grid lines (subtle)
- Data points as dots on peaks

Animate drawing from left to right on popup open.
*/
```

---

### 5️⃣ Spotify Web Support

**File**: `manifest.json` (add content script)

```json
/*
Add Spotify to content_scripts:
{
  "matches": ["*://open.spotify.com/*"],
  "js": ["content.js"],
  "css": ["toast.css"]
}

Spotify uses different DOM structure than YouTube.
*/
```

**File**: `content.js` (add platform detection)

```javascript
/*
Function: isSpotifyWeb()
Returns true if hostname === 'open.spotify.com'

Function: getSpotifyTrackId()
Extract track ID from:
- URL: /track/TRACK_ID
- Player bar: [data-testid="now-playing-widget"]
- Metadata JSON in page

Return format: spotify:track:TRACK_ID

Function: getSpotifySongInfo()
Extract from DOM:
- Title: [data-testid="context-item-link"] or .Root__now-playing-bar
- Artist: [data-testid="context-item-info-artist"]
- Return: { title, artist, channel: artist }

Function: skipSpotifyTrack()
Click: document.querySelector('[data-testid="control-button-skip-forward"]')
Fallback: dispatch keyboard MediaTrackNext event

Integrate these into existing processSong() with platform checks.
*/
```

---

### 6️⃣ Anti-Algorithm Spam Detection

**File**: `content.js` (new function in Smart Auto section)

```javascript
/*
Function: detectAlgorithmSpam(videoId, songData, history)

Checks if song appears across multiple YouTube recommendation contexts:
- Appears in "Up Next" repeatedly
- Appears in "Mix" playlists often
- Recommended after different songs

Track in songData:
- recommendationContexts: [] (array of contexts)
- spamScore: 0-1 (higher = more spammy)

If spamScore > 0.7:
  Increase cooldown by 2x
  Add penalty to affection score

This prevents YouTube from forcing songs into your rotation.
*/
```

---

### 7️⃣ Export/Import Affection Scores

**File**: `popup.js` (enhance export function)

```javascript
/*
Update handleExport() to include Smart Auto learning data:

Export JSON format:
{
  settings,
  songHistory: {
    videoId: {
      ...existing fields,
      affectionScore: calculated,  // Add computed score
      cooldownHours: calculated,   // Add current cooldown
      learnedAt: timestamp         // When first learned
    }
  },
  smartAutoMeta: {
    totalSongsLearned,
    avgAffectionScore,
    intelligenceLevel,
    exportDate
  }
}

On import:
- Validate Smart Auto fields
- Recalculate scores if data version mismatch
- Show import summary with learning stats
*/
```

---

### 8️⃣ Smart Auto Training Mode

**File**: `popup.html` (add button)

```html
<!--
Add "Train Smart Auto" button in Smart Auto section:
<button id="trainSmartAuto" class="action-btn">
  🧠 Quick Train (Review 20 Songs)
</button>

Opens training modal where user rates songs 1-5 stars.
Fast-tracks learning without waiting for organic plays.
-->
```

**File**: `popup.js` (add training logic)

```javascript
/*
Function: startTraining()

1. Get random 20 songs from history
2. Show modal with song info + star rating (1-5)
3. User rates each song
4. Convert ratings to affection scores:
   - 1 star = 0.0 (instant block)
   - 2 stars = 0.25
   - 3 stars = 0.5
   - 4 stars = 0.75
   - 5 stars = 1.0
5. Update avgListenDuration based on rating
6. Save to history
7. Show completion message

This accelerates Smart Auto learning for impatient users.
*/
```

---

### 9️⃣ Mood-Based Cooldown Adjustment

**File**: `content.js` (enhance calculateSmartCooldown)

```javascript
/*
Add session mood detection to Smart Auto:

Detect user mood from behavior:
- Fast skipping (many songs in 5 min) = Searching mood
  → Reduce cooldowns by 30%
- Slow listening (full songs) = Relaxed mood
  → Normal cooldowns
- Mix of both = Neutral mood
  → Normal cooldowns

Track in sessionData:
- currentMood: 'searching' | 'relaxed' | 'neutral'
- moodConfidence: 0-1

Apply mood modifier to cooldown calculation.
This makes Smart Auto context-aware.
*/
```

---

### 🔟 Smart Notifications System

**File**: `background.js` (add notification logic)

```javascript
/*
Add chrome.notifications for Smart Auto milestones:

Trigger notifications for:
1. Intelligence level up (Learning → Smart → Advanced → Expert)
2. 100th song learned
3. Perfect week (7 days, no manual adjustments needed)
4. Affection score insight ("You really love [Artist]!")

Use:
- chrome.notifications.create()
- Type: 'basic'
- Icon: extension icon
- Title: "Smart Auto Milestone"
- Message: Contextual message
- Buttons: "View Stats" (opens popup)

Show max once per day to avoid spam.
*/
```

---

## 🧪 Testing Prompts

### Unit Test Generation

**File**: Create `tests/smart-auto.test.js`

```javascript
/*
Generate Jest unit tests for Smart Auto functions:

Test calculateAffectionScore():
- High listen duration (0.9) → score ~0.8
- Multiple skips → score decreases
- Quick skips → extra penalty applied
- Edge cases (0 plays, 0 skips)

Test calculateSmartCooldown():
- Loved song (score 0.9) → cooldown ~4-12h
- Neutral song (score 0.5) → cooldown ~80h
- Disliked song (3+ skips) → hard block weeks
- Quick skip penalty → extended block

Test smartAutoDecision():
- Session repeat → skip
- Hard dislike (3+ skips) → skip
- Artist diversity (3+ recent) → skip
- Cooldown active → skip
- All checks pass → allow

Mock chrome.storage.local for storage tests.
*/
```

---

## 🎨 UI Enhancement Prompts

### Animated Affection Meter

**File**: `popup.html` + `popup.css`

```javascript
/*
Add animated circular progress meter for currently playing song's affection score:

HTML structure:
<div class="affection-meter">
  <svg width="80" height="80">
    <circle class="meter-bg" .../>
    <circle class="meter-fill" .../>
  </svg>
  <div class="meter-label">
    <span class="meter-score">78%</span>
    <span class="meter-text">Affection</span>
  </div>
</div>

CSS:
- Circular SVG progress (stroke-dashoffset animation)
- Color gradient based on score:
  * 0-30% = red
  * 31-60% = yellow
  * 61-100% = green
- Smooth transition when score updates
- Pulse animation on score change

Update in real-time as current song plays.
*/
```

---

## 📊 Analytics Prompts

### Smart Auto Analytics Dashboard

**File**: Create `popup/analytics.html`

```javascript
/*
Build comprehensive analytics page for Smart Auto:

Sections:
1. Overview Card
   - Total songs learned
   - Intelligence level with progress bar
   - Days active
   - Avg affection score

2. Learning Timeline
   - Line graph: songs learned per week
   - Bar graph: skips prevented per week
   - Area chart: affection score distribution

3. Top Lists
   - Most loved artists (top 10)
   - Most played songs (top 10)
   - Most skipped songs (top 10)

4. Insights
   - "You love [Genre]" (inferred from artist patterns)
   - "You avoid [Artist]"
   - "Peak listening time: [Time]" (from timestamps)

5. Health Score
   - Overall: Freshness * Satisfaction * Diversity
   - Display as radial chart

Use Chart.js for graphs. Make it beautiful and minimal.
*/
```

---

## 🚀 Performance Optimization Prompts

### Debounced Storage Writes

**File**: `content.js` (optimize saveSong)

```javascript
/*
Add debounced batch storage writes to reduce I/O:

Create saveQueue[] array to hold pending saves.

Function: queueSave(videoId, songInfo, duration)
- Push to saveQueue
- Start 5-second timer
- When timer fires:
  * Batch all queued updates
  * Single chrome.storage.local.set() call
  * Clear queue

Function: flushQueue()
- Immediately process queue (on extension unload)

This reduces storage writes by 80%+ during rapid song changes.
*/
```

---

## 🌍 Multi-Platform Prompts

### Apple Music Web Support

**File**: `content.js` (add Apple Music detection)

```javascript
/*
Add Apple Music (music.apple.com) support:

Function: isAppleMusic()
Returns: hostname includes 'music.apple.com'

Function: getAppleMusicTrackId()
Extract from:
- URL: /song/SONG_NAME/TRACK_ID
- Player: [data-testid="web-chrome-playback-lcd"] attributes
Return: apple:track:TRACK_ID

Function: getAppleMusicSongInfo()
Extract from DOM:
- Title: .web-chrome-playback-lcd__song-name-scroll-inner-text-wrapper
- Artist: .web-chrome-playback-lcd__sub-copy-scroll-inner-text-wrapper
Return: { title, artist, channel: artist }

Function: skipAppleMusicTrack()
Click: button.web-chrome-playback-controls__playback-btn--next
Fallback: MediaTrackNext keyboard event

Integrate with existing platform detection logic.
*/
```

---

## 💡 Creative Feature Prompts

### Smart Auto Voice Announcements

**File**: `content.js` (add voice feedback)

```javascript
/*
Add optional voice announcements using Web Speech API:

Function: announceDecision(decision, songInfo)

If user enables voice feedback in settings:
- On skip: "Skipping, heard too recently"
- On allow: "Playing [Song Title]"
- On new discovery: "New song added to your collection"

Use: window.speechSynthesis.speak(utterance)
Settings:
- Voice: user preference or system default
- Rate: 1.2 (slightly faster than normal)
- Volume: 0.6 (subtle)
- Pitch: 1.0

Add toggle in popup settings.
This creates an accessibility-friendly, hands-free experience.
*/
```

---

## 🎯 Summary

These prompts cover **15+ major features** ready to implement:

✅ **Implemented** (Phase 1):
- Core Smart Auto engine
- UI integration
- Crash protection
- Learning algorithms

🚀 **Ready to Build** (Phase 2):
- Session persistence
- Intelligence indicators
- Taste profiles
- Learning graphs
- Spotify support
- Training mode
- Analytics dashboard
- Multi-platform support
- Voice announcements

Each prompt is designed to be:
- **Copy-paste ready** - No modification needed
- **Contextual** - Includes implementation hints
- **Complete** - Covers HTML, CSS, JS logic
- **Production-ready** - Follows existing code style

Just paste into files and let Copilot work its magic! 🪄✨
