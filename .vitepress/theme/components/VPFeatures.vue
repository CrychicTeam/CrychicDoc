<script setup lang="ts">
    import type { DefaultTheme } from "vitepress/theme";
    import { computed, onMounted, ref, onUnmounted } from "vue";
    import VPFeature from "vitepress/dist/client/theme-default/components/VPFeature.vue";
    import { motion } from "motion-v";

    export interface Feature {
        icon?: DefaultTheme.FeatureIcon;
        title: string;
        details: string;
        link?: string;
        linkText?: string;
        rel?: string;
        target?: string;
    }

    const props = defineProps<{
        features: Feature[];
    }>();

    const containerRef = ref<HTMLElement>();
    const scrollRef = ref<HTMLElement>();
    const isPaused = ref(false);
    
    const extendedFeatures = computed(() => {
        if (props.features.length < 8) {
            const repeatCount = Math.ceil(12 / props.features.length);
            return Array(repeatCount).fill(props.features).flat();
        }
        return [...props.features, ...props.features];
    });

    const grid = computed(() => {
        const length = props.features.length;

        if (!length) {
            return;
        } else if (length === 2) {
            return "grid-2";
        } else if (length === 3) {
            return "grid-3";
        } else if (length % 3 === 0) {
            return "grid-6";
        } else if (length > 3) {
            return "grid-4";
        }
    });

    // Enhanced animation variants
    const containerVariants = {
        hidden: {
            opacity: 0,
            y: 60,
        },
        visible: {
            opacity: 1,
            y: 0,
            transition: {
                duration: 1,
                ease: "easeOut",
                when: "beforeChildren",
                staggerChildren: 0.15,
            },
        },
    };

    const itemVariants = {
        hidden: {
            opacity: 0,
            y: 40,
            scale: 0.95,
        },
        visible: {
            opacity: 1,
            y: 0,
            scale: 1,
            transition: {
                type: "spring",
                stiffness: 100,
                damping: 15,
                duration: 0.8,
            },
        },
    };

    const itemHover = {
        y: -8,
        scale: 1.02,
        transition: {
            type: "spring",
            stiffness: 400,
            damping: 25,
        },
    };

    onMounted(() => {
        const scrollElement = scrollRef.value;
        if (!scrollElement) return;

        // Calculate scroll distance (half the width for seamless loop)
        const scrollDistance = scrollElement.scrollWidth / 2;
        let currentTranslate = 0;

        const animate = () => {
            if (!isPaused.value) {
                currentTranslate -= 0.5;

                if (Math.abs(currentTranslate) >= scrollDistance) {
                    currentTranslate = 0;
                }

                scrollElement.style.transform = `translateX(${currentTranslate}px)`;
            }
            requestAnimationFrame(animate);
        };

        animate();
    });

    const handleMouseEnter = () => {
        isPaused.value = true;
    };

    const handleMouseLeave = () => {
        isPaused.value = false;
    };
</script>

<template>
    <div v-if="features" class="VPFeatures features-enhanced">
        <div class="container" ref="containerRef">
            <motion.div
                class="scroll-container"
                :variants="containerVariants"
                initial="hidden"
                :whileInView="'visible'"
                :viewport="{ once: true, margin: '-50px' }"
                @mouseenter="handleMouseEnter"
                @mouseleave="handleMouseLeave"
            >
                <div class="scroll-content" ref="scrollRef">
                    <motion.div
                        v-for="(feature, index) in extendedFeatures"
                        :key="`${feature.title}-${index}`"
                        class="item feature-card"
                        :class="[grid]"
                        :variants="itemVariants"
                        :whileHover="itemHover"
                    >
                        <VPFeature
                            :icon="feature.icon"
                            :title="feature.title"
                            :details="feature.details"
                            :link="feature.link"
                            :link-text="feature.linkText"
                            :rel="feature.rel"
                            :target="feature.target"
                        />
                    </motion.div>
                </div>
            </motion.div>
        </div>
    </div>
</template>

<style scoped>
    .VPFeatures.features-enhanced {
        position: relative;
        /* Fixed background colors */
        background: #ffffff;
        padding: 0;
        margin: 0;
        width: 100vw;
        margin-left: 50%;
        transform: translateX(-50%);
        overflow: hidden;
        /* Add more space for hover effects */
        padding-top: 20px;
        padding-bottom: 20px;
    }

    .dark .VPFeatures.features-enhanced {
        background: #1b1b1f;
    }

    .container {
        margin: 0 auto;
        max-width: none; /* Remove max-width for full scrolling */
        position: relative;
        z-index: 10;
        padding: 80px 0 100px;
    }

    @media (min-width: 640px) {
        .container {
            padding: 100px 0 120px;
        }
    }

    @media (min-width: 960px) {
        .container {
            padding: 120px 0 140px;
        }
    }

    .scroll-container {
        position: relative;
        width: 100%;
        overflow: visible; /* Allow overflow for hover effects */
        mask: linear-gradient(
            90deg,
            transparent 0%,
            black 5%,
            black 95%,
            transparent 100%
        );
        -webkit-mask: linear-gradient(
            90deg,
            transparent 0%,
            black 5%,
            black 95%,
            transparent 100%
        );
        /* Add padding for hover effects */
        padding: 20px 0;
        margin: -20px 0;
    }

    .scroll-content {
        display: flex;
        gap: 32px;
        will-change: transform;
        padding: 0 40px;
    }

    @media (min-width: 640px) {
        .scroll-content {
            gap: 40px;
            padding: 0 60px;
        }
    }

    @media (min-width: 960px) {
        .scroll-content {
            gap: 48px;
            padding: 0 80px;
        }
    }

    .item.feature-card {
        flex: none;
        width: 380px;
        min-width: 380px;
        position: relative;
        /* Ensure hover effects have space */
        z-index: 1;
    }

    @media (min-width: 640px) {
        .item.feature-card {
            width: 420px;
            min-width: 420px;
        }
    }

    @media (min-width: 960px) {
        .item.feature-card {
            width: 480px;
            min-width: 480px;
        }
    }

    /* Clean and elegant VPFeature styles - no shadows, no clipping */
    :deep(.VPFeature) {
        background: var(--vp-c-bg-soft);
        border: 1px solid var(--vp-c-divider);
        border-radius: 16px;
        padding: 32px 24px;
        height: 100%;
        min-height: 280px;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        position: relative;
        overflow: visible; /* Prevent clipping */
        display: flex;
        flex-direction: column;
        /* Ensure proper z-index stacking */
        z-index: 1;
    }

    /* Simple hover effect without shadows and no clipping */
    :deep(.VPFeature::before) {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 3px;
        background: linear-gradient(
            90deg,
            transparent,
            var(--vp-c-brand),
            transparent
        );
        opacity: 0;
        transition: opacity 0.3s ease;
        border-radius: 16px 16px 0 0;
    }

    :deep(.VPFeature:hover) {
        background: var(--vp-c-bg-alt);
        border-color: var(--vp-c-brand);
        transform: translateY(-8px); /* Slightly more movement */
        /* Ensure hover doesn't get clipped */
        z-index: 10;
    }

    :deep(.VPFeature:hover::before) {
        opacity: 1;
    }

    :deep(.VPFeature .icon) {
        margin-bottom: 24px;
        width: 64px;
        height: 64px;
        padding: 16px;
        background: var(--vp-c-default-soft);
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s ease;
        color: var(--vp-c-brand);
    }

    :deep(.VPFeature:hover .icon) {
        background: var(--vp-c-brand-soft);
        transform: scale(1.05);
    }

    :deep(.VPFeature .title) {
        font-size: 20px;
        font-weight: 700;
        line-height: 1.3;
        margin-bottom: 16px;
        color: var(--vp-c-text-1);
        transition: all 0.3s ease;
    }

    :deep(.VPFeature:hover .title) {
        color: var(--vp-c-brand);
        transform: translateX(2px);
    }

    :deep(.VPFeature .details) {
        font-size: 15px;
        line-height: 1.6;
        color: var(--vp-c-text-2);
        font-weight: 400;
        transition: color 0.3s ease;
        flex-grow: 1;
    }

    :deep(.VPFeature:hover .details) {
        color: var(--vp-c-text-1);
    }

    /* Mobile optimizations */
    @media (max-width: 768px) {
        .container {
            padding: 60px 0 80px;
        }

        .scroll-content {
            gap: 24px;
            padding: 0 20px;
        }

        .item.feature-card {
            width: 320px;
            min-width: 320px;
        }

        :deep(.VPFeature) {
            padding: 24px 20px;
            min-height: 240px;
        }

        :deep(.VPFeature .icon) {
            width: 56px;
            height: 56px;
            padding: 14px;
            margin-bottom: 20px;
        }

        :deep(.VPFeature .title) {
            font-size: 18px;
            margin-bottom: 12px;
        }

        :deep(.VPFeature .details) {
            font-size: 14px;
            line-height: 1.5;
        }
    }

    /* Accessibility */
    @media (prefers-reduced-motion: reduce) {
        .scroll-content {
            animation: none !important;
            transform: none !important;
        }

        * {
            animation-duration: 0.01ms !important;
            animation-iteration-count: 1 !important;
            transition-duration: 0.01ms !important;
        }
    }

    /* Print styles */
    @media print {
        .VPFeatures {
            break-inside: avoid;
            page-break-inside: avoid;
        }
    }
</style>
