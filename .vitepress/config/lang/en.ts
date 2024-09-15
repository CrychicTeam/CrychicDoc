import { DefaultTheme } from "vitepress";
import { sidebars } from "../sidebarControl";

export const en_US = <DefaultTheme.Config>{
    lang: "en-US",
    link: "/en/",
    title: "CryChicDoc",
    description: "A site containing docs for Minecraft developing.",
    themeConfig: {
        nav: [
            {
                text: "KubeJS",
                items: [
                    {
                        text: "Index",
                        link: "/en/modpack/kubejs/",
                    },
                    {
                        text: "Docs",
                        items: [
                            {
                                text: "1.21-Planning",
                                link: "...",
                            },
                            {
                                text: "1.20.1",
                                link: "/en/modpack/kubejs/1.20.1/",
                                activeMatch: "/en/modpack/kubejs/1.20.1/",
                            },
                            {
                                text: "1.19.2-Planning",
                                link: "...",
                            },
                            {
                                text: "1.18.2-Planning",
                                link: "...",
                            },
                        ],
                    },
                    {
                        text: "Third Party Docs",
                        items: [
                            {
                                text: "gumeng",
                                link: "/en/modpack/kubejs/1.20.1/KubeJSCourse/README",
                                activeMatch: "/en/modpack/kubejs/1.20.1/",
                            },
                            {
                                text: "Wudji-1.19.2",
                                link: "en/modpack/kubejs/1.19.2/XPlusKubeJSTutorial/README",
                            },
                            {
                                text: "Wudji-1.18.2",
                                link: "en/modpack/kubejs/1.18.2/XPlusKubeJSTutorial/README",
                            },
                        ],
                    },
                ],
            },
            { text: "Guide", link: "/en/doc/guide" },
        ],
        sidebar: sidebars("en"),
        outline: {
            level: "deep",
            label: "Page Content",
        },
        docFooter: {
            prev: "Previous Page",
            next: "Next Page",
        },
        langMenuLabel: "Change Language",
        darkModeSwitchLabel: "Switch Theme",
        lightModeSwitchTitle: "Switch to light mode",
        darkModeSwitchTitle: "Switch to dark mode",
    },
};
