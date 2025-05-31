import { demo, MarkdownItDemoOptions } from "@mdit/plugin-demo";

export const mdDemo: MarkdownItDemoOptions = {
    openRender: (tokens, index) => {
        const info: string = tokens[index].info.trim();
        return `<div class="demo">\n\t<details class="custom-block">\n\t<summary>\n\t${
            info || "Demo"
        }\n\t<hr/>`;
    },
    contentCloseRender: (tokens, idx, options, env, slf) => {
        let htmlResult = slf.renderToken(tokens, idx, options);
        return `${htmlResult}</summary>\n\t<hr/>\n\t`;
    },
    closeRender: () => {
        return `</details>\n</div>`;
    },
};
