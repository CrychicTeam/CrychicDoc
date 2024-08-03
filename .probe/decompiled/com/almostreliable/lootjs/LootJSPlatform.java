package com.almostreliable.lootjs;

import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;

public interface LootJSPlatform {

    LootJSPlatform INSTANCE = PlatformLoader.load(LootJSPlatform.class);

    String getPlatformName();

    boolean isModLoaded(String var1);

    boolean isDevelopmentEnvironment();

    ResourceLocation getRegistryName(Block var1);

    ResourceLocation getRegistryName(EntityType<?> var1);

    ResourceLocation getQueriedLootTableId(LootContext var1);

    void setQueriedLootTableId(LootContext var1, ResourceLocation var2);

    void registerBindings(BindingsEvent var1);
}