import path from 'node:path';
import { SidebarItem } from '../types';
import { normalizePathSeparators, sanitizeTitleForPath } from '../shared/objectUtils';

/**
 * @file PathKeyProcessor.ts
 * @description Handles path key processing and relative key extraction for JSON config synchronization.
 */
export class PathKeyProcessor {
    
    /**
     * Extracts the relative key for the current directory from a full _relativePathKey.
     * For example, if currentConfigDirSignature is "modpack/kubejs/1.20.1/Introduction/Addon" 
     * and item._relativePathKey is "Introduction/Addon/ProbeJS/", this returns "ProbeJS/".
     * 
     * @param item The sidebar item to extract the key from
     * @param currentConfigDirSignature The current directory signature (e.g., "concepts" or "modpack/kubejs/1.20.1/Introduction/Addon")
     * @returns The relative key for the current directory context
     */
    public extractRelativeKeyForCurrentDir(item: SidebarItem, currentConfigDirSignature: string): string {
        const fullKey = item._relativePathKey || sanitizeTitleForPath(item.text || 'untitled');
        
        console.log(`DEBUG: PathKeyProcessor - Input: fullKey="${fullKey}", currentConfigDirSignature="${currentConfigDirSignature}", isDirectory=${item._isDirectory}, text="${item.text}"`);
        
        // If we're at root level, extract the immediate child component
        if (currentConfigDirSignature === '_root') {
            const parts = fullKey.split('/').filter(p => p.length > 0);
            let result: string;
            
            if (item._isDirectory) {
                // Directories should have trailing slash: "Introduction/"
                result = parts.length > 0 ? parts[0] + '/' : fullKey;
            } else {
                // Files should NOT have trailing slash: "Description.md"
                result = parts.length > 0 ? parts[0] : fullKey;
                // Ensure file extensions are preserved
                if (!result.includes('.') && item.text && item.text.includes('.')) {
                    result = item.text;
                }
            }
            
            console.log(`DEBUG: PathKeyProcessor - Root level result: "${result}"`);
            return result;
        }
        
        // For nested directories, we need to find the content path portion of the config signature
        // The config signature format is like "modpack/kubejs/1.20.1/Introduction/Addon"
        // The content path portion is the part that appears in the fullKey
        
        // Split the config signature to find the content path
        const configParts = currentConfigDirSignature.split('/');
        
        // Find where the content path starts by looking for version patterns or matching with fullKey
        let contentPathStartIndex = -1;
        
        // Strategy 1: Look for version patterns (e.g., "1.20.1", "1.18.2")
        for (let i = 0; i < configParts.length; i++) {
            if (/^\d+\.\d+(\.\d+)?$/.test(configParts[i])) {
                contentPathStartIndex = i + 1;
                break;
            }
        }
        
        // Strategy 2: If no version found, find the longest suffix that matches the start of fullKey
        if (contentPathStartIndex === -1) {
            for (let i = 1; i < configParts.length; i++) {
                const testContentPath = configParts.slice(i).join('/');
                if (fullKey.startsWith(testContentPath + '/') || fullKey === testContentPath + '/') {
                    contentPathStartIndex = i;
                    break;
                }
            }
        }
        
        if (contentPathStartIndex >= 0 && contentPathStartIndex < configParts.length) {
            // Extract the content path from the config signature
            const contentPath = configParts.slice(contentPathStartIndex).join('/');
            console.log(`DEBUG: PathKeyProcessor - Extracted content path: "${contentPath}"`);
            
            // Remove the content path prefix from the fullKey to get the relative part
            if (fullKey.startsWith(contentPath + '/')) {
                const relativePart = fullKey.substring(contentPath.length + 1);
                console.log(`DEBUG: PathKeyProcessor - Extracted relative part: "${relativePart}"`);
                
                // Return only the immediate child portion
                const parts = relativePart.split('/').filter(p => p.length > 0);
                let result: string;
                
                if (item._isDirectory) {
                    // Directories should have trailing slash
                    result = parts.length > 0 ? parts[0] + '/' : relativePart;
                } else {
                    // Files should NOT have trailing slash and preserve extensions
                    result = parts.length > 0 ? parts[0] : relativePart;
                    // Remove trailing slash if present for files
                    if (result.endsWith('/')) {
                        result = result.slice(0, -1);
                    }
                }
                
                console.log(`DEBUG: PathKeyProcessor - Nested result: "${result}"`);
                return result;
            } else if (fullKey === contentPath + '/') {
                // This item represents the directory itself
                return '';
            }
        }
        
        // FALLBACK: Extract immediate child from fullKey
        const parts = fullKey.split('/').filter(p => p.length > 0);
        let result: string;
        
        if (item._isDirectory) {
            // Directories should have trailing slash
            result = parts.length > 0 ? parts[parts.length - 1] + '/' : fullKey;
        } else {
            // Files should NOT have trailing slash
            result = parts.length > 0 ? parts[parts.length - 1] : fullKey;
            if (result.endsWith('/')) {
                result = result.slice(0, -1);
            }
        }
        
        console.log(`DEBUG: PathKeyProcessor - Fallback result: "${result}"`);
        return result;
    }

    /**
     * Derives the directory path signature for the JSON config files of a given root view.
     * @param rootPathKey The global path key for the sidebar root (e.g., '/en/guide/').
     * @param lang The language code (e.g., 'en').
     * @returns Path signature relative to the language folder (e.g., 'guide', '_root').
     */
    public getSignatureForRootView(rootPathKey: string, lang: string): string {
        let normalizedKey = normalizePathSeparators(rootPathKey);
        const langPrefix = lang ? `/${lang}/` : '/'; // Handle empty lang for single-lang sites
        let pathRelativeToLangRoot: string;

        if (lang && normalizedKey.startsWith(langPrefix)) {
            pathRelativeToLangRoot = normalizedKey.substring(langPrefix.length);
        } else if (lang && normalizedKey === `/${lang}`) {
            pathRelativeToLangRoot = '';
        } else if (!lang && normalizedKey.startsWith('/')) { // Single lang site, key is like /guide/
            pathRelativeToLangRoot = normalizedKey.substring(1);
        } else if (lang && !normalizedKey.startsWith(langPrefix)){
            // This case means rootPathKey might be like '/guide/' but lang is 'en'.
            // This shouldn't happen if main.ts always provides full /en/guide/ style keys.
            // As a fallback, if lang is present, assume key is already relative to lang dir or it's a malformed global key.
            // For safety, if it starts with '/', remove it. This is best effort.
            pathRelativeToLangRoot = normalizedKey.startsWith('/') ? normalizedKey.substring(1) : normalizedKey;

        } else { // lang is empty, and key is empty or not starting with /
            pathRelativeToLangRoot = normalizedKey; // Treat as already relative or just empty
        }

        if (pathRelativeToLangRoot.endsWith('/')) {
            pathRelativeToLangRoot = pathRelativeToLangRoot.slice(0, -1);
        }
        return pathRelativeToLangRoot === '' ? '_root' : pathRelativeToLangRoot;
    }

    /**
     * Determines if a given sidebar item (by its content path relative to lang) is a GitBook root.
     * @param itemContentPathRelativeToLang Path of the item, relative to the language folder (e.g., 'my-gitbook-folder').
     * @param lang Current language.
     * @param langGitbookPaths Absolute paths to GitBook directories for the current language.
     * @param absDocsPath Absolute path to the docs directory.
     */
    public isGitBookRoot(itemContentPathRelativeToLang: string, lang: string, langGitbookPaths: string[], absDocsPath: string): boolean {
        if (itemContentPathRelativeToLang === '_root') return false; // _root itself cannot be a gitbook path signature
        const itemAbsPath = normalizePathSeparators(path.join(absDocsPath, lang, itemContentPathRelativeToLang));
        return langGitbookPaths.includes(itemAbsPath);
    }
} 


