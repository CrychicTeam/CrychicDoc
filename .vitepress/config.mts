import { defineConfig } from 'vitepress'
import { withMermaid } from "vitepress-plugin-mermaid";

import {en_US} from "./config/lang/en"
import {zh_CN} from "./config/lang/zh"
import {commonConfig} from "./config/common-config"

/**
 * VitePress configuration with Mermaid support
 * Combines common configuration with locale-specific settings
 */
export default withMermaid(
    defineConfig({
        ...commonConfig,
        locales: {
            root: { label: '简体中文', ...zh_CN },
            en: { label: 'English', ...en_US }
            // Add other locales here
        }
    })
);