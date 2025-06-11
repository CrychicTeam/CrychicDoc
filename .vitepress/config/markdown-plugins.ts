//@ts-nocheck
import { argv, cwd, env } from "node:process";

import { MarkdownOptions } from "vitepress";

import timeline from "vitepress-markdown-timeline";
import { BiDirectionalLinks } from "@nolebase/markdown-it-bi-directional-links";
import { InlineLinkPreviewElementTransform } from "@nolebase/vitepress-plugin-inline-link-preview/markdown-it";
import { tabsMarkdownPlugin } from "vitepress-plugin-tabs";
import mdFootnote from "markdown-it-footnote";
import mdTaskLists from "markdown-it-task-lists";
import mdDeflist from "markdown-it-deflist";
import mdAbbr from "markdown-it-abbr";
import { imgSize } from "@mdit/plugin-img-size";
import { align } from "@mdit/plugin-align";
import { spoiler } from "@mdit/plugin-spoiler";
import { sub } from "@mdit/plugin-sub";
import { sup } from "@mdit/plugin-sup";
import { ruby } from "@mdit/plugin-ruby";
import { demo } from "@mdit/plugin-demo";
import { dl } from "@mdit/plugin-dl";
import { stepper } from "../plugins/stepper";
import { tab } from "@mdit/plugin-tab";
import { mark } from "@mdit/plugin-mark";
import { ins } from "@mdit/plugin-ins";
import { customAlert } from "../plugins/custom-alert";
import { v_alert } from "../plugins/v-alert";
import { mdDemo } from "../plugins/demo";
import { carousels } from "../plugins/carousels";
import { iframes } from "../plugins/iframe";
import { card } from "../plugins/card";
import { groupIconMdPlugin } from "vitepress-plugin-group-icons";
import MagicMovePlugin from "../plugins/magic-move";
import { dialogPlugin } from "../plugins/dialog";
import ts from "typescript";

import fs from "fs";
import path from "path";

let magicMoveShiki: any = null;

export const markdown: MarkdownOptions = {
    math: true,
    theme: {
        light: "github-light",
        dark: "github-dark"
    },
    shikiSetup: async (shiki) => {
        // Pre-load common languages for Magic Move
        const commonLanguages = [
            'javascript', 'js', 'typescript', 'ts', 'java', 'python', 'py',
            'cpp', 'c', 'csharp', 'cs', 'php', 'ruby', 'go', 'rust', 'swift',
            'kotlin', 'scala', 'r', 'sql', 'html', 'css', 'scss', 'sass',
            'json', 'yaml', 'yml', 'xml', 'markdown', 'md', 'bash', 'sh',
            'powershell', 'docker', 'dockerfile', 'vue', 'react', 'jsx', 'tsx'
        ];
        
        for (const lang of commonLanguages) {
            try {
                if (!shiki.getLoadedLanguages().includes(lang)) {
                    await shiki.loadLanguage(lang as any);
                }
            } catch (error) {
                // Silently ignore languages that can't be loaded
            }
        }
        
        magicMoveShiki = shiki;
    },
    config: async (md) => {
        md.use(InlineLinkPreviewElementTransform);
        md.use(BiDirectionalLinks());
        md.use(groupIconMdPlugin);
        
        md.use(timeline);
        md.use(tabsMarkdownPlugin);
        md.use(dialogPlugin);

        md.use(mdFootnote);
        md.use(mdTaskLists);
        md.use(mdDeflist);
        md.use(mdAbbr);
        md.use(imgSize);
        md.use(align);
        md.use(spoiler);
        md.use(sub);
        md.use(sup);
        md.use(ruby);
        md.use(demo, mdDemo);
        md.use(dl);
        md.use(customAlert);
        md.use(v_alert);
        md.use(mark);
        md.use(ins);

        // Register tab-based plugins individually
        md.use(tab, stepper);
        md.use(tab, carousels);
        md.use(tab, iframes);

        // Non-tab plugins
        md.use(card);
        
        // Magic move plugin with shiki integration
        if (magicMoveShiki) {
            md.use(MagicMovePlugin, magicMoveShiki, {
                light: "github-light",
                dark: "github-dark"
            });
        }

        md.renderer.rules.heading_close = (tokens, idx, options, env, slf) => {
            let htmlResult = slf.renderToken(tokens, idx, options);
            if (tokens[idx].tag === "h1") htmlResult += `<ArticleMetadata />`;
            return htmlResult;
        };
    },
    image: {
        lazyLoading: true,
    },
};
