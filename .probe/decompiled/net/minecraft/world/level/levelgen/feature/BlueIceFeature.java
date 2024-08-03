package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BlueIceFeature extends Feature<NoneFeatureConfiguration> {

    public BlueIceFeature(Codec<NoneFeatureConfiguration> codecNoneFeatureConfiguration0) {
        super(codecNoneFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContextNoneFeatureConfiguration0) {
        BlockPos $$1 = featurePlaceContextNoneFeatureConfiguration0.origin();
        WorldGenLevel $$2 = featurePlaceContextNoneFeatureConfiguration0.level();
        RandomSource $$3 = featurePlaceContextNoneFeatureConfiguration0.random();
        if ($$1.m_123342_() > $$2.m_5736_() - 1) {
            return false;
        } else if (!$$2.m_8055_($$1).m_60713_(Blocks.WATER) && !$$2.m_8055_($$1.below()).m_60713_(Blocks.WATER)) {
            return false;
        } else {
            boolean $$4 = false;
            for (Direction $$5 : Direction.values()) {
                if ($$5 != Direction.DOWN && $$2.m_8055_($$1.relative($$5)).m_60713_(Blocks.PACKED_ICE)) {
                    $$4 = true;
                    break;
                }
            }
            if (!$$4) {
                return false;
            } else {
                $$2.m_7731_($$1, Blocks.BLUE_ICE.defaultBlockState(), 2);
                for (int $$6 = 0; $$6 < 200; $$6++) {
                    int $$7 = $$3.nextInt(5) - $$3.nextInt(6);
                    int $$8 = 3;
                    if ($$7 < 2) {
                        $$8 += $$7 / 2;
                    }
                    if ($$8 >= 1) {
                        BlockPos $$9 = $$1.offset($$3.nextInt($$8) - $$3.nextInt($$8), $$7, $$3.nextInt($$8) - $$3.nextInt($$8));
                        BlockState $$10 = $$2.m_8055_($$9);
                        if ($$10.m_60795_() || $$10.m_60713_(Blocks.WATER) || $$10.m_60713_(Blocks.PACKED_ICE) || $$10.m_60713_(Blocks.ICE)) {
                            for (Direction $$11 : Direction.values()) {
                                BlockState $$12 = $$2.m_8055_($$9.relative($$11));
                                if ($$12.m_60713_(Blocks.BLUE_ICE)) {
                                    $$2.m_7731_($$9, Blocks.BLUE_ICE.defaultBlockState(), 2);
                                    break;
                                }
                            }
                        }
                    }
                }
                return true;
            }
        }
    }
}