import fs from 'fs';
import path from 'path';

// 指定目标目录
const targetDirectory = './docs/zh/modpack/kubejs/1.20.1/KubeJSCourse/KubeJSProjects/Wolf/'; // 将此处替换为你的目标目录路径
// 要添加的作者名
const authorToAdd = 'LitterWolf-fufu';

function processDirectory(directory) {
    fs.readdir(directory, { withFileTypes: true }, (err, files) => {
        if (err) {
            console.error(`无法读取目录: ${directory}, 错误: ${err.message}`);
            return;
        }

        files.forEach(file => {
            const filePath = path.join(directory, file.name);

            if (file.isDirectory()) {
                processDirectory(filePath);
            } else if (file.isFile() && path.extname(file.name) === '.md') {
                processFile(filePath);
            }
        });
    });
}

function processFile(filePath) {
    fs.readFile(filePath, 'utf8', (readErr, data) => {
        if (readErr) {
            console.error(`无法读取文件: ${filePath}, 错误: ${readErr.message}`);
            return;
        }

        const frontmatterRegex = /^---\s*\n([\s\S]*?)\n---\s*\n/;
        const match = data.match(frontmatterRegex);

        if (match) {
            let frontmatter = match[1];
            const content = data.slice(match[0].length);
            const frontmatterLines = frontmatter.split('\n');
            let authorsLine = frontmatterLines.find(line => line.startsWith('authors:'));

            if (authorsLine) {
                const authors = authorsLine.match(/\[(.*?)\]/)[1].split(',').map(a => a.trim().replace(/^['"]|['"]$/g, ''));
                if (!authors.includes(authorToAdd)) {
                    authors.push(authorToAdd);
                    authorsLine = `authors: [${authors.map(a => `'${a}'`).join(', ')}]`;
                    frontmatter = frontmatterLines.map(line => 
                        line.startsWith('authors:') ? authorsLine : line
                    ).join('\n');
                } else {
                    console.log(`文件已包含作者 ${authorToAdd}: ${filePath}`);
                    return;
                }
            } else {
                frontmatter += `\nauthors: ['${authorToAdd}']`;
            }

            const updatedContent = `---\n${frontmatter}\n---\n${content}`;
            fs.writeFile(filePath, updatedContent, 'utf8', (writeErr) => {
                if (writeErr) {
                    console.error(`无法写入文件: ${filePath}, 错误: ${writeErr.message}`);
                } else {
                    console.log(`已更新文件: ${filePath}`);
                }
            });
        } else {
            const newFrontmatter = `---\nauthors: ['${authorToAdd}']\n---\n\n`;
            const updatedContent = newFrontmatter + data;
            fs.writeFile(filePath, updatedContent, 'utf8', (writeErr) => {
                if (writeErr) {
                    console.error(`无法写入文件: ${filePath}, 错误: ${writeErr.message}`);
                } else {
                    console.log(`已更新文件并添加新的 frontmatter: ${filePath}`);
                }
            });
        }
    });
}
processDirectory(targetDirectory);