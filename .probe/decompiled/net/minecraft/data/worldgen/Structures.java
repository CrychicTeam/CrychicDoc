package net.minecraft.data.worldgen;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.BuriedTreasureStructure;
import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidStructure;
import net.minecraft.world.level.levelgen.structure.structures.EndCityStructure;
import net.minecraft.world.level.levelgen.structure.structures.IglooStructure;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.structures.JungleTempleStructure;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure;
import net.minecraft.world.level.levelgen.structure.structures.NetherFossilStructure;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
import net.minecraft.world.level.levelgen.structure.structures.OceanRuinStructure;
import net.minecraft.world.level.levelgen.structure.structures.RuinedPortalPiece;
import net.minecraft.world.level.levelgen.structure.structures.RuinedPortalStructure;
import net.minecraft.world.level.levelgen.structure.structures.ShipwreckStructure;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdStructure;
import net.minecraft.world.level.levelgen.structure.structures.SwampHutStructure;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionStructure;

public class Structures {

    private static Structure.StructureSettings structure(HolderSet<Biome> holderSetBiome0, Map<MobCategory, StructureSpawnOverride> mapMobCategoryStructureSpawnOverride1, GenerationStep.Decoration generationStepDecoration2, TerrainAdjustment terrainAdjustment3) {
        return new Structure.StructureSettings(holderSetBiome0, mapMobCategoryStructureSpawnOverride1, generationStepDecoration2, terrainAdjustment3);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> holderSetBiome0, GenerationStep.Decoration generationStepDecoration1, TerrainAdjustment terrainAdjustment2) {
        return structure(holderSetBiome0, Map.of(), generationStepDecoration1, terrainAdjustment2);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> holderSetBiome0, TerrainAdjustment terrainAdjustment1) {
        return structure(holderSetBiome0, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, terrainAdjustment1);
    }

    public static void bootstrap(BootstapContext<Structure> bootstapContextStructure0) {
        HolderGetter<Biome> $$1 = bootstapContextStructure0.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> $$2 = bootstapContextStructure0.lookup(Registries.TEMPLATE_POOL);
        bootstapContextStructure0.register(BuiltinStructures.PILLAGER_OUTPOST, new JigsawStructure(structure($$1.getOrThrow(BiomeTags.HAS_PILLAGER_OUTPOST), Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create(new MobSpawnSettings.SpawnerData(EntityType.PILLAGER, 1, 1, 1)))), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN), $$2.getOrThrow(PillagerOutpostPools.START), 7, ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));
        bootstapContextStructure0.register(BuiltinStructures.MINESHAFT, new MineshaftStructure(structure($$1.getOrThrow(BiomeTags.HAS_MINESHAFT), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, TerrainAdjustment.NONE), MineshaftStructure.Type.NORMAL));
        bootstapContextStructure0.register(BuiltinStructures.MINESHAFT_MESA, new MineshaftStructure(structure($$1.getOrThrow(BiomeTags.HAS_MINESHAFT_MESA), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, TerrainAdjustment.NONE), MineshaftStructure.Type.MESA));
        bootstapContextStructure0.register(BuiltinStructures.WOODLAND_MANSION, new WoodlandMansionStructure(structure($$1.getOrThrow(BiomeTags.HAS_WOODLAND_MANSION), TerrainAdjustment.NONE)));
        bootstapContextStructure0.register(BuiltinStructures.JUNGLE_TEMPLE, new JungleTempleStructure(structure($$1.getOrThrow(BiomeTags.HAS_JUNGLE_TEMPLE), TerrainAdjustment.NONE)));
        bootstapContextStructure0.register(BuiltinStructures.DESERT_PYRAMID, new DesertPyramidStructure(structure($$1.getOrThrow(BiomeTags.HAS_DESERT_PYRAMID), TerrainAdjustment.NONE)));
        bootstapContextStructure0.register(BuiltinStructures.IGLOO, new IglooStructure(structure($$1.getOrThrow(BiomeTags.HAS_IGLOO), TerrainAdjustment.NONE)));
        bootstapContextStructure0.register(BuiltinStructures.SHIPWRECK, new ShipwreckStructure(structure($$1.getOrThrow(BiomeTags.HAS_SHIPWRECK), TerrainAdjustment.NONE), false));
        bootstapContextStructure0.register(BuiltinStructures.SHIPWRECK_BEACHED, new ShipwreckStructure(structure($$1.getOrThrow(BiomeTags.HAS_SHIPWRECK_BEACHED), TerrainAdjustment.NONE), true));
        bootstapContextStructure0.register(BuiltinStructures.SWAMP_HUT, new SwampHutStructure(structure($$1.getOrThrow(BiomeTags.HAS_SWAMP_HUT), Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE, WeightedRandomList.create(new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1))), MobCategory.CREATURE, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE, WeightedRandomList.create(new MobSpawnSettings.SpawnerData(EntityType.CAT, 1, 1, 1)))), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
        bootstapContextStructure0.register(BuiltinStructures.STRONGHOLD, new StrongholdStructure(structure($$1.getOrThrow(BiomeTags.HAS_STRONGHOLD), TerrainAdjustment.BURY)));
        bootstapContextStructure0.register(BuiltinStructures.OCEAN_MONUMENT, new OceanMonumentStructure(structure($$1.getOrThrow(BiomeTags.HAS_OCEAN_MONUMENT), Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create(new MobSpawnSettings.SpawnerData(EntityType.GUARDIAN, 1, 2, 4))), MobCategory.UNDERGROUND_WATER_CREATURE, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, MobSpawnSettings.EMPTY_MOB_LIST), MobCategory.AXOLOTLS, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, MobSpawnSettings.EMPTY_MOB_LIST)), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
        bootstapContextStructure0.register(BuiltinStructures.OCEAN_RUIN_COLD, new OceanRuinStructure(structure($$1.getOrThrow(BiomeTags.HAS_OCEAN_RUIN_COLD), TerrainAdjustment.NONE), OceanRuinStructure.Type.COLD, 0.3F, 0.9F));
        bootstapContextStructure0.register(BuiltinStructures.OCEAN_RUIN_WARM, new OceanRuinStructure(structure($$1.getOrThrow(BiomeTags.HAS_OCEAN_RUIN_WARM), TerrainAdjustment.NONE), OceanRuinStructure.Type.WARM, 0.3F, 0.9F));
        bootstapContextStructure0.register(BuiltinStructures.FORTRESS, new NetherFortressStructure(structure($$1.getOrThrow(BiomeTags.HAS_NETHER_FORTRESS), Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE, NetherFortressStructure.FORTRESS_ENEMIES)), GenerationStep.Decoration.UNDERGROUND_DECORATION, TerrainAdjustment.NONE)));
        bootstapContextStructure0.register(BuiltinStructures.NETHER_FOSSIL, new NetherFossilStructure(structure($$1.getOrThrow(BiomeTags.HAS_NETHER_FOSSIL), GenerationStep.Decoration.UNDERGROUND_DECORATION, TerrainAdjustment.BEARD_THIN), UniformHeight.of(VerticalAnchor.absolute(32), VerticalAnchor.belowTop(2))));
        bootstapContextStructure0.register(BuiltinStructures.END_CITY, new EndCityStructure(structure($$1.getOrThrow(BiomeTags.HAS_END_CITY), TerrainAdjustment.NONE)));
        bootstapContextStructure0.register(BuiltinStructures.BURIED_TREASURE, new BuriedTreasureStructure(structure($$1.getOrThrow(BiomeTags.HAS_BURIED_TREASURE), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, TerrainAdjustment.NONE)));
        bootstapContextStructure0.register(BuiltinStructures.BASTION_REMNANT, new JigsawStructure(structure($$1.getOrThrow(BiomeTags.HAS_BASTION_REMNANT), TerrainAdjustment.NONE), $$2.getOrThrow(BastionPieces.START), 6, ConstantHeight.of(VerticalAnchor.absolute(33)), false));
        bootstapContextStructure0.register(BuiltinStructures.VILLAGE_PLAINS, new JigsawStructure(structure($$1.getOrThrow(BiomeTags.HAS_VILLAGE_PLAINS), TerrainAdjustment.BEARD_THIN), $$2.getOrThrow(PlainVillagePools.START), 6, ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));
        bootstapContextStructure0.register(BuiltinStructures.VILLAGE_DESERT, new JigsawStructure(structure($$1.getOrThrow(BiomeTags.HAS_VILLAGE_DESERT), TerrainAdjustment.BEARD_THIN), $$2.getOrThrow(DesertVillagePools.START), 6, ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));
        bootstapContextStructure0.register(BuiltinStructures.VILLAGE_SAVANNA, new JigsawStructure(structure($$1.getOrThrow(BiomeTags.HAS_VILLAGE_SAVANNA), TerrainAdjustment.BEARD_THIN), $$2.getOrThrow(SavannaVillagePools.START), 6, ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));
        bootstapContextStructure0.register(BuiltinStructures.VILLAGE_SNOWY, new JigsawStructure(structure($$1.getOrThrow(BiomeTags.HAS_VILLAGE_SNOWY), TerrainAdjustment.BEARD_THIN), $$2.getOrThrow(SnowyVillagePools.START), 6, ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));
        bootstapContextStructure0.register(BuiltinStructures.VILLAGE_TAIGA, new JigsawStructure(structure($$1.getOrThrow(BiomeTags.HAS_VILLAGE_TAIGA), TerrainAdjustment.BEARD_THIN), $$2.getOrThrow(TaigaVillagePools.START), 6, ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));
        bootstapContextStructure0.register(BuiltinStructures.RUINED_PORTAL_STANDARD, new RuinedPortalStructure(structure($$1.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_STANDARD), TerrainAdjustment.NONE), List.of(new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.UNDERGROUND, 1.0F, 0.2F, false, false, true, false, 0.5F), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.ON_LAND_SURFACE, 0.5F, 0.2F, false, false, true, false, 0.5F))));
        bootstapContextStructure0.register(BuiltinStructures.RUINED_PORTAL_DESERT, new RuinedPortalStructure(structure($$1.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_DESERT), TerrainAdjustment.NONE), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.PARTLY_BURIED, 0.0F, 0.0F, false, false, false, false, 1.0F)));
        bootstapContextStructure0.register(BuiltinStructures.RUINED_PORTAL_JUNGLE, new RuinedPortalStructure(structure($$1.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_JUNGLE), TerrainAdjustment.NONE), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.ON_LAND_SURFACE, 0.5F, 0.8F, true, true, false, false, 1.0F)));
        bootstapContextStructure0.register(BuiltinStructures.RUINED_PORTAL_SWAMP, new RuinedPortalStructure(structure($$1.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_SWAMP), TerrainAdjustment.NONE), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR, 0.0F, 0.5F, false, true, false, false, 1.0F)));
        bootstapContextStructure0.register(BuiltinStructures.RUINED_PORTAL_MOUNTAIN, new RuinedPortalStructure(structure($$1.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_MOUNTAIN), TerrainAdjustment.NONE), List.of(new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.IN_MOUNTAIN, 1.0F, 0.2F, false, false, true, false, 0.5F), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.ON_LAND_SURFACE, 0.5F, 0.2F, false, false, true, false, 0.5F))));
        bootstapContextStructure0.register(BuiltinStructures.RUINED_PORTAL_OCEAN, new RuinedPortalStructure(structure($$1.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_OCEAN), TerrainAdjustment.NONE), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR, 0.0F, 0.8F, false, false, true, false, 1.0F)));
        bootstapContextStructure0.register(BuiltinStructures.RUINED_PORTAL_NETHER, new RuinedPortalStructure(structure($$1.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_NETHER), TerrainAdjustment.NONE), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.IN_NETHER, 0.5F, 0.0F, false, false, false, true, 1.0F)));
        bootstapContextStructure0.register(BuiltinStructures.ANCIENT_CITY, new JigsawStructure(structure($$1.getOrThrow(BiomeTags.HAS_ANCIENT_CITY), (Map<MobCategory, StructureSpawnOverride>) Arrays.stream(MobCategory.values()).collect(Collectors.toMap(p_236555_ -> p_236555_, p_236551_ -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), GenerationStep.Decoration.UNDERGROUND_DECORATION, TerrainAdjustment.BEARD_BOX), $$2.getOrThrow(AncientCityStructurePieces.START), Optional.of(new ResourceLocation("city_anchor")), 7, ConstantHeight.of(VerticalAnchor.absolute(-27)), false, Optional.empty(), 116));
        bootstapContextStructure0.register(BuiltinStructures.TRAIL_RUINS, new JigsawStructure(structure($$1.getOrThrow(BiomeTags.HAS_TRAIL_RUINS), Map.of(), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, TerrainAdjustment.BURY), $$2.getOrThrow(TrailRuinsStructurePools.START), 7, ConstantHeight.of(VerticalAnchor.absolute(-15)), false, Heightmap.Types.WORLD_SURFACE_WG));
    }
}