package net.minecraft.world.item;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SignItem extends StandingAndWallBlockItem {

    public SignItem(Item.Properties itemProperties0, Block block1, Block block2) {
        super(block1, block2, itemProperties0, Direction.DOWN);
    }

    public SignItem(Item.Properties itemProperties0, Block block1, Block block2, Direction direction3) {
        super(block1, block2, itemProperties0, direction3);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos blockPos0, Level level1, @Nullable Player player2, ItemStack itemStack3, BlockState blockState4) {
        boolean $$5 = super.m_7274_(blockPos0, level1, player2, itemStack3, blockState4);
        if (!level1.isClientSide && !$$5 && player2 != null && level1.getBlockEntity(blockPos0) instanceof SignBlockEntity $$6 && level1.getBlockState(blockPos0).m_60734_() instanceof SignBlock $$7) {
            $$7.openTextEdit(player2, $$6, true);
        }
        return $$5;
    }
}