# Tab Plugin Factory

English | [中文](./README.md)

A factory function system for creating custom VitePress Markdown plugins based on `@mdit/plugin-tab`.

## Overview

The `createTabPlugin` factory function simplifies the process of creating custom tab container plugins, allowing you to easily transform Markdown content into various Vue/Vuetify components.

## Quick Start

### Basic Usage

```typescript
import { createTabPlugin, configMappers } from "./tab-plugin-factory";

export const myPlugin = createTabPlugin({
    name: "my-container",
    containerComponent: "v-card",
    tabComponent: "v-card-item",
});
```

Use in Markdown:

```markdown
::: my-container
@tab Title 1
Content 1

@tab Title 2  
Content 2
:::
```

### Advanced Usage with Configuration

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

Use in Markdown:

```markdown
::: carousel{"arrows": true, "interval": 3000}
@tab image1.jpg
@tab image2.jpg  
@tab image3.jpg
:::
```

## API Reference

### createTabPlugin(config)

Creates a tab plugin configuration object.

#### Parameters

| Parameter                | Type                                     | Required | Description                       |
| ------------------------ | ---------------------------------------- | -------- | --------------------------------- |
| `name`                   | `string`                                 | ✅       | Name of the plugin/container      |
| `containerComponent`     | `string`                                 | ✅       | Container component tag name      |
| `tabComponent`           | `string`                                 | ✅       | Individual tab component tag name |
| `configMapping`          | `Record<string, (value: any) => string>` | ❌       | Configuration options mapping     |
| `containerRenderer`      | `Function`                               | ❌       | Custom container renderer         |
| `containerCloseRenderer` | `Function`                               | ❌       | Custom container close renderer   |
| `tabRenderer`            | `Function`                               | ❌       | Custom tab renderer               |
| `defaultConfig`          | `Record<string, any>`                    | ❌       | Default configuration values      |
| `requiredConfig`         | `string[]`                               | ❌       | Required configuration keys       |
| `useSlots`               | `boolean`                                | ❌       | Whether to use Vue slots          |
| `slotPattern`            | `Function`                               | ❌       | Custom slot pattern               |

### configMappers

Predefined configuration mappers:

```typescript
export const configMappers = {
    // Maps boolean to show/hide attribute
    showHide: (key: string) => (value: boolean) => `:${key}="${value}"`,

    // Maps value directly to attribute
    direct: (key: string) => (value: any) => `${key}="${value}"`,

    // Maps value to Vue prop
    prop: (key: string) => (value: any) => `:${key}="${value}"`,

    // Maps string value to attribute
    attr: (key: string) => (value: string) => `${key}="${value}"`,

    // Maps object to JSON prop
    json: (key: string) => (value: object) =>
        `:${key}='${JSON.stringify(value)}'`,
};
```

## Real Examples

### 1. Image Carousel Plugin

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

### 2. Stepper Plugin (Using Slots)

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

### 3. Iframe Embed Plugin

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

## More Examples

Check out [`example-new-plugin.ts`](./example-new-plugin.ts) for more creative examples:

-   **Gallery Plugin** - Grid layout image showcase
-   **Video Player Plugin** - Multi-configuration video playback
-   **Code Comparison Plugin** - Before/after comparison using Vuetify tabs
-   **Alert Stack Plugin** - Different types of warning messages

## Registering Plugins

Register your plugins in `markdown-plugins.ts`:

```typescript
import { tab } from "@mdit/plugin-tab";
import { myPlugin } from "../plugins/my-plugin";

export const markdown: MarkdownOptions = {
    config: async (md) => {
        // Register tab-based plugins
        md.use(tab, myPlugin);

        // Other plugins...
    },
};
```

## Best Practices

### 1. Configuration Validation

Use `requiredConfig` to ensure necessary configuration exists:

```typescript
export const myPlugin = createTabPlugin({
    name: "my-plugin",
    requiredConfig: ["src", "title"],
    // ...
});
```

### 2. Default Values

Provide reasonable default configurations:

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

### 3. Type Safety

Leverage TypeScript's type checking:

```typescript
interface MyPluginConfig {
    src: string;
    width?: number;
    height?: number;
}

export const myPlugin = createTabPlugin({
    name: "my-plugin",
    containerRenderer: (info, config: MyPluginConfig) => {
        // TypeScript will validate config types
        return `<my-component src="${config.src}">`;
    },
});
```

### 4. Error Handling

The factory automatically handles configuration parsing errors and required field validation, but you can add additional validation in custom renderers.

## Contributing

Pull requests are welcome to improve this factory system!

## License

MIT
