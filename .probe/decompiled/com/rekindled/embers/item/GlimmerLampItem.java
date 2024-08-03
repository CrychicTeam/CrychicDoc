package com.rekindled.embers.item;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.entity.GlimmerProjectileEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class GlimmerLampItem extends Item {

    public GlimmerLampItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide() && world.getGameTime() % 10L == 0L && world.m_45517_(LightLayer.SKY, entity.blockPosition()) - world.getSkyDarken() > 12) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !ItemStack.isSameItem(oldStack, newStack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!level.isClientSide()) {
            GlimmerProjectileEntity light = RegistryManager.GLIMMER_PROJECTILE.get().create(level);
            double handmod = hand == InteractionHand.MAIN_HAND ? 1.0 : -1.0;
            handmod *= player.getMainArm() == HumanoidArm.RIGHT ? 1.0 : -1.0;
            double posX = player.m_20182_().x + player.m_20154_().x + handmod * ((double) player.m_20205_() / 2.0) * Math.sin(Math.toRadians((double) (-player.m_6080_() - 90.0F)));
            double posY = player.m_20182_().y + (double) player.m_20192_() - 0.2 + player.m_20154_().y;
            double posZ = player.m_20182_().z + player.m_20154_().z + handmod * ((double) player.m_20205_() / 2.0) * Math.cos(Math.toRadians((double) (-player.m_6080_() - 90.0F)));
            light.m_6034_(posX, posY, posZ);
            light.m_20256_(player.m_20154_());
            light.m_5602_(player);
            level.m_7967_(light);
        }
        stack.hurtAndBreak(10, player, e -> {
        });
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return 16777215;
    }
}