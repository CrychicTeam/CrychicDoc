package com.craisinlord.integrated_api.world.processors;

import com.craisinlord.integrated_api.modinit.IAProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class MechanicalBearingProcessor extends StructureProcessor {

    public static final Codec<MechanicalBearingProcessor> CODEC = Codec.unit(MechanicalBearingProcessor::new);

    private MechanicalBearingProcessor() {
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (structureBlockInfoWorld.state().m_60734_().getDescriptionId().equals("block.create.mechanical_bearing")) {
            CompoundTag compoundTag = structureBlockInfoWorld.nbt();
            compoundTag.putBoolean("QueueAssembly", true);
            ((LevelAccessor) worldView).scheduleTick(structureBlockInfoWorld.pos(), structureBlockInfoWorld.state().m_60734_(), 0);
            return new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), structureBlockInfoWorld.state(), compoundTag);
        } else {
            return structureBlockInfoWorld;
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IAProcessors.MECHANICAL_BEARING_PROCESSOR.get();
    }
}