import { DefaultTheme, UserConfig } from "vitepress"
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { TDesignResolver } from 'unplugin-vue-components/resolvers';
import * as config from "./markdown-plugins"

export const commonConfig: UserConfig<DefaultTheme.Config> = {
    srcDir: "./docs",
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
    markdown : {...config.markdown},
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
}