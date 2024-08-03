package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.PingPongSpongeBlock;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class PingPongSpongeFeature extends Feature<NoneFeatureConfiguration> {

    public PingPongSpongeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        BlockPos.MutableBlockPos trenchBottom = new BlockPos.MutableBlockPos();
        trenchBottom.set(context.origin());
        while (!level.m_8055_(trenchBottom).m_60819_().isEmpty() && trenchBottom.m_123342_() > level.m_141937_()) {
            trenchBottom.move(0, -1, 0);
        }
        if (level.m_8055_(trenchBottom.m_7495_()).m_60713_(ACBlockRegistry.MUCK.get()) && context.origin().m_123342_() - trenchBottom.m_123342_() >= 15) {
            int height = (int) Math.ceil((double) (randomsource.nextFloat() * 3.5F));
            BlockPos genAt = trenchBottom.immutable();
            for (int i = 0; i <= height; i++) {
                BlockPos trunk = genAt.above(i);
                if (canReplace(level.m_8055_(trunk))) {
                    level.m_7731_(trunk, (BlockState) ACBlockRegistry.PING_PONG_SPONGE.get().defaultBlockState().m_61124_(PingPongSpongeBlock.TOP, i == height), 2);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private static boolean canReplace(BlockState state) {
        return !state.m_204336_(ACTagRegistry.UNMOVEABLE) && state.m_60819_().is(FluidTags.WATER);
    }
}