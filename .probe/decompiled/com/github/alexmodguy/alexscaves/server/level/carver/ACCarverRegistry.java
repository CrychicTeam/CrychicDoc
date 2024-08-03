package com.github.alexmodguy.alexscaves.server.level.carver;

import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACCarverRegistry {

    public static final DeferredRegister<WorldCarver<?>> DEF_REG = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, "alexscaves");

    public static RegistryObject<WaterBubbleCarver> WATER_BUBBLE_CARVER = DEF_REG.register("water_bubble_carver", () -> new WaterBubbleCarver(CaveCarverConfiguration.CODEC));
}