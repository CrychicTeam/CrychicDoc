package net.minecraft.world.level.storage.loot.entries;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.apache.commons.lang3.ArrayUtils;

public abstract class LootPoolSingletonContainer extends LootPoolEntryContainer {

    public static final int DEFAULT_WEIGHT = 1;

    public static final int DEFAULT_QUALITY = 0;

    protected final int weight;

    protected final int quality;

    protected final LootItemFunction[] functions;

    final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;

    private final LootPoolEntry entry = new LootPoolSingletonContainer.EntryBase() {

        @Override
        public void createItemStack(Consumer<ItemStack> p_79700_, LootContext p_79701_) {
            LootPoolSingletonContainer.this.createItemStack(LootItemFunction.decorate(LootPoolSingletonContainer.this.compositeFunction, p_79700_, p_79701_), p_79701_);
        }
    };

    protected LootPoolSingletonContainer(int int0, int int1, LootItemCondition[] lootItemCondition2, LootItemFunction[] lootItemFunction3) {
        super(lootItemCondition2);
        this.weight = int0;
        this.quality = int1;
        this.functions = lootItemFunction3;
        this.compositeFunction = LootItemFunctions.compose(lootItemFunction3);
    }

    @Override
    public void validate(ValidationContext validationContext0) {
        super.validate(validationContext0);
        for (int $$1 = 0; $$1 < this.functions.length; $$1++) {
            this.functions[$$1].m_6169_(validationContext0.forChild(".functions[" + $$1 + "]"));
        }
    }

    protected abstract void createItemStack(Consumer<ItemStack> var1, LootContext var2);

    @Override
    public boolean expand(LootContext lootContext0, Consumer<LootPoolEntry> consumerLootPoolEntry1) {
        if (this.m_79639_(lootContext0)) {
            consumerLootPoolEntry1.accept(this.entry);
            return true;
        } else {
            return false;
        }
    }

    public static LootPoolSingletonContainer.Builder<?> simpleBuilder(LootPoolSingletonContainer.EntryConstructor lootPoolSingletonContainerEntryConstructor0) {
        return new LootPoolSingletonContainer.DummyBuilder(lootPoolSingletonContainerEntryConstructor0);
    }

    public abstract static class Builder<T extends LootPoolSingletonContainer.Builder<T>> extends LootPoolEntryContainer.Builder<T> implements FunctionUserBuilder<T> {

        protected int weight = 1;

        protected int quality = 0;

        private final List<LootItemFunction> functions = Lists.newArrayList();

        public T apply(LootItemFunction.Builder lootItemFunctionBuilder0) {
            this.functions.add(lootItemFunctionBuilder0.build());
            return (T) this.m_6897_();
        }

        protected LootItemFunction[] getFunctions() {
            return (LootItemFunction[]) this.functions.toArray(new LootItemFunction[0]);
        }

        public T setWeight(int int0) {
            this.weight = int0;
            return (T) this.m_6897_();
        }

        public T setQuality(int int0) {
            this.quality = int0;
            return (T) this.m_6897_();
        }
    }

    static class DummyBuilder extends LootPoolSingletonContainer.Builder<LootPoolSingletonContainer.DummyBuilder> {

        private final LootPoolSingletonContainer.EntryConstructor constructor;

        public DummyBuilder(LootPoolSingletonContainer.EntryConstructor lootPoolSingletonContainerEntryConstructor0) {
            this.constructor = lootPoolSingletonContainerEntryConstructor0;
        }

        protected LootPoolSingletonContainer.DummyBuilder getThis() {
            return this;
        }

        @Override
        public LootPoolEntryContainer build() {
            return this.constructor.build(this.f_79702_, this.f_79703_, this.m_79651_(), this.m_79706_());
        }
    }

    protected abstract class EntryBase implements LootPoolEntry {

        @Override
        public int getWeight(float float0) {
            return Math.max(Mth.floor((float) LootPoolSingletonContainer.this.weight + (float) LootPoolSingletonContainer.this.quality * float0), 0);
        }
    }

    @FunctionalInterface
    protected interface EntryConstructor {

        LootPoolSingletonContainer build(int var1, int var2, LootItemCondition[] var3, LootItemFunction[] var4);
    }

    public abstract static class Serializer<T extends LootPoolSingletonContainer> extends LootPoolEntryContainer.Serializer<T> {

        public void serializeCustom(JsonObject jsonObject0, T t1, JsonSerializationContext jsonSerializationContext2) {
            if (t1.weight != 1) {
                jsonObject0.addProperty("weight", t1.weight);
            }
            if (t1.quality != 0) {
                jsonObject0.addProperty("quality", t1.quality);
            }
            if (!ArrayUtils.isEmpty(t1.functions)) {
                jsonObject0.add("functions", jsonSerializationContext2.serialize(t1.functions));
            }
        }

        public final T deserializeCustom(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            int $$3 = GsonHelper.getAsInt(jsonObject0, "weight", 1);
            int $$4 = GsonHelper.getAsInt(jsonObject0, "quality", 0);
            LootItemFunction[] $$5 = GsonHelper.getAsObject(jsonObject0, "functions", new LootItemFunction[0], jsonDeserializationContext1, LootItemFunction[].class);
            return this.deserialize(jsonObject0, jsonDeserializationContext1, $$3, $$4, lootItemCondition2, $$5);
        }

        protected abstract T deserialize(JsonObject var1, JsonDeserializationContext var2, int var3, int var4, LootItemCondition[] var5, LootItemFunction[] var6);
    }
}