package dev.xkmc.l2hostility.compat.curios;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record EquipmentSlotAccess(LivingEntity le, EquipmentSlot slot) implements EntitySlotAccess {

    @Override
    public ItemStack get() {
        return this.le.getItemBySlot(this.slot);
    }

    @Override
    public void set(ItemStack stack) {
        this.le.setItemSlot(this.slot, stack);
    }

    @Override
    public String getID() {
        return "equipment/" + this.slot.getName();
    }
}