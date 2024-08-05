package net.minecraft.world.level.levelgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class EndPodiumFeature extends Feature<NoneFeatureConfiguration> {

    public static final int PODIUM_RADIUS = 4;

    public static final int PODIUM_PILLAR_HEIGHT = 4;

    public static final int RIM_RADIUS = 1;

    public static final float CORNER_ROUNDING = 0.5F;

    private static final BlockPos END_PODIUM_LOCATION = BlockPos.ZERO;

    private final boolean active;

    public static BlockPos getLocation(BlockPos blockPos0) {
        return END_PODIUM_LOCATION.offset(blockPos0);
    }

    public EndPodiumFeature(boolean boolean0) {
        super(NoneFeatureConfiguration.CODEC);
        this.active = boolean0;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContextNoneFeatureConfiguration0) {
        BlockPos $$1 = featurePlaceContextNoneFeatureConfiguration0.origin();
        WorldGenLevel $$2 = featurePlaceContextNoneFeatureConfiguration0.level();
        for (BlockPos $$3 : BlockPos.betweenClosed(new BlockPos($$1.m_123341_() - 4, $$1.m_123342_() - 1, $$1.m_123343_() - 4), new BlockPos($$1.m_123341_() + 4, $$1.m_123342_() + 32, $$1.m_123343_() + 4))) {
            boolean $$4 = $$3.m_123314_($$1, 2.5);
            if ($$4 || $$3.m_123314_($$1, 3.5)) {
                if ($$3.m_123342_() < $$1.m_123342_()) {
                    if ($$4) {
                        this.m_5974_($$2, $$3, Blocks.BEDROCK.defaultBlockState());
                    } else if ($$3.m_123342_() < $$1.m_123342_()) {
                        this.m_5974_($$2, $$3, Blocks.END_STONE.defaultBlockState());
                    }
                } else if ($$3.m_123342_() > $$1.m_123342_()) {
                    this.m_5974_($$2, $$3, Blocks.AIR.defaultBlockState());
                } else if (!$$4) {
                    this.m_5974_($$2, $$3, Blocks.BEDROCK.defaultBlockState());
                } else if (this.active) {
                    this.m_5974_($$2, new BlockPos($$3), Blocks.END_PORTAL.defaultBlockState());
                } else {
                    this.m_5974_($$2, new BlockPos($$3), Blocks.AIR.defaultBlockState());
                }
            }
        }
        for (int $$5 = 0; $$5 < 4; $$5++) {
            this.m_5974_($$2, $$1.above($$5), Blocks.BEDROCK.defaultBlockState());
        }
        BlockPos $$6 = $$1.above(2);
        for (Direction $$7 : Direction.Plane.HORIZONTAL) {
            this.m_5974_($$2, $$6.relative($$7), (BlockState) Blocks.WALL_TORCH.defaultBlockState().m_61124_(WallTorchBlock.FACING, $$7));
        }
        return true;
    }
}