package dev.xkmc.l2complements.content.feature;

import java.util.function.Supplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public record EnchantmentFeaturePredicate(Supplier<Enchantment> e) implements FeaturePredicate {

    @Override
    public boolean test(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel((Enchantment) this.e.get(), entity) > 0;
    }
}