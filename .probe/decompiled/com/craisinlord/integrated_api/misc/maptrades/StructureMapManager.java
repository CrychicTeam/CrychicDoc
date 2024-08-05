package com.craisinlord.integrated_api.misc.maptrades;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class StructureMapManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();

    public static final StructureMapManager STRUCTURE_MAP_MANAGER = new StructureMapManager();

    public Map<String, List<VillagerMapObj>> VILLAGER_MAP_TRADES = new HashMap();

    public Map<WanderingTraderMapObj.TRADE_TYPE, List<WanderingTraderMapObj>> WANDERING_TRADER_MAP_TRADES = new HashMap();

    public StructureMapManager() {
        super(GSON, "integrated_structure_map_trades");
    }

    protected void apply(Map<ResourceLocation, JsonElement> loader, ResourceManager manager, ProfilerFiller profiler) {
        Builder<String, List<VillagerMapObj>> builderVillager = ImmutableMap.builder();
        Builder<WanderingTraderMapObj.TRADE_TYPE, List<WanderingTraderMapObj>> builderWandering = ImmutableMap.builder();
        loader.forEach((fileIdentifier, jsonElement) -> {
            try {
                StructureMapCollectionObj spawnerMobEntries = (StructureMapCollectionObj) GSON.fromJson(jsonElement, StructureMapCollectionObj.class);
                builderVillager.putAll(spawnerMobEntries.villagerMaps);
                builderWandering.putAll(spawnerMobEntries.wanderingTraderMap);
            } catch (Exception var5x) {
                IntegratedAPI.LOGGER.error("Integrated API Error: Couldn't parse structure map file {}", fileIdentifier, var5x);
            }
        });
        this.VILLAGER_MAP_TRADES = builderVillager.build();
        this.WANDERING_TRADER_MAP_TRADES = builderWandering.build();
    }
}