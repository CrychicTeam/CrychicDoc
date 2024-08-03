import { defineConfig } from 'vitepress'
import timeline from "vitepress-markdown-timeline";
import { tabsMarkdownPlugin } from 'vitepress-plugin-tabs'
import { withMermaid } from "vitepress-plugin-mermaid";
import { transformerTwoslash } from '@shikijs/vitepress-twoslash'

export default withMermaid({
  srcDir : "./docs",
  lang : "zh-CN",
  locales: {
    root: {
      label: '简体中文',
      lang: 'zh-CN',
      link: '/zh/'
    },
    en: {
      label: 'English',
      lang: 'en_US',
      link: '/en/'
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
    math: true,
    config: (md) => {
      md.use(timeline);
      md.use(tabsMarkdownPlugin);
    },
    codeTransformers: [
      transformerTwoslash() 
    ]
  },
  cleanUrls: true,
  mermaid: {},
  vite: {}
})
