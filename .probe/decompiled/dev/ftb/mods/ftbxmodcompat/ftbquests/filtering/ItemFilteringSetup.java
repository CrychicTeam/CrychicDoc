package dev.ftb.mods.ftbxmodcompat.ftbquests.filtering;

import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;

public class ItemFilteringSetup {

    public static void init() {
        if (FTBXModCompat.isFTBFilterSystemLoaded) {
            FFSSetup.init();
        }
        if (FTBXModCompat.isItemFiltersLoaded) {
            ItemFiltersSetup.init();
        }
    }
}