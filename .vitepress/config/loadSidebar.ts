import { sidebars } from "./sidebarControl";
import fs from "fs";
import path from "path";
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export function loadSidebar(lang: string): any {
    try {
        const sidebarPath = path.join(__dirname, '..', `sidebar-${lang}.json`);
        if (fs.existsSync(sidebarPath)) {
            console.log(`使用预生成的侧边栏配置: sidebar-${lang}.json`);
            const sidebarContent = fs.readFileSync(sidebarPath, 'utf-8');
            return JSON.parse(sidebarContent);
        }
    } catch (error) {
        console.log(`无法加载预生成的侧边栏配置，将使用动态生成: ${error instanceof Error ? error.message : String(error)}`);
    }
    console.log(`使用动态生成的侧边栏配置...`);
    return sidebars(lang);
}