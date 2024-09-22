import fs from 'fs';
import path from 'path';

// 指定目标目录
const targetDirectory = './docs/en/modpack/kubejs/1.20.1/KubeJSCourse/'; // 将此处替换为你的目标目录路径

// 要插入的内容
const contentToAdd = `---\nauthors: ['Gu-meng']\n---\n`;

// 递归遍历目录并处理所有 .md 文件
function processDirectory(directory) {
    fs.readdir(directory, { withFileTypes: true }, (err, files) => {
        if (err) {
            console.error(`无法读取目录: ${directory}, 错误: ${err.message}`);
            return;
        }

        files.forEach(file => {
            const filePath = path.join(directory, file.name);

            if (file.isDirectory()) {
                // 如果是子目录，则递归处理
                processDirectory(filePath);
            } else if (file.isFile() && path.extname(file.name) === '.md') {
                // 如果是 .md 文件，则处理文件内容
                processFile(filePath);
            }
        });
    });
}

// 处理 .md 文件的函数
function processFile(filePath) {
    // 读取文件内容
    fs.readFile(filePath, 'utf8', (readErr, data) => {
        if (readErr) {
            console.error(`无法读取文件: ${filePath}, 错误: ${readErr.message}`);
            return;
        }

        // 检查文件是否已经包含目标内容，避免重复添加
        if (!data.startsWith(contentToAdd)) {
            // 将内容插入到文件的开头
            const updatedContent = contentToAdd + data;

            // 写回文件
            fs.writeFile(filePath, updatedContent, 'utf8', (writeErr) => {
                if (writeErr) {
                    console.error(`无法写入文件: ${filePath}, 错误: ${writeErr.message}`);
                } else {
                    console.log(`已更新文件: ${filePath}`);
                }
            });
        } else {
            console.log(`文件已包含指定内容: ${filePath}`);
        }
    });
}

// 开始处理指定目录
processDirectory(targetDirectory);
