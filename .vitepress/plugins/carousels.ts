import { tab, MarkdownItTabOptions } from "@mdit/plugin-tab";
import { logger } from '../config/sidebarControl';
import type { PluginSimple } from "markdown-it";

export const carousels: PluginSimple = (md) => {
    md.use(tab, carouselsRenderer)

    md.renderer.rules.paragraph_open = (tokens, idx, options, env, self) => {
        const token = tokens[idx - 1];
        if (token && token.type.match("carousels_tab_open")) {
            return ''; // Skip paragraph opening inside container
        }
        return self.renderToken(tokens, idx, options);
    };

    md.renderer.rules.paragraph_close = (tokens, idx, options, env, self) => {
        const token = tokens[idx - 3];
        if (token && token.type.match("carousels_tab_open")) {
            return ''; // Skip paragraph closing inside container
        }
        return self.renderToken(tokens, idx, options);
    };
};

const carouselsRenderer: MarkdownItTabOptions = {
    name: "carousels",
    tabsOpenRenderer(info, tokens, index, opt, env) {
        const content = JSON.parse(JSON.stringify(env))
        const IContent = content.content
        let token: string = ""
        let config: string = ""
        if (IContent && typeof IContent === "string") {
            const matches = IContent.match(/carousels#\{[^\}]*\}/g)
            if (matches) {
                matches.forEach(match => {
                    token += match.replace("carousels#", "")
                })
            }
        }
        // logger(token, "jsonCheck.json")
        try {
            const configObj = JSON.parse(token)

            if (configObj.arrows) {
                if (typeof configObj.arrows === "boolean") {
                    config += ` :show-arrows="${configObj.arrows}"`
                } else if (configObj.arrows === "hover") {
                    config += ` :show-arrows="hover"`
                }
            }

            if (configObj.undelimiters && configObj.undelimiters === true) config += ` :hide-delimiters="true"`

            if (configObj.cycle && configObj.cycle === true) {
                config += ` :cycle="true"`

                if (configObj.interval && typeof configObj.interval === "number") {
                    config += ` :interval="${configObj.interval}"`
                }
            }

            // if (configObj.height && typeof configObj.height === "number") config += `height="${configObj.height * 0.36}" `
        } catch (error) { }

        return `<MdCarousel${config} >`;
    },
    tabsCloseRenderer() {
        return `</MdCarousel>`;
    },
    tabOpenRenderer(data) {
        return `\n<v-carousel-item cover src="${data.title}">\n`;
    },
    tabCloseRenderer() {
        return `</v-carousel-item>`;
    },
};