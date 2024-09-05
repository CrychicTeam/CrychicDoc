import { container } from "@mdit/plugin-container";
import type { PluginSimple } from "markdown-it";

export const v_alert: PluginSimple = (md) => {
    const type = ["v-success", "v-info", "v-warning", "v-error"]
    type.forEach((name) =>
        md.use((md) =>
            container(md, {
                name,
                openRender: (tokens, index, _options) => {{
                        const info: string = tokens[index].info.trim().slice(name.length).trim();
                        const defaultTitle: string = name.replace("v-", "")
                        return `<p><v-alert title="${info || defaultTitle}" type="${defaultTitle}" >\n`;}
                },
                closeRender: (): string => `</v-alert></p>\n`,
            }),
        )
        
    );

    md.renderer.rules.paragraph_open = (tokens, idx, options, env, self) => {
        const token = tokens[idx - 1];
        if (token && token.type.startsWith('container_v-') && token.type.endsWith('open')) {
            return ''; // Skip paragraph opening inside container
        }
        return self.renderToken(tokens, idx, options);
    };

    md.renderer.rules.paragraph_close = (tokens, idx, options, env, self) => {
        const token = tokens[idx + 1];
        if (token && token.type.startsWith('container_v-') && token.type.endsWith('close')) {
            return '\n'; // Skip paragraph closing inside container
        }
        return self.renderToken(tokens, idx, options);
    };
};