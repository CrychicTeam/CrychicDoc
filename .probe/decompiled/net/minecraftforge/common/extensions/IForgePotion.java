package net.minecraftforge.common.extensions;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

public interface IForgePotion {

    private Potion self() {
        return (Potion) this;
    }

    default boolean isFoil(ItemStack stack) {
        return !PotionUtils.getMobEffects(stack).isEmpty();
    }
}