<template>
    <v-footer :style="{ background: 'var(--footer-bg)' }">
        <v-row justify="center" align="end" no-gutters>
            <v-btn
                v-for="link in links"
                :key="link.code[lang]"
                class="mx-0 bold-text theme-button"
                rounded="xl"
                variant="text"
                :href="link.link"
            >
                <v-sheet class="pa-0 ma-0" color="transparent">
                    <v-icon class="theme-icon" size="14" :icon="link.icon" />
                    {{ link.code[lang] }}
                </v-sheet>
            </v-btn>
            <v-col
                class="text-center text-large bold-text mt-2 theme-text"
                cols="12"
            >
                {{ copyright[lang] }}©{{ currentYear }}-{{ beginYear }}
                {{ author.code[lang] }}
            </v-col>
        </v-row>
    </v-footer>
</template>

<script setup>
    import { ref } from "vue";
    import { useData } from "vitepress";

    const { lang } = useData();

    const currentYear = new Date().getFullYear();
    const beginYear = "2024";

    const copyright = {
        "en-US": "Copyright",
        "zh-CN": "版权所有",
    };

    const author = {
        code: {
            "en-US": "M1hono",
            "zh-CN": "不是客服",
        },
        icon: "mdi-copyright",
    };

    const icp = {
        code: {
            "en-US": "晋ICP备2022005790号-2",
            "zh-CN": "晋ICP备2022005790号-2",
        },
        link: "https://beian.miit.gov.cn/#/Integrated/index",
        icon: "mdi-monitor",
    };

    const license = {
        code: {
            "en-US": "Licensed under CC BY-SA 4.0",
            "zh-CN": "基于 CC BY-SA 4.0 证书发布",
        },
        link: "https://creativecommons.org/licenses/by-sa/4.0/",
        icon: "mdi-wrench-outline",
    };

    const links = ref([icp, license]);
</script>

<style scoped>
    .text-large {
        font-size: 14px;
    }
    .bold-text {
        font-weight: 400;
    }
    .theme-button {
        color: var(--footer-text-color);
    }
    .theme-icon {
        color: var(--footer-text-color);
    }
    .theme-text {
        color: var(--footer-text-color);
    }
</style>
