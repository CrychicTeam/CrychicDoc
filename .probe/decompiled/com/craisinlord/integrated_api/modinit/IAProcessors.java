package com.craisinlord.integrated_api.modinit;

import com.craisinlord.integrated_api.modinit.registry.RegistryEntry;
import com.craisinlord.integrated_api.modinit.registry.ResourcefulRegistries;
import com.craisinlord.integrated_api.modinit.registry.ResourcefulRegistry;
import com.craisinlord.integrated_api.world.processors.BlockRemovalPostProcessor;
import com.craisinlord.integrated_api.world.processors.CappedStructureSurfaceProcessor;
import com.craisinlord.integrated_api.world.processors.CloseOffAirSourcesProcessor;
import com.craisinlord.integrated_api.world.processors.CloseOffFluidSourcesProcessor;
import com.craisinlord.integrated_api.world.processors.FillEndPortalFrameProcessor;
import com.craisinlord.integrated_api.world.processors.FloodWithWaterProcessor;
import com.craisinlord.integrated_api.world.processors.IntegratedBlockReplaceProcessor;
import com.craisinlord.integrated_api.world.processors.MechanicalBearingProcessor;
import com.craisinlord.integrated_api.world.processors.PostProcessListProcessor;
import com.craisinlord.integrated_api.world.processors.RandomReplaceWithPropertiesProcessor;
import com.craisinlord.integrated_api.world.processors.RemoveFloatingBlocksProcessor;
import com.craisinlord.integrated_api.world.processors.ReplaceAirOnlyProcessor;
import com.craisinlord.integrated_api.world.processors.ReplaceLiquidOnlyProcessor;
import com.craisinlord.integrated_api.world.processors.SpawnerRandomizingProcessor;
import com.craisinlord.integrated_api.world.processors.TickBlocksProcessor;
import com.craisinlord.integrated_api.world.processors.WaterloggingFixProcessor;
import com.craisinlord.integrated_api.world.processors.WindmillBearingProcessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public final class IAProcessors {

    public static final ResourcefulRegistry<StructureProcessorType<?>> STRUCTURE_PROCESSOR = ResourcefulRegistries.create(BuiltInRegistries.STRUCTURE_PROCESSOR, "integrated_api");

    public static final RegistryEntry<StructureProcessorType<BlockRemovalPostProcessor>> BLOCK_REMOVAL_POST_PROCESSOR = STRUCTURE_PROCESSOR.register("block_removal_post_processor", () -> () -> BlockRemovalPostProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<FloodWithWaterProcessor>> FLOOD_WITH_WATER_PROCESSOR = STRUCTURE_PROCESSOR.register("flood_with_water_processor", () -> () -> FloodWithWaterProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<ReplaceAirOnlyProcessor>> REPLACE_AIR_ONLY_PROCESSOR = STRUCTURE_PROCESSOR.register("replace_air_only_processor", () -> () -> ReplaceAirOnlyProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<ReplaceLiquidOnlyProcessor>> REPLACE_LIQUIDS_ONLY_PROCESSOR = STRUCTURE_PROCESSOR.register("replace_liquids_only_processor", () -> () -> ReplaceLiquidOnlyProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<SpawnerRandomizingProcessor>> SPAWNER_RANDOMIZING_PROCESSOR = STRUCTURE_PROCESSOR.register("spawner_randomizing_processor", () -> () -> SpawnerRandomizingProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<FillEndPortalFrameProcessor>> FILL_END_PORTAL_FRAME_PROCESSOR = STRUCTURE_PROCESSOR.register("fill_end_portal_frame_processor", () -> () -> FillEndPortalFrameProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<RemoveFloatingBlocksProcessor>> REMOVE_FLOATING_BLOCKS_PROCESSOR = STRUCTURE_PROCESSOR.register("remove_floating_blocks_processor", () -> () -> RemoveFloatingBlocksProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<CloseOffFluidSourcesProcessor>> CLOSE_OFF_FLUID_SOURCES_PROCESSOR = STRUCTURE_PROCESSOR.register("close_off_fluid_sources_processor", () -> () -> CloseOffFluidSourcesProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<CloseOffAirSourcesProcessor>> CLOSE_OFF_AIR_SOURCES_PROCESSOR = STRUCTURE_PROCESSOR.register("close_off_air_sources_processor", () -> () -> CloseOffAirSourcesProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<RandomReplaceWithPropertiesProcessor>> RANDOM_REPLACE_WITH_PROPERTIES_PROCESSOR = STRUCTURE_PROCESSOR.register("random_replace_with_properties_processor", () -> () -> RandomReplaceWithPropertiesProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<WaterloggingFixProcessor>> WATERLOGGING_FIX_PROCESSOR = STRUCTURE_PROCESSOR.register("waterlogging_fix_processor", () -> () -> WaterloggingFixProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<CappedStructureSurfaceProcessor>> CAPPED_STRUCTURE_SURFACE_PROCESSOR = STRUCTURE_PROCESSOR.register("capped_structure_surface_processor", () -> () -> CappedStructureSurfaceProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<PostProcessListProcessor>> POST_PROCESS_LIST_PROCESSOR = STRUCTURE_PROCESSOR.register("post_process_list_processor", () -> () -> PostProcessListProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<WindmillBearingProcessor>> WINDMILL_BEARING_PROCESSOR = STRUCTURE_PROCESSOR.register("windmill_bearing_processor", () -> () -> WindmillBearingProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<MechanicalBearingProcessor>> MECHANICAL_BEARING_PROCESSOR = STRUCTURE_PROCESSOR.register("mechanical_bearing_processor", () -> () -> MechanicalBearingProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<TickBlocksProcessor>> TICK_BLOCKS_PROCESSOR = STRUCTURE_PROCESSOR.register("tick_blocks_processor", () -> () -> TickBlocksProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<IntegratedBlockReplaceProcessor>> INTEGRATED_BLOCK_REPLACE_PROCESSOR = STRUCTURE_PROCESSOR.register("integrated_block_replace_processor", () -> () -> IntegratedBlockReplaceProcessor.CODEC);
}