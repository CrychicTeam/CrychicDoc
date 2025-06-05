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
     * For example, if currentConfigDirSignature is "concepts" and item._relativePathKey is "concepts/concept-1.md",
     * this returns "concept-1.md".
     * 
     * @param item The sidebar item to extract the key from
     * @param currentConfigDirSignature The current directory signature (e.g., "concepts" or "test-sidebar/concepts")
     * @returns The relative key for the current directory context
     */
    public extractRelativeKeyForCurrentDir(item: SidebarItem, currentConfigDirSignature: string): string {
        const fullKey = item._relativePathKey || sanitizeTitleForPath(item.text || 'untitled');
        
        // If we're at root level, return the key as-is
        if (currentConfigDirSignature === '_root') {
            return fullKey;
        }
        
        // Extract just the local directory name from the full signature
        // e.g., "test-sidebar/concepts/" �?"concepts"
        const localDirName = path.basename(currentConfigDirSignature.replace(/\/$/, ''));
        
        // Remove the local directory prefix to get the relative key
        const dirPrefix = localDirName.endsWith('/') ? localDirName : localDirName + '/';
        
        if (fullKey.startsWith(dirPrefix)) {
            return fullKey.substring(dirPrefix.length);
        }
        
        // If the key doesn't start with the expected prefix, return as-is (fallback)
        return fullKey;
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

