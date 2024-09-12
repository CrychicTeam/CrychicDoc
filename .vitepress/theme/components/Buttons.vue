<template>
    <Transition name="fade">
        <div
            v-show="showBackTop"
            class="floating-button top-button"
            :title="translations.backToTop[lang]"
            @click="scrollToTop"
        >
            <svg class="icon" viewBox="0 0 1024 1024" width="30" height="30">
                <path
                    d="M752.736 431.063C757.159 140.575 520.41 8.97 504.518 0.41V0l-0.45 0.205-0.41-0.205v0.41c-15.934 8.56-252.723 140.165-248.259 430.653-48.21 31.457-98.713 87.368-90.685 184.074 8.028 96.666 101.007 160.768 136.601 157.287 35.595-3.482 25.232-30.31 25.232-30.31l12.206-50.095s52.47 80.569 69.304 80.528c15.114-1.23 87-0.123 95.6 0h0.82c8.602-0.123 80.486-1.23 95.6 0 16.794 0 69.305-80.528 69.305-80.528l12.165 50.094s-10.322 26.83 25.272 30.31c35.595 3.482 128.574-60.62 136.602-157.286 8.028-96.665-42.475-152.617-90.685-184.074z m-248.669-4.26c-6.758-0.123-94.781-3.359-102.891-107.192 2.95-98.714 95.97-107.438 102.891-107.93 6.964 0.492 99.943 9.216 102.892 107.93-8.11 103.833-96.174 107.07-102.892 107.192z m-52.019 500.531c0 11.838-9.42 21.382-21.012 21.382a21.217 21.217 0 0 1-21.054-21.34V821.74c0-11.797 9.421-21.382 21.054-21.382 11.591 0 21.012 9.585 21.012 21.382v105.635z m77.333 57.222a21.504 21.504 0 0 1-21.34 21.626 21.504 21.504 0 0 1-21.34-21.626V827.474c0-11.96 9.543-21.668 21.299-21.668 11.796 0 21.38 9.708 21.38 21.668v157.082z m71.147-82.043c0 11.796-9.42 21.34-21.053 21.34a21.217 21.217 0 0 1-21.013-21.34v-75.367c0-11.755 9.421-21.299 21.013-21.299 11.632 0 21.053 9.544 21.053 21.3v75.366z"
                    fill="currentColor"
                ></path>
            </svg>
        </div>
    </Transition>

    <button
        @click="refreshPage"
        class="floating-button refresh-button"
        :title="translations.refresh[lang]"
    >
        <svg
            xmlns="http://www.w3.org/2000/svg"
            x="0px"
            y="0px"
            width="30"
            height="30"
            viewBox="0,0,250,250"
        >
            <g
                fill="#ffffff"
                fill-rule="nonzero"
                stroke="none"
                stroke-width="1"
                stroke-linecap="butt"
                stroke-linejoin="miter"
                stroke-miterlimit="10"
                stroke-dasharray=""
                stroke-dashoffset="0"
                font-family="none"
                font-weight="none"
                font-size="none"
                text-anchor="none"
                style="mix-blend-mode: normal"
            >
                <g transform="scale(10.66667,10.66667)">
                    <path
                        d="M21,15v-5c0,-3.866 -3.134,-7 -7,-7h-3c-0.552,0 -1,0.448 -1,1v0c0,1.657 1.343,3 3,3h1c1.657,0 3,1.343 3,3v5h-1.294c-0.615,0 -0.924,0.742 -0.491,1.178l3.075,3.104c0.391,0.395 1.03,0.395 1.421,0l3.075,-3.104c0.432,-0.436 0.122,-1.178 -0.492,-1.178z"
                        opacity="0.35"
                    ></path>
                    <path
                        d="M3,9v5c0,3.866 3.134,7 7,7h3c0.552,0 1,-0.448 1,-1v0c0,-1.657 -1.343,-3 -3,-3h-1c-1.657,0 -3,-1.343 -3,-3v-5h1.294c0.615,0 0.924,-0.742 0.491,-1.178l-3.075,-3.105c-0.391,-0.395 -1.03,-0.395 -1.421,0l-3.074,3.105c-0.433,0.436 -0.123,1.178 0.491,1.178z"
                    ></path>
                </g>
            </g>
        </svg>
    </button>

    <button
        @click="copyLink"
        class="floating-button copy-button"
        :class="{ copied }"
        :title="translations.copyLink[lang]"
    >
        <svg class="icon" viewBox="0 0 1024 1024" width="30" height="30">
            <path
                d="M585.142857 365.714286a73.142857 73.142857 0 0 1 73.142857 73.142857v390.095238a73.142857 73.142857 0 0 1-73.142857 73.142857H195.047619a73.142857 73.142857 0 0 1-73.142857-73.142857V438.857143a73.142857 73.142857 0 0 1 73.142857-73.142857h390.095238z m0 73.142857H195.047619v390.095238h390.095238V438.857143z m-73.142857 219.428571v73.142857H268.190476v-73.142857h243.809524zM828.952381 121.904762a73.142857 73.142857 0 0 1 73.142857 73.142857v390.095238a73.142857 73.142857 0 0 1-73.142857 73.142857h-121.904762v-73.142857h121.904762V195.047619H438.857143v121.904762h-73.142857V195.047619a73.142857 73.142857 0 0 1 73.142857-73.142857h390.095238zM512 536.380952v73.142858H268.190476v-73.142858h243.809524z"
                fill="currentColor"
            ></path>
        </svg>
    </button>

    <button
        @click="goBack"
        class="floating-button back-button"
        :title="translations.back[lang]"
    >
        <svg
            xmlns="http://www.w3.org/2000/svg"
            width="25"
            height="30"
            viewBox="0 0 48 48"
        >
            <path
                fill="#ffffff"
                fill-rule="evenodd"
                stroke="#ffffff"
                stroke-linejoin="round"
                stroke-width="4"
                d="M44 40.836q-7.34-8.96-13.036-10.168t-10.846-.365V41L4 23.545L20.118 7v10.167q9.523.075 16.192 6.833q6.668 6.758 7.69 16.836Z"
                clip-rule="evenodd"
            />
        </svg>
    </button>
</template>

<script setup lang="ts">
    import { ref, onMounted, onBeforeUnmount, watch, computed } from "vue";
    import { useData, useRouter } from "vitepress";

    const { isDark, lang, page, frontmatter } = useData();
    const router = useRouter();

    const showBackTop = ref(false);
    const copied = ref(false);

    const translations = {
        backToTop: { "en-US": "Back to Top", "zh-CN": "返回顶部" },
        copyLink: { "en-US": "Copy Link", "zh-CN": "复制链接" },
        refresh: { "en-US": "Refresh", "zh-CN": "刷新" },
        back: { "en-US": "Back", "zh-CN": "返回" },
    };

    const currentPath = computed(() => router.route.path);

    const specialPaths = [
        {
            regex: /^\/(zh|en|jp)\/modpack\/kubejs\/1\.20\.1\/KubejsCourse\//,
            getTargetPath: (match) => `/${match[1]}/modpack/kubejs/1.20.1/`,
        },
        {
            regex: /^\/(zh|en|jp)\/modpack\/kubejs\/?$/,
            getTargetPath: (match) => `/${match[1]}/`,
        },
        {
            regex: /^\/(zh|en|jp)\/modpack\/kubejs\/1\.20\.1\/Introduction\/Catalogue$/,
            getTargetPath: (match) => `/${match[1]}/modpack/kubejs/1.20.1/`,
        },
        {
            regex: /^\/(zh|en|jp)\/modpack\/kubejs\/1\.20\.1\/(?!KubejsCourse)/,
            getTargetPath: (match) => `/${match[1]}/modpack/kubejs/1.20.1/`,
        },
    ];

    const scrollToTop = () => window.scrollTo({ top: 0, behavior: "smooth" });
    const onScroll = () => {
        showBackTop.value = window.scrollY > 100;
    };

    const copyLink = () => {
        let currentUrl = window.location.href;
        let modifiedUrl = currentUrl.replace(/\/[a-z]{2}\//, "/");
        navigator.clipboard.writeText(modifiedUrl).then(() => {
            copied.value = true;
            setTimeout(() => (copied.value = false), 2000);
        });
    };

    const updateTheme = (isDarkMode: boolean) => {
        document.documentElement.classList.toggle("dark-theme", isDarkMode);
        document.documentElement.classList.toggle("light-theme", !isDarkMode);

        document.documentElement.style.setProperty(
            "--button-bg-color",
            isDarkMode ? "#2b4796" : "#c5a16b"
        );
        document.documentElement.style.setProperty(
            "--button-hover-color",
            isDarkMode ? "#283d83" : "#a38348"
        );
        document.documentElement.style.setProperty(
            "--button-copied-color",
            isDarkMode ? "#45a049" : "#4caf50"
        );
    };

    onMounted(() => {
        window.addEventListener("scroll", onScroll);
        updateTheme(isDark.value);

        watch(isDark, (newVal) => {
            updateTheme(newVal);
        });
    });

    const refreshPage = () => {
        window.location.reload();
    };

    const normalizePath = (path) => {
        return path.endsWith("/") || path === "/" ? path : `${path}/`;
    };

    const goBack = () => {
        const path = normalizePath(currentPath.value);

        if (frontmatter.value.backPath) {
            let backPath = frontmatter.value.backPath;
            const baseUrl = window.location.origin + router.route.path;
            backPath = new URL(backPath, baseUrl).pathname;

            router.go(backPath);
            return;
        }

        for (const { regex, getTargetPath } of specialPaths) {
            const match = path.match(regex);
            if (match) {
                const targetPath = normalizePath(getTargetPath(match));
                if (targetPath !== path) {
                    router.go(targetPath);
                    return;
                }
            }
        }

        const segments = path.split("/").filter((segment) => segment !== "");
        if (segments.length <= 1) {
            router.go("/");
            return;
        }
        segments.pop();
        const newPath = normalizePath(`/${segments.join("/")}`);
        if (newPath === path) {
            segments.pop();
            router.go(normalizePath(`/${segments.join("/")}`));
        } else {
            router.go(newPath);
        }
    };

    onBeforeUnmount(() => {
        window.removeEventListener("scroll", onScroll);
    });
</script>

<style>
    :root {
        --button-bg-color: #c5a16b;
        --button-hover-color: #a38348;
        --button-copied-color: #4caf50;
    }

    .dark-theme {
        --button-bg-color: #2b4796;
        --button-hover-color: #283d83;
        --button-copied-color: #45a049;
    }

    .floating-button {
        z-index: 999;
        position: fixed;
        right: 20px;
        cursor: pointer;
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background-color: var(--button-bg-color);
        border: none;
        padding: 10px;
        box-shadow: 2px 2px 10px 4px rgba(0, 0, 0, 0.15);
        transition: background-color 0.3s, transform 0.3s;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .floating-button:hover {
        background-color: var(--button-hover-color);
        transform: scale(1.1);
    }

    .top-button {
        bottom: 200px;
    }

    .copy-button {
        bottom: 20px;
    }

    .refresh-button {
        bottom: 80px;
    }

    .back-button {
        bottom: 140px;
    }

    .floating-button.copied {
        background-color: var(--button-copied-color);
    }

    .fade-enter-active,
    .fade-leave-active {
        transition: opacity 0.5s;
    }

    .fade-enter,
    .fade-leave-to {
        opacity: 0;
    }

    .icon {
        fill: currentColor;
        color: #fff;
    }
</style>
