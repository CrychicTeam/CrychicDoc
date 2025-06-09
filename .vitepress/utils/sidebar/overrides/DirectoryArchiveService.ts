import path from 'node:path';
import { normalizePathSeparators } from '../shared/objectUtils';
import { JsonFileHandler, JsonOverrideFileType } from './JsonFileHandler';
import { MetadataManager } from './MetadataManager';
import { FileSystem } from '../shared/FileSystem';

/**
 * @file DirectoryArchiveService.ts
 * @description Handles archiving of entire directory configurations when folders are physically moved or removed.
 * This service ONLY archives entire folders, never individual config values.
 */
export class DirectoryArchiveService {
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
     * Archives entire directory configurations only when the corresponding physical folder
     * no longer exists in the file system.
     * @param outdatedDirs - An array of directory signatures that are no longer active.
     * @param lang - The language code.
     */
    public async archiveOutdatedDirectories(outdatedDirs: string[], lang: string): Promise<void> {
        if (outdatedDirs.length === 0) {
            return;
        }

        await this.fs.ensureDir(this.archivePath);

        for (const outdatedDir of outdatedDirs) {
            // Check if the physical directory still exists
            const physicalDirPath = this.getPhysicalDirPath(outdatedDir, lang);
            const physicalDirExists = await this.fs.exists(physicalDirPath);

            // Only archive if the physical directory is gone AND it has config files
            if (!physicalDirExists && await this.hasAnyConfigFiles(lang, outdatedDir)) {
                await this.archiveEntireDirectory(lang, outdatedDir);
            }
        }
    }

    /**
     * Gets the physical directory path from a directory signature.
     */
    private getPhysicalDirPath(dirSignature: string, lang: string): string {
        if (dirSignature === '_root') {
            return normalizePathSeparators(path.join(this.docsPath, lang));
        }
        return normalizePathSeparators(path.join(this.docsPath, lang, dirSignature));
    }

    /**
     * Checks if a directory has any configuration files at all.
     */
    private async hasAnyConfigFiles(lang: string, dirSignature: string): Promise<boolean> {
        const overrideTypes: JsonOverrideFileType[] = ['locales', 'order', 'collapsed', 'hidden'];
        for (const type of overrideTypes) {
            try {
                const data = await this.jsonFileHandler.readJsonFile(type, lang, dirSignature);
                if (Object.keys(data).length > 0) {
                    return true;
                }
            } catch (error) {
                // File doesn't exist, continue
            }
        }
        return false;
    }

    /**
     * Archives the entire directory configuration (all config files) to the archive.
     */
    private async archiveEntireDirectory(lang: string, dirSignature: string): Promise<void> {
        const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
        const archiveDirName = `${path.basename(dirSignature)}_removed_${timestamp}`;
        const archiveDirParent = path.dirname(dirSignature);
        
        const targetDirSignature = normalizePathSeparators(path.join(archiveDirParent, archiveDirName));

        const overrideTypes: JsonOverrideFileType[] = ['locales', 'order', 'collapsed', 'hidden'];
        let hasArchivedAnyFile = false;

        for (const type of overrideTypes) {
            try {
                // Read source data and metadata
                const sourceData = await this.jsonFileHandler.readJsonFile(type, lang, dirSignature);
                const sourceMetadata = await this.metadataManager.readMetadata(type, lang, dirSignature);

                // Only archive if there's actually data to archive
                if (Object.keys(sourceData).length > 0 || Object.keys(sourceMetadata).length > 0) {
                    // Write to archive location
                    await this.jsonFileHandler.writeJsonFileToArchive(type, lang, targetDirSignature, sourceData, this.archivePath);
                    await this.metadataManager.writeMetadataToArchive(type, lang, targetDirSignature, sourceMetadata, this.archivePath);

                    // Delete original files after archiving
                    await this.jsonFileHandler.deleteJsonFile(type, lang, dirSignature);
                    await this.metadataManager.deleteMetadata(type, lang, dirSignature);

                    hasArchivedAnyFile = true;
                }
            } catch (error) {
                // This can happen if a file type doesn't exist for the directory.
            }
        }

        if (hasArchivedAnyFile) {
            // Directory successfully archived
        }
    }
} 