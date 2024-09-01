<template>
    <v-footer style="background: transparent;">
        <v-row justify="center" align="end" no-gutters>
            <v-btn
            v-for="link in links"
            :key="link.code"
            class="mx-0 bold-text"
            color="grey-darken-1"
            rounded="xl"
            variant="text"
            :href="link.link"
            >
                <v-sheet class="pa-0 ma-0" color="transparent">
                    <v-icon :color="isDark ? 'grey-lighten-2' : 'primary'" size="14" :icon="link.icon" /> {{ link.code[lang] }}
                </v-sheet>
            </v-btn>
            <v-col style="color: grey" class="text-center text-large bold-text mt-2" cols="12">
                {{copyright[lang]}}©{{ currentYear }}-{{beginYear}} {{author.code[lang]}}
            </v-col>
        </v-row>
    </v-footer>
</template>

<script>
import { useData } from 'vitepress';
export default {
    setup() {
        const { isDark , lang } = useData();
        return { isDark, lang };
    },
    data: () => ({
        links: [icp, license],
        currentYear,
        beginYear,
        author,
        copyright,
    }),
}
  
const currentYear = new Date().getFullYear()
const beginYear = '2024'
const copyright = {
    "en-US":"Copyright",
    "zh-CN":"版权所有"
}
const author = {
    code: {
        "en-US":"M1hono",
        "zh-CN":"不是客服"
    },
    icon: 'mdi-copyright',
}
const icp = {
    code: {
        "en-US":'晋ICP备2022005790号-2',
        "zh-CN":'晋ICP备2022005790号-2',
    },
    link: 'https://beian.miit.gov.cn/#/Integrated/index',
    icon: 'mdi-monitor',
}
const license = {
    code: {
        "en-US":"Licensed under MIT",
        "zh-CN":"基于 MIT 证书发布"
    },
    link: 'https://mit-license.org/',
    icon: 'mdi-wrench-outline',
}
</script>
<style>
.text-large {
    font-size: 14px; /* 根据需要设置大小 */
}
.bold-text {
    font-weight: 400; /* 粗体 */
}
</style>
  