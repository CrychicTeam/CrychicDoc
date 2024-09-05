import { defineConfig } from 'vitepress'
import { withMermaid } from "vitepress-plugin-mermaid";

import {en_US} from "./config/lang/en.ts"
import {zh_CN} from "./config/lang/zh.ts"
import {commonConfig} from "./config/common-config.ts"

export default withMermaid(
    defineConfig({
        ...commonConfig,
        locales: {
            root: { label: '简体中文', ...zh_CN },
            en: { label: 'English', ...en_US }
        },
        
    })
);