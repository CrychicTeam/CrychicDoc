package net.minecraft.world.level.storage.loot.providers.number;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.providers.score.ContextScoreboardNameProvider;
import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProvider;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;

public class ScoreboardValue implements NumberProvider {

    final ScoreboardNameProvider target;

    final String score;

    final float scale;

    ScoreboardValue(ScoreboardNameProvider scoreboardNameProvider0, String string1, float float2) {
        this.target = scoreboardNameProvider0;
        this.score = string1;
        this.scale = float2;
    }

    @Override
    public LootNumberProviderType getType() {
        return NumberProviders.SCORE;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.target.getReferencedContextParams();
    }

    public static ScoreboardValue fromScoreboard(LootContext.EntityTarget lootContextEntityTarget0, String string1) {
        return fromScoreboard(lootContextEntityTarget0, string1, 1.0F);
    }

    public static ScoreboardValue fromScoreboard(LootContext.EntityTarget lootContextEntityTarget0, String string1, float float2) {
        return new ScoreboardValue(ContextScoreboardNameProvider.forTarget(lootContextEntityTarget0), string1, float2);
    }

    @Override
    public float getFloat(LootContext lootContext0) {
        String $$1 = this.target.getScoreboardName(lootContext0);
        if ($$1 == null) {
            return 0.0F;
        } else {
            Scoreboard $$2 = lootContext0.getLevel().getScoreboard();
            Objective $$3 = $$2.getObjective(this.score);
            if ($$3 == null) {
                return 0.0F;
            } else {
                return !$$2.hasPlayerScore($$1, $$3) ? 0.0F : (float) $$2.getOrCreatePlayerScore($$1, $$3).getScore() * this.scale;
            }
        }
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ScoreboardValue> {

        public ScoreboardValue deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            String $$2 = GsonHelper.getAsString(jsonObject0, "score");
            float $$3 = GsonHelper.getAsFloat(jsonObject0, "scale", 1.0F);
            ScoreboardNameProvider $$4 = GsonHelper.getAsObject(jsonObject0, "target", jsonDeserializationContext1, ScoreboardNameProvider.class);
            return new ScoreboardValue($$4, $$2, $$3);
        }

        public void serialize(JsonObject jsonObject0, ScoreboardValue scoreboardValue1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("score", scoreboardValue1.score);
            jsonObject0.add("target", jsonSerializationContext2.serialize(scoreboardValue1.target));
            jsonObject0.addProperty("scale", scoreboardValue1.scale);
        }
    }
}