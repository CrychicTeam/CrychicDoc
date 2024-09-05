<script setup>
import { ref, computed, onMounted, onBeforeUnmount, watch, nextTick } from 'vue';
import { useData, useRouter } from 'vitepress';

const { isDark } = useData();
const router = useRouter();
const showBackTop = ref(false);
const copied = ref(false);

const translations = {
  backToTop: {
    "en": "Back to Top",
    "zh": "返回顶部"
  },
  copyLink: {
    "en": "Copy Link",
    "zh": "复制链接"
  }
};

const currentLang = computed(() => {
  const path = router.route.path;
  return path.startsWith('/en/') ? 'en' : 'zh';
});

function scrollToTop() {
  window.scrollTo({
    top: 0,
    behavior: 'smooth',
  });
}

function throttle(fn, delay = 100) {
  let lastTime = 0;
  return function () {
    let nowTime = +new Date();
    if (nowTime - lastTime > delay) {
      fn.apply(this, arguments);
      lastTime = nowTime;
    }
  };
}

const onScroll = throttle(() => (showBackTop.value = Boolean(window.scrollY > 100)));

function copyLink() {
  const currentUrl = window.location.href;
  const urlWithoutLang = currentUrl.replace(/\/[a-z]{2}\//, '/');
  navigator.clipboard.writeText(urlWithoutLang).then(() => {
    copied.value = true;
    setTimeout(() => {
      copied.value = false;
    }, 2000);
  });
}

// 新增：更新主题的函数
const updateTheme = (isDarkMode) => {
  if (isDarkMode) {
    document.documentElement.classList.add('dark-mode');
  } else {
    document.documentElement.classList.remove('dark-mode');
  }
};

onMounted(async () => {
  window.addEventListener('scroll', onScroll);
  
  // 确保DOM已更新
  await nextTick();
  
  // 立即应用主题
  updateTheme(isDark.value);

  // 监听主题变化
  watch(isDark, (newValue) => {
    updateTheme(newValue);
  });
});

onBeforeUnmount(() => {
  window.removeEventListener('scroll', onScroll);
});
</script>

<template>
  <!-- 返回顶部按钮 -->
  <Transition name="fade">
    <div
      v-show="showBackTop"
      class="floating-button top-button"
      :class="{ 'dark-mode': isDark }"
      :title="translations.backToTop[currentLang]"
      @click="scrollToTop"
    >
      <svg
        t="1720595052079"
        class="icon"
        viewBox="0 0 1024 1024"
        version="1.1"
        xmlns="http://www.w3.org/2000/svg"
        p-id="4279"
        width="30"
        height="30"
      >
        <path
          d="M752.736 431.063C757.159 140.575 520.41 8.97 504.518 0.41V0l-0.45 0.205-0.41-0.205v0.41c-15.934 8.56-252.723 140.165-248.259 430.653-48.21 31.457-98.713 87.368-90.685 184.074 8.028 96.666 101.007 160.768 136.601 157.287 35.595-3.482 25.232-30.31 25.232-30.31l12.206-50.095s52.47 80.569 69.304 80.528c15.114-1.23 87-0.123 95.6 0h0.82c8.602-0.123 80.486-1.23 95.6 0 16.794 0 69.305-80.528 69.305-80.528l12.165 50.094s-10.322 26.83 25.272 30.31c35.595 3.482 128.574-60.62 136.602-157.286 8.028-96.665-42.475-152.617-90.685-184.074z m-248.669-4.26c-6.758-0.123-94.781-3.359-102.891-107.192 2.95-98.714 95.97-107.438 102.891-107.93 6.964 0.492 99.943 9.216 102.892 107.93-8.11 103.833-96.174 107.07-102.892 107.192z m-52.019 500.531c0 11.838-9.42 21.382-21.012 21.382a21.217 21.217 0 0 1-21.054-21.34V821.74c0-11.797 9.421-21.382 21.054-21.382 11.591 0 21.012 9.585 21.012 21.382v105.635z m77.333 57.222a21.504 21.504 0 0 1-21.34 21.626 21.504 21.504 0 0 1-21.34-21.626V827.474c0-11.96 9.543-21.668 21.299-21.668 11.796 0 21.38 9.708 21.38 21.668v157.082z m71.147-82.043c0 11.796-9.42 21.34-21.053 21.34a21.217 21.217 0 0 1-21.013-21.34v-75.367c0-11.755 9.421-21.299 21.013-21.299 11.632 0 21.053 9.544 21.053 21.3v75.366z"
          fill="#FFF"
          p-id="4280"
        ></path>
      </svg>
    </div>
  </Transition>
  
  <!-- 分享按钮 -->
  <button 
    @click="copyLink" 
    class="floating-button copy-button" 
    :class="{ copied, 'dark-mode': isDark }" 
    :title="translations.copyLink[currentLang]"
  >
    <svg t="1725560898807" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4269" width="30" height="30"><path d="M585.142857 365.714286a73.142857 73.142857 0 0 1 73.142857 73.142857v390.095238a73.142857 73.142857 0 0 1-73.142857 73.142857H195.047619a73.142857 73.142857 0 0 1-73.142857-73.142857V438.857143a73.142857 73.142857 0 0 1 73.142857-73.142857h390.095238z m0 73.142857H195.047619v390.095238h390.095238V438.857143z m-73.142857 219.428571v73.142857H268.190476v-73.142857h243.809524zM828.952381 121.904762a73.142857 73.142857 0 0 1 73.142857 73.142857v390.095238a73.142857 73.142857 0 0 1-73.142857 73.142857h-121.904762v-73.142857h121.904762V195.047619H438.857143v121.904762h-73.142857V195.047619a73.142857 73.142857 0 0 1 73.142857-73.142857h390.095238zM512 536.380952v73.142858H268.190476v-73.142858h243.809524z" p-id="4270" fill="#FFF"></path></svg>
  </button>
</template>

<style lang="css" scoped>
.floating-button {
  z-index: 999;
  position: fixed;
  right: 20px;
  cursor: pointer;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: var(--button-bg-color, #005dc0);
  border: none;
  padding: 10px;
  box-shadow: 2px 2px 10px 4px rgba(0, 0, 0, 0.15);
  transition: background-color 0.3s, transform 0.3s;
  display: flex;
  justify-content: center;
  align-items: center;
}

.floating-button.dark-mode {
  background-color: var(--button-bg-color-dark, #af6900);
}

.floating-button:hover {
  background-color: var(--button-hover-color, #0056b3);
  transform: scale(1.1);
}

.floating-button.dark-mode:hover {
  background-color: var(--button-hover-color-dark, #e65100);
}

.top-button {
  bottom: 80px;
}

.copy-button {
  bottom: 20px;
}

.floating-button.copied {
  background-color: var(--button-copied-color, #4caf50);
}

.dark-mode .floating-button.copied {
  background-color: var(--button-copied-color-dark, #45a049);
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}

.icon {
  fill: #FFF;
}

/* 使用CSS变量定义颜色 */
:root {
  --button-bg-color: #005dc0;
  --button-bg-color-dark: #af6900;
  --button-hover-color: #0056b3;
  --button-hover-color-dark: #e65100;
  --button-copied-color: #4caf50;
  --button-copied-color-dark: #45a049;
}

.dark-mode {
  --button-bg-color: var(--button-bg-color-dark);
  --button-hover-color: var(--button-hover-color-dark);
  --button-copied-color: var(--button-copied-color-dark);
}
</style>