import path from 'path';
import fs from 'fs';
import { fileURLToPath } from 'url';
import sidebar from './.vitepress/utils/sidebarGenerator';
import md from './.vitepress/utils/mdParser';
import { dirs, summary } from './.vitepress/index';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

function generateSidebarJSON() {
    const languages = ['zh', 'en'];
    
    for (const lang of languages) {
        console.log(`正在生成 ${lang} 语言的侧边栏配置...`);
        let ISidebar = {};
        dirs.forEach(dir => {
            const generator = new sidebar(`docs/${lang}/${dir}`, true);
            ISidebar[`${lang}/${dir}/`] = [generator.sidebar];
        });
        summary.forEach(path => {
            ISidebar[path[0]] = [new md(path[1]).sidebar];
        });
        const outputPath = path.join(__dirname, '.vitepress', `sidebar-${lang}.json`);
        fs.writeFileSync(outputPath, JSON.stringify(ISidebar, null, 2));
        console.log(`已生成侧边栏配置: .vitepress/sidebar-${lang}.json`);
    }
}
generateSidebarJSON();