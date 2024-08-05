package dev.xkmc.l2complements.content.feature;

import java.util.function.Supplier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public record SlotEnchantmentFeaturePredicate(EquipmentSlot slot, Supplier<Enchantment> e) implements FeaturePredicate {

    @Override
    public boolean test(LivingEntity entity) {
        ItemStack stack = entity.getItemBySlot(this.slot);
        return stack.getEnchantmentLevel((Enchantment) this.e.get()) > 0;
    }
}