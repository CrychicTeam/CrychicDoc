//@ts-nocheck
import { h } from "vue";
import type { Theme } from "vitepress";
import DefaultTheme from "vitepress/theme";
import vitepressNprogress from "vitepress-plugin-nprogress";
import { useData, useRoute, inBrowser } from "vitepress";
import "./styles/index.css";
import { enhanceAppWithTabs } from "vitepress-plugin-tabs/client";
import vuetify from "./vuetify";
import { onMounted, onUnmounted, watch, defineAsyncComponent } from "vue";
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

// Import Fancybox image viewer
import { bindFancybox, destroyFancybox } from "./components/media/ImgViewer";

// Import components using @components system
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

const Contributors = defineAsyncComponent(
    () => import("@components/content/Contributors.vue")
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
        if (inBrowser) {
            // Load Busuanzi script
            const loadBusuanzi = () => {
                if (document.querySelector('script[src*="busuanzi"]')) return;
                
                const script = document.createElement('script');
                script.async = true;
                script.src = 'https://busuanzi.ibruce.info/busuanzi/2.3/busuanzi.pure.mini.js';
                document.head.appendChild(script);
            };
            
            // Update page view statistics
            const updateStats = () => {
                setTimeout(() => {
                    if (typeof (window as any).busuanzi?.fetch === 'function') {
                        (window as any).busuanzi.fetch();
                    }
                }, 1000);
            };
            
            // Initialize on first load
            loadBusuanzi();
            updateStats();
            
            // Update on route change
            ctx.router.onAfterRouteChanged = updateStats;
        }
        
        DefaultTheme.enhanceApp(ctx);
        vitepressNprogress(ctx);
        enhanceAppWithTabs(ctx.app);

        // Conditionally import browser-dependent plugins
        if (!import.meta.env.SSR) {
            ctx.app.use(vuetify);
            ctx.app.use(NolebaseInlineLinkPreviewPlugin);
            ctx.app.use(NolebaseGitChangelogPlugin);

            // Setup Fancybox router hooks
            ctx.router.onBeforeRouteChange = () => {
                destroyFancybox(); // Destroy before route change
            };
            ctx.router.onAfterRouteChange = () => {
                bindFancybox(); // Bind after route change
            };
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
        ctx.app.component("Contributors", Contributors);


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
                
                // Initialize Fancybox
                bindFancybox();
                
                // 首次加载时调用不蒜子统计
                setTimeout(() => {
                    console.log('首次加载，调用不蒜子统计');
                    if (typeof busuanzi !== 'undefined') {
                        busuanzi.fetch();
                    }
                }, 1000);

                watch(
                    () => route.path,
                    () => {
                        setTimeout(() => {
                            mermaid.init(undefined, ".mermaid");
                            // 路由变化时也调用不蒜子统计
                            console.log('路由变化，调用不蒜子统计');
                            if (typeof busuanzi !== 'undefined') {
                                busuanzi.fetch();
                            }
                        }, 100);
                    }
                );
            }
        });

        onUnmounted(() => {
            destroyFancybox();
        });
    },
} satisfies Theme;
