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
            let busuanziScriptLoaded = false;
            
            // 动态加载不蒜子官方脚本
            const loadBusuanziScript = () => {
                return new Promise((resolve) => {
                    if (document.querySelector('script[src*="busuanzi"]') || busuanziScriptLoaded) {
                        // 检查window.busuanzi是否已经可用
                        if (typeof (window as any).busuanzi !== 'undefined') {
                            resolve(true);
                            return;
                        }
                        // 如果脚本已加载但window.busuanzi不可用，等待一下
                        setTimeout(() => {
                            resolve(typeof (window as any).busuanzi !== 'undefined');
                        }, 500);
                        return;
                    }
                    
                    const script = document.createElement('script');
                    script.async = true;
                    script.src = 'https://busuanzi.ibruce.info/busuanzi/2.3/busuanzi.pure.mini.js';
                    script.onload = () => {
                        busuanziScriptLoaded = true;
                        // console.log('✅ 不蒜子官方脚本加载完成');
                        
                        // 等待一下让脚本完全初始化
                        setTimeout(() => {
                            if (typeof (window as any).busuanzi !== 'undefined') {
                                // console.log('🎯 window.busuanzi 已可用');
                                resolve(true);
                            } else {
                                // console.warn('⚠️ 脚本加载但window.busuanzi不可用');
                                resolve(false);
                            }
                        }, 500);
                    };
                    script.onerror = () => {
                        // console.warn('❌ 不蒜子官方脚本加载失败');
                        resolve(false);
                    };
                    document.head.appendChild(script);
                    // console.log('🔧 开始加载不蒜子官方脚本');
                });
            };
            
            const updateBusuanzi = () => {
                setTimeout(async () => {
                    const scriptLoaded = await loadBusuanziScript();
                    
                    if (!scriptLoaded) {
                        // console.warn('⚠️ 不蒜子脚本加载失败，跳过统计');
                        return;
                    }
                    
                    // 清空现有统计元素的内容
                    const pagePV = document.querySelector('#busuanzi_value_page_pv');
                    const sitePV = document.querySelector('#busuanzi_value_site_pv');
                    const siteUV = document.querySelector('#busuanzi_value_site_uv');
                    
                    if (pagePV) pagePV.innerHTML = '';
                    if (sitePV) sitePV.innerHTML = '';
                    if (siteUV) siteUV.innerHTML = '';
                    
                    // 等待DOM更新后调用统计
                    setTimeout(() => {
                        const currentURL = window.location.href;
                        const pathname = window.location.pathname;
                        
                        // console.log('📊 更新不蒜子统计:', { 
                        //     url: currentURL, 
                        //     pathname,
                        //     busuanziWindow: typeof (window as any).busuanzi !== 'undefined',
                        //     elements: {
                        //         pagePV: !!document.querySelector('#busuanzi_value_page_pv'),
                        //         sitePV: !!document.querySelector('#busuanzi_value_site_pv'),
                        //         siteUV: !!document.querySelector('#busuanzi_value_site_uv')
                        //     }
                        // });
                        
                        // 只使用官方脚本
                        if (typeof (window as any).busuanzi !== 'undefined') {
                            const busuanziObj = (window as any).busuanzi;
                            
                            if (typeof busuanziObj.fetch === 'function') {
                                try {
                                    busuanziObj.fetch();
                                    // console.log('✅ 调用 busuanzi.fetch()');
                                } catch (error) {
                                    // console.error('❌ busuanzi.fetch() 调用失败:', error);
                                }
                            }
                            
                            // 尝试调用其他可能的方法来强制刷新
                            if (typeof busuanziObj.send === 'function') {
                                try {
                                    busuanziObj.send();
                                    console.log('✅ 调用 busuanzi.send()');
                                } catch (error) {
                                    console.error('❌ busuanzi.send() 调用失败:', error);
                                }
                            }
                            
                            // console.log('🔧 不蒜子对象方法:', Object.keys(busuanziObj));
                        } else {
                            // console.warn('❌ window.busuanzi 不可用');
                            
                            // 如果官方脚本还未初始化，尝试手动触发
                            const scriptElements = document.querySelectorAll('script[src*="busuanzi"]');
                            if (scriptElements.length > 0) {
                                // console.log('🔄 发现不蒜子脚本，但未初始化，尝试重新加载页面统计');
                                
                                // 尝试通过修改URL hash来触发统计更新
                                const hash = '#' + Date.now();
                                if (window.location.hash !== hash) {
                                    window.history.replaceState(null, '', window.location.pathname + hash);
                                    setTimeout(() => {
                                        window.history.replaceState(null, '', window.location.pathname);
                                    }, 100);
                                }
                            }
                        }
                        
                        // 2秒后检查结果
                        setTimeout(() => {
                            const pagePVNew = document.querySelector('#busuanzi_value_page_pv');
                            const sitePVNew = document.querySelector('#busuanzi_value_site_pv');
                            const siteUVNew = document.querySelector('#busuanzi_value_site_uv');
                            
                            // console.log('🔍 统计更新结果:', {
                            //     pagePV: pagePVNew?.innerHTML || '空',
                            //     sitePV: sitePVNew?.innerHTML || '空',
                            //     siteUV: siteUVNew?.innerHTML || '空'
                            // });
                        }, 2000);
                    }, 500);
                }, 300);
            };
            
            // 首次加载
            updateBusuanzi();
            
            // 路由切换时重新调用
            ctx.router.onAfterRouteChanged = () => {
                // console.log('🔄 路由切换到:', window.location.pathname);
                updateBusuanzi();
            };
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
