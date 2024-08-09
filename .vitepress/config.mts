import { defineConfig } from 'vitepress'
import timeline from "vitepress-markdown-timeline";
import { tabsMarkdownPlugin } from 'vitepress-plugin-tabs'
import { withMermaid } from "vitepress-plugin-mermaid";
import { transformerTwoslash } from '@shikijs/vitepress-twoslash'
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { TDesignResolver } from 'unplugin-vue-components/resolvers';
import { generateSidebar } from './theme/generatesidebar';

export default withMermaid(
  defineConfig({
    srcDir: "./docs",
    locales: {
      root: {
        label: '简体中文',
        lang: 'zh-CN',
        link: '/zh/',
        title: "CryChic文档",
        description: "一个包含 Minecraft 开发文档的网站。",
        themeConfig: {
          nav: [],
          sidebar: {
            '/zh/': generateSidebar('../../docs/zh', '','zh'),
            '/zh/developers/': generateSidebar('../../docs/zh', 'developers','zh'),
            '/zh/doc/': generateSidebar('../../docs/zh', 'doc','zh'),
            '/zh/modpack/': generateSidebar('../../docs/zh', 'modpack','zh'),
            '/zh/mods/': generateSidebar('../../docs/zh', 'mods','zh'),
            '/zh/mods/adventure/Champions-Unofficial': generateSidebar('../../docs/zh', 'mods/adventure/Champions-Unofficial','zh')
          },
          editLink: {
            pattern: 'https://github.com/M1hono/CryChicDoc/blob/main/docs/:path?plain=1',
            text: '在 GitHub 上查看此页面'
          },
          lastUpdated: {
            text: '最后更新于'
          },
          outline: {
            label: '页面导航'
          },
          docFooter: {
            prev: '上一页',
            next: '下一页'
          },
          footer: {
            message: '基于 MIT 证书发布-晋ICP备2022005790号-2',
            copyright: `版权所有 © 2024-${new Date().getFullYear()} 不是客服`
          },
          langMenuLabel: '改变语言',
          darkModeSwitchLabel: '切换主题',
          lightModeSwitchTitle: '切换到浅色模式',
          darkModeSwitchTitle: '切换到深色模式'
        }
      },
      en: {
        label: 'English',
        lang: 'en-US',
        link: '/en/',
        title: "CryChicDoc",
        description: "A site containing docs for Minecraft developing.",
        themeConfig: {
          nav: [],
          sidebar: {
            '/en/': generateSidebar('../../docs/en', '','en'),
            '/en/developers/': generateSidebar('../../docs/en', 'developers','en'),
            '/en/doc/': generateSidebar('../../docs/en', 'doc','en'),
            '/en/modpack/': generateSidebar('../../docs/en', 'modpack','en'),
            '/en/mods/': generateSidebar('../../docs/en', 'mods','en'),
            '/en/mods/adventure/Champions-Unofficial': generateSidebar('../../docs/en', 'mods/adventure/Champions-Unofficial','en')
          },
          editLink: {
            pattern: 'https://github.com/M1hono/CryChicDoc/blob/main/docs/:path?plain=1',
            text: 'Check this page on GitHub'
          },
          lastUpdated: {
            text: 'Last updated'
          },
          outline: {
            label: 'Page Content'
          },
          docFooter: {
            prev: 'Previous Page', 
            next: 'Next Page'
          },
          footer: {
            message: 'Licensed under MIT-晋ICP备2022005790号-2',
            copyright: `Copyright © 2024-${new Date().getFullYear()} M1hono`
          },
          langMenuLabel: 'Change Language',
          darkModeSwitchLabel: 'Switch Theme',
          lightModeSwitchTitle: 'Switch to light mode',
          darkModeSwitchTitle: 'Switch to dark mode'
        }
      }
    },
    themeConfig: {
      logo: {
        alt: 'CryChicDoc',
        light: '/logo.png',
        dark: '/logodark.png'
      },
      search: {
        provider: 'local'
      },
      socialLinks: [
        { icon: 'github', link: 'https://github.com/M1hono/CrychicDoc' }
      ],
      langMenuLabel: 'Change Language',
      lastUpdated: {},
      // 添加以下配置来启用多语言支持
      locales: {
        'root': { label: '简体中文', lang: 'zh-CN' },
        'en-US': { label: 'English', lang: 'en-US' }
      }
    },
    markdown: {
      math: true,
      config: async (md) => {
        md.use(timeline);
        md.use(tabsMarkdownPlugin);

        const taskLists = await import('markdown-it-task-lists').then(m => m.default || m);
        const markdownItFootnote = await import('markdown-it-footnote').then(m => m.default || m);
        const markdownItDeflist  = await import('markdown-it-deflist').then(m => m.default || m);
        const markdownItAbbr   = await import('markdown-it-abbr').then(m => m.default || m);
        const { imgSize } = await import("@mdit/plugin-img-size").then(m => m.default || m);
        const { align } = await import("@mdit/plugin-align").then(m => m.default || m);
        const { spoiler } = await import("@mdit/plugin-spoiler").then(m => m.default || m);
        const { sub } = await import("@mdit/plugin-sub").then(m => m.default || m);
        const { sup } = await import("@mdit/plugin-sup").then(m => m.default || m);
        const { ruby } = await import("@mdit/plugin-ruby").then(m => m.default || m);
        const { demo } = await import("@mdit/plugin-demo").then(m => m.default || m);
        
        md.use(align)
        md.use(spoiler)
        md.use(sub)
        md.use(sup)
        md.use(ruby)
        md.use(demo)
        md.use(imgSize);
        md.use(markdownItDeflist);
        md.use(markdownItAbbr);
        md.use(taskLists);
        md.use(markdownItFootnote);
      },
      codeTransformers: [
        transformerTwoslash() 
      ],
      image: {
        lazyLoading: true
      }
    },
    cleanUrls: true,
    mermaid: {
      startOnLoad: true,
      securityLevel: 'loose',
      theme: 'default'
    },
    vite: {
      ssr: {
        noExternal: ['vuetify'],
      },
      plugins: [
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
      define: {
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: true,
      },
    },
    head: [['link', { rel: 'icon', href: 'https://docs.mihono.cn/favicon.ico' }]]
  })
);