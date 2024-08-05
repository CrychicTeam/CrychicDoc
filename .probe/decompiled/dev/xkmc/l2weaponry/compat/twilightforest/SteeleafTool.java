package dev.xkmc.l2weaponry.compat.twilightforest;

import dev.xkmc.l2complements.content.effect.skill.BleedEffect;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2weaponry.content.item.base.BaseShieldItem;
import dev.xkmc.l2weaponry.content.item.base.GenericWeaponItem;
import dev.xkmc.l2weaponry.content.item.base.LWTieredItem;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.data.LangData;
import dev.xkmc.l2weaponry.init.materials.LWExtraConfig;
import dev.xkmc.l2weaponry.init.materials.LWToolTypes;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;

public class SteeleafTool extends ExtraToolConfig implements LWExtraConfig {

    @Override
    public void addEnchants(List<EnchantmentInstance> list, LWToolTypes type, Item tool) {
        if (tool instanceof LWTieredItem weapon && weapon.isSharp()) {
            list.add(new EnchantmentInstance(Enchantments.SHARPNESS, 2));
        }
        if (tool instanceof GenericWeaponItem) {
            list.add(new EnchantmentInstance(Enchantments.MOB_LOOTING, 2));
        }
    }

    private boolean canTrigger(LivingEntity target) {
        return target.getAttribute(Attributes.ARMOR) == null || target.getAttributeValue(Attributes.ARMOR) == 0.0;
    }

    private void addEffect(LivingEntity user, LivingEntity target) {
        if (this.canTrigger(target)) {
            double chance = LWConfig.COMMON.steeleafChance.get();
            if (user.getRandom().nextDouble() < chance) {
                ((BleedEffect) LCEffects.BLEED.get()).addTo(target, 100, 4, EffectUtil.AddReason.NONE, user);
            }
        }
    }

    @Override
    public void onShieldBlock(ItemStack stack, LivingEntity user, LivingEntity entity) {
        this.addEffect(user, entity);
    }

    @Override
    public double onShieldReflect(ItemStack stack, LivingEntity user, LivingEntity entity, double original, double reflect) {
        return reflect + original * LWConfig.COMMON.steeleafReflect.get();
    }

    public void onDamage(AttackCache cache, ItemStack stack) {
        LivingEntity target = cache.getAttackTarget();
        if (this.canTrigger(target)) {
            double bonus = LWConfig.COMMON.steeleafBonus.get();
            cache.addHurtModifier(DamageModifier.multBase((float) bonus));
            if (cache.getAttacker() != null) {
                this.addEffect(cache.getAttacker(), target);
            }
        }
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        double bonus = LWConfig.COMMON.steeleafBonus.get();
        double chance = LWConfig.COMMON.steeleafChance.get();
        list.add(LangData.MATS_STEELEAF.get((int) Math.round(bonus * 100.0), (int) Math.round(chance * 100.0)));
        if (stack.getItem() instanceof BaseShieldItem) {
            double reflect = LWConfig.COMMON.steeleafReflect.get();
            list.add(LangData.MATS_REFLECT.get((int) Math.round(reflect * 100.0)));
        }
    }
}