package com.almostreliable.lootjs.loot.condition;

import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class CustomParamPredicate<T> implements IExtendedLootCondition {

    private final Predicate<T> predicate;

    private final LootContextParam<T> param;

    public CustomParamPredicate(LootContextParam<T> param, Predicate<T> predicate) {
        Objects.requireNonNull(param);
        Objects.requireNonNull(predicate);
        this.param = param;
        this.predicate = predicate;
    }

    public boolean test(LootContext lootContext) {
        T paramOrNull = lootContext.getParamOrNull(this.param);
        return paramOrNull != null && this.predicate.test(paramOrNull);
    }
}