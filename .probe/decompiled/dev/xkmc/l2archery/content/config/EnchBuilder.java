package dev.xkmc.l2archery.content.config;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2archery.content.enchantment.PotionArrowEnchantment;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchBuilder extends BaseBuilder<EnchBuilder, PotionArrowEnchantment, Enchantment, BowArrowStatConfig.EnchantmentConfigEffect> {

    EnchBuilder(BowArrowStatConfig config, RegistryEntry<PotionArrowEnchantment> ench) {
        super(config, config.enchantment_effects, ench);
    }

    public EnchBuilder putEffect(MobEffect type, int duration, int amplifier, int durBonus, int ampBonus) {
        this.effects.put(type, new BowArrowStatConfig.EnchantmentConfigEffect(duration, amplifier, durBonus, ampBonus));
        return this;
    }
}