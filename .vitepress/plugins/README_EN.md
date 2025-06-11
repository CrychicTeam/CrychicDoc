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

## Registering Plugins

Register your plugins in `markdown-plugins.ts`. Note the difference between registering a tab-factory plugin and a standalone plugin.

-   **Tab Factory Plugins:** Must be registered via the main `@mdit/plugin-tab`.
-   **Standalone Plugins:** Can be registered directly with `md.use()`.

```typescript
import { tab } from "@mdit/plugin-tab";
import { myTabPlugin } from "../plugins/my-tab-plugin"; // Tab factory plugin
import { dialogPlugin } from "../plugins/dialog";     // Standalone plugin

export const markdown: MarkdownOptions = {
    config: async (md) => {
        // Register a tab-based plugin
        md.use(tab, myTabPlugin);

        // Register a standalone plugin
        md.use(dialogPlugin);
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

## Real-World Examples (Tab Factory)

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

## Advanced Custom Plugins

While the Tab Plugin Factory is excellent for container-based components, some scenarios require more complex interactions. The `dialog` plugin serves as a perfect case study for building a standalone plugin from scratch.

### Case Study: The Dialog Plugin

The dialog plugin allows defining a block of content and then triggering it from anywhere in the document with an inline link. This requires a more sophisticated approach than a single container rule.

**Syntax:**

1.  **Definition (Block Rule):**
    ```markdown
    @@@ dialog-def#my-dialog {"title": "My Dialog"}
    # Dialog Content
    This is **markdown** content.
    @@@
    ```
2.  **Trigger (Inline Rule):**
    ```markdown
    Click :::dialog#my-dialog here::: to open.
    ```

**Architecture:**

The plugin uses a three-rule architecture to achieve this:

1.  **Block Rule (`dialog_def`):** Scans for `@@@ dialog-def#...` blocks. It parses the ID, configuration, and raw Markdown content. It doesn't render anything immediately; instead, it stores the parsed definition in `state.env.dialogs.definitions` for later use.
2.  **Inline Rule (`dialog_ref`):** Scans for `:::dialog#...` syntax. It creates a temporary `dialog_ref` placeholder token and stores the trigger's metadata (ID, display text) in `state.env.dialogs.list`.
3.  **Core Rule (`dialog_tail`):** This rule runs *after* all block and inline parsing is complete. It iterates through the final token stream, finds the `dialog_ref` tokens, and replaces them with the final `<MdDialog>` Vue component string. It looks up the full definition from `state.env` using the ID stored in the token.

**Key Techniques:**

-   **State Management with `state.env`:** `state.env` acts as a temporary, per-file datastore, allowing different rules (block, inline, core) to communicate and share data during the render process.
-   **Deferred Rendering with Core Rules:** By using a core rule, we defer the final HTML generation until all information is collected. This is a powerful pattern for plugins that need context from the entire document.
-   **Rendering Nested Markdown:** To ensure the dialog's content is rendered with full VitePress features (like syntax highlighting and other plugins), the core rule uses `md.render(content, env)`. This is the recommended way to process nested Markdown content within a plugin.
-   **Content Manipulation:** The plugin programmatically downgrades headings (H1 -> H2, etc.) by manipulating the raw markdown string (`content.replace(/^(#+) /gm, ...)` before rendering, ensuring visual consistency.
-   **Preserving Indentation:** Special care is taken when parsing the `dialog-def` block to correctly calculate and trim the base indentation, which preserves the relative indentation of code blocks within.

This architecture provides a robust blueprint for creating powerful custom Markdown features in VitePress.

## Contributing

Pull requests are welcome to improve this factory system!

## License

MIT
