<template>
    <div class="image-viewer">
        <!-- 使用 t-image-viewer 组件预览图片 -->
        <t-image-viewer
            v-model:visible="show"
            :images="previewImageInfo.list"
            :default-index="previewImageInfo.idx"
            :key="previewImageInfo.idx"
            @close="show = false"
        >
        </t-image-viewer>
        <!-- 自定义暗模式支持 -->
        <tdesign-dark></tdesign-dark>
    </div>
</template>

<script setup lang="ts">
    import { reactive, ref, onMounted, onUnmounted } from "vue";
    import tdesignDark from "./themeControl.vue";

    const show = ref(false);
    const previewImageInfo = reactive<{
        url: string;
        list: string[];
        idx: number;
    }>({
        url: "",
        list: [],
        idx: 0,
    });

    const prefixBlacklist = ref<string[]>([
        "https://avatars.githubusercontent.com/",
        "https://www.github.com/",
        "https://github.com/",
    ]);

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

    onMounted(() => {
        const docDomContainer = document.querySelector("#VPContent");
        if (docDomContainer) {
            docDomContainer.addEventListener("click", previewImage);
        } else {
            console.error("#VPContent 元素未找到，无法绑定事件");
        }
    });

    onUnmounted(() => {
        const docDomContainer = document.querySelector("#VPContent");
        docDomContainer?.removeEventListener("click", previewImage);
    });
</script>
