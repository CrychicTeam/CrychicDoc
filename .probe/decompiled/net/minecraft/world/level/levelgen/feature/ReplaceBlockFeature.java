package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;

public class ReplaceBlockFeature extends Feature<ReplaceBlockConfiguration> {

    public ReplaceBlockFeature(Codec<ReplaceBlockConfiguration> codecReplaceBlockConfiguration0) {
        super(codecReplaceBlockConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<ReplaceBlockConfiguration> featurePlaceContextReplaceBlockConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextReplaceBlockConfiguration0.level();
        BlockPos $$2 = featurePlaceContextReplaceBlockConfiguration0.origin();
        ReplaceBlockConfiguration $$3 = featurePlaceContextReplaceBlockConfiguration0.config();
        for (OreConfiguration.TargetBlockState $$4 : $$3.targetStates) {
            if ($$4.target.test($$1.m_8055_($$2), featurePlaceContextReplaceBlockConfiguration0.random())) {
                $$1.m_7731_($$2, $$4.state, 2);
                break;
            }
        }
        return true;
    }
}