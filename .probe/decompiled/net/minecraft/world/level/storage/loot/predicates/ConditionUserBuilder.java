package net.minecraft.world.level.storage.loot.predicates;

import java.util.function.Function;

public interface ConditionUserBuilder<T extends ConditionUserBuilder<T>> {

    T when(LootItemCondition.Builder var1);

    default <E> T when(Iterable<E> iterableE0, Function<E, LootItemCondition.Builder> functionELootItemConditionBuilder1) {
        T $$2 = this.unwrap();
        for (E $$3 : iterableE0) {
            $$2 = $$2.when((LootItemCondition.Builder) functionELootItemConditionBuilder1.apply($$3));
        }
        return $$2;
    }

    T unwrap();
}