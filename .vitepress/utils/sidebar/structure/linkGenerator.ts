import path from "node:path";
import { GroupConfig } from "../types";
import { FileSystem } from "../shared/FileSystem";
import { normalizePathSeparators } from "../shared/objectUtils";

/**
 * Generates a normalized URL link for a directory path relative to the language root.
 * Ensures it starts and ends with a slash.
 * Example: /path/to/dir/
 * @param dirAbsPath Absolute path to the directory.
 * @param docsAbsPath Absolute path to the /docs directory.
 * @param lang Current language code.
 */
function normalizeDirPathToUrl(
    dirAbsPath: string,
    docsAbsPath: string,
    lang: string
): string {
    const langRootAbsPath = normalizePathSeparators(
        path.join(docsAbsPath, lang)
    );
    let relativePath = normalizePathSeparators(
        path.relative(langRootAbsPath, dirAbsPath)
    );

    // If relativePath is empty, it means dirAbsPath is the langRootAbsPath itself.
    if (relativePath === "" || relativePath === ".") {
        relativePath = ""; // Link to /lang/ or / if lang is empty
    } else {
        relativePath = relativePath + "/";
    }

    let link = `/${lang}/${relativePath}`;
    // Consolidate multiple slashes into one, but not for protocol (e.g. http://)
    link = link.replace(/([^:])\/\/+/g, "$1/");
    // Ensure it ends with a slash if it has content beyond the language
    if (link !== `/${lang}/` && link !== "/" && !link.endsWith("/")) {
        link += "/";
    }
    if (link === `//`) link = "/"; // Handles root of docs if lang is empty or not used in path
    if (link.endsWith("//") && link !== "//") link = link.slice(0, -1); // Clean up potential double slash at end

    return link;
}

/**
 * Normalizes a group title or directory name into a slug for path matching.
 * Example: "Core Concepts" -> "core-concepts"
 */
function slugify(text: string): string {
    return text
        .toString()
        .toLowerCase()
        .replace(/\s+/g, "-") // Replace spaces with -
        .replace(/[^\w-]+/g, "") // Remove all non-word chars (keeps underscore and hyphen)
        .replace(/--+/g, "-") // Replace multiple - with single -
        .replace(/^-+/, "") // Trim - from start of text
        .replace(/-+$/, ""); // Trim - from end of text
}

/**
 * Generates a link for a directory or a group item based on defined rules.
 * @param itemName The name of the directory (e.g., "concepts") or group title (e.g., "Core Concepts").
 * @param itemType 'directory' or 'group'.
 * @param currentDirAbsPath Absolute FS path to the directory where the item (or group definition) resides.
 * @param docsAbsPath Absolute path to the 'docs' directory.
 * @param lang Current language code.
 * @param fs FileSystem instance.
 * @param groupConfig Optional. The configuration of the group if generating a link for a group.
 * @returns The link string if clickable (e.g., /en/guide/concepts/), or null if not.
 */
export async function generateLink(
    itemName: string, // For directories, this is the dir name. For groups, this is the group title.
    itemType: "directory" | "group",
    currentDirAbsPath: string, // For a dir, its parent. For a group, dir of index.md defining it.
    docsAbsPath: string,
    lang: string,
    fs: FileSystem,
    groupConfig?: GroupConfig
): Promise<string | null> {
    const normalizedCurrentDirAbsPath =
        normalizePathSeparators(currentDirAbsPath);

    if (itemType === "group" && groupConfig) {
        if (typeof groupConfig.link === "string") {
            return groupConfig.link.startsWith("/") ||
                groupConfig.link.startsWith("http")
                ? groupConfig.link
                : normalizePathSeparators(
                        path.join("/", lang, groupConfig.link)
                  ); // Assume relative to lang root if not absolute
        }
        if (groupConfig.link === false) {
            return null; // Explicitly not linkable
        }

        let targetDirForGroupIndexMdAbs: string | null = null;

        // 1. Check groupConfig.path
        if (groupConfig.path) {
            targetDirForGroupIndexMdAbs = normalizePathSeparators(
                path.resolve(normalizedCurrentDirAbsPath, groupConfig.path)
            );
            if (
                await fs.exists(
                    path.join(targetDirForGroupIndexMdAbs, "index.md")
                )
            ) {
                return normalizeDirPathToUrl(
                    targetDirForGroupIndexMdAbs,
                    docsAbsPath,
                    lang
                );
            }
        }

        // 2. Check groupConfig.title match if path didn't lead to a linkable index.md
        const sluggedTitle = slugify(groupConfig.title);
        if (sluggedTitle) {
            const potentialDirFromTitleAbs = normalizePathSeparators(
                path.join(normalizedCurrentDirAbsPath, sluggedTitle)
            );
            if (
                await fs.exists(path.join(potentialDirFromTitleAbs, "index.md"))
            ) {
                return normalizeDirPathToUrl(
                    potentialDirFromTitleAbs,
                    docsAbsPath,
                    lang
                );
            }
        }
        return null; // Group is not clickable if no explicit link and no target dir with index.md found
    }

    if (itemType === "directory") {
        // itemName here is the directory name itself.
        const directoryFullPathAbs = normalizePathSeparators(
            path.join(currentDirAbsPath, itemName)
        );
        const indexPath = path.join(directoryFullPathAbs, "index.md");
        
        const indexExists = await fs.exists(indexPath);
        
        if (indexExists) {
            const link = normalizeDirPathToUrl(
                directoryFullPathAbs,
                docsAbsPath,
                lang
            );
            return link;
        }
    }

    return null;
}

