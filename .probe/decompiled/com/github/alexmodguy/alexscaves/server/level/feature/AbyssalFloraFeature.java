package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.level.feature.config.AbyssalFloraFeatureConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class AbyssalFloraFeature extends Feature<AbyssalFloraFeatureConfiguration> {

    public AbyssalFloraFeature(Codec<AbyssalFloraFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<AbyssalFloraFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        BlockPos.MutableBlockPos trenchBottom = new BlockPos.MutableBlockPos();
        trenchBottom.set(context.origin());
        while (!level.m_8055_(trenchBottom).m_60819_().isEmpty() && trenchBottom.m_123342_() > level.m_141937_()) {
            trenchBottom.move(0, -1, 0);
        }
        if (context.origin().m_123342_() - trenchBottom.m_123342_() < 15) {
            return false;
        } else {
            BlockPos above = trenchBottom.m_7494_();
            if (canReplace(level.m_8055_(above))) {
                level.m_7731_(above, context.config().floraBlock.getState(randomsource, above), 2);
            }
            return true;
        }
    }

    private static boolean canReplace(BlockState state) {
        return state.m_60819_().is(FluidTags.WATER) && state.m_278721_();
    }
}