import type { App } from 'vue';
import {
    comment,
    ArticleMetadata,
    Linkcard,
    ResponsibleEditor,
    MdDialog,
    MdMultiPageDialog,
    CustomAlert
} from "../../theme/components/content";
import { YoutubeVideo, BilibiliVideo, PdfViewer } from "../../theme/components/media";
import { Footer, MNavLinks } from "../../theme/components/navigation";
import {
    Buttons,
    Carousels,
    Animation,
    Preview,
    NotFound,
} from "../../theme/components/ui";
import MagicMoveContainer from "../../theme/components/ui/MagicMoveContainer.vue";
import { defineAsyncComponent } from 'vue';
import { LiteTree } from "@lite-tree/vue";

// Async components
const CommitsCounter = defineAsyncComponent(() => import("../../theme/components/content/CommitsCounter.vue"));
const Contributors = defineAsyncComponent(() => import("../../theme/components/content/Contributors.vue"));
const MinecraftAdvancedDamageChart = defineAsyncComponent(() => import("../../theme/components/content/minecraft-advanced-damage-chart.vue"));

// Component registry
const components = {
    MdCarousel: Carousels,
    YoutubeVideo,
    BilibiliVideo,
    DamageChart: MinecraftAdvancedDamageChart,
    ArticleMetadata,
    Linkcard,
    commitsCounter: CommitsCounter,
    MNavLinks,
    PdfViewer,
    LiteTree,
    MagicMoveContainer,
    Contributors,
    Buttons,
    comment,
    ResponsibleEditor,
    Animation,
    Preview,
    NotFound,
    // Vuetify components
    MdDialog,
    MdMultiPageDialog,
    CustomAlert
};

export const registerComponents = (app: App) => {
    // Register all components
    Object.entries(components).forEach(([name, component]) => {
        if (component) {
            app.component(name, component);
        }
    });
}; 