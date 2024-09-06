<template>
    <div
        v-if="authors.length > 0 || authorsNoGitHub.length > 0"
        class="authors-section"
    >
        <h2>{{ translations[lang].heading }}</h2>
        <div class="page-authors">
            <a
                v-for="author in authors"
                :key="author"
                :href="`https://github.com/${author}`"
                target="_blank"
                class="author-link"
                :title="author"
            >
                <img
                    loading="lazy"
                    class="author-avatar non-preview-image"
                    :src="getAvatarUrl(author)"
                    :alt="author"
                />
            </a>
            <img
                v-for="author in authorsNoGitHub"
                :key="author"
                loading="lazy"
                class="author-avatar non-preview-image"
                :src="defaultAvatarUrl"
                :alt="author"
                :title="translations[lang].labelNoGitHub.replace('%s', author)"
            />
        </div>
    </div>
</template>

<script setup lang="ts">
    import { ref, onMounted, watch } from "vue";
    import { useData } from "vitepress";

    const { lang, frontmatter, theme } = useData();

    const authors = ref<string[]>([]);
    const authorsNoGitHub = ref<string[]>([]);

    const translations = {
        "en-US": {
            heading: "Authors",
            labelNoGitHub: "Contributor without GitHub account: %s",
        },
        "zh-CN": {
            heading: "作者",
            labelNoGitHub: "没有 GitHub 帐户的贡献者：%s",
        },
    };

    function updateAuthors() {
        authors.value = frontmatter.value.authors || [];
        authorsNoGitHub.value = frontmatter.value["authors-nogithub"] || [];
    }

    function getAvatarUrl(author: string, size: number = 32) {
        return `https://wsrv.nl/?url=${encodeURIComponent(
            `https://github.com/${author}.png?size=${size}`
        )}&af`;
    }

    const defaultAvatarUrl = getAvatarUrl("FabricMC");

    onMounted(() => {
        updateAuthors();
    });

    watch(() => frontmatter.value, updateAuthors);
</script>

<style scoped>
    .authors-section {
        margin-top: 8px;
    }

    .authors-section > h2 {
        font-weight: 700;
        color: var(--vp-c-text-1);
        font-size: 14px;
    }

    .page-authors {
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        margin-top: 8px;
        gap: 8px;
    }

    .author-avatar {
        border-radius: 50%;
        width: 32px;
        height: 32px;
        transition: filter 0.2s ease-in-out;
    }

    .page-authors > a:hover > .author-avatar {
        filter: brightness(1.2);
    }
</style>
