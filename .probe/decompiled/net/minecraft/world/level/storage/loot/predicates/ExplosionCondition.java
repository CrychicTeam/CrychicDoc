package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class ExplosionCondition implements LootItemCondition {

    static final ExplosionCondition INSTANCE = new ExplosionCondition();

    private ExplosionCondition() {
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.SURVIVES_EXPLOSION;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.EXPLOSION_RADIUS);
    }

    public boolean test(LootContext lootContext0) {
        Float $$1 = lootContext0.getParamOrNull(LootContextParams.EXPLOSION_RADIUS);
        if ($$1 != null) {
            RandomSource $$2 = lootContext0.getRandom();
            float $$3 = 1.0F / $$1;
            return $$2.nextFloat() <= $$3;
        } else {
            return true;
        }
    }

    public static LootItemCondition.Builder survivesExplosion() {
        return () -> INSTANCE;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ExplosionCondition> {

        public void serialize(JsonObject jsonObject0, ExplosionCondition explosionCondition1, JsonSerializationContext jsonSerializationContext2) {
        }

        public ExplosionCondition deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            return ExplosionCondition.INSTANCE;
        }
    }
}