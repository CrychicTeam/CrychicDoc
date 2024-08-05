package dev.latvian.mods.kubejs.recipe.component;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.schema.DynamicRecipeComponent;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.rhino.ScriptRuntime;
import dev.latvian.mods.rhino.Wrapper;
import net.minecraft.util.Mth;

public interface NumberComponent<T extends Number> extends RecipeComponent<T> {

    NumberComponent.IntRange INT = intRange(0, Integer.MAX_VALUE);

    NumberComponent.LongRange LONG = longRange(0L, Long.MAX_VALUE);

    NumberComponent.FloatRange FLOAT = floatRange(0.0F, Float.POSITIVE_INFINITY);

    NumberComponent.DoubleRange DOUBLE = doubleRange(0.0, Double.POSITIVE_INFINITY);

    NumberComponent.IntRange ANY_INT = intRange(Integer.MIN_VALUE, Integer.MAX_VALUE);

    NumberComponent.LongRange ANY_LONG = longRange(Long.MIN_VALUE, Long.MAX_VALUE);

    NumberComponent.FloatRange ANY_FLOAT = floatRange(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);

    NumberComponent.DoubleRange ANY_DOUBLE = doubleRange(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

    DynamicRecipeComponent DYNAMIC_INT = new DynamicRecipeComponent(TypeDescJS.object().add("min", TypeDescJS.NUMBER).add("max", TypeDescJS.NUMBER), (cx, scope, args) -> {
        int min = ScriptRuntime.toInt32(cx, Wrapper.unwrapped(args.getOrDefault("min", 0)));
        int max = ScriptRuntime.toInt32(cx, Wrapper.unwrapped(args.getOrDefault("max", Integer.MAX_VALUE)));
        return intRange(min, max);
    });

    DynamicRecipeComponent DYNAMIC_LONG = new DynamicRecipeComponent(TypeDescJS.object().add("min", TypeDescJS.NUMBER).add("max", TypeDescJS.NUMBER), (cx, scope, args) -> {
        double min = ScriptRuntime.toNumber(cx, Wrapper.unwrapped(args.getOrDefault("min", 0)));
        double max = ScriptRuntime.toNumber(cx, Wrapper.unwrapped(args.getOrDefault("max", Long.MAX_VALUE)));
        return longRange((long) min, (long) max);
    });

    DynamicRecipeComponent DYNAMIC_FLOAT = new DynamicRecipeComponent(TypeDescJS.object().add("min", TypeDescJS.NUMBER).add("max", TypeDescJS.NUMBER), (cx, scope, args) -> {
        double min = ScriptRuntime.toNumber(cx, Wrapper.unwrapped(args.getOrDefault("min", 0.0F)));
        double max = ScriptRuntime.toNumber(cx, Wrapper.unwrapped(args.getOrDefault("max", Float.MAX_VALUE)));
        return floatRange((float) min, (float) max);
    });

    DynamicRecipeComponent DYNAMIC_DOUBLE = new DynamicRecipeComponent(TypeDescJS.object().add("min", TypeDescJS.NUMBER).add("max", TypeDescJS.NUMBER), (cx, scope, args) -> {
        double min = ScriptRuntime.toNumber(cx, Wrapper.unwrapped(args.getOrDefault("min", 0)));
        double max = ScriptRuntime.toNumber(cx, Wrapper.unwrapped(args.getOrDefault("max", Double.MAX_VALUE)));
        return doubleRange(min, max);
    });

    static NumberComponent.IntRange intRange(int min, int max) {
        return new NumberComponent.IntRange(min, max);
    }

    static NumberComponent.LongRange longRange(long min, long max) {
        return new NumberComponent.LongRange(min, max);
    }

    static NumberComponent.FloatRange floatRange(float min, float max) {
        return new NumberComponent.FloatRange(min, max);
    }

    static NumberComponent.DoubleRange doubleRange(double min, double max) {
        return new NumberComponent.DoubleRange(min, max);
    }

    private static Number numberOf(Object from) {
        if (from instanceof Number) {
            return (Number) from;
        } else if (from instanceof JsonPrimitive json) {
            return json.getAsNumber();
        } else if (from instanceof CharSequence) {
            return Double.parseDouble(from.toString());
        } else {
            throw new IllegalStateException("Expected a number!");
        }
    }

    @Override
    default String componentType() {
        return "number";
    }

    @Override
    default Class<?> componentClass() {
        return Number.class;
    }

    @Override
    default TypeDescJS constructorDescription(DescriptionContext ctx) {
        return TypeDescJS.NUMBER;
    }

    @Override
    default boolean hasPriority(RecipeJS recipe, Object from) {
        if (from instanceof Number) {
            return true;
        } else {
            if (from instanceof JsonPrimitive json && json.isNumber()) {
                return true;
            }
            return false;
        }
    }

    public static record DoubleRange(double min, double max) implements NumberComponent<Double> {

        @Override
        public Class<?> componentClass() {
            return Double.class;
        }

        public JsonPrimitive write(RecipeJS recipe, Double value) {
            return new JsonPrimitive(value);
        }

        public Double read(RecipeJS recipe, Object from) {
            return Mth.clamp(NumberComponent.numberOf(from).doubleValue(), this.min, this.max);
        }

        public NumberComponent.DoubleRange min(double min) {
            return new NumberComponent.DoubleRange(min, this.max);
        }

        public NumberComponent.DoubleRange max(double max) {
            return new NumberComponent.DoubleRange(this.min, max);
        }

        public String toString() {
            return "double";
        }
    }

    public static record FloatRange(float min, float max) implements NumberComponent<Float> {

        @Override
        public Class<?> componentClass() {
            return Float.class;
        }

        public JsonPrimitive write(RecipeJS recipe, Float value) {
            return new JsonPrimitive(value);
        }

        public Float read(RecipeJS recipe, Object from) {
            return Mth.clamp(NumberComponent.numberOf(from).floatValue(), this.min, this.max);
        }

        public NumberComponent.FloatRange min(float min) {
            return new NumberComponent.FloatRange(min, this.max);
        }

        public NumberComponent.FloatRange max(float max) {
            return new NumberComponent.FloatRange(this.min, max);
        }

        public String toString() {
            return "float";
        }
    }

    public static record IntRange(int min, int max) implements NumberComponent<Integer> {

        @Override
        public Class<?> componentClass() {
            return Integer.class;
        }

        public JsonPrimitive write(RecipeJS recipe, Integer value) {
            return new JsonPrimitive(value);
        }

        public Integer read(RecipeJS recipe, Object from) {
            return Mth.clamp(NumberComponent.numberOf(from).intValue(), this.min, this.max);
        }

        public NumberComponent.IntRange min(int min) {
            return new NumberComponent.IntRange(min, this.max);
        }

        public NumberComponent.IntRange max(int max) {
            return new NumberComponent.IntRange(this.min, max);
        }

        public String toString() {
            return "int";
        }
    }

    public static record LongRange(long min, long max) implements NumberComponent<Long> {

        @Override
        public Class<?> componentClass() {
            return Long.class;
        }

        public JsonPrimitive write(RecipeJS recipe, Long value) {
            return new JsonPrimitive(value);
        }

        public Long read(RecipeJS recipe, Object from) {
            long val = NumberComponent.numberOf(from).longValue();
            return val < this.min ? this.min : Math.min(val, this.max);
        }

        public NumberComponent.LongRange min(long min) {
            return new NumberComponent.LongRange(min, this.max);
        }

        public NumberComponent.LongRange max(long max) {
            return new NumberComponent.LongRange(this.min, max);
        }

        public String toString() {
            return "long";
        }
    }
}