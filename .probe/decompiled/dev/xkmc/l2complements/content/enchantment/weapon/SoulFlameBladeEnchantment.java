package dev.xkmc.l2complements.content.enchantment.weapon;

import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SoulFlameBladeEnchantment extends AbstractBladeEnchantment {

    public SoulFlameBladeEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    protected MobEffectInstance getEffect(int pLevel) {
        return new MobEffectInstance((MobEffect) LCEffects.FLAME.get(), LCConfig.COMMON.flameEnchantDuration.get(), pLevel - 1);
    }
}