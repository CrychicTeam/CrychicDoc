#!/bin/bash

echo "🔧 Setting up Git case sensitivity for CrychicDoc repository..."

# Detect the operating system
OS="unknown"
case "$(uname -s)" in
    Linux*)     OS="Linux";;
    Darwin*)    OS="macOS";;
    CYGWIN*|MINGW*|MSYS*) OS="Windows";;
    *)          OS="unknown";;
esac

echo "📱 Detected OS: $OS"

# Set core.ignorecase to false for case sensitivity
echo "⚙️  Configuring Git case sensitivity..."
git config core.ignorecase false

# Configure Git to use our custom hooks directory
echo "🔗 Setting up Git hooks..."
git config core.hooksPath .githooks

# Make sure the hooks are executable (Unix-like systems)
if [ "$OS" != "Windows" ]; then
    chmod +x .githooks/* 2>/dev/null || true
else
    echo "ℹ️  On Windows, hook executability is handled automatically by Git"
fi

# Verify the configuration
IGNORECASE_VALUE=$(git config --get core.ignorecase)
HOOKS_PATH=$(git config --get core.hooksPath)

echo ""
echo "🔍 Verifying configuration..."

if [ "$IGNORECASE_VALUE" = "false" ]; then
    echo "✅ Success! Git is now configured for case sensitivity."
    echo "   core.ignorecase = false"
else
    echo "❌ Error: Failed to configure case sensitivity."
    echo "   Please manually run: git config core.ignorecase false"
    exit 1
fi

if [ "$HOOKS_PATH" = ".githooks" ]; then
    echo "✅ Success! Git hooks directory configured."
    echo "   core.hooksPath = .githooks"
else
    echo "❌ Error: Failed to configure hooks directory."
    echo "   Please manually run: git config core.hooksPath .githooks"
fi

# Check for any potential case conflicts in the repository
echo ""
echo "🔍 Checking for potential case conflicts..."

# Find files that might have case conflicts (cross-platform compatible)
if command -v sort >/dev/null 2>&1 && command -v uniq >/dev/null 2>&1; then
    CONFLICTS=$(git ls-files | sort -f | uniq -i -d 2>/dev/null || git ls-files | sort | uniq -d)
    
    if [ -n "$CONFLICTS" ]; then
        echo "⚠️  Warning: Potential case conflicts detected:"
        echo "$CONFLICTS"
        echo ""
        echo "Please review these files to ensure they don't conflict on case-insensitive filesystems."
    else
        echo "✅ No case conflicts detected."
    fi
else
    echo "ℹ️  Case conflict check skipped (sort/uniq not available)"
fi

echo ""
echo "🎉 Git case sensitivity setup complete!"
echo ""
echo "📝 Important notes for $OS:"

case "$OS" in
    "Windows")
        echo "   • Windows filesystems are typically case-insensitive"
        echo "   • Git case sensitivity helps maintain consistency across platforms"
        echo "   • Use Git Bash or PowerShell for best compatibility"
        ;;
    "macOS")
        echo "   • macOS filesystems are case-insensitive by default (APFS/HFS+)"
        echo "   • Git case sensitivity prevents issues when collaborating with Linux users"
        echo "   • Consider using case-sensitive APFS volumes for development"
        ;;
    "Linux")
        echo "   • Linux filesystems are case-sensitive by default"
        echo "   • This configuration ensures consistency with other platforms"
        echo "   • You're already on the most Git-friendly platform!"
        ;;
    *)
        echo "   • Unknown OS detected, basic configuration applied"
        ;;
esac

echo ""
echo "   • This configuration only affects your local repository"
echo "   • The post-checkout hook will automatically run when you checkout branches"
echo "   • New contributors should run this setup script after cloning"
echo "   • The .githooks directory is now synced with the repository" 