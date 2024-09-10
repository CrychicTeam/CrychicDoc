import { tab, MarkdownItTabOptions } from "@mdit/plugin-tab";
import { logger } from '../config/sidebarControl';
import type { PluginSimple } from "markdown-it";

export const carousels: PluginSimple = (md) => {
    md.use(tab, {
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
            // logger(JSON.stringify(tokens[index]), "jsonCheck.json")
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
    
                if (configObj.ratio && typeof configObj.ratio === "number") config += `aspectRatio="${configObj.ratio}" `
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
    })
};