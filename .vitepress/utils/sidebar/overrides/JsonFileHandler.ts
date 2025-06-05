import path from 'node:path';
import { FileSystem } from '../shared/FileSystem';
import { normalizePathSeparators } from '../shared/objectUtils';

export type JsonOverrideFileType = 'locales' | 'order' | 'collapsed' | 'hidden';

/**
 * @file JsonFileHandler.ts
 * @description Handles reading and writing scoped JSON override files 
 * (locales.json, order.json, collapsed.json) which always reside within a directory
 * corresponding to a sidebar folder/group item or a root view.
 */
export class JsonFileHandler {
    private readonly fs: FileSystem;
    private readonly baseOverridesPath: string; // e.g., .vitepress/config/sidebar

    /**
     * @param fs FileSystem instance.
     * @param baseOverridesPath Absolute path to the directory containing type subfolders (locales, order, etc.).
     */
    constructor(fs: FileSystem, baseOverridesPath: string) {
        this.fs = fs;
        this.baseOverridesPath = normalizePathSeparators(baseOverridesPath);
    }

    /**
     * Generates the file path for a JSON override file within a given directory signature.
     * @param type The type of override ('locales', 'order', 'collapsed').
     * @param lang The language code (e.g., 'en').
     * @param itemDirectoryPathSignature The path signature of the directory/group this JSON file belongs to,
     *                                   relative to the language root (e.g., 'guide/concepts', or '_root').
     */
    private getJsonFilePath(type: JsonOverrideFileType, lang: string, itemDirectoryPathSignature: string): string {
        const fileName = `${type}.json`;
        // If itemDirectoryPathSignature is empty, it means files are directly under {lang}
        // This case might be consolidated if _root is always used for the top-level of a root view.
        const fullPath = itemDirectoryPathSignature 
            ? path.join(this.baseOverridesPath, lang, itemDirectoryPathSignature, fileName)
            : path.join(this.baseOverridesPath, lang, fileName); // fallback if signature is empty, should be rare
        return normalizePathSeparators(fullPath);
    }

    /**
     * Reads a specific JSON override file.
     * @returns The parsed JSON object, or an empty object if not found or parse error.
     */
    public async readJsonFile(
        type: JsonOverrideFileType, 
        lang: string, 
        itemDirectoryPathSignature: string
    ): Promise<Record<string, any>> {
        const filePath = this.getJsonFilePath(type, lang, itemDirectoryPathSignature);
        try {
            if (await this.fs.exists(filePath)) {
                const content = await this.fs.readFile(filePath);
                if (content.trim() === '') return {};
                return JSON.parse(content);
            }
        } catch (error: any) {

        }
        return {};
    }

    /**
     * Writes data to a specific JSON override file.
     */
    public async writeJsonFile(
        type: JsonOverrideFileType, 
        lang: string, 
        itemDirectoryPathSignature: string, 
        data: Record<string, any>
    ): Promise<void> {
        const filePath = this.getJsonFilePath(type, lang, itemDirectoryPathSignature);
        try {
            await this.fs.ensureDir(path.dirname(filePath));
            await this.fs.writeFile(filePath, JSON.stringify(data, null, 2));
        } catch (error: any) {

        }
    }

    /**
     * Gets the FileSystem instance for direct access by other services.
     */
    public getFileSystem(): FileSystem {
        return this.fs;
    }

    /**
     * Gets the base overrides path for direct access by other services.
     */
    public getBaseOverridesPath(): string {
        return this.baseOverridesPath;
    }
} 

