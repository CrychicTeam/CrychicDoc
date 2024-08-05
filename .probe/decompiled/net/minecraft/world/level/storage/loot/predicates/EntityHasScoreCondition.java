package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;

public class EntityHasScoreCondition implements LootItemCondition {

    final Map<String, IntRange> scores;

    final LootContext.EntityTarget entityTarget;

    EntityHasScoreCondition(Map<String, IntRange> mapStringIntRange0, LootContext.EntityTarget lootContextEntityTarget1) {
        this.scores = ImmutableMap.copyOf(mapStringIntRange0);
        this.entityTarget = lootContextEntityTarget1;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.ENTITY_SCORES;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>) Stream.concat(Stream.of(this.entityTarget.getParam()), this.scores.values().stream().flatMap(p_165487_ -> p_165487_.getReferencedContextParams().stream())).collect(ImmutableSet.toImmutableSet());
    }

    public boolean test(LootContext lootContext0) {
        Entity $$1 = lootContext0.getParamOrNull(this.entityTarget.getParam());
        if ($$1 == null) {
            return false;
        } else {
            Scoreboard $$2 = $$1.level().getScoreboard();
            for (Entry<String, IntRange> $$3 : this.scores.entrySet()) {
                if (!this.hasScore(lootContext0, $$1, $$2, (String) $$3.getKey(), (IntRange) $$3.getValue())) {
                    return false;
                }
            }
            return true;
        }
    }

    protected boolean hasScore(LootContext lootContext0, Entity entity1, Scoreboard scoreboard2, String string3, IntRange intRange4) {
        Objective $$5 = scoreboard2.getObjective(string3);
        if ($$5 == null) {
            return false;
        } else {
            String $$6 = entity1.getScoreboardName();
            return !scoreboard2.hasPlayerScore($$6, $$5) ? false : intRange4.test(lootContext0, scoreboard2.getOrCreatePlayerScore($$6, $$5).getScore());
        }
    }

    public static EntityHasScoreCondition.Builder hasScores(LootContext.EntityTarget lootContextEntityTarget0) {
        return new EntityHasScoreCondition.Builder(lootContextEntityTarget0);
    }

    public static class Builder implements LootItemCondition.Builder {

        private final Map<String, IntRange> scores = Maps.newHashMap();

        private final LootContext.EntityTarget entityTarget;

        public Builder(LootContext.EntityTarget lootContextEntityTarget0) {
            this.entityTarget = lootContextEntityTarget0;
        }

        public EntityHasScoreCondition.Builder withScore(String string0, IntRange intRange1) {
            this.scores.put(string0, intRange1);
            return this;
        }

        @Override
        public LootItemCondition build() {
            return new EntityHasScoreCondition(this.scores, this.entityTarget);
        }
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<EntityHasScoreCondition> {

        public void serialize(JsonObject jsonObject0, EntityHasScoreCondition entityHasScoreCondition1, JsonSerializationContext jsonSerializationContext2) {
            JsonObject $$3 = new JsonObject();
            for (Entry<String, IntRange> $$4 : entityHasScoreCondition1.scores.entrySet()) {
                $$3.add((String) $$4.getKey(), jsonSerializationContext2.serialize($$4.getValue()));
            }
            jsonObject0.add("scores", $$3);
            jsonObject0.add("entity", jsonSerializationContext2.serialize(entityHasScoreCondition1.entityTarget));
        }

        public EntityHasScoreCondition deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            Set<Entry<String, JsonElement>> $$2 = GsonHelper.getAsJsonObject(jsonObject0, "scores").entrySet();
            Map<String, IntRange> $$3 = Maps.newLinkedHashMap();
            for (Entry<String, JsonElement> $$4 : $$2) {
                $$3.put((String) $$4.getKey(), (IntRange) GsonHelper.convertToObject((JsonElement) $$4.getValue(), "score", jsonDeserializationContext1, IntRange.class));
            }
            return new EntityHasScoreCondition($$3, GsonHelper.getAsObject(jsonObject0, "entity", jsonDeserializationContext1, LootContext.EntityTarget.class));
        }
    }
}