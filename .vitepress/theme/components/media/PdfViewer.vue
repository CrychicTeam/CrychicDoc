<template>
    <div class="pdf-viewer" ref="pdfContainer">
        <embed
            v-if="isReady"
            :src="pdfSource"
            :width="containerWidth + 'px'"
            :height="containerHeight + 'px'"
            type="application/pdf"
            class="pdf-embed"
        />
    </div>
</template>

<script setup lang="ts">
    import { ref, onMounted, onUnmounted, nextTick } from "vue";

    const props = defineProps({
        pdfSource: {
            type: String,
            required: true,
        },
    });

    const pdfContainer = ref<HTMLElement | null>(null);
    const containerWidth = ref("100%");
    const containerHeight = ref("auto");
    const isReady = ref(false);

    const updateViewerDimensions = () => {
        if (pdfContainer.value) {
            const pdfWidth = pdfContainer.value.clientWidth;
            const pdfHeight = pdfWidth * 1.5;

            containerWidth.value = `${pdfWidth}px`;
            containerHeight.value = `${pdfHeight}px`;
            isReady.value = true;
        }
    };

    function debounce(func: (...args: any[]) => void, wait: number) {
        let timeout: NodeJS.Timeout | null = null;
        return function executedFunction(...args: any[]) {
            const later = () => {
                clearTimeout(timeout as NodeJS.Timeout);
                func(...args);
            };
            clearTimeout(timeout as NodeJS.Timeout);
            timeout = setTimeout(later, wait);
        };
    }

    const debouncedUpdateDimensions = debounce(updateViewerDimensions, 100);

    let resizeObserver = null;
    if (typeof ResizeObserver !== "undefined") {
        resizeObserver = new ResizeObserver(debouncedUpdateDimensions);
    }

    onMounted(() => {
        nextTick(() => {
            if (pdfContainer.value && resizeObserver) {
                resizeObserver.observe(pdfContainer.value);
            }
            window.addEventListener("resize", debouncedUpdateDimensions);
            updateViewerDimensions();
        });
    });

    onUnmounted(() => {
        if (pdfContainer.value && resizeObserver) {
            resizeObserver.unobserve(pdfContainer.value);
        }
        if (resizeObserver) {
            resizeObserver.disconnect();
        }
        window.removeEventListener("resize", debouncedUpdateDimensions);
    });
</script>

<style scoped>
    .pdf-viewer {
        width: 100%;
        max-width: 100vw;
        display: flex;
        justify-content: center;
        align-items: center;
        overflow: hidden;
        background-color: var(--pdf-bg-color);
        color: var(--pdf-text-color);
    }

    .pdf-embed {
        max-width: 100%;
        max-height: 100%;
        border-radius: var(--video-border-radius);
        border: 1px solid var(--pdf-border-color);
        box-shadow: var(--video-shadow);
        transition: background-color var(--transition-duration),
            color var(--transition-duration), border var(--transition-duration);
    }
</style>
