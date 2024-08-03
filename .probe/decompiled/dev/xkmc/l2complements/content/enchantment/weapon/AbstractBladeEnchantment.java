package dev.xkmc.l2complements.content.enchantment.weapon;

import dev.xkmc.l2complements.content.enchantment.core.BattleEnchantment;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractBladeEnchantment extends BattleEnchantment {

    protected AbstractBladeEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int pLevel) {
        LivingEntity le = this.getTarget(target);
        if (le != null && le != attacker && !attacker.m_9236_().isClientSide()) {
            EffectUtil.addEffect(le, this.getEffect(pLevel), EffectUtil.AddReason.FORCE, attacker);
        }
    }

    protected abstract MobEffectInstance getEffect(int var1);
}