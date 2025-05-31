@echo off
setlocal enabledelayedexpansion

echo 🔧 Setting up Git case sensitivity for CrychicDoc repository...
echo 📱 Detected OS: Windows

echo.
echo ⚙️  Configuring Git case sensitivity...
git config core.ignorecase false

echo 🔗 Setting up Git hooks...
git config core.hooksPath .githooks

echo ℹ️  On Windows, hook executability is handled automatically by Git

echo.
echo 🔍 Verifying configuration...

for /f "tokens=*" %%i in ('git config --get core.ignorecase') do set IGNORECASE_VALUE=%%i
for /f "tokens=*" %%i in ('git config --get core.hooksPath') do set HOOKS_PATH=%%i

if "!IGNORECASE_VALUE!"=="false" (
    echo ✅ Success! Git is now configured for case sensitivity.
    echo    core.ignorecase = false
) else (
    echo ❌ Error: Failed to configure case sensitivity.
    echo    Please manually run: git config core.ignorecase false
    exit /b 1
)

if "!HOOKS_PATH!"==".githooks" (
    echo ✅ Success! Git hooks directory configured.
    echo    core.hooksPath = .githooks
) else (
    echo ❌ Error: Failed to configure hooks directory.
    echo    Please manually run: git config core.hooksPath .githooks
)

echo.
echo 🔍 Checking for potential case conflicts...
echo ℹ️  Case conflict check requires additional tools on Windows

echo.
echo 🎉 Git case sensitivity setup complete!
echo.
echo 📝 Important notes for Windows:
echo    • Windows filesystems are typically case-insensitive
echo    • Git case sensitivity helps maintain consistency across platforms
echo    • Use Git Bash or PowerShell for best compatibility
echo    • Consider using WSL for a more Unix-like development environment
echo.
echo    • This configuration only affects your local repository
echo    • The post-checkout hook will automatically run when you checkout branches
echo    • New contributors should run this setup script after cloning
echo    • The .githooks directory is now synced with the repository

pause 