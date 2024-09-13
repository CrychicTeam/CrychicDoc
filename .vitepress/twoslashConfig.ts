import { cwd } from "node:process";
import { join } from "node:path";
import fs from "fs";

const internalTypeFiles = fs
    .readdirSync(join(cwd(), "typefiles/1.20.1/probe/generated/internals"))
    .filter((file) => file.startsWith("internal_") && file.endsWith(".d.ts"))
    .map((file) =>
        join(cwd(), "typefiles/1.20.1/probe/generated/internals", file)
    );

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
        types: ["node", ...internalTypeFiles],
        esModuleInterop: true,
        isolatedModules: true,
        verbatimModuleSyntax: true,
        skipLibCheck: true,
        skipDefaultLibCheck: true,
    },
};
