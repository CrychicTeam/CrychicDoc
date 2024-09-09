import path from "path";
import fs from "fs";
import matter from "gray-matter";

type LanguageConfig = {
    [key: string]: {
        backText: string;
        pathIdentifier: string;
    };
};

export default class SidebarGenerator {
    private dirPath: string;
    private _correctedPathname: string;
    private pathname: string;
    private items: string[];
    private _sidebar: Sidebar;
    private isTop: boolean;
    private language: string;

    private static DIR_PATH: string = path.resolve();
    private static readonly BLACK_LIST: string[] = [
        ".vitepress",
        "node_modules",
        "assets",
    ];
    private static readonly LANGUAGE_CONFIG: LanguageConfig = {
        en: { backText: "Back to parent", pathIdentifier: "/en/" },
        zh: { backText: "回到上级", pathIdentifier: "/zh/" },
    };
    private static readonly DEFAULT_LANGUAGE: string = "zh";

    constructor(pathname: string, isTop: boolean) {
        this._sidebar = {
            text: "",
            items: [],
        };
        this.pathname = pathname;
        this.dirPath = path.join(SidebarGenerator.DIR_PATH, pathname);
        this.items = this.filterOutWhiteList(
            fs.readdirSync(this.dirPath),
            SidebarGenerator.BLACK_LIST
        );
        this._correctedPathname = this.pathname.replace(/^docs\//, "/");
        this.isTop = isTop;
        this.language = this.detectLanguage();

        this.builder();
    }

    public get sidebar(): Sidebar {
        return this._sidebar;
    }

    public get correctedPathname(): string {
        return this._correctedPathname;
    }

    private builder(): void {
        const root = this.indexReader()?.root;
        const backLink = root?.backLink || "..";
        if (this.isTop) {
            const backText =
                SidebarGenerator.LANGUAGE_CONFIG[this.language].backText;
            this._sidebar.items.unshift({
                text: backText,
                link: backLink,
            });
        }

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
            // 如果没有找到 index.md，扫描当前目录
            this._sidebar.items.push(
                ...this.scanDirectory(this.dirPath, this._correctedPathname)
            );
        }
    }

    private buildSidebarItems(
        children: SubDir[],
        currentPath: string,
        currentDirPath: string
    ): Array<Sidebar | FileItem> {
        return children.map((child) => {
            const childPath = path.join(currentDirPath, child.path);

            // 如果存在 file 属性，则创建文件项
            if (child.file) {
                return this.createFileItem(child, currentPath, childPath);
            }

            // 处理目录
            const subSideBar: Sidebar = {
                text: child.title,
                collapsed: child.collapsed,
                items: [],
            };

            const subPath =
                child.path === "/"
                    ? currentPath
                    : `${currentPath}/${child.path}`.replace(/\/+/g, "/");

            if (child.children && child.children.length > 0) {
                subSideBar.items = this.buildSidebarItems(
                    child.children,
                    subPath,
                    childPath
                );
            } else if (!child.noScan) {
                // 只有在 noScan 不为 true 且没有 file 属性时才扫描目录
                subSideBar.items = this.scanDirectory(childPath, subPath);
            }

            return subSideBar;
        });
    }

    private scanDirectory(
        dirPath: string,
        currentPath: string
    ): Array<Sidebar | FileItem> {
        const items = fs.readdirSync(dirPath);
        const filteredItems = items
            .filter((item) => !SidebarGenerator.BLACK_LIST.includes(item))
            .map((item) => {
                const fullPath = path.join(dirPath, item);
                if (fs.statSync(fullPath).isDirectory()) {
                    const sidebarItem: Sidebar = {
                        text: item,
                        collapsed: true,
                        items: this.scanDirectory(
                            fullPath,
                            `${currentPath}/${item}`
                        ),
                    };
                    return sidebarItem;
                } else if (item.endsWith(".md")) {
                    const fileContent = this.fileReader(fullPath);
                    const itemWithoutMd = item.replace(/\.md$/i, "");
                    const fileItem: FileItem = {
                        text: fileContent?.title || itemWithoutMd,
                        link: `${currentPath}/${itemWithoutMd}`,
                    };
                    return fileItem;
                }
                return null;
            });

        return filteredItems.filter(
            (item): item is Sidebar | FileItem => item !== null
        );
    }

    private createFileItem(
        file: SubDir,
        currentPath: string,
        filePath: string
    ): FileItem {
        const fileContent = this.fileReader(filePath);
        const itemWithoutMd = (file.file || file.path).replace(/\.md$/i, "");

        let link: string;
        if (file.path === "/") {
            link = file.file
                ? `${currentPath}/${file.file.replace(/\.md$/i, "")}`
                : currentPath;
        } else {
            link = file.file
                ? `${currentPath}/${file.path}/${file.file.replace(
                        /\.md$/i,
                        ""
                    )}`
                : `${currentPath}/${file.path}`;
        }

        link = link.replace(/\/+/g, "/");

        return {
            text: file.title || fileContent?.title || itemWithoutMd,
            link: link,
        };
    }

    private detectLanguage(): string {
        for (const [lang, config] of Object.entries(
            SidebarGenerator.LANGUAGE_CONFIG
        )) {
            if (this.pathname.includes(config.pathIdentifier)) {
                return lang;
            }
        }
        return SidebarGenerator.DEFAULT_LANGUAGE;
    }

    private fileReader(filePath: string): FileFrontMatter | null {
        try {
            const fileObject: string = fs.readFileSync(filePath, "utf8");
            const { data } = matter(fileObject);
            if (data) {
                return data as FileFrontMatter;
            } else {
                throw new Error("Invalid file format");
            }
        } catch (error) {
            return null;
        }
    }

    private indexReader(): Index | null {
        try {
            const indexPath: string = path.join(this.dirPath, "index.md");
            const indexFile: string = fs.readFileSync(indexPath, "utf8");
            const { data } = matter(indexFile);
            if (data.root && Array.isArray(data.root.children)) {
                return data as Index;
            } else {
                throw new Error("Invalid index file format");
            }
        } catch (error) {
            return null;
        }
    }

    private filterOutWhiteList = (files: string[], blackList: string[]) =>
        files.filter((file: string) => !blackList.includes(file));
}

export interface Sidebar {
    text: string;
    collapsed?: boolean;
    link?: string;
    items: Array<Sidebar | FileItem>;
}

export interface FileItem {
    text: string;
    link: string;
    collapsed?: boolean;
    items?: FileItem[];
}

interface Index {
    root: {
        title: string;
        backLink?: string;
        collapsed?: boolean;
        children: SubDir[];
    };
}

interface SubDir {
    title: string;
    path: string;
    noScan?: boolean;
    collapsed?: boolean;
    file?: string;
    children?: SubDir[];
}

interface FileFrontMatter {
    title: string;
    noguide?: boolean;
}
