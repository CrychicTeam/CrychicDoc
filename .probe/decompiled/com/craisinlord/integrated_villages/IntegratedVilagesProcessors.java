package com.craisinlord.integrated_villages;

import com.craisinlord.integrated_api.modinit.registry.RegistryEntry;
import com.craisinlord.integrated_api.modinit.registry.ResourcefulRegistries;
import com.craisinlord.integrated_api.modinit.registry.ResourcefulRegistry;
import com.craisinlord.integrated_villages.world.processors.MechanicalBearingProcessor;
import com.craisinlord.integrated_villages.world.processors.TickBlocksProcessor;
import com.craisinlord.integrated_villages.world.processors.WindmillBearingProcessor;
import com.craisinlord.integrated_villages.world.processors.WorkstationProcessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public final class IntegratedVilagesProcessors {

    public static final ResourcefulRegistry<StructureProcessorType<?>> STRUCTURE_PROCESSOR = ResourcefulRegistries.create(BuiltInRegistries.STRUCTURE_PROCESSOR, "integrated_villages");

    public static final RegistryEntry<StructureProcessorType<WorkstationProcessor>> WORKSTATION_PROCESSOR = STRUCTURE_PROCESSOR.register("workstation_processor", () -> () -> WorkstationProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<WindmillBearingProcessor>> WINDMILL_BEARING_PROCESSOR = STRUCTURE_PROCESSOR.register("windmill_bearing_processor", () -> () -> WindmillBearingProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<MechanicalBearingProcessor>> MECHANICAL_BEARING_PROCESSOR = STRUCTURE_PROCESSOR.register("mechanical_bearing_processor", () -> () -> MechanicalBearingProcessor.CODEC);

    public static final RegistryEntry<StructureProcessorType<TickBlocksProcessor>> TICK_BLOCKS_PROCESSOR = STRUCTURE_PROCESSOR.register("tick_blocks_processor", () -> () -> TickBlocksProcessor.CODEC);
}