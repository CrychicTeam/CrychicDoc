package com.mna.items.artifice;

import com.mna.api.items.TieredItem;
import com.mna.effects.EffectInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

public class ItemPilgrimStaff extends TieredItem {

    public ItemPilgrimStaff() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity && ((LivingEntity) entityIn).getOffhandItem().getItem() == this && !worldIn.isClientSide) {
            MobEffectInstance activeEffect = ((LivingEntity) entityIn).getEffect(EffectInit.PILGRIM.get());
            if (activeEffect == null || activeEffect.getDuration() <= 20) {
                ((LivingEntity) entityIn).addEffect(new MobEffectInstance(EffectInit.PILGRIM.get(), 200, 0, false, false));
            }
        }
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return true;
    }
}