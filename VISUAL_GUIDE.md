# 🎨 Unloop - Visual Feature Overview

> **A beautiful visual guide to all Unloop features and how they work**

---

## 🔄 The Loop Problem

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│         YOU                    ALGORITHM                    │
│                                                             │
│    "Play me music"  ────────>  [Finds what you liked]      │
│                                                             │
│    Listens          <────────  [Plays same 10 songs]       │
│                                                             │
│    Gets bored       ────────>  [You didn't skip, so...]    │
│                                                             │
│    Forced to skip   <────────  [Plays same 10 again!]      │
│                                                             │
│    😔 Frustrated     ────────>  [We think you love these!]  │
│                                                             │
└─────────────────────────────────────────────────────────────┘

                         ❌ THE LOOP
```

---

## ✨ The Unloop Solution

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│         YOU                    UNLOOP                       │
│                                                             │
│    "Play me music"  ────────>  [Tracking enabled]          │
│                                                             │
│    Listens          <────────  [Song A plays]              │
│         📝                     [Saved to history]           │
│                                                             │
│    Continues        <────────  [Song B plays]              │
│         📝                     [Saved to history]           │
│                                                             │
│    Relaxed          <────────  [Song A tries to play]      │
│                                [❌ BLOCKED - already heard] │
│                                [⏭️ Skips to Song C]         │
│                                                             │
│    😊 Discovering   <────────  [Fresh music forever!]       │
│                                                             │
└─────────────────────────────────────────────────────────────┘

                    ✅ FREEDOM FROM LOOPS
```

---

## 🧠 Five Discovery Modes Explained

### 1️⃣ Smart Auto (AI-Powered)

```
Song plays → Tracks how long you listen → Calculates Affection Score

┌─────────────────────────────────────────────────────────────┐
│                                                             │
│  Affection Score = 0.6 × Listen% + 0.3 × PlayRatio        │
│                   - 0.1 × QuickSkips                       │
│                                                             │
│  ┌────────────┬─────────────┬────────────────────────┐    │
│  │   Score    │  Cooldown   │      Meaning           │    │
│  ├────────────┼─────────────┼────────────────────────┤    │
│  │ 0.8 - 1.0  │  4-12 hrs   │ 😍 Loved - back soon  │    │
│  │ 0.5 - 0.8  │  24-48 hrs  │ 😊 Liked              │    │
│  │ 0.3 - 0.5  │  ~80 hrs    │ 😐 Neutral            │    │
│  │ 0.0 - 0.3  │  140-160hrs │ 😔 Disliked           │    │
│  │  3+ skips  │  90 days!   │ 😡 Hated - stay away! │    │
│  └────────────┴─────────────┴────────────────────────┘    │
│                                                             │
└─────────────────────────────────────────────────────────────┘

💡 Smart Auto learns from your behavior and adapts automatically!
```

---

### 2️⃣ Strict (Pure Discovery)

```
┌─────────────────────────────────────────────────────────┐
│                                                         │
│   Song Database: [A] [B] [C] [D] [E] [F] [G] [H] ...  │
│                                                         │
│   Monday:    [A✓] [B✓] [C✓] [D✓] [E✓] [F✓]           │
│                                                         │
│   Tuesday:   [G✓] [H✓] [I✓] [J✓] [K✓] [L✓]           │
│                                                         │
│   Wednesday: [M✓] [N✓] [O✓] [P✓] [Q✓] [R✓]           │
│                                                         │
│   ❌ Try to play [A] again → BLOCKED FOREVER           │
│   ✅ Only plays [S] and beyond                         │
│                                                         │
│   Result: Infinite discovery, zero repeats 🚀          │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

### 3️⃣ Memory Fade (Time-Based)

```
Timeline:
├──────────────────────────────────────────────────────────►
0hrs       24hrs      48hrs      72hrs      96hrs    120hrs

Song A plays: ●
              │
              │ Cooldown Period (e.g., 72 hours)
              ├──────────────────────┐
              │                      │
              │ ❌ Blocked           ✅ Allowed again
              │ during cooldown      │ after cooldown
              │                      │
              0────────────────────►72hrs

💡 Set cooldown: 1 hour to 1 year (365 days)
💡 Perfect for: Favorite songs you want occasionally
```

---

### 4️⃣ Semi-Strict (Discovery Quota)

```
Threshold: 5 new songs before repeat allowed

┌─────────────────────────────────────────────────────────┐
│                                                         │
│  Play: [A] → Counter: 0                                │
│  Play: [B] → Counter: 1 (1 new since A)                │
│  Play: [C] → Counter: 2 (2 new since A)                │
│  Play: [D] → Counter: 3 (3 new since A)                │
│  Play: [E] → Counter: 4 (4 new since A)                │
│  Play: [F] → Counter: 5 (5 new since A)                │
│                                                         │
│  Try [A] again: ✅ ALLOWED! (5 new songs discovered)   │
│  Counter resets: 0                                      │
│                                                         │
│  Play: [B] again: ❌ BLOCKED (need 5 new first)        │
│                                                         │
└─────────────────────────────────────────────────────────┘

💡 Forces discovery while allowing eventual repeats
💡 Gamification: "Discover 10 before repeating!"
```

---

### 5️⃣ Artist Smart (Rotation Guard)

```
Max Artist Plays: 3 per session

Session:
  [Taylor Swift - Song 1]  → Play #1 ✅
  [Ed Sheeran - Song 2]    → Play #1 ✅
  [Taylor Swift - Song 3]  → Play #2 ✅
  [Billie Eilish - Song 4] → Play #1 ✅
  [Taylor Swift - Song 5]  → Play #3 ✅
  [Taylor Swift - Song 6]  → ❌ BLOCKED! (Max 3 reached)
  
  ↓ Skips to different artist
  
  [Ariana Grande - Song 7] → Play #1 ✅
  
💡 Prevents "same artist marathon"
💡 Still allows repeats, just from variety of artists
💡 Counter resets daily (session-based)
```

---

## 📊 Stats Dashboard Explained

```
╔════════════════════════════════════════════════════════╗
║                  📊 STATISTICS                        ║
╠════════════════════════════════════════════════════════╣
║                                                        ║
║  🎵 Songs Explored: 847                               ║
║     Every unique track you've discovered              ║
║                                                        ║
║  ⏭️ Loops Prevented: 234                              ║
║     Times Unloop saved you from repeats               ║
║                                                        ║
║  🎨 Artist Discovery: 67                              ║
║     Unique artists in your history                    ║
║                                                        ║
║  ⏱️ This Session: 12 songs                            ║
║     Discovered since browser opened                   ║
║                                                        ║
║  🎯 Freshness: 94%                                    ║
║     (Unique Songs ÷ Total Plays) × 100                ║
║     Higher = More discovery!                          ║
║                                                        ║
║  🧠 Intelligence: 73%                                 ║
║     How well Smart Auto understands you               ║
║     Grows with usage over time                        ║
║                                                        ║
║  🎨 Variety: 89%                                      ║
║     Artist diversity in your listening                ║
║     (Unique Artists ÷ Total Songs)                    ║
║                                                        ║
╚════════════════════════════════════════════════════════╝
```

---

## 🔔 Toast Notification System

```
Scenario 1: Repeat Detected (Strict Mode)
┌─────────────────────────────────────────────────────┐
│  🎵 Unloop                                          │
│  Skipped — already heard this                       │
│  Keeping your discoveries fresh ✨                  │
└─────────────────────────────────────────────────────┘
     ↓
Auto-skips to next song in 400ms


Scenario 2: Smart Auto Approval
┌─────────────────────────────────────────────────────┐
│  🧠 Unloop                                          │
│  Smart choice — letting this one play               │
│  You seem to love this artist! 💜                   │
└─────────────────────────────────────────────────────┘
     ↓
Song plays normally


Scenario 3: Achievement Unlocked
┌─────────────────────────────────────────────────────┐
│  🎉 Achievement Unlocked!                           │
│  250 loops prevented — Discovery Master             │
│  Amazing! Keep exploring! 🏆                        │
└─────────────────────────────────────────────────────┘
     ↓
Motivation boost!


Scenario 4: Artist Variety Protection
┌─────────────────────────────────────────────────────┐
│  🎨 Unloop                                          │
│  Keeping artist variety                             │
│  Heard enough Taylor Swift for now 😎              │
└─────────────────────────────────────────────────────┘
     ↓
Skips to different artist
```

---

## 🎮 Whitelist & Blacklist

```
WHITELIST (⚪ Always Play)
──────────────────────────────────────────────────
Override ALL modes and rules

  [Song A] ────> ⚪ Whitelisted
       │
       │  Even if played 100 times...
       │  Even in Strict mode...
       │  Even if skipped before...
       ↓
  ✅ ALWAYS PLAYS

Use case: All-time favorite songs you never tire of


BLACKLIST (⚫ Never Play)
──────────────────────────────────────────────────
Override ALL modes and rules

  [Song B] ────> ⚫ Blacklisted
       │
       │  Even if never played...
       │  Even if whitelisted before...
       │  Even in Memory Fade mode...
       ↓
  ❌ ALWAYS SKIPS

Use case: Songs you hate but algorithms keep showing
```

---

## 💾 Data Export/Import

```
EXPORT (Backup Your History)
═════════════════════════════════════════════════

  Your Data                    Export Formats
      │                             │
      ├──────> JSON      ──>   Complete backup
      │        (full)           All metadata
      │                         Import anywhere
      │
      └──────> CSV       ──>   Spreadsheet format
               (table)          Excel/Google Sheets
                                Data analysis


JSON Structure:
{
  "songHistory": {
    "abc123": {
      "title": "Ocean Eyes",
      "artist": "Billie Eilish",
      "totalPlays": 5,
      "avgListenDuration": 0.85,
      "lastPlayed": 1735689600000
    }
  },
  "stats": {...},
  "settings": {...}
}


CSV Structure:
Platform      | Title        | Artist          | Plays | Skips | Last Played
─────────────────────────────────────────────────────────────────────────────
YouTube Music | Ocean Eyes   | Billie Eilish   | 5     | 1     | 2025-12-27
Spotify       | Blinding...  | The Weeknd      | 3     | 0     | 2025-12-26
YouTube       | Bohemian...  | Queen           | 2     | 2     | 2025-12-25


IMPORT (Restore Your History)
═════════════════════════════════════════════════

  Backup File
      │
      │ Drag & Drop or Click "Import"
      ↓
  [Validation]
      │
      ├─> ✅ Valid   ──> Merge with existing data
      │                  Update stats
      │                  Restore settings
      │
      └─> ❌ Invalid ──> Show error
                          Preserve current data
```

---

## 🎨 Theme System

```
┌───────────────────────────────────────────────────────┐
│                                                       │
│  DARK MODE (Default)          LIGHT MODE             │
│  ═══════════════════          ══════════             │
│                                                       │
│  Background: #0f0f0f          Background: #ffffff    │
│  Text: #ffffff                Text: #0f0f0f          │
│  Accent: Purple               Accent: Blue           │
│  Cards: #1a1a1a               Cards: #f5f5f5         │
│                                                       │
│  ┌─────────────┐              ┌─────────────┐        │
│  │ 🌙 Perfect  │              │ ☀️ Perfect   │        │
│  │ for night   │              │ for day      │        │
│  │ listening   │              │ listening    │        │
│  └─────────────┘              └─────────────┘        │
│                                                       │
│                AUTO MODE                              │
│                ═════════                              │
│                                                       │
│  Syncs with system theme                             │
│  Automatically switches at sunrise/sunset            │
│                                                       │
└───────────────────────────────────────────────────────┘
```

---

## ⚙️ Minimal Mode

```
FULL MODE                          MINIMAL MODE
═════════════════════              ════════════════

[Main Toggle]                      [Main Toggle]
[Status Header]                    [Status Header]
[Health Grid] ◄───────────────────X Hidden
[Intelligence Badge] ◄─────────────X Hidden
[Stats Cards]                      [Stats Cards]
[Mode Selector]                    [Mode Selector]
[Current Song]                     [Current Song]
[Recent Wins] ◄────────────────────X Hidden
[Learning Graphs] ◄────────────────X Hidden
[Data Management]                  [Data Management]
[Customization] ◄──────────────────X Hidden

Result: Clean, zen, focus-friendly interface
Perfect for: Work/study sessions, minimal distractions
```

---

## 🏆 Achievement System

```
LOOP MILESTONES
═══════════════════════════════════════════════════

   25 loops → 🎉 "Nice! You've avoided 25 repeats"
   50 loops → 🌟 "Awesome! 50 repeats dodged"
  100 loops → 🎊 "Incredible! 100 loops prevented"
  250 loops → 🏆 "Discovery Master!"
  500 loops → 💎 "Legendary Champion!"
  
  Progress Bar:
  [████████████░░░░░░░░] 234/500 loops


INTELLIGENCE MILESTONES (Smart Auto)
═══════════════════════════════════════════════════

  50% → 🧠 "I'm starting to understand..."
  70% → 😎 "I know you pretty well now"
  85% → 🔥 "We're totally in sync!"
  95% → ✨ "I know you better than yourself!"
  
  Intelligence Score:
  [█████████████████░░░] 73%


EXPLORER MILESTONE
═══════════════════════════════════════════════════

  500 songs → 🗺️ "Explorer Extraordinaire!"
  
  Discovery Progress:
  [█████████████████████████████░] 847/1000
```

---

## 🔒 Privacy Architecture

```
┌────────────────────────────────────────────────────────┐
│                                                        │
│   YOUR COMPUTER                     THE INTERNET      │
│   ═════════════                     ════════════      │
│                                                        │
│   ┌──────────┐                                        │
│   │  Unloop  │                      [No Connection]   │
│   │Extension │              ×                         │
│   └─────┬────┘              ×                         │
│         │                   ×                         │
│         │ Saves to          ×       ┌────────────┐   │
│         ↓                   ×  ────X│  Servers   │   │
│   ┌──────────┐              ×       │ (None!)    │   │
│   │ chrome.  │              ×       └────────────┘   │
│   │ storage. │              ×                         │
│   │  local   │              ×       ┌────────────┐   │
│   └──────────┘              ×  ────X│ Analytics  │   │
│                             ×       │ (None!)    │   │
│   Your Data:                ×       └────────────┘   │
│   • Song history            ×                         │
│   • Settings                ×       ┌────────────┐   │
│   • Stats                   ×  ────X│  Tracking  │   │
│                             ×       │ (None!)    │   │
│   NEVER leaves your device! ×       └────────────┘   │
│                                                        │
└────────────────────────────────────────────────────────┘

✅ 100% Local Processing
✅ Zero External Calls
✅ No Telemetry
✅ Open Source
```

---

## 🚀 Performance Impact

```
CPU Usage Over Time
═══════════════════════════════════════════════

100%│
    │
 50%│
    │                                      Other Apps
  5%│                                    ███████████████
    │                                    ███████████████
  0%│━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━███████████████
    └────────────────────────────────────────────────────►
                                          ↑
                                       Unloop
                                    (flatline at 0%)


Memory Usage
═══════════════════════════════════════════════

          Chrome Tab: ~200MB     ████████████████████
   Unloop Extension: <5MB        ██
                                    ↑
                              Minimal impact!


Storage Growth (Per Month)
═══════════════════════════════════════════════

  Month 1: ~1MB   (100 songs discovered)
  Month 2: ~3MB   (300 songs discovered)  
  Month 3: ~5MB   (500 songs discovered)
  Year 1:  ~15MB  (1500 songs discovered)
  
  Even after 1 year: Less than 1 YouTube video!
```

---

**🎵 Ready to never hear the same song twice? Install Unloop today! 🔄**

*Made with 💜 by music lovers, for music lovers*
