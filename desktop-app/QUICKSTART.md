# 🎵 Unloop Desktop App - Quick Start

## What This Is

A professional Windows desktop application for tracking and managing your music discovery across multiple platforms.

### Features

- 📊 **Real-time Statistics** - Track songs, artists, and listening time
- 🎨 **Modern Interface** - Clean, animated UI with dark/light themes
- 📈 **Analytics Dashboard** - View your listening patterns and top tracks
- ⚙️ **Customizable Settings** - Multiple discovery modes
- 🎯 **Mini Player** - Compact always-on-top view
- 📥 **Export Data** - Download your history as CSV
- ⌨️ **Global Shortcuts** - Quick access from anywhere

## Quick Start

### 1. Install Dependencies
```bash
cd desktop-app
npm install
```

### 2. Run the App
```bash
npm start
```

### 3. Build Installer (Optional)
```bash
npm run build
```

## Keyboard Shortcuts

- **Ctrl+Shift+U** - Show/hide window
- **Ctrl+Shift+M** - Open mini player

## Settings

### Discovery Modes
- **Strict** - Never play the same song twice
- **Memory Fade** - Forget songs after specified hours
- **Semi-Strict** - Allow repeats after X new songs
- **Artist Smart** - Limit plays per artist per session

### Appearance
- Dark/Light theme toggle
- Minimal mode option
- Customizable notifications

## Data Management

All data stored locally in:
```
%APPDATA%/unloop-desktop/config.json
```

Export your data anytime from the History page.

## Building Installer

The build process creates a Windows installer:

```bash
npm run build
```

Output: `dist/Unloop Setup.exe`

## Need Help?

Check the main README.md for detailed documentation and troubleshooting.

---

Made by Sangam

