<script setup lang="ts">
    import { computed } from "vue";
    import { withBase, useData } from "vitepress";
    import { slugify } from "@mdit-vue/shared";

    import type { NavLink, NavIcon, NavThemeIcon } from "@utils/type";

    const { isDark } = useData();

    const props = defineProps<{
        noIcon?: boolean;
        icon?: NavLink["icon"];
        badge?: NavLink["badge"];
        title?: NavLink["title"];
        desc?: NavLink["desc"];
        link: NavLink["link"];
    }>();

    const themeIcon = (icon: NavIcon | NavThemeIcon): NavIcon => {
        if (typeof icon === "object" && "dark" in icon && "light" in icon) {
            return isDark.value ? icon.dark : icon.light;
        }
        // 如果是 NavIcon，直接返回
        return icon;
    };

    const formatTitle = computed(() => {
        if (!props.title) {
            return "";
        }
        return slugify(props.title);
    });

    const svg = computed(() => {
        if (props.icon) {
            const icon = themeIcon(props.icon);
            if (typeof icon === "object" && "svg" in icon) {
                return icon.svg;
            }
        }
        return "";
    });

    const url = computed(() => {
        if (props.icon) {
            const icon = themeIcon(props.icon);
            if (typeof icon === "string") {
                return withBase(icon);
            }
        }
        return "";
    });

    const formatBadge = computed(() => {
        if (typeof props.badge === "string") {
            return { text: props.badge, type: "info" };
        }
        return props.badge;
    });

    /**
     * Simple tooltip text - just show badge content
     */
    const tooltipText = computed(() => {
        if (!formatBadge.value) return '';
        return formatBadge.value.text;
    });
</script>

<template>
    <a
        v-if="link"
        class="m-nav-link"
        :href="link"
        target="_blank"
        rel="noreferrer"
    >
        <div class="m-nav-link-box">
            <template v-if="!noIcon">
                <div v-if="svg" class="m-nav-link-icon" v-html="svg"></div>
                <div v-else-if="url" class="m-nav-link-icon">
                    <img
                        :src="url"
                        :alt="title"
                        onerror="this.parentElement.style.display='none'"
                    />
                </div>
            </template>
            <div class="m-nav-link-content">
                <div class="m-nav-link-header">
                    <v-tooltip
                        v-if="formatBadge"
                        :text="tooltipText"
                        location="top"
                        :offset="4"
                        transition="fade-transition"
                        class="m-nav-tooltip"
                    >
                        <template v-slot:activator="{ props: tooltipProps }">
                            <Badge
                                v-bind="tooltipProps"
                                class="m-nav-link-badge"
                                :type="formatBadge.type"
                                :text="formatBadge.text"
                                :data-type="formatBadge.type"
                            />
                        </template>
                    </v-tooltip>
                <h5 v-if="title" :id="formatTitle" class="m-nav-link-title">
                    {{ title }}
                </h5>
                </div>
                <p v-if="desc" class="m-nav-link-desc">{{ desc }}</p>
            </div>
        </div>
    </a>
</template>

<style scoped>
/* Custom Vuetify tooltip styling */
:deep(.v-tooltip > .v-overlay__content) {
    background: rgba(0, 0, 0, 0.9) !important;
    color: white !important;
    border-radius: 8px !important;
    padding: 8px 12px !important;
    font-size: 12px !important;
    font-weight: 500 !important;
    letter-spacing: 0.5px !important;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3) !important;
    backdrop-filter: blur(8px) !important;
    border: 1px solid rgba(255, 255, 255, 0.1) !important;
    }

/* Dark mode tooltip */
.dark :deep(.v-tooltip > .v-overlay__content) {
    background: rgba(20, 20, 20, 0.95) !important;
    border: 1px solid rgba(255, 255, 255, 0.15) !important;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.5) !important;
    }

/* Tooltip arrow styling */
:deep(.v-tooltip .v-overlay__content::before) {
    border-top-color: rgba(0, 0, 0, 0.9) !important;
    }

.dark :deep(.v-tooltip .v-overlay__content::before) {
    border-top-color: rgba(20, 20, 20, 0.95) !important;
    }
</style>

<!-- Main styles are handled in nav.css -->
