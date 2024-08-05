package dev.ftb.mods.ftbxmodcompat.ftbquests.filtering;

import dev.ftb.mods.ftbfiltersystem.api.FTBFilterSystemAPI;
import dev.ftb.mods.ftbfiltersystem.api.FilterException;
import dev.ftb.mods.ftbfiltersystem.api.filter.SmartFilter;
import dev.ftb.mods.ftbquests.api.FTBQuestsAPI;
import dev.ftb.mods.ftbquests.api.ItemFilterAdapter;
import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

class FFSSetup {

    public static void init() {
        FTBQuestsAPI.api().registerFilterAdapter(new FFSSetup.FFSAdapter());
        FTBXModCompat.LOGGER.info("[FTB Quests] Enabled FTB Filter System integration");
    }

    private static class FFSAdapter implements ItemFilterAdapter {

        @Override
        public String getName() {
            return "FTB Filter System";
        }

        @Override
        public boolean isFilterStack(ItemStack stack) {
            return FTBFilterSystemAPI.api().isFilterItem(stack);
        }

        @Override
        public boolean doesItemMatch(ItemStack filterStack, ItemStack toCheck) {
            return FTBFilterSystemAPI.api().doesFilterMatch(filterStack, toCheck);
        }

        @Override
        public ItemFilterAdapter.Matcher getMatcher(ItemStack filterStack) {
            try {
                return new FFSSetup.FFSMatcher(filterStack);
            } catch (FilterException var3) {
                return ItemFilterAdapter.NO_MATCH;
            }
        }

        @Override
        public ItemStack makeTagFilterStack(TagKey<Item> tag) {
            return FTBFilterSystemAPI.api().makeTagFilter(tag);
        }
    }

    private static class FFSMatcher implements ItemFilterAdapter.Matcher {

        private final SmartFilter smartFilter;

        public FFSMatcher(ItemStack filterStack) {
            this.smartFilter = FTBFilterSystemAPI.api().parseFilter(filterStack);
        }

        public boolean test(ItemStack stack) {
            return this.smartFilter.test(stack);
        }
    }
}