package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.world.IafProcessors;
import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

public class GorgonTempleProcessor extends StructureProcessor {

    public static final GorgonTempleProcessor INSTANCE = new GorgonTempleProcessor();

    public static final Codec<GorgonTempleProcessor> CODEC = Codec.unit(() -> INSTANCE);

    public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader worldReader, @NotNull BlockPos pos, @NotNull BlockPos pos2, @NotNull StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (infoIn2.state().m_60734_() instanceof SimpleWaterloggedBlock && worldReader.m_6425_(infoIn2.pos()).is(FluidTags.WATER)) {
            ChunkPos currentChunk = new ChunkPos(infoIn2.pos());
            worldReader.getChunk(currentChunk.x, currentChunk.z).setBlockState(infoIn2.pos(), Blocks.AIR.defaultBlockState(), false);
        }
        return infoIn2;
    }

    @NotNull
    @Override
    protected StructureProcessorType getType() {
        return IafProcessors.GORGONTEMPLEPROCESSOR.get();
    }
}