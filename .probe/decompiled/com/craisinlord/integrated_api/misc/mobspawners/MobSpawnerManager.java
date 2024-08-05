package com.craisinlord.integrated_api.misc.mobspawners;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.mixins.features.DungeonFeatureAccessor;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import org.apache.logging.log4j.Level;

public class MobSpawnerManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();

    public static final MobSpawnerManager MOB_SPAWNER_MANAGER = new MobSpawnerManager();

    private Map<ResourceLocation, List<MobSpawnerObj>> spawnerMap = ImmutableMap.of();

    public MobSpawnerManager() {
        super(GSON, "integrated_structure_spawners");
    }

    protected void apply(Map<ResourceLocation, JsonElement> loader, ResourceManager manager, ProfilerFiller profiler) {
        Builder<ResourceLocation, List<MobSpawnerObj>> builder = ImmutableMap.builder();
        loader.forEach((fileIdentifier, jsonElement) -> {
            try {
                List<MobSpawnerObj> spawnerMobEntries = (List<MobSpawnerObj>) GSON.fromJson(jsonElement.getAsJsonObject().get("mobs"), (new TypeToken<List<MobSpawnerObj>>() {
                }).getType());
                for (int i = spawnerMobEntries.size() - 1; i >= 0; i--) {
                    MobSpawnerObj entry = (MobSpawnerObj) spawnerMobEntries.get(i);
                    entry.setEntityType();
                    if (entry.weight != 0.0F && entry.entityType != null) {
                        if (entry.weight < 0.0F) {
                            throw new Exception("Error: Found " + entry.name + " entry has a weight less than 0. Please remove the entry if you don't want a mob to be picked");
                        }
                    } else {
                        spawnerMobEntries.remove(i);
                    }
                }
                builder.put(fileIdentifier, spawnerMobEntries);
            } catch (Exception var7) {
                IntegratedAPI.LOGGER.error("Integrated API Error: Couldn't parse spawner mob list {}", fileIdentifier, var7);
            }
        });
        this.spawnerMap = builder.build();
    }

    public EntityType<?> getSpawnerMob(ResourceLocation spawnerJsonEntry, RandomSource random) {
        List<MobSpawnerObj> spawnerMobEntries = (List<MobSpawnerObj>) this.spawnerMap.get(spawnerJsonEntry);
        if (spawnerMobEntries == null) {
            IntegratedAPI.LOGGER.log(Level.ERROR, "\n***************************************\nFailed to get mob. Please check that " + spawnerJsonEntry + ".json is correct or that no other mod is interfering with how vanilla reads data folders. Let TelepathicGrunt know about this too!\n***************************************");
            return Util.getRandom(DungeonFeatureAccessor.getMOBS(), random);
        } else {
            float totalWeight = 0.0F;
            for (MobSpawnerObj mobSpawnerObj : spawnerMobEntries) {
                totalWeight += mobSpawnerObj.weight;
            }
            if (totalWeight == 0.0F) {
                return null;
            } else {
                float randomWeight = random.nextFloat() * totalWeight;
                int index = 0;
                try {
                    while (true) {
                        randomWeight -= ((MobSpawnerObj) spawnerMobEntries.get(index)).weight;
                        if (randomWeight <= 0.0F) {
                            return BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(((MobSpawnerObj) spawnerMobEntries.get(index)).name));
                        }
                        index++;
                    }
                } catch (Exception var8) {
                    IntegratedAPI.LOGGER.log(Level.ERROR, "\n***************************************\nFailed to get mob. Please check that " + spawnerJsonEntry + ".json is correct and let Telepathicgrunt (mod author) know he broke the mob spawner code!\n***************************************");
                    return EntityType.PIG;
                }
            }
        }
    }
}