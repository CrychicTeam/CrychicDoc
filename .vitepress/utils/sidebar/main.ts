import path from "node:path";
import glob from "fast-glob";
import matter from "gray-matter";
import {
    SidebarMulti,
    EffectiveDirConfig,
    SidebarItem,
    GlobalSidebarConfig,
} from "./types";
import { ConfigReaderService } from "./config";
import { StructuralGeneratorService } from "./structure";
import { JsonConfigSynchronizerService } from "./overrides";
import { GitBookService } from "./external";
import { GitBookParserService } from "./external/GitBookParserService";
import { UpdateTrackingService } from "./build";
import { FileSystem, NodeFileSystem } from "./shared/index";
import { normalizePathSeparators } from "./shared/objectUtils";

/**
 * @file main.ts
 * @description Top-level orchestrator for the entire sidebar generation process.
 * This file exports the main `generateSidebars` function that drives the system.
 */

/**
 * Options for the main sidebar generation process.
 * @interface GenerateSidebarsOptions
 */
interface GenerateSidebarsOptions {
    /** Absolute path to the root of the `/docs` directory. */
    docsPath: string;
    /** Boolean flag indicating if running in development mode (e.g., `vitepress dev`). */
    isDevMode: boolean;
    /** The specific language code (e.g., 'en', 'zh') to generate the sidebar for. */
    lang: string;
}

/**
 * Finds all `index.md` files within a given language path that are marked with `root: true`.
 * These define independent sidebar roots.
 * Filters out paths that are part of the GitBook exclusion list.
 * Also filters out nested roots that are within the scope of parent roots.
 * @param {string} langPath Absolute path to the language directory (e.g., `/path/to/docs/en`).
 * @param {FileSystem} fs FileSystem instance.
 * @param {string[]} gitbookExclusionList Array of absolute paths to globally excluded GitBook directories.
 * @returns {Promise<string[]>} A promise resolving to an array of absolute paths to `index.md` files that are roots.
 * @private
 */
async function findAllRootIndexMdPaths(
    currentLanguagePath: string,
    fs: FileSystem,
    gitbookExclusionList: string[]
): Promise<string[]> {
    const normalizedLangPath = normalizePathSeparators(currentLanguagePath);
    const pattern = normalizePathSeparators(
        path.join(normalizedLangPath, "**", "index.md")
    );

    const ignorePatterns = gitbookExclusionList
        .map((p) => {
            const relativeP = normalizePathSeparators(
                path.relative(normalizedLangPath, p)
            );
            return relativeP === ""
                ? "**"
                : relativeP.startsWith("..")
                ? undefined
                : `${relativeP}/**`;
        })
        .filter((p) => p !== undefined) as string[];

    const indexFiles = await glob(pattern, {
        cwd: normalizedLangPath,
        ignore: ignorePatterns,
        onlyFiles: true,
        absolute: true,
    });

    const potentialRootIndexPaths: string[] = [];
    for (const filePath of indexFiles) {
        const normalizedFilePath = normalizePathSeparators(filePath);
        
        if (
            gitbookExclusionList.some((exPath) =>
                normalizedFilePath.startsWith(exPath)
            )
        ) {
            continue;
        }
        try {
            const content = await fs.readFile(normalizedFilePath);
            const frontmatter = matter(content).data;
            
            if (frontmatter && frontmatter.root === true) {
                potentialRootIndexPaths.push(normalizedFilePath);
            }
        } catch (e: any) {
        }
    }

    const validRootIndexPaths: string[] = [];
    
    for (const currentRoot of potentialRootIndexPaths) {
        const currentRootDir = normalizePathSeparators(path.dirname(currentRoot));
        
        const relativeToLang = normalizePathSeparators(path.relative(normalizedLangPath, currentRootDir));
        const depthFromLang = relativeToLang === '' ? 0 : relativeToLang.split(path.sep).length;
        
        let isProblematicNestedRoot = false;
        
        for (const otherRoot of potentialRootIndexPaths) {
            if (currentRoot === otherRoot) continue;
            
            const otherRootDir = normalizePathSeparators(path.dirname(otherRoot));
            const otherRelativeToLang = normalizePathSeparators(path.relative(normalizedLangPath, otherRootDir));
            const otherDepthFromLang = otherRelativeToLang === '' ? 0 : otherRelativeToLang.split(path.sep).length;
            
            const isWithinOther = (currentRootDir.startsWith(otherRootDir + '/') || currentRootDir.startsWith(otherRootDir + path.sep));
            const isMuchDeeper = depthFromLang > otherDepthFromLang + 2;
            
            if (isWithinOther && isMuchDeeper) {
                isProblematicNestedRoot = true;
                break;
            }
        }
        
        if (!isProblematicNestedRoot) {
            validRootIndexPaths.push(currentRoot);
        }
    }
    
    return validRootIndexPaths;
}

/**
 * Main orchestration function to generate all sidebars for all configured languages and roots.
 * It reads declarative configurations, generates a base structure, applies JSON overrides,
 * and writes the final `sidebars.json` file in `SidebarMulti` format.
 *
 * @param {GenerateSidebarsOptions} options Configuration options for the generation process.
 * @param {string} options.docsPath Absolute path to the root of the `/docs` directory.
 * @param {boolean} options.isDevMode Boolean flag indicating if running in development mode.
 * @returns {Promise<SidebarMulti | null>} A promise resolving to the generated `SidebarMulti` object,
 *                                        or `null` if a critical error occurs.
 */
export async function generateSidebars(
    options: GenerateSidebarsOptions
): Promise<SidebarMulti | null> {
    const { docsPath, isDevMode, lang } = options;
    const absDocsPath = normalizePathSeparators(path.resolve(docsPath));

    const currentLanguagePath = normalizePathSeparators(path.join(absDocsPath, lang));

    const nodeFs = new NodeFileSystem();
    const configReader = new ConfigReaderService(absDocsPath);
    const gitbookService = new GitBookService(nodeFs);
    const gitbookParserService = new GitBookParserService(nodeFs, absDocsPath);

    const generatedSidebarsDir = normalizePathSeparators(
        path.join(absDocsPath, "..", ".vitepress", "config", "generated")
    );
    const generatedSidebarsPath = normalizePathSeparators(
        path.join(generatedSidebarsDir, "sidebars.json")
    );

    let outputLangSidebar: SidebarMulti = {};

    try {
        configReader.clearCache();

        if (!await nodeFs.exists(currentLanguagePath) || ! (await nodeFs.stat(currentLanguagePath)).isDirectory()) {
            return {};
        }

        const langGitbookPaths = await gitbookService.findGitBookDirectoriesInPath(currentLanguagePath);
        
        const structuralGenerator = new StructuralGeneratorService(
            absDocsPath,
            configReader,
            nodeFs,
            langGitbookPaths 
        );
        const jsonSynchronizer = new JsonConfigSynchronizerService(
            absDocsPath,
            nodeFs
        );

        const normalRootIndexMdPaths = await findAllRootIndexMdPaths(
            currentLanguagePath,
            nodeFs,
            langGitbookPaths
        );

        for (const rootIndexMdPath of normalRootIndexMdPaths) {
            const rootContentPath = normalizePathSeparators(
                path.dirname(rootIndexMdPath)
            );

            let rootKeyInSidebarMulti: string;
            const relativePathFromDocsRoot = normalizePathSeparators(
                path.relative(absDocsPath, rootContentPath) 
            );

            if (relativePathFromDocsRoot === "" || relativePathFromDocsRoot === ".") {
                rootKeyInSidebarMulti = "/";
            } else {
                rootKeyInSidebarMulti = `/${relativePathFromDocsRoot}/`;
            }

            rootKeyInSidebarMulti = rootKeyInSidebarMulti.replace(/\/\/+/g, "/");
            if (rootKeyInSidebarMulti !== "/" && !rootKeyInSidebarMulti.endsWith("/")) {
                 rootKeyInSidebarMulti += "/";
            }
            if (!rootKeyInSidebarMulti.startsWith("/")) {
                rootKeyInSidebarMulti = "/" + rootKeyInSidebarMulti;
            }

            let effectiveConfig = await configReader.getEffectiveConfig(
                rootIndexMdPath,
                lang,
                isDevMode
            );
            effectiveConfig = {
                ...effectiveConfig,
                _baseRelativePathForChildren: "", 
            };

            const structuralItems =
                await structuralGenerator.generateSidebarView(
                    rootContentPath,
                    effectiveConfig,
                    lang,
                    0,
                    isDevMode
                );

            const finalItems = await jsonSynchronizer.synchronize(
                rootKeyInSidebarMulti,
                structuralItems, 
                lang,
                isDevMode,
                langGitbookPaths 
            );

            if (finalItems && finalItems.length > 0) {
                outputLangSidebar[rootKeyInSidebarMulti] = finalItems;
            }
        }
        
        const processedFlattenedSections = new Set<string>();
        
        const searchForFlattenedRoots = async (items: any[], currentPath: string = ''): Promise<void> => {
            if (!Array.isArray(items)) return;
            
            for (const item of items) {
                if (item._isDirectory && item._relativePathKey) {
                    const itemPath = currentPath + item._relativePathKey;
                    const itemPathInDocs = normalizePathSeparators(
                        path.join(currentLanguagePath, itemPath)
                    );
                    const itemIndexPath = normalizePathSeparators(
                        path.join(itemPathInDocs, "index.md")
                    );
                    
                    try {
                        if (await nodeFs.exists(itemIndexPath)) {
                            const itemContent = await nodeFs.readFile(itemIndexPath);
                            const itemFrontmatter = matter(itemContent).data;
                            
                            if (itemFrontmatter && itemFrontmatter.root === true) {
                                const itemRootKey = `/${normalizePathSeparators(path.relative(absDocsPath, itemPathInDocs))}/`;
                                const normalizedItemRootKey = itemRootKey.replace(/\/\/+/g, "/");
                                
                                if (!processedFlattenedSections.has(normalizedItemRootKey)) {
                                    const standaloneSection = [{
                                        ...item,
                                        _isRoot: true
                                    }];
                                    
                                    const processedSection = await jsonSynchronizer.synchronize(
                                        normalizedItemRootKey,
                                        standaloneSection,
                                        lang,
                                        isDevMode,
                                        langGitbookPaths
                                    );
                                    
                                    if (processedSection && processedSection.length > 0) {
                                        const processedItem = processedSection[0];
                                        Object.assign(item, processedItem);
                                        
                                        processedFlattenedSections.add(normalizedItemRootKey);
                                    }
                                }
                            }
                        }
                    } catch (e) {
                    }
                    
                    if (item.items && Array.isArray(item.items)) {
                        await searchForFlattenedRoots(item.items, itemPath);
                    }
                } else if (item.items && Array.isArray(item.items)) {
                    await searchForFlattenedRoots(item.items, currentPath);
                }
            }
        };
        
        for (const [sidebarKey, sidebarItems] of Object.entries(outputLangSidebar)) {
            if (Array.isArray(sidebarItems) && sidebarItems.length === 1) {
                const rootItem = sidebarItems[0];
                if (rootItem._isRoot && rootItem.items && rootItem.items.length > 0) {
                    const pathFromKey = sidebarKey.replace(/^\/+|\/+$/g, '');
                    const sectionPathInDocs = normalizePathSeparators(
                        path.join(absDocsPath, pathFromKey)
                    );
                    const sectionIndexPath = normalizePathSeparators(
                        path.join(sectionPathInDocs, "index.md")
                    );
                    
                    try {
                        if (await nodeFs.exists(sectionIndexPath)) {
                            const sectionContent = await nodeFs.readFile(sectionIndexPath);
                            const sectionFrontmatter = matter(sectionContent).data;
                            
                            if (sectionFrontmatter && sectionFrontmatter.root === true) {
                                const normalizedSectionKey = sidebarKey.replace(/\/\/+/g, "/");
                                
                                if (!processedFlattenedSections.has(normalizedSectionKey)) {
                                    const processedSection = await jsonSynchronizer.synchronize(
                                        normalizedSectionKey,
                                        sidebarItems,
                                        lang,
                                        isDevMode,
                                        langGitbookPaths
                                    );
                                    
                                    if (processedSection && processedSection.length > 0) {
                                        outputLangSidebar[sidebarKey] = processedSection;
                                        
                                        processedFlattenedSections.add(normalizedSectionKey);
                                    }
                                }
                            }
                        }
                    } catch (e) {
                    }
                    
                    await searchForFlattenedRoots(rootItem.items, '');
                    
                    for (const childItem of rootItem.items) {
                        if (childItem._isDirectory && childItem._relativePathKey) {
                            const childPathInDocs = normalizePathSeparators(
                                path.join(currentLanguagePath, childItem._relativePathKey)
                            );
                            const childIndexPath = normalizePathSeparators(
                                path.join(childPathInDocs, "index.md")
                            );
                            
                            try {
                                if (await nodeFs.exists(childIndexPath)) {
                                    const childContent = await nodeFs.readFile(childIndexPath);
                                    const childFrontmatter = matter(childContent).data;
                                    
                                    if (childFrontmatter && childFrontmatter.root === true) {
                                        const childRootKey = `/${normalizePathSeparators(path.relative(absDocsPath, childPathInDocs))}/`;
                                        const normalizedChildRootKey = childRootKey.replace(/\/\/+/g, "/");
                                        
                                        if (!processedFlattenedSections.has(normalizedChildRootKey)) {
                                            const standaloneSection = [{
                                                ...childItem,
                                                _isRoot: true
                                            }];
                                            
                                            const processedSection = await jsonSynchronizer.synchronize(
                                                normalizedChildRootKey,
                                                standaloneSection,
                                                lang,
                                                isDevMode,
                                                langGitbookPaths
                                            );
                                            
                                            if (processedSection && processedSection.length > 0) {
                                                const processedChild = processedSection[0];
                                                Object.assign(childItem, processedChild);
                                                
                                                processedFlattenedSections.add(normalizedChildRootKey);
                                            }
                                        }
                                    }
                                }
                            } catch (e) {
                            }
                        }
                    }
                }
            }
        }

        for (const gitbookDirAbsPath of langGitbookPaths) {
            const gitbookItems = await gitbookParserService.generateSidebarView(
                gitbookDirAbsPath,
                lang
            );

            if (gitbookItems.length > 0) {
                const gitbookKeyInSidebarMulti = `/${normalizePathSeparators(
                    path.relative(absDocsPath, gitbookDirAbsPath)
                )}/`;

                outputLangSidebar[gitbookKeyInSidebarMulti] = gitbookItems;
            }
        }

        return outputLangSidebar;
    } catch (error: any) {
        return null;
    }
}

