<script setup lang="ts">
import { useData } from "vitepress";
import { watch, onMounted, onUnmounted, ref, nextTick } from "vue";
import { useRoute } from 'vitepress'

const { isDark } = useData();
const route = useRoute()
const isHomePage = ref(true)

const setHeroBackground = (isDarkMode: boolean) => {
  if (!isHomePage.value) return;
  
  const darkGradient = 'linear-gradient(-45deg, #3a2f2f 50%, #4a3f3f 50%)';
  const lightGradient = 'linear-gradient(-45deg, #93f5fc 50%, #cadfd9 50%)';
  const darkBlur = 'blur(72px)';
  const lightBlur = 'blur(68px)';

  document.documentElement.style.setProperty('--vp-home-hero-image-background-image', isDarkMode ? darkGradient : lightGradient);
  document.documentElement.style.setProperty('--vp-home-hero-image-filter', isDarkMode ? darkBlur : lightBlur);
};

const updateThemeMode = (isDarkMode: boolean) => {
  if (isDarkMode) {
    document.documentElement.classList.add('dark');
    document.documentElement.setAttribute('theme-mode', 'dark');
  } else {
    document.documentElement.classList.remove('dark');
    document.documentElement.removeAttribute('theme-mode');
  }
};

onMounted(async () => {
  isHomePage.value = route.path === '/';
  
  // 确保DOM已更新
  await nextTick();
  
  // 立即应用主题
  updateThemeMode(isDark.value);
  setHeroBackground(isDark.value);

  watch(isDark, (newValue) => {
    updateThemeMode(newValue);
    setHeroBackground(newValue);
  });

  watch(() => route.path, (newPath) => {
    isHomePage.value = newPath === '/';
    if (isHomePage.value) {
      setHeroBackground(isDark.value);
    }
  });
});

onUnmounted(() => {
  document.documentElement.style.removeProperty('--vp-home-hero-image-background-image');
  document.documentElement.style.removeProperty('--vp-home-hero-image-filter');
});
</script>

<template>
  <!-- This component doesn't render anything visible -->
</template>

<style>
:root {
  --vp-home-hero-image-background-image: linear-gradient(-45deg, #93f5fc 50%, #cadfd9 50%);
  --vp-home-hero-image-filter: blur(68px);
  --vp-c-brand: #1565C0;
  --vp-c-text-2: #546E7A;
}

.dark {
  --vp-home-hero-image-background-image: linear-gradient(-45deg, #3a2f2f 50%, #4a3f3f 50%);
  --vp-home-hero-image-filter: blur(72px);
  --vp-c-brand: #4A148C;
  --vp-c-text-2: #B0BEC5;
}
</style>