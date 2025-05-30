<template>
    <div class="video-container">
        <iframe
            :src="'https://www.youtube.com/embed/' + videoId"
            frameborder="0"
            allow="autoplay; encrypted-media"
            allowfullscreen
        ></iframe>
    </div>
</template>

<script setup lang="ts">
    import { defineProps } from "vue";

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
</style>
