import path from 'node:path';
import fs from 'node:fs/promises';
import yaml from 'js-yaml';
import { GlobalSidebarConfig } from '../types';

/**
 * Loads and parses the global .sidebarrc.yml file from the docs root.
 * @param docsPath Absolute path to the root of the 'docs' directory.
 * @returns The parsed GlobalSidebarConfig object, or null if the file is not found or a parse error occurs.
 */
export async function loadGlobalConfig(docsPath: string): Promise<GlobalSidebarConfig | null> {
    const globalConfigPath = path.join(docsPath, '.sidebarrc.yml');
    try {
        const fileContent = await fs.readFile(globalConfigPath, 'utf-8');
        const parsedConfig = yaml.load(fileContent) as GlobalSidebarConfig;
        // Ensure it's an object, even if the YAML file is empty but valid (yaml.load might return undefined/null for empty string)
        if (parsedConfig && typeof parsedConfig === 'object') {
            return parsedConfig;
        }
        // If file is empty or not a valid object structure after parsing (e.g. empty file parsed to null/undefined by js-yaml)
        // console.warn(`[ConfigReader/GlobalLoader] '.sidebarrc.yml' at ${globalConfigPath} is empty or not a valid object. Using no global defaults.`);
        return {}; // Return an empty object to signify loaded but empty/invalid config, allows defaults merging to proceed
    } catch (error: any) {
        if (error.code === 'ENOENT') {
            // File not found is an expected case, no need to log an error, just return null.
            // The ConfigReaderService will treat null as "no global config found".
        } else {
            // Log other errors like YAML parsing errors.

        }
        return null; // Indicates not found or error during load/parse
    }
} 


