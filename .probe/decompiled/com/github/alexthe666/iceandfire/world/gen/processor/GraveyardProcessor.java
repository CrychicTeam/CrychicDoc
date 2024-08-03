package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.world.IafProcessors;
import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

public class GraveyardProcessor extends StructureProcessor {

    private final float integrity = 1.0F;

    public static final GraveyardProcessor INSTANCE = new GraveyardProcessor();

    public static final Codec<GraveyardProcessor> CODEC = Codec.unit(() -> INSTANCE);

    public static BlockState getRandomCobblestone(@Nullable BlockState prev, RandomSource random) {
        float rand = random.nextFloat();
        if ((double) rand < 0.5) {
            return Blocks.COBBLESTONE.defaultBlockState();
        } else {
            return (double) rand < 0.9 ? Blocks.MOSSY_COBBLESTONE.defaultBlockState() : Blocks.INFESTED_COBBLESTONE.defaultBlockState();
        }
    }

    public static BlockState getRandomCrackedBlock(@Nullable BlockState prev, RandomSource random) {
        float rand = random.nextFloat();
        if ((double) rand < 0.5) {
            return Blocks.STONE_BRICKS.defaultBlockState();
        } else {
            return (double) rand < 0.9 ? Blocks.CRACKED_STONE_BRICKS.defaultBlockState() : Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
        }
    }

    public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader worldReader, @NotNull BlockPos pos, @NotNull BlockPos pos2, @NotNull StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        RandomSource random = settings.getRandom(infoIn2.pos());
        if (infoIn2.state().m_60734_() == Blocks.STONE_BRICKS) {
            BlockState state = getRandomCrackedBlock(null, random);
            return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), state, null);
        } else if (infoIn2.state().m_60734_() == Blocks.COBBLESTONE) {
            BlockState state = getRandomCobblestone(null, random);
            return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), state, null);
        } else {
            return infoIn2;
        }
    }

    @NotNull
    @Override
    protected StructureProcessorType getType() {
        return IafProcessors.GRAVEYARDPROCESSOR.get();
    }
}