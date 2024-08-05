package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

public abstract class AbstractHugeMushroomFeature extends Feature<HugeMushroomFeatureConfiguration> {

    public AbstractHugeMushroomFeature(Codec<HugeMushroomFeatureConfiguration> codecHugeMushroomFeatureConfiguration0) {
        super(codecHugeMushroomFeatureConfiguration0);
    }

    protected void placeTrunk(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos blockPos2, HugeMushroomFeatureConfiguration hugeMushroomFeatureConfiguration3, int int4, BlockPos.MutableBlockPos blockPosMutableBlockPos5) {
        for (int $$6 = 0; $$6 < int4; $$6++) {
            blockPosMutableBlockPos5.set(blockPos2).move(Direction.UP, $$6);
            if (!levelAccessor0.m_8055_(blockPosMutableBlockPos5).m_60804_(levelAccessor0, blockPosMutableBlockPos5)) {
                this.m_5974_(levelAccessor0, blockPosMutableBlockPos5, hugeMushroomFeatureConfiguration3.stemProvider.getState(randomSource1, blockPos2));
            }
        }
    }

    protected int getTreeHeight(RandomSource randomSource0) {
        int $$1 = randomSource0.nextInt(3) + 4;
        if (randomSource0.nextInt(12) == 0) {
            $$1 *= 2;
        }
        return $$1;
    }

    protected boolean isValidPosition(LevelAccessor levelAccessor0, BlockPos blockPos1, int int2, BlockPos.MutableBlockPos blockPosMutableBlockPos3, HugeMushroomFeatureConfiguration hugeMushroomFeatureConfiguration4) {
        int $$5 = blockPos1.m_123342_();
        if ($$5 >= levelAccessor0.m_141937_() + 1 && $$5 + int2 + 1 < levelAccessor0.m_151558_()) {
            BlockState $$6 = levelAccessor0.m_8055_(blockPos1.below());
            if (!m_159759_($$6) && !$$6.m_204336_(BlockTags.MUSHROOM_GROW_BLOCK)) {
                return false;
            } else {
                for (int $$7 = 0; $$7 <= int2; $$7++) {
                    int $$8 = this.getTreeRadiusForHeight(-1, -1, hugeMushroomFeatureConfiguration4.foliageRadius, $$7);
                    for (int $$9 = -$$8; $$9 <= $$8; $$9++) {
                        for (int $$10 = -$$8; $$10 <= $$8; $$10++) {
                            BlockState $$11 = levelAccessor0.m_8055_(blockPosMutableBlockPos3.setWithOffset(blockPos1, $$9, $$7, $$10));
                            if (!$$11.m_60795_() && !$$11.m_204336_(BlockTags.LEAVES)) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean place(FeaturePlaceContext<HugeMushroomFeatureConfiguration> featurePlaceContextHugeMushroomFeatureConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextHugeMushroomFeatureConfiguration0.level();
        BlockPos $$2 = featurePlaceContextHugeMushroomFeatureConfiguration0.origin();
        RandomSource $$3 = featurePlaceContextHugeMushroomFeatureConfiguration0.random();
        HugeMushroomFeatureConfiguration $$4 = featurePlaceContextHugeMushroomFeatureConfiguration0.config();
        int $$5 = this.getTreeHeight($$3);
        BlockPos.MutableBlockPos $$6 = new BlockPos.MutableBlockPos();
        if (!this.isValidPosition($$1, $$2, $$5, $$6, $$4)) {
            return false;
        } else {
            this.makeCap($$1, $$3, $$2, $$5, $$6, $$4);
            this.placeTrunk($$1, $$3, $$2, $$4, $$5, $$6);
            return true;
        }
    }

    protected abstract int getTreeRadiusForHeight(int var1, int var2, int var3, int var4);

    protected abstract void makeCap(LevelAccessor var1, RandomSource var2, BlockPos var3, int var4, BlockPos.MutableBlockPos var5, HugeMushroomFeatureConfiguration var6);
}