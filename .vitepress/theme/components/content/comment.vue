<template>
    <!-- 使用 v-if 根据 showComment 是否为 true 来控制评论组件的显示 -->
    <div v-if="showComment" class="giscus-wrapper" ref="giscusContainer"></div>
</template>

<script lang="ts" setup>
    import { ref, watch, onMounted, computed, nextTick } from "vue";
    import { useData, useRoute } from "vitepress";

    // 从 useData 中获取 frontmatter、isDark 和 lang
    const { isDark, lang, frontmatter } = useData();
    const route = useRoute();

    // 根据 frontmatter.showComment 控制显示，默认为 true
    const showComment = computed(() => frontmatter.value.showComment !== false);

    const translations = {
        "en-US": {
            langCode: "en",
        },
        "zh-CN": {
            langCode: "zh-CN",
        },
    };

    const currentLangConfig = computed(() => {
        return (
            translations[lang.value as keyof typeof translations] ||
            translations["en-US"]
        );
    });

    const extractTerm = (path: string) => {
        const cleanedPath = path.replace(/^\/[a-z]{2}\//, "");
        return cleanedPath.length > 0 ? cleanedPath : "none";
    };

    const giscusContainer = ref<HTMLElement | null>(null);

    /**
     * Initialize Giscus comments safely for SSR
     */
    const initGiscus = () => {
        if (!import.meta.env.SSR && showComment.value && giscusContainer.value) {
            // Clear existing content
            giscusContainer.value.innerHTML = '';

        const script = document.createElement("script");
        script.src = "https://giscus.app/client.js";
        script.async = true;
            script.crossOrigin = "anonymous";
            script.dataset.repo = "Wudji/CrychicDoc";
            script.dataset.repoId = "R_kgDOKWjvqA";
            script.dataset.category = "Announcements";
            script.dataset.categoryId = "DIC_kwDOKWjvqM4CZFXY";
            script.dataset.mapping = "pathname";
            script.dataset.strict = "0";
            script.dataset.reactionsEnabled = "1";
            script.dataset.emitMetadata = "0";
            script.dataset.inputPosition = "bottom";
            script.dataset.lang = lang.value;
            script.dataset.theme = isDark.value ? "dark" : "light";

            giscusContainer.value.appendChild(script);
        }
    };

    /**
     * Update Giscus theme safely
     */
    const updateGiscusTheme = () => {
        if (!import.meta.env.SSR && showComment.value) {
            const iframe = document.querySelector(
                ".giscus-frame"
            ) as HTMLIFrameElement;
            if (iframe?.contentWindow) {
                iframe.contentWindow.postMessage(
                    {
                        giscus: {
                            setConfig: {
                                theme: isDark.value ? "dark" : "light",
                            },
                        },
                    },
                    "https://giscus.app"
                );
            }
        }
    };

    /**
     * Update Giscus language safely
     */
    const updateGiscusLang = () => {
        if (!import.meta.env.SSR && showComment.value) {
            const iframe = document.querySelector(
                ".giscus-frame"
            ) as HTMLIFrameElement;
            if (iframe?.contentWindow) {
                iframe.contentWindow.postMessage(
                    {
                        giscus: {
                            setConfig: {
                                lang: lang.value,
                            },
                        },
                    },
                    "https://giscus.app"
                );
            }
        }
    };

    // Watch for theme changes
    watch(isDark, updateGiscusTheme);

    // Watch for language changes  
    watch(lang, updateGiscusLang);

    // Watch for comment visibility changes
    watch(showComment, (newVal) => {
        if (newVal) {
            nextTick(initGiscus);
        }
    });

    // Initialize on mount
    onMounted(() => {
        if (showComment.value) {
            initGiscus();
        }
    });

    watch(
        () => route.path,
        () => {
            if (showComment.value) {
                initGiscus();
            }
        }
    );
</script>

<style>
    .giscus-wrapper {
        margin-top: 2rem;
        padding-top: 1rem;
        border-top: 1px solid var(--vp-c-divider);
    }

    main .giscus,
    main .giscus-frame {
        width: 100%;
    }

    main .giscus-frame {
        border: none;
    }
</style>
