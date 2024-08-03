package net.minecraft.world.level.storage.loot.providers.number;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class UniformGenerator implements NumberProvider {

    final NumberProvider min;

    final NumberProvider max;

    UniformGenerator(NumberProvider numberProvider0, NumberProvider numberProvider1) {
        this.min = numberProvider0;
        this.max = numberProvider1;
    }

    @Override
    public LootNumberProviderType getType() {
        return NumberProviders.UNIFORM;
    }

    public static UniformGenerator between(float float0, float float1) {
        return new UniformGenerator(ConstantValue.exactly(float0), ConstantValue.exactly(float1));
    }

    @Override
    public int getInt(LootContext lootContext0) {
        return Mth.nextInt(lootContext0.getRandom(), this.min.getInt(lootContext0), this.max.getInt(lootContext0));
    }

    @Override
    public float getFloat(LootContext lootContext0) {
        return Mth.nextFloat(lootContext0.getRandom(), this.min.getFloat(lootContext0), this.max.getFloat(lootContext0));
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Sets.union(this.min.m_6231_(), this.max.m_6231_());
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<UniformGenerator> {

        public UniformGenerator deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            NumberProvider $$2 = GsonHelper.getAsObject(jsonObject0, "min", jsonDeserializationContext1, NumberProvider.class);
            NumberProvider $$3 = GsonHelper.getAsObject(jsonObject0, "max", jsonDeserializationContext1, NumberProvider.class);
            return new UniformGenerator($$2, $$3);
        }

        public void serialize(JsonObject jsonObject0, UniformGenerator uniformGenerator1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.add("min", jsonSerializationContext2.serialize(uniformGenerator1.min));
            jsonObject0.add("max", jsonSerializationContext2.serialize(uniformGenerator1.max));
        }
    }
}