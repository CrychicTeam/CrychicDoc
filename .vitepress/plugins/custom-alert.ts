import {
    createContainerPlugin,
    containerConfigMappers as mappers,
} from "./container-plugin-factory";

/**
 * Creates a configurable v-alert component based on Vuetify's API.
 * This plugin handles complex alerts with JSON-based configurations.
 *
 * @usage
 * ::: alert {"type": "success", "variant": "tonal", "title": "My Title"}
 * Content for the alert's default slot.
 * :::
 */
export const customAlert = createContainerPlugin({
        name: "alert",
    component: "CustomAlert",
    configMapping: {
        type: mappers.prop("type"),
        variant: mappers.prop("variant"),
        density: mappers.prop("density"),
        border: mappers.prop("border"),
        color: mappers.prop("color"),
        lightColor: mappers.prop("lightColor"),
        darkColor: mappers.prop("darkColor"),
        icon: mappers.prop("icon"),
        title: mappers.prop("title"),
        text: mappers.prop("text"),
        },
    });
