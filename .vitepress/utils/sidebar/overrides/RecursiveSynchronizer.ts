import path from 'node:path';
import { SidebarItem, JsonFileMetadata } from '../types';
import { normalizePathSeparators } from '../shared/objectUtils';
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


        // Step 0: If currentConfigDirSignature points to a GitBook path, none of its items get JSON processing.
        if (!isTopLevelCall && this.pathProcessor.isGitBookRoot(currentConfigDirSignature, lang, langGitbookPaths, this.absDocsPath)) {
            // Children of a GitBook root are not further processed for JSON overrides by this system.
            return;
        }

        // Check for flattened root structure
        const isFlattenedRootStructure = isTopLevelCall && items.length === 1 && items[0]._isRoot && items[0].items && items[0].items.length > 0;

        if (isFlattenedRootStructure) {
            // Handle flattened structure: process all flattened items at root level
            const rootItem = items[0];
            const flattenedItems = rootItem.items!;

            // Process the root item's _self_ properties
            await this.processSelfProperties(rootItem, currentConfigDirSignature, lang, isDevMode);

            // Process all flattened items using root config
            await this.processFlattenedItemsLocales(flattenedItems, currentConfigDirSignature, lang, isDevMode);
            await this.processFlattenedItemsCollapsed(flattenedItems, currentConfigDirSignature, lang, isDevMode);
            await this.processFlattenedItemsHidden(flattenedItems, currentConfigDirSignature, lang, isDevMode);
            await this.processFlattenedItemsOrder(flattenedItems, currentConfigDirSignature, lang, isDevMode);

            // Apply order to flattened items
            rootItem.items = this.jsonItemSorter.sortItems(flattenedItems, 
                await this.jsonFileHandler.readJsonFile('order', lang, currentConfigDirSignature));


            if (currentConfigDirSignature.includes('modpack/kubejs/1.20.1')) {

                const upgradeItem = rootItem.items.find(item => item._relativePathKey === 'Upgrade/');

                if (upgradeItem) {

                }
            }

            // Step 2: Process nested directories recursively
            for (const currentItem of flattenedItems) {
                if (currentItem._isDirectory && currentItem.items && currentItem.items.length > 0) {
                    // Calculate the next config directory signature for nested processing
                    const nextConfigDirSignature = normalizePathSeparators(
                        path.join(currentConfigDirSignature, currentItem._relativePathKey || '')
                    );

                    // Check if this nested directory is a GitBook root
                    if (this.pathProcessor.isGitBookRoot(nextConfigDirSignature, lang, langGitbookPaths, this.absDocsPath)) {
                        continue; // Skip GitBook directories
                    }

                    // Recursively process this nested directory's items
                    await this.synchronizeItemsRecursively(currentItem.items, nextConfigDirSignature, lang, isDevMode, langGitbookPaths);
                }
            }

            // NOTE: Hidden filtering moved to reapplyMigratedValues() to ensure config generation happens first
        } else {
            // Normal hierarchical structure processing
            // Step 1: Process children's text, collapsed, and order for current items
            if (items && items.length > 0) {
                await this.processChildrenLocales(items, currentConfigDirSignature, lang, isDevMode);
                await this.processChildrenCollapsed(items, currentConfigDirSignature, lang, isDevMode);
                await this.processChildrenHidden(items, currentConfigDirSignature, lang, isDevMode);
                await this.processCurrentItemsOrder(items, currentConfigDirSignature, lang, isDevMode);
            }

            // Step 2: Iterate through items and process directories
            for (const currentItem of items) {
                const itemKeyPart = this.pathProcessor.extractRelativeKeyForCurrentDir(currentItem, currentConfigDirSignature);
                const nextConfigDirSignature = currentConfigDirSignature === '_root' 
                    ? itemKeyPart 
                    : normalizePathSeparators(path.join(currentConfigDirSignature, itemKeyPart));

                if (currentItem._isDirectory || (currentItem.items && currentItem.items.length > 0)) {
                    // Check if THIS item itself is a GitBook root path.
                    if (this.pathProcessor.isGitBookRoot(nextConfigDirSignature, lang, langGitbookPaths, this.absDocsPath)) {
                        continue; 
                    }

                    // Sync _self_ properties for this directory/group item
                    await this.processSelfProperties(currentItem, nextConfigDirSignature, lang, isDevMode);

                    // Recursively call for children of this directory/group
                    if (currentItem.items && currentItem.items.length > 0) {
                        await this.synchronizeItemsRecursively(currentItem.items, nextConfigDirSignature, lang, isDevMode, langGitbookPaths);
                    }

                    // Process 'order' for the children of this directory/group item (redundant but kept for safety)
                    if (currentItem.items && currentItem.items.length > 0) {
                        await this.processChildrenOrder(currentItem.items, nextConfigDirSignature, lang, isDevMode);
                        currentItem.items = this.jsonItemSorter.sortItems(currentItem.items, 
                            await this.jsonFileHandler.readJsonFile('order', lang, nextConfigDirSignature));
                    }
                }
            }

            // NOTE: Hidden filtering moved to reapplyMigratedValues() to ensure config generation happens first
        }
    }

    /**
     * Re-applies migrated values to ensure migrated values are reflected in the final sidebar.
     * This is also used as a final pass to ensure all JSON overrides are properly applied.
     */
    public async reapplyMigratedValues(
        items: SidebarItem[],
        rootConfigDirSignature: string,
        lang: string,
        langGitbookPaths: string[]
    ): Promise<void> {
        // Apply overrides for current level items
        const localesData = await this.jsonFileHandler.readJsonFile('locales', lang, rootConfigDirSignature);
        const collapsedData = await this.jsonFileHandler.readJsonFile('collapsed', lang, rootConfigDirSignature);
        const hiddenData = await this.jsonFileHandler.readJsonFile('hidden', lang, rootConfigDirSignature);
        const orderData = await this.jsonFileHandler.readJsonFile('order', lang, rootConfigDirSignature);
        
        // Debug logging for flattened structure detection


        if (items.length > 0) {



        }

        
        // Special check for our target section
        if (rootConfigDirSignature.includes('modpack/kubejs/1.20.1')) {


        }
        
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
                
                // For flattened structure, apply overrides to children using root config
                if (item.items && item.items.length > 0) {

                    await this.applyFlattenedOverrides(item.items, localesData, collapsedData, hiddenData, rootConfigDirSignature, lang, langGitbookPaths);
                    
                    // Apply order to flattened children
                    item.items = this.jsonItemSorter.sortItems(item.items, orderData);
                }
            } else {

                // Normal hierarchical structure processing
                // Apply locales override if it exists
                if (localesData.hasOwnProperty(itemKey)) {
                    item.text = localesData[itemKey];
                }
                
                // Apply collapsed override if it exists
                if (collapsedData.hasOwnProperty(itemKey) && item._isDirectory) {
                    item.collapsed = collapsedData[itemKey];
                }
                
                // If this item is a directory, also apply its _self_ values and recurse
                if (item._isDirectory || (item.items && item.items.length > 0)) {
                    const nextConfigDirSignature = rootConfigDirSignature === '_root' 
                        ? itemKey 
                        : normalizePathSeparators(path.join(rootConfigDirSignature, itemKey));
                    
                    // Skip GitBook directories
                    if (!this.pathProcessor.isGitBookRoot(nextConfigDirSignature, lang, langGitbookPaths, this.absDocsPath)) {
                        // Apply _self_ text value for the directory itself
                        const itemLocalesData = await this.jsonFileHandler.readJsonFile('locales', lang, nextConfigDirSignature);
                        if (itemLocalesData.hasOwnProperty('_self_')) {
                            item.text = itemLocalesData['_self_'];
                        }
                        
                        // Apply _self_ collapsed value for the directory itself
                        const itemCollapsedData = await this.jsonFileHandler.readJsonFile('collapsed', lang, nextConfigDirSignature);
                        if (itemCollapsedData.hasOwnProperty('_self_')) {
                            item.collapsed = itemCollapsedData['_self_'];
                        }
                        
                        // Recursively apply to children first
                        if (item.items && item.items.length > 0) {
                            await this.reapplyMigratedValues(item.items, nextConfigDirSignature, lang, langGitbookPaths);
                            
                            // Apply order to children after all other overrides
                            const childOrderData = await this.jsonFileHandler.readJsonFile('order', lang, nextConfigDirSignature);
                            item.items = this.jsonItemSorter.sortItems(item.items, childOrderData);
                        }
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
     * Applies overrides to flattened directory items using the root configuration.
     */
    private async applyFlattenedOverrides(
        flattenedItems: SidebarItem[],
        rootLocalesData: Record<string, any>,
        rootCollapsedData: Record<string, any>,
        rootHiddenData: Record<string, any>,
        rootConfigDirSignature: string,
        lang: string,
        langGitbookPaths: string[]
    ): Promise<void> {


        
        for (const item of flattenedItems) {
            // Use the item's relative path key for override lookups
            const itemKey = item._relativePathKey;

            
            // Apply locales override from root config
            if (rootLocalesData.hasOwnProperty(itemKey as string)) {


                item.text = rootLocalesData[itemKey as string];

            } else {

            }
            
            // Apply collapsed override from root config
            if (rootCollapsedData.hasOwnProperty(itemKey as string) && item._isDirectory) {

                item.collapsed = rootCollapsedData[itemKey as string];
            }
            
            // Recursively apply to nested children if they exist
            if (item.items && item.items.length > 0) {

                await this.applyFlattenedOverrides(item.items, rootLocalesData, rootCollapsedData, rootHiddenData, rootConfigDirSignature, lang, langGitbookPaths);
            }
        }
    }

    /**
     * Processes locales for flattened items using the root configuration.
     */
    private async processFlattenedItemsLocales(
        flattenedItems: SidebarItem[],
        rootConfigDirSignature: string,
        lang: string,
        isDevMode: boolean
    ): Promise<void> {
        // Debug logging for KubeJS section
        if (rootConfigDirSignature.includes('modpack/kubejs/1.20.1')) {


            flattenedItems.forEach(item => {

            });
        }

        const rootLocalesData = await this.jsonFileHandler.readJsonFile('locales', lang, rootConfigDirSignature);
        const rootLocalesMetadata = await this.metadataManager.readMetadata('locales', lang, rootConfigDirSignature);

        const localeSyncResult = await this.syncEngine.syncOverrideType(
            flattenedItems, 
            rootLocalesData, 
            rootLocalesMetadata, 
            'locales', lang, isDevMode,
            (item: SidebarItem) => item._relativePathKey || '',
            this.metadataManager
        );
        
        // IMPORTANT: Preserve existing _self_ entry when writing flattened items
        const finalLocalesData = { ...localeSyncResult.updatedJsonData };
        if (rootLocalesData['_self_'] !== undefined) {
            finalLocalesData['_self_'] = rootLocalesData['_self_'];
        }
        
        await this.jsonFileHandler.writeJsonFile('locales', lang, rootConfigDirSignature, finalLocalesData);
        await this.metadataManager.writeMetadata('locales', lang, rootConfigDirSignature, localeSyncResult.updatedMetadata);
    }

    /**
     * Processes collapsed states for flattened items using the root configuration.
     */
    private async processFlattenedItemsCollapsed(
        flattenedItems: SidebarItem[],
        rootConfigDirSignature: string,
        lang: string,
        isDevMode: boolean
    ): Promise<void> {
        const rootCollapsedData = await this.jsonFileHandler.readJsonFile('collapsed', lang, rootConfigDirSignature);
        const rootCollapsedMetadata = await this.metadataManager.readMetadata('collapsed', lang, rootConfigDirSignature);

        const collapsedSyncResult = await this.syncEngine.syncOverrideType(
            flattenedItems, 
            rootCollapsedData, 
            rootCollapsedMetadata, 
            'collapsed', lang, isDevMode,
            (item: SidebarItem) => item._relativePathKey || '',
            this.metadataManager
        );
        
        // IMPORTANT: Preserve existing _self_ entry when writing flattened items
        const finalCollapsedData = { ...collapsedSyncResult.updatedJsonData };
        if (rootCollapsedData['_self_'] !== undefined) {
            finalCollapsedData['_self_'] = rootCollapsedData['_self_'];
        }
        
        await this.jsonFileHandler.writeJsonFile('collapsed', lang, rootConfigDirSignature, finalCollapsedData);
        await this.metadataManager.writeMetadata('collapsed', lang, rootConfigDirSignature, collapsedSyncResult.updatedMetadata);
    }

    /**
     * Processes hidden states for flattened items using the root configuration.
     */
    private async processFlattenedItemsHidden(
        flattenedItems: SidebarItem[],
        rootConfigDirSignature: string,
        lang: string,
        isDevMode: boolean
    ): Promise<void> {
        const rootHiddenData = await this.jsonFileHandler.readJsonFile('hidden', lang, rootConfigDirSignature);
        const rootHiddenMetadata = await this.metadataManager.readMetadata('hidden', lang, rootConfigDirSignature);

        const hiddenSyncResult = await this.syncEngine.syncOverrideType(
            flattenedItems, 
            rootHiddenData, 
            rootHiddenMetadata, 
            'hidden', lang, isDevMode,
            (item: SidebarItem) => item._relativePathKey || '',
            this.metadataManager
        );
        
        await this.jsonFileHandler.writeJsonFile('hidden', lang, rootConfigDirSignature, hiddenSyncResult.updatedJsonData);
        await this.metadataManager.writeMetadata('hidden', lang, rootConfigDirSignature, hiddenSyncResult.updatedMetadata);
    }

    /**
     * Processes order for flattened items using the root configuration.
     */
    private async processFlattenedItemsOrder(
        flattenedItems: SidebarItem[],
        rootConfigDirSignature: string,
        lang: string,
        isDevMode: boolean
    ): Promise<void> {
        const rootOrderData = await this.jsonFileHandler.readJsonFile('order', lang, rootConfigDirSignature);
        const rootOrderMetadata = await this.metadataManager.readMetadata('order', lang, rootConfigDirSignature);

        const orderSyncResult = await this.syncEngine.syncOverrideType(
            flattenedItems, 
            rootOrderData, 
            rootOrderMetadata, 
            'order', lang, isDevMode,
            (item: SidebarItem) => item._relativePathKey || '',
            this.metadataManager
        );
        
        await this.jsonFileHandler.writeJsonFile('order', lang, rootConfigDirSignature, orderSyncResult.updatedJsonData);
        await this.metadataManager.writeMetadata('order', lang, rootConfigDirSignature, orderSyncResult.updatedMetadata);
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
        const parentLocalesData = await this.jsonFileHandler.readJsonFile('locales', lang, currentConfigDirSignature);
        const parentLocalesMetadata = await this.metadataManager.readMetadata('locales', lang, currentConfigDirSignature);

        const localeSyncResultForChildren = await this.syncEngine.syncOverrideType(
            items, 
            parentLocalesData, 
            parentLocalesMetadata, 
            'locales', lang, isDevMode,
            (childItem: SidebarItem) => this.pathProcessor.extractRelativeKeyForCurrentDir(childItem, currentConfigDirSignature),
            this.metadataManager
        );
        
        await this.jsonFileHandler.writeJsonFile('locales', lang, currentConfigDirSignature, localeSyncResultForChildren.updatedJsonData);
        await this.metadataManager.writeMetadata('locales', lang, currentConfigDirSignature, localeSyncResultForChildren.updatedMetadata);
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
        
        await this.jsonFileHandler.writeJsonFile('collapsed', lang, currentConfigDirSignature, collapsedSyncResultForChildren.updatedJsonData);
        await this.metadataManager.writeMetadata('collapsed', lang, currentConfigDirSignature, collapsedSyncResultForChildren.updatedMetadata);
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
        
        await this.jsonFileHandler.writeJsonFile('hidden', lang, currentConfigDirSignature, hiddenSyncResultForChildren.updatedJsonData);
        await this.metadataManager.writeMetadata('hidden', lang, currentConfigDirSignature, hiddenSyncResultForChildren.updatedMetadata);
    }

    /**
     * Processes _self_ properties for a directory item.
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
            
            let itemToProcessArray = [currentItem];
            let keyGetter = (_item: SidebarItem) => '_self_'; 
            let pseudoJsonForSelf = { '_self_': itemOwnJsonData['_self_'] }; 

            if (type === 'locales' && typeof itemOwnJsonData['_self_'] === 'undefined' && itemOwnJsonData[currentItem.text!] !== undefined) {
                pseudoJsonForSelf = { '_self_': itemOwnJsonData[currentItem.text!] };
            } else if (type === 'collapsed' && typeof itemOwnJsonData !== 'object') {
                pseudoJsonForSelf = { '_self_': itemOwnJsonData };
            } else if (type === 'hidden' && typeof itemOwnJsonData !== 'object') {
                pseudoJsonForSelf = { '_self_': itemOwnJsonData };
            } else if (itemOwnJsonData['_self_'] === undefined) {
                 pseudoJsonForSelf = { '_self_': (type === 'locales' ? currentItem.text : type === 'collapsed' ? currentItem.collapsed : false) };
            }

            const selfSyncResult = await this.syncEngine.syncOverrideType(
                itemToProcessArray, pseudoJsonForSelf, itemOwnMetadata, type, lang, isDevMode, keyGetter, this.metadataManager
            );

            let selfDataToWrite = selfSyncResult.updatedJsonData['_self_'];
            let finalJsonStructure = itemOwnJsonData;
            
            if (typeof finalJsonStructure !== 'object' && type === 'collapsed') finalJsonStructure = {};
            if (typeof finalJsonStructure !== 'object' && type === 'locales') finalJsonStructure = {};
            if (typeof finalJsonStructure !== 'object' && type === 'hidden') finalJsonStructure = {};
            
            finalJsonStructure['_self_'] = selfDataToWrite;
            if(selfDataToWrite === undefined && type === 'locales') finalJsonStructure['_self_'] = currentItem.text;
            if(selfDataToWrite === undefined && type === 'collapsed') finalJsonStructure['_self_'] = currentItem.collapsed;
            if(selfDataToWrite === undefined && type === 'hidden') finalJsonStructure['_self_'] = false;

            await this.jsonFileHandler.writeJsonFile(type, lang, nextConfigDirSignature, finalJsonStructure);
            await this.metadataManager.writeMetadata(type, lang, nextConfigDirSignature, selfSyncResult.updatedMetadata);
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
        
        await this.jsonFileHandler.writeJsonFile('order', lang, configDirSignature, orderSyncResult.updatedJsonData);
        await this.metadataManager.writeMetadata('order', lang, configDirSignature, orderSyncResult.updatedMetadata);
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
        
        await this.jsonFileHandler.writeJsonFile('order', lang, currentConfigDirSignature, orderSyncResultForCurrentItems.updatedJsonData);
        await this.metadataManager.writeMetadata('order', lang, currentConfigDirSignature, orderSyncResultForCurrentItems.updatedMetadata);
        
        const sortedItems = this.jsonItemSorter.sortItems(items, orderSyncResultForCurrentItems.updatedJsonData);
        items.length = 0;
        items.push(...sortedItems);
    }

    /**
     * Applies hidden states to items in the sidebar structure.
     * Instead of removing hidden items, mark them with a hidden property.
     * This preserves the structure while allowing conditional display.
     */
    private async filterHiddenItems(
        items: SidebarItem[],
        currentConfigDirSignature: string,
        lang: string,
        isFlattened: boolean = false
    ): Promise<void> {
        const hiddenData = await this.jsonFileHandler.readJsonFile('hidden', lang, currentConfigDirSignature);

        // Apply hidden states to items (don't remove them)
        for (let i = 0; i < items.length; i++) {
            const item = items[i];
            const itemKey = isFlattened && item._relativePathKey 
                ? item._relativePathKey 
                : this.pathProcessor.extractRelativeKeyForCurrentDir(item, currentConfigDirSignature);

            // Check if this item should be hidden and mark it accordingly
            if (hiddenData[itemKey] === true) {

                // Instead of removing, mark as hidden in the item's properties
                (item as any)._hidden = true;
            } else {
                // Ensure the item is marked as visible if it was previously hidden
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
                    await this.filterHiddenItems(item.items, currentConfigDirSignature, lang, isFlattened);
                }
            }
        }
    }
} 


