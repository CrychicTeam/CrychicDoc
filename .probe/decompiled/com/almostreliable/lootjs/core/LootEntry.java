package com.almostreliable.lootjs.core;

import com.almostreliable.lootjs.kube.LootConditionsContainer;
import com.almostreliable.lootjs.loot.LootFunctionsContainer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public class LootEntry implements LootFunctionsContainer<LootEntry> {

    private final LootEntry.Generator generator;

    private final List<LootItemFunction> postModifications = new ArrayList();

    private final List<ILootCondition> conditions = new ArrayList();

    private int weight = 1;

    public LootEntry(Item item) {
        this.generator = new LootEntry.ItemGenerator(new ItemStack(item));
    }

    public LootEntry(ItemStack itemStack) {
        this.generator = new LootEntry.ItemGenerator(itemStack);
    }

    public LootEntry(LootEntry.Generator generator) {
        this.generator = generator;
    }

    @Nullable
    public ItemStack createItem(LootContext context) {
        if (!this.matchesConditions(context)) {
            return null;
        } else {
            ItemStack itemStack = this.generator.create(context);
            if (itemStack == null) {
                return null;
            } else if (itemStack.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                ItemStack result = itemStack.copy();
                for (LootItemFunction lootItemFunction : this.postModifications) {
                    result = (ItemStack) lootItemFunction.apply(result, context);
                }
                return result;
            }
        }
    }

    public LootEntry addFunction(LootItemFunction lootItemFunction) {
        this.postModifications.add(lootItemFunction);
        return this;
    }

    public boolean matchesConditions(LootContext context) {
        for (ILootCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasWeight() {
        return this.weight >= 1;
    }

    public int getWeight() {
        return this.weight;
    }

    public LootEntry withWeight(int weight) {
        this.weight = Math.max(1, weight);
        return this;
    }

    public LootEntry withChance(int chance) {
        return this.withWeight(chance);
    }

    public LootEntry when(Consumer<LootConditionsContainer<?>> action) {
        final List<ILootCondition> conditions = new ArrayList();
        var lcc = new LootConditionsContainer<LootConditionsContainer<?>>() {

            @Override
            public LootConditionsContainer<?> addCondition(ILootCondition condition) {
                conditions.add(condition);
                return this;
            }
        };
        action.accept(lcc);
        this.conditions.addAll(conditions);
        return this;
    }

    public interface Generator {

        @Nullable
        ItemStack create(LootContext var1);
    }

    public static record ItemGenerator(ItemStack item) implements LootEntry.Generator {

        @Nullable
        @Override
        public ItemStack create(LootContext context) {
            return this.item;
        }
    }

    public static record RandomIngredientGenerator(Ingredient ingredient) implements LootEntry.Generator {

        @Nullable
        @Override
        public ItemStack create(LootContext context) {
            ItemStack[] items = this.ingredient.getItems();
            if (items.length == 0) {
                return ItemStack.EMPTY;
            } else {
                int index = context.getRandom().nextInt(items.length);
                return items[index];
            }
        }
    }

    public static record VanillaWrappedLootEntry(LootPoolEntryContainer entry) implements LootEntry.Generator {

        @Override
        public ItemStack create(LootContext context) {
            List<ItemStack> items = new ArrayList();
            this.entry.m_6562_(context, entry -> entry.createItemStack(items::add, context));
            return items.isEmpty() ? null : (ItemStack) items.get(context.getRandom().nextInt(items.size()));
        }
    }
}