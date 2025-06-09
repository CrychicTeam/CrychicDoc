import path from 'node:path';
import { SidebarItem, GroupConfig, EffectiveDirConfig } from '../types';
import { FileSystem } from '../shared/FileSystem';
import { ConfigReaderService } from '../config';
import { generateLink } from './linkGenerator';
import { sortItems } from './itemSorter';
import { normalizePathSeparators } from '../shared/objectUtils';

// Type for the itemProcessor callback
export type ItemProcessorFunction = (
    entryName: string,
    itemAbsPath: string,
    isDir: boolean,
    parentConfigForEntry: EffectiveDirConfig,
    lang: string,
    depthForEntry: number,
    isDevMode: boolean,
    configReader: ConfigReaderService,
    fs: FileSystem,
    recursiveGeneratorForSubDir: any,
    globalGitBookExclusionList: string[],
    docsAbsPath: string
) => Promise<SidebarItem | null>;

// Type for the recursiveViewGenerator callback (generateSidebarView)
export type RecursiveViewGeneratorFunction = (
    contentPath: string,
    effectiveConfig: EffectiveDirConfig,
    lang: string,
    depth: number,
    isDevMode: boolean
) => Promise<SidebarItem[]>;

/**
 * Processes a group defined in frontmatter and generates its separate SidebarItem.
 * This creates a standalone sidebar section that extracts content from the specified path.
 * 
 * @param groupConfig - The simplified group configuration
 * @param baseDirAbsPath - Absolute path of the directory defining this group
 * @param parentDirEffectiveConfig - Effective config of the parent directory
 * @param lang - Current language code
 * @param isDevMode - Whether running in development mode
 * @param configReader - Configuration reader service
 * @param fs - File system interface
 * @param recursiveViewGenerator - Function to generate nested content
 * @param globalGitBookExclusionList - Paths to exclude from processing
 * @param docsAbsPath - Absolute path to docs root
 * @returns Promise resolving to a SidebarItem for the group, or null if invalid
 */
export async function processGroup(
    groupConfig: GroupConfig,
    baseDirAbsPath: string,
    parentDirEffectiveConfig: EffectiveDirConfig,
    lang: string,
    isDevMode: boolean,
    configReader: ConfigReaderService,
    fs: FileSystem,
    recursiveViewGenerator: RecursiveViewGeneratorFunction,
    globalGitBookExclusionList: string[],
    docsAbsPath: string
): Promise<SidebarItem | null> {
    const groupTitle = groupConfig.title;
    const groupPath = groupConfig.path;

    // Resolve the absolute path for the group content
    const groupContentAbsPath = normalizePathSeparators(
        path.resolve(baseDirAbsPath, groupPath)
    );

    // Check if the group path exists
    try {
        const stat = await fs.stat(groupContentAbsPath);
        if (!stat.isDirectory()) {
            console.warn(`Group path is not a directory: ${groupContentAbsPath}`);
            return null;
        }
    } catch (error) {
        console.warn(`Group path does not exist: ${groupContentAbsPath}`);
        return null;
    }

    // Check for index.md in the group directory to get configuration
    const groupIndexPath = path.join(groupContentAbsPath, 'index.md');
    let groupEffectiveConfig: EffectiveDirConfig;
    
    try {
        groupEffectiveConfig = await configReader.getEffectiveConfig(
            groupIndexPath,
            lang,
            isDevMode
        );
    } catch (error) {
        // If no index.md or error reading config, use parent config as base
        groupEffectiveConfig = {
            ...parentDirEffectiveConfig,
            title: groupTitle,
            root: false, // Groups are not roots themselves
            priority: groupConfig.priority ?? 0,
            maxDepth: groupConfig.maxDepth ?? parentDirEffectiveConfig.maxDepth,
            path: groupContentAbsPath,
            _baseRelativePathForChildren: ''
        };
    }

    // Override config with group-specific settings
    groupEffectiveConfig = {
        ...groupEffectiveConfig,
        title: groupTitle,
        priority: groupConfig.priority ?? groupEffectiveConfig.priority ?? 0,
        maxDepth: groupConfig.maxDepth ?? groupEffectiveConfig.maxDepth,
        _baseRelativePathForChildren: ''
    };

    // Generate content for the group using the recursive view generator
    const groupItems = await recursiveViewGenerator(
        groupContentAbsPath,
        groupEffectiveConfig,
        lang,
        0, // Start at depth 0 for the group
        isDevMode
    );

    // If no items generated, don't create an empty group
    if (!groupItems || groupItems.length === 0) {
        return null;
    }

    // Generate link for the group (to its index.md if it exists)
    const groupLink = await generateLink(
        groupTitle,
        'group',
        baseDirAbsPath,
        docsAbsPath,
        lang,
        fs,
        groupConfig
    );

    // Create the group sidebar item
    const groupSidebarItem: SidebarItem = {
        text: groupTitle,
        link: groupLink || undefined,
        items: groupItems,
        collapsed: true, // Groups are collapsed by default
        _priority: groupConfig.priority ?? 0,
        _relativePathKey: groupPath,
        _isDirectory: true,
        _isRoot: false,
        _hidden: false
    };

    return groupSidebarItem;
}

/**
 * Extracts grouped content from sidebar items and returns both the extracted groups
 * and the remaining items with grouped content removed.
 * 
 * @param sidebarItems - Original sidebar items
 * @param groups - Group configurations to process
 * @param baseDirAbsPath - Base directory path
 * @param parentConfig - Parent directory configuration
 * @param lang - Language code
 * @param isDevMode - Development mode flag
 * @param configReader - Configuration reader
 * @param fs - File system interface
 * @param recursiveViewGenerator - Recursive generator function
 * @param globalGitBookExclusionList - Exclusion list
 * @param docsAbsPath - Docs root path
 * @returns Promise resolving to extracted groups and filtered items
 */
export async function extractGroups(
    sidebarItems: SidebarItem[],
    groups: GroupConfig[],
    baseDirAbsPath: string,
    parentConfig: EffectiveDirConfig,
    lang: string,
    isDevMode: boolean,
    configReader: ConfigReaderService,
    fs: FileSystem,
    recursiveViewGenerator: RecursiveViewGeneratorFunction,
    globalGitBookExclusionList: string[],
    docsAbsPath: string
): Promise<{ extractedGroups: SidebarItem[]; filteredItems: SidebarItem[] }> {
    const extractedGroups: SidebarItem[] = [];
    const pathsToRemove = new Set<string>();

    // Process each group configuration
    for (const groupConfig of groups) {
        const groupItem = await processGroup(
            groupConfig,
            baseDirAbsPath,
            parentConfig,
            lang,
            isDevMode,
            configReader,
            fs,
            recursiveViewGenerator,
            globalGitBookExclusionList,
            docsAbsPath
        );

        if (groupItem) {
            extractedGroups.push(groupItem);
            
            // Track the path to remove from original items
            const groupAbsPath = normalizePathSeparators(
                path.resolve(baseDirAbsPath, groupConfig.path)
            );
            pathsToRemove.add(groupAbsPath);
        }
    }

    // Filter out items that match the grouped paths
    const filteredItems = filterItemsByPaths(sidebarItems, pathsToRemove);

    return { extractedGroups, filteredItems };
}

/**
 * Removes sidebar items that match the specified paths.
 * 
 * @param items - Sidebar items to filter
 * @param pathsToRemove - Set of absolute paths to remove
 * @returns Filtered sidebar items
 */
function filterItemsByPaths(items: SidebarItem[], pathsToRemove: Set<string>): SidebarItem[] {
    return items.filter(item => {
        // Check if this item's path matches any path to remove
        if (item._filePath && pathsToRemove.has(normalizePathSeparators(item._filePath))) {
            return false;
        }
        
        // For directory items, check if the directory path matches
        if (item._isDirectory && item._relativePathKey) {
            // This is a rough check - might need refinement based on actual path structure
            const itemPath = item._relativePathKey.replace(/\/$/, '');
            for (const pathToRemove of pathsToRemove) {
                if (pathToRemove.endsWith(itemPath) || pathToRemove.includes(`/${itemPath}/`)) {
                    return false;
                }
            }
        }
        
        // Recursively filter nested items
        if (item.items) {
            item.items = filterItemsByPaths(item.items, pathsToRemove);
        }
        
        return true;
    });
}

/**
 * Extracts and reorganizes groups from a fully synchronized sidebar structure.
 * This preserves all existing configurations while creating independent group entries.
 * 
 * @param synchronizedItems - The fully synchronized sidebar items with all configs applied
 * @param groups - Group configurations to process
 * @param originalRootKey - Original root key (e.g., "/zh/modpack/kubejs/1.20.1/")
 * @param rootContentPath - Root content path
 * @param docsAbsPath - Docs absolute path
 * @param lang - Language code
 * @returns Promise resolving to reorganized sidebar structure
 */
// This function is no longer used - group processing now happens in main.ts

// Helper for slugify, can be moved to shared utils
function slugify(text: string): string {
    return text.toString().toLowerCase()
        .replace(/\s+/g, '-')
        .replace(/[^\w-]+/g, '')
        .replace(/--+/g, '-')
        .replace(/^-+/, '')
        .replace(/-+$/, '');
}

function isGitBookExcluded(absPath: string, exclusionList: string[]): boolean {
    const normalizedAbsPath = normalizePathSeparators(absPath);
    return exclusionList.some(excludedPath => 
        normalizedAbsPath === excludedPath || normalizedAbsPath.startsWith(excludedPath + '/')
    );
} 

