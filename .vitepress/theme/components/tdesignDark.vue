<script setup lang="ts">
import { useData } from "vitepress";
import { watch, onMounted, onUnmounted } from "vue";

const { isDark } = useData();

const updateThemeClass = (isDarkMode: boolean) => {
  if (isDarkMode) {
    document.documentElement.classList.add('dark');
    document.documentElement.classList.remove('light');
  } else {
    document.documentElement.classList.add('light');
    document.documentElement.classList.remove('dark');
  }
};

onMounted(() => {
  updateThemeClass(isDark.value);
  watch(isDark, updateThemeClass);
});

onUnmounted(() => {
  document.documentElement.classList.remove('dark', 'light');
});
</script>

<template>
  <!-- This component doesn't render anything visible -->
</template>

<style>
:root {
  --stepper-text-color: var(--vp-c-text-1);
  --stepper-active-color: var(--vp-c-brand);
  --stepper-hover-color: var(--vp-c-brand-light);
}

.theme-stepper {
  background-color: transparent;
  color: var(--stepper-text-color);
}

.theme-stepper .v-stepper__step {
  background-color: transparent;
}

.theme-stepper .v-stepper__step--active {
  color: var(--stepper-active-color);
}

.theme-stepper .v-stepper__step:hover {
  background-color: var(--stepper-hover-color);
  opacity: 0.1;
}

.theme-stepper .v-stepper__content {
  background-color: transparent;
}

.dark .theme-stepper {
  --stepper-text-color: var(--vp-c-text-1);
  --stepper-active-color: var(--vp-c-brand-dark);
  --stepper-hover-color: var(--vp-c-brand);
}
</style>