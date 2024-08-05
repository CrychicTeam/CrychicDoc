package com.sihenzhang.crockpot.base;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public final class CrockPotDamageTypes {

    public static final ResourceKey<DamageType> CANDY = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("crockpot", "candy"));

    public static final ResourceKey<DamageType> MONSTER_FOOD = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("crockpot", "monster_food"));

    public static final ResourceKey<DamageType> POW_CAKE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("crockpot", "pow_cake"));

    public static final ResourceKey<DamageType> SPICY = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("crockpot", "spicy"));

    public static final ResourceKey<DamageType> TAFFY = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("crockpot", "taffy"));
}