import { defineConfig } from 'vitepress'
import timeline from "vitepress-markdown-timeline";
import { tabsMarkdownPlugin } from 'vitepress-plugin-tabs'
import { withMermaid } from "vitepress-plugin-mermaid";

export default withMermaid({
  srcDir : "./docs",
  lang : "zh-CN",
  locales: {
    root: {
      label: '简体中文',
      lang: 'zh-CN'
    },
    en: {
      label: 'English',
      lang: 'en'
    }
  },
  title: "CryChicDoc",
  description: "A site contained docs for minecraft developing.",
  themeConfig: {
    nav: [
      // { text: 'Home', link: '/' }
    ],
    sidebar: [
      {
        // text: 'Examples',
        // items: [
        //   { text: 'Markdown Examples', link: '/markdown-examples' },
        //   { text: 'Runtime API Examples', link: '/api-examples' }
        // ]
      }
    ],
    search: {
      provider: 'local'
    },
    socialLinks: [
      { icon: 'github', link: 'https://github.com/vuejs/vitepress' }
    ],
  },
  markdown: {
    config: (md) => {
      md.use(timeline);
      md.use(tabsMarkdownPlugin);
    },
  },
  mermaid: {}
})
