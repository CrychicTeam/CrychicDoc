package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;

public class SpringFeature extends Feature<SpringConfiguration> {

    public SpringFeature(Codec<SpringConfiguration> codecSpringConfiguration0) {
        super(codecSpringConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<SpringConfiguration> featurePlaceContextSpringConfiguration0) {
        SpringConfiguration $$1 = featurePlaceContextSpringConfiguration0.config();
        WorldGenLevel $$2 = featurePlaceContextSpringConfiguration0.level();
        BlockPos $$3 = featurePlaceContextSpringConfiguration0.origin();
        if (!$$2.m_8055_($$3.above()).m_204341_($$1.validBlocks)) {
            return false;
        } else if ($$1.requiresBlockBelow && !$$2.m_8055_($$3.below()).m_204341_($$1.validBlocks)) {
            return false;
        } else {
            BlockState $$4 = $$2.m_8055_($$3);
            if (!$$4.m_60795_() && !$$4.m_204341_($$1.validBlocks)) {
                return false;
            } else {
                int $$5 = 0;
                int $$6 = 0;
                if ($$2.m_8055_($$3.west()).m_204341_($$1.validBlocks)) {
                    $$6++;
                }
                if ($$2.m_8055_($$3.east()).m_204341_($$1.validBlocks)) {
                    $$6++;
                }
                if ($$2.m_8055_($$3.north()).m_204341_($$1.validBlocks)) {
                    $$6++;
                }
                if ($$2.m_8055_($$3.south()).m_204341_($$1.validBlocks)) {
                    $$6++;
                }
                if ($$2.m_8055_($$3.below()).m_204341_($$1.validBlocks)) {
                    $$6++;
                }
                int $$7 = 0;
                if ($$2.m_46859_($$3.west())) {
                    $$7++;
                }
                if ($$2.m_46859_($$3.east())) {
                    $$7++;
                }
                if ($$2.m_46859_($$3.north())) {
                    $$7++;
                }
                if ($$2.m_46859_($$3.south())) {
                    $$7++;
                }
                if ($$2.m_46859_($$3.below())) {
                    $$7++;
                }
                if ($$6 == $$1.rockCount && $$7 == $$1.holeCount) {
                    $$2.m_7731_($$3, $$1.state.createLegacyBlock(), 2);
                    $$2.m_186469_($$3, $$1.state.getType(), 0);
                    $$5++;
                }
                return $$5 > 0;
            }
        }
    }
}