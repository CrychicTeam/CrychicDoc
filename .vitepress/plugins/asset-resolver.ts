import type { PluginSimple } from "markdown-it";

/**
 * Asset Resolver Plugin for VitePress
 * Converts @asset_name.ext to proper asset paths
 *
 * Usage in markdown:
 * @model_1.png -> /assets/models/1.png
 * @texture_diamond.jpg -> /assets/textures/diamond.jpg
 * @screenshot_gui.png -> /assets/screenshots/gui.png
 */
export const assetResolver: PluginSimple = (md) => {
    const originalRender = md.render.bind(md);

    md.render = (src: string, env?: any) => {
        // Process asset references before markdown rendering
        const processedSrc = src.replace(
            /@([a-zA-Z0-9_-]+)\.([a-zA-Z0-9]+)/g,
            (match, namepart, extension) => {
                // Try to split by underscore to get type and id
                const parts = namepart.split("_");
                let type: string;
                let id: string;

                if (parts.length > 1) {
                    type = parts[0];
                    id = parts.slice(1).join("_");
                } else {
                    // If no underscore, infer type from extension
                    const extensionMapping: Record<string, string> = {
                        png: "images",
                        jpg: "images",
                        jpeg: "images",
                        gif: "images",
                        svg: "icons",
                        webp: "images",
                        json: "models",
                        obj: "models",
                        mtl: "models",
                    };
                    type =
                        extensionMapping[extension.toLowerCase()] || "assets";
                    id = namepart;
                }

                // Map types to directories
                const typeMapping: Record<string, string> = {
                    model: "models",
                    texture: "textures",
                    image: "images",
                    icon: "icons",
                    screenshot: "screenshots",
                    diagram: "diagrams",
                    ui: "ui",
                    block: "blocks",
                    item: "items",
                };

                const typeDir = typeMapping[type] || type;
                return `/assets/${typeDir}/${id}.${extension}`;
            }
        );

        return originalRender(processedSrc, env);
    };
};
