package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.minecraft.world.entity.LivingEntity;

public record InheritContext(LivingEntity parent, MobTraitCap parentCap, LivingEntity child, MobTraitCap childCap, boolean isPrimary) {
}