# Tab 插件工厂

[English](./README_EN.md) | 中文

一个用于创建基于 `@mdit/plugin-tab` 的 VitePress 自定义 Markdown 插件的工厂函数系统。

## 概述

`createTabPlugin` 工厂函数简化了创建自定义 tab 容器插件的过程，让你可以轻松地将 Markdown 内容转换为各种 Vue/Vuetify 组件。

## 快速开始

### 基本用法

```typescript
import { createTabPlugin, configMappers } from "./tab-plugin-factory";

export const myPlugin = createTabPlugin({
    name: "my-container",
    containerComponent: "v-card",
    tabComponent: "v-card-item",
});
```

在 Markdown 中使用：

```markdown
::: my-container
@tab 标题 1
内容 1

@tab 标题 2  
内容 2
:::
```

### 带配置的高级用法

```typescript
export const carousel = createTabPlugin({
    name: "carousel",
    containerComponent: "MdCarousel",
    tabComponent: "v-carousel-item",

    configMapping: {
        arrows: configMappers.prop("show-arrows"),
        cycle: configMappers.prop("cycle"),
        interval: configMappers.prop("interval"),
    },

    defaultConfig: {
        cycle: true,
    },

    containerRenderer: (info, config, parsedConfig) => {
        return `<MdCarousel v-model="currentIndex"${parsedConfig}>`;
    },
});
```

在 Markdown 中使用：

```markdown
::: carousel{"arrows": true, "interval": 3000}
@tab 图片 1.jpg
@tab 图片 2.jpg  
@tab 图片 3.jpg
:::
```

## API 参考

### createTabPlugin(config)

创建一个 tab 插件配置对象。

#### 参数

| 参数                     | 类型                                     | 必需 | 描述                 |
| ------------------------ | ---------------------------------------- | ---- | -------------------- |
| `name`                   | `string`                                 | ✅   | 插件/容器的名称      |
| `containerComponent`     | `string`                                 | ✅   | 容器组件标签名       |
| `tabComponent`           | `string`                                 | ✅   | 单个 tab 组件标签名  |
| `configMapping`          | `Record<string, (value: any) => string>` | ❌   | 配置选项映射         |
| `containerRenderer`      | `Function`                               | ❌   | 自定义容器渲染器     |
| `containerCloseRenderer` | `Function`                               | ❌   | 自定义容器关闭渲染器 |
| `tabRenderer`            | `Function`                               | ❌   | 自定义 tab 渲染器    |
| `defaultConfig`          | `Record<string, any>`                    | ❌   | 默认配置值           |
| `requiredConfig`         | `string[]`                               | ❌   | 必需的配置键         |
| `useSlots`               | `boolean`                                | ❌   | 是否使用 Vue 插槽    |
| `slotPattern`            | `Function`                               | ❌   | 自定义插槽模式       |

### configMappers

预定义的配置映射器：

```typescript
export const configMappers = {
    // 布尔值映射为 show/hide 属性
    showHide: (key: string) => (value: boolean) => `:${key}="${value}"`,

    // 直接映射为属性
    direct: (key: string) => (value: any) => `${key}="${value}"`,

    // 映射为 Vue prop
    prop: (key: string) => (value: any) => `:${key}="${value}"`,

    // 映射为字符串属性
    attr: (key: string) => (value: string) => `${key}="${value}"`,

    // 对象映射为 JSON prop
    json: (key: string) => (value: object) =>
        `:${key}='${JSON.stringify(value)}'`,
};
```

## 实际例子

### 1. 图片轮播插件

```typescript
export const carousels = createTabPlugin({
    name: "carousels",
    containerComponent: "MdCarousel",
    tabComponent: "v-carousel-item",

    configMapping: {
        arrows: configMappers.prop("show-arrows"),
        cycle: configMappers.prop("cycle"),
        interval: configMappers.prop("interval"),
        continuous: configMappers.prop("continuous"),
    },

    defaultConfig: {
        continuous: true,
    },

    containerRenderer: (info, config, parsedConfig) => {
        return `<MdCarousel v-model="currentIndex"${parsedConfig}>`;
    },
});
```

### 2. 步骤器插件（使用插槽）

```typescript
export const stepper = createTabPlugin({
    name: "stepper",
    containerComponent: "v-stepper",
    tabComponent: "template",
    useSlots: true,

    containerRenderer: (info) => {
        const { data } = info;
        const items = data.map((tab) => `'${tab.title}'`).join(", ");
        return `<v-stepper :items="[${items}]" class="theme-stepper">`;
    },

    slotPattern: (data) => `<template v-slot:item.${data.index + 1}>`,
});
```

### 3. iframe 嵌入插件

```typescript
export const iframes = createTabPlugin({
    name: "iframes",
    containerComponent: "div",
    tabComponent: "span",

    requiredConfig: ["src"],

    configMapping: {
        height: configMappers.attr("height"),
    },

    containerRenderer: (info, config) => {
        const baseConfig = 'style="width: 100%; border: none;"';
        let iframeConfig = `${baseConfig} src="${config.src}"`;

        if (config.height) {
            iframeConfig += ` height="${config.height}"`;
        }

        return `<div class="iframe-container"><iframe ${iframeConfig}>`;
    },

    containerCloseRenderer: () => `</iframe></div>`,
});
```

## 更多示例

查看 [`example-new-plugin.ts`](./example-new-plugin.ts) 了解更多创意示例：

-   **图片画廊插件** - 网格布局的图片展示
-   **视频播放器插件** - 支持多种配置的视频播放
-   **代码对比插件** - 使用 Vuetify tabs 的前后对比
-   **警告堆栈插件** - 不同类型的警告消息

## 注册插件

在 `markdown-plugins.ts` 中注册你的插件：

```typescript
import { tab } from "@mdit/plugin-tab";
import { myPlugin } from "../plugins/my-plugin";

export const markdown: MarkdownOptions = {
    config: async (md) => {
        // 注册基于 tab 的插件
        md.use(tab, myPlugin);

        // 其他插件...
    },
};
```

## 最佳实践

### 1. 配置验证

使用 `requiredConfig` 确保必需的配置存在：

```typescript
export const myPlugin = createTabPlugin({
    name: "my-plugin",
    requiredConfig: ["src", "title"],
    // ...
});
```

### 2. 默认值

提供合理的默认配置：

```typescript
export const myPlugin = createTabPlugin({
    name: "my-plugin",
    defaultConfig: {
        autoplay: false,
        controls: true,
    },
    // ...
});
```

### 3. 类型安全

利用 TypeScript 的类型检查：

```typescript
interface MyPluginConfig {
    src: string;
    width?: number;
    height?: number;
}

export const myPlugin = createTabPlugin({
    name: "my-plugin",
    containerRenderer: (info, config: MyPluginConfig) => {
        // TypeScript 会验证 config 的类型
        return `<my-component src="${config.src}">`;
    },
});
```

### 4. 错误处理

工厂会自动处理配置解析错误和必需字段验证，但你也可以在自定义渲染器中添加额外的验证。

## 贡献

欢迎提交 Pull Request 来改进这个工厂系统！

## 许可证

MIT
