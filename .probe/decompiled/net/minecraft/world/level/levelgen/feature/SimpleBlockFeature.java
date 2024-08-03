package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

public class SimpleBlockFeature extends Feature<SimpleBlockConfiguration> {

    public SimpleBlockFeature(Codec<SimpleBlockConfiguration> codecSimpleBlockConfiguration0) {
        super(codecSimpleBlockConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> featurePlaceContextSimpleBlockConfiguration0) {
        SimpleBlockConfiguration $$1 = featurePlaceContextSimpleBlockConfiguration0.config();
        WorldGenLevel $$2 = featurePlaceContextSimpleBlockConfiguration0.level();
        BlockPos $$3 = featurePlaceContextSimpleBlockConfiguration0.origin();
        BlockState $$4 = $$1.toPlace().getState(featurePlaceContextSimpleBlockConfiguration0.random(), $$3);
        if ($$4.m_60710_($$2, $$3)) {
            if ($$4.m_60734_() instanceof DoublePlantBlock) {
                if (!$$2.m_46859_($$3.above())) {
                    return false;
                }
                DoublePlantBlock.placeAt($$2, $$4, $$3, 2);
            } else {
                $$2.m_7731_($$3, $$4, 2);
            }
            return true;
        } else {
            return false;
        }
    }
}