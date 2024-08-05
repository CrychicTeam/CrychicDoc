package net.minecraft.world.level.storage.loot.providers.score;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class ContextScoreboardNameProvider implements ScoreboardNameProvider {

    final LootContext.EntityTarget target;

    ContextScoreboardNameProvider(LootContext.EntityTarget lootContextEntityTarget0) {
        this.target = lootContextEntityTarget0;
    }

    public static ScoreboardNameProvider forTarget(LootContext.EntityTarget lootContextEntityTarget0) {
        return new ContextScoreboardNameProvider(lootContextEntityTarget0);
    }

    @Override
    public LootScoreProviderType getType() {
        return ScoreboardNameProviders.CONTEXT;
    }

    @Nullable
    @Override
    public String getScoreboardName(LootContext lootContext0) {
        Entity $$1 = lootContext0.getParamOrNull(this.target.getParam());
        return $$1 != null ? $$1.getScoreboardName() : null;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(this.target.getParam());
    }

    public static class InlineSerializer implements GsonAdapterFactory.InlineSerializer<ContextScoreboardNameProvider> {

        public JsonElement serialize(ContextScoreboardNameProvider contextScoreboardNameProvider0, JsonSerializationContext jsonSerializationContext1) {
            return jsonSerializationContext1.serialize(contextScoreboardNameProvider0.target);
        }

        public ContextScoreboardNameProvider deserialize(JsonElement jsonElement0, JsonDeserializationContext jsonDeserializationContext1) {
            LootContext.EntityTarget $$2 = (LootContext.EntityTarget) jsonDeserializationContext1.deserialize(jsonElement0, LootContext.EntityTarget.class);
            return new ContextScoreboardNameProvider($$2);
        }
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ContextScoreboardNameProvider> {

        public void serialize(JsonObject jsonObject0, ContextScoreboardNameProvider contextScoreboardNameProvider1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("target", contextScoreboardNameProvider1.target.name());
        }

        public ContextScoreboardNameProvider deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            LootContext.EntityTarget $$2 = GsonHelper.getAsObject(jsonObject0, "target", jsonDeserializationContext1, LootContext.EntityTarget.class);
            return new ContextScoreboardNameProvider($$2);
        }
    }
}