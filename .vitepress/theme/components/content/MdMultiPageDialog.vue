<template>
    <span class="md-trigger" @click="open">{{ text || t.defaultText }}</span>

    <v-dialog v-model="isOpen" v-bind="dialogProps">
        <v-card
            class="md-dialog-card"
            :class="{ 'md-dialog-card--fullscreen': fullscreen }"
        >
            <!-- Fullscreen toolbar -->
            <v-toolbar v-if="fullscreen" class="md-toolbar">
                <v-toolbar-title>{{ title || t.defaultTitle }}</v-toolbar-title>
                <v-spacer></v-spacer>
                <div class="page-indicator-toolbar">
                    {{ t.pageIndicator(currentPage + 1, pageCount) }}
                </div>
                <v-spacer></v-spacer>

                <!-- Fullscreen Navigation -->
                <div v-if="pageCount > 1" class="toolbar-navigation">
                    <v-btn
                        :disabled="currentPage === 0"
                        @click="prevPage"
                        icon
                        size="small"
                        class="toolbar-nav-btn"
                    >
                        <v-icon>mdi-chevron-left</v-icon>
                    </v-btn>

                    <span class="toolbar-page-dots">
                        <template
                            v-for="(item, index) in visiblePages"
                            :key="index"
                        >
                            <v-btn
                                v-if="item.type === 'page'"
                                size="x-small"
                                :variant="
                                    item.page === currentPage + 1
                                        ? 'flat'
                                        : 'text'
                                "
                                :color="
                                    item.page === currentPage + 1
                                        ? 'white'
                                        : 'grey-lighten-1'
                                "
                                @click="goToPage(item.page - 1)"
                                class="toolbar-dot-btn"
                            >
                                {{ item.page }}
                            </v-btn>
                            <span
                                v-else-if="item.type === 'ellipsis'"
                                class="toolbar-ellipsis"
                                >...</span
                            >
                        </template>
                    </span>

                    <v-btn
                        :disabled="currentPage === pageCount - 1"
                        @click="nextPage"
                        icon
                        size="small"
                        class="toolbar-nav-btn"
                    >
                        <v-icon>mdi-chevron-right</v-icon>
                    </v-btn>
                </div>

                <v-btn icon @click="close" class="md-close-btn">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </v-toolbar>

            <!-- Regular title -->
            <v-card-title v-else-if="title" class="md-card-title">{{
                title || t.defaultTitle
            }}</v-card-title>

            <!-- Content Area -->
            <v-card-text
                class="md-card-content"
                :class="contentWrapperClasses"
            >
                <div
                    v-if="!fullscreen && pageCount > 1"
                    class="page-indicator"
                >
                    {{ t.pageIndicator(currentPage + 1, pageCount) }}
                </div>

                <div class="vp-doc">
                    <div
                        v-for="pageIndex in pageCount"
                        :key="pageIndex"
                        class="page-content"
                        v-show="currentPage === pageIndex - 1"
                    >
                        <slot :name="`page${pageIndex}`"></slot>
                    </div>
                </div>
            </v-card-text>

            <!-- Regular Navigation -->
            <v-card-actions
                v-if="!fullscreen && pageCount > 1"
                class="navigation-toolbar md-card-actions"
            >
                <v-btn
                    :disabled="currentPage === 0"
                    @click="prevPage"
                    variant="outlined"
                    size="small"
                    prepend-icon="mdi-chevron-left"
                    class="nav-btn"
                >
                    {{ t.prevButton }}
                </v-btn>
                <v-spacer></v-spacer>
                <div class="page-dots">
                    <template
                        v-for="(item, index) in visiblePages"
                        :key="index"
                    >
                        <v-btn
                            v-if="item.type === 'page'"
                            size="small"
                            :variant="
                                item.page === currentPage + 1 ? 'flat' : 'text'
                            "
                            @click="goToPage(item.page - 1)"
                            class="dot-btn"
                        >
                            {{ item.page }}
                        </v-btn>
                        <span
                            v-else-if="item.type === 'ellipsis'"
                            class="page-ellipsis"
                            >...</span
                        >
                    </template>
                </div>
                <v-spacer></v-spacer>
                <v-btn
                    :disabled="currentPage === pageCount - 1"
                    @click="nextPage"
                    variant="outlined"
                    size="small"
                    append-icon="mdi-chevron-right"
                    class="nav-btn"
                >
                    {{ t.nextButton }}
                </v-btn>
            </v-card-actions>

            <!-- Close button for single page (or non-navigable) -->
            <v-card-actions v-else-if="!fullscreen" class="md-card-actions">
                <v-spacer></v-spacer>
                <v-btn @click="close" color="primary" variant="text">{{
                    t.closeButton
                }}</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { ref, computed } from "vue";
import { useData } from "vitepress";

const { lang } = useData();

const props = defineProps({
    title: { type: String },
    text: { type: String },
    pageCount: { type: Number, default: 1 },
    fullscreen: { type: Boolean, default: false },
    maxWidth: { type: [String, Number], default: 900 },
    maxHeight: { type: [String, Number], default: undefined },
    width: { type: [String, Number], default: undefined },
    height: { type: [String, Number], default: undefined },
    persistent: { type: Boolean, default: false },
    scrollable: { type: Boolean, default: true },
    transition: { type: String, default: "dialog-transition" },
    activator: { type: String, default: undefined },
    closeOnBack: { type: Boolean, default: true },
    contained: { type: Boolean, default: false },
    noClickAnimation: { type: Boolean, default: false },
    scrim: { type: [Boolean, String], default: true },
    zIndex: { type: [Number, String], default: 2400 },
});

const isOpen = ref(false);
const currentPage = ref(0);

const translations = {
    "zh-CN": {
        defaultTitle: "多页面对话框",
        defaultText: "点击打开",
        pageIndicator: (current, total) => `第 ${current} 页，共 ${total} 页`,
        prevButton: "上一页",
        nextButton: "下一页",
        closeButton: "关闭",
    },
    "en-US": {
        defaultTitle: "Multi-Page Dialog",
        defaultText: "Click to open",
        pageIndicator: (current, total) => `Page ${current} of ${total}`,
        prevButton: "Prev",
        nextButton: "Next",
        closeButton: "Close",
    },
};

const t = computed(() => {
    return translations[lang.value] || translations["en-US"];
});

const dialogProps = computed(() => ({
    maxWidth: props.maxWidth,
    maxHeight: props.maxHeight,
    width: props.width,
    height: props.height,
    persistent: props.persistent,
    scrollable: props.scrollable,
    transition: props.transition,
    activator: props.activator,
    closeOnBack: props.closeOnBack,
    contained: props.contained,
    noClickAnimation: props.noClickAnimation,
    scrim: props.scrim,
    zIndex: props.zIndex,
    fullscreen: props.fullscreen,
}));

const contentWrapperClasses = computed(() => ({
    "md-content--fullscreen": props.fullscreen,
    "md-content--scrollable": props.scrollable,
}));

const open = () => {
    isOpen.value = true;
    currentPage.value = 0;
};

const close = () => {
    isOpen.value = false;
};
const nextPage = () => {
    if (currentPage.value < props.pageCount - 1) currentPage.value++;
};
const prevPage = () => {
    if (currentPage.value > 0) currentPage.value--;
};
const goToPage = (index) => {
    currentPage.value = index;
};

const visiblePages = computed(() => {
    const total = props.pageCount;
    const current = currentPage.value + 1;
    const maxVisible = 7;
    if (total <= maxVisible) {
        return Array.from({ length: total }, (_, i) => ({
            type: "page",
            page: i + 1,
        }));
    }
    const result = [];
    result.push({ type: "page", page: 1 });
    let startPage, endPage;
    if (current <= 4) {
        startPage = 2;
        endPage = 5;
    } else if (current >= total - 3) {
        startPage = total - 4;
        endPage = total - 1;
    } else {
        startPage = current - 1;
        endPage = current + 1;
    }
    if (startPage > 2) result.push({ type: "ellipsis" });
    for (let i = startPage; i <= endPage; i++)
        result.push({ type: "page", page: i });
    if (endPage < total - 1) result.push({ type: "ellipsis" });
    if (total > 1) result.push({ type: "page", page: total });
    return result;
});
</script>

<style scoped>
    /* Basic Trigger */
    .md-trigger {
        color: var(--vp-c-brand-1);
        cursor: pointer;
        text-decoration: underline;
    }

    /* Dialog Card */
    .md-dialog-card {
        border-radius: 12px !important;
        background-color: var(--vp-c-bg) !important;
        border: 1px solid var(--vp-c-divider) !important;
    }
    .md-dialog-card--fullscreen {
        border-radius: 0 !important;
        height: 100vh !important;
        max-height: 100vh !important;
        border: none !important;
        display: flex !important;
        flex-direction: column !important;
        overflow: hidden !important;
    }

    /* Toolbar */
    .md-toolbar {
        background: var(--vp-c-brand-1) !important;
        color: white !important;
    }
    .md-close-btn,
    .toolbar-nav-btn {
        color: white !important;
    }
    .toolbar-nav-btn:disabled {
        color: rgba(255, 255, 255, 0.4) !important;
    }

    /* Toolbar Navigation */
    .toolbar-navigation {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-right: 16px;
    }
    .toolbar-page-dots {
        display: flex;
        gap: 4px;
        align-items: center;
    }
    .toolbar-dot-btn {
        min-width: 32px !important;
        height: 32px !important;
        font-weight: 600 !important;
    }
    .toolbar-ellipsis {
        color: rgba(255, 255, 255, 0.7) !important;
        user-select: none !important;
    }
    .page-indicator-toolbar {
        color: rgba(255, 255, 255, 0.9) !important;
        font-size: 0.875rem;
    }

    /* Card Title & Content */
    .md-card-title {
        background: var(--vp-c-bg-alt) !important;
        color: var(--vp-c-text-1) !important;
        border-bottom: 1px solid var(--vp-c-divider) !important;
        font-size: 1.25rem !important;
        font-weight: 600 !important;
        padding: 20px 24px !important;
    }
    .md-card-content {
        background: var(--vp-c-bg) !important;
        color: var(--vp-c-text-1) !important;
        padding: 24px !important;
        flex: 1;
        overflow-y: auto;
    }
    .md-content--scrollable {
        max-height: 60vh !important;
    }

    /* Regular Page Indicator */
    .page-indicator {
        background: var(--vp-c-bg-soft) !important;
        color: var(--vp-c-text-2) !important;
        padding: 8px 16px !important;
        margin-bottom: 20px !important;
        border-radius: 6px !important;
        font-size: 0.875rem !important;
        text-align: center;
    }

    /* Regular Navigation */
    .md-card-actions {
        background: var(--vp-c-bg-alt) !important;
        border-top: 1px solid var(--vp-c-divider) !important;
        padding: 16px 24px !important;
    }
    .navigation-toolbar {
        display: flex;
        justify-content: space-between !important;
        align-items: center !important;
    }
    .page-dots {
        display: flex;
        gap: 8px;
        align-items: center;
    }
    .nav-btn {
        min-width: 100px !important;
        color: var(--vp-c-brand-1) !important;
        border-color: var(--vp-c-brand-1) !important;
    }
    .dot-btn {
        min-width: 44px !important;
        height: 44px !important;
        color: var(--vp-c-text-2) !important;
    }
    .dot-btn.v-btn--variant-flat {
        background: var(--vp-c-brand-1) !important;
        color: white !important;
    }
    .page-ellipsis {
        color: var(--vp-c-text-2);
        user-select: none;
        line-height: 44px;
    }

    /* VitePress Doc Styles */
    .vp-doc {
        font-family: var(--vp-font-family-base);
        line-height: 1.7;
        color: var(--vp-c-text-1);
    }
    .vp-doc :deep(h1),
    .vp-doc :deep(h2),
    .vp-doc :deep(h3),
    .vp-doc :deep(h4) {
        font-weight: 600;
        color: var(--vp-c-text-1);
        margin-top: 32px;
    }
    .vp-doc :deep(h1) {
        font-size: 28px;
    }
    .vp-doc :deep(h2) {
        font-size: 24px;
        border-top: 1px solid var(--vp-c-divider);
        padding-top: 24px;
    }
    .vp-doc :deep(h3) {
        font-size: 20px;
    }
    .vp-doc :deep(h4) {
        font-size: 18px;
    }
    .vp-doc :deep(p) {
        margin: 16px 0;
    }
    .vp-doc :deep(a) {
        color: var(--vp-c-brand-1);
        text-decoration: underline;
    }
    .vp-doc :deep(code) {
        font-size: 0.875em;
        color: var(--vp-c-text-code);
        background-color: var(--vp-c-mute);
        border-radius: 4px;
        padding: 3px 6px;
    }
    .vp-doc :deep(pre) {
        background-color: var(--vp-code-block-bg);
        border-radius: 6px;
    }
    .vp-doc :deep(pre code) {
        padding: 20px 24px;
    }
    .vp-doc :deep(ul),
    .vp-doc :deep(ol) {
        padding-left: 1.25rem;
        margin: 16px 0;
    }
    .vp-doc :deep(li) {
        margin: 8px 0;
    }
    .vp-doc :deep(blockquote) {
        margin: 16px 0;
        border-left: 2px solid var(--vp-c-divider);
        padding-left: 16px;
        color: var(--vp-c-text-2);
    }
    .vp-doc :deep(hr) {
        margin: 16px 0;
        border-top: 1px solid var(--vp-c-divider);
    }
    .vp-doc :deep(table) {
        border-collapse: collapse;
        margin: 20px 0;
        display: block;
        overflow-x: auto;
    }
    .vp-doc :deep(th),
    .vp-doc :deep(td) {
        border: 1px solid var(--vp-c-divider);
        padding: 8px 16px;
    }
</style>
