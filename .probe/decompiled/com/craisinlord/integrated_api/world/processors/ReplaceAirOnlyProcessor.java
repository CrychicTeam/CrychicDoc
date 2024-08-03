package com.craisinlord.integrated_api.world.processors;

import com.craisinlord.integrated_api.modinit.IAProcessors;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class ReplaceAirOnlyProcessor extends StructureProcessor {

    public static final Codec<ReplaceAirOnlyProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(BlockState.CODEC.listOf().xmap(Sets::newHashSet, Lists::newArrayList).optionalFieldOf("blocks_to_always_place", new HashSet()).forGetter(config -> config.blocksToAlwaysPlace)).apply(instance, instance.stable(ReplaceAirOnlyProcessor::new)));

    public final HashSet<BlockState> blocksToAlwaysPlace;

    private ReplaceAirOnlyProcessor(HashSet<BlockState> blocksToAlwaysPlace) {
        this.blocksToAlwaysPlace = blocksToAlwaysPlace;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (!this.blocksToAlwaysPlace.contains(structureBlockInfoWorld.state())) {
            BlockPos position = structureBlockInfoWorld.pos();
            BlockState worldState = worldView.m_8055_(position);
            BlockState aboveWorldState = worldView.m_8055_(position.above());
            if (worldState.m_60795_() && !structureBlockInfoWorld.state().m_155947_() && !aboveWorldState.m_155947_()) {
                structureBlockInfoWorld = new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), worldState, null);
            } else if (worldState.m_155947_()) {
                structureBlockInfoWorld = new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), worldState, null);
            }
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IAProcessors.REPLACE_AIR_ONLY_PROCESSOR.get();
    }
}