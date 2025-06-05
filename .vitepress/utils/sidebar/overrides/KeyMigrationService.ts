import path from 'node:path';
import { SidebarItem, MetadataEntry } from '../types';
import { JsonFileHandler, JsonOverrideFileType } from './JsonFileHandler';
import { MetadataManager } from './MetadataManager';
import { PathKeyProcessor } from './PathKeyProcessor';

/**
 * @file KeyMigrationService.ts
 * @description Handles migration of full path keys to relative keys in JSON config files.
 * This system transforms old data format to new data format while preserving user customizations.
 */
export class KeyMigrationService {
    private jsonFileHandler: JsonFileHandler;
    private metadataManager: MetadataManager;
    private pathProcessor: PathKeyProcessor;

    constructor(jsonFileHandler: JsonFileHandler, metadataManager: MetadataManager) {
        this.jsonFileHandler = jsonFileHandler;
        this.metadataManager = metadataManager;
        this.pathProcessor = new PathKeyProcessor();
    }

    /**
     * Migrates JSON keys from full paths to relative paths for a given directory.
     * CORE MIGRATION ALGORITHM: Transforms "concepts/concept-1.md" �?"concept-1.md" in concepts/ context
     * 
     * @param items The current structural items in this directory
     * @param currentConfigDirSignature The current directory signature
     * @param lang The language code
     * @returns True if any migration occurred
     */
    public async migrateKeysToRelative(
        items: SidebarItem[],
        currentConfigDirSignature: string,
        lang: string
    ): Promise<boolean> {
        let migrationOccurred = false;
        const overrideTypes: JsonOverrideFileType[] = ['locales', 'order', 'collapsed'];

        for (const type of overrideTypes) {
            try {
                const jsonData = await this.jsonFileHandler.readJsonFile(type, lang, currentConfigDirSignature);
                const metadata = await this.metadataManager.readMetadata(type, lang, currentConfigDirSignature);

                const migratedData: Record<string, any> = {};
                const migratedMetadata: Record<string, any> = {};
                let typeHadMigration = false;

                // STEP 1: Build migration map - full path �?relative path
                const pathMigrationMap = new Map<string, string>();
                for (const item of items) {
                    const fullKey = item._relativePathKey || item.text || 'untitled';
                    const relativeKey = this.pathProcessor.extractRelativeKeyForCurrentDir(item, currentConfigDirSignature);
                    
                    if (fullKey !== relativeKey) {
                        pathMigrationMap.set(fullKey, relativeKey);
                    }
                }

                // STEP 2: Migrate existing JSON keys while preserving user values
                for (const [existingKey, value] of Object.entries(jsonData)) {
                    // Skip _self_ keys - they don't need migration
                    if (existingKey === '_self_') {
                        migratedData[existingKey] = value;
                        if (metadata[existingKey]) {
                            migratedMetadata[existingKey] = metadata[existingKey];
                        }
                        continue;
                    }

                    // Check if this key needs migration
                    const newKey = pathMigrationMap.get(existingKey) || existingKey;
                    
                    if (newKey !== existingKey) {

                        typeHadMigration = true;
                        migrationOccurred = true;
                    }

                    // Use the new key (or original if no migration needed)
                    migratedData[newKey] = value;
                    
                    // STEP 3: Migrate metadata while preserving user modifications
                    if (metadata[existingKey]) {
                        migratedMetadata[newKey] = {
                            ...metadata[existingKey],
                            lastSeen: Date.now() // Update timestamp for migrated entries
                        };
                    }
                }

                // STEP 4: Write migrated data only if changes occurred
                if (typeHadMigration) {
                    await this.jsonFileHandler.writeJsonFile(type, lang, currentConfigDirSignature, migratedData);
                    await this.metadataManager.writeMetadata(type, lang, currentConfigDirSignature, migratedMetadata);

                }

            } catch (error) {

            }
        }

        return migrationOccurred;
    }

    /**
     * Recursively migrates keys for all directories in a structure.
     * RECURSIVE MIGRATION: Processes entire directory tree systematically
     */
    public async migrateKeysRecursively(
        items: SidebarItem[],
        currentConfigDirSignature: string,
        lang: string,
        langGitbookPaths: string[],
        absDocsPath: string
    ): Promise<boolean> {
        let anyMigrationOccurred = false;

        // Migrate keys for current directory
        const currentMigration = await this.migrateKeysToRelative(items, currentConfigDirSignature, lang);
        if (currentMigration) {
            anyMigrationOccurred = true;
        }

        // Recursively migrate subdirectories
        for (const item of items) {
            if (item._isDirectory || (item.items && item.items.length > 0)) {
                const itemKeyPart = this.pathProcessor.extractRelativeKeyForCurrentDir(item, currentConfigDirSignature);
                const nextConfigDirSignature = currentConfigDirSignature === '_root' 
                    ? itemKeyPart 
                    : path.join(currentConfigDirSignature, itemKeyPart).replace(/\\/g, '/');

                // Skip GitBook directories
                if (!this.pathProcessor.isGitBookRoot(nextConfigDirSignature, lang, langGitbookPaths, absDocsPath)) {
                    if (item.items && item.items.length > 0) {
                        const subMigration = await this.migrateKeysRecursively(
                            item.items,
                            nextConfigDirSignature,
                            lang,
                            langGitbookPaths,
                            absDocsPath
                        );
                        if (subMigration) {
                            anyMigrationOccurred = true;
                        }
                    }
                }
            }
        }

        return anyMigrationOccurred;
    }

    /**
     * Cleans up any remaining duplicate directories created by the old system.
     * CLEANUP PHASE: Removes artifacts from old architecture (concepts/concepts/)
     */
    public async cleanupDuplicateDirectories(
        rootConfigDirSignature: string,
        lang: string
    ): Promise<void> {
        try {
            const configPath = path.join(
                this.jsonFileHandler.getBaseOverridesPath(), 
                lang, 
                rootConfigDirSignature === '_root' ? '' : rootConfigDirSignature
            );

            // Look for duplicate nested directories (e.g., concepts/concepts/)
            await this.cleanupDuplicateInDirectory(configPath);
            
        } catch (error) {

        }
    }

    /**
     * Recursively cleans up duplicate nested directories.
     */
    private async cleanupDuplicateInDirectory(dirPath: string): Promise<void> {
        try {
            const fs = this.jsonFileHandler.getFileSystem();
            const entries = await fs.readDir(dirPath);

            for (const entry of entries) {
                if (entry.isDirectory() && !entry.name.startsWith('.')) {
                    const subDirPath = path.join(dirPath, entry.name);
                    const duplicatePath = path.join(subDirPath, entry.name);

                    // Check if there's a duplicate nested directory
                    if (await fs.exists(duplicatePath)) {

                        
                        try {
                            // Try to delete the duplicate nested directory
                            await fs.deleteDir(duplicatePath);

                        } catch (deleteError) {

                        }
                    }

                    // Recursively check subdirectories
                    await this.cleanupDuplicateInDirectory(subDirPath);
                }
            }
        } catch (error) {
            // Directory might not exist or be accessible - that's okay
        }
    }

    /**
     * Removes old metadata entries that are no longer active in the current structure.
     * This is what you requested - cleaning up old metadata while keeping the migration system.
     */
    public async cleanupOldMetadata(
        currentActiveItems: SidebarItem[],
        currentConfigDirSignature: string,
        lang: string
    ): Promise<void> {
        const overrideTypes: JsonOverrideFileType[] = ['locales', 'order', 'collapsed'];
        
        for (const type of overrideTypes) {
            try {
                const metadata = await this.metadataManager.readMetadata(type, lang, currentConfigDirSignature);
                const activeKeys = new Set<string>();
                
                // Collect all active keys from current structure
                for (const item of currentActiveItems) {
                    const itemKey = this.pathProcessor.extractRelativeKeyForCurrentDir(item, currentConfigDirSignature);
                    activeKeys.add(itemKey);
                }
                activeKeys.add('_self_'); // Always keep _self_ keys
                
                // Filter out inactive metadata entries
                const cleanedMetadata: Record<string, any> = {};
                let entriesRemoved = 0;
                
                for (const [key, metadataEntry] of Object.entries(metadata)) {
                    if (activeKeys.has(key) || metadataEntry.isUserSet) {
                        // Keep active entries and user-set entries
                        cleanedMetadata[key] = metadataEntry;
                    } else {
                        // Remove old system-generated entries
                        entriesRemoved++;

                    }
                }
                
                if (entriesRemoved > 0) {
                    await this.metadataManager.writeMetadata(type, lang, currentConfigDirSignature, cleanedMetadata);

                }
                
            } catch (error) {

            }
        }
    }

    /**
     * 清理目录重命名后的旧元数�?(Clean up old metadata after directory renaming)
     * 例如：tools �?tool 时，清理旧的 tools 元数�?     */
    public async cleanupRenamedDirectoryMetadata(
        rootConfigDirSignature: string,
        lang: string,
        currentActiveSignatures: Set<string>
    ): Promise<void> {
        try {
            const basePath = path.join(this.jsonFileHandler.getBaseOverridesPath(), lang, rootConfigDirSignature);
            const metadataBasePath = path.join(this.jsonFileHandler.getBaseOverridesPath(), '.metadata', lang, rootConfigDirSignature);
            
            // Check for orphaned metadata directories
            await this.cleanupOrphanedMetadataDirectories(basePath, metadataBasePath, currentActiveSignatures);
            
        } catch (error) {

        }
    }

    /**
     * 递归清理孤立的元数据目录 (Recursively clean up orphaned metadata directories)
     * Enhanced to detect successful migration and safely remove old metadata
     */
    private async cleanupOrphanedMetadataDirectories(
        basePath: string,
        metadataBasePath: string,
        activeSignatures: Set<string>
    ): Promise<void> {
        try {
            const fs = this.jsonFileHandler.getFileSystem();
            
            // Check if metadata base path exists
            if (!await fs.exists(metadataBasePath)) {
                return;
            }
            
            const metadataEntries = await fs.readDir(metadataBasePath);
            
            for (const entry of metadataEntries) {
                if (entry.isDirectory()) {
                    const dirName = entry.name;
                    const metadataPath = path.join(metadataBasePath, dirName);
                    const actualPath = path.join(basePath, dirName);
                    
                    // Check if the actual directory still exists
                    const actualExists = await fs.exists(actualPath);
                    const isActive = activeSignatures.has(dirName) || activeSignatures.has(dirName + '/');
                    
                    if (!actualExists && !isActive) {
                        // Check if this metadata was successfully migrated to another location
                        const migrationTarget = await this.findMigrationTarget(dirName, activeSignatures);
                        
                        if (migrationTarget) {
                            const targetMetadataPath = path.join(metadataBasePath, migrationTarget);
                            const migrationConfirmed = await this.verifySuccessfulMigration(metadataPath, targetMetadataPath);
                            
                            if (migrationConfirmed.hasMigrated) {


                                
                                try {
                                    await fs.deleteDir(metadataPath);

                                } catch (deleteError) {

                                }
                                continue;
                            }
                        }
                        
                        // No migration detected, check if safe to delete (no user data)
                        const hasUserData = await this.checkForUserData(metadataPath);
                        
                        if (!hasUserData) {
                            try {
                                await fs.deleteDir(metadataPath);

                            } catch (deleteError) {

                            }
                        } else {

                        }
                    } else {
                        // Recursively check subdirectories
                        const subMetadataPath = path.join(metadataPath);
                        const subBasePath = path.join(basePath, dirName);
                        await this.cleanupOrphanedMetadataDirectories(subBasePath, subMetadataPath, activeSignatures);
                    }
                }
            }
            
        } catch (error) {

        }
    }

    /**
     * 寻找可能的迁移目�?(Find potential migration target for orphaned metadata)
     */
    private async findMigrationTarget(orphanedDir: string, activeSignatures: Set<string>): Promise<string | null> {
        // Extract directory names from active signatures 
        // activeSignatures contains paths like "test-sidebar/tools/", we need just "tools"
        const activeDirectories = Array.from(activeSignatures)
            .map(sig => {
                // Remove trailing slash
                const cleanSig = sig.replace(/\/$/, '');
                // Extract the last part of the path (directory name)
                const parts = cleanSig.split('/');
                return parts[parts.length - 1];
            })
            .filter(dir => dir.length > 0 && dir !== orphanedDir);
        
        // Look for similar directory names that could be rename targets
        for (const activeDir of activeDirectories) {
            // Check for common rename patterns - prioritize exact patterns first
            if (
                // PRIORITY 1: Exact tool �?tools or tools �?tool patterns
                (orphanedDir === 'tool' && activeDir === 'tools') ||
                (orphanedDir === 'tools' && activeDir === 'tool')
            ) {
                return activeDir;
            }
        }
        
        // PRIORITY 2: Other similarity patterns
        for (const activeDir of activeDirectories) {
            if (
                // Similar names (contain each other) but not the exact tool/tools case handled above
                (activeDir.includes(orphanedDir) || orphanedDir.includes(activeDir)) &&
                !(orphanedDir === 'tool' || orphanedDir === 'tools' || activeDir === 'tool' || activeDir === 'tools') ||
                // Length difference <= 2 characters (common for renames)
                (Math.abs(activeDir.length - orphanedDir.length) <= 2 && Math.abs(activeDir.length - orphanedDir.length) > 0)
            ) {
                return activeDir;
            }
        }
        
        return null;
    }

    /**
     * 验证成功迁移 (Verify successful migration between metadata directories)
     */
    private async verifySuccessfulMigration(sourcePath: string, targetPath: string): Promise<{
        hasMigrated: boolean;
        migratedEntries: Array<{ key: string; type: string; hash: string }>;
    }> {
        const overrideTypes: JsonOverrideFileType[] = ['locales', 'order', 'collapsed'];
        const migratedEntries: Array<{ key: string; type: string; hash: string }> = [];
        
        try {
            const fs = this.jsonFileHandler.getFileSystem();
            
            for (const type of overrideTypes) {
                const sourceFile = path.join(sourcePath, `${type}.json`);
                const targetFile = path.join(targetPath, `${type}.json`);
                
                try {
                    // Read source metadata
                    const sourceContent = await fs.readFile(sourceFile);
                    const sourceData = JSON.parse(sourceContent);
                    
                    // Read target metadata
                    const targetContent = await fs.readFile(targetFile);
                    const targetData = JSON.parse(targetContent);
                    
                    // Check for migrated user-set entries
                    for (const [key, sourceEntry] of Object.entries(sourceData)) {
                        if (this.isValidMetadataEntry(sourceEntry) && sourceEntry.isUserSet && targetData[key]) {
                            const targetEntry = targetData[key];
                            
                            // Check if same user data exists in target with newer timestamp
                            if (this.isValidMetadataEntry(targetEntry) && 
                                targetEntry.isUserSet && 
                                targetEntry.valueHash === sourceEntry.valueHash &&
                                targetEntry.lastSeen >= sourceEntry.lastSeen) {
                                
                                migratedEntries.push({
                                    key,
                                    type,
                                    hash: sourceEntry.valueHash
                                });
                            }
                        }
                    }
                } catch (fileError) {
                    // File might not exist, continue
                }
            }
        } catch (error) {
            // Error reading metadata, assume no migration
            return { hasMigrated: false, migratedEntries: [] };
        }
        
        return {
            hasMigrated: migratedEntries.length > 0,
            migratedEntries
        };
    }

    /**
     * 检查元数据中是否有用户数据 (Check if metadata contains user data)
     */
    private async checkForUserData(metadataPath: string): Promise<boolean> {
        const overrideTypes: JsonOverrideFileType[] = ['locales', 'order', 'collapsed'];
        
        try {
            const fs = this.jsonFileHandler.getFileSystem();
            
            for (const type of overrideTypes) {
                const filePath = path.join(metadataPath, `${type}.json`);
                
                try {
                    const content = await fs.readFile(filePath);
                    const data = JSON.parse(content);
                    
                    // Check if any entries are user-set
                    for (const [key, entry] of Object.entries(data)) {
                        if (this.isValidMetadataEntry(entry) && entry.isUserSet) {
                            return true;
                        }
                    }
                } catch (fileError) {
                    // File might not exist, continue
                }
            }
        } catch (error) {
            // Error reading, assume has user data to be safe
            return true;
        }
        
        return false;
    }

    /**
     * Type guard to check if an object is a valid metadata entry
     */
    private isValidMetadataEntry(obj: unknown): obj is MetadataEntry {
        return typeof obj === 'object' && 
               obj !== null && 
               'isUserSet' in obj && 
               'valueHash' in obj && 
               'lastSeen' in obj;
    }
} 


