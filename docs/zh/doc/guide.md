---
layout: doc
title: CrychicDoc导航
layoutClass: m-nav-layout
sidebar: false
noguide: true
prev: false
next: false
editLink: false
editor: false
gitChangelog: false
showComment: false
metadata: true
outline: [2,2]
---
<style src="../../../.vitepress/theme/style/nav.scss"></style>

<script setup>
import { ref } from "vue";
import { NAV_DATA } from '../Components/guide.ts'
const NAV_DATAS = ref(NAV_DATA)
</script>

# {{ $frontmatter.title }}

<MNavLinks v-for="{title, items} in NAV_DATAS" :title="title" :items="items"/>