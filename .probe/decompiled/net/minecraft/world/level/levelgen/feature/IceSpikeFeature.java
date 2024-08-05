package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class IceSpikeFeature extends Feature<NoneFeatureConfiguration> {

    public IceSpikeFeature(Codec<NoneFeatureConfiguration> codecNoneFeatureConfiguration0) {
        super(codecNoneFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContextNoneFeatureConfiguration0) {
        BlockPos $$1 = featurePlaceContextNoneFeatureConfiguration0.origin();
        RandomSource $$2 = featurePlaceContextNoneFeatureConfiguration0.random();
        WorldGenLevel $$3 = featurePlaceContextNoneFeatureConfiguration0.level();
        while ($$3.m_46859_($$1) && $$1.m_123342_() > $$3.m_141937_() + 2) {
            $$1 = $$1.below();
        }
        if (!$$3.m_8055_($$1).m_60713_(Blocks.SNOW_BLOCK)) {
            return false;
        } else {
            $$1 = $$1.above($$2.nextInt(4));
            int $$4 = $$2.nextInt(4) + 7;
            int $$5 = $$4 / 4 + $$2.nextInt(2);
            if ($$5 > 1 && $$2.nextInt(60) == 0) {
                $$1 = $$1.above(10 + $$2.nextInt(30));
            }
            for (int $$6 = 0; $$6 < $$4; $$6++) {
                float $$7 = (1.0F - (float) $$6 / (float) $$4) * (float) $$5;
                int $$8 = Mth.ceil($$7);
                for (int $$9 = -$$8; $$9 <= $$8; $$9++) {
                    float $$10 = (float) Mth.abs($$9) - 0.25F;
                    for (int $$11 = -$$8; $$11 <= $$8; $$11++) {
                        float $$12 = (float) Mth.abs($$11) - 0.25F;
                        if (($$9 == 0 && $$11 == 0 || !($$10 * $$10 + $$12 * $$12 > $$7 * $$7)) && ($$9 != -$$8 && $$9 != $$8 && $$11 != -$$8 && $$11 != $$8 || !($$2.nextFloat() > 0.75F))) {
                            BlockState $$13 = $$3.m_8055_($$1.offset($$9, $$6, $$11));
                            if ($$13.m_60795_() || m_159759_($$13) || $$13.m_60713_(Blocks.SNOW_BLOCK) || $$13.m_60713_(Blocks.ICE)) {
                                this.m_5974_($$3, $$1.offset($$9, $$6, $$11), Blocks.PACKED_ICE.defaultBlockState());
                            }
                            if ($$6 != 0 && $$8 > 1) {
                                $$13 = $$3.m_8055_($$1.offset($$9, -$$6, $$11));
                                if ($$13.m_60795_() || m_159759_($$13) || $$13.m_60713_(Blocks.SNOW_BLOCK) || $$13.m_60713_(Blocks.ICE)) {
                                    this.m_5974_($$3, $$1.offset($$9, -$$6, $$11), Blocks.PACKED_ICE.defaultBlockState());
                                }
                            }
                        }
                    }
                }
            }
            int $$14 = $$5 - 1;
            if ($$14 < 0) {
                $$14 = 0;
            } else if ($$14 > 1) {
                $$14 = 1;
            }
            for (int $$15 = -$$14; $$15 <= $$14; $$15++) {
                for (int $$16 = -$$14; $$16 <= $$14; $$16++) {
                    BlockPos $$17 = $$1.offset($$15, -1, $$16);
                    int $$18 = 50;
                    if (Math.abs($$15) == 1 && Math.abs($$16) == 1) {
                        $$18 = $$2.nextInt(5);
                    }
                    while ($$17.m_123342_() > 50) {
                        BlockState $$19 = $$3.m_8055_($$17);
                        if (!$$19.m_60795_() && !m_159759_($$19) && !$$19.m_60713_(Blocks.SNOW_BLOCK) && !$$19.m_60713_(Blocks.ICE) && !$$19.m_60713_(Blocks.PACKED_ICE)) {
                            break;
                        }
                        this.m_5974_($$3, $$17, Blocks.PACKED_ICE.defaultBlockState());
                        $$17 = $$17.below();
                        if (--$$18 <= 0) {
                            $$17 = $$17.below($$2.nextInt(5) + 1);
                            $$18 = $$2.nextInt(5);
                        }
                    }
                }
            }
            return true;
        }
    }
}