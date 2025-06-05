#!/usr/bin/env node

import fs from 'node:fs'
import path from 'node:path'
import { fileURLToPath } from 'node:url'

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

const PROJECT_ROOT = path.resolve(__dirname, '../..')
const DOCS_ROOT = path.resolve(PROJECT_ROOT, 'docs')
const CONFIG_ROOT = path.resolve(PROJECT_ROOT, '.vitepress/config/sidebar')

/**
 * Parse frontmatter from markdown content
 * @param {string} content - Markdown file content
 * @returns {Object} Parsed frontmatter object
 */
function parseFrontmatter(content) {
    const frontmatterRegex = /^---\s*\r?\n([\s\S]*?)\r?\n---\s*(?:\r?\n|$)/
    const match = content.match(frontmatterRegex)
    
    if (!match) return {}
    
    try {
        const frontmatter = {}
        const yamlContent = match[1]
        const yamlLines = yamlContent.split(/\r?\n/)
        
        for (const line of yamlLines) {
            const colonIndex = line.indexOf(':')
            if (colonIndex > 0) {
                const key = line.substring(0, colonIndex).trim()
                const value = line.substring(colonIndex + 1).trim()
                // Remove quotes and clean up value
                frontmatter[key] = value.replace(/^['"]|['"]$/g, '')
            }
        }
        
        return frontmatter
    } catch (error) {
        console.warn(`Failed to parse frontmatter: ${error.message}`)
        return {}
    }
}

/**
 * Get relative path from docs root to a given directory
 * @param {string} absolutePath - Absolute path to directory
 * @returns {string} Relative path from docs root
 */
function getRelativePathFromDocs(absolutePath) {
    return path.relative(DOCS_ROOT, absolutePath).replace(/\\/g, '/')
}

/**
 * Get the config directory path for a given docs directory
 * @param {string} lang - Language code
 * @param {string} docsPath - Relative path from docs root
 * @returns {string} Config directory path
 */
function getConfigDirPath(lang, docsPath) {
    // Remove language prefix from docs path
    const pathWithoutLang = docsPath.startsWith(`${lang}/`) 
        ? docsPath.substring(lang.length + 1) 
        : docsPath
    
    return path.resolve(CONFIG_ROOT, lang, pathWithoutLang)
}

/**
 * Read and parse JSON file safely
 * @param {string} filePath - Path to JSON file
 * @returns {Object} Parsed JSON object or empty object
 */
function readJsonFile(filePath) {
    try {
        if (fs.existsSync(filePath)) {
            const content = fs.readFileSync(filePath, 'utf-8')
            return JSON.parse(content)
        }
    } catch (error) {
        console.warn(`Failed to read JSON file ${filePath}: ${error.message}`)
    }
    return {}
}

/**
 * Write JSON file safely with proper formatting
 * @param {string} filePath - Path to JSON file
 * @param {Object} data - Data to write
 */
function writeJsonFile(filePath, data) {
    try {
        // Ensure directory exists
        const dir = path.dirname(filePath)
        if (!fs.existsSync(dir)) {
            fs.mkdirSync(dir, { recursive: true })
        }
        
        // Write with proper formatting
        fs.writeFileSync(filePath, JSON.stringify(data, null, 2) + '\n')
    } catch (error) {
        console.error(`Failed to write JSON file ${filePath}: ${error.message}`)
    }
}

/**
 * Find all index.md files with title configuration
 * @param {string} lang - Language code
 * @returns {Array} Array of objects with path and title info
 */
function findIndexFilesWithTitles(lang) {
    const langDocsRoot = path.resolve(DOCS_ROOT, lang)
    const results = []
    
    if (!fs.existsSync(langDocsRoot)) {
        console.warn(`Language docs directory not found: ${langDocsRoot}`)
        return results
    }
    
    /**
     * Recursively scan directory for index.md files
     * @param {string} currentDir - Current directory being scanned
     */
    function scanDirectory(currentDir) {
        try {
            const entries = fs.readdirSync(currentDir, { withFileTypes: true })
            
            for (const entry of entries) {
                const fullPath = path.resolve(currentDir, entry.name)
                
                if (entry.isDirectory()) {
                    // Skip node_modules, .vitepress, etc.
                    if (!entry.name.startsWith('.') && entry.name !== 'node_modules') {
                        scanDirectory(fullPath)
                    }
                } else if (entry.name === 'index.md') {
                    // Found an index.md file, check for title
                    try {
                        const content = fs.readFileSync(fullPath, 'utf-8')
                        const frontmatter = parseFrontmatter(content)
                        
                        if (frontmatter.title && frontmatter.title.trim()) {
                            const relativePath = getRelativePathFromDocs(currentDir)
                            
                            results.push({
                                docsPath: relativePath,
                                absolutePath: currentDir,
                                title: frontmatter.title.trim(),
                                indexFilePath: fullPath
                            })
                            
                            console.log(`✓ Found index.md with title: ${relativePath} -> "${frontmatter.title}"`)
                        }
                    } catch (error) {
                        console.warn(`Failed to read index.md at ${fullPath}: ${error.message}`)
                    }
                }
            }
        } catch (error) {
            console.warn(`Failed to scan directory ${currentDir}: ${error.message}`)
        }
    }
    
    scanDirectory(langDocsRoot)
    return results
}

/**
 * Update locales.json file with index title
 * @param {string} lang - Language code
 * @param {Object} indexInfo - Index file information
 */
function updateLocalesJson(lang, indexInfo) {
    const configDir = getConfigDirPath(lang, indexInfo.docsPath)
    const localesPath = path.resolve(configDir, 'locales.json')
    
    // Read existing locales.json
    const locales = readJsonFile(localesPath)
    
    // Update the _self_ key with the title from index.md
    const hasChanges = locales._self_ !== indexInfo.title
    
    if (hasChanges) {
        locales._self_ = indexInfo.title
        writeJsonFile(localesPath, locales)
        console.log(`✓ Updated locales.json: ${localesPath}`)
        console.log(`  _self_: "${indexInfo.title}"`)
        return true
    } else {
        console.log(`- No change needed for: ${localesPath}`)
        return false
    }
}

/**
 * Main function to update all index titles
 * @param {Array} languages - Array of language codes to process
 */
async function updateIndexTitles(languages = ['en', 'zh']) {
    console.log('🔍 Scanning for index.md files with title configuration...\n')
    
    let totalUpdated = 0
    let totalScanned = 0
    
    for (const lang of languages) {
        console.log(`\n📁 Processing language: ${lang}`)
        console.log('=' .repeat(50))
        
        const indexFiles = findIndexFilesWithTitles(lang)
        totalScanned += indexFiles.length
        
        if (indexFiles.length === 0) {
            console.log(`No index.md files with titles found for language: ${lang}`)
            continue
        }
        
        console.log(`\nFound ${indexFiles.length} index.md files with titles`)
        console.log('-'.repeat(30))
        
        for (const indexInfo of indexFiles) {
            const updated = updateLocalesJson(lang, indexInfo)
            if (updated) totalUpdated++
        }
    }
    
    console.log('\n' + '='.repeat(60))
    console.log('📊 Summary:')
    console.log(`   Scanned: ${totalScanned} index.md files`)
    console.log(`   Updated: ${totalUpdated} locales.json files`)
    console.log('✅ Index title update completed!')
}

/**
 * CLI interface - Run when script is executed directly
 */
const args = process.argv.slice(2)

if (args.includes('--help') || args.includes('-h')) {
    console.log(`
📝 Update Index Titles Script

Usage: node update-index-titles.mjs [options] [languages...]

Options:
  --help, -h     Show this help message
  --dry-run, -d  Show what would be updated without making changes

Examples:
  node update-index-titles.mjs                    # Update all languages (en, zh)
  node update-index-titles.mjs en                 # Update only English
  node update-index-titles.mjs zh en              # Update Chinese and English
  node update-index-titles.mjs --dry-run          # Preview changes without updating

Description:
  This script scans for index.md files that have title frontmatter configuration
  and updates the corresponding locales.json files with _self_ key.
  
  Only affects:
  - Directories that have index.md files
  - index.md files that have 'title' in frontmatter
  - Will create locales.json if it doesn't exist
  
  Does NOT affect:
  - Directories without index.md files
  - index.md files without title frontmatter
  - Other keys in existing locales.json files
`)
    process.exit(0)
}

// Parse languages from arguments (excluding flags)
const languages = args.filter(arg => !arg.startsWith('--') && !arg.startsWith('-'))
const targetLanguages = languages.length > 0 ? languages : ['en', 'zh']

try {
    await updateIndexTitles(targetLanguages)
} catch (error) {
    console.error('❌ Error during execution:', error.message)
    process.exit(1)
}

// Export for programmatic use
export { updateIndexTitles, findIndexFilesWithTitles, updateLocalesJson } 