import crypto from 'node:crypto';
import { FileSystem } from '../shared/FileSystem';
import { normalizePathSeparators } from '../shared/objectUtils';

/**
 * @file FileHasher.ts
 * @description Utility class to generate hashes for file content.
 */
export class FileHasher {
    private fs: FileSystem;

    constructor(fs: FileSystem) {
        this.fs = fs;
    }

    /**
     * Generates an MD5 hash for the content of a given file.
     * @param filePath Absolute path to the file.
     * @returns A promise resolving to the hex MD5 hash, or an empty string if an error occurs.
     */
    async hashFile(filePath: string): Promise<string> {
        const normalizedFilePath = normalizePathSeparators(filePath);
        try {
            const content = await this.fs.readFile(normalizedFilePath);
            return crypto.createHash('md5').update(content).digest('hex');
        } catch (error: any) {
            // console.warn(`[FileHasher] Could not hash file ${normalizedFilePath}: ${error.message}`);
            return ''; // Return empty string to indicate hashing failure for this file
        }
    }
} 


