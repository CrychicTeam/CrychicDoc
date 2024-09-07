import { container } from "@mdit/plugin-container";
import type { PluginSimple } from "markdown-it";

export const justify: PluginSimple = (md) => {
    md.use((md) =>
        container(md, {
            name: "justified-text",
            openRender: () => {
                return `<div class="justified-text">\n`;
            },
            closeRender: () => `</div>\n`,
        })
    );
};
