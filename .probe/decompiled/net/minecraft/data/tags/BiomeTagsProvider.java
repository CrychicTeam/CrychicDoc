package net.minecraft.data.tags;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;

public class BiomeTagsProvider extends TagsProvider<Biome> {

    public BiomeTagsProvider(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1) {
        super(packOutput0, Registries.BIOME, completableFutureHolderLookupProvider1);
    }

    @Override
    protected void addTags(HolderLookup.Provider holderLookupProvider0) {
        this.m_206424_(BiomeTags.IS_DEEP_OCEAN).add(Biomes.DEEP_FROZEN_OCEAN).add(Biomes.DEEP_COLD_OCEAN).add(Biomes.DEEP_OCEAN).add(Biomes.DEEP_LUKEWARM_OCEAN);
        this.m_206424_(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_DEEP_OCEAN).add(Biomes.FROZEN_OCEAN).add(Biomes.OCEAN).add(Biomes.COLD_OCEAN).add(Biomes.LUKEWARM_OCEAN).add(Biomes.WARM_OCEAN);
        this.m_206424_(BiomeTags.IS_BEACH).add(Biomes.BEACH).add(Biomes.SNOWY_BEACH);
        this.m_206424_(BiomeTags.IS_RIVER).add(Biomes.RIVER).add(Biomes.FROZEN_RIVER);
        this.m_206424_(BiomeTags.IS_MOUNTAIN).add(Biomes.MEADOW).add(Biomes.FROZEN_PEAKS).add(Biomes.JAGGED_PEAKS).add(Biomes.STONY_PEAKS).add(Biomes.SNOWY_SLOPES).add(Biomes.CHERRY_GROVE);
        this.m_206424_(BiomeTags.IS_BADLANDS).add(Biomes.BADLANDS).add(Biomes.ERODED_BADLANDS).add(Biomes.WOODED_BADLANDS);
        this.m_206424_(BiomeTags.IS_HILL).add(Biomes.WINDSWEPT_HILLS).add(Biomes.WINDSWEPT_FOREST).add(Biomes.WINDSWEPT_GRAVELLY_HILLS);
        this.m_206424_(BiomeTags.IS_TAIGA).add(Biomes.TAIGA).add(Biomes.SNOWY_TAIGA).add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);
        this.m_206424_(BiomeTags.IS_JUNGLE).add(Biomes.BAMBOO_JUNGLE).add(Biomes.JUNGLE).add(Biomes.SPARSE_JUNGLE);
        this.m_206424_(BiomeTags.IS_FOREST).add(Biomes.FOREST).add(Biomes.FLOWER_FOREST).add(Biomes.BIRCH_FOREST).add(Biomes.OLD_GROWTH_BIRCH_FOREST).add(Biomes.DARK_FOREST).add(Biomes.GROVE);
        this.m_206424_(BiomeTags.IS_SAVANNA).add(Biomes.SAVANNA).add(Biomes.SAVANNA_PLATEAU).add(Biomes.WINDSWEPT_SAVANNA);
        TagsProvider.TagAppender<Biome> $$1 = this.m_206424_(BiomeTags.IS_NETHER);
        MultiNoiseBiomeSourceParameterList.Preset.NETHER.usedBiomes().forEach($$1::m_255204_);
        TagsProvider.TagAppender<Biome> $$2 = this.m_206424_(BiomeTags.IS_OVERWORLD);
        MultiNoiseBiomeSourceParameterList.Preset.OVERWORLD.usedBiomes().forEach($$2::m_255204_);
        this.m_206424_(BiomeTags.IS_END).add(Biomes.THE_END).add(Biomes.END_HIGHLANDS).add(Biomes.END_MIDLANDS).add(Biomes.SMALL_END_ISLANDS).add(Biomes.END_BARRENS);
        this.m_206424_(BiomeTags.HAS_BURIED_TREASURE).addTag(BiomeTags.IS_BEACH);
        this.m_206424_(BiomeTags.HAS_DESERT_PYRAMID).add(Biomes.DESERT);
        this.m_206424_(BiomeTags.HAS_IGLOO).add(Biomes.SNOWY_TAIGA).add(Biomes.SNOWY_PLAINS).add(Biomes.SNOWY_SLOPES);
        this.m_206424_(BiomeTags.HAS_JUNGLE_TEMPLE).add(Biomes.BAMBOO_JUNGLE).add(Biomes.JUNGLE);
        this.m_206424_(BiomeTags.HAS_MINESHAFT).addTag(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_RIVER).addTag(BiomeTags.IS_BEACH).addTag(BiomeTags.IS_MOUNTAIN).addTag(BiomeTags.IS_HILL).addTag(BiomeTags.IS_TAIGA).addTag(BiomeTags.IS_JUNGLE).addTag(BiomeTags.IS_FOREST).add(Biomes.STONY_SHORE).add(Biomes.MUSHROOM_FIELDS).add(Biomes.ICE_SPIKES).add(Biomes.WINDSWEPT_SAVANNA).add(Biomes.DESERT).add(Biomes.SAVANNA).add(Biomes.SNOWY_PLAINS).add(Biomes.PLAINS).add(Biomes.SUNFLOWER_PLAINS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.SAVANNA_PLATEAU).add(Biomes.DRIPSTONE_CAVES).add(Biomes.LUSH_CAVES);
        this.m_206424_(BiomeTags.HAS_MINESHAFT_MESA).addTag(BiomeTags.IS_BADLANDS);
        this.m_206424_(BiomeTags.MINESHAFT_BLOCKING).add(Biomes.DEEP_DARK);
        this.m_206424_(BiomeTags.HAS_OCEAN_MONUMENT).addTag(BiomeTags.IS_DEEP_OCEAN);
        this.m_206424_(BiomeTags.REQUIRED_OCEAN_MONUMENT_SURROUNDING).addTag(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_RIVER);
        this.m_206424_(BiomeTags.HAS_OCEAN_RUIN_COLD).add(Biomes.FROZEN_OCEAN).add(Biomes.COLD_OCEAN).add(Biomes.OCEAN).add(Biomes.DEEP_FROZEN_OCEAN).add(Biomes.DEEP_COLD_OCEAN).add(Biomes.DEEP_OCEAN);
        this.m_206424_(BiomeTags.HAS_OCEAN_RUIN_WARM).add(Biomes.LUKEWARM_OCEAN).add(Biomes.WARM_OCEAN).add(Biomes.DEEP_LUKEWARM_OCEAN);
        this.m_206424_(BiomeTags.HAS_PILLAGER_OUTPOST).add(Biomes.DESERT).add(Biomes.PLAINS).add(Biomes.SAVANNA).add(Biomes.SNOWY_PLAINS).add(Biomes.TAIGA).addTag(BiomeTags.IS_MOUNTAIN).add(Biomes.GROVE);
        this.m_206424_(BiomeTags.HAS_RUINED_PORTAL_DESERT).add(Biomes.DESERT);
        this.m_206424_(BiomeTags.HAS_RUINED_PORTAL_JUNGLE).addTag(BiomeTags.IS_JUNGLE);
        this.m_206424_(BiomeTags.HAS_RUINED_PORTAL_OCEAN).addTag(BiomeTags.IS_OCEAN);
        this.m_206424_(BiomeTags.HAS_RUINED_PORTAL_SWAMP).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP);
        this.m_206424_(BiomeTags.HAS_RUINED_PORTAL_MOUNTAIN).addTag(BiomeTags.IS_BADLANDS).addTag(BiomeTags.IS_HILL).add(Biomes.SAVANNA_PLATEAU).add(Biomes.WINDSWEPT_SAVANNA).add(Biomes.STONY_SHORE).addTag(BiomeTags.IS_MOUNTAIN);
        this.m_206424_(BiomeTags.HAS_RUINED_PORTAL_STANDARD).addTag(BiomeTags.IS_BEACH).addTag(BiomeTags.IS_RIVER).addTag(BiomeTags.IS_TAIGA).addTag(BiomeTags.IS_FOREST).add(Biomes.MUSHROOM_FIELDS).add(Biomes.ICE_SPIKES).add(Biomes.DRIPSTONE_CAVES).add(Biomes.LUSH_CAVES).add(Biomes.SAVANNA).add(Biomes.SNOWY_PLAINS).add(Biomes.PLAINS).add(Biomes.SUNFLOWER_PLAINS);
        this.m_206424_(BiomeTags.HAS_SHIPWRECK_BEACHED).addTag(BiomeTags.IS_BEACH);
        this.m_206424_(BiomeTags.HAS_SHIPWRECK).addTag(BiomeTags.IS_OCEAN);
        this.m_206424_(BiomeTags.HAS_SWAMP_HUT).add(Biomes.SWAMP);
        this.m_206424_(BiomeTags.HAS_VILLAGE_DESERT).add(Biomes.DESERT);
        this.m_206424_(BiomeTags.HAS_VILLAGE_PLAINS).add(Biomes.PLAINS).add(Biomes.MEADOW);
        this.m_206424_(BiomeTags.HAS_VILLAGE_SAVANNA).add(Biomes.SAVANNA);
        this.m_206424_(BiomeTags.HAS_VILLAGE_SNOWY).add(Biomes.SNOWY_PLAINS);
        this.m_206424_(BiomeTags.HAS_VILLAGE_TAIGA).add(Biomes.TAIGA);
        this.m_206424_(BiomeTags.HAS_TRAIL_RUINS).add(Biomes.TAIGA).add(Biomes.SNOWY_TAIGA).add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA).add(Biomes.OLD_GROWTH_BIRCH_FOREST).add(Biomes.JUNGLE);
        this.m_206424_(BiomeTags.HAS_WOODLAND_MANSION).add(Biomes.DARK_FOREST);
        this.m_206424_(BiomeTags.STRONGHOLD_BIASED_TO).add(Biomes.PLAINS).add(Biomes.SUNFLOWER_PLAINS).add(Biomes.SNOWY_PLAINS).add(Biomes.ICE_SPIKES).add(Biomes.DESERT).add(Biomes.FOREST).add(Biomes.FLOWER_FOREST).add(Biomes.BIRCH_FOREST).add(Biomes.DARK_FOREST).add(Biomes.OLD_GROWTH_BIRCH_FOREST).add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA).add(Biomes.TAIGA).add(Biomes.SNOWY_TAIGA).add(Biomes.SAVANNA).add(Biomes.SAVANNA_PLATEAU).add(Biomes.WINDSWEPT_HILLS).add(Biomes.WINDSWEPT_GRAVELLY_HILLS).add(Biomes.WINDSWEPT_FOREST).add(Biomes.WINDSWEPT_SAVANNA).add(Biomes.JUNGLE).add(Biomes.SPARSE_JUNGLE).add(Biomes.BAMBOO_JUNGLE).add(Biomes.BADLANDS).add(Biomes.ERODED_BADLANDS).add(Biomes.WOODED_BADLANDS).add(Biomes.MEADOW).add(Biomes.GROVE).add(Biomes.SNOWY_SLOPES).add(Biomes.FROZEN_PEAKS).add(Biomes.JAGGED_PEAKS).add(Biomes.STONY_PEAKS).add(Biomes.MUSHROOM_FIELDS).add(Biomes.DRIPSTONE_CAVES).add(Biomes.LUSH_CAVES);
        this.m_206424_(BiomeTags.HAS_STRONGHOLD).addTag(BiomeTags.IS_OVERWORLD);
        this.m_206424_(BiomeTags.HAS_NETHER_FORTRESS).addTag(BiomeTags.IS_NETHER);
        this.m_206424_(BiomeTags.HAS_NETHER_FOSSIL).add(Biomes.SOUL_SAND_VALLEY);
        this.m_206424_(BiomeTags.HAS_BASTION_REMNANT).add(Biomes.CRIMSON_FOREST).add(Biomes.NETHER_WASTES).add(Biomes.SOUL_SAND_VALLEY).add(Biomes.WARPED_FOREST);
        this.m_206424_(BiomeTags.HAS_ANCIENT_CITY).add(Biomes.DEEP_DARK);
        this.m_206424_(BiomeTags.HAS_RUINED_PORTAL_NETHER).addTag(BiomeTags.IS_NETHER);
        this.m_206424_(BiomeTags.HAS_END_CITY).add(Biomes.END_HIGHLANDS).add(Biomes.END_MIDLANDS);
        this.m_206424_(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL).add(Biomes.WARM_OCEAN);
        this.m_206424_(BiomeTags.PLAYS_UNDERWATER_MUSIC).addTag(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_RIVER);
        this.m_206424_(BiomeTags.HAS_CLOSER_WATER_FOG).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP);
        this.m_206424_(BiomeTags.WATER_ON_MAP_OUTLINES).addTag(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_RIVER).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP);
        this.m_206424_(BiomeTags.WITHOUT_ZOMBIE_SIEGES).add(Biomes.MUSHROOM_FIELDS);
        this.m_206424_(BiomeTags.WITHOUT_PATROL_SPAWNS).add(Biomes.MUSHROOM_FIELDS);
        this.m_206424_(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS).add(Biomes.THE_VOID);
        this.m_206424_(BiomeTags.SPAWNS_COLD_VARIANT_FROGS).add(Biomes.SNOWY_PLAINS).add(Biomes.ICE_SPIKES).add(Biomes.FROZEN_PEAKS).add(Biomes.JAGGED_PEAKS).add(Biomes.SNOWY_SLOPES).add(Biomes.FROZEN_OCEAN).add(Biomes.DEEP_FROZEN_OCEAN).add(Biomes.GROVE).add(Biomes.DEEP_DARK).add(Biomes.FROZEN_RIVER).add(Biomes.SNOWY_TAIGA).add(Biomes.SNOWY_BEACH).addTag(BiomeTags.IS_END);
        this.m_206424_(BiomeTags.SPAWNS_WARM_VARIANT_FROGS).add(Biomes.DESERT).add(Biomes.WARM_OCEAN).addTag(BiomeTags.IS_JUNGLE).addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_NETHER).addTag(BiomeTags.IS_BADLANDS).add(Biomes.MANGROVE_SWAMP);
        this.m_206424_(BiomeTags.SPAWNS_GOLD_RABBITS).add(Biomes.DESERT);
        this.m_206424_(BiomeTags.SPAWNS_WHITE_RABBITS).add(Biomes.SNOWY_PLAINS).add(Biomes.ICE_SPIKES).add(Biomes.FROZEN_OCEAN).add(Biomes.SNOWY_TAIGA).add(Biomes.FROZEN_RIVER).add(Biomes.SNOWY_BEACH).add(Biomes.FROZEN_PEAKS).add(Biomes.JAGGED_PEAKS).add(Biomes.SNOWY_SLOPES).add(Biomes.GROVE);
        this.m_206424_(BiomeTags.REDUCED_WATER_AMBIENT_SPAWNS).addTag(BiomeTags.IS_RIVER);
        this.m_206424_(BiomeTags.ALLOWS_TROPICAL_FISH_SPAWNS_AT_ANY_HEIGHT).add(Biomes.LUSH_CAVES);
        this.m_206424_(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS).add(Biomes.FROZEN_OCEAN).add(Biomes.DEEP_FROZEN_OCEAN);
        this.m_206424_(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS).addTag(BiomeTags.IS_RIVER);
        this.m_206424_(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP);
        this.m_206424_(BiomeTags.SPAWNS_SNOW_FOXES).add(Biomes.SNOWY_PLAINS).add(Biomes.ICE_SPIKES).add(Biomes.FROZEN_OCEAN).add(Biomes.SNOWY_TAIGA).add(Biomes.FROZEN_RIVER).add(Biomes.SNOWY_BEACH).add(Biomes.FROZEN_PEAKS).add(Biomes.JAGGED_PEAKS).add(Biomes.SNOWY_SLOPES).add(Biomes.GROVE);
        this.m_206424_(BiomeTags.INCREASED_FIRE_BURNOUT).add(Biomes.BAMBOO_JUNGLE).add(Biomes.MUSHROOM_FIELDS).add(Biomes.MANGROVE_SWAMP).add(Biomes.SNOWY_SLOPES).add(Biomes.FROZEN_PEAKS).add(Biomes.JAGGED_PEAKS).add(Biomes.SWAMP).add(Biomes.JUNGLE);
        this.m_206424_(BiomeTags.SNOW_GOLEM_MELTS).add(Biomes.BADLANDS).add(Biomes.BASALT_DELTAS).add(Biomes.CRIMSON_FOREST).add(Biomes.DESERT).add(Biomes.ERODED_BADLANDS).add(Biomes.NETHER_WASTES).add(Biomes.SAVANNA).add(Biomes.SAVANNA_PLATEAU).add(Biomes.SOUL_SAND_VALLEY).add(Biomes.WARPED_FOREST).add(Biomes.WINDSWEPT_SAVANNA).add(Biomes.WOODED_BADLANDS);
    }
}