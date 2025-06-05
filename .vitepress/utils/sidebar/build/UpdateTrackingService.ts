import { FileSystem } from '../shared/FileSystem';
import { FileHasher } from './FileHasher';
import { normalizePathSeparators } from '../shared/objectUtils';
import path from 'node:path';

/**
 * @file UpdateTrackingService.ts
 * @description Service to track file changes to determine if sidebar regeneration is needed.
 * This is a basic implementation; more sophisticated manifest structures might be needed
 * for fine-grained dependency tracking beyond simple file content hashes.
 */
export class UpdateTrackingService {
    private manifestFilePath: string;
    private fs: FileSystem;
    private fileHasher: FileHasher;
    private previousManifest: Record<string, string> = {}; // filePath (normalized) -> hash

    constructor(manifestFilePath: string, fs: FileSystem, fileHasher: FileHasher) {
        this.manifestFilePath = normalizePathSeparators(manifestFilePath);
        this.fs = fs;
        this.fileHasher = fileHasher;
    }

    /**
     * Loads the previously saved manifest of file hashes.
     */
    async loadPreviousManifest(): Promise<void> {
        try {
            if (await this.fs.exists(this.manifestFilePath)) {
                const content = await this.fs.readFile(this.manifestFilePath);
                this.previousManifest = JSON.parse(content);
            } else {
                this.previousManifest = {};
            }
        } catch (e: any) {

            this.previousManifest = {};
        }
    }

    /**
     * Checks if regeneration is needed by comparing current file hashes against the loaded manifest.
     * @param currentFilePaths An array of absolute paths to all files that influence sidebar generation.
     * @returns True if regeneration is needed, false otherwise.
     */
    async needsRegeneration(currentFilePaths: string[]): Promise<boolean> {
        await this.loadPreviousManifest();
        const normalizedCurrentFilePaths = currentFilePaths.map(p => normalizePathSeparators(p));

        if (Object.keys(this.previousManifest).length !== normalizedCurrentFilePaths.length) {
            // console.log('[UpdateTrackingService] Needs regen: Number of files changed.');
            return true; 
        }

        for (const filePath of normalizedCurrentFilePaths) {
            const currentHash = await this.fileHasher.hashFile(filePath);
            if (currentHash === '') { // Hashing failed for a file
                // console.log(`[UpdateTrackingService] Needs regen: Hashing failed for ${filePath}`);
                return true; // Force regeneration if any file can't be hashed
            }
            if (!this.previousManifest[filePath] || this.previousManifest[filePath] !== currentHash) {
                // console.log(`[UpdateTrackingService] Needs regen: File changed or new: ${filePath}`);
                return true; 
            }
        }
        // console.log('[UpdateTrackingService] No regeneration needed.');
        return false; 
    }

    /**
     * Saves the manifest of current file paths and their hashes.
     * @param currentFilePaths An array of absolute paths to all files that influence sidebar generation.
     */
    async saveCurrentManifest(currentFilePaths: string[]): Promise<void> {
        const newManifest: Record<string, string> = {};
        const normalizedCurrentFilePaths = currentFilePaths.map(p => normalizePathSeparators(p));

        for (const filePath of normalizedCurrentFilePaths) {
            const hash = await this.fileHasher.hashFile(filePath);
            if (hash === '') {
                // console.warn(`[UpdateTrackingService] Skipping file in manifest due to hashing error: ${filePath}`);
                // Alternatively, could store a special value or re-throw to indicate critical error.
                // For now, if a file can't be hashed, it might be better to ensure regeneration next time.
                // However, to prevent constant regeneration loops on a problematic file, we might skip it.
                // For robustness, if any hash is empty, perhaps manifest should not be saved or saved as incomplete.
                // This part needs careful consideration based on desired error recovery.
                // For now, let's just skip problematic files from the new manifest.
                continue; 
            }
            newManifest[filePath] = hash;
        }
        try {
            await this.fs.ensureDir(path.dirname(this.manifestFilePath));
            await this.fs.writeFile(this.manifestFilePath, JSON.stringify(newManifest, null, 2));
            this.previousManifest = newManifest;
        } catch (e: any) {

        }
    }
} 

