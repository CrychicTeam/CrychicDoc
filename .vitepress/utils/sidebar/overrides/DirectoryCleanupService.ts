import path from 'node:path';
import { normalizePathSeparators } from '../shared/objectUtils';
import { JsonFileHandler, JsonOverrideFileType } from './JsonFileHandler';
import { MetadataManager } from './MetadataManager';
import { FileSystem } from '../shared/FileSystem';

/**
 * @file DirectoryCleanupService.ts
 * @description Handles cleanup and archival of outdated JSON config directories.
 */
export class DirectoryCleanupService {
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
     * Cleans up outdated directories by archiving or deleting them based on user modifications.
     */
    public async cleanupOutdatedDirectories(outdatedDirs: string[], lang: string): Promise<void> {
        for (const outdatedDir of outdatedDirs) {

            await this.archiveOrDeleteJsonDirectory(lang, outdatedDir);
        }
    }

    /**
     * Archives or deletes a JSON directory, preserving user-modified values.
     * For now, we'll move user-modified values to a special location or just delete system stubs.
     */
    private async archiveOrDeleteJsonDirectory(lang: string, dirSignature: string): Promise<void> {
        // For this implementation, we'll simply delete the directory since we have metadata to track user modifications
        // In a more sophisticated implementation, you might want to archive user-modified values
        
        const overrideTypes: JsonOverrideFileType[] = ['locales', 'order', 'collapsed'];
        let hasUserModifiedContent = false;
        
        // Check if any files have user-modified content
        for (const type of overrideTypes) {
            try {
                const metadata = await this.metadataManager.readMetadata(type, lang, dirSignature);
                for (const entry of Object.values(metadata)) {
                    if (entry.isUserSet) {
                        hasUserModifiedContent = true;
                        break;
                    }
                }
                if (hasUserModifiedContent) break;
            } catch (error) {
                // File doesn't exist, continue
            }
        }
        
        if (hasUserModifiedContent) {

            // Mark all entries as inactive but preserve them
            for (const type of overrideTypes) {
                try {
                    const metadata = await this.metadataManager.readMetadata(type, lang, dirSignature);
                    for (const key in metadata) {
                        metadata[key].isActiveInStructure = false;
                        metadata[key].lastSeen = Date.now();
                    }
                    await this.metadataManager.writeMetadata(type, lang, dirSignature, metadata);
                } catch (error) {
                    // File doesn't exist, continue
                }
            }
        } else {

            // Delete the entire directory since it has no user content
            try {
                const configDir = normalizePathSeparators(path.join(
                    this.docsPath, '..', '.vitepress', 'config', 'sidebar', lang, dirSignature
                ));
                await this.deleteDirectory(configDir);
            } catch (error) {

            }
        }
    }

    /**
     * Checks if a directory has only system-generated content (no user modifications).
     */
    private async hasOnlySystemGeneratedContent(lang: string, dirSignature: string): Promise<boolean> {
        const overrideTypes: JsonOverrideFileType[] = ['locales', 'order', 'collapsed'];
        
        for (const type of overrideTypes) {
            try {
                const metadata = await this.metadataManager.readMetadata(type, lang, dirSignature);
                for (const entry of Object.values(metadata)) {
                    if (entry.isUserSet) {
                        return false; // Found user-modified content
                    }
                }
            } catch (error) {
                // File doesn't exist, continue
            }
        }
        
        return true; // Only system content found
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

