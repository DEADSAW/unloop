# Unloop Desktop App

Professional Windows desktop application for music discovery and tracking.

## Features

- 🎵 **Track music listening** across YouTube, YouTube Music, and Spotify
- 🛡️ **Auto-skip repeated songs** with customizable modes
- 📊 **Advanced analytics** with real-time statistics
- 🌙 **Dark/Light themes** with smooth animations
- 🎯 **Mini player mode** - compact always-on-top view
- ⌨️ **Global hotkeys** for quick access
- 💾 **Local storage** - all data stays on your device
- 📈 **Export history** to CSV
- ✨ **Modern UI** with glassmorphism effects

## Installation

### From Source

1. Install dependencies:
```bash
cd desktop-app
npm install
```

2. Run the app:
```bash
npm start
```

3. Build .exe installer:
```bash
npm run build
```

The installer will be created in the `dist` folder.

## Usage

### Global Shortcuts

- **Ctrl+Shift+U** - Toggle main window
- **Ctrl+Shift+M** - Open mini player

### Discovery Modes

- **Strict** - Never repeat a song (default)
- **Memory Fade** - Forget songs after X hours
- **Semi-Strict** - Allow repeats after X new songs
- **Artist Smart** - Limit same artist per session

### System Tray

Right-click the tray icon for quick access to:
- Show/hide main window
- Open mini player
- Enable/disable discovery mode
- Quick stats
- Quit application

## How It Works

The desktop app provides a comprehensive interface for managing your music discovery:

1. Track your listening across platforms
2. View detailed statistics and insights
3. Customize discovery settings
4. Export your data anytime

## Data Storage

All data is stored locally using `electron-store`:
- Location: `%APPDATA%/unloop-desktop/config.json`
- No cloud sync required
- No external servers
- 100% private

## Development

### Project Structure
```
desktop-app/
├── main.js           # Electron main process
├── src/
│   ├── index.html    # Main UI
│   ├── styles.css    # Styling
│   ├── renderer.js   # UI logic
│   ├── smart-engine.js # Analytics engine
│   └── mini.html     # Mini player
├── assets/           # Icons and images
└── package.json      # Dependencies
```

### Tech Stack
- **Electron** - Desktop framework
- **Vanilla JavaScript** - Lightweight and fast
- **electron-store** - Persistent storage
- **electron-builder** - Packaging

## Building for Distribution

```bash
# Build Windows installer
npm run build

# Build portable version
npm run build:dir
```

## Troubleshooting

### App won't start
- Make sure Node.js 16+ is installed
- Delete `node_modules` and run `npm install` again

### Can't see tray icon
- Check Windows notification area settings
- App may be hidden in overflow menu

## License

MIT License - See LICENSE file

## Author

Created by Sangam

