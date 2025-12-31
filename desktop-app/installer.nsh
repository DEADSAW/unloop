!macro customInstall
  ; Copy extension files to installation directory
  CreateDirectory "$INSTDIR\extension"
  
  ; Create desktop shortcut for extension installer
  CreateShortCut "$DESKTOP\Install Unloop Extension.lnk" "$INSTDIR\install-extension.bat" "" "$INSTDIR\extension\icons\icon48.png"
  
  ; Create install script
  FileOpen $0 "$INSTDIR\install-extension.bat" w
  FileWrite $0 '@echo off$\r$\n'
  FileWrite $0 'echo ============================================$\r$\n'
  FileWrite $0 'echo Unloop Browser Extension Installer$\r$\n'
  FileWrite $0 'echo ============================================$\r$\n'
  FileWrite $0 'echo.$\r$\n'
  FileWrite $0 'echo Extension Location: %~dp0extension$\r$\n'
  FileWrite $0 'echo.$\r$\n'
  FileWrite $0 'echo To install in Chrome/Brave:$\r$\n'
  FileWrite $0 'echo 1. Open chrome://extensions or brave://extensions$\r$\n'
  FileWrite $0 'echo 2. Enable Developer Mode (toggle in top right)$\r$\n'
  FileWrite $0 'echo 3. Click "Load Unpacked"$\r$\n'
  FileWrite $0 'echo 4. Select folder: %~dp0extension$\r$\n'
  FileWrite $0 'echo.$\r$\n'
  FileWrite $0 'echo Opening extension folder...$\r$\n'
  FileWrite $0 'explorer "%~dp0extension"$\r$\n'
  FileWrite $0 'echo.$\r$\n'
  FileWrite $0 'pause$\r$\n'
  FileClose $0
  
  ; Show option to install extension
  MessageBox MB_YESNO "Installation Complete!$\r$\n$\r$\nWould you like to install the browser extension now?$\r$\n$\r$\nThis will open instructions and the extension folder." IDYES InstallExt IDNO SkipExt
  
  InstallExt:
    ExecShell "open" "$INSTDIR\install-extension.bat"
    Goto Done
  
  SkipExt:
    MessageBox MB_OK "You can install the extension later by running:$\r$\n$\r$\n$INSTDIR\install-extension.bat$\r$\n$\r$\nOr use the desktop shortcut 'Install Unloop Extension'"
  
  Done:
!macroend

!macro customUnInstall
  ; Remove desktop shortcut
  Delete "$DESKTOP\Install Unloop Extension.lnk"
!macroend
