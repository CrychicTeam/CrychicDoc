/**
 * TypeScript types and interfaces for CryChicDoc utilities
 */

// Re-export existing types from type.ts
export * from '../type';

// Enhanced utility types
export interface UtilsConfig {
  /** Content utilities configuration */
  content?: {
    defaultWordsPerMinute?: number;
    enableReadingTime?: boolean;
  };
  /** VitePress utilities configuration */
  vitepress?: {
    enableSidebarGeneration?: boolean;
    defaultCollapsed?: boolean;
  };
  /** Charts utilities configuration */
  charts?: {
    defaultTheme?: 'light' | 'dark';
    enableInteractivity?: boolean;
  };
}

// Function utility types
export interface WordCountResult {
  characters: number;
  words: number;
  lines: number;
  readingTime: number; // in minutes
}

// Enhanced sidebar types
export interface EnhancedFileItem {
  text: string;
  link: string;
  collapsed?: boolean;
  items?: EnhancedFileItem[];
  badge?: string | { text: string; type: 'info' | 'tip' | 'warning' | 'danger' };
  icon?: string;
}

export interface EnhancedSidebar {
  text: string;
  collapsed: boolean;
  items: EnhancedFileItem[];
  icon?: string;
  badge?: string;
}

// Plugin-related types
export interface UtilsPluginConfig {
  name: string;
  version?: string;
  enabled?: boolean;
  options?: Record<string, any>;
}

// Component-related types
export interface MetadataConfig {
  update: (text: string) => string;
  wordCount: (text: number) => string;
  readTime: (text: number) => string;
  pageViews: (text: number) => string;
}

export interface TranslationDictionary {
  [key: string]: {
    [lang: string]: string;
  };
}

export interface ScrollPosition {
  x: number;
  y: number;
}

export interface GitHubCommit {
  commit: {
    author: {
      date: string;
    };
  };
} 