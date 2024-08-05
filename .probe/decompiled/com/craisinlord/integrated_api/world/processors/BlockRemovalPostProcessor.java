package com.craisinlord.integrated_api.world.processors;

import com.craisinlord.integrated_api.modinit.IAProcessors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class BlockRemovalPostProcessor extends StructureProcessor {

    public static final Codec<BlockRemovalPostProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(BuiltInRegistries.BLOCK.m_194605_().listOf().fieldOf("remove_blocks").orElse(new ArrayList()).xmap(HashSet::new, ArrayList::new).forGetter(config -> config.removeBlocks)).apply(instance, instance.stable(BlockRemovalPostProcessor::new)));

    private final HashSet<Block> removeBlocks;

    private BlockRemovalPostProcessor(HashSet<Block> removeBlocks) {
        this.removeBlocks = removeBlocks;
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor serverLevelAccessor, BlockPos blockPos, BlockPos blockPos2, List<StructureTemplate.StructureBlockInfo> list, List<StructureTemplate.StructureBlockInfo> list2, StructurePlaceSettings structurePlaceSettings) {
        if (this.removeBlocks.isEmpty()) {
            return list2;
        } else {
            for (int i = list2.size() - 1; i >= 0; i--) {
                if (this.removeBlocks.contains(((StructureTemplate.StructureBlockInfo) list2.get(i)).state().m_60734_())) {
                    list2.remove(i);
                }
            }
            return list2;
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IAProcessors.BLOCK_REMOVAL_POST_PROCESSOR.get();
    }
}