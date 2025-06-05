import { SidebarItem, EffectiveDirConfig, JsonFileMetadata, MetadataEntry } from '../types';
import { JsonFileHandler, JsonOverrideFileType } from './JsonFileHandler'; 
import { MetadataManager } from './MetadataManager';

/**
 * @file SyncEngine.ts
 * @description Core logic for synchronizing structural sidebar items with JSON overrides,
 * handling persistence, user edit preservation, and metadata updates.
 */
export class SyncEngine {
    // private readonly jsonFileHandler: JsonFileHandler; // Injected via JsonConfigSynchronizerService
    // private readonly metadataManager: MetadataManager; // Injected via JsonConfigSynchronizerService

    constructor(
        // In Phase 2, these might be injected if SyncEngine becomes more complex
        // and directly uses them, or JsonConfigSynchronizerService orchestrates them.
    ) {
        // For now, constructor might be empty if methods are static or dependencies passed to methods.
    }

    /**
     * Performs the smart synchronization for a given set of structural items against
     * one type of JSON override (locale, order, or collapsed).
     *
     * @param structuralItems The raw items from StructuralGeneratorService, representing the current declarative structure.
     * @param existingJsonData Current data from the relevant JSON file (e.g., locale.json for the current scope).
     * @param existingMetadata Current metadata for that JSON file.
     * @param itemType The type of JSON override being processed ('locales', 'order', 'collapsed').
     * @param lang Language code (primarily for context, logging, or if stubs differ by lang).
     * @param isDevMode Dev mode flag (primarily for context, or if stub generation differs).
     * @param getPathKeyForItem Function to get the unique JSON/metadata key for a SidebarItem.
     * @param metadataManagerInstance An instance of MetadataManager for hash generation and metadata entry creation.
     *
     * @returns An object containing:
     *  - `updatedJsonData`: The new data to be written to the JSON file.
     *  - `updatedMetadata`: The new metadata to be written.
     *  - `processedItems`: The structuralItems array, modified with applied overrides (text for locales, collapsed state for collapsed).
     */
    public async syncOverrideType(
        structuralItems: SidebarItem[],
        existingJsonData: Record<string, any>,
        existingMetadata: JsonFileMetadata,
        itemType: JsonOverrideFileType,
        lang: string, 
        isDevMode: boolean,
        getPathKeyForItem: (item: SidebarItem) => string,
        metadataManagerInstance: MetadataManager
    ): Promise<{
        updatedJsonData: Record<string, any>;
        updatedMetadata: JsonFileMetadata;
        processedItems: SidebarItem[]; 
    }> {
        const updatedJsonData: Record<string, any> = {}; 
        const updatedMetadata: JsonFileMetadata = {};
        
        // Deep copy items to allow modification. JSON.parse(JSON.stringify(...)) is a common way.
        const processedItems = structuralItems.map(item => ({ 
            ...item, 
            items: item.items ? JSON.parse(JSON.stringify(item.items)) as SidebarItem[] : undefined 
        })); 

        const structuralItemKeys = new Set<string>();

        // Debug logging for KubeJS section
        const isKubeJSSection = structuralItems.some(item => 
            item._relativePathKey?.includes('1.20.1') || 
            item.text?.includes('KubeJS') ||
            (item.items && item.items.some(child => child._relativePathKey?.includes('Upgrade')))
        );
        if (isKubeJSSection && itemType === 'locales') {



            structuralItems.forEach(item => {

                if (item.items) {
                    item.items.forEach(child => {

                    });
                }
            });
        }

        // Pass 1: Iterate current structural items. Update JSON/metadata based on structure vs. existing data.
        for (let i = 0; i < processedItems.length; i++) {
            const currentProcessedItem = processedItems[i];
            const itemKey = getPathKeyForItem(currentProcessedItem);
            structuralItemKeys.add(itemKey);

            let valueFromStructure: any;
            
            if (itemType === 'locales') {
                valueFromStructure = currentProcessedItem.text;
            } else if (itemType === 'order') {
                // For 'order', use the current structural position as the default value
                valueFromStructure = i;
            } else if (itemType === 'collapsed') {
                // For 'collapsed', the structure provides a default/calculated collapsed state.
                // For directory items, use their collapsed state; for file items, they don't have collapsed state in structure
                valueFromStructure = currentProcessedItem._isDirectory 
                    ? (currentProcessedItem.collapsed !== undefined ? currentProcessedItem.collapsed : true)
                    : undefined; // File items don't have collapsed state
            } else if (itemType === 'hidden') {
                // For 'hidden', default all items to false (not hidden)
                valueFromStructure = false;
            }
            
            const valueFromJson = existingJsonData[itemKey];
            const metadataForThisKey = existingMetadata[itemKey];
            
            let finalValueToStoreInJson = valueFromStructure; // Default to value from structure for stubs
            let isUserSetValue = false;

            if (existingJsonData.hasOwnProperty(itemKey)) { // Key exists in current JSON
                if (metadataManagerInstance.isEntryUserModified(valueFromJson, metadataForThisKey)) {
                    finalValueToStoreInJson = valueFromJson; // Preserve user's value
                    isUserSetValue = true;
                } else {
                    // It's a system stub or a value previously from structure that user hasn't touched.
                    // For 'locales', if the structural title changed, update the stub.
                    if (itemType === 'locales' && valueFromStructure !== valueFromJson) {
                        finalValueToStoreInJson = valueFromStructure;
                    } 
                    // For 'collapsed', if structural collapsed state changed, update stub.
                    else if (itemType === 'collapsed' && valueFromStructure !== valueFromJson && valueFromStructure !== undefined) {
                        finalValueToStoreInJson = valueFromStructure;
                    } 
                    // For 'order', update the position to reflect current structural order
                    else if (itemType === 'order' && valueFromStructure !== valueFromJson) {
                        finalValueToStoreInJson = valueFromStructure;
                    }
                    else {
                        finalValueToStoreInJson = valueFromJson; // Keep existing JSON value (could be old stub)
                    }
                    isUserSetValue = metadataForThisKey?.isUserSet ?? false; // Preserve flag if it was already considered user-set but value matched
                }
            } else { // New item in structure, not in JSON: create a stub.
                isUserSetValue = false; 
                if (itemType === 'locales') {
                    finalValueToStoreInJson = currentProcessedItem.text; // Default to structural text
                }
                else if (itemType === 'collapsed') {
                    // Only create collapsed entries for directory items
                    if (currentProcessedItem._isDirectory) {
                        finalValueToStoreInJson = currentProcessedItem.collapsed !== undefined ? currentProcessedItem.collapsed : true; // Default new directories to collapsed
                    } else {
                        finalValueToStoreInJson = undefined; // File items don't get collapsed entries
                    }
                }
                else if (itemType === 'order') {
                    finalValueToStoreInJson = i; // Use structural position as default order
                }
            }

            // Update JSON data: Write stubs for all types, but only include collapsed for directory items
            if (finalValueToStoreInJson !== undefined) {
                updatedJsonData[itemKey] = finalValueToStoreInJson;
            }
            
            // Create/update metadata entry for this item from structure (only if we're storing a value)
            if (finalValueToStoreInJson !== undefined) {
                updatedMetadata[itemKey] = metadataManagerInstance.createNewMetadataEntry(
                    updatedJsonData.hasOwnProperty(itemKey) ? updatedJsonData[itemKey] : valueFromStructure, // Hash what's in JSON, or structural value if no JSON entry
                    isUserSetValue,
                    true // isActiveInStructure
                );
            }

            // Apply to processedItems for immediate use (text for locales, collapsed for collapsed)
            if (itemType === 'locales' && updatedJsonData.hasOwnProperty(itemKey)) {
                currentProcessedItem.text = updatedJsonData[itemKey];
            }
            if (itemType === 'collapsed' && updatedJsonData.hasOwnProperty(itemKey)) {
                currentProcessedItem.collapsed = updatedJsonData[itemKey] as boolean | undefined;
            }
        }

        // Debug logging for KubeJS section results
        if (isKubeJSSection && itemType === 'locales') {

        }

        // Pass 2: Iterate existing JSON keys to handle orphaned user-set entries
        for (const existingKey in existingJsonData) {
            if (!structuralItemKeys.has(existingKey)) { // Key is in JSON but not in current structure (orphaned)
                const orphanedMetadataEntry = existingMetadata[existingKey];
                // Only preserve if it was explicitly user-set.
                if (orphanedMetadataEntry && orphanedMetadataEntry.isUserSet) {
                    const orphanedValue = existingJsonData[existingKey];
                    // Keep user-set orphaned entries for all types including order
                    if (orphanedValue !== undefined) { 
                        updatedJsonData[existingKey] = orphanedValue; 
                    }
                    updatedMetadata[existingKey] = { 
                        ...orphanedMetadataEntry, 
                        isActiveInStructure: false, // Mark as inactive
                        lastSeen: Date.now() // Update lastSeen for the orphaned entry
                    }; 
                }
                // Orphaned system stubs (isUserSet: false) are implicitly dropped from updatedJsonData & updatedMetadata.
            }
        }

        return { updatedJsonData, updatedMetadata, processedItems };
    }
} 


