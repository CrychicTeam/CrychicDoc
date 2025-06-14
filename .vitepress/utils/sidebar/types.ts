/**
 * Represents a single item in the sidebar.
 */
export interface SidebarItem {
    text: string
    link?: string
    items?: SidebarItem[]
    collapsed?: boolean
    // Internal properties used during generation, might not be in final output
    _priority?: number
    _filePath?: string // Original file path for files
    _isDirectory?: boolean
    _isRoot?: boolean // If this item represents a root defined by root:true
    _hidden?: boolean // True if this item should be hidden from sidebar
    _relativePathKey?: string // Key used for itemOrder lookups and JSON sync. 
                             // For files/dirs: relative path from current sidebar root (e.g., 'concepts/file.md').
                             // For groups (ordered by title): the group's title.
}

/**
 * Overall sidebar configuration structure used by VitePress for multiple sidebars.
 * Keys are base paths (e.g., '/guide/'), values are the array of sidebar items for that path.
 */
export type SidebarMulti = Record<string, SidebarItem[]>;

/**
 * The type VitePress ultimately accepts for its themeConfig.sidebar option.
 * It can be a single sidebar array, a multi-sidebar object, or false to disable.
 */
export type VitePressSidebar = SidebarItem[] | SidebarMulti | false;

/**
 * Configuration for an external link in the sidebar.
 */
export interface ExternalLinkConfig {
    /** Display text for the external link */
    text: string
    /** URL for the external link (must start with http:// or https://) */
    link: string
    /** Priority for ordering among sidebar items (lower numbers appear first) */
    priority?: number
    /** Whether this link should be hidden */
    hidden?: boolean
}

/**
 * Configuration options for a directory, typically from index.md frontmatter.
 */
export interface DirectoryConfig {
    root?: boolean
    title?: string
    hidden?: boolean // True if this directory should be hidden from sidebar
    priority?: number // For ordering among sibling directories/roots
    maxDepth?: number
    collapsed?: boolean // Default collapsed state for this item if it's a directory
    itemOrder?: string[] | Record<string, number> // Order of items within this directory
    groups?: GroupConfig[]
    /** External links to be added to this directory's sidebar */
    externalLinks?: ExternalLinkConfig[]
    [key: string]: any // Allow other frontmatter fields
}

/**
 * Configuration for a group within an index.md's `groups` array.
 * Groups allow splitting content from a directory into separate sidebar sections.
 * The grouped content will be removed from the original parent and displayed as a separate top-level item.
 */
export interface GroupConfig {
    title: string // Display title for the group item in the sidebar
    path: string // Relative path to the content directory to be grouped
    priority?: number // Priority for ordering the group in the sidebar (higher = earlier)
    maxDepth?: number // Maximum nesting depth for items within this group
}

/**
 * Configuration options from the global .sidebarrc.yml file.
 */
export interface GlobalSidebarConfig {
    defaults?: {
        maxDepth?: number
        collapsed?: boolean // Default for root items
        itemOrder?: Record<string, number> | string[] // Global item order fallbacks
        hidden?: boolean // Default hidden state for items if not specified
    }
    [key: string]: any
}

/**
 * The effective, merged configuration for a directory, after considering global, root, and local settings.
 * This is what the StructuralGeneratorService will primarily work with for a given directory.
 */
export interface EffectiveDirConfig {
    root: boolean; // Resolved: true if it's a root, false otherwise
    title: string; // Resolved title (e.g., from frontmatter or derived from dirname)
    hidden: boolean; // Resolved hidden state
    priority: number; // Resolved priority (e.g., from frontmatter or default)
    maxDepth: number; // Resolved maxDepth (1-based for user config)
    collapsed: boolean; // Resolved collapsed state for this directory's item
    itemOrder: Record<string, number>; // RESOLVED to Record<string, number>, default {}
    groups: GroupConfig[]; // Resolved groups, if any
    externalLinks: ExternalLinkConfig[]; // Resolved external links, if any
    path: string; // Absolute path to the directory this config is for
    lang: string; // Language of this config
    isDevMode: boolean; // Whether running in dev mode (affects draft status)
    _baseRelativePathForChildren?: string; // Internal: base for relative keys of children
    [key: string]: any; // Allow other merged fields
}

/**
 * Configuration for an individual markdown file, from its frontmatter.
 */
export interface FileConfig {
    title?: string
    hidden?: boolean // True if this file should be hidden from sidebar
    priority?: number
    [key: string]: any // Allow other frontmatter fields
}

/**
 * Represents a single entry in a metadata file, corresponding to a key in a JSON override file.
 */
export interface MetadataEntry {
    valueHash: string;      // Hash of the value in the corresponding JSON file (e.g., MD5 of the translated string)
    isUserSet: boolean;     // True if the user created/significantly edited this entry, false if system-generated stub
    isActiveInStructure: boolean; // True if this key currently maps to an item in the live declarative structure
    lastSeen?: number;      // DEPRECATED: No longer actively maintained to prevent heavy git commits
}

/**
 * Represents the content of a metadata file, which is a map of item keys to their metadata entries.
 */
export type JsonFileMetadata = Record<string, MetadataEntry>;

// Re-exporting existing types if they are still relevant and used by the new services.
// Ensure to remove or update any types that become obsolete with the refactor.

// Example of a previously existing type that might be adjusted or kept:
// export interface DiscoveredFile { ... }
// export interface SidebarContext { ... }

// Clean up old types not used by the new services based on the refactor plan.
// The old `SidebarConfig` from the original `SidebarGenerator.ts` might be one such candidate for removal or major rework.

export interface DiscoveredFile {
    name: string
    path: string
    type: 'file' | 'directory'
    // Use Partial<EffectiveDirConfig> as directory config might not be fully resolved yet
    // or might not be applicable if it's a file.
    config?: Partial<EffectiveDirConfig> 
}

export interface SidebarContext {
    basePath: string
    currentPath: string
    // config is the fully resolved config for the current directory being processed
    config: EffectiveDirConfig 
    depth: number
    lang: string
    isDevMode: boolean
    // Add any other contextual information needed during generation
}

// Removed old SidebarConfig type and references 

