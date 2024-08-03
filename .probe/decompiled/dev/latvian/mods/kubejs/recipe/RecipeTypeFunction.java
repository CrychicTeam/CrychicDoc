package dev.latvian.mods.kubejs.recipe;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.component.ComponentValueMap;
import dev.latvian.mods.kubejs.recipe.schema.RecipeConstructor;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaType;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.kubejs.util.WrappedJS;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.Wrapper;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;

public class RecipeTypeFunction extends BaseFunction implements WrappedJS {

    public static final Pattern SKIP_ERROR = Pattern.compile("dev\\.latvian\\.mods\\.kubejs\\.recipe\\.RecipeTypeFunction\\.call");

    public final RecipesEventJS event;

    public final ResourceLocation id;

    public final String idString;

    public final RecipeSchemaType schemaType;

    public RecipeTypeFunction(RecipesEventJS event, RecipeSchemaType schemaType) {
        this.event = event;
        this.id = schemaType.id;
        this.idString = this.id.toString();
        this.schemaType = schemaType;
    }

    public RecipeJS call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args0) {
        try {
            return this.createRecipe(args0);
        } catch (RecipeExceptionJS var6) {
            if (var6.error) {
                throw var6;
            } else {
                return new ErroredRecipeJS(this.event, "Failed to create recipe for type '%s'".formatted(this.idString), var6, SKIP_ERROR);
            }
        }
    }

    public RecipeJS createRecipe(Object[] args) {
        try {
            for (int i = 0; i < args.length; i++) {
                args[i] = Wrapper.unwrapped(args[i]);
            }
            this.schemaType.getSerializer();
            RecipeConstructor constructor = (RecipeConstructor) this.schemaType.schema.constructors().get(args.length);
            if (constructor == null) {
                if (args.length != 1 || !(args[0] instanceof Map) && !(args[0] instanceof JsonObject)) {
                    throw new RecipeExceptionJS("Constructor for " + this.id + " with " + args.length + " arguments not found!");
                } else {
                    RecipeJS recipe = this.schemaType.schema.deserialize(this, null, MapJS.json(args[0]));
                    recipe.afterLoaded();
                    return this.event.addRecipe(recipe, true);
                }
            } else {
                ComponentValueMap argMap = new ComponentValueMap(args.length);
                int index = 0;
                for (RecipeKey<?> key : constructor.keys()) {
                    argMap.put(key, Wrapper.unwrapped(args[index++]));
                }
                RecipeJS recipe = constructor.factory().create(this, this.schemaType, constructor.keys(), argMap);
                recipe.afterLoaded();
                return this.event.addRecipe(recipe, false);
            }
        } catch (RecipeExceptionJS var9) {
            throw var9;
        } catch (Throwable var10) {
            throw new RecipeExceptionJS("Failed to create recipe for type '" + this.id + "' with args " + (String) Arrays.stream(args).map(o -> o == null ? "null" : o + ": " + o.getClass().getSimpleName()).collect(Collectors.joining(", ", "[", "]")), var10);
        }
    }

    @Override
    public String toString() {
        return this.idString;
    }

    public String getMod() {
        return this.id.getNamespace();
    }

    public int hashCode() {
        return this.idString.hashCode();
    }

    public boolean equals(Object obj) {
        return this.idString.equals(obj.toString());
    }
}