package com.mna.items;

import com.mna.entities.EntityInit;
import com.mna.entities.WandClone;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ItemWandOfCloning extends Item {

    public ItemWandOfCloning() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.m_21120_(handIn);
        if (!worldIn.isClientSide) {
            playerIn.m_21190_(handIn);
            stack.shrink(1);
            double dist = 3.0;
            double vOffset = 1.0;
            Vec3 spawnPos = playerIn.m_20182_().add(0.0, vOffset, 0.0).add(playerIn.m_20154_().normalize().scale(dist));
            WandClone ewc = new WandClone(EntityInit.WAND_CLONE.get(), worldIn);
            ewc.m_19890_(spawnPos.x, spawnPos.y, spawnPos.z, playerIn.f_20885_, playerIn.m_146909_());
            worldIn.m_7967_(ewc);
        }
        return InteractionResultHolder.success(stack);
    }
}