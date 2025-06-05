import { DefaultTheme } from 'vitepress'
import path from 'node:path'
import fs from 'node:fs'
import { fileURLToPath } from 'node:url'
import { generateSidebars } from '../utils/sidebar'

/**
 * Recursively filter out hidden items from sidebar structure
 */
function filterHiddenItems(items: DefaultTheme.SidebarItem[]): DefaultTheme.SidebarItem[] {
    if (!Array.isArray(items)) return items;
    
    return items
        .filter(item => !(item as any)._hidden)
        .map(item => {
            if (item.items && Array.isArray(item.items)) {
                return {
                    ...item,
                    items: filterHiddenItems(item.items)
                };
            }
            return item;
        });
}

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

const docsRootAbsPath = path.resolve(__dirname, '../../docs')
const generatedPath = path.resolve(__dirname, './generated')

type SidebarConfig = Record<string, DefaultTheme.SidebarItem[]>

let generationPromises: Map<string, Promise<any> | null> = new Map()
let lastLoadedSidebars: Map<string, SidebarConfig> = new Map()
let lastGenerationFailed: Map<string, boolean> = new Map()

/**
 * Get the path for a language-specific sidebar file
 */
function getSidebarFilePath(lang: string): string {
    return path.resolve(generatedPath, `sidebar_${lang}.json`)
}

/**
 * Check if a language-specific sidebar file exists and is valid
 */
function isValidSidebarFile(lang: string): boolean {
    const filePath = getSidebarFilePath(lang)
    if (!fs.existsSync(filePath)) {
        return false
    }
    
    try {
        const content = fs.readFileSync(filePath, 'utf-8')
        JSON.parse(content)
        return true
    } catch {
        return false
    }
}

/**
 * Load sidebar from language-specific file
 */
function loadSidebarFromFile(lang: string): SidebarConfig {
    const filePath = getSidebarFilePath(lang)
    
    try {
        if (fs.existsSync(filePath)) {
            const content = fs.readFileSync(filePath, 'utf-8')
            const sidebar = JSON.parse(content)
            if (typeof sidebar === 'object' && sidebar !== null) {
                const filteredSidebar: SidebarConfig = {}
                for (const [path, items] of Object.entries(sidebar)) {
                    if (Array.isArray(items)) {
                        const filteredItems = filterHiddenItems(items)
                        if (filteredItems.length > 0) {
                            filteredSidebar[path] = filteredItems
                        }
                    }
                }
                return filteredSidebar
            }
        }
    } catch (error) {
        // Silent fail for missing sidebar files
    }
    
    return {}
}

/**
 * Generate sidebar for a specific language and save to language-specific file
 */
async function generateSidebarForLanguage(lang: string): Promise<SidebarConfig> {
    const isDev = process.env.NODE_ENV === 'development'
    
    try {
        const generatedResult = await generateSidebars({
            docsPath: docsRootAbsPath,
            isDevMode: isDev,
            lang: lang
        })
        
        if (generatedResult) {
            const langPrefix = `/${lang}/`
            const langSidebar: SidebarConfig = {}
            
            for (const [path, items] of Object.entries(generatedResult)) {
                if (path.startsWith(langPrefix)) {
                    const filteredItems = filterHiddenItems(items as DefaultTheme.SidebarItem[])
                    if (filteredItems.length > 0) {
                        langSidebar[path] = filteredItems
                    }
                }
            }
            
            if (!fs.existsSync(generatedPath)) {
                fs.mkdirSync(generatedPath, { recursive: true })
            }
            
            const sidebarFile = getSidebarFilePath(lang)
            fs.writeFileSync(sidebarFile, JSON.stringify(langSidebar, null, 2))
            
            return langSidebar
        } else {
            return {}
        }
    } catch (error) {
        return {}
    }
}

/**
 * Ensures the sidebar configuration is generated (if needed) and then loads it.
 */
export async function ensureAndLoadGeneratedSidebars(lang: string = 'en'): Promise<SidebarConfig> {
    const isDev = process.env.NODE_ENV === 'development'
    const hasFailed = lastGenerationFailed.get(lang) || false
    const existingPromise = generationPromises.get(lang)
    
    if (!isDev && !hasFailed && isValidSidebarFile(lang)) {
        const cachedSidebar = lastLoadedSidebars.get(lang)
        if (cachedSidebar) {
            return cachedSidebar
        }
        
        const sidebar = loadSidebarFromFile(lang)
        lastLoadedSidebars.set(lang, sidebar)
        return sidebar
    }

    if (isDev || !lastLoadedSidebars.has(lang) || hasFailed || !isValidSidebarFile(lang)) {
        if (!existingPromise) {
            lastGenerationFailed.set(lang, false)
            const promise = generateSidebarForLanguage(lang)
            generationPromises.set(lang, promise)

            try {
                const sidebar = await promise
                lastLoadedSidebars.set(lang, sidebar)
                return sidebar
            } catch (error) {
                lastGenerationFailed.set(lang, true)
                return {}
            } finally {
                generationPromises.set(lang, null)
            }
        } else {
            try {
                const sidebar = await existingPromise
                return sidebar || {}
            } catch (error) {
                return {}
            }
        }
    }

    const cachedSidebar = lastLoadedSidebars.get(lang)
    if (cachedSidebar) {
        return cachedSidebar
    }

    const sidebar = loadSidebarFromFile(lang)
    lastLoadedSidebars.set(lang, sidebar)
    return sidebar
}

/**
 * Get sidebar configuration for a specific language (synchronous).
 */
export function sidebarForLanguage(lang: string): SidebarConfig {
    const cachedSidebar = lastLoadedSidebars.get(lang)
    if (cachedSidebar) {
        return cachedSidebar
    }
    
    const sidebar = loadSidebarFromFile(lang)
    if (Object.keys(sidebar).length > 0) {
        lastLoadedSidebars.set(lang, sidebar)
        return sidebar
    }
    
    try {
        const legacyPath = path.resolve(generatedPath, 'sidebars.json')
        if (fs.existsSync(legacyPath)) {
            const content = fs.readFileSync(legacyPath, 'utf-8')
            const allSidebars = JSON.parse(content)
            const langPrefix = `/${lang}/`
            const langSidebar: SidebarConfig = {}
            
            for (const [pathKey, items] of Object.entries(allSidebars)) {
                if (pathKey.startsWith(langPrefix)) {
                    langSidebar[pathKey] = items as DefaultTheme.SidebarItem[]
                }
            }
            
            if (Object.keys(langSidebar).length > 0) {
                lastLoadedSidebars.set(lang, langSidebar)
                return langSidebar
            }
        }
    } catch (error) {
        // Silent fail for legacy fallback
    }
    
    return {}
}

/**
 * Preload sidebars for all supported languages
 */
export async function preloadAllSidebars(): Promise<void> {
    const languages = ['en', 'zh']
    const promises = languages.map(lang => ensureAndLoadGeneratedSidebars(lang))
    await Promise.all(promises)
} 