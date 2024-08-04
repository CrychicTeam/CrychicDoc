import { defineConfig } from 'vitepress'
import timeline from "vitepress-markdown-timeline";
import { tabsMarkdownPlugin } from 'vitepress-plugin-tabs'
import { withMermaid } from "vitepress-plugin-mermaid";
import { transformerTwoslash } from '@shikijs/vitepress-twoslash'
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { TDesignResolver } from 'unplugin-vue-components/resolvers';

export default withMermaid(
  defineConfig({
    srcDir: "./docs",
    locales: {
      root: {
        label: '简体中文',
        lang: 'zh-CN',
        link: '/zh/',
        title: "CryChicDoc - 中文",
        description: "一个包含 Minecraft 开发文档的网站。",
        themeConfig: {
          nav: [
            // 添加中文导航项
          ],
          sidebar: [
            // 添加中文侧边栏项
          ],
          editLink: {
            pattern: 'https://github.com/M1hono/CrychicDoc/edit/main/docs/:path',
            text: '在 GitHub 上编辑此页面'
          },
          lastUpdated: {
            text: '最后更新于'
          }
        }
      },
      en: {
        label: 'English',
        lang: 'en_US',
        link: '/en/',
        title: "CryChicDoc - English",
        description: "A site containing docs for Minecraft developing.",
        themeConfig: {
          nav: [
            // Add English navigation items
          ],
          sidebar: [
            // Add English sidebar items
          ],
          editLink: {
            pattern: 'https://github.com/M1hono/CrychicDoc/edit/main/docs/:path',
            text: 'Edit this page on GitHub'
          },
          lastUpdated: {
            text: 'Last updated'
          }
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
      footer: {
        message: '',
        copyright: 'Copyright © 2024-present M1hono	晋ICP备2022005790号-2'
      },
      lastUpdated: {}
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
    },
    head: [['link', { rel: 'icon', href: 'http://docs.mihono.cn/favicon.ico' }]]
  })
)