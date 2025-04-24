//@ts-nocheck
import { argv, cwd, env } from 'node:process'

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
import { v_alert } from "../plugins/v-alert";
import { mdDemo } from "../plugins/demo";
import { carousels } from "../plugins/carousels";
import { iframes } from "../plugins/iframe";
import { card } from "../plugins/card";
import { groupIconMdPlugin } from "vitepress-plugin-group-icons";
import ts from 'typescript';

import fs from "fs";
import path from "path";
export const markdown: MarkdownOptions = {
    math: true,
    config: async (md) => {
        md.use(InlineLinkPreviewElementTransform);
        md.use(BiDirectionalLinks());
        md.use(groupIconMdPlugin);
        md.use(timeline);
        md.use(tabsMarkdownPlugin);

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
        md.use(v_alert);
        md.use(mark);
        md.use(ins);
        //md.use(container, stepper);
        //md.use(container, template);
        md.use(tab, stepper);
        md.use(carousels);
        md.use(iframes);
        md.use(card);
        // const test = md.render("::: carousels#{\"test\": 123}\n123546\n@tab https://docs.mihono.cn/mods/adventure/champions-unofficial/1.png\n\n@tab https://docs.mihono.cn/mods/adventure/champions-unofficial/2.png\n\n:::\n", {})
        // const test = md.render(fs.readFileSync(path.join("docs","zh","modpack","kubejs","KubeJSCourse","KubeJSBasic","FileStructure.md")).toString())
        // fs.writeFileSync('output.html', test);
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
