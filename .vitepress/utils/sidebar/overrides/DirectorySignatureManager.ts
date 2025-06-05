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
     */
    public async identifyOutdatedDirectories(
        rootDirSignature: string,
        lang: string,
        activeDirectorySignatures: Set<string>
    ): Promise<string[]> {
        try {
            const langConfigPath = path.join(this.docsPath, '..', '.vitepress', 'config', 'sidebar', lang);
            const rootDirPath = rootDirSignature === '_root' ? langConfigPath : path.join(langConfigPath, rootDirSignature);
            
            const existingDirs = await this.getAllJsonDirectories(rootDirPath);
            const outdatedDirs: string[] = [];
            
            // Collect outdated directories
            for (const existingDir of existingDirs) {
                const relativeDirSignature = rootDirSignature === '_root' 
                    ? existingDir
                    : normalizePathSeparators(path.join(rootDirSignature, existingDir));
                
                if (!activeDirectorySignatures.has(relativeDirSignature) && !activeDirectorySignatures.has(relativeDirSignature + '/')) {
                    outdatedDirs.push(relativeDirSignature);
                }
            }
            
            return outdatedDirs;
        } catch (error) {

            return [];
        }
    }
} 


