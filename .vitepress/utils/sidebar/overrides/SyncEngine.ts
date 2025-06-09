import {
    SidebarItem,
    EffectiveDirConfig,
    JsonFileMetadata,
    MetadataEntry,
} from "../types";
import { JsonFileHandler, JsonOverrideFileType } from "./JsonFileHandler";
import { MetadataManager } from "./MetadataManager";

/**
 * @file SyncEngine.ts
 * @description Core logic for synchronizing structural sidebar items with JSON overrides,
 * handling persistence, user edit preservation, and metadata updates.
 */
export class SyncEngine {
    // private readonly jsonFileHandler: JsonFileHandler; // Injected via JsonConfigSynchronizerService
    // private readonly metadataManager: MetadataManager; // Injected via JsonConfigSynchronizerService

    constructor() // In Phase 2, these might be injected if SyncEngine becomes more complex
    // and directly uses them, or JsonConfigSynchronizerService orchestrates them.
    {
        // For now, constructor might be empty if methods are static or dependencies passed to methods.
    }

    /**
     * Synchronizes a specific override type (e.g., 'locales', 'collapsed') with the current sidebar structure.
     * This method ONLY ADDS missing entries - it NEVER overrides existing configurations.
     * This prevents losing user configs and reduces unnecessary Git changes.
     */
    public async syncOverrideType(
        currentItems: SidebarItem[],
        existingJsonData: Record<string, any>,
        existingMetadata: JsonFileMetadata,
        overrideType: JsonOverrideFileType,
        lang: string,
        isDevMode: boolean,
        keyExtractor: (item: SidebarItem) => string,
        metadataManager: MetadataManager
    ): Promise<{ updatedJsonData: Record<string, any>; updatedMetadata: JsonFileMetadata }> {
        const updatedJsonData = { ...existingJsonData };
        const updatedMetadata = { ...existingMetadata };
        const currentTimestamp = Date.now();

        // CONSERVATIVE APPROACH: Only mark entries as active for existing entries
        // and ADD missing entries - NEVER override existing values
        let newEntriesAdded = 0;
        let existingEntriesUpdated = 0;

        for (const item of currentItems) {
            const itemKey = keyExtractor(item);
            if (!itemKey) {
                continue;
            }

            // Skip collapsed processing for non-directories
            if (overrideType === 'collapsed' && !item._isDirectory) {
                continue;
            }

            const existingEntry = existingMetadata[itemKey];
            const existingJsonValue = existingJsonData[itemKey];

            if (existingEntry && existingJsonData.hasOwnProperty(itemKey)) {
                // ENTRY EXISTS - Only update metadata status, NEVER touch the JSON value
                updatedMetadata[itemKey] = {
                    ...existingEntry,
                    isActiveInStructure: true,
                };

                // For order type, update the item's priority from the existing value
                if (overrideType === 'order' && typeof existingJsonValue === 'number') {
                    item._priority = existingJsonValue;
                }
                
                existingEntriesUpdated++;
            } else {
                // ENTRY MISSING - Add new entry (this is safe)
                let defaultValue: any;
                if (overrideType === 'locales') {
                    defaultValue = item.text;
                } else if (overrideType === 'collapsed') {
                    defaultValue = item.collapsed !== undefined ? item.collapsed : true;
                } else if (overrideType === 'hidden') {
                    defaultValue = false; // Default to not hidden
                } else if (overrideType === 'order') {
                    // For order type, use the item's priority if set, otherwise use Number.MAX_SAFE_INTEGER
                    defaultValue = typeof item._priority === 'number' ? item._priority : Number.MAX_SAFE_INTEGER;
                } else {
                    continue;
                }

                // Add new JSON entry
                updatedJsonData[itemKey] = defaultValue;

                // Add new metadata entry (marked as system-generated, not user-set)
                updatedMetadata[itemKey] = metadataManager.createNewMetadataEntry(defaultValue, false, true);
                
                newEntriesAdded++;
            }
        }

        // Mark inactive entries and remove them from JSON (but preserve in metadata for restoration)
        let inactiveEntriesMarked = 0;
        let orphanedEntriesRemoved = 0;
        const currentItemKeys = new Set(currentItems.map(item => keyExtractor(item)).filter(key => key));
        
        for (const [existingKey, existingEntry] of Object.entries(existingMetadata)) {
            // NEVER touch _self_ entries - they are directory self-references
            if (existingKey === '_self_') {
                continue;
            }
            
            // If this key is not in the current active structure, it's orphaned
            if (!currentItemKeys.has(existingKey)) {
                // Mark as inactive in metadata (for potential restoration)
                updatedMetadata[existingKey] = {
                    ...existingEntry,
                    isActiveInStructure: false,
                };
                
                // REMOVE from JSON data - orphaned entries should not clutter the config
                // The metadata preserves the value and user-set status for potential restoration
                if (updatedJsonData.hasOwnProperty(existingKey)) {
                    delete updatedJsonData[existingKey];
                    orphanedEntriesRemoved++;
                }
                
                inactiveEntriesMarked++;
            }
        }

        return {
            updatedJsonData,
            updatedMetadata
        };
    }
}
