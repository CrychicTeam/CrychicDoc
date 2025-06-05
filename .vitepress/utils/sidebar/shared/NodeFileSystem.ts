import fs from 'node:fs/promises';
import path from 'node:path'; // Import path module
import { Dirent, Stats } from 'node:fs'; // Import types directly
import { FileSystem } from './FileSystem';
import { normalizePathSeparators } from './objectUtils';

/**
 * @file NodeFileSystem.ts
 * @description Concrete implementation of the FileSystem interface using Node.js fs/promises module.
 */
export class NodeFileSystem implements FileSystem {
    public async readFile(filePath: string): Promise<string> {
        return fs.readFile(normalizePathSeparators(filePath), 'utf-8');
    }

    public async readDir(dirPath: string): Promise<Dirent[]> {
        return fs.readdir(normalizePathSeparators(dirPath), { withFileTypes: true });
    }

    public async exists(filePath: string): Promise<boolean> {
        try {
            await fs.access(normalizePathSeparators(filePath));
            return true;
        } catch {
            return false;
        }
    }

    public async stat(filePath: string): Promise<Stats> {
        return fs.stat(normalizePathSeparators(filePath));
    }

    public async writeFile(filePath: string, content: string): Promise<void> {
        const normalizedFilePath = normalizePathSeparators(filePath);
        await fs.mkdir(path.dirname(normalizedFilePath), { recursive: true });
        return fs.writeFile(normalizedFilePath, content, 'utf-8');
    }

    public async ensureDir(dirPath: string): Promise<void> {
        await fs.mkdir(normalizePathSeparators(dirPath), { recursive: true });
    }

    public async deleteFile(filePath: string): Promise<void> {
        try {
            await fs.unlink(normalizePathSeparators(filePath));
        } catch (error: any) {
            if (error.code !== 'ENOENT') {
                throw error; // Re-throw if it's not a "file not found" error
            }
            // Silently ignore if file doesn't exist
        }
    }

    public async deleteDir(dirPath: string): Promise<void> {
        try {
            await fs.rm(normalizePathSeparators(dirPath), { recursive: true, force: true });
        } catch (error: any) {
            if (error.code !== 'ENOENT') {
                throw error; // Re-throw if it's not a "directory not found" error
            }
            // Silently ignore if directory doesn't exist
        }
    }
} 


