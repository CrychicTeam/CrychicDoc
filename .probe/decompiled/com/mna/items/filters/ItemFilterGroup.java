package com.mna.items.filters;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public class ItemFilterGroup {

    public static final ItemFilterGroup GROUND_RUNE_NON_PLACEABLE = new ItemFilterGroup(new GroundRuneNonPlaceable());

    public static final ItemFilterGroup ANY_STONE_RUNE = new ItemFilterGroup(new RuneItemFilter());

    public static final ItemFilterGroup ANY_SPELL = new ItemFilterGroup(new SpellItemFilter());

    public static final ItemFilterGroup ALL_ITEMS = new ItemFilterGroup(new AllItemFilter());

    public static final ItemFilterGroup ANY_NON_EMPTY_PHYLACTERY = new ItemFilterGroup(new PhylacteryFilter());

    public static final ItemFilterGroup ANY_MARKING_RUNE = new ItemFilterGroup(new MarkingRuneFilter());

    public static final ItemFilterGroup ANY_ENCHANTED_RUNE = new ItemFilterGroup(new EnchantedRuneFilter());

    List<ItemFilter> filters = new ArrayList();

    public ItemFilterGroup(ItemFilter filter) {
        this.filters.add(filter);
    }

    public ItemFilterGroup(List<ItemFilter> filters) {
        this.filters.addAll(filters);
    }

    public boolean anyMatchesFilter(ItemStack stack) {
        for (ItemFilter filter : this.filters) {
            if (filter.IsValidItem(stack)) {
                return true;
            }
        }
        return false;
    }
}