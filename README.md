# 🔄 Unloop - Music Discovery Mode

> **Never hear the same song twice.** Auto-skip repeated tracks on YouTube & YouTube Music for endless music discovery.

![Version](https://img.shields.io/badge/version-1.0.0-6366f1)
![Platform](https://img.shields.io/badge/platform-Chrome-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

---

## ✨ Features

### 🎯 Core Functionality
- **Auto-detection** - Detects currently playing songs on YouTube & YouTube Music
- **Smart memory** - Remembers every song you've listened to
- **Instant skip** - Automatically skips to next when a repeat is detected
- **Zero effort** - Works silently in the background

### 🧠 Four Discovery Modes

| Mode | Description |
|------|-------------|
| **Strict** | Never play the same song again. True discovery mode. |
| **Memory Fade** | Allow repeats after X days (1 week to 1 year). |
| **Semi-Strict** | Allow repeat only after listening to X new songs first. |
| **Artist Smart** | Prevent algorithm from spamming same artist. |

### 📊 Statistics Dashboard
- Songs discovered
- Repeats blocked
- Freshness percentage

### ⚡ Additional Features
- Export/Import listening history
- Clear history option
- Works offline
- 100% private (no server, no tracking)

---

## 🚀 Installation

### Option 1: Load Unpacked (Developer Mode)

1. **Download or clone** this repository
2. **Generate icons** (see below)
3. Open Chrome and go to `chrome://extensions/`
4. Enable **Developer mode** (toggle in top right)
5. Click **Load unpacked**
6. Select the `unloop` folder
7. Done! 🎉

### Option 2: Chrome Web Store
*Coming soon...*

---

## 🎨 Generate Icons

Before loading the extension, you need PNG icons:

1. Open `icons/generate-icons.html` in your browser
2. Click **Download All** button
3. Move the downloaded PNGs to the `icons/` folder

Or create your own 16x16, 32x32, 48x48, and 128x128 PNG icons.

---

## 📁 Project Structure

```
unloop/
├── manifest.json          # Extension configuration
├── background.js          # Service worker (storage, messaging)
├── content.js             # Content script (song detection, skipping)
├── popup/
│   ├── popup.html         # Popup UI structure
│   ├── popup.css          # Popup styles
│   └── popup.js           # Popup logic
├── icons/
│   ├── icon16.png         # Toolbar icon
│   ├── icon32.png         # Extension list
│   ├── icon48.png         # Extensions page
│   ├── icon128.png        # Chrome Web Store
│   └── generate-icons.html # Icon generator tool
└── README.md
```

---

## 🧪 Testing

1. Install the extension locally
2. Go to [YouTube Music](https://music.youtube.com) or [YouTube](https://youtube.com)
3. Play a few songs
4. Try to replay a song you've already heard
5. Watch it automatically skip! ⏭️

### Test Scenarios
- [ ] New song plays normally
- [ ] Repeated song gets skipped
- [ ] Toggle on/off works
- [ ] Mode switching works
- [ ] Stats update correctly
- [ ] Export/Import works
- [ ] Clear history works
- [ ] Persists across browser restarts

---

## ⚙️ How It Works

```
┌──────────────────────────────────────────────────────┐
│                    Content Script                     │
│  ┌─────────────┐    ┌─────────────┐    ┌──────────┐ │
│  │ Detect Song │ -> │ Check Store │ -> │ Skip or  │ │
│  │   Change    │    │   History   │    │  Allow   │ │
│  └─────────────┘    └─────────────┘    └──────────┘ │
└──────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────┐
│              chrome.storage.local                     │
│  ┌─────────────────────────────────────────────────┐ │
│  │ { videoId: { timestamp, artist, title, count } }│ │
│  └─────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────┘
```

---

## 🛠️ Tech Stack

- **Manifest V3** - Latest Chrome extension standard
- **Vanilla JavaScript** - No frameworks, fast & lightweight
- **chrome.storage.local** - Persistent local storage
- **MutationObserver** - Efficient DOM change detection

---

## 🗺️ Roadmap

- [ ] Whitelist favorite songs (never skip these)
- [ ] Blacklist songs (always skip these)
- [ ] Cloud sync via Google account
- [ ] Firefox support
- [ ] Spotify support
- [ ] Listening insights/analytics

---

## 🤝 Contributing

Contributions are welcome! Feel free to:
- Report bugs
- Suggest features
- Submit pull requests

---

## 📜 License

MIT License - feel free to use, modify, and distribute.

---

## 💜 Support

If you find this useful:
- ⭐ Star this repo
- 📢 Share with friends
- 🐛 Report issues

---

**Made with ∞ for music lovers who crave discovery.**