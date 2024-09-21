import { tab } from "@mdit/plugin-tab";
import type { PluginSimple } from "markdown-it";

function deepCloneEnv(env) {
    return JSON.parse(JSON.stringify(env));
}

export const iframes: PluginSimple = (md) => {
    md.use(tab, {
        name: "iframes",
        tabsOpenRenderer(info, tokens, index, opt, env) {
            const localEnv = deepCloneEnv(env);
            const IContent = localEnv.content;
            let config = 'style="width: 100%; border: none;"';

            const token = tokens[index]; 
            if (token && token.meta && typeof token.meta.id === 'string') {
                try {
                    const configObj = JSON.parse(token.meta.id);
                    if (!configObj.src) {
                        throw new Error("src is required for iframe");
                    }
                    config += ` src="${configObj.src}"`;
                    
                    if (configObj.height) {
                        config += ` height="${configObj.height}"`;
                    }
                } catch (error) {
                    console.error("Error parsing iframe config:", error);
                    return '';
                }
            } else {
                console.error("Invalid or missing iframe configuration");
                return '';
            }
            
            return `<div class="iframe-container"><iframe ${config}>`;
        },
        tabsCloseRenderer() {
            return `</iframe></div>`;
        },
        tabOpenRenderer() {
            return `\n`;
        },
        tabCloseRenderer() {
            return `\n`;
        },
    });

    // 添加自定义样式
    const style = `
    <style>
    .iframe-container {
        width: 100%;
        overflow: hidden;
    }
    .iframe-container iframe {
        width: 100% !important;
        border: none;
        overflow: hidden;
    }
    /* Hide scrollbar for Chrome, Safari and Opera */
    .iframe-container iframe::-webkit-scrollbar {
        display: none;
    }
    /* Hide scrollbar for IE, Edge and Firefox */
    .iframe-container iframe {
        -ms-overflow-style: none;  /* IE and Edge */
        scrollbar-width: none;  /* Firefox */
    }
    </style>
    `;
    md.renderer.rules.iframes_open = (tokens, idx) => style + md.renderer.renderToken(tokens, idx, {});
};