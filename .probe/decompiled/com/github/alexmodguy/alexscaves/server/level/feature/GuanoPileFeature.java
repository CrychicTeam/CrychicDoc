package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.GuanoLayerBlock;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class GuanoPileFeature extends Feature<NoneFeatureConfiguration> {

    public GuanoPileFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        BlockPos.MutableBlockPos pileBottom = new BlockPos.MutableBlockPos();
        pileBottom.set(context.origin());
        if (level.m_8055_(pileBottom).m_60795_() && !level.m_45527_(pileBottom)) {
            while (pileBottom.m_123342_() > level.m_141937_() && level.m_8055_(pileBottom).m_247087_()) {
                pileBottom.move(0, -1, 0);
            }
            int centerLayerHeight = 1 + randomsource.nextInt(3);
            int radius = 3 + randomsource.nextInt(4);
            BlockPos center = pileBottom.immutable().above(centerLayerHeight).below();
            BlockPos.MutableBlockPos side = new BlockPos.MutableBlockPos();
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    int placedLayers = 0;
                    side.set(pileBottom.m_123341_() + x, pileBottom.m_123342_() + placedLayers, pileBottom.m_123343_() + z);
                    while (!level.m_8055_(side).m_247087_() && side.m_123342_() < level.m_151558_()) {
                        side.move(0, 1, 0);
                    }
                    while (level.m_8055_(side).m_247087_() && side.m_123342_() > level.m_141937_()) {
                        side.move(0, -1, 0);
                    }
                    double dist = side.m_203202_((double) center.m_123341_(), (double) side.m_123342_(), (double) center.m_123343_());
                    float seaFloorExtra = (1.0F + ACMath.sampleNoise2D(side.m_123341_(), side.m_123343_(), 6.0F)) * 2.0F;
                    double radiusSq = (double) ((float) radius * ((float) radius - seaFloorExtra));
                    if (dist <= radiusSq) {
                        int y = 0;
                        for (double invDist = 1.0 - dist / (double) ((float) radiusSq); (double) y < (double) centerLayerHeight * invDist; side.move(0, 1, 0)) {
                            BlockState guanoState = ACBlockRegistry.GUANO_LAYER.get().defaultBlockState();
                            if (++y < centerLayerHeight) {
                                guanoState = (BlockState) guanoState.m_61124_(GuanoLayerBlock.f_56581_, 8);
                            } else {
                                int j = Mth.clamp((int) Math.round(8.0 * invDist * invDist) - randomsource.nextInt(2), 1, 8);
                                guanoState = (BlockState) guanoState.m_61124_(GuanoLayerBlock.f_56581_, j);
                            }
                            if (canReplace(level.m_8055_(side)) && !level.m_45527_(side.m_7494_()) && level.m_8055_(side.m_7495_()).m_60838_(level, side.m_7495_())) {
                                level.m_7731_(side, guanoState, 3);
                            }
                        }
                    }
                    if (level.m_45527_(side)) {
                        break;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private static boolean canReplace(BlockState state) {
        return (state.m_60795_() || state.m_247087_()) && !state.m_204336_(ACTagRegistry.UNMOVEABLE) && state.m_60819_().isEmpty();
    }
}