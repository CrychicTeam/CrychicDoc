package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class LavaSubmergedBlockProcessor extends StructureProcessor {

    public static final Codec<LavaSubmergedBlockProcessor> CODEC = Codec.unit(() -> LavaSubmergedBlockProcessor.INSTANCE);

    public static final LavaSubmergedBlockProcessor INSTANCE = new LavaSubmergedBlockProcessor();

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader0, BlockPos blockPos1, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo3, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo4, StructurePlaceSettings structurePlaceSettings5) {
        BlockPos $$6 = structureTemplateStructureBlockInfo4.pos();
        boolean $$7 = levelReader0.m_8055_($$6).m_60713_(Blocks.LAVA);
        return $$7 && !Block.isShapeFullBlock(structureTemplateStructureBlockInfo4.state().m_60808_(levelReader0, $$6)) ? new StructureTemplate.StructureBlockInfo($$6, Blocks.LAVA.defaultBlockState(), structureTemplateStructureBlockInfo4.nbt()) : structureTemplateStructureBlockInfo4;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.LAVA_SUBMERGED_BLOCK;
    }
}