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

    const readTime = computed(() =>
        utils.vitepress.readingTime.calculateTotalTime(
            wordCount.value,
            imageCount.value
        )
    );

    function analyze() {
        if (!import.meta.env.SSR) {
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

    const data = ref([update, wordCount, readTime]);

    const metadataText = computed(() => {
        return utils.vitepress.getMetadataText(lang.value);
    });

    const metadataKeys = ["update", "wordCount", "readTime"];
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
                        {{ metadataText[key](data[index].value) }}
                    </v-btn>
                </v-col>
            </v-row>
            <ProgressLinear />
        </div>
    </div>
    <State />
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
