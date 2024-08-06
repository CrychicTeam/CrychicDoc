<template>
    <div class="giscus-wrapper">
      <component
        v-if="showComment"
        src="https://giscus.app/client.js"
        :is="'script'"
        :key="commentKey"
        :data-repo="giscusConfig.repo"
        :data-repo-id="giscusConfig.repoId"
        :data-category="giscusConfig.category"
        :data-category-id="giscusConfig.categoryId"
        :data-mapping="giscusConfig.mapping"
        :data-term="term"
        :data-strict="giscusConfig.strict"
        :data-reactions-enabled="giscusConfig.reactionsEnabled"
        :data-emit-metadata="giscusConfig.emitMetadata"
        :data-input-position="giscusConfig.inputPosition"
        :data-lang="lang"
        :data-theme="giscusConfig.theme"
        :data-loading="giscusConfig.loading"
        crossorigin="anonymous"
      />
    </div>
  </template>
  
  <script lang="ts" setup>
  import { reactive, ref, watch, computed } from "vue";
  import { useData, useRoute } from "vitepress";
  
  const route = useRoute();
  const { isDark } = useData();
  
  const lang = computed(() => route.path.startsWith("/en") ? 'en' : 'zh-Hans');
  
  const giscusConfig = reactive({
    repo: "M1hono/CrychicDoc",
    repoId: "R_kgDOMdKRUQ",
    category: "Announcements",
    categoryId: "DIC_kwDOMdKRUc4ChSHG",
    mapping: "specific",
    strict: "0",
    reactionsEnabled: "1",
    emitMetadata: "0",
    inputPosition: "top",
    loading: "lazy",
    theme: computed(() => (isDark.value ? "noborder_dark" : "noborder_light")),
  });
  
  const term = computed(() => {
    let path = route.path;
    path = path.replace(/^\/(?:zh|en)/, '').replace(/^\//, '');
    return path || 'home';
  });
  
  const commentKey = computed(() => term.value);
  
  const showComment = ref(true);
  
  watch(
    () => route.path,
    () => {
      showComment.value = false;
      setTimeout(() => {
        showComment.value = true;
      }, 200);
    },
    { immediate: true }
  );
  // 监听主题变化并更新 giscus
  watch(
    () => isDark.value,
    () => {
      const iframe = document.querySelector('iframe.giscus-frame') as HTMLIFrameElement;
      if (iframe) {
        iframe.contentWindow?.postMessage(
          { giscus: { setConfig: { theme: giscusConfig.theme } } },
          'https://giscus.app'
        );
      }
    }
  );
  </script>
  
  <style>
  .giscus-wrapper {
    margin-top: 2rem;
  }
  
  main .giscus, main .giscus-frame {
    width: 100%;
  }
  
  main .giscus-frame {
    border: none;
  }
  </style>