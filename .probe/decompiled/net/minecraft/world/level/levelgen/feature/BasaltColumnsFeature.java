package net.minecraft.world.level.levelgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;

public class BasaltColumnsFeature extends Feature<ColumnFeatureConfiguration> {

    private static final ImmutableList<Block> CANNOT_PLACE_ON = ImmutableList.of(Blocks.LAVA, Blocks.BEDROCK, Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_WART, Blocks.CHEST, Blocks.SPAWNER);

    private static final int CLUSTERED_REACH = 5;

    private static final int CLUSTERED_SIZE = 50;

    private static final int UNCLUSTERED_REACH = 8;

    private static final int UNCLUSTERED_SIZE = 15;

    public BasaltColumnsFeature(Codec<ColumnFeatureConfiguration> codecColumnFeatureConfiguration0) {
        super(codecColumnFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<ColumnFeatureConfiguration> featurePlaceContextColumnFeatureConfiguration0) {
        int $$1 = featurePlaceContextColumnFeatureConfiguration0.chunkGenerator().getSeaLevel();
        BlockPos $$2 = featurePlaceContextColumnFeatureConfiguration0.origin();
        WorldGenLevel $$3 = featurePlaceContextColumnFeatureConfiguration0.level();
        RandomSource $$4 = featurePlaceContextColumnFeatureConfiguration0.random();
        ColumnFeatureConfiguration $$5 = featurePlaceContextColumnFeatureConfiguration0.config();
        if (!canPlaceAt($$3, $$1, $$2.mutable())) {
            return false;
        } else {
            int $$6 = $$5.height().sample($$4);
            boolean $$7 = $$4.nextFloat() < 0.9F;
            int $$8 = Math.min($$6, $$7 ? 5 : 8);
            int $$9 = $$7 ? 50 : 15;
            boolean $$10 = false;
            for (BlockPos $$11 : BlockPos.randomBetweenClosed($$4, $$9, $$2.m_123341_() - $$8, $$2.m_123342_(), $$2.m_123343_() - $$8, $$2.m_123341_() + $$8, $$2.m_123342_(), $$2.m_123343_() + $$8)) {
                int $$12 = $$6 - $$11.m_123333_($$2);
                if ($$12 >= 0) {
                    $$10 |= this.placeColumn($$3, $$1, $$11, $$12, $$5.reach().sample($$4));
                }
            }
            return $$10;
        }
    }

    private boolean placeColumn(LevelAccessor levelAccessor0, int int1, BlockPos blockPos2, int int3, int int4) {
        boolean $$5 = false;
        for (BlockPos $$6 : BlockPos.betweenClosed(blockPos2.m_123341_() - int4, blockPos2.m_123342_(), blockPos2.m_123343_() - int4, blockPos2.m_123341_() + int4, blockPos2.m_123342_(), blockPos2.m_123343_() + int4)) {
            int $$7 = $$6.m_123333_(blockPos2);
            BlockPos $$8 = isAirOrLavaOcean(levelAccessor0, int1, $$6) ? findSurface(levelAccessor0, int1, $$6.mutable(), $$7) : findAir(levelAccessor0, $$6.mutable(), $$7);
            if ($$8 != null) {
                int $$9 = int3 - $$7 / 2;
                for (BlockPos.MutableBlockPos $$10 = $$8.mutable(); $$9 >= 0; $$9--) {
                    if (isAirOrLavaOcean(levelAccessor0, int1, $$10)) {
                        this.m_5974_(levelAccessor0, $$10, Blocks.BASALT.defaultBlockState());
                        $$10.move(Direction.UP);
                        $$5 = true;
                    } else {
                        if (!levelAccessor0.m_8055_($$10).m_60713_(Blocks.BASALT)) {
                            break;
                        }
                        $$10.move(Direction.UP);
                    }
                }
            }
        }
        return $$5;
    }

    @Nullable
    private static BlockPos findSurface(LevelAccessor levelAccessor0, int int1, BlockPos.MutableBlockPos blockPosMutableBlockPos2, int int3) {
        while (blockPosMutableBlockPos2.m_123342_() > levelAccessor0.m_141937_() + 1 && int3 > 0) {
            int3--;
            if (canPlaceAt(levelAccessor0, int1, blockPosMutableBlockPos2)) {
                return blockPosMutableBlockPos2;
            }
            blockPosMutableBlockPos2.move(Direction.DOWN);
        }
        return null;
    }

    private static boolean canPlaceAt(LevelAccessor levelAccessor0, int int1, BlockPos.MutableBlockPos blockPosMutableBlockPos2) {
        if (!isAirOrLavaOcean(levelAccessor0, int1, blockPosMutableBlockPos2)) {
            return false;
        } else {
            BlockState $$3 = levelAccessor0.m_8055_(blockPosMutableBlockPos2.move(Direction.DOWN));
            blockPosMutableBlockPos2.move(Direction.UP);
            return !$$3.m_60795_() && !CANNOT_PLACE_ON.contains($$3.m_60734_());
        }
    }

    @Nullable
    private static BlockPos findAir(LevelAccessor levelAccessor0, BlockPos.MutableBlockPos blockPosMutableBlockPos1, int int2) {
        while (blockPosMutableBlockPos1.m_123342_() < levelAccessor0.m_151558_() && int2 > 0) {
            int2--;
            BlockState $$3 = levelAccessor0.m_8055_(blockPosMutableBlockPos1);
            if (CANNOT_PLACE_ON.contains($$3.m_60734_())) {
                return null;
            }
            if ($$3.m_60795_()) {
                return blockPosMutableBlockPos1;
            }
            blockPosMutableBlockPos1.move(Direction.UP);
        }
        return null;
    }

    private static boolean isAirOrLavaOcean(LevelAccessor levelAccessor0, int int1, BlockPos blockPos2) {
        BlockState $$3 = levelAccessor0.m_8055_(blockPos2);
        return $$3.m_60795_() || $$3.m_60713_(Blocks.LAVA) && blockPos2.m_123342_() <= int1;
    }
}