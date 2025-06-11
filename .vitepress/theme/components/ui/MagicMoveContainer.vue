<template>
    <div class="magic-move vp-code-group">
        <div class="tabs">
            <template v-for="(item, index) in content" :key="index">
                <input
                    :id="elementId + index"
                    :name="elementId"
                    :checked="index === 0"
                    type="radio"
                    @click="toggle(index)"
                />
                <label :for="elementId + index" :data-title="item.fileName" class="flex items-center">
                    {{ item.fileName }}
                </label>
            </template>
        </div>
        <div class="block">
            <div class="language">
                <ShikiMagicMove
                    v-if="highlighter && currentStep"
                    :highlighter="highlighter"
                    :code="currentStep.code"
                    :lang="currentStep.lang"
                    :theme="isDark ? 'github-dark' : 'github-light'"
                />
                <div v-else class="loading">Loading...</div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
    import { ref, computed, onMounted } from "vue";
    import { ShikiMagicMove } from "shiki-magic-move/vue";
    import { useData } from "vitepress";
    import { createHighlighter } from "shiki";
    import type { HighlighterCore } from 'shiki';

    const { isDark } = useData();

    const props = defineProps<{
        stepsLz: string;
        stepRanges: string[][];
    }>();

    /**
     * Generate a random string for unique element IDs
     */
    function generateRandomString(length: number) {
        const charset =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        let result = "";
        for (let i = 0; i < length; i++) {
            const randomIndex = Math.floor(Math.random() * charset.length);
            result += charset[randomIndex];
        }
        return result;
    }

    const highlighter = ref<HighlighterCore | null>(null);
    const elementId = generateRandomString(6);
    const content = JSON.parse(decodeURIComponent(props.stepsLz));
    const step = ref(0);

    // Initialize highlighter with comprehensive language support
    onMounted(async () => {
        try {
            highlighter.value = await createHighlighter({
                themes: ['github-dark', 'github-light'],
                langs: [
                    // Core web languages
                    'javascript', 'typescript', 'jsx', 'tsx', 'html', 'css', 'scss', 'sass', 'vue',
                    // Backend languages  
                    'java', 'python', 'cpp', 'c', 'csharp', 'php', 'ruby', 'go', 'rust', 'swift', 'kotlin', 'scala', 'r',
                    // Data & Config
                    'json', 'yaml', 'xml', 'sql',
                    // Documentation & Scripts
                    'markdown', 'bash', 'powershell', 'dockerfile',
                    // Fallback
                    'text'
                ],
            });
        } catch (error) {
            console.error('Failed to create highlighter:', error);
        }
    });

    /**
     * Current step data with extracted code from tokens
     */
    const currentStep = computed(() => {
        const stepData = content[step.value];
        if (!stepData) return null;
        
        // Extract code from precompiled tokens
        if (stepData.tokens && Array.isArray(stepData.tokens)) {
            return {
                code: stepData.tokens.map((token: any) => token.content || token).join(''),
                lang: stepData.lang || 'text',
                fileName: stepData.fileName
            };
        }
        
        return stepData;
    });

    /**
     * Switch to a different step
     */
    const toggle = (i: number) => {
        step.value = i;
    };
</script>

<style scoped>
    @media screen and (min-width: 640px) {
        .language {
            margin: 0 0 !important;
        }
    }

    .language {
        position: relative;
        margin: 0 -24px;
        background-color: var(--vp-code-block-bg);
        overflow-x: auto;
        transition: background-color 0.5s;
        border-radius: 0 0 8px 8px;
    }

    .magic-move :deep(pre) {
        padding: 20px 0;
        padding-left: 24px;
        padding-right: 24px;
        overflow-y: hidden !important;
        background: transparent !important;
    }

    /* Theme-aware Magic Move styling */
    .magic-move :deep(.shiki) {
        background: transparent !important;
    }

    /* Ensure proper dark mode background */
    .dark .language {
        background-color: var(--vp-code-block-bg) !important;
    }

    .dark .magic-move :deep(pre) {
        background: transparent !important;
    }

    /* Magic Move tabs inherit the group icons plugin CSS */
    .tabs label[data-title]::before {
        display: inline-block;
        width: 1em;
        height: 1em;
        margin-right: 0.5em;
        margin-bottom: -0.2em;
        background: var(--icon) no-repeat center / contain;
        content: '';
    }

    /* Loading state styling */
    .loading {
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 40px 20px;
        color: var(--vp-c-text-2);
        font-size: 14px;
    }
</style>
