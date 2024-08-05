package dev.xkmc.l2weaponry.compat.twilightforest;

import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.content.item.base.BaseShieldItem;
import dev.xkmc.l2weaponry.content.item.base.GenericWeaponItem;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.data.LangData;
import dev.xkmc.l2weaponry.init.materials.LWExtraConfig;
import dev.xkmc.l2weaponry.init.materials.LWToolTypes;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class FieryTool extends ExtraToolConfig implements LWExtraConfig {

    @Override
    public void addEnchants(List<EnchantmentInstance> list, LWToolTypes type, Item tool) {
        if (tool instanceof GenericWeaponItem) {
            list.add(new EnchantmentInstance((Enchantment) LCEnchantments.SMELT.get(), 1));
        }
    }

    private static MobEffectInstance getEffect() {
        return new MobEffectInstance(MobEffects.FIRE_RESISTANCE, LWConfig.COMMON.fieryDuration.get() * 20);
    }

    @Override
    public void onShieldBlock(ItemStack stack, LivingEntity user, LivingEntity entity) {
        if (!entity.m_5825_()) {
            entity.m_20254_(LWConfig.COMMON.fieryDuration.get());
        }
        user.addEffect(getEffect());
    }

    public void onDamage(AttackCache cache, ItemStack stack) {
        if (!cache.getAttackTarget().m_5825_()) {
            double bonus = LWConfig.COMMON.fieryBonus.get();
            cache.addHurtModifier(DamageModifier.multBase((float) bonus));
            cache.getAttackTarget().m_20254_(LWConfig.COMMON.fieryDuration.get());
        }
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        double bonus = LWConfig.COMMON.knightmetalBonus.get();
        list.add(LangData.MATS_FIERY.get((int) Math.round(bonus * 100.0), LWConfig.COMMON.fieryDuration.get()));
        if (stack.getItem() instanceof BaseShieldItem) {
            list.add(LangData.MATS_EFFECT.get(LangData.getTooltip(getEffect())));
        }
    }
}