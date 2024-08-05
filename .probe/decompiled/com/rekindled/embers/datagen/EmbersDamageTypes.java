package com.rekindled.embers.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class EmbersDamageTypes {

    public static final ResourceKey<DamageType> EMBER_KEY = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("embers", "ember"));

    public static final DamageType EMBER = new DamageType("ember", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F);

    public static void generate(BootstapContext<DamageType> bootstrap) {
        bootstrap.register(EMBER_KEY, EMBER);
    }
}