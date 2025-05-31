# PowerShell script for setting up Git case sensitivity on Windows
# Compatible with PowerShell 5.1+ and PowerShell Core

Write-Host "🔧 Setting up Git case sensitivity for CrychicDoc repository..." -ForegroundColor Cyan
Write-Host "📱 Detected OS: Windows (PowerShell)" -ForegroundColor Green

Write-Host ""
Write-Host "⚙️  Configuring Git case sensitivity..." -ForegroundColor Yellow
& git config core.ignorecase false

Write-Host "🔗 Setting up Git hooks..." -ForegroundColor Yellow
& git config core.hooksPath .githooks

Write-Host "ℹ️  On Windows, hook executability is handled automatically by Git" -ForegroundColor Blue

Write-Host ""
Write-Host "🔍 Verifying configuration..." -ForegroundColor Yellow

try {
    $ignorecaseValue = & git config --get core.ignorecase 2>$null
    $hooksPath = & git config --get core.hooksPath 2>$null
    
    if ($ignorecaseValue -eq "false") {
        Write-Host "✅ Success! Git is now configured for case sensitivity." -ForegroundColor Green
        Write-Host "   core.ignorecase = false" -ForegroundColor Green
    } else {
        Write-Host "❌ Error: Failed to configure case sensitivity." -ForegroundColor Red
        Write-Host "   Please manually run: git config core.ignorecase false" -ForegroundColor Red
        exit 1
    }
    
    if ($hooksPath -eq ".githooks") {
        Write-Host "✅ Success! Git hooks directory configured." -ForegroundColor Green
        Write-Host "   core.hooksPath = .githooks" -ForegroundColor Green
    } else {
        Write-Host "❌ Error: Failed to configure hooks directory." -ForegroundColor Red
        Write-Host "   Please manually run: git config core.hooksPath .githooks" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Error: Failed to verify Git configuration." -ForegroundColor Red
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "🔍 Checking for potential case conflicts..." -ForegroundColor Yellow

try {
    $gitFiles = & git ls-files 2>$null
    if ($gitFiles) {
        # Simple case conflict detection for PowerShell
        $duplicates = $gitFiles | Group-Object { $_.ToLower() } | Where-Object { $_.Count -gt 1 }
        
        if ($duplicates) {
            Write-Host "⚠️  Warning: Potential case conflicts detected:" -ForegroundColor Yellow
            foreach ($group in $duplicates) {
                Write-Host "   $($group.Group -join ', ')" -ForegroundColor Yellow
            }
            Write-Host ""
            Write-Host "Please review these files to ensure they don't conflict on case-insensitive filesystems." -ForegroundColor Yellow
        } else {
            Write-Host "✅ No case conflicts detected." -ForegroundColor Green
        }
    } else {
        Write-Host "ℹ️  No Git files found or not in a Git repository" -ForegroundColor Blue
    }
} catch {
    Write-Host "ℹ️  Case conflict check failed: $($_.Exception.Message)" -ForegroundColor Blue
}

Write-Host ""
Write-Host "🎉 Git case sensitivity setup complete!" -ForegroundColor Green
Write-Host ""
Write-Host "📝 Important notes for Windows (PowerShell):" -ForegroundColor Cyan
Write-Host "   • Windows filesystems (NTFS/FAT32) are typically case-insensitive" -ForegroundColor White
Write-Host "   • Git case sensitivity helps maintain consistency across platforms" -ForegroundColor White
Write-Host "   • PowerShell provides excellent Git integration on Windows" -ForegroundColor White
Write-Host "   • Consider using Windows Subsystem for Linux (WSL) for development" -ForegroundColor White
Write-Host ""
Write-Host "   • This configuration only affects your local repository" -ForegroundColor White
Write-Host "   • The post-checkout hook will automatically run when you checkout branches" -ForegroundColor White
Write-Host "   • New contributors should run this setup script after cloning" -ForegroundColor White
Write-Host "   • The .githooks directory is now synced with the repository" -ForegroundColor White

Write-Host ""
Write-Host "Press any key to continue..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown") 