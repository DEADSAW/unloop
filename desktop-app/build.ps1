# Unloop Desktop - Build Script
Write-Host "🎵 Unloop Desktop App - Build Process" -ForegroundColor Cyan
Write-Host "======================================`n" -ForegroundColor Cyan

# Check if we're in the right directory
if (!(Test-Path "package.json")) {
    Write-Host "❌ Error: Not in desktop-app directory!" -ForegroundColor Red
    Write-Host "Please run: cd desktop-app" -ForegroundColor Yellow
    exit 1
}

# Step 1: Check for icons
Write-Host "📷 Step 1: Checking for icons..." -ForegroundColor Yellow
$hasIcons = (Test-Path "assets\icon.png") -and (Test-Path "assets\tray-icon.png")

if (!$hasIcons) {
    Write-Host "⚠️  Icons not found! Opening icon generator..." -ForegroundColor Yellow
    Start-Process "assets\generate-icons.html"
    Write-Host "`n📝 Instructions:" -ForegroundColor Cyan
    Write-Host "1. Click 'Download Icon' button in browser" -ForegroundColor White
    Write-Host "2. Save as 'icon.png' in the assets folder" -ForegroundColor White
    Write-Host "3. Click 'Download Tray Icon' button" -ForegroundColor White
    Write-Host "4. Save as 'tray-icon.png' in the assets folder" -ForegroundColor White
    Write-Host "5. Run this script again`n" -ForegroundColor White
    
    Read-Host "Press Enter after downloading icons"
    
    # Check again
    if (!(Test-Path "assets\icon.png") -or !(Test-Path "assets\tray-icon.png")) {
        Write-Host "❌ Icons still not found. Please download them first." -ForegroundColor Red
        exit 1
    }
}

Write-Host "✅ Icons found!`n" -ForegroundColor Green

# Step 2: Install dependencies
Write-Host "📦 Step 2: Checking dependencies..." -ForegroundColor Yellow
if (!(Test-Path "node_modules")) {
    Write-Host "Installing dependencies..." -ForegroundColor Yellow
    npm install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Failed to install dependencies" -ForegroundColor Red
        exit 1
    }
}
Write-Host "✅ Dependencies ready!`n" -ForegroundColor Green

# Step 3: Test the app
Write-Host "🧪 Step 3: Would you like to test the app first? (Y/N)" -ForegroundColor Yellow
$test = Read-Host
if ($test -eq "Y" -or $test -eq "y") {
    Write-Host "Starting app... (Close it when done testing)`n" -ForegroundColor Cyan
    npm start
}

# Step 4: Build
Write-Host "`n🔨 Step 4: Building .exe installer..." -ForegroundColor Yellow
Write-Host "This may take a few minutes...`n" -ForegroundColor White

npm run build

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n🎉 SUCCESS! Your app is built!" -ForegroundColor Green
    Write-Host "======================================" -ForegroundColor Cyan
    Write-Host "📁 Installer location: dist\" -ForegroundColor White
    Write-Host "📦 File: Unloop Setup.exe`n" -ForegroundColor White
    
    # Open dist folder
    $openDist = Read-Host "Open dist folder? (Y/N)"
    if ($openDist -eq "Y" -or $openDist -eq "y") {
        Start-Process "dist"
    }
    
    Write-Host "`n✨ You can now:" -ForegroundColor Cyan
    Write-Host "   1. Install the app by running the setup.exe" -ForegroundColor White
    Write-Host "   2. Share the installer with others" -ForegroundColor White
    Write-Host "   3. The app will be in your Start Menu`n" -ForegroundColor White
} else {
    Write-Host "`n❌ Build failed. Check the errors above." -ForegroundColor Red
}

Write-Host "Press any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
