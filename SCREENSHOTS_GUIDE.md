# 📸 Screenshots Guide

This guide explains how to capture and add real screenshots to the README.

## 🎯 Recommended Screenshots

### 1. **Main Popup Dashboard** (Priority: HIGH)
- **What to capture**: The main extension popup showing:
  - Toggle switch (ON state)
  - Status header with emoji
  - Health metrics (Freshness, Intelligence, Variety)
  - Stats cards (Songs Explored, Loops Prevented, etc.)
  - Mode selector with all 5 modes
  - Current song info
  
- **How to capture**:
  1. Install the extension
  2. Play several songs on YouTube Music
  3. Click the Unloop icon in toolbar
  4. Take screenshot (Windows: Win+Shift+S)
  5. Save as `screenshots/popup-dashboard.png`

- **Recommended size**: 400x600px (popup is 400px wide)
- **Theme**: Dark mode (default) for consistency

---

### 2. **Smart Auto Mode Active** (Priority: HIGH)
- **What to capture**: Popup with Smart Auto selected showing:
  - Gradient background on Smart Auto option
  - Intelligence badge visible
  - Learning curve graph
  - Recent wins section

- **How to capture**:
  1. Select "Smart Auto" mode
  2. Let it run for a few hours/days to get data
  3. Open popup
  4. Screenshot with scrolling to get full view
  5. Save as `screenshots/smart-auto-mode.png`

---

### 3. **Toast Notification** (Priority: HIGH)
- **What to capture**: In-page toast notification on YouTube Music showing:
  - "Skipped — already heard this" message
  - Beautiful gradient design
  - Unloop branding

- **How to capture**:
  1. Enable Strict mode
  2. Play a song you've already heard
  3. Wait for auto-skip
  4. Quickly screenshot the toast (it only shows for 2.5s!)
  5. Save as `screenshots/toast-notification.png`

**Tip**: You might need to try multiple times to catch the toast!

---

### 4. **Stats Dashboard with Data** (Priority: MEDIUM)
- **What to capture**: Popup after using for 1+ week showing:
  - High song count (100+)
  - Multiple loops prevented
  - Good freshness percentage (80%+)
  - Session stats populated

- **How to capture**:
  1. Use extension for at least 1 week
  2. Open popup
  3. Screenshot stats section
  4. Save as `screenshots/stats-dashboard.png`

---

### 5. **Mode Selection Grid** (Priority: MEDIUM)
- **What to capture**: Just the mode selector showing all 5 modes:
  - Smart Auto (with gradient)
  - Strict
  - Memory Fade
  - Semi-Strict
  - Artist Smart

- **How to capture**:
  1. Open popup
  2. Crop to just show mode selector
  3. Save as `screenshots/mode-selection.png`

---

### 6. **Settings Panel** (Priority: LOW)
- **What to capture**: Mode-specific settings, e.g.:
  - Memory Fade with hour slider
  - Semi-Strict with song threshold
  - Artist Smart with max plays slider

- **How to capture**:
  1. Select a mode with settings
  2. Screenshot the settings panel
  3. Save as `screenshots/settings-panel.png`

---

### 7. **Theme Toggle** (Priority: LOW)
- **What to capture**: Side-by-side comparison of Dark vs Light mode

- **How to capture**:
  1. Take screenshot in Dark mode
  2. Switch to Light mode
  3. Take another screenshot
  4. Combine in image editor
  5. Save as `screenshots/theme-comparison.png`

---

### 8. **CSV Export Example** (Priority: LOW)
- **What to capture**: Downloaded CSV file opened in Excel/Google Sheets

- **How to capture**:
  1. Click "CSV Export" in popup
  2. Open the downloaded file in Excel
  3. Screenshot showing columns: Platform, Title, Artist, Plays, Skips, etc.
  4. Save as `screenshots/csv-export.png`

---

## 📁 Folder Structure

Create this structure:
```
unloop/
├── screenshots/
│   ├── popup-dashboard.png
│   ├── smart-auto-mode.png
│   ├── toast-notification.png
│   ├── stats-dashboard.png
│   ├── mode-selection.png
│   ├── settings-panel.png
│   ├── theme-comparison.png
│   └── csv-export.png
└── README.md
```

---

## 🎨 Image Guidelines

### **Dimensions**
- **Popup screenshots**: 400px width (actual popup size)
- **Toast screenshots**: Crop to ~500x150px
- **Comparison screenshots**: Max 800px width

### **Format**
- Use PNG format (lossless, supports transparency)
- Compress with TinyPNG to reduce file size
- Target: <200KB per image

### **Quality**
- Use high DPI displays if available (Retina, 4K)
- Ensure text is crisp and readable
- Good lighting (not too dark or washed out)

### **Consistency**
- All screenshots in same theme (Dark mode)
- Same window size for popup screenshots
- Clean desktop background (no distractions)

---

## 🔗 Adding to README

Once you have screenshots, update the README:

### Option 1: GitHub-hosted (recommended)
```markdown
![Popup Dashboard](screenshots/popup-dashboard.png)
```

### Option 2: External hosting (imgur, etc.)
```markdown
![Popup Dashboard](https://i.imgur.com/abc123.png)
```

### Suggested placement in README:

1. **Hero Screenshot**: After the first "Why Unloop?" section
   ```markdown
   ## 🎯 Why Unloop?
   
   [comparison table here]
   
   ![Unloop Dashboard](screenshots/popup-dashboard.png)
   ```

2. **Mode Screenshots**: In the "Five Intelligent Discovery Modes" section
   ```markdown
   ### 🧠 **Five Intelligent Discovery Modes**
   
   ![Mode Selection](screenshots/mode-selection.png)
   ```

3. **Toast Screenshot**: In the "How It Works" section
   ```markdown
   ### 🔔 Toast Notifications
   
   ![Toast Example](screenshots/toast-notification.png)
   ```

4. **Stats Screenshot**: In the "Statistics Dashboard" section
   ```markdown
   ### 📊 **Beautiful Statistics Dashboard**
   
   ![Stats Dashboard](screenshots/stats-dashboard.png)
   ```

---

## ⚡ Quick Screenshot Workflow

1. **Install & Use**: Use the extension for 1-2 weeks naturally
2. **Capture**: Take all screenshots in one session
3. **Edit**: Crop, resize, and optimize images
4. **Organize**: Place in `screenshots/` folder
5. **Update README**: Add markdown image tags
6. **Commit**: Push to GitHub

---

## 🎬 Bonus: GIF/Video Demos

For even better documentation, consider creating:

### **Auto-Skip Demo GIF** (Priority: HIGH)
Record a GIF showing:
1. Song playing on YouTube Music
2. Repeat song comes up
3. Auto-skip happens instantly
4. Toast appears briefly

**Tools**: 
- Windows: ScreenToGif
- Mac: Kap, Gifox
- Chrome: Screen Recorder extension

**Duration**: 5-10 seconds max
**Format**: GIF or MP4
**Size**: < 5MB

### **Full Feature Tour Video** (Priority: LOW)
Record a 1-2 minute video showing:
- Installation process
- Mode selection
- Settings customization
- Real usage on YouTube Music
- Stats dashboard

Upload to YouTube and embed in README:
```markdown
[![Unloop Demo](http://img.youtube.com/vi/VIDEO_ID/0.jpg)](https://www.youtube.com/watch?v=VIDEO_ID)
```

---

## 📝 Checklist

Before considering screenshots "done":

- [ ] Popup dashboard screenshot (main view)
- [ ] Smart Auto mode active
- [ ] Toast notification captured
- [ ] Stats with real data (not zeros)
- [ ] At least one mode settings panel
- [ ] All screenshots optimized (<200KB each)
- [ ] Screenshots added to README with captions
- [ ] README still renders correctly on GitHub
- [ ] Images look good on both desktop and mobile
- [ ] Consider adding 1 GIF demo of auto-skip

---

## 💡 Tips

1. **Use the extension first**: Don't fake screenshots with empty data. Real usage data looks more authentic.

2. **Timing is everything**: Toast screenshots are hardest. Use a screen recording tool and extract frames if needed.

3. **Consistency matters**: All screenshots should have the same theme, size, and style.

4. **Don't overdo it**: 4-6 great screenshots > 20 mediocre ones.

5. **Show, don't tell**: Screenshots should visualize what words describe.

---

**Ready to make your README shine? Start capturing! 📸**
