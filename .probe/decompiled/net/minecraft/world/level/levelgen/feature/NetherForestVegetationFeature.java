package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;

public class NetherForestVegetationFeature extends Feature<NetherForestVegetationConfig> {

    public NetherForestVegetationFeature(Codec<NetherForestVegetationConfig> codecNetherForestVegetationConfig0) {
        super(codecNetherForestVegetationConfig0);
    }

    @Override
    public boolean place(FeaturePlaceContext<NetherForestVegetationConfig> featurePlaceContextNetherForestVegetationConfig0) {
        WorldGenLevel $$1 = featurePlaceContextNetherForestVegetationConfig0.level();
        BlockPos $$2 = featurePlaceContextNetherForestVegetationConfig0.origin();
        BlockState $$3 = $$1.m_8055_($$2.below());
        NetherForestVegetationConfig $$4 = featurePlaceContextNetherForestVegetationConfig0.config();
        RandomSource $$5 = featurePlaceContextNetherForestVegetationConfig0.random();
        if (!$$3.m_204336_(BlockTags.NYLIUM)) {
            return false;
        } else {
            int $$6 = $$2.m_123342_();
            if ($$6 >= $$1.m_141937_() + 1 && $$6 + 1 < $$1.m_151558_()) {
                int $$7 = 0;
                for (int $$8 = 0; $$8 < $$4.spreadWidth * $$4.spreadWidth; $$8++) {
                    BlockPos $$9 = $$2.offset($$5.nextInt($$4.spreadWidth) - $$5.nextInt($$4.spreadWidth), $$5.nextInt($$4.spreadHeight) - $$5.nextInt($$4.spreadHeight), $$5.nextInt($$4.spreadWidth) - $$5.nextInt($$4.spreadWidth));
                    BlockState $$10 = $$4.f_67540_.getState($$5, $$9);
                    if ($$1.m_46859_($$9) && $$9.m_123342_() > $$1.m_141937_() && $$10.m_60710_($$1, $$9)) {
                        $$1.m_7731_($$9, $$10, 2);
                        $$7++;
                    }
                }
                return $$7 > 0;
            } else {
                return false;
            }
        }
    }
}