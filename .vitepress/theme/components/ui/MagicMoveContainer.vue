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
                <label :for="elementId + index" flex items-center>
                    {{ item.fileName }}
                </label>
            </template>
        </div>
        <div class="block">
            <div class="language">
                <ShikiMagicMovePrecompiled :steps="content" :step="step" />
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
    import { ref } from "vue";
    import { ShikiMagicMovePrecompiled } from "shiki-magic-move/vue";

    const props = defineProps<{
        stepsLz: string;
        stepRanges: string[][];
    }>();

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

    const elementId = generateRandomString(6);
    const content = JSON.parse(decodeURIComponent(props.stepsLz));
    const step = ref(0);

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
    }
</style>
