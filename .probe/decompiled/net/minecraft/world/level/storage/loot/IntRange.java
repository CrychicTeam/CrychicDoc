package net.minecraft.world.level.storage.loot;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class IntRange {

    @Nullable
    final NumberProvider min;

    @Nullable
    final NumberProvider max;

    private final IntRange.IntLimiter limiter;

    private final IntRange.IntChecker predicate;

    public Set<LootContextParam<?>> getReferencedContextParams() {
        Builder<LootContextParam<?>> $$0 = ImmutableSet.builder();
        if (this.min != null) {
            $$0.addAll(this.min.m_6231_());
        }
        if (this.max != null) {
            $$0.addAll(this.max.m_6231_());
        }
        return $$0.build();
    }

    IntRange(@Nullable NumberProvider numberProvider0, @Nullable NumberProvider numberProvider1) {
        this.min = numberProvider0;
        this.max = numberProvider1;
        if (numberProvider0 == null) {
            if (numberProvider1 == null) {
                this.limiter = (p_165050_, p_165051_) -> p_165051_;
                this.predicate = (p_165043_, p_165044_) -> true;
            } else {
                this.limiter = (p_165054_, p_165055_) -> Math.min(numberProvider1.getInt(p_165054_), p_165055_);
                this.predicate = (p_165047_, p_165048_) -> p_165048_ <= numberProvider1.getInt(p_165047_);
            }
        } else if (numberProvider1 == null) {
            this.limiter = (p_165033_, p_165034_) -> Math.max(numberProvider0.getInt(p_165033_), p_165034_);
            this.predicate = (p_165019_, p_165020_) -> p_165020_ >= numberProvider0.getInt(p_165019_);
        } else {
            this.limiter = (p_165038_, p_165039_) -> Mth.clamp(p_165039_, numberProvider0.getInt(p_165038_), numberProvider1.getInt(p_165038_));
            this.predicate = (p_165024_, p_165025_) -> p_165025_ >= numberProvider0.getInt(p_165024_) && p_165025_ <= numberProvider1.getInt(p_165024_);
        }
    }

    public static IntRange exact(int int0) {
        ConstantValue $$1 = ConstantValue.exactly((float) int0);
        return new IntRange($$1, $$1);
    }

    public static IntRange range(int int0, int int1) {
        return new IntRange(ConstantValue.exactly((float) int0), ConstantValue.exactly((float) int1));
    }

    public static IntRange lowerBound(int int0) {
        return new IntRange(ConstantValue.exactly((float) int0), null);
    }

    public static IntRange upperBound(int int0) {
        return new IntRange(null, ConstantValue.exactly((float) int0));
    }

    public int clamp(LootContext lootContext0, int int1) {
        return this.limiter.apply(lootContext0, int1);
    }

    public boolean test(LootContext lootContext0, int int1) {
        return this.predicate.test(lootContext0, int1);
    }

    @FunctionalInterface
    interface IntChecker {

        boolean test(LootContext var1, int var2);
    }

    @FunctionalInterface
    interface IntLimiter {

        int apply(LootContext var1, int var2);
    }

    public static class Serializer implements JsonDeserializer<IntRange>, JsonSerializer<IntRange> {

        public IntRange deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) {
            if (jsonElement0.isJsonPrimitive()) {
                return IntRange.exact(jsonElement0.getAsInt());
            } else {
                JsonObject $$3 = GsonHelper.convertToJsonObject(jsonElement0, "value");
                NumberProvider $$4 = $$3.has("min") ? GsonHelper.getAsObject($$3, "min", jsonDeserializationContext2, NumberProvider.class) : null;
                NumberProvider $$5 = $$3.has("max") ? GsonHelper.getAsObject($$3, "max", jsonDeserializationContext2, NumberProvider.class) : null;
                return new IntRange($$4, $$5);
            }
        }

        public JsonElement serialize(IntRange intRange0, Type type1, JsonSerializationContext jsonSerializationContext2) {
            JsonObject $$3 = new JsonObject();
            if (Objects.equals(intRange0.max, intRange0.min)) {
                return jsonSerializationContext2.serialize(intRange0.min);
            } else {
                if (intRange0.max != null) {
                    $$3.add("max", jsonSerializationContext2.serialize(intRange0.max));
                }
                if (intRange0.min != null) {
                    $$3.add("min", jsonSerializationContext2.serialize(intRange0.min));
                }
                return $$3;
            }
        }
    }
}