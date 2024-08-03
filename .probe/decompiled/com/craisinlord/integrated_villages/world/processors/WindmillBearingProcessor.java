package com.craisinlord.integrated_villages.world.processors;

import com.craisinlord.integrated_villages.IntegratedVilagesProcessors;
import com.craisinlord.integrated_villages.IntegratedVillages;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class WindmillBearingProcessor extends StructureProcessor {

    public static final Codec<WindmillBearingProcessor> CODEC = Codec.unit(WindmillBearingProcessor::new);

    private WindmillBearingProcessor() {
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (structureBlockInfoWorld.state().m_60734_().getDescriptionId().equals("block.create.windmill_bearing") && IntegratedVillages.CONFIG.general.activateCreateContraptions) {
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
        return IntegratedVilagesProcessors.WINDMILL_BEARING_PROCESSOR.get();
    }
}