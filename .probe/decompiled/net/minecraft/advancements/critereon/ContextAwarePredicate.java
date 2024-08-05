package net.minecraft.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

public class ContextAwarePredicate {

    public static final ContextAwarePredicate ANY = new ContextAwarePredicate(new LootItemCondition[0]);

    private final LootItemCondition[] conditions;

    private final Predicate<LootContext> compositePredicates;

    ContextAwarePredicate(LootItemCondition[] lootItemCondition0) {
        this.conditions = lootItemCondition0;
        this.compositePredicates = LootItemConditions.andConditions(lootItemCondition0);
    }

    public static ContextAwarePredicate create(LootItemCondition... lootItemCondition0) {
        return new ContextAwarePredicate(lootItemCondition0);
    }

    @Nullable
    public static ContextAwarePredicate fromElement(String string0, DeserializationContext deserializationContext1, @Nullable JsonElement jsonElement2, LootContextParamSet lootContextParamSet3) {
        if (jsonElement2 != null && jsonElement2.isJsonArray()) {
            LootItemCondition[] $$4 = deserializationContext1.deserializeConditions(jsonElement2.getAsJsonArray(), deserializationContext1.getAdvancementId() + "/" + string0, lootContextParamSet3);
            return new ContextAwarePredicate($$4);
        } else {
            return null;
        }
    }

    public boolean matches(LootContext lootContext0) {
        return this.compositePredicates.test(lootContext0);
    }

    public JsonElement toJson(SerializationContext serializationContext0) {
        return (JsonElement) (this.conditions.length == 0 ? JsonNull.INSTANCE : serializationContext0.serializeConditions(this.conditions));
    }

    public static JsonElement toJson(ContextAwarePredicate[] contextAwarePredicate0, SerializationContext serializationContext1) {
        if (contextAwarePredicate0.length == 0) {
            return JsonNull.INSTANCE;
        } else {
            JsonArray $$2 = new JsonArray();
            for (ContextAwarePredicate $$3 : contextAwarePredicate0) {
                $$2.add($$3.toJson(serializationContext1));
            }
            return $$2;
        }
    }
}