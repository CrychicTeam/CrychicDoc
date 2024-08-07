import fs from 'fs';
import path from 'path';
import matter from 'gray-matter';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

function readDir(dir, rootDir, langPrefix) {
    let items = [];
    let indexItem = null;
    fs.readdirSync(dir, { withFileTypes: true }).forEach(dirent => {
        const fullPath = path.join(dir, dirent.name);
        const relativePath = path.relative(rootDir, fullPath);
        if (dirent.isFile() && dirent.name.endsWith('.md')) {
            const content = fs.readFileSync(fullPath, 'utf8');
            const { data } = matter(content);
            if (data.title) {
                const link = `/${langPrefix}/${relativePath.replace(/\.md$/, '.html')}`;
                const item = { text: data.title, link };
                if (dirent.name === 'index.md') {
                    indexItem = item;
                } else {
                    items.push(item);
                }
            }
        }
    });

    // Place index.md at the beginning of the array if it exists
    if (indexItem) {
        items.unshift(indexItem);
    }

    return items;
}

function generateSidebar(baseDir, currentDir, langPrefix) {
    const docsPath = path.resolve(__dirname, baseDir);
    const currentPath = path.resolve(docsPath, currentDir);
    const rootDir = docsPath;
    const sidebarItems = readDir(currentPath, rootDir, langPrefix);
    return sidebarItems.length > 0 ? sidebarItems : [];
}

export { generateSidebar };