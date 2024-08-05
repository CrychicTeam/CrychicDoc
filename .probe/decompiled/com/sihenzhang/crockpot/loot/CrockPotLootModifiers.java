package com.sihenzhang.crockpot.loot;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CrockPotLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, "crockpot");

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM = LOOT_MODIFIERS.register("add_item", AddItemModifier.CODEC);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM_WITH_LOOTING_ENCHANT = LOOT_MODIFIERS.register("add_item_with_looting_enchant", AddItemWithLootingEnchantModifier.CODEC);

    private CrockPotLootModifiers() {
    }
}