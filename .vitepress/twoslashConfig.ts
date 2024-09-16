import { cwd } from "node:process";
import { join } from "node:path";
import fs from "fs";

const typeFilesPath = join(cwd(), "typefiles/1.20.1/probe/generated/internals");
const internalTypeFiles = fs.existsSync(typeFilesPath)
    ? fs
          .readdirSync(typeFilesPath)
          .filter(
              (file) => file.startsWith("internal_") && file.endsWith(".d.ts")
          )
          .map((file) => join(typeFilesPath, file))
    : [];

export const compilerOptions = {
    cache: true,
    compilerOptions: {
        baseUrl: cwd(),
        target: 99,
        module: 99,
        moduleResolution: 100,
        paths: {
            "*": [join(cwd(), "typefiles/1.20.1/probe/generated/*")],
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
