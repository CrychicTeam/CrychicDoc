package net.minecraft.world.level.storage.loot.entries;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.apache.commons.lang3.ArrayUtils;

public abstract class LootPoolEntryContainer implements ComposableEntryContainer {

    protected final LootItemCondition[] conditions;

    private final Predicate<LootContext> compositeCondition;

    protected LootPoolEntryContainer(LootItemCondition[] lootItemCondition0) {
        this.conditions = lootItemCondition0;
        this.compositeCondition = LootItemConditions.andConditions(lootItemCondition0);
    }

    public void validate(ValidationContext validationContext0) {
        for (int $$1 = 0; $$1 < this.conditions.length; $$1++) {
            this.conditions[$$1].m_6169_(validationContext0.forChild(".condition[" + $$1 + "]"));
        }
    }

    protected final boolean canRun(LootContext lootContext0) {
        return this.compositeCondition.test(lootContext0);
    }

    public abstract LootPoolEntryType getType();

    public abstract static class Builder<T extends LootPoolEntryContainer.Builder<T>> implements ConditionUserBuilder<T> {

        private final List<LootItemCondition> conditions = Lists.newArrayList();

        protected abstract T getThis();

        public T when(LootItemCondition.Builder lootItemConditionBuilder0) {
            this.conditions.add(lootItemConditionBuilder0.build());
            return this.getThis();
        }

        public final T unwrap() {
            return this.getThis();
        }

        protected LootItemCondition[] getConditions() {
            return (LootItemCondition[]) this.conditions.toArray(new LootItemCondition[0]);
        }

        public AlternativesEntry.Builder otherwise(LootPoolEntryContainer.Builder<?> lootPoolEntryContainerBuilder0) {
            return new AlternativesEntry.Builder(this, lootPoolEntryContainerBuilder0);
        }

        public EntryGroup.Builder append(LootPoolEntryContainer.Builder<?> lootPoolEntryContainerBuilder0) {
            return new EntryGroup.Builder(this, lootPoolEntryContainerBuilder0);
        }

        public SequentialEntry.Builder then(LootPoolEntryContainer.Builder<?> lootPoolEntryContainerBuilder0) {
            return new SequentialEntry.Builder(this, lootPoolEntryContainerBuilder0);
        }

        public abstract LootPoolEntryContainer build();
    }

    public abstract static class Serializer<T extends LootPoolEntryContainer> implements net.minecraft.world.level.storage.loot.Serializer<T> {

        public final void serialize(JsonObject jsonObject0, T t1, JsonSerializationContext jsonSerializationContext2) {
            if (!ArrayUtils.isEmpty(t1.conditions)) {
                jsonObject0.add("conditions", jsonSerializationContext2.serialize(t1.conditions));
            }
            this.serializeCustom(jsonObject0, t1, jsonSerializationContext2);
        }

        public final T deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            LootItemCondition[] $$2 = GsonHelper.getAsObject(jsonObject0, "conditions", new LootItemCondition[0], jsonDeserializationContext1, LootItemCondition[].class);
            return this.deserializeCustom(jsonObject0, jsonDeserializationContext1, $$2);
        }

        public abstract void serializeCustom(JsonObject var1, T var2, JsonSerializationContext var3);

        public abstract T deserializeCustom(JsonObject var1, JsonDeserializationContext var2, LootItemCondition[] var3);
    }
}