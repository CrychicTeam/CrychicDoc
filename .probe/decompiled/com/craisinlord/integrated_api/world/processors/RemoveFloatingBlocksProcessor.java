package com.craisinlord.integrated_api.world.processors;

import com.craisinlord.integrated_api.modinit.IAProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class RemoveFloatingBlocksProcessor extends StructureProcessor {

    public static final Codec<RemoveFloatingBlocksProcessor> CODEC = Codec.unit(RemoveFloatingBlocksProcessor::new);

    private RemoveFloatingBlocksProcessor() {
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(structureBlockInfoWorld.pos());
        if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(mutable))) {
            return structureBlockInfoWorld;
        }
        ChunkAccess cachedChunk = levelReader.getChunk(mutable);
        if (structureBlockInfoWorld.state().m_60795_() || !structureBlockInfoWorld.state().m_60819_().isEmpty()) {
            cachedChunk.setBlockState(mutable, structureBlockInfoWorld.state(), false);
            for (BlockState aboveWorldState = levelReader.m_8055_(mutable.move(Direction.UP)); mutable.m_123342_() < levelReader.getHeight() && !aboveWorldState.m_60710_(levelReader, mutable); aboveWorldState = levelReader.m_8055_(mutable.move(Direction.UP))) {
                cachedChunk.setBlockState(mutable, structureBlockInfoWorld.state(), false);
            }
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                mutable.set(structureBlockInfoWorld.pos());
                mutable.move(direction);
                ChunkPos chunkPos = new ChunkPos(mutable);
                ChunkAccess chunkAccess2 = cachedChunk;
                if (!chunkPos.equals(cachedChunk.getPos())) {
                    chunkAccess2 = levelReader.getChunk(mutable);
                }
                BlockState sideBlock = chunkAccess2.m_8055_(mutable);
                if (!sideBlock.m_60710_(levelReader, mutable)) {
                    chunkAccess2.setBlockState(mutable, structureBlockInfoWorld.state(), false);
                }
            }
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IAProcessors.REMOVE_FLOATING_BLOCKS_PROCESSOR.get();
    }
}