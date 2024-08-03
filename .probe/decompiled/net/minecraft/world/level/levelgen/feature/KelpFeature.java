package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class KelpFeature extends Feature<NoneFeatureConfiguration> {

    public KelpFeature(Codec<NoneFeatureConfiguration> codecNoneFeatureConfiguration0) {
        super(codecNoneFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContextNoneFeatureConfiguration0) {
        int $$1 = 0;
        WorldGenLevel $$2 = featurePlaceContextNoneFeatureConfiguration0.level();
        BlockPos $$3 = featurePlaceContextNoneFeatureConfiguration0.origin();
        RandomSource $$4 = featurePlaceContextNoneFeatureConfiguration0.random();
        int $$5 = $$2.m_6924_(Heightmap.Types.OCEAN_FLOOR, $$3.m_123341_(), $$3.m_123343_());
        BlockPos $$6 = new BlockPos($$3.m_123341_(), $$5, $$3.m_123343_());
        if ($$2.m_8055_($$6).m_60713_(Blocks.WATER)) {
            BlockState $$7 = Blocks.KELP.defaultBlockState();
            BlockState $$8 = Blocks.KELP_PLANT.defaultBlockState();
            int $$9 = 1 + $$4.nextInt(10);
            for (int $$10 = 0; $$10 <= $$9; $$10++) {
                if ($$2.m_8055_($$6).m_60713_(Blocks.WATER) && $$2.m_8055_($$6.above()).m_60713_(Blocks.WATER) && $$8.m_60710_($$2, $$6)) {
                    if ($$10 == $$9) {
                        $$2.m_7731_($$6, (BlockState) $$7.m_61124_(KelpBlock.f_53924_, $$4.nextInt(4) + 20), 2);
                        $$1++;
                    } else {
                        $$2.m_7731_($$6, $$8, 2);
                    }
                } else if ($$10 > 0) {
                    BlockPos $$11 = $$6.below();
                    if ($$7.m_60710_($$2, $$11) && !$$2.m_8055_($$11.below()).m_60713_(Blocks.KELP)) {
                        $$2.m_7731_($$11, (BlockState) $$7.m_61124_(KelpBlock.f_53924_, $$4.nextInt(4) + 20), 2);
                        $$1++;
                    }
                    break;
                }
                $$6 = $$6.above();
            }
        }
        return $$1 > 0;
    }
}