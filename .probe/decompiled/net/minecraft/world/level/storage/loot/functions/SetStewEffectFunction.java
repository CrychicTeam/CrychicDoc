package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class SetStewEffectFunction extends LootItemConditionalFunction {

    final Map<MobEffect, NumberProvider> effectDurationMap;

    SetStewEffectFunction(LootItemCondition[] lootItemCondition0, Map<MobEffect, NumberProvider> mapMobEffectNumberProvider1) {
        super(lootItemCondition0);
        this.effectDurationMap = ImmutableMap.copyOf(mapMobEffectNumberProvider1);
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_STEW_EFFECT;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>) this.effectDurationMap.values().stream().flatMap(p_279082_ -> p_279082_.m_6231_().stream()).collect(ImmutableSet.toImmutableSet());
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        if (itemStack0.is(Items.SUSPICIOUS_STEW) && !this.effectDurationMap.isEmpty()) {
            RandomSource $$2 = lootContext1.getRandom();
            int $$3 = $$2.nextInt(this.effectDurationMap.size());
            Entry<MobEffect, NumberProvider> $$4 = (Entry<MobEffect, NumberProvider>) Iterables.get(this.effectDurationMap.entrySet(), $$3);
            MobEffect $$5 = (MobEffect) $$4.getKey();
            int $$6 = ((NumberProvider) $$4.getValue()).getInt(lootContext1);
            if (!$$5.isInstantenous()) {
                $$6 *= 20;
            }
            SuspiciousStewItem.saveMobEffect(itemStack0, $$5, $$6);
            return itemStack0;
        } else {
            return itemStack0;
        }
    }

    public static SetStewEffectFunction.Builder stewEffect() {
        return new SetStewEffectFunction.Builder();
    }

    public static class Builder extends LootItemConditionalFunction.Builder<SetStewEffectFunction.Builder> {

        private final Map<MobEffect, NumberProvider> effectDurationMap = Maps.newLinkedHashMap();

        protected SetStewEffectFunction.Builder getThis() {
            return this;
        }

        public SetStewEffectFunction.Builder withEffect(MobEffect mobEffect0, NumberProvider numberProvider1) {
            this.effectDurationMap.put(mobEffect0, numberProvider1);
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new SetStewEffectFunction(this.m_80699_(), this.effectDurationMap);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetStewEffectFunction> {

        public void serialize(JsonObject jsonObject0, SetStewEffectFunction setStewEffectFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, setStewEffectFunction1, jsonSerializationContext2);
            if (!setStewEffectFunction1.effectDurationMap.isEmpty()) {
                JsonArray $$3 = new JsonArray();
                for (MobEffect $$4 : setStewEffectFunction1.effectDurationMap.keySet()) {
                    JsonObject $$5 = new JsonObject();
                    ResourceLocation $$6 = BuiltInRegistries.MOB_EFFECT.getKey($$4);
                    if ($$6 == null) {
                        throw new IllegalArgumentException("Don't know how to serialize mob effect " + $$4);
                    }
                    $$5.add("type", new JsonPrimitive($$6.toString()));
                    $$5.add("duration", jsonSerializationContext2.serialize(setStewEffectFunction1.effectDurationMap.get($$4)));
                    $$3.add($$5);
                }
                jsonObject0.add("effects", $$3);
            }
        }

        public SetStewEffectFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            Map<MobEffect, NumberProvider> $$3 = Maps.newLinkedHashMap();
            if (jsonObject0.has("effects")) {
                for (JsonElement $$5 : GsonHelper.getAsJsonArray(jsonObject0, "effects")) {
                    String $$6 = GsonHelper.getAsString($$5.getAsJsonObject(), "type");
                    MobEffect $$7 = (MobEffect) BuiltInRegistries.MOB_EFFECT.getOptional(new ResourceLocation($$6)).orElseThrow(() -> new JsonSyntaxException("Unknown mob effect '" + $$6 + "'"));
                    NumberProvider $$8 = GsonHelper.getAsObject($$5.getAsJsonObject(), "duration", jsonDeserializationContext1, NumberProvider.class);
                    $$3.put($$7, $$8);
                }
            }
            return new SetStewEffectFunction(lootItemCondition2, $$3);
        }
    }
}