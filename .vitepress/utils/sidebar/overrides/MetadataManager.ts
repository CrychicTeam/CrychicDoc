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
            lastSeen: Date.now(),
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
            return true; 
        }
        if (metadataEntry.isUserSet) {
            return true;
        }
        const currentHash = this.generateValueHash(currentJsonValue);
        return currentHash !== metadataEntry.valueHash;
    }
} 


