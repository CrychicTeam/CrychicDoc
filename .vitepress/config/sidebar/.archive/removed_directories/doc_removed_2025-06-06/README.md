# Archived Directory: doc

**Archive Date**: 2025-06-06T23:38:11.054Z
**Original Location**: doc/doc
**Reason**: Physical directory no longer exists in docs structure

## Contents
This archive contains both the configuration files and metadata for a directory that was removed from the physical docs structure.

- `config/` - Contains the JSON configuration files (locales.json, order.json, etc.)
- `metadata/` - Contains the metadata files tracking configuration history

## Restoration
To restore this directory:

1. **Recreate the physical directory**: 
   `mkdir -p docs/zh/doc/doc/`

2. **Restore configuration files**:
   `cp -r config/doc/ .vitepress/config/sidebar/zh/doc/doc/`

3. **Restore metadata files**:
   `cp -r metadata/doc/ .vitepress/config/sidebar/.metadata/zh/doc/doc/`

4. **Restart the development server**

## Archive Structure
```
doc_removed_2025-06-06/
├── README.md (this file)
├── config/doc/     # Original config files
└── metadata/doc/   # Original metadata files
```
