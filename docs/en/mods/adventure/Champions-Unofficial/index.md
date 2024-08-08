---
Layout: doc
title: Introduction
prev:
  text: 'Previous'
  link: 'en/mods/adventure/Champions-Unofficial/2_wiki'
next:
  text: 'Next'
  link: 'en/mods/adventure/Champions-Unofficial/2_wiki'
---

## Champions-Unofficial

<v-card class="mb-4" text="This mod is an unofficial port of the Champions mod for Minecraft the version above 1.18.2. This version is currently under limited testing and may contain bugs." variant="tonal"></v-card>

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