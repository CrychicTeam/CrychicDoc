package net.minecraft.world.level.storage.loot.providers.number;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public final class BinomialDistributionGenerator implements NumberProvider {

    final NumberProvider n;

    final NumberProvider p;

    BinomialDistributionGenerator(NumberProvider numberProvider0, NumberProvider numberProvider1) {
        this.n = numberProvider0;
        this.p = numberProvider1;
    }

    @Override
    public LootNumberProviderType getType() {
        return NumberProviders.BINOMIAL;
    }

    @Override
    public int getInt(LootContext lootContext0) {
        int $$1 = this.n.getInt(lootContext0);
        float $$2 = this.p.getFloat(lootContext0);
        RandomSource $$3 = lootContext0.getRandom();
        int $$4 = 0;
        for (int $$5 = 0; $$5 < $$1; $$5++) {
            if ($$3.nextFloat() < $$2) {
                $$4++;
            }
        }
        return $$4;
    }

    @Override
    public float getFloat(LootContext lootContext0) {
        return (float) this.getInt(lootContext0);
    }

    public static BinomialDistributionGenerator binomial(int int0, float float1) {
        return new BinomialDistributionGenerator(ConstantValue.exactly((float) int0), ConstantValue.exactly(float1));
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Sets.union(this.n.m_6231_(), this.p.m_6231_());
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<BinomialDistributionGenerator> {

        public BinomialDistributionGenerator deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            NumberProvider $$2 = GsonHelper.getAsObject(jsonObject0, "n", jsonDeserializationContext1, NumberProvider.class);
            NumberProvider $$3 = GsonHelper.getAsObject(jsonObject0, "p", jsonDeserializationContext1, NumberProvider.class);
            return new BinomialDistributionGenerator($$2, $$3);
        }

        public void serialize(JsonObject jsonObject0, BinomialDistributionGenerator binomialDistributionGenerator1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.add("n", jsonSerializationContext2.serialize(binomialDistributionGenerator1.n));
            jsonObject0.add("p", jsonSerializationContext2.serialize(binomialDistributionGenerator1.p));
        }
    }
}