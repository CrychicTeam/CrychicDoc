import { createTabPlugin, configMappers } from "./tab-plugin-factory";

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
