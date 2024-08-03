package net.blay09.mods.balm.api.loot;

import net.minecraft.resources.ResourceLocation;

public interface BalmLootTables {

    void registerLootModifier(ResourceLocation var1, BalmLootModifier var2);
}