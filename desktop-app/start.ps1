# Quick Start Script
Write-Host "🎵 Unloop Desktop - Quick Start" -ForegroundColor Cyan
Write-Host "================================`n" -ForegroundColor Cyan

if (!(Test-Path "package.json")) {
    Write-Host "❌ Error: Not in desktop-app directory!" -ForegroundColor Red
    exit 1
}

# Check icons
if (!(Test-Path "assets\icon.png")) {
    Write-Host "⚠️  Tip: Add icon.png to assets folder for proper icon" -ForegroundColor Yellow
}

Write-Host "🚀 Starting Unloop Desktop App...`n" -ForegroundColor Green
npm start
