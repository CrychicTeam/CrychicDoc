import { DefaultTheme } from "vitepress";
import { sidebars } from "../sidebarControl";

export const zh_CN = <DefaultTheme.Config>{
    lang: "zh-CN",
    link: "/zh/",
    title: "CryChic文档",
    description: "一个包含 Minecraft 开发文档的网站。",
    themeConfig: {
        nav: [
            {
                text: "Kubejs",
                items: [
                    {
                        text: "主页",
                        link: "/zh/modpack/kubejs/",
                    },
                    {
                    text: "文档",
                    items: [
                        {
                            text: "1.21-计划中",
                            link: "...",
                        },
                        {
                            text: "1.20.1",
                            link: "/zh/modpack/kubejs/1.20.1/",
                            activeMatch: "/zh/modpack/kubejs/1.20.1/",
                        },
                        {
                            text: "1.19.2-计划中",
                            link: "...",
                        },
                        {
                            text: "1.18.2-计划中",
                            link: "...",
                        },
                    ]
                },
                    {
                        text: "第三方文档",
                        items: [
                            {
                                text: "孤梦",
                                link: "/zh/modpack/kubejs/1.20.1/KubejsCourse/README",
                                activeMatch: "/zh/modpack/kubejs/1.20.1/",
                            },
                            {
                                text: "Wudji-1.19.2",
                                link: "zh/modpack/kubejs/1.19.2/XPlusKubeJSTutorial/README",
                            },
                            {
                                text: "Wudji-1.18.2",
                                link: "zh/modpack/kubejs/1.18.2/XPlusKubeJSTutorial/README",
                            },
                        ]
                    },
                ]
            },
            { text: "导航", link: "/zh/doc/guide" },
        ],
        sidebar: sidebars("zh"),
        outline: {
            level: "deep",
            label: "页面导航",
        },
        docFooter: {
            prev: "上一页",
            next: "下一页",
        },

        langMenuLabel: "切换语言",
        returnToTopLabel: "回到顶部",
        sidebarMenuLabel: "菜单",
        darkModeSwitchLabel: "主题",
        lightModeSwitchTitle: "切换到浅色模式",
        darkModeSwitchTitle: "切换到深色模式",
    },
};

export const search: DefaultTheme.AlgoliaSearchOptions["locales"] = {
    root: {
        placeholder: "搜索文档",
        translations: {
            button: {
                buttonText: "搜索文档",
                buttonAriaLabel: "搜索文档",
            },
            modal: {
                searchBox: {
                    resetButtonTitle: "清除查询条件",
                    resetButtonAriaLabel: "清除查询条件",
                    cancelButtonText: "取消",
                    cancelButtonAriaLabel: "取消",
                },
                startScreen: {
                    recentSearchesTitle: "搜索历史",
                    noRecentSearchesText: "没有搜索历史",
                    saveRecentSearchButtonTitle: "保存至搜索历史",
                    removeRecentSearchButtonTitle: "从搜索历史中移除",
                    favoriteSearchesTitle: "收藏",
                    removeFavoriteSearchButtonTitle: "从收藏中移除",
                },
                errorScreen: {
                    titleText: "无法获取结果",
                    helpText: "你可能需要检查你的网络连接",
                },
                footer: {
                    selectText: "选择",
                    navigateText: "切换",
                    closeText: "关闭",
                    searchByText: "搜索提供者",
                },
                noResultsScreen: {
                    noResultsText: "无法找到相关结果",
                    suggestedQueryText: "你可以尝试查询",
                    reportMissingResultsText: "你认为该查询应该有结果？",
                    reportMissingResultsLinkText: "点击反馈",
                },
            },
        },
    },
};
