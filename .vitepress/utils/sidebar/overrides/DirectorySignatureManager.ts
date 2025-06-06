import path from 'node:path';
import { SidebarItem } from '../types';
import { normalizePathSeparators } from '../shared/objectUtils';
import { FileSystem } from '../shared/FileSystem';
import { PathKeyProcessor } from './PathKeyProcessor';

/**
 * @file DirectorySignatureManager.ts
 * @description Manages directory signatures for JSON config synchronization.
 */
export class DirectorySignatureManager {
    private pathProcessor: PathKeyProcessor;
    private fs: FileSystem;
    private docsPath: string;

    constructor(fs: FileSystem, docsPath: string) {
        this.fs = fs;
        this.docsPath = docsPath;
        this.pathProcessor = new PathKeyProcessor();
    }

    /**
     * Collects all active directory signatures in the current structure for cleanup purposes.
     */
    public collectActiveDirectorySignatures(
        items: SidebarItem[],
        currentDirSignature: string,
        gitbookPaths: string[],
        lang: string,
        absDocsPath: string,
        activeSignatures: Set<string>
    ): void {
        activeSignatures.add(currentDirSignature);
        
        for (const item of items) {
            if (item._isDirectory || (item.items && item.items.length > 0)) {
                // Use relative key extraction to prevent duplicate nesting
                const itemKeyPart = this.pathProcessor.extractRelativeKeyForCurrentDir(item, currentDirSignature);
                const nextDirSignature = currentDirSignature === '_root' 
                    ? itemKeyPart 
                    : normalizePathSeparators(path.join(currentDirSignature, itemKeyPart));
                
                // Skip GitBook directories
                if (!this.pathProcessor.isGitBookRoot(nextDirSignature, lang, gitbookPaths, absDocsPath)) {
                    activeSignatures.add(nextDirSignature);
                    
                    if (item.items && item.items.length > 0) {
                        this.collectActiveDirectorySignatures(item.items, nextDirSignature, gitbookPaths, lang, absDocsPath, activeSignatures);
                    }
                }
            }
        }
    }

    /**
     * Gets all JSON config directories under a given path.
     */
    public async getAllJsonDirectories(basePath: string): Promise<string[]> {
        try {
            const entries = await this.fs.readDir(basePath);
            const directories: string[] = [];
            
            for (const entry of entries) {
                if (entry.isDirectory() && !entry.name.startsWith('.')) {
                    directories.push(entry.name);
                }
            }
            
            return directories;
        } catch (error) {
            return []; // Directory doesn't exist or can't be read
        }
    }

    /**
     * Identifies outdated directories that are no longer active in the current structure.
     * This method is CONSERVATIVE - it only marks directories as outdated if the corresponding
     * physical directory no longer exists in the file system.
     */
    public async identifyOutdatedDirectories(
        rootDirSignature: string,
        lang: string,
        activeDirectorySignatures: Set<string>
    ): Promise<string[]> {
        try {
            const langConfigPath = path.join(this.docsPath, '..', '.vitepress', 'config', 'sidebar', lang);
            const outdatedDirs: string[] = [];
            
            // Recursively find all config directories
            const allConfigDirs = await this.findAllConfigDirectories(langConfigPath, rootDirSignature);
            
            for (const configDir of allConfigDirs) {
                // Check if the physical directory still exists
                const physicalDirPath = this.getPhysicalDirPath(configDir, lang);
                const physicalExists = await this.fs.exists(physicalDirPath);
                
                // Only mark as outdated if the physical directory is gone
                if (!physicalExists) {
                    outdatedDirs.push(configDir);
                }
            }
            
            return outdatedDirs;
        } catch (error) {
            console.error('Error identifying outdated directories:', error);
            return [];
        }
    }

    /**
     * Recursively finds all directories that have config files.
     */
    private async findAllConfigDirectories(basePath: string, rootSignature: string): Promise<string[]> {
        const configDirs: string[] = [];
        
        try {
            await this.findConfigDirectoriesRecursive(basePath, rootSignature, configDirs);
        } catch (error) {
            // Directory might not exist
        }
        
        return configDirs;
    }

    /**
     * Recursively searches for directories with config files.
     */
    private async findConfigDirectoriesRecursive(
        currentPath: string,
        currentSignature: string,
        results: string[]
    ): Promise<void> {
        try {
            // Check if this directory has any config files
            if (await this.hasConfigFiles(currentPath)) {
                results.push(currentSignature);
            }
            
            const entries = await this.fs.readDir(currentPath);
            
            for (const entry of entries) {
                if (entry.isDirectory() && !entry.name.startsWith('.')) {
                    const childPath = path.join(currentPath, entry.name);
                    const childSignature = currentSignature === '_root' 
                        ? entry.name 
                        : normalizePathSeparators(path.join(currentSignature, entry.name));
                    
                    await this.findConfigDirectoriesRecursive(childPath, childSignature, results);
                }
            }
        } catch (error) {
            // Directory might not exist or be readable
        }
    }

    /**
     * Checks if a directory has any config files.
     */
    private async hasConfigFiles(dirPath: string): Promise<boolean> {
        const configFiles = ['locales.json', 'order.json', 'collapsed.json', 'hidden.json'];
        
        for (const configFile of configFiles) {
            try {
                const filePath = path.join(dirPath, configFile);
                if (await this.fs.exists(filePath)) {
                    return true;
                }
            } catch (error) {
                // File doesn't exist, continue
            }
        }
        
        return false;
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
} 

