package dev.xkmc.l2hostility.content.enchantments;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.minecraft.world.entity.LivingEntity;

public interface HitTargetEnchantment {

    void hitMob(LivingEntity var1, MobTraitCap var2, Integer var3, AttackCache var4);
}