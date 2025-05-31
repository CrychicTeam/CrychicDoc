import { createTabPlugin, configMappers } from "./tab-plugin-factory";

export const carousels = createTabPlugin({
    name: "carousels",
    containerComponent: "MdCarousel",
    tabComponent: "v-carousel-item",
    
    configMapping: {
        arrows: configMappers.prop("show-arrows"),
        cycle: configMappers.prop("cycle"),
        interval: configMappers.prop("interval"),
        undelimiters: configMappers.prop("hide-delimiters"),
        continuous: configMappers.prop("continuous"),
        height: configMappers.attr("height"),
    },
    
    defaultConfig: {
        continuous: true,
    },
    
    containerRenderer: (info, config, parsedConfig) => {
        return `<MdCarousel
            v-model="currentIndex"${parsedConfig}
            @update:model-value="handleSlideChange"
            @before-change="onBeforeChange"
        >`;
    },
});