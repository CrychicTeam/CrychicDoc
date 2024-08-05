package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.FloaterEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FloaterItem extends Item {

    public FloaterItem() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (player.m_20072_() && !player.m_6144_()) {
            FloaterEntity floaterEntity = ACEntityRegistry.FLOATER.get().create(level);
            floaterEntity.m_20359_(player);
            if (!level.isClientSide) {
                level.m_7967_(floaterEntity);
            }
            player.m_20201_().startRiding(floaterEntity);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            return InteractionResultHolder.pass(itemstack);
        }
    }
}