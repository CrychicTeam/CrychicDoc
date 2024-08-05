package net.minecraft.data.worldgen;

import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;

public class BiomeDefaultFeatures {

    public static void addDefaultCarversAndLakes(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
        biomeGenerationSettingsBuilder0.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND);
        biomeGenerationSettingsBuilder0.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_SURFACE);
    }

    public static void addDefaultMonsterRoom(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.MONSTER_ROOM);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.MONSTER_ROOM_DEEP);
    }

    public static void addDefaultUndergroundVariety(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIRT);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRAVEL);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRANITE_UPPER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRANITE_LOWER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIORITE_UPPER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIORITE_LOWER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_ANDESITE_UPPER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_ANDESITE_LOWER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_TUFF);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.GLOW_LICHEN);
    }

    public static void addDripstone(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, CavePlacements.LARGE_DRIPSTONE);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.DRIPSTONE_CLUSTER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.POINTED_DRIPSTONE);
    }

    public static void addSculk(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.SCULK_VEIN);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.SCULK_PATCH_DEEP_DARK);
    }

    public static void addDefaultOres(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        addDefaultOres(biomeGenerationSettingsBuilder0, false);
    }

    public static void addDefaultOres(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0, boolean boolean1) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_COAL_UPPER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_COAL_LOWER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_UPPER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_MIDDLE);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_SMALL);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GOLD);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GOLD_LOWER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_REDSTONE);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_REDSTONE_LOWER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIAMOND);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIAMOND_LARGE);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIAMOND_BURIED);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_LAPIS);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_LAPIS_BURIED);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, boolean1 ? OrePlacements.ORE_COPPER_LARGE : OrePlacements.ORE_COPPER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CavePlacements.UNDERWATER_MAGMA);
    }

    public static void addExtraGold(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GOLD_EXTRA);
    }

    public static void addExtraEmeralds(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_EMERALD);
    }

    public static void addInfestedStone(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_INFESTED);
    }

    public static void addDefaultSoftDisks(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_SAND);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_CLAY);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_GRAVEL);
    }

    public static void addSwampClayDisk(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_CLAY);
    }

    public static void addMangroveSwampDisks(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_GRASS);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_CLAY);
    }

    public static void addMossyStoneBlock(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.FOREST_ROCK);
    }

    public static void addFerns(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_LARGE_FERN);
    }

    public static void addRareBerryBushes(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_BERRY_RARE);
    }

    public static void addCommonBerryBushes(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_BERRY_COMMON);
    }

    public static void addLightBambooVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BAMBOO_LIGHT);
    }

    public static void addBambooVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BAMBOO);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BAMBOO_VEGETATION);
    }

    public static void addTaigaTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_TAIGA);
    }

    public static void addGroveTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_GROVE);
    }

    public static void addWaterTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_WATER);
    }

    public static void addBirchTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_BIRCH);
    }

    public static void addOtherBirchTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_BIRCH_AND_OAK);
    }

    public static void addTallBirchTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BIRCH_TALL);
    }

    public static void addSavannaTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_SAVANNA);
    }

    public static void addShatteredSavannaTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_WINDSWEPT_SAVANNA);
    }

    public static void addLushCavesVegetationFeatures(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_CEILING_VEGETATION);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.CAVE_VINES);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_CLAY);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_VEGETATION);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.ROOTED_AZALEA_TREE);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.SPORE_BLOSSOM);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.CLASSIC_VINES);
    }

    public static void addLushCavesSpecialOres(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_CLAY);
    }

    public static void addMountainTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_WINDSWEPT_HILLS);
    }

    public static void addMountainForestTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_WINDSWEPT_FOREST);
    }

    public static void addJungleTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_JUNGLE);
    }

    public static void addSparseJungleTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_SPARSE_JUNGLE);
    }

    public static void addBadlandsTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_BADLANDS);
    }

    public static void addSnowyTrees(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_SNOWY);
    }

    public static void addJungleGrass(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_JUNGLE);
    }

    public static void addSavannaGrass(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS);
    }

    public static void addShatteredSavannaGrass(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_NORMAL);
    }

    public static void addSavannaExtraGrass(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_SAVANNA);
    }

    public static void addBadlandGrass(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_BADLANDS);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH_BADLANDS);
    }

    public static void addForestFlowers(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FOREST_FLOWERS);
    }

    public static void addForestGrass(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST);
    }

    public static void addSwampVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_SWAMP);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_SWAMP);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_NORMAL);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_WATERLILY);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_SWAMP);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_SWAMP);
    }

    public static void addMangroveSwampVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_MANGROVE);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_NORMAL);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_WATERLILY);
    }

    public static void addMushroomFieldVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.MUSHROOM_ISLAND_VEGETATION);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_TAIGA);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_TAIGA);
    }

    public static void addPlainVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_PLAINS);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_PLAINS);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
    }

    public static void addDesertVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH_2);
    }

    public static void addGiantTaigaVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_TAIGA);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_OLD_GROWTH);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_OLD_GROWTH);
    }

    public static void addDefaultFlowers(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_DEFAULT);
    }

    public static void addCherryGroveVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_CHERRY);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_CHERRY);
    }

    public static void addMeadowVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_MEADOW);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_MEADOW);
    }

    public static void addWarmFlowers(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_WARM);
    }

    public static void addDefaultGrass(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_BADLANDS);
    }

    public static void addTaigaGrass(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_TAIGA_2);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_TAIGA);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_TAIGA);
    }

    public static void addPlainGrass(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS_2);
    }

    public static void addDefaultMushrooms(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_NORMAL);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_NORMAL);
    }

    public static void addDefaultExtraVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
    }

    public static void addBadlandExtraVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE_BADLANDS);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DECORATED);
    }

    public static void addJungleMelons(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_MELON);
    }

    public static void addSparseJungleMelons(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_MELON_SPARSE);
    }

    public static void addJungleVines(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.VINES);
    }

    public static void addDesertExtraVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE_DESERT);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DESERT);
    }

    public static void addSwampExtraVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE_SWAMP);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
    }

    public static void addDesertExtraDecoration(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.DESERT_WELL);
    }

    public static void addFossilDecoration(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.FOSSIL_UPPER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.FOSSIL_LOWER);
    }

    public static void addColdOceanExtraVegetation(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.KELP_COLD);
    }

    public static void addDefaultSeagrass(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SIMPLE);
    }

    public static void addLukeWarmKelp(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.KELP_WARM);
    }

    public static void addDefaultSprings(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_WATER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_LAVA);
    }

    public static void addFrozenSprings(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_LAVA_FROZEN);
    }

    public static void addIcebergs(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.ICEBERG_PACKED);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.ICEBERG_BLUE);
    }

    public static void addBlueIce(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.BLUE_ICE);
    }

    public static void addSurfaceFreezing(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MiscOverworldPlacements.FREEZE_TOP_LAYER);
    }

    public static void addNetherDefaultOres(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_GRAVEL_NETHER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_BLACKSTONE);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_GOLD_NETHER);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_QUARTZ_NETHER);
        addAncientDebris(biomeGenerationSettingsBuilder0);
    }

    public static void addAncientDebris(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_ANCIENT_DEBRIS_LARGE);
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_ANCIENT_DEBRIS_SMALL);
    }

    public static void addDefaultCrystalFormations(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        biomeGenerationSettingsBuilder0.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, CavePlacements.AMETHYST_GEODE);
    }

    public static void farmAnimals(MobSpawnSettings.Builder mobSpawnSettingsBuilder0) {
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 12, 4, 4));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PIG, 10, 4, 4));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 10, 4, 4));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.COW, 8, 4, 4));
    }

    public static void caveSpawns(MobSpawnSettings.Builder mobSpawnSettingsBuilder0) {
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 8, 8));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 10, 4, 6));
    }

    public static void commonSpawns(MobSpawnSettings.Builder mobSpawnSettingsBuilder0) {
        caveSpawns(mobSpawnSettingsBuilder0);
        monsters(mobSpawnSettingsBuilder0, 95, 5, 100, false);
    }

    public static void oceanSpawns(MobSpawnSettings.Builder mobSpawnSettingsBuilder0, int int1, int int2, int int3) {
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, int1, 1, int2));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.COD, int3, 3, 6));
        commonSpawns(mobSpawnSettingsBuilder0);
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 5, 1, 1));
    }

    public static void warmOceanSpawns(MobSpawnSettings.Builder mobSpawnSettingsBuilder0, int int1, int int2) {
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, int1, int2, 4));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 25, 8, 8));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 2, 1, 2));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 5, 1, 1));
        commonSpawns(mobSpawnSettingsBuilder0);
    }

    public static void plainsSpawns(MobSpawnSettings.Builder mobSpawnSettingsBuilder0) {
        farmAnimals(mobSpawnSettingsBuilder0);
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 5, 2, 6));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 1, 3));
        commonSpawns(mobSpawnSettingsBuilder0);
    }

    public static void snowySpawns(MobSpawnSettings.Builder mobSpawnSettingsBuilder0) {
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 10, 2, 3));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 1, 1, 2));
        caveSpawns(mobSpawnSettingsBuilder0);
        monsters(mobSpawnSettingsBuilder0, 95, 5, 20, false);
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.STRAY, 80, 4, 4));
    }

    public static void desertSpawns(MobSpawnSettings.Builder mobSpawnSettingsBuilder0) {
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3));
        caveSpawns(mobSpawnSettingsBuilder0);
        monsters(mobSpawnSettingsBuilder0, 19, 1, 100, false);
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.HUSK, 80, 4, 4));
    }

    public static void dripstoneCavesSpawns(MobSpawnSettings.Builder mobSpawnSettingsBuilder0) {
        caveSpawns(mobSpawnSettingsBuilder0);
        int $$1 = 95;
        monsters(mobSpawnSettingsBuilder0, 95, 5, 100, false);
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 95, 4, 4));
    }

    public static void monsters(MobSpawnSettings.Builder mobSpawnSettingsBuilder0, int int1, int int2, int int3, boolean boolean4) {
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 100, 4, 4));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(boolean4 ? EntityType.DROWNED : EntityType.ZOMBIE, int1, 4, 4));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, int2, 1, 1));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, int3, 4, 4));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 100, 4, 4));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 100, 4, 4));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 10, 1, 4));
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 5, 1, 1));
    }

    public static void mooshroomSpawns(MobSpawnSettings.Builder mobSpawnSettingsBuilder0) {
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.MOOSHROOM, 8, 4, 8));
        caveSpawns(mobSpawnSettingsBuilder0);
    }

    public static void baseJungleSpawns(MobSpawnSettings.Builder mobSpawnSettingsBuilder0) {
        farmAnimals(mobSpawnSettingsBuilder0);
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 10, 4, 4));
        commonSpawns(mobSpawnSettingsBuilder0);
    }

    public static void endSpawns(MobSpawnSettings.Builder mobSpawnSettingsBuilder0) {
        mobSpawnSettingsBuilder0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 10, 4, 4));
    }
}