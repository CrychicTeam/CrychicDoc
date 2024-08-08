<script setup lang="ts">
import { onMounted, ref, watch } from "vue";
import { useData, useRoute } from "vitepress";

const data = useData();
const route = useRoute();

const authors = ref<string[]>([]);
const authorsNoGitHub = ref<string[]>([]);
const heading = ref<string>("Authors");
const labelNoGitHub = ref<string>("Contributor without GitHub account: %s");

function updateAuthors() {
  authors.value = data.frontmatter.value.authors || [];
  authorsNoGitHub.value = data.frontmatter.value["authors-nogithub"] || [];
}

function updateHeading() {
  if (route.path.startsWith('/zh/')) {
    heading.value = '作者';
  } else if (route.path.startsWith('/en/')) {
    heading.value = 'Authors';
  } else {
    heading.value = data.theme.value.authors?.heading || "Authors";
  }
}

function updateLabelNoGitHub() {
  labelNoGitHub.value = data.theme.value.authors?.nogithub || "Contributor without GitHub account: %s";
}

function getAvatarUrl(author: string, size: number = 32) {
  return `https://wsrv.nl/?url=${encodeURIComponent(
    `https://github.com/${author}.png?size=${size}`
  )}&af`;
}

const defaultAvatarUrl = getAvatarUrl('FabricMC');

onMounted(() => {
  updateAuthors();
  updateHeading();
  updateLabelNoGitHub();
});

watch(() => data.page.value, updateAuthors);
watch(() => route.path, updateHeading);
watch(() => data.theme.value, updateLabelNoGitHub);
</script>

<template>
  <div v-if="authors.length > 0 || authorsNoGitHub.length > 0" class="authors-section">
    <h2>{{ heading }}</h2>
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
        :title="labelNoGitHub.replace('%s', author)"
      />
    </div>
  </div>
</template>

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
  flex-direction: row;
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