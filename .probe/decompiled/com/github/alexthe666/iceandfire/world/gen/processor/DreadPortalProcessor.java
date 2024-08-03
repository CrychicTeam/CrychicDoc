package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

public class DreadPortalProcessor extends StructureProcessor {

    private final float integrity = 1.0F;

    public DreadPortalProcessor(BlockPos position, StructurePlaceSettings settings, Biome biome) {
    }

    public static BlockState getRandomCrackedBlock(@Nullable BlockState prev, RandomSource random) {
        float rand = random.nextFloat();
        if ((double) rand < 0.3) {
            return IafBlockRegistry.DREAD_STONE_BRICKS.get().m_49966_();
        } else {
            return (double) rand < 0.6 ? IafBlockRegistry.DREAD_STONE_BRICKS_CRACKED.get().m_49966_() : IafBlockRegistry.DREAD_STONE_BRICKS_MOSSY.get().m_49966_();
        }
    }

    @Nullable
    public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockPos p_230386_3_, @NotNull StructureTemplate.StructureBlockInfo blockInfoIn, @NotNull StructureTemplate.StructureBlockInfo p_230386_5_, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        RandomSource random = settings.getRandom(pos);
        if (random.nextFloat() <= 1.0F) {
            if (blockInfoIn.state().m_60734_() == Blocks.DIAMOND_BLOCK) {
                return new StructureTemplate.StructureBlockInfo(pos, IafBlockRegistry.DREAD_PORTAL.get().defaultBlockState(), null);
            } else if (blockInfoIn.state().m_60734_() == IafBlockRegistry.DREAD_STONE_BRICKS.get()) {
                BlockState state = getRandomCrackedBlock(null, random);
                return new StructureTemplate.StructureBlockInfo(pos, state, null);
            } else {
                return blockInfoIn;
            }
        } else {
            return blockInfoIn;
        }
    }

    @NotNull
    @Override
    protected StructureProcessorType getType() {
        return StructureProcessorType.BLOCK_ROT;
    }
}