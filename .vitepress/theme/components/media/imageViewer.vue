<template>
    <div class="image-viewer">
        <client-only>
            <t-image-viewer
                v-if="isMounted"
                v-model:visible="show"
                :images="previewImageInfo.list"
                :default-index="previewImageInfo.idx"
                :key="previewImageInfo.idx"
                @close="show = false"
            >
            </t-image-viewer>
        </client-only>
    </div>
</template>

<script setup lang="ts">
    import {
        reactive,
        shallowRef,
        onMounted,
        onUnmounted,
        defineAsyncComponent,
        onBeforeMount,
        ref,
    } from "vue";

    import { useData } from "vitepress";
    import { watch } from "vue";

    const { isDark } = useData();

    watch(
        isDark,
        () => {
            if (isDark.value) {
                document.documentElement.setAttribute("theme-mode", "dark");
            } else {
                document.documentElement.removeAttribute("theme-mode");
            }
        },
        {
            immediate: true,
        }
    );

    const TImageViewer = defineAsyncComponent(() =>
        import("tdesign-vue-next").then((mod) => mod.ImageViewer)
    );

    const show = shallowRef(false);
    const isMounted = shallowRef(false);
    const previewImageInfo = reactive<{
        url: string;
        list: string[];
        idx: number;
    }>({
        url: "",
        list: [],
        idx: 0,
    });

    const prefixBlacklist = shallowRef<string[]>([
        "https://avatars.githubusercontent.com/",
        "https://www.github.com/",
        "https://github.com/",
    ]);

    let docDomContainer: Element | null = null;

    function previewImage(e: Event) {
        const target = e.target as HTMLElement;
        const currentTarget = e.currentTarget as HTMLElement;

        if (
            target.tagName.toLowerCase() === "img" &&
            !target.classList.contains("non-preview-image")
        ) {
            const url = (target as HTMLImageElement).src;

            const isPrefixBlacklisted = prefixBlacklist.value.some((prefix) =>
                url.startsWith(prefix)
            );

            if (isPrefixBlacklisted) {
                return;
            }

            const imgs = currentTarget.querySelectorAll<HTMLImageElement>(
                ".content-container .main img:not(.non-preview-image)"
            );
            const idx = Array.from(imgs).findIndex((el) => el === target);
            const urls = Array.from(imgs).map((el) => el.src);

            previewImageInfo.url = url;
            previewImageInfo.list = urls;
            previewImageInfo.idx = idx;

            if (idx === -1 && url) {
                previewImageInfo.list.push(url);
                previewImageInfo.idx = previewImageInfo.list.length - 1;
            }

            show.value = true;
        }
    }

    onBeforeMount(() => {
        docDomContainer = null;
    });

    onMounted(() => {
        isMounted.value = true;
        docDomContainer = document.querySelector("#VPContent");
        if (docDomContainer) {
            docDomContainer.addEventListener("click", previewImage);
        } else {
            console.error("#VPContent 元素未找到，无法绑定事件");
        }
    });

    onUnmounted(() => {
        if (docDomContainer) {
            docDomContainer.removeEventListener("click", previewImage);
        }
    });
</script>
