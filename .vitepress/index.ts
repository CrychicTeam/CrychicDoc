import sidebar from "./utils/sidebarGenerator"
import md from "./utils/mdParser"
import Path from "path";
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
    ISidebar["zh/modpack/kubejs/KubejsCourse/"] = [new md("./docs/zh/modpack/kubejs/KubejsCourse").sidebar]
    // logger(JSON.stringify(ISidebar, null, 2), "dev.json")
    // logger(JSON.stringify(new md("./docs/zh/modpack/kubejs/kubejs-course-main").sidebar, null, 2), "ast.json")
    return ISidebar;
}
function logger(string: string, name: string): void {
    fs.writeFileSync(Path.join(__dirname, name), `${string}\n`, { flag: 'w+' });
}