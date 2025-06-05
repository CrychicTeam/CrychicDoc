import fs from 'node:fs/promises';
import matter from 'gray-matter';
import { DirectoryConfig } from '../types';

/**
 * Loads and parses frontmatter from a given index.md file.
 * @param absoluteIndexMdPath Absolute path to the index.md file.
 * @returns A Partial<DirectoryConfig> object from the frontmatter data, 
 *          or an empty object if the file is not found, has no frontmatter, 
 *          or an error occurs during parsing.
 */
export async function loadFrontmatter(absoluteIndexMdPath: string): Promise<Partial<DirectoryConfig>> {
    try {
        const fileContent = await fs.readFile(absoluteIndexMdPath, 'utf-8');
        const { data } = matter(fileContent);
        // Ensure data is an object, even if frontmatter is empty or not an object (e.g. just ---)
        if (data && typeof data === 'object') {
            return data as Partial<DirectoryConfig>;
        }
        return {}; // No frontmatter data or not an object
    } catch (error: any) {
        if (error.code === 'ENOENT') {
            // File not found is expected for optional index.md files.
        } else {
            // Log other errors (e.g., permission issues, though gray-matter usually doesn't throw for parse errors)

        }
        return {}; // Return empty object on error or if file not found
    }
} 


