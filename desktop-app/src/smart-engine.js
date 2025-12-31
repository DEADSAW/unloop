// Advanced Analytics Engine for Unloop
// Custom analytics system for tracking music discovery patterns
// Author: Sangam
// Date: 2026

const { ipcRenderer } = require('electron');

class AnalyticsEngine {
  constructor() {
    this.moodPatterns = {
      energetic: ['workout', 'party', 'dance', 'upbeat', 'edm', 'rock'],
      calm: ['chill', 'ambient', 'acoustic', 'piano', 'relax', 'sleep'],
      focused: ['lo-fi', 'study', 'instrumental', 'classical', 'jazz'],
      emotional: ['sad', 'melancholic', 'ballad', 'slow', 'emotional']
    };
    
    this.listeningPatterns = [];
    this.predictions = [];
  }

  // Detect mood from song metadata
  detectMood(title, artist) {
    const text = `${title} ${artist}`.toLowerCase();
    
    for (const [mood, keywords] of Object.entries(this.moodPatterns)) {
      if (keywords.some(keyword => text.includes(keyword))) {
        return mood;
      }
    }
    
    return 'neutral';
  }

  // Analyze listening time patterns
  analyzeTimePatterns(history) {
    const hourlyStats = Array(24).fill(0);
    
    Object.values(history).forEach(song => {
      if (song.lastPlayed) {
        const hour = new Date(song.lastPlayed).getHours();
        hourlyStats[hour]++;
      }
    });
    
    const peakHour = hourlyStats.indexOf(Math.max(...hourlyStats));
    return {
      peakHour,
      morningListener: hourlyStats.slice(6, 12).reduce((a, b) => a + b, 0) > hourlyStats.slice(18, 24).reduce((a, b) => a + b, 0),
      nightOwl: hourlyStats.slice(22, 24).reduce((a, b) => a + b, 0) + hourlyStats.slice(0, 6).reduce((a, b) => a + b, 0) > hourlyStats.slice(12, 18).reduce((a, b) => a + b, 0)
    };
  }

  // Calculate discovery score
  calculateDiscoveryScore(stats, history) {
    let score = 50; // Base score
    
    // Variety bonus
    const uniqueRatio = stats.totalUniqueSongs / (stats.listened + stats.skipped || 1);
    score += uniqueRatio * 20;
    
    // Skip efficiency (skipping repeats = smart)
    const skipRatio = stats.skipped / (stats.listened + stats.skipped || 1);
    score += skipRatio * 15;
    
    // Artist diversity
    const artistCount = stats.totalArtists || 0;
    score += Math.min(artistCount / 10, 15);
    
    return Math.min(100, Math.round(score));
  }

  // Generate personalized insights based on listening data
  generateInsights(stats, history) {
    const insights = [];
    
    const historyArray = Object.values(history);
    const totalPlays = stats.listened + stats.skipped;
    
    // Discovery rate
    const discoveryRate = totalPlays > 0 ? (stats.totalUniqueSongs / totalPlays) * 100 : 0;
    if (discoveryRate > 80) {
      insights.push({
        icon: '🌟',
        title: 'Discovery Master',
        message: `You're exploring ${discoveryRate.toFixed(0)}% unique songs! Keep discovering!`,
        type: 'success'
      });
    } else if (discoveryRate < 30) {
      insights.push({
        icon: '🔄',
        title: 'Comfort Zone Alert',
        message: 'Try exploring more new music. Your loop rate is high!',
        type: 'warning'
      });
    }
    
    // Artist diversity
    if (stats.totalArtists > 50) {
      insights.push({
        icon: '🎭',
        title: 'Diverse Taste',
        message: `${stats.totalArtists} artists in your library! You love variety!`,
        type: 'success'
      });
    }
    
    // Listening streaks
    const hoursListened = Math.floor(stats.totalListeningSeconds / 3600);
    if (hoursListened > 100) {
      insights.push({
        icon: '🎧',
        title: 'Music Enthusiast',
        message: `${hoursListened}+ hours of music! You're a true audiophile!`,
        type: 'info'
      });
    }
    
    // Protection stats
    if (stats.loopsPrevented > 100) {
      insights.push({
        icon: '🛡️',
        title: 'Loop Fighter',
        message: `Saved you from ${stats.loopsPrevented} repeats! Time saved: ${Math.floor(stats.loopsPrevented * 3 / 60)}+ hours!`,
        type: 'success'
      });
    }
    
    return insights;
  }

  // Smart recommendations
  getRecommendations(history, recentSongs) {
    const recommendations = [];
    
    // Find least played songs
    const sortedByPlays = Object.entries(history)
      .sort((a, b) => (a[1].totalPlays || 0) - (b[1].totalPlays || 0))
      .slice(0, 5);
    
    if (sortedByPlays.length > 0) {
      recommendations.push({
        type: 'rediscover',
        title: 'Rediscover',
        songs: sortedByPlays.map(([id, song]) => ({
          title: song.title,
          artist: song.artist,
          plays: song.totalPlays || 0
        }))
      });
    }
    
    return recommendations;
  }

  // Predict what user might want to listen to
  predictNextPreference(history, currentTime = new Date()) {
    const hour = currentTime.getHours();
    
    if (hour >= 6 && hour < 12) {
      return {
        mood: 'energetic',
        suggestion: 'Start your day with upbeat music! ☀️'
      };
    } else if (hour >= 12 && hour < 17) {
      return {
        mood: 'focused',
        suggestion: 'Perfect time for focus music 🎯'
      };
    } else if (hour >= 17 && hour < 22) {
      return {
        mood: 'calm',
        suggestion: 'Wind down with relaxing tunes 🌅'
      };
    } else {
      return {
        mood: 'calm',
        suggestion: 'Late night vibes 🌙'
      };
    }
  }
}

module.exports = AnalyticsEngine;
