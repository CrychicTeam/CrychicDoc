import sidebar from "../utils/sidebarGenerator"
import md from "../utils/mdParser"
import Path from "path";
import fs from "fs";
import {dirs, summary} from "../index"

export function sidebars(lang: string): {} {
    let ISidebar = {};
    indexParser(dirs, lang, ISidebar)
    gitbookParser(summary, ISidebar)
    
    return ISidebar;
}

export function logger(string: string, name: string): void {
    fs.writeFileSync(Path.join(__dirname, name), `${string}\n`, { flag: 'w+' });
}

function indexParser(Idirs: string[], lang: string, ISidebar: {}) {
    Idirs.forEach(dir => {
        const generator = new sidebar(`docs/${lang}/${dir}`, true);
        ISidebar[`${lang}/${dir}/`] = [generator.sidebar]
    })
}

function gitbookParser(Isummary: string[][], ISidebar: {}) {
    Isummary.forEach(path => {
        ISidebar[path[0]] = [new md(path[1]).sidebar]
    })
}