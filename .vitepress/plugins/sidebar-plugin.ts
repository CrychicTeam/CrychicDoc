import { Plugin } from 'vite'
import { generateSidebars } from '../utils/sidebar'
import fs from 'node:fs'
import path from 'node:path'

let isGenerating = false
let lastGenerationTime = 0

/**
 * Recursively filter out hidden items from sidebar structure
 */
function filterHiddenItems(items: any[]): any[] {
    if (!Array.isArray(items)) return items;
    
    return items
        .filter(item => !item._hidden)
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

/**
 * VitePress plugin for intelligent sidebar generation with performance optimization
 */
export function sidebarPlugin(): Plugin {
    const docsPath = path.resolve('./docs')
    const configPath = path.resolve('./.vitepress/config/sidebar')
    const generatedPath = path.resolve('./.vitepress/config/generated')
    
    /**
     * Check if sidebar needs regeneration based on file changes
     */
    function needsRegeneration(lang: string): boolean {
        const sidebarFile = path.join(generatedPath, `sidebar_${lang}.json`)
        
        if (!fs.existsSync(sidebarFile)) {
            return true
        }
        
        const sidebarStat = fs.statSync(sidebarFile)
        const sidebarTime = sidebarStat.mtime.getTime()
        
        const indexFiles = findIndexFiles(path.join(docsPath, lang))
        for (const indexFile of indexFiles) {
            if (fs.existsSync(indexFile)) {
                const indexStat = fs.statSync(indexFile)
                if (indexStat.mtime.getTime() > sidebarTime) {
                    return true
                }
            }
        }
        
        const jsonConfigDir = path.join(configPath, lang)
        if (fs.existsSync(jsonConfigDir)) {
            const hasNewerConfigs = checkNewerConfigs(jsonConfigDir, sidebarTime)
            if (hasNewerConfigs) {
                return true
            }
        }
        
        return false
    }
    
    /**
     * Find all index.md files recursively
     */
    function findIndexFiles(dir: string): string[] {
        const results: string[] = []
        
        if (!fs.existsSync(dir)) return results
        
        try {
            const items = fs.readdirSync(dir)
            for (const item of items) {
                const fullPath = path.join(dir, item)
                const stat = fs.statSync(fullPath)
                
                if (stat.isDirectory()) {
                    results.push(...findIndexFiles(fullPath))
                } else if (item === 'index.md') {
                    results.push(fullPath)
                }
            }
        } catch (error) {
            // Skip directories we can't read
        }
        
        return results
    }
    
    /**
     * Check if any config files are newer than the sidebar
     */
    function checkNewerConfigs(configDir: string, sidebarTime: number): boolean {
        try {
            const items = fs.readdirSync(configDir, { withFileTypes: true })
            
            for (const item of items) {
                const fullPath = path.join(configDir, item.name)
                
                if (item.isDirectory()) {
                    if (checkNewerConfigs(fullPath, sidebarTime)) {
                        return true
                    }
                } else if (item.name.endsWith('.json')) {
                    const stat = fs.statSync(fullPath)
                    if (stat.mtime.getTime() > sidebarTime) {
                        return true
                    }
                }
            }
        } catch (error) {
            // Skip directories we can't read
        }
        
        return false
    }
    
    /**
     * Find all JSON config files recursively for watching
     */
    function findAllJsonConfigFiles(): string[] {
        const results: string[] = []
        
        if (!fs.existsSync(configPath)) return results
        
        function scanDirectory(dir: string) {
            try {
                const items = fs.readdirSync(dir, { withFileTypes: true })
                
                for (const item of items) {
                    const fullPath = path.join(dir, item.name)
                    
                    if (item.isDirectory()) {
                        scanDirectory(fullPath)
                    } else if (item.name.endsWith('.json')) {
                        results.push(fullPath)
                    }
                }
            } catch (error) {
                // Skip directories we can't read
            }
        }
        
        scanDirectory(configPath)
        return results
    }
    
    /**
     * Generate sidebars for specific languages
     */
    async function generateSidebarsForLanguages(languages: string[] = ['en', 'zh']) {
        if (isGenerating) {
            return
        }
                    
                    isGenerating = true
        const currentTime = Date.now()
        
                    try {
            if (!fs.existsSync(generatedPath)) {
                fs.mkdirSync(generatedPath, { recursive: true })
            }
            
            for (const lang of languages) {
                if (needsRegeneration(lang)) {
                    try {
                        const result = await generateSidebars({
                            docsPath: docsPath,
                            isDevMode: process.env.NODE_ENV === 'development',
                            lang: lang
                        })
                        
                        if (result) {
                            const langPrefix = `/${lang}/`
                            const langSidebar: Record<string, any[]> = {}
                            
                            for (const [path, items] of Object.entries(result)) {
                                if (path.startsWith(langPrefix)) {
                                    const filteredItems = filterHiddenItems(items as any[]);
                                    if (filteredItems.length > 0) {
                                        langSidebar[path] = filteredItems;
                                    }
                                }
                            }
                            
                            const sidebarFile = path.join(generatedPath, `sidebar_${lang}.json`)
                            fs.writeFileSync(sidebarFile, JSON.stringify(langSidebar, null, 2))
                        }
                    } catch (error) {
                        console.error(`[SidebarPlugin] Failed to generate sidebar for ${lang}:`, error)
                    }
                }
            }
            
            lastGenerationTime = currentTime
            
                    } finally {
                        isGenerating = false
                    }
                }
    
    /**
     * Check if a file path should trigger regeneration
     */
    function shouldTriggerRegeneration(filePath: string): boolean {
        return filePath.endsWith('index.md') || 
               (filePath.includes('.vitepress/config/sidebar') && filePath.endsWith('.json'))
    }
    
    /**
     * Trigger a proper VitePress reload
     */
    function triggerVitePressReload(server: any) {
        // Force a config reload by touching the config file
        const configFile = path.resolve('./.vitepress/config.mts')
        if (fs.existsSync(configFile)) {
            const now = new Date()
            fs.utimesSync(configFile, now, now)
        }
        
        // Send multiple reload signals to ensure VitePress picks up the change
        setTimeout(() => {
            server.ws.send({
                type: 'full-reload'
            })
        }, 100)
        
        setTimeout(() => {
            server.ws.send({
                type: 'update',
                updates: []
            })
        }, 200)
    }
    
    return {
        name: 'vitepress-intelligent-sidebar-generator',
        
        async buildStart() {
            await generateSidebarsForLanguages(['en', 'zh'])
        },
        
        configureServer(server) {
            // Watch for index.md changes
            server.watcher.on('change', async (filePath) => {
                if (shouldTriggerRegeneration(filePath)) {
                    setTimeout(async () => {
                        if (Date.now() - lastGenerationTime > 1000) {
                            await generateSidebarsForLanguages(['en', 'zh'])
                            triggerVitePressReload(server)
                        }
                    }, 100)
                }
            })
            
            // Watch for new files
            server.watcher.on('add', async (filePath) => {
                if (shouldTriggerRegeneration(filePath)) {
                    await generateSidebarsForLanguages(['en', 'zh'])
                    triggerVitePressReload(server)
                }
            })
            
            // Watch for deleted files
            server.watcher.on('unlink', async (filePath) => {
                if (shouldTriggerRegeneration(filePath)) {
                    await generateSidebarsForLanguages(['en', 'zh'])
                    triggerVitePressReload(server)
                }
            })
            
            // Watch all JSON config files specifically
            const jsonConfigFiles = findAllJsonConfigFiles()
            for (const jsonFile of jsonConfigFiles) {
                server.watcher.add(jsonFile)
            }
        }
    }
} 