# CryChicDoc Components

Organized component system for CryChicDoc, with plugin-like utilities integration.

## 📁 Directory Structure

```
components/
├── ui/                 # User Interface Components
├── content/            # Content-related Components
├── media/              # Media Viewer Components
├── navigation/         # Navigation Components
└── index.ts           # Component Manager & Auto-imports
```

## 🎯 Component Categories

### UI Components (`./ui/`)

-   **ProgressLinear** - Progress bar component
-   **Buttons** - Floating action buttons with navigation
-   **State** - Application state display
-   **Animation** - Animation utilities and components
-   **NotFound** - 404 error page component
-   **Preview** - Content preview component
-   **Carousels** - Carousel/slider component

### Content Components (`./content/`)

-   **ArticleMetadataCN** - Article metadata display (Chinese)
-   **Comment** - Comment system component
-   **CommitsCounter** - GitHub commits visualization
-   **ResponsibleEditor** - Content editor component
-   **Linkcard** - Link card preview component
-   **MinecraftAdvancedDamageChart** - Minecraft damage chart

### Media Components (`./media/`)

-   **PdfViewer** - PDF document viewer
-   **YoutubeVideo** - YouTube video embed
-   **BilibiliVideo** - Bilibili video embed
-   **ImageViewer** - Image viewer with controls

### Navigation Components (`./navigation/`)

-   **MNavLink** - Navigation link component
-   **MNavLinks** - Navigation links group
-   **Footer** - Site footer component

## 📦 Import Methods

### 1. Direct Category Import

```typescript
import { ProgressLinear, Buttons } from "@components/ui";
import { ArticleMetadataCN, Comment } from "@components/content";
import { PdfViewer, YoutubeVideo } from "@components/media";
import { MNavLink, Footer } from "@components/navigation";
```

### 2. Component Manager Import

```typescript
import { components, getComponent } from "@components";

// Access via registry
const ProgressLinear = components.ui.ProgressLinear;
const ArticleMetadata = components.content.ArticleMetadataCN;

// Dynamic component access
const ButtonComponent = getComponent("ui", "Buttons");
const CommentComponent = getComponent("content", "Comment");
```

### 3. Auto-import for Vue Apps

```typescript
import { registerComponents } from "@components";

// Register all components globally
registerComponents(app);

// Now use in templates without imports
<template>
    <ProgressLinear />
    <Buttons />
    <ArticleMetadataCN />
</template>;
```

### 4. Common Component Groups

```typescript
import { commonComponents } from "@components";

// Pre-defined component groups
const layoutComponents = commonComponents.layout;
const contentPageComponents = commonComponents.contentPage;
const mediaViewers = commonComponents.mediaViewers;
```

## 🔧 Integration with Utils

Components are integrated with the utils system (`@utils`) for:

-   **Navigation utilities** - Scroll, browser actions, path navigation
-   **GitHub integration** - Commit data, chart generation
-   **Metadata processing** - Reading time, word count, translations
-   **Content analysis** - Text processing, image counting

### Example Component Usage with Utils

```vue
<script setup>
    import { utils } from "@utils";
    import { ProgressLinear } from "@components/ui";

    // Use navigation utilities
    const scrollToTop = () => utils.vitepress.scroll.toTop();

    // Use content utilities
    const wordCount = utils.content.countWord(text);

    // Use chart utilities
    const chartOptions =
        utils.charts.github.chartOptions.generateSparklineOptions(data);
</script>
```

## 📱 Component Dependencies

### External Dependencies

-   **Vue 3** - Composition API, reactivity
-   **VitePress** - useData, useRouter hooks
-   **ECharts/vue-echarts** - Chart visualization
-   **Vuetify** - UI components (some components)

### Internal Dependencies

-   **@utils** - Utility functions and helpers
-   **Type definitions** - TypeScript support

## 🎨 Styling

Components use CSS custom properties for theming:

-   `--metadata-text-color`
-   `--button-bg-color`
-   `--button-hover-color`
-   `--transition-duration`

## 🔍 Type Safety

All components include TypeScript definitions:

```typescript
export type ComponentRegistry = typeof components;
export const getComponent: <C, N>(
    category: C,
    name: N
) => ComponentRegistry[C][N];
```

## 📋 Usage Examples

### Basic Component Usage

```vue
<template>
    <div>
        <ArticleMetadataCN />
        <Buttons />
        <CommitsCounter username="PickAID" repo-name="CrychicDoc" />
    </div>
</template>

<script setup>
    import { ArticleMetadataCN } from "@components/content";
    import { Buttons } from "@components/ui";
    import { CommitsCounter } from "@components/content";
</script>
```

### Advanced Component Manager Usage

```typescript
import { components, commonComponents, registerComponents } from "@components";

// Get specific components
const { ui, content, media, navigation } = components;

// Register commonly used components
const app = createApp(App);
registerComponents(app);

// Use component groups
const pageComponents = commonComponents.contentPage;
```

This organized system provides:

-   ✅ **Clear separation** of component types
-   ✅ **Plugin-like imports** with `@components`
-   ✅ **Auto-completion** and type safety
-   ✅ **Flexible import methods** for different use cases
-   ✅ **Integration with utils** for shared functionality
