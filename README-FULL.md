# Unloop - Music Discovery System

A comprehensive music discovery tool available as both a browser extension and desktop application.

## Components

### Browser Extension
Chrome/Edge/Firefox extension that prevents music loops across YouTube, YouTube Music, and Spotify.

**Features:**
- Auto-skip repeated songs
- Multiple discovery modes
- Whitelist/blacklist management
- Session tracking
- Statistics tracking

### Desktop Application
Professional Windows desktop app for advanced analytics and control.

**Features:**
- Real-time statistics dashboard
- Advanced analytics and insights
- Multiple pages (Dashboard, History, Settings, Analytics)
- Export data to CSV
- System tray integration
- Global keyboard shortcuts
- Mini player mode
- Dark/Light themes

## Getting Started

### Browser Extension
1. Load the extension in your browser
2. Navigate to YouTube, YouTube Music, or Spotify
3. The extension automatically manages your music discovery

### Desktop App
```bash
cd desktop-app
npm install
npm start
```

To build installer:
```bash
npm run build
```

## Discovery Modes

- **Strict** - Never repeat a song
- **Memory Fade** - Forget songs after specified hours
- **Semi-Strict** - Allow repeats after X new songs
- **Artist Smart** - Limit plays per artist per session

## Technology Stack

**Extension:**
- Vanilla JavaScript
- Chrome Storage API
- Content Scripts
- Service Worker

**Desktop:**
- Electron
- Node.js
- electron-store
- electron-builder

## Data & Privacy

All data is stored locally:
- No cloud storage
- No external servers
- No tracking
- Export anytime

## Development

### Extension Development
```bash
# Load unpacked extension in Chrome
# Navigate to chrome://extensions
# Enable Developer Mode
# Click "Load Unpacked" and select the extension folder
```

### Desktop Development
```bash
cd desktop-app
npm install
npm start
```

## Building

### Desktop Installer
```bash
cd desktop-app
npm run build
```

Output: `desktop-app/dist/Unloop Setup.exe`

## Features

### Analytics
- Discovery score calculation
- Listening pattern analysis
- Time-based suggestions
- Artist diversity tracking
- Mood categorization
- Personalized insights

### UI/UX
- Modern glassmorphism design
- Smooth animations
- Responsive layouts
- Interactive visualizations
- Multi-theme support

## License

MIT License

## Author

Sangam - 2026

## Contributing

This is a personal project. Feel free to fork and modify for your own use.

## Roadmap

- [ ] Mobile companion app
- [ ] Cloud sync option
- [ ] Playlist generation
- [ ] Social features
- [ ] More streaming platforms
- [ ] Advanced ML recommendations

## Support

For issues or questions, check the documentation in each component folder.
