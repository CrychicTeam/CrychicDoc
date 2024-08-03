package com.sihenzhang.crockpot.base;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.tuple.Pair;

public class FoodValues {

    private static final FoodCategory[] CATEGORIES = FoodCategory.values();

    private final float[] values = new float[CATEGORIES.length];

    private int size;

    private FoodValues() {
    }

    public static FoodValues create() {
        return new FoodValues();
    }

    @SafeVarargs
    public static FoodValues of(Pair<FoodCategory, Float>... pairs) {
        FoodValues foodValues = create();
        for (Pair<FoodCategory, Float> pair : pairs) {
            foodValues.put((FoodCategory) pair.getKey(), (Float) pair.getValue());
        }
        return foodValues;
    }

    public static FoodValues of(Object... categoriesAndValues) {
        FoodValues foodValues = create();
        FoodCategory category = null;
        for (int i = 0; i < categoriesAndValues.length; i++) {
            if (i % 2 == 0) {
                category = (FoodCategory) categoriesAndValues[i];
            } else {
                foodValues.put(category, (Float) categoriesAndValues[i]);
            }
        }
        return foodValues;
    }

    public static FoodValues of(Map<FoodCategory, Float> map) {
        FoodValues foodValues = create();
        if (map != null) {
            map.forEach(foodValues::put);
        }
        return foodValues;
    }

    public static FoodValues merge(Collection<FoodValues> foodValues) {
        FoodValues mergedFoodValues = create();
        foodValues.forEach(foodValue -> foodValue.entrySet().forEach(entry -> mergedFoodValues.put((FoodCategory) entry.getKey(), (Float) entry.getValue() + mergedFoodValues.get((FoodCategory) entry.getKey()))));
        return mergedFoodValues;
    }

    public FoodValues set(FoodCategory category, float value) {
        this.put(category, value);
        return this;
    }

    public float get(FoodCategory category) {
        return category == null ? 0.0F : Math.max(this.values[category.ordinal()], 0.0F);
    }

    public boolean has(FoodCategory category) {
        return category == null ? false : this.values[category.ordinal()] > 0.0F;
    }

    public void put(FoodCategory category, float value) {
        if (category != null) {
            if (!Float.isNaN(value) && !(value <= 0.0F)) {
                boolean hasOldValue = this.has(category);
                this.values[category.ordinal()] = value;
                if (!hasOldValue) {
                    this.size++;
                }
            } else {
                this.remove(category);
            }
        }
    }

    public void remove(FoodCategory category) {
        if (category != null && this.has(category)) {
            this.values[category.ordinal()] = 0.0F;
            this.size--;
        }
    }

    public void clear() {
        Arrays.fill(this.values, 0.0F);
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size <= 0;
    }

    public int size() {
        return this.size;
    }

    public Set<Pair<FoodCategory, Float>> entrySet() {
        Builder<Pair<FoodCategory, Float>> builder = ImmutableSet.builder();
        for (int i = 0; i < this.values.length; i++) {
            if (this.values[i] > 0.0F) {
                builder.add(Pair.of(CATEGORIES[i], this.values[i]));
            }
        }
        return builder.build();
    }

    public static FoodValues fromJson(JsonElement json) {
        if (json == null || json.isJsonNull()) {
            throw new JsonSyntaxException("Json cannot be null");
        } else if (!json.isJsonObject()) {
            throw new JsonSyntaxException("Expected food value to be an object, was " + GsonHelper.getType(json));
        } else {
            FoodValues foodValues = create();
            JsonObject obj = json.getAsJsonObject();
            obj.entrySet().forEach(entry -> {
                String category = ((String) entry.getKey()).toUpperCase();
                if (!EnumUtils.isValidEnum(FoodCategory.class, category)) {
                    throw new JsonSyntaxException("Expected the key of food value to be an enum of food category, was unknown name: '" + category + "'");
                } else if (!GsonHelper.isNumberValue((JsonElement) entry.getValue())) {
                    throw new JsonSyntaxException("Expected the value of food value to be a number, was " + GsonHelper.getType((JsonElement) entry.getValue()));
                } else {
                    foodValues.put(FoodCategory.valueOf(category), ((JsonElement) entry.getValue()).getAsFloat());
                }
            });
            return foodValues;
        }
    }

    public JsonElement toJson() {
        JsonObject obj = new JsonObject();
        this.entrySet().forEach(entry -> obj.addProperty(((FoodCategory) entry.getKey()).name(), (Number) entry.getValue()));
        return obj;
    }

    public static FoodValues fromNetwork(FriendlyByteBuf buffer) {
        FoodValues foodValues = create();
        int length = buffer.readByte();
        for (int i = 0; i < length; i++) {
            FoodCategory category = buffer.readEnum(FoodCategory.class);
            float value = buffer.readFloat();
            foodValues.put(category, value);
        }
        return foodValues;
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        Set<Pair<FoodCategory, Float>> entrySet = this.entrySet();
        buffer.writeByte(entrySet.size());
        entrySet.forEach(entry -> {
            buffer.writeEnum((Enum<?>) entry.getKey());
            buffer.writeFloat((Float) entry.getValue());
        });
    }
}