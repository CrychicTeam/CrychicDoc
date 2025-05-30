<template>
    <div class="video-container">
        <iframe
            ref="videoIframe"
            :src="
                'https://player.bilibili.com/player.html?bvid=' +
                bvid +
                '&high_quality=1&autoplay=0'
            "
            scrolling="no"
            border="0"
            frameborder="no"
            framespacing="0"
            allowfullscreen="true"
        ></iframe>
        <div class="video-overlay" @click="togglePlay"></div>
    </div>
</template>

<script setup lang="ts">
    import { useData } from "vitepress";
    import { defineProps, ref, onMounted, onUnmounted } from "vue";

    const { isDark } = useData();
    const props = defineProps({
        bvid: {
            type: String,
            required: true,
        },
    });

    const videoIframe = ref<HTMLIFrameElement | null>(null);
    let player: any = null;

    const togglePlay = () => {
        if (player) {
            if (player.paused) {
                player.play();
            } else {
                player.pause();
            }
        }
    };

    const handleMessage = (event: MessageEvent) => {
        if (event.source === videoIframe.value?.contentWindow) {
            if (event.data.type === "videoInfo") {
                player = event.data.data;
            }
        }
    };

    onMounted(() => {
        window.addEventListener("message", handleMessage);
    });

    onUnmounted(() => {
        window.removeEventListener("message", handleMessage);
    });
</script>

<style scoped>
    .video-container {
        position: relative;
        padding-bottom: var(--video-aspect-ratio);
        height: 0;
        overflow: hidden;
        border: 2px solid var(--video-border-color);
        border-radius: var(--video-border-radius);
        box-shadow: var(--video-shadow);
        background-color: var(--video-bg-color);
        transition: background-color var(--transition-duration),
            border-color var(--transition-duration),
            box-shadow var(--transition-duration);
    }

    .video-container iframe {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        border-radius: var(--video-border-radius);
    }

    .video-overlay {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: calc(100% - 40px);
        background: transparent;
        cursor: pointer;
    }
</style>
