import { generateSidebars } from '../utils/sidebar/index.js';
import fs from 'node:fs';
import path from 'node:path';

const docsPath = path.resolve('./docs');
const configPath = path.resolve('./.vitepress/config/sidebar');
const generatedPath = path.resolve('./.vitepress/config/generated');

/**
 * Recursively filter out hidden items from sidebar structure
 */
function filterHiddenItems(items) {
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
 * Find all index.md files recursively
 */
function findIndexFiles(dir) {
    const results = [];
    
    if (!fs.existsSync(dir)) return results;
    
    try {
        const items = fs.readdirSync(dir);
        for (const item of items) {
            const fullPath = path.join(dir, item);
            const stat = fs.statSync(fullPath);
            
            if (stat.isDirectory()) {
                results.push(...findIndexFiles(fullPath));
            } else if (item === 'index.md') {
                results.push(fullPath);
            }
        }
    } catch (error) {
        // Skip directories we can't read
    }
    
    return results;
}

/**
 * Check if any config files are newer than the sidebar
 */
function checkNewerConfigs(configDir, sidebarTime) {
    try {
        const items = fs.readdirSync(configDir, { withFileTypes: true });
        
        for (const item of items) {
            const fullPath = path.join(configDir, item.name);
            
            if (item.isDirectory()) {
                if (checkNewerConfigs(fullPath, sidebarTime)) {
                    return true;
                }
            } else if (item.name.endsWith('.json')) {
                const stat = fs.statSync(fullPath);
                if (stat.mtime.getTime() > sidebarTime) {
                    return true;
                }
            }
        }
    } catch (error) {
        // Skip directories we can't read
    }
    
    return false;
}

/**
 * Check if sidebar needs regeneration based on file changes
 */
function needsRegeneration(lang) {
    const sidebarFile = path.join(generatedPath, `sidebar_${lang}.json`);
    
    if (!fs.existsSync(sidebarFile)) {
        return true;
    }
    
    const sidebarStat = fs.statSync(sidebarFile);
    const sidebarTime = sidebarStat.mtime.getTime();
    
    // Check index.md files
    const indexFiles = findIndexFiles(path.join(docsPath, lang));
    for (const indexFile of indexFiles) {
        if (fs.existsSync(indexFile)) {
            const indexStat = fs.statSync(indexFile);
            if (indexStat.mtime.getTime() > sidebarTime) {
                console.log(`Index file ${indexFile} is newer than sidebar, regeneration needed`);
                return true;
            }
        }
    }
    
    // Check JSON config files
    const jsonConfigDir = path.join(configPath, lang);
    if (fs.existsSync(jsonConfigDir)) {
        const hasNewerConfigs = checkNewerConfigs(jsonConfigDir, sidebarTime);
        if (hasNewerConfigs) {
            console.log(`Config files in ${jsonConfigDir} are newer than sidebar, regeneration needed`);
            return true;
        }
    }
    
    return false;
}

/**
 * Generate sidebar for a specific language
 */
async function generateSidebarForLanguage(lang) {
    try {
        console.log(`Generating sidebar for language: ${lang}`);
        
        const result = await generateSidebars({
            docsPath: docsPath,
            isDevMode: false,
            lang: lang
        });
        
        if (result) {
            const langPrefix = `/${lang}/`;
            const langSidebar = {};
            
            for (const [path, items] of Object.entries(result)) {
                if (path.startsWith(langPrefix)) {
                    const filteredItems = filterHiddenItems(items);
                    if (filteredItems.length > 0) {
                        langSidebar[path] = filteredItems;
                    }
                }
            }
            
            if (!fs.existsSync(generatedPath)) {
                fs.mkdirSync(generatedPath, { recursive: true });
            }
            
            const sidebarFile = path.join(generatedPath, `sidebar_${lang}.json`);
            fs.writeFileSync(sidebarFile, JSON.stringify(langSidebar, null, 2));
            console.log(`✓ Sidebar generated for ${lang}: ${sidebarFile}`);
        }
    } catch (error) {
        console.error(`✗ Failed to generate sidebar for ${lang}:`, error);
        throw error;
    }
}

/**
 * Generate sidebars for all languages with intelligent regeneration
 */
async function generateSidebarsIntelligently() {
    const languages = ['en', 'zh'];
    
    console.log('Checking which sidebars need regeneration...');
    
    const languagesToRegenerate = [];
    for (const lang of languages) {
        if (needsRegeneration(lang)) {
            languagesToRegenerate.push(lang);
            console.log(`✓ ${lang}: Needs regeneration`);
        } else {
            console.log(`✓ ${lang}: Up to date, skipping`);
        }
    }
    
    if (languagesToRegenerate.length === 0) {
        console.log('All sidebars are up to date. No regeneration needed.');
        return;
    }
    
    console.log(`Regenerating sidebars for: ${languagesToRegenerate.join(', ')}`);
    
    for (const lang of languagesToRegenerate) {
        await generateSidebarForLanguage(lang);
    }
}

async function generate() {
    console.log('Starting intelligent sidebar generation...');
    
    try {
        await generateSidebarsIntelligently();
        console.log('✓ Sidebar generation completed successfully.');
    } catch (error) {
        console.error('✗ Error during sidebar generation:', error);
        process.exit(1); // Exit with non-zero code to stop subsequent build steps
    }
}

generate(); 