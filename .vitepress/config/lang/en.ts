import { DefaultTheme } from "vitepress"
import {sidebars} from '../sidebarControl'

export const en_US = <DefaultTheme.Config>({
    lang: 'en-US',
    link: '/en/',
    title: "CryChicDoc",
    description: "A site containing docs for Minecraft developing.",
    themeConfig: {
        nav: [
            { text: 'Guide', link: '/en/doc/guide' }
            ],
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
})