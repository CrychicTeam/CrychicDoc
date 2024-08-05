package me.jellysquid.mods.lithium.common.entity.pushable;

import cpw.mods.modlauncher.api.INameMappingService.Domain;
import me.jellysquid.mods.lithium.common.entity.EntityClassGroup;
import me.jellysquid.mods.lithium.common.reflection.ReflectionUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class PushableEntityClassGroup {

    public static final EntityClassGroup CACHABLE_UNPUSHABILITY;

    public static final EntityClassGroup MAYBE_PUSHABLE;

    static {
        String remapped_isClimbing = ObfuscationReflectionHelper.remapName(Domain.METHOD, "m_6147_");
        String remapped_isPushable = ObfuscationReflectionHelper.remapName(Domain.METHOD, "m_6094_");
        CACHABLE_UNPUSHABILITY = new EntityClassGroup(entityClass -> LivingEntity.class.isAssignableFrom(entityClass) && !Player.class.isAssignableFrom(entityClass) && !ReflectionUtil.hasMethodOverride(entityClass, LivingEntity.class, true, remapped_isPushable) && !ReflectionUtil.hasMethodOverride(entityClass, LivingEntity.class, true, remapped_isClimbing));
        MAYBE_PUSHABLE = new EntityClassGroup(entityClass -> {
            if (ReflectionUtil.hasMethodOverride(entityClass, Entity.class, true, remapped_isPushable)) {
                if (EnderDragon.class.isAssignableFrom(entityClass)) {
                    return false;
                } else if (ArmorStand.class.isAssignableFrom(entityClass)) {
                    return ReflectionUtil.hasMethodOverride(entityClass, ArmorStand.class, true, remapped_isPushable);
                } else {
                    return Bat.class.isAssignableFrom(entityClass) ? ReflectionUtil.hasMethodOverride(entityClass, Bat.class, true, remapped_isPushable) : true;
                }
            } else {
                return Player.class.isAssignableFrom(entityClass);
            }
        });
    }
}