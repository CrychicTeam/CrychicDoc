package net.minecraft.world.level.storage.loot.predicates;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;

public abstract class CompositeLootItemCondition implements LootItemCondition {

    final LootItemCondition[] terms;

    private final Predicate<LootContext> composedPredicate;

    protected CompositeLootItemCondition(LootItemCondition[] lootItemCondition0, Predicate<LootContext> predicateLootContext1) {
        this.terms = lootItemCondition0;
        this.composedPredicate = predicateLootContext1;
    }

    public final boolean test(LootContext lootContext0) {
        return this.composedPredicate.test(lootContext0);
    }

    @Override
    public void validate(ValidationContext validationContext0) {
        LootItemCondition.super.m_6169_(validationContext0);
        for (int $$1 = 0; $$1 < this.terms.length; $$1++) {
            this.terms[$$1].m_6169_(validationContext0.forChild(".term[" + $$1 + "]"));
        }
    }

    public abstract static class Builder implements LootItemCondition.Builder {

        private final List<LootItemCondition> terms = new ArrayList();

        public Builder(LootItemCondition.Builder... lootItemConditionBuilder0) {
            for (LootItemCondition.Builder $$1 : lootItemConditionBuilder0) {
                this.terms.add($$1.build());
            }
        }

        public void addTerm(LootItemCondition.Builder lootItemConditionBuilder0) {
            this.terms.add(lootItemConditionBuilder0.build());
        }

        @Override
        public LootItemCondition build() {
            LootItemCondition[] $$0 = (LootItemCondition[]) this.terms.toArray(LootItemCondition[]::new);
            return this.create($$0);
        }

        protected abstract LootItemCondition create(LootItemCondition[] var1);
    }

    public abstract static class Serializer<T extends CompositeLootItemCondition> implements net.minecraft.world.level.storage.loot.Serializer<T> {

        public void serialize(JsonObject jsonObject0, CompositeLootItemCondition compositeLootItemCondition1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.add("terms", jsonSerializationContext2.serialize(compositeLootItemCondition1.terms));
        }

        public T deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            LootItemCondition[] $$2 = GsonHelper.getAsObject(jsonObject0, "terms", jsonDeserializationContext1, LootItemCondition[].class);
            return this.create($$2);
        }

        protected abstract T create(LootItemCondition[] var1);
    }
}