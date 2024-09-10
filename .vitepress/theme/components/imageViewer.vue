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

    // URL 前缀黑名单，用于屏蔽特定的图片
    const prefixBlacklist = ref<string[]>([
        "https://avatars.githubusercontent.com/", // 添加你想屏蔽的前缀
    ]);

    // 图片预览功能，增加前缀过滤
    function previewImage(e: Event) {
  const target = e.target as HTMLElement;
  const currentTarget = e.currentTarget as HTMLElement;

  if (target.tagName.toLowerCase() === "img" && !target.classList.contains("non-preview-image")) {
    const url = (target as HTMLImageElement).src;

    // 输出调试信息，确认获取的 URL
    console.log("Clicked image URL: ", url);

    // 检查图片 URL 是否在前缀黑名单中
    const isPrefixBlacklisted = prefixBlacklist.value.some(prefix => url.startsWith(prefix));

    // 输出调试信息，确认黑名单匹配情况
    console.log("Is blacklisted: ", isPrefixBlacklisted);

    if (isPrefixBlacklisted) {
      console.log("该图片的 URL 存在于黑名单中，无法预览。");
      return;
    }

    const imgs = currentTarget.querySelectorAll<HTMLImageElement>(".content-container .main img:not(.non-preview-image)");
    const idx = Array.from(imgs).findIndex((el) => el === target);
    const urls = Array.from(imgs).map((el) => el.src);

    previewImageInfo.url = url;
    previewImageInfo.list = urls;
    previewImageInfo.idx = idx;

    // 兼容点击 main 之外的图片
    if (idx === -1 && url) {
      previewImageInfo.list.push(url);
      previewImageInfo.idx = previewImageInfo.list.length - 1;
    }

    show.value = true;
  }
}


    // 组件挂载时添加点击事件，卸载时移除事件
    onMounted(() => {
        const docDomContainer = document.querySelector("#VPContent");
        docDomContainer?.addEventListener("click", previewImage);
    });

    onUnmounted(() => {
        const docDomContainer = document.querySelector("#VPContent");
        docDomContainer?.removeEventListener("click", previewImage);
    });
</script>
