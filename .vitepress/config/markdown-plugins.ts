import { MarkdownOptions } from "vitepress"

import timeline from "vitepress-markdown-timeline";
import { tabsMarkdownPlugin } from 'vitepress-plugin-tabs'
import { transformerTwoslash } from '@shikijs/vitepress-twoslash'
import mdFootnote from 'markdown-it-footnote'
import mdTaskLists from 'markdown-it-task-lists'
import mdDeflist from 'markdown-it-deflist'
import mdAbbr from 'markdown-it-abbr'
import { imgSize } from "@mdit/plugin-img-size"
import { align } from "@mdit/plugin-align"
import { spoiler } from "@mdit/plugin-spoiler"
import { sub } from "@mdit/plugin-sub"
import { sup } from "@mdit/plugin-sup"
import { ruby } from "@mdit/plugin-ruby"
import { demo } from "@mdit/plugin-demo"
import { dl } from "@mdit/plugin-dl";
import { stepper} from '../plugins/stepper';
import { tab } from "@mdit/plugin-tab";
import { mark } from "@mdit/plugin-mark";
import { ins } from "@mdit/plugin-ins";
import { v_alert } from "../plugins/v-alert"
import fs from 'fs';
import path from 'path'

export const markdown: MarkdownOptions = {
    math: true,
      lineNumbers: true,
      config: async (md) => {
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
        md.use(demo);
        md.use(dl);
        md.use(v_alert);
        md.use(mark);
        md.use(ins);
        //md.use(container, stepper);
        //md.use(container, template);
        md.use(tab, stepper)
        // const test = md.render(fs.readFileSync(path.join("docs","zh","modpack","kubejs","KubejsCourse","KubejsBasic","FileStructure.md")).toString())
        // fs.writeFileSync('output.html', test);
        md.renderer.rules.heading_close = (tokens, idx, options, env, slf) => {
          let htmlResult = slf.renderToken(tokens, idx, options);
          if (tokens[idx].tag === 'h1') htmlResult += `<ArticleMetadata />`; 
          return htmlResult;
        }
      },
      codeTransformers: [
        transformerTwoslash() 
      ],
      image: {
        lazyLoading: true
      }
}