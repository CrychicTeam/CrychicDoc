import fs from 'node:fs'; // For Dirent type, can also import Dirent directly if preferred

/**
 * @file FileSystem.ts
 * @description Interface for abstracting file system operations, allowing for easier testing
 * and potentially different FS implementations in the future.
 */
export interface FileSystem {
    readFile(path: string): Promise<string>;
    readDir(path: string): Promise<fs.Dirent[]>; // Using fs.Dirent to get file/directory info
    exists(path: string): Promise<boolean>;
    stat(path: string): Promise<fs.Stats>;
    writeFile(path: string, content: string): Promise<void>;
    ensureDir(path: string): Promise<void>;
    deleteFile(path: string): Promise<void>;
    deleteDir(path: string): Promise<void>;
    // Add other methods as needed by services, e.g.:
    // deleteFile(path: string): Promise<void>;
    // deleteDir(path: string): Promise<void>;
} 


