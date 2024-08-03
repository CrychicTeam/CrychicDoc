package dev.architectury.extensions;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ItemExtension {

    default void tickArmor(ItemStack stack, Player player) {
    }

    @Nullable
    default EquipmentSlot getCustomEquipmentSlot(ItemStack stack) {
        return null;
    }
}