import path from 'node:path';
import { normalizePathSeparators } from '../shared/objectUtils';
import { JsonFileHandler, JsonOverrideFileType } from './JsonFileHandler';
import { MetadataManager } from './MetadataManager';
import { FileSystem } from '../shared/FileSystem';

/**
 * @file BackupPackageService.ts
 * @description Creates self-contained backup packages that are user-friendly and easy to restore.
 * 
 * Archive Structure:
 * .archive/
 * ├── README.md                                    # How to use archives
 * ├── removed_directories/
 * │   └── Introduction-copy_removed_2025-06-06/
 * │       ├── RESTORE.md                          # Step-by-step restore guide  
 * │       ├── backup_info.json                   # What was removed, when, why
 * │       ├── config/                            # All JSON configs combined
 * │       │   ├── locales.json
 * │       │   ├── hidden.json  
 * │       │   ├── collapsed.json
 * │       │   └── order.json
 * │       └── metadata/                          # Metadata for reference
 * └── inactive_entries/
 *     └── kubejs_cleanup_2025-06-06/
 *         ├── RESTORE.md
 *         ├── cleanup_info.json
 *         └── archived_entries.json             # User-set configs only
 */
export class BackupPackageService {
    private jsonFileHandler: JsonFileHandler;
    private metadataManager: MetadataManager;
    private fs: FileSystem;
    private docsPath: string;
    private archivePath: string;

    constructor(jsonFileHandler: JsonFileHandler, metadataManager: MetadataManager, fs: FileSystem, docsPath: string) {
        this.jsonFileHandler = jsonFileHandler;
        this.metadataManager = metadataManager;
        this.fs = fs;
        this.docsPath = docsPath;
        this.archivePath = normalizePathSeparators(path.join(this.docsPath, '..', '.vitepress', 'config', 'sidebar', '.archive'));
    }

    /**
     * Creates a backup package for a removed directory with all its configs and documentation.
     */
    public async createRemovedDirectoryBackup(
        lang: string, 
        dirSignature: string, 
        reason: string = 'Directory no longer exists in filesystem'
    ): Promise<void> {
        const timestamp = new Date().toISOString().split('T')[0]; // YYYY-MM-DD format
        const dirName = path.basename(dirSignature);
        const packageName = `${dirName}_removed_${timestamp}`;
        
        const packagePath = normalizePathSeparators(path.join(
            this.archivePath, 'removed_directories', packageName
        ));
        
        console.log(`Creating backup package for removed directory: ${dirSignature} -> ${packageName}`);
        
        // Ensure package directory exists
        await this.fs.ensureDir(packagePath);
        
        // Create backup info
        const backupInfo = {
            type: 'removed_directory',
            original_path: dirSignature,
            language: lang,
            removed_date: new Date().toISOString(),
            reason: reason,
            package_name: packageName,
            contents: {
                config_files: [] as string[],
                metadata_files: [] as string[],
                nested_directories: [] as string[]
            }
        };
        
        // Create merged config and metadata objects to store all configurations
        const mergedConfigs: Record<string, Record<string, any>> = {
            'locales': {},
            'collapsed': {},  
            'hidden': {},
            'order': {}
        };
        
        const mergedMetadata: Record<string, Record<string, any>> = {
            'locales': {},
            'collapsed': {},
            'hidden': {},
            'order': {}
        };
        
        // Recursively collect all configurations from this directory and its subdirectories
        await this.recursivelyCollectConfigurations(
            lang,
            dirSignature,
            mergedConfigs,
            mergedMetadata,
            backupInfo,
            ''  // relative path within the directory structure
        );
        
        // Write the merged config files
        const configPath = path.join(packagePath, 'config');
        await this.fs.ensureDir(configPath);
        
        const overrideTypes: JsonOverrideFileType[] = ['locales', 'collapsed', 'hidden', 'order'];
        for (const type of overrideTypes) {
            if (Object.keys(mergedConfigs[type]).length > 0) {
                const configFilePath = path.join(configPath, `${type}.json`);
                await this.fs.writeFile(configFilePath, JSON.stringify(mergedConfigs[type], null, 2));
                backupInfo.contents.config_files.push(`${type}.json`);
            }
        }
        
        // Write the merged metadata files
        const metadataPath = path.join(packagePath, 'metadata');
        await this.fs.ensureDir(metadataPath);
        
        for (const type of overrideTypes) {
            if (Object.keys(mergedMetadata[type]).length > 0) {
                const metadataFilePath = path.join(metadataPath, `${type}.json`);
                await this.fs.writeFile(metadataFilePath, JSON.stringify(mergedMetadata[type], null, 2));
                backupInfo.contents.metadata_files.push(`${type}.json`);
            }
        }
        
        // Write backup info
        const backupInfoPath = path.join(packagePath, 'backup_info.json');
        await this.fs.writeFile(backupInfoPath, JSON.stringify(backupInfo, null, 2));
        
        // Create restore instructions
        await this.createRestoreInstructions(packagePath, 'removed_directory', backupInfo);
        
        // Update main README
        await this.updateMainReadme();
        
        console.log(`✅ Created backup package: ${packageName} (${backupInfo.contents.nested_directories.length} nested directories)`);
    }
    
    /**
     * Recursively collects all configurations from a directory structure into merged objects.
     */
    private async recursivelyCollectConfigurations(
        lang: string,
        configDirSignature: string,
        mergedConfigs: Record<string, Record<string, any>>,
        mergedMetadata: Record<string, Record<string, any>>,
        backupInfo: any,
        relativePath: string
    ): Promise<void> {
        const overrideTypes: JsonOverrideFileType[] = ['locales', 'collapsed', 'hidden', 'order'];
        
        // Collect config files for current directory
        for (const type of overrideTypes) {
            try {
                const sourceData = await this.jsonFileHandler.readJsonFile(type, lang, configDirSignature);
                
                if (Object.keys(sourceData).length > 0) {
                    // Merge data into the combined config, prefixing keys with relative path if needed
                    for (const [key, value] of Object.entries(sourceData)) {
                        const fullKey = relativePath && key !== '_self_' 
                            ? `${relativePath}${key}` 
                            : relativePath && key === '_self_'
                            ? `${relativePath}_self_`  // _self_ from subdirectory becomes "SubDir/_self_"
                            : key;  // Root _self_ stays as "_self_"
                        mergedConfigs[type][fullKey] = value;
                    }
                    
                    // Delete original after backing up
                    await this.jsonFileHandler.deleteJsonFile(type, lang, configDirSignature);
                }
            } catch (error) {
                // File might not exist, continue
            }
        }
        
        // Collect metadata files for current directory
        for (const type of overrideTypes) {
            try {
                const sourceMetadata = await this.metadataManager.readMetadata(type, lang, configDirSignature);
                
                if (Object.keys(sourceMetadata).length > 0) {
                    // Merge metadata into the combined metadata, prefixing keys with relative path if needed
                    for (const [key, value] of Object.entries(sourceMetadata)) {
                        const fullKey = relativePath && key !== '_self_' 
                            ? `${relativePath}${key}` 
                            : key;
                        mergedMetadata[type][fullKey] = value;
                    }
                    
                    // Delete original metadata after backing up
                    await this.metadataManager.deleteMetadata(type, lang, configDirSignature);
                }
            } catch (error) {
                // File might not exist, continue
            }
        }
        
        // Find and recursively collect from subdirectories
        try {
            const configBasePath = normalizePathSeparators(path.join(
                this.docsPath, '..', '.vitepress', 'config', 'sidebar', lang
            ));
            
            const currentConfigPath = normalizePathSeparators(path.join(configBasePath, configDirSignature));
            
            if (await this.fs.exists(currentConfigPath)) {
                const items = await this.fs.readDir(currentConfigPath);
                
                for (const item of items) {
                    const itemName = typeof item === 'string' ? item : item.name;
                    
                    // Skip hidden files/directories
                    if (itemName.startsWith('.')) {
                        continue;
                    }
                    
                    const itemPath = normalizePathSeparators(path.join(currentConfigPath, itemName));
                    const stat = await this.fs.stat(itemPath);
                    
                    if (stat.isDirectory()) {
                        const subDirSignature = normalizePathSeparators(path.join(configDirSignature, itemName));
                        const subRelativePath = relativePath 
                            ? `${relativePath}${itemName}/`
                            : `${itemName}/`;
                        
                        console.log(`Collecting configs from nested directory: ${subDirSignature}`);
                        backupInfo.contents.nested_directories.push(subDirSignature);
                        
                        // Recursively collect from this subdirectory
                        await this.recursivelyCollectConfigurations(
                            lang,
                            subDirSignature,
                            mergedConfigs,
                            mergedMetadata,
                            backupInfo,
                            subRelativePath
                        );
                    }
                }
            }
        } catch (error) {
            console.warn(`Error finding subdirectories in ${configDirSignature}:`, error);
        }
    }
    
    /**
     * Creates a backup package for inactive entries that were cleaned up.
     */
    public async createInactiveEntriesBackup(
        lang: string,
        configDirSignature: string,
        inactiveEntries: Record<string, Record<string, any>>, // type -> entries
        reason: string = 'Entries marked as inactive during cleanup'
    ): Promise<void> {
        const timestamp = new Date().toISOString().split('T')[0]; // YYYY-MM-DD format
        const dirName = configDirSignature === '_root' ? 'root' : path.basename(configDirSignature);
        const packageName = `${dirName}_cleanup_${timestamp}`;
        
        const packagePath = normalizePathSeparators(path.join(
            this.archivePath, 'inactive_entries', packageName
        ));
        
        console.log(`Creating backup package for inactive entries: ${configDirSignature} -> ${packageName}`);
        
        // Ensure package directory exists
        await this.fs.ensureDir(packagePath);
        
        // Create cleanup info
        const cleanupInfo = {
            type: 'inactive_entries',
            source_path: configDirSignature,
            language: lang,
            cleanup_date: new Date().toISOString(),
            reason: reason,
            package_name: packageName,
            entries_count: Object.values(inactiveEntries).reduce((total, entries) => total + Object.keys(entries).length, 0),
            entries_by_type: Object.fromEntries(
                Object.entries(inactiveEntries).map(([type, entries]) => [type, Object.keys(entries).length])
            )
        };
        
        // Create archived entries file
        const archivedEntriesPath = path.join(packagePath, 'archived_entries.json');
        await this.fs.writeFile(archivedEntriesPath, JSON.stringify(inactiveEntries, null, 2));
        
        // Write cleanup info
        const cleanupInfoPath = path.join(packagePath, 'cleanup_info.json');
        await this.fs.writeFile(cleanupInfoPath, JSON.stringify(cleanupInfo, null, 2));
        
        // Create restore instructions
        await this.createRestoreInstructions(packagePath, 'inactive_entries', cleanupInfo);
        
        // Update main README
        await this.updateMainReadme();
        
        console.log(`✅ Created cleanup backup package: ${packageName}`);
    }
    
    /**
     * Creates restore instructions for a backup package.
     */
    private async createRestoreInstructions(
        packagePath: string, 
        backupType: 'removed_directory' | 'inactive_entries',
        info: any
    ): Promise<void> {
        let instructions = '';
        
        if (backupType === 'removed_directory') {
            instructions = `# Restore Instructions: ${info.package_name}

## What was backed up?
- **Original path**: \`${info.original_path}\`
- **Language**: \`${info.language}\`
- **Removed on**: ${new Date(info.removed_date).toLocaleString()}
- **Reason**: ${info.reason}

## Contents
- **Config files**: ${info.contents.config_files.join(', ') || 'None'}
- **Metadata files**: ${info.contents.metadata_files.join(', ') || 'None'}
- **Nested directories**: ${info.contents.nested_directories.length} subdirectories backed up recursively

## Backup Structure
This backup contains **merged JSON files** with all configurations from the removed directory and its subdirectories:

### Config Files
Each JSON file contains entries for all directories that were removed:
- \`config/locales.json\` - All text translations (e.g., \`"Recipe/": "配方", "Recipe/AddRecipe/": "添加配方"\`)
- \`config/hidden.json\` - All visibility settings (e.g., \`"Recipe/": false, "LootTable/": false\`)  
- \`config/collapsed.json\` - All collapse states (e.g., \`"Recipe/": true, "Event/": false\`)
- \`config/order.json\` - All ordering values (e.g., \`"Recipe/": 1, "LootTable/": 2\`)

### Nested directory structure
${info.contents.nested_directories.length > 0 ? 
    'The following subdirectories were also backed up:\n' + 
    info.contents.nested_directories.map(dir => `- \`${dir}\``).join('\n') :
    'No nested subdirectories found.'
}

## How to restore?

### Option 1: Manual restore (recommended)
1. Recreate the directory structure in your docs:
   \`\`\`
   docs/${info.language}/${info.original_path}/
   \`\`\`
   **Note**: Also recreate any nested subdirectories that existed within this directory.

2. Copy and split config entries back:
   \`\`\`bash
   # From config/locales.json, extract entries and place them in:
   # .vitepress/config/sidebar/${info.language}/${info.original_path}/locales.json
   # 
   # For nested entries like "Recipe/AddRecipe/": "添加配方", place them in:
   # .vitepress/config/sidebar/${info.language}/${info.original_path}/Recipe/AddRecipe/locales.json
   \`\`\`

3. Copy and split metadata entries back:
   \`\`\`bash
   # From metadata/locales.json, extract entries and place them in:
   # .vitepress/config/sidebar/.metadata/${info.language}/${info.original_path}/locales.json
   # 
   # For nested entries, split them to appropriate subdirectory metadata files
   \`\`\`

4. Run sidebar rebuild:
   \`\`\`bash
   npm run build:sidebar
   \`\`\`

### Option 2: Script restore (advanced)
Create a restore script that automatically:
1. Parses the merged JSON files
2. Splits entries by directory path
3. Creates the appropriate directory structure  
4. Places each entry in its correct location

## Files in this backup
- \`backup_info.json\` - Details about what was backed up
- \`config/*.json\` - **Merged JSON configuration files** containing all entries from all subdirectories
- \`metadata/*.json\` - **Merged metadata files** containing all metadata from all subdirectories
- \`RESTORE.md\` - This file

## Notes
- This backup was created automatically when the directory was removed
- All original files have been deleted from their original locations
- **Configurations are merged**: All subdirectory entries are combined into single JSON files with path prefixes
- The backup preserves the complete nested directory structure as JSON entries
- The backup is self-contained and portable
- ${info.contents.nested_directories.length} nested subdirectories are included in this backup

## Example of merged structure
If you had:
\`\`\`
Introduction/Recipe/locales.json: {"_self_": "配方", "AddRecipe/": "添加配方"}
Introduction/Recipe/AddRecipe/locales.json: {"_self_": "添加配方", "Vanilla/": "原版"}
\`\`\`

The backup will contain:
\`\`\`
config/locales.json: {
  "_self_": "Introduction value",
  "Recipe/": "配方", 
  "Recipe/_self_": "配方",
  "Recipe/AddRecipe/": "添加配方",
  "Recipe/AddRecipe/_self_": "添加配方", 
  "Recipe/AddRecipe/Vanilla/": "原版"
}
\`\`\`
`;
        } else {
            instructions = `# Restore Instructions: ${info.package_name}

## What was backed up?
- **Source path**: \`${info.source_path}\`
- **Language**: \`${info.language}\`
- **Cleaned on**: ${new Date(info.cleanup_date).toLocaleString()}
- **Reason**: ${info.reason}
- **Total entries**: ${info.entries_count}

## Entries by type
${Object.entries(info.entries_by_type).map(([type, count]) => `- **${type}**: ${count} entries`).join('\n')}

## How to restore?

### Option 1: Manual restore (recommended)
1. Open \`archived_entries.json\` to see what was removed
2. For each entry you want to restore:
   - Add it back to the appropriate JSON file in:
     \`\`\`
     .vitepress/config/sidebar/${info.language}/${info.source_path}/
     \`\`\`
3. Run sidebar rebuild:
   \`\`\`bash
   npm run build:sidebar
   \`\`\`

### Option 2: Bulk restore (advanced)
You can create a script to automatically restore all or selected entries.

## Files in this backup
- \`cleanup_info.json\` - Details about the cleanup
- \`archived_entries.json\` - The actual entries that were removed
- \`RESTORE.md\` - This file

## Notes
- These entries were removed because they were marked as inactive
- Only user-modified entries are backed up (system-generated entries are not)
- You can safely restore any or all of these entries
`;
        }
        
        const restoreInstructionsPath = path.join(packagePath, 'RESTORE.md');
        await this.fs.writeFile(restoreInstructionsPath, instructions);
    }
    
    /**
     * Updates the main README file in the archive root.
     */
    private async updateMainReadme(): Promise<void> {
        const readmePath = path.join(this.archivePath, 'README.md');
        
        const readme = `# Sidebar Configuration Archive

This directory contains automated backups of sidebar configurations that were removed or cleaned up.

## Structure

### \`removed_directories/\`
Contains complete backups of directory configurations that were removed because the physical directories no longer exist.

Each backup package includes:
- \`RESTORE.md\` - Step-by-step restore instructions
- \`backup_info.json\` - What was backed up and why
- \`config/\` - All JSON configuration files
- \`metadata/\` - Metadata files for reference

### \`inactive_entries/\`
Contains backups of individual config entries that were cleaned up during maintenance.

Each backup package includes:
- \`RESTORE.md\` - Step-by-step restore instructions  
- \`cleanup_info.json\` - Details about what was cleaned up
- \`archived_entries.json\` - The actual entries that were removed

## How to use

1. **Browse packages**: Each backup is self-contained with clear naming
2. **Read instructions**: Every package has a \`RESTORE.md\` with detailed steps
3. **Restore selectively**: You can restore individual files or entire configurations
4. **Stay organized**: Old backups can be safely deleted if no longer needed

## Maintenance

- Backup packages are created automatically during sidebar maintenance
- Each package is timestamped and self-documenting
- You can safely delete old packages you no longer need
- Consider archiving very old packages to external storage

## Last updated
${new Date().toLocaleString()}
`;
        
        try {
            await this.fs.writeFile(readmePath, readme);
        } catch (error) {
            console.warn('Failed to update main README:', error);
        }
    }
} 