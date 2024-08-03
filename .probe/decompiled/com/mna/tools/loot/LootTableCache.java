package com.mna.tools.loot;

import java.util.HashMap;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public class LootTableCache {

    private static HashMap<ResourceLocation, List<LootDrop>> __cachedLoot = new HashMap();

    public static List<LootDrop> getLoot(ResourceLocation lootTableID) {
        return (List<LootDrop>) __cachedLoot.get(lootTableID);
    }

    public static void cacheLoot(ResourceLocation lootTableID, List<LootDrop> drops) {
        __cachedLoot.put(lootTableID, drops);
    }
}