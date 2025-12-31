# Unloop Desktop - Build & Features

Windows desktop application for music discovery tracking.

## What's Included

### Core Features
- 📊 **Statistics Dashboard** - Real-time tracking
- 📜 **History Management** - Complete listening log
- ⚙️ **Settings Panel** - Customizable discovery modes
- 📈 **Analytics** - Top artists and songs

### UI & Experience
- Modern glassmorphism design
- Smooth animations throughout
- Dark/Light theme support
- System tray integration
- Mini player mode
- Global keyboard shortcuts

### Analytics Features
- Discovery score calculation
- Listening pattern analysis
- Time-based suggestions
- Personalized insights
- Mood categorization
- Artist diversity tracking

## Technical Implementation

### Architecture
- Electron for cross-platform desktop
- Vanilla JavaScript (no frameworks)
- Local storage with electron-store
- Custom analytics engine

### Key Components
- `main.js` - Application window management
- `renderer.js` - UI logic and interactions
- `smart-engine.js` - Analytics calculations
- `styles.css` - Modern UI styling

## Building

```bash
# Install dependencies
npm install

# Run development
npm start

# Build installer
npm run build
```

## Features Breakdown

### Discovery Score
Calculated from:
- Song variety (unique ratio)
- Skip efficiency
- Artist diversity

### Insights
- Discovery achievement badges
- Artist diversity recognition
- Listening time milestones
- Loop prevention statistics

### Time-Based Features
- Morning: Upbeat suggestions
- Afternoon: Focus recommendations
- Evening: Relaxing tracks
- Night: Late night vibes

## Data Storage

All data stored locally:
- No cloud required
- No external services
- 100% private
- Export anytime

## License

MIT License

## Author

Sangam - 2026
