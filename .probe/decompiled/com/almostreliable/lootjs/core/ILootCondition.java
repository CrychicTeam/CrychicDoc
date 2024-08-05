package com.almostreliable.lootjs.core;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public interface ILootCondition extends ILootHandler, Predicate<LootContext> {

    @Override
    default boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        return this.test(context);
    }
}