import path from 'node:path';
import { normalizePathSeparators } from '../shared/objectUtils';

/**
 * Determines the hierarchy of potential index.md file paths from a target directory
 * up to the language root.
 * @param targetDirPath Absolute path to the target directory for which to resolve config.
 * @param langRootAbsPath Absolute path to the language root (e.g., /path/to/project/docs/en).
 * @param docsAbsPath Absolute path to the docs root (e.g., /path/to/project/docs).
 * @returns An array of absolute paths to potential index.md files, ordered from
 *          most general (language root) to most specific (target directory).
 */
export function getPathHierarchy(
    targetDirPath: string,
    langRootAbsPath: string,
    docsAbsPath: string // Not strictly needed with absolute paths, but good for context/validation
): string[] {
    const normalizedTargetDirPath = normalizePathSeparators(targetDirPath);
    const normalizedLangRootAbsPath = normalizePathSeparators(langRootAbsPath);
    // const normalizedDocsAbsPath = normalizePathSeparators(docsAbsPath); // For validation if needed

    const hierarchyIndexMdPaths: string[] = [];
    let currentPath = normalizedTargetDirPath;

    // Walk up from the target directory to the language root
    while (true) {
        hierarchyIndexMdPaths.push(path.join(currentPath, 'index.md'));
        
        if (currentPath === normalizedLangRootAbsPath) {
            break; // Reached the language root
        }

        const parentPath = normalizePathSeparators(path.dirname(currentPath));

        // Stop if we are about to go above the language root or if path isn't changing
        if (parentPath === currentPath || !parentPath.startsWith(normalizedLangRootAbsPath) || parentPath.length < normalizedLangRootAbsPath.length) {
            // The last condition (parentPath.length < normalizedLangRootAbsPath.length) is a stricter check
            // to ensure we don't accidentally go to `docs/` if `langRootAbsPath` is `docs/en`.
            // However, `!parentPath.startsWith(normalizedLangRootAbsPath)` should mostly cover it.
            break;
        }
        currentPath = parentPath;
    }
    return hierarchyIndexMdPaths.reverse(); // Order from general (lang root) to specific (target)
} 

