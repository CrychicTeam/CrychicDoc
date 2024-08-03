package com.mna.items.armor;

import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public interface IBrokenArmorReplaceable<I extends ArmorItem> {

    default <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if (stack.getDamageValue() + amount >= stack.getMaxDamage() && entity instanceof LivingEntity) {
            BrokenMageArmor brokenItem = BrokenMageArmor.getBySlot(((ArmorItem) this).getEquipmentSlot());
            if (brokenItem == null) {
                return amount;
            } else {
                ItemStack brokenStack = brokenItem.convertFrom(stack);
                entity.setItemSlot(((ArmorItem) this).getEquipmentSlot(), brokenStack);
                return 0;
            }
        } else {
            return amount;
        }
    }
}