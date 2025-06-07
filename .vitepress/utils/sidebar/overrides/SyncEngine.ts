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

        console.log(`DEBUG: SyncEngine - CONSERVATIVE sync for ${overrideType} (${currentItems.length} items)`);
        console.log(`DEBUG: SyncEngine - Existing JSON keys:`, Object.keys(existingJsonData));

        // CONSERVATIVE APPROACH: Only mark entries as active for existing entries
        // and ADD missing entries - NEVER override existing values
        let newEntriesAdded = 0;
        let existingEntriesUpdated = 0;

        for (const item of currentItems) {
            const itemKey = keyExtractor(item);
            if (!itemKey) {
                console.log(`DEBUG: SyncEngine - Skipping item with no key: "${item.text}"`);
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
                    // lastSeen: currentTimestamp  // Removed to prevent heavy git commits
                    // KEEP existing valueHash and isUserSet unchanged
                };
                
                console.log(`DEBUG: SyncEngine - Preserved existing entry: "${itemKey}" = "${existingJsonValue}"`);
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
                    defaultValue = item._priority !== undefined ? item._priority : 0;
                    } else {
                    continue;
            }

                // Add new JSON entry
                updatedJsonData[itemKey] = defaultValue;

                // Add new metadata entry (marked as system-generated, not user-set)
                updatedMetadata[itemKey] = metadataManager.createNewMetadataEntry(defaultValue, false, true);
                // updatedMetadata[itemKey].lastSeen = currentTimestamp;  // Removed to prevent heavy git commits
                
                console.log(`DEBUG: SyncEngine - Added new entry: "${itemKey}" = "${defaultValue}"`);
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
                    // lastSeen: currentTimestamp  // Removed to prevent heavy git commits
                };
                
                // REMOVE from JSON data - orphaned entries should not clutter the config
                // The metadata preserves the value and user-set status for potential restoration
                if (updatedJsonData.hasOwnProperty(existingKey)) {
                    console.log(`🗑️ SyncEngine - Removing orphaned entry: "${existingKey}" (file renamed/deleted)`);
                    delete updatedJsonData[existingKey];
                    orphanedEntriesRemoved++;
                }
                
                inactiveEntriesMarked++;
            }
        }

        console.log(`DEBUG: SyncEngine - CONSERVATIVE sync results:`);
        console.log(`  - New entries added: ${newEntriesAdded}`);
        console.log(`  - Existing entries preserved: ${existingEntriesUpdated}`);
        console.log(`  - Inactive entries marked: ${inactiveEntriesMarked}`);
        console.log(`  - Orphaned entries removed: ${orphanedEntriesRemoved}`);
        console.log(`  - Total JSON keys: ${Object.keys(updatedJsonData).length}`);

        return {
            updatedJsonData,
            updatedMetadata
        };
    }
}
