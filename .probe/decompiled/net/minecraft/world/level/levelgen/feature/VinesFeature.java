package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class VinesFeature extends Feature<NoneFeatureConfiguration> {

    public VinesFeature(Codec<NoneFeatureConfiguration> codecNoneFeatureConfiguration0) {
        super(codecNoneFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContextNoneFeatureConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextNoneFeatureConfiguration0.level();
        BlockPos $$2 = featurePlaceContextNoneFeatureConfiguration0.origin();
        featurePlaceContextNoneFeatureConfiguration0.config();
        if (!$$1.m_46859_($$2)) {
            return false;
        } else {
            for (Direction $$3 : Direction.values()) {
                if ($$3 != Direction.DOWN && VineBlock.isAcceptableNeighbour($$1, $$2.relative($$3), $$3)) {
                    $$1.m_7731_($$2, (BlockState) Blocks.VINE.defaultBlockState().m_61124_(VineBlock.getPropertyForFace($$3), true), 2);
                    return true;
                }
            }
            return false;
        }
    }
}