package dev.latvian.mods.kubejs.recipe.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.schema.DynamicRecipeComponent;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.NativeJavaObject;
import dev.latvian.mods.rhino.ScriptableObject;
import dev.latvian.mods.rhino.Wrapper;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;

public record StringComponent(String error, Predicate<String> predicate) implements RecipeComponent<String> {

    public static final RecipeComponent<String> ANY = new StringComponent("", s -> true);

    public static final RecipeComponent<String> NON_EMPTY = new StringComponent("can't be empty", s -> !s.isEmpty());

    public static final RecipeComponent<String> NON_BLANK = new StringComponent("can't be blank", s -> !s.isBlank());

    public static final RecipeComponent<String> ID = new StringComponent("invalid ID", ResourceLocation::m_135830_);

    public static final RecipeComponent<Character> CHARACTER = new RecipeComponent<Character>() {

        @Override
        public String componentType() {
            return "char";
        }

        @Override
        public Class<?> componentClass() {
            return Character.class;
        }

        @Override
        public TypeDescJS constructorDescription(DescriptionContext ctx) {
            return TypeDescJS.STRING;
        }

        public JsonElement write(RecipeJS recipe, Character value) {
            return new JsonPrimitive(value);
        }

        public Character read(RecipeJS recipe, Object from) {
            return from instanceof Character c ? c : String.valueOf(from).charAt(0);
        }

        public String toString() {
            return this.componentType();
        }
    };

    public static final DynamicRecipeComponent DYNAMIC = new DynamicRecipeComponent(TypeDescJS.object().add("error", TypeDescJS.STRING, true).add("filter", TypeDescJS.ANY), (cx, scope, args) -> {
        String error = String.valueOf(Wrapper.unwrapped(args.getOrDefault("error", "invalid string")));
        Object filter = args.get("filter") instanceof ScriptableObject obj ? NativeJavaObject.createInterfaceAdapter(cx, Predicate.class, obj) : UtilsJS.ALWAYS_TRUE;
        return new StringComponent(error, (Predicate<String>) filter);
    });

    @Override
    public String componentType() {
        return "string";
    }

    @Override
    public Class<?> componentClass() {
        return String.class;
    }

    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        return TypeDescJS.STRING;
    }

    public JsonPrimitive write(RecipeJS recipe, String value) {
        return new JsonPrimitive(value);
    }

    public String read(RecipeJS recipe, Object from) {
        String str = from instanceof JsonPrimitive json ? json.getAsString() : String.valueOf(from);
        if (!this.predicate.test(str)) {
            if (this.error.isEmpty()) {
                throw new IllegalArgumentException("Invalid string '" + str + "'");
            } else {
                throw new IllegalArgumentException("Invalid string '" + str + "': " + this.error);
            }
        } else {
            return str;
        }
    }

    @Override
    public boolean hasPriority(RecipeJS recipe, Object from) {
        if (from instanceof CharSequence) {
            return true;
        } else {
            if (from instanceof JsonPrimitive json && json.isString()) {
                return true;
            }
            return false;
        }
    }

    public String toString() {
        return this.componentType();
    }
}