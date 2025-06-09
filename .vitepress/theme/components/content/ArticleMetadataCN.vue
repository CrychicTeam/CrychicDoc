<script lang="ts" setup>
    import { useData } from "vitepress";
    import { computed, ref, onMounted } from "vue";
    import utils from "@utils";
    import ProgressLinear from "@components/ui/ProgressLinear.vue";
    import State from "@components/ui/State.vue";

    const { page, frontmatter, lang } = useData();

    const update = computed(() =>
        page.value.lastUpdated
            ? new Date(page.value.lastUpdated).toLocaleDateString()
            : ""
    );

    const wordCount = ref(0);
    const imageCount = ref(0);
    const pageViews = ref(0);

    const readTime = computed(() =>
        utils.vitepress.readingTime.calculateTotalTime(
            wordCount.value,
            imageCount.value
        )
    );

    function analyze() {
        if (typeof window !== 'undefined' && typeof document !== 'undefined') {
            utils.vitepress.contentAnalysis.cleanupMetadata();

            const docDomContainer = window.document.querySelector("#VPContent");
            const imgs = docDomContainer?.querySelectorAll<HTMLImageElement>(
                ".content-container .main img"
            );
            imageCount.value = imgs?.length || 0;

            const words =
                docDomContainer?.querySelector(".content-container .main")
                    ?.textContent || "";
            wordCount.value = utils.content.countWord(words);
        }
    }

    onMounted(() => {
        analyze();
        
        // 获取不蒜子统计数据
        if (typeof window !== 'undefined') {
            let timeoutPV = 0;
            let retryCount = 0;
            
            const initBusuanzi = () => {
                // 先尝试调用busuanzi.fetch()来确保统计更新
                if (typeof (window as any).busuanzi !== 'undefined') {
                    console.log('调用 busuanzi.fetch() 更新统计');
                    (window as any).busuanzi.fetch();
                }
            };
            
            const getPV = () => {
                if (timeoutPV) clearTimeout(timeoutPV);
                timeoutPV = window.setTimeout(() => {
                    const $PV = document.querySelector('#busuanzi_value_page_pv');
                    const text = $PV?.innerHTML;
                    console.log('不蒜子检查:', { 
                        element: !!$PV, 
                        text, 
                        parsed: text ? parseInt(text) : 0,
                        retryCount,
                        busuanziLoaded: typeof (window as any).busuanzi !== 'undefined'
                    });
                    
                    if ($PV && text && text.trim() !== '' && !isNaN(parseInt(text))) {
                        pageViews.value = parseInt(text);
                        console.log('✅ 不蒜子统计更新:', pageViews.value);
                    } else {
                        // 如果超过15秒还没获取到，就停止重试
                        retryCount++;
                        if (retryCount < 30) {
                            // 每隔一段时间尝试初始化不蒜子
                            if (retryCount % 5 === 0) {
                                initBusuanzi();
                            }
                            getPV();
                        } else {
                            console.warn('❌ 不蒜子统计获取失败，停止重试');
                        }
                    }
                }, 500);
            };
            
            // 延迟开始，确保DOM已加载
            setTimeout(() => {
                initBusuanzi();
                getPV();
            }, 1000);
        }
    });

    const isMetadata = computed(() => {
        return frontmatter.value?.metadata ?? true;
    });

    /**
     * Get icon name by metadata key
     */
    const icon = (key: string) => {
        return utils.vitepress.getMetadataIcon(key);
    };

    const data = ref([update, wordCount, readTime, pageViews]);

    const metadataText = computed(() => {
        return utils.vitepress.getMetadataText(lang.value);
    });

    const metadataKeys = ["update", "wordCount", "readTime", "pageViews"] as const;
</script>

<template>
    <div v-if="isMetadata" class="word">
        <div>
            <v-row no-gutters>
                <v-col v-for="(key, index) in metadataKeys" :key="key">
                    <v-btn
                        class="mx-0 btn btn-icon"
                        rounded="lg"
                        variant="text"
                        density="comfortable"
                        :prepend-icon="icon(key)"
                    >
                        {{ (metadataText as any)[key]?.(data[index].value) }}
                    </v-btn>
                </v-col>
            </v-row>
            <ProgressLinear />
        </div>
    </div>
    <State />
    
    <!-- 不蒜子统计元素 - 必须可见才能正确统计 -->
    <span id="busuanzi_container_page_pv" style="position: absolute; left: -9999px;">
        <span id="busuanzi_value_page_pv"></span>
    </span>
</template>

<style>
    .word,
    .btn {
        color: var(--metadata-text-color);
        font-size: var(--metadata-font-size);
    }

    .btn {
        padding-left: 12px;
        padding-right: 8px;
        font-weight: 300;
    }

    .btn-icon .v-btn__prepend {
        margin-inline: calc(var(--v-btn-height) / -9) 0px;
        color: var(--metadata-text-color);
        opacity: var(--metadata-icon-opacity);
    }
</style>
