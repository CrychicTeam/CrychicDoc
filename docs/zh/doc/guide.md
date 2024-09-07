---
layout: doc
title: CrychicDoc导航
layoutClass: m-nav-layout
sidebar: false
noguide: true
prev: false
next: false
---
<style src="../../../.vitepress/theme/style/nav.scss"></style>

<script setup>
import { ref } from "vue";
import { NAV_DATA } from './guide.ts'
const NAV_DATAS = ref(NAV_DATA)
</script>

# {{ $frontmatter.title }}

<MNavLinks v-for="{title, items} in NAV_DATAS" :title="title" :items="items"/>