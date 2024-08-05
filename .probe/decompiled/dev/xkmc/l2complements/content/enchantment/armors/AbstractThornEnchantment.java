package dev.xkmc.l2complements.content.enchantment.armors;

import dev.xkmc.l2complements.content.enchantment.core.BattleEnchantment;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractThornEnchantment extends BattleEnchantment {

    protected AbstractThornEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostHurt(LivingEntity self, Entity attacker, int pLevel) {
        LivingEntity le = this.getTarget(attacker);
        if (le != null && le != self && !self.m_9236_().isClientSide()) {
            EffectUtil.addEffect(le, this.getEffect(pLevel), EffectUtil.AddReason.FORCE, self);
        }
    }

    protected abstract MobEffectInstance getEffect(int var1);
}