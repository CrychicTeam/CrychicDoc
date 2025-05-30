/**
 * VitePress utilities for CryChicDoc
 * Sidebar generation and VitePress-specific functionality
 */

import * as metadata from './metadata';
import * as navigation from './navigation';

// Re-export all utilities
export * from './metadata';
export * from './navigation';

// Conditional sidebar generator (server-side only)
const getSidebarGenerator = () => {
  if (typeof window === 'undefined') {
    try {
      return require('../sidebarGenerator').default;
    } catch {
      return null;
    }
  }
  return null;
};

/**
 * Sidebar generation utilities
 */
export const sidebar = {
  /** Create sidebar generator instance (server-side only) */
  generator: (pathname: string) => {
    const SidebarGenerator = getSidebarGenerator();
    if (!SidebarGenerator) {
      console.warn('SidebarGenerator is only available server-side');
      return null;
    }
    return new SidebarGenerator(pathname);
  },
  
  /** Generate sidebar and return the result (server-side only) */
  generate: (pathname: string) => {
    const generator = sidebar.generator(pathname);
    return generator ? generator.sidebar : null;
  },
  
  /** Get corrected pathname from generator (server-side only) */
  getCorrectedPathname: (pathname: string) => {
    const generator = sidebar.generator(pathname);
    return generator ? generator.correctedPathname : pathname;
  },
};

/**
 * Navigation utilities
 */
export const nav = {
  sidebar,
  
  // Direct access
  get SidebarGenerator() {
    return getSidebarGenerator();
  },
  createSidebarGenerator: (pathname: string) => sidebar.generator(pathname),
};

/**
 * Main VitePress utilities export
 */
export const vitepressUtils = {
  sidebar,
  nav,
  
  // Direct access
  get SidebarGenerator() {
    return getSidebarGenerator();
  },
  generateSidebar: (pathname: string) => sidebar.generate(pathname),
  
  // Metadata utilities
  ...metadata,
  
  // Navigation utilities  
  ...navigation,
};

export default vitepressUtils; 