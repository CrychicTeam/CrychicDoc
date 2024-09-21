<script lang="ts" setup>
    import { useData } from 'vitepress'
    import { computed } from 'vue'

    const { frontmatter } = useData()

    const description = computed(() => frontmatter.value?.description ?? "" );

    const isDescription = computed(() => {
            return frontmatter.value?.description ?? false;
    });
</script>
<template>
    <div v-if="isDescription">
        <v-card variant="plain">
            {{ description }}
        </v-card>
    </div>
</template>