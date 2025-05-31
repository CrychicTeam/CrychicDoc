<script lang="ts" setup>
    import { useData } from "vitepress";
    import { computed, ref } from "vue";

    const { frontmatter, lang } = useData();

    type StateType = "preliminary" | "unfinished" | "outdated" | "renovating";

    const state = computed(
        () => switchState(frontmatter.value?.state) ?? false
    );

    function switchState(state: string): StateType | void {
        switch (state) {
            case "preliminary":
                return state;
            case "unfinished":
                return state;
            case "outdated":
                return state;
            case "renovating":
                return state;
            default:
                break;
        }
    }

    const text = ref({
        "zh-CN": {
            preliminary: {
                title: "🌱初步完成",
                text: "本文中内容已经初步完成，能够正常提供参考，但存在可能的错漏/亟待改进部分。",
            },
            unfinished: {
                title: "🚧未完成",
                text: "本文中的内容尚未完成编撰，或许会出现错漏/内容缺失，将在未来补充完整。",
            },
            outdated: {
                title: "🚨已过时",
                text: "本文中的内容或许已经过时，并不适用于最新版本，请仔细甄别。",
            },
            renovating: {
                title: "🕓翻新中",
                text: "本文中的过时内容正在翻新中，过时部分或许并不适用于最新版本，请仔细甄别。",
            },
        },
        "en-US": {
            preliminary: {
                title: "🌱 Preliminary Completion",
                text: "The content in this article has been preliminarily completed and can serve as a reference. However, there may be possible errors or areas in need of improvement.",
            },
            unfinished: {
                title: "🚧 Unfinished",
                text: "The content in this article is still being written and may contain errors or missing information. It will be completed in the future.",
            },
            outdated: {
                title: "🚨 Outdated",
                text: "The content in this article may be outdated and not applicable to the latest version. Please discern carefully.",
            },
            renovating: {
                title: "🕓 Under Renovation",
                text: "The outdated content in this article is currently being renovated. The outdated sections may not apply to the latest version. Please discern carefully.",
            },
        },
    });

    const i18nText = computed(() => {
        return (
            text.value[lang.value as keyof typeof text.value] ||
            text.value["en-US"]
        );
    });

    const colorControl = computed(() =>
        state.value === "preliminary" || state.value === "renovating"
            ? "var(--vp-custom-block-warning-bg)"
            : "var(--vp-custom-block-danger-bg)"
    );
</script>
<template>
    <div v-if="state" class="state custom-block">
        <p class="custom-block-title">
            {{ i18nText[state as StateType].title }}
        </p>
        <p>{{ i18nText[state as StateType].text }}</p>
    </div>
</template>
<style scoped>
    .custom-block.state {
        background-color: v-bind(colorControl);
    }
</style>
