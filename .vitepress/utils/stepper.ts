import type { MarkdownItTabOptions } from "@mdit/plugin-tab";

export const stepper: MarkdownItTabOptions = {
  name: "stepper",
  tabsOpenRenderer(info) {
    const { data } = info;
    const items = data.map(tab => {
      return `'${tab.title}'`
    })
    return `\n<v-stepper :items="[${items}]">`;
  },
  tabsCloseRenderer() {
    return `\n</v-stepper>\n`;
  },
  tabOpenRenderer(data) {
    return `\n<template v-slot:item.${data.index + 1}>\n`;
  },
  tabCloseRenderer() {
    return `</template> `;
  },
}
