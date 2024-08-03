package com.almostreliable.lootjs.loot.action;

import com.almostreliable.lootjs.core.ILootAction;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class ModifyLootAction implements ILootAction {

    private final Predicate<ItemStack> predicate;

    private final ModifyLootAction.Callback callback;

    public ModifyLootAction(Predicate<ItemStack> predicate, ModifyLootAction.Callback callback) {
        this.predicate = predicate;
        this.callback = callback;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        for (int i = 0; i < loot.size(); i++) {
            if (this.predicate.test((ItemStack) loot.get(i))) {
                ItemStack currentItemStack = (ItemStack) loot.get(i);
                loot.set(i, this.callback.modify(currentItemStack).copy());
            }
        }
        return true;
    }

    @FunctionalInterface
    public interface Callback {

        ItemStack modify(ItemStack var1);
    }
}