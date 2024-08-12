<template>
  <div v-if="isMounted" class="image-viewer">
    <t-image-viewer
      v-if="show"
      v-model:visible="show"
      :images="previewImageInfo.list"
      :default-index="previewImageInfo.idx"
      :key="previewImageInfo.idx"
      @close="show = false"
    >
    </t-image-viewer>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, reactive, ref } from 'vue'

const isMounted = ref(false)
const show = ref(false)
const previewImageInfo = reactive<{ url: string; list: string[]; idx: number }>({
  url: '',
  list: [],
  idx: 0
})

function previewImage(e: Event) {
  const target = e.target as HTMLElement
  const currentTarget = e.currentTarget as HTMLElement
  if (target.tagName.toLowerCase() === 'img' && !target.classList.contains('non-preview-image')) {
    const imgs = currentTarget.querySelectorAll<HTMLImageElement>('.content-container .main img:not(.non-preview-image)')
    const idx = Array.from(imgs).findIndex(el => el === target)
    const urls = Array.from(imgs).map(el => el.src)

    const url = target.getAttribute('src')
    previewImageInfo.url = url!
    previewImageInfo.list = urls
    previewImageInfo.idx = idx

    if (idx === -1 && url) {
      previewImageInfo.list.push(url)
      previewImageInfo.idx = previewImageInfo.list.length - 1
    }
    show.value = true
  }
}

onMounted(() => {
  isMounted.value = true
  const docDomContainer = document.querySelector('#VPContent')
  docDomContainer?.addEventListener('click', previewImage)
})

onUnmounted(() => {
  const docDomContainer = document.querySelector('#VPContent')
  docDomContainer?.removeEventListener('click', previewImage)
})
</script>

<style>
.t-image-viewer__modal-icon:nth-child(7) {
  display: none !important;
}

.image-viewer {
  width: 100%;
  height: 100%;
}

.t-image-viewer__modal {
  max-width: 100vw !important;
  max-height: 100vh !important;
}

.t-image-viewer__modal-content {
  max-width: 100% !important;
  max-height: 100% !important;
}

.t-image-viewer__modal-image {
  max-width: 100% !important;
  max-height: 100% !important;
  object-fit: contain;
}

@media (max-width: 768px) {
  .t-image-viewer__modal-content {
    padding: 10px !important;
  }

  .t-image-viewer__modal-image {
    max-width: 90vw !important;
    max-height: 90vh !important;
  }
}
</style>