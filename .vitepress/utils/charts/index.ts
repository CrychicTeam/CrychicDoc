/**
 * Chart utilities for CryChicDoc
 * Data visualization and chart generation helpers
 */

export * from './github';
import * as github from './github';

/**
 * Chart configuration utilities
 */
export const config = {
  /** Get default chart theme */
  getDefaultTheme: () => 'light' as const,
  
  /** Chart color palettes */
  palettes: {
    light: ['#3eaf7c', '#2c3e50', '#e74c3c', '#f39c12', '#9b59b6'],
    dark: ['#4ade80', '#e5e7eb', '#ef4444', '#f59e0b', '#a855f7'],
  },
};

/**
 * Data processing utilities
 */
export const data = {
  /** Process data for chart rendering */
  processData: (rawData: any[]) => {
    // Placeholder for data processing logic
    return rawData;
  },
  
  /** Validate chart data */
  validateData: (data: any) => {
    return Array.isArray(data) && data.length > 0;
  },
};

/**
 * Main charts utilities export
 */
export const chartsUtils = {
  config,
  data,
  
  // GitHub integration - direct import
  github,
  
  // Placeholder for future chart utilities
  // mermaid: {},
  // plotly: {},
  // chartjs: {},
};

export default chartsUtils; 