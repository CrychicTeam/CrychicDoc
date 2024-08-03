package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.SelfEffectTrait;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class InvisibleTrait extends SelfEffectTrait {

    public InvisibleTrait() {
        super(() -> MobEffects.INVISIBILITY);
    }

    @Override
    public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
        return ((MobTraitCap) MobTraitCap.HOLDER.get(le)).isSummoned() ? false : super.allow(le, difficulty, maxModLv);
    }

    @Override
    public void postInit(LivingEntity mob, int lv) {
        for (EquipmentSlot e : EquipmentSlot.values()) {
            ItemStack stack = mob.getItemBySlot(e);
            if (!stack.isEmpty() && stack.getEnchantmentLevel((Enchantment) LCEnchantments.SHULKER_ARMOR.get()) == 0) {
                stack.enchant((Enchantment) LCEnchantments.SHULKER_ARMOR.get(), 1);
            }
        }
    }
}