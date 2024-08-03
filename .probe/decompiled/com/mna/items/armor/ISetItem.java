package com.mna.items.armor;

import com.mna.ManaAndArtifice;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ISetItem {

    EquipmentSlot[] defaultSlotTypes = new EquipmentSlot[] { EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.HEAD, EquipmentSlot.LEGS };

    ResourceLocation getSetIdentifier();

    default int itemsForSetBonus() {
        return 4;
    }

    default void applySetBonus(LivingEntity living, EquipmentSlot... setSlots) {
    }

    default void removeSetBonus(LivingEntity living, EquipmentSlot... setSlots) {
    }

    default EquipmentSlot[] getValidSetSlots() {
        return defaultSlotTypes;
    }

    default void addSetTooltip(List<Component> tooltip) {
        boolean setEquipped = this.isSetEquipped(ManaAndArtifice.instance.proxy.getClientPlayer());
        String s = Component.translatable(this.getSetIdentifier().toString()).getString();
        for (String spl : s.split("\n")) {
            tooltip.add(Component.literal(spl).withStyle(setEquipped ? ChatFormatting.GREEN : ChatFormatting.RED));
        }
    }

    default boolean isSetEquipped(LivingEntity living) {
        if (living == null) {
            return false;
        } else {
            int count = 0;
            for (EquipmentSlot slot : this.getValidSetSlots()) {
                if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) {
                    ItemStack stack = living.getItemBySlot(slot);
                    if (stack.getItem() instanceof ISetItem && ((ISetItem) stack.getItem()).getSetIdentifier().equals(this.getSetIdentifier())) {
                        count++;
                    }
                }
            }
            return count >= this.itemsForSetBonus();
        }
    }
}