<script setup lang="ts">
    import {
        type Ref,
        inject,
        onMounted,
        onUnmounted,
        ref,
        nextTick,
    } from "vue";
    import type { DefaultTheme } from "vitepress/theme";
    import VPButton from "vitepress/dist/client/theme-default/components/VPButton.vue";
    import VPImage from "vitepress/dist/client/theme-default/components/VPImage.vue";
    import { motion } from "motion-v";

    export interface HeroAction {
        theme?: "brand" | "alt";
        text: string;
        link: string;
        target?: string;
        rel?: string;
    }

    defineProps<{
        name?: string;
        text?: string;
        tagline?: string;
        image?: DefaultTheme.ThemeableImage;
        actions?: HeroAction[];
    }>();

    const heroImageSlotExists = inject(
        "hero-image-slot-exists"
    ) as Ref<boolean>;

    /**
     * Get code snippet by category and index
     * @param category - The category of code snippet
     * @param index - The index within the category
     */
    const getCodeSnippet = (category: string, index: number): string => {
        const snippets: Record<string, string[]> = {
            "kubejs-events": [
                "ServerEvents.recipes(event => {})",
                "PlayerEvents.tick(event => {})",
                "ItemEvents.rightClicked(event => {})",
                "BlockEvents.broken(event => {})",
                'StartupEvents.registry("item", event => {})',
                "ServerEvents.loaded(event => {})",
            ],
            "kubejs-recipes": [
                'event.shaped("item", ["ABC", "DEF"])',
                'event.shapeless("output", ["input1", "input2"])',
                'event.smelting("output", "input")',
                'event.remove({id: "minecraft:stick"})',
                'event.replaceInput({}, "old", "new")',
                'event.stonecutting("output", "input")',
            ],
            "minecraft-api": [
                "Level level = player.level()",
                "BlockPos pos = new BlockPos(x, y, z)",
                "ItemStack stack = new ItemStack(Items.DIAMOND)",
                "BlockState state = level.getBlockState(pos)",
            ],
            "forge-modding": [
                '@Mod("modid")',
                '@EventBusSubscriber(modid = "modid")',
                "@SubscribeEvent",
                'ForgeRegistries.ITEMS.register("item", item)',
            ],
            "minecraft-commands": [
                "/give @p minecraft:diamond 64",
                "/tp @a 0 100 0",
                "/gamemode creative @s",
                "/effect give @p minecraft:speed 60 2",
            ],
        };

        const categorySnippets =
            snippets[category] || snippets["kubejs-events"];
        return categorySnippets[index % categorySnippets.length];
    };



    // Slice-by-slice reveal animations
    const heroContainerVariants = {
        hidden: { opacity: 0 },
        visible: {
            opacity: 1,
            transition: {
                duration: 0.6,
                ease: "easeOut",
                staggerChildren: 0.1,
            },
        },
    };

    // Slice reveal effect for letters - like cutting through
    const letterVariants = {
        hidden: {
            opacity: 0,
            y: 50,
            scaleY: 0,
            transformOrigin: "bottom",
        },
        visible: (i: unknown) => ({
            opacity: 1,
            y: 0,
            scaleY: 1,
            transformOrigin: "bottom",
            transition: {
                type: "spring",
                damping: 18,
                stiffness: 120,
                delay: (i as number) * 0.03,
                duration: 0.8,
            },
        }),
    };

    // Word slice animation - reveals like unfolding
    const wordVariants = {
        hidden: {
            opacity: 0,
            y: 30,
            scaleX: 0.3,
            transformOrigin: "left",
        },
        visible: (i: unknown) => ({
            opacity: 1,
            y: 0,
            scaleX: 1,
            transformOrigin: "left",
            transition: {
                type: "spring",
                damping: 20,
                stiffness: 100,
                delay: (i as number) * 0.08,
                duration: 1.0,
            },
        }),
    };

    const subtitleVariants = {
        hidden: {
            opacity: 0,
            y: 40,
            clipPath: "inset(0 100% 0 0)",
        },
        visible: {
            opacity: 1,
            y: 0,
            clipPath: "inset(0 0% 0 0)",
            transition: {
                duration: 1.0,
                ease: "easeOut",
                delay: 0.6,
            },
        },
    };

    const taglineVariants = {
        hidden: {
            opacity: 0,
            y: 30,
            clipPath: "inset(0 100% 0 0)",
        },
        visible: {
            opacity: 1,
            y: 0,
            clipPath: "inset(0 0% 0 0)",
            transition: {
                duration: 0.8,
                ease: "easeOut",
                delay: 0.8,
            },
        },
    };

    const actionsVariants = {
        hidden: { opacity: 0, y: 40 },
        visible: {
            opacity: 1,
            y: 0,
            transition: {
                duration: 0.8,
                ease: "easeOut",
                delay: 1.0,
                staggerChildren: 0.1,
            },
        },
    };

    const buttonVariants = {
        hidden: {
            opacity: 0,
            scale: 0.8,
            y: 20,
        },
        visible: {
            opacity: 1,
            scale: 1,
            y: 0,
            transition: {
                type: "spring",
                stiffness: 150,
                damping: 15,
                duration: 0.6,
            },
        },
    };

    const buttonHover = {
        scale: 1.05,
        y: -2,
        transition: {
            type: "spring",
            stiffness: 300,
            damping: 20,
            duration: 0.2,
        },
    };

    const buttonTap = {
        scale: 0.95,
        transition: {
            type: "spring",
            stiffness: 400,
            damping: 25,
            duration: 0.1,
        },
    };

    // Image slice reveal - appears in slices from top to bottom
    const imageVariants = {
        hidden: {
            opacity: 0,
            clipPath: "inset(100% 0 0 0)",
        },
        visible: {
            opacity: 1,
            clipPath: "inset(0% 0 0 0)",
            transition: {
                duration: 1.2,
                ease: "easeOut",
                delay: 0.4,
            },
        },
    };

    const imageHover = {
        scale: 1.02,
        transition: {
            type: "spring",
            stiffness: 200,
            damping: 20,
            duration: 0.3,
        },
    };

    // Simple parallax for floating words
    const parallaxContainer = ref<HTMLElement | null>(null);
    const floatingWords = ref<HTMLElement[]>([]);

    const handleMouseMove = (event: MouseEvent) => {
        if (!parallaxContainer.value || window.innerWidth < 768) return;

        const { clientX, clientY } = event;
        const { offsetWidth, offsetHeight } = parallaxContainer.value;
        const xPercent = (clientX / offsetWidth - 0.5) * 2;
        const yPercent = (clientY / offsetHeight - 0.5) * 2;

        floatingWords.value.forEach((word, index) => {
            const intensity = parseFloat(word.dataset.intensity || "3");
            const strength = ((index % 6) + 1) * intensity;
            const x = xPercent * strength * 0.5;
            const y = yPercent * strength * 0.5;

            word.style.transform = `translate3d(${x}px, ${y}px, 0)`;
        });
    };

    onMounted(() => {
        nextTick(() => {
            parallaxContainer.value = document.querySelector(".hero-bg");
            floatingWords.value = Array.from(
                document.querySelectorAll(".floating-word")
            );

            if (typeof window !== "undefined") {
                window.addEventListener("mousemove", handleMouseMove, {
                    passive: true,
                });
            }
        });
    });

    onUnmounted(() => {
        if (typeof window !== "undefined") {
            window.removeEventListener("mousemove", handleMouseMove);
        }
    });
</script>

<template>
    <div
        class="VPHero hero-enhanced"
        :class="{ 'has-image': image || heroImageSlotExists }"
    >
        <!-- Fantasy background with floating code snippets -->
        <div class="hero-bg" ref="parallaxContainer">
            <div class="bg-gradient"></div>

            <!-- Fantasy floating code snippets -->
            <div class="floating-words">
                <div
                    v-for="(category, catIndex) in [
                        'kubejs-events',
                        'kubejs-recipes',
                        'minecraft-api',
                        'forge-modding',
                        'minecraft-commands',
                    ]"
                    :key="category"
                    class="word-group"
                    :class="category"
                >
                    <span
                        v-for="i in 2"
                        :key="`${category}-${i}`"
                        class="floating-word"
                        :data-intensity="3 + (i % 2)"
                        :style="{
                            animationDelay: `${catIndex * 3 + i * 1.5}s`,
                            '--float-duration': `${20 + i * 6}s`,
                            '--hue-offset': `${catIndex * 72}deg`,
                        }"
                    >
                        {{ getCodeSnippet(category, i - 1) }}
                    </span>
                </div>
            </div>
        </div>

        <div class="container">
            <motion.div
                class="main"
                :variants="heroContainerVariants"
                initial="hidden"
                :whileInView="'visible'"
                :viewport="{ once: true, margin: '-50px' }"
            >
                <slot name="home-hero-info-before" />
                <slot name="home-hero-info">
                    <div class="heading">
                        <!-- Slice reveal title -->
                        <h1 v-if="name" class="name">
                            <motion.span
                                v-for="(word, wordIndex) in name.split(' ')"
                                :key="`word-${wordIndex}`"
                                class="word-wrapper"
                                :variants="wordVariants"
                                :custom="wordIndex"
                                initial="hidden"
                                :whileInView="'visible'"
                                :viewport="{ once: true, margin: '-100px' }"
                            >
                                <motion.span
                                    v-for="(letter, letterIndex) in word.split(
                                        ''
                                    )"
                                    :key="`letter-${wordIndex}-${letterIndex}`"
                                    class="letter"
                                    :variants="letterVariants"
                                    :custom="wordIndex * 5 + letterIndex"
                                    initial="hidden"
                                    :whileInView="'visible'"
                                    :viewport="{ once: true, margin: '-100px' }"
                                >
                                    {{ letter }}
                                </motion.span>
                                <span class="word-space">&nbsp;</span>
                            </motion.span>
                        </h1>

                        <motion.h2
                            v-if="text"
                            class="text"
                            :variants="subtitleVariants"
                            initial="hidden"
                            :whileInView="'visible'"
                            :viewport="{ once: true, margin: '-100px' }"
                            v-html="text"
                        />
                    </div>

                    <motion.p
                        v-if="tagline"
                        class="tagline"
                        :variants="taglineVariants"
                        initial="hidden"
                        :whileInView="'visible'"
                        :viewport="{ once: true, margin: '-100px' }"
                        v-html="tagline"
                    />
                </slot>
                <slot name="home-hero-info-after" />

                <motion.div
                    v-if="actions"
                    class="actions"
                    :variants="actionsVariants"
                    initial="hidden"
                    :whileInView="'visible'"
                    :viewport="{ once: true, margin: '-100px' }"
                >
                    <motion.div
                        v-for="action in actions"
                        :key="action.link"
                        class="action"
                        :variants="buttonVariants"
                        :whileHover="buttonHover"
                        :whileTap="buttonTap"
                    >
                        <VPButton
                            tag="a"
                            size="medium"
                            :theme="action.theme"
                            :text="action.text"
                            :href="action.link"
                            :target="action.target"
                            :rel="action.rel"
                        />
                    </motion.div>
                </motion.div>
                <slot name="home-hero-actions-after" />
            </motion.div>

            <motion.div
                v-if="image || heroImageSlotExists"
                class="image"
                :variants="imageVariants"
                :whileHover="imageHover"
                initial="hidden"
                :whileInView="'visible'"
                :viewport="{ once: true, margin: '-200px' }"
            >
                <div class="image-container">
                    <slot name="home-hero-image">
                        <VPImage v-if="image" class="image-src" :image />
                    </slot>
                </div>
            </motion.div>
        </div>

        <!-- Multi-layered bottom wave -->
        <div class="hero-wave">
            <svg
                viewBox="0 0 1200 120"
                preserveAspectRatio="none"
                class="wave-svg"
            >
                <path
                    d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V0Z"
                    opacity=".25"
                    class="shape-fill"
                ></path>
                <path
                    d="M0,0V15.81C13,36.92,27.64,56.86,47.69,72.05,99.41,111.27,165,111,224.58,91.58c31.15-10.15,60.09-26.07,89.67-39.8,40.92-19,84.73-46,130.83-49.67,36.26-2.85,70.9,9.42,98.6,31.56,31.77,25.39,62.32,62,103.63,73,40.44,10.79,81.35-6.69,119.13-24.28s75.16-39,116.92-43.05c59.73-5.85,113.28,22.88,168.9,38.84,30.2,8.66,59,6.17,87.09-7.5,22.43-10.89,48-26.93,60.65-49.24V0Z"
                    opacity=".5"
                    class="shape-fill"
                ></path>
                <path
                    d="M0,0V5.63C149.93,59,314.09,71.32,475.83,42.57c43-7.64,84.23-20.12,127.61-26.46,59-8.63,112.48,12.24,165.56,35.4C827.93,77.22,886,95.24,951.2,90c86.53-7,172.46-45.71,248.8-84.81V0Z"
                    class="shape-fill"
                ></path>
            </svg>
        </div>
    </div>
</template>

<style scoped>
    .VPHero.hero-enhanced {
        position: relative;
        margin-top: calc(
            (var(--vp-nav-height) + var(--vp-layout-top-height, 0px)) * -1
        );
        padding: calc(
                var(--vp-nav-height) + var(--vp-layout-top-height, 0px) + 80px
            )
            24px 120px;
        min-height: 100vh;
        display: flex;
        align-items: center;
        overflow: hidden;
        background: var(--vp-c-bg);
    }

    .container {
        position: relative;
        z-index: 10;
        display: flex;
        flex-direction: column;
        margin: 0 auto;
        max-width: 1400px;
        width: 100%;
    }

    @media (min-width: 960px) {
        .container {
            flex-direction: row;
            align-items: center;
        }
    }

    .main {
        position: relative;
        z-index: 10;
        order: 2;
        flex-grow: 1;
        flex-shrink: 0;
    }

    .VPHero.has-image .container {
        text-align: center;
    }

    @media (min-width: 960px) {
        .VPHero.has-image .container {
            text-align: left;
        }

        .main {
            order: 1;
            width: calc((100% / 5) * 3);
            padding-right: 64px;
        }

        .VPHero.has-image .main {
            max-width: none;
        }
    }

    .heading {
        display: flex;
        flex-direction: column;
        margin-bottom: 32px;
    }

    .name {
        margin: 0 0 24px 0;
        font-size: clamp(40px, 8vw, 80px);
        font-weight: 900;
        line-height: 1.1;
        letter-spacing: -0.02em;
        font-family: "Inter", "SF Pro Display", -apple-system,
            BlinkMacSystemFont, "Segoe UI", system-ui, sans-serif;
        background: linear-gradient(
            135deg,
            var(--vp-c-brand-1) 0%,
            var(--vp-c-brand-2) 30%,
            var(--vp-c-brand-3) 60%,
            var(--vp-c-brand-1) 100%
        );
        background-size: 300% 300%;
        animation: gradient-flow 6s ease-in-out infinite;
        -webkit-background-clip: text;
        background-clip: text;
        -webkit-text-fill-color: transparent;
        text-rendering: optimizeLegibility;
        -webkit-font-smoothing: antialiased;
        position: relative;
        filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
    }

    @keyframes gradient-flow {
        0%,
        100% {
            background-position: 0% 50%;
        }
        50% {
            background-position: 100% 50%;
        }
    }

    .word-wrapper {
        display: inline-block;
    }

    .letter {
        display: inline-block;
        transform-origin: bottom;
    }

    .word-space {
        display: inline-block;
        width: 0.3em;
    }

    .text {
        margin: 0 0 16px 0;
        font-size: clamp(24px, 4vw, 42px);
        font-weight: 700;
        line-height: 1.2;
        font-family: "Inter", "SF Pro Display", -apple-system,
            BlinkMacSystemFont, "Segoe UI", system-ui, sans-serif;
        color: var(--vp-c-text-1);
        text-rendering: optimizeLegibility;
        -webkit-font-smoothing: antialiased;
        position: relative;
        text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
        filter: drop-shadow(0 1px 3px rgba(0, 0, 0, 0.08));
    }

    .tagline {
        margin: 0 0 48px 0;
        font-size: clamp(18px, 2.5vw, 28px);
        font-weight: 500;
        line-height: 1.6;
        color: var(--vp-c-text-2);
        max-width: 600px;
        text-rendering: optimizeLegibility;
        -webkit-font-smoothing: antialiased;
        position: relative;
        text-shadow: 0 1px 2px rgba(0, 0, 0, 0.08);
        filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.05));
    }

    

    .VPHero.has-image .tagline {
        margin-left: auto;
        margin-right: auto;
    }

    @media (min-width: 960px) {
        .VPHero.has-image .tagline {
            margin-left: 0;
            margin-right: 0;
        }
    }

    .actions {
        display: flex;
        flex-wrap: wrap;
        gap: 16px;
        margin-top: 8px;
    }

    .VPHero.has-image .actions {
        justify-content: center;
    }

    @media (min-width: 960px) {
        .VPHero.has-image .actions {
            justify-content: flex-start;
        }
    }

    .action {
        flex-shrink: 0;
    }

    :deep(.VPButton) {
        padding: 14px 32px !important;
        font-size: 16px !important;
        font-weight: 600 !important;
        border-radius: 12px !important;
        min-width: 140px !important;
        color: var(--vp-c-text-1) !important;
        background: linear-gradient(
            135deg,
            rgba(var(--vp-c-bg-soft-rgb), 0.9),
            rgba(var(--vp-c-bg-alt-rgb), 0.95)
        ) !important;
        border: 2px solid rgba(var(--vp-c-divider-rgb), 0.8) !important;
        backdrop-filter: blur(12px) !important;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
        position: relative !important;
        overflow: hidden !important;
        box-shadow: 
            0 2px 8px rgba(0, 0, 0, 0.12),
            0 1px 3px rgba(0, 0, 0, 0.08),
            inset 0 1px 0 rgba(255, 255, 255, 0.1) !important;
        text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1) !important;
    }

    :deep(.VPButton::after) {
        content: "";
        position: absolute;
        top: 1px;
        left: 1px;
        right: 1px;
        bottom: 1px;
        border-radius: 10px;
        border: 1px solid rgba(0, 0, 0, 0.15);
        pointer-events: none;
        transition: all 0.3s ease;
    }

    :deep(.VPButton::before) {
        content: "";
        position: absolute;
        top: 0;
        left: -100%;
        width: 100%;
        height: 100%;
        background: linear-gradient(
            90deg,
            transparent,
            rgba(255, 255, 255, 0.2),
            transparent
        );
        transition: left 0.5s ease;
    }

    :deep(.VPButton:hover::before) {
        left: 100%;
    }

    :deep(.VPButton:hover) {
        background: linear-gradient(
            135deg,
            rgba(var(--vp-c-bg-alt-rgb), 0.95),
            rgba(var(--vp-c-brand-rgb), 0.15)
        ) !important;
        border-color: var(--vp-c-brand-1) !important;
        color: var(--vp-c-brand-1) !important;
        box-shadow: 
            0 4px 16px rgba(var(--vp-c-brand-rgb), 0.15),
            0 2px 8px rgba(0, 0, 0, 0.1),
            inset 0 1px 0 rgba(255, 255, 255, 0.15) !important;
        transform: translateY(-1px) !important;
    }

    :deep(.VPButton:hover::after) {
        border-color: rgba(var(--vp-c-brand-rgb), 0.5);
    }

    :deep(.VPButton.brand) {
        background: linear-gradient(
            135deg,
            var(--vp-c-brand-1),
            var(--vp-c-brand-2)
        ) !important;
        border-color: var(--vp-c-brand-1) !important;
        color: var(--vp-c-white) !important;
        box-shadow: 
            0 4px 12px rgba(var(--vp-c-brand-rgb), 0.4),
            0 2px 6px rgba(0, 0, 0, 0.15),
            inset 0 1px 0 rgba(255, 255, 255, 0.2) !important;
        text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2) !important;
    }

    :deep(.VPButton.brand::after) {
        border-color: rgba(255, 255, 255, 0.4);
    }

    :deep(.VPButton.brand:hover) {
        background: linear-gradient(
            135deg,
            var(--vp-c-brand-2),
            var(--vp-c-brand-3)
        ) !important;
        border-color: var(--vp-c-brand-2) !important;
        color: var(--vp-c-white) !important;
    }

    :deep(.VPButton.brand:hover::after) {
        border-color: rgba(255, 255, 255, 0.5);
    }

    :deep(.VPButton.alt) {
        background: linear-gradient(
            135deg,
            rgba(var(--vp-c-bg-soft-rgb), 0.7),
            rgba(var(--vp-c-bg-alt-rgb), 0.8)
        ) !important;
        border-color: rgba(var(--vp-c-divider-rgb), 0.5) !important;
        color: var(--vp-c-text-1) !important;
    }

    .dark :deep(.VPButton) {
        background: rgba(255, 255, 255, 0.08) !important;
        border: 2px solid rgba(255, 255, 255, 0.4) !important;
        color: rgba(255, 255, 255, 0.9) !important;
        backdrop-filter: blur(8px) !important;
        box-shadow: 
            0 2px 8px rgba(0, 0, 0, 0.4),
            0 1px 3px rgba(0, 0, 0, 0.2),
            inset 0 1px 0 rgba(255, 255, 255, 0.1) !important;
        text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3) !important;
    }

    .dark :deep(.VPButton::after) {
        border-color: rgba(255, 255, 255, 0.5);
    }

    .dark :deep(.VPButton:hover) {
        background: rgba(var(--vp-c-brand-rgb), 0.15) !important;
        border-color: var(--vp-c-brand-1) !important;
        color: var(--vp-c-brand-1) !important;
    }

    .dark :deep(.VPButton:hover::after) {
        border-color: rgba(var(--vp-c-brand-rgb), 0.6);
    }

    .dark :deep(.VPButton.alt) {
        background: rgba(255, 255, 255, 0.06) !important;
        border-color: rgba(255, 255, 255, 0.35) !important;
        color: rgba(255, 255, 255, 0.85) !important;
    }

    .dark :deep(.VPButton.alt::after) {
        border-color: rgba(255, 255, 255, 0.45);
    }

    .dark :deep(.VPButton.alt:hover) {
        background: rgba(255, 255, 255, 0.12) !important;
        border-color: rgba(255, 255, 255, 0.3) !important;
        color: var(--vp-c-white) !important;
    }

    .dark :deep(.VPButton.alt:hover::after) {
        border-color: rgba(255, 255, 255, 0.7);
    }



    .image {
        order: 1;
        margin: -40px -24px 40px;
        position: relative;
    }

    @media (min-width: 960px) {
        .image {
            flex-grow: 1;
            order: 2;
            margin: 0;
            min-height: 100%;
            width: calc((100% / 5) * 2);
        }
    }

    .image-container {
        position: relative;
        margin: 0 auto;
        width: 360px;
        height: 360px;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    @media (min-width: 640px) {
        .image-container {
            width: 480px;
            height: 480px;
        }
    }

    @media (min-width: 960px) {
        .image-container {
            width: 100%;
            height: 100%;
            max-width: 600px;
            max-height: 600px;
        }
    }

    .image-src {
        position: relative;
        z-index: 2;
        width: 100%;
        height: 100%;
        object-fit: contain;
        border-radius: 16px;
        transition: all 0.3s ease;
    }

    .hero-bg {
        position: absolute;
        inset: 0;
        overflow: hidden;
        z-index: 1;
    }

    .bg-gradient {
        position: absolute;
        inset: 0;
        background: radial-gradient(
                circle at 30% 20%,
                rgba(var(--vp-c-brand-rgb), 0.25) 0%,
                transparent 60%
            ),
            radial-gradient(
                circle at 70% 80%,
                rgba(var(--vp-c-brand-2-rgb), 0.2) 0%,
                transparent 60%
            ),
            radial-gradient(
                circle at 50% 50%,
                rgba(var(--vp-c-brand-3-rgb), 0.15) 0%,
                transparent 70%
            ),
            linear-gradient(
                135deg,
                rgba(var(--vp-c-brand-rgb), 0.1) 0%,
                rgba(var(--vp-c-brand-2-rgb), 0.08) 50%,
                transparent 100%
            );
        animation: bg-shift 15s ease-in-out infinite;
    }

    /* Sunshine light ray effects */
    .bg-gradient::before {
        content: "";
        position: absolute;
        inset: 0;
        background: linear-gradient(
                45deg,
                transparent 25%,
                rgba(255, 255, 255, 0.15) 50%,
                transparent 75%
            ),
            linear-gradient(
                -45deg,
                transparent 25%,
                rgba(255, 255, 255, 0.12) 50%,
                transparent 75%
            ),
            linear-gradient(
                135deg,
                transparent 35%,
                rgba(var(--vp-c-brand-rgb), 0.1) 50%,
                transparent 65%
            );
        animation: sunshine-rays 20s ease-in-out infinite;
        pointer-events: none;
    }

    .dark .bg-gradient::before {
        background: linear-gradient(
                45deg,
                transparent 25%,
                rgba(255, 255, 255, 0.08) 50%,
                transparent 75%
            ),
            linear-gradient(
                -45deg,
                transparent 25%,
                rgba(255, 255, 255, 0.06) 50%,
                transparent 75%
            ),
            linear-gradient(
                135deg,
                transparent 35%,
                rgba(var(--vp-c-brand-rgb), 0.12) 50%,
                transparent 65%
            );
    }

    @keyframes bg-shift {
        0%,
        100% {
            background-position: 0% 0%, 100% 100%, 0% 50%;
            opacity: 1;
        }
        50% {
            background-position: 100% 100%, 0% 0%, 100% 50%;
            opacity: 0.8;
        }
    }

    @keyframes sunshine-rays {
        0%,
        100% {
            transform: rotate(0deg) scale(1);
            opacity: 0.6;
        }
        25% {
            transform: rotate(2deg) scale(1.02);
            opacity: 0.8;
        }
        50% {
            transform: rotate(-1deg) scale(1.01);
            opacity: 1;
        }
        75% {
            transform: rotate(1deg) scale(1.02);
            opacity: 0.7;
        }
    }



    .floating-words {
        position: absolute;
        inset: 0;
        pointer-events: none;
        overflow: hidden;
    }

    .word-group {
        position: absolute;
        width: 100%;
        height: 100%;
    }

    .floating-word {
        position: absolute;
        font-family: "Fira Code", "JetBrains Mono", "SF Mono", "Consolas",
            monospace;
        font-weight: 500;
        font-size: 14px;
        opacity: 0;
        color: var(--vp-c-text-3);
        pointer-events: none;
        user-select: none;
        white-space: nowrap;
        transform-origin: center;
        animation: float-curve var(--float-duration, 20s) ease-in-out infinite;
        transition: all 0.3s ease;
        text-shadow: 0 1px 3px rgba(0, 0, 0, 0.15);
        filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
        backdrop-filter: blur(1px);
    }

    .dark .floating-word {
        opacity: 0;
        color: var(--vp-c-text-2);
        text-shadow: 0 1px 4px rgba(0, 0, 0, 0.4);
        filter: drop-shadow(0 2px 6px rgba(0, 0, 0, 0.3));
    }

    /* Light theme colors - proper visibility */
    .word-group.kubejs-events .floating-word {
        color: #7c3aed;
    }
    .word-group.kubejs-recipes .floating-word {
        color: #059669;
    }
    .word-group.minecraft-api .floating-word {
        color: #d97706;
    }
    .word-group.forge-modding .floating-word {
        color: #dc2626;
    }
    .word-group.minecraft-commands .floating-word {
        color: #2563eb;
    }

    /* Different colors for different categories in dark theme */
    .dark .word-group.kubejs-events .floating-word {
        color: #a78bfa;
    }
    .dark .word-group.kubejs-recipes .floating-word {
        color: #34d399;
    }
    .dark .word-group.minecraft-api .floating-word {
        color: #fbbf24;
    }
    .dark .word-group.forge-modding .floating-word {
        color: #f87171;
    }
    .dark .word-group.minecraft-commands .floating-word {
        color: #60a5fa;
    }

    /* Alternate curved paths for visual variety */
    .word-group.kubejs-events .floating-word:nth-child(odd) {
        animation-name: float-curve;
    }
    .word-group.kubejs-events .floating-word:nth-child(even) {
        animation-name: float-curve-bottom;
    }

    .word-group.kubejs-recipes .floating-word:nth-child(odd) {
        animation-name: float-curve-bottom;
    }
    .word-group.kubejs-recipes .floating-word:nth-child(even) {
        animation-name: float-curve;
    }

    .word-group.minecraft-api .floating-word:nth-child(odd) {
        animation-name: float-curve;
    }
    .word-group.minecraft-api .floating-word:nth-child(even) {
        animation-name: float-curve-bottom;
    }

    .word-group.forge-modding .floating-word:nth-child(odd) {
        animation-name: float-curve-bottom;
    }
    .word-group.forge-modding .floating-word:nth-child(even) {
        animation-name: float-curve;
    }

    .word-group.minecraft-commands .floating-word:nth-child(odd) {
        animation-name: float-curve;
    }
    .word-group.minecraft-commands .floating-word:nth-child(even) {
        animation-name: float-curve-bottom;
    }

    @keyframes float-curve {
        0% {
            transform: translateX(-150px) translateY(20px) rotate(-5deg)
                scale(0.7);
            opacity: 0;
        }
        8% {
            opacity: 0.4;
        }
        20% {
            transform: translateX(-80px) translateY(-25px) rotate(3deg)
                scale(0.85);
            opacity: 0.7;
        }
        40% {
            transform: translateX(-20px) translateY(-45px) rotate(0deg) scale(1);
            opacity: 0.9;
        }
        60% {
            transform: translateX(40px) translateY(-35px) rotate(-2deg) scale(1);
            opacity: 1;
        }
        80% {
            transform: translateX(100px) translateY(-15px) rotate(2deg)
                scale(0.9);
            opacity: 0.6;
        }
        92% {
            opacity: 0.3;
        }
        100% {
            transform: translateX(150px) translateY(10px) rotate(5deg)
                scale(0.8);
            opacity: 0;
        }
    }



    /* Bottom curved path animation for variety */
    @keyframes float-curve-bottom {
        0% {
            transform: translateX(-150px) translateY(-20px) rotate(5deg)
                scale(0.7);
            opacity: 0;
        }
        8% {
            opacity: 0.4;
        }
        20% {
            transform: translateX(-80px) translateY(25px) rotate(-3deg)
                scale(0.85);
            opacity: 0.7;
        }
        40% {
            transform: translateX(-20px) translateY(45px) rotate(0deg) scale(1);
            opacity: 0.9;
        }
        60% {
            transform: translateX(40px) translateY(35px) rotate(2deg) scale(1);
            opacity: 1;
        }
        80% {
            transform: translateX(100px) translateY(15px) rotate(-2deg)
                scale(0.9);
            opacity: 0.6;
        }
        92% {
            opacity: 0.3;
        }
        100% {
            transform: translateX(150px) translateY(-10px) rotate(-5deg)
                scale(0.8);
            opacity: 0;
        }
    }

    /* Clear positioning for different code categories */
    .word-group.kubejs-events .floating-word:nth-child(1) {
        top: 15%;
        left: 8%;
    }
    .word-group.kubejs-events .floating-word:nth-child(2) {
        top: 60%;
        left: 85%;
    }

    .word-group.kubejs-recipes .floating-word:nth-child(1) {
        top: 25%;
        left: 75%;
    }
    .word-group.kubejs-recipes .floating-word:nth-child(2) {
        top: 70%;
        left: 12%;
    }

    .word-group.minecraft-api .floating-word:nth-child(1) {
        top: 35%;
        left: 50%;
    }
    .word-group.minecraft-api .floating-word:nth-child(2) {
        top: 80%;
        left: 40%;
    }

    .word-group.forge-modding .floating-word:nth-child(1) {
        top: 10%;
        left: 30%;
    }
    .word-group.forge-modding .floating-word:nth-child(2) {
        top: 75%;
        left: 70%;
    }

    .word-group.minecraft-commands .floating-word:nth-child(1) {
        top: 45%;
        left: 85%;
    }
    .word-group.minecraft-commands .floating-word:nth-child(2) {
        top: 85%;
        left: 15%;
    }

    .hero-wave {
        position: absolute;
        bottom: 0;
        left: 0;
        width: 100%;
        overflow: hidden;
        line-height: 0;
        transform: rotate(180deg);
        z-index: 5;
    }

    .wave-svg {
        position: relative;
        display: block;
        width: calc(100% + 1.3px);
        height: 80px;
    }

    .shape-fill {
        fill: var(--vp-c-bg);
        filter: drop-shadow(0 -2px 4px rgba(0, 0, 0, 0.1));
    }

    .dark .shape-fill {
        fill: #1B1B1F;
        filter: drop-shadow(0 -2px 4px rgba(0, 0, 0, 0.3));
    }

    .dark .name {
        filter: drop-shadow(0 3px 6px rgba(0, 0, 0, 0.4));
    }

    .dark .text {
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        filter: drop-shadow(0 2px 5px rgba(0, 0, 0, 0.2));
    }

    .dark .tagline {
        text-shadow: 0 2px 3px rgba(0, 0, 0, 0.25);
        filter: drop-shadow(0 1px 3px rgba(0, 0, 0, 0.15));
    }

    /* Responsive design */
    @media (max-width: 768px) {
        .VPHero.hero-enhanced {
            min-height: 90vh;
            padding: calc(
                    var(--vp-nav-height) + var(--vp-layout-top-height, 0px) +
                        40px
                )
                16px 80px;
        }

        .floating-word {
            font-size: 11px !important;
            opacity: 0.1 !important;
        }

        .dark .floating-word {
            opacity: 0.2 !important;
        }

        .main {
            text-align: center;
        }

        .name {
            font-size: clamp(32px, 10vw, 60px) !important;
        }

        .text {
            font-size: clamp(20px, 6vw, 32px) !important;
        }

        .tagline {
            font-size: clamp(16px, 4vw, 22px) !important;
            margin-bottom: 32px !important;
        }

        .actions {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 12px 10px;
            max-width: 320px;
            margin: 0 auto;
            justify-items: center;
        }

        .action {
            width: 100%;
            display: flex;
            justify-content: center;
        }

        :deep(.VPButton) {
            width: 100% !important;
            min-width: unset !important;
            max-width: 140px !important;
            padding: 8px 14px !important;
            font-size: 14px !important;
            font-weight: 600 !important;
            min-height: 36px !important;
            border-radius: 14px !important;
        }
    }

    @media (max-width: 480px) {
        .name {
            font-size: clamp(28px, 12vw, 48px) !important;
            margin-bottom: 16px !important;
        }

        .text {
            font-size: clamp(18px, 8vw, 28px) !important;
            margin-bottom: 12px !important;
        }

        .tagline {
            font-size: clamp(14px, 5vw, 20px) !important;
            margin-bottom: 24px !important;
        }

        .actions {
            grid-template-columns: 1fr 1fr !important;
            max-width: 280px !important;
            gap: 10px 8px !important;
        }

        :deep(.VPButton) {
            max-width: 125px !important;
            padding: 6px 10px !important;
            font-size: 13px !important;
            min-height: 32px !important;
            border-radius: 12px !important;
        }
    }

    /* Accessibility */
    @media (prefers-reduced-motion: reduce) {
        * {
            animation-duration: 0.01ms !important;
            animation-iteration-count: 1 !important;
            transition-duration: 0.01ms !important;
        }

        .floating-word {
            animation: none !important;
        }
    }
</style>
