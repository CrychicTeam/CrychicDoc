package com.craisinlord.integrated_api.misc.maptrades;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.events.RegisterVillagerTradesEvent;
import com.craisinlord.integrated_api.events.RegisterWanderingTradesEvent;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.maps.MapDecoration;

public final class StructureMapTradesEvents {

    private StructureMapTradesEvents() {
    }

    public static void addVillagerTrades(RegisterVillagerTradesEvent event) {
        ResourceLocation currentVillager = BuiltInRegistries.VILLAGER_PROFESSION.getKey(event.type());
        if (currentVillager != null && StructureMapManager.STRUCTURE_MAP_MANAGER.VILLAGER_MAP_TRADES.containsKey(currentVillager.toString())) {
            for (VillagerMapObj mapTrade : (List) StructureMapManager.STRUCTURE_MAP_MANAGER.VILLAGER_MAP_TRADES.get(currentVillager.toString())) {
                MapDecoration.Type icon;
                try {
                    icon = MapDecoration.Type.valueOf(mapTrade.mapIcon.toUpperCase(Locale.ROOT));
                } catch (Exception var6) {
                    IntegratedAPI.LOGGER.error(var6);
                    icon = MapDecoration.Type.MANSION;
                }
                event.addTrade(mapTrade.tradeLevel, new StructureSpecificMaps.TreasureMapForEmeralds(mapTrade.emeraldsRequired, mapTrade.structure, mapTrade.mapName, icon, mapTrade.tradesAllowed, mapTrade.xpReward, mapTrade.spawnRegionSearchRadius));
            }
        }
    }

    public static void addWanderingTrades(RegisterWanderingTradesEvent event) {
        for (Entry<WanderingTraderMapObj.TRADE_TYPE, List<WanderingTraderMapObj>> tradeEntry : StructureMapManager.STRUCTURE_MAP_MANAGER.WANDERING_TRADER_MAP_TRADES.entrySet()) {
            for (WanderingTraderMapObj mapTrade : (List) tradeEntry.getValue()) {
                MapDecoration.Type icon;
                try {
                    icon = MapDecoration.Type.valueOf(mapTrade.mapIcon.toUpperCase(Locale.ROOT));
                } catch (Exception var7) {
                    IntegratedAPI.LOGGER.error(var7);
                    icon = MapDecoration.Type.MANSION;
                }
                if (tradeEntry.getKey() == WanderingTraderMapObj.TRADE_TYPE.RARE) {
                    event.addRareTrade(new StructureSpecificMaps.TreasureMapForEmeralds(mapTrade.emeraldsRequired, mapTrade.structure, mapTrade.mapName, icon, mapTrade.tradesAllowed, mapTrade.xpReward, mapTrade.spawnRegionSearchRadius));
                } else if (tradeEntry.getKey() == WanderingTraderMapObj.TRADE_TYPE.COMMON) {
                    event.addRareTrade(new StructureSpecificMaps.TreasureMapForEmeralds(mapTrade.emeraldsRequired, mapTrade.structure, mapTrade.mapName, icon, mapTrade.tradesAllowed, mapTrade.xpReward, mapTrade.spawnRegionSearchRadius));
                }
            }
        }
    }
}