---
Layout: doc
title: 介绍
prev:
  text: '上一页'
  link: 'zh/mods/adventure/Champions-Unofficial/2_wiki'
next:
  text: '下一页'
  link: 'zh/mods/adventure/Champions-Unofficial/2_wiki'
---

## 冠军

<v-card class="mt-4" text="该模组是冠军在1.18.2以上版本的非官方移植，该移植目前没有进行深入测试所以可能存在BUG。"variant="tonal"></v-card>

<div class="mt-8">
  <Carousel :images="carouselImages" />
</div>

<script setup>
import Carousel from '../../../../components/carousel.vue'

const carouselImages = [
  { src: "https://docs.mihono.cn/mods/adventure/champions-unofficial/1.png", alt: "Champions Unofficial 1" },
  { src: "https://docs.mihono.cn/mods/adventure/champions-unofficial/2.png", alt: "Champions Unofficial 2" },
]
</script>