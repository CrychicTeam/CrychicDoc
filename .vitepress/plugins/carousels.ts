import { tab, MarkdownItTabOptions } from "@mdit/plugin-tab";
import { logger } from "../config/sidebarControl";
import type { PluginSimple } from "markdown-it";

// 深拷贝函数
function deepCloneEnv(env) {
    return JSON.parse(JSON.stringify(env));
}

export const carousels: PluginSimple = (md) => {
    md.use(tab, {
        name: "carousels",
        tabsOpenRenderer(info, tokens, index, opt, env) {
            // 每次使用时深拷贝 env，确保配置不被共享
            const localEnv = deepCloneEnv(env);
            const IContent = localEnv.content;
            let config = "";

            // 重新解析 meta 字段中的配置，确保每次渲染时配置独立
            const token = tokens[index]; 
            if (token && token.meta && typeof token.meta.id === 'string') {
                try {
                    const configObj = JSON.parse(token.meta.id);
                    if (configObj.arrows !== undefined) {
                        config += ` :show-arrows="${configObj.arrows}"`;
                    }
                    if (configObj.cycle !== undefined) {
                        config += ` :cycle="${configObj.cycle}"`;
                    }
                    if (configObj.interval !== undefined) {
                        config += ` :interval="${configObj.interval}"`;
                    }
                    if (configObj.undelimiters !== undefined) {
                        config += ` :hide-delimiters="${configObj.undelimiters}"`;
                    }
                    config += ` :continuous="true"`;
                } catch (error) {
                    console.error("Error parsing carousel config from meta:", error);
                }
            }
            
            // 确保每次返回独立配置的组件实例
            return `<MdCarousel
                v-model="currentIndex"${config}
                @update:model-value="handleSlideChange"
                @before-change="onBeforeChange"
            >`;
        },
        tabsCloseRenderer() {
            return `</MdCarousel>`;
        },
        tabOpenRenderer() {
            return `\n<v-carousel-item>\n`;
        },
        tabCloseRenderer() {
            return `</v-carousel-item>\n`;
        },
    });
};