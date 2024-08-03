package com.rekindled.embers.datagen;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.worldgen.CaveStructure;
import com.rekindled.embers.worldgen.CrystalSeedStructureProcessor;
import com.rekindled.embers.worldgen.EntityMobilizerStructureProcessor;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.holdersets.AndHolderSet;
import net.minecraftforge.registries.holdersets.OrHolderSet;

public class EmbersStructures {

    public static final ResourceKey<StructureProcessorList> RUIN_PROCESSORS = ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("embers", "small_ruin"));

    public static final ResourceKey<StructureTemplatePool> SMALL_RUIN_POOL = ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation("embers", "small_ruin"));

    public static final ResourceKey<Structure> SMALL_RUIN = ResourceKey.create(Registries.STRUCTURE, new ResourceLocation("embers", "small_ruin"));

    public static final ResourceKey<StructureSet> SMALL_RUIN_SET = ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation("embers", "small_ruin"));

    public static void generateProcessors(BootstapContext<StructureProcessorList> bootstrap) {
        bootstrap.register(RUIN_PROCESSORS, new StructureProcessorList(Lists.newArrayList(new StructureProcessor[] { new CrystalSeedStructureProcessor(210000, 5050000, 900), EntityMobilizerStructureProcessor.INSTANCE })));
    }

    public static void generatePools(BootstapContext<StructureTemplatePool> bootstrap) {
        HolderGetter<StructureTemplatePool> templatePool = bootstrap.lookup(Registries.TEMPLATE_POOL);
        HolderGetter<StructureProcessorList> processor = bootstrap.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureTemplatePool> empty = templatePool.getOrThrow(Pools.EMPTY);
        Holder<StructureProcessorList> ruinProcessor = processor.getOrThrow(RUIN_PROCESSORS);
        bootstrap.register(SMALL_RUIN_POOL, new StructureTemplatePool(empty, Lists.newArrayList(new Pair[] { Pair.of(SinglePoolElement.m_210531_("embers:small_ruin_copper", ruinProcessor), 2), Pair.of(SinglePoolElement.m_210531_("embers:small_ruin_iron", ruinProcessor), 2), Pair.of(SinglePoolElement.m_210531_("embers:small_ruin_gold", ruinProcessor), 1), Pair.of(SinglePoolElement.m_210531_("embers:small_ruin_lead", ruinProcessor), 2), Pair.of(SinglePoolElement.m_210531_("embers:small_ruin_silver", ruinProcessor), 1) }), StructureTemplatePool.Projection.RIGID));
    }

    public static void generateStructures(BootstapContext<Structure> bootstrap) {
        HolderGetter<StructureTemplatePool> templatePool = bootstrap.lookup(Registries.TEMPLATE_POOL);
        HolderGetter<Biome> biome = bootstrap.lookup(ForgeRegistries.Keys.BIOMES);
        HolderSet<Biome> overworldBiomes = biome.getOrThrow(BiomeTags.IS_OVERWORLD);
        List<HolderSet<Biome>> biomeBlackList = List.of(biome.getOrThrow(Tags.Biomes.IS_MUSHROOM), HolderSet.direct(biome.getOrThrow(Biomes.DEEP_DARK)), biome.getOrThrow(EmbersBiomeModifiers.NO_MONSTERS));
        HolderSet<Biome> ruinSpawns = new AndHolderSet<>(List.of(overworldBiomes, new EmbersBiomeModifiers.NotHolderSetWrapper(new OrHolderSet(biomeBlackList))));
        bootstrap.register(SMALL_RUIN, new CaveStructure(new Structure.StructureSettings(ruinSpawns, Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create(new MobSpawnSettings.SpawnerData(RegistryManager.ANCIENT_GOLEM.get(), 20, 1, 1)))), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, TerrainAdjustment.NONE), templatePool.getOrThrow(SMALL_RUIN_POOL), 2, ConstantHeight.of(VerticalAnchor.absolute(-10)), ConstantHeight.of(VerticalAnchor.aboveBottom(128)), false, Heightmap.Types.OCEAN_FLOOR_WG));
    }

    public static void generateSets(BootstapContext<StructureSet> bootstrap) {
        HolderGetter<Structure> structure = bootstrap.lookup(Registries.STRUCTURE);
        bootstrap.register(SMALL_RUIN_SET, new StructureSet(structure.getOrThrow(SMALL_RUIN), new RandomSpreadStructurePlacement(7, 1, RandomSpreadType.LINEAR, 193826405)));
    }
}