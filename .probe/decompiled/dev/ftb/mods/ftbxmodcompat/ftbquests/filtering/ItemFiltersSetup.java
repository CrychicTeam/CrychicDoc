package dev.ftb.mods.ftbxmodcompat.ftbquests.filtering;

import dev.ftb.mods.ftbquests.api.FTBQuestsAPI;
import dev.ftb.mods.ftbquests.api.ItemFilterAdapter;
import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;
import dev.latvian.mods.itemfilters.api.IItemFilter;
import dev.latvian.mods.itemfilters.api.IStringValueFilter;
import dev.latvian.mods.itemfilters.api.ItemFiltersAPI;
import dev.latvian.mods.itemfilters.api.ItemFiltersItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

class ItemFiltersSetup {

    public static void init() {
        FTBQuestsAPI.api().registerFilterAdapter(new ItemFiltersSetup.ItemFiltersAdapter());
        FTBXModCompat.LOGGER.info("[FTB Quests] Enabled Item Filters integration");
    }

    private static class IFMatcher implements ItemFilterAdapter.Matcher {

        private final ItemStack filterStack;

        private final IItemFilter filter;

        private IFMatcher(ItemStack filterStack) {
            this.filterStack = filterStack;
            this.filter = ItemFiltersAPI.getFilter(filterStack);
        }

        public boolean test(ItemStack stack) {
            return this.filter != null && this.filter.filter(this.filterStack, stack);
        }
    }

    private static class ItemFiltersAdapter implements ItemFilterAdapter {

        @Override
        public String getName() {
            return "Item Filters";
        }

        @Override
        public boolean isFilterStack(ItemStack stack) {
            return ItemFiltersAPI.isFilter(stack);
        }

        @Override
        public boolean doesItemMatch(ItemStack filterStack, ItemStack toCheck) {
            return ItemFiltersAPI.isFilter(filterStack) && ItemFiltersAPI.filter(filterStack, toCheck);
        }

        @Override
        public ItemFilterAdapter.Matcher getMatcher(ItemStack filterStack) {
            return new ItemFiltersSetup.IFMatcher(filterStack);
        }

        @Override
        public ItemStack makeTagFilterStack(TagKey<Item> tag) {
            ItemStack tagFilter = new ItemStack((ItemLike) ItemFiltersItems.TAG.get());
            ((IStringValueFilter) tagFilter.getItem()).setValue(tagFilter, tag.location().toString());
            return tagFilter;
        }
    }
}