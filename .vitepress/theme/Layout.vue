<script setup lang="ts">
    import DefaultTheme from "vitepress/theme";
    import { useData, inBrowser } from "vitepress";
    import { watchEffect, onMounted } from "vue";
    import { VApp } from "vuetify/components";

    const { lang } = useData();
    watchEffect(() => {
        if (inBrowser) {
            document.cookie = `nf_lang=${lang.value}; expires=Mon, 1 Jan 2030 00:00:00 UTC; path=/`;
        }
    });

    // 加载不蒜子统计脚本
    onMounted(() => {
        if (inBrowser) {
            const script = document.createElement('script');
            script.async = true;
            script.src = '//busuanzi.ibruce.info/busuanzi/2.3/busuanzi.pure.mini.js';
            document.head.appendChild(script);
        }
    });
</script>

<template>
    <VApp>
        <DefaultTheme.Layout />
    </VApp>
</template>
