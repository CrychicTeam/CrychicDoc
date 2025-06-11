//@ts-nocheck
import { h } from "vue";
import type { Theme } from "vitepress";
import DefaultTheme from 'vitepress/theme-without-fonts'
import vitepressNprogress from "vitepress-plugin-nprogress";
import { useData, useRoute, inBrowser } from "vitepress";
import "./styles/index.css";
import "./styles/base/fonts.css";
import 'virtual:group-icons.css'
import { enhanceAppWithTabs } from "vitepress-plugin-tabs/client";
import vuetify from "./vuetify";
import { onMounted, onUnmounted, watch } from "vue";
import mermaid from "mermaid";
import {
    NolebaseEnhancedReadabilitiesMenu,
    NolebaseEnhancedReadabilitiesScreenMenu,
} from "@nolebase/vitepress-plugin-enhanced-readabilities/client";
import { NolebaseInlineLinkPreviewPlugin } from "@nolebase/vitepress-plugin-inline-link-preview/client";
import { NolebaseGitChangelogPlugin } from "@nolebase/vitepress-plugin-git-changelog/client";

// Import components
import Layout from "./Layout.vue";
import VPHero from "./components/VPHero.vue";
import { bindFancybox, destroyFancybox } from "./components/media/ImgViewer";
import { Animation, Preview, NotFound, Buttons } from "./components/ui";
import { comment } from "./components/content";
import { Footer } from "./components/navigation";
import { ResponsibleEditor } from "./components/content";

// Import utilities
import { setupLanguageControl } from "@utils/i18n/languageControl";
import { initMermaidConfig } from "@utils/charts/mermaid";
import { registerComponents } from "@utils/vitepress/components";

// Theme configuration
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
                "aside-outline-after": () => null,
                "doc-after": () => [h(Buttons), h(comment)],
                "layout-bottom": () => h(Footer),
                "doc-footer-before": () => h(ResponsibleEditor),
                "not-found": () => [h(NotFound)],
                "nav-bar-content-after": () => h(NolebaseEnhancedReadabilitiesMenu),
                "nav-screen-content-after": () => h(NolebaseEnhancedReadabilitiesScreenMenu),
                "doc-before": () => [h(Preview)],
            }),
        });
    },
    
    async enhanceApp(ctx) {
        if (!import.meta.env.SSR) {
            ctx.app.use(vuetify);
            ctx.app.use(NolebaseInlineLinkPreviewPlugin);
            ctx.app.use(NolebaseGitChangelogPlugin);
        }
        
        DefaultTheme.enhanceApp(ctx);
        vitepressNprogress(ctx);
        enhanceAppWithTabs(ctx.app);
        registerComponents(ctx.app);
    },
    
    setup() {
        const route = useRoute();
        const { isDark } = useData();

        // Watch for theme changes and update Vuetify theme
        watch(isDark, (dark) => {
            if (inBrowser) {
                vuetify.theme.global.name.value = dark ? 'dark' : 'light';
            }
        }, { immediate: true });
        
        onMounted(() => {
            if (!import.meta.env.SSR) {
                setupLanguageControl();
                initMermaidConfig();
                mermaid.init(undefined, ".mermaid");
                bindFancybox();
                watch(() => route.path, setupLanguageControl);
            }
        });
        
        onUnmounted(() => {
            destroyFancybox();
        });
    },
} satisfies Theme;
