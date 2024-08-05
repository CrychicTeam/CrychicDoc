package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class ScatteredOreFeature extends Feature<OreConfiguration> {

    private static final int MAX_DIST_FROM_ORIGIN = 7;

    ScatteredOreFeature(Codec<OreConfiguration> codecOreConfiguration0) {
        super(codecOreConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<OreConfiguration> featurePlaceContextOreConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextOreConfiguration0.level();
        RandomSource $$2 = featurePlaceContextOreConfiguration0.random();
        OreConfiguration $$3 = featurePlaceContextOreConfiguration0.config();
        BlockPos $$4 = featurePlaceContextOreConfiguration0.origin();
        int $$5 = $$2.nextInt($$3.size + 1);
        BlockPos.MutableBlockPos $$6 = new BlockPos.MutableBlockPos();
        for (int $$7 = 0; $$7 < $$5; $$7++) {
            this.offsetTargetPos($$6, $$2, $$4, Math.min($$7, 7));
            BlockState $$8 = $$1.m_8055_($$6);
            for (OreConfiguration.TargetBlockState $$9 : $$3.targetStates) {
                if (OreFeature.canPlaceOre($$8, $$1::m_8055_, $$2, $$3, $$9, $$6)) {
                    $$1.m_7731_($$6, $$9.state, 2);
                    break;
                }
            }
        }
        return true;
    }

    private void offsetTargetPos(BlockPos.MutableBlockPos blockPosMutableBlockPos0, RandomSource randomSource1, BlockPos blockPos2, int int3) {
        int $$4 = this.getRandomPlacementInOneAxisRelativeToOrigin(randomSource1, int3);
        int $$5 = this.getRandomPlacementInOneAxisRelativeToOrigin(randomSource1, int3);
        int $$6 = this.getRandomPlacementInOneAxisRelativeToOrigin(randomSource1, int3);
        blockPosMutableBlockPos0.setWithOffset(blockPos2, $$4, $$5, $$6);
    }

    private int getRandomPlacementInOneAxisRelativeToOrigin(RandomSource randomSource0, int int1) {
        return Math.round((randomSource0.nextFloat() - randomSource0.nextFloat()) * (float) int1);
    }
}