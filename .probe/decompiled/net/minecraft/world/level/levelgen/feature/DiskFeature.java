package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;

public class DiskFeature extends Feature<DiskConfiguration> {

    public DiskFeature(Codec<DiskConfiguration> codecDiskConfiguration0) {
        super(codecDiskConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<DiskConfiguration> featurePlaceContextDiskConfiguration0) {
        DiskConfiguration $$1 = featurePlaceContextDiskConfiguration0.config();
        BlockPos $$2 = featurePlaceContextDiskConfiguration0.origin();
        WorldGenLevel $$3 = featurePlaceContextDiskConfiguration0.level();
        RandomSource $$4 = featurePlaceContextDiskConfiguration0.random();
        boolean $$5 = false;
        int $$6 = $$2.m_123342_();
        int $$7 = $$6 + $$1.halfHeight();
        int $$8 = $$6 - $$1.halfHeight() - 1;
        int $$9 = $$1.radius().sample($$4);
        BlockPos.MutableBlockPos $$10 = new BlockPos.MutableBlockPos();
        for (BlockPos $$11 : BlockPos.betweenClosed($$2.offset(-$$9, 0, -$$9), $$2.offset($$9, 0, $$9))) {
            int $$12 = $$11.m_123341_() - $$2.m_123341_();
            int $$13 = $$11.m_123343_() - $$2.m_123343_();
            if ($$12 * $$12 + $$13 * $$13 <= $$9 * $$9) {
                $$5 |= this.placeColumn($$1, $$3, $$4, $$7, $$8, $$10.set($$11));
            }
        }
        return $$5;
    }

    protected boolean placeColumn(DiskConfiguration diskConfiguration0, WorldGenLevel worldGenLevel1, RandomSource randomSource2, int int3, int int4, BlockPos.MutableBlockPos blockPosMutableBlockPos5) {
        boolean $$6 = false;
        BlockState $$7 = null;
        for (int $$8 = int3; $$8 > int4; $$8--) {
            blockPosMutableBlockPos5.setY($$8);
            if (diskConfiguration0.target().test(worldGenLevel1, blockPosMutableBlockPos5)) {
                BlockState $$9 = diskConfiguration0.stateProvider().getState(worldGenLevel1, randomSource2, blockPosMutableBlockPos5);
                worldGenLevel1.m_7731_(blockPosMutableBlockPos5, $$9, 2);
                this.m_159739_(worldGenLevel1, blockPosMutableBlockPos5);
                $$6 = true;
            }
        }
        return $$6;
    }
}