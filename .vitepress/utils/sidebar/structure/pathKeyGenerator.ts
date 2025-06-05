// import { SidebarItem } from '../types'; // SidebarItem might not be needed directly as input

/**
 * Generates a unique relative path key for a sidebar item.
 * For files: 'filename.md'
 * For directories: 'dirname/' (always with trailing slash)
 * For groups (that are direct children of a root and ordered by title): 'Group Title String'
 * For items within groups or nested directories, it should be relative to the group's path or parent dir.
 * This key is used for matching against itemOrder Record<string, number> and for JSON sync keys.
 *
 * @param itemName The raw name of the file (e.g., "my-doc.md"), directory (e.g., "concepts"), or group title.
 * @param itemType Indicates if it's a 'file', 'directory', or 'group'.
 * @param parentPathKey Optional. The path key of the parent directory or group scope.
 *                      For items directly under a sidebar root, this would be undefined or an empty string.
 *                      For items inside a group that sources from a `path` (e.g. `group.path = ./concepts-docs/`),
 *                      this parentPathKey should incorporate `concepts-docs/`.
 * @returns A string key, e.g., "file.md", "dir/", "dir/subfile.md", or for a top-level group "My Group Title".
 *          Nested items get keys like "group-path/file.md".
 */
export function generatePathKey(
    itemName: string,
    itemType: 'file' | 'directory' | 'group', // 'group' is for the group title itself as a key
    parentPathKey?: string
): string {
    let key = itemName;

    if (itemType === 'directory' && !key.endsWith('/')) {
        key = key + '/';
    } else if (itemType === 'file' && key.endsWith('/')) {
        key = key.slice(0, -1); // Files shouldn't end with /
    }
    // Group titles are used as-is when they are direct children to be ordered/localized by their title.
    // If a group has a `path` property, items *within* that group will have their keys prefixed by that path,
    // which would be handled by the caller by setting `parentPathKey` appropriately before calling this for child items.

    let fullKey: string;
    if (parentPathKey && parentPathKey.trim() !== '' && parentPathKey.trim() !== './' && parentPathKey.trim() !== '/') {
        const prefix = parentPathKey.endsWith('/') ? parentPathKey : parentPathKey + '/';
        fullKey = prefix + key;
    } else {
        fullKey = key;
    }
    
    // Normalize to remove leading './' if present from relative constructions
    // and ensure no double slashes except for protocol (http://)
    let normalizedKey = fullKey.startsWith('./') ? fullKey.substring(2) : fullKey;
    normalizedKey = normalizedKey.replace(/\/+/g, '/'); // Replace multiple slashes with single, except for protocol
    
    return normalizedKey;
} 


