package com.almostreliable.lootjs.kube.builder;

import com.almostreliable.lootjs.core.ILootAction;
import com.almostreliable.lootjs.core.ILootCondition;
import com.almostreliable.lootjs.core.ILootHandler;
import com.almostreliable.lootjs.kube.LootConditionsContainer;
import com.almostreliable.lootjs.kube.LootContextJS;
import com.almostreliable.lootjs.kube.action.CustomJSAction;
import com.almostreliable.lootjs.loot.GroupedLootBuilder;
import com.almostreliable.lootjs.loot.LootActionsContainer;
import com.almostreliable.lootjs.loot.LootFunctionsContainer;
import com.almostreliable.lootjs.loot.action.CustomPlayerAction;
import com.almostreliable.lootjs.loot.action.LootItemFunctionWrapperAction;
import com.almostreliable.lootjs.util.Utils;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public class LootActionsBuilderJS implements LootConditionsContainer<LootActionsBuilderJS>, LootFunctionsContainer<LootActionsBuilderJS>, LootActionsContainer<LootActionsBuilderJS> {

    public static final String DEPRECATED_MSG = "[Deprecated in 1.18.2-2.4.0] Will be removed in future versions. Please use ";

    private final List<ILootHandler> handlers = new ArrayList();

    private String logName;

    @HideFromJS
    public List<ILootHandler> getHandlers() {
        if (this.handlers.isEmpty()) {
            throw new IllegalArgumentException("No actions were added to the modifier");
        } else {
            return this.handlers;
        }
    }

    public String getLogName(String alternative) {
        return this.logName == null ? alternative : Utils.quote(this.logName);
    }

    public LootActionsBuilderJS logName(String logName) {
        this.logName = logName;
        return this;
    }

    public LootActionsBuilderJS addCondition(ILootCondition condition) {
        this.handlers.add(condition);
        return this;
    }

    public LootActionsBuilderJS addAction(ILootAction action) {
        this.handlers.add(action);
        return this;
    }

    public LootActionsBuilderJS pool(Consumer<GroupedLootBuilder> callback) {
        return this.group(callback);
    }

    public LootActionsBuilderJS group(Consumer<GroupedLootBuilder> callback) {
        GroupedLootBuilder poolBuilder = new GroupedLootBuilder();
        callback.accept(poolBuilder);
        this.handlers.add(poolBuilder.build());
        return this;
    }

    public LootActionsBuilderJS apply(Consumer<LootContextJS> action) {
        this.handlers.add(new CustomJSAction(action));
        return this;
    }

    public LootActionsBuilderJS playerAction(Consumer<ServerPlayer> action) {
        this.handlers.add(new CustomPlayerAction(action));
        return this;
    }

    public LootActionsBuilderJS addFunction(LootItemFunction lootItemFunction) {
        return this.addAction(new LootItemFunctionWrapperAction(lootItemFunction));
    }
}