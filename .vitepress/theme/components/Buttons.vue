<template>
    <Transition name="fade">
        <div
            v-show="showBackTop"
            class="floating-button top-button"
            :title="translations.backToTop[lang]"
            @click="scrollToTop"
        >
            <svg
                xmlns="http://www.w3.org/2000/svg"
                width="30"
                height="30"
                viewBox="0 0 24 24"
            >
                <path
                    fill="#ffffff"
                    d="M15.78 15.84S18.64 13 19.61 12c3.07-3 1.54-9.18 1.54-9.18S15 1.29 12 4.36C9.66 6.64 8.14 8.22 8.14 8.22S4.3 7.42 2 9.72L14.25 22c2.3-2.33 1.53-6.16 1.53-6.16m-1.5-9a2 2 0 0 1 2.83 0a2 2 0 1 1-2.83 0M3 21a7.8 7.8 0 0 0 5-2l-3-3c-2 1-2 5-2 5"
                />
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
        <svg
            xmlns="http://www.w3.org/2000/svg"
            width="30"
            height="30"
            viewBox="0 0 1025 1024"
        >
            <path
                fill="#ffffff"
                d="M960.193 1024h-512q-27 0-45.5-18.5t-18.5-45.5V768h224q31 0 55.5-18t34.5-46h166q13 0 22.5-9.5t9.5-22.5t-9.5-22.5t-22.5-9.5h-160V512h160q13 0 22.5-9.5t9.5-22.5t-9.5-22.5t-22.5-9.5h-160V320h256q26 0 45 19t19 45v576q0 26-18.5 45t-45.5 19m-96-192h-320q-13 0-22.5 9.5t-9.5 22.5t9.5 22.5t22.5 9.5h320q13 0 22.5-9.5t9.5-22.5t-9.5-22.5t-22.5-9.5m-288-128h-512q-27 0-45.5-18.5T.193 640V64q0-26 18.5-45t45.5-19h512q27 0 45.5 19t18.5 45v576q0 27-19 45.5t-45 18.5m-96-576h-320q-13 0-22.5 9.5t-9.5 22.5t9.5 22.5t22.5 9.5h320q13 0 22.5-9.5t9.5-22.5t-9.5-22.5t-22.5-9.5m0 192h-320q-13 0-22.5 9.5t-9.5 22.5t9.5 22.5t22.5 9.5h320q13 0 22.5-9.5t9.5-22.5t-9.5-22.5t-22.5-9.5m0 192h-320q-13 0-22.5 9.5t-9.5 22.5t9.5 22.5t22.5 9.5h320q13 0 22.5-9.5t9.5-22.5t-9.5-22.5t-22.5-9.5"
            />
        </svg>
    </button>

    <button
        @click="goBack"
        class="floating-button back-button"
        :title="translations.back[lang]"
    >
        <svg
            xmlns="http://www.w3.org/2000/svg"
            width="30"
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

    <button
        @click="scrollToBottom"
        class="floating-button comment-button"
        :title="translations.back[lang]"
    >
        <svg
            xmlns="http://www.w3.org/2000/svg"
            width="30"
            height="30"
            viewBox="0 0 24 24"
        >
            <path
                fill="#ffffff"
                d="M6 14h12v-2H6zm0-3h12V9H6zm0-3h12V6H6zM4 18q-.825 0-1.412-.587T2 16V4q0-.825.588-1.412T4 2h16q.825 0 1.413.588T22 4v18l-4-4z"
            />
        </svg>
    </button>

    <button
        @click="openLink('https://qm.qq.com/q/CsR20JGLAc')"
        class="floating-button qq-button"
        :title="translations.qq[lang]"
    >
        <svg
            xmlns="http://www.w3.org/2000/svg"
            width="30"
            height="30"
            viewBox="0 0 32 32"
        >
            <path
                fill="#ffffff"
                d="M29.11 26.278c-.72.087-2.804-3.296-2.804-3.296c0 1.959-1.009 4.515-3.191 6.362c1.052.325 3.428 1.198 2.863 2.151c-.457.772-7.844.493-9.977.252c-2.133.24-9.52.519-9.977-.252c-.565-.953 1.807-1.826 2.861-2.151c-2.182-1.846-3.191-4.403-3.191-6.362c0 0-2.083 3.384-2.804 3.296c-.335-.041-.776-1.853.584-6.231c.641-2.064 1.375-3.78 2.509-6.611C5.792 6.13 8.811.001 15.999.001c7.109.001 10.197 6.008 10.017 13.435c1.132 2.826 1.869 4.553 2.509 6.611c1.361 4.379.92 6.191.584 6.231z"
            />
        </svg>
    </button>

    <button
        @click="openLink('https://discord.gg/uPJHxU46td')"
        class="floating-button discord-button"
        :title="translations.discord[lang]"
    >
        <svg
            xmlns="http://www.w3.org/2000/svg"
            width="30"
            height="30"
            viewBox="0 0 24 24"
        >
            <path
                fill="#ffffff"
                d="m22 24l-5.25-5l.63 2H4.5A2.5 2.5 0 0 1 2 18.5v-15A2.5 2.5 0 0 1 4.5 1h15A2.5 2.5 0 0 1 22 3.5V24M12 6.8c-2.68 0-4.56 1.15-4.56 1.15c1.03-.92 2.83-1.45 2.83-1.45l-.17-.17c-1.69.03-3.22 1.2-3.22 1.2c-1.72 3.59-1.61 6.69-1.61 6.69c1.4 1.81 3.48 1.68 3.48 1.68l.71-.9c-1.25-.27-2.04-1.38-2.04-1.38S9.3 14.9 12 14.9s4.58-1.28 4.58-1.28s-.79 1.11-2.04 1.38l.71.9s2.08.13 3.48-1.68c0 0 .11-3.1-1.61-6.69c0 0-1.53-1.17-3.22-1.2l-.17.17s1.8.53 2.83 1.45c0 0-1.88-1.15-4.56-1.15m-2.07 3.79c.65 0 1.18.57 1.17 1.27c0 .69-.52 1.27-1.17 1.27c-.64 0-1.16-.58-1.16-1.27c0-.7.51-1.27 1.16-1.27m4.17 0c.65 0 1.17.57 1.17 1.27c0 .69-.52 1.27-1.17 1.27c-.64 0-1.16-.58-1.16-1.27c0-.7.51-1.27 1.16-1.27Z"
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
        comment: { "en-US": "Comment", "zh-CN": "评论" },
        qq: { "en-US": "QQ", "zh-CN": "QQ" },
        discord: { "en-US": "Discord", "zh-CN": "Discord" },
    };

    const currentPath = computed(() => router.route.path);

    const specialPaths = [
        {
            regex: /^\/(zh|en|jp)\/modpack\/kubejs\/1\.20\.1\/KubeJSCourse\//,
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
            regex: /^\/(zh|en|jp)\/modpack\/kubejs\/1\.20\.1\/(?!KubeJSCourse)/,
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

    const scrollToBottom = () => {
        window.scrollTo({
            top: document.documentElement.scrollHeight,
            behavior: "smooth",
        });
    };

    const openLink = (link: string) => {
        if (link) {
            window.open(link, "_blank");
        } else {
            alert("Please provide a valid link!");
        }
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
        width: 45px;
        height: 45px;
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
        bottom: 170px;
    }

    .copy-button {
        bottom: 120px;
    }

    .refresh-button {
        bottom: 70px;
    }

    .comment-button {
        bottom: 20px;
    }

    .qq-button {
        bottom: 140px;
        left: 10px !important;
    }

    .discord-button {
        bottom: 90px;
        left: 10px !important;
    }

    .back-button {
        bottom: 40px;
        left: 10px !important;
    }

    .fade-enter-active,
    .fade-leave-active {
        transition: opacity 0.5s;
    }

    .fade-enter,
    .fade-leave-to {
        opacity: 0;
    }
</style>
