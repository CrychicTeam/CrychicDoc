package net.minecraft.world.level.storage.loot.predicates;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class TimeCheck implements LootItemCondition {

    @Nullable
    final Long period;

    final IntRange value;

    TimeCheck(@Nullable Long long0, IntRange intRange1) {
        this.period = long0;
        this.value = intRange1;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.TIME_CHECK;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.value.getReferencedContextParams();
    }

    public boolean test(LootContext lootContext0) {
        ServerLevel $$1 = lootContext0.getLevel();
        long $$2 = $$1.m_46468_();
        if (this.period != null) {
            $$2 %= this.period;
        }
        return this.value.test(lootContext0, (int) $$2);
    }

    public static TimeCheck.Builder time(IntRange intRange0) {
        return new TimeCheck.Builder(intRange0);
    }

    public static class Builder implements LootItemCondition.Builder {

        @Nullable
        private Long period;

        private final IntRange value;

        public Builder(IntRange intRange0) {
            this.value = intRange0;
        }

        public TimeCheck.Builder setPeriod(long long0) {
            this.period = long0;
            return this;
        }

        public TimeCheck build() {
            return new TimeCheck(this.period, this.value);
        }
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<TimeCheck> {

        public void serialize(JsonObject jsonObject0, TimeCheck timeCheck1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("period", timeCheck1.period);
            jsonObject0.add("value", jsonSerializationContext2.serialize(timeCheck1.value));
        }

        public TimeCheck deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            Long $$2 = jsonObject0.has("period") ? GsonHelper.getAsLong(jsonObject0, "period") : null;
            IntRange $$3 = GsonHelper.getAsObject(jsonObject0, "value", jsonDeserializationContext1, IntRange.class);
            return new TimeCheck($$2, $$3);
        }
    }
}