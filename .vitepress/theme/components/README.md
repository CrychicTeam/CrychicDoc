# CryChicDoc 组件系统

> [English Documentation](./README_EN.md)

为 CryChicDoc 设计的有组织的组件系统，具有插件式工具集成。

## 📁 目录结构

```
components/
├── ui/                 # 用户界面组件
├── content/            # 内容相关组件
├── media/              # 媒体查看器组件
├── navigation/         # 导航组件
└── index.ts           # 组件管理器与自动导入
```

## 🎯 组件分类详解

### UI 组件 (`./ui/`)

| 组件名             | 描述         | 主要功能                           |
| ------------------ | ------------ | ---------------------------------- |
| **ProgressLinear** | 进度条组件   | 显示加载进度和状态                 |
| **Buttons**        | 浮动操作按钮 | 导航快捷操作，包含回到顶部、主页等 |
| **State**          | 应用状态显示 | 显示应用当前状态信息               |
| **Animation**      | 动画工具组件 | 提供各种动画效果和过渡             |
| **NotFound**       | 404 错误页面 | 自定义 404 页面组件                |
| **Preview**        | 内容预览组件 | 内容快速预览功能                   |
| **carousels**      | 轮播图组件   | 图片/内容轮播滑动展示              |

### 内容组件 (`./content/`)

| 组件名                              | 描述               | 主要功能                           | SSR 支持      |
| ----------------------------------- | ------------------ | ---------------------------------- | ------------- |
| **ArticleMetadataCN**               | 文章元数据显示     | 显示阅读时间、字数统计等中文元信息 | ✅            |
| **comment**                         | 评论系统组件       | 集成评论功能                       | ✅            |
| **CommitsCounter**                  | GitHub 提交可视化  | 显示仓库提交活动图表               | ⚠️ ClientOnly |
| **ResponsibleEditor**               | 内容编辑器组件     | 负责人编辑功能                     | ✅            |
| **Linkcard**                        | 链接卡片预览       | 美化的链接预览卡片                 | ✅            |
| **minecraft-advanced-damage-chart** | Minecraft 伤害图表 | 交互式伤害计算和可视化             | ⚠️ ClientOnly |

### 媒体组件 (`./media/`)

| 组件名            | 描述             | 支持格式            | 响应式 |
| ----------------- | ---------------- | ------------------- | ------ |
| **PdfViewer**     | PDF 文档查看器   | PDF 文档            | ✅     |
| **YoutubeVideo**  | YouTube 视频嵌入 | YouTube 链接        | ✅     |
| **BilibiliVideo** | B 站视频嵌入     | Bilibili 视频       | ✅     |
| **imageViewer**   | 图片查看器       | JPG, PNG, GIF, WebP | ✅     |

### 导航组件 (`./navigation/`)

| 组件名        | 描述         | 特性                     |
| ------------- | ------------ | ------------------------ |
| **MNavLink**  | 导航链接组件 | 支持图标、徽章、工具提示 |
| **MNavLinks** | 导航链接组   | 批量导航链接管理         |
| **Footer**    | 网站页脚组件 | 网站底部信息展示         |

## 📦 导入方式

### 1. 直接分类导入

```typescript
// UI 组件
import { ProgressLinear, Buttons, State } from "@components/ui";

// 内容组件
import {
    ArticleMetadataCN,
    comment,
    CommitsCounter,
} from "@components/content";

// 媒体组件
import { PdfViewer, YoutubeVideo, BilibiliVideo } from "@components/media";

// 导航组件
import { MNavLink, MNavLinks, Footer } from "@components/navigation";
```

### 2. 组件管理器导入

```typescript
import { components, getComponent } from "@components";

// 通过注册表访问
const ProgressLinear = components.ui.ProgressLinear;
const ArticleMetadata = components.content.ArticleMetadataCN;

// 动态组件访问
const ButtonComponent = getComponent("ui", "Buttons");
const CommentComponent = getComponent("content", "comment");
```

### 3. 常用组件组合

```typescript
import { commonComponents } from "@components";

// 预定义组件组合
const layoutComponents = commonComponents.layout; // 布局组件
const contentPageComponents = commonComponents.contentPage; // 内容页组件
const mediaViewers = commonComponents.mediaViewers; // 媒体查看器
```

## 🔧 与工具集成

组件与工具系统 (`@utils`) 深度集成，提供：

### 导航工具集成

```typescript
import utils from "@utils";

// 滚动控制
utils.vitepress.scroll.toTop();
utils.vitepress.scroll.toElement("#target");

// 浏览器操作
utils.vitepress.browser.goBack();
utils.vitepress.browser.goHome();
```

### GitHub 集成

```typescript
// GitHub API 调用
const commits = await utils.charts.github.githubApi.fetchAllCommits(
    username,
    repo
);

// 图表生成
const chartOptions =
    utils.charts.github.chartOptions.generateSparklineOptions(data);
```

### 内容分析

```typescript
// 文本处理
const wordCount = utils.content.countWord(text);
const readingTime = utils.content.readingTime.calculate(text);

// 元数据提取
const metadata = utils.content.extractMetadata(content);
```

## 📱 组件依赖关系

### 外部依赖

| 依赖库        | 版本要求 | 用途                     |
| ------------- | -------- | ------------------------ |
| **Vue 3**     | ^3.0.0   | 组合式 API、响应式系统   |
| **VitePress** | ^1.0.0   | useData、useRouter 钩子  |
| **ECharts**   | ^5.0.0   | 图表可视化 (vue-echarts) |
| **Vuetify**   | ^3.0.0   | UI 组件库 (部分组件)     |

### 内部依赖

```typescript
// 工具依赖
import utils from "@utils";

// 类型定义
import type { NavLink, NavIcon } from "@utils/type";

// 组件间依赖
import { MNavLink } from "./MNavLink.vue";
```

## 🎨 样式系统

### CSS 自定义属性

组件使用 CSS 自定义属性实现主题化：

```css
:root {
    /* 元数据样式 */
    --metadata-text-color: #666;
    --metadata-accent-color: #007acc;

    /* 按钮样式 */
    --button-bg-color: #f5f5f5;
    --button-hover-color: #e0e0e0;
    --button-active-color: #d0d0d0;

    /* 动画配置 */
    --transition-duration: 0.3s;
    --animation-easing: cubic-bezier(0.4, 0, 0.2, 1);

    /* 图表样式 */
    --chart-height: 400px;
    --chart-min-height: 300px;
    --mobile-chart-height: 250px;
}
```

### 响应式设计

```css
/* 移动端适配 */
@media (max-width: 768px) {
    .chart-container {
        height: var(--mobile-chart-height);
    }
}

/* 平板适配 */
@media (max-width: 1024px) {
    .navigation-grid {
        grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    }
}
```

## 🔍 TypeScript 支持

### 完整类型定义

```typescript
// 组件注册表类型
export type ComponentRegistry = {
    ui: typeof uiComponents;
    content: typeof contentComponents;
    media: typeof mediaComponents;
    navigation: typeof navigationComponents;
};

// 组件获取器类型
export const getComponent: <
    C extends keyof ComponentRegistry,
    N extends keyof ComponentRegistry[C]
>(
    category: C,
    name: N
) => ComponentRegistry[C][N];

// 属性类型定义
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

### 组件属性接口

```typescript
// 图表组件属性
interface ChartComponentProps {
    username?: string;
    repoName?: string;
    daysToFetch?: number;
    height?: number;
    lineWidth?: number;
    fill?: boolean;
    smooth?: boolean;
}

// 媒体组件属性
interface MediaComponentProps {
    src: string;
    width?: string | number;
    height?: string | number;
    autoplay?: boolean;
    controls?: boolean;
}
```

## 📋 使用示例

### 基础组件使用

```vue
<template>
    <div class="page-layout">
        <!-- 文章元数据 -->
        <ArticleMetadataCN />

        <!-- 浮动按钮 -->
        <Buttons />

        <!-- GitHub 提交统计 -->
        <CommitsCounter
            username="PickAID"
            repo-name="CrychicDoc"
            :days-to-fetch="30"
        />

        <!-- 导航链接 -->
        <MNavLinks title="相关链接" :items="navItems" />
    </div>
</template>

<script setup lang="ts">
    import { ArticleMetadataCN, CommitsCounter } from "@components/content";
    import { Buttons } from "@components/ui";
    import { MNavLinks } from "@components/navigation";

    const navItems = [
        {
            title: "GitHub",
            desc: "项目源码",
            link: "https://github.com/PickAID/CrychicDoc",
            icon: "https://github.com/favicon.ico",
            badge: { text: "开源", type: "info" },
        },
    ];
</script>
```

### 高级组件管理器使用

```typescript
// main.ts - 全局注册
import { createApp } from "vue";
import { components, registerComponents } from "@components";

const app = createApp(App);

// 批量注册常用组件
registerComponents(app, {
    ui: ["Buttons", "ProgressLinear"],
    content: ["ArticleMetadataCN", "CommitsCounter"],
    navigation: ["MNavLink", "Footer"],
});

// 或者注册所有组件
registerComponents(app);
```

### 动态组件加载

```vue
<script setup lang="ts">
    import { ref, defineAsyncComponent } from "vue";
    import { getComponent } from "@components";

    // 动态加载组件
    const currentComponent = ref("CommitsCounter");
    const DynamicComponent = defineAsyncComponent(() =>
        getComponent("content", currentComponent.value)
    );

    // 切换组件
    const switchComponent = (componentName: string) => {
        currentComponent.value = componentName;
    };
</script>

<template>
    <div>
        <button @click="switchComponent('CommitsCounter')">显示提交统计</button>
        <button @click="switchComponent('ArticleMetadataCN')">
            显示文章信息
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

## ⚠️ SSR 兼容性

### 客户端渲染组件

某些组件需要使用 `ClientOnly` 包装或异步导入以避免 SSR 问题：

```vue
<template>
    <div>
        <!-- 服务端安全组件 -->
        <ArticleMetadataCN />
        <comment />

        <!-- 客户端渲染组件 -->
        <ClientOnly>
            <CommitsCounter />
        </ClientOnly>

        <ClientOnly>
            <minecraft-advanced-damage-chart />
        </ClientOnly>
    </div>
</template>
```

### 异步组件导入

对于包含浏览器特定 API 的组件：

```typescript
// 使用 defineAsyncComponent 避免 SSR 问题
const ChartComponent = defineAsyncComponent(
    () => import("@components/content/CommitsCounter.vue")
);

// 或使用 VitePress 的 defineClientComponent
import { defineClientComponent } from "vitepress";

const ClientChart = defineClientComponent(() => {
    return import("@components/content/minecraft-advanced-damage-chart.vue");
});
```

### 浏览器环境检查

在组件内部进行浏览器 API 访问前检查：

```typescript
// 使用 import.meta.env.SSR 检查
if (!import.meta.env.SSR) {
    // 安全访问 document、window 等浏览器 API
    const element = document.querySelector("#app");
}

// 使用 inBrowser 检查（VitePress 提供）
import { inBrowser } from "vitepress";

if (inBrowser) {
    // 浏览器环境代码
}
```

### 条件插件注册

在主题增强函数中条件性地注册插件：

```typescript
async enhanceApp({ app }) {
  if (!import.meta.env.SSR) {
    // 只在客户端注册需要浏览器 API 的插件
    const plugin = await import('plugin-that-access-window-on-import')
    app.use(plugin.default)
  }
}
```

## 🚀 性能优化

### 代码分割

```typescript
// 按需加载组件
const LazyChart = defineAsyncComponent(
    () => import("@components/content/CommitsCounter.vue")
);

// 预加载关键组件
import("@components/ui/Buttons.vue");
```

### 缓存策略

```typescript
// 组件级缓存
const cachedComponent = markRaw(
    defineAsyncComponent(() => import("@components/heavy-component.vue"))
);
```

## 📈 扩展开发

### 添加新组件

1. **创建组件文件**

```bash
# 在对应分类目录下创建
touch .vitepress/theme/components/ui/NewComponent.vue
```

2. **更新索引文件**

```typescript
// ui/index.ts
export { default as NewComponent } from "./NewComponent.vue";
```

3. **添加类型定义**

```typescript
// 在相应的类型文件中添加组件属性接口
interface NewComponentProps {
    title: string;
    variant?: "primary" | "secondary";
}
```

### 组件开发规范

```vue
<script setup lang="ts">
    /**
     * 新组件模板
     * @description 组件功能描述
     * @version 1.0.0
     * @author 开发者名称
     */

    // 导入依赖
    import { ref, computed } from "vue";
    import { useData } from "vitepress";
    import utils from "@utils";

    // 属性定义
    interface Props {
        title: string;
        variant?: "primary" | "secondary";
    }

    const props = withDefaults(defineProps<Props>(), {
        variant: "primary",
    });

    // 响应式数据
    const isActive = ref(false);

    // 计算属性
    const cssClasses = computed(() => ({
        "component-active": isActive.value,
        [`component-${props.variant}`]: true,
    }));
</script>

<template>
    <div :class="cssClasses" class="new-component">
        <h3>{{ title }}</h3>
        <!-- 组件内容 -->
    </div>
</template>

<style scoped>
    .new-component {
        /* 组件样式 */
        transition: all var(--transition-duration) var(--animation-easing);
    }

    .component-active {
        /* 激活状态样式 */
    }

    .component-primary {
        /* 主要变体样式 */
    }

    .component-secondary {
        /* 次要变体样式 */
    }
</style>
```

---

这个有组织的系统提供：

-   ✅ **清晰的组件分类** - 按功能组织
-   ✅ **插件式导入** - 使用 `@components` 别名
-   ✅ **自动补全** - 完整的 TypeScript 支持
-   ✅ **灵活导入方式** - 适应不同使用场景
-   ✅ **工具集成** - 与 `@utils` 系统深度整合
-   ✅ **SSR 兼容** - 支持服务端渲染
-   ✅ **性能优化** - 按需加载和代码分割
-   ✅ **响应式设计** - 移动端友好
-   ✅ **主题支持** - CSS 自定义属性
