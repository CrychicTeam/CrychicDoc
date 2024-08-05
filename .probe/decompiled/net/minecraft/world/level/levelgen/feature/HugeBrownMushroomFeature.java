package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

public class HugeBrownMushroomFeature extends AbstractHugeMushroomFeature {

    public HugeBrownMushroomFeature(Codec<HugeMushroomFeatureConfiguration> codecHugeMushroomFeatureConfiguration0) {
        super(codecHugeMushroomFeatureConfiguration0);
    }

    @Override
    protected void makeCap(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos blockPos2, int int3, BlockPos.MutableBlockPos blockPosMutableBlockPos4, HugeMushroomFeatureConfiguration hugeMushroomFeatureConfiguration5) {
        int $$6 = hugeMushroomFeatureConfiguration5.foliageRadius;
        for (int $$7 = -$$6; $$7 <= $$6; $$7++) {
            for (int $$8 = -$$6; $$8 <= $$6; $$8++) {
                boolean $$9 = $$7 == -$$6;
                boolean $$10 = $$7 == $$6;
                boolean $$11 = $$8 == -$$6;
                boolean $$12 = $$8 == $$6;
                boolean $$13 = $$9 || $$10;
                boolean $$14 = $$11 || $$12;
                if (!$$13 || !$$14) {
                    blockPosMutableBlockPos4.setWithOffset(blockPos2, $$7, int3, $$8);
                    if (!levelAccessor0.m_8055_(blockPosMutableBlockPos4).m_60804_(levelAccessor0, blockPosMutableBlockPos4)) {
                        boolean $$15 = $$9 || $$14 && $$7 == 1 - $$6;
                        boolean $$16 = $$10 || $$14 && $$7 == $$6 - 1;
                        boolean $$17 = $$11 || $$13 && $$8 == 1 - $$6;
                        boolean $$18 = $$12 || $$13 && $$8 == $$6 - 1;
                        BlockState $$19 = hugeMushroomFeatureConfiguration5.capProvider.getState(randomSource1, blockPos2);
                        if ($$19.m_61138_(HugeMushroomBlock.WEST) && $$19.m_61138_(HugeMushroomBlock.EAST) && $$19.m_61138_(HugeMushroomBlock.NORTH) && $$19.m_61138_(HugeMushroomBlock.SOUTH)) {
                            $$19 = (BlockState) ((BlockState) ((BlockState) ((BlockState) $$19.m_61124_(HugeMushroomBlock.WEST, $$15)).m_61124_(HugeMushroomBlock.EAST, $$16)).m_61124_(HugeMushroomBlock.NORTH, $$17)).m_61124_(HugeMushroomBlock.SOUTH, $$18);
                        }
                        this.m_5974_(levelAccessor0, blockPosMutableBlockPos4, $$19);
                    }
                }
            }
        }
    }

    @Override
    protected int getTreeRadiusForHeight(int int0, int int1, int int2, int int3) {
        return int3 <= 3 ? 0 : int2;
    }
}