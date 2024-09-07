import { container } from "@mdit/plugin-container";
import type { PluginSimple } from "markdown-it";

export const card: PluginSimple = (md) => {
    const type = ["text", "flat", "elevated", "tonal", "outlined", "plain"]
    let insideContainer = false;
    type.forEach(name => {
            md.use((md) =>
                container(md, {
                    name,
                    openRender: (tokens, index, _options) => {
                            const info: string = tokens[index].info.trim().slice(name.length).trim();
                            const titles = info.split("#")
                            let title = ""
                            let subTitile = ""
                            switch (titles.length) {
                                case 0:
                                    break;
                                case 1: 
                                    if (titles[0] !== "") {
                                        title += `<template v-slot:title>${titles[0]}</template>`
                                    }
                                    break;
                                case 2:
                                    if (titles[0] !== "") {
                                        title += `<template v-slot:title>${titles[0]}</template>`
                                    }
                                    if (titles[1] !== "") {
                                        subTitile += `<template v-slot:subtitle>${titles[1]}</template>`
                                    }
                                    break;
                                default:
                                    break;
                            }
                            insideContainer = true;
                            return `<p><v-card variant="${name}" >${title}${subTitile}<template v-slot:text>\n`;
                    },
                    closeRender: (): string => {
                        insideContainer = false;
                        return `</template></v-card></p>\n`
                    },
                }),
            )
        }
    );
    md.renderer.rules.paragraph_open = (tokens, idx, options, env, self) => {
        if (insideContainer && tokens[idx].tag === "p") return '';
        return self.renderToken(tokens, idx, options);
    };

    md.renderer.rules.paragraph_close = (tokens, idx, options, env, self) => {
        if (insideContainer && tokens[idx].tag === "p") return '';
        return self.renderToken(tokens, idx, options);
    };
};