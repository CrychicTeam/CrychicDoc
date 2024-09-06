<template>
    <div :class="['video-container', { dark: isDark }]">
        <iframe
            :src="'https://www.youtube.com/embed/' + videoId"
            frameborder="0"
            allow="autoplay; encrypted-media"
            allowfullscreen
        ></iframe>
    </div>
</template>

<script setup lang="ts">
    import { useData } from "vitepress";
    import { defineProps } from "vue";

    const { isDark } = useData();
    const props = defineProps({
        videoId: {
            type: String,
            required: true,
        },
    });
</script>

<style scoped>
    .video-container {
        position: relative;
        padding-bottom: 56.25%;
        height: 0;
        overflow: hidden;
        border: 2px solid #ccc;
        border-radius: 10px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        background-color: #f9f9f9;
        transition: background-color 0.3s, border-color 0.3s, box-shadow 0.3s;
    }

    .video-container.dark {
        border-color: #555;
        background-color: #333;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.6);
    }

    .video-container iframe {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        border-radius: 10px;
    }
</style>
