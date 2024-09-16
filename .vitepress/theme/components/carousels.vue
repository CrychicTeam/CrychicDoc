<template>
    <div class="carousel">
        <v-responsive :aspect-ratio="aspectRatio" ref="responsiveContainer">
            <!-- 动态的 v-responsive 宽高比 -->
            <v-carousel
                :height="carouselHeight"
                :show-arrows="showArrows"
                :cycle="cycle"
                :interval="interval"
                :hide-delimiters="hideDelimiters"
            >
                <slot></slot>
            </v-carousel>
        </v-responsive>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'

const props = defineProps({
    aspectRatio: {
        type: Number,
        default: 16 / 9, // 默认宽高比
    },
    showArrows: {
        type: [Boolean, String],
        default: true, // 控制箭头的显示，默认为 true，可以传入 'hover' 显示
    },
    cycle: {
        type: Boolean,
        default: false, // 是否启用自动轮播
    },
    interval: {
        type: Number,
        default: 6000, // 自动轮播的间隔
    },
    hideDelimiters: {
        type: Boolean,
        default: false, // 是否隐藏指示点
    },
})

const carouselHeight = ref(0)
const responsiveContainer = ref(null)

const updateCarouselHeight = () => {
    if (responsiveContainer.value) {
        const containerElement = responsiveContainer.value.$el;
        carouselHeight.value = containerElement.offsetWidth / props.aspectRatio;
    }
}

const resizeObserver = new ResizeObserver(() => {
    updateCarouselHeight()
})

onMounted(() => {
    nextTick(() => {
        updateCarouselHeight()
        if (responsiveContainer.value) {
            resizeObserver.observe(responsiveContainer.value.$el)
        }
    })
})

onUnmounted(() => {
    if (responsiveContainer.value) {
        resizeObserver.unobserve(responsiveContainer.value.$el)
    }
    resizeObserver.disconnect()
})
</script>

<style>
.carousel {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: auto;
    max-height: 100vh;
    position: relative;
}

.carousel img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}
</style>
