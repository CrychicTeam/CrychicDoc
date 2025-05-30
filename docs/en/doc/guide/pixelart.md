---
layout: doc
title: Pixel Art
layoutClass: m-nav-layout
sidebar: false

prev: false
next: false
editLink: false
editor: false
gitChangelog: false
showComment: false
metadata: false
outline: [2,2]
---

<script setup>
import { ref } from "vue";
import { NAV_DATA } from './guidets/pixelart.ts'
const NAV_DATAS = ref(NAV_DATA)
</script>

# {{ $frontmatter.title }}

<MNavLinks v-for="{title, items} in NAV_DATAS" :title="title" :items="items"/>