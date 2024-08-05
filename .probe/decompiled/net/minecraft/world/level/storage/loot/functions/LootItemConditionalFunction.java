package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.apache.commons.lang3.ArrayUtils;

public abstract class LootItemConditionalFunction implements LootItemFunction {

    protected final LootItemCondition[] predicates;

    private final Predicate<LootContext> compositePredicates;

    protected LootItemConditionalFunction(LootItemCondition[] lootItemCondition0) {
        this.predicates = lootItemCondition0;
        this.compositePredicates = LootItemConditions.andConditions(lootItemCondition0);
    }

    public final ItemStack apply(ItemStack itemStack0, LootContext lootContext1) {
        return this.compositePredicates.test(lootContext1) ? this.run(itemStack0, lootContext1) : itemStack0;
    }

    protected abstract ItemStack run(ItemStack var1, LootContext var2);

    @Override
    public void validate(ValidationContext validationContext0) {
        LootItemFunction.super.m_6169_(validationContext0);
        for (int $$1 = 0; $$1 < this.predicates.length; $$1++) {
            this.predicates[$$1].m_6169_(validationContext0.forChild(".conditions[" + $$1 + "]"));
        }
    }

    protected static LootItemConditionalFunction.Builder<?> simpleBuilder(Function<LootItemCondition[], LootItemFunction> functionLootItemConditionLootItemFunction0) {
        return new LootItemConditionalFunction.DummyBuilder(functionLootItemConditionLootItemFunction0);
    }

    public abstract static class Builder<T extends LootItemConditionalFunction.Builder<T>> implements LootItemFunction.Builder, ConditionUserBuilder<T> {

        private final List<LootItemCondition> conditions = Lists.newArrayList();

        public T when(LootItemCondition.Builder lootItemConditionBuilder0) {
            this.conditions.add(lootItemConditionBuilder0.build());
            return this.getThis();
        }

        public final T unwrap() {
            return this.getThis();
        }

        protected abstract T getThis();

        protected LootItemCondition[] getConditions() {
            return (LootItemCondition[]) this.conditions.toArray(new LootItemCondition[0]);
        }
    }

    static final class DummyBuilder extends LootItemConditionalFunction.Builder<LootItemConditionalFunction.DummyBuilder> {

        private final Function<LootItemCondition[], LootItemFunction> constructor;

        public DummyBuilder(Function<LootItemCondition[], LootItemFunction> functionLootItemConditionLootItemFunction0) {
            this.constructor = functionLootItemConditionLootItemFunction0;
        }

        protected LootItemConditionalFunction.DummyBuilder getThis() {
            return this;
        }

        @Override
        public LootItemFunction build() {
            return (LootItemFunction) this.constructor.apply(this.m_80699_());
        }
    }

    public abstract static class Serializer<T extends LootItemConditionalFunction> implements net.minecraft.world.level.storage.loot.Serializer<T> {

        public void serialize(JsonObject jsonObject0, T t1, JsonSerializationContext jsonSerializationContext2) {
            if (!ArrayUtils.isEmpty(t1.predicates)) {
                jsonObject0.add("conditions", jsonSerializationContext2.serialize(t1.predicates));
            }
        }

        public final T deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            LootItemCondition[] $$2 = GsonHelper.getAsObject(jsonObject0, "conditions", new LootItemCondition[0], jsonDeserializationContext1, LootItemCondition[].class);
            return this.deserialize(jsonObject0, jsonDeserializationContext1, $$2);
        }

        public abstract T deserialize(JsonObject var1, JsonDeserializationContext var2, LootItemCondition[] var3);
    }
}