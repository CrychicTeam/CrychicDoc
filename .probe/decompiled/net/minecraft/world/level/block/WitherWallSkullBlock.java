package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class WitherWallSkullBlock extends WallSkullBlock {

    protected WitherWallSkullBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(SkullBlock.Types.WITHER_SKELETON, blockBehaviourProperties0);
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, @Nullable LivingEntity livingEntity3, ItemStack itemStack4) {
        Blocks.WITHER_SKELETON_SKULL.setPlacedBy(level0, blockPos1, blockState2, livingEntity3, itemStack4);
    }
}