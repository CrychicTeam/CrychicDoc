/**
 * CryChicDoc Utils Plugin System
 * Organized utility functions for easy access via @utils
 */

// Import organized utilities from subdirectories
import { contentUtils } from "./content";
import { vitepressUtils } from "./vitepress";
import { chartsUtils } from "./charts";
import { i18nUtils } from "./i18n";

// Direct imports for backward compatibility
import { countWord } from "./functions";
import gitbookParser from "./mdParser";

// Server-side only utility (will be undefined on client)
const getSidebarGenerator = () => {
    if (typeof window === "undefined") {
        try {
            return require("./sidebarGenerator").default;
        } catch {
            return null;
        }
    }
    return null;
};

/**
 * Main utils object - this is what you'll access with @utils
 *
 * Usage examples:
 * import { utils } from '@utils'
 * utils.content.countWord(text)
 * utils.vitepress.generateSidebar(path) // Only works server-side
 * utils.charts.config.getDefaultTheme()
 */
export const utils = {
    // Organized utility modules
    content: contentUtils,
    vitepress: vitepressUtils,
    charts: chartsUtils,
    i18n: i18nUtils,

    // Direct access for common functions (backward compatibility)
    countWord,
    parseGitbook: (path: string) => new gitbookParser(path),
    generateSidebar: (pathname: string) => {
        const SidebarGenerator = getSidebarGenerator();
        if (SidebarGenerator) {
            return vitepressUtils.generateSidebar(pathname);
        }
        console.warn("SidebarGenerator is only available server-side");
        return null;
    },
    get SidebarGenerator() {
        return getSidebarGenerator();
    },

    // Utility categories for organized access
    text: contentUtils.text,
    parser: contentUtils.parser,
    sidebar: vitepressUtils.sidebar,
    nav: vitepressUtils.nav,
};

// Default export for easy importing
export default utils;

// Organized exports for specific use cases
export { contentUtils as content } from "./content";
export { vitepressUtils as vitepress } from "./vitepress";
export { chartsUtils as charts } from "./charts";
export { i18nUtils as i18n } from "./i18n";

// Individual exports for direct access (backward compatibility)
export * from "./functions";
export * from "./mdParser";
export * from "./type";
export * from "./types";
