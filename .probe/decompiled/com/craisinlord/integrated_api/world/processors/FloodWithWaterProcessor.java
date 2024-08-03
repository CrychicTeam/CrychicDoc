package com.craisinlord.integrated_api.world.processors;

import com.craisinlord.integrated_api.modinit.IAProcessors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.Fluids;

public class FloodWithWaterProcessor extends StructureProcessor {

    public static final Codec<FloodWithWaterProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.INT.fieldOf("flood_level").forGetter(config -> config.floodLevel)).apply(instance, instance.stable(FloodWithWaterProcessor::new)));

    private final int floodLevel;

    private FloodWithWaterProcessor(int floodLevel) {
        this.floodLevel = floodLevel;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (structureBlockInfoWorld.state().m_60819_().is(FluidTags.WATER)) {
            this.tickWaterFluid(levelReader, structureBlockInfoWorld);
            return structureBlockInfoWorld;
        } else {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(structureBlockInfoWorld.pos()))) {
                return structureBlockInfoWorld;
            }
            if (structureBlockInfoWorld.pos().m_123342_() <= this.floodLevel) {
                boolean flooded = false;
                if (structureBlockInfoWorld.state().m_60795_() || structureBlockInfoWorld.state().m_204336_(BlockTags.FLOWER_POTS) || structureBlockInfoWorld.state().m_204336_(BlockTags.BUTTONS) || structureBlockInfoWorld.state().m_60722_(Fluids.WATER)) {
                    structureBlockInfoWorld = new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), Blocks.WATER.defaultBlockState(), null);
                    this.tickWaterFluid(levelReader, structureBlockInfoWorld);
                    flooded = true;
                } else if (structureBlockInfoWorld.state().m_61138_(BlockStateProperties.WATERLOGGED)) {
                    structureBlockInfoWorld = new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), (BlockState) structureBlockInfoWorld.state().m_61124_(BlockStateProperties.WATERLOGGED, true), structureBlockInfoWorld.nbt());
                    this.tickWaterFluid(levelReader, structureBlockInfoWorld);
                    flooded = true;
                } else if (structureBlockInfoWorld.state().m_60734_() instanceof BushBlock) {
                    structureBlockInfoWorld = new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), Blocks.WATER.defaultBlockState(), null);
                    this.tickWaterFluid(levelReader, structureBlockInfoWorld);
                    flooded = true;
                }
                if (flooded) {
                    ChunkPos currentChunkPos = new ChunkPos(structureBlockInfoWorld.pos());
                    ChunkAccess currentChunk = levelReader.getChunk(currentChunkPos.x, currentChunkPos.z);
                    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                    for (Direction direction : Direction.values()) {
                        if (direction != Direction.UP) {
                            mutable.set(structureBlockInfoWorld.pos()).move(direction);
                            if (currentChunkPos.x != mutable.m_123341_() >> 4 || currentChunkPos.z != mutable.m_123343_() >> 4) {
                                currentChunk = levelReader.getChunk(mutable);
                                currentChunkPos = new ChunkPos(mutable);
                            }
                            BlockState neighboringBlock = currentChunk.m_8055_(mutable);
                            if (!neighboringBlock.m_60815_() && neighboringBlock.m_60819_().isEmpty()) {
                                currentChunk.setBlockState(mutable, Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), false);
                            }
                        }
                    }
                }
            }
            return structureBlockInfoWorld;
        }
    }

    private void tickWaterFluid(LevelReader worldView, StructureTemplate.StructureBlockInfo structureBlockInfoWorld) {
        ((LevelAccessor) worldView).scheduleTick(structureBlockInfoWorld.pos(), Fluids.WATER, 1);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IAProcessors.FLOOD_WITH_WATER_PROCESSOR.get();
    }
}