package net.minecraft.world.level.storage.loot.predicates;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;

public class WeatherCheck implements LootItemCondition {

    @Nullable
    final Boolean isRaining;

    @Nullable
    final Boolean isThundering;

    WeatherCheck(@Nullable Boolean boolean0, @Nullable Boolean boolean1) {
        this.isRaining = boolean0;
        this.isThundering = boolean1;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.WEATHER_CHECK;
    }

    public boolean test(LootContext lootContext0) {
        ServerLevel $$1 = lootContext0.getLevel();
        return this.isRaining != null && this.isRaining != $$1.m_46471_() ? false : this.isThundering == null || this.isThundering == $$1.m_46470_();
    }

    public static WeatherCheck.Builder weather() {
        return new WeatherCheck.Builder();
    }

    public static class Builder implements LootItemCondition.Builder {

        @Nullable
        private Boolean isRaining;

        @Nullable
        private Boolean isThundering;

        public WeatherCheck.Builder setRaining(@Nullable Boolean boolean0) {
            this.isRaining = boolean0;
            return this;
        }

        public WeatherCheck.Builder setThundering(@Nullable Boolean boolean0) {
            this.isThundering = boolean0;
            return this;
        }

        public WeatherCheck build() {
            return new WeatherCheck(this.isRaining, this.isThundering);
        }
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<WeatherCheck> {

        public void serialize(JsonObject jsonObject0, WeatherCheck weatherCheck1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("raining", weatherCheck1.isRaining);
            jsonObject0.addProperty("thundering", weatherCheck1.isThundering);
        }

        public WeatherCheck deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            Boolean $$2 = jsonObject0.has("raining") ? GsonHelper.getAsBoolean(jsonObject0, "raining") : null;
            Boolean $$3 = jsonObject0.has("thundering") ? GsonHelper.getAsBoolean(jsonObject0, "thundering") : null;
            return new WeatherCheck($$2, $$3);
        }
    }
}