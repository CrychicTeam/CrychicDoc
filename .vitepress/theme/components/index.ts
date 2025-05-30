/**
 * CryChicDoc Components Manager
 * Centralized component system with plugin-like utilities integration
 */

// Import component categories
import * as uiComponents from './ui';
import * as contentComponents from './content';
import * as mediaComponents from './media';
import * as navigationComponents from './navigation';

/**
 * Component registry type definition
 */
export type ComponentRegistry = {
  ui: typeof uiComponents;
  content: typeof contentComponents;
  media: typeof mediaComponents;
  navigation: typeof navigationComponents;
};

/**
 * Main component registry
 */
export const components: ComponentRegistry = {
  ui: uiComponents,
  content: contentComponents,
  media: mediaComponents,
  navigation: navigationComponents,
};

/**
 * Get component by category and name
 */
export const getComponent = <
  C extends keyof ComponentRegistry,
  N extends keyof ComponentRegistry[C]
>(
  category: C,
  name: N
): ComponentRegistry[C][N] => {
  return components[category][name];
};

/**
 * Common component groups for convenience
 */
export const commonComponents = {
  // Layout components
  layout: {
    Buttons: components.ui.Buttons,
    Footer: components.navigation.Footer,
    ProgressLinear: components.ui.ProgressLinear,
  },
  
  // Content page components
  contentPage: {
    ArticleMetadataCN: components.content.ArticleMetadataCN,
    comment: components.content.comment,
    Linkcard: components.content.Linkcard,
  },
  
  // Media viewers
  mediaViewers: {
    PdfViewer: components.media.PdfViewer,
    YoutubeVideo: components.media.YoutubeVideo,
    BilibiliVideo: components.media.BilibiliVideo,
    ImageViewer: components.media.ImageViewer,
  },
  
  // Charts and data visualization
  charts: {
    CommitsCounter: components.content.CommitsCounter,
    MinecraftAdvancedDamageChart: components.content.MinecraftAdvancedDamageChart,
  },
};

/**
 * Register components globally in Vue app
 */
export const registerComponents = (
  app: any,
  selection?: Partial<{
    [K in keyof ComponentRegistry]: Array<keyof ComponentRegistry[K]>;
  }>
) => {
  if (!selection) {
    // Register all components
    Object.entries(components).forEach(([category, categoryComponents]) => {
      Object.entries(categoryComponents).forEach(([name, component]) => {
        app.component(name, component);
      });
    });
  } else {
    // Register selected components
    Object.entries(selection).forEach(([category, componentNames]) => {
      if (componentNames && components[category as keyof ComponentRegistry]) {
        componentNames.forEach((name) => {
          const component = components[category as keyof ComponentRegistry][name];
          if (component) {
            app.component(name as string, component);
          }
        });
      }
    });
  }
};

// Re-export all component categories for direct imports
export * from './ui';
export * from './content';
export * from './media';
export * from './navigation';

// Export individual commonly used components
export { 
  // UI
  Buttons,
  ProgressLinear,
  State,
  Animation,
  NotFound,
  Preview,
  Carousels
} from './ui';

export {
  // Content
  ArticleMetadataCN,
  comment,
  CommitsCounter,
  ResponsibleEditor,
  Linkcard,
  MinecraftAdvancedDamageChart
} from './content';

export {
  // Media
  PdfViewer,
  YoutubeVideo,
  BilibiliVideo,
  ImageViewer
} from './media';

export {
  // Navigation
  MNavLink,
  MNavLinks,
  Footer
} from './navigation'; 