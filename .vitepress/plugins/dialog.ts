import type { PluginSimple } from "markdown-it";
import type { RuleBlock } from "markdown-it/lib/parser_block.mjs";
import type { RuleCore } from "markdown-it/lib/parser_core.mjs";
import type { RuleInline } from "markdown-it/lib/parser_inline.mjs";
import type { RenderRule } from "markdown-it/lib/renderer.mjs";
import type StateBlock from "markdown-it/lib/rules_block/state_block.mjs";
import type StateCore from "markdown-it/lib/rules_core/state_core.mjs";
import type StateInline from "markdown-it/lib/rules_inline/state_inline.mjs";
import type Token from "markdown-it/lib/token.mjs";

// Dialog environment interface
interface DialogEnv {
    dialogs?: {
        definitions?: Record<string, DialogDefinition>;
        list?: DialogItem[];
    };
}

interface DialogDefinition {
    id: string;
    title?: string;
    props?: Record<string, any>;
    pages: Array<{
        title?: string;
        content: string;
        tokens?: Token[];
    }>;
}

interface DialogItem {
    id: string;
    definition: DialogDefinition;
    displayText: string;
    inlineProps?: Record<string, any>;
}

interface DialogStateBlock extends StateBlock {
    env: DialogEnv;
    tokens: Token[];
}

interface DialogStateInline extends StateInline {
    env: DialogEnv;
    tokens: Token[];
}

interface DialogStateCore extends StateCore {
    env: DialogEnv;
    tokens: Token[];
}

/**
 * Parse dialog pages from content - improved version
 */
function parseDialogPages(content: string, md: any, env: any): Array<{
    title?: string;
    content: string;
    tokens?: Token[];
}> {
    const pageContents = content.split(/^---\s*$/m);
    const pages: Array<{ title?: string; content: string; tokens?: Token[] }> = [];

    for (const pageContent of pageContents) {
        const trimmedContent = pageContent.trim();
        if (!trimmedContent) continue;

        const pageLines = trimmedContent.split('\n');
        let title: string | undefined;
        let content: string;

        // Check if first line is a title
        const firstLine = pageLines[0].trim();
        if (firstLine && !firstLine.match(/^[:#>*+-\[]/) && pageLines.length > 1) {
            title = firstLine;
            content = pageLines.slice(1).join('\n').trim();
        } else {
            content = trimmedContent;
        }

        if (content) {
            // Parse the markdown content into tokens for later rendering
            const tokens: Token[] = [];
            try {
                md.block.parse(content, md, env, tokens);
            } catch (error) {
                console.warn('Failed to parse dialog content:', error);
                // Fallback: create a simple paragraph token
                const paragraphOpen = new md.Token('paragraph_open', 'p', 1);
                const inline = new md.Token('inline', '', 0);
                inline.content = content;
                inline.children = [];
                const paragraphClose = new md.Token('paragraph_close', 'p', -1);
                tokens.push(paragraphOpen, inline, paragraphClose);
            }
            
            pages.push({ title, content, tokens });
        }
    }

    // If no pages found, treat entire content as single page
    if (pages.length === 0 && content.trim()) {
        const tokens: Token[] = [];
        try {
            md.block.parse(content.trim(), md, env, tokens);
        } catch (error) {
            console.warn('Failed to parse dialog content:', error);
            const paragraphOpen = new md.Token('paragraph_open', 'p', 1);
            const inline = new md.Token('inline', '', 0);
            inline.content = content.trim();
            inline.children = [];
            const paragraphClose = new md.Token('paragraph_close', 'p', -1);
            tokens.push(paragraphOpen, inline, paragraphClose);
        }
        pages.push({ content: content.trim(), tokens });
    }

    return pages;
}

/**
 * Parse dialog arguments with enhanced support
 */
function parseDialogArgs(argsStr: string): Record<string, any> {
    const args: Record<string, any> = {};

    if (!argsStr.trim()) return args;

    // Handle JSON-like configuration
    try {
        if (argsStr.trim().startsWith('{') && argsStr.trim().endsWith('}')) {
            return JSON.parse(argsStr);
        }
    } catch (error) {
        console.warn('Failed to parse JSON config:', argsStr, error);
        // Fall through to key-value parsing
    }

    // Handle key-value pairs like "title": "Dialog Title" "width": "500" "persistent": true
    const kvMatches = argsStr.matchAll(/"([^"]+)":\s*(?:"([^"]+)"|([a-zA-Z]+)|(\d+))/g);
    for (const match of kvMatches) {
        const key = match[1];
        const stringValue = match[2];
        const booleanValue = match[3];
        const numberValue = match[4];

        if (stringValue !== undefined) {
            args[key] = stringValue;
        } else if (booleanValue !== undefined) {
            args[key] = booleanValue === 'true';
        } else if (numberValue !== undefined) {
            args[key] = parseInt(numberValue, 10);
        }
    }

    return args;
}

/**
 * Block rule for dialog content definitions
 * Syntax: @@@ dialog-def#id {"title": "Dialog Title"}
 */
const dialogDef: RuleBlock = (state: DialogStateBlock, startLine, endLine, silent) => {
    const marker = "@@@";
    const markerCount = marker.length;
    let pos = state.bMarks[startLine] + state.tShift[startLine];
    const max = state.eMarks[startLine];

    // Check for @@@ marker
    if (pos + markerCount > max) return false;

    // Verify marker
    for (let i = 0; i < markerCount; i++) {
        if (marker.charCodeAt(i) !== state.src.charCodeAt(pos + i)) {
            return false;
        }
    }

    pos += markerCount;

    // Skip whitespace
    while (pos < max && state.src.charCodeAt(pos) === 0x20) pos++;

    // Check for dialog-def# syntax
    const afterMarker = state.src.slice(pos, max).trim();
    if (!afterMarker.startsWith('dialog-def#')) return false;

    // Parse ID and configuration
    const configStr = afterMarker.slice(11).trim(); // Remove "dialog-def#"
    let id = '';
    let configPart = '';

    // Extract ID (until space or end)
    let i = 0;
    while (i < configStr.length && configStr[i] !== ' ' && configStr[i] !== '{') {
        id += configStr[i];
        i++;
    }

    if (!id) return false;

    // Extract configuration if present
    if (i < configStr.length) {
        configPart = configStr.slice(i).trim();
    }

    if (silent) return true;

    // Find closing marker
    let nextLine = startLine;
    let autoClose = false;

    while (nextLine < endLine) {
        nextLine++;
        if (nextLine >= endLine) break;

        const lineStart = state.bMarks[nextLine] + state.tShift[nextLine];
        const lineMax = state.eMarks[nextLine];

        if (lineStart < lineMax && state.sCount[nextLine] < state.blkIndent) {
            break;
        }

        if (
            state.src.slice(lineStart, lineStart + markerCount) === marker &&
            state.src.slice(lineStart + markerCount, lineMax).trim() === ''
        ) {
            autoClose = true;
            break;
        }
    }

    if (!autoClose) return false;

    // Extract content
    const contentLines: string[] = [];
    const indentToTrim = state.sCount[startLine];
    for (let i = startLine + 1; i < nextLine; i++) {
        const lineStart = state.bMarks[i];
        const lineEnd = state.eMarks[i];
        const line = state.src.slice(lineStart, lineEnd);
        const lineIndent = state.sCount[i];

        if (lineIndent >= indentToTrim) {
            // Basic dedent, assumes spaces
            let pos = 0;
            while(pos < indentToTrim && line.charCodeAt(pos) === 0x20) {
                pos++;
            }
            contentLines.push(line.slice(pos));
        } else {
            contentLines.push(line);
        }
    }

    const fullContent = contentLines.join('\n');
    const props = parseDialogArgs(configPart);
    const pages = parseDialogPages(fullContent, state.md, state.env);

    // Store definition
    state.env.dialogs ??= {};
    state.env.dialogs.definitions ??= {};
    state.env.dialogs.definitions[id] = {
        id,
        props,
        pages,
        title: props.title
    };

    // console.log(`Defined dialog: ${id}`, { props, pageCount: pages.length });

    state.line = nextLine + (autoClose ? 1 : 0);
    return true;
};

/**
 * Inline rule for dialog triggers
 * Syntax: :::dialog#id{"variant": "button"}Display Text:::
 */
const dialogRef: RuleInline = (state: DialogStateInline, silent) => {
    const start = state.pos;
    const max = state.posMax;

    // Check for ::: dialog# syntax
    if (
        start + 10 >= max ||
        state.src.slice(start, start + 3) !== ':::'
    ) {
        return false;
    }

    // Look for dialog# after :::
    const afterMarker = state.src.slice(start + 3);
    if (!afterMarker.startsWith('dialog#')) {
        return false;
    }

    // Find closing :::
    let contentEnd = start + 3;
    let foundClose = false;
    
    while (contentEnd < max - 2) {
        if (state.src.slice(contentEnd, contentEnd + 3) === ':::') {
            foundClose = true;
            break;
        }
        contentEnd++;
    }

    if (!foundClose) return false;

    // Parse content: dialog#id{config}Display Text
    const inlineContent = state.src.slice(start + 3, contentEnd).trim();
    const contentAfterDialog = inlineContent.slice(7); // Remove "dialog#"
    
    // Extract ID and configuration
    let id = '';
    let configStr = '';
    let displayText = '';
    
    // Find where ID ends
    let i = 0;
    while (i < contentAfterDialog.length && 
           contentAfterDialog[i] !== '{' && 
           contentAfterDialog[i] !== ' ') {
        id += contentAfterDialog[i];
        i++;
    }

    if (!id) return false;

    // Parse configuration if present
    if (i < contentAfterDialog.length && contentAfterDialog[i] === '{') {
        let braceCount = 0;
        let configStart = i;
        
        while (i < contentAfterDialog.length) {
            if (contentAfterDialog[i] === '{') braceCount++;
            else if (contentAfterDialog[i] === '}') {
                braceCount--;
                if (braceCount === 0) {
                    configStr = contentAfterDialog.slice(configStart, i + 1);
                    i++;
                    break;
                }
            }
            i++;
        }
    }

    // Remaining text is display text
    displayText = contentAfterDialog.slice(i).trim();
    if (!displayText) displayText = 'Click here';

    if (silent) return true;

    // Parse inline configuration
    const inlineProps = configStr ? parseDialogArgs(configStr) : {};

    // Add to references list
    state.env.dialogs ??= {};
    state.env.dialogs.list ??= [];
    
    const dialogItem: DialogItem = {
        id,
        definition: state.env.dialogs.definitions?.[id] || { id, pages: [], props: {} },
        displayText,
        inlineProps
    };

    const refIndex = state.env.dialogs.list.length;
    state.env.dialogs.list.push(dialogItem);

    // console.log(`Dialog reference: ${id}`, { displayText, inlineProps });

    // Create reference token
    const refToken = state.push('dialog_ref', '', 0);
    refToken.meta = { refIndex, id, displayText, inlineProps };

    state.pos = contentEnd + 3;
    return true;
};

/**
 * Core rule to process dialog references and render components
 */
const dialogTail: RuleCore = (state: DialogStateCore): boolean => {
    if (!state.env.dialogs?.list?.length) return false;

    const { definitions } = state.env.dialogs;
    if (!definitions) return false;

    for (const blockToken of state.tokens) {
        if (blockToken.type !== 'inline' || !blockToken.children) {
            continue;
        }

        const newChildren: Token[] = [];
        let modified = false;

        for (const token of blockToken.children) {
        if (token.type === 'dialog_ref') {
                modified = true;
                const { id, displayText, inlineProps } = token.meta;
                const definition = definitions[id];

            if (!definition) {
                const errorToken = new state.Token('html_inline', '', 0);
                errorToken.content = `<span style="color: red;">[Dialog ${id} not found]</span>`;
                    newChildren.push(errorToken);
                continue;
            }

            const mergedProps = { ...definition.props, ...inlineProps };
            const isMultiPage = definition.pages.length > 1;
            const componentName = isMultiPage ? 'MdMultiPageDialog' : 'MdDialog';

            const propsStr = Object.entries(mergedProps)
                .map(([key, value]) => {
                        if (value === undefined || value === null) return '';
                    if (typeof value === 'boolean') {
                            return value ? ` ${key}` : ` :${key}="false"`;
                        }
                        if (typeof value === 'number') {
                            return ` :${key}="${value}"`;
                    }
                        return ` ${key}="${escapeHtml(String(value))}"`;
                })
                    .join('');

                const pageCountProp = isMultiPage ? ` :page-count="${definition.pages.length}"` : '';
                const openStr = `<${componentName} text="${escapeHtml(displayText)}"${propsStr}${pageCountProp}>`;

                let contentStr = '';
            if (isMultiPage) {
                definition.pages.forEach((page, pageIndex) => {
                        const content = page.content || '';
                        const downgradedContent = content.replace(/^(#+) /gm, (_match, hashes) => `${hashes}# `);
                        const renderedContent = state.md.render(downgradedContent, state.env);
                        contentStr += `<template #page${pageIndex + 1}>${renderedContent}</template>`;
                    });
                    } else {
                    const content = definition.pages[0]?.content || '';
                    const downgradedContent = content.replace(/^(#+) /gm, (_match, hashes) => `${hashes}# `);
                    contentStr = state.md.render(downgradedContent, state.env);
                }

                const closeStr = `</${componentName}>`;
                
                const componentToken = new state.Token('html_inline', '', 0);
                componentToken.content = openStr + contentStr + closeStr;
                newChildren.push(componentToken);

            } else {
                newChildren.push(token);
            }
        }

        if (modified) {
            blockToken.children = newChildren;
        }
    }

    return true;
};

/**
 * Escape HTML attributes safely
 */
function escapeHtml(value: string): string {
    return value
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

/**
 * Enhanced Dialog plugin for markdown-it
 * 
 * Features:
 * - Proper markdown content processing
 * - Token-based rendering like footnote plugin
 * - Support for both single and multi-page dialogs
 * - Inline configuration support
 * 
 * Usage:
 * 1. Define dialog content:
 * @@@ dialog-def#my-dialog {"title": "My Dialog", "fullscreen": true}
 * # Page 1
 * This is **markdown** content with full VitePress support.
 * 
 * ```javascript
 * console.log('Code highlighting works!');
 * ```
 * ---
 * # Page 2
 * Second page content
 * @@@
 * 
 * 2. Use dialog trigger:
 * Click :::dialog#my-dialog{"variant": "button"}this text::: to open dialog.
 */
export const dialogPlugin: PluginSimple = (md) => {
    // Register parsing rules
    md.block.ruler.before('paragraph', 'dialog_def', dialogDef, {
        alt: ['paragraph', 'reference', 'blockquote', 'list']
    });

    md.inline.ruler.before('text', 'dialog_ref', dialogRef);

    // Register core rule for processing dialog tokens
    md.core.ruler.after('inline', 'dialog_tail', dialogTail);
};
