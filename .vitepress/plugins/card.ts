import { container } from "@mdit/plugin-container";
import type { PluginSimple } from "markdown-it";

export const card: PluginSimple = (md) => {
    const type = ["text", "flat", "elevated", "tonal", "outlined", "plain"];
    let nestingLevel = 0;

    function setupCardContainer(name: string) {
        md.use((md) =>
            container(md, {
                name,
                openRender: (tokens, index, _options) => {
                    const info: string = tokens[index].info.trim().slice(name.length).trim();
                    const titles = info.split("#");
                    let title = "";
                    let subTitle = "";
                    if (titles.length >= 1 && titles[0] !== "") {
                        title = `<template v-slot:title>${titles[0]}</template>`;
                    }
                    if (titles.length >= 2 && titles[1] !== "") {
                        subTitle = `<template v-slot:subtitle>${titles[1]}</template>`;
                    }
                    nestingLevel++;
                    return `<v-card variant="${name}">${title}${subTitle}<template v-slot:text><div class="v-card-content">`;
                },
                closeRender: (): string => {
                    nestingLevel--;
                    return `</div></template></v-card>`;
                },
            })
        );
    }

    // 设置每种类型的卡片容器
    type.forEach(setupCardContainer);

    // 重写段落规则
    const originalParagraphOpen = md.renderer.rules.paragraph_open || function(tokens, idx, options, env, self) {
        return self.renderToken(tokens, idx, options);
    };
    const originalParagraphClose = md.renderer.rules.paragraph_close || function(tokens, idx, options, env, self) {
        return self.renderToken(tokens, idx, options);
    };

    md.renderer.rules.paragraph_open = function(tokens, idx, options, env, self) {
        if (nestingLevel > 0) {
            return '';
        }
        return originalParagraphOpen(tokens, idx, options, env, self);
    };

    md.renderer.rules.paragraph_close = function(tokens, idx, options, env, self) {
        if (nestingLevel > 0) {
            return '';
        }
        return originalParagraphClose(tokens, idx, options, env, self);
    };
};