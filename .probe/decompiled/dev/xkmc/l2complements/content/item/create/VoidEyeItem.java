package dev.xkmc.l2complements.content.item.create;

import dev.xkmc.l2complements.init.data.DamageTypeGen;
import java.util.function.Supplier;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class VoidEyeItem extends ShadowSteelItem {

    public VoidEyeItem(Item.Properties properties, Supplier<MutableComponent> sup) {
        super(properties, sup);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        super.onEntityItemUpdate(stack, entity);
        if (entity.m_20184_().y() < 0.05) {
            entity.m_20256_(entity.m_20184_().add(0.0, 0.005, 0.0));
        }
        return false;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide() && pEntity instanceof LivingEntity le && le.m_20182_().y() < (double) pLevel.m_141937_()) {
            pStack.setCount(0);
            le.hurt(new DamageSource(DamageTypeGen.forKey(pLevel, DamageTypeGen.VOID_EYE)), le.getMaxHealth());
        }
    }
}