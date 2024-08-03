package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;

public class ProtectedBlockProcessor extends StructureProcessor {

    public final TagKey<Block> cannotReplace;

    public static final Codec<ProtectedBlockProcessor> CODEC = TagKey.hashedCodec(Registries.BLOCK).xmap(ProtectedBlockProcessor::new, p_205053_ -> p_205053_.cannotReplace);

    public ProtectedBlockProcessor(TagKey<Block> tagKeyBlock0) {
        this.cannotReplace = tagKeyBlock0;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader0, BlockPos blockPos1, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo3, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo4, StructurePlaceSettings structurePlaceSettings5) {
        return Feature.isReplaceable(this.cannotReplace).test(levelReader0.m_8055_(structureTemplateStructureBlockInfo4.pos())) ? structureTemplateStructureBlockInfo4 : null;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.PROTECTED_BLOCKS;
    }
}