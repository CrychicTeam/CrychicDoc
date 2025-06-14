import { resolve } from "path";
import {
    existsSync,
    readFileSync,
    writeFileSync,
    mkdirSync,
    statSync,
    readdirSync,
} from "fs";
import { generateSidebars } from "./main";

export interface SidebarLibConfig {
    rootDir?: string;
    docsDir?: string;
    cacheDir?: string;
    debug?: boolean;
    devMode?: boolean;
    languages: string[];
}

const defaultConfig: Omit<Required<SidebarLibConfig>, "languages"> = {
    rootDir: process.cwd(),
    docsDir: "./docs",
    cacheDir: "./.vitepress/cache/sidebar",
    debug: false,
    devMode: process.env.NODE_ENV === "development",
};

let currentConfig: SidebarLibConfig | null = null;

const memoryCache = new Map<string, { data: any; timestamp: number }>();

const CACHE_EXPIRY = 5 * 60 * 1000;

export function _internalConfigureSidebar(config: SidebarLibConfig): void {
    if (!config.languages || config.languages.length === 0) {
        throw new Error(
            "[SidebarLib] Configuration error: languages array is required and cannot be empty"
        );
    }

    currentConfig = { ...defaultConfig, ...config };

    if (currentConfig.debug) {
        console.log("[SidebarLib] Configuration updated:", currentConfig);
    }
}

/**
 * @deprecated Use sidebarPlugin configuration instead
 */
export function configureSidebar(config: SidebarLibConfig): void {
    console.warn('[SidebarLib] configureSidebar() is deprecated. Please configure via sidebarPlugin() in your vite config instead.');
    _internalConfigureSidebar(config);
}

export function getConfig(): Required<SidebarLibConfig> {
    if (!currentConfig) {
        console.warn('[SidebarLib] No configuration found. Please ensure sidebarPlugin is properly configured in your vite config.');
        
        return {
            ...defaultConfig,
            languages: ['']
        };
    }
    return currentConfig as Required<SidebarLibConfig>;
}

function validateLanguage(lang: string): void {
    const config = getConfig();

    if (!config.languages.includes(lang)) {
        throw new Error(
            `[SidebarLib] Language "${lang}" is not configured. Available languages: ${config.languages.join(
                ", "
            )}`
        );
    }
}

function getCacheKey(lang: string): string {
    return `sidebar_${lang || "root"}`;
}

function getFileCachePath(lang: string): string {
    const config = getConfig();
    const cacheDir = resolve(config.rootDir, config.cacheDir);
    return resolve(cacheDir, `${getCacheKey(lang)}.json`);
}

function isFileCacheValid(lang: string): boolean {
    const config = getConfig();
    const cachePath = getFileCachePath(lang);

    if (!existsSync(cachePath)) {
        return false;
    }

    try {
        const cacheStats = statSync(cachePath);
        const now = Date.now();

        if (now - cacheStats.mtime.getTime() > CACHE_EXPIRY) {
            return false;
        }

        const docsPath = resolve(config.rootDir, config.docsDir, lang);
        if (existsSync(docsPath)) {
            const docsStats = statSync(docsPath);
            if (docsStats.mtime.getTime() > cacheStats.mtime.getTime()) {
                return false;
            }
        }

        return true;
    } catch (error) {
        if (config.debug) {
            console.warn(`[SidebarLib] Error checking cache validity:`, error);
        }
        return false;
    }
}

function readFileCache(lang: string): any | null {
    if (!isFileCacheValid(lang)) {
        return null;
    }

    const config = getConfig();
    const cachePath = getFileCachePath(lang);

    try {
        const content = readFileSync(cachePath, "utf-8");
        const data = JSON.parse(content);

        if (config.debug) {
            console.log(`[SidebarLib] File cache hit for ${lang}`);
        }

        return data;
    } catch (error) {
        if (config.debug) {
            console.warn(`[SidebarLib] Error reading file cache:`, error);
        }
        return null;
    }
}

function writeFileCache(lang: string, data: any): void {
    const config = getConfig();
    const cachePath = getFileCachePath(lang);
    const cacheDir = resolve(config.rootDir, config.cacheDir);

    try {
        if (!existsSync(cacheDir)) {
            mkdirSync(cacheDir, { recursive: true });
        }

        writeFileSync(cachePath, JSON.stringify(data, null, 2));

        if (config.debug) {
            console.log(`[SidebarLib] File cache written for ${lang}`);
        }
    } catch (error) {
        if (config.debug) {
            console.warn(`[SidebarLib] Error writing file cache:`, error);
        }
    }
}

async function generateSidebarForLang(lang: string): Promise<any> {
    const config = getConfig();

    if (config.debug) {
        console.log(`[SidebarLib] Generating sidebar for language: ${lang}`);
    }

    try {
        const result = await generateSidebars({
            docsPath: resolve(config.rootDir, config.docsDir),
            isDevMode: config.devMode,
            lang: lang,
        });

        if (!result) {
            if (config.debug) {
                console.warn(`[SidebarLib] No sidebar generated for ${lang}`);
            }
            return {};
        }

        const langPrefix = lang ? `/${lang}/` : "/";
        const langSidebar: Record<string, any[]> = {};

        for (const [path, items] of Object.entries(result)) {
            if (path.startsWith(langPrefix)) {
                const filteredItems = filterHiddenItems(items as any[]);
                if (filteredItems.length > 0) {
                    langSidebar[path] = filteredItems;
                }
            }
        }

        return langSidebar;
    } catch (error) {
        console.error(
            `[SidebarLib] Error generating sidebar for ${lang}:`,
            error
        );
        return {};
    }
}

function filterHiddenItems(items: any[]): any[] {
    if (!Array.isArray(items)) return items;

    return items
        .filter((item) => !item._hidden)
        .map((item) => {
            if (item.items && Array.isArray(item.items)) {
                return {
                    ...item,
                    items: filterHiddenItems(item.items),
                };
            }
            return item;
        });
}

function checkGeneratedFallback(lang: string): any | null {
    const config = getConfig();
    const generatedPath = resolve(
        config.rootDir,
        ".vitepress/config/generated",
        `sidebar_${lang || "root"}.json`
    );

    if (existsSync(generatedPath)) {
        try {
            const content = readFileSync(generatedPath, "utf-8");
            const data = JSON.parse(content);

            if (config.debug) {
                console.log(
                    `[SidebarLib] Using generated fallback for ${lang}`
                );
            }

            return data;
        } catch (error) {
            if (config.debug) {
                console.warn(
                    `[SidebarLib] Error reading generated fallback:`,
                    error
                );
            }
        }
    }

    return null;
}

/**
 * 内部同步获取侧边栏的实际实现
 */
function _getSidebarSyncInternal(lang: string): Record<string, any[]> {
    // 等待配置就绪
    if (!currentConfig) {
        console.warn(`[SidebarLib] No configuration found. Auto-initializing with fallback config.`);
        _internalConfigureSidebar({
            languages: [''],
            debug: process.env.NODE_ENV === 'development'
        });
    }
    
    validateLanguage(lang);

    const config = getConfig();
    const cacheKey = getCacheKey(lang);
    const memoryItem = memoryCache.get(cacheKey);
    if (memoryItem && Date.now() - memoryItem.timestamp < CACHE_EXPIRY) {
        if (config.debug) {
            console.log(`[SidebarLib] Memory cache hit for ${lang}`);
        }
        return memoryItem.data;
    }
    const fileCache = readFileCache(lang);
    if (fileCache) {
        memoryCache.set(cacheKey, { data: fileCache, timestamp: Date.now() });
        return fileCache;
    }
    const fallback = checkGeneratedFallback(lang);
    if (fallback) {
        memoryCache.set(cacheKey, { data: fallback, timestamp: Date.now() });
        writeFileCache(lang, fallback);
        return fallback;
    }

    if (config.debug) {
        console.warn(
            `[SidebarLib] No cache available for ${lang}, generating asynchronously...`
        );
    }

    generateSidebarForLang(lang)
        .then((data) => {
            memoryCache.set(cacheKey, { data, timestamp: Date.now() });
            writeFileCache(lang, data);
        })
        .catch((error) => {
            console.error(
                `[SidebarLib] Async generation failed for ${lang}:`,
                error
            );
        });

    return {};
}

/**
 * 延迟加载的侧边栏获取函数
 * 
 * 返回一个代理对象，只有在VitePress真正使用时才执行生成
 */
export function getSidebarSync(lang: string): any {
    // 创建一个代理对象，延迟执行实际的侧边栏生成
    return new Proxy({}, {
        get(target, prop, receiver) {
            // 当VitePress访问侧边栏时，才真正执行生成
            const realSidebar = _getSidebarSyncInternal(lang);
            return Reflect.get(realSidebar, prop, receiver);
        },
        
        has(target, prop) {
            const realSidebar = _getSidebarSyncInternal(lang);
            return Reflect.has(realSidebar, prop);
        },
        
        ownKeys(target) {
            const realSidebar = _getSidebarSyncInternal(lang);
            return Reflect.ownKeys(realSidebar);
        },
        
        getOwnPropertyDescriptor(target, prop) {
            const realSidebar = _getSidebarSyncInternal(lang);
            return Reflect.getOwnPropertyDescriptor(realSidebar, prop);
        }
    });
}

export async function getSidebar(lang: string): Promise<Record<string, any[]>> {
    if (!currentConfig) {
        console.warn(`[SidebarLib] No configuration found. Auto-initializing with fallback config.`);
        _internalConfigureSidebar({
            languages: [''],
            debug: process.env.NODE_ENV === 'development'
        });
    }
    
    validateLanguage(lang);

    const config = getConfig();
    const cacheKey = getCacheKey(lang);

    const memoryItem = memoryCache.get(cacheKey);
    if (memoryItem && Date.now() - memoryItem.timestamp < CACHE_EXPIRY) {
        if (config.debug) {
            console.log(`[SidebarLib] Memory cache hit for ${lang}`);
        }
        return memoryItem.data;
    }

    const fileCache = readFileCache(lang);
    if (fileCache) {
        memoryCache.set(cacheKey, { data: fileCache, timestamp: Date.now() });
        return fileCache;
    }

    const data = await generateSidebarForLang(lang);

    memoryCache.set(cacheKey, { data, timestamp: Date.now() });
    writeFileCache(lang, data);

    return data;
}

export async function getAllSidebars(): Promise<
    Record<string, Record<string, any[]>>
> {
    const config = getConfig();
    const result: Record<string, Record<string, any[]>> = {};

    for (const lang of config.languages) {
        result[lang || "root"] = await getSidebar(lang);
    }

    return result;
}

export function clearCache(lang?: string): void {
    const config = getConfig();

    if (lang) {
        validateLanguage(lang);

        const cacheKey = getCacheKey(lang);
        memoryCache.delete(cacheKey);

        const cachePath = getFileCachePath(lang);
        if (existsSync(cachePath)) {
            try {
                require("fs").unlinkSync(cachePath);
            } catch (error) {
                if (config.debug) {
                    console.warn(
                        `[SidebarLib] Error clearing file cache:`,
                        error
                    );
                }
            }
        }
    } else {
        memoryCache.clear();

        const cacheDir = resolve(config.rootDir, config.cacheDir);
        if (existsSync(cacheDir)) {
            try {
                const files = readdirSync(cacheDir);
                for (const file of files) {
                    if (file.startsWith("sidebar_") && file.endsWith(".json")) {
                        require("fs").unlinkSync(resolve(cacheDir, file));
                    }
                }
            } catch (error) {
                if (config.debug) {
                    console.warn(
                        `[SidebarLib] Error clearing cache directory:`,
                        error
                    );
                }
            }
        }
    }

    if (config.debug) {
        console.log(
            `[SidebarLib] Cache cleared${lang ? ` for ${lang}` : " (all)"}`
        );
    }
}

export function getConfiguredLanguages(): string[] {
    const config = getConfig();
    return [...config.languages];
}
