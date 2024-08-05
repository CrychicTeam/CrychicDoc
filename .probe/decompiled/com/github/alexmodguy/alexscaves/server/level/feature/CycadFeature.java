package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.CycadBlock;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CycadFeature extends Feature<NoneFeatureConfiguration> {

    public CycadFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        BlockPos treeBottom = context.origin();
        if (!level.m_8055_(treeBottom.below()).m_204336_(BlockTags.DIRT)) {
            return false;
        } else {
            int height = 1 + (int) Math.ceil((double) (randomsource.nextFloat() * 2.5F));
            for (int i = 0; i <= height; i++) {
                BlockPos trunk = treeBottom.above(i);
                if (canReplace(level.m_8055_(trunk))) {
                    level.m_7731_(trunk, (BlockState) ACBlockRegistry.CYCAD.get().defaultBlockState().m_61124_(CycadBlock.TOP, i == height), 2);
                }
            }
            return true;
        }
    }

    private static boolean canReplace(BlockState state) {
        return (state.m_60795_() || state.m_247087_()) && !state.m_204336_(ACTagRegistry.UNMOVEABLE) && state.m_60819_().isEmpty();
    }
}