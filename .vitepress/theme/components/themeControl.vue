<script setup lang="ts">
    import { useData } from "vitepress";
    import { watch, onMounted, onUnmounted, ref, nextTick } from "vue";
    import { useRoute } from "vitepress";

    const { isDark } = useData();
    const route = useRoute();
    const isHomePage = ref(true);

    const setHeroBackground = (isDarkMode: boolean) => {
        if (!isHomePage.value) return;

        const darkGradient =
            "linear-gradient(-45deg, #1B1B1F 50%, #1B1B1F 50%)";
        const lightGradient =
            "linear-gradient(-45deg, #ffffff 50%, #ffffff 50%)";
        const darkBlur = "blur(68px)";
        const lightBlur = "blur(68px)";

        document.documentElement.style.setProperty(
            "--vp-home-hero-image-background-image",
            isDarkMode ? darkGradient : lightGradient
        );
        document.documentElement.style.setProperty(
            "--vp-home-hero-image-filter",
            isDarkMode ? darkBlur : lightBlur
        );
    };

    const updateThemeMode = (isDarkMode: boolean) => {
        if (isDarkMode) {
            document.documentElement.classList.add("dark");
            document.documentElement.setAttribute("theme-mode", "dark");
        } else {
            document.documentElement.classList.remove("dark");
            document.documentElement.removeAttribute("theme-mode");
        }
    };

    onMounted(async () => {
        isHomePage.value = route.path === "/";

        // 确保DOM已更新
        await nextTick();

        // 立即应用主题
        updateThemeMode(isDark.value);
        setHeroBackground(isDark.value);

        watch(isDark, (newValue) => {
            updateThemeMode(newValue);
            setHeroBackground(newValue);
        });

        watch(
            () => route.path,
            (newPath) => {
                isHomePage.value = newPath === "/";
                if (isHomePage.value) {
                    setHeroBackground(isDark.value);
                }
            }
        );
    });

    onUnmounted(() => {
        document.documentElement.style.removeProperty(
            "--vp-home-hero-image-background-image"
        );
        document.documentElement.style.removeProperty(
            "--vp-home-hero-image-filter"
        );
    });
</script>

<style>
    :root {
        --vp-home-hero-image-background-image: linear-gradient(-45deg, #ffffff 50%, #ffffff 50%);
        --vp-home-hero-image-filter: blur(68px);
        --code-bg-color: var(--vp-c-brand-light);
        --code-group-border-color: #cccac0;
        --vp-c-brand: #1565c0;
        --vp-c-text-2: #546e7a;
        --stepper-text-color: var(--vp-c-text-1);
        --stepper-active-color: var(--vp-c-brand);
        --stepper-hover-color: var(--vp-c-brand-light);
        --github-bg-image: url("/icon/github.png");
        --pdf-bg-color: #ffffff;
        --pdf-text-color: #000000;
        --pdf-border-color: #ddd;
    }

    .dark {
        --vp-home-hero-image-background-image: linear-gradient(
            -45deg,
            #1B1B1F 50%,
            #1B1B1F 50%
        );
        --vp-home-hero-image-filter: blur(72px);
        --code-bg-color: var(--vp-c-brand-dark);
        --code-group-border-color: #242631;
        --vp-c-brand: #6e7aad;
        --vp-c-text-2: #b0bec5; 
        --stepper-text-color: var(--vp-c-text-1);
        --stepper-active-color: var(--vp-c-brand-dark);
        --stepper-hover-color: var(--vp-c-brand);
        --github-bg-image: url("/icon/github_dark.png");
        --pdf-bg-color: #1e1e1e;
        --pdf-text-color: #ffffff;
        --pdf-border-color: #333;
    }

    .theme-stepper {
        background-color: var(--vp-custom-block-info-bg)!important;
        color: var(--stepper-text-color)!important;
    }

    .theme-stepper .v-stepper__step {
        background-color: transparent!important;
    }

    .theme-stepper .v-stepper__step--active {
        color: var(--stepper-active-color)!important;
    }

    .theme-stepper .v-stepper__step:hover {
        background-color: var(--stepper-hover-color)!important;
        opacity: 0.1;
    }

    .theme-stepper .v-stepper__content {
        background-color: transparent!important;
    }

    .v-divider.v-theme--light {
        border-color: var(--stepper-text-color)!important;
    }

    .v-divider.v-theme--dark {
        border-color: var(--stepper-active-color)!important;
    }

</style>
