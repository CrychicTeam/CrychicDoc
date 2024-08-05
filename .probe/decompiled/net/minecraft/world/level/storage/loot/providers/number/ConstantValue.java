package net.minecraft.world.level.storage.loot.providers.number;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
import net.minecraft.world.level.storage.loot.LootContext;

public final class ConstantValue implements NumberProvider {

    final float value;

    ConstantValue(float float0) {
        this.value = float0;
    }

    @Override
    public LootNumberProviderType getType() {
        return NumberProviders.CONSTANT;
    }

    @Override
    public float getFloat(LootContext lootContext0) {
        return this.value;
    }

    public static ConstantValue exactly(float float0) {
        return new ConstantValue(float0);
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return object0 != null && this.getClass() == object0.getClass() ? Float.compare(((ConstantValue) object0).value, this.value) == 0 : false;
        }
    }

    public int hashCode() {
        return this.value != 0.0F ? Float.floatToIntBits(this.value) : 0;
    }

    public static class InlineSerializer implements GsonAdapterFactory.InlineSerializer<ConstantValue> {

        public JsonElement serialize(ConstantValue constantValue0, JsonSerializationContext jsonSerializationContext1) {
            return new JsonPrimitive(constantValue0.value);
        }

        public ConstantValue deserialize(JsonElement jsonElement0, JsonDeserializationContext jsonDeserializationContext1) {
            return new ConstantValue(GsonHelper.convertToFloat(jsonElement0, "value"));
        }
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ConstantValue> {

        public void serialize(JsonObject jsonObject0, ConstantValue constantValue1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("value", constantValue1.value);
        }

        public ConstantValue deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            float $$2 = GsonHelper.getAsFloat(jsonObject0, "value");
            return new ConstantValue($$2);
        }
    }
}