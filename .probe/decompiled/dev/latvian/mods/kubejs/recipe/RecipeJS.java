package dev.latvian.mods.kubejs.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.core.RecipeKJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.platform.RecipePlatformHelper;
import dev.latvian.mods.kubejs.recipe.component.MissingComponentException;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentBuilderMap;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentValue;
import dev.latvian.mods.kubejs.recipe.ingredientaction.ConsumeAction;
import dev.latvian.mods.kubejs.recipe.ingredientaction.CustomIngredientAction;
import dev.latvian.mods.kubejs.recipe.ingredientaction.DamageAction;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientActionFilter;
import dev.latvian.mods.kubejs.recipe.ingredientaction.KeepAction;
import dev.latvian.mods.kubejs.recipe.ingredientaction.ReplaceAction;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.Wrapper;
import dev.latvian.mods.rhino.util.CustomJavaToJsWrapper;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;

public class RecipeJS implements RecipeKJS, CustomJavaToJsWrapper {

    public static boolean itemErrors = false;

    public ResourceLocation id;

    public RecipeTypeFunction type;

    public boolean newRecipe;

    public boolean removed;

    public ModifyRecipeResultCallback modifyResult = null;

    private RecipeComponentBuilderMap valueMap = RecipeComponentBuilderMap.EMPTY;

    private RecipeComponentValue<?>[] inputValues;

    private RecipeComponentValue<?>[] outputValues;

    private Map<String, RecipeComponentValue<?>> allValueMap;

    public JsonObject originalJson = null;

    private MutableObject<Recipe<?>> originalRecipe = null;

    public JsonObject json = null;

    public boolean changed = false;

    protected List<IngredientAction> recipeIngredientActions;

    @Override
    public final Scriptable convertJavaToJs(Context cx, Scriptable scope, Class<?> staticType) {
        return new RecipeFunction(cx, scope, staticType, this);
    }

    public void deserialize(boolean merge) {
        for (RecipeComponentValue<?> v : this.valueMap.holders) {
            v.key.component.readFromJson(this, UtilsJS.cast(v), this.json);
            if (v.value != null) {
                if (merge) {
                    v.write();
                }
            } else if (!v.key.optional()) {
                throw new MissingComponentException(v.key.name, v.key, this.valueMap.keySet());
            }
        }
    }

    public void serialize() {
        for (RecipeComponentValue<?> v : this.valueMap.holders) {
            if (v.shouldWrite()) {
                if (v.value == null) {
                    throw new RecipeExceptionJS("Value not set for " + v.key + " in recipe " + this);
                }
                v.key.component.writeToJson(this, UtilsJS.cast(v), this.json);
            }
        }
    }

    public <T> T getValue(RecipeKey<T> key) {
        RecipeComponentValue<?> v = this.valueMap.getHolder(key);
        if (v == null) {
            throw new MissingComponentException(key.name, key, this.valueMap.keySet());
        } else {
            return UtilsJS.cast(v.value);
        }
    }

    public <T> RecipeJS setValue(RecipeKey<T> key, T value) {
        RecipeComponentValue<T> v = UtilsJS.cast(this.valueMap.getHolder(key));
        if (v == null) {
            throw new MissingComponentException(key.name, key, this.valueMap.keySet());
        } else {
            v.value = value;
            v.write();
            this.save();
            return this;
        }
    }

    @Nullable
    public Object get(String key) {
        for (RecipeComponentValue<?> h : this.valueMap.holders) {
            for (String name : h.key.names) {
                if (name.equals(key)) {
                    return h.value;
                }
            }
        }
        throw new MissingComponentException(key, null, this.valueMap.keySet());
    }

    public RecipeJS set(String key, Object value) {
        for (RecipeComponentValue<?> h : this.valueMap.holders) {
            for (String name : h.key.names) {
                if (name.equals(key)) {
                    h.value = UtilsJS.cast(h.key.component.read(this, Wrapper.unwrapped(value)));
                    h.write();
                    this.save();
                    return this;
                }
            }
        }
        throw new MissingComponentException(key, null, this.valueMap.keySet());
    }

    public void initValues(boolean created) {
        if (created) {
            this.save();
        }
        if (this.type.schemaType.schema.keys.length > 0) {
            this.valueMap = new RecipeComponentBuilderMap(this.type.schemaType.schema.keys);
            if (created) {
                for (RecipeComponentValue<?> v : this.valueMap.holders) {
                    if (v.key.alwaysWrite || !v.key.optional()) {
                        if (v.key.alwaysWrite) {
                            v.value = UtilsJS.cast(v.key.component.read(this, v.key.optional.getDefaultValue(this.type.schemaType)));
                        }
                        v.write();
                    }
                }
            }
        }
    }

    public Map<String, RecipeComponentValue<?>> getAllValueMap() {
        if (this.allValueMap == null) {
            this.allValueMap = new HashMap();
            for (RecipeComponentValue<?> v : this.valueMap.holders) {
                for (String n : v.key.names) {
                    this.allValueMap.put(n, v);
                }
            }
        }
        return this.allValueMap;
    }

    public void afterLoaded() {
        for (RecipeComponentValue<?> v : this.valueMap.holders) {
            String e = v.checkEmpty();
            if (!e.isEmpty()) {
                throw new RecipeExceptionJS(e);
            }
        }
    }

    public final void save() {
        this.changed = true;
    }

    public RecipeJS id(ResourceLocation _id) {
        this.id = _id;
        this.save();
        return this;
    }

    public RecipeJS group(String g) {
        this.kjs$setGroup(g);
        return this;
    }

    public RecipeJS merge(JsonObject j) {
        if (j != null && j.size() > 0) {
            for (Entry<String, JsonElement> entry : j.entrySet()) {
                this.json.add((String) entry.getKey(), (JsonElement) entry.getValue());
            }
            this.save();
            this.deserialize(true);
        }
        return this;
    }

    public final boolean hasChanged() {
        if (this.changed) {
            return true;
        } else {
            for (RecipeComponentValue<?> vc : this.valueMap.holders) {
                if (vc.shouldWrite()) {
                    return true;
                }
            }
            return false;
        }
    }

    @Deprecated
    @Override
    public final String kjs$getGroup() {
        JsonElement e = this.json.get("group");
        return e instanceof JsonPrimitive ? e.getAsString() : "";
    }

    @Deprecated
    @Override
    public final void kjs$setGroup(String group) {
        if (!this.kjs$getGroup().equals(group)) {
            if (group.isEmpty()) {
                this.json.remove("group");
            } else {
                this.json.addProperty("group", group);
            }
            this.save();
        }
    }

    @Deprecated
    @Override
    public final ResourceLocation kjs$getOrCreateId() {
        return this.getOrCreateId();
    }

    @Deprecated
    @Override
    public final RecipeSchema kjs$getSchema() {
        return this.type.schemaType.schema;
    }

    @Deprecated
    @Override
    public final ResourceLocation kjs$getType() {
        return this.getType();
    }

    public final RecipeComponentValue<?>[] inputValues() {
        if (this.inputValues == null) {
            if (this.type.schemaType.schema.inputCount() == 0) {
                this.inputValues = UtilsJS.cast(RecipeComponentValue.EMPTY_ARRAY);
            } else {
                ArrayList<Object> list = new ArrayList(this.type.schemaType.schema.inputCount());
                for (RecipeComponentValue<?> v : this.valueMap.holders) {
                    if (v.key.component.role().isInput()) {
                        list.add(v);
                    }
                }
                this.inputValues = (RecipeComponentValue<?>[]) list.toArray(new RecipeComponentValue[list.size()]);
            }
        }
        return this.inputValues;
    }

    public final RecipeComponentValue<?>[] outputValues() {
        if (this.outputValues == null) {
            if (this.type.schemaType.schema.outputCount() == 0) {
                this.outputValues = UtilsJS.cast(RecipeComponentValue.EMPTY_ARRAY);
            } else {
                ArrayList<Object> list = new ArrayList(this.type.schemaType.schema.outputCount());
                for (RecipeComponentValue<?> v : this.valueMap.holders) {
                    if (v.key.component.role().isOutput()) {
                        list.add(v);
                    }
                }
                this.outputValues = (RecipeComponentValue<?>[]) list.toArray(new RecipeComponentValue[list.size()]);
            }
        }
        return this.outputValues;
    }

    @Override
    public boolean hasInput(ReplacementMatch match) {
        for (RecipeComponentValue<?> v : this.inputValues()) {
            if (v.isInput(this, match)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean replaceInput(ReplacementMatch match, InputReplacement with) {
        boolean replaced = false;
        for (RecipeComponentValue<?> v : this.inputValues()) {
            replaced = v.replaceInput(this, match, with) || replaced;
        }
        if (replaced) {
            this.save();
        }
        return replaced;
    }

    @Override
    public boolean hasOutput(ReplacementMatch match) {
        for (RecipeComponentValue<?> v : this.outputValues()) {
            if (v.isOutput(this, match)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean replaceOutput(ReplacementMatch match, OutputReplacement with) {
        boolean replaced = false;
        for (RecipeComponentValue<?> v : this.outputValues()) {
            replaced = v.replaceOutput(this, match, with) || replaced;
        }
        if (replaced) {
            this.save();
        }
        return replaced;
    }

    public String toString() {
        return this.id == null && this.json == null ? "<no id> [" + this.type + "]" : this.getOrCreateId() + "[" + this.type + "]";
    }

    public String getId() {
        return this.getOrCreateId().toString();
    }

    public String getPath() {
        return this.getOrCreateId().getPath();
    }

    @HideFromJS
    public ResourceLocation getType() {
        return this.type.id;
    }

    @HideFromJS
    public ResourceLocation getOrCreateId() {
        if (this.id == null) {
            RecipeTypeFunction js = this.getSerializationTypeFunction();
            String ids = CommonProperties.get().ignoreCustomUniqueRecipeIds ? null : (String) js.schemaType.schema.uniqueIdFunction.apply(this);
            String prefix = js.id.getNamespace() + ":kjs/";
            if (ids != null && !ids.isEmpty()) {
                ids = ids.replace(':', '_');
            } else {
                ids = UtilsJS.getUniqueId(this.json);
            }
            this.id = this.type.event.takeId(this, prefix, ids);
        }
        return this.id;
    }

    public String getFromToString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (RecipeComponentValue<?> v : this.inputValues()) {
            if (sb.length() > 1) {
                sb.append(",");
            }
            sb.append(v.value);
        }
        sb.append("] -> [");
        for (RecipeComponentValue<?> v : this.outputValues()) {
            if (sb.length() > 1) {
                sb.append(",");
            }
            sb.append(v.value);
        }
        return sb.append(']').toString();
    }

    public final void remove() {
        if (!this.removed) {
            this.removed = true;
            if (DevProperties.get().logRemovedRecipes) {
                ConsoleJS.SERVER.info("- " + this + ": " + this.getFromToString());
            } else if (ConsoleJS.SERVER.shouldPrintDebug()) {
                ConsoleJS.SERVER.debug("- " + this + ": " + this.getFromToString());
            }
        }
    }

    public RecipeJS stage(String s) {
        this.json.addProperty("kubejs:stage", s);
        this.save();
        return this;
    }

    public RecipeTypeFunction getSerializationTypeFunction() {
        return this.type;
    }

    @Nullable
    public Recipe<?> createRecipe() {
        if (this.removed) {
            return null;
        } else {
            if (!this.newRecipe && !this.hasChanged()) {
                if (this.originalRecipe != null) {
                    return (Recipe<?>) this.originalRecipe.getValue();
                }
            } else {
                this.serialize();
                if (this.modifyResult != null) {
                    this.json.addProperty("kubejs:modify_result", true);
                }
                if (this.recipeIngredientActions != null && !this.recipeIngredientActions.isEmpty()) {
                    JsonArray arr = new JsonArray(this.recipeIngredientActions.size());
                    for (IngredientAction action : this.recipeIngredientActions) {
                        arr.add(action.toJson());
                    }
                    this.json.add("kubejs:actions", arr);
                }
                if (this.newRecipe) {
                    this.json.addProperty("type", this.getSerializationTypeFunction().idString);
                }
                ResourceLocation id = this.getOrCreateId();
                if (this.modifyResult != null) {
                    RecipesEventJS.MODIFY_RESULT_CALLBACKS.put(id, this.modifyResult);
                }
                if (this.type.event.stageSerializer != null && this.json.has("kubejs:stage") && !this.type.idString.equals("recipestages:stage")) {
                    JsonObject o = new JsonObject();
                    o.addProperty("stage", this.json.get("kubejs:stage").getAsString());
                    o.add("recipe", this.json);
                    return this.type.event.stageSerializer.fromJson(id, o);
                }
            }
            return RecipePlatformHelper.get().fromJson(this.getSerializationTypeFunction().schemaType.getSerializer(), this.getOrCreateId(), this.json);
        }
    }

    @Nullable
    public Recipe<?> getOriginalRecipe() {
        Throwable error = new Throwable("Original recipe is null!");
        if (this.originalRecipe == null) {
            this.originalRecipe = new MutableObject();
            try {
                this.originalRecipe.setValue(RecipePlatformHelper.get().fromJson(this.type.schemaType.getSerializer(), this.getOrCreateId(), this.json));
            } catch (Throwable var3) {
                error = var3;
            }
            if (this.originalRecipe == null) {
                if (itemErrors) {
                    throw new RecipeExceptionJS("Could not create recipe from json for " + this, error);
                }
                ConsoleJS.SERVER.warn("Could not create recipe from json for " + this, error);
            }
        }
        return (Recipe<?>) this.originalRecipe.getValue();
    }

    public ItemStack getOriginalRecipeResult() {
        if (this.getOriginalRecipe() == null) {
            ConsoleJS.SERVER.warn("Original recipe is null - could not get result");
            return ItemStack.EMPTY;
        } else {
            ItemStack result = this.getOriginalRecipe().getResultItem(UtilsJS.staticRegistryAccess);
            return result == null ? ItemStack.EMPTY : result;
        }
    }

    public List<Ingredient> getOriginalRecipeIngredients() {
        if (this.getOriginalRecipe() == null) {
            ConsoleJS.SERVER.warn("Original recipe is null - could not get ingredients");
            return List.of();
        } else {
            return List.copyOf(this.getOriginalRecipe().getIngredients());
        }
    }

    public RecipeJS ingredientAction(IngredientActionFilter filter, IngredientAction action) {
        if (this.recipeIngredientActions == null) {
            this.recipeIngredientActions = new ArrayList(2);
        }
        action.copyFrom(filter);
        this.recipeIngredientActions.add(action);
        this.save();
        return this;
    }

    public final RecipeJS damageIngredient(IngredientActionFilter filter, int damage) {
        return this.ingredientAction(filter, new DamageAction(damage));
    }

    public final RecipeJS damageIngredient(IngredientActionFilter filter) {
        return this.damageIngredient(filter, 1);
    }

    public final RecipeJS replaceIngredient(IngredientActionFilter filter, ItemStack item) {
        return this.ingredientAction(filter, new ReplaceAction(item));
    }

    public final RecipeJS customIngredientAction(IngredientActionFilter filter, String id) {
        return this.ingredientAction(filter, new CustomIngredientAction(id));
    }

    public final RecipeJS keepIngredient(IngredientActionFilter filter) {
        return this.ingredientAction(filter, new KeepAction());
    }

    public final RecipeJS consumeIngredient(IngredientActionFilter filter) {
        return this.ingredientAction(filter, new ConsumeAction());
    }

    public final RecipeJS modifyResult(ModifyRecipeResultCallback callback) {
        this.modifyResult = callback;
        this.save();
        return this;
    }

    public boolean inputItemHasPriority(Object from) {
        return from instanceof InputItem || from instanceof ItemStack || from instanceof Ingredient || !InputItem.of(from).isEmpty();
    }

    public InputItem readInputItem(Object from) {
        return InputItem.of(from);
    }

    public JsonElement writeInputItem(InputItem value) {
        return value.ingredient.toJson();
    }

    public boolean outputItemHasPriority(Object from) {
        return from instanceof OutputItem || from instanceof ItemStack || !OutputItem.of(from).isEmpty();
    }

    public OutputItem readOutputItem(Object from) {
        return OutputItem.of(from);
    }

    public JsonElement writeOutputItem(OutputItem value) {
        JsonObject json = new JsonObject();
        json.addProperty("item", value.item.kjs$getId());
        json.addProperty("count", value.item.getCount());
        if (value.item.getTag() != null) {
            json.addProperty("nbt", value.item.getTag().toString());
        }
        if (value.hasChance()) {
            json.addProperty("chance", value.getChance());
        }
        if (value.rolls != null) {
            json.addProperty("minRolls", value.rolls.getMinValue());
            json.addProperty("maxRolls", value.rolls.getMaxValue());
        }
        return json;
    }

    public boolean inputFluidHasPriority(Object from) {
        if (from instanceof InputFluid) {
            return true;
        } else {
            if (from instanceof JsonObject j && j.has("fluid")) {
                return true;
            }
            return false;
        }
    }

    public InputFluid readInputFluid(Object from) {
        return FluidStackJS.of(from);
    }

    public JsonElement writeInputFluid(InputFluid value) {
        return ((FluidStackJS) value).toJson();
    }

    public boolean outputFluidHasPriority(Object from) {
        if (from instanceof OutputFluid) {
            return true;
        } else {
            if (from instanceof JsonObject j && j.has("fluid")) {
                return true;
            }
            return false;
        }
    }

    public OutputFluid readOutputFluid(Object from) {
        return FluidStackJS.of(from);
    }

    public JsonElement writeOutputFluid(OutputFluid value) {
        return ((FluidStackJS) value).toJson();
    }
}