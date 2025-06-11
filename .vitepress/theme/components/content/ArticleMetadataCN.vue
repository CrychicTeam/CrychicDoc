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

            const mainContainer = window.document.querySelector(".content-container .main");
            if (!mainContainer) return;

            // Clone the container to avoid modifying the live DOM
            const clone = mainContainer.cloneNode(true) as HTMLElement;

            // Remove all dialog cards from the cloned container before analysis
            clone.querySelectorAll('.md-dialog-card').forEach(el => el.remove());

            const imgs = clone.querySelectorAll<HTMLImageElement>("img");
            imageCount.value = imgs?.length || 0;
            
            const words = clone.textContent || "";
            wordCount.value = utils.content.countWord(words);
        }
    }

    onMounted(() => {
        analyze();
        
        const checkPageViews = () => {
            const pvElement = document.querySelector('#busuanzi_value_page_pv');
            const text = pvElement?.innerHTML;
            if (text && !isNaN(parseInt(text))) {
                pageViews.value = parseInt(text);
            }
        };
        
        const interval = setInterval(checkPageViews, 1000);
        setTimeout(() => clearInterval(interval), 10000);
        setTimeout(checkPageViews, 2000);
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
