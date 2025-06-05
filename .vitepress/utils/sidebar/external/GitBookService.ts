import { FileSystem } from '../shared/FileSystem';
import { normalizePathSeparators } from '../shared/objectUtils';
import glob from 'fast-glob';
import path from 'node:path';

/**
 * @file GitBookService.ts
 * @description Service for detecting GitBook directories (those containing SUMMARY.md).
 */
export class GitBookService {
    private fs: FileSystem;

    constructor(fs: FileSystem) {
        this.fs = fs;
    }

    /**
     * Finds all directories containing a SUMMARY.md file within the specified base path.
     * @param basePath Absolute path to search within (usually a language root like docs/en).
     * @returns A promise resolving to an array of absolute paths to GitBook directories.
     */
    async findGitBookDirectoriesInPath(basePath: string): Promise<string[]> {
        const normalizedBasePath = normalizePathSeparators(basePath);
        const summaryFiles = await glob(normalizePathSeparators(path.join(normalizedBasePath, '**', 'SUMMARY.md')), {
            cwd: normalizedBasePath,
            ignore: ['**/node_modules/**', '**/.vitepress/**', '**/assets/**'],
            onlyFiles: true,
            absolute: true,
        });
        return summaryFiles.map(file => normalizePathSeparators(path.dirname(file)));
    }

    /**
     * Finds all GitBook directories within the entire /docs path, scanning language subdirectories.
     * @param docsAbsPath Absolute path to the /docs directory.
     * @returns A promise resolving to an array of absolute paths to all GitBook directories found.
     */
    async findAllGitBookDirsInDocs(docsAbsPath: string): Promise<string[]> {
        const normalizedDocsPath = normalizePathSeparators(docsAbsPath);
        const summaryFiles = await glob(normalizePathSeparators(path.join(normalizedDocsPath, '**', 'SUMMARY.md')), {
            cwd: normalizedDocsPath,
            ignore: ['**/node_modules/**', '**/.vitepress/**', '**/assets/**'],
            onlyFiles: true,
            absolute: true,
        });
        return summaryFiles.map(file => normalizePathSeparators(path.dirname(file)));
    }
} 


