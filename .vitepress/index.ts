import sidebar from "./utils/sidebarGenerator"
import { Sidebar } from "./utils/sidebarGenerator"
import path from "path";
import fs from "fs";

const dirs = [
    "doc",
    "developers",
    "mods",
    "mods/adventure",
    "modpack",
    "modpack/kubejs"
];

export default function sidebars(lang: string): {} {
    let ISidebar = {};
    dirs.forEach(dir => {
        const generator = new sidebar(`docs/${lang}/${dir}`, true);
        ISidebar[`${lang}/${dir}/`] = [generator.sidebar]
    })
    
    logger(JSON.stringify(ISidebar, null, 2))
    return ISidebar;
}
function logger(string: string): void {
    fs.writeFileSync(path.join(__dirname, 'dev.json'), `${string}\n`, { flag: 'w+' });
}