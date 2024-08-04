import { defineConfig } from 'vitepress'
import timeline from "vitepress-markdown-timeline";
import { tabsMarkdownPlugin } from 'vitepress-plugin-tabs'
import { withMermaid } from "vitepress-plugin-mermaid";
import { transformerTwoslash } from '@shikijs/vitepress-twoslash'
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { TDesignResolver } from 'unplugin-vue-components/resolvers';

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
    ],
    image: {
      lazyLoading: true
    }
  },
  cleanUrls: true,
  mermaid: {},
  vite: {
    ssr: {
      noExternal: ['vuetify'],
    },
    plugins: [
      // ...
      AutoImport({
        resolvers: [TDesignResolver({
          library: 'vue-next'
        })],
      }),
      Components({
        resolvers: [TDesignResolver({
          library: 'vue-next'
        })],
      }),
    ],
  }
})
