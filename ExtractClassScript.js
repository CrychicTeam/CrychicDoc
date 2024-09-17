import * as ts from 'typescript';
import { promises as fs } from 'fs';
import path from 'path';

// 递归读取文件夹内的所有文件
async function getFilesFromDir(startPath, filter, fileList = []) {
    const files = await fs.readdir(startPath);
    for (let file of files) {
        const filename = path.join(startPath, file);
        const stat = await fs.lstat(filename);
        if (stat.isDirectory()) {
            await getFilesFromDir(filename, filter, fileList); // 递归遍历子文件夹
        } else if (filename.endsWith(filter)) {
            fileList.push(filename); // 添加 .d.ts 文件
        }
    }
    return fileList;
}

// 解析文件，提取类名
function getClassesFromSourceFile(filePath, fileContent) {
    const sourceFile = ts.createSourceFile(
        filePath,
        fileContent,
        ts.ScriptTarget.Latest,
        true
    );

    const classes = [];
    function visit(node) {
        if (ts.isClassDeclaration(node) && node.name) {
            classes.push(node.name.text);
        }
        ts.forEachChild(node, visit);
    }
    visit(sourceFile);
    return classes;
}

async function main() {
    const dir = './typefiles/1.20.1/probe/generated/internals'; // 类型文件目录
    const files = await getFilesFromDir(dir, '.d.ts');

    let allClasses = [];
    for (let file of files) {
        const fileContent = await fs.readFile(file, 'utf-8');
        const classes = getClassesFromSourceFile(file, fileContent);
        allClasses = allClasses.concat(classes);
    }

    // 创建 Markdown 文件内容
    let markdownContent = '# 导出的类名\n\n';
    allClasses.forEach(className => {
        markdownContent += `Internal.${className}\n`;
    });

    // 将内容写入 Markdown 文件
    await fs.writeFile('./extracted_classes.md', markdownContent, 'utf-8');
    console.log("类名已成功导出到 'extracted_classes.md'");
}

main().catch(console.error);
