package net.minecraft.world.level.storage.loot.entries;

import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.LootContext;

@FunctionalInterface
interface ComposableEntryContainer {

    ComposableEntryContainer ALWAYS_FALSE = (p_79418_, p_79419_) -> false;

    ComposableEntryContainer ALWAYS_TRUE = (p_79409_, p_79410_) -> true;

    boolean expand(LootContext var1, Consumer<LootPoolEntry> var2);

    default ComposableEntryContainer and(ComposableEntryContainer composableEntryContainer0) {
        Objects.requireNonNull(composableEntryContainer0);
        return (p_79424_, p_79425_) -> this.expand(p_79424_, p_79425_) && composableEntryContainer0.expand(p_79424_, p_79425_);
    }

    default ComposableEntryContainer or(ComposableEntryContainer composableEntryContainer0) {
        Objects.requireNonNull(composableEntryContainer0);
        return (p_79415_, p_79416_) -> this.expand(p_79415_, p_79416_) || composableEntryContainer0.expand(p_79415_, p_79416_);
    }
}