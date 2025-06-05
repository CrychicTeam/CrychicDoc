import path from 'node:path';
import { normalizePathSeparators } from '../shared/objectUtils';
import { JsonFileHandler, JsonOverrideFileType } from './JsonFileHandler';
import { MetadataManager } from './MetadataManager';
import { FileSystem } from '../shared/FileSystem';

/**
 * @file DirectoryMigrationService.ts
 * @description Handles detection and execution of directory migrations for JSON config synchronization.
 */
export class DirectoryMigrationService {
    private jsonFileHandler: JsonFileHandler;
    private metadataManager: MetadataManager;
    private fs: FileSystem;
    private docsPath: string;

    constructor(jsonFileHandler: JsonFileHandler, metadataManager: MetadataManager, fs: FileSystem, docsPath: string) {
        this.jsonFileHandler = jsonFileHandler;
        this.metadataManager = metadataManager;
        this.fs = fs;
        this.docsPath = docsPath;
    }

    /**
     * Detects directory renames/moves and migrates configuration values.
     * Uses similarity scoring to identify potential renames.
     */
    public async handleDirectoryMigrations(
        rootDirSignature: string,
        lang: string,
        activeDirectorySignatures: Set<string>,
        outdatedDirs: string[]
    ): Promise<boolean> {
        const activeDirsArray = Array.from(activeDirectorySignatures);
        const migrationsToProcess: Array<{ from: string; to: string }> = [];
        const migrationTargets = new Set<string>(); // Track migration targets to avoid conflicts
        
        // Create a copy of outdatedDirs to track which ones we've processed
        const remainingOutdatedDirs = [...outdatedDirs];
        
        // For each outdated directory, try to find a similar active directory
        for (const outdatedDir of outdatedDirs) {
            const bestMatch = await this.findBestDirectoryMatch(outdatedDir, activeDirsArray, lang);
            
            if (bestMatch && bestMatch !== outdatedDir) { // Prevent self-migration
                // Check if this target is already used by another migration
                if (migrationTargets.has(bestMatch)) {

                    continue;
                }
                
                // Check if this outdated directory has user modifications (we want to migrate those)
                const sourceHasUserModifications = await this.hasUserModifications(lang, outdatedDir);
                
                if (sourceHasUserModifications) {
                    migrationsToProcess.push({ from: outdatedDir, to: bestMatch });
                    migrationTargets.add(bestMatch); // Mark target as used

                    
                    // Remove from the remaining outdated dirs since we'll handle it via migration
                    const index = remainingOutdatedDirs.indexOf(outdatedDir);
                    if (index > -1) {
                        remainingOutdatedDirs.splice(index, 1);
                    }
                }
            }
        }
        
        // Process migrations
        for (const migration of migrationsToProcess) {
            await this.migrateDirectoryConfiguration(lang, migration.from, migration.to);
            // Migration method now handles deletion of source directory
        }
        
        // Remove migration targets from remaining outdated dirs to prevent self-migration
        for (const target of migrationTargets) {
            const targetIndex = remainingOutdatedDirs.indexOf(target);
            if (targetIndex > -1) {
                remainingOutdatedDirs.splice(targetIndex, 1);

            }
        }
        
        // Update the outdatedDirs array to only include unprocessed directories
        outdatedDirs.length = 0;
        outdatedDirs.push(...remainingOutdatedDirs);
        
        return migrationsToProcess.length > 0;
    }

    /**
     * Checks if a directory has user modifications.
     */
    private async hasUserModifications(lang: string, dirSignature: string): Promise<boolean> {
        const overrideTypes: JsonOverrideFileType[] = ['locales', 'order', 'collapsed'];
        
        for (const type of overrideTypes) {
            try {
                const metadata = await this.metadataManager.readMetadata(type, lang, dirSignature);
                for (const entry of Object.values(metadata)) {
                    if (entry.isUserSet) {
                        return true; // Found user-modified content
                    }
                }
            } catch (error) {
                // File doesn't exist, continue
            }
        }
        
        return false; // No user modifications found
    }

    /**
     * Finds the best matching active directory for a potentially renamed outdated directory.
     */
    private async findBestDirectoryMatch(
        outdatedDir: string,
        activeDirs: string[],
        lang: string
    ): Promise<string | null> {
        const outdatedBaseName = path.basename(outdatedDir);
        
        // Look for similar directory names
        let bestMatch: string | null = null;
        let bestScore = 0;
        
        for (const activeDir of activeDirs) {
            const activeBaseName = path.basename(activeDir);
            
            // Skip if the directories are in different parent paths
            if (path.dirname(outdatedDir) !== path.dirname(activeDir)) {
                continue;
            }
            
            const similarity = this.calculateStringSimilarity(outdatedBaseName, activeBaseName);
            
            // Require a high similarity threshold for auto-migration
            if (similarity > 0.7 && similarity > bestScore) {
                bestScore = similarity;
                bestMatch = activeDir;
            }
        }
        
        // Additional check: look for common rename patterns
        if (!bestMatch) {
            for (const activeDir of activeDirs) {
                const activeBaseName = path.basename(activeDir);
                
                // Skip if the directories are in different parent paths
                if (path.dirname(outdatedDir) !== path.dirname(activeDir)) {
                    continue;
                }
                
                // Check for common patterns like singular �?plural
                if (this.isLikelyRename(outdatedBaseName, activeBaseName)) {
                    bestMatch = activeDir;
                    break;
                }
            }
        }
        
        return bestMatch;
    }

    /**
     * Calculates string similarity using Levenshtein distance.
     */
    private calculateStringSimilarity(str1: string, str2: string): number {
        const maxLength = Math.max(str1.length, str2.length);
        if (maxLength === 0) return 1.0;
        
        const distance = this.levenshteinDistance(str1, str2);
        return (maxLength - distance) / maxLength;
    }

    /**
     * Calculates Levenshtein distance between two strings.
     */
    private levenshteinDistance(str1: string, str2: string): number {
        const matrix = Array(str2.length + 1).fill(null).map(() => Array(str1.length + 1).fill(null));
        
        for (let i = 0; i <= str1.length; i++) matrix[0][i] = i;
        for (let j = 0; j <= str2.length; j++) matrix[j][0] = j;
        
        for (let j = 1; j <= str2.length; j++) {
            for (let i = 1; i <= str1.length; i++) {
                const substitutionCost = str1[i - 1] === str2[j - 1] ? 0 : 1;
                matrix[j][i] = Math.min(
                    matrix[j][i - 1] + 1, // deletion
                    matrix[j - 1][i] + 1, // insertion
                    matrix[j - 1][i - 1] + substitutionCost // substitution
                );
            }
        }
        
        return matrix[str2.length][str1.length];
    }

    /**
     * Checks for common rename patterns like singular �?plural.
     */
    private isLikelyRename(oldName: string, newName: string): boolean {
        // Check for singular �?plural transformations
        if (oldName + 's' === newName) return true;
        if (oldName === newName + 's') return true;
        
        // Check for other common patterns
        const patterns = [
            [/tool$/, /tools$/], // tool �?tools
            [/item$/, /items$/], // item �?items
            [/doc$/, /docs$/],   // doc �?docs
            [/page$/, /pages$/], // page �?pages
        ];
        
        for (const [pattern1, pattern2] of patterns) {
            if (pattern1.test(oldName) && pattern2.test(newName)) return true;
            if (pattern2.test(oldName) && pattern1.test(newName)) return true;
        }
        
        return false;
    }

    /**
     * Migrates configuration values from one directory to another.
     */
    private async migrateDirectoryConfiguration(lang: string, fromDir: string, toDir: string): Promise<void> {

        
        const overrideTypes: JsonOverrideFileType[] = ['locales', 'order', 'collapsed'];
        
        for (const type of overrideTypes) {
            try {
                // Read source configuration and metadata
                const sourceData = await this.jsonFileHandler.readJsonFile(type, lang, fromDir);
                const sourceMetadata = await this.metadataManager.readMetadata(type, lang, fromDir);
                
                // Read target configuration and metadata (might be empty/new)
                const targetData = await this.jsonFileHandler.readJsonFile(type, lang, toDir);
                const targetMetadata = await this.metadataManager.readMetadata(type, lang, toDir);
                
                // Merge configurations: prioritize user-set values from source
                const mergedData = { ...targetData }; // Start with target (newer structure)
                const mergedMetadata = { ...targetMetadata };
                
                let migrationCount = 0;
                
                for (const [key, value] of Object.entries(sourceData)) {
                    const sourceEntry = sourceMetadata[key];
                    
                    // During migration, prioritize user-set values from source over target
                    // This handles cases where both directories have user modifications but we want to preserve source
                    if (sourceEntry?.isUserSet) {
                        mergedData[key] = value;
                        mergedMetadata[key] = {
                            ...sourceEntry,
                            isActiveInStructure: true, // Mark as active in new location
                            lastSeen: Date.now()
                        };
                        migrationCount++;

                    }
                }
                
                // Write merged configuration to target
                await this.jsonFileHandler.writeJsonFile(type, lang, toDir, mergedData);
                await this.metadataManager.writeMetadata(type, lang, toDir, mergedMetadata);
                
            } catch (error) {

            }
        }
        
        // After successful migration, delete the source directory

        try {
            const configDir = normalizePathSeparators(path.join(
                this.docsPath, '..', '.vitepress', 'config', 'sidebar', lang, fromDir
            ));
            await this.deleteDirectory(configDir);
        } catch (error) {

        }
    }

    /**
     * Recursively deletes a directory and all its contents.
     */
    private async deleteDirectory(dirPath: string): Promise<void> {
        try {
            // Try to delete the entire directory using the FileSystem deleteDir method
            await this.fs.deleteDir(dirPath);
            
        } catch (error) {

            
            // Fallback: try to delete individual JSON files if directory deletion fails
            try {
                const jsonFiles = ['locales.json', 'order.json', 'collapsed.json'];
                
                for (const jsonFile of jsonFiles) {
                    try {
                        await this.fs.deleteFile(path.join(dirPath, jsonFile));
                    } catch (fileError) {
                        // File might not exist, continue
                    }
                }
            } catch (fallbackError) {

            }
        }
    }
} 

