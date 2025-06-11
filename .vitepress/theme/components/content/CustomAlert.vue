<template>
    <v-alert
        :type="type"
        :variant="variant"
        :density="density"
        :border="border"
        :color="color"
        :icon="icon"
        :title="title"
        :text="text"
        class="custom-alert-wrapper"
        :style="colorStyles"
    >
        <div v-if="$slots.default" class="custom-alert-content">
            <slot />
        </div>
    </v-alert>
</template>

<script setup lang="ts">
    import type { PropType } from "vue";
    import { computed } from "vue";

    type AlertVariant =
        | "elevated"
        | "flat"
        | "tonal"
        | "outlined"
        | "text"
        | "plain";
    type AlertDensity = "default" | "comfortable" | "compact";
    type AlertBorder = "start" | "end" | "top" | "bottom" | boolean;

    const props = defineProps({
        type: {
            type: String as PropType<"success" | "info" | "warning" | "error">,
            default: undefined,
        },
        variant: {
            type: String as PropType<AlertVariant>,
            default: "flat",
        },
        density: {
            type: String as PropType<AlertDensity>,
            default: "default",
        },
        border: {
            type: [String, Boolean] as PropType<AlertBorder>,
            default: undefined,
        },
        color: {
            type: String,
            default: undefined,
        },
        lightColor: {
            type: String,
            default: undefined,
        },
        darkColor: {
            type: String,
            default: undefined,
        },
        icon: {
            type: String,
            default: undefined,
        },
        title: {
            type: String,
            default: undefined,
        },
        text: {
            type: String,
            default: undefined,
        },
    });

    const colorStyles = computed(() => {
        const styles: Record<string, string> = {};
        
        if (props.lightColor) {
            styles['--custom-alert-light-color'] = props.lightColor;
        }
        
        if (props.darkColor) {
            styles['--custom-alert-dark-color'] = props.darkColor;
        }
        
        return styles;
    });
</script>

<style scoped>
    .custom-alert-wrapper {
        margin: 1rem 0;
    }
    
    .custom-alert-wrapper:where([style*="--custom-alert-light-color"]) {
        --v-theme-surface-variant: var(--custom-alert-light-color);
        background-color: var(--custom-alert-light-color) !important;
    }
    
    .dark .custom-alert-wrapper:where([style*="--custom-alert-dark-color"]) {
        --v-theme-surface-variant: var(--custom-alert-dark-color);
        background-color: var(--custom-alert-dark-color) !important;
    }
    
    .custom-alert-content {
        font-size: var(--vp-custom-block-font-size);
        line-height: var(--vp-custom-block-line-height);
        padding-top: 8px;
    }
</style>
