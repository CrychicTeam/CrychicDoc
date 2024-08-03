package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class GlowstoneFeature extends Feature<NoneFeatureConfiguration> {

    public GlowstoneFeature(Codec<NoneFeatureConfiguration> codecNoneFeatureConfiguration0) {
        super(codecNoneFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContextNoneFeatureConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextNoneFeatureConfiguration0.level();
        BlockPos $$2 = featurePlaceContextNoneFeatureConfiguration0.origin();
        RandomSource $$3 = featurePlaceContextNoneFeatureConfiguration0.random();
        if (!$$1.m_46859_($$2)) {
            return false;
        } else {
            BlockState $$4 = $$1.m_8055_($$2.above());
            if (!$$4.m_60713_(Blocks.NETHERRACK) && !$$4.m_60713_(Blocks.BASALT) && !$$4.m_60713_(Blocks.BLACKSTONE)) {
                return false;
            } else {
                $$1.m_7731_($$2, Blocks.GLOWSTONE.defaultBlockState(), 2);
                for (int $$5 = 0; $$5 < 1500; $$5++) {
                    BlockPos $$6 = $$2.offset($$3.nextInt(8) - $$3.nextInt(8), -$$3.nextInt(12), $$3.nextInt(8) - $$3.nextInt(8));
                    if ($$1.m_8055_($$6).m_60795_()) {
                        int $$7 = 0;
                        for (Direction $$8 : Direction.values()) {
                            if ($$1.m_8055_($$6.relative($$8)).m_60713_(Blocks.GLOWSTONE)) {
                                $$7++;
                            }
                            if ($$7 > 1) {
                                break;
                            }
                        }
                        if ($$7 == 1) {
                            $$1.m_7731_($$6, Blocks.GLOWSTONE.defaultBlockState(), 2);
                        }
                    }
                }
                return true;
            }
        }
    }
}