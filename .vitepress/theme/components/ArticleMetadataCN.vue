<script lang="ts" setup>
    import { dataSymbol, useData } from 'vitepress'
    import { computed, ref, onMounted } from 'vue'
    import { countWord } from '../../utils/functions'

    const { page, frontmatter, lang } = useData()

    const update = computed(
        () => new Date(page.value.lastUpdated!).toLocaleDateString()
    )

    const wordCount = ref(0)
    const imageCount = ref(0)

    const wordTime = computed(() => {
        return ((wordCount.value / 275) * 60)
    })

    const imageTime = computed(() => {
        const n = imageCount.value
        if (imageCount.value <= 10) {
            // 等差数列求和
            return n * 13 + (n * (n - 1)) / 2
        }
        return 175 + (n - 10) * 3
    })

    // 阅读时间
    const readTime = computed(() => {
        return Math.ceil((wordTime.value + imageTime.value) / 60)
    })


    function analyze() {
        document.querySelectorAll('.meta-des').forEach(v => v.remove())
        const docDomContainer = window.document.querySelector('#VPContent')
        const imgs = docDomContainer?.querySelectorAll<HTMLImageElement>(
            '.content-container .main img'
        )
        imageCount.value = imgs?.length || 0
        const words = docDomContainer?.querySelector('.content-container .main')?.textContent || ''
        wordCount.value = countWord(words)
    }

    onMounted(() => {
        // 初始化时执行一次
        analyze()
    })

    const isMetadata = computed(() => {
            return frontmatter.value?.metadata ?? true;
    });

    const metadata = {
        "zh-CN": {
            update: (text: string)=> `最后更新于：${text}`,
            wordCount: (text: number) => `字数：${text}字`,
            readTime: (text: number) => `时长：${text}分钟`,
        },
        "en-US": {
            update: (text: string)=> `Last updated on: ${text}`,
            wordCount: (text: number) => `Word count: ${text} word`,
            readTime: (text: number) => `Reading time: ${text} minutes`,
        },
        icon: {
            update: "mdi-refresh",
            wordCount: "mdi-text-shadow",
            readTime: "mdi-timer-outline"
        }
    }

    const icon = (key: number) => {
        return metadata.icon[key]
    };

    const data = ref([update, wordCount, readTime])

    const matadataText = computed(() => {
        return (
            metadata[lang.value] ||
            metadata["en-US"]
        );
    });
</script>


<template>
    <div v-if="isMetadata" class="word">
        <p>
            <v-btn
                v-for="(value, key, index) in matadataText" 
                class="mx-0 btn"
                rounded="lg"
                variant="text"
                density="comfortable"
                :key="index"
                :prepend-icon="icon(key)"
                >
                    {{ value(data[index].value) }}
            </v-btn>
        </p>
    </div>
</template>

<style>
.word, .btn {
  color: var(--vp-c-text-2);
  font-size: 15px;
  /* margin-left: -5px; */
}

.btn {
    padding-left: 12px;
    padding-right: 8px;
    font-weight: 300;
}

.v-icon, .v-btn__prepend {
    margin-inline: calc(var(--v-btn-height) / -9) 0px;
    color: var(--vp-c-text-2);
    opacity: 85%;

}
</style>