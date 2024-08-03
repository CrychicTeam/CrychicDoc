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
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.Fluid;

public class CloseOffAirSourcesProcessor extends StructureProcessor {

    public static final Codec<CloseOffAirSourcesProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.mapPair(BuiltInRegistries.BLOCK.m_194605_().fieldOf("block"), Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight")).codec().listOf().fieldOf("weighted_list_of_replacement_blocks").forGetter(processor -> processor.weightedReplacementBlocks)).apply(instance, instance.stable(CloseOffAirSourcesProcessor::new)));

    private final List<Pair<Block, Integer>> weightedReplacementBlocks;

    public CloseOffAirSourcesProcessor(List<Pair<Block, Integer>> weightedReplacementBlocks) {
        this.weightedReplacementBlocks = weightedReplacementBlocks;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlaceSettings settings) {
        ChunkPos currentChunkPos = new ChunkPos(infoIn2.pos());
        if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(currentChunkPos)) {
            return infoIn2;
        }
        if (!infoIn2.state().m_60819_().isEmpty()) {
            ChunkAccess currentChunk = levelReader.getChunk(currentChunkPos.x, currentChunkPos.z);
            Fluid currentFluid = infoIn2.state().m_60819_().getType();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
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
                            BlockState neighboringState = levelChunkSection.getBlockState(SectionPos.sectionRelative(mutable.m_123341_()), SectionPos.sectionRelative(mutable.m_123342_()), SectionPos.sectionRelative(mutable.m_123343_()));
                            if (neighboringState.m_60795_() || neighboringState.m_60734_() instanceof LiquidBlock && !currentFluid.equals(neighboringState.m_60819_().getType())) {
                                Block replacementBlock;
                                if (this.weightedReplacementBlocks.size() == 1) {
                                    replacementBlock = (Block) ((Pair) this.weightedReplacementBlocks.get(0)).getFirst();
                                } else {
                                    RandomSource random = settings.getRandom(infoIn2.pos());
                                    replacementBlock = GeneralUtils.getRandomEntry(this.weightedReplacementBlocks, random);
                                }
                                levelChunkSection.setBlockState(SectionPos.sectionRelative(mutable.m_123341_()), SectionPos.sectionRelative(mutable.m_123342_()), SectionPos.sectionRelative(mutable.m_123343_()), replacementBlock.defaultBlockState(), false);
                            }
                        }
                    }
                }
            }
        }
        return infoIn2;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IAProcessors.CLOSE_OFF_AIR_SOURCES_PROCESSOR.get();
    }
}