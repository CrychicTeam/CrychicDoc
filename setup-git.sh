#!/bin/bash

# Simple wrapper script to set up Git case sensitivity
# This script calls the appropriate setup script from the .git-setup directory

echo "🔧 CrychicDoc Git Setup"
echo "======================="

# Check if .git-setup directory exists
if [ ! -d ".git-setup" ]; then
    echo "❌ Error: .git-setup directory not found!"
    echo "   Please make sure you're in the root of the CrychicDoc repository."
    exit 1
fi

# Detect the operating system and run the appropriate script
OS="unknown"
case "$(uname -s 2>/dev/null || echo unknown)" in
    Linux*)     OS="Linux";;
    Darwin*)    OS="macOS";;
    CYGWIN*|MINGW*|MSYS*) OS="Windows";;
    *)          OS="unknown";;
esac

echo "📱 Detected OS: $OS"
echo ""

case "$OS" in
    "Linux"|"macOS"|"unknown")
        echo "🚀 Running Git case sensitivity setup..."
        ./.git-setup/setup-git-case-sensitivity.sh
        ;;
    "Windows")
        echo "🚀 Running Git case sensitivity setup..."
        echo "ℹ️  Note: On Windows, you can also use:"
        echo "   - .git-setup\\setup-git-case-sensitivity.bat (Command Prompt)"
        echo "   - .git-setup\\setup-git-case-sensitivity.ps1 (PowerShell)"
        echo ""
        ./.git-setup/setup-git-case-sensitivity.sh
        ;;
esac 