import path from 'node:path';
import { SidebarItem, EffectiveDirConfig, FileConfig, GroupConfig, ExternalLinkConfig } from '../types';
import { ConfigReaderService } from '../config'; // To check if a subdir is a new root
import { FileSystem } from '../shared/FileSystem'; // Or direct fs, to check for index.md
import { normalizePathSeparators } from '../shared/objectUtils';
import { ItemProcessorFunction, RecursiveViewGeneratorFunction } from './groupProcessor';
import { processItem } from './itemProcessor';
import { sortItems } from './itemSorter';
// import { generateLink } from './linkGenerator'; // Placeholder for helper
// import { generatePathKey } from './pathKeyGenerator'; // Placeholder for helper

/**
 * @file StructuralGeneratorService.ts
 * @description Service responsible for generating the raw sidebar item structure 
 * for a single sidebar root view. It uses an effective declarative configuration
 * to recursively process files and directories, handle internal groups, manage
 * conditional linking, apply depth limits, and perform initial sorting.
 * It identifies subdirectories that are new roots and creates link-only items for them.
 */
export class StructuralGeneratorService {
    private readonly configReader: ConfigReaderService;
    private readonly fs: FileSystem; // Or use direct fs from 'node:fs/promises'
    private readonly globalGitBookExclusionList: string[];
    private readonly docsPath: string; // Absolute path to /docs

    /**
     * Creates an instance of StructuralGeneratorService.
     * @param docsPath Absolute path to the /docs directory.
     * @param configReader An instance of ConfigReaderService to fetch configurations.
     * @param fileSystem An instance of a FileSystem implementation.
     * @param globalGitBookExclusionList An array of absolute paths to GitBook directories to exclude.
     */
    constructor(
        docsPath: string,
        configReader: ConfigReaderService, 
        fileSystem: FileSystem, // Or remove if using direct fs
        globalGitBookExclusionList: string[] = []
    ) {
        this.docsPath = normalizePathSeparators(docsPath);
        this.configReader = configReader;
        this.fs = fileSystem; 
        this.globalGitBookExclusionList = globalGitBookExclusionList.map(p => normalizePathSeparators(p));
    }

    /**
     * Checks if a given absolute path is within any of the globally excluded GitBook directories.
     * @param absPath The absolute path to check.
     * @returns True if the path is excluded, false otherwise.
     * @private
     */
    private isGitBookExcluded(absPath: string): boolean {
        const normalizedAbsPath = normalizePathSeparators(absPath);
        return this.globalGitBookExclusionList.some(excludedPath => 
            normalizedAbsPath === excludedPath || normalizedAbsPath.startsWith(excludedPath + '/')
        );
    }

    /**
     * Validates and processes external links from configuration.
     * @param externalLinks Array of external link configurations
     * @param baseRelativePathKey Base relative path for generating keys
     * @returns Array of valid SidebarItems for external links
     * @private
     */
    private processExternalLinks(
        externalLinks: ExternalLinkConfig[],
        baseRelativePathKey: string
    ): SidebarItem[] {
        if (!externalLinks || externalLinks.length === 0) {
            return [];
        }

        const externalLinkItems: SidebarItem[] = [];

        for (const linkConfig of externalLinks) {
            // Skip hidden links
            if (linkConfig.hidden) {
                continue;
            }

            // Validate external link
            if (!linkConfig.text || !linkConfig.link) {
                console.warn('External link must have both text and link properties:', linkConfig);
                continue;
            }

            // Ensure it's actually an external link
            if (!linkConfig.link.startsWith('http://') && !linkConfig.link.startsWith('https://')) {
                console.warn('External link must start with http:// or https://:', linkConfig.link);
                continue;
            }

            // Generate unique key for external link
            const linkKey = `external:${linkConfig.text}`;
            const relativePathKey = baseRelativePathKey ? `${baseRelativePathKey}${linkKey}` : linkKey;

            const externalLinkItem: SidebarItem = {
                text: linkConfig.text,
                link: linkConfig.link,
                _priority: linkConfig.priority ?? 0,
                _relativePathKey: relativePathKey,
                _hidden: linkConfig.hidden ?? false,
                _isDirectory: false,
                _isRoot: false,
            };

            externalLinkItems.push(externalLinkItem);
        }

        return externalLinkItems;
    }

    /**
     * Generates flattened content for a root directory by collecting all subdirectory content
     * into a single array instead of creating separate top-level items.
     * @param rootContentPath Absolute path to the root directory.
     * @param rootConfig The effective configuration for the root directory.
     * @param lang Current language code.
     * @param isDevMode Boolean indicating if running in development mode.
     * @returns A promise resolving to a single SidebarItem with all content flattened.
     * @private
     */
    private async generateRootSectionWithFlattenedContent(
        rootContentPath: string,
        rootConfig: EffectiveDirConfig,
        lang: string,
        isDevMode: boolean
    ): Promise<SidebarItem> {
        const normalizedRootPath = normalizePathSeparators(rootContentPath);
        
        // Create the root section container
        const rootSection: SidebarItem = {
            text: rootConfig.title || "Untitled",
            items: [],
            collapsed: rootConfig.collapsed,
            _priority: rootConfig.priority,
            _relativePathKey: "",
            _isDirectory: true,
            _isRoot: true,
            _hidden: rootConfig.hidden || false
        };

        // Get all entries in the root directory
        let entries: { name: string; path: string; dirent?: any; isDirectory(): boolean; isFile(): boolean; }[] = [];
        try {
            const dirents = await this.fs.readDir(normalizedRootPath); 
            entries = dirents.map(d => ({ 
                name: d.name, 
                path: path.join(normalizedRootPath, d.name),
                dirent: d,
                isDirectory: () => d.isDirectory(), 
                isFile: () => d.isFile()
            }));
        } catch (error: any) {
        }

        // Flatten all content from subdirectories and files into the root section
        const flattenedContent = await this.flattenDirectoryContent(
            entries,
            normalizedRootPath,
            rootConfig,
            lang,
            0, // Start at depth 0 for root
            isDevMode
        );

        // Add external links to the root section
        const externalLinkItems = this.processExternalLinks(
            rootConfig.externalLinks || [],
            rootConfig._baseRelativePathForChildren ?? ""
        );

        // Combine flattened content with external links
        const allContent = [...flattenedContent, ...externalLinkItems];

        // Sort all items together
        const sortedContent = sortItems(allContent, rootConfig.itemOrder);

        rootSection.items = sortedContent;
        return rootSection;
    }

    /**
     * Recursively flattens content from directories and files into a single array.
     * @param entries Array of directory entries to process.
     * @param currentPath Current directory path being processed.
     * @param parentConfig Configuration from parent directory.
     * @param lang Current language code.
     * @param currentDepth Current recursion depth.
     * @param isDevMode Boolean indicating if running in development mode.
     * @returns A promise resolving to an array of flattened SidebarItems.
     * @private
     */
    private async flattenDirectoryContent(
        entries: { name: string; path: string; isDirectory(): boolean; isFile(): boolean; }[],
        currentPath: string,
        parentConfig: EffectiveDirConfig,
        lang: string,
        currentDepth: number,
        isDevMode: boolean
    ): Promise<SidebarItem[]> {
        const flattenedItems: SidebarItem[] = [];
        const baseRelativePathKey = parentConfig._baseRelativePathForChildren ?? "";

        for (const entry of entries) {
            const itemAbsPath = normalizePathSeparators(entry.path);
            
            if (this.isGitBookExcluded(itemAbsPath)) {
                continue;
            }

            if (entry.isFile()) {
                // Process files directly - skip index.md files as they represent directories
                if (entry.name.toLowerCase() === "index.md") {
                    continue;
                }

                const fileItem = await processItem(
                    entry.name,
                    itemAbsPath,
                    false, // isDir
                    parentConfig,
                    lang,
                    currentDepth,
                    isDevMode,
                    this.configReader,
                    this.fs,
                    this.generateSidebarView.bind(this) as RecursiveViewGeneratorFunction,
                    this.globalGitBookExclusionList,
                    this.docsPath
                );

                if (fileItem) {
                    flattenedItems.push(fileItem);
                }
            } else {
                // For directories, check if they are nested roots first
                const dirIndexPath = path.join(itemAbsPath, "index.md");
                const dirEffectiveConfig = await this.configReader.getEffectiveConfig(
                    dirIndexPath,
                    lang,
                    isDevMode
                );

                // Skip hidden directories
                if (dirEffectiveConfig.hidden) {
                    continue;
                }

                // If this is a nested root and we're at depth > 0, create a link-only item
                if (dirEffectiveConfig.root && currentDepth > 0) {
                    const rootLinkItem = await processItem(
                        entry.name,
                        itemAbsPath,
                        true, // isDir
                        parentConfig,
                        lang,
                        currentDepth,
                        isDevMode,
                        this.configReader,
                        this.fs,
                        this.generateSidebarView.bind(this) as RecursiveViewGeneratorFunction,
                        this.globalGitBookExclusionList,
                        this.docsPath
                    );

                    if (rootLinkItem) {
                        flattenedItems.push(rootLinkItem);
                    }
                    continue;
                }

                // Get subdirectory entries to check content
                    let subEntries: { name: string; path: string; isDirectory(): boolean; isFile(): boolean; }[] = [];
                let hasMarkdownFiles = false;
                let hasSubdirectories = false;
                
                try {
                        const subDirents = await this.fs.readDir(itemAbsPath);
                        
                        subEntries = subDirents.map(d => ({
                            name: d.name,
                            path: path.join(itemAbsPath, d.name),
                            isDirectory: () => d.isDirectory(),
                            isFile: () => d.isFile()
                        }));

                    // Check what type of content this directory has
                    for (const subEntry of subEntries) {
                        if (subEntry.isFile() && subEntry.name.toLowerCase().endsWith('.md') && subEntry.name.toLowerCase() !== 'index.md') {
                            hasMarkdownFiles = true;
                        }
                        if (subEntry.isDirectory()) {
                            hasSubdirectories = true;
                        }
                    }
                    } catch (error: any) {
                        if (error.code !== 'ENOENT') {
                        console.warn(`Could not read directory ${itemAbsPath}:`, error.message);
                        }
                        continue;
                    }

                // For file-only directories (like flandre/), always create a directory item
                // This ensures RecursiveSynchronizer can generate JSON configs for them
                if (hasMarkdownFiles && !hasSubdirectories) {
                    
                    // Process all markdown files in this directory
                    const dirRelativeKey = baseRelativePathKey ? `${baseRelativePathKey}${entry.name}/` : `${entry.name}/`;
                    const dirConfigForFiles = {
                        ...dirEffectiveConfig,
                        _baseRelativePathForChildren: dirRelativeKey
                    };

                    const fileItems: SidebarItem[] = [];
                    for (const subEntry of subEntries) {
                        if (subEntry.isFile() && subEntry.name.toLowerCase().endsWith('.md') && subEntry.name.toLowerCase() !== 'index.md') {
                            const fileItem = await processItem(
                                subEntry.name,
                                subEntry.path,
                                false, // isDir
                                dirConfigForFiles,
                                lang,
                                currentDepth + 1,
                                isDevMode,
                                this.configReader,
                                this.fs,
                                this.generateSidebarView.bind(this) as RecursiveViewGeneratorFunction,
                                this.globalGitBookExclusionList,
                                this.docsPath
                            );
                            if (fileItem) {
                                fileItems.push(fileItem);
                            }
                        }
                    }

                    // Create the directory item with the files as children
                    const directoryItem = await processItem(
                        entry.name,
                        itemAbsPath,
                        true, // isDir
                        parentConfig,
                        lang,
                        currentDepth,
                        isDevMode,
                        this.configReader,
                        this.fs,
                        this.generateSidebarView.bind(this) as RecursiveViewGeneratorFunction,
                        this.globalGitBookExclusionList,
                        this.docsPath
                    );

                    if (directoryItem) {
                        directoryItem.items = fileItems.length > 0 ? fileItems : undefined;
                        flattenedItems.push(directoryItem);
                    }
                    continue;
                }

                // For regular directories (with subdirectories), check if we should continue based on maxDepth
                // dirEffectiveConfig.maxDepth already includes proper inheritance:
                // - If directory has own maxDepth config, uses that
                // - If directory doesn't have own config, inherits from parent/global
                if (currentDepth < dirEffectiveConfig.maxDepth) {
                    
                    // Create directory config for flattening
                    const dirRelativeKey = baseRelativePathKey ? `${baseRelativePathKey}${entry.name}/` : `${entry.name}/`;
                    const dirConfigForFlattening = {
                        ...dirEffectiveConfig,
                        _baseRelativePathForChildren: dirRelativeKey
                    };

                    // Recursively process subdirectory content
                    const subContent = await this.flattenDirectoryContent(
                        subEntries,
                        itemAbsPath,
                        dirConfigForFlattening,
                        lang,
                        currentDepth + 1,
                        isDevMode
                    );

                    // Create directory item - ALWAYS create it for directories with subdirectories
                    // This ensures proper hierarchical structure even if subdirectories return no visible items
                    
                    const directoryItem = await processItem(
                        entry.name,
                        itemAbsPath,
                        true, // isDir
                        parentConfig,
                        lang,
                        currentDepth,
                        isDevMode,
                        this.configReader,
                        this.fs,
                        this.generateSidebarView.bind(this) as RecursiveViewGeneratorFunction,
                        this.globalGitBookExclusionList,
                        this.docsPath
                    );

                    if (directoryItem) {
                        // Set nested content as children - this creates proper hierarchy
                        directoryItem.items = subContent.length > 0 ? subContent : undefined;
                        flattenedItems.push(directoryItem);
                    }
                } else {
                    // At max depth, just add the directory as a link-only item if linkable
                    const directoryItem = await processItem(
                        entry.name,
                        itemAbsPath,
                        true, // isDir
                        parentConfig,
                        lang,
                        currentDepth,
                        isDevMode,
                        this.configReader,
                        this.fs,
                        this.generateSidebarView.bind(this) as RecursiveViewGeneratorFunction,
                        this.globalGitBookExclusionList,
                        this.docsPath
                    );

                    if (directoryItem) {
                        flattenedItems.push(directoryItem);
                    }
                }
            }
        }

        return flattenedItems;
    }

    /**
     * Generates the `SidebarItem[]` for a single, specific sidebar root view.
     * This method orchestrates the processing of groups and individual file/directory entries.
     * It is recursive for subdirectories that are not new roots themselves.
     *
     * @param currentContentPath Absolute path to the directory currently being processed for this view.
     * @param effectiveConfigForThisView The effective configuration governing this specific view/path.
     *                                    Must have `_baseRelativePathForChildren` set by the caller 
     *                                    (e.g., `main.ts` sets it to '' for top-level roots, 
     *                                    `itemProcessor` sets it for subdirectories).
     * @param lang Current language code.
     * @param currentLevelDepth Internal 0-indexed recursion depth for items within this view.
     * @param isDevMode Boolean indicating if running in development mode (for draft status).
     * @returns A promise resolving to an array of `SidebarItem` objects for the current path.
     */
    public async generateSidebarView(
        currentContentPath: string,
        effectiveConfigForThisView: EffectiveDirConfig, 
        lang: string,
        currentLevelDepth: number, 
        isDevMode: boolean
    ): Promise<SidebarItem[]> {
        const normalizedCurrentContentPath = normalizePathSeparators(currentContentPath);
        
        // Check if this is a root directory processing call (depth 0 and has root: true)
        const isRootDirectoryProcessing = currentLevelDepth === 0 && effectiveConfigForThisView.root;
        
        if (isRootDirectoryProcessing) {
            // Use flattening mode for root directories
            const rootSection = await this.generateRootSectionWithFlattenedContent(
                normalizedCurrentContentPath,
                effectiveConfigForThisView,
                lang,
                isDevMode
            );
            
            return [rootSection];
        }

        // Original logic for non-root directory processing (recursive calls, groups, etc.)
        const generatedItems: SidebarItem[] = [];
        
        // Ensure _baseRelativePathForChildren is initialized for items at this level.
        // This property is crucial for generating correct relative keys for children.
        // For the very first call to generateSidebarView for a root, main.ts should ensure this is ''.
        // For recursive calls (e.g. from itemProcessor for a subdirectory), the caller sets it.
        const baseRelativePathKeyForChildrenInThisScope = effectiveConfigForThisView._baseRelativePathForChildren ?? '';
        
        // Create a version of the current config that explicitly carries this base path for its children.
        // This is what gets passed down as 'parentViewEffectiveConfig' to itemProcessor/groupProcessor.
        const currentScopeConfigWithBaseKey: EffectiveDirConfig = {
            ...effectiveConfigForThisView,
            _baseRelativePathForChildren: baseRelativePathKeyForChildrenInThisScope
        };

        // Groups are now handled at the main.ts level through reorganization
        // to avoid config regeneration issues

        // 1. Entry Processing
        let entries: { name: string; path: string; dirent?: any; isDirectory(): boolean; isFile(): boolean; }[] = [];
        
        try {
            const dirents = await this.fs.readDir(normalizedCurrentContentPath); 
            entries = dirents.map(d => ({ 
                name: d.name, 
                path: path.join(normalizedCurrentContentPath, d.name),
                dirent: d,
                isDirectory: () => d.isDirectory(), 
                isFile: () => d.isFile()
            }));
        } catch (error: any) {
            if (error.code !== 'ENOENT') {
                console.warn(`Could not read directory ${normalizedCurrentContentPath}:`, error.message);
            }
        }

        for (const entry of entries) {
            const itemAbsPath = normalizePathSeparators(entry.path);
            
            // Skip GitBook excluded paths
            if (this.isGitBookExcluded(itemAbsPath)) {
                continue;
            }
            
            const item = await processItem(
                entry.name,
                itemAbsPath,
                entry.isDirectory(),
                currentScopeConfigWithBaseKey,
                lang,
                currentLevelDepth, 
                isDevMode,
                this.configReader,
                this.fs,
                this.generateSidebarView.bind(this) as RecursiveViewGeneratorFunction, 
                this.globalGitBookExclusionList,
                this.docsPath
            );

            if (item) {
                generatedItems.push(item);
            }
        }

        // 2. External Links Processing
        const externalLinkItems = this.processExternalLinks(
            effectiveConfigForThisView.externalLinks || [],
            baseRelativePathKeyForChildrenInThisScope
        );
        
        // Combine file/directory items with external links
        const allItems = [...generatedItems, ...externalLinkItems];

        // 3. Sorting
        const sortedItems = sortItems(allItems, currentScopeConfigWithBaseKey.itemOrder);
        
        return sortedItems;
    }


} 

