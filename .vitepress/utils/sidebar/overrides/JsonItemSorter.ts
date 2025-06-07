import { SidebarItem } from '../types';

/**
 * @file JsonItemSorter.ts
 * @description Sorts sidebar items based on `order.json` data, with alphanumeric fallback.
 */
export class JsonItemSorter {
    constructor() {
        // Constructor might be empty or take global sorting configurations if any
    }

    /**
     * Sorts an array of SidebarItems based on order data from an order.json file.
     * Items in orderData get priority, sorted by their numeric value.
     * Items not in orderData are appended, sorted alphanumerically by their 
     * `_relativePathKey` (for files/dirs) or `text` (for groups).
     *
     * @param itemsToFinalSort The array of SidebarItems (potentially with text/collapsed states already updated).
     * @param orderJsonData The `Record<string, any>` from the corresponding order.json file.
     *                      Keys are item._relativePathKey or item.text (for groups).
     *                      Values are expected to be numbers for ordering.
     * @returns A new array of finally sorted SidebarItems.
     */
    public sortItems(        
        itemsToFinalSort: SidebarItem[], 
        // order.json data, where values are expected to be numbers if they are for ordering
        orderJsonData: Record<string, any> 
    ): SidebarItem[] {
        if (!itemsToFinalSort || itemsToFinalSort.length === 0) {
            return [];
        }

        const itemsWithSortInfo = itemsToFinalSort.map(item => {
            const orderKey = item._relativePathKey || item.text; // Groups might be keyed by their title
            let order = Number.MAX_SAFE_INTEGER; // Default for items not in orderData or invalid orderData value
            
            if (orderKey && orderJsonData.hasOwnProperty(orderKey)) {
                const orderVal = orderJsonData[orderKey];
                if (typeof orderVal === 'number' && !isNaN(orderVal)) {
                    order = orderVal;
                    // Set the priority from order.json value
                    item._priority = order;
                } else if (typeof orderVal === 'string') {
                    // Try to parse string values as numbers
                    const parsedOrder = parseFloat(orderVal);
                    if (!isNaN(parsedOrder)) {
                        order = parsedOrder;
                        // Set the priority from parsed order value
                        item._priority = order;
                    }
                }
            }

            // If this is a directory, also process its items recursively
            if (item._isDirectory && item.items && item.items.length > 0) {
                // Pass down the same order data to maintain consistent priorities
                item.items = this.sortItems(item.items, orderJsonData);
                
                // If directory has no explicit order, use the minimum priority of its children
                if (item._priority === Number.MAX_SAFE_INTEGER) {
                    const childPriorities = item.items
                        .map(child => typeof child._priority === 'number' ? child._priority : Number.MAX_SAFE_INTEGER)
                        .filter(p => p !== Number.MAX_SAFE_INTEGER);
                    if (childPriorities.length > 0) {
                        const minChildPriority = Math.min(...childPriorities);
                        item._priority = minChildPriority;
                        order = minChildPriority;
                    }
                }
            }

            return { 
                item, 
                // Use _priority for sorting instead of order.json value
                order: typeof item._priority === 'number' ? item._priority : Number.MAX_SAFE_INTEGER,
                // Use _relativePathKey for more stable alphanumeric sort if text is the same or missing
                sortKey: item._relativePathKey || item.text || '' 
            };
        });

        itemsWithSortInfo.sort((a, b) => {
            if (a.order !== b.order) {
                return a.order - b.order; // Primary sort by priority
            }
            // Secondary sort alphanumerically by sortKey (which prefers _relativePathKey over text)
            return a.sortKey.localeCompare(b.sortKey);
        });

        return itemsWithSortInfo.map(wrappedItem => wrappedItem.item);
    }
} 

