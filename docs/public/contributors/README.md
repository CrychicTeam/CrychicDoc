# Contributors Avatar Cache Directory

This directory contains cached avatar images for project contributors.

## File Naming Convention
- File name: `{github-username}.png`
- Recommended size: 100x100px
- Supported formats: PNG (recommended), JPG

## Usage
The Contributors component will:
1. First attempt to fetch real-time avatars from GitHub
2. Fall back to cached avatars in this directory if GitHub fails
3. Use GitHub's default identicon as final fallback

## Path Configuration
The component uses direct paths: `/contributors/{username}.png`
- No URL prefix is needed
- Files should be directly accessible from the public directory

## Manual Cache Generation
```bash
# Download a contributor's avatar
curl "https://github.com/{username}.png?s=100" \
  -o docs/public/contributors/{username}.png

# Example: Download PickAID's avatar
curl "https://github.com/PickAID.png?s=100" \
  -o docs/public/contributors/PickAID.png
```

## Bulk Cache Generation Script
```bash
#!/bin/bash
# Generate cache for multiple contributors

CONTRIBUTORS=("PickAID" "contributor1" "contributor2")
CACHE_DIR="docs/public/contributors"

mkdir -p "$CACHE_DIR"

for username in "${CONTRIBUTORS[@]}"; do
  echo "Downloading avatar for $username..."
  curl -L "https://github.com/$username.png?s=100" \
    -o "$CACHE_DIR/$username.png" \
    --fail --silent --show-error
  
  if [ $? -eq 0 ]; then
    echo "✓ Successfully cached avatar for $username"
  else
    echo "✗ Failed to cache avatar for $username"
  fi
done
```

## Example Structure
```
docs/public/contributors/
├── PickAID.png
├── contributor1.png
├── contributor2.png
├── username.png
└── README.md
```

## Tips
- Use consistent 100x100px size for best display
- PNG format is recommended for transparency support
- Compress images to reduce file size
- Consider automated cache updates in CI/CD pipelines 