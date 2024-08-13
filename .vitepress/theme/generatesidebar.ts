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

function readDirContent(dir, rootDir, langPrefix, tagOrder = {}, isCurrentDir = true, parentTags = null) {
    let items = [];
    let indexData = null;
    let sidebarOrder = {};
    let tagGroups = {};
    let useTagDisplay = false;
    let backPath = null;
    let autoPN = false;
    let folderBlackList = [];

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
        if (data.autoPN === true) {
            autoPN = true;
        }
        if (data.tagorder) {
            tagOrder = { ...tagOrder, ...data.tagorder };
        }
        folderBlackList = data.folderBlackList || [];

        if (data.generateSidebar === true) {
            items.push({
                text: isCurrentDir ? (data.title || data.sidetitle || path.basename(dir)) : (data.sidetitle || data.title || path.basename(dir)),
                link: `/${langPrefix}/${path.relative(rootDir, indexPath).replace(/\.md$/, '')}`,
                order: sidebarOrder['index'] || -Infinity
            });
        }
    }

    for (const dirent of dirents) {
        if (dirent.name === 'index.md') continue;
        if (folderBlackList.includes(dirent.name)) continue;

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
                    link: `/${langPrefix}/${relativePath.replace(/\.md$/, '')}`,
                    order: sidebarOrder[itemName] || sidebarOrder[data.title] || Infinity
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
                    order: sidebarOrder[dirent.name] || sidebarOrder[data.title] || Infinity
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

    const sortItems = (itemsToSort) => {
        return itemsToSort.sort((a, b) => {
            if (a.order === b.order) {
                return a.text.localeCompare(b.text);
            }
            return (a.order || Infinity) - (b.order || Infinity);
        });
    };

    if (useTagDisplay) {
        const sortedGroups = Object.entries(tagGroups).map(([tag, groupItems]) => ({
            text: tag,
            items: sortItems(groupItems),
            collapsible: true,
            collapsed: false,
            order: tagOrder[tag] !== undefined ? tagOrder[tag] : Infinity
        }));

        sortedGroups.sort((a, b) => {
            const orderA = tagOrder[a.text] !== undefined ? tagOrder[a.text] : Infinity;
            const orderB = tagOrder[b.text] !== undefined ? tagOrder[b.text] : Infinity;
            if (orderA === orderB) {
                return a.text.localeCompare(b.text);
            }
            return orderA - orderB;
        });
        items = sortedGroups;
    } else {
        items = sortItems(items);
    }

    return { items, indexData, useTagDisplay, backPath, autoPN };
}

function generatePrevNext(dir, items, rootDir) {
    const docItems = items.flatMap(item => 
        Array.isArray(item.items) ? item.items : [item]
    ).filter(item => 
        item.link && typeof item.link === 'string' && !item.link.endsWith('/') && item.link !== '#'
    );

    const introItem = docItems.find(item => item.link.endsWith('index')) || docItems[0];

    docItems.forEach((item, index) => {
        if (!item.link) return;

        // 修复文件路径处理
        const relativePath = item.link.split('/').slice(2).join('/');
        const filePath = path.join(rootDir, relativePath + '.md');

        if (!fs.existsSync(filePath)) {
            console.warn(`File not found: ${filePath}`);
            return;
        }

        try {
            const content = fs.readFileSync(filePath, 'utf8');
            const { data, content: fileContent } = matter(content);

            let updatedFrontmatter = { ...data };

            if (index === 0) {
                updatedFrontmatter.prev = {
                    text: docItems[docItems.length - 1].text,
                    link: docItems[docItems.length - 1].link
                };
            } else {
                updatedFrontmatter.prev = {
                    text: docItems[index - 1].text,
                    link: docItems[index - 1].link
                };
            }

            if (index === docItems.length - 1) {
                updatedFrontmatter.next = {
                    text: introItem.text,
                    link: introItem.link
                };
            } else {
                updatedFrontmatter.next = {
                    text: docItems[index + 1].text,
                    link: docItems[index + 1].link
                };
            }

            const updatedContent = matter.stringify(fileContent, updatedFrontmatter);
            fs.writeFileSync(filePath, updatedContent);
            console.log(`Updated prev/next for: ${filePath}`);
        } catch (error) {
            console.error(`Error processing file: ${filePath}`, error);
        }
    });
}

function generateSidebar(fullPath, tagOrder = {}, parentTags = null) {
    const docsPath = path.resolve(__dirname, '../../docs');
    const normalizedPath = path.normalize(fullPath).replace(/\\/g, '/');
    const [lang, ...rest] = normalizedPath.split('/');
    const langPrefix = lang;

    const fullDir = path.join(docsPath, normalizedPath);
    const rootDir = path.join(docsPath, lang);

    if (!fs.existsSync(fullDir)) {
        console.error(`Directory not found: ${fullDir}`);
        return { items: [], tags: null, useTagDisplay: false };
    }

    const indexPath = path.join(fullDir, 'index.md');
    if (fs.existsSync(indexPath)) {
        const indexContent = fs.readFileSync(indexPath, 'utf8');
        const { data } = matter(indexContent);
        if (data.tagorder) {
            tagOrder = { ...tagOrder, ...data.tagorder };
        }
    }

    const { items, indexData, useTagDisplay, backPath, autoPN } = readDirContent(fullDir, rootDir, langPrefix, tagOrder, true, parentTags);

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

    if (autoPN) {
        generatePrevNext(fullDir, items, rootDir);
    }

    return { items, tags: indexData?.tags, useTagDisplay };
}

function generateSidebarConfig(basePath, tagOrder = {}) {
    const docsPath = path.resolve(__dirname, '../../docs');
    const fullBasePath = path.join(docsPath, basePath);
    const sidebarConfig = {};

    function traverse(dir, currentPath, parentTags = null) {
        const relativePath = path.relative(fullBasePath, dir).replace(/\\/g, '/');
        const key = `/${basePath}${relativePath ? '/' + relativePath : ''}/`;
        const { items, tags, useTagDisplay } = generateSidebar(path.join(basePath, relativePath), tagOrder, parentTags);
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