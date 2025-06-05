import path from 'node:path';
import { FileSystem } from '../shared/FileSystem';
import { normalizePathSeparators } from '../shared/objectUtils';
import { SidebarItem } from '../types';

/**
 * @file GitBookParserService.ts
 * @description Enhanced service for parsing GitBook SUMMARY.md files to generate sidebar structures.
 * Supports proper hierarchical nesting, frontmatter handling, and robust link processing.
 */
export class GitBookParserService {
    private fs: FileSystem;
    private docsAbsPath: string; // Absolute path to the /docs directory

    constructor(fs: FileSystem, docsAbsPath: string) {
        this.fs = fs;
        this.docsAbsPath = normalizePathSeparators(docsAbsPath);
    }

    /**
     * Parses a SUMMARY.md file from a GitBook directory and generates SidebarItem[].
     * Enhanced with proper hierarchical nesting and robust link processing.
     * 
     * @param gitbookDirAbsPath Absolute path to the GitBook directory (containing SUMMARY.md).
     * @param lang The current language code (e.g., 'en').
     * @returns A promise resolving to an array of SidebarItem objects for the GitBook root.
     */
    public async generateSidebarView(gitbookDirAbsPath: string, lang: string): Promise<SidebarItem[]> {
        const summaryFilePath = normalizePathSeparators(path.join(gitbookDirAbsPath, 'SUMMARY.md'));
        const generatedItems: SidebarItem[] = [];

        try {
            if (!await this.fs.exists(summaryFilePath)) {
                return [];
            }

            const content = await this.fs.readFile(summaryFilePath);
            
            // Parse frontmatter if present
            const { content: cleanContent } = this.parseFrontmatter(content);
            
            const lines = cleanContent.split(/\r?\n/);
            
            // Stack to track nesting: each entry contains { items: SidebarItem[], level: number, parent?: SidebarItem }
            const itemStack: Array<{ items: SidebarItem[]; level: number; parent?: SidebarItem }> = [
                { items: generatedItems, level: -1 }
            ];

            for (let lineIndex = 0; lineIndex < lines.length; lineIndex++) {
                const line = lines[lineIndex];
                const processedItem = this.processLine(line, gitbookDirAbsPath, lang);
                if (!processedItem) continue;

                const { item, level } = processedItem;

                // Pop stack until we find the correct parent level
                while (itemStack.length > 1 && level <= itemStack[itemStack.length - 1].level) {
                        itemStack.pop();
                    }

                // Add item to current level
                const currentParent = itemStack[itemStack.length - 1];
                currentParent.items.push(item);

                // Check if next lines might be children (look ahead for higher indentation)
                const shouldPrepareForChildren = this.shouldPrepareForChildren(lines, lineIndex + 1, level);

                if (shouldPrepareForChildren) {
                    // Initialize items array for potential children
                    item.items = [];
                    itemStack.push({ items: item.items, level, parent: item });
                }
            }

        } catch (error: any) {

            return [];
        }
        
        // Post-process to clean up empty items and finalize structure
        this.finalizeStructure(generatedItems);

        return generatedItems;
    }

    /**
     * Check if we should prepare for children by looking ahead at the next lines
     */
    private shouldPrepareForChildren(lines: string[], nextLineIndex: number, currentLevel: number): boolean {
        // Look ahead to see if there are any items with higher indentation
        for (let i = nextLineIndex; i < Math.min(nextLineIndex + 5, lines.length); i++) {
            const originalLine = lines[i];
            const trimmedLine = originalLine.trim();
            
            // Skip empty lines and comments
            if (!trimmedLine || trimmedLine.startsWith('#') || 
                (!trimmedLine.startsWith('*') && !trimmedLine.startsWith('-'))) {
                continue;
            }

            // Parse the ORIGINAL line (with spaces) to check its level
            const linkMatch = originalLine.match(/^(\s*)(?:\*|\-)\s+\[(.+?)\]\((.+?)\)/);
            if (linkMatch) {
                const nextLevel = this.calculateLevel(linkMatch[1]);
                
                if (nextLevel > currentLevel) {
                    return true; // Found a child item
                } else {
                    return false; // Found a sibling or uncle, no children expected
                }
            }
        }
        
        return false; // No children found in lookahead
    }

    /**
     * Parse frontmatter from content if present
     */
    private parseFrontmatter(content: string): { frontmatter: Record<string, any>; content: string } {
        const frontmatterRegex = /^---\s*\r?\n([\s\S]*?)\r?\n---\s*\r?\n/;
        const match = content.match(frontmatterRegex);
        
        if (match) {
            try {
                // Basic YAML parsing for simple cases
                const frontmatter: Record<string, any> = {};
                const yamlContent = match[1];
                const yamlLines = yamlContent.split(/\r?\n/);
                
                for (const line of yamlLines) {
                    const colonIndex = line.indexOf(':');
                    if (colonIndex > 0) {
                        const key = line.substring(0, colonIndex).trim();
                        const value = line.substring(colonIndex + 1).trim();
                        frontmatter[key] = value.replace(/^['"]|['"]$/g, ''); // Remove quotes
                    }
                }
                
                return {
                    frontmatter,
                    content: content.substring(match[0].length)
                };
            } catch (error) {

            }
        }
        
        return { frontmatter: {}, content };
    }

    /**
     * Process a single line from SUMMARY.md
     */
    private processLine(line: string, gitbookDirAbsPath: string, lang: string): { item: SidebarItem; level: number } | null {
        const trimmedLine = line.trim();
        
        // Skip empty lines, comments, and non-list items
        if (!trimmedLine || trimmedLine.startsWith('#') || 
            (!trimmedLine.startsWith('*') && !trimmedLine.startsWith('-'))) {
            return null;
        }

        // Parse markdown link using ORIGINAL line (with spaces): * [Title](path) or - [Title](path)
        const linkMatch = line.match(/^(\s*)(?:\*|\-)\s+\[(.+?)\]\((.+?)\)/);
        if (!linkMatch) {
            return null;
        }

        const [, indentStr, title, linkPath] = linkMatch;
        const level = this.calculateLevel(indentStr);

        // Process the link path
        const processedLink = this.processLinkPath(linkPath, gitbookDirAbsPath, lang);
        
        const item: SidebarItem = {
            text: title.trim(),
            link: processedLink.url,
            _relativePathKey: processedLink.relativeKey,
            _hidden: false, // GitBook items are typically published
            _isDirectory: processedLink.isDirectory,
            _isRoot: false,
            collapsed: true // Will be adjusted in finalization
        };

        return { item, level };
    }

    /**
     * Calculate nesting level from indentation
     */
    private calculateLevel(indentStr: string): number {
        // Count spaces - typically 2 spaces per level in GitBook
        const spaces = indentStr.length;
        return Math.floor(spaces / 2); // Assuming 2 spaces per level
    }

    /**
     * Process link path to generate proper URLs and keys
     */
    private processLinkPath(linkPath: string, gitbookDirAbsPath: string, lang: string): {
        url: string;
        relativeKey: string;
        isDirectory: boolean;
    } {
        // Normalize the link path
        let cleanPath = linkPath.trim();
        
        // Remove leading slash if present (GitBook paths are relative to SUMMARY.md)
        if (cleanPath.startsWith('/')) {
            cleanPath = cleanPath.substring(1);
        }

        // Determine if this is a directory (ends with README.md or index.md)
        const isDirectory = cleanPath.endsWith('README.md') || cleanPath.endsWith('index.md') || cleanPath.endsWith('/');

        // Generate the site-absolute URL
        const relativePathFromDocs = normalizePathSeparators(path.relative(this.docsAbsPath, gitbookDirAbsPath));
        let fullUrl = `/${relativePathFromDocs}/${cleanPath}`.replace(/\/\/+/g, '/');

        // Convert .md to clean URLs for VitePress (no extension needed)
        if (fullUrl.endsWith('.md')) {
            fullUrl = fullUrl.slice(0, -3);
        }

        // Handle directory links - only convert index files to directory style
        // Keep README files as-is since some GitBook docs specifically need the README part
        if (isDirectory && fullUrl.endsWith('/index')) {
            fullUrl = path.dirname(fullUrl) + '/';
        }

        // Ensure proper formatting
        if (!fullUrl.startsWith('/')) {
            fullUrl = '/' + fullUrl;
        }

        return {
            url: fullUrl,
            relativeKey: cleanPath,
            isDirectory
        };
    }

    /**
     * Finalize the structure by cleaning up empty items arrays and setting proper collapsed states
     */
    private finalizeStructure(items: SidebarItem[]): void {
            for (const item of items) {
            if (item.items) {
                // Recursively finalize children
                this.finalizeStructure(item.items);
                
                // If no children, remove items array
                if (item.items.length === 0) {
                    delete item.items;
                    item.collapsed = undefined; // No need for collapsed state if no children
                } else {
                    // Has children, ensure collapsed state is set appropriately
                    item.collapsed = true; // Default to collapsed for sections with children
                }
            } else {
                // No children, remove collapsed property
                item.collapsed = undefined;
            }
        }
    }

    /**
     * Enhanced error handling and validation
     */
    private validateStructure(items: SidebarItem[]): boolean {
        try {
            for (const item of items) {
                if (!item.text || !item.link) {

                    return false;
                }
                
                if (item.items && !this.validateStructure(item.items)) {
                    return false;
                }
            }
            return true;
        } catch (error) {

            return false;
        }
    }
} 


