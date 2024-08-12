import fs from 'fs';
import path from 'path';
import matter from 'gray-matter';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const localization = {
    'zh': { 'backToParent': '返回上级', 'uncategorized': '未分类' },
    'en': { 'backToParent': 'Back to Parent', 'uncategorized': 'Uncategorized' }
};

function readDirContent(dir, rootDir, langPrefix, isCurrentDir = true, parentTags = null) {
    let items = [];
    let indexData = null;
    let sidebarOrder = {};
    let tagGroups = {};
    let useTagDisplay = false;
    let backPath = null;

    const dirents = fs.readdirSync(dir, { withFileTypes: true });
    
    const indexFile = dirents.find(dirent => dirent.isFile() && dirent.name === 'index.md');
    if (indexFile) {
        const indexPath = path.join(dir, 'index.md');
        const indexContent = fs.readFileSync(indexPath, 'utf8');
        const { data } = matter(indexContent);
        indexData = data;
        if (data.sidebarorder) {
            sidebarOrder = data.sidebarorder;
        }
        if (data.tagDisplay === true) {
            useTagDisplay = true;
        }
        if (data.back) {
            backPath = data.back;
        }

        if (!isCurrentDir || data.generateSidebar === true) {
            items.push({
                text: data.sidetitle || data.title || path.basename(dir),
                link: `/${langPrefix}/${path.relative(rootDir, indexPath).replace(/\.md$/, '.html')}`,
                order: -Infinity
            });
        }
    }

    for (const dirent of dirents) {
        if (dirent.name === 'index.md') continue;

        const fullPath = path.join(dir, dirent.name);
        const relativePath = path.relative(rootDir, fullPath);

        if (dirent.isFile() && dirent.name.endsWith('.md')) {
            const content = fs.readFileSync(fullPath, 'utf8');
            const { data } = matter(content);
            if (data.title) {
                const itemName = path.parse(dirent.name).name;
                const displayTitle = isCurrentDir ? (data.title || data.sidetitle) : (data.sidetitle || data.title);
                const item = {
                    text: displayTitle,
                    link: `/${langPrefix}/${relativePath.replace(/\.md$/, '.html')}`,
                    order: sidebarOrder[data.sidetitle] || sidebarOrder[data.title] || sidebarOrder[itemName] || Infinity
                };
                
                if (useTagDisplay) {
                    const tag = data.tag || indexData?.tags || parentTags || localization[langPrefix]?.uncategorized || 'Uncategorized';
                    if (!tagGroups[tag]) {
                        tagGroups[tag] = [];
                    }
                    tagGroups[tag].push(item);
                } else {
                    items.push(item);
                }
            }
        } else if (dirent.isDirectory()) {
            const subIndexPath = path.join(fullPath, 'index.md');
            if (fs.existsSync(subIndexPath)) {
                const subIndexContent = fs.readFileSync(subIndexPath, 'utf8');
                const { data } = matter(subIndexContent);
                const displayTitle = data.sidetitle || data.title || dirent.name;
                const item = {
                    text: displayTitle,
                    link: `/${langPrefix}/${relativePath}/`,
                    order: sidebarOrder[data.sidetitle] || sidebarOrder[data.title] || sidebarOrder[dirent.name] || Infinity
                };
                
                if (useTagDisplay) {
                    const tag = data.tags || indexData?.tags || parentTags || localization[langPrefix]?.uncategorized || 'Uncategorized';
                    if (!tagGroups[tag]) {
                        tagGroups[tag] = [];
                    }
                    tagGroups[tag].push(item);
                } else {
                    items.push(item);
                }
            }
        }
    }

    if (useTagDisplay) {
        const sortedGroups = Object.entries(tagGroups).map(([tag, groupItems]) => ({
            text: tag,
            items: groupItems.sort((a, b) => (a.order || Infinity) - (b.order || Infinity)),
            collapsible: true,
            collapsed: false
        }));

        sortedGroups.sort((a, b) => (a.items[0]?.order || Infinity) - (b.items[0]?.order || Infinity));
        items = sortedGroups;
    } else {
        items.sort((a, b) => (a.order || Infinity) - (b.order || Infinity));
    }

    return { items, indexData, useTagDisplay, backPath };
}

function generateSidebar(fullPath, parentTags = null) {
    const docsPath = path.resolve(__dirname, '../../docs');
    const normalizedPath = path.normalize(fullPath).replace(/\\/g, '/');
    const [lang, ...rest] = normalizedPath.split('/');
    const langPrefix = lang;
    
    const fullDir = path.join(docsPath, normalizedPath);
    const rootDir = path.join(docsPath, lang);
    
    if (!fs.existsSync(fullDir)) {
        console.error(`Directory not found: ${fullDir}`);
        return [];
    }

    const { items, indexData, useTagDisplay, backPath } = readDirContent(fullDir, rootDir, langPrefix, true, parentTags);

    if (rest.length > 0) {
        let parentLink;
        if (backPath) {
            if (backPath.startsWith('/')) {
                parentLink = backPath;
            } else {
                const currentDir = path.dirname(normalizedPath);
                parentLink = '/' + path.normalize(path.join(currentDir, backPath)).replace(/\\/g, '/');
            }
        } else {
            const parentPath = path.dirname(normalizedPath);
            parentLink = `/${parentPath}/`;
        }
        
        items.unshift({
            text: localization[lang]?.backToParent || 'Back to Parent',
            link: parentLink,
            order: -Infinity
        });
    }

    return { items, tags: indexData?.tags, useTagDisplay };
}

function generateSidebarConfig(basePath) {
    const docsPath = path.resolve(__dirname, '../../docs');
    const fullBasePath = path.join(docsPath, basePath);
    const sidebarConfig = {};

    function traverse(dir, currentPath, parentTags = null) {
        const relativePath = path.relative(fullBasePath, dir).replace(/\\/g, '/');
        const key = `/${basePath}${relativePath ? '/' + relativePath : ''}/`;
        const { items, tags, useTagDisplay } = generateSidebar(path.join(basePath, relativePath), parentTags);
        sidebarConfig[key] = items;

        const subdirs = fs.readdirSync(dir, { withFileTypes: true })
            .filter(dirent => dirent.isDirectory())
            .map(dirent => dirent.name);

        for (const subdir of subdirs) {
            traverse(path.join(dir, subdir), path.join(currentPath, subdir), useTagDisplay ? tags : null);
        }
    }

    traverse(fullBasePath, '');
    return sidebarConfig;
}

export { generateSidebarConfig };
