package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;

public class FillLayerFeature extends Feature<LayerConfiguration> {

    public FillLayerFeature(Codec<LayerConfiguration> codecLayerConfiguration0) {
        super(codecLayerConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<LayerConfiguration> featurePlaceContextLayerConfiguration0) {
        BlockPos $$1 = featurePlaceContextLayerConfiguration0.origin();
        LayerConfiguration $$2 = featurePlaceContextLayerConfiguration0.config();
        WorldGenLevel $$3 = featurePlaceContextLayerConfiguration0.level();
        BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
        for (int $$5 = 0; $$5 < 16; $$5++) {
            for (int $$6 = 0; $$6 < 16; $$6++) {
                int $$7 = $$1.m_123341_() + $$5;
                int $$8 = $$1.m_123343_() + $$6;
                int $$9 = $$3.m_141937_() + $$2.height;
                $$4.set($$7, $$9, $$8);
                if ($$3.m_8055_($$4).m_60795_()) {
                    $$3.m_7731_($$4, $$2.state, 2);
                }
            }
        }
        return true;
    }
}