import path from 'node:path';
import { SidebarItem, JsonFileMetadata } from '../types';
import { normalizePathSeparators, isDeepEqual } from '../shared/objectUtils';
import { JsonFileHandler, JsonOverrideFileType } from './JsonFileHandler';
import { MetadataManager } from './MetadataManager';
import { SyncEngine } from './SyncEngine';
import { JsonItemSorter } from './JsonItemSorter';
import { PathKeyProcessor } from './PathKeyProcessor';

/**
 * @file RecursiveSynchronizer.ts
 * @description Handles recursive synchronization of sidebar items with JSON overrides.
 */
export class RecursiveSynchronizer {
    private jsonFileHandler: JsonFileHandler;
    private metadataManager: MetadataManager;
    private syncEngine: SyncEngine;
    private jsonItemSorter: JsonItemSorter;
    private pathProcessor: PathKeyProcessor;
    private absDocsPath: string;

    constructor(
        jsonFileHandler: JsonFileHandler,
        metadataManager: MetadataManager,
        syncEngine: SyncEngine,
        jsonItemSorter: JsonItemSorter,
        absDocsPath: string
    ) {
        this.jsonFileHandler = jsonFileHandler;
        this.metadataManager = metadataManager;
        this.syncEngine = syncEngine;
        this.jsonItemSorter = jsonItemSorter;
        this.pathProcessor = new PathKeyProcessor();
        this.absDocsPath = absDocsPath;
    }

    /**
     * Recursively synchronizes an array of sidebar items and their children.
     * @param items The current array of SidebarItems to process.
     * @param currentConfigDirSignature Path signature for the parent's JSON config directory (e.g., '_root', 'guide/concepts').
     * @param lang The language code.
     * @param isDevMode Development mode flag.
     * @param langGitbookPaths Absolute paths to GitBook directories to exclude.
     * @param isTopLevelCall To know if items are direct children of the root view
     */
    public async synchronizeItemsRecursively(
        items: SidebarItem[],
        currentConfigDirSignature: string, 
        lang: string,
        isDevMode: boolean,
        langGitbookPaths: string[],
        isTopLevelCall: boolean = false
    ): Promise<void> {
        // Skip GitBook directories
        if (!isTopLevelCall && this.pathProcessor.isGitBookRoot(currentConfigDirSignature, lang, langGitbookPaths, this.absDocsPath)) {
            return;
        }

        console.log(`DEBUG: synchronizeItemsRecursively called for ${currentConfigDirSignature} with ${items.length} items`);

        // Check for flattened root structure (single root item containing all content)
        const isFlattenedRootStructure = isTopLevelCall && items.length === 1 && items[0]._isRoot && items[0].items && items[0].items.length > 0;

        if (isFlattenedRootStructure) {
            console.log(`DEBUG: Processing flattened root structure with ${items[0].items!.length} children`);

            // Process the root item's _self_ properties
            const rootItem = items[0];
            await this.processSelfProperties(rootItem, currentConfigDirSignature, lang, isDevMode);

            // Process the flattened children using the simplified approach
            await this.processDirectChildren(rootItem.items!, currentConfigDirSignature, lang, isDevMode);

            // Recursively process subdirectories
            await this.processSubdirectoriesRecursively(rootItem.items!, currentConfigDirSignature, lang, isDevMode, langGitbookPaths);

            // Apply order to the root item's children
            const orderData = await this.jsonFileHandler.readJsonFile('order', lang, currentConfigDirSignature);
            rootItem.items = this.jsonItemSorter.sortItems(rootItem.items!, orderData);
        } else {
            console.log(`DEBUG: Processing normal hierarchical structure with ${items.length} items`);

            // Process direct children at current level
            await this.processDirectChildren(items, currentConfigDirSignature, lang, isDevMode);

            // Recursively process subdirectories
            await this.processSubdirectoriesRecursively(items, currentConfigDirSignature, lang, isDevMode, langGitbookPaths);

            // Apply order to current level items
            const orderData = await this.jsonFileHandler.readJsonFile('order', lang, currentConfigDirSignature);
            const sortedItems = this.jsonItemSorter.sortItems(items, orderData);
            items.length = 0;
            items.push(...sortedItems);
        }

        // CRUCIAL: Clean up orphaned configurations after processing
        await this.cleanupOrphanedConfigurations(currentConfigDirSignature, lang, items);
            }

    /**
     * Recursively processes subdirectories, creating their _self_ properties and processing their children.
     */
    private async processSubdirectoriesRecursively(
        items: SidebarItem[],
        currentConfigDirSignature: string,
        lang: string,
        isDevMode: boolean,
        langGitbookPaths: string[]
    ): Promise<void> {
        for (const item of items) {
            // FIXED: Process ALL directories, even those with only files (no subdirectories)
            // This ensures that file-only directories like flandre/ get their JSON configs generated
            if (item._isDirectory) {
                // Calculate the next config directory signature
                const itemRelativeKey = this.pathProcessor.extractRelativeKeyForCurrentDir(item, currentConfigDirSignature);
                const nextConfigDirSignature = currentConfigDirSignature === '_root' 
                    ? itemRelativeKey.replace(/\/$/, '') // Remove trailing slash for root-level items
                    : normalizePathSeparators(path.join(currentConfigDirSignature, itemRelativeKey.replace(/\/$/, '')));

                console.log(`DEBUG: Processing directory: ${item.text} -> ${nextConfigDirSignature}`);
                console.log(`DEBUG: Directory has ${item.items?.length || 0} children:`, item.items?.map(child => ({
                    text: child.text,
                    _isDirectory: child._isDirectory,
                    _relativePathKey: child._relativePathKey,
                    hasItems: child.items ? child.items.length : 0
                })));

                // Special debugging for flandre and other file-only directories
                if (item.text === 'flandre' || nextConfigDirSignature.includes('flandre') || (!item.items || item.items.length === 0)) {
                    console.log(`🔍 SPECIAL DEBUG for file-only directory: ${item.text}`);
                    console.log(`  - Item text: "${item.text}"`);
                    console.log(`  - Item _relativePathKey: "${item._relativePathKey}"`);
                    console.log(`  - NextConfigDirSignature: "${nextConfigDirSignature}"`);
                    console.log(`  - Has ${item.items?.length || 0} children`);
                    console.log(`  - Is directory with files only: ${!item.items || item.items.length === 0}`);
                    
                    if (item.items && item.items.length > 0) {
                        console.log(`  - Children details:`, item.items.map(child => ({
                            text: child.text,
                            _isDirectory: child._isDirectory,
                            _relativePathKey: child._relativePathKey,
                            link: child.link,
                            _isRoot: child._isRoot
                        })));
                    }
                }

                // Skip GitBook directories
                    if (this.pathProcessor.isGitBookRoot(nextConfigDirSignature, lang, langGitbookPaths, this.absDocsPath)) {
                    console.log(`DEBUG: Skipping GitBook directory: ${nextConfigDirSignature}`);
                        continue; 
                    }

                // ALWAYS process _self_ properties for ALL directories (file-only or not)
                await this.processSelfProperties(item, nextConfigDirSignature, lang, isDevMode);

                // Process children if they exist (both files and subdirectories)
                if (item.items && item.items.length > 0) {
                    console.log(`DEBUG: About to recursively process ${item.items.length} children in directory: ${nextConfigDirSignature}`);
                    
                    // Add detailed debugging for the children
                    const childrenDetails = item.items.map(child => ({
                        text: child.text,
                        _isDirectory: child._isDirectory,
                        _relativePathKey: child._relativePathKey,
                        link: child.link
                    }));
                    console.log(`DEBUG: Children details:`, JSON.stringify(childrenDetails, null, 2));
                    
                    // This call will handle both direct children processing AND recursive subdirectory processing
                    await this.synchronizeItemsRecursively(item.items, nextConfigDirSignature, lang, isDevMode, langGitbookPaths, false);
                } else {
                    console.log(`DEBUG: Directory "${item.text}" has no children - file-only directory, _self_ properties processed`);
                }
                
                console.log(`DEBUG: Finished processing directory: ${nextConfigDirSignature}`);
            }
        }
                    }

    /**
     * Processes direct children of a directory for JSON config synchronization.
     * This method only handles immediate children, not nested content.
     */
    private async processDirectChildren(
        items: SidebarItem[],
        currentConfigDirSignature: string,
        lang: string,
        isDevMode: boolean
    ): Promise<void> {
        console.log(`DEBUG: processDirectChildren called for ${currentConfigDirSignature} with ${items.length} items`);
        
        // Add debugging to see what items we're processing
        const itemsDebug = items.map(item => ({
            text: item.text,
            _isDirectory: item._isDirectory,
            _relativePathKey: item._relativePathKey,
            link: item.link
        }));
        console.log(`DEBUG: Items to process:`, JSON.stringify(itemsDebug, null, 2));

        // Process locales (text overrides) for direct children
        console.log(`DEBUG: Processing locales for ${items.length} items...`);
        await this.processChildrenLocales(items, currentConfigDirSignature, lang, isDevMode);
        
        // Process collapsed states for direct children
        console.log(`DEBUG: Processing collapsed for ${items.length} items...`);
        await this.processChildrenCollapsed(items, currentConfigDirSignature, lang, isDevMode);
        
        // Process hidden states for direct children
        console.log(`DEBUG: Processing hidden for ${items.length} items...`);
        await this.processChildrenHidden(items, currentConfigDirSignature, lang, isDevMode);
        
        // Process order for direct children
        console.log(`DEBUG: Processing order for ${items.length} items...`);
        await this.processCurrentItemsOrder(items, currentConfigDirSignature, lang, isDevMode);
        
        console.log(`DEBUG: Finished processing all direct children for ${currentConfigDirSignature}`);
    }

    /**
     * Re-applies migrated values to ensure migrated values are reflected in the final sidebar.
     * This is also used as a final pass to ensure all JSON overrides are properly applied.
     * Priority: JSON configs > sub config > root config > global config
     */
    public async reapplyMigratedValues(
        items: SidebarItem[],
        rootConfigDirSignature: string,
        lang: string,
        langGitbookPaths: string[]
    ): Promise<void> {
        console.log(`DEBUG: reapplyMigratedValues called for ${rootConfigDirSignature} (${lang})`);
        
        // Apply overrides for current level items
        const localesData = await this.jsonFileHandler.readJsonFile('locales', lang, rootConfigDirSignature);
        const collapsedData = await this.jsonFileHandler.readJsonFile('collapsed', lang, rootConfigDirSignature);
        const hiddenData = await this.jsonFileHandler.readJsonFile('hidden', lang, rootConfigDirSignature);
        const orderData = await this.jsonFileHandler.readJsonFile('order', lang, rootConfigDirSignature);
        
        for (const item of items) {
            const itemKey = this.pathProcessor.extractRelativeKeyForCurrentDir(item, rootConfigDirSignature);
            
            // Special handling for flattened root structures
            if (item._isRoot && items.length === 1) {
                // This is a flattened root structure - apply _self_ override to the root section
                if (localesData.hasOwnProperty('_self_')) {
                    item.text = localesData['_self_'];
                }
                if (collapsedData.hasOwnProperty('_self_')) {
                    item.collapsed = collapsedData['_self_'];
                }
                
                // Recursively apply to children
                if (item.items && item.items.length > 0) {
                    await this.reapplyMigratedValues(item.items, rootConfigDirSignature, lang, langGitbookPaths);
                }
            } else {
                // Normal hierarchical structure processing
                
                // STEP 1: Apply parent-level overrides (HIGHEST PRIORITY)
                // These take absolute precedence over anything else
                let parentOverrideApplied = {
                    text: false,
                    collapsed: false,
                    hidden: false
                };
                
                // Apply locales override from parent if it exists
                if (localesData.hasOwnProperty(itemKey)) {
                    item.text = localesData[itemKey];
                    parentOverrideApplied.text = true;
                    console.log(`DEBUG: Applied parent text override for "${itemKey}": "${localesData[itemKey]}"`);
                }
                
                // Apply collapsed override from parent if it exists
                if (collapsedData.hasOwnProperty(itemKey) && item._isDirectory) {
                    item.collapsed = collapsedData[itemKey];
                    parentOverrideApplied.collapsed = true;
                    console.log(`DEBUG: Applied parent collapsed override for "${itemKey}": ${collapsedData[itemKey]}`);
                }
                
                // Apply hidden override from parent if it exists
                if (hiddenData.hasOwnProperty(itemKey)) {
                    (item as any)._hidden = hiddenData[itemKey];
                    parentOverrideApplied.hidden = true;
                } else {
                    (item as any)._hidden = false;
                }
                
                // STEP 2: If this item is a directory, process its children and apply _self_ values ONLY if no parent override
                if (item._isDirectory && item.items && item.items.length > 0) {
                    const nextConfigDirSignature = rootConfigDirSignature === '_root' 
                        ? itemKey.replace(/\/$/, '') 
                        : normalizePathSeparators(path.join(rootConfigDirSignature, itemKey.replace(/\/$/, '')));
                    
                    // Skip GitBook directories
                    if (!this.pathProcessor.isGitBookRoot(nextConfigDirSignature, lang, langGitbookPaths, this.absDocsPath)) {
                        
                        // Apply _self_ text value ONLY if parent didn't override it
                        if (!parentOverrideApplied.text) {
                        const itemLocalesData = await this.jsonFileHandler.readJsonFile('locales', lang, nextConfigDirSignature);
                        if (itemLocalesData.hasOwnProperty('_self_')) {
                            item.text = itemLocalesData['_self_'];
                                console.log(`DEBUG: Applied _self_ text for "${itemKey}": "${itemLocalesData['_self_']}" (no parent override)`);
                            }
                        } else {
                            console.log(`DEBUG: Skipped _self_ text for "${itemKey}" (parent override takes priority)`);
                        }
                        
                        // Apply _self_ collapsed value ONLY if parent didn't override it
                        if (!parentOverrideApplied.collapsed) {
                        const itemCollapsedData = await this.jsonFileHandler.readJsonFile('collapsed', lang, nextConfigDirSignature);
                        if (itemCollapsedData.hasOwnProperty('_self_')) {
                            item.collapsed = itemCollapsedData['_self_'];
                                console.log(`DEBUG: Applied _self_ collapsed for "${itemKey}": ${itemCollapsedData['_self_']} (no parent override)`);
                            }
                        } else {
                            console.log(`DEBUG: Skipped _self_ collapsed for "${itemKey}" (parent override takes priority)`);
                        }
                        
                        // Recursively apply to children
                            await this.reapplyMigratedValues(item.items, nextConfigDirSignature, lang, langGitbookPaths);
                            
                            // Apply order to children after all other overrides
                            const childOrderData = await this.jsonFileHandler.readJsonFile('order', lang, nextConfigDirSignature);
                            item.items = this.jsonItemSorter.sortItems(item.items, childOrderData);
                    }
                }
            }
        }
        
        // Apply order to current level items last (only for non-flattened structures)
        if (items.length > 0 && !(items.length === 1 && items[0]._isRoot)) {
            const sortedItems = this.jsonItemSorter.sortItems(items, orderData);
            items.length = 0;
            items.push(...sortedItems);
        }

        // Filter out hidden items as the final step
        const isFlattened = items.length === 1 && items[0]._isRoot;
        if (isFlattened && items[0].items) {
            // For flattened structures, filter the children
            await this.filterHiddenItems(items[0].items, rootConfigDirSignature, lang, true);
        } else {
            // For normal structures, filter the current items
            await this.filterHiddenItems(items, rootConfigDirSignature, lang, false);
        }
    }

    /**
     * Applies hidden states to items in the sidebar structure.
     * Since hidden values are now correctly applied by applyHierarchyAwareOverrides with proper hierarchy awareness,
     * this method simply checks the _hidden property that was already set.
     */
    private async filterHiddenItems(
        items: SidebarItem[],
        currentConfigDirSignature: string,
        lang: string,
        isFlattened: boolean = false
    ): Promise<void> {
        // For flattened structures, hidden values are already applied by applyHierarchyAwareOverrides
        // For normal structures, we still need to apply hidden values using the appropriate config
        if (!isFlattened) {
            // Only apply hidden data for non-flattened structures
        const hiddenData = await this.jsonFileHandler.readJsonFile('hidden', lang, currentConfigDirSignature);

        for (let i = 0; i < items.length; i++) {
            const item = items[i];
                const itemKey = this.pathProcessor.extractRelativeKeyForCurrentDir(item, currentConfigDirSignature);

                // Apply hidden state from the appropriate config
            if (hiddenData[itemKey] === true) {
                (item as any)._hidden = true;
            } else {
                (item as any)._hidden = false;
            }
            
            // Recursively apply hidden states to children if they exist
            if (item.items && item.items.length > 0) {
                if (item._isDirectory) {
                    // For directories, use their own config directory signature
                    const nextConfigDirSignature = currentConfigDirSignature === '_root' 
                        ? itemKey 
                        : normalizePathSeparators(path.join(currentConfigDirSignature, itemKey));
                    await this.filterHiddenItems(item.items, nextConfigDirSignature, lang, false);
                } else {
                    // For non-directories with children, continue with current config
                        await this.filterHiddenItems(item.items, currentConfigDirSignature, lang, false);
    }
                }
            }
        }
        // For flattened structures, _hidden is already set by applyHierarchyAwareOverrides
        // No additional processing needed since hierarchy-aware approach was already applied
    }

    /**
     * Processes children's locales (text overrides).
     */
    private async processChildrenLocales(
        items: SidebarItem[],
        currentConfigDirSignature: string,
        lang: string,
        isDevMode: boolean
    ): Promise<void> {
        console.log(`DEBUG: processChildrenLocales - Processing ${items.length} items for ${currentConfigDirSignature}`);

        const parentLocalesData = await this.jsonFileHandler.readJsonFile('locales', lang, currentConfigDirSignature);
        const parentLocalesMetadata = await this.metadataManager.readMetadata('locales', lang, currentConfigDirSignature);

        console.log(`DEBUG: processChildrenLocales - Existing JSON has ${Object.keys(parentLocalesData).length} entries`);

        const localeSyncResultForChildren = await this.syncEngine.syncOverrideType(
            items, 
            parentLocalesData, 
            parentLocalesMetadata, 
            'locales', lang, isDevMode,
            (childItem: SidebarItem) => this.pathProcessor.extractRelativeKeyForCurrentDir(childItem, currentConfigDirSignature),
            this.metadataManager
        );
        
        // CONSERVATIVE: Only write if there are actual changes
        const hasJsonChanges = !isDeepEqual(parentLocalesData, localeSyncResultForChildren.updatedJsonData);
        const hasMetadataChanges = !isDeepEqual(parentLocalesMetadata, localeSyncResultForChildren.updatedMetadata);

        if (hasJsonChanges) {
        await this.jsonFileHandler.writeJsonFile('locales', lang, currentConfigDirSignature, localeSyncResultForChildren.updatedJsonData);
            console.log(`✅ processChildrenLocales - Updated locales.json for ${currentConfigDirSignature} (new entries added)`);
        } else {
            console.log(`⚪ processChildrenLocales - No JSON changes for ${currentConfigDirSignature} (all entries preserved)`);
        }

        if (hasMetadataChanges) {
        await this.metadataManager.writeMetadata('locales', lang, currentConfigDirSignature, localeSyncResultForChildren.updatedMetadata);
            console.log(`✅ processChildrenLocales - Updated metadata for ${currentConfigDirSignature}`);
        } else {
            console.log(`⚪ processChildrenLocales - No metadata changes for ${currentConfigDirSignature}`);
        }
    }

    /**
     * Processes children's collapsed states.
     */
    private async processChildrenCollapsed(
        items: SidebarItem[],
        currentConfigDirSignature: string,
        lang: string,
        isDevMode: boolean
    ): Promise<void> {
        const parentCollapsedData = await this.jsonFileHandler.readJsonFile('collapsed', lang, currentConfigDirSignature);
        const parentCollapsedMetadata = await this.metadataManager.readMetadata('collapsed', lang, currentConfigDirSignature);

        const collapsedSyncResultForChildren = await this.syncEngine.syncOverrideType(
            items, 
            parentCollapsedData, 
            parentCollapsedMetadata, 
            'collapsed', lang, isDevMode,
            (childItem: SidebarItem) => this.pathProcessor.extractRelativeKeyForCurrentDir(childItem, currentConfigDirSignature),
            this.metadataManager
        );
        
        // CONSERVATIVE: Only write if there are actual changes
        const hasJsonChanges = !isDeepEqual(parentCollapsedData, collapsedSyncResultForChildren.updatedJsonData);
        const hasMetadataChanges = !isDeepEqual(parentCollapsedMetadata, collapsedSyncResultForChildren.updatedMetadata);

        if (hasJsonChanges) {
        await this.jsonFileHandler.writeJsonFile('collapsed', lang, currentConfigDirSignature, collapsedSyncResultForChildren.updatedJsonData);
        }
        if (hasMetadataChanges) {
        await this.metadataManager.writeMetadata('collapsed', lang, currentConfigDirSignature, collapsedSyncResultForChildren.updatedMetadata);
        }
    }

    /**
     * Processes children's hidden states.
     */
    private async processChildrenHidden(
        items: SidebarItem[],
        currentConfigDirSignature: string,
        lang: string,
        isDevMode: boolean
    ): Promise<void> {
        const parentHiddenData = await this.jsonFileHandler.readJsonFile('hidden', lang, currentConfigDirSignature);
        const parentHiddenMetadata = await this.metadataManager.readMetadata('hidden', lang, currentConfigDirSignature);

        const hiddenSyncResultForChildren = await this.syncEngine.syncOverrideType(
            items, 
            parentHiddenData, 
            parentHiddenMetadata, 
            'hidden', lang, isDevMode,
            (childItem: SidebarItem) => this.pathProcessor.extractRelativeKeyForCurrentDir(childItem, currentConfigDirSignature),
            this.metadataManager
        );
        
        // CONSERVATIVE: Only write if there are actual changes
        const hasJsonChanges = !isDeepEqual(parentHiddenData, hiddenSyncResultForChildren.updatedJsonData);
        const hasMetadataChanges = !isDeepEqual(parentHiddenMetadata, hiddenSyncResultForChildren.updatedMetadata);

        if (hasJsonChanges) {
        await this.jsonFileHandler.writeJsonFile('hidden', lang, currentConfigDirSignature, hiddenSyncResultForChildren.updatedJsonData);
        }
        if (hasMetadataChanges) {
        await this.metadataManager.writeMetadata('hidden', lang, currentConfigDirSignature, hiddenSyncResultForChildren.updatedMetadata);
        }
    }

    /**
     * Processes _self_ properties for a directory item.
     * This method is CONSERVATIVE - it only creates _self_ entries if they don't exist,
     * and never modifies existing user configurations.
     */
    private async processSelfProperties(
        currentItem: SidebarItem,
        nextConfigDirSignature: string,
        lang: string,
        isDevMode: boolean
    ): Promise<void> {
        const selfOverrideTypes: JsonOverrideFileType[] = ['locales', 'collapsed', 'hidden'];
        
        for (const type of selfOverrideTypes) {
            const itemOwnJsonData = await this.jsonFileHandler.readJsonFile(type, lang, nextConfigDirSignature);
            const itemOwnMetadata = await this.metadataManager.readMetadata(type, lang, nextConfigDirSignature);
            
            // If _self_ already exists in the JSON, NEVER touch it
            if (itemOwnJsonData.hasOwnProperty('_self_')) {
                console.log(`⚪ processSelfProperties - Preserved existing _self_ for ${type} in ${nextConfigDirSignature}`);
                continue;
            }

            // Only create _self_ entry if it doesn't exist
            let defaultSelfValue: any;
            if (type === 'locales') {
                defaultSelfValue = currentItem.text;
            } else if (type === 'collapsed') {
                defaultSelfValue = currentItem.collapsed ?? true;
            } else if (type === 'hidden') {
                defaultSelfValue = false;
            }

            if (defaultSelfValue !== undefined) {
                const updatedJsonData = { ...itemOwnJsonData, '_self_': defaultSelfValue };
                const updatedMetadata = { 
                    ...itemOwnMetadata, 
                    '_self_': this.metadataManager.createNewMetadataEntry(defaultSelfValue, false, true) 
                };

                // CONSERVATIVE: Only write if _self_ was actually added
                await this.jsonFileHandler.writeJsonFile(type, lang, nextConfigDirSignature, updatedJsonData);
                await this.metadataManager.writeMetadata(type, lang, nextConfigDirSignature, updatedMetadata);
                
                console.log(`✅ processSelfProperties - Added missing _self_ for ${type} in ${nextConfigDirSignature}: "${defaultSelfValue}"`);
            }
        }
    }

    /**
     * Processes order for children of a directory item.
     */
    private async processChildrenOrder(
        children: SidebarItem[],
        configDirSignature: string,
        lang: string,
        isDevMode: boolean
    ): Promise<void> {
        const itemOwnOrderJson = await this.jsonFileHandler.readJsonFile('order', lang, configDirSignature);
        const itemOwnOrderMetadata = await this.metadataManager.readMetadata('order', lang, configDirSignature);
        
        const orderSyncResult = await this.syncEngine.syncOverrideType(
            children, itemOwnOrderJson, itemOwnOrderMetadata, 'order', lang, isDevMode,
            (childItem: SidebarItem) => this.pathProcessor.extractRelativeKeyForCurrentDir(childItem, configDirSignature),
            this.metadataManager
        );
        
        // CONSERVATIVE: Only write if there are actual changes
        const hasJsonChanges = !isDeepEqual(itemOwnOrderJson, orderSyncResult.updatedJsonData);
        const hasMetadataChanges = !isDeepEqual(itemOwnOrderMetadata, orderSyncResult.updatedMetadata);

        if (hasJsonChanges) {
        await this.jsonFileHandler.writeJsonFile('order', lang, configDirSignature, orderSyncResult.updatedJsonData);
        }
        if (hasMetadataChanges) {
        await this.metadataManager.writeMetadata('order', lang, configDirSignature, orderSyncResult.updatedMetadata);
        }
    }

    /**
     * Processes order for the current list of items.
     */
    private async processCurrentItemsOrder(
        items: SidebarItem[],
        currentConfigDirSignature: string,
        lang: string,
        isDevMode: boolean
    ): Promise<void> {
        const parentOrderJsonData = await this.jsonFileHandler.readJsonFile('order', lang, currentConfigDirSignature);
        const parentOrderMetadata = await this.metadataManager.readMetadata('order', lang, currentConfigDirSignature);

        const orderSyncResultForCurrentItems = await this.syncEngine.syncOverrideType(
            items, 
            parentOrderJsonData, 
            parentOrderMetadata, 
            'order', lang, isDevMode, 
            (item: SidebarItem) => this.pathProcessor.extractRelativeKeyForCurrentDir(item, currentConfigDirSignature), 
            this.metadataManager
        );
        
        // CONSERVATIVE: Only write if there are actual changes
        const hasJsonChanges = !isDeepEqual(parentOrderJsonData, orderSyncResultForCurrentItems.updatedJsonData);
        const hasMetadataChanges = !isDeepEqual(parentOrderMetadata, orderSyncResultForCurrentItems.updatedMetadata);

        if (hasJsonChanges) {
        await this.jsonFileHandler.writeJsonFile('order', lang, currentConfigDirSignature, orderSyncResultForCurrentItems.updatedJsonData);
        }
        if (hasMetadataChanges) {
        await this.metadataManager.writeMetadata('order', lang, currentConfigDirSignature, orderSyncResultForCurrentItems.updatedMetadata);
        }
        
        // Apply sorting regardless of whether we wrote new files
        const sortedItems = this.jsonItemSorter.sortItems(items, orderSyncResultForCurrentItems.updatedJsonData);
        items.length = 0;
        items.push(...sortedItems);
    }

    /**
     * Cleans up orphaned configuration directories and JSON entries that don't correspond to physical directories.
     * This ensures the config structure stays in sync with the actual file structure.
     */
    private async cleanupOrphanedConfigurations(
        currentConfigDirSignature: string,
        lang: string,
        items: SidebarItem[]
    ): Promise<void> {
        console.log(`DEBUG: Cleaning up orphaned configurations for ${currentConfigDirSignature}`);

        try {
            // Get the config directory path
            const configDirPath = currentConfigDirSignature === '_root'
                ? normalizePathSeparators(path.join(this.absDocsPath, '..', '.vitepress', 'config', 'sidebar', lang))
                : normalizePathSeparators(path.join(this.absDocsPath, '..', '.vitepress', 'config', 'sidebar', lang, currentConfigDirSignature));

            // Get the metadata directory path
            const metadataDirPath = currentConfigDirSignature === '_root'
                ? normalizePathSeparators(path.join(this.absDocsPath, '..', '.vitepress', 'config', 'sidebar', '.metadata', lang))
                : normalizePathSeparators(path.join(this.absDocsPath, '..', '.vitepress', 'config', 'sidebar', '.metadata', lang, currentConfigDirSignature));

            // Get the physical directory path
            const physicalDirPath = currentConfigDirSignature === '_root'
                ? normalizePathSeparators(path.join(this.absDocsPath, lang))
                : normalizePathSeparators(path.join(this.absDocsPath, lang, currentConfigDirSignature));

            // Get list of actual physical directories
            const physicalDirectories = new Set<string>();
            if (await this.jsonFileHandler.getFileSystem().exists(physicalDirPath)) {
                const physicalItems = await this.jsonFileHandler.getFileSystem().readDir(physicalDirPath);
                for (const item of physicalItems) {
                    const itemName = typeof item === 'string' ? item : item.name;
                    const itemPath = normalizePathSeparators(path.join(physicalDirPath, itemName));
                    const stat = await this.jsonFileHandler.getFileSystem().stat(itemPath);
                    
                    if (stat.isDirectory()) {
                        physicalDirectories.add(itemName);
                    }
                }
            }

            console.log(`DEBUG: Found ${physicalDirectories.size} physical directories:`, Array.from(physicalDirectories));

            // Collect orphaned directories from both config and metadata
            const orphanedDirectories = new Set<string>();

            // Check config directories
            if (await this.jsonFileHandler.getFileSystem().exists(configDirPath)) {
                const configItems = await this.jsonFileHandler.getFileSystem().readDir(configDirPath);
                for (const item of configItems) {
                    const itemName = typeof item === 'string' ? item : item.name;

                    // Skip JSON files and hidden directories
                    if (itemName.endsWith('.json') || itemName.startsWith('.')) {
                        continue;
                    }

                    const itemPath = normalizePathSeparators(path.join(configDirPath, itemName));
                    const stat = await this.jsonFileHandler.getFileSystem().stat(itemPath);
                    
                    if (stat.isDirectory() && !physicalDirectories.has(itemName)) {
                        orphanedDirectories.add(itemName);
                    }
                }
            }

            // Check metadata directories
            if (await this.jsonFileHandler.getFileSystem().exists(metadataDirPath)) {
                const metadataItems = await this.jsonFileHandler.getFileSystem().readDir(metadataDirPath);
                for (const item of metadataItems) {
                    const itemName = typeof item === 'string' ? item : item.name;
                    
                    // Skip JSON files and hidden directories
                    if (itemName.endsWith('.json') || itemName.startsWith('.')) {
                        continue;
                    }

                    const itemPath = normalizePathSeparators(path.join(metadataDirPath, itemName));
                    const stat = await this.jsonFileHandler.getFileSystem().stat(itemPath);
                    
                    if (stat.isDirectory() && !physicalDirectories.has(itemName)) {
                        orphanedDirectories.add(itemName);
                    }
                }
            }

            // Archive all orphaned directories (both config and metadata)
            for (const orphanedDirName of orphanedDirectories) {
                console.log(`CLEANUP: Found orphaned directory: ${orphanedDirName}`);
                await this.archiveOrphanedConfigAndMetadata(configDirPath, metadataDirPath, currentConfigDirSignature, lang, orphanedDirName);
            }

            // Clean up orphaned JSON entries
            await this.cleanupOrphanedJsonEntries(currentConfigDirSignature, lang, physicalDirectories);

        } catch (error) {
            console.warn(`Error during cleanup for ${currentConfigDirSignature}:`, error);
        }
    }

    /**
     * Archives both the config and metadata directories for an orphaned directory.
     */
    private async archiveOrphanedConfigAndMetadata(
        configDirPath: string,
        metadataDirPath: string,
        currentConfigDirSignature: string,
        lang: string,
        dirName: string
    ): Promise<void> {
        try {
            const timestamp = new Date().toISOString().split('T')[0];
            
            // Create archive directory structure
            const archiveBasePath = normalizePathSeparators(path.join(
                this.absDocsPath, '..', '.vitepress', 'config', 'sidebar', '.archive',
                'removed_directories',
                `${dirName}_removed_${timestamp}`
            ));

            // Create archive directories
            await this.jsonFileHandler.getFileSystem().ensureDir(archiveBasePath);
            
            const configArchivePath = normalizePathSeparators(path.join(archiveBasePath, 'config'));
            const metadataArchivePath = normalizePathSeparators(path.join(archiveBasePath, 'metadata'));
            
            await this.jsonFileHandler.getFileSystem().ensureDir(configArchivePath);
            await this.jsonFileHandler.getFileSystem().ensureDir(metadataArchivePath);

            // Use Node.js fs directly for the move operations
            const fs = await import('node:fs/promises');
            
            // Archive config directory if it exists
            const sourceConfigPath = normalizePathSeparators(path.join(configDirPath, dirName));
            if (await this.jsonFileHandler.getFileSystem().exists(sourceConfigPath)) {
                const targetConfigPath = normalizePathSeparators(path.join(configArchivePath, dirName));
                
                try {
                    await fs.rename(sourceConfigPath, targetConfigPath);
                } catch (renameError) {
                    await fs.cp(sourceConfigPath, targetConfigPath, { recursive: true });
                    await this.jsonFileHandler.getFileSystem().deleteDir(sourceConfigPath);
                }
                
                console.log(`✅ Archived config directory: ${dirName} -> ${targetConfigPath}`);
            }

            // Archive metadata directory if it exists
            const sourceMetadataPath = normalizePathSeparators(path.join(metadataDirPath, dirName));
            if (await this.jsonFileHandler.getFileSystem().exists(sourceMetadataPath)) {
                const targetMetadataPath = normalizePathSeparators(path.join(metadataArchivePath, dirName));
                
                try {
                    await fs.rename(sourceMetadataPath, targetMetadataPath);
                } catch (renameError) {
                    await fs.cp(sourceMetadataPath, targetMetadataPath, { recursive: true });
                    await this.jsonFileHandler.getFileSystem().deleteDir(sourceMetadataPath);
                }
                
                console.log(`✅ Archived metadata directory: ${dirName} -> ${targetMetadataPath}`);
            }

            // Create a comprehensive README in the archive
            const readmePath = normalizePathSeparators(path.join(archiveBasePath, 'README.md'));
            const readmeContent = `# Archived Directory: ${dirName}

**Archive Date**: ${new Date().toISOString()}
**Original Location**: ${currentConfigDirSignature}/${dirName}
**Reason**: Physical directory no longer exists in docs structure

## Contents
This archive contains both the configuration files and metadata for a directory that was removed from the physical docs structure.

- \`config/\` - Contains the JSON configuration files (locales.json, order.json, etc.)
- \`metadata/\` - Contains the metadata files tracking configuration history

## Restoration
To restore this directory:

1. **Recreate the physical directory**: 
   \`mkdir -p docs/${lang}/${currentConfigDirSignature}/${dirName}/\`

2. **Restore configuration files**:
   \`cp -r config/${dirName}/ .vitepress/config/sidebar/${lang}/${currentConfigDirSignature}/${dirName}/\`

3. **Restore metadata files**:
   \`cp -r metadata/${dirName}/ .vitepress/config/sidebar/.metadata/${lang}/${currentConfigDirSignature}/${dirName}/\`

4. **Restart the development server**

## Archive Structure
\`\`\`
${dirName}_removed_${timestamp}/
├── README.md (this file)
├── config/${dirName}/     # Original config files
└── metadata/${dirName}/   # Original metadata files
\`\`\`
`;

            await this.jsonFileHandler.getFileSystem().writeFile(readmePath, readmeContent);
            
            console.log(`🎯 Completely archived orphaned directory: ${dirName}`);

        } catch (error) {
            console.error(`Failed to archive config and metadata for directory ${dirName}:`, error);
        }
    }

    /**
     * Cleans up orphaned entries in JSON files that don't correspond to physical directories.
     */
    private async cleanupOrphanedJsonEntries(
        currentConfigDirSignature: string,
        lang: string,
        physicalDirectories: Set<string>
    ): Promise<void> {
        const overrideTypes: JsonOverrideFileType[] = ['locales', 'collapsed', 'hidden', 'order'];

        for (const type of overrideTypes) {
            try {
                const currentData = await this.jsonFileHandler.readJsonFile(type, lang, currentConfigDirSignature);
                const updatedData = { ...currentData };
                let hasChanges = false;

                // Check each JSON entry
                for (const [key, value] of Object.entries(currentData)) {
                    // NEVER remove _self_ entries - they are directory self-references
                    if (key === '_self_') {
                        continue;
                    }

                    // Extract directory name from key (remove trailing slash)
                    const dirName = key.endsWith('/') ? key.slice(0, -1) : key.replace(/\.(md|html)$/, '');
                    
                    // Check if this entry corresponds to a physical directory
                    if (key.endsWith('/') && !physicalDirectories.has(dirName)) {
                        console.log(`CLEANUP: Removing orphaned JSON entry "${key}" from ${type}.json`);
                        delete updatedData[key];
                        hasChanges = true;
                    }
                }

                // Write updated data if changes were made
                if (hasChanges) {
                    console.log(`✅ Cleaned ${type}.json for ${currentConfigDirSignature}`);
                    await this.jsonFileHandler.writeJsonFile(type, lang, currentConfigDirSignature, updatedData);
                }

            } catch (error) {
                console.warn(`Error cleaning ${type}.json for ${currentConfigDirSignature}:`, error);
            }
        }
    }
} 

