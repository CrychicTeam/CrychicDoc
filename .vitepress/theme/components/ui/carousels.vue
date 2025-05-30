<template>
    <div class="carousel" ref="carouselContainer">
        <v-carousel
            v-if="isReady"
            :height="carouselHeight"
            :show-arrows="showArrows"
            :cycle="cycle"
            :interval="interval"
            :hide-delimiters="hideDelimiters"
        >
            <slot></slot>
        </v-carousel>
    </div>
</template>

<script setup>
    import { ref, onMounted, onUnmounted, nextTick, watch } from "vue";

    const props = defineProps({
        showArrows: {
            type: [Boolean, String],
            default: true,
        },
        cycle: {
            type: Boolean,
            default: false,
        },
        interval: {
            type: Number,
            default: 6000,
        },
        hideDelimiters: {
            type: Boolean,
            default: false,
        },
    });

    const carouselContainer = ref(null);
    const carouselHeight = ref("auto");
    const isReady = ref(false);

    const updateCarouselHeight = () => {
        if (carouselContainer.value) {
            const items =
                carouselContainer.value.querySelectorAll(".v-carousel__item");
            let maxHeight = 0;

            items.forEach((item) => {
                item.style.display = "block";
                const itemHeight = item.scrollHeight;
                item.style.display = "";
                if (itemHeight > maxHeight) maxHeight = itemHeight;
            });

            carouselHeight.value = maxHeight > 0 ? `${maxHeight}px` : "auto";
            isReady.value = true;
        }
    };

    const debouncedUpdateHeight = debounce(updateCarouselHeight, 100);

    let resizeObserver = null;
    if (typeof ResizeObserver !== "undefined") {
        resizeObserver = new ResizeObserver(debouncedUpdateHeight);
    }

    onMounted(() => {
        nextTick(() => {
            if (carouselContainer.value && resizeObserver) {
                resizeObserver.observe(carouselContainer.value);
            }
            window.addEventListener("resize", debouncedUpdateHeight);
            updateCarouselHeight();
        });
    });

    onUnmounted(() => {
        if (carouselContainer.value && resizeObserver) {
            resizeObserver.unobserve(carouselContainer.value);
        }
        if (resizeObserver) {
            resizeObserver.disconnect();
        }
        window.removeEventListener("resize", debouncedUpdateHeight);
    });

    watch(
        () => props,
        () => {
            nextTick(updateCarouselHeight());
        },
        { deep: true, immediate: true }
    );

    function debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }
</script>

<style scoped>
    .carousel {
        width: 100%;
        max-width: 100vw;
    }

    :deep(.v-carousel__item) {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    :deep(.v-carousel__item img) {
        max-width: 100%;
        max-height: 100%;
        object-fit: contain;
    }
</style>
