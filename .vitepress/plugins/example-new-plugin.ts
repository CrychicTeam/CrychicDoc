import { createTabPlugin, configMappers } from "./tab-plugin-factory";

/**
 * Example: Simple Gallery Plugin
 * Usage: ::: gallery{"columns": 3, "gap": "10px"}
 * @tab image1.jpg
 * @tab image2.jpg
 * :::
 */
export const gallery = createTabPlugin({
    name: "gallery",
    containerComponent: "div",
    tabComponent: "img",

    configMapping: {
        columns: configMappers.attr("data-columns"),
        gap: configMappers.attr("data-gap"),
    },

    defaultConfig: {
        columns: 2,
        gap: "8px",
    },

    containerRenderer: (info, config, parsedConfig) => {
        return `<div class="md-gallery" style="display: grid; grid-template-columns: repeat(${config.columns}, 1fr); gap: ${config.gap};"${parsedConfig}>`;
    },

    tabRenderer: (data) => {
        return `<img src="${data.title}" alt="Gallery image ${
            data.index + 1
        }" class="gallery-item">`;
    },
});

/**
 * Example: Video Player Plugin
 * Usage: ::: video{"autoplay": false, "controls": true}
 * @tab video1.mp4
 * @tab video2.mp4
 * :::
 */
export const video = createTabPlugin({
    name: "video",
    containerComponent: "div",
    tabComponent: "video",

    configMapping: {
        autoplay: configMappers.prop("autoplay"),
        controls: configMappers.prop("controls"),
        muted: configMappers.prop("muted"),
        loop: configMappers.prop("loop"),
        width: configMappers.attr("width"),
        height: configMappers.attr("height"),
    },

    defaultConfig: {
        controls: true,
        muted: false,
    },

    containerRenderer: (info, config) => {
        return `<div class="video-container">`;
    },

    tabRenderer: (data, info) => {
        const { title: src } = data;
        const isActive = data.isActive
            ? ' style="display: block;"'
            : ' style="display: none;"';
        return `<video src="${src}" class="video-player"${isActive}>`;
    },

    containerCloseRenderer: () => `</div>`,
});

/**
 * Example: Code Comparison Plugin with Slots
 * Usage: ::: comparison
 * @tab Before
 * @tab After
 * :::
 */
export const comparison = createTabPlugin({
    name: "comparison",
    containerComponent: "v-card",
    tabComponent: "template",
    useSlots: true,

    containerRenderer: () => {
        return `<v-card class="code-comparison">
      <v-tabs v-model="activeTab">
        <v-tab value="before">Before</v-tab>
        <v-tab value="after">After</v-tab>
      </v-tabs>
      <v-window v-model="activeTab">`;
    },

    slotPattern: (data) => {
        const value = data.index === 0 ? "before" : "after";
        return `<v-window-item value="${value}">`;
    },

    containerCloseRenderer: () => `</v-window></v-card>`,
});

/**
 * Example: Alert Tabs Plugin
 * Usage: ::: alerts{"variant": "outlined"}
 * @tab:warning Warning message
 * @tab:error Error message
 * @tab:success Success message
 * :::
 */
export const alerts = createTabPlugin({
    name: "alerts",
    containerComponent: "div",
    tabComponent: "v-alert",

    configMapping: {
        variant: configMappers.attr("variant"),
        closable: configMappers.prop("closable"),
    },

    defaultConfig: {
        variant: "text",
    },

    containerRenderer: (info, config) => {
        return `<div class="alert-stack">`;
    },

    tabRenderer: (data) => {
        // Extract alert type from tab ID if provided (e.g., @tab:warning becomes type="warning")
        const alertType = data.id || "info";
        return `<v-alert type="${alertType}" class="mb-2">`;
    },

    containerCloseRenderer: () => `</div>`,
});
