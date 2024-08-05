package net.minecraft.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;

public class ItemFrameItem extends HangingEntityItem {

    public ItemFrameItem(EntityType<? extends HangingEntity> entityTypeExtendsHangingEntity0, Item.Properties itemProperties1) {
        super(entityTypeExtendsHangingEntity0, itemProperties1);
    }

    @Override
    protected boolean mayPlace(Player player0, Direction direction1, ItemStack itemStack2, BlockPos blockPos3) {
        return !player0.m_9236_().m_151570_(blockPos3) && player0.mayUseItemAt(blockPos3, direction1, itemStack2);
    }
}