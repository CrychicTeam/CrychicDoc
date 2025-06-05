/**
 * VitePress utilities for CryChicDoc
 * Sidebar generation and VitePress-specific functionality
 */

import * as metadata from "./metadata";
import * as navigation from "./navigation";

// Re-export all utilities
export * from "./metadata";
export * from "./navigation";

// Conditional imports for server-side modules
const getOldSidebarGenerator = () => {
    if (typeof window === "undefined") {
        try {
            return require("../sidebarGenerator").default;
        } catch {
            return null;
        }
    }
    return null;
};

const getNewSidebarSystem = () => {
    if (typeof window === "undefined") {
        try {
            const sidebarModule = require("../sidebar");
            return {
                generateSidebar: sidebarModule.generateSidebar,
                generateMultiLanguageSidebar:
                    sidebarModule.generateMultiLanguageSidebar,
                createSidebarSystem: sidebarModule.createSidebarSystem,
            };
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
    /** Create sidebar generator instance (server-side only) - DEPRECATED */
    generator: (pathname: string) => {
        const SidebarGenerator = getOldSidebarGenerator();
        if (!SidebarGenerator) {
            console.warn("SidebarGenerator is only available server-side");
            return null;
        }
        return new SidebarGenerator(pathname);
    },

    /** Generate sidebar and return the result (server-side only) - DEPRECATED */
    generate: (pathname: string) => {
        const generator = sidebar.generator(pathname);
        return generator ? generator.sidebar : null;
    },

    /** Get corrected pathname from generator (server-side only) - DEPRECATED */
    getCorrectedPathname: (pathname: string) => {
        const generator = sidebar.generator(pathname);
        return generator ? generator.correctedPathname : pathname;
    },

    // New sidebar system methods
    /** Generate sidebar for a specific language (NEW) */
    generateForLanguage: async (language: string) => {
        const newSystem = getNewSidebarSystem();
        if (newSystem) {
            return await newSystem.generateSidebar(language);
        }
        console.warn("New sidebar system is only available server-side");
        return null;
    },

    /** Generate sidebars for multiple languages (NEW) */
    generateMultiLanguage: async (languages: string[]) => {
        const newSystem = getNewSidebarSystem();
        if (newSystem) {
            return await newSystem.generateMultiLanguageSidebar(languages);
        }
        console.warn("New sidebar system is only available server-side");
        return null;
    },

    /** Create a custom sidebar system instance (NEW) */
    createSystem: () => {
        const newSystem = getNewSidebarSystem();
        if (newSystem) {
            return newSystem.createSidebarSystem();
        }
        console.warn("New sidebar system is only available server-side");
        return null;
    },
};

/**
 * Navigation utilities
 */
export const nav = {
    sidebar,

    // Direct access (DEPRECATED)
    get SidebarGenerator() {
        return getOldSidebarGenerator();
    },
    createSidebarGenerator: (pathname: string) => sidebar.generator(pathname),
};

/**
 * Main VitePress utilities export
 */
export const vitepressUtils = {
    sidebar,
    nav,

    // Direct access (DEPRECATED)
    get SidebarGenerator() {
        return getOldSidebarGenerator();
    },
    generateSidebar: (pathname: string) => sidebar.generate(pathname),

    // New sidebar system shortcuts
    generateSidebarForLanguage: async (language: string) =>
        sidebar.generateForLanguage(language),
    generateMultiLanguageSidebar: async (languages: string[]) =>
        sidebar.generateMultiLanguage(languages),

    // Metadata utilities
    ...metadata,

    // Navigation utilities
    ...navigation,
};

export default vitepressUtils;
