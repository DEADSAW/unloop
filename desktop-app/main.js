/**
 * Unloop Desktop - Main Process
 * Author: Sangam
 * Electron application for music discovery and analytics
 */

const { app, BrowserWindow, Tray, Menu, ipcMain, globalShortcut, nativeImage } = require('electron');
const path = require('path');
const fs = require('fs');
const Store = require('electron-store');

const store = new Store();

// Helper to get app icon
function getAppIcon() {
  const iconPaths = [
    path.join(__dirname, 'assets', 'icon.png'),
    path.join(__dirname, 'assets', 'icon.ico'),
    path.join(__dirname, 'assets', 'icon.svg')
  ];
  
  for (const iconPath of iconPaths) {
    if (fs.existsSync(iconPath)) {
      return iconPath;
    }
  }
  
  return undefined;
}

let mainWindow = null;
let tray = null;
let miniWindow = null;

// ═══════════════════════════════════════════════════════════════
// APP LIFECYCLE
// ═══════════════════════════════════════════════════════════════

app.whenReady().then(() => {
  createMainWindow();
  createTray();
  registerGlobalShortcuts();
  
  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
      createMainWindow();
    }
  });
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

app.on('will-quit', () => {
  globalShortcut.unregisterAll();
});

// ═══════════════════════════════════════════════════════════════
// MAIN WINDOW
// ═══════════════════════════════════════════════════════════════

function createMainWindow() {
  mainWindow = new BrowserWindow({
    width: 900,
    height: 700,
    minWidth: 600,
    minHeight: 500,
    backgroundColor: '#0a0e1a',
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: false
    },
    frame: true,
    titleBarStyle: 'default',
    icon: getAppIcon(),
    show: false
  });

  mainWindow.loadFile('src/index.html');

  mainWindow.once('ready-to-show', () => {
    mainWindow.show();
  });

  mainWindow.on('close', (event) => {
    if (!app.isQuitting) {
      event.preventDefault();
      mainWindow.hide();
    }
  });

  mainWindow.on('closed', () => {
    mainWindow = null;
  });
}

// ═══════════════════════════════════════════════════════════════
// SYSTEM TRAY
// ═══════════════════════════════════════════════════════════════

function createTray() {
  try {
    // Create tray icon - try multiple formats
    let iconPath = path.join(__dirname, 'assets', 'tray-icon.png');
    if (!fs.existsSync(iconPath)) {
      iconPath = path.join(__dirname, 'assets', 'icon.png');
    }
    
    if (fs.existsSync(iconPath)) {
      tray = new Tray(iconPath);
    } else {
      // Use empty icon if none exists
      const icon = nativeImage.createEmpty();
      tray = new Tray(icon);
    }
  } catch (error) {
    console.log('Tray icon creation skipped:', error.message);
    // Continue without tray icon
    return;
  }

  const contextMenu = Menu.buildFromTemplate([
    {
      label: 'Show Unloop',
      click: () => {
        mainWindow.show();
      }
    },
    {
      label: 'Mini Player',
      click: () => {
        createMiniWindow();
      }
    },
    { type: 'separator' },
    {
      label: 'Enable/Disable',
      type: 'checkbox',
      checked: store.get('enabled', true),
      click: (menuItem) => {
        store.set('enabled', menuItem.checked);
        if (mainWindow) {
          mainWindow.webContents.send('toggle-enabled', menuItem.checked);
        }
      }
    },
    { type: 'separator' },
    {
      label: 'Stats',
      submenu: [
        {
          label: `Songs: ${store.get('stats.listened', 0)}`,
          enabled: false
        },
        {
          label: `Skipped: ${store.get('stats.skipped', 0)}`,
          enabled: false
        }
      ]
    },
    { type: 'separator' },
    {
      label: 'Quit',
      click: () => {
        app.isQuitting = true;
        app.quit();
      }
    }
  ]);

  tray.setToolTip('Unloop - Music Discovery');
  tray.setContextMenu(contextMenu);

  tray.on('click', () => {
    mainWindow.isVisible() ? mainWindow.hide() : mainWindow.show();
  });
}

// ═══════════════════════════════════════════════════════════════
// MINI PLAYER WINDOW
// ═══════════════════════════════════════════════════════════════

function createMiniWindow() {
  if (miniWindow) {
    miniWindow.show();
    return;
  }

  miniWindow = new BrowserWindow({
    width: 350,
    height: 200,
    frame: false,
    resizable: false,
    alwaysOnTop: true,
    backgroundColor: '#0a0e1a',
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: false
    },
    icon: getAppIcon()
  });

  miniWindow.loadFile('src/mini.html');

  miniWindow.on('closed', () => {
    miniWindow = null;
  });
}

// ═══════════════════════════════════════════════════════════════
// GLOBAL SHORTCUTS
// ═══════════════════════════════════════════════════════════════

function registerGlobalShortcuts() {
  // Ctrl+Shift+U - Toggle main window
  globalShortcut.register('CommandOrControl+Shift+U', () => {
    if (mainWindow) {
      mainWindow.isVisible() ? mainWindow.hide() : mainWindow.show();
    }
  });

  // Ctrl+Shift+M - Toggle mini player
  globalShortcut.register('CommandOrControl+Shift+M', () => {
    createMiniWindow();
  });
}

// ═══════════════════════════════════════════════════════════════
// IPC HANDLERS - Storage & Data Management
// ═══════════════════════════════════════════════════════════════

ipcMain.handle('store-get', async (event, key, defaultValue) => {
  return store.get(key, defaultValue);
});

ipcMain.handle('store-set', async (event, key, value) => {
  store.set(key, value);
  updateTrayMenu();
});

ipcMain.handle('store-get-all', async () => {
  return store.store;
});

ipcMain.handle('store-clear', async () => {
  store.clear();
});

ipcMain.on('update-tray-tooltip', (event, text) => {
  if (tray) {
    tray.setToolTip(text);
  }
});

ipcMain.on('show-notification', (event, { title, body }) => {
  // Native notifications will be handled in renderer
});

ipcMain.on('minimize-to-tray', () => {
  if (mainWindow) {
    mainWindow.hide();
  }
});

// ═══════════════════════════════════════════════════════════════
// UTILITY FUNCTIONS
// ═══════════════════════════════════════════════════════════════

function updateTrayMenu() {
  if (!tray) return;
  
  const contextMenu = Menu.buildFromTemplate([
    {
      label: 'Show Unloop',
      click: () => mainWindow.show()
    },
    {
      label: 'Mini Player',
      click: () => createMiniWindow()
    },
    { type: 'separator' },
    {
      label: 'Enable/Disable',
      type: 'checkbox',
      checked: store.get('enabled', true),
      click: (menuItem) => {
        store.set('enabled', menuItem.checked);
        if (mainWindow) {
          mainWindow.webContents.send('toggle-enabled', menuItem.checked);
        }
      }
    },
    { type: 'separator' },
    {
      label: 'Stats',
      submenu: [
        {
          label: `Songs: ${store.get('stats.listened', 0)}`,
          enabled: false
        },
        {
          label: `Skipped: ${store.get('stats.skipped', 0)}`,
          enabled: false
        }
      ]
    },
    { type: 'separator' },
    {
      label: 'Quit',
      click: () => {
        app.isQuitting = true;
        app.quit();
      }
    }
  ]);

  tray.setContextMenu(contextMenu);
}
