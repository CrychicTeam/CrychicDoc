/**
 * Navigation utilities for VitePress
 * Extracted from Buttons component and other navigation components
 */

import type { ScrollPosition, TranslationDictionary } from '../types';

/**
 * Scroll utilities
 */
export const scroll = {
  /** Scroll to top of page smoothly */
  toTop: (): void => {
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  },

  /** Scroll to bottom of page */
  toBottom: (): void => {
    window.scrollTo({
      top: document.body.scrollHeight,
      behavior: 'smooth'
    });
  },

  /** Get current scroll position */
  getCurrentPosition: (): ScrollPosition => {
    return {
      x: window.scrollX,
      y: window.scrollY
    };
  },

  /** Check if should show back to top button */
  shouldShowBackTop: (threshold: number = 300): boolean => {
    return window.scrollY > threshold;
  }
};

/**
 * Browser utilities
 */
export const browser = {
  /** Refresh the current page */
  refresh: (): void => {
    window.location.reload();
  },

  /** Go back in browser history */
  goBack: (): void => {
    window.history.back();
  },

  /** Copy current page URL to clipboard */
  copyCurrentUrl: async (): Promise<boolean> => {
    try {
      await navigator.clipboard.writeText(window.location.href);
      return true;
    } catch (error) {
      console.error('Failed to copy URL:', error);
      return false;
    }
  },

  /** Open URL in new tab */
  openInNewTab: (url: string): void => {
    window.open(url, '_blank', 'noopener,noreferrer');
  }
};

/**
 * Path navigation utilities
 */
export const pathNavigation = {
  /** Special path configurations for different routes */
  specialPaths: [
    {
      regex: /^\/(zh|en|jp)\/modpack\/kubejs\/1\.20\.1\/KubeJSCourse\//,
      getTargetPath: (match: RegExpMatchArray) => `/${match[1]}/modpack/kubejs/1.20.1/`,
    },
    {
      regex: /^\/(zh|en|jp)\/modpack\/kubejs\/?$/,
      getTargetPath: (match: RegExpMatchArray) => `/${match[1]}/`,
    },
    {
      regex: /^\/(zh|en|jp)\/modpack\/kubejs\/1\.20\.1\/Introduction\/Catalogue$/,
      getTargetPath: (match: RegExpMatchArray) => `/${match[1]}/modpack/kubejs/1.20.1/`,
    },
    {
      regex: /^\/(zh|en|jp)\/modpack\/kubejs\/1\.20\.1\/(?!KubeJSCourse)/,
      getTargetPath: (match: RegExpMatchArray) => `/${match[1]}/modpack/kubejs/1.20.1/`,
    },
  ],

  /** Get target path for current route */
  getTargetPath: (currentPath: string): string | null => {
    for (const pathConfig of pathNavigation.specialPaths) {
      const match = currentPath.match(pathConfig.regex);
      if (match) {
        return pathConfig.getTargetPath(match);
      }
    }
    return null;
  }
};

/**
 * Button translations for navigation
 */
export const navigationTranslations: TranslationDictionary = {
  backToTop: { "en-US": "Back to Top", "zh-CN": "返回顶部" },
  copyLink: { "en-US": "Copy Link", "zh-CN": "复制链接" },
  refresh: { "en-US": "Refresh", "zh-CN": "刷新" },
  back: { "en-US": "Back", "zh-CN": "返回" },
  comment: { "en-US": "Comment", "zh-CN": "评论" },
  qq: { "en-US": "QQ", "zh-CN": "QQ" },
  discord: { "en-US": "Discord", "zh-CN": "Discord" },
};

/**
 * Get translation for navigation element
 */
export const getNavigationText = (key: string, lang: string): string => {
  return navigationTranslations[key]?.[lang] || navigationTranslations[key]?.["en-US"] || key;
}; 