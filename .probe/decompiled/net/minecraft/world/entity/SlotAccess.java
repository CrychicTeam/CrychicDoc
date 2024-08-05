package net.minecraft.world.entity;

import java.util.function.Predicate;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public interface SlotAccess {

    SlotAccess NULL = new SlotAccess() {

        @Override
        public ItemStack get() {
            return ItemStack.EMPTY;
        }

        @Override
        public boolean set(ItemStack p_147314_) {
            return false;
        }
    };

    static SlotAccess forContainer(final Container container0, final int int1, final Predicate<ItemStack> predicateItemStack2) {
        return new SlotAccess() {

            @Override
            public ItemStack get() {
                return container0.getItem(int1);
            }

            @Override
            public boolean set(ItemStack p_147324_) {
                if (!predicateItemStack2.test(p_147324_)) {
                    return false;
                } else {
                    container0.setItem(int1, p_147324_);
                    return true;
                }
            }
        };
    }

    static SlotAccess forContainer(Container container0, int int1) {
        return forContainer(container0, int1, p_147310_ -> true);
    }

    static SlotAccess forEquipmentSlot(final LivingEntity livingEntity0, final EquipmentSlot equipmentSlot1, final Predicate<ItemStack> predicateItemStack2) {
        return new SlotAccess() {

            @Override
            public ItemStack get() {
                return livingEntity0.getItemBySlot(equipmentSlot1);
            }

            @Override
            public boolean set(ItemStack p_147334_) {
                if (!predicateItemStack2.test(p_147334_)) {
                    return false;
                } else {
                    livingEntity0.setItemSlot(equipmentSlot1, p_147334_);
                    return true;
                }
            }
        };
    }

    static SlotAccess forEquipmentSlot(LivingEntity livingEntity0, EquipmentSlot equipmentSlot1) {
        return forEquipmentSlot(livingEntity0, equipmentSlot1, p_147308_ -> true);
    }

    ItemStack get();

    boolean set(ItemStack var1);
}