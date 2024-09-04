import { defineConfig } from 'vitepress'
import timeline from "vitepress-markdown-timeline";
import { tabsMarkdownPlugin } from 'vitepress-plugin-tabs'
import { withMermaid } from "vitepress-plugin-mermaid";
import { transformerTwoslash } from '@shikijs/vitepress-twoslash'
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { TDesignResolver } from 'unplugin-vue-components/resolvers';
import sidebars from './index';

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
          sidebar: sidebars("zh"),
          editLink: {
            pattern: 'https://github.com/CrychicTeam/CrychicDoc/blob/main/docs/:path?plain=1',
            text: '在 GitHub 上查看此页面'
          },
          lastUpdated: {
            text: '最后更新于'
          },
          outline: {
            level: "deep",
            label: '页面导航'
          },
          docFooter: {
            prev: '上一页',
            next: '下一页'
          },
          langMenuLabel: '改变语言',
          darkModeSwitchLabel: '切换主题',
          lightModeSwitchTitle: '切换到浅色模式',
          darkModeSwitchTitle: '切换到深色模式'
        },
      },
      en: {
        label: 'English',
        lang: 'en-US',
        link: '/en/',
        title: "CryChicDoc",
        description: "A site containing docs for Minecraft developing.",
        themeConfig: {
          nav: [],
          sidebar: sidebars("en"),
          editLink: {
            pattern: 'https://github.com/CrychicTeam/CrychicDoc/blob/main/docs/:path?plain=1',
            text: 'Check this page on GitHub'
          },
          lastUpdated: {
            text: 'Last updated'
          },
          outline: {
            level: "deep",
            label: 'Page Content'
          },
          docFooter: {
            prev: 'Previous Page', 
            next: 'Next Page'
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
        { icon: 'github', link: 'https://github.com/CrychicTeam/CrychicDoc' }
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
    head: [['link', { rel: 'icon', href: 'https://docs.mihono.cn/favicon.ico' }]],
    ignoreDeadLinks: true
  })
);