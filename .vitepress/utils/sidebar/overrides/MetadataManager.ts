import path from 'node:path';
import crypto from 'node:crypto';
import { FileSystem } from '../shared/FileSystem';
import { normalizePathSeparators } from '../shared/objectUtils';
import { JsonFileMetadata, MetadataEntry } from '../types';
import { JsonOverrideFileType } from './JsonFileHandler';

/**
 * @file MetadataManager.ts
 * @description Manages metadata for JSON override files. Assumes JSON files
 * (and thus their metadata) are per-directory/group, not per-individual-file.
 */
export class MetadataManager {
    private readonly fs: FileSystem;
    private readonly baseMetadataStoragePath: string;

    constructor(fs: FileSystem, baseMetadataStoragePath: string) {
        this.fs = fs;
        this.baseMetadataStoragePath = normalizePathSeparators(baseMetadataStoragePath);
    }

    /**
     * Generates the file path for a metadata file, corresponding to a directory-based JSON override.
     * @param type The type of override metadata ('locales', 'order', 'collapsed').
     * @param lang The language code (e.g., 'en').
     * @param itemDirectoryPathSignature The path signature of the directory/group this metadata belongs to,
     *                                   relative to the language root (e.g., 'guide/concepts', or '_root').
     */
    public getMetadataFilePath(type: JsonOverrideFileType, lang: string, itemDirectoryPathSignature: string): string {
        const fileName = `${type}.json`; // Metadata file keeps same name as JSON data file
        const fullPath = itemDirectoryPathSignature
            ? path.join(this.baseMetadataStoragePath, lang, itemDirectoryPathSignature, fileName)
            : path.join(this.baseMetadataStoragePath, lang, fileName);
        return normalizePathSeparators(fullPath);
    }

    public async readMetadata(
        type: JsonOverrideFileType, 
        lang: string, 
        itemDirectoryPathSignature: string
    ): Promise<JsonFileMetadata> {
        const filePath = this.getMetadataFilePath(type, lang, itemDirectoryPathSignature);
        try {
            if (await this.fs.exists(filePath)) {
                const content = await this.fs.readFile(filePath);
                if (content.trim() === '') return {};
                const parsed = JSON.parse(content);
                if (typeof parsed === 'object' && parsed !== null) {
                    return parsed as JsonFileMetadata;
                }

                return {};
            }
        } catch (error: any) {

        }
        return {};
    }

    public async writeMetadata(
        type: JsonOverrideFileType, 
        lang: string, 
        itemDirectoryPathSignature: string, 
        metadata: JsonFileMetadata
    ): Promise<void> {
        const filePath = this.getMetadataFilePath(type, lang, itemDirectoryPathSignature);
        try {
            await this.fs.ensureDir(path.dirname(filePath));
            await this.fs.writeFile(filePath, JSON.stringify(metadata, null, 2));
        } catch (error: any) {

        }
    }

    /**
     * Writes metadata to a specific file in an archive location.
     */
    public async writeMetadataToArchive(
        type: JsonOverrideFileType,
        lang: string,
        itemDirectoryPathSignature: string,
        metadata: JsonFileMetadata,
        archiveBasePath: string
    ): Promise<void> {
        const fileName = `${type}.json`;
        // Note: The archiveBasePath for metadata should point to the .metadata folder inside the general archive.
        const metadataArchivePath = normalizePathSeparators(path.join(archiveBasePath, '.metadata'));
        const filePath = normalizePathSeparators(path.join(metadataArchivePath, lang, itemDirectoryPathSignature, fileName));
        try {
            await this.fs.ensureDir(path.dirname(filePath));
            await this.fs.writeFile(filePath, JSON.stringify(metadata, null, 2));
        } catch (error: any) {
            console.error(`Failed to write archive metadata to ${filePath}`, error);
        }
    }

    /**
     * Deletes a specific metadata file.
     */
    public async deleteMetadata(
        type: JsonOverrideFileType,
        lang: string,
        itemDirectoryPathSignature: string
    ): Promise<void> {
        const filePath = this.getMetadataFilePath(type, lang, itemDirectoryPathSignature);
        try {
            if (await this.fs.exists(filePath)) {
                await this.fs.deleteFile(filePath);
            }
        } catch (error: any) {
            console.error(`Failed to delete metadata file ${filePath}`, error);
        }
    }

    /**
     * Generates a hash for a given value (typically a string from a JSON override file).
     */
    public generateValueHash(value: any): string {
        if (value === null || value === undefined) return 'null_or_undefined_hash';
        const stringValue = typeof value === 'string' ? value : JSON.stringify(value);
        return crypto.createHash('md5').update(stringValue).digest('hex');
    }

    /**
     * Creates a new MetadataEntry.
     */
    public createNewMetadataEntry(value: any, isUserSet: boolean, isActiveInStructure: boolean): MetadataEntry {
        return {
            valueHash: this.generateValueHash(value),
            isUserSet,
            isActiveInStructure,
        };
    }

    /**
     * Checks if a current value for a key likely represents a user modification compared to its metadata.
     * This is a key function for preserving user edits.
     * @param currentJsonValue The current value from the JSON override file for a given key.
     * @param metadataEntry The existing metadata entry for that same key.
     * @returns True if the value appears to be user-modified or explicitly set by the user.
     */
    public isEntryUserModified(currentJsonValue: any, metadataEntry?: MetadataEntry): boolean {
        if (!metadataEntry) {
            // If there's no metadata, it's a new or untracked item. Treat as a system-generated stub.
            return false;
        }
        if (metadataEntry.isUserSet) {
            // If it was explicitly marked as user-set, it remains so.
            return true;
        }
        
        // If not explicitly user-set, check if the value has changed from what we last recorded.
        // A change in value implies a user modification.
        const currentHash = this.generateValueHash(currentJsonValue);
        return currentHash !== metadataEntry.valueHash;
    }
} 

