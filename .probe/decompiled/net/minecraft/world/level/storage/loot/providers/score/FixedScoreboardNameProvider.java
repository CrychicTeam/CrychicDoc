package net.minecraft.world.level.storage.loot.providers.score;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class FixedScoreboardNameProvider implements ScoreboardNameProvider {

    final String name;

    FixedScoreboardNameProvider(String string0) {
        this.name = string0;
    }

    public static ScoreboardNameProvider forName(String string0) {
        return new FixedScoreboardNameProvider(string0);
    }

    @Override
    public LootScoreProviderType getType() {
        return ScoreboardNameProviders.FIXED;
    }

    public String getName() {
        return this.name;
    }

    @Nullable
    @Override
    public String getScoreboardName(LootContext lootContext0) {
        return this.name;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of();
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<FixedScoreboardNameProvider> {

        public void serialize(JsonObject jsonObject0, FixedScoreboardNameProvider fixedScoreboardNameProvider1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("name", fixedScoreboardNameProvider1.name);
        }

        public FixedScoreboardNameProvider deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            String $$2 = GsonHelper.getAsString(jsonObject0, "name");
            return new FixedScoreboardNameProvider($$2);
        }
    }
}