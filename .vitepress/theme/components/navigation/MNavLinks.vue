<script setup lang="ts">
    import { computed } from "vue";
    import { slugify } from "@mdit-vue/shared";

    import MNavLink from "./MNavLink.vue";
    import type { NavLink } from "@utils/type";

    const props = defineProps<{
        title: string;
        noIcon?: boolean;
        items: NavLink[];
    }>();

    const formatTitle = computed(() => {
        return slugify(props.title);
    });
</script>

<template>
    <h2 v-if="title" :id="formatTitle" tabindex="-1">
        {{ title }}
        <a
            class="header-anchor"
            :href="`#${formatTitle}`"
            aria-hidden="true"
        ></a>
    </h2>
    <div class="m-nav-links">
        <MNavLink
            v-for="(item, index) in items"
            :key="index"
            :noIcon="noIcon"
            v-bind="item"
        />
    </div>
</template>
