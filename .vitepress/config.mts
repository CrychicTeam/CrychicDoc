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
import { stepper} from './utils/stepper';
import { tab } from "@mdit/plugin-tab";

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
        { icon: 'github', link: 'https://github.com/CrychicTeam/CrychicDoc' },
        { icon: {
          svg: '<svg t="1725435852282" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5271" width="200" height="200"><path d="M512 1000.12c-268.466 0-488.12-219.654-488.12-488.12S243.533 23.88 512 23.88 1000.12 243.533 1000.12 512 780.467 1000.12 512 1000.12z m247.111-543.034H481.492c-12.203 0-24.406 12.203-24.406 24.406v61.016c0 12.203 12.203 24.406 24.406 24.406h167.792c12.203 0 24.406 12.203 24.406 24.406v12.203c0 39.66-33.558 73.218-73.218 73.218H371.665c-12.203 0-24.406-12.203-24.406-24.406V420.477c0-39.66 33.559-73.218 73.218-73.218h338.634c12.203 0 24.406-12.203 24.406-24.406v-61.015c0-12.203-12.203-24.406-24.406-24.406H420.477c-100.675 0-179.994 82.37-179.994 179.995V756.06c0 12.203 12.203 24.406 24.406 24.406h356.938c88.472 0 161.69-73.218 161.69-161.69V481.492c0-12.203-12.203-24.406-24.406-24.406z" fill="#C71D23" p-id="5272"></path></svg>'
        }, link: 'https://gitee.com/CrychicTeam/CrychicDoc' }
      ],
      langMenuLabel: 'Change Language',
      lastUpdated: {},
      // 添加以下配置来启用多语言支持
      //@ts-ignore
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
        //md.use(container, stepper);
        //md.use(container, template);
        md.use(tab, stepper)
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