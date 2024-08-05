package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

public class HugeRedMushroomFeature extends AbstractHugeMushroomFeature {

    public HugeRedMushroomFeature(Codec<HugeMushroomFeatureConfiguration> codecHugeMushroomFeatureConfiguration0) {
        super(codecHugeMushroomFeatureConfiguration0);
    }

    @Override
    protected void makeCap(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos blockPos2, int int3, BlockPos.MutableBlockPos blockPosMutableBlockPos4, HugeMushroomFeatureConfiguration hugeMushroomFeatureConfiguration5) {
        for (int $$6 = int3 - 3; $$6 <= int3; $$6++) {
            int $$7 = $$6 < int3 ? hugeMushroomFeatureConfiguration5.foliageRadius : hugeMushroomFeatureConfiguration5.foliageRadius - 1;
            int $$8 = hugeMushroomFeatureConfiguration5.foliageRadius - 2;
            for (int $$9 = -$$7; $$9 <= $$7; $$9++) {
                for (int $$10 = -$$7; $$10 <= $$7; $$10++) {
                    boolean $$11 = $$9 == -$$7;
                    boolean $$12 = $$9 == $$7;
                    boolean $$13 = $$10 == -$$7;
                    boolean $$14 = $$10 == $$7;
                    boolean $$15 = $$11 || $$12;
                    boolean $$16 = $$13 || $$14;
                    if ($$6 >= int3 || $$15 != $$16) {
                        blockPosMutableBlockPos4.setWithOffset(blockPos2, $$9, $$6, $$10);
                        if (!levelAccessor0.m_8055_(blockPosMutableBlockPos4).m_60804_(levelAccessor0, blockPosMutableBlockPos4)) {
                            BlockState $$17 = hugeMushroomFeatureConfiguration5.capProvider.getState(randomSource1, blockPos2);
                            if ($$17.m_61138_(HugeMushroomBlock.WEST) && $$17.m_61138_(HugeMushroomBlock.EAST) && $$17.m_61138_(HugeMushroomBlock.NORTH) && $$17.m_61138_(HugeMushroomBlock.SOUTH) && $$17.m_61138_(HugeMushroomBlock.UP)) {
                                $$17 = (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) $$17.m_61124_(HugeMushroomBlock.UP, $$6 >= int3 - 1)).m_61124_(HugeMushroomBlock.WEST, $$9 < -$$8)).m_61124_(HugeMushroomBlock.EAST, $$9 > $$8)).m_61124_(HugeMushroomBlock.NORTH, $$10 < -$$8)).m_61124_(HugeMushroomBlock.SOUTH, $$10 > $$8);
                            }
                            this.m_5974_(levelAccessor0, blockPosMutableBlockPos4, $$17);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected int getTreeRadiusForHeight(int int0, int int1, int int2, int int3) {
        int $$4 = 0;
        if (int3 < int1 && int3 >= int1 - 3) {
            $$4 = int2;
        } else if (int3 == int1) {
            $$4 = int2;
        }
        return $$4;
    }
}