//@ts-nocheck
import { h } from "vue";
import type { Theme } from "vitepress";
import DefaultTheme from "vitepress/theme";
import vitepressNprogress from "vitepress-plugin-nprogress";
import { useData, useRoute } from "vitepress";
import "./styles/index.css";
import { enhanceAppWithTabs } from "vitepress-plugin-tabs/client";
import vuetify from "./vuetify";
import { onMounted, watch, defineAsyncComponent } from "vue";
import mermaid from "mermaid";
import {
    NolebaseEnhancedReadabilitiesMenu,
    NolebaseEnhancedReadabilitiesScreenMenu,
} from "@nolebase/vitepress-plugin-enhanced-readabilities/client";
import { NolebaseInlineLinkPreviewPlugin } from "@nolebase/vitepress-plugin-inline-link-preview/client";
import { NolebaseGitChangelogPlugin } from "@nolebase/vitepress-plugin-git-changelog/client";

import { LiteTree } from "@lite-tree/vue";
import Layout from "./Layout.vue";

// Import custom components
import VPHero from "./components/VPHero.vue";

// Import components using @components system
import { ImageViewer } from "@components/media";
import {
    comment,
    ArticleMetadataCN,
    Linkcard,
    ResponsibleEditor,
} from "@components/content";
import { YoutubeVideo, BilibiliVideo, PdfViewer } from "@components/media";
import { Footer, MNavLinks } from "@components/navigation";
import {
    Buttons,
    Carousels,
    Animation,
    Preview,
    NotFound,
} from "@components/ui";
import MagicMoveContainer from "@components/ui/MagicMoveContainer.vue";

// Define SSR-safe chart components
const CommitsCounter = defineAsyncComponent(
    () => import("@components/content/CommitsCounter.vue")
);

const MinecraftAdvancedDamageChart = defineAsyncComponent(
    () => import("@components/content/minecraft-advanced-damage-chart.vue")
);

import { InjectionKey } from "@nolebase/vitepress-plugin-inline-link-preview/client";

export default {
    extends: DefaultTheme,
    Layout: () => {
        const props: Record<string, any> = {};
        const { frontmatter } = useData();
        if (frontmatter.value?.layoutClass) {
            props.class = frontmatter.value.layoutClass;
        }
        return h(Animation, props, {
            slot: () =>
                h(DefaultTheme.Layout, null, {
                    "doc-bottom": () => h(ImageViewer),
                    "aside-outline-after": () => h(),
                    "doc-after": () => [h(Buttons), h(comment)],
                    "layout-bottom": () => h(Footer),
                    "doc-footer-before": () => h(ResponsibleEditor),
                    "not-found": () => [h(NotFound)],
                    "nav-bar-content-after": () =>
                        h(NolebaseEnhancedReadabilitiesMenu),
                    "nav-screen-content-after": () =>
                        h(NolebaseEnhancedReadabilitiesScreenMenu),
                    "doc-before": () => [h(Preview)],
                }),
        });
    },
    async enhanceApp(ctx) {
        DefaultTheme.enhanceApp(ctx);
        vitepressNprogress(ctx);
        enhanceAppWithTabs(ctx.app);

        // Conditionally import browser-dependent plugins
        if (!import.meta.env.SSR) {
            ctx.app.use(vuetify);
            ctx.app.use(NolebaseInlineLinkPreviewPlugin);
            ctx.app.use(NolebaseGitChangelogPlugin);
        }

        // Register global components
        ctx.app.component("MdCarousel", Carousels);
        ctx.app.component("YoutubeVideo", YoutubeVideo);
        ctx.app.component("BilibiliVideo", BilibiliVideo);
        ctx.app.component("DamageChart", MinecraftAdvancedDamageChart);
        ctx.app.component("ArticleMetadata", ArticleMetadataCN);
        ctx.app.component("Linkcard", Linkcard);
        ctx.app.component("commitsCounter", CommitsCounter);
        ctx.app.component("MNavLinks", MNavLinks);
        ctx.app.component("PdfViewer", PdfViewer);
        ctx.app.component("LiteTree", LiteTree);
        ctx.app.component("MagicMoveContainer", MagicMoveContainer);
    },
    setup() {
        const route = useRoute();
        const { frontmatter } = useData();

        const initMermaid = () => {
            if (!import.meta.env.SSR) {
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
            }
        };

        onMounted(() => {
            if (!import.meta.env.SSR) {
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
            }
        });
    },
} satisfies Theme;
