import path from 'node:path';
import fs from 'node:fs/promises';
import yaml from 'js-yaml';
import matter from 'gray-matter';
import { 
    GlobalSidebarConfig, 
    DirectoryConfig, 
    EffectiveDirConfig
} from '../types';
import { deepMerge, normalizePathSeparators } from '../shared/objectUtils'; // Assuming deepMerge is moved to shared
import { loadGlobalConfig } from './globalConfigLoader';
import { loadFrontmatter } from './frontmatterParser';
import { getPathHierarchy } from './configHierarchyResolver';
import { applyConfigDefaults } from './configDefaultsProvider';

/**
 * @file ConfigReaderService.ts
 * @description Service responsible for loading and merging sidebar configurations
 * from global `.sidebarrc.yml` and hierarchical `index.md` frontmatter.
 * It acts as a facade, orchestrating calls to more specialized loader, parser, resolver,
 * merger (implicitly via deepMerge), and defaults provider modules/functions.
 */

export class ConfigReaderService {
    private readonly docsPath: string;
    private globalConfigCache: GlobalSidebarConfig | null | undefined = undefined;
    private frontmatterCache: Map<string, Partial<DirectoryConfig>> = new Map();

    /**
     * Creates an instance of ConfigReaderService.
     * @param docsPath Absolute path to the root of the documentation (e.g., /path/to/project/docs).
     */
    constructor(docsPath: string) {
        this.docsPath = path.resolve(docsPath);
    }

    /**
     * Loads and caches global configuration from `docs/.sidebarrc.yml`.
     * Delegates to `loadGlobalConfig` helper.
     * @returns A promise resolving to a partial `GlobalSidebarConfig` object, 
     *          or an empty object if not found or an error occurs.
     * @private
     */
    private async getGlobalConfig(): Promise<Partial<GlobalSidebarConfig>> {
        if (this.globalConfigCache !== undefined) {
            return this.globalConfigCache === null ? {} : this.globalConfigCache;
        }
        this.globalConfigCache = await loadGlobalConfig(this.docsPath);
        // Ensure return is never null from this point, to simplify consumer logic.
        return this.globalConfigCache === null ? {} : this.globalConfigCache!;
    }

    /**
     * Loads and caches frontmatter from a given index.md file path.
     * Delegates to `loadFrontmatter` helper.
     * @param absoluteIndexMdPath Absolute path to the index.md file.
     * @returns A promise resolving to a partial `DirectoryConfig` object from the frontmatter, 
     *          or an empty object if not found, no frontmatter, or an error occurs.
     * @private
     */
    private async getFrontmatter(absoluteIndexMdPath: string): Promise<Partial<DirectoryConfig>> {
        const normalizedPath = normalizePathSeparators(absoluteIndexMdPath);
        if (!this.frontmatterCache.has(normalizedPath)) {
            const fm = await loadFrontmatter(normalizedPath);
            this.frontmatterCache.set(normalizedPath, fm);
            return fm;
        }
        return this.frontmatterCache.get(normalizedPath)!;
    }

    /**
     * Retrieves the effective, merged configuration for a directory specified by its `index.md` path.
     * This is the main public method of this service.
     * It considers global settings (`.sidebarrc.yml`), and hierarchically merges `index.md` frontmatter
     * from the language root up to the specified `indexMdAbsPath`.
     * Finally, it applies system defaults to ensure a complete `EffectiveDirConfig`.
     *
     * @param indexMdAbsPath Absolute path to the target directory's `index.md` file that defines the scope.
     * @param lang The current language code (e.g., 'en', 'zh').
     * @param isDevMode Boolean indicating if running in development mode (affects draft status handling).
     * @returns A promise resolving to a fully resolved `EffectiveDirConfig` object.
     */
    public async getEffectiveConfig(indexMdAbsPath: string, lang: string, isDevMode: boolean): Promise<EffectiveDirConfig> {
        const globalConfigData = await this.getGlobalConfig();
        const directoryPath = normalizePathSeparators(path.dirname(indexMdAbsPath));
        const langRootPath = normalizePathSeparators(path.join(this.docsPath, lang));

        // Determine the hierarchy of index.md files whose configs need to be merged.
        const hierarchyIndexMdPaths = getPathHierarchy(directoryPath, langRootPath, this.docsPath);

        let mergedConfig: Partial<EffectiveDirConfig> = {}; 

        // Start with global defaults, if any.
        if (globalConfigData.defaults) {
            mergedConfig = deepMerge<Partial<EffectiveDirConfig>>({}, globalConfigData.defaults as Partial<EffectiveDirConfig>);
        }

        // Merge frontmatter from each index.md in the hierarchy.
        // NOTE: We exclude 'root' property from hierarchical inheritance as it should only apply 
        // to the specific directory that declares it, not its children.
        for (const hIndexMdPath of hierarchyIndexMdPaths) {
            const frontmatter = await this.getFrontmatter(hIndexMdPath);
            const { root: _, ...frontmatterWithoutRoot } = frontmatter; // Exclude 'root' from inheritance
            mergedConfig = deepMerge<Partial<EffectiveDirConfig>>(mergedConfig, frontmatterWithoutRoot as Partial<EffectiveDirConfig>);
        }
        
        // Ensure the specific target index.md frontmatter has the highest precedence.
        // For the target directory itself, we DO include the 'root' property if present.
        const targetFrontmatter = await this.getFrontmatter(indexMdAbsPath); 
        mergedConfig = deepMerge<Partial<EffectiveDirConfig>>(mergedConfig, targetFrontmatter as Partial<EffectiveDirConfig>);



        // Apply final system defaults and resolve all required fields.
        const finalConfig = applyConfigDefaults(
            mergedConfig, 
            directoryPath, 
            lang, 
            isDevMode,
            globalConfigData.defaults // Pass global defaults again for applyConfigDefaults to use
        );

        return finalConfig;
    }

    /**
     * Clears the internal caches for global configuration and frontmatter data.
     * This should be called if underlying files (`.sidebarrc.yml`, `index.md` files) change.
     */
    public clearCache(): void {
        this.globalConfigCache = undefined; // Mark as not loaded
        this.frontmatterCache.clear();
        // console.log('[ConfigReaderService] Caches cleared.');
    }
} 

