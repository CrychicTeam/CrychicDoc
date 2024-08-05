package com.craisinlord.integrated_api.world.processors;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.modinit.IAProcessors;
import com.craisinlord.integrated_api.utils.PlatformHooks;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class IntegratedBlockReplaceProcessor extends StructureProcessor {

    public static final Codec<IntegratedBlockReplaceProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(BuiltInRegistries.BLOCK.m_194605_().fieldOf("input_block").forGetter(config -> config.inputBlock), Codec.STRING.fieldOf("required_mod").forGetter(config -> config.requiredMod), BuiltInRegistries.BLOCK.m_194605_().optionalFieldOf("output_block").forGetter(config -> config.outputBlock), BuiltInRegistries.BLOCK.m_194605_().listOf().optionalFieldOf("output_blocks", ImmutableList.of()).forGetter(config -> config.outputBlocks), Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(config -> config.probability), BuiltInRegistries.BLOCK.m_194605_().optionalFieldOf("otherwise_block").forGetter(config -> config.otherwiseBlock)).apply(instance, instance.stable(IntegratedBlockReplaceProcessor::new)));

    private final Block inputBlock;

    private final String requiredMod;

    private final Optional<Block> outputBlock;

    private final List<Block> outputBlocks;

    private final float probability;

    private final Optional<Block> otherwiseBlock;

    private IntegratedBlockReplaceProcessor(Block inputBlock, String requiredMod, Optional<Block> outputBlock, List<Block> outputBlocks, float probability, Optional<Block> otherwiseBlock) {
        this.inputBlock = inputBlock;
        this.requiredMod = requiredMod;
        this.outputBlock = outputBlock;
        this.outputBlocks = outputBlocks;
        this.probability = probability;
        this.otherwiseBlock = otherwiseBlock;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (structureBlockInfoWorld.state().m_60734_() == this.inputBlock) {
            if (PlatformHooks.isModLoaded(this.requiredMod)) {
                RandomSource random = RandomSource.create();
                if (random.nextFloat() < this.probability) {
                    if (this.outputBlock.isPresent()) {
                        BlockState newBlockState = ((Block) this.outputBlock.get()).defaultBlockState();
                        return new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), newBlockState, structureBlockInfoWorld.nbt());
                    }
                    if (!this.outputBlocks.isEmpty()) {
                        BlockState newBlockState = ((Block) this.outputBlocks.get(random.nextInt(this.outputBlocks.size()))).defaultBlockState();
                        return new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), newBlockState, structureBlockInfoWorld.nbt());
                    }
                    IntegratedAPI.LOGGER.warn("Integrated API: integrated_api:integrated_block_replace_processor in a processor file has no replacement block of any kind.");
                }
            } else if (this.otherwiseBlock.isPresent()) {
                BlockState newBlockState = ((Block) this.otherwiseBlock.get()).defaultBlockState();
                return new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), newBlockState, structureBlockInfoWorld.nbt());
            }
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IAProcessors.INTEGRATED_BLOCK_REPLACE_PROCESSOR.get();
    }
}