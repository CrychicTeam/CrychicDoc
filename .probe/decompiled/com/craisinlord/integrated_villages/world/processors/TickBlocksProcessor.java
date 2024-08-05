package com.craisinlord.integrated_villages.world.processors;

import com.craisinlord.integrated_villages.IntegratedVilagesProcessors;
import com.craisinlord.integrated_villages.IntegratedVillages;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class TickBlocksProcessor extends StructureProcessor {

    public static final Codec<TickBlocksProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(BuiltInRegistries.BLOCK.m_194605_().listOf().fieldOf("blocks_to_tick").orElse(new ArrayList()).xmap(HashSet::new, ArrayList::new).forGetter(config -> config.blocksToTick)).apply(instance, instance.stable(TickBlocksProcessor::new)));

    private final HashSet<Block> blocksToTick;

    private TickBlocksProcessor(HashSet<Block> blocksToTick) {
        this.blocksToTick = blocksToTick;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (this.blocksToTick.contains(structureBlockInfoWorld.state().m_60734_()) && IntegratedVillages.CONFIG.general.activateCreateContraptions) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(structureBlockInfoWorld.pos()))) {
                return structureBlockInfoWorld;
            }
            ChunkAccess chunk = levelReader.getChunk(structureBlockInfoWorld.pos());
            int minY = chunk.getMinBuildHeight();
            int maxY = chunk.m_151558_();
            int currentY = structureBlockInfoWorld.pos().m_123342_();
            if (currentY >= minY && currentY <= maxY) {
                ((LevelAccessor) levelReader).scheduleTick(structureBlockInfoWorld.pos(), structureBlockInfoWorld.state().m_60734_(), 0);
            }
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IntegratedVilagesProcessors.TICK_BLOCKS_PROCESSOR.get();
    }
}