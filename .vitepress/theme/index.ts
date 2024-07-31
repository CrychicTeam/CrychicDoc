// https://vitepress.dev/guide/custom-theme
import { h } from 'vue'
import type { Theme } from 'vitepress'
import DefaultTheme from 'vitepress/theme'
import vitepressNprogress from 'vitepress-plugin-nprogress'
import 'vitepress-plugin-nprogress/lib/css/index.css'
import "vitepress-markdown-timeline/dist/theme/index.css";
import 'viewerjs/dist/viewer.min.css';
import imageViewer from 'vitepress-plugin-image-viewer';
import vImageViewer from 'vitepress-plugin-image-viewer/lib/vImageViewer.vue';
import giscusTalk from 'vitepress-plugin-comment-with-giscus';
import { useData, useRoute } from 'vitepress';
import { toRefs } from "vue";
import codeblocksFold from 'vitepress-plugin-codeblocks-fold'; // import method
import 'vitepress-plugin-codeblocks-fold/style/index.css'; // import style
import './style.css'
import { enhanceAppWithTabs } from 'vitepress-plugin-tabs/client'

export default {
  extends: DefaultTheme,
  Layout: () => {
    return h(DefaultTheme.Layout, null, {
      // https://vitepress.dev/guide/extending-default-theme#layout-slots
    })
  },
  enhanceApp: (ctx) => {
    DefaultTheme.enhanceApp(ctx);
    vitepressNprogress(ctx)
    ctx.app.component('vImageViewer', vImageViewer);
    enhanceAppWithTabs(ctx.app);
  },
  setup() {
      const route = useRoute();
      const { frontmatter } = useData();
      // basic use
      codeblocksFold({ route, frontmatter }, true, 400);
      // 使用
      imageViewer(route);
      
      // Obtain configuration from: https://giscus.app/
      giscusTalk({
          repo: 'M1hono/CrychicGitDiscuss',
          repoId: 'R_kgDOMdKRUQ',
          category: 'Announcements', // default: `General`
          categoryId: 'DIC_kwDOMdKRUc4ChSHG',
          mapping: 'pathname', // default: `pathname`
          inputPosition: 'top', // default: `top`
          lang: 'zh-CN', // default: `zh-CN`
          // i18n setting (Note: This configuration will override the default language set by lang)
          // Configured as an object with key-value pairs inside:
          // [your i18n configuration name]: [corresponds to the language pack name in Giscus]
          locales: {
              'zh-Hans': 'zh-CN',
              'en-US': 'en'
          },
          homePageShowComment: false, // Whether to display the comment area on the homepage, the default is false
          lightTheme: 'light_protanopia', // default: `light`
          darkTheme: 'transparent_dark', // default: `transparent_dark`
          // ...
      }, {
          frontmatter, route
      },
          // Whether to activate the comment area on all pages.
          // The default is true, which means enabled, this parameter can be ignored;
          // If it is false, it means it is not enabled.
          // You can use `comment: true` preface to enable it separately on the page.
          true
      );
  }
} satisfies Theme
