import path from 'node:path';
import glob from 'fast-glob'; // For pattern matching
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
    parentConfigForEntry: EffectiveDirConfig, // Config context for the item being processed by itemProcessor
    lang: string,
    depthForEntry: number,
    isDevMode: boolean,
    configReader: ConfigReaderService,
    fs: FileSystem,
    recursiveGeneratorForSubDir: any, // generateSidebarView from StructuralGeneratorService
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
 * Processes a group defined in frontmatter and generates its SidebarItem.
 * @returns A SidebarItem for the group, or null if the group is empty or hidden.
 */
export async function processGroup(
    groupConfig: GroupConfig,
    // Absolute path of the directory whose index.md defines this group
    baseDirAbsPath: string, 
    // Effective config of the directory whose index.md defines this group
    parentDirEffectiveConfig: EffectiveDirConfig, 
    lang: string,
    // Depth of the group item itself within the parent sidebar structure
    groupItemLevelDepth: number, 
    isDevMode: boolean,
    configReader: ConfigReaderService,
    fs: FileSystem,
    itemProcessor: ItemProcessorFunction,
    recursiveViewGenerator: RecursiveViewGeneratorFunction, // To handle nested groups that are NOT new roots
    globalGitBookExclusionList: string[],
    docsAbsPath: string,
    processedPathsSet: Set<string> 
): Promise<SidebarItem | null> {
    const groupTitle = groupConfig.title;
    

    const groupItems: SidebarItem[] = [];
    
    // Determine content source path for the group's items
    // Defaults to baseDirAbsPath if groupConfig.path is not set
    const groupContentSourceAbsPath = groupConfig.path 
        ? normalizePathSeparators(path.resolve(baseDirAbsPath, groupConfig.path)) 
        : normalizePathSeparators(baseDirAbsPath);

    let resolvedGroupItemOrder: Record<string, number> = {};
    if (Array.isArray(groupConfig.itemOrder)) {
        groupConfig.itemOrder.forEach((item, index) => {
            if (typeof item === 'string') resolvedGroupItemOrder[item] = index;
        });
    } else if (groupConfig.itemOrder && typeof groupConfig.itemOrder === 'object') {
        resolvedGroupItemOrder = groupConfig.itemOrder as Record<string, number>;
    }

    // Create an effective config for items within this group
    // It inherits from parentDirEffectiveConfig but can be overridden by groupConfig properties
    const groupContextEffectiveConfig: EffectiveDirConfig = {
        ...parentDirEffectiveConfig, // Inherit from parent
        title: groupTitle, // Group's title for context, not necessarily for item generation
        maxDepth: groupConfig.maxDepth ?? parentDirEffectiveConfig.maxDepth,
        itemOrder: resolvedGroupItemOrder,
        // _baseRelativePathForChildren should be formed by parent's base + group.path or group.title (slugified)
        // This needs careful construction based on how group.path or group.title forms a logical subpath
        _baseRelativePathForChildren: normalizePathSeparators(path.join(parentDirEffectiveConfig._baseRelativePathForChildren || '', groupConfig.path || slugify(groupTitle)))  + '/' // Ensure trailing slash
    };

    // 1. Process explicit `items` array
    if (Array.isArray(groupConfig.items)) {
        for (const itemEntry of groupConfig.items) {
            let itemName: string;
            let itemIsLikelyDir = false;

            if (typeof itemEntry === 'string') {
                itemName = itemEntry;
                if (itemName.endsWith('/')) itemIsLikelyDir = true;
            } else { // It's a SidebarItem-like object, less common for raw config
                itemName = itemEntry.text || '';
                if (itemEntry.items) itemIsLikelyDir = true;
            }
            if (!itemName) continue;

            const itemAbsPath = normalizePathSeparators(path.resolve(groupContentSourceAbsPath, itemName));
            if (processedPathsSet.has(itemAbsPath) || isGitBookExcluded(itemAbsPath, globalGitBookExclusionList)) continue;

            // Determine actual type by checking fs, unless it's clearly a non-fs item already
            let isDir = itemIsLikelyDir;
            try {
                const stat = await fs.stat(itemAbsPath); // Check existence and type
                isDir = stat.isDirectory();
            } catch { continue; /* Skip if stat fails (e.g. not found) */ }

            const processedItem = await itemProcessor(
                path.basename(itemName.replace(/\/$/, '')), // entryName
                itemAbsPath,
                isDir,
                groupContextEffectiveConfig, // Config context for items within the group
                lang,
                groupItemLevelDepth + 1, // Items within a group are one level deeper
                isDevMode,
                configReader,
                fs,
                recursiveViewGenerator, 
                globalGitBookExclusionList,
                docsAbsPath
            );
            if (processedItem) {
                groupItems.push(processedItem);
                processedPathsSet.add(itemAbsPath);
            }
        }
    }
    // 2. Process `pattern` if no explicit items or if pattern is also provided (additive? TBD by plan)
    else if (groupConfig.pattern) {
        const patterns = Array.isArray(groupConfig.pattern) ? groupConfig.pattern : [groupConfig.pattern];
        const globResults = await glob(patterns.map(p => normalizePathSeparators(p)), { 
            cwd: groupContentSourceAbsPath, 
            onlyFiles: false, // Get both files and directories
            dot: false, 
            objectMode: true // This should give Dirent-like objects
        });

        for (const entry of globResults) {
            const entryName = entry.name;
            const itemAbsPath = normalizePathSeparators(entry.path);
            // Glob with mark:true should suffix dirs with '/', but dirent is more reliable if available.
            // entry.dirent might not be available on all fs-like objects from glob, let's check
            let isDir = entry.name.endsWith('/'); // Fallback if dirent is not there
            if(entry.dirent) {
                isDir = entry.dirent.isDirectory();
            }
             // If still unsure, could use await fs.stat(itemAbsPath).then(s => s.isDirectory()).catch(() => false);

            if (processedPathsSet.has(itemAbsPath) || isGitBookExcluded(itemAbsPath, globalGitBookExclusionList)) continue;

            const processedItem = await itemProcessor(
                entryName.replace(/\/$/, ''),
                itemAbsPath,
                isDir,
                groupContextEffectiveConfig,
                lang,
                groupItemLevelDepth + 1, 
                isDevMode,
                configReader,
                fs,
                recursiveViewGenerator,
                globalGitBookExclusionList,
                docsAbsPath
            );
            if (processedItem) {
                groupItems.push(processedItem);
                processedPathsSet.add(itemAbsPath);
            }
        }
    }

    if (groupItems.length === 0 && !groupConfig.link && ! (groupConfig.path && await fs.exists(path.join(groupContentSourceAbsPath, 'index.md')))) {
        // Only return null if group has no items AND no explicit link AND its path doesn't have an index.md
        // This allows empty groups to still be clickable if they have a link or corresponding index.md
        return null;
    }

    const finalGroupItems = sortItems(groupItems, groupContextEffectiveConfig.itemOrder);

    const groupLink = await generateLink(
        groupTitle, // Item name for link generation is the group title
        'group', 
        baseDirAbsPath, // Group is defined in this dir, its link is relative to this
        docsAbsPath, 
        lang, 
        fs, 
        groupConfig
    );

    return {
        text: groupTitle,
        link: groupLink || undefined,
        items: finalGroupItems.length > 0 ? finalGroupItems : undefined,
        collapsed: groupConfig.collapsed ?? parentDirEffectiveConfig.collapsed ?? true,
        _priority: groupConfig.priority, // Group's own priority among its siblings
        _relativePathKey: groupTitle, // Groups are keyed by their title for parent's itemOrder
        _isDirectory: true, // Treat as directory-like for collapsing etc.
        _isRoot: false, // Groups are not roots themselves
    };
}

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

