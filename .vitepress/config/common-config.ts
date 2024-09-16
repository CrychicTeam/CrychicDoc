import { DefaultTheme, UserConfig, HeadConfig, Awaitable } from "vitepress";
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import { TDesignResolver } from "unplugin-vue-components/resolvers";
import {
    groupIconVitePlugin,
    localIconLoader,
} from "vitepress-plugin-group-icons";
import {
    GitChangelog,
    GitChangelogMarkdownSection,
} from "@nolebase/vitepress-plugin-git-changelog/vite";
import * as config from "./markdown-plugins";

import { search as zhSearch } from "./lang/zh";

function generateAvatarUrl(username: string) {
    return `https://github.com/${username}.png`;
}

export const commonConfig: UserConfig<DefaultTheme.Config> = {
    srcDir: "./docs",
    themeConfig: {
        logo: {
            alt: "CryChicDoc",
            light: "/logo.png",
            dark: "/logodark.png",
        },
        search: {
            provider: "algolia",
            options: {
                appId: "ATKJZ0G8V5",
                apiKey: "f75b80326d9a5599254436f088bcb548",
                indexName: "mihono",
                locales: {
                    ...zhSearch,
                },
            },
        },
        socialLinks: [
            {
                icon: "github",
                link: "https://github.com/CrychicTeam/CrychicDoc",
            },
            {
                icon: {
                    svg: '<svg xmlns="http://www.w3.org/2000/svg" width="128" height="128" viewBox="0 0 24 24"><path fill="#c71d23" d="M11.984 0A12 12 0 0 0 0 12a12 12 0 0 0 12 12a12 12 0 0 0 12-12A12 12 0 0 0 12 0zm6.09 5.333c.328 0 .593.266.592.593v1.482a.594.594 0 0 1-.593.592H9.777c-.982 0-1.778.796-1.778 1.778v5.63c0 .327.266.592.593.592h5.63c.982 0 1.778-.796 1.778-1.778v-.296a.593.593 0 0 0-.592-.593h-4.15a.59.59 0 0 1-.592-.592v-1.482a.593.593 0 0 1 .593-.592h6.815c.327 0 .593.265.593.592v3.408a4 4 0 0 1-4 4H5.926a.593.593 0 0 1-.593-.593V9.778a4.444 4.444 0 0 1 4.445-4.444h8.296Z"/></svg>',
                },
                link: "https://gitee.com/CrychicTeam/CrychicDoc",
            },
            {
                icon: {
                    svg: '<svg width="128" height="128" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet"><g><path id="svg_1" d="m0.22811,8.42244l0,-2.67626c0,-0.13022 0.00485,-0.25928 0.01372,-0.38725l-0.01372,3.06353l0,0l0,-0.00002zm22.13572,-6.35785c0.87539,0.97663 1.40807,2.26682 1.40807,3.68159l0,12.50765c0,3.04754 -2.47054,5.51808 -5.51808,5.51808l-12.50765,0c-1.52088,0 -2.89798,-0.61536 -3.89611,-1.61059l20.51375,-20.09673l0,0l0.00002,0z" fill="rgb(88, 182, 216)" fill-rule="evenodd" stroke="null"/><path id="svg_2" d="m1.88786,22.19821c-1.02398,-1.00178 -1.65975,-2.39874 -1.65975,-3.94439l0,-12.50765c0,-3.04754 2.47054,-5.51808 5.51808,-5.51808l12.50765,0c1.66068,0 3.14985,0.7337 4.16147,1.89447l-20.52744,20.07565l-0.00001,0z" fill="rgb(134, 193, 85)" fill-rule="evenodd" stroke="null"/><path id="svg_3" d="m19.6569,9.39041l-2.886,0c-0.94354,0.19393 -0.81466,1.06567 -0.81466,1.06567l0,3.24521c0.10339,0.93088 1.00853,0.79334 1.00853,0.79334l4.57694,0l0,1.90834l-5.01086,0c-1.95265,-0.10849 -2.36748,-1.44849 -2.36748,-1.44849c-0.19389,-0.43958 -0.1609,-0.87369 -0.1609,-0.87369l0,-3.56376c0.01292,-2.52116 1.7239,-2.874 1.7239,-2.874c0.29728,-0.10345 1.24123,-0.13795 1.24123,-0.13795l4.62009,0l-1.93077,1.88535l0,0l-0.00002,-0.00002zm-8.4846,0.36788l-2.29919,6.5757l-2.09227,0l-2.43714,-6.5757l-0.02299,6.55271l-1.90834,0l0,-8.80594l3.10391,0l2.25321,6.02391l2.23022,-6.02391l3.17291,0l0,8.85193l-2.00031,0l0,-6.59869l0,0l-0.00001,-0.00001z" fill="rgb(255, 255, 255)" fill-rule="evenodd" stroke="null"/></svg>',
                },
                link: "https://www.mcmod.cn/author/32860.html",
            },
        ],
        langMenuLabel: "Change Language",
        lastUpdated: {},
        // 添加以下配置来启用多语言支持
        //@ts-ignore
        locales: {
            root: { label: "简体中文", lang: "zh-CN" },
            "en-US": { label: "English", lang: "en-US" },
        },
    },
    markdown: { ...config.markdown },
    cleanUrls: true,
    mermaid: {
        startOnLoad: true,
        securityLevel: "loose",
        theme: "default",
    },
    vite: {
        optimizeDeps: {
            exclude: ["@nolebase/*"],
        },
        ssr: {
            noExternal: ["vuetify", "@nolebase/*"],
        },
        plugins: [
            GitChangelog({
                repoURL: () => "https://github.com/CrychicTeam/CrychicDoc",
                mapAuthors: [
                    {
                        name: "M1hono", // The name you want to display
                        username: "M1hono", // The username of the author which is used to summon github's link. (won't work with links options)
                        mapByNameAliases: [
                            "CrychicTeam",
                            "M1hono",
                            "m1hono",
                            "Guda chen",
                            "Customer service is currently offline.",
                        ], // Add the name you want to map, these names will be replaced with the name.
                        avatar: generateAvatarUrl("M1hono"), // The avatar of the author, normally it's the github avatar
                        // links: "https://gitee.com/CrychicTeam/CrychicDoc" Change to the url You want to link to
                    },
                    {
                        name: "skyraah",
                        username: "skyraah",
                        mapByNameAliases: ["cyciling", "skyraah"],
                        avatar: generateAvatarUrl("skyraah"),
                    },
                    {
                        name: "Eikidona",
                        username: "Eikidona",
                        mapByNameAliases: ["Nagasaki Soyo", "Eikidona"],
                        avatar: generateAvatarUrl("Eikidona"),
                    },
                ],
            }),
            GitChangelogMarkdownSection(),
            groupIconVitePlugin({
                customIcon: {
                    mcmeta: localIconLoader(
                        import.meta.url,
                        "../../docs/public/svg/minecraft.svg"
                    ),
                    json: localIconLoader(
                        import.meta.url,
                        "../../docs/public/svg/json.svg"
                    ),
                    md: localIconLoader(
                        import.meta.url,
                        "../../docs/public/svg/markdown.svg"
                    ),
                    kubejs: localIconLoader(
                        import.meta.url,
                        "../../docs/public/svg/kubejs.svg"
                    ),
                    js: localIconLoader(
                        import.meta.url,
                        "../../docs/public/svg/Javascript.svg"
                    ),
                    sh: localIconLoader(
                        import.meta.url,
                        "../../docs/public/svg/powershell.svg"
                    ),
                    npm: localIconLoader(
                        import.meta.url,
                        "../../docs/public/svg/npm.svg"
                    ),
                    ts: "logos:typescript-icon-round",
                    java: "logos:java",
                    css: "logos:css-3",
                    git: "logos:git-icon",
                },
            }),
            AutoImport({
                resolvers: [
                    TDesignResolver({
                        library: "vue-next",
                    }),
                ],
            }),
            Components({
                resolvers: [
                    TDesignResolver({
                        library: "vue-next",
                    }),
                ],
            }),
        ],
        // define: {
        // __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: true,
        // },
    },
    head: [
        ["link", { rel: "icon", href: "https://docs.mihono.cn/favicon.ico" }],
    ],
    ignoreDeadLinks: true,
    transformHead({ assets }) {
        const fonts = (): string[] => {
            return [
                assets.find((file) => /JetBrainsMono-Regular\.\w+\.woff2/),
                assets.find((file) => /ChillRoundGothic_Bold\.\w+\.woff2/),
                assets.find((file) => /ChillRoundGothic_ExtraLight\.\w+\.woff2/),
                assets.find((file) => /ChillRoundGothic_Heavy\.\w+\.woff2/),
                assets.find((file) => /ChillRoundGothic_Light\.\w+\.woff2/),
                assets.find((file) => /ChillRoundGothic_Medium\.\w+\.woff2/),
                assets.find((file) => /ChillRoundGothic_Regular\.\w+\.woff2/),
            ].filter((value): value is string => value !== undefined);
        };
        const fontConfig = (): HeadConfig[] => {
            return fonts().map((font) => [
                "link",
                {
                    rel: "preload",
                    href: font,
                    as: "font",
                    type: "font/woff2",
                    crossorigin: "",
                },
            ]);
        };
        return fontConfig();
    },
};
