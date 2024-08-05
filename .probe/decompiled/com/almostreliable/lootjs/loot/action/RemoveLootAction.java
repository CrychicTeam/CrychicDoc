package com.almostreliable.lootjs.loot.action;

import com.almostreliable.lootjs.core.ILootAction;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class RemoveLootAction implements ILootAction {

    private final Predicate<ItemStack> predicate;

    public RemoveLootAction(Predicate<ItemStack> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        loot.removeIf(this.predicate);
        return true;
    }
}