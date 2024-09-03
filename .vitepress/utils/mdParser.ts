import Path from "path";
import fs from "fs";
import { marked, TokensList, Tokens } from 'marked';
import { FileItem, Sidebar } from "./sidebarGenerator";

export default class gitbookParser {
    private path: string;
    private file: string;
    private token: TokensList;
    public sidebar: Sidebar;
    constructor(path: string) {
        this.path = path;
        this.file = fs.readFileSync(Path.join(path, 'SUMMARY.md'), 'utf8');
        this.token = marked.lexer(this.file)
        this.locator()
    }

    private locator() {
        this.token.forEach(tk => {
            if (this.isList(tk)) {
                this.sidebar = this.listLooper(tk)
            }
        })
    }
    private listLooper(list: Tokens.List): Sidebar {
        const sidebar: Sidebar = {
            text: "",
            collapsed: true,
            items: []
        }
        list.items.forEach(item => {
            if(this.isListItem(item)) {
                item.tokens.forEach(litk => {
                    if (this.isText(litk) && litk.tokens && this.isLink(litk.tokens[0])) {
                        const link: string = litk.tokens[0].href.replace(".md", "")
                        const text: string = litk.tokens[0].text
                        const item: FileItem = {
                            text: text,
                            link: `${this.path.replace("./docs", "")}${link}`
                        }
                        sidebar.items.push(item)
                    } else if (this.isList(litk)) {
                        const subSidebar: Sidebar = this.listLooper(litk)
                        sidebar.items[sidebar.items.length - 1].collapsed =  true,
                        sidebar.items[sidebar.items.length - 1].items = subSidebar.items
                    }
                })
            }
        })
        return sidebar;
    }
    private isList(token: any): token is Tokens.List {
        return token.type === "list"
    }
    private isListItem(token: any): token is Tokens.ListItem {
        return token.type === "list_item"
    }
    private isText(token: any): token is Tokens.Text {
        return token.type === "text"
    }
    private isLink(token: any): token is Tokens.Link {
        return token.type === "link"
    }
}