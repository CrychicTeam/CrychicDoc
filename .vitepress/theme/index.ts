//@ts-nocheck
import { h } from "vue";
import type { Theme } from "vitepress";
import DefaultTheme from "vitepress/theme";
import vitepressNprogress from "vitepress-plugin-nprogress";
import "vitepress-plugin-nprogress/lib/css/index.css";
import "vitepress-markdown-timeline/dist/theme/index.css";
import { useData, useRoute } from "vitepress";
import "./style.css";
import { enhanceAppWithTabs } from "vitepress-plugin-tabs/client";
import TwoslashFloatingVue from "@shikijs/vitepress-twoslash/client";
import "@shikijs/vitepress-twoslash/style.css";
import imageViewer from "./components/imageViewer.vue";
import vuetify from "./vuetify";
import { onMounted, watch } from "vue";
import mermaid from "mermaid";
import {
    NolebaseEnhancedReadabilitiesMenu,
    NolebaseEnhancedReadabilitiesScreenMenu,
} from "@nolebase/vitepress-plugin-enhanced-readabilities/client";
import { NolebaseInlineLinkPreviewPlugin } from "@nolebase/vitepress-plugin-inline-link-preview/client";
import { NolebaseGitChangelogPlugin } from "@nolebase/vitepress-plugin-git-changelog/client";

import Layout from "./Layout.vue";
import themeControl from "./components/themeControl.vue";
import Comment from "./components/comment.vue";
import YoutubeVideo from "./components/YoutubeVideo.vue";
import BilibiliVideo from "./components/BilibiliVideo.vue";
import damageChart from "./components/minecraft-advanced-damage-chart.vue";
import Footer from "./components/Footer.vue";
import ArticleMetadata from "./components/ArticleMetadataCN.vue";
import buttons from "./components/Buttons.vue";
import Linkcard from "./components/Linkcard.vue";
import carousels from "./components/carousels.vue";
import commitsCounter from "./components/CommitsCounter.vue";
import MNavLinks from "./components/MNavLinks.vue";
import ResponsibleEditor from "./components/ResponsibleEditor.vue";
import Animation from "./components/Animation.vue";
import PdfViewer from "./components/PdfViewer.vue";

import { InjectionKey } from '@nolebase/vitepress-plugin-inline-link-preview/client'

import "@nolebase/vitepress-plugin-inline-link-preview/client/style.css";

import "@mdit/plugin-spoiler/style";
import "./style/index.css";
import "virtual:group-icons.css";
import "@nolebase/vitepress-plugin-enhanced-readabilities/client/style.css";
import "@nolebase/vitepress-plugin-enhanced-mark/client/style.css";
import "@nolebase/vitepress-plugin-inline-link-preview/client/style.css";
import "@nolebase/vitepress-plugin-git-changelog/client/style.css";

export default {
    extends: DefaultTheme,
    Layout: () => {
        const props: Record<string, any> = {};
        const { frontmatter } = useData();
        if (frontmatter.value?.layoutClass) {
            props.class = frontmatter.value.layoutClass;
        }
        return h(Animation, props, {
            slot: () => h(DefaultTheme.Layout, null, {
                "doc-bottom": () => h(imageViewer),
                "layout-top": () => h(themeControl),
                "aside-outline-after": () => h(),
                "doc-after": () => h(Comment),
                "layout-bottom": () => h(Footer),
                "doc-footer-before": () => [ h(buttons) , h(ResponsibleEditor) ],
                "nav-bar-content-after": () => h(NolebaseEnhancedReadabilitiesMenu),
                "nav-screen-content-after": () =>
                    h(NolebaseEnhancedReadabilitiesScreenMenu),
            })
        });
    },
    enhanceApp: (ctx) => {
        DefaultTheme.enhanceApp(ctx);
        vitepressNprogress(ctx);
        enhanceAppWithTabs(ctx.app);
        ctx.app.use(vuetify);
        ctx.app.use(TwoslashFloatingVue);
        ctx.app.use(Layout);
        ctx.app.use(NolebaseInlineLinkPreviewPlugin);
        ctx.app.use(NolebaseGitChangelogPlugin);
        ctx.app.component("MdCarousel", carousels);
        ctx.app.component("YoutubeVideo", YoutubeVideo);
        ctx.app.component("BilibiliVideo", BilibiliVideo);
        ctx.app.component("DamageChart", damageChart);
        ctx.app.component("ArticleMetadata", ArticleMetadata);
        ctx.app.component("Linkcard", Linkcard);
        ctx.app.component("commitsCounter", commitsCounter);
        ctx.app.component("MNavLinks", MNavLinks);
        ctx.app.component("PdfViewer", PdfViewer);
    },
    setup() {
        const route = useRoute();
        const { frontmatter } = useData();

        const initMermaid = () => {
            mermaid.initialize({
                startOnLoad: true,
                theme: "default",
                securityLevel: "loose",
                flowchart: {
                    useMaxWidth: true,
                    htmlLabels: true,
                    curve: "cardinal",
                },
                sequence: {
                    diagramMarginX: 50,
                    diagramMarginY: 10,
                    actorMargin: 50,
                    width: 150,
                    height: 65,
                    boxMargin: 10,
                    boxTextMargin: 5,
                    noteMargin: 10,
                    messageMargin: 35,
                    mirrorActors: true,
                },
                gantt: {
                    titleTopMargin: 25,
                    barHeight: 20,
                    barGap: 4,
                    topPadding: 50,
                    leftPadding: 75,
                    gridLineStartPadding: 35,
                    fontSize: 11,
                    //@ts-expect-error
                    fontFamily: '"Open-Sans", "sans-serif"',
                    numberSectionStyles: 4,
                    axisFormat: "%Y-%m-%d",
                },
            });
        };

        onMounted(() => {
            initMermaid();
            mermaid.init(undefined, ".mermaid");

            watch(
                () => route.path,
                () => {
                    setTimeout(() => {
                        mermaid.init(undefined, ".mermaid");
                    }, 100);
                }
            );
        });
    },
} satisfies Theme;
