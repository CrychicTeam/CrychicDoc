package io.github.apace100.origins.registry;

import io.github.apace100.origins.Origins;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageSources {

    public static final ResourceKey<DamageType> NO_WATER_FOR_GILLS = ResourceKey.create(Registries.DAMAGE_TYPE, Origins.identifier("no_water_for_gills"));
}