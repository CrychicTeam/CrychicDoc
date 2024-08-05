package net.minecraft.world.level.storage.loot;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

public class LootTable {

    static final Logger LOGGER = LogUtils.getLogger();

    public static final LootTable EMPTY = new LootTable(LootContextParamSets.EMPTY, null, new LootPool[0], new LootItemFunction[0]);

    public static final LootContextParamSet DEFAULT_PARAM_SET = LootContextParamSets.ALL_PARAMS;

    final LootContextParamSet paramSet;

    @Nullable
    final ResourceLocation randomSequence;

    final LootPool[] pools;

    final LootItemFunction[] functions;

    private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;

    LootTable(LootContextParamSet lootContextParamSet0, @Nullable ResourceLocation resourceLocation1, LootPool[] lootPool2, LootItemFunction[] lootItemFunction3) {
        this.paramSet = lootContextParamSet0;
        this.randomSequence = resourceLocation1;
        this.pools = lootPool2;
        this.functions = lootItemFunction3;
        this.compositeFunction = LootItemFunctions.compose(lootItemFunction3);
    }

    public static Consumer<ItemStack> createStackSplitter(ServerLevel serverLevel0, Consumer<ItemStack> consumerItemStack1) {
        return p_287570_ -> {
            if (p_287570_.isItemEnabled(serverLevel0.enabledFeatures())) {
                if (p_287570_.getCount() < p_287570_.getMaxStackSize()) {
                    consumerItemStack1.accept(p_287570_);
                } else {
                    int $$3 = p_287570_.getCount();
                    while ($$3 > 0) {
                        ItemStack $$4 = p_287570_.copyWithCount(Math.min(p_287570_.getMaxStackSize(), $$3));
                        $$3 -= $$4.getCount();
                        consumerItemStack1.accept($$4);
                    }
                }
            }
        };
    }

    public void getRandomItemsRaw(LootParams lootParams0, Consumer<ItemStack> consumerItemStack1) {
        this.getRandomItemsRaw(new LootContext.Builder(lootParams0).create(this.randomSequence), consumerItemStack1);
    }

    public void getRandomItemsRaw(LootContext lootContext0, Consumer<ItemStack> consumerItemStack1) {
        LootContext.VisitedEntry<?> $$2 = LootContext.createVisitedEntry(this);
        if (lootContext0.pushVisitedElement($$2)) {
            Consumer<ItemStack> $$3 = LootItemFunction.decorate(this.compositeFunction, consumerItemStack1, lootContext0);
            for (LootPool $$4 : this.pools) {
                $$4.addRandomItems($$3, lootContext0);
            }
            lootContext0.popVisitedElement($$2);
        } else {
            LOGGER.warn("Detected infinite loop in loot tables");
        }
    }

    public void getRandomItems(LootParams lootParams0, long long1, Consumer<ItemStack> consumerItemStack2) {
        this.getRandomItemsRaw(new LootContext.Builder(lootParams0).withOptionalRandomSeed(long1).create(this.randomSequence), createStackSplitter(lootParams0.getLevel(), consumerItemStack2));
    }

    public void getRandomItems(LootParams lootParams0, Consumer<ItemStack> consumerItemStack1) {
        this.getRandomItemsRaw(lootParams0, createStackSplitter(lootParams0.getLevel(), consumerItemStack1));
    }

    public void getRandomItems(LootContext lootContext0, Consumer<ItemStack> consumerItemStack1) {
        this.getRandomItemsRaw(lootContext0, createStackSplitter(lootContext0.getLevel(), consumerItemStack1));
    }

    public ObjectArrayList<ItemStack> getRandomItems(LootParams lootParams0, long long1) {
        return this.getRandomItems(new LootContext.Builder(lootParams0).withOptionalRandomSeed(long1).create(this.randomSequence));
    }

    public ObjectArrayList<ItemStack> getRandomItems(LootParams lootParams0) {
        return this.getRandomItems(new LootContext.Builder(lootParams0).create(this.randomSequence));
    }

    private ObjectArrayList<ItemStack> getRandomItems(LootContext lootContext0) {
        ObjectArrayList<ItemStack> $$1 = new ObjectArrayList();
        this.getRandomItems(lootContext0, $$1::add);
        return $$1;
    }

    public LootContextParamSet getParamSet() {
        return this.paramSet;
    }

    public void validate(ValidationContext validationContext0) {
        for (int $$1 = 0; $$1 < this.pools.length; $$1++) {
            this.pools[$$1].validate(validationContext0.forChild(".pools[" + $$1 + "]"));
        }
        for (int $$2 = 0; $$2 < this.functions.length; $$2++) {
            this.functions[$$2].m_6169_(validationContext0.forChild(".functions[" + $$2 + "]"));
        }
    }

    public void fill(Container container0, LootParams lootParams1, long long2) {
        LootContext $$3 = new LootContext.Builder(lootParams1).withOptionalRandomSeed(long2).create(this.randomSequence);
        ObjectArrayList<ItemStack> $$4 = this.getRandomItems($$3);
        RandomSource $$5 = $$3.getRandom();
        List<Integer> $$6 = this.getAvailableSlots(container0, $$5);
        this.shuffleAndSplitItems($$4, $$6.size(), $$5);
        ObjectListIterator var9 = $$4.iterator();
        while (var9.hasNext()) {
            ItemStack $$7 = (ItemStack) var9.next();
            if ($$6.isEmpty()) {
                LOGGER.warn("Tried to over-fill a container");
                return;
            }
            if ($$7.isEmpty()) {
                container0.setItem((Integer) $$6.remove($$6.size() - 1), ItemStack.EMPTY);
            } else {
                container0.setItem((Integer) $$6.remove($$6.size() - 1), $$7);
            }
        }
    }

    private void shuffleAndSplitItems(ObjectArrayList<ItemStack> objectArrayListItemStack0, int int1, RandomSource randomSource2) {
        List<ItemStack> $$3 = Lists.newArrayList();
        Iterator<ItemStack> $$4 = objectArrayListItemStack0.iterator();
        while ($$4.hasNext()) {
            ItemStack $$5 = (ItemStack) $$4.next();
            if ($$5.isEmpty()) {
                $$4.remove();
            } else if ($$5.getCount() > 1) {
                $$3.add($$5);
                $$4.remove();
            }
        }
        while (int1 - objectArrayListItemStack0.size() - $$3.size() > 0 && !$$3.isEmpty()) {
            ItemStack $$6 = (ItemStack) $$3.remove(Mth.nextInt(randomSource2, 0, $$3.size() - 1));
            int $$7 = Mth.nextInt(randomSource2, 1, $$6.getCount() / 2);
            ItemStack $$8 = $$6.split($$7);
            if ($$6.getCount() > 1 && randomSource2.nextBoolean()) {
                $$3.add($$6);
            } else {
                objectArrayListItemStack0.add($$6);
            }
            if ($$8.getCount() > 1 && randomSource2.nextBoolean()) {
                $$3.add($$8);
            } else {
                objectArrayListItemStack0.add($$8);
            }
        }
        objectArrayListItemStack0.addAll($$3);
        Util.shuffle(objectArrayListItemStack0, randomSource2);
    }

    private List<Integer> getAvailableSlots(Container container0, RandomSource randomSource1) {
        ObjectArrayList<Integer> $$2 = new ObjectArrayList();
        for (int $$3 = 0; $$3 < container0.getContainerSize(); $$3++) {
            if (container0.getItem($$3).isEmpty()) {
                $$2.add($$3);
            }
        }
        Util.shuffle($$2, randomSource1);
        return $$2;
    }

    public static LootTable.Builder lootTable() {
        return new LootTable.Builder();
    }

    public static class Builder implements FunctionUserBuilder<LootTable.Builder> {

        private final List<LootPool> pools = Lists.newArrayList();

        private final List<LootItemFunction> functions = Lists.newArrayList();

        private LootContextParamSet paramSet = LootTable.DEFAULT_PARAM_SET;

        @Nullable
        private ResourceLocation randomSequence = null;

        public LootTable.Builder withPool(LootPool.Builder lootPoolBuilder0) {
            this.pools.add(lootPoolBuilder0.build());
            return this;
        }

        public LootTable.Builder setParamSet(LootContextParamSet lootContextParamSet0) {
            this.paramSet = lootContextParamSet0;
            return this;
        }

        public LootTable.Builder setRandomSequence(ResourceLocation resourceLocation0) {
            this.randomSequence = resourceLocation0;
            return this;
        }

        public LootTable.Builder apply(LootItemFunction.Builder lootItemFunctionBuilder0) {
            this.functions.add(lootItemFunctionBuilder0.build());
            return this;
        }

        public LootTable.Builder unwrap() {
            return this;
        }

        public LootTable build() {
            return new LootTable(this.paramSet, this.randomSequence, (LootPool[]) this.pools.toArray(new LootPool[0]), (LootItemFunction[]) this.functions.toArray(new LootItemFunction[0]));
        }
    }

    public static class Serializer implements JsonDeserializer<LootTable>, JsonSerializer<LootTable> {

        public LootTable deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            JsonObject $$3 = GsonHelper.convertToJsonObject(jsonElement0, "loot table");
            LootPool[] $$4 = GsonHelper.getAsObject($$3, "pools", new LootPool[0], jsonDeserializationContext2, LootPool[].class);
            LootContextParamSet $$5 = null;
            if ($$3.has("type")) {
                String $$6 = GsonHelper.getAsString($$3, "type");
                $$5 = LootContextParamSets.get(new ResourceLocation($$6));
            }
            ResourceLocation $$8;
            if ($$3.has("random_sequence")) {
                String $$7 = GsonHelper.getAsString($$3, "random_sequence");
                $$8 = new ResourceLocation($$7);
            } else {
                $$8 = null;
            }
            LootItemFunction[] $$10 = GsonHelper.getAsObject($$3, "functions", new LootItemFunction[0], jsonDeserializationContext2, LootItemFunction[].class);
            return new LootTable($$5 != null ? $$5 : LootContextParamSets.ALL_PARAMS, $$8, $$4, $$10);
        }

        public JsonElement serialize(LootTable lootTable0, Type type1, JsonSerializationContext jsonSerializationContext2) {
            JsonObject $$3 = new JsonObject();
            if (lootTable0.paramSet != LootTable.DEFAULT_PARAM_SET) {
                ResourceLocation $$4 = LootContextParamSets.getKey(lootTable0.paramSet);
                if ($$4 != null) {
                    $$3.addProperty("type", $$4.toString());
                } else {
                    LootTable.LOGGER.warn("Failed to find id for param set {}", lootTable0.paramSet);
                }
            }
            if (lootTable0.randomSequence != null) {
                $$3.addProperty("random_sequence", lootTable0.randomSequence.toString());
            }
            if (lootTable0.pools.length > 0) {
                $$3.add("pools", jsonSerializationContext2.serialize(lootTable0.pools));
            }
            if (!ArrayUtils.isEmpty(lootTable0.functions)) {
                $$3.add("functions", jsonSerializationContext2.serialize(lootTable0.functions));
            }
            return $$3;
        }
    }
}