package com.craisinlord.integrated_api.world.processors;

import com.craisinlord.integrated_api.modinit.IAProcessors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class FillEndPortalFrameProcessor extends StructureProcessor {

    public static final Codec<FillEndPortalFrameProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.FLOAT.fieldOf("probability_per_block").stable().forGetter(processor -> processor.probability)).apply(instance, instance.stable(FillEndPortalFrameProcessor::new)));

    private final float probability;

    public FillEndPortalFrameProcessor(Float probability) {
        this.probability = probability;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (structureBlockInfoWorld.state().m_60713_(Blocks.END_PORTAL_FRAME)) {
            BlockPos worldPos = structureBlockInfoWorld.pos();
            RandomSource random = structurePlacementData.getRandom(worldPos);
            return new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), (BlockState) structureBlockInfoWorld.state().m_61124_(EndPortalFrameBlock.HAS_EYE, random.nextFloat() < this.probability), structureBlockInfoWorld.nbt());
        } else {
            return structureBlockInfoWorld;
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IAProcessors.FILL_END_PORTAL_FRAME_PROCESSOR.get();
    }
}