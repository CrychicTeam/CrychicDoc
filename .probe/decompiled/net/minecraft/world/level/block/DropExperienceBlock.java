package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class DropExperienceBlock extends Block {

    private final IntProvider xpRange;

    public DropExperienceBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        this(blockBehaviourProperties0, ConstantInt.of(0));
    }

    public DropExperienceBlock(BlockBehaviour.Properties blockBehaviourProperties0, IntProvider intProvider1) {
        super(blockBehaviourProperties0);
        this.xpRange = intProvider1;
    }

    @Override
    public void spawnAfterBreak(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, ItemStack itemStack3, boolean boolean4) {
        super.m_213646_(blockState0, serverLevel1, blockPos2, itemStack3, boolean4);
        if (boolean4) {
            this.m_220822_(serverLevel1, blockPos2, itemStack3, this.xpRange);
        }
    }
}