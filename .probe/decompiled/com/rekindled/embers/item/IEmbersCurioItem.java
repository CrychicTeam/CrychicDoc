package com.rekindled.embers.item;

import com.rekindled.embers.datagen.EmbersSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public interface IEmbersCurioItem extends ICurioItem {

    @Override
    default boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    default ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.EMPTY, 1.0F, 1.0F);
    }

    @Override
    default void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        this.playEquipSound(slotContext, false);
    }

    @Override
    default void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        this.playEquipSound(slotContext, true);
    }

    default void playEquipSound(SlotContext slotContext, boolean unequip) {
        Vec3 pos = slotContext.entity().m_20182_();
        slotContext.entity().m_9236_().playSound(null, pos.x, pos.y, pos.z, unequip ? this.unequipSound() : this.equipSound(), slotContext.entity().m_5720_(), 1.0F, 1.0F);
    }

    default SoundEvent equipSound() {
        return EmbersSounds.BAUBLE_EQUIP.get();
    }

    default SoundEvent unequipSound() {
        return EmbersSounds.BAUBLE_UNEQUIP.get();
    }
}