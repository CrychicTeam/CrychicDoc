/**
 * Content utilities for CryChicDoc
 * Word counting, text processing, and content parsing
 */

import { countWord } from '../functions';
import gitbookParser from '../mdParser';

/**
 * Text processing utilities
 */
export const text = {
  /** Count words in text with multi-language support */
  countWord,
  
  /** Calculate reading time based on word count */
  getReadingTime: (text: string, wordsPerMinute: number = 200): number => {
    const wordCount = countWord(text);
    return Math.ceil(wordCount / wordsPerMinute);
  },
};

/**
 * Parser utilities
 */
export const parser = {
  /** Parse GitBook-style SUMMARY.md files */
  gitbook: (path: string) => new gitbookParser(path),
  parseGitbook: (path: string) => new gitbookParser(path),
};

/**
 * Main content utilities export
 */
export const contentUtils = {
  text,
  parser,
  
  // Direct access
  countWord,
  parseGitbook: (path: string) => new gitbookParser(path),
  getReadingTime: text.getReadingTime,
};

export default contentUtils; 