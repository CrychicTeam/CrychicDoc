<script lang="ts" setup>
    import { useData } from 'vitepress'
    import { computed, ref, onMounted } from 'vue'

    const { page, frontmatter, lang, isDark } = useData()

    onMounted(() => {
        updateProgress()
    })

    const metadata = {
        "zh-CN": `进度`,
        "en-US":  `Progress`,
    }

    const isProgress = computed(() => {
            return frontmatter.value?.progress ?? false;
    });

    const bufferValue = computed(() => frontmatter.value?.progress ?? 0 );
    const progressValue = ref(0);

    function updateProgress() {
        if (progressValue.value < bufferValue.value) {
            progressValue.value += 1;
            setTimeout(updateProgress, 20); 
        }
    }

    const color = computed(() =>
        isDark.value ? "var(--vp-c-text-2)" : "var(--vp-c-brand)"
    );

    const text = computed(() => {
        return (
            metadata[lang.value] ||
            metadata["en-US"]
        );
    });

</script>


<template>
    <v-row v-if="isProgress" align="center">
        <v-col cols="2">
            <v-btn 
                class="btn progress" 
                rounded="lg"
                prepend-icon="mdi-progress-clock" 
                variant="text"
                density="comfortable"
                >
                {{ text }}
            </v-btn>
        </v-col>
        <v-col>
            <v-progress-linear
                height="7"
                class="theme"
                rounded
                :model-value="progressValue"
                :buffer-value="bufferValue"
            />
        </v-col>
    </v-row>
</template>

<style>

    .theme {
        color: v-bind(color);
    }

    .progress .v-btn__prepend {
        margin-inline: calc(var(--v-btn-height) / -9) 0px !important;
        color: var(--vp-c-text-2);
        opacity: 85%;
    }

</style>