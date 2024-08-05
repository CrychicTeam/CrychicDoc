package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityTriops;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FrogspawnBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class BlockTriopsEggs extends FrogspawnBlock {

    public BlockTriopsEggs() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instabreak().noOcclusion().noCollission().sound(SoundType.FROGSPAWN).offsetType(BlockBehaviour.OffsetType.XZ));
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (!this.m_7898_(blockState, serverLevel, blockPos)) {
            serverLevel.m_46961_(blockPos, false);
        } else if (serverLevel.m_6425_(blockPos.below()).is(FluidTags.WATER)) {
            serverLevel.m_46961_(blockPos, false);
            int i = 2 + randomSource.nextInt(2);
            for (int j = 1; j <= i; j++) {
                EntityTriops tadpole = AMEntityRegistry.TRIOPS.get().create(serverLevel);
                if (tadpole != null) {
                    double d0 = (double) blockPos.m_123341_();
                    double d1 = (double) blockPos.m_123343_();
                    int k = randomSource.nextInt(1, 361);
                    tadpole.m_7678_(d0, (double) blockPos.m_123342_() - 0.5, d1, (float) k, 0.0F);
                    tadpole.m_21530_();
                    tadpole.setBabyAge(-12000);
                    serverLevel.addFreshEntity(tadpole);
                }
            }
        }
    }
}