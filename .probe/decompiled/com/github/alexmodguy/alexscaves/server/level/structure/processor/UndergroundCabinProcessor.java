package com.github.alexmodguy.alexscaves.server.level.structure.processor;

import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class UndergroundCabinProcessor extends StructureProcessor {

    private static final UndergroundCabinProcessor INSTANCE = new UndergroundCabinProcessor();

    public static final Codec<UndergroundCabinProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPosUnused, BlockPos pos, StructureTemplate.StructureBlockInfo relativeInfo, StructureTemplate.StructureBlockInfo info, StructurePlaceSettings settings) {
        RandomSource randomsource = settings.getRandom(info.pos());
        BlockState in = info.state();
        if (blockPosUnused.m_123342_() < 0) {
            if (in.m_60713_(Blocks.COBBLESTONE) || in.m_60713_(Blocks.STONE)) {
                return new StructureTemplate.StructureBlockInfo(info.pos(), Blocks.COBBLED_DEEPSLATE.defaultBlockState(), info.nbt());
            }
            if (in.m_60713_(Blocks.COBBLESTONE_STAIRS) || in.m_60713_(Blocks.STONE_STAIRS)) {
                return new StructureTemplate.StructureBlockInfo(info.pos(), copyBlockStateProperties(in, Blocks.COBBLED_DEEPSLATE_STAIRS.defaultBlockState()), info.nbt());
            }
            if (in.m_60713_(Blocks.COBBLESTONE_SLAB) || in.m_60713_(Blocks.STONE_SLAB)) {
                return new StructureTemplate.StructureBlockInfo(info.pos(), copyBlockStateProperties(in, Blocks.COBBLED_DEEPSLATE_SLAB.defaultBlockState()), info.nbt());
            }
            if (in.m_60713_(Blocks.COBBLESTONE_WALL) || in.m_60713_(Blocks.STONE_BRICK_WALL)) {
                return new StructureTemplate.StructureBlockInfo(info.pos(), copyBlockStateProperties(in, Blocks.COBBLED_DEEPSLATE_WALL.defaultBlockState()), info.nbt());
            }
        } else if (in.m_60713_(Blocks.COBBLESTONE) && (double) randomsource.nextFloat() < 0.2) {
            if (randomsource.nextFloat() > 0.3F) {
                return null;
            }
            return new StructureTemplate.StructureBlockInfo(info.pos(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(), info.nbt());
        }
        if (in.m_204336_(BlockTags.LOGS) || in.m_204336_(BlockTags.PLANKS)) {
            float above = (float) relativeInfo.pos().m_123342_() / 7.0F;
            float woodDecay = levelReader.m_8055_(info.pos()).m_60795_() ? 0.9F : 0.2F;
            if (above * randomsource.nextFloat() > woodDecay) {
                return null;
            }
        }
        return info;
    }

    private static BlockState copyBlockStateProperties(BlockState from, BlockState to) {
        for (Property prop : from.m_61147_()) {
            to = to.m_61138_(prop) ? (BlockState) to.m_61124_(prop, from.m_61143_(prop)) : to;
        }
        return to;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ACStructureProcessorRegistry.UNDERGROUND_CABIN.get();
    }
}