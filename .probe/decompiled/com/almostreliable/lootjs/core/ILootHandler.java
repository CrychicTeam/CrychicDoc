package com.almostreliable.lootjs.core;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public interface ILootHandler {

    boolean applyLootHandler(LootContext var1, List<ItemStack> var2);
}