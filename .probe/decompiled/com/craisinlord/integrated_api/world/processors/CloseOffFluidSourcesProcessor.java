package com.craisinlord.integrated_api.world.processors;

import com.craisinlord.integrated_api.modinit.IAProcessors;
import com.craisinlord.integrated_api.utils.GeneralUtils;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;

public class CloseOffFluidSourcesProcessor extends StructureProcessor {

    public static final Codec<CloseOffFluidSourcesProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.mapPair(BuiltInRegistries.BLOCK.m_194605_().fieldOf("block"), Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight")).codec().listOf().fieldOf("weighted_list_of_replacement_blocks").forGetter(processor -> processor.weightedReplacementBlocks), Codec.BOOL.fieldOf("ignore_down").orElse(false).forGetter(processor -> processor.ignoreDown), Codec.BOOL.fieldOf("if_air_in_world").orElse(false).forGetter(processor -> processor.ifAirInWorld)).apply(instance, instance.stable(CloseOffFluidSourcesProcessor::new)));

    private final List<Pair<Block, Integer>> weightedReplacementBlocks;

    private final boolean ignoreDown;

    private final boolean ifAirInWorld;

    public CloseOffFluidSourcesProcessor(List<Pair<Block, Integer>> weightedReplacementBlocks, boolean ignoreDown, boolean ifAirInWorld) {
        this.weightedReplacementBlocks = weightedReplacementBlocks;
        this.ignoreDown = ignoreDown;
        this.ifAirInWorld = ifAirInWorld;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlaceSettings settings) {
        ChunkPos currentChunkPos = new ChunkPos(infoIn2.pos());
        if (!infoIn2.state().m_60713_(Blocks.STRUCTURE_VOID) && infoIn2.state().m_60819_().isEmpty()) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(currentChunkPos)) {
                return infoIn2;
            }
            if (!GeneralUtils.isFullCube(levelReader, infoIn2.pos(), infoIn2.state()) || !infoIn2.state().m_280555_()) {
                ChunkAccess currentChunk = levelReader.getChunk(currentChunkPos.x, currentChunkPos.z);
                if (this.ifAirInWorld && !currentChunk.m_8055_(infoIn2.pos()).m_60795_()) {
                    return infoIn2;
                }
                BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                for (Direction direction : Direction.values()) {
                    if (!this.ignoreDown || direction != Direction.DOWN) {
                        mutable.set(infoIn2.pos()).move(direction);
                        if (mutable.m_123342_() >= currentChunk.getMinBuildHeight() && mutable.m_123342_() < currentChunk.m_151558_()) {
                            if (currentChunkPos.x != mutable.m_123341_() >> 4 || currentChunkPos.z != mutable.m_123343_() >> 4) {
                                currentChunk = levelReader.getChunk(mutable);
                                currentChunkPos = new ChunkPos(mutable);
                            }
                            LevelHeightAccessor levelHeightAccessor = currentChunk.getHeightAccessorForGeneration();
                            if (levelReader instanceof WorldGenLevel && mutable.m_123342_() >= levelHeightAccessor.getMinBuildHeight() && mutable.m_123342_() < levelHeightAccessor.getMaxBuildHeight()) {
                                int sectionYIndex = currentChunk.m_151564_(mutable.m_123342_());
                                LevelChunkSection levelChunkSection = currentChunk.getSection(sectionYIndex);
                                if (levelChunkSection != null) {
                                    FluidState fluidState = levelChunkSection.getFluidState(SectionPos.sectionRelative(mutable.m_123341_()), SectionPos.sectionRelative(mutable.m_123342_()), SectionPos.sectionRelative(mutable.m_123343_()));
                                    if (fluidState.isSource()) {
                                        RandomSource random = settings.getRandom(infoIn2.pos());
                                        Block replacementBlock = GeneralUtils.getRandomEntry(this.weightedReplacementBlocks, random);
                                        levelChunkSection.setBlockState(SectionPos.sectionRelative(mutable.m_123341_()), SectionPos.sectionRelative(mutable.m_123342_()), SectionPos.sectionRelative(mutable.m_123343_()), replacementBlock.defaultBlockState(), false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return infoIn2;
        } else {
            return infoIn2;
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IAProcessors.CLOSE_OFF_FLUID_SOURCES_PROCESSOR.get();
    }
}