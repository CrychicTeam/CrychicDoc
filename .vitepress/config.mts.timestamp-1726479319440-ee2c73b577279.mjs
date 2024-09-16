// .vitepress/config.mts
import { defineConfig } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/vitepress/dist/node/index.js";
import { withMermaid } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/vitepress-plugin-mermaid/dist/vitepress-plugin-mermaid.es.mjs";

// .vitepress/utils/sidebarGenerator.ts
import path from "path";
import fs from "fs";
import matter from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/gray-matter/index.js";
var SidebarGenerator = class _SidebarGenerator {
  dirPath;
  _correctedPathname;
  pathname;
  items;
  _sidebar;
  static DIR_PATH = path.resolve();
  static BLACK_LIST = [
    ".vitepress",
    "node_modules",
    "assets"
  ];
  constructor(pathname) {
    this._sidebar = {
      text: "",
      items: []
    };
    this.pathname = pathname;
    this.dirPath = path.join(_SidebarGenerator.DIR_PATH, pathname);
    this.items = this.filterOutWhiteList(
      fs.readdirSync(this.dirPath),
      _SidebarGenerator.BLACK_LIST
    );
    this._correctedPathname = this.pathname.replace(/^docs\//, "/");
    this.builder();
  }
  get sidebar() {
    return this._sidebar;
  }
  get correctedPathname() {
    return this._correctedPathname;
  }
  builder() {
    const root = this.indexReader()?.root;
    if (root) {
      this._sidebar.text = root.title;
      this._sidebar.collapsed = root.collapsed;
      if (root.children && root.children.length > 0) {
        this._sidebar.items.push(
          ...this.buildSidebarItems(
            root.children,
            this._correctedPathname,
            this.dirPath
          )
        );
      } else {
        this._sidebar.items.push(
          ...this.scanDirectory(this.dirPath, this._correctedPathname)
        );
      }
    } else {
      this._sidebar.items.push(
        ...this.scanDirectory(this.dirPath, this._correctedPathname)
      );
    }
  }
  buildSidebarItems(children, currentPath, currentDirPath) {
    return children.map((child) => {
      const childPath = path.join(currentDirPath, child.path);
      if (child.file) {
        return this.createFileItem(child, currentPath, childPath);
      }
      const subSideBar = {
        text: child.title,
        collapsed: child.collapsed,
        items: []
      };
      const subPath = child.path === "/" ? currentPath : `${currentPath}/${child.path}`.replace(/\/+/g, "/");
      if (child.children && child.children.length > 0) {
        subSideBar.items = this.buildSidebarItems(
          child.children,
          subPath,
          childPath
        );
      } else if (!child.noScan) {
        subSideBar.items = this.scanDirectory(childPath, subPath);
      }
      return subSideBar;
    });
  }
  scanDirectory(dirPath, currentPath) {
    const items = fs.readdirSync(dirPath);
    const filteredItems = items.filter((item) => !_SidebarGenerator.BLACK_LIST.includes(item)).map((item) => {
      const fullPath = path.join(dirPath, item);
      if (fs.statSync(fullPath).isDirectory()) {
        const sidebarItem = {
          text: item,
          collapsed: true,
          items: this.scanDirectory(
            fullPath,
            `${currentPath}/${item}`
          )
        };
        return sidebarItem;
      } else if (item.endsWith(".md")) {
        const fileContent = this.fileReader(fullPath);
        const itemWithoutMd = item.replace(/\.md$/i, "");
        const fileItem = {
          text: fileContent?.title || itemWithoutMd,
          link: `${currentPath}/${itemWithoutMd}`
        };
        return fileItem;
      }
      return null;
    });
    return filteredItems.filter(
      (item) => item !== null
    );
  }
  createFileItem(file, currentPath, filePath) {
    const fileContent = this.fileReader(filePath);
    const itemWithoutMd = (file.file || file.path).replace(/\.md$/i, "");
    let link;
    if (file.path === "/") {
      link = file.file ? `${currentPath}/${file.file.replace(/\.md$/i, "")}` : currentPath;
    } else {
      link = file.file ? `${currentPath}/${file.path}/${file.file.replace(
        /\.md$/i,
        ""
      )}` : `${currentPath}/${file.path}`;
    }
    link = link.replace(/\/+/g, "/");
    return {
      text: file.title || fileContent?.title || itemWithoutMd,
      link
    };
  }
  fileReader(filePath) {
    try {
      const fileObject = fs.readFileSync(filePath, "utf8");
      const { data } = matter(fileObject);
      if (data) {
        return data;
      } else {
        throw new Error("Invalid file format");
      }
    } catch (error) {
      return null;
    }
  }
  indexReader() {
    try {
      const indexPath = path.join(this.dirPath, "index.md");
      const indexFile = fs.readFileSync(indexPath, "utf8");
      const { data } = matter(indexFile);
      if (data.root && Array.isArray(data.root.children)) {
        return data;
      } else {
        throw new Error("Invalid index file format");
      }
    } catch (error) {
      return null;
    }
  }
  filterOutWhiteList = (files, blackList) => files.filter((file) => !blackList.includes(file));
};

// .vitepress/utils/mdParser.ts
import Path from "path";
import fs2 from "fs";
import { marked } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/marked/lib/marked.esm.js";
var gitbookParser = class {
  path;
  file;
  token;
  sidebar;
  constructor(path2) {
    this.path = path2;
    this.file = fs2.readFileSync(Path.join(path2, "SUMMARY.md"), "utf8");
    this.token = marked.lexer(this.file);
    this.locator();
  }
  locator() {
    this.token.forEach((tk) => {
      if (this.isList(tk)) {
        this.sidebar = this.listLooper(tk);
      }
    });
  }
  listLooper(list) {
    const sidebar = {
      text: "",
      collapsed: true,
      items: []
    };
    list.items.forEach((item) => {
      if (this.isListItem(item)) {
        item.tokens.forEach((litk) => {
          if (this.isText(litk) && litk.tokens && this.isLink(litk.tokens[0])) {
            const link = litk.tokens[0].href.replace(".md", "");
            const text = litk.tokens[0].text;
            const item2 = {
              text,
              link: `${this.path.replace("./docs", "")}${link}`
            };
            sidebar.items.push(item2);
          } else if (this.isList(litk)) {
            const subSidebar = this.listLooper(litk);
            sidebar.items[sidebar.items.length - 1].collapsed = true, sidebar.items[sidebar.items.length - 1].items = subSidebar.items;
          }
        });
      }
    });
    return sidebar;
  }
  isList(token) {
    return token.type === "list";
  }
  isListItem(token) {
    return token.type === "list_item";
  }
  isText(token) {
    return token.type === "text";
  }
  isLink(token) {
    return token.type === "link";
  }
};

// .vitepress/index.ts
var dirs = [
  "doc",
  "developers",
  "mods",
  "mods/adventure",
  "mods/adventure/Champions-Unofficial",
  "modpack",
  "modpack/kubejs",
  "modpack/kubejs/1.20.1",
  "modpack/kubejs/1.19.2",
  "modpack/kubejs/1.18.2"
];
var summary = [
  ["zh/modpack/kubejs/1.20.1/KubeJSCourse/", "./docs/zh/modpack/kubejs/1.20.1/KubeJSCourse"],
  ["en/modpack/kubejs/1.20.1/KubeJSCourse/", "./docs/en/modpack/kubejs/1.20.1/KubeJSCourse"],
  ["zh/modpack/kubejs/1.19.2/XPlusKubeJSTutorial/", "./docs/zh/modpack/kubejs/1.19.2/XPlusKubeJSTutorial"],
  ["en/modpack/kubejs/1.19.2/XPlusKubeJSTutorial/", "./docs/en/modpack/kubejs/1.19.2/XPlusKubeJSTutorial"],
  ["zh/modpack/kubejs/1.18.2/XPlusKubeJSTutorial/", "./docs/zh/modpack/kubejs/1.18.2/XPlusKubeJSTutorial"],
  ["en/modpack/kubejs/1.18.2/XPlusKubeJSTutorial/", "./docs/en/modpack/kubejs/1.18.2/XPlusKubeJSTutorial"]
];

// .vitepress/config/sidebarControl.ts
function sidebars(lang) {
  let ISidebar = {};
  indexParser(dirs, lang, ISidebar);
  gitbookParser2(summary, ISidebar);
  return ISidebar;
}
function indexParser(Idirs, lang, ISidebar) {
  Idirs.forEach((dir) => {
    const generator = new SidebarGenerator(`docs/${lang}/${dir}`, true);
    ISidebar[`${lang}/${dir}/`] = [generator.sidebar];
  });
}
function gitbookParser2(Isummary, ISidebar) {
  Isummary.forEach((path2) => {
    ISidebar[path2[0]] = [new gitbookParser(path2[1]).sidebar];
  });
}

// .vitepress/config/lang/en.ts
var en_US = {
  lang: "en-US",
  link: "/en/",
  title: "CryChicDoc",
  description: "A site containing docs for Minecraft developing.",
  themeConfig: {
    nav: [
      {
        text: "KubeJS",
        items: [
          {
            text: "Index",
            link: "/en/modpack/kubejs/"
          },
          {
            text: "Docs",
            items: [
              {
                text: "1.21-Planning",
                link: "..."
              },
              {
                text: "1.20.1",
                link: "/en/modpack/kubejs/1.20.1/",
                activeMatch: "/en/modpack/kubejs/1.20.1/"
              },
              {
                text: "1.19.2-Planning",
                link: "..."
              },
              {
                text: "1.18.2-Planning",
                link: "..."
              }
            ]
          },
          {
            text: "Third Party Docs",
            items: [
              {
                text: "gumeng",
                link: "/en/modpack/kubejs/1.20.1/KubeJSCourse/README",
                activeMatch: "/en/modpack/kubejs/1.20.1/"
              },
              {
                text: "Wudji-1.19.2",
                link: "en/modpack/kubejs/1.19.2/XPlusKubeJSTutorial/README"
              },
              {
                text: "Wudji-1.18.2",
                link: "en/modpack/kubejs/1.18.2/XPlusKubeJSTutorial/README"
              }
            ]
          }
        ]
      },
      { text: "Guide", link: "/en/doc/guide" }
    ],
    sidebar: sidebars("en"),
    outline: {
      level: "deep",
      label: "Page Content"
    },
    docFooter: {
      prev: "Previous Page",
      next: "Next Page"
    },
    langMenuLabel: "Change Language",
    darkModeSwitchLabel: "Switch Theme",
    lightModeSwitchTitle: "Switch to light mode",
    darkModeSwitchTitle: "Switch to dark mode"
  }
};

// .vitepress/config/lang/zh.ts
var zh_CN = {
  lang: "zh-CN",
  link: "/zh/",
  title: "CryChic\u6587\u6863",
  description: "\u4E00\u4E2A\u5305\u542B Minecraft \u5F00\u53D1\u6587\u6863\u7684\u7F51\u7AD9\u3002",
  themeConfig: {
    nav: [
      {
        text: "KubeJS",
        items: [
          {
            text: "\u4E3B\u9875",
            link: "/zh/modpack/kubejs/"
          },
          {
            text: "\u6587\u6863",
            items: [
              {
                text: "1.21-\u8BA1\u5212\u4E2D",
                link: "..."
              },
              {
                text: "1.20.1",
                link: "/zh/modpack/kubejs/1.20.1/",
                activeMatch: "/zh/modpack/kubejs/1.20.1/"
              },
              {
                text: "1.19.2-\u8BA1\u5212\u4E2D",
                link: "..."
              },
              {
                text: "1.18.2-\u8BA1\u5212\u4E2D",
                link: "..."
              }
            ]
          },
          {
            text: "\u7B2C\u4E09\u65B9\u6587\u6863",
            items: [
              {
                text: "\u5B64\u68A6",
                link: "/zh/modpack/kubejs/1.20.1/KubeJSCourse/README",
                activeMatch: "/zh/modpack/kubejs/1.20.1/"
              },
              {
                text: "Wudji-1.19.2",
                link: "zh/modpack/kubejs/1.19.2/XPlusKubeJSTutorial/README"
              },
              {
                text: "Wudji-1.18.2",
                link: "zh/modpack/kubejs/1.18.2/XPlusKubeJSTutorial/README"
              }
            ]
          }
        ]
      },
      { text: "\u5BFC\u822A", link: "/zh/doc/guide" }
    ],
    sidebar: sidebars("zh"),
    outline: {
      level: "deep",
      label: "\u9875\u9762\u5BFC\u822A"
    },
    docFooter: {
      prev: "\u4E0A\u4E00\u9875",
      next: "\u4E0B\u4E00\u9875"
    },
    langMenuLabel: "\u5207\u6362\u8BED\u8A00",
    returnToTopLabel: "\u56DE\u5230\u9876\u90E8",
    sidebarMenuLabel: "\u83DC\u5355",
    darkModeSwitchLabel: "\u4E3B\u9898",
    lightModeSwitchTitle: "\u5207\u6362\u5230\u6D45\u8272\u6A21\u5F0F",
    darkModeSwitchTitle: "\u5207\u6362\u5230\u6DF1\u8272\u6A21\u5F0F"
  }
};
var search = {
  root: {
    placeholder: "\u641C\u7D22\u6587\u6863",
    translations: {
      button: {
        buttonText: "\u641C\u7D22\u6587\u6863",
        buttonAriaLabel: "\u641C\u7D22\u6587\u6863"
      },
      modal: {
        searchBox: {
          resetButtonTitle: "\u6E05\u9664\u67E5\u8BE2\u6761\u4EF6",
          resetButtonAriaLabel: "\u6E05\u9664\u67E5\u8BE2\u6761\u4EF6",
          cancelButtonText: "\u53D6\u6D88",
          cancelButtonAriaLabel: "\u53D6\u6D88"
        },
        startScreen: {
          recentSearchesTitle: "\u641C\u7D22\u5386\u53F2",
          noRecentSearchesText: "\u6CA1\u6709\u641C\u7D22\u5386\u53F2",
          saveRecentSearchButtonTitle: "\u4FDD\u5B58\u81F3\u641C\u7D22\u5386\u53F2",
          removeRecentSearchButtonTitle: "\u4ECE\u641C\u7D22\u5386\u53F2\u4E2D\u79FB\u9664",
          favoriteSearchesTitle: "\u6536\u85CF",
          removeFavoriteSearchButtonTitle: "\u4ECE\u6536\u85CF\u4E2D\u79FB\u9664"
        },
        errorScreen: {
          titleText: "\u65E0\u6CD5\u83B7\u53D6\u7ED3\u679C",
          helpText: "\u4F60\u53EF\u80FD\u9700\u8981\u68C0\u67E5\u4F60\u7684\u7F51\u7EDC\u8FDE\u63A5"
        },
        footer: {
          selectText: "\u9009\u62E9",
          navigateText: "\u5207\u6362",
          closeText: "\u5173\u95ED",
          searchByText: "\u641C\u7D22\u63D0\u4F9B\u8005"
        },
        noResultsScreen: {
          noResultsText: "\u65E0\u6CD5\u627E\u5230\u76F8\u5173\u7ED3\u679C",
          suggestedQueryText: "\u4F60\u53EF\u4EE5\u5C1D\u8BD5\u67E5\u8BE2",
          reportMissingResultsText: "\u4F60\u8BA4\u4E3A\u8BE5\u67E5\u8BE2\u5E94\u8BE5\u6709\u7ED3\u679C\uFF1F",
          reportMissingResultsLinkText: "\u70B9\u51FB\u53CD\u9988"
        }
      }
    }
  }
};

// .vitepress/config/common-config.ts
import AutoImport from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/unplugin-auto-import/dist/vite.js";
import Components from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/unplugin-vue-components/dist/vite.js";
import { TDesignResolver } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/unplugin-vue-components/dist/resolvers.js";
import {
  groupIconVitePlugin,
  localIconLoader
} from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/vitepress-plugin-group-icons/dist/index.mjs";
import {
  GitChangelog,
  GitChangelogMarkdownSection
} from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@nolebase/vitepress-plugin-git-changelog/dist/vite/index.mjs";

// .vitepress/twoslashConfig.ts
import { cwd } from "node:process";
import { join } from "node:path";
import fs3 from "fs";
var typeFilesPath = join(cwd(), "typefiles/1.20.1/probe/generated/internals");
var internalTypeFiles = fs3.existsSync(typeFilesPath) ? fs3.readdirSync(typeFilesPath).filter(
  (file) => file.startsWith("internal_") && file.endsWith(".d.ts")
).map((file) => join(typeFilesPath, file)) : [];
var compilerOptions = {
  cache: true,
  compilerOptions: {
    baseUrl: cwd(),
    target: 99,
    module: 99,
    moduleResolution: 100,
    paths: {
      "*": [join(cwd(), "typefiles/1.20.1/probe/generated/*")]
    },
    resolveJsonModule: true,
    types: ["node", ...internalTypeFiles],
    // 自动使用文件，如果没有则保持现状
    esModuleInterop: true,
    isolatedModules: true,
    verbatimModuleSyntax: true,
    skipLibCheck: true,
    skipDefaultLibCheck: true
  }
};

// .vitepress/config/markdown-plugins.ts
import timeline from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/vitepress-markdown-timeline/dist/cjs/index.cjs.js";
import { BiDirectionalLinks } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@nolebase/markdown-it-bi-directional-links/dist/index.mjs";
import { InlineLinkPreviewElementTransform } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@nolebase/vitepress-plugin-inline-link-preview/dist/markdown-it/index.mjs";
import { tabsMarkdownPlugin } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/vitepress-plugin-tabs/dist/index.js";
import { transformerTwoslash } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@shikijs/vitepress-twoslash/dist/index.mjs";
import mdFootnote from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/markdown-it-footnote/index.mjs";
import mdTaskLists from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/markdown-it-task-lists/index.js";
import mdDeflist from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/markdown-it-deflist/index.mjs";
import mdAbbr from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/markdown-it-abbr/index.mjs";
import { imgSize } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-img-size/lib/index.js";
import { align } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-align/lib/index.js";
import { spoiler } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-spoiler/lib/index.js";
import { sub } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-sub/lib/index.js";
import { sup } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-sup/lib/index.js";
import { ruby } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-ruby/lib/index.js";
import { demo } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-demo/lib/index.js";
import { dl } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-dl/lib/index.js";

// .vitepress/plugins/stepper.ts
var stepper = {
  name: "stepper",
  tabsOpenRenderer(info) {
    const { data } = info;
    const items = data.map((tab3) => `'${tab3.title}'`);
    return `
<v-stepper :items="[${items}]" class="theme-stepper">`;
  },
  tabsCloseRenderer() {
    return `
</v-stepper>
`;
  },
  tabOpenRenderer(data) {
    return `
<template v-slot:item.${data.index + 1}>
`;
  },
  tabCloseRenderer() {
    return `</template> `;
  }
};

// .vitepress/config/markdown-plugins.ts
import { tab as tab2 } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-tab/lib/index.js";
import { mark } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-mark/lib/index.js";
import { ins } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-ins/lib/index.js";

// .vitepress/plugins/v-alert.ts
import { container } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-container/lib/index.js";
var v_alert = (md) => {
  const type = ["v-success", "v-info", "v-warning", "v-error"];
  type.forEach(
    (name) => md.use(
      (md2) => container(md2, {
        name,
        openRender: (tokens, index, _options) => {
          const info = tokens[index].info.trim().slice(name.length).trim();
          const defaultTitle = name.replace("v-", "");
          return `<p><v-alert class="v-alert" title="${info || defaultTitle}" type="${defaultTitle}" >
`;
        },
        closeRender: () => {
          return `</v-alert></p>
`;
        }
      })
    )
  );
};

// .vitepress/plugins/demo.ts
var mdDemo = {
  beforeContent: false,
  openRender: (tokens, index) => {
    const info = tokens[index].info.trim();
    return `<div class="demo">
	<details class="custom-block">
	<summary>
	${info || "Demo"}
	<hr/>`;
  },
  contentCloseRender: (tokens, idx, options, env, slf) => {
    let htmlResult = slf.renderToken(tokens, idx, options);
    return `${htmlResult}</summary>
	<hr/>
	`;
  },
  closeRender: () => {
    return `</details>
</div>`;
  }
};

// .vitepress/plugins/carousels.ts
import { tab } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-tab/lib/index.js";
var carousels = (md) => {
  md.use(tab, {
    name: "carousels",
    tabsOpenRenderer(info, tokens, index, opt, env) {
      const content = JSON.parse(JSON.stringify(env));
      const IContent = content.content;
      let token = "";
      let config = "";
      if (IContent && typeof IContent === "string") {
        const matches = IContent.match(/carousels#\{[^\}]*\}/g);
        if (matches) {
          matches.forEach((match) => {
            token += match.replace("carousels#", "");
          });
        }
      }
      try {
        const configObj = JSON.parse(token);
        if (configObj.arrows) {
          if (typeof configObj.arrows === "boolean") {
            config += ` :show-arrows="${configObj.arrows}"`;
          } else if (configObj.arrows === "hover") {
            config += ` :show-arrows="hover"`;
          }
        }
        if (configObj.undelimiters && configObj.undelimiters === true) config += ` :hide-delimiters="true"`;
        if (configObj.cycle && configObj.cycle === true) {
          config += ` :cycle="true"`;
          if (configObj.interval && typeof configObj.interval === "number") {
            config += ` :interval="${configObj.interval}"`;
          }
        }
        if (configObj.ratio && typeof configObj.ratio === "number") config += `aspectRatio="${configObj.ratio}" `;
      } catch (error) {
      }
      return `<MdCarousel${config} >`;
    },
    tabsCloseRenderer() {
      return `</MdCarousel>`;
    },
    tabOpenRenderer(data) {
      return `
<v-carousel-item cover src="${data.title}">
`;
    },
    tabCloseRenderer() {
      return `</v-carousel-item>`;
    }
  });
};

// .vitepress/plugins/card.ts
import { container as container2 } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/@mdit/plugin-container/lib/index.js";
var card = (md) => {
  const type = ["text", "flat", "elevated", "tonal", "outlined", "plain"];
  let insideContainer = false;
  type.forEach(
    (name) => {
      md.use(
        (md2) => container2(md2, {
          name,
          openRender: (tokens, index, _options) => {
            const info = tokens[index].info.trim().slice(name.length).trim();
            const titles = info.split("#");
            let title = "";
            let subTitile = "";
            switch (titles.length) {
              case 0:
                break;
              case 1:
                if (titles[0] !== "") {
                  title += `<template v-slot:title>${titles[0]}</template>`;
                }
                break;
              case 2:
                if (titles[0] !== "") {
                  title += `<template v-slot:title>${titles[0]}</template>`;
                }
                if (titles[1] !== "") {
                  subTitile += `<template v-slot:subtitle>${titles[1]}</template>`;
                }
                break;
              default:
                break;
            }
            insideContainer = true;
            return `<p><v-card variant="${name}" >${title}${subTitile}<template v-slot:text>
`;
          },
          closeRender: () => {
            insideContainer = false;
            return `</template></v-card></p>
`;
          }
        })
      );
    }
  );
  md.renderer.rules.paragraph_open = (tokens, idx, options, env, self) => {
    if (insideContainer && tokens[idx].tag === "p") return "";
    return self.renderToken(tokens, idx, options);
  };
  md.renderer.rules.paragraph_close = (tokens, idx, options, env, self) => {
    if (insideContainer && tokens[idx].tag === "p") return "";
    return self.renderToken(tokens, idx, options);
  };
};

// .vitepress/config/markdown-plugins.ts
import { groupIconMdPlugin } from "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/node_modules/vitepress-plugin-group-icons/dist/index.mjs";
var markdown = {
  math: true,
  config: async (md) => {
    md.use(InlineLinkPreviewElementTransform);
    md.use(BiDirectionalLinks());
    md.use(groupIconMdPlugin);
    md.use(timeline);
    md.use(tabsMarkdownPlugin);
    md.use(mdFootnote);
    md.use(mdTaskLists);
    md.use(mdDeflist);
    md.use(mdAbbr);
    md.use(imgSize);
    md.use(align);
    md.use(spoiler);
    md.use(sub);
    md.use(sup);
    md.use(ruby);
    md.use(demo, mdDemo);
    md.use(dl);
    md.use(v_alert);
    md.use(mark);
    md.use(ins);
    md.use(tab2, stepper);
    md.use(carousels);
    md.use(card);
    md.renderer.rules.heading_close = (tokens, idx, options, env, slf) => {
      let htmlResult = slf.renderToken(tokens, idx, options);
      if (tokens[idx].tag === "h1") htmlResult += `<ArticleMetadata />`;
      return htmlResult;
    };
  },
  codeTransformers: [
    transformerTwoslash({
      twoslashOptions: compilerOptions
    })
  ],
  image: {
    lazyLoading: true
  }
};

// .vitepress/config/common-config.ts
var __vite_injected_original_import_meta_url = "file:///C:/Users/FengMing/Desktop/1111/77/CrychicDoc/.vitepress/config/common-config.ts";
function generateAvatarUrl(username) {
  return `https://github.com/${username}.png`;
}
var commonConfig = {
  srcDir: "./docs",
  themeConfig: {
    logo: {
      alt: "CryChicDoc",
      light: "/logo.png",
      dark: "/logodark.png"
    },
    search: {
      provider: "algolia",
      options: {
        appId: "ATKJZ0G8V5",
        apiKey: "f75b80326d9a5599254436f088bcb548",
        indexName: "mihono",
        locales: {
          ...search
        }
      }
    },
    socialLinks: [
      {
        icon: "github",
        link: "https://github.com/CrychicTeam/CrychicDoc"
      },
      {
        icon: {
          svg: '<svg xmlns="http://www.w3.org/2000/svg" width="128" height="128" viewBox="0 0 24 24"><path fill="#c71d23" d="M11.984 0A12 12 0 0 0 0 12a12 12 0 0 0 12 12a12 12 0 0 0 12-12A12 12 0 0 0 12 0zm6.09 5.333c.328 0 .593.266.592.593v1.482a.594.594 0 0 1-.593.592H9.777c-.982 0-1.778.796-1.778 1.778v5.63c0 .327.266.592.593.592h5.63c.982 0 1.778-.796 1.778-1.778v-.296a.593.593 0 0 0-.592-.593h-4.15a.59.59 0 0 1-.592-.592v-1.482a.593.593 0 0 1 .593-.592h6.815c.327 0 .593.265.593.592v3.408a4 4 0 0 1-4 4H5.926a.593.593 0 0 1-.593-.593V9.778a4.444 4.444 0 0 1 4.445-4.444h8.296Z"/></svg>'
        },
        link: "https://gitee.com/CrychicTeam/CrychicDoc"
      },
      {
        icon: {
          svg: '<svg width="128" height="128" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet"><g><path id="svg_1" d="m0.22811,8.42244l0,-2.67626c0,-0.13022 0.00485,-0.25928 0.01372,-0.38725l-0.01372,3.06353l0,0l0,-0.00002zm22.13572,-6.35785c0.87539,0.97663 1.40807,2.26682 1.40807,3.68159l0,12.50765c0,3.04754 -2.47054,5.51808 -5.51808,5.51808l-12.50765,0c-1.52088,0 -2.89798,-0.61536 -3.89611,-1.61059l20.51375,-20.09673l0,0l0.00002,0z" fill="rgb(88, 182, 216)" fill-rule="evenodd" stroke="null"/><path id="svg_2" d="m1.88786,22.19821c-1.02398,-1.00178 -1.65975,-2.39874 -1.65975,-3.94439l0,-12.50765c0,-3.04754 2.47054,-5.51808 5.51808,-5.51808l12.50765,0c1.66068,0 3.14985,0.7337 4.16147,1.89447l-20.52744,20.07565l-0.00001,0z" fill="rgb(134, 193, 85)" fill-rule="evenodd" stroke="null"/><path id="svg_3" d="m19.6569,9.39041l-2.886,0c-0.94354,0.19393 -0.81466,1.06567 -0.81466,1.06567l0,3.24521c0.10339,0.93088 1.00853,0.79334 1.00853,0.79334l4.57694,0l0,1.90834l-5.01086,0c-1.95265,-0.10849 -2.36748,-1.44849 -2.36748,-1.44849c-0.19389,-0.43958 -0.1609,-0.87369 -0.1609,-0.87369l0,-3.56376c0.01292,-2.52116 1.7239,-2.874 1.7239,-2.874c0.29728,-0.10345 1.24123,-0.13795 1.24123,-0.13795l4.62009,0l-1.93077,1.88535l0,0l-0.00002,-0.00002zm-8.4846,0.36788l-2.29919,6.5757l-2.09227,0l-2.43714,-6.5757l-0.02299,6.55271l-1.90834,0l0,-8.80594l3.10391,0l2.25321,6.02391l2.23022,-6.02391l3.17291,0l0,8.85193l-2.00031,0l0,-6.59869l0,0l-0.00001,-0.00001z" fill="rgb(255, 255, 255)" fill-rule="evenodd" stroke="null"/></svg>'
        },
        link: "https://www.mcmod.cn/author/32860.html"
      }
    ],
    langMenuLabel: "Change Language",
    lastUpdated: {},
    // 添加以下配置来启用多语言支持
    //@ts-ignore
    locales: {
      root: { label: "\u7B80\u4F53\u4E2D\u6587", lang: "zh-CN" },
      "en-US": { label: "English", lang: "en-US" }
    }
  },
  markdown: { ...markdown },
  cleanUrls: true,
  mermaid: {
    startOnLoad: true,
    securityLevel: "loose",
    theme: "default"
  },
  vite: {
    optimizeDeps: {
      exclude: ["@nolebase/*"]
    },
    ssr: {
      noExternal: ["vuetify", "@nolebase/*"]
    },
    plugins: [
      GitChangelog({
        repoURL: () => "https://github.com/CrychicTeam/CrychicDoc",
        mapAuthors: [
          {
            name: "M1hono",
            // The name you want to display
            username: "M1hono",
            // The username of the author which is used to summon github's link. (won't work with links options)
            mapByNameAliases: [
              "CrychicTeam",
              "M1hono",
              "m1hono",
              "Guda chen",
              "Customer service is currently offline."
            ],
            // Add the name you want to map, these names will be replaced with the name.
            avatar: generateAvatarUrl("M1hono")
            // The avatar of the author, normally it's the github avatar
            // links: "https://gitee.com/CrychicTeam/CrychicDoc" Change to the url You want to link to
          },
          {
            name: "skyraah",
            username: "skyraah",
            mapByNameAliases: ["cyciling", "skyraah"],
            avatar: generateAvatarUrl("skyraah")
          },
          {
            name: "Eikidona",
            username: "Eikidona",
            mapByNameAliases: ["Nagasaki Soyo", "Eikidona"],
            avatar: generateAvatarUrl("Eikidona")
          }
        ]
      }),
      GitChangelogMarkdownSection(),
      groupIconVitePlugin({
        customIcon: {
          mcmeta: localIconLoader(
            __vite_injected_original_import_meta_url,
            "../../docs/public/svg/minecraft.svg"
          ),
          json: localIconLoader(
            __vite_injected_original_import_meta_url,
            "../../docs/public/svg/json.svg"
          ),
          md: localIconLoader(
            __vite_injected_original_import_meta_url,
            "../../docs/public/svg/markdown.svg"
          ),
          kubejs: localIconLoader(
            __vite_injected_original_import_meta_url,
            "../../docs/public/svg/kubejs.svg"
          ),
          js: "logos:javascript",
          sh: localIconLoader(
            __vite_injected_original_import_meta_url,
            "../../docs/public/svg/powershell.svg"
          ),
          npm: localIconLoader(
            __vite_injected_original_import_meta_url,
            "../../docs/public/svg/npm.svg"
          ),
          ts: "logos:typescript-icon-round",
          java: "logos:java",
          css: "logos:css-3",
          git: "logos:git-icon"
        }
      }),
      AutoImport({
        resolvers: [
          TDesignResolver({
            library: "vue-next"
          })
        ]
      }),
      Components({
        resolvers: [
          TDesignResolver({
            library: "vue-next"
          })
        ]
      })
    ]
    // define: {
    // __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: true,
    // },
  },
  head: [
    ["link", { rel: "icon", href: "https://docs.mihono.cn/favicon.ico" }]
  ],
  ignoreDeadLinks: true,
  transformHead({ assets }) {
    const fonts = () => {
      return [
        assets.find((file) => /JetBrainsMono-Regular\.\w+\.woff2/),
        assets.find((file) => /ChillRoundGothic_ExtraLight\.\w+\.woff2/),
        assets.find((file) => /ChillRoundGothic_Light\.\w+\.woff2/),
        assets.find((file) => /ChillRoundGothic_Regular\.\w+\.woff2/)
      ].filter((value) => value !== void 0);
    };
    const fontConfig = () => {
      return fonts().map((font) => [
        "link",
        {
          href: font,
          as: "font",
          type: "font/woff2",
          crossorigin: ""
        }
      ]);
    };
    return fontConfig();
  }
};

// .vitepress/config.mts
var config_default = withMermaid(
  defineConfig({
    ...commonConfig,
    locales: {
      root: { label: "\u7B80\u4F53\u4E2D\u6587", ...zh_CN },
      en: { label: "English", ...en_US }
    }
  })
);
export {
  config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsiLnZpdGVwcmVzcy9jb25maWcubXRzIiwgIi52aXRlcHJlc3MvdXRpbHMvc2lkZWJhckdlbmVyYXRvci50cyIsICIudml0ZXByZXNzL3V0aWxzL21kUGFyc2VyLnRzIiwgIi52aXRlcHJlc3MvaW5kZXgudHMiLCAiLnZpdGVwcmVzcy9jb25maWcvc2lkZWJhckNvbnRyb2wudHMiLCAiLnZpdGVwcmVzcy9jb25maWcvbGFuZy9lbi50cyIsICIudml0ZXByZXNzL2NvbmZpZy9sYW5nL3poLnRzIiwgIi52aXRlcHJlc3MvY29uZmlnL2NvbW1vbi1jb25maWcudHMiLCAiLnZpdGVwcmVzcy90d29zbGFzaENvbmZpZy50cyIsICIudml0ZXByZXNzL2NvbmZpZy9tYXJrZG93bi1wbHVnaW5zLnRzIiwgIi52aXRlcHJlc3MvcGx1Z2lucy9zdGVwcGVyLnRzIiwgIi52aXRlcHJlc3MvcGx1Z2lucy92LWFsZXJ0LnRzIiwgIi52aXRlcHJlc3MvcGx1Z2lucy9kZW1vLnRzIiwgIi52aXRlcHJlc3MvcGx1Z2lucy9jYXJvdXNlbHMudHMiLCAiLnZpdGVwcmVzcy9wbHVnaW5zL2NhcmQudHMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxGZW5nTWluZ1xcXFxEZXNrdG9wXFxcXDExMTFcXFxcNzdcXFxcQ3J5Y2hpY0RvY1xcXFwudml0ZXByZXNzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxGZW5nTWluZ1xcXFxEZXNrdG9wXFxcXDExMTFcXFxcNzdcXFxcQ3J5Y2hpY0RvY1xcXFwudml0ZXByZXNzXFxcXGNvbmZpZy5tdHNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0M6L1VzZXJzL0ZlbmdNaW5nL0Rlc2t0b3AvMTExMS83Ny9DcnljaGljRG9jLy52aXRlcHJlc3MvY29uZmlnLm10c1wiO2ltcG9ydCB7IGRlZmluZUNvbmZpZyB9IGZyb20gJ3ZpdGVwcmVzcydcclxuaW1wb3J0IHsgd2l0aE1lcm1haWQgfSBmcm9tIFwidml0ZXByZXNzLXBsdWdpbi1tZXJtYWlkXCI7XHJcblxyXG5pbXBvcnQge2VuX1VTfSBmcm9tIFwiLi9jb25maWcvbGFuZy9lbi50c1wiXHJcbmltcG9ydCB7emhfQ059IGZyb20gXCIuL2NvbmZpZy9sYW5nL3poLnRzXCJcclxuaW1wb3J0IHtjb21tb25Db25maWd9IGZyb20gXCIuL2NvbmZpZy9jb21tb24tY29uZmlnLnRzXCJcclxuXHJcblxyXG5leHBvcnQgZGVmYXVsdCB3aXRoTWVybWFpZChcclxuICAgIGRlZmluZUNvbmZpZyh7XHJcbiAgICAgICAgLi4uY29tbW9uQ29uZmlnLFxyXG4gICAgICAgIGxvY2FsZXM6IHtcclxuICAgICAgICAgICAgcm9vdDogeyBsYWJlbDogJ1x1N0I4MFx1NEY1M1x1NEUyRFx1NjU4NycsIC4uLnpoX0NOIH0sXHJcbiAgICAgICAgICAgIGVuOiB7IGxhYmVsOiAnRW5nbGlzaCcsIC4uLmVuX1VTIH1cclxuICAgICAgICB9LFxyXG4gICAgICAgIFxyXG4gICAgfSlcclxuKTsiLCAiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIkM6XFxcXFVzZXJzXFxcXEZlbmdNaW5nXFxcXERlc2t0b3BcXFxcMTExMVxcXFw3N1xcXFxDcnljaGljRG9jXFxcXC52aXRlcHJlc3NcXFxcdXRpbHNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkM6XFxcXFVzZXJzXFxcXEZlbmdNaW5nXFxcXERlc2t0b3BcXFxcMTExMVxcXFw3N1xcXFxDcnljaGljRG9jXFxcXC52aXRlcHJlc3NcXFxcdXRpbHNcXFxcc2lkZWJhckdlbmVyYXRvci50c1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vQzovVXNlcnMvRmVuZ01pbmcvRGVza3RvcC8xMTExLzc3L0NyeWNoaWNEb2MvLnZpdGVwcmVzcy91dGlscy9zaWRlYmFyR2VuZXJhdG9yLnRzXCI7aW1wb3J0IHBhdGggZnJvbSBcInBhdGhcIjtcclxuaW1wb3J0IGZzIGZyb20gXCJmc1wiO1xyXG5pbXBvcnQgbWF0dGVyIGZyb20gXCJncmF5LW1hdHRlclwiO1xyXG5cclxuZXhwb3J0IGRlZmF1bHQgY2xhc3MgU2lkZWJhckdlbmVyYXRvciB7XHJcbiAgICBwcml2YXRlIGRpclBhdGg6IHN0cmluZztcclxuICAgIHByaXZhdGUgX2NvcnJlY3RlZFBhdGhuYW1lOiBzdHJpbmc7XHJcbiAgICBwcml2YXRlIHBhdGhuYW1lOiBzdHJpbmc7XHJcbiAgICBwcml2YXRlIGl0ZW1zOiBzdHJpbmdbXTtcclxuICAgIHByaXZhdGUgX3NpZGViYXI6IFNpZGViYXI7XHJcblxyXG4gICAgcHJpdmF0ZSBzdGF0aWMgRElSX1BBVEg6IHN0cmluZyA9IHBhdGgucmVzb2x2ZSgpO1xyXG4gICAgcHJpdmF0ZSBzdGF0aWMgcmVhZG9ubHkgQkxBQ0tfTElTVDogc3RyaW5nW10gPSBbXHJcbiAgICAgICAgXCIudml0ZXByZXNzXCIsXHJcbiAgICAgICAgXCJub2RlX21vZHVsZXNcIixcclxuICAgICAgICBcImFzc2V0c1wiLFxyXG4gICAgXTtcclxuXHJcbiAgICBjb25zdHJ1Y3RvcihwYXRobmFtZTogc3RyaW5nKSB7XHJcbiAgICAgICAgdGhpcy5fc2lkZWJhciA9IHtcclxuICAgICAgICAgICAgdGV4dDogXCJcIixcclxuICAgICAgICAgICAgaXRlbXM6IFtdLFxyXG4gICAgICAgIH07XHJcbiAgICAgICAgdGhpcy5wYXRobmFtZSA9IHBhdGhuYW1lO1xyXG4gICAgICAgIHRoaXMuZGlyUGF0aCA9IHBhdGguam9pbihTaWRlYmFyR2VuZXJhdG9yLkRJUl9QQVRILCBwYXRobmFtZSk7XHJcbiAgICAgICAgdGhpcy5pdGVtcyA9IHRoaXMuZmlsdGVyT3V0V2hpdGVMaXN0KFxyXG4gICAgICAgICAgICBmcy5yZWFkZGlyU3luYyh0aGlzLmRpclBhdGgpLFxyXG4gICAgICAgICAgICBTaWRlYmFyR2VuZXJhdG9yLkJMQUNLX0xJU1RcclxuICAgICAgICApO1xyXG4gICAgICAgIHRoaXMuX2NvcnJlY3RlZFBhdGhuYW1lID0gdGhpcy5wYXRobmFtZS5yZXBsYWNlKC9eZG9jc1xcLy8sIFwiL1wiKTtcclxuXHJcbiAgICAgICAgdGhpcy5idWlsZGVyKCk7XHJcbiAgICB9XHJcblxyXG4gICAgcHVibGljIGdldCBzaWRlYmFyKCk6IFNpZGViYXIge1xyXG4gICAgICAgIHJldHVybiB0aGlzLl9zaWRlYmFyO1xyXG4gICAgfVxyXG5cclxuICAgIHB1YmxpYyBnZXQgY29ycmVjdGVkUGF0aG5hbWUoKTogc3RyaW5nIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5fY29ycmVjdGVkUGF0aG5hbWU7XHJcbiAgICB9XHJcblxyXG4gICAgcHJpdmF0ZSBidWlsZGVyKCk6IHZvaWQge1xyXG4gICAgICAgIGNvbnN0IHJvb3QgPSB0aGlzLmluZGV4UmVhZGVyKCk/LnJvb3Q7XHJcblxyXG4gICAgICAgIGlmIChyb290KSB7XHJcbiAgICAgICAgICAgIHRoaXMuX3NpZGViYXIudGV4dCA9IHJvb3QudGl0bGU7XHJcbiAgICAgICAgICAgIHRoaXMuX3NpZGViYXIuY29sbGFwc2VkID0gcm9vdC5jb2xsYXBzZWQ7XHJcblxyXG4gICAgICAgICAgICBpZiAocm9vdC5jaGlsZHJlbiAmJiByb290LmNoaWxkcmVuLmxlbmd0aCA+IDApIHtcclxuICAgICAgICAgICAgICAgIHRoaXMuX3NpZGViYXIuaXRlbXMucHVzaChcclxuICAgICAgICAgICAgICAgICAgICAuLi50aGlzLmJ1aWxkU2lkZWJhckl0ZW1zKFxyXG4gICAgICAgICAgICAgICAgICAgICAgICByb290LmNoaWxkcmVuLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICB0aGlzLl9jb3JyZWN0ZWRQYXRobmFtZSxcclxuICAgICAgICAgICAgICAgICAgICAgICAgdGhpcy5kaXJQYXRoXHJcbiAgICAgICAgICAgICAgICAgICAgKVxyXG4gICAgICAgICAgICAgICAgKTtcclxuICAgICAgICAgICAgfSBlbHNlIHtcclxuICAgICAgICAgICAgICAgIHRoaXMuX3NpZGViYXIuaXRlbXMucHVzaChcclxuICAgICAgICAgICAgICAgICAgICAuLi50aGlzLnNjYW5EaXJlY3RvcnkodGhpcy5kaXJQYXRoLCB0aGlzLl9jb3JyZWN0ZWRQYXRobmFtZSlcclxuICAgICAgICAgICAgICAgICk7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9IGVsc2Uge1xyXG4gICAgICAgICAgICAvLyBJZiBpbmRleC5tZCBpcyBub3QgZm91bmQsIHNjYW4gdGhlIGN1cnJlbnQgZGlyZWN0b3J5XHJcbiAgICAgICAgICAgIHRoaXMuX3NpZGViYXIuaXRlbXMucHVzaChcclxuICAgICAgICAgICAgICAgIC4uLnRoaXMuc2NhbkRpcmVjdG9yeSh0aGlzLmRpclBhdGgsIHRoaXMuX2NvcnJlY3RlZFBhdGhuYW1lKVxyXG4gICAgICAgICAgICApO1xyXG4gICAgICAgIH1cclxuICAgIH1cclxuXHJcbiAgICBwcml2YXRlIGJ1aWxkU2lkZWJhckl0ZW1zKFxyXG4gICAgICAgIGNoaWxkcmVuOiBTdWJEaXJbXSxcclxuICAgICAgICBjdXJyZW50UGF0aDogc3RyaW5nLFxyXG4gICAgICAgIGN1cnJlbnREaXJQYXRoOiBzdHJpbmdcclxuICAgICk6IEFycmF5PFNpZGViYXIgfCBGaWxlSXRlbT4ge1xyXG4gICAgICAgIHJldHVybiBjaGlsZHJlbi5tYXAoKGNoaWxkKSA9PiB7XHJcbiAgICAgICAgICAgIGNvbnN0IGNoaWxkUGF0aCA9IHBhdGguam9pbihjdXJyZW50RGlyUGF0aCwgY2hpbGQucGF0aCk7XHJcblxyXG4gICAgICAgICAgICAvLyBJZiBmaWxlIHByb3BlcnR5IGV4aXN0cywgY3JlYXRlIGEgZmlsZSBpdGVtXHJcbiAgICAgICAgICAgIGlmIChjaGlsZC5maWxlKSB7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gdGhpcy5jcmVhdGVGaWxlSXRlbShjaGlsZCwgY3VycmVudFBhdGgsIGNoaWxkUGF0aCk7XHJcbiAgICAgICAgICAgIH1cclxuXHJcbiAgICAgICAgICAgIC8vIEhhbmRsZSBkaXJlY3Rvcmllc1xyXG4gICAgICAgICAgICBjb25zdCBzdWJTaWRlQmFyOiBTaWRlYmFyID0ge1xyXG4gICAgICAgICAgICAgICAgdGV4dDogY2hpbGQudGl0bGUsXHJcbiAgICAgICAgICAgICAgICBjb2xsYXBzZWQ6IGNoaWxkLmNvbGxhcHNlZCxcclxuICAgICAgICAgICAgICAgIGl0ZW1zOiBbXSxcclxuICAgICAgICAgICAgfTtcclxuXHJcbiAgICAgICAgICAgIGNvbnN0IHN1YlBhdGggPVxyXG4gICAgICAgICAgICAgICAgY2hpbGQucGF0aCA9PT0gXCIvXCJcclxuICAgICAgICAgICAgICAgICAgICA/IGN1cnJlbnRQYXRoXHJcbiAgICAgICAgICAgICAgICAgICAgOiBgJHtjdXJyZW50UGF0aH0vJHtjaGlsZC5wYXRofWAucmVwbGFjZSgvXFwvKy9nLCBcIi9cIik7XHJcblxyXG4gICAgICAgICAgICBpZiAoY2hpbGQuY2hpbGRyZW4gJiYgY2hpbGQuY2hpbGRyZW4ubGVuZ3RoID4gMCkge1xyXG4gICAgICAgICAgICAgICAgc3ViU2lkZUJhci5pdGVtcyA9IHRoaXMuYnVpbGRTaWRlYmFySXRlbXMoXHJcbiAgICAgICAgICAgICAgICAgICAgY2hpbGQuY2hpbGRyZW4sXHJcbiAgICAgICAgICAgICAgICAgICAgc3ViUGF0aCxcclxuICAgICAgICAgICAgICAgICAgICBjaGlsZFBhdGhcclxuICAgICAgICAgICAgICAgICk7XHJcbiAgICAgICAgICAgIH0gZWxzZSBpZiAoIWNoaWxkLm5vU2Nhbikge1xyXG4gICAgICAgICAgICAgICAgLy8gT25seSBzY2FuIGRpcmVjdG9yeSBpZiBub1NjYW4gaXMgbm90IHRydWUgYW5kIGZpbGUgcHJvcGVydHkgZG9lc24ndCBleGlzdFxyXG4gICAgICAgICAgICAgICAgc3ViU2lkZUJhci5pdGVtcyA9IHRoaXMuc2NhbkRpcmVjdG9yeShjaGlsZFBhdGgsIHN1YlBhdGgpO1xyXG4gICAgICAgICAgICB9XHJcblxyXG4gICAgICAgICAgICByZXR1cm4gc3ViU2lkZUJhcjtcclxuICAgICAgICB9KTtcclxuICAgIH1cclxuXHJcbiAgICBwcml2YXRlIHNjYW5EaXJlY3RvcnkoXHJcbiAgICAgICAgZGlyUGF0aDogc3RyaW5nLFxyXG4gICAgICAgIGN1cnJlbnRQYXRoOiBzdHJpbmdcclxuICAgICk6IEFycmF5PFNpZGViYXIgfCBGaWxlSXRlbT4ge1xyXG4gICAgICAgIGNvbnN0IGl0ZW1zID0gZnMucmVhZGRpclN5bmMoZGlyUGF0aCk7XHJcbiAgICAgICAgY29uc3QgZmlsdGVyZWRJdGVtcyA9IGl0ZW1zXHJcbiAgICAgICAgICAgIC5maWx0ZXIoKGl0ZW0pID0+ICFTaWRlYmFyR2VuZXJhdG9yLkJMQUNLX0xJU1QuaW5jbHVkZXMoaXRlbSkpXHJcbiAgICAgICAgICAgIC5tYXAoKGl0ZW0pID0+IHtcclxuICAgICAgICAgICAgICAgIGNvbnN0IGZ1bGxQYXRoID0gcGF0aC5qb2luKGRpclBhdGgsIGl0ZW0pO1xyXG4gICAgICAgICAgICAgICAgaWYgKGZzLnN0YXRTeW5jKGZ1bGxQYXRoKS5pc0RpcmVjdG9yeSgpKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgY29uc3Qgc2lkZWJhckl0ZW06IFNpZGViYXIgPSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHRleHQ6IGl0ZW0sXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGNvbGxhcHNlZDogdHJ1ZSxcclxuICAgICAgICAgICAgICAgICAgICAgICAgaXRlbXM6IHRoaXMuc2NhbkRpcmVjdG9yeShcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIGZ1bGxQYXRoLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgYCR7Y3VycmVudFBhdGh9LyR7aXRlbX1gXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICksXHJcbiAgICAgICAgICAgICAgICAgICAgfTtcclxuICAgICAgICAgICAgICAgICAgICByZXR1cm4gc2lkZWJhckl0ZW07XHJcbiAgICAgICAgICAgICAgICB9IGVsc2UgaWYgKGl0ZW0uZW5kc1dpdGgoXCIubWRcIikpIHtcclxuICAgICAgICAgICAgICAgICAgICBjb25zdCBmaWxlQ29udGVudCA9IHRoaXMuZmlsZVJlYWRlcihmdWxsUGF0aCk7XHJcbiAgICAgICAgICAgICAgICAgICAgY29uc3QgaXRlbVdpdGhvdXRNZCA9IGl0ZW0ucmVwbGFjZSgvXFwubWQkL2ksIFwiXCIpO1xyXG4gICAgICAgICAgICAgICAgICAgIGNvbnN0IGZpbGVJdGVtOiBGaWxlSXRlbSA9IHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgdGV4dDogZmlsZUNvbnRlbnQ/LnRpdGxlIHx8IGl0ZW1XaXRob3V0TWQsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGxpbms6IGAke2N1cnJlbnRQYXRofS8ke2l0ZW1XaXRob3V0TWR9YCxcclxuICAgICAgICAgICAgICAgICAgICB9O1xyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiBmaWxlSXRlbTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIHJldHVybiBudWxsO1xyXG4gICAgICAgICAgICB9KTtcclxuXHJcbiAgICAgICAgcmV0dXJuIGZpbHRlcmVkSXRlbXMuZmlsdGVyKFxyXG4gICAgICAgICAgICAoaXRlbSk6IGl0ZW0gaXMgU2lkZWJhciB8IEZpbGVJdGVtID0+IGl0ZW0gIT09IG51bGxcclxuICAgICAgICApO1xyXG4gICAgfVxyXG5cclxuICAgIHByaXZhdGUgY3JlYXRlRmlsZUl0ZW0oXHJcbiAgICAgICAgZmlsZTogU3ViRGlyLFxyXG4gICAgICAgIGN1cnJlbnRQYXRoOiBzdHJpbmcsXHJcbiAgICAgICAgZmlsZVBhdGg6IHN0cmluZ1xyXG4gICAgKTogRmlsZUl0ZW0ge1xyXG4gICAgICAgIGNvbnN0IGZpbGVDb250ZW50ID0gdGhpcy5maWxlUmVhZGVyKGZpbGVQYXRoKTtcclxuICAgICAgICBjb25zdCBpdGVtV2l0aG91dE1kID0gKGZpbGUuZmlsZSB8fCBmaWxlLnBhdGgpLnJlcGxhY2UoL1xcLm1kJC9pLCBcIlwiKTtcclxuXHJcbiAgICAgICAgbGV0IGxpbms6IHN0cmluZztcclxuICAgICAgICBpZiAoZmlsZS5wYXRoID09PSBcIi9cIikge1xyXG4gICAgICAgICAgICBsaW5rID0gZmlsZS5maWxlXHJcbiAgICAgICAgICAgICAgICA/IGAke2N1cnJlbnRQYXRofS8ke2ZpbGUuZmlsZS5yZXBsYWNlKC9cXC5tZCQvaSwgXCJcIil9YFxyXG4gICAgICAgICAgICAgICAgOiBjdXJyZW50UGF0aDtcclxuICAgICAgICB9IGVsc2Uge1xyXG4gICAgICAgICAgICBsaW5rID0gZmlsZS5maWxlXHJcbiAgICAgICAgICAgICAgICA/IGAke2N1cnJlbnRQYXRofS8ke2ZpbGUucGF0aH0vJHtmaWxlLmZpbGUucmVwbGFjZShcclxuICAgICAgICAgICAgICAgICAgICAgICAgL1xcLm1kJC9pLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBcIlwiXHJcbiAgICAgICAgICAgICAgICAgICAgKX1gXHJcbiAgICAgICAgICAgICAgICA6IGAke2N1cnJlbnRQYXRofS8ke2ZpbGUucGF0aH1gO1xyXG4gICAgICAgIH1cclxuXHJcbiAgICAgICAgbGluayA9IGxpbmsucmVwbGFjZSgvXFwvKy9nLCBcIi9cIik7XHJcblxyXG4gICAgICAgIHJldHVybiB7XHJcbiAgICAgICAgICAgIHRleHQ6IGZpbGUudGl0bGUgfHwgZmlsZUNvbnRlbnQ/LnRpdGxlIHx8IGl0ZW1XaXRob3V0TWQsXHJcbiAgICAgICAgICAgIGxpbms6IGxpbmssXHJcbiAgICAgICAgfTtcclxuICAgIH1cclxuXHJcbiAgICBwcml2YXRlIGZpbGVSZWFkZXIoZmlsZVBhdGg6IHN0cmluZyk6IEZpbGVGcm9udE1hdHRlciB8IG51bGwge1xyXG4gICAgICAgIHRyeSB7XHJcbiAgICAgICAgICAgIGNvbnN0IGZpbGVPYmplY3Q6IHN0cmluZyA9IGZzLnJlYWRGaWxlU3luYyhmaWxlUGF0aCwgXCJ1dGY4XCIpO1xyXG4gICAgICAgICAgICBjb25zdCB7IGRhdGEgfSA9IG1hdHRlcihmaWxlT2JqZWN0KTtcclxuICAgICAgICAgICAgaWYgKGRhdGEpIHtcclxuICAgICAgICAgICAgICAgIHJldHVybiBkYXRhIGFzIEZpbGVGcm9udE1hdHRlcjtcclxuICAgICAgICAgICAgfSBlbHNlIHtcclxuICAgICAgICAgICAgICAgIHRocm93IG5ldyBFcnJvcihcIkludmFsaWQgZmlsZSBmb3JtYXRcIik7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9IGNhdGNoIChlcnJvcikge1xyXG4gICAgICAgICAgICByZXR1cm4gbnVsbDtcclxuICAgICAgICB9XHJcbiAgICB9XHJcblxyXG4gICAgcHJpdmF0ZSBpbmRleFJlYWRlcigpOiBJbmRleCB8IG51bGwge1xyXG4gICAgICAgIHRyeSB7XHJcbiAgICAgICAgICAgIGNvbnN0IGluZGV4UGF0aDogc3RyaW5nID0gcGF0aC5qb2luKHRoaXMuZGlyUGF0aCwgXCJpbmRleC5tZFwiKTtcclxuICAgICAgICAgICAgY29uc3QgaW5kZXhGaWxlOiBzdHJpbmcgPSBmcy5yZWFkRmlsZVN5bmMoaW5kZXhQYXRoLCBcInV0ZjhcIik7XHJcbiAgICAgICAgICAgIGNvbnN0IHsgZGF0YSB9ID0gbWF0dGVyKGluZGV4RmlsZSk7XHJcbiAgICAgICAgICAgIGlmIChkYXRhLnJvb3QgJiYgQXJyYXkuaXNBcnJheShkYXRhLnJvb3QuY2hpbGRyZW4pKSB7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gZGF0YSBhcyBJbmRleDtcclxuICAgICAgICAgICAgfSBlbHNlIHtcclxuICAgICAgICAgICAgICAgIHRocm93IG5ldyBFcnJvcihcIkludmFsaWQgaW5kZXggZmlsZSBmb3JtYXRcIik7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9IGNhdGNoIChlcnJvcikge1xyXG4gICAgICAgICAgICByZXR1cm4gbnVsbDtcclxuICAgICAgICB9XHJcbiAgICB9XHJcblxyXG4gICAgcHJpdmF0ZSBmaWx0ZXJPdXRXaGl0ZUxpc3QgPSAoZmlsZXM6IHN0cmluZ1tdLCBibGFja0xpc3Q6IHN0cmluZ1tdKSA9PlxyXG4gICAgICAgIGZpbGVzLmZpbHRlcigoZmlsZTogc3RyaW5nKSA9PiAhYmxhY2tMaXN0LmluY2x1ZGVzKGZpbGUpKTtcclxufVxyXG5cclxuZXhwb3J0IGludGVyZmFjZSBTaWRlYmFyIHtcclxuICAgIHRleHQ6IHN0cmluZztcclxuICAgIGNvbGxhcHNlZD86IGJvb2xlYW47XHJcbiAgICBsaW5rPzogc3RyaW5nO1xyXG4gICAgaXRlbXM6IEFycmF5PFNpZGViYXIgfCBGaWxlSXRlbT47XHJcbn1cclxuXHJcbmV4cG9ydCBpbnRlcmZhY2UgRmlsZUl0ZW0ge1xyXG4gICAgdGV4dDogc3RyaW5nO1xyXG4gICAgbGluazogc3RyaW5nO1xyXG4gICAgY29sbGFwc2VkPzogYm9vbGVhbjtcclxuICAgIGl0ZW1zPzogRmlsZUl0ZW1bXTtcclxufVxyXG5cclxuaW50ZXJmYWNlIEluZGV4IHtcclxuICAgIHJvb3Q6IHtcclxuICAgICAgICB0aXRsZTogc3RyaW5nO1xyXG4gICAgICAgIGNvbGxhcHNlZD86IGJvb2xlYW47XHJcbiAgICAgICAgY2hpbGRyZW46IFN1YkRpcltdO1xyXG4gICAgfTtcclxufVxyXG5cclxuaW50ZXJmYWNlIFN1YkRpciB7XHJcbiAgICB0aXRsZTogc3RyaW5nO1xyXG4gICAgcGF0aDogc3RyaW5nO1xyXG4gICAgbm9TY2FuPzogYm9vbGVhbjtcclxuICAgIGNvbGxhcHNlZD86IGJvb2xlYW47XHJcbiAgICBmaWxlPzogc3RyaW5nO1xyXG4gICAgY2hpbGRyZW4/OiBTdWJEaXJbXTtcclxufVxyXG5cclxuaW50ZXJmYWNlIEZpbGVGcm9udE1hdHRlciB7XHJcbiAgICB0aXRsZTogc3RyaW5nO1xyXG4gICAgbm9ndWlkZT86IGJvb2xlYW47XHJcbn0iLCAiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIkM6XFxcXFVzZXJzXFxcXEZlbmdNaW5nXFxcXERlc2t0b3BcXFxcMTExMVxcXFw3N1xcXFxDcnljaGljRG9jXFxcXC52aXRlcHJlc3NcXFxcdXRpbHNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkM6XFxcXFVzZXJzXFxcXEZlbmdNaW5nXFxcXERlc2t0b3BcXFxcMTExMVxcXFw3N1xcXFxDcnljaGljRG9jXFxcXC52aXRlcHJlc3NcXFxcdXRpbHNcXFxcbWRQYXJzZXIudHNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0M6L1VzZXJzL0ZlbmdNaW5nL0Rlc2t0b3AvMTExMS83Ny9DcnljaGljRG9jLy52aXRlcHJlc3MvdXRpbHMvbWRQYXJzZXIudHNcIjtpbXBvcnQgUGF0aCBmcm9tIFwicGF0aFwiO1xyXG5pbXBvcnQgZnMgZnJvbSBcImZzXCI7XHJcbmltcG9ydCB7IG1hcmtlZCwgVG9rZW5zTGlzdCwgVG9rZW5zIH0gZnJvbSAnbWFya2VkJztcclxuaW1wb3J0IHsgRmlsZUl0ZW0sIFNpZGViYXIgfSBmcm9tIFwiLi9zaWRlYmFyR2VuZXJhdG9yXCI7XHJcblxyXG5leHBvcnQgZGVmYXVsdCBjbGFzcyBnaXRib29rUGFyc2VyIHtcclxuICAgIHByaXZhdGUgcGF0aDogc3RyaW5nO1xyXG4gICAgcHJpdmF0ZSBmaWxlOiBzdHJpbmc7XHJcbiAgICBwcml2YXRlIHRva2VuOiBUb2tlbnNMaXN0O1xyXG4gICAgcHVibGljIHNpZGViYXI6IFNpZGViYXI7XHJcbiAgICBjb25zdHJ1Y3RvcihwYXRoOiBzdHJpbmcpIHtcclxuICAgICAgICB0aGlzLnBhdGggPSBwYXRoO1xyXG4gICAgICAgIHRoaXMuZmlsZSA9IGZzLnJlYWRGaWxlU3luYyhQYXRoLmpvaW4ocGF0aCwgJ1NVTU1BUlkubWQnKSwgJ3V0ZjgnKTtcclxuICAgICAgICB0aGlzLnRva2VuID0gbWFya2VkLmxleGVyKHRoaXMuZmlsZSlcclxuICAgICAgICB0aGlzLmxvY2F0b3IoKVxyXG4gICAgfVxyXG5cclxuICAgIHByaXZhdGUgbG9jYXRvcigpIHtcclxuICAgICAgICB0aGlzLnRva2VuLmZvckVhY2godGsgPT4ge1xyXG4gICAgICAgICAgICBpZiAodGhpcy5pc0xpc3QodGspKSB7XHJcbiAgICAgICAgICAgICAgICB0aGlzLnNpZGViYXIgPSB0aGlzLmxpc3RMb29wZXIodGspXHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9KVxyXG4gICAgfVxyXG4gICAgcHJpdmF0ZSBsaXN0TG9vcGVyKGxpc3Q6IFRva2Vucy5MaXN0KTogU2lkZWJhciB7XHJcbiAgICAgICAgY29uc3Qgc2lkZWJhcjogU2lkZWJhciA9IHtcclxuICAgICAgICAgICAgdGV4dDogXCJcIixcclxuICAgICAgICAgICAgY29sbGFwc2VkOiB0cnVlLFxyXG4gICAgICAgICAgICBpdGVtczogW11cclxuICAgICAgICB9XHJcbiAgICAgICAgbGlzdC5pdGVtcy5mb3JFYWNoKGl0ZW0gPT4ge1xyXG4gICAgICAgICAgICBpZih0aGlzLmlzTGlzdEl0ZW0oaXRlbSkpIHtcclxuICAgICAgICAgICAgICAgIGl0ZW0udG9rZW5zLmZvckVhY2gobGl0ayA9PiB7XHJcbiAgICAgICAgICAgICAgICAgICAgaWYgKHRoaXMuaXNUZXh0KGxpdGspICYmIGxpdGsudG9rZW5zICYmIHRoaXMuaXNMaW5rKGxpdGsudG9rZW5zWzBdKSkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBjb25zdCBsaW5rOiBzdHJpbmcgPSBsaXRrLnRva2Vuc1swXS5ocmVmLnJlcGxhY2UoXCIubWRcIiwgXCJcIilcclxuICAgICAgICAgICAgICAgICAgICAgICAgY29uc3QgdGV4dDogc3RyaW5nID0gbGl0ay50b2tlbnNbMF0udGV4dFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBjb25zdCBpdGVtOiBGaWxlSXRlbSA9IHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIHRleHQ6IHRleHQsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBsaW5rOiBgJHt0aGlzLnBhdGgucmVwbGFjZShcIi4vZG9jc1wiLCBcIlwiKX0ke2xpbmt9YFxyXG4gICAgICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHNpZGViYXIuaXRlbXMucHVzaChpdGVtKVxyXG4gICAgICAgICAgICAgICAgICAgIH0gZWxzZSBpZiAodGhpcy5pc0xpc3QobGl0aykpIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgY29uc3Qgc3ViU2lkZWJhcjogU2lkZWJhciA9IHRoaXMubGlzdExvb3BlcihsaXRrKVxyXG4gICAgICAgICAgICAgICAgICAgICAgICBzaWRlYmFyLml0ZW1zW3NpZGViYXIuaXRlbXMubGVuZ3RoIC0gMV0uY29sbGFwc2VkID0gIHRydWUsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHNpZGViYXIuaXRlbXNbc2lkZWJhci5pdGVtcy5sZW5ndGggLSAxXS5pdGVtcyA9IHN1YlNpZGViYXIuaXRlbXNcclxuICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICB9KVxyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfSlcclxuICAgICAgICByZXR1cm4gc2lkZWJhcjtcclxuICAgIH1cclxuICAgIHByaXZhdGUgaXNMaXN0KHRva2VuOiBhbnkpOiB0b2tlbiBpcyBUb2tlbnMuTGlzdCB7XHJcbiAgICAgICAgcmV0dXJuIHRva2VuLnR5cGUgPT09IFwibGlzdFwiXHJcbiAgICB9XHJcbiAgICBwcml2YXRlIGlzTGlzdEl0ZW0odG9rZW46IGFueSk6IHRva2VuIGlzIFRva2Vucy5MaXN0SXRlbSB7XHJcbiAgICAgICAgcmV0dXJuIHRva2VuLnR5cGUgPT09IFwibGlzdF9pdGVtXCJcclxuICAgIH1cclxuICAgIHByaXZhdGUgaXNUZXh0KHRva2VuOiBhbnkpOiB0b2tlbiBpcyBUb2tlbnMuVGV4dCB7XHJcbiAgICAgICAgcmV0dXJuIHRva2VuLnR5cGUgPT09IFwidGV4dFwiXHJcbiAgICB9XHJcbiAgICBwcml2YXRlIGlzTGluayh0b2tlbjogYW55KTogdG9rZW4gaXMgVG9rZW5zLkxpbmsge1xyXG4gICAgICAgIHJldHVybiB0b2tlbi50eXBlID09PSBcImxpbmtcIlxyXG4gICAgfVxyXG59IiwgImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxGZW5nTWluZ1xcXFxEZXNrdG9wXFxcXDExMTFcXFxcNzdcXFxcQ3J5Y2hpY0RvY1xcXFwudml0ZXByZXNzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxGZW5nTWluZ1xcXFxEZXNrdG9wXFxcXDExMTFcXFxcNzdcXFxcQ3J5Y2hpY0RvY1xcXFwudml0ZXByZXNzXFxcXGluZGV4LnRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9DOi9Vc2Vycy9GZW5nTWluZy9EZXNrdG9wLzExMTEvNzcvQ3J5Y2hpY0RvYy8udml0ZXByZXNzL2luZGV4LnRzXCI7Y29uc3QgZGlycyA9IFtcclxuICAgIFwiZG9jXCIsXHJcbiAgICBcImRldmVsb3BlcnNcIixcclxuICAgIFwibW9kc1wiLFxyXG4gICAgXCJtb2RzL2FkdmVudHVyZVwiLFxyXG4gICAgXCJtb2RzL2FkdmVudHVyZS9DaGFtcGlvbnMtVW5vZmZpY2lhbFwiLFxyXG4gICAgXCJtb2RwYWNrXCIsXHJcbiAgICBcIm1vZHBhY2sva3ViZWpzXCIsXHJcbiAgICBcIm1vZHBhY2sva3ViZWpzLzEuMjAuMVwiLFxyXG4gICAgXCJtb2RwYWNrL2t1YmVqcy8xLjE5LjJcIixcclxuICAgIFwibW9kcGFjay9rdWJlanMvMS4xOC4yXCJcclxuXTtcclxuXHJcbmNvbnN0IHN1bW1hcnkgPSBbXHJcbiAgICBbXCJ6aC9tb2RwYWNrL2t1YmVqcy8xLjIwLjEvS3ViZUpTQ291cnNlL1wiLCBcIi4vZG9jcy96aC9tb2RwYWNrL2t1YmVqcy8xLjIwLjEvS3ViZUpTQ291cnNlXCJdLFxyXG4gICAgW1wiZW4vbW9kcGFjay9rdWJlanMvMS4yMC4xL0t1YmVKU0NvdXJzZS9cIiwgXCIuL2RvY3MvZW4vbW9kcGFjay9rdWJlanMvMS4yMC4xL0t1YmVKU0NvdXJzZVwiXSxcclxuICAgIFtcInpoL21vZHBhY2sva3ViZWpzLzEuMTkuMi9YUGx1c0t1YmVKU1R1dG9yaWFsL1wiLCBcIi4vZG9jcy96aC9tb2RwYWNrL2t1YmVqcy8xLjE5LjIvWFBsdXNLdWJlSlNUdXRvcmlhbFwiXSxcclxuICAgIFtcImVuL21vZHBhY2sva3ViZWpzLzEuMTkuMi9YUGx1c0t1YmVKU1R1dG9yaWFsL1wiLCBcIi4vZG9jcy9lbi9tb2RwYWNrL2t1YmVqcy8xLjE5LjIvWFBsdXNLdWJlSlNUdXRvcmlhbFwiXSxcclxuICAgIFtcInpoL21vZHBhY2sva3ViZWpzLzEuMTguMi9YUGx1c0t1YmVKU1R1dG9yaWFsL1wiLCBcIi4vZG9jcy96aC9tb2RwYWNrL2t1YmVqcy8xLjE4LjIvWFBsdXNLdWJlSlNUdXRvcmlhbFwiXSxcclxuICAgIFtcImVuL21vZHBhY2sva3ViZWpzLzEuMTguMi9YUGx1c0t1YmVKU1R1dG9yaWFsL1wiLCBcIi4vZG9jcy9lbi9tb2RwYWNrL2t1YmVqcy8xLjE4LjIvWFBsdXNLdWJlSlNUdXRvcmlhbFwiXSxcclxuXVxyXG5cclxuZXhwb3J0IHtkaXJzLCBzdW1tYXJ5fSIsICJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcRmVuZ01pbmdcXFxcRGVza3RvcFxcXFwxMTExXFxcXDc3XFxcXENyeWNoaWNEb2NcXFxcLnZpdGVwcmVzc1xcXFxjb25maWdcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkM6XFxcXFVzZXJzXFxcXEZlbmdNaW5nXFxcXERlc2t0b3BcXFxcMTExMVxcXFw3N1xcXFxDcnljaGljRG9jXFxcXC52aXRlcHJlc3NcXFxcY29uZmlnXFxcXHNpZGViYXJDb250cm9sLnRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9DOi9Vc2Vycy9GZW5nTWluZy9EZXNrdG9wLzExMTEvNzcvQ3J5Y2hpY0RvYy8udml0ZXByZXNzL2NvbmZpZy9zaWRlYmFyQ29udHJvbC50c1wiO2ltcG9ydCBzaWRlYmFyIGZyb20gXCIuLi91dGlscy9zaWRlYmFyR2VuZXJhdG9yXCJcclxuaW1wb3J0IG1kIGZyb20gXCIuLi91dGlscy9tZFBhcnNlclwiXHJcbmltcG9ydCBQYXRoIGZyb20gXCJwYXRoXCI7XHJcbmltcG9ydCBmcyBmcm9tIFwiZnNcIjtcclxuaW1wb3J0IHtkaXJzLCBzdW1tYXJ5fSBmcm9tIFwiLi4vaW5kZXhcIlxyXG5cclxuZXhwb3J0IGZ1bmN0aW9uIHNpZGViYXJzKGxhbmc6IHN0cmluZyk6IHt9IHtcclxuICAgIGxldCBJU2lkZWJhciA9IHt9O1xyXG4gICAgaW5kZXhQYXJzZXIoZGlycywgbGFuZywgSVNpZGViYXIpXHJcbiAgICBnaXRib29rUGFyc2VyKHN1bW1hcnksIElTaWRlYmFyKVxyXG4gICAgXHJcbiAgICByZXR1cm4gSVNpZGViYXI7XHJcbn1cclxuXHJcbmV4cG9ydCBmdW5jdGlvbiBsb2dnZXIoc3RyaW5nOiBzdHJpbmcsIG5hbWU6IHN0cmluZyk6IHZvaWQge1xyXG4gICAgZnMud3JpdGVGaWxlU3luYyhQYXRoLmpvaW4oX19kaXJuYW1lLCBuYW1lKSwgYCR7c3RyaW5nfVxcbmAsIHsgZmxhZzogJ3crJyB9KTtcclxufVxyXG5cclxuZnVuY3Rpb24gaW5kZXhQYXJzZXIoSWRpcnM6IHN0cmluZ1tdLCBsYW5nOiBzdHJpbmcsIElTaWRlYmFyOiB7fSkge1xyXG4gICAgSWRpcnMuZm9yRWFjaChkaXIgPT4ge1xyXG4gICAgICAgIGNvbnN0IGdlbmVyYXRvciA9IG5ldyBzaWRlYmFyKGBkb2NzLyR7bGFuZ30vJHtkaXJ9YCwgdHJ1ZSk7XHJcbiAgICAgICAgSVNpZGViYXJbYCR7bGFuZ30vJHtkaXJ9L2BdID0gW2dlbmVyYXRvci5zaWRlYmFyXVxyXG4gICAgfSlcclxufVxyXG5cclxuZnVuY3Rpb24gZ2l0Ym9va1BhcnNlcihJc3VtbWFyeTogc3RyaW5nW11bXSwgSVNpZGViYXI6IHt9KSB7XHJcbiAgICBJc3VtbWFyeS5mb3JFYWNoKHBhdGggPT4ge1xyXG4gICAgICAgIElTaWRlYmFyW3BhdGhbMF1dID0gW25ldyBtZChwYXRoWzFdKS5zaWRlYmFyXVxyXG4gICAgfSlcclxufSIsICJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcRmVuZ01pbmdcXFxcRGVza3RvcFxcXFwxMTExXFxcXDc3XFxcXENyeWNoaWNEb2NcXFxcLnZpdGVwcmVzc1xcXFxjb25maWdcXFxcbGFuZ1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcRmVuZ01pbmdcXFxcRGVza3RvcFxcXFwxMTExXFxcXDc3XFxcXENyeWNoaWNEb2NcXFxcLnZpdGVwcmVzc1xcXFxjb25maWdcXFxcbGFuZ1xcXFxlbi50c1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vQzovVXNlcnMvRmVuZ01pbmcvRGVza3RvcC8xMTExLzc3L0NyeWNoaWNEb2MvLnZpdGVwcmVzcy9jb25maWcvbGFuZy9lbi50c1wiO2ltcG9ydCB7IERlZmF1bHRUaGVtZSB9IGZyb20gXCJ2aXRlcHJlc3NcIjtcclxuaW1wb3J0IHsgc2lkZWJhcnMgfSBmcm9tIFwiLi4vc2lkZWJhckNvbnRyb2xcIjtcclxuXHJcbmV4cG9ydCBjb25zdCBlbl9VUyA9IDxEZWZhdWx0VGhlbWUuQ29uZmlnPntcclxuICAgIGxhbmc6IFwiZW4tVVNcIixcclxuICAgIGxpbms6IFwiL2VuL1wiLFxyXG4gICAgdGl0bGU6IFwiQ3J5Q2hpY0RvY1wiLFxyXG4gICAgZGVzY3JpcHRpb246IFwiQSBzaXRlIGNvbnRhaW5pbmcgZG9jcyBmb3IgTWluZWNyYWZ0IGRldmVsb3BpbmcuXCIsXHJcbiAgICB0aGVtZUNvbmZpZzoge1xyXG4gICAgICAgIG5hdjogW1xyXG4gICAgICAgICAgICB7XHJcbiAgICAgICAgICAgICAgICB0ZXh0OiBcIkt1YmVKU1wiLFxyXG4gICAgICAgICAgICAgICAgaXRlbXM6IFtcclxuICAgICAgICAgICAgICAgICAgICB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHRleHQ6IFwiSW5kZXhcIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgbGluazogXCIvZW4vbW9kcGFjay9rdWJlanMvXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgfSxcclxuICAgICAgICAgICAgICAgICAgICB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHRleHQ6IFwiRG9jc1wiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBpdGVtczogW1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHRleHQ6IFwiMS4yMS1QbGFubmluZ1wiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGxpbms6IFwiLi4uXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHRleHQ6IFwiMS4yMC4xXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgbGluazogXCIvZW4vbW9kcGFjay9rdWJlanMvMS4yMC4xL1wiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGFjdGl2ZU1hdGNoOiBcIi9lbi9tb2RwYWNrL2t1YmVqcy8xLjIwLjEvXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHRleHQ6IFwiMS4xOS4yLVBsYW5uaW5nXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgbGluazogXCIuLi5cIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIH0sXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgdGV4dDogXCIxLjE4LjItUGxhbm5pbmdcIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBsaW5rOiBcIi4uLlwiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgfSxcclxuICAgICAgICAgICAgICAgICAgICAgICAgXSxcclxuICAgICAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgdGV4dDogXCJUaGlyZCBQYXJ0eSBEb2NzXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGl0ZW1zOiBbXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgdGV4dDogXCJndW1lbmdcIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBsaW5rOiBcIi9lbi9tb2RwYWNrL2t1YmVqcy8xLjIwLjEvS3ViZUpTQ291cnNlL1JFQURNRVwiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGFjdGl2ZU1hdGNoOiBcIi9lbi9tb2RwYWNrL2t1YmVqcy8xLjIwLjEvXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHRleHQ6IFwiV3VkamktMS4xOS4yXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgbGluazogXCJlbi9tb2RwYWNrL2t1YmVqcy8xLjE5LjIvWFBsdXNLdWJlSlNUdXRvcmlhbC9SRUFETUVcIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIH0sXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgdGV4dDogXCJXdWRqaS0xLjE4LjJcIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBsaW5rOiBcImVuL21vZHBhY2sva3ViZWpzLzEuMTguMi9YUGx1c0t1YmVKU1R1dG9yaWFsL1JFQURNRVwiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgfSxcclxuICAgICAgICAgICAgICAgICAgICAgICAgXSxcclxuICAgICAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgXSxcclxuICAgICAgICAgICAgfSxcclxuICAgICAgICAgICAgeyB0ZXh0OiBcIkd1aWRlXCIsIGxpbms6IFwiL2VuL2RvYy9ndWlkZVwiIH0sXHJcbiAgICAgICAgXSxcclxuICAgICAgICBzaWRlYmFyOiBzaWRlYmFycyhcImVuXCIpLFxyXG4gICAgICAgIG91dGxpbmU6IHtcclxuICAgICAgICAgICAgbGV2ZWw6IFwiZGVlcFwiLFxyXG4gICAgICAgICAgICBsYWJlbDogXCJQYWdlIENvbnRlbnRcIixcclxuICAgICAgICB9LFxyXG4gICAgICAgIGRvY0Zvb3Rlcjoge1xyXG4gICAgICAgICAgICBwcmV2OiBcIlByZXZpb3VzIFBhZ2VcIixcclxuICAgICAgICAgICAgbmV4dDogXCJOZXh0IFBhZ2VcIixcclxuICAgICAgICB9LFxyXG4gICAgICAgIGxhbmdNZW51TGFiZWw6IFwiQ2hhbmdlIExhbmd1YWdlXCIsXHJcbiAgICAgICAgZGFya01vZGVTd2l0Y2hMYWJlbDogXCJTd2l0Y2ggVGhlbWVcIixcclxuICAgICAgICBsaWdodE1vZGVTd2l0Y2hUaXRsZTogXCJTd2l0Y2ggdG8gbGlnaHQgbW9kZVwiLFxyXG4gICAgICAgIGRhcmtNb2RlU3dpdGNoVGl0bGU6IFwiU3dpdGNoIHRvIGRhcmsgbW9kZVwiLFxyXG4gICAgfSxcclxufTtcclxuIiwgImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxGZW5nTWluZ1xcXFxEZXNrdG9wXFxcXDExMTFcXFxcNzdcXFxcQ3J5Y2hpY0RvY1xcXFwudml0ZXByZXNzXFxcXGNvbmZpZ1xcXFxsYW5nXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxGZW5nTWluZ1xcXFxEZXNrdG9wXFxcXDExMTFcXFxcNzdcXFxcQ3J5Y2hpY0RvY1xcXFwudml0ZXByZXNzXFxcXGNvbmZpZ1xcXFxsYW5nXFxcXHpoLnRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9DOi9Vc2Vycy9GZW5nTWluZy9EZXNrdG9wLzExMTEvNzcvQ3J5Y2hpY0RvYy8udml0ZXByZXNzL2NvbmZpZy9sYW5nL3poLnRzXCI7aW1wb3J0IHsgRGVmYXVsdFRoZW1lIH0gZnJvbSBcInZpdGVwcmVzc1wiO1xyXG5pbXBvcnQgeyBzaWRlYmFycyB9IGZyb20gXCIuLi9zaWRlYmFyQ29udHJvbFwiO1xyXG5cclxuZXhwb3J0IGNvbnN0IHpoX0NOID0gPERlZmF1bHRUaGVtZS5Db25maWc+e1xyXG4gICAgbGFuZzogXCJ6aC1DTlwiLFxyXG4gICAgbGluazogXCIvemgvXCIsXHJcbiAgICB0aXRsZTogXCJDcnlDaGljXHU2NTg3XHU2ODYzXCIsXHJcbiAgICBkZXNjcmlwdGlvbjogXCJcdTRFMDBcdTRFMkFcdTUzMDVcdTU0MkIgTWluZWNyYWZ0IFx1NUYwMFx1NTNEMVx1NjU4N1x1Njg2M1x1NzY4NFx1N0Y1MVx1N0FEOVx1MzAwMlwiLFxyXG4gICAgdGhlbWVDb25maWc6IHtcclxuICAgICAgICBuYXY6IFtcclxuICAgICAgICAgICAge1xyXG4gICAgICAgICAgICAgICAgdGV4dDogXCJLdWJlSlNcIixcclxuICAgICAgICAgICAgICAgIGl0ZW1zOiBbXHJcbiAgICAgICAgICAgICAgICAgICAge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICB0ZXh0OiBcIlx1NEUzQlx1OTg3NVwiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBsaW5rOiBcIi96aC9tb2RwYWNrL2t1YmVqcy9cIixcclxuICAgICAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgICAgICB0ZXh0OiBcIlx1NjU4N1x1Njg2M1wiLFxyXG4gICAgICAgICAgICAgICAgICAgIGl0ZW1zOiBbXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIHRleHQ6IFwiMS4yMS1cdThCQTFcdTUyMTJcdTRFMkRcIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIGxpbms6IFwiLi4uXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIH0sXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIHRleHQ6IFwiMS4yMC4xXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBsaW5rOiBcIi96aC9tb2RwYWNrL2t1YmVqcy8xLjIwLjEvXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBhY3RpdmVNYXRjaDogXCIvemgvbW9kcGFjay9rdWJlanMvMS4yMC4xL1wiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgICAgICAgICB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB0ZXh0OiBcIjEuMTkuMi1cdThCQTFcdTUyMTJcdTRFMkRcIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIGxpbms6IFwiLi4uXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIH0sXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIHRleHQ6IFwiMS4xOC4yLVx1OEJBMVx1NTIxMlx1NEUyRFwiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgbGluazogXCIuLi5cIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgfSxcclxuICAgICAgICAgICAgICAgICAgICBdXHJcbiAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgdGV4dDogXCJcdTdCMkNcdTRFMDlcdTY1QjlcdTY1ODdcdTY4NjNcIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgaXRlbXM6IFtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB0ZXh0OiBcIlx1NUI2NFx1NjhBNlwiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGxpbms6IFwiL3poL21vZHBhY2sva3ViZWpzLzEuMjAuMS9LdWJlSlNDb3Vyc2UvUkVBRE1FXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgYWN0aXZlTWF0Y2g6IFwiL3poL21vZHBhY2sva3ViZWpzLzEuMjAuMS9cIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIH0sXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgdGV4dDogXCJXdWRqaS0xLjE5LjJcIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBsaW5rOiBcInpoL21vZHBhY2sva3ViZWpzLzEuMTkuMi9YUGx1c0t1YmVKU1R1dG9yaWFsL1JFQURNRVwiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgfSxcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB0ZXh0OiBcIld1ZGppLTEuMTguMlwiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGxpbms6IFwiemgvbW9kcGFjay9rdWJlanMvMS4xOC4yL1hQbHVzS3ViZUpTVHV0b3JpYWwvUkVBRE1FXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBdXHJcbiAgICAgICAgICAgICAgICAgICAgfSxcclxuICAgICAgICAgICAgICAgIF1cclxuICAgICAgICAgICAgfSxcclxuICAgICAgICAgICAgeyB0ZXh0OiBcIlx1NUJGQ1x1ODIyQVwiLCBsaW5rOiBcIi96aC9kb2MvZ3VpZGVcIiB9LFxyXG4gICAgICAgIF0sXHJcbiAgICAgICAgc2lkZWJhcjogc2lkZWJhcnMoXCJ6aFwiKSxcclxuICAgICAgICBvdXRsaW5lOiB7XHJcbiAgICAgICAgICAgIGxldmVsOiBcImRlZXBcIixcclxuICAgICAgICAgICAgbGFiZWw6IFwiXHU5ODc1XHU5NzYyXHU1QkZDXHU4MjJBXCIsXHJcbiAgICAgICAgfSxcclxuICAgICAgICBkb2NGb290ZXI6IHtcclxuICAgICAgICAgICAgcHJldjogXCJcdTRFMEFcdTRFMDBcdTk4NzVcIixcclxuICAgICAgICAgICAgbmV4dDogXCJcdTRFMEJcdTRFMDBcdTk4NzVcIixcclxuICAgICAgICB9LFxyXG5cclxuICAgICAgICBsYW5nTWVudUxhYmVsOiBcIlx1NTIwN1x1NjM2Mlx1OEJFRFx1OEEwMFwiLFxyXG4gICAgICAgIHJldHVyblRvVG9wTGFiZWw6IFwiXHU1NkRFXHU1MjMwXHU5ODc2XHU5MEU4XCIsXHJcbiAgICAgICAgc2lkZWJhck1lbnVMYWJlbDogXCJcdTgzRENcdTUzNTVcIixcclxuICAgICAgICBkYXJrTW9kZVN3aXRjaExhYmVsOiBcIlx1NEUzQlx1OTg5OFwiLFxyXG4gICAgICAgIGxpZ2h0TW9kZVN3aXRjaFRpdGxlOiBcIlx1NTIwN1x1NjM2Mlx1NTIzMFx1NkQ0NVx1ODI3Mlx1NkEyMVx1NUYwRlwiLFxyXG4gICAgICAgIGRhcmtNb2RlU3dpdGNoVGl0bGU6IFwiXHU1MjA3XHU2MzYyXHU1MjMwXHU2REYxXHU4MjcyXHU2QTIxXHU1RjBGXCIsXHJcbiAgICB9LFxyXG59O1xyXG5cclxuZXhwb3J0IGNvbnN0IHNlYXJjaDogRGVmYXVsdFRoZW1lLkFsZ29saWFTZWFyY2hPcHRpb25zW1wibG9jYWxlc1wiXSA9IHtcclxuICAgIHJvb3Q6IHtcclxuICAgICAgICBwbGFjZWhvbGRlcjogXCJcdTY0MUNcdTdEMjJcdTY1ODdcdTY4NjNcIixcclxuICAgICAgICB0cmFuc2xhdGlvbnM6IHtcclxuICAgICAgICAgICAgYnV0dG9uOiB7XHJcbiAgICAgICAgICAgICAgICBidXR0b25UZXh0OiBcIlx1NjQxQ1x1N0QyMlx1NjU4N1x1Njg2M1wiLFxyXG4gICAgICAgICAgICAgICAgYnV0dG9uQXJpYUxhYmVsOiBcIlx1NjQxQ1x1N0QyMlx1NjU4N1x1Njg2M1wiLFxyXG4gICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICBtb2RhbDoge1xyXG4gICAgICAgICAgICAgICAgc2VhcmNoQm94OiB7XHJcbiAgICAgICAgICAgICAgICAgICAgcmVzZXRCdXR0b25UaXRsZTogXCJcdTZFMDVcdTk2NjRcdTY3RTVcdThCRTJcdTY3NjFcdTRFRjZcIixcclxuICAgICAgICAgICAgICAgICAgICByZXNldEJ1dHRvbkFyaWFMYWJlbDogXCJcdTZFMDVcdTk2NjRcdTY3RTVcdThCRTJcdTY3NjFcdTRFRjZcIixcclxuICAgICAgICAgICAgICAgICAgICBjYW5jZWxCdXR0b25UZXh0OiBcIlx1NTNENlx1NkQ4OFwiLFxyXG4gICAgICAgICAgICAgICAgICAgIGNhbmNlbEJ1dHRvbkFyaWFMYWJlbDogXCJcdTUzRDZcdTZEODhcIixcclxuICAgICAgICAgICAgICAgIH0sXHJcbiAgICAgICAgICAgICAgICBzdGFydFNjcmVlbjoge1xyXG4gICAgICAgICAgICAgICAgICAgIHJlY2VudFNlYXJjaGVzVGl0bGU6IFwiXHU2NDFDXHU3RDIyXHU1Mzg2XHU1M0YyXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgbm9SZWNlbnRTZWFyY2hlc1RleHQ6IFwiXHU2Q0ExXHU2NzA5XHU2NDFDXHU3RDIyXHU1Mzg2XHU1M0YyXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgc2F2ZVJlY2VudFNlYXJjaEJ1dHRvblRpdGxlOiBcIlx1NEZERFx1NUI1OFx1ODFGM1x1NjQxQ1x1N0QyMlx1NTM4Nlx1NTNGMlwiLFxyXG4gICAgICAgICAgICAgICAgICAgIHJlbW92ZVJlY2VudFNlYXJjaEJ1dHRvblRpdGxlOiBcIlx1NEVDRVx1NjQxQ1x1N0QyMlx1NTM4Nlx1NTNGMlx1NEUyRFx1NzlGQlx1OTY2NFwiLFxyXG4gICAgICAgICAgICAgICAgICAgIGZhdm9yaXRlU2VhcmNoZXNUaXRsZTogXCJcdTY1MzZcdTg1Q0ZcIixcclxuICAgICAgICAgICAgICAgICAgICByZW1vdmVGYXZvcml0ZVNlYXJjaEJ1dHRvblRpdGxlOiBcIlx1NEVDRVx1NjUzNlx1ODVDRlx1NEUyRFx1NzlGQlx1OTY2NFwiLFxyXG4gICAgICAgICAgICAgICAgfSxcclxuICAgICAgICAgICAgICAgIGVycm9yU2NyZWVuOiB7XHJcbiAgICAgICAgICAgICAgICAgICAgdGl0bGVUZXh0OiBcIlx1NjVFMFx1NkNENVx1ODNCN1x1NTNENlx1N0VEM1x1Njc5Q1wiLFxyXG4gICAgICAgICAgICAgICAgICAgIGhlbHBUZXh0OiBcIlx1NEY2MFx1NTNFRlx1ODBGRFx1OTcwMFx1ODk4MVx1NjhDMFx1NjdFNVx1NEY2MFx1NzY4NFx1N0Y1MVx1N0VEQ1x1OEZERVx1NjNBNVwiLFxyXG4gICAgICAgICAgICAgICAgfSxcclxuICAgICAgICAgICAgICAgIGZvb3Rlcjoge1xyXG4gICAgICAgICAgICAgICAgICAgIHNlbGVjdFRleHQ6IFwiXHU5MDA5XHU2MkU5XCIsXHJcbiAgICAgICAgICAgICAgICAgICAgbmF2aWdhdGVUZXh0OiBcIlx1NTIwN1x1NjM2MlwiLFxyXG4gICAgICAgICAgICAgICAgICAgIGNsb3NlVGV4dDogXCJcdTUxNzNcdTk1RURcIixcclxuICAgICAgICAgICAgICAgICAgICBzZWFyY2hCeVRleHQ6IFwiXHU2NDFDXHU3RDIyXHU2M0QwXHU0RjlCXHU4MDA1XCIsXHJcbiAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgbm9SZXN1bHRzU2NyZWVuOiB7XHJcbiAgICAgICAgICAgICAgICAgICAgbm9SZXN1bHRzVGV4dDogXCJcdTY1RTBcdTZDRDVcdTYyN0VcdTUyMzBcdTc2RjhcdTUxNzNcdTdFRDNcdTY3OUNcIixcclxuICAgICAgICAgICAgICAgICAgICBzdWdnZXN0ZWRRdWVyeVRleHQ6IFwiXHU0RjYwXHU1M0VGXHU0RUU1XHU1QzFEXHU4QkQ1XHU2N0U1XHU4QkUyXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgcmVwb3J0TWlzc2luZ1Jlc3VsdHNUZXh0OiBcIlx1NEY2MFx1OEJBNFx1NEUzQVx1OEJFNVx1NjdFNVx1OEJFMlx1NUU5NFx1OEJFNVx1NjcwOVx1N0VEM1x1Njc5Q1x1RkYxRlwiLFxyXG4gICAgICAgICAgICAgICAgICAgIHJlcG9ydE1pc3NpbmdSZXN1bHRzTGlua1RleHQ6IFwiXHU3MEI5XHU1MUZCXHU1M0NEXHU5OTg4XCIsXHJcbiAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICB9LFxyXG4gICAgICAgIH0sXHJcbiAgICB9LFxyXG59O1xyXG4iLCAiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIkM6XFxcXFVzZXJzXFxcXEZlbmdNaW5nXFxcXERlc2t0b3BcXFxcMTExMVxcXFw3N1xcXFxDcnljaGljRG9jXFxcXC52aXRlcHJlc3NcXFxcY29uZmlnXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxGZW5nTWluZ1xcXFxEZXNrdG9wXFxcXDExMTFcXFxcNzdcXFxcQ3J5Y2hpY0RvY1xcXFwudml0ZXByZXNzXFxcXGNvbmZpZ1xcXFxjb21tb24tY29uZmlnLnRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9DOi9Vc2Vycy9GZW5nTWluZy9EZXNrdG9wLzExMTEvNzcvQ3J5Y2hpY0RvYy8udml0ZXByZXNzL2NvbmZpZy9jb21tb24tY29uZmlnLnRzXCI7aW1wb3J0IHsgRGVmYXVsdFRoZW1lLCBVc2VyQ29uZmlnLCBIZWFkQ29uZmlnLCBBd2FpdGFibGUgfSBmcm9tIFwidml0ZXByZXNzXCI7XHJcbmltcG9ydCBBdXRvSW1wb3J0IGZyb20gXCJ1bnBsdWdpbi1hdXRvLWltcG9ydC92aXRlXCI7XHJcbmltcG9ydCBDb21wb25lbnRzIGZyb20gXCJ1bnBsdWdpbi12dWUtY29tcG9uZW50cy92aXRlXCI7XHJcbmltcG9ydCB7IFREZXNpZ25SZXNvbHZlciB9IGZyb20gXCJ1bnBsdWdpbi12dWUtY29tcG9uZW50cy9yZXNvbHZlcnNcIjtcclxuaW1wb3J0IHtcclxuICAgIGdyb3VwSWNvblZpdGVQbHVnaW4sXHJcbiAgICBsb2NhbEljb25Mb2FkZXIsXHJcbn0gZnJvbSBcInZpdGVwcmVzcy1wbHVnaW4tZ3JvdXAtaWNvbnNcIjtcclxuaW1wb3J0IHtcclxuICAgIEdpdENoYW5nZWxvZyxcclxuICAgIEdpdENoYW5nZWxvZ01hcmtkb3duU2VjdGlvbixcclxufSBmcm9tIFwiQG5vbGViYXNlL3ZpdGVwcmVzcy1wbHVnaW4tZ2l0LWNoYW5nZWxvZy92aXRlXCI7XHJcbmltcG9ydCAqIGFzIGNvbmZpZyBmcm9tIFwiLi9tYXJrZG93bi1wbHVnaW5zXCI7XHJcblxyXG5pbXBvcnQgeyBzZWFyY2ggYXMgemhTZWFyY2ggfSBmcm9tIFwiLi9sYW5nL3poXCI7XHJcblxyXG5mdW5jdGlvbiBnZW5lcmF0ZUF2YXRhclVybCh1c2VybmFtZTogc3RyaW5nKSB7XHJcbiAgICByZXR1cm4gYGh0dHBzOi8vZ2l0aHViLmNvbS8ke3VzZXJuYW1lfS5wbmdgO1xyXG59XHJcblxyXG5leHBvcnQgY29uc3QgY29tbW9uQ29uZmlnOiBVc2VyQ29uZmlnPERlZmF1bHRUaGVtZS5Db25maWc+ID0ge1xyXG4gICAgc3JjRGlyOiBcIi4vZG9jc1wiLFxyXG4gICAgdGhlbWVDb25maWc6IHtcclxuICAgICAgICBsb2dvOiB7XHJcbiAgICAgICAgICAgIGFsdDogXCJDcnlDaGljRG9jXCIsXHJcbiAgICAgICAgICAgIGxpZ2h0OiBcIi9sb2dvLnBuZ1wiLFxyXG4gICAgICAgICAgICBkYXJrOiBcIi9sb2dvZGFyay5wbmdcIixcclxuICAgICAgICB9LFxyXG4gICAgICAgIHNlYXJjaDoge1xyXG4gICAgICAgICAgICBwcm92aWRlcjogXCJhbGdvbGlhXCIsXHJcbiAgICAgICAgICAgIG9wdGlvbnM6IHtcclxuICAgICAgICAgICAgICAgIGFwcElkOiBcIkFUS0paMEc4VjVcIixcclxuICAgICAgICAgICAgICAgIGFwaUtleTogXCJmNzViODAzMjZkOWE1NTk5MjU0NDM2ZjA4OGJjYjU0OFwiLFxyXG4gICAgICAgICAgICAgICAgaW5kZXhOYW1lOiBcIm1paG9ub1wiLFxyXG4gICAgICAgICAgICAgICAgbG9jYWxlczoge1xyXG4gICAgICAgICAgICAgICAgICAgIC4uLnpoU2VhcmNoLFxyXG4gICAgICAgICAgICAgICAgfSxcclxuICAgICAgICAgICAgfSxcclxuICAgICAgICB9LFxyXG4gICAgICAgIHNvY2lhbExpbmtzOiBbXHJcbiAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgIGljb246IFwiZ2l0aHViXCIsXHJcbiAgICAgICAgICAgICAgICBsaW5rOiBcImh0dHBzOi8vZ2l0aHViLmNvbS9DcnljaGljVGVhbS9DcnljaGljRG9jXCIsXHJcbiAgICAgICAgICAgIH0sXHJcbiAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgIGljb246IHtcclxuICAgICAgICAgICAgICAgICAgICBzdmc6ICc8c3ZnIHhtbG5zPVwiaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmdcIiB3aWR0aD1cIjEyOFwiIGhlaWdodD1cIjEyOFwiIHZpZXdCb3g9XCIwIDAgMjQgMjRcIj48cGF0aCBmaWxsPVwiI2M3MWQyM1wiIGQ9XCJNMTEuOTg0IDBBMTIgMTIgMCAwIDAgMCAxMmExMiAxMiAwIDAgMCAxMiAxMmExMiAxMiAwIDAgMCAxMi0xMkExMiAxMiAwIDAgMCAxMiAwem02LjA5IDUuMzMzYy4zMjggMCAuNTkzLjI2Ni41OTIuNTkzdjEuNDgyYS41OTQuNTk0IDAgMCAxLS41OTMuNTkySDkuNzc3Yy0uOTgyIDAtMS43NzguNzk2LTEuNzc4IDEuNzc4djUuNjNjMCAuMzI3LjI2Ni41OTIuNTkzLjU5Mmg1LjYzYy45ODIgMCAxLjc3OC0uNzk2IDEuNzc4LTEuNzc4di0uMjk2YS41OTMuNTkzIDAgMCAwLS41OTItLjU5M2gtNC4xNWEuNTkuNTkgMCAwIDEtLjU5Mi0uNTkydi0xLjQ4MmEuNTkzLjU5MyAwIDAgMSAuNTkzLS41OTJoNi44MTVjLjMyNyAwIC41OTMuMjY1LjU5My41OTJ2My40MDhhNCA0IDAgMCAxLTQgNEg1LjkyNmEuNTkzLjU5MyAwIDAgMS0uNTkzLS41OTNWOS43NzhhNC40NDQgNC40NDQgMCAwIDEgNC40NDUtNC40NDRoOC4yOTZaXCIvPjwvc3ZnPicsXHJcbiAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgbGluazogXCJodHRwczovL2dpdGVlLmNvbS9DcnljaGljVGVhbS9DcnljaGljRG9jXCIsXHJcbiAgICAgICAgICAgIH0sXHJcbiAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgIGljb246IHtcclxuICAgICAgICAgICAgICAgICAgICBzdmc6ICc8c3ZnIHdpZHRoPVwiMTI4XCIgaGVpZ2h0PVwiMTI4XCIgdmlld0JveD1cIjAgMCAyNCAyNFwiIHhtbG5zPVwiaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmdcIiBwcmVzZXJ2ZUFzcGVjdFJhdGlvPVwieE1pZFlNaWQgbWVldFwiPjxnPjxwYXRoIGlkPVwic3ZnXzFcIiBkPVwibTAuMjI4MTEsOC40MjI0NGwwLC0yLjY3NjI2YzAsLTAuMTMwMjIgMC4wMDQ4NSwtMC4yNTkyOCAwLjAxMzcyLC0wLjM4NzI1bC0wLjAxMzcyLDMuMDYzNTNsMCwwbDAsLTAuMDAwMDJ6bTIyLjEzNTcyLC02LjM1Nzg1YzAuODc1MzksMC45NzY2MyAxLjQwODA3LDIuMjY2ODIgMS40MDgwNywzLjY4MTU5bDAsMTIuNTA3NjVjMCwzLjA0NzU0IC0yLjQ3MDU0LDUuNTE4MDggLTUuNTE4MDgsNS41MTgwOGwtMTIuNTA3NjUsMGMtMS41MjA4OCwwIC0yLjg5Nzk4LC0wLjYxNTM2IC0zLjg5NjExLC0xLjYxMDU5bDIwLjUxMzc1LC0yMC4wOTY3M2wwLDBsMC4wMDAwMiwwelwiIGZpbGw9XCJyZ2IoODgsIDE4MiwgMjE2KVwiIGZpbGwtcnVsZT1cImV2ZW5vZGRcIiBzdHJva2U9XCJudWxsXCIvPjxwYXRoIGlkPVwic3ZnXzJcIiBkPVwibTEuODg3ODYsMjIuMTk4MjFjLTEuMDIzOTgsLTEuMDAxNzggLTEuNjU5NzUsLTIuMzk4NzQgLTEuNjU5NzUsLTMuOTQ0MzlsMCwtMTIuNTA3NjVjMCwtMy4wNDc1NCAyLjQ3MDU0LC01LjUxODA4IDUuNTE4MDgsLTUuNTE4MDhsMTIuNTA3NjUsMGMxLjY2MDY4LDAgMy4xNDk4NSwwLjczMzcgNC4xNjE0NywxLjg5NDQ3bC0yMC41Mjc0NCwyMC4wNzU2NWwtMC4wMDAwMSwwelwiIGZpbGw9XCJyZ2IoMTM0LCAxOTMsIDg1KVwiIGZpbGwtcnVsZT1cImV2ZW5vZGRcIiBzdHJva2U9XCJudWxsXCIvPjxwYXRoIGlkPVwic3ZnXzNcIiBkPVwibTE5LjY1NjksOS4zOTA0MWwtMi44ODYsMGMtMC45NDM1NCwwLjE5MzkzIC0wLjgxNDY2LDEuMDY1NjcgLTAuODE0NjYsMS4wNjU2N2wwLDMuMjQ1MjFjMC4xMDMzOSwwLjkzMDg4IDEuMDA4NTMsMC43OTMzNCAxLjAwODUzLDAuNzkzMzRsNC41NzY5NCwwbDAsMS45MDgzNGwtNS4wMTA4NiwwYy0xLjk1MjY1LC0wLjEwODQ5IC0yLjM2NzQ4LC0xLjQ0ODQ5IC0yLjM2NzQ4LC0xLjQ0ODQ5Yy0wLjE5Mzg5LC0wLjQzOTU4IC0wLjE2MDksLTAuODczNjkgLTAuMTYwOSwtMC44NzM2OWwwLC0zLjU2Mzc2YzAuMDEyOTIsLTIuNTIxMTYgMS43MjM5LC0yLjg3NCAxLjcyMzksLTIuODc0YzAuMjk3MjgsLTAuMTAzNDUgMS4yNDEyMywtMC4xMzc5NSAxLjI0MTIzLC0wLjEzNzk1bDQuNjIwMDksMGwtMS45MzA3NywxLjg4NTM1bDAsMGwtMC4wMDAwMiwtMC4wMDAwMnptLTguNDg0NiwwLjM2Nzg4bC0yLjI5OTE5LDYuNTc1N2wtMi4wOTIyNywwbC0yLjQzNzE0LC02LjU3NTdsLTAuMDIyOTksNi41NTI3MWwtMS45MDgzNCwwbDAsLTguODA1OTRsMy4xMDM5MSwwbDIuMjUzMjEsNi4wMjM5MWwyLjIzMDIyLC02LjAyMzkxbDMuMTcyOTEsMGwwLDguODUxOTNsLTIuMDAwMzEsMGwwLC02LjU5ODY5bDAsMGwtMC4wMDAwMSwtMC4wMDAwMXpcIiBmaWxsPVwicmdiKDI1NSwgMjU1LCAyNTUpXCIgZmlsbC1ydWxlPVwiZXZlbm9kZFwiIHN0cm9rZT1cIm51bGxcIi8+PC9zdmc+JyxcclxuICAgICAgICAgICAgICAgIH0sXHJcbiAgICAgICAgICAgICAgICBsaW5rOiBcImh0dHBzOi8vd3d3Lm1jbW9kLmNuL2F1dGhvci8zMjg2MC5odG1sXCIsXHJcbiAgICAgICAgICAgIH0sXHJcbiAgICAgICAgXSxcclxuICAgICAgICBsYW5nTWVudUxhYmVsOiBcIkNoYW5nZSBMYW5ndWFnZVwiLFxyXG4gICAgICAgIGxhc3RVcGRhdGVkOiB7fSxcclxuICAgICAgICAvLyBcdTZERkJcdTUyQTBcdTRFRTVcdTRFMEJcdTkxNERcdTdGNkVcdTY3NjVcdTU0MkZcdTc1MjhcdTU5MUFcdThCRURcdThBMDBcdTY1MkZcdTYzMDFcclxuICAgICAgICAvL0B0cy1pZ25vcmVcclxuICAgICAgICBsb2NhbGVzOiB7XHJcbiAgICAgICAgICAgIHJvb3Q6IHsgbGFiZWw6IFwiXHU3QjgwXHU0RjUzXHU0RTJEXHU2NTg3XCIsIGxhbmc6IFwiemgtQ05cIiB9LFxyXG4gICAgICAgICAgICBcImVuLVVTXCI6IHsgbGFiZWw6IFwiRW5nbGlzaFwiLCBsYW5nOiBcImVuLVVTXCIgfSxcclxuICAgICAgICB9LFxyXG4gICAgfSxcclxuICAgIG1hcmtkb3duOiB7IC4uLmNvbmZpZy5tYXJrZG93biB9LFxyXG4gICAgY2xlYW5VcmxzOiB0cnVlLFxyXG4gICAgbWVybWFpZDoge1xyXG4gICAgICAgIHN0YXJ0T25Mb2FkOiB0cnVlLFxyXG4gICAgICAgIHNlY3VyaXR5TGV2ZWw6IFwibG9vc2VcIixcclxuICAgICAgICB0aGVtZTogXCJkZWZhdWx0XCIsXHJcbiAgICB9LFxyXG4gICAgdml0ZToge1xyXG4gICAgICAgIG9wdGltaXplRGVwczoge1xyXG4gICAgICAgICAgICBleGNsdWRlOiBbXCJAbm9sZWJhc2UvKlwiXSxcclxuICAgICAgICB9LFxyXG4gICAgICAgIHNzcjoge1xyXG4gICAgICAgICAgICBub0V4dGVybmFsOiBbXCJ2dWV0aWZ5XCIsIFwiQG5vbGViYXNlLypcIl0sXHJcbiAgICAgICAgfSxcclxuICAgICAgICBwbHVnaW5zOiBbXHJcbiAgICAgICAgICAgIEdpdENoYW5nZWxvZyh7XHJcbiAgICAgICAgICAgICAgICByZXBvVVJMOiAoKSA9PiBcImh0dHBzOi8vZ2l0aHViLmNvbS9DcnljaGljVGVhbS9DcnljaGljRG9jXCIsXHJcbiAgICAgICAgICAgICAgICBtYXBBdXRob3JzOiBbXHJcbiAgICAgICAgICAgICAgICAgICAge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBuYW1lOiBcIk0xaG9ub1wiLCAvLyBUaGUgbmFtZSB5b3Ugd2FudCB0byBkaXNwbGF5XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHVzZXJuYW1lOiBcIk0xaG9ub1wiLCAvLyBUaGUgdXNlcm5hbWUgb2YgdGhlIGF1dGhvciB3aGljaCBpcyB1c2VkIHRvIHN1bW1vbiBnaXRodWIncyBsaW5rLiAod29uJ3Qgd29yayB3aXRoIGxpbmtzIG9wdGlvbnMpXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIG1hcEJ5TmFtZUFsaWFzZXM6IFtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwiQ3J5Y2hpY1RlYW1cIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwiTTFob25vXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBcIm0xaG9ub1wiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJHdWRhIGNoZW5cIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwiQ3VzdG9tZXIgc2VydmljZSBpcyBjdXJyZW50bHkgb2ZmbGluZS5cIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgXSwgLy8gQWRkIHRoZSBuYW1lIHlvdSB3YW50IHRvIG1hcCwgdGhlc2UgbmFtZXMgd2lsbCBiZSByZXBsYWNlZCB3aXRoIHRoZSBuYW1lLlxyXG4gICAgICAgICAgICAgICAgICAgICAgICBhdmF0YXI6IGdlbmVyYXRlQXZhdGFyVXJsKFwiTTFob25vXCIpLCAvLyBUaGUgYXZhdGFyIG9mIHRoZSBhdXRob3IsIG5vcm1hbGx5IGl0J3MgdGhlIGdpdGh1YiBhdmF0YXJcclxuICAgICAgICAgICAgICAgICAgICAgICAgLy8gbGlua3M6IFwiaHR0cHM6Ly9naXRlZS5jb20vQ3J5Y2hpY1RlYW0vQ3J5Y2hpY0RvY1wiIENoYW5nZSB0byB0aGUgdXJsIFlvdSB3YW50IHRvIGxpbmsgdG9cclxuICAgICAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgbmFtZTogXCJza3lyYWFoXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHVzZXJuYW1lOiBcInNreXJhYWhcIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgbWFwQnlOYW1lQWxpYXNlczogW1wiY3ljaWxpbmdcIiwgXCJza3lyYWFoXCJdLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBhdmF0YXI6IGdlbmVyYXRlQXZhdGFyVXJsKFwic2t5cmFhaFwiKSxcclxuICAgICAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgbmFtZTogXCJFaWtpZG9uYVwiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICB1c2VybmFtZTogXCJFaWtpZG9uYVwiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBtYXBCeU5hbWVBbGlhc2VzOiBbXCJOYWdhc2FraSBTb3lvXCIsIFwiRWlraWRvbmFcIl0sXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGF2YXRhcjogZ2VuZXJhdGVBdmF0YXJVcmwoXCJFaWtpZG9uYVwiKSxcclxuICAgICAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgXSxcclxuICAgICAgICAgICAgfSksXHJcbiAgICAgICAgICAgIEdpdENoYW5nZWxvZ01hcmtkb3duU2VjdGlvbigpLFxyXG4gICAgICAgICAgICBncm91cEljb25WaXRlUGx1Z2luKHtcclxuICAgICAgICAgICAgICAgIGN1c3RvbUljb246IHtcclxuICAgICAgICAgICAgICAgICAgICBtY21ldGE6IGxvY2FsSWNvbkxvYWRlcihcclxuICAgICAgICAgICAgICAgICAgICAgICAgaW1wb3J0Lm1ldGEudXJsLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBcIi4uLy4uL2RvY3MvcHVibGljL3N2Zy9taW5lY3JhZnQuc3ZnXCJcclxuICAgICAgICAgICAgICAgICAgICApLFxyXG4gICAgICAgICAgICAgICAgICAgIGpzb246IGxvY2FsSWNvbkxvYWRlcihcclxuICAgICAgICAgICAgICAgICAgICAgICAgaW1wb3J0Lm1ldGEudXJsLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBcIi4uLy4uL2RvY3MvcHVibGljL3N2Zy9qc29uLnN2Z1wiXHJcbiAgICAgICAgICAgICAgICAgICAgKSxcclxuICAgICAgICAgICAgICAgICAgICBtZDogbG9jYWxJY29uTG9hZGVyKFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBpbXBvcnQubWV0YS51cmwsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIFwiLi4vLi4vZG9jcy9wdWJsaWMvc3ZnL21hcmtkb3duLnN2Z1wiXHJcbiAgICAgICAgICAgICAgICAgICAgKSxcclxuICAgICAgICAgICAgICAgICAgICBrdWJlanM6IGxvY2FsSWNvbkxvYWRlcihcclxuICAgICAgICAgICAgICAgICAgICAgICAgaW1wb3J0Lm1ldGEudXJsLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBcIi4uLy4uL2RvY3MvcHVibGljL3N2Zy9rdWJlanMuc3ZnXCJcclxuICAgICAgICAgICAgICAgICAgICApLFxyXG4gICAgICAgICAgICAgICAgICAgIGpzOiBcImxvZ29zOmphdmFzY3JpcHRcIixcclxuICAgICAgICAgICAgICAgICAgICBzaDogbG9jYWxJY29uTG9hZGVyKFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBpbXBvcnQubWV0YS51cmwsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIFwiLi4vLi4vZG9jcy9wdWJsaWMvc3ZnL3Bvd2Vyc2hlbGwuc3ZnXCJcclxuICAgICAgICAgICAgICAgICAgICApLFxyXG4gICAgICAgICAgICAgICAgICAgIG5wbTogbG9jYWxJY29uTG9hZGVyKFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBpbXBvcnQubWV0YS51cmwsXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIFwiLi4vLi4vZG9jcy9wdWJsaWMvc3ZnL25wbS5zdmdcIlxyXG4gICAgICAgICAgICAgICAgICAgICksXHJcbiAgICAgICAgICAgICAgICAgICAgdHM6IFwibG9nb3M6dHlwZXNjcmlwdC1pY29uLXJvdW5kXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgamF2YTogXCJsb2dvczpqYXZhXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgY3NzOiBcImxvZ29zOmNzcy0zXCIsXHJcbiAgICAgICAgICAgICAgICAgICAgZ2l0OiBcImxvZ29zOmdpdC1pY29uXCIsXHJcbiAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICB9KSxcclxuICAgICAgICAgICAgQXV0b0ltcG9ydCh7XHJcbiAgICAgICAgICAgICAgICByZXNvbHZlcnM6IFtcclxuICAgICAgICAgICAgICAgICAgICBURGVzaWduUmVzb2x2ZXIoe1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBsaWJyYXJ5OiBcInZ1ZS1uZXh0XCIsXHJcbiAgICAgICAgICAgICAgICAgICAgfSksXHJcbiAgICAgICAgICAgICAgICBdLFxyXG4gICAgICAgICAgICB9KSxcclxuICAgICAgICAgICAgQ29tcG9uZW50cyh7XHJcbiAgICAgICAgICAgICAgICByZXNvbHZlcnM6IFtcclxuICAgICAgICAgICAgICAgICAgICBURGVzaWduUmVzb2x2ZXIoe1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBsaWJyYXJ5OiBcInZ1ZS1uZXh0XCIsXHJcbiAgICAgICAgICAgICAgICAgICAgfSksXHJcbiAgICAgICAgICAgICAgICBdLFxyXG4gICAgICAgICAgICB9KSxcclxuICAgICAgICBdLFxyXG4gICAgICAgIC8vIGRlZmluZToge1xyXG4gICAgICAgIC8vIF9fVlVFX1BST0RfSFlEUkFUSU9OX01JU01BVENIX0RFVEFJTFNfXzogdHJ1ZSxcclxuICAgICAgICAvLyB9LFxyXG4gICAgfSxcclxuICAgIGhlYWQ6IFtcclxuICAgICAgICBbXCJsaW5rXCIsIHsgcmVsOiBcImljb25cIiwgaHJlZjogXCJodHRwczovL2RvY3MubWlob25vLmNuL2Zhdmljb24uaWNvXCIgfV0sXHJcbiAgICBdLFxyXG4gICAgaWdub3JlRGVhZExpbmtzOiB0cnVlLFxyXG4gICAgdHJhbnNmb3JtSGVhZCh7IGFzc2V0cyB9KSB7XHJcbiAgICAgICAgY29uc3QgZm9udHMgPSAoKTogc3RyaW5nW10gPT4ge1xyXG4gICAgICAgICAgICByZXR1cm4gW1xyXG4gICAgICAgICAgICAgICAgYXNzZXRzLmZpbmQoKGZpbGUpID0+IC9KZXRCcmFpbnNNb25vLVJlZ3VsYXJcXC5cXHcrXFwud29mZjIvKSxcclxuICAgICAgICAgICAgICAgIGFzc2V0cy5maW5kKChmaWxlKSA9PiAvQ2hpbGxSb3VuZEdvdGhpY19FeHRyYUxpZ2h0XFwuXFx3K1xcLndvZmYyLyksXHJcbiAgICAgICAgICAgICAgICBhc3NldHMuZmluZCgoZmlsZSkgPT4gL0NoaWxsUm91bmRHb3RoaWNfTGlnaHRcXC5cXHcrXFwud29mZjIvKSxcclxuICAgICAgICAgICAgICAgIGFzc2V0cy5maW5kKChmaWxlKSA9PiAvQ2hpbGxSb3VuZEdvdGhpY19SZWd1bGFyXFwuXFx3K1xcLndvZmYyLyksXHJcbiAgICAgICAgICAgIF0uZmlsdGVyKCh2YWx1ZSk6IHZhbHVlIGlzIHN0cmluZyA9PiB2YWx1ZSAhPT0gdW5kZWZpbmVkKTtcclxuICAgICAgICB9O1xyXG4gICAgICAgIGNvbnN0IGZvbnRDb25maWcgPSAoKTogSGVhZENvbmZpZ1tdID0+IHtcclxuICAgICAgICAgICAgcmV0dXJuIGZvbnRzKCkubWFwKChmb250KSA9PiBbXHJcbiAgICAgICAgICAgICAgICBcImxpbmtcIixcclxuICAgICAgICAgICAgICAgIHtcclxuICAgICAgICAgICAgICAgICAgICBocmVmOiBmb250LFxyXG4gICAgICAgICAgICAgICAgICAgIGFzOiBcImZvbnRcIixcclxuICAgICAgICAgICAgICAgICAgICB0eXBlOiBcImZvbnQvd29mZjJcIixcclxuICAgICAgICAgICAgICAgICAgICBjcm9zc29yaWdpbjogXCJcIixcclxuICAgICAgICAgICAgICAgIH0sXHJcbiAgICAgICAgICAgIF0pO1xyXG4gICAgICAgIH07XHJcbiAgICAgICAgcmV0dXJuIGZvbnRDb25maWcoKTtcclxuICAgIH0sXHJcbn07XHJcbiIsICJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcRmVuZ01pbmdcXFxcRGVza3RvcFxcXFwxMTExXFxcXDc3XFxcXENyeWNoaWNEb2NcXFxcLnZpdGVwcmVzc1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcRmVuZ01pbmdcXFxcRGVza3RvcFxcXFwxMTExXFxcXDc3XFxcXENyeWNoaWNEb2NcXFxcLnZpdGVwcmVzc1xcXFx0d29zbGFzaENvbmZpZy50c1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vQzovVXNlcnMvRmVuZ01pbmcvRGVza3RvcC8xMTExLzc3L0NyeWNoaWNEb2MvLnZpdGVwcmVzcy90d29zbGFzaENvbmZpZy50c1wiO2ltcG9ydCB7IGN3ZCB9IGZyb20gXCJub2RlOnByb2Nlc3NcIjtcclxuaW1wb3J0IHsgam9pbiB9IGZyb20gXCJub2RlOnBhdGhcIjtcclxuaW1wb3J0IGZzIGZyb20gXCJmc1wiO1xyXG5cclxuY29uc3QgdHlwZUZpbGVzUGF0aCA9IGpvaW4oY3dkKCksIFwidHlwZWZpbGVzLzEuMjAuMS9wcm9iZS9nZW5lcmF0ZWQvaW50ZXJuYWxzXCIpO1xyXG5jb25zdCBpbnRlcm5hbFR5cGVGaWxlcyA9IGZzLmV4aXN0c1N5bmModHlwZUZpbGVzUGF0aClcclxuICAgID8gZnNcclxuICAgICAgICAgIC5yZWFkZGlyU3luYyh0eXBlRmlsZXNQYXRoKVxyXG4gICAgICAgICAgLmZpbHRlcihcclxuICAgICAgICAgICAgICAoZmlsZSkgPT4gZmlsZS5zdGFydHNXaXRoKFwiaW50ZXJuYWxfXCIpICYmIGZpbGUuZW5kc1dpdGgoXCIuZC50c1wiKVxyXG4gICAgICAgICAgKVxyXG4gICAgICAgICAgLm1hcCgoZmlsZSkgPT4gam9pbih0eXBlRmlsZXNQYXRoLCBmaWxlKSlcclxuICAgIDogW107XHJcblxyXG5leHBvcnQgY29uc3QgY29tcGlsZXJPcHRpb25zID0ge1xyXG4gICAgY2FjaGU6IHRydWUsXHJcbiAgICBjb21waWxlck9wdGlvbnM6IHtcclxuICAgICAgICBiYXNlVXJsOiBjd2QoKSxcclxuICAgICAgICB0YXJnZXQ6IDk5LFxyXG4gICAgICAgIG1vZHVsZTogOTksXHJcbiAgICAgICAgbW9kdWxlUmVzb2x1dGlvbjogMTAwLFxyXG4gICAgICAgIHBhdGhzOiB7XHJcbiAgICAgICAgICAgIFwiKlwiOiBbam9pbihjd2QoKSwgXCJ0eXBlZmlsZXMvMS4yMC4xL3Byb2JlL2dlbmVyYXRlZC8qXCIpXSxcclxuICAgICAgICB9LFxyXG4gICAgICAgIHJlc29sdmVKc29uTW9kdWxlOiB0cnVlLFxyXG4gICAgICAgIHR5cGVzOiBbXCJub2RlXCIsIC4uLmludGVybmFsVHlwZUZpbGVzXSwgLy8gXHU4MUVBXHU1MkE4XHU0RjdGXHU3NTI4XHU2NTg3XHU0RUY2XHVGRjBDXHU1OTgyXHU2NzlDXHU2Q0ExXHU2NzA5XHU1MjE5XHU0RkREXHU2MzAxXHU3M0IwXHU3MkI2XHJcbiAgICAgICAgZXNNb2R1bGVJbnRlcm9wOiB0cnVlLFxyXG4gICAgICAgIGlzb2xhdGVkTW9kdWxlczogdHJ1ZSxcclxuICAgICAgICB2ZXJiYXRpbU1vZHVsZVN5bnRheDogdHJ1ZSxcclxuICAgICAgICBza2lwTGliQ2hlY2s6IHRydWUsXHJcbiAgICAgICAgc2tpcERlZmF1bHRMaWJDaGVjazogdHJ1ZSxcclxuICAgIH0sXHJcbn07IiwgImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxGZW5nTWluZ1xcXFxEZXNrdG9wXFxcXDExMTFcXFxcNzdcXFxcQ3J5Y2hpY0RvY1xcXFwudml0ZXByZXNzXFxcXGNvbmZpZ1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcRmVuZ01pbmdcXFxcRGVza3RvcFxcXFwxMTExXFxcXDc3XFxcXENyeWNoaWNEb2NcXFxcLnZpdGVwcmVzc1xcXFxjb25maWdcXFxcbWFya2Rvd24tcGx1Z2lucy50c1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vQzovVXNlcnMvRmVuZ01pbmcvRGVza3RvcC8xMTExLzc3L0NyeWNoaWNEb2MvLnZpdGVwcmVzcy9jb25maWcvbWFya2Rvd24tcGx1Z2lucy50c1wiOy8vQHRzLW5vY2hlY2tcclxuaW1wb3J0IHsgYXJndiwgY3dkLCBlbnYgfSBmcm9tICdub2RlOnByb2Nlc3MnXHJcblxyXG5pbXBvcnQgeyBNYXJrZG93bk9wdGlvbnMgfSBmcm9tIFwidml0ZXByZXNzXCI7XHJcbmltcG9ydCB7IGNvbXBpbGVyT3B0aW9ucyB9IGZyb20gXCIuLi90d29zbGFzaENvbmZpZ1wiO1xyXG5cclxuaW1wb3J0IHRpbWVsaW5lIGZyb20gXCJ2aXRlcHJlc3MtbWFya2Rvd24tdGltZWxpbmVcIjtcclxuaW1wb3J0IHsgQmlEaXJlY3Rpb25hbExpbmtzIH0gZnJvbSBcIkBub2xlYmFzZS9tYXJrZG93bi1pdC1iaS1kaXJlY3Rpb25hbC1saW5rc1wiO1xyXG5pbXBvcnQgeyBJbmxpbmVMaW5rUHJldmlld0VsZW1lbnRUcmFuc2Zvcm0gfSBmcm9tIFwiQG5vbGViYXNlL3ZpdGVwcmVzcy1wbHVnaW4taW5saW5lLWxpbmstcHJldmlldy9tYXJrZG93bi1pdFwiO1xyXG5pbXBvcnQgeyB0YWJzTWFya2Rvd25QbHVnaW4gfSBmcm9tIFwidml0ZXByZXNzLXBsdWdpbi10YWJzXCI7XHJcbmltcG9ydCB7IHRyYW5zZm9ybWVyVHdvc2xhc2ggfSBmcm9tIFwiQHNoaWtpanMvdml0ZXByZXNzLXR3b3NsYXNoXCI7XHJcbmltcG9ydCBtZEZvb3Rub3RlIGZyb20gXCJtYXJrZG93bi1pdC1mb290bm90ZVwiO1xyXG5pbXBvcnQgbWRUYXNrTGlzdHMgZnJvbSBcIm1hcmtkb3duLWl0LXRhc2stbGlzdHNcIjtcclxuaW1wb3J0IG1kRGVmbGlzdCBmcm9tIFwibWFya2Rvd24taXQtZGVmbGlzdFwiO1xyXG5pbXBvcnQgbWRBYmJyIGZyb20gXCJtYXJrZG93bi1pdC1hYmJyXCI7XHJcbmltcG9ydCB7IGltZ1NpemUgfSBmcm9tIFwiQG1kaXQvcGx1Z2luLWltZy1zaXplXCI7XHJcbmltcG9ydCB7IGFsaWduIH0gZnJvbSBcIkBtZGl0L3BsdWdpbi1hbGlnblwiO1xyXG5pbXBvcnQgeyBzcG9pbGVyIH0gZnJvbSBcIkBtZGl0L3BsdWdpbi1zcG9pbGVyXCI7XHJcbmltcG9ydCB7IHN1YiB9IGZyb20gXCJAbWRpdC9wbHVnaW4tc3ViXCI7XHJcbmltcG9ydCB7IHN1cCB9IGZyb20gXCJAbWRpdC9wbHVnaW4tc3VwXCI7XHJcbmltcG9ydCB7IHJ1YnkgfSBmcm9tIFwiQG1kaXQvcGx1Z2luLXJ1YnlcIjtcclxuaW1wb3J0IHsgZGVtbyB9IGZyb20gXCJAbWRpdC9wbHVnaW4tZGVtb1wiO1xyXG5pbXBvcnQgeyBkbCB9IGZyb20gXCJAbWRpdC9wbHVnaW4tZGxcIjtcclxuaW1wb3J0IHsgc3RlcHBlciB9IGZyb20gXCIuLi9wbHVnaW5zL3N0ZXBwZXJcIjtcclxuaW1wb3J0IHsgdGFiIH0gZnJvbSBcIkBtZGl0L3BsdWdpbi10YWJcIjtcclxuaW1wb3J0IHsgbWFyayB9IGZyb20gXCJAbWRpdC9wbHVnaW4tbWFya1wiO1xyXG5pbXBvcnQgeyBpbnMgfSBmcm9tIFwiQG1kaXQvcGx1Z2luLWluc1wiO1xyXG5pbXBvcnQgeyB2X2FsZXJ0IH0gZnJvbSBcIi4uL3BsdWdpbnMvdi1hbGVydFwiO1xyXG5pbXBvcnQgeyBtZERlbW8gfSBmcm9tIFwiLi4vcGx1Z2lucy9kZW1vXCI7XHJcbmltcG9ydCB7IGNhcm91c2VscyB9IGZyb20gXCIuLi9wbHVnaW5zL2Nhcm91c2Vsc1wiO1xyXG5pbXBvcnQgeyBjYXJkIH0gZnJvbSBcIi4uL3BsdWdpbnMvY2FyZFwiO1xyXG5pbXBvcnQgeyBncm91cEljb25NZFBsdWdpbiB9IGZyb20gXCJ2aXRlcHJlc3MtcGx1Z2luLWdyb3VwLWljb25zXCI7XHJcbmltcG9ydCB0cyBmcm9tICd0eXBlc2NyaXB0JztcclxuXHJcbmltcG9ydCBmcyBmcm9tIFwiZnNcIjtcclxuaW1wb3J0IHBhdGggZnJvbSBcInBhdGhcIjtcclxuXHJcbmZ1bmN0aW9uIG5vVHdvc2xhc2goKSB7XHJcbiAgLy8gXHU1OUNCXHU3RUM4XHU4RkQ0XHU1NkRFIGZhbHNlXHVGRjBDXHU0RUU1XHU3ODZFXHU0RkREIFR3b3NsYXNoIFx1OEY2Q1x1NjM2Mlx1NjAzQlx1NjYyRlx1NTQyRlx1NzUyOFxyXG4gIHJldHVybiBmYWxzZTtcclxufVxyXG5cclxuZXhwb3J0IGNvbnN0IG1hcmtkb3duOiBNYXJrZG93bk9wdGlvbnMgPSB7XHJcbiAgICBtYXRoOiB0cnVlLFxyXG4gICAgY29uZmlnOiBhc3luYyAobWQpID0+IHtcclxuICAgICAgICBtZC51c2UoSW5saW5lTGlua1ByZXZpZXdFbGVtZW50VHJhbnNmb3JtKTtcclxuICAgICAgICBtZC51c2UoQmlEaXJlY3Rpb25hbExpbmtzKCkpO1xyXG4gICAgICAgIG1kLnVzZShncm91cEljb25NZFBsdWdpbik7XHJcbiAgICAgICAgbWQudXNlKHRpbWVsaW5lKTtcclxuICAgICAgICBtZC51c2UodGFic01hcmtkb3duUGx1Z2luKTtcclxuXHJcbiAgICAgICAgbWQudXNlKG1kRm9vdG5vdGUpO1xyXG4gICAgICAgIG1kLnVzZShtZFRhc2tMaXN0cyk7XHJcbiAgICAgICAgbWQudXNlKG1kRGVmbGlzdCk7XHJcbiAgICAgICAgbWQudXNlKG1kQWJicik7XHJcbiAgICAgICAgbWQudXNlKGltZ1NpemUpO1xyXG4gICAgICAgIG1kLnVzZShhbGlnbik7XHJcbiAgICAgICAgbWQudXNlKHNwb2lsZXIpO1xyXG4gICAgICAgIG1kLnVzZShzdWIpO1xyXG4gICAgICAgIG1kLnVzZShzdXApO1xyXG4gICAgICAgIG1kLnVzZShydWJ5KTtcclxuICAgICAgICBtZC51c2UoZGVtbywgbWREZW1vKTtcclxuICAgICAgICBtZC51c2UoZGwpO1xyXG4gICAgICAgIG1kLnVzZSh2X2FsZXJ0KTtcclxuICAgICAgICBtZC51c2UobWFyayk7XHJcbiAgICAgICAgbWQudXNlKGlucyk7XHJcbiAgICAgICAgLy9tZC51c2UoY29udGFpbmVyLCBzdGVwcGVyKTtcclxuICAgICAgICAvL21kLnVzZShjb250YWluZXIsIHRlbXBsYXRlKTtcclxuICAgICAgICBtZC51c2UodGFiLCBzdGVwcGVyKTtcclxuICAgICAgICBtZC51c2UoY2Fyb3VzZWxzKTtcclxuICAgICAgICBtZC51c2UoY2FyZCk7XHJcbiAgICAgICAgLy8gY29uc3QgdGVzdCA9IG1kLnJlbmRlcihcIjo6OiBjYXJvdXNlbHMje1xcXCJ0ZXN0XFxcIjogMTIzfVxcbjEyMzU0NlxcbkB0YWIgaHR0cHM6Ly9kb2NzLm1paG9uby5jbi9tb2RzL2FkdmVudHVyZS9jaGFtcGlvbnMtdW5vZmZpY2lhbC8xLnBuZ1xcblxcbkB0YWIgaHR0cHM6Ly9kb2NzLm1paG9uby5jbi9tb2RzL2FkdmVudHVyZS9jaGFtcGlvbnMtdW5vZmZpY2lhbC8yLnBuZ1xcblxcbjo6OlxcblwiLCB7fSlcclxuICAgICAgICAvLyBjb25zdCB0ZXN0ID0gbWQucmVuZGVyKGZzLnJlYWRGaWxlU3luYyhwYXRoLmpvaW4oXCJkb2NzXCIsXCJ6aFwiLFwibW9kcGFja1wiLFwia3ViZWpzXCIsXCJLdWJlSlNDb3Vyc2VcIixcIkt1YmVKU0Jhc2ljXCIsXCJGaWxlU3RydWN0dXJlLm1kXCIpKS50b1N0cmluZygpKVxyXG4gICAgICAgIC8vIGZzLndyaXRlRmlsZVN5bmMoJ291dHB1dC5odG1sJywgdGVzdCk7XHJcbiAgICAgICAgbWQucmVuZGVyZXIucnVsZXMuaGVhZGluZ19jbG9zZSA9ICh0b2tlbnMsIGlkeCwgb3B0aW9ucywgZW52LCBzbGYpID0+IHtcclxuICAgICAgICAgICAgbGV0IGh0bWxSZXN1bHQgPSBzbGYucmVuZGVyVG9rZW4odG9rZW5zLCBpZHgsIG9wdGlvbnMpO1xyXG4gICAgICAgICAgICBpZiAodG9rZW5zW2lkeF0udGFnID09PSBcImgxXCIpIGh0bWxSZXN1bHQgKz0gYDxBcnRpY2xlTWV0YWRhdGEgLz5gO1xyXG4gICAgICAgICAgICByZXR1cm4gaHRtbFJlc3VsdDtcclxuICAgICAgICB9O1xyXG4gICAgfSxcclxuXHJcbiAgICBjb2RlVHJhbnNmb3JtZXJzOiBbXHJcbiAgICAgIHRyYW5zZm9ybWVyVHdvc2xhc2goe1xyXG4gICAgICAgIHR3b3NsYXNoT3B0aW9uczogY29tcGlsZXJPcHRpb25zXHJcbiAgICAgIH0pXHJcbiAgICBdLFxyXG4gICAgaW1hZ2U6IHtcclxuICAgICAgICBsYXp5TG9hZGluZzogdHJ1ZSxcclxuICAgIH0sXHJcbn07XHJcbiIsICJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcRmVuZ01pbmdcXFxcRGVza3RvcFxcXFwxMTExXFxcXDc3XFxcXENyeWNoaWNEb2NcXFxcLnZpdGVwcmVzc1xcXFxwbHVnaW5zXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxGZW5nTWluZ1xcXFxEZXNrdG9wXFxcXDExMTFcXFxcNzdcXFxcQ3J5Y2hpY0RvY1xcXFwudml0ZXByZXNzXFxcXHBsdWdpbnNcXFxcc3RlcHBlci50c1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vQzovVXNlcnMvRmVuZ01pbmcvRGVza3RvcC8xMTExLzc3L0NyeWNoaWNEb2MvLnZpdGVwcmVzcy9wbHVnaW5zL3N0ZXBwZXIudHNcIjtpbXBvcnQgdHlwZSB7IE1hcmtkb3duSXRUYWJPcHRpb25zIH0gZnJvbSBcIkBtZGl0L3BsdWdpbi10YWJcIjtcclxuXHJcbmV4cG9ydCBjb25zdCBzdGVwcGVyOiBNYXJrZG93bkl0VGFiT3B0aW9ucyA9IHtcclxuICBuYW1lOiBcInN0ZXBwZXJcIixcclxuICB0YWJzT3BlblJlbmRlcmVyKGluZm8pIHtcclxuICAgIGNvbnN0IHsgZGF0YSB9ID0gaW5mbztcclxuICAgIGNvbnN0IGl0ZW1zID0gZGF0YS5tYXAodGFiID0+IGAnJHt0YWIudGl0bGV9J2ApO1xyXG4gICAgcmV0dXJuIGBcXG48di1zdGVwcGVyIDppdGVtcz1cIlske2l0ZW1zfV1cIiBjbGFzcz1cInRoZW1lLXN0ZXBwZXJcIj5gO1xyXG4gIH0sXHJcbiAgdGFic0Nsb3NlUmVuZGVyZXIoKSB7XHJcbiAgICByZXR1cm4gYFxcbjwvdi1zdGVwcGVyPlxcbmA7XHJcbiAgfSxcclxuICB0YWJPcGVuUmVuZGVyZXIoZGF0YSkge1xyXG4gICAgcmV0dXJuIGBcXG48dGVtcGxhdGUgdi1zbG90Oml0ZW0uJHtkYXRhLmluZGV4ICsgMX0+XFxuYDtcclxuICB9LFxyXG4gIHRhYkNsb3NlUmVuZGVyZXIoKSB7XHJcbiAgICByZXR1cm4gYDwvdGVtcGxhdGU+IGA7XHJcbiAgfSxcclxufTsiLCAiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIkM6XFxcXFVzZXJzXFxcXEZlbmdNaW5nXFxcXERlc2t0b3BcXFxcMTExMVxcXFw3N1xcXFxDcnljaGljRG9jXFxcXC52aXRlcHJlc3NcXFxccGx1Z2luc1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcRmVuZ01pbmdcXFxcRGVza3RvcFxcXFwxMTExXFxcXDc3XFxcXENyeWNoaWNEb2NcXFxcLnZpdGVwcmVzc1xcXFxwbHVnaW5zXFxcXHYtYWxlcnQudHNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0M6L1VzZXJzL0ZlbmdNaW5nL0Rlc2t0b3AvMTExMS83Ny9DcnljaGljRG9jLy52aXRlcHJlc3MvcGx1Z2lucy92LWFsZXJ0LnRzXCI7aW1wb3J0IHsgY29udGFpbmVyIH0gZnJvbSBcIkBtZGl0L3BsdWdpbi1jb250YWluZXJcIjtcclxuaW1wb3J0IHsgbG9nZ2VyIH0gZnJvbSAnLi4vY29uZmlnL3NpZGViYXJDb250cm9sJztcclxuaW1wb3J0IHR5cGUgeyBQbHVnaW5TaW1wbGUgfSBmcm9tIFwibWFya2Rvd24taXRcIjtcclxuXHJcbmV4cG9ydCBjb25zdCB2X2FsZXJ0OiBQbHVnaW5TaW1wbGUgPSAobWQpID0+IHtcclxuICAgIGNvbnN0IHR5cGUgPSBbXCJ2LXN1Y2Nlc3NcIiwgXCJ2LWluZm9cIiwgXCJ2LXdhcm5pbmdcIiwgXCJ2LWVycm9yXCJdXHJcbiAgICB0eXBlLmZvckVhY2goKG5hbWUpID0+XHJcbiAgICAgICAgbWQudXNlKChtZCkgPT5cclxuICAgICAgICAgICAgY29udGFpbmVyKG1kLCB7XHJcbiAgICAgICAgICAgICAgICBuYW1lLFxyXG4gICAgICAgICAgICAgICAgb3BlblJlbmRlcjogKHRva2VucywgaW5kZXgsIF9vcHRpb25zKSA9PiB7XHJcbiAgICAgICAgICAgICAgICAgICAgY29uc3QgaW5mbzogc3RyaW5nID0gdG9rZW5zW2luZGV4XS5pbmZvLnRyaW0oKS5zbGljZShuYW1lLmxlbmd0aCkudHJpbSgpO1xyXG4gICAgICAgICAgICAgICAgICAgIGNvbnN0IGRlZmF1bHRUaXRsZTogc3RyaW5nID0gbmFtZS5yZXBsYWNlKFwidi1cIiwgXCJcIilcclxuICAgICAgICAgICAgICAgICAgICByZXR1cm4gYDxwPjx2LWFsZXJ0IGNsYXNzPVwidi1hbGVydFwiIHRpdGxlPVwiJHtpbmZvIHx8IGRlZmF1bHRUaXRsZX1cIiB0eXBlPVwiJHtkZWZhdWx0VGl0bGV9XCIgPlxcbmA7XHJcbiAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgY2xvc2VSZW5kZXI6ICgpOiBzdHJpbmcgPT4ge1xyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiBgPC92LWFsZXJ0PjwvcD5cXG5gXHJcbiAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICB9KSxcclxuICAgICAgICApXHJcbiAgICAgICAgXHJcbiAgICApO1xyXG59OyIsICJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcRmVuZ01pbmdcXFxcRGVza3RvcFxcXFwxMTExXFxcXDc3XFxcXENyeWNoaWNEb2NcXFxcLnZpdGVwcmVzc1xcXFxwbHVnaW5zXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxGZW5nTWluZ1xcXFxEZXNrdG9wXFxcXDExMTFcXFxcNzdcXFxcQ3J5Y2hpY0RvY1xcXFwudml0ZXByZXNzXFxcXHBsdWdpbnNcXFxcZGVtby50c1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vQzovVXNlcnMvRmVuZ01pbmcvRGVza3RvcC8xMTExLzc3L0NyeWNoaWNEb2MvLnZpdGVwcmVzcy9wbHVnaW5zL2RlbW8udHNcIjtpbXBvcnQge2RlbW8sIE1hcmtkb3duSXREZW1vT3B0aW9uc30gZnJvbSAnQG1kaXQvcGx1Z2luLWRlbW8nO1xyXG5cclxuZXhwb3J0IGNvbnN0IG1kRGVtbzogTWFya2Rvd25JdERlbW9PcHRpb25zID0ge1xyXG4gICAgYmVmb3JlQ29udGVudDogZmFsc2UsXHJcbiAgICBvcGVuUmVuZGVyOiAodG9rZW5zLCBpbmRleCkgPT4ge1xyXG4gICAgICAgIGNvbnN0IGluZm86IHN0cmluZyA9IHRva2Vuc1tpbmRleF0uaW5mby50cmltKCk7XHJcbiAgICAgICAgcmV0dXJuIGA8ZGl2IGNsYXNzPVwiZGVtb1wiPlxcblxcdDxkZXRhaWxzIGNsYXNzPVwiY3VzdG9tLWJsb2NrXCI+XFxuXFx0PHN1bW1hcnk+XFxuXFx0JHtpbmZvfHwgXCJEZW1vXCJ9XFxuXFx0PGhyLz5gXHJcbiAgICB9LFxyXG4gICAgY29udGVudENsb3NlUmVuZGVyOiAodG9rZW5zLCBpZHgsIG9wdGlvbnMsIGVudiwgc2xmKSA9PiB7XHJcbiAgICAgICAgbGV0IGh0bWxSZXN1bHQgPSBzbGYucmVuZGVyVG9rZW4odG9rZW5zLCBpZHgsIG9wdGlvbnMpO1xyXG4gICAgICAgIHJldHVybiBgJHtodG1sUmVzdWx0fTwvc3VtbWFyeT5cXG5cXHQ8aHIvPlxcblxcdGBcclxuICAgIH0sXHJcbiAgICBjbG9zZVJlbmRlcjogKCkgPT4ge1xyXG4gICAgICAgIHJldHVybiBgPC9kZXRhaWxzPlxcbjwvZGl2PmBcclxuICAgIH1cclxufSIsICJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcRmVuZ01pbmdcXFxcRGVza3RvcFxcXFwxMTExXFxcXDc3XFxcXENyeWNoaWNEb2NcXFxcLnZpdGVwcmVzc1xcXFxwbHVnaW5zXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxGZW5nTWluZ1xcXFxEZXNrdG9wXFxcXDExMTFcXFxcNzdcXFxcQ3J5Y2hpY0RvY1xcXFwudml0ZXByZXNzXFxcXHBsdWdpbnNcXFxcY2Fyb3VzZWxzLnRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9DOi9Vc2Vycy9GZW5nTWluZy9EZXNrdG9wLzExMTEvNzcvQ3J5Y2hpY0RvYy8udml0ZXByZXNzL3BsdWdpbnMvY2Fyb3VzZWxzLnRzXCI7aW1wb3J0IHsgdGFiLCBNYXJrZG93bkl0VGFiT3B0aW9ucyB9IGZyb20gXCJAbWRpdC9wbHVnaW4tdGFiXCI7XHJcbmltcG9ydCB7IGxvZ2dlciB9IGZyb20gJy4uL2NvbmZpZy9zaWRlYmFyQ29udHJvbCc7XHJcbmltcG9ydCB0eXBlIHsgUGx1Z2luU2ltcGxlIH0gZnJvbSBcIm1hcmtkb3duLWl0XCI7XHJcblxyXG5leHBvcnQgY29uc3QgY2Fyb3VzZWxzOiBQbHVnaW5TaW1wbGUgPSAobWQpID0+IHtcclxuICAgIG1kLnVzZSh0YWIsIHtcclxuICAgICAgICBuYW1lOiBcImNhcm91c2Vsc1wiLFxyXG4gICAgICAgIHRhYnNPcGVuUmVuZGVyZXIoaW5mbywgdG9rZW5zLCBpbmRleCwgb3B0LCBlbnYpIHtcclxuICAgICAgICAgICAgY29uc3QgY29udGVudCA9IEpTT04ucGFyc2UoSlNPTi5zdHJpbmdpZnkoZW52KSlcclxuICAgICAgICAgICAgY29uc3QgSUNvbnRlbnQgPSBjb250ZW50LmNvbnRlbnRcclxuICAgICAgICAgICAgbGV0IHRva2VuOiBzdHJpbmcgPSBcIlwiXHJcbiAgICAgICAgICAgIGxldCBjb25maWc6IHN0cmluZyA9IFwiXCJcclxuICAgICAgICAgICAgaWYgKElDb250ZW50ICYmIHR5cGVvZiBJQ29udGVudCA9PT0gXCJzdHJpbmdcIikge1xyXG4gICAgICAgICAgICAgICAgY29uc3QgbWF0Y2hlcyA9IElDb250ZW50Lm1hdGNoKC9jYXJvdXNlbHMjXFx7W15cXH1dKlxcfS9nKVxyXG4gICAgICAgICAgICAgICAgaWYgKG1hdGNoZXMpIHtcclxuICAgICAgICAgICAgICAgICAgICBtYXRjaGVzLmZvckVhY2gobWF0Y2ggPT4ge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICB0b2tlbiArPSBtYXRjaC5yZXBsYWNlKFwiY2Fyb3VzZWxzI1wiLCBcIlwiKVxyXG4gICAgICAgICAgICAgICAgICAgIH0pXHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgLy8gbG9nZ2VyKEpTT04uc3RyaW5naWZ5KHRva2Vuc1tpbmRleF0pLCBcImpzb25DaGVjay5qc29uXCIpXHJcbiAgICAgICAgICAgIHRyeSB7XHJcbiAgICAgICAgICAgICAgICBjb25zdCBjb25maWdPYmogPSBKU09OLnBhcnNlKHRva2VuKVxyXG4gICAgXHJcbiAgICAgICAgICAgICAgICBpZiAoY29uZmlnT2JqLmFycm93cykge1xyXG4gICAgICAgICAgICAgICAgICAgIGlmICh0eXBlb2YgY29uZmlnT2JqLmFycm93cyA9PT0gXCJib29sZWFuXCIpIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgY29uZmlnICs9IGAgOnNob3ctYXJyb3dzPVwiJHtjb25maWdPYmouYXJyb3dzfVwiYFxyXG4gICAgICAgICAgICAgICAgICAgIH0gZWxzZSBpZiAoY29uZmlnT2JqLmFycm93cyA9PT0gXCJob3ZlclwiKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGNvbmZpZyArPSBgIDpzaG93LWFycm93cz1cImhvdmVyXCJgXHJcbiAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgXHJcbiAgICAgICAgICAgICAgICBpZiAoY29uZmlnT2JqLnVuZGVsaW1pdGVycyAmJiBjb25maWdPYmoudW5kZWxpbWl0ZXJzID09PSB0cnVlKSBjb25maWcgKz0gYCA6aGlkZS1kZWxpbWl0ZXJzPVwidHJ1ZVwiYFxyXG4gICAgXHJcbiAgICAgICAgICAgICAgICBpZiAoY29uZmlnT2JqLmN5Y2xlICYmIGNvbmZpZ09iai5jeWNsZSA9PT0gdHJ1ZSkge1xyXG4gICAgICAgICAgICAgICAgICAgIGNvbmZpZyArPSBgIDpjeWNsZT1cInRydWVcImBcclxuICAgIFxyXG4gICAgICAgICAgICAgICAgICAgIGlmIChjb25maWdPYmouaW50ZXJ2YWwgJiYgdHlwZW9mIGNvbmZpZ09iai5pbnRlcnZhbCA9PT0gXCJudW1iZXJcIikge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBjb25maWcgKz0gYCA6aW50ZXJ2YWw9XCIke2NvbmZpZ09iai5pbnRlcnZhbH1cImBcclxuICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICBcclxuICAgICAgICAgICAgICAgIGlmIChjb25maWdPYmoucmF0aW8gJiYgdHlwZW9mIGNvbmZpZ09iai5yYXRpbyA9PT0gXCJudW1iZXJcIikgY29uZmlnICs9IGBhc3BlY3RSYXRpbz1cIiR7Y29uZmlnT2JqLnJhdGlvfVwiIGBcclxuICAgICAgICAgICAgfSBjYXRjaCAoZXJyb3IpIHsgfVxyXG4gICAgICAgICAgICByZXR1cm4gYDxNZENhcm91c2VsJHtjb25maWd9ID5gO1xyXG4gICAgICAgIH0sXHJcbiAgICAgICAgdGFic0Nsb3NlUmVuZGVyZXIoKSB7XHJcbiAgICAgICAgICAgIHJldHVybiBgPC9NZENhcm91c2VsPmA7XHJcbiAgICAgICAgfSxcclxuICAgICAgICB0YWJPcGVuUmVuZGVyZXIoZGF0YSkge1xyXG4gICAgICAgICAgICBcclxuICAgICAgICAgICAgcmV0dXJuIGBcXG48di1jYXJvdXNlbC1pdGVtIGNvdmVyIHNyYz1cIiR7ZGF0YS50aXRsZX1cIj5cXG5gO1xyXG4gICAgICAgIH0sXHJcbiAgICAgICAgdGFiQ2xvc2VSZW5kZXJlcigpIHtcclxuICAgICAgICAgICAgcmV0dXJuIGA8L3YtY2Fyb3VzZWwtaXRlbT5gO1xyXG4gICAgICAgIH0sXHJcbiAgICB9KVxyXG59OyIsICJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcRmVuZ01pbmdcXFxcRGVza3RvcFxcXFwxMTExXFxcXDc3XFxcXENyeWNoaWNEb2NcXFxcLnZpdGVwcmVzc1xcXFxwbHVnaW5zXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxGZW5nTWluZ1xcXFxEZXNrdG9wXFxcXDExMTFcXFxcNzdcXFxcQ3J5Y2hpY0RvY1xcXFwudml0ZXByZXNzXFxcXHBsdWdpbnNcXFxcY2FyZC50c1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vQzovVXNlcnMvRmVuZ01pbmcvRGVza3RvcC8xMTExLzc3L0NyeWNoaWNEb2MvLnZpdGVwcmVzcy9wbHVnaW5zL2NhcmQudHNcIjtpbXBvcnQgeyBjb250YWluZXIgfSBmcm9tIFwiQG1kaXQvcGx1Z2luLWNvbnRhaW5lclwiO1xyXG5pbXBvcnQgdHlwZSB7IFBsdWdpblNpbXBsZSB9IGZyb20gXCJtYXJrZG93bi1pdFwiO1xyXG5cclxuZXhwb3J0IGNvbnN0IGNhcmQ6IFBsdWdpblNpbXBsZSA9IChtZCkgPT4ge1xyXG4gICAgY29uc3QgdHlwZSA9IFtcInRleHRcIiwgXCJmbGF0XCIsIFwiZWxldmF0ZWRcIiwgXCJ0b25hbFwiLCBcIm91dGxpbmVkXCIsIFwicGxhaW5cIl1cclxuICAgIGxldCBpbnNpZGVDb250YWluZXIgPSBmYWxzZTtcclxuICAgIHR5cGUuZm9yRWFjaChuYW1lID0+IHtcclxuICAgICAgICAgICAgbWQudXNlKChtZCkgPT5cclxuICAgICAgICAgICAgICAgIGNvbnRhaW5lcihtZCwge1xyXG4gICAgICAgICAgICAgICAgICAgIG5hbWUsXHJcbiAgICAgICAgICAgICAgICAgICAgb3BlblJlbmRlcjogKHRva2VucywgaW5kZXgsIF9vcHRpb25zKSA9PiB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBjb25zdCBpbmZvOiBzdHJpbmcgPSB0b2tlbnNbaW5kZXhdLmluZm8udHJpbSgpLnNsaWNlKG5hbWUubGVuZ3RoKS50cmltKCk7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBjb25zdCB0aXRsZXMgPSBpbmZvLnNwbGl0KFwiI1wiKVxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgbGV0IHRpdGxlID0gXCJcIlxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgbGV0IHN1YlRpdGlsZSA9IFwiXCJcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIHN3aXRjaCAodGl0bGVzLmxlbmd0aCkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGNhc2UgMDpcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgYnJlYWs7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgY2FzZSAxOiBcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgaWYgKHRpdGxlc1swXSAhPT0gXCJcIikge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgdGl0bGUgKz0gYDx0ZW1wbGF0ZSB2LXNsb3Q6dGl0bGU+JHt0aXRsZXNbMF19PC90ZW1wbGF0ZT5gXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgYnJlYWs7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgY2FzZSAyOlxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBpZiAodGl0bGVzWzBdICE9PSBcIlwiKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB0aXRsZSArPSBgPHRlbXBsYXRlIHYtc2xvdDp0aXRsZT4ke3RpdGxlc1swXX08L3RlbXBsYXRlPmBcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBpZiAodGl0bGVzWzFdICE9PSBcIlwiKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBzdWJUaXRpbGUgKz0gYDx0ZW1wbGF0ZSB2LXNsb3Q6c3VidGl0bGU+JHt0aXRsZXNbMV19PC90ZW1wbGF0ZT5gXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgYnJlYWs7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgZGVmYXVsdDpcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgYnJlYWs7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBpbnNpZGVDb250YWluZXIgPSB0cnVlO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgcmV0dXJuIGA8cD48di1jYXJkIHZhcmlhbnQ9XCIke25hbWV9XCIgPiR7dGl0bGV9JHtzdWJUaXRpbGV9PHRlbXBsYXRlIHYtc2xvdDp0ZXh0PlxcbmA7XHJcbiAgICAgICAgICAgICAgICAgICAgfSxcclxuICAgICAgICAgICAgICAgICAgICBjbG9zZVJlbmRlcjogKCk6IHN0cmluZyA9PiB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGluc2lkZUNvbnRhaW5lciA9IGZhbHNlO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICByZXR1cm4gYDwvdGVtcGxhdGU+PC92LWNhcmQ+PC9wPlxcbmBcclxuICAgICAgICAgICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICAgICAgfSksXHJcbiAgICAgICAgICAgIClcclxuICAgICAgICB9XHJcbiAgICApO1xyXG4gICAgbWQucmVuZGVyZXIucnVsZXMucGFyYWdyYXBoX29wZW4gPSAodG9rZW5zLCBpZHgsIG9wdGlvbnMsIGVudiwgc2VsZikgPT4ge1xyXG4gICAgICAgIGlmIChpbnNpZGVDb250YWluZXIgJiYgdG9rZW5zW2lkeF0udGFnID09PSBcInBcIikgcmV0dXJuICcnO1xyXG4gICAgICAgIHJldHVybiBzZWxmLnJlbmRlclRva2VuKHRva2VucywgaWR4LCBvcHRpb25zKTtcclxuICAgIH07XHJcblxyXG4gICAgbWQucmVuZGVyZXIucnVsZXMucGFyYWdyYXBoX2Nsb3NlID0gKHRva2VucywgaWR4LCBvcHRpb25zLCBlbnYsIHNlbGYpID0+IHtcclxuICAgICAgICBpZiAoaW5zaWRlQ29udGFpbmVyICYmIHRva2Vuc1tpZHhdLnRhZyA9PT0gXCJwXCIpIHJldHVybiAnJztcclxuICAgICAgICByZXR1cm4gc2VsZi5yZW5kZXJUb2tlbih0b2tlbnMsIGlkeCwgb3B0aW9ucyk7XHJcbiAgICB9O1xyXG59OyJdLAogICJtYXBwaW5ncyI6ICI7QUFBK1YsU0FBUyxvQkFBb0I7QUFDNVgsU0FBUyxtQkFBbUI7OztBQ0R5VyxPQUFPLFVBQVU7QUFDdFosT0FBTyxRQUFRO0FBQ2YsT0FBTyxZQUFZO0FBRW5CLElBQXFCLG1CQUFyQixNQUFxQixrQkFBaUI7QUFBQSxFQUMxQjtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUVSLE9BQWUsV0FBbUIsS0FBSyxRQUFRO0FBQUEsRUFDL0MsT0FBd0IsYUFBdUI7QUFBQSxJQUMzQztBQUFBLElBQ0E7QUFBQSxJQUNBO0FBQUEsRUFDSjtBQUFBLEVBRUEsWUFBWSxVQUFrQjtBQUMxQixTQUFLLFdBQVc7QUFBQSxNQUNaLE1BQU07QUFBQSxNQUNOLE9BQU8sQ0FBQztBQUFBLElBQ1o7QUFDQSxTQUFLLFdBQVc7QUFDaEIsU0FBSyxVQUFVLEtBQUssS0FBSyxrQkFBaUIsVUFBVSxRQUFRO0FBQzVELFNBQUssUUFBUSxLQUFLO0FBQUEsTUFDZCxHQUFHLFlBQVksS0FBSyxPQUFPO0FBQUEsTUFDM0Isa0JBQWlCO0FBQUEsSUFDckI7QUFDQSxTQUFLLHFCQUFxQixLQUFLLFNBQVMsUUFBUSxXQUFXLEdBQUc7QUFFOUQsU0FBSyxRQUFRO0FBQUEsRUFDakI7QUFBQSxFQUVBLElBQVcsVUFBbUI7QUFDMUIsV0FBTyxLQUFLO0FBQUEsRUFDaEI7QUFBQSxFQUVBLElBQVcsb0JBQTRCO0FBQ25DLFdBQU8sS0FBSztBQUFBLEVBQ2hCO0FBQUEsRUFFUSxV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LFNBQVMseUNBQXlDO0FBQUEsUUFDL0QsT0FBTyxLQUFLLENBQUMsU0FBUyxvQ0FBb0M7QUFBQSxRQUMxRCxPQUFPLEtBQUssQ0FBQyxTQUFTLHNDQUFzQztBQUFBLE1BQ2hFLEVBQUUsT0FBTyxDQUFDLFVBQTJCLFVBQVUsTUFBUztBQUFBLElBQzVEO0FBQ0EsVUFBTSxhQUFhLE1BQW9CO0FBQ25DLGFBQU8sTUFBTSxFQUFFLElBQUksQ0FBQyxTQUFTO0FBQUEsUUFDekI7QUFBQSxRQUNBO0FBQUEsVUFDSSxNQUFNO0FBQUEsVUFDTixJQUFJO0FBQUEsVUFDSixNQUFNO0FBQUEsVUFDTixhQUFhO0FBQUEsUUFDakI7QUFBQSxNQUNKLENBQUM7QUFBQSxJQUNMO0FBQ0EsV0FBTyxXQUFXO0FBQUEsRUFDdEI7QUFDSjs7O0FQdExBLElBQU8saUJBQVE7QUFBQSxFQUNYLGFBQWE7QUFBQSxJQUNULEdBQUc7QUFBQSxJQUNILFNBQVM7QUFBQSxNQUNMLE1BQU0sRUFBRSxPQUFPLDRCQUFRLEdBQUcsTUFBTTtBQUFBLE1BQ2hDLElBQUksRUFBRSxPQUFPLFdBQVcsR0FBRyxNQUFNO0FBQUEsSUFDckM7QUFBQSxFQUVKLENBQUM7QUFDTDsiLAogICJuYW1lcyI6IFsiZnMiLCAicGF0aCIsICJmcyIsICJpdGVtIiwgImdpdGJvb2tQYXJzZXIiLCAiZ2l0Ym9va1BhcnNlciIsICJwYXRoIiwgImZzIiwgImZzIiwgInRhYiIsICJ0YWIiLCAibWQiLCAiY29udGFpbmVyIiwgIm1kIiwgImNvbnRhaW5lciIsICJ0YWIiXQp9Cg==
