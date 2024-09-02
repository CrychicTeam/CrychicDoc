import path from "path";
import fs from "fs";
import matter from "gray-matter";

export default class SidebarGenerator{
    private dirPath: string;
    private _correctedPathname: string;
    private pathname: string;
    private items: string[];
    private _sidebar: Sidebar;
    private isTop: boolean;

    private static DIR_PATH: string = path.resolve();
    private static readonly BLACK_LIST: string[] = [
        ".vitepress",
        "node_modules",
        "assets",
    ];
    
    constructor(pathname:string, isTop:boolean) {
        this._sidebar = {
            text: "",
            items: [],
        }
        this.pathname = pathname;
        this.dirPath = path.join(SidebarGenerator.DIR_PATH, pathname);
        this.items = this.filterOutWhiteList(fs.readdirSync(this.dirPath), SidebarGenerator.BLACK_LIST);
        this._correctedPathname = this.pathname.replace(/^docs\//, '/');
        this.isTop= isTop;

        this.builder();
        //this.logger(JSON.stringify(this._sidebar, null, 2))
    }

    public get sidebar() : Sidebar {
        return this._sidebar;
    }

    public get correctedPathname() : string {
        return this._correctedPathname;
    }
    

    private builder(): void{
        if(this.isTop) this._sidebar.items.unshift({
            text:"回到上级",
            link:".."
        })
        const root = this.indexReader()?.root;
        if(root){
            const rootTitle: string = root.title;
            const subDirs: SubDir[] = root.subDir;
            
            this._sidebar.text = rootTitle;
            this._sidebar.collapsed = root.collapsed;

            subDirs.forEach(subDir => {
                const subSideBar: Sidebar = {
                    text: "",
                    items: []
                };
                const subDirPath: string = path.join(this.dirPath, subDir.path);
                
                if (this.isDir(subDirPath)) {
                    subSideBar.text = subDir.title;
                    subSideBar.collapsed = subDir.collapsed;
                    this._sidebar.items.push(subSideBar);
                    const subDirContainer = new SidebarGenerator(`${this.pathname}/${subDir.path}`, false);
                    subDirContainer._sidebar.items.forEach(item => {
                        subSideBar.items.push(item)
                    });
                }
            });
        }
        this.items.forEach(item => {
            const filePath: string = path.join(this.dirPath, item);
            const file: FileFrontMatter| null = this.fileReader(filePath);
            if(!this.isDir(filePath) && !file?.noguide) {
                const itemWithoutMd: string = item.replace(/\.md$/i, "")
                this._sidebar.items.push({
                    text : file?.title || itemWithoutMd,
                    link : `${this._correctedPathname}/${itemWithoutMd}`
                });
            }
            
        });
    }

    private isDir(path: string): boolean {
        return fs.lstatSync(path).isDirectory();
    }

    private fileReader(filePath: string): FileFrontMatter| null{
        try {
            const fileObject: string = fs.readFileSync(filePath, 'utf8');
            const {data} = matter(fileObject);
            if (data) {
                return data as FileFrontMatter;
            } else {
                throw new Error('Invalid file format');
            }
        } catch(error) {
            return null
        }
    }

    private indexReader(): Index|null {
        try {
            const indexPath: string = path.join(this.dirPath, 'index.md');
            const indexFile: string = fs.readFileSync(indexPath, 'utf8');
            const {data} = matter(indexFile);
            if (data.root && Array.isArray(data.root.subDir)) {
                return data as Index;
            } else {
                throw new Error('Invalid index file format');
            }
        } catch(error) {
            return null;
        }
    }

    public logger(string: string): void {
        fs.writeFileSync(path.join(__dirname, 'dev.log'), `${string}\n`, { flag: 'a+' });
    }

    private filterOutWhiteList = (files: string[], blackList: string[]) => files.filter((file: string) => !blackList.includes(file));
}

export interface Sidebar {
    text: string;
    collapsed?:boolean;
    items: Array<FileItem | Sidebar>;
}
interface FileItem {
    text: string;
    link: string;
}
interface Index {
    root: {
        title: string,
        collapsed?: boolean; 
        subDir: SubDir[]
    }
}
interface SubDir {
    title: string;
    path: string;
    collapsed?: boolean;
}
interface FileFrontMatter {
    title: string;
    noguide?: boolean;
}