package com.github.alexthe666.alexsmobs.inventory;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AMMenuRegistry {

    public static final DeferredRegister<MenuType<?>> DEF_REG = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "alexsmobs");

    public static final RegistryObject<MenuType<MenuTransmutationTable>> TRANSMUTATION_TABLE = DEF_REG.register("transmutation_table", () -> new MenuType(MenuTransmutationTable::new, FeatureFlags.DEFAULT_FLAGS));
}