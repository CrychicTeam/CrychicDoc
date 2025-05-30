# CryChicDoc Components System

> English Documentation | [中文文档](./README.md)

Organized component system for CryChicDoc with plugin-like utilities integration and modern development patterns.

## 📁 Directory Structure

```
components/
├── ui/                 # User Interface Components
├── content/            # Content-related Components
├── media/              # Media Viewer Components
├── navigation/         # Navigation Components
└── index.ts           # Component Manager & Auto-imports
```

## 🎯 Component Categories Overview

### UI Components (`./ui/`)

| Component          | Description               | Main Features                                 |
| ------------------ | ------------------------- | --------------------------------------------- |
| **ProgressLinear** | Progress bar component    | Loading progress and status display           |
| **Buttons**        | Floating action buttons   | Navigation shortcuts, back to top, home, etc. |
| **State**          | Application state display | Current application state information         |
| **Animation**      | Animation utilities       | Various animation effects and transitions     |
| **NotFound**       | 404 error page            | Custom 404 page component                     |
| **Preview**        | Content preview           | Quick content preview functionality           |
| **carousels**      | Carousel component        | Image/content slideshow display               |

### Content Components (`./content/`)

| Component                           | Description                  | Main Features                                    | SSR Support   |
| ----------------------------------- | ---------------------------- | ------------------------------------------------ | ------------- |
| **ArticleMetadataCN**               | Article metadata display     | Reading time, word count, Chinese metadata       | ✅            |
| **comment**                         | Comment system               | Integrated commenting functionality              | ✅            |
| **CommitsCounter**                  | GitHub commits visualization | Repository commit activity charts                | ⚠️ ClientOnly |
| **ResponsibleEditor**               | Content editor               | Responsible person editing functionality         | ✅            |
| **Linkcard**                        | Link card preview            | Beautiful link preview cards                     | ✅            |
| **minecraft-advanced-damage-chart** | Minecraft damage chart       | Interactive damage calculation and visualization | ⚠️ ClientOnly |

### Media Components (`./media/`)

| Component         | Description          | Supported Formats   | Responsive |
| ----------------- | -------------------- | ------------------- | ---------- |
| **PdfViewer**     | PDF document viewer  | PDF documents       | ✅         |
| **YoutubeVideo**  | YouTube video embed  | YouTube links       | ✅         |
| **BilibiliVideo** | Bilibili video embed | Bilibili videos     | ✅         |
| **imageViewer**   | Image viewer         | JPG, PNG, GIF, WebP | ✅         |

### Navigation Components (`./navigation/`)

| Component     | Description               | Features                         |
| ------------- | ------------------------- | -------------------------------- |
| **MNavLink**  | Navigation link component | Icons, badges, tooltips support  |
| **MNavLinks** | Navigation links group    | Batch navigation link management |
| **Footer**    | Website footer            | Site footer information display  |

## 📦 Import Methods

### 1. Direct Category Import

```typescript
// UI Components
import { ProgressLinear, Buttons, State } from "@components/ui";

// Content Components
import {
    ArticleMetadataCN,
    comment,
    CommitsCounter,
} from "@components/content";

// Media Components
import { PdfViewer, YoutubeVideo, BilibiliVideo } from "@components/media";

// Navigation Components
import { MNavLink, MNavLinks, Footer } from "@components/navigation";
```

### 2. Component Manager Import

```typescript
import { components, getComponent } from "@components";

// Access via registry
const ProgressLinear = components.ui.ProgressLinear;
const ArticleMetadata = components.content.ArticleMetadataCN;

// Dynamic component access
const ButtonComponent = getComponent("ui", "Buttons");
const CommentComponent = getComponent("content", "comment");
```

### 3. Common Component Groups

```typescript
import { commonComponents } from "@components";

// Pre-defined component groups
const layoutComponents = commonComponents.layout; // Layout components
const contentPageComponents = commonComponents.contentPage; // Content page components
const mediaViewers = commonComponents.mediaViewers; // Media viewers
```

## 🔧 Utils Integration

Components are deeply integrated with the utils system (`@utils`), providing:

### Navigation Utils Integration

```typescript
import utils from "@utils";

// Scroll control
utils.vitepress.scroll.toTop();
utils.vitepress.scroll.toElement("#target");

// Browser operations
utils.vitepress.browser.goBack();
utils.vitepress.browser.goHome();
```

### GitHub Integration

```typescript
// GitHub API calls
const commits = await utils.charts.github.githubApi.fetchAllCommits(
    username,
    repo
);

// Chart generation
const chartOptions =
    utils.charts.github.chartOptions.generateSparklineOptions(data);
```

### Content Analysis

```typescript
// Text processing
const wordCount = utils.content.countWord(text);
const readingTime = utils.content.readingTime.calculate(text);

// Metadata extraction
const metadata = utils.content.extractMetadata(content);
```

## 📱 Component Dependencies

### External Dependencies

| Library       | Version | Usage                                  |
| ------------- | ------- | -------------------------------------- |
| **Vue 3**     | ^3.0.0  | Composition API, reactivity system     |
| **VitePress** | ^1.0.0  | useData, useRouter hooks               |
| **ECharts**   | ^5.0.0  | Chart visualization (vue-echarts)      |
| **Vuetify**   | ^3.0.0  | UI component library (some components) |

### Internal Dependencies

```typescript
// Utils dependency
import utils from "@utils";

// Type definitions
import type { NavLink, NavIcon } from "@utils/type";

// Component dependencies
import { MNavLink } from "./MNavLink.vue";
```

## 🎨 Styling System

### CSS Custom Properties

Components use CSS custom properties for theming:

```css
:root {
    /* Metadata styles */
    --metadata-text-color: #666;
    --metadata-accent-color: #007acc;

    /* Button styles */
    --button-bg-color: #f5f5f5;
    --button-hover-color: #e0e0e0;
    --button-active-color: #d0d0d0;

    /* Animation configuration */
    --transition-duration: 0.3s;
    --animation-easing: cubic-bezier(0.4, 0, 0.2, 1);

    /* Chart styles */
    --chart-height: 400px;
    --chart-min-height: 300px;
    --mobile-chart-height: 250px;
}
```

### Responsive Design

```css
/* Mobile adaptation */
@media (max-width: 768px) {
    .chart-container {
        height: var(--mobile-chart-height);
    }
}

/* Tablet adaptation */
@media (max-width: 1024px) {
    .navigation-grid {
        grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    }
}
```

## 🔍 TypeScript Support

### Complete Type Definitions

```typescript
// Component registry types
export type ComponentRegistry = {
    ui: typeof uiComponents;
    content: typeof contentComponents;
    media: typeof mediaComponents;
    navigation: typeof navigationComponents;
};

// Component getter types
export const getComponent: <
    C extends keyof ComponentRegistry,
    N extends keyof ComponentRegistry[C]
>(
    category: C,
    name: N
) => ComponentRegistry[C][N];

// Props type definitions
export interface NavLinkProps {
    icon?: NavIcon | NavThemeIcon;
    title?: string;
    desc?: string;
    link: string;
    badge?:
        | string
        | { text: string; type: "info" | "warning" | "danger" | "tip" };
}
```

### Component Props Interfaces

```typescript
// Chart component props
interface ChartComponentProps {
    username?: string;
    repoName?: string;
    daysToFetch?: number;
    height?: number;
    lineWidth?: number;
    fill?: boolean;
    smooth?: boolean;
}

// Media component props
interface MediaComponentProps {
    src: string;
    width?: string | number;
    height?: string | number;
    autoplay?: boolean;
    controls?: boolean;
}
```

## 📋 Usage Examples

### Basic Component Usage

```vue
<template>
    <div class="page-layout">
        <!-- Article metadata -->
        <ArticleMetadataCN />

        <!-- Floating buttons -->
        <Buttons />

        <!-- GitHub commit stats -->
        <CommitsCounter
            username="PickAID"
            repo-name="CrychicDoc"
            :days-to-fetch="30"
        />

        <!-- Navigation links -->
        <MNavLinks title="Related Links" :items="navItems" />
    </div>
</template>

<script setup lang="ts">
    import { ArticleMetadataCN, CommitsCounter } from "@components/content";
    import { Buttons } from "@components/ui";
    import { MNavLinks } from "@components/navigation";

    const navItems = [
        {
            title: "GitHub",
            desc: "Project source code",
            link: "https://github.com/PickAID/CrychicDoc",
            icon: "https://github.com/favicon.ico",
            badge: { text: "Open Source", type: "info" },
        },
    ];
</script>
```

### Advanced Component Manager Usage

```typescript
// main.ts - Global registration
import { createApp } from "vue";
import { components, registerComponents } from "@components";

const app = createApp(App);

// Register commonly used components
registerComponents(app, {
    ui: ["Buttons", "ProgressLinear"],
    content: ["ArticleMetadataCN", "CommitsCounter"],
    navigation: ["MNavLink", "Footer"],
});

// Or register all components
registerComponents(app);
```

### Dynamic Component Loading

```vue
<script setup lang="ts">
    import { ref, defineAsyncComponent } from "vue";
    import { getComponent } from "@components";

    // Dynamic component loading
    const currentComponent = ref("CommitsCounter");
    const DynamicComponent = defineAsyncComponent(() =>
        getComponent("content", currentComponent.value)
    );

    // Switch components
    const switchComponent = (componentName: string) => {
        currentComponent.value = componentName;
    };
</script>

<template>
    <div>
        <button @click="switchComponent('CommitsCounter')">
            Show Commit Stats
        </button>
        <button @click="switchComponent('ArticleMetadataCN')">
            Show Article Info
        </button>

        <Suspense>
            <template #default>
                <DynamicComponent />
            </template>
            <template #fallback>
                <ProgressLinear />
            </template>
        </Suspense>
    </div>
</template>
```

## ⚠️ SSR Compatibility

### Client-Side Rendering Components

Some components need `ClientOnly` wrapper or async imports to avoid SSR issues:

```vue
<template>
    <div>
        <!-- Server-safe components -->
        <ArticleMetadataCN />
        <comment />

        <!-- Client-side rendering components -->
        <ClientOnly>
            <CommitsCounter />
        </ClientOnly>

        <ClientOnly>
            <minecraft-advanced-damage-chart />
        </ClientOnly>
    </div>
</template>
```

### Async Component Imports

For components containing browser-specific APIs:

```typescript
// Use defineAsyncComponent to avoid SSR issues
const ChartComponent = defineAsyncComponent(
    () => import("@components/content/CommitsCounter.vue")
);

// Or use VitePress's defineClientComponent
import { defineClientComponent } from "vitepress";

const ClientChart = defineClientComponent(() => {
    return import("@components/content/minecraft-advanced-damage-chart.vue");
});
```

### Browser Environment Checks

Check for browser environment before accessing browser APIs in components:

```typescript
// Use import.meta.env.SSR check
if (!import.meta.env.SSR) {
    // Safe access to document, window and other browser APIs
    const element = document.querySelector("#app");
}

// Use inBrowser check (provided by VitePress)
import { inBrowser } from "vitepress";

if (inBrowser) {
    // Browser environment code
}
```

### Conditional Plugin Registration

Conditionally register plugins in theme enhance function:

```typescript
async enhanceApp({ app }) {
    if (!import.meta.env.SSR) {
        // Only register plugins that need browser APIs on client side
        const plugin = await import("plugin-that-access-window-on-import");
        app.use(plugin.default);
    }
}
```

## 🚀 Performance Optimization

### Code Splitting

```typescript
// Lazy load components
const LazyChart = defineAsyncComponent(
    () => import("@components/content/CommitsCounter.vue")
);

// Preload critical components
import("@components/ui/Buttons.vue");
```

### Caching Strategies

```typescript
// Component-level caching
const cachedComponent = markRaw(
    defineAsyncComponent(() => import("@components/heavy-component.vue"))
);
```

## 📈 Extension Development

### Adding New Components

1. **Create Component File**

```bash
# Create in corresponding category directory
touch .vitepress/theme/components/ui/NewComponent.vue
```

2. **Update Index File**

```typescript
// ui/index.ts
export { default as NewComponent } from "./NewComponent.vue";
```

3. **Add Type Definitions**

```typescript
// Add component props interface in corresponding type file
interface NewComponentProps {
    title: string;
    variant?: "primary" | "secondary";
}
```

### Component Development Standards

```vue
<script setup lang="ts">
    /**
     * New Component Template
     * @description Component functionality description
     * @version 1.0.0
     * @author Developer name
     */

    // Import dependencies
    import { ref, computed } from "vue";
    import { useData } from "vitepress";
    import utils from "@utils";

    // Props definition
    interface Props {
        title: string;
        variant?: "primary" | "secondary";
    }

    const props = withDefaults(defineProps<Props>(), {
        variant: "primary",
    });

    // Reactive data
    const isActive = ref(false);

    // Computed properties
    const cssClasses = computed(() => ({
        "component-active": isActive.value,
        [`component-${props.variant}`]: true,
    }));
</script>

<template>
    <div :class="cssClasses" class="new-component">
        <h3>{{ title }}</h3>
        <!-- Component content -->
    </div>
</template>

<style scoped>
    .new-component {
        /* Component styles */
        transition: all var(--transition-duration) var(--animation-easing);
    }

    .component-active {
        /* Active state styles */
    }

    .component-primary {
        /* Primary variant styles */
    }

    .component-secondary {
        /* Secondary variant styles */
    }
</style>
```

## 🔧 Build and Development

### Development Setup

```bash
# Install dependencies
npm install

# Start development server
npm run docs:dev

# Build for production
npm run docs:build
```

### Component Testing

```typescript
// Component unit test example
import { mount } from "@vue/test-utils";
import { describe, it, expect } from "vitest";
import { Buttons } from "@components/ui";

describe("Buttons Component", () => {
    it("renders correctly", () => {
        const wrapper = mount(Buttons);
        expect(wrapper.exists()).toBe(true);
    });

    it("handles scroll to top", async () => {
        const wrapper = mount(Buttons);
        const scrollSpy = vi.spyOn(utils.vitepress.scroll, "toTop");

        await wrapper.find(".scroll-top-btn").trigger("click");
        expect(scrollSpy).toHaveBeenCalled();
    });
});
```

### Linting and Formatting

```json
// .eslintrc.js
{
    "extends": ["@vue/typescript/recommended", "plugin:vue/vue3-recommended"],
    "rules": {
        "vue/component-name-in-template-casing": ["error", "PascalCase"],
        "vue/multi-word-component-names": "off"
    }
}
```

## 📚 Migration Guide

### From Legacy Components

```typescript
// Before (legacy import)
import Button from "../components/Button.vue";
import Chart from "../components/Chart.vue";

// After (organized import)
import { Buttons } from "@components/ui";
import { CommitsCounter } from "@components/content";
```

### Component API Changes

```typescript
// Before
<CommitsCounter
  :username="'PickAID'"
  :repo-name="'CrychicDoc'"
/>

// After (with enhanced props)
<CommitsCounter
  username="PickAID"
  repo-name="CrychicDoc"
  :days-to-fetch="30"
  :smooth="true"
  :fill="true"
/>
```

---

This organized system provides:

-   ✅ **Clear component separation** - Organized by functionality
-   ✅ **Plugin-like imports** - Using `@components` alias
-   ✅ **Auto-completion** - Full TypeScript support
-   ✅ **Flexible import methods** - Adapts to different use cases
-   ✅ **Utils integration** - Deep integration with `@utils` system
-   ✅ **SSR compatibility** - Server-side rendering support
-   ✅ **Performance optimization** - Lazy loading and code splitting
-   ✅ **Responsive design** - Mobile-friendly
-   ✅ **Theme support** - CSS custom properties
-   ✅ **Developer experience** - Modern development patterns
-   ✅ **Type safety** - Comprehensive TypeScript definitions
