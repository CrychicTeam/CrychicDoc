<script setup lang="ts">
    import { type Ref, inject } from "vue";
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
                "PlayerEvents.chat(event => {})",
                "EntityEvents.spawned(event => {})",
                "ClientEvents.init(event => {})",
            ],
            "kubejs-recipes": [
                'event.shaped("item", ["ABC", "DEF"])',
                'event.shapeless("output", ["input1", "input2"])',
                'event.smelting("output", "input")',
                'event.remove({id: "minecraft:stick"})',
                'event.replaceInput({}, "old", "new")',
                'event.stonecutting("output", "input")',
                'event.campfireCooking("output", "input")',
                'event.smoking("output", "input")',
                'event.blasting("output", "input")',
            ],
            "minecraft-api": [
                "Level level = player.level()",
                "BlockPos pos = new BlockPos(x, y, z)",
                "ItemStack stack = new ItemStack(Items.DIAMOND)",
                "BlockState state = level.getBlockState(pos)",
                "Entity entity = level.getEntity(uuid)",
                'Component.literal("Hello World")',
                'ResourceLocation.fromNamespaceAndPath("mod", "item")',
                "GameProfile profile = player.getGameProfile()",
            ],
            "forge-modding": [
                '@Mod("modid")',
                '@EventBusSubscriber(modid = "modid")',
                "@SubscribeEvent",
                'ForgeRegistries.ITEMS.register("item", item)',
                "FMLJavaModLoadingContext.get().getModEventBus()",
                "ModLoadingContext.get().registerConfig()",
                "DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {})",
                "IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus()",
            ],
            "neoforge-api": [
                'NeoForgeRegistries.BLOCKS.register("block", block)',
                "NeoForgeEventBus.EVENT_BUS.addListener()",
                "ModContainer container = ModLoadingContext.get().getActiveContainer()",
                "NeoForge.EVENT_BUS.register(this)",
                "@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)",
                "IModBus modBus = NeoForgeEventBus.NEOFORGE",
                "CapabilityManager.get().register()",
                "ModConfigEvent.Loading event",
            ],
            "minecraft-commands": [
                "/give @p minecraft:diamond 64",
                "/tp @a 0 100 0",
                "/gamemode creative @s",
                "/effect give @p minecraft:speed 60 2",
                "/summon minecraft:zombie ~ ~ ~",
                "/fill ~-5 ~-1 ~-5 ~5 ~-1 ~5 minecraft:stone",
                "/setblock ~ ~ ~ minecraft:chest{Items:[]}",
                "/scoreboard players set @p score 100",
            ],
            "data-generation": [
                "DataGenerator generator = event.getGenerator()",
                "generator.addProvider(new RecipeProvider())",
                "generator.addProvider(new LootTableProvider())",
                "generator.addProvider(new BlockStateProvider())",
                "generator.addProvider(new ItemModelProvider())",
                "generator.addProvider(new LanguageProvider())",
                "PackOutput packOutput = generator.getPackOutput()",
                "ExistingFileHelper fileHelper = event.getExistingFileHelper()",
            ],
            "capabilities-api": [
                "IItemHandler itemHandler = stack.getCapability()",
                "IEnergyStorage energy = blockEntity.getCapability()",
                "IFluidHandler fluidHandler = tank.getCapability()",
                "LazyOptional<IItemHandler> optional = LazyOptional.of()",
                "CapabilityProvider.create()",
                "BlockCapabilityCache.create()",
                "ItemCapabilityCache.create()",
                "capability.orElse(null)",
            ],
        };

        const categorySnippets =
            snippets[category] || snippets["kubejs-events"];
        return categorySnippets[index % categorySnippets.length];
    };

    // Enhanced animation variants for bigger, more impressive design
    const heroContainerVariants = {
        hidden: {
            opacity: 0,
            y: 40,
        },
        visible: {
            opacity: 1,
            y: 0,
            transition: {
                duration: 1.2,
                ease: "easeOut",
                when: "beforeChildren",
                staggerChildren: 0.2,
            },
        },
    };

    const titleVariants = {
        hidden: {
            opacity: 0,
            y: 60,
            scale: 0.9,
        },
        visible: {
            opacity: 1,
            y: 0,
            scale: 1,
            transition: {
                type: "spring",
                stiffness: 100,
                damping: 25,
                duration: 1.5,
            },
        },
    };

    const subtitleVariants = {
        hidden: {
            opacity: 0,
            y: 40,
        },
        visible: {
            opacity: 1,
            y: 0,
            transition: {
                duration: 1,
                ease: [0.25, 0.46, 0.45, 0.94],
                delay: 0.3,
            },
        },
    };

    const taglineVariants = {
        hidden: {
            opacity: 0,
            y: 30,
        },
        visible: {
            opacity: 1,
            y: 0,
            transition: {
                duration: 0.8,
                ease: "easeOut",
                delay: 0.5,
            },
        },
    };

    const actionsVariants = {
        hidden: {
            opacity: 0,
            y: 40,
        },
        visible: {
            opacity: 1,
            y: 0,
            transition: {
                duration: 0.8,
                ease: "easeOut",
                delay: 0.7,
                when: "beforeChildren",
                staggerChildren: 0.08,
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
                stiffness: 200,
                damping: 20,
            },
        },
    };

    const buttonHover = {
        scale: 1.05,
        y: -3,
        transition: {
            type: "spring",
            stiffness: 400,
            damping: 25,
        },
    };

    const buttonPress = {
        scale: 0.98,
        transition: {
            type: "spring",
            stiffness: 500,
            damping: 30,
        },
    };

    const imageVariants = {
        hidden: {
            opacity: 0,
            scale: 0.7,
            rotate: -10,
        },
        visible: {
            opacity: 1,
            scale: 1,
            rotate: 0,
            transition: {
                duration: 1.5,
                ease: "easeOut",
                delay: 0.4,
            },
        },
    };

    const imageHover = {
        scale: 1.05,
        rotate: 5,
        transition: {
            type: "spring",
            stiffness: 300,
            damping: 20,
        },
    };
</script>

<template>
    <div
        class="VPHero hero-enhanced"
        :class="{ 'has-image': image || heroImageSlotExists }"
    >
        <!-- Enhanced background decorations with floating text -->
        <div class="hero-bg">
            <div class="bg-gradient"></div>
            <div class="bg-pattern"></div>
            <div class="floating-words">
                <div
                    class="word-group kubejs-events"
                    v-for="i in 5"
                    :key="`kubejs-events-${i}`"
                >
                    <span
                        class="floating-word"
                        :style="{ '--delay': `${i * 1.5}s` }"
                    >
                        {{ getCodeSnippet("kubejs-events", i - 1) }}
                    </span>
                </div>
                <div
                    class="word-group kubejs-recipes"
                    v-for="i in 5"
                    :key="`kubejs-recipes-${i}`"
                >
                    <span
                        class="floating-word"
                        :style="{ '--delay': `${i * 2}s` }"
                    >
                        {{ getCodeSnippet("kubejs-recipes", i - 1) }}
                    </span>
                </div>
                <div
                    class="word-group minecraft-api"
                    v-for="i in 4"
                    :key="`minecraft-api-${i}`"
                >
                    <span
                        class="floating-word"
                        :style="{ '--delay': `${i * 2.5}s` }"
                    >
                        {{ getCodeSnippet("minecraft-api", i - 1) }}
                    </span>
                </div>
                <div
                    class="word-group forge-modding"
                    v-for="i in 4"
                    :key="`forge-modding-${i}`"
                >
                    <span
                        class="floating-word"
                        :style="{ '--delay': `${i * 3}s` }"
                    >
                        {{ getCodeSnippet("forge-modding", i - 1) }}
                    </span>
                </div>
                <div
                    class="word-group neoforge-api"
                    v-for="i in 4"
                    :key="`neoforge-api-${i}`"
                >
                    <span
                        class="floating-word"
                        :style="{ '--delay': `${i * 3.5}s` }"
                    >
                        {{ getCodeSnippet("neoforge-api", i - 1) }}
                    </span>
                </div>
                <div
                    class="word-group minecraft-commands"
                    v-for="i in 4"
                    :key="`minecraft-commands-${i}`"
                >
                    <span
                        class="floating-word"
                        :style="{ '--delay': `${i * 4}s` }"
                    >
                        {{ getCodeSnippet("minecraft-commands", i - 1) }}
                    </span>
                </div>
                <div
                    class="word-group data-generation"
                    v-for="i in 3"
                    :key="`data-generation-${i}`"
                >
                    <span
                        class="floating-word"
                        :style="{ '--delay': `${i * 4.5}s` }"
                    >
                        {{ getCodeSnippet("data-generation", i - 1) }}
                    </span>
                </div>
                <div
                    class="word-group capabilities-api"
                    v-for="i in 3"
                    :key="`capabilities-api-${i}`"
                >
                    <span
                        class="floating-word"
                        :style="{ '--delay': `${i * 5}s` }"
                    >
                        {{ getCodeSnippet("capabilities-api", i - 1) }}
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
                :viewport="{ once: true, margin: '-100px' }"
            >
                <slot name="home-hero-info-before" />
                <slot name="home-hero-info">
                    <motion.div class="heading" :variants="titleVariants">
                        <motion.span
                            v-if="name"
                            v-html="name"
                            class="name clip"
                            :whileHover="{
                                scale: 1.02,
                                transition: { duration: 0.3 },
                            }"
                        />
                        <motion.span
                            v-if="text"
                            v-html="text"
                            class="text"
                            :variants="subtitleVariants"
                        />
                    </motion.div>
                    <motion.p
                        v-if="tagline"
                        v-html="tagline"
                        class="tagline"
                        :variants="taglineVariants"
                    />
                </slot>
                <slot name="home-hero-info-after" />

                <motion.div
                    v-if="actions"
                    class="actions"
                    :variants="actionsVariants"
                >
                    <motion.div
                        v-for="action in actions"
                        :key="action.link"
                        class="action"
                        :variants="buttonVariants"
                        :whileHover="buttonHover"
                        :whilePress="buttonPress"
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
                    <div class="image-glow"></div>
                    <div class="image-bg"></div>
                    <slot name="home-hero-image">
                        <VPImage v-if="image" class="image-src" :image />
                    </slot>
                </div>
            </motion.div>
        </div>

        <!-- Bottom wave for seamless transition -->
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
    /* Use VitePress default structure with enhanced animations */
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
        /* White background for light mode */
        background: #ffffff;
    }

    /* Dark mode background */
    .dark .VPHero.hero-enhanced {
        background: var(--vp-c-bg);
    }

    @media (min-width: 640px) {
        .VPHero.hero-enhanced {
            padding: calc(
                    var(--vp-nav-height) + var(--vp-layout-top-height, 0px) +
                        120px
                )
                48px 160px;
        }
    }

    @media (min-width: 960px) {
        .VPHero.hero-enhanced {
            padding: calc(
                    var(--vp-nav-height) + var(--vp-layout-top-height, 0px) +
                        140px
                )
                64px 180px;
        }
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
    }

    @media (min-width: 960px) {
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

    .name,
    .text {
        width: fit-content;
        max-width: 100%;
        letter-spacing: -0.02em;
        line-height: 1.1;
        font-size: 48px;
        font-weight: 900;
        white-space: pre-wrap;
        overflow: visible !important;
        text-overflow: unset !important;
        text-rendering: optimizeLegibility;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
    }

    .VPHero.has-image .name,
    .VPHero.has-image .text {
        margin: 0 auto;
    }

    .name {
        color: var(--vp-home-hero-name-color);
        margin-bottom: 16px;
    }

    /* Fixed brand text styling for both light and dark modes */
    .clip {
        background: linear-gradient(
            135deg,
            var(--vp-c-brand-1) 0%,
            var(--vp-c-brand-2) 50%,
            var(--vp-c-brand-3) 100%
        ) !important;
        -webkit-background-clip: text !important;
        background-clip: text !important;
        -webkit-text-fill-color: transparent !important;
        filter: drop-shadow(0 4px 8px rgba(var(--vp-c-brand-rgb), 0.3));
    }

    /* Ensure name is visible in light mode */
    .name:not(.clip) {
        color: var(--vp-c-text-1) !important;
    }

    @media (min-width: 640px) {
        .name,
        .text {
            font-size: 64px;
            line-height: 1.1;
        }
    }

    @media (min-width: 960px) {
        .name,
        .text {
            font-size: 72px;
            line-height: 1.1;
        }

        .VPHero.has-image .name,
        .VPHero.has-image .text {
            margin: 0;
        }
    }

    .tagline {
        padding-top: 16px;
        max-width: 100%;
        line-height: 1.6;
        font-size: 20px;
        font-weight: 500;
        white-space: pre-wrap;
        color: var(--vp-c-text-2);
        overflow: visible !important;
        text-overflow: unset !important;
        text-rendering: optimizeLegibility;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        margin-bottom: 48px;
    }

    .VPHero.has-image .tagline {
        margin: 16px auto 48px;
    }

    @media (min-width: 640px) {
        .tagline {
            font-size: 24px;
            line-height: 1.6;
            margin-bottom: 56px;
        }
    }

    @media (min-width: 960px) {
        .tagline {
            font-size: 28px;
            line-height: 1.6;
        }

        .VPHero.has-image .tagline {
            margin: 16px 0 56px;
        }
    }

    /* Enhanced Actions with better button sizing */
    .actions {
        display: flex;
        flex-wrap: wrap;
        gap: 12px;
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

    /* Better button styling - more curved borders */
    :deep(.VPButton) {
        padding: 12px 24px !important;
        font-size: 16px !important;
        font-weight: 600 !important;
        border-radius: 20px !important;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
        backdrop-filter: blur(10px) !important;
        min-width: 120px !important;
        border: 1px solid rgba(var(--vp-c-brand-rgb), 0.2) !important;
    }

    :deep(.VPButton:hover) {
        transform: translateY(-1px) !important;
        box-shadow: 0 8px 20px rgba(var(--vp-c-brand-rgb), 0.3) !important;
    }

    @media (min-width: 640px) {
        :deep(.VPButton) {
            padding: 14px 28px !important;
            font-size: 17px !important;
            min-width: 140px !important;
        }
    }

    /* Enhanced image styling with better effects - fixed blur issues */
    .image {
        order: 1;
        margin: -40px -24px 40px;
        position: relative;
    }

    @media (min-width: 640px) {
        .image {
            margin: -60px -24px 60px;
        }
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
            transform: none;
        }
    }

    .image-bg {
        position: absolute;
        inset: 0;
        border-radius: 20px;
    }

    .image-src {
        position: relative;
        z-index: 2;
        width: 100%;
        height: 100%;
        object-fit: contain;
        /* Completely clear image with no blur or filters */
        border-radius: 16px;
        transition: all 0.3s ease;
    }

    .image-container:hover .image-src {
        transform: scale(1.02);
        /* Still no filters on hover */
    }

    @keyframes pulse {
        0%,
        100% {
            opacity: 0.8;
            transform: translate(-50%, -50%) scale(1);
        }
        50% {
            opacity: 1;
            transform: translate(-50%, -50%) scale(1.02);
        }
    }

    /* Enhanced floating background elements with bigger, clearer code snippets */
    .hero-bg {
        position: absolute;
        inset: 0;
        overflow: hidden;
        z-index: 1;
        /* Fixed background colors */
        background: #ffffff;
    }

    .dark .hero-bg {
        background: #1b1b1f;
    }

    .bg-gradient {
        position: absolute;
        inset: 0;
    }

    .bg-pattern {
        position: absolute;
        inset: 0;
        opacity: 0.8;
    }

    .floating-words {
        position: absolute;
        inset: 0;
        pointer-events: none;
    }

    .word-group {
        position: absolute;
        width: 100%;
        height: 100%;
    }

    .floating-word {
        position: absolute;
        font-family: "JetBrains Mono", "Fira Code", "SF Mono", monospace;
        font-weight: 700;
        pointer-events: none;
        opacity: 0; /* Start completely invisible */
        animation: float-diagonal 20s linear infinite;
        animation-delay: var(--delay);
        animation-fill-mode: forwards; /* Keep final state */
        white-space: nowrap;
        user-select: none;
        font-size: 32px;
        visibility: hidden; /* Additional layer of hiding */
        animation-play-state: paused; /* Pause until ready */
    }

    /* Only show and start animations when component is mounted and ready */
    .VPHero.hero-enhanced .floating-word {
        visibility: visible;
        animation-play-state: running;
    }

    .dark .floating-word {
        animation-name: float-diagonal-dark;
    }

    @keyframes float-diagonal-dark {
        0% {
            transform: translate(-200px, 150px) rotate(-8deg);
            opacity: 0;
        }
        5% {
            opacity: 0.8; /* Slightly less opacity in dark mode */
        }
        10% {
            opacity: 0.8;
        }
        90% {
            opacity: 0.8;
        }
        95% {
            opacity: 0;
        }
        100% {
            transform: translate(calc(100vw + 200px), -150px) rotate(8deg);
            opacity: 0;
        }
    }

    .word-group.kubejs-events .floating-word {
        color: #c186e6; /* KubeJS purple */
        top: 8%;
        left: 3%;
        animation-duration: 25s;
        font-size: 18px;
        font-weight: 600;
        padding: 6px 12px;
        border-radius: 8px;
        backdrop-filter: blur(4px);
    }

    .word-group.kubejs-recipes .floating-word {
        color: #c186e6; /* KubeJS purple */
        top: 75%;
        left: 85%;
        animation-duration: 28s;
        animation-direction: reverse;
        font-size: 17px;
        font-weight: 600;
        padding: 6px 12px;
        border-radius: 8px;
        backdrop-filter: blur(4px);
    }

    .word-group.minecraft-api .floating-word {
        color: #259925; /* Minecraft green */
        top: 35%;
        left: 92%;
        animation-duration: 32s;
        font-size: 16px;
        font-weight: 500;
        padding: 5px 10px;
        border-radius: 6px;
        backdrop-filter: blur(4px);
    }

    .word-group.forge-modding .floating-word {
        color: #dfa86a; /* Forge orange */
        top: 55%;
        left: 12%;
        animation-duration: 26s;
        animation-direction: reverse;
        font-size: 19px;
        font-weight: 600;
        padding: 7px 14px;
        border-radius: 8px;
        backdrop-filter: blur(4px);
    }

    .word-group.neoforge-api .floating-word {
        color: #e68c37; /* NeoForge darker orange */
        top: 18%;
        left: 65%;
        animation-duration: 30s;
        font-size: 17px;
        font-weight: 600;
        padding: 6px 12px;
        border-radius: 8px;
        backdrop-filter: blur(4px);
    }

    .word-group.minecraft-commands .floating-word {
        color: #101b81; /* Command blue */
        top: 88%;
        left: 42%;
        animation-duration: 23s;
        animation-direction: reverse;
        font-size: 18px;
        font-weight: 600;
        padding: 6px 12px;
        border-radius: 8px;
        backdrop-filter: blur(4px);
    }

    .word-group.data-generation .floating-word {
        color: #101b81; /* Data generation blue */
        top: 2%;
        left: 28%;
        animation-duration: 34s;
        font-size: 16px;
        font-weight: 500;
        padding: 5px 10px;
        border-radius: 6px;
        backdrop-filter: blur(4px);
    }

    .word-group.capabilities-api .floating-word {
        color: #a73232; /* Advanced red */
        top: 68%;
        left: 8%;
        animation-duration: 27s;
        animation-direction: reverse;
        font-size: 17px;
        font-weight: 600;
        padding: 6px 12px;
        border-radius: 8px;
        backdrop-filter: blur(4px);
    }

    /* Enhanced spacing and staggered positioning to prevent overlap */
    .word-group:nth-child(1) .floating-word {
        animation-delay: calc(var(--delay) + 1s); /* Add base delay */
    }
    .word-group:nth-child(2) .floating-word {
        animation-delay: calc(var(--delay) + 4s);
    }
    .word-group:nth-child(3) .floating-word {
        animation-delay: calc(var(--delay) + 7s);
    }
    .word-group:nth-child(4) .floating-word {
        animation-delay: calc(var(--delay) + 10s);
    }
    .word-group:nth-child(5) .floating-word {
        animation-delay: calc(var(--delay) + 13s);
    }
    .word-group:nth-child(6) .floating-word {
        animation-delay: calc(var(--delay) + 16s);
    }
    .word-group:nth-child(7) .floating-word {
        animation-delay: calc(var(--delay) + 19s);
    }
    .word-group:nth-child(8) .floating-word {
        animation-delay: calc(var(--delay) + 22s);
    }

    @keyframes float-diagonal {
        0% {
            transform: translate(-200px, 150px) rotate(-8deg);
            opacity: 0;
        }
        5% {
            opacity: 0.9; /* Fade in gradually after movement starts */
        }
        10% {
            opacity: 0.9;
        }
        90% {
            opacity: 0.9;
        }
        95% {
            opacity: 0; /* Fade out before disappearing */
        }
        100% {
            transform: translate(calc(100vw + 200px), -150px) rotate(8deg);
            opacity: 0;
        }
    }

    /* Enhanced wave transition with clear patterns */
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
        fill: var(--vp-c-bg-soft);
        filter: drop-shadow(0 -2px 4px rgba(0, 0, 0, 0.3));
    }

    /* Accessibility improvements */
    @media (prefers-reduced-motion: reduce) {
        .VPHero *,
        .floating-word {
            animation-duration: 0.01ms !important;
            animation-iteration-count: 1 !important;
            transition-duration: 0.01ms !important;
            transform: none !important;
        }
    }

    /* Enhanced mobile and tablet optimizations */
    @media (max-width: 768px) {
        .VPHero.hero-enhanced {
            min-height: 90vh !important;
            padding: calc(
                    var(--vp-nav-height) + var(--vp-layout-top-height, 0px) +
                        35px
                )
                16px 50px;
        }

        .floating-word {
            font-size: 12px !important;
            opacity: 0.05 !important;
        }

        /* Center hero name and content on mobile */
        .main {
            text-align: center !important;
        }

        .heading {
            align-items: center !important;
        }

        .name,
        .text {
            margin: 0 auto !important;
            width: fit-content !important;
        }

        .tagline {
            margin-left: auto !important;
            margin-right: auto !important;
        }

        .actions {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 16px;
            margin-top: 28px;
            width: 100%;
            max-width: 320px;
            margin-left: auto;
            margin-right: auto;
        }

        :deep(.VPButton) {
            width: auto !important;
            min-width: 80px !important;
            max-width: 140px !important;
            padding: 8px 16px !important;
            font-size: 15px !important;
            font-weight: 600 !important;
            min-height: 36px !important;
            display: flex !important;
            align-items: center !important;
            justify-content: center !important;
            border-radius: 16px !important;
            flex-shrink: 0 !important;
        }
    }

    /* Tablet optimizations - refined flex layout */
    @media (min-width: 480px) and (max-width: 768px) {
        .actions {
            max-width: 380px !important;
            gap: 18px !important;
            margin-top: 32px !important;
        }

        :deep(.VPButton) {
            min-width: 90px !important;
            max-width: 160px !important;
            padding: 9px 18px !important;
            font-size: 16px !important;
            min-height: 38px !important;
            border-radius: 18px !important;
        }
    }

    /* Small mobile phones - flex layout for better content fitting */
    @media (max-width: 480px) {
        .VPHero.hero-enhanced {
            padding: calc(
                    var(--vp-nav-height) + var(--vp-layout-top-height, 0px) +
                        25px
                )
                12px 35px;
        }

        /* Center hero name on small screens with higher specificity */
        .main {
            text-align: center !important;
        }

        .heading {
            align-items: center !important;
        }

        .name,
        .text {
            font-size: 30px !important;
            line-height: 1.1 !important;
            margin: 0 auto !important;
            width: fit-content !important;
        }

        .tagline {
            font-size: 16px !important;
            line-height: 1.4 !important;
            margin-bottom: 24px !important;
            margin-left: auto !important;
            margin-right: auto !important;
        }

        .actions {
            gap: 14px !important;
            margin-top: 24px !important;
            max-width: 300px !important;
        }

        :deep(.VPButton) {
            min-width: 70px !important;
            max-width: 120px !important;
            padding: 7px 14px !important;
            font-size: 15px !important;
            font-weight: 700 !important;
            min-height: 34px !important;
            border-radius: 14px !important;
        }
    }
</style>
