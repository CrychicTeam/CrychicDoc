package dev.xkmc.l2complements.content.enchantment.weapon;

import dev.xkmc.l2complements.content.effect.skill.BleedEffect;
import dev.xkmc.l2complements.content.enchantment.core.BattleEnchantment;
import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SharpBladeEnchantment extends BattleEnchantment {

    public SharpBladeEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int pLevel) {
        LivingEntity le = this.getTarget(target);
        if (le != null && !attacker.m_9236_().isClientSide()) {
            ((BleedEffect) LCEffects.BLEED.get()).addTo(le, LCConfig.COMMON.bleedEnchantDuration.get(), pLevel * 3 - 1, EffectUtil.AddReason.SKILL, attacker);
        }
    }
}