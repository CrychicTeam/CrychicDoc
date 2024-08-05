package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;

public class EndGatewayFeature extends Feature<EndGatewayConfiguration> {

    public EndGatewayFeature(Codec<EndGatewayConfiguration> codecEndGatewayConfiguration0) {
        super(codecEndGatewayConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<EndGatewayConfiguration> featurePlaceContextEndGatewayConfiguration0) {
        BlockPos $$1 = featurePlaceContextEndGatewayConfiguration0.origin();
        WorldGenLevel $$2 = featurePlaceContextEndGatewayConfiguration0.level();
        EndGatewayConfiguration $$3 = featurePlaceContextEndGatewayConfiguration0.config();
        for (BlockPos $$4 : BlockPos.betweenClosed($$1.offset(-1, -2, -1), $$1.offset(1, 2, 1))) {
            boolean $$5 = $$4.m_123341_() == $$1.m_123341_();
            boolean $$6 = $$4.m_123342_() == $$1.m_123342_();
            boolean $$7 = $$4.m_123343_() == $$1.m_123343_();
            boolean $$8 = Math.abs($$4.m_123342_() - $$1.m_123342_()) == 2;
            if ($$5 && $$6 && $$7) {
                BlockPos $$9 = $$4.immutable();
                this.m_5974_($$2, $$9, Blocks.END_GATEWAY.defaultBlockState());
                $$3.getExit().ifPresent(p_65699_ -> {
                    BlockEntity $$4x = $$2.m_7702_($$9);
                    if ($$4x instanceof TheEndGatewayBlockEntity $$5x) {
                        $$5x.setExitPosition(p_65699_, $$3.isExitExact());
                        $$4x.setChanged();
                    }
                });
            } else if ($$6) {
                this.m_5974_($$2, $$4, Blocks.AIR.defaultBlockState());
            } else if ($$8 && $$5 && $$7) {
                this.m_5974_($$2, $$4, Blocks.BEDROCK.defaultBlockState());
            } else if (($$5 || $$7) && !$$8) {
                this.m_5974_($$2, $$4, Blocks.BEDROCK.defaultBlockState());
            } else {
                this.m_5974_($$2, $$4, Blocks.AIR.defaultBlockState());
            }
        }
        return true;
    }
}