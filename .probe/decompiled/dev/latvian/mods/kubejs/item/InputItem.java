package dev.latvian.mods.kubejs.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.core.IngredientSupplierKJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.platform.IngredientPlatformHelper;
import dev.latvian.mods.kubejs.platform.RecipePlatformHelper;
import dev.latvian.mods.kubejs.recipe.InputReplacement;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.rhino.mod.util.JsonSerializable;
import dev.latvian.mods.rhino.util.RemapForJS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class InputItem implements IngredientSupplierKJS, InputReplacement, JsonSerializable {

    public static final InputItem EMPTY = new InputItem(Ingredient.EMPTY, 0);

    public static final Map<String, InputItem> PARSE_CACHE = new HashMap();

    public final Ingredient ingredient;

    public final int count;

    public static InputItem of(Ingredient ingredient, int count) {
        return count > 0 && ingredient != Ingredient.EMPTY ? new InputItem(ingredient, count) : EMPTY;
    }

    public static InputItem of(Object o) {
        if (o instanceof InputItem) {
            return (InputItem) o;
        } else if (o instanceof ItemStack stack) {
            return stack.isEmpty() ? EMPTY : of(Ingredient.of(stack), stack.getCount());
        } else if (o instanceof OutputItem out) {
            return out.isEmpty() ? EMPTY : of(Ingredient.of(out.item), out.getCount());
        } else if (o instanceof CharSequence) {
            String str = o.toString();
            if (!str.isEmpty() && !str.equals("air")) {
                InputItem cached = (InputItem) PARSE_CACHE.get(str);
                if (cached != null) {
                    return cached;
                } else {
                    int x = str.indexOf(120);
                    if (x > 0 && x < str.length() - 2 && str.charAt(x + 1) == ' ') {
                        try {
                            Ingredient ingredient = IngredientJS.of(str.substring(x + 2));
                            if (ingredient == Ingredient.EMPTY) {
                                return EMPTY;
                            }
                            int count = Integer.parseInt(str.substring(0, x));
                            cached = of(IngredientJS.of(str.substring(x + 2)), count);
                        } catch (Exception var10) {
                            throw new RecipeExceptionJS("Invalid item input: " + str);
                        }
                    }
                    if (cached == null) {
                        cached = of(IngredientJS.of(str), 1);
                    }
                    PARSE_CACHE.put(str, cached);
                    return cached;
                }
            } else {
                return EMPTY;
            }
        } else {
            return o instanceof JsonElement json ? ofJson(json) : of(IngredientJS.of(o), 1);
        }
    }

    static InputItem ofJson(JsonElement json) {
        if (json != null && !json.isJsonNull() && (!json.isJsonArray() || !json.getAsJsonArray().isEmpty())) {
            if (json.isJsonPrimitive()) {
                return of(json.getAsString());
            } else if (json.isJsonObject()) {
                JsonObject o = json.getAsJsonObject();
                boolean val = o.has("value");
                int count = o.has("count") ? o.get("count").getAsInt() : 1;
                if (o.has("type")) {
                    try {
                        return of(RecipePlatformHelper.get().getCustomIngredient(o), count);
                    } catch (Exception var5) {
                        throw new RecipeExceptionJS("Failed to parse custom ingredient (" + o.get("type") + ") from " + o + ": " + var5);
                    }
                } else if (val || o.has("ingredient")) {
                    return of(IngredientJS.ofJson(val ? o.get("value") : o.get("ingredient")), count);
                } else if (o.has("tag")) {
                    return IngredientPlatformHelper.get().tag(o.get("tag").getAsString()).kjs$withCount(count);
                } else {
                    return o.has("item") ? ItemStackJS.of(o.get("item").getAsString()).getItem().kjs$asIngredient().kjs$withCount(count) : EMPTY;
                }
            } else {
                return of(Ingredient.fromJson(json), 1);
            }
        } else {
            return EMPTY;
        }
    }

    protected InputItem(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    @Override
    public Ingredient kjs$asIngredient() {
        return this.ingredient;
    }

    public InputItem withCount(int count) {
        return count == this.count ? this : new InputItem(this.ingredient, count);
    }

    public boolean isEmpty() {
        return this == EMPTY || this.ingredient.isEmpty() || this.count <= 0;
    }

    public boolean validForMatching() {
        return !this.isEmpty() && this.ingredient.kjs$canBeUsedForMatching();
    }

    public List<InputItem> unwrap() {
        if (this.count <= 1) {
            return List.of(this);
        } else {
            ArrayList<InputItem> list = new ArrayList(this.count);
            InputItem single = this.withCount(1);
            for (int i = 0; i < this.count; i++) {
                list.add(single);
            }
            return list;
        }
    }

    public String toString() {
        return this.count > 1 ? this.count + "x " + this.ingredient : this.ingredient.toString();
    }

    @Override
    public JsonElement toJsonJS() {
        return this.toJsonJS(true);
    }

    @RemapForJS("toJson")
    public JsonElement toJsonJS(boolean alwaysNest) {
        if (!alwaysNest && this.count == 1) {
            return this.ingredient.toJson();
        } else {
            JsonObject o = new JsonObject();
            o.addProperty("count", this.count);
            o.add("ingredient", this.ingredient.toJson());
            return o;
        }
    }

    @Override
    public Object replaceInput(RecipeJS recipe, ReplacementMatch match, InputReplacement original) {
        return original instanceof InputItem o ? this.withCount(o.count) : this;
    }
}