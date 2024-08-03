package com.github.alexmodguy.alexscaves.server.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ACFrogRegistry {

    public static final DeferredRegister<FrogVariant> DEF_REG = DeferredRegister.create(Registries.FROG_VARIANT, "alexscaves");

    public static final RegistryObject<FrogVariant> PRIMORDIAL = DEF_REG.register("primordial", () -> new FrogVariant(new ResourceLocation("alexscaves", "textures/entity/primordial_frog.png")));
}