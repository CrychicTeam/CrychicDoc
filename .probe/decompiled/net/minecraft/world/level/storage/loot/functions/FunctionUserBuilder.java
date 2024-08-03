package net.minecraft.world.level.storage.loot.functions;

import java.util.Arrays;
import java.util.function.Function;

public interface FunctionUserBuilder<T extends FunctionUserBuilder<T>> {

    T apply(LootItemFunction.Builder var1);

    default <E> T apply(Iterable<E> iterableE0, Function<E, LootItemFunction.Builder> functionELootItemFunctionBuilder1) {
        T $$2 = this.unwrap();
        for (E $$3 : iterableE0) {
            $$2 = $$2.apply((LootItemFunction.Builder) functionELootItemFunctionBuilder1.apply($$3));
        }
        return $$2;
    }

    default <E> T apply(E[] e0, Function<E, LootItemFunction.Builder> functionELootItemFunctionBuilder1) {
        return this.apply(Arrays.asList(e0), functionELootItemFunctionBuilder1);
    }

    T unwrap();
}