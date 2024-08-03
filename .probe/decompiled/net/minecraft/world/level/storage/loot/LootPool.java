package net.minecraft.world.level.storage.loot;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.mutable.MutableInt;

public class LootPool {

    final LootPoolEntryContainer[] entries;

    final LootItemCondition[] conditions;

    private final Predicate<LootContext> compositeCondition;

    final LootItemFunction[] functions;

    private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;

    final NumberProvider rolls;

    final NumberProvider bonusRolls;

    LootPool(LootPoolEntryContainer[] lootPoolEntryContainer0, LootItemCondition[] lootItemCondition1, LootItemFunction[] lootItemFunction2, NumberProvider numberProvider3, NumberProvider numberProvider4) {
        this.entries = lootPoolEntryContainer0;
        this.conditions = lootItemCondition1;
        this.compositeCondition = LootItemConditions.andConditions(lootItemCondition1);
        this.functions = lootItemFunction2;
        this.compositeFunction = LootItemFunctions.compose(lootItemFunction2);
        this.rolls = numberProvider3;
        this.bonusRolls = numberProvider4;
    }

    private void addRandomItem(Consumer<ItemStack> consumerItemStack0, LootContext lootContext1) {
        RandomSource $$2 = lootContext1.getRandom();
        List<LootPoolEntry> $$3 = Lists.newArrayList();
        MutableInt $$4 = new MutableInt();
        for (LootPoolEntryContainer $$5 : this.entries) {
            $$5.m_6562_(lootContext1, p_79048_ -> {
                int $$4x = p_79048_.getWeight(lootContext1.getLuck());
                if ($$4x > 0) {
                    $$3.add(p_79048_);
                    $$4.add($$4x);
                }
            });
        }
        int $$6 = $$3.size();
        if ($$4.intValue() != 0 && $$6 != 0) {
            if ($$6 == 1) {
                ((LootPoolEntry) $$3.get(0)).createItemStack(consumerItemStack0, lootContext1);
            } else {
                int $$7 = $$2.nextInt($$4.intValue());
                for (LootPoolEntry $$8 : $$3) {
                    $$7 -= $$8.getWeight(lootContext1.getLuck());
                    if ($$7 < 0) {
                        $$8.createItemStack(consumerItemStack0, lootContext1);
                        return;
                    }
                }
            }
        }
    }

    public void addRandomItems(Consumer<ItemStack> consumerItemStack0, LootContext lootContext1) {
        if (this.compositeCondition.test(lootContext1)) {
            Consumer<ItemStack> $$2 = LootItemFunction.decorate(this.compositeFunction, consumerItemStack0, lootContext1);
            int $$3 = this.rolls.getInt(lootContext1) + Mth.floor(this.bonusRolls.getFloat(lootContext1) * lootContext1.getLuck());
            for (int $$4 = 0; $$4 < $$3; $$4++) {
                this.addRandomItem($$2, lootContext1);
            }
        }
    }

    public void validate(ValidationContext validationContext0) {
        for (int $$1 = 0; $$1 < this.conditions.length; $$1++) {
            this.conditions[$$1].m_6169_(validationContext0.forChild(".condition[" + $$1 + "]"));
        }
        for (int $$2 = 0; $$2 < this.functions.length; $$2++) {
            this.functions[$$2].m_6169_(validationContext0.forChild(".functions[" + $$2 + "]"));
        }
        for (int $$3 = 0; $$3 < this.entries.length; $$3++) {
            this.entries[$$3].validate(validationContext0.forChild(".entries[" + $$3 + "]"));
        }
        this.rolls.m_6169_(validationContext0.forChild(".rolls"));
        this.bonusRolls.m_6169_(validationContext0.forChild(".bonusRolls"));
    }

    public static LootPool.Builder lootPool() {
        return new LootPool.Builder();
    }

    public static class Builder implements FunctionUserBuilder<LootPool.Builder>, ConditionUserBuilder<LootPool.Builder> {

        private final List<LootPoolEntryContainer> entries = Lists.newArrayList();

        private final List<LootItemCondition> conditions = Lists.newArrayList();

        private final List<LootItemFunction> functions = Lists.newArrayList();

        private NumberProvider rolls = ConstantValue.exactly(1.0F);

        private NumberProvider bonusRolls = ConstantValue.exactly(0.0F);

        public LootPool.Builder setRolls(NumberProvider numberProvider0) {
            this.rolls = numberProvider0;
            return this;
        }

        public LootPool.Builder unwrap() {
            return this;
        }

        public LootPool.Builder setBonusRolls(NumberProvider numberProvider0) {
            this.bonusRolls = numberProvider0;
            return this;
        }

        public LootPool.Builder add(LootPoolEntryContainer.Builder<?> lootPoolEntryContainerBuilder0) {
            this.entries.add(lootPoolEntryContainerBuilder0.build());
            return this;
        }

        public LootPool.Builder when(LootItemCondition.Builder lootItemConditionBuilder0) {
            this.conditions.add(lootItemConditionBuilder0.build());
            return this;
        }

        public LootPool.Builder apply(LootItemFunction.Builder lootItemFunctionBuilder0) {
            this.functions.add(lootItemFunctionBuilder0.build());
            return this;
        }

        public LootPool build() {
            if (this.rolls == null) {
                throw new IllegalArgumentException("Rolls not set");
            } else {
                return new LootPool((LootPoolEntryContainer[]) this.entries.toArray(new LootPoolEntryContainer[0]), (LootItemCondition[]) this.conditions.toArray(new LootItemCondition[0]), (LootItemFunction[]) this.functions.toArray(new LootItemFunction[0]), this.rolls, this.bonusRolls);
            }
        }
    }

    public static class Serializer implements JsonDeserializer<LootPool>, JsonSerializer<LootPool> {

        public LootPool deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            JsonObject $$3 = GsonHelper.convertToJsonObject(jsonElement0, "loot pool");
            LootPoolEntryContainer[] $$4 = GsonHelper.getAsObject($$3, "entries", jsonDeserializationContext2, LootPoolEntryContainer[].class);
            LootItemCondition[] $$5 = GsonHelper.getAsObject($$3, "conditions", new LootItemCondition[0], jsonDeserializationContext2, LootItemCondition[].class);
            LootItemFunction[] $$6 = GsonHelper.getAsObject($$3, "functions", new LootItemFunction[0], jsonDeserializationContext2, LootItemFunction[].class);
            NumberProvider $$7 = GsonHelper.getAsObject($$3, "rolls", jsonDeserializationContext2, NumberProvider.class);
            NumberProvider $$8 = GsonHelper.getAsObject($$3, "bonus_rolls", ConstantValue.exactly(0.0F), jsonDeserializationContext2, NumberProvider.class);
            return new LootPool($$4, $$5, $$6, $$7, $$8);
        }

        public JsonElement serialize(LootPool lootPool0, Type type1, JsonSerializationContext jsonSerializationContext2) {
            JsonObject $$3 = new JsonObject();
            $$3.add("rolls", jsonSerializationContext2.serialize(lootPool0.rolls));
            $$3.add("bonus_rolls", jsonSerializationContext2.serialize(lootPool0.bonusRolls));
            $$3.add("entries", jsonSerializationContext2.serialize(lootPool0.entries));
            if (!ArrayUtils.isEmpty(lootPool0.conditions)) {
                $$3.add("conditions", jsonSerializationContext2.serialize(lootPool0.conditions));
            }
            if (!ArrayUtils.isEmpty(lootPool0.functions)) {
                $$3.add("functions", jsonSerializationContext2.serialize(lootPool0.functions));
            }
            return $$3;
        }
    }
}