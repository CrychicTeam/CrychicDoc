import { DefaultTheme, UserConfig } from "vitepress"
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { TDesignResolver } from 'unplugin-vue-components/resolvers';
import { groupIconVitePlugin, localIconLoader } from 'vitepress-plugin-group-icons'
import { 
  GitChangelog, 
  GitChangelogMarkdownSection, 
} from '@nolebase/vitepress-plugin-git-changelog/vite'
import * as config from "./markdown-plugins"

import { search as zhSearch } from './lang/zh'

function generateAvatarUrl(username: string) {
	return `https://github.com/${username}.png`
}

export const commonConfig: UserConfig<DefaultTheme.Config> = {
    srcDir: "./docs",
    themeConfig: {
        logo: {
          alt: 'CryChicDoc',
          light: '/logo.png',
          dark: '/logodark.png'
        },
        search: {
          provider: 'algolia',
          options: {
            appId: 'ATKJZ0G8V5',
            apiKey: 'f75b80326d9a5599254436f088bcb548',
            indexName: 'mihono',
            locales: {
              ...zhSearch
            }
          }
        },
        socialLinks: [
          { icon: 'github', link: 'https://github.com/CrychicTeam/CrychicDoc' },
          { icon: {
            svg: '<svg xmlns="http://www.w3.org/2000/svg" width="128" height="128" viewBox="0 0 24 24"><path fill="#c71d23" d="M11.984 0A12 12 0 0 0 0 12a12 12 0 0 0 12 12a12 12 0 0 0 12-12A12 12 0 0 0 12 0zm6.09 5.333c.328 0 .593.266.592.593v1.482a.594.594 0 0 1-.593.592H9.777c-.982 0-1.778.796-1.778 1.778v5.63c0 .327.266.592.593.592h5.63c.982 0 1.778-.796 1.778-1.778v-.296a.593.593 0 0 0-.592-.593h-4.15a.59.59 0 0 1-.592-.592v-1.482a.593.593 0 0 1 .593-.592h6.815c.327 0 .593.265.593.592v3.408a4 4 0 0 1-4 4H5.926a.593.593 0 0 1-.593-.593V9.778a4.444 4.444 0 0 1 4.445-4.444h8.296Z"/></svg>'
          }, link: 'https://gitee.com/CrychicTeam/CrychicDoc' },
          { icon: {
            svg: '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32" width="200" height="200"><path d="M0 0h32v32H0V0zm22.523 10.069c-1.248.345-2.463.625-3.15 1.833-.373.652-.572 1.563-.669 2.303l-.937-.198c-.017 1.613.355 6.901-.193 8.063-.766.216-1.33.158-2.102.027-.45-.353-.419-.539-.486-1.096-.306-2.523-.012-5.357-.022-7.926-.597 1.84-2.179 8.101-3.435 9.055-.7.125-1.396.223-2.103.299-.559 1.527-5.864 5.985-7.245 7.444.792.933 1.796 1.503 2.83 2.127H32V5.074c-.476-.9-1.045-1.723-1.627-2.558-2.73 2.002-5.482 5.095-7.85 7.553zM0 26.67V32h5.01c-1.033-.624-2.037-1.194-2.83-2.127-.897-.94-1.415-1.997-1.982-3.147L0 26.67zM0 0v5.163l.187.091C1.34 2.663 2.628 1.441 5 0H0zm26.983 0c1.23.77 2.3 1.553 3.39 2.516.582.835 1.15 1.657 1.627 2.558V0h-5.017zM0 5.163v21.508l.198.055C.51 19.616.353 12.37.187 5.254L0 5.164v-.001z" fill="#87C255"/><path d="M0 5.163l.187.091c.166 7.117.323 14.363.01 21.472L0 26.67V5.163z" fill-opacity=".988" fill="#87C255"/><path stroke="null" d="M13.325 10.175c1.467-.035 2.96.05 4.428.077l.014 3.755c-.017 1.613.355 6.901-.193 8.063-.766.216-1.33.158-2.102.027-.45-.353-.419-.539-.486-1.096-.306-2.523-.012-5.357-.022-7.926-.597 1.84-2.179 8.101-3.435 9.055-.7.125-1.396.223-2.103.299-1.945-2.51-2.61-6.475-3.906-9.394-.101 2.882.783 6.219-.016 9l-.507.234c-.735-.047-.93-.043-1.537-.51-1.13-2.164-.443-8.842-.45-11.558l4.298.017c1.12 2.677 2.084 5.421 3.087 8.144l2.93-8.187z" fill="#FEFEFE"/><path d="M22.498 10.044c2.368-2.458 5.12-5.551 7.85-7.553.582.835 1.15 1.657 1.627 2.558v26.926H4.985c-1.033-.624-2.037-1.194-2.83-2.127 1.382-1.46 6.687-5.918 7.246-7.444a30.15 30.15 0 002.103-.3c1.256-.953 2.838-7.213 3.435-9.054.01 2.57-.284 5.403.022 7.926.067.557.035.743.486 1.096.77.13 1.336.189 2.102-.027.548-1.162.176-6.45.193-8.063l.937.198c.097-.74.296-1.651.668-2.303.688-1.208 1.903-1.488 3.15-1.833zm4.43 21.93h5.047v-5.041c-1.335 2.404-2.518 3.81-5.046 5.042z" fill="#56B6D8"/><path d="M22.523 10.069c2.31.222 4.638.1 6.942-.136-.917.91-1.816 1.903-2.81 2.728-1.346-.021-3.545-.353-4.591.654-.536.515-.646 1.205-.654 1.913-.014 1.392.151 2.868.302 4.254 1.816.236 6.318-.167 7.499.62.112.697.086.814-.212 1.456-1.378 1.141-3.924.864-5.619.706-1.075-.1-2.72-.22-3.447-1.095-1.71-2.06-.392-4.724-1.229-6.964.097-.74.296-1.651.668-2.303.688-1.208 1.903-1.488 3.15-1.833h.001z" fill="#FEFEFE"/><path d="M10.31 23.491l-2.117-2.55" opacity="NaN" fill="#56B6D8"/></svg>'
          }, link: 'https://www.mcmod.cn/author/32860.html' }
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
        optimizeDeps: {
          exclude: [ 
            '@nolebase/*'
          ], 
        },
        ssr: {
        noExternal: [
          'vuetify',
          '@nolebase/*',
        ],
        },
        plugins: [
        GitChangelog({ 
          repoURL: () => 'https://github.com/CrychicTeam/CrychicDoc',
          mapAuthors: [ 
            { 
              name: 'M1hono',  // The name you want to display
              username: 'M1hono', // The username of the author which is used to summon github's link. (won't work with links options)
              mapByNameAliases: ['CrychicTeam', 'M1hono', 'm1hono' , 'Guda chen','Customer service is currently offline.'], // Add the name you want to map, these names will be replaced with the name.
              avatar: generateAvatarUrl('M1hono') // The avatar of the author, normally it's the github avatar
              // links: "https://gitee.com/CrychicTeam/CrychicDoc" Change to the url You want to link to
            },
            { 
              name: 'skyraah', 
              username: 'skyraah',
              mapByNameAliases: ['cyciling','skyraah'],
              avatar: generateAvatarUrl('skyraah')
            },
            { 
              name: 'Eikidona', 
              username: 'Eikidona',
              mapByNameAliases: ['Nagasaki Soyo','Eikidona'],
              avatar: generateAvatarUrl('Eikidona')
            }
          ]
        }), 
        GitChangelogMarkdownSection(), 
        groupIconVitePlugin({ 
            customIcon: {
              mcmeta: localIconLoader(import.meta.url, '../../docs/public/svg/minecraft.svg'),
              json: localIconLoader(import.meta.url, '../../docs/public/svg/json.svg'),
              md: localIconLoader(import.meta.url, '../../docs/public/svg/markdown.svg'),
              kubejs: localIconLoader(import.meta.url, '../../docs/public/svg/kubejs.svg'),
              js: localIconLoader(import.meta.url, '../../docs/public/svg/Javascript.svg'),
              ts: 'logos:typescript-icon-round',
              java: 'logos:java',
              css: 'logos:css-3',
              git: 'logos:git-icon',
            }}),
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
    head: [['link', { rel: 'icon', href: 'https://docs.mihono.cn/favicon.ico' }],
    ['meta', { property: 'og:type', content: 'website' }],
    ['meta', { property: 'og:locale', content: 'zh' }],
    ['meta', { property: 'og:title', content: 'CrychicDoc | 一个由Crychic团队维护的我的世界文档' }],
    ['meta', { property: 'og:site_name', content: 'CrychicDoc' }],
    ['meta', { property: 'og:image', content: 'https://docs.mihono.cn/logo.png' }],
    ['meta', { property: 'og:url', content: 'https://docs.mihono.cn/' }],],
    ignoreDeadLinks: true,
    transformHead({ assets }) {
      // 相应地调整正则表达式以匹配字体
      const JetBrainsMono = assets.find(file => /JetBrainsMono-Light\.\w+\.woff2/)
      if (JetBrainsMono) {
        return [
          [
            'link',
            {
              rel: 'preload',
              href: JetBrainsMono,
              as: 'font',
              type: 'font/woff2',
              crossorigin: ''
            }
          ]
        ]
      }
    }
}