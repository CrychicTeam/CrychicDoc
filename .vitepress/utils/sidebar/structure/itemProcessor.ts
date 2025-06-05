import path from "node:path";
import matter from "gray-matter";
import { SidebarItem, EffectiveDirConfig, FileConfig } from "../types";
import { FileSystem } from "../shared/FileSystem";
import { ConfigReaderService } from "../config";
import { generateLink } from "./linkGenerator";
import { generatePathKey } from "./pathKeyGenerator";
import { normalizePathSeparators } from "../shared/objectUtils";

/**
 * @file itemProcessor.ts
 * @description Core processor for individual file system entries in sidebar generation.
 * Handles both files and directories, applying configurations and creating SidebarItem objects.
 */

/**
 * Checks if an absolute path is excluded due to GitBook restrictions.
 * @param absPath The absolute path to check
 * @param exclusionList Array of absolute paths to excluded GitBook directories
 * @returns True if the path should be excluded
 */
function isGitBookExcluded(absPath: string, exclusionList: string[]): boolean {
    const normalizedAbsPath = normalizePathSeparators(absPath);
    return exclusionList.some(
        (excludedPath) =>
            normalizedAbsPath === excludedPath ||
            normalizedAbsPath.startsWith(excludedPath + "/")
    );
}

/**
 * Processes a markdown file entry and creates a SidebarItem.
 * @param entryName The filename (e.g., 'my-doc.md')
 * @param normalizedItemAbsPath Normalized absolute path to the file
 * @param itemRelativePathKey The relative path key for this file
 * @param docsAbsPath Absolute path to the /docs directory
 * @param lang Current language code
 * @param fs FileSystem instance
 * @returns A SidebarItem for the file or null if it should be excluded
 */
async function processFileEntry(
    entryName: string,
    normalizedItemAbsPath: string,
    itemRelativePathKey: string,
    docsAbsPath: string,
    lang: string,
    fs: FileSystem
): Promise<SidebarItem | null> {
    // Skip non-markdown files
    if (!entryName.toLowerCase().endsWith('.md')) {
        return null;
    }

    if (entryName.toLowerCase() === "index.md") {
        return null;
    }

    let fileFrontmatter: Partial<FileConfig> = {};
    try {
        const fileContent = await fs.readFile(normalizedItemAbsPath);
        fileFrontmatter = matter(fileContent).data as Partial<FileConfig>;
    } catch (e: any) {
        if (e.code !== "ENOENT") {
            // File exists but couldn't be read - continue with defaults
        }
    }

    const hidden = fileFrontmatter.hidden ?? false;
    if (hidden) {
        return null;
    }

    const title = fileFrontmatter.title || entryName.replace(/\.md$/i, "");
    
    const relativeToLangRoot = normalizePathSeparators(
        path.relative(path.join(docsAbsPath, lang), normalizedItemAbsPath)
    );
    let link = `/${lang}/${relativeToLangRoot.replace(/\.md$/i, ".html")}`.replace(/\/+/g, "/");
    if (link.startsWith("//")) link = link.substring(1);

    return {
        text: title,
        link: link,
        _priority: fileFrontmatter.priority,
        _relativePathKey: itemRelativePathKey,
        _hidden: hidden,
        _isDirectory: false,
        _isRoot: false,
    };
}

/**
 * Creates a link-only item for a subdirectory that defines a new sidebar root.
 * @param entryName The directory name
 * @param normalizedItemAbsPath Normalized absolute path to the directory
 * @param itemRelativePathKey The relative path key for this directory
 * @param dirEffectiveConfig The effective configuration for this directory
 * @param docsAbsPath Absolute path to the /docs directory
 * @param lang Current language code
 * @param fs FileSystem instance
 * @returns A SidebarItem representing the root link or null if not linkable
 */
async function createRootLinkItem(
    entryName: string,
    normalizedItemAbsPath: string,
    itemRelativePathKey: string,
    dirEffectiveConfig: EffectiveDirConfig,
    docsAbsPath: string,
    lang: string,
    fs: FileSystem
): Promise<SidebarItem | null> {
    const linkToSubRoot = await generateLink(
        entryName,
        "directory",
        path.dirname(normalizedItemAbsPath),
        docsAbsPath,
        lang,
        fs
    );

    if (!linkToSubRoot) {
        return null;
    }

    return {
        text: dirEffectiveConfig.title,
        link: linkToSubRoot,
        items: [],
        collapsed: dirEffectiveConfig.collapsed,
        _priority: dirEffectiveConfig.priority,
        _relativePathKey: itemRelativePathKey,
        _isDirectory: true,
        _isRoot: true,
        _hidden: dirEffectiveConfig.hidden || false,
    };
}

/**
 * Processes a directory entry and creates a SidebarItem with children.
 * @param entryName The directory name
 * @param normalizedItemAbsPath Normalized absolute path to the directory
 * @param itemRelativePathKey The relative path key for this directory
 * @param dirEffectiveConfig The effective configuration for this directory
 * @param parentViewEffectiveConfig Configuration from the parent directory
 * @param currentLevelDepth Current recursion depth (0-indexed)
 * @param lang Current language code
 * @param isDevMode Whether running in development mode
 * @param docsAbsPath Absolute path to the /docs directory
 * @param fs FileSystem instance
 * @param recursiveGenerator Function for recursive sidebar generation
 * @returns A SidebarItem for the directory or null if it should be excluded
 */
async function processDirectoryEntry(
    entryName: string,
    normalizedItemAbsPath: string,
    itemRelativePathKey: string,
    dirEffectiveConfig: EffectiveDirConfig,
    parentViewEffectiveConfig: EffectiveDirConfig,
    currentLevelDepth: number,
    lang: string,
    isDevMode: boolean,
    docsAbsPath: string,
    fs: FileSystem,
    recursiveGenerator: (
        contentPath: string,
        effectiveConfig: EffectiveDirConfig,
        lang: string,
        depth: number,
        devMode: boolean
    ) => Promise<SidebarItem[]>
): Promise<SidebarItem | null> {
    let subItems: SidebarItem[] = [];
    
    if (currentLevelDepth < parentViewEffectiveConfig.maxDepth) {
        const subDirContextConfig = {
            ...dirEffectiveConfig,
            _baseRelativePathForChildren: itemRelativePathKey,
        };
        subItems = await recursiveGenerator(
            normalizedItemAbsPath,
            subDirContextConfig,
            lang,
            currentLevelDepth + 1,
            isDevMode
        );
    }

    const linkToDir = await generateLink(
        entryName,
        "directory",
        path.dirname(normalizedItemAbsPath),
        docsAbsPath,
        lang,
        fs
    );

    if (subItems.length === 0 && !linkToDir) {
        return null;
    }

    const directoryTitle = await getDirectoryTitle(
        entryName,
        normalizedItemAbsPath,
        fs
    );

    return {
        text: directoryTitle,
        link: linkToDir || undefined,
        items: subItems.length > 0 ? subItems : undefined,
        collapsed: dirEffectiveConfig.collapsed,
        _priority: dirEffectiveConfig.priority,
        _relativePathKey: itemRelativePathKey,
        _isDirectory: true,
        _isRoot: false,
        _hidden: dirEffectiveConfig.hidden || false,
    };
}

/**
 * Determines the appropriate title for a directory.
 * Priority: 1. Directory's own frontmatter title, 2. Directory name
 * @param entryName The directory name
 * @param normalizedItemAbsPath Normalized absolute path to the directory
 * @param fs FileSystem instance
 * @returns The resolved directory title
 */
async function getDirectoryTitle(
    entryName: string,
    normalizedItemAbsPath: string,
    fs: FileSystem
): Promise<string> {
    let directoryTitle = entryName;
    
    try {
        const dirIndexPath = path.join(normalizedItemAbsPath, "index.md");
        if (await fs.exists(dirIndexPath)) {
            const dirIndexContent = await fs.readFile(dirIndexPath);
            const dirFrontmatter = matter(dirIndexContent).data;
            if (dirFrontmatter && dirFrontmatter.title) {
                directoryTitle = dirFrontmatter.title;
            }
        }
    } catch (e: any) {
        // If can't read index.md, fallback to directory name
    }

    return directoryTitle;
}

/**
 * Processes a single file system entry (file or directory) and returns a SidebarItem or null.
 * This is the main entry point for processing individual items in the sidebar generation.
 * 
 * @param entryName Filename (e.g., 'my-doc.md') or dirname (e.g., 'concepts')
 * @param itemAbsPath Absolute path to the file or directory
 * @param isDir Whether the entry is a directory
 * @param parentViewEffectiveConfig Config of the parent directory providing context
 * @param lang Current language code
 * @param currentLevelDepth 0-indexed recursion depth
 * @param isDevMode Whether running in development mode
 * @param configReader Instance for reading directory configurations
 * @param fs FileSystem instance
 * @param recursiveGenerator Main generateSidebarView method for recursion
 * @param globalGitBookExclusionList Array of absolute paths to excluded GitBook directories
 * @param docsAbsPath Absolute path to the /docs directory
 * @returns A SidebarItem for the entry or null if it should be excluded
 */
export async function processItem(
    entryName: string,
    itemAbsPath: string,
    isDir: boolean,
    parentViewEffectiveConfig: EffectiveDirConfig,
    lang: string,
    currentLevelDepth: number,
    isDevMode: boolean,
    configReader: ConfigReaderService,
    fs: FileSystem,
    recursiveGenerator: (
        contentPath: string,
        effectiveConfig: EffectiveDirConfig,
        lang: string,
        depth: number,
        devMode: boolean
    ) => Promise<SidebarItem[]>,
    globalGitBookExclusionList: string[],
    docsAbsPath: string
): Promise<SidebarItem | null> {
    const normalizedItemAbsPath = normalizePathSeparators(itemAbsPath);

    if (isGitBookExcluded(normalizedItemAbsPath, globalGitBookExclusionList)) {
        return null;
    }

    const parentKeyForChildren = parentViewEffectiveConfig._baseRelativePathForChildren ?? "";
    const itemRelativePathKey = generatePathKey(
        entryName,
        isDir ? "directory" : "file",
        parentKeyForChildren
    );

    if (!isDir) {
        return processFileEntry(
            entryName,
            normalizedItemAbsPath,
            itemRelativePathKey,
            docsAbsPath,
            lang,
            fs
        );
    }

    const dirIndexPath = path.join(normalizedItemAbsPath, "index.md");
    const dirEffectiveConfig = await configReader.getEffectiveConfig(
        dirIndexPath,
        lang,
        isDevMode
    );

    if (dirEffectiveConfig.hidden) {
        return null;
    }

    const isProcessingWithinExistingRoot = currentLevelDepth > 0;
    
    if (
        dirEffectiveConfig.root &&
        normalizedItemAbsPath !== parentViewEffectiveConfig.path &&
        !isProcessingWithinExistingRoot
    ) {
        return createRootLinkItem(
            entryName,
            normalizedItemAbsPath,
            itemRelativePathKey,
            dirEffectiveConfig,
            docsAbsPath,
            lang,
            fs
        );
    }

    return processDirectoryEntry(
        entryName,
        normalizedItemAbsPath,
        itemRelativePathKey,
        dirEffectiveConfig,
        parentViewEffectiveConfig,
        currentLevelDepth,
        lang,
        isDevMode,
        docsAbsPath,
        fs,
        recursiveGenerator
    );
}