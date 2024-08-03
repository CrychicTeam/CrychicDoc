package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;

public class ReplaceBlobsFeature extends Feature<ReplaceSphereConfiguration> {

    public ReplaceBlobsFeature(Codec<ReplaceSphereConfiguration> codecReplaceSphereConfiguration0) {
        super(codecReplaceSphereConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<ReplaceSphereConfiguration> featurePlaceContextReplaceSphereConfiguration0) {
        ReplaceSphereConfiguration $$1 = featurePlaceContextReplaceSphereConfiguration0.config();
        WorldGenLevel $$2 = featurePlaceContextReplaceSphereConfiguration0.level();
        RandomSource $$3 = featurePlaceContextReplaceSphereConfiguration0.random();
        Block $$4 = $$1.targetState.m_60734_();
        BlockPos $$5 = findTarget($$2, featurePlaceContextReplaceSphereConfiguration0.origin().mutable().clamp(Direction.Axis.Y, $$2.m_141937_() + 1, $$2.m_151558_() - 1), $$4);
        if ($$5 == null) {
            return false;
        } else {
            int $$6 = $$1.radius().sample($$3);
            int $$7 = $$1.radius().sample($$3);
            int $$8 = $$1.radius().sample($$3);
            int $$9 = Math.max($$6, Math.max($$7, $$8));
            boolean $$10 = false;
            for (BlockPos $$11 : BlockPos.withinManhattan($$5, $$6, $$7, $$8)) {
                if ($$11.m_123333_($$5) > $$9) {
                    break;
                }
                BlockState $$12 = $$2.m_8055_($$11);
                if ($$12.m_60713_($$4)) {
                    this.m_5974_($$2, $$11, $$1.replaceState);
                    $$10 = true;
                }
            }
            return $$10;
        }
    }

    @Nullable
    private static BlockPos findTarget(LevelAccessor levelAccessor0, BlockPos.MutableBlockPos blockPosMutableBlockPos1, Block block2) {
        while (blockPosMutableBlockPos1.m_123342_() > levelAccessor0.m_141937_() + 1) {
            BlockState $$3 = levelAccessor0.m_8055_(blockPosMutableBlockPos1);
            if ($$3.m_60713_(block2)) {
                return blockPosMutableBlockPos1;
            }
            blockPosMutableBlockPos1.move(Direction.DOWN);
        }
        return null;
    }
}