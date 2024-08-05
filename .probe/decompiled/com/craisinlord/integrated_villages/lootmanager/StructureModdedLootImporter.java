package com.craisinlord.integrated_villages.lootmanager;

import com.craisinlord.integrated_api.utils.PlatformHooks;
import com.craisinlord.integrated_villages.IntegratedVillages;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.loot.LootTable;

public class StructureModdedLootImporter {

    public static final Map<ResourceLocation, ResourceLocation> TABLE_IMPORTS = createMap();

    public static Map<ResourceLocation, ResourceLocation> createMap() {
        Map<ResourceLocation, ResourceLocation> tableMap = new HashMap();
        tableMap.put(new ResourceLocation("integrated_villages", "chests/tavern_village/tavern_village"), new ResourceLocation("minecraft:chests/village/village_taiga_house"));
        tableMap.put(new ResourceLocation("integrated_villages", "chests/tavern_village/tavern_village_blacksmith"), new ResourceLocation("minecraft:chests/village/village_weaponsmith"));
        if (PlatformHooks.isModLoaded("betterstrongholds")) {
            tableMap.put(new ResourceLocation("betterstrongholds", "chests/end/armoury"), new ResourceLocation("minecraft:chests/end_city_treasure"));
            tableMap.put(new ResourceLocation("betterstrongholds", "chests/end/common"), new ResourceLocation("minecraft:chests/end_city_treasure"));
        }
        return tableMap;
    }

    public static void checkLoottables(MinecraftServer minecraftServer) {
        boolean invalidLootTableFound = false;
        for (Entry<ResourceLocation, ResourceLocation> entry : TABLE_IMPORTS.entrySet()) {
            if (!((ResourceLocation) entry.getKey()).getNamespace().equals("betterstrongholds") && isInvalidLootTableFound(minecraftServer, entry)) {
                invalidLootTableFound = true;
            }
        }
        if (invalidLootTableFound) {
            IntegratedVillages.LOGGER.error("Unknown import/target loot tables found for Integrated Villages. See above logs and report to CraisinLord.");
        }
    }

    public static boolean isInvalidLootTableFound(MinecraftServer minecraftServer, Entry<ResourceLocation, ResourceLocation> entry) {
        boolean invalidLootTableFound = false;
        if (minecraftServer.getLootData().m_278676_((ResourceLocation) entry.getKey()) == LootTable.EMPTY) {
            IntegratedVillages.LOGGER.error("Unable to find loot table key: {}", entry.getKey());
            invalidLootTableFound = true;
        }
        if (minecraftServer.getLootData().m_278676_((ResourceLocation) entry.getValue()) == LootTable.EMPTY) {
            IntegratedVillages.LOGGER.error("Unable to find loot table value: {}", entry.getValue());
            invalidLootTableFound = true;
        }
        return invalidLootTableFound;
    }
}