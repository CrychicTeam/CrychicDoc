import { createTabPlugin } from "./tab-plugin-factory";

export const stepper = createTabPlugin({
    name: "stepper",
    containerComponent: "v-stepper",
    tabComponent: "template",
    useSlots: true,

    containerRenderer: (info) => {
        const { data } = info;
        const items = data.map((tab) => `'${tab.title}'`).join(", ");
        return `<v-stepper :items="[${items}]" class="theme-stepper">`;
    },

    slotPattern: (data) => `<template v-slot:item.${data.index + 1}>`,
});
