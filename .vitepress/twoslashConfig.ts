import { cwd } from "node:process";
import { join } from "node:path";
import fs from "fs";

const typeFilesPath = join(
    cwd(),
    "typefiles",
    "1.20.1",
    "probe",
    "generated",
    "internals"
);
const internalTypeFiles = fs.existsSync(typeFilesPath)
    ? fs
          .readdirSync(typeFilesPath)
          .filter(
              (file) => file.startsWith("internal_") && file.endsWith(".d.ts")
          )
          .map((file) => join(typeFilesPath, file))
    : [];

// 添加一个日志，以便当目录不存在时能够进行排查
if (!internalTypeFiles.length) {
    console.warn(`No internal type files found in ${typeFilesPath}`);
}

export const compilerOptions = {
    cache: true,
    compilerOptions: {
        baseUrl: cwd(),
        target: "ES2020", // 使用更标准的 TypeScript 目标
        module: "ESNext", // 使用标准模块格式
        moduleResolution: "node",
        paths: {
            "*": [
                join(cwd(), "typefiles", "1.20.1", "probe", "generated", "*"),
            ],
        },
        resolveJsonModule: true,
        types: ["node", ...internalTypeFiles], // 自动使用文件，如果没有则保持现状
        esModuleInterop: true,
        isolatedModules: true,
        verbatimModuleSyntax: true,
        skipLibCheck: true,
        skipDefaultLibCheck: true,
    },
};
