package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class BlockBlobFeature extends Feature<BlockStateConfiguration> {

    public BlockBlobFeature(Codec<BlockStateConfiguration> codecBlockStateConfiguration0) {
        super(codecBlockStateConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStateConfiguration> featurePlaceContextBlockStateConfiguration0) {
        BlockPos $$1 = featurePlaceContextBlockStateConfiguration0.origin();
        WorldGenLevel $$2 = featurePlaceContextBlockStateConfiguration0.level();
        RandomSource $$3 = featurePlaceContextBlockStateConfiguration0.random();
        BlockStateConfiguration $$4;
        for ($$4 = featurePlaceContextBlockStateConfiguration0.config(); $$1.m_123342_() > $$2.m_141937_() + 3; $$1 = $$1.below()) {
            if (!$$2.m_46859_($$1.below())) {
                BlockState $$5 = $$2.m_8055_($$1.below());
                if (m_159759_($$5) || m_159747_($$5)) {
                    break;
                }
            }
        }
        if ($$1.m_123342_() <= $$2.m_141937_() + 3) {
            return false;
        } else {
            for (int $$6 = 0; $$6 < 3; $$6++) {
                int $$7 = $$3.nextInt(2);
                int $$8 = $$3.nextInt(2);
                int $$9 = $$3.nextInt(2);
                float $$10 = (float) ($$7 + $$8 + $$9) * 0.333F + 0.5F;
                for (BlockPos $$11 : BlockPos.betweenClosed($$1.offset(-$$7, -$$8, -$$9), $$1.offset($$7, $$8, $$9))) {
                    if ($$11.m_123331_($$1) <= (double) ($$10 * $$10)) {
                        $$2.m_7731_($$11, $$4.state, 3);
                    }
                }
                $$1 = $$1.offset(-1 + $$3.nextInt(2), -$$3.nextInt(2), -1 + $$3.nextInt(2));
            }
            return true;
        }
    }
}