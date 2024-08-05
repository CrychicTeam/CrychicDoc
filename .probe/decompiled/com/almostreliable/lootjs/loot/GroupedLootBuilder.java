package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.ILootAction;
import com.almostreliable.lootjs.core.ILootCondition;
import com.almostreliable.lootjs.core.ILootHandler;
import com.almostreliable.lootjs.kube.LootConditionsContainer;
import com.almostreliable.lootjs.loot.action.GroupedLootAction;
import com.almostreliable.lootjs.loot.action.LootItemFunctionWrapperAction;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class GroupedLootBuilder implements LootConditionsContainer<GroupedLootBuilder>, LootFunctionsContainer<GroupedLootBuilder>, LootActionsContainer<GroupedLootBuilder> {

    private final List<ILootHandler> handlers = new ArrayList();

    private NumberProvider numberProvider = ConstantValue.exactly(1.0F);

    public GroupedLootBuilder rolls(NumberProvider numberProvider) {
        this.numberProvider = numberProvider;
        return this;
    }

    public GroupedLootBuilder addCondition(ILootCondition condition) {
        this.handlers.add(condition);
        return this;
    }

    public GroupedLootBuilder addAction(ILootAction action) {
        this.handlers.add(action);
        return this;
    }

    public GroupedLootAction build() {
        return new GroupedLootAction(this.numberProvider, this.handlers);
    }

    public GroupedLootBuilder addFunction(LootItemFunction lootItemFunction) {
        return this.addAction(new LootItemFunctionWrapperAction(lootItemFunction));
    }
}