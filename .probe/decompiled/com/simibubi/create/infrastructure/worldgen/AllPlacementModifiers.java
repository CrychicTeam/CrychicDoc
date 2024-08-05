package com.simibubi.create.infrastructure.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AllPlacementModifiers {

    private static final DeferredRegister<PlacementModifierType<?>> REGISTER = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, "create");

    public static final RegistryObject<PlacementModifierType<ConfigPlacementFilter>> CONFIG_FILTER = REGISTER.register("config_filter", () -> () -> ConfigPlacementFilter.CODEC);

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}