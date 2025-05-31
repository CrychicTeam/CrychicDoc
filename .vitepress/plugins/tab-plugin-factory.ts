import type {
    MarkdownItTabOptions,
    MarkdownItTabInfo,
    MarkdownItTabData,
} from "@mdit/plugin-tab";
import type MarkdownIt from "markdown-it";

/**
 * Configuration for a tab-based plugin
 */
interface TabPluginConfig {
    /** Name of the plugin/container */
    name: string;

    /** Component tag name for the container */
    containerComponent: string;

    /** Component tag name for individual tabs */
    tabComponent: string;

    /** Configuration options mapping for the container */
    configMapping?: Record<string, (value: any) => string>;

    /** Custom container renderer */
    containerRenderer?: (
        info: MarkdownItTabInfo,
        config: any,
        parsedConfig: string
    ) => string;

    /** Custom container close renderer */
    containerCloseRenderer?: () => string;

    /** Custom tab renderer */
    tabRenderer?: (data: MarkdownItTabData, info: MarkdownItTabInfo) => string;

    /** Default configuration values */
    defaultConfig?: Record<string, any>;

    /** Required configuration keys */
    requiredConfig?: string[];

    /** Whether to use slots for tabs */
    useSlots?: boolean;

    /** Custom slot pattern */
    slotPattern?: (data: MarkdownItTabData) => string;
}

/**
 * Creates a tab-based markdown plugin configuration
 */
export function createTabPlugin(config: TabPluginConfig): MarkdownItTabOptions {
    const {
        name,
        containerComponent,
        tabComponent,
        configMapping = {},
        containerRenderer,
        containerCloseRenderer,
        tabRenderer,
        defaultConfig = {},
        requiredConfig = [],
        useSlots = false,
        slotPattern,
    } = config;

    return {
        name,

        openRender(
            info: MarkdownItTabInfo,
            tokens: any[],
            index: number,
            opt: any,
            env: any
        ): string {
            let parsedConfigString = "";
            let parsedConfig = { ...defaultConfig };

            const token = tokens[index];
            if (token?.meta?.id && typeof token.meta.id === "string") {
                try {
                    const configObj = JSON.parse(token.meta.id);
                    parsedConfig = { ...defaultConfig, ...configObj };

                    // Check required config
                    for (const required of requiredConfig) {
                        if (!(required in parsedConfig)) {
                            throw new Error(
                                `${required} is required for ${name}`
                            );
                        }
                    }

                    // Apply config mapping
                    for (const [key, mapper] of Object.entries(configMapping)) {
                        if (parsedConfig[key] !== undefined) {
                            parsedConfigString += ` ${mapper(
                                parsedConfig[key]
                            )}`;
                        }
                    }
                } catch (error) {
                    console.error(`Error parsing ${name} config:`, error);
                    return "";
                }
            }

            // Use custom renderer or default
            if (containerRenderer) {
                return containerRenderer(
                    info,
                    parsedConfig,
                    parsedConfigString
                );
            }

            return `<${containerComponent}${parsedConfigString}>`;
        },

        closeRender(): string {
            if (containerCloseRenderer) {
                return containerCloseRenderer();
            }
            return `</${containerComponent}>`;
        },

        tabOpenRender(data: MarkdownItTabData): string {
            if (tabRenderer) {
                // Get the full info from the parent container
                const info = {
                    active: data.index,
                    data: [data],
                } as MarkdownItTabInfo;
                return tabRenderer(data, info);
            }

            if (useSlots && slotPattern) {
                return slotPattern(data);
            }

            if (useSlots) {
                return `<template v-slot:item.${data.index + 1}>`;
            }

            return `<${tabComponent}>`;
        },

        tabCloseRender(): string {
            if (useSlots) {
                return `</template>`;
            }

            return `</${tabComponent}>`;
        },
    };
}

/**
 * Common configuration mappers
 */
export const configMappers = {
    /** Maps boolean to show/hide attribute */
    showHide: (key: string) => (value: boolean) => `:${key}="${value}"`,

    /** Maps value directly to attribute */
    direct: (key: string) => (value: any) => `${key}="${value}"`,

    /** Maps value to Vue prop */
    prop: (key: string) => (value: any) => `:${key}="${value}"`,

    /** Maps string value to attribute */
    attr: (key: string) => (value: string) => `${key}="${value}"`,

    /** Maps object to JSON prop */
    json: (key: string) => (value: object) =>
        `:${key}='${JSON.stringify(value)}'`,
};
