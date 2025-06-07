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

// Removed intelligent regeneration functions - now using compulsory generation mode

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
 * Generate sidebars for all languages with compulsory regeneration
 */
async function generateSidebarsCompulsory() {
    const languages = ['en', 'zh'];
    
    console.log('🔥 COMPULSORY GENERATION MODE - Regenerating all sidebars...');
    
    for (const lang of languages) {
        console.log(`🔄 Force regenerating sidebar for: ${lang}`);
        await generateSidebarForLanguage(lang);
    }
    
    console.log('✅ Compulsory regeneration completed for all languages.');
}

async function generate() {
    console.log('🚀 Starting compulsory sidebar generation...');
    
    try {
        await generateSidebarsCompulsory();
        console.log('✓ Sidebar generation completed successfully.');
  } catch (error) {
        console.error('✗ Error during sidebar generation:', error);
        process.exit(1); // Exit with non-zero code to stop subsequent build steps
  }
}

generate(); 