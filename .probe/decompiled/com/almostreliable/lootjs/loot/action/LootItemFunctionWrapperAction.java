package com.almostreliable.lootjs.loot.action;

import com.almostreliable.lootjs.core.ILootAction;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class LootItemFunctionWrapperAction implements ILootAction {

    private final LootItemFunction lootItemFunction;

    public LootItemFunctionWrapperAction(LootItemFunction lootItemFunction) {
        this.lootItemFunction = lootItemFunction;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        loot.replaceAll(itemStack -> (ItemStack) this.lootItemFunction.apply(itemStack, context));
        return true;
    }

    public LootItemFunction getLootItemFunction() {
        return this.lootItemFunction;
    }

    public static class CompositeLootItemFunction implements LootItemFunction {

        private final List<LootItemFunction> lootItemFunctions;

        private final Predicate<ItemStack> filter;

        public CompositeLootItemFunction(List<LootItemFunction> lootItemFunctions, Predicate<ItemStack> filter) {
            this.lootItemFunctions = lootItemFunctions;
            this.filter = filter;
        }

        @Override
        public LootItemFunctionType getType() {
            return null;
        }

        public ItemStack apply(ItemStack itemStack, LootContext context) {
            if (!this.filter.test(itemStack)) {
                return itemStack;
            } else {
                itemStack = itemStack.copy();
                for (LootItemFunction lootItemFunction : this.lootItemFunctions) {
                    itemStack = (ItemStack) lootItemFunction.apply(itemStack, context);
                }
                return itemStack;
            }
        }
    }
}