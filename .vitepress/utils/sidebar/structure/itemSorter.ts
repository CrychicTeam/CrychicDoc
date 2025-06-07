import { SidebarItem } from '../types';
// Removed: import { generatePathKey } from './pathKeyGenerator'; // Not directly used here, key should be on item

/**
 * Converts itemOrder configuration to priority values.
 * This ensures that order.json values (which become itemOrder) properly affect priorities.
 * 
 * @param items Array of SidebarItems to process
 * @param itemOrderConfig The itemOrder configuration from order.json
 */
function applyItemOrderToPriority(
    items: SidebarItem[],
    itemOrderConfig: Record<string, number> = {}
): void {
    // First pass: Find the range of explicit priorities
    let minExplicitPriority = Number.MAX_SAFE_INTEGER;
    let maxExplicitPriority = Number.MIN_SAFE_INTEGER;
    
    for (const item of items) {
        if (item._priority !== undefined) {
            minExplicitPriority = Math.min(minExplicitPriority, item._priority);
            maxExplicitPriority = Math.max(maxExplicitPriority, item._priority);
        }
    }
    
    // Second pass: Apply itemOrder as priority
    for (const item of items) {
        const orderKey = item._relativePathKey || item.text;
        if (orderKey && itemOrderConfig.hasOwnProperty(orderKey)) {
            // If item has an order config, use it to set priority
            item._priority = itemOrderConfig[orderKey];
        } else if (item._priority === undefined) {
            // If no explicit priority and no order config, use a middle value
            item._priority = 0;
        }
        
        // Recursively process child items
        if (item.items && Array.isArray(item.items)) {
            applyItemOrderToPriority(item.items, itemOrderConfig);
        }
    }
}

/**
 * Sorts an array of SidebarItems based on their priority values.
 * The priority values should have been previously set by applyItemOrderToPriority.
 *
 * @param itemsToSort The array of SidebarItems to sort
 * @param itemOrderConfig The itemOrder configuration from order.json
 * @returns A new array of sorted SidebarItems
 */
export function sortItems(
    itemsToSort: SidebarItem[], 
    itemOrderConfig: Record<string, number> = {}
): SidebarItem[] {
    // First ensure all items have proper priority values
    applyItemOrderToPriority(itemsToSort, itemOrderConfig);
    
    // Now sort based on priority
    const itemsWithSortInfo = itemsToSort.map(item => ({
        item,
        priority: item._priority ?? 0,
        originalText: item.text || item._relativePathKey || ''
    }));

    itemsWithSortInfo.sort((a, b) => {
        // Primary sort by priority
        if (a.priority !== b.priority) {
            return a.priority - b.priority;
        }
        
        // Secondary sort by text for stable ordering
        return a.originalText.localeCompare(b.originalText);
    });

    const sortedItems = itemsWithSortInfo.map(wrappedItem => {
        const item = wrappedItem.item;
        
        // Recursively sort child items if they exist
        if (item.items && Array.isArray(item.items)) {
            item.items = sortItems(item.items, itemOrderConfig);
        }
        
        return item;
    });

    return sortedItems;
} 

