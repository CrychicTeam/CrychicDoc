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
            <!-- 网站统计信息 -->
            <v-col 
                v-if="siteViews > 0 || siteVisitors > 0"
                class="text-center text-small bold-text theme-text"
                cols="12"
            >
                <v-sheet class="pa-0 ma-0" color="transparent">
                    <v-icon class="theme-icon mr-1" size="12" icon="mdi-eye-outline" />
                    <span>{{ lang === 'zh-CN' ? '总访问量' : 'Total Views' }}: {{ siteViews.toLocaleString() }}</span>
                    <span class="mx-2">|</span>
                    <v-icon class="theme-icon mr-1" size="12" icon="mdi-account-outline" />
                    <span>{{ lang === 'zh-CN' ? '访客数' : 'Visitors' }}: {{ siteVisitors.toLocaleString() }}</span>
                </v-sheet>
            </v-col>
            
            <v-col
                class="text-center text-large bold-text mt-2 theme-text"
                cols="12"
            >
                {{ copyright[lang] }}©{{ currentYear }}-{{ beginYear }}
                {{ author.code[lang] }}
            </v-col>
        </v-row>
        
        <!-- 不蒜子统计元素 - 使用绝对定位隐藏 -->
        <div style="position: absolute; left: -9999px;">
            <span id="busuanzi_container_site_pv">
                <span id="busuanzi_value_site_pv"></span>
            </span>
            <span id="busuanzi_container_site_uv">
                <span id="busuanzi_value_site_uv"></span>
            </span>
        </div>
    </v-footer>
</template>

<script setup>
    import { ref, onMounted } from "vue";
    import { useData } from "vitepress";

    const { lang } = useData();
    
    // 不蒜子统计数据
    const siteViews = ref(0);
    const siteVisitors = ref(0);

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

    onMounted(() => {
        if (typeof window !== 'undefined') {
            let timeoutPV = 0;
            let timeoutUV = 0;
            let retryCount = 0;
            
            const getSiteStats = () => {
                if (timeoutPV) clearTimeout(timeoutPV);
                if (timeoutUV) clearTimeout(timeoutUV);
                
                timeoutPV = window.setTimeout(() => {
                    const $sitePV = document.querySelector('#busuanzi_value_site_pv');
                    const pvText = $sitePV?.innerHTML;
                    if ($sitePV && pvText && pvText.trim() !== '' && !isNaN(parseInt(pvText))) {
                        siteViews.value = parseInt(pvText);
                    }
                }, 500);
                
                timeoutUV = window.setTimeout(() => {
                    const $siteUV = document.querySelector('#busuanzi_value_site_uv');
                    const uvText = $siteUV?.innerHTML;
                    if ($siteUV && uvText && uvText.trim() !== '' && !isNaN(parseInt(uvText))) {
                        siteVisitors.value = parseInt(uvText);
                    }
                    
                    // 如果都没获取到且重试次数未超限，继续重试
                    if ((siteViews.value === 0 || siteVisitors.value === 0) && retryCount < 15) {
                        retryCount++;
                        setTimeout(getSiteStats, 1000);
                    }
                }, 500);
            };
            
            getSiteStats();
        }
    });
</script>

<style scoped>
    .text-large {
        font-size: 14px;
    }
    .text-small {
        font-size: 12px;
        opacity: 0.8;
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
