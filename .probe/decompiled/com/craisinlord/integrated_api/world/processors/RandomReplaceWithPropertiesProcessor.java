package com.craisinlord.integrated_api.world.processors;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.modinit.IAProcessors;
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
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class RandomReplaceWithPropertiesProcessor extends StructureProcessor {

    public static final Codec<RandomReplaceWithPropertiesProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(BuiltInRegistries.BLOCK.m_194605_().fieldOf("input_block").forGetter(config -> config.inputBlock), BuiltInRegistries.BLOCK.m_194605_().optionalFieldOf("output_block").forGetter(config -> config.outputBlock), BuiltInRegistries.BLOCK.m_194605_().listOf().optionalFieldOf("output_blocks", ImmutableList.of()).forGetter(config -> config.outputBlocks), Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(config -> config.probability)).apply(instance, instance.stable(RandomReplaceWithPropertiesProcessor::new)));

    private final Block inputBlock;

    private final Optional<Block> outputBlock;

    private final List<Block> outputBlocks;

    private final float probability;

    public RandomReplaceWithPropertiesProcessor(Block inputBlock, Optional<Block> outputBlock, List<Block> outputBlocks, float probability) {
        this.inputBlock = inputBlock;
        this.outputBlock = outputBlock;
        this.outputBlocks = outputBlocks;
        this.probability = probability;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlaceSettings settings) {
        if (infoIn2.state().m_60734_() == this.inputBlock) {
            BlockPos worldPos = infoIn2.pos();
            RandomSource random = RandomSource.create();
            int offSet = settings.getProcessors().indexOf(this) + 1;
            random.setSeed(worldPos.asLong() * worldPos.asLong() * (long) offSet);
            if (random.nextFloat() < this.probability) {
                if (this.outputBlock.isPresent()) {
                    BlockState newBlockState = ((Block) this.outputBlock.get()).defaultBlockState();
                    for (Property<?> property : infoIn2.state().m_61147_()) {
                        if (newBlockState.m_61138_(property)) {
                            newBlockState = this.getStateWithProperty(newBlockState, infoIn2.state(), property);
                        }
                    }
                    return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), newBlockState, infoIn2.nbt());
                }
                if (!this.outputBlocks.isEmpty()) {
                    BlockState newBlockState = ((Block) this.outputBlocks.get(random.nextInt(this.outputBlocks.size()))).defaultBlockState();
                    for (Property<?> propertyx : infoIn2.state().m_61147_()) {
                        if (newBlockState.m_61138_(propertyx)) {
                            newBlockState = this.getStateWithProperty(newBlockState, infoIn2.state(), propertyx);
                        }
                    }
                    return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), newBlockState, infoIn2.nbt());
                }
                IntegratedAPI.LOGGER.warn("Integrated API: integrated_api:random_replace_with_properties_processor in a processor file has no replacement block of any kind.");
            }
        }
        return infoIn2;
    }

    private <T extends Comparable<T>> BlockState getStateWithProperty(BlockState state, BlockState stateToCopy, Property<T> property) {
        return (BlockState) state.m_61124_(property, stateToCopy.m_61143_(property));
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IAProcessors.RANDOM_REPLACE_WITH_PROPERTIES_PROCESSOR.get();
    }
}