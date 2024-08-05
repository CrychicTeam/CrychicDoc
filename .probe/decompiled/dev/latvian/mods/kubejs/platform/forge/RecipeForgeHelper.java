package dev.latvian.mods.kubejs.platform.forge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.core.mixin.forge.RecipeManagerAccessor;
import dev.latvian.mods.kubejs.platform.RecipePlatformHelper;
import dev.latvian.mods.kubejs.server.KubeJSReloadListener;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

public class RecipeForgeHelper implements RecipePlatformHelper {

    public static final String FORGE_CONDITIONAL = "forge:conditional";

    @Nullable
    @Override
    public Recipe<?> fromJson(RecipeSerializer<?> serializer, ResourceLocation id, JsonObject json) {
        return serializer.fromJson(id, json, (ICondition.IContext) KubeJSReloadListener.recipeContext);
    }

    @Nullable
    @Override
    public JsonObject checkConditions(JsonObject json) {
        ICondition.IContext context = (ICondition.IContext) KubeJSReloadListener.recipeContext;
        if (!json.has("type")) {
            return null;
        } else if (json.get("type").getAsString().equals("forge:conditional")) {
            for (JsonElement ele : GsonHelper.getAsJsonArray(json, "recipes")) {
                if (!ele.isJsonObject()) {
                    return null;
                }
                if (CraftingHelper.processConditions(GsonHelper.getAsJsonArray(ele.getAsJsonObject(), "conditions"), context)) {
                    return GsonHelper.getAsJsonObject(ele.getAsJsonObject(), "recipe");
                }
            }
            return null;
        } else {
            if (json.get("conditions") instanceof JsonArray arr && !CraftingHelper.processConditions(arr, context)) {
                return null;
            }
            return json;
        }
    }

    @Override
    public Ingredient getCustomIngredient(JsonObject object) {
        return CraftingHelper.getIngredient(object, false);
    }

    @Override
    public boolean processConditions(RecipeManager recipeManager, JsonObject json) {
        return !json.has("conditions") || CraftingHelper.processConditions(json, "conditions", (ICondition.IContext) KubeJSReloadListener.recipeContext);
    }

    @Override
    public void pingNewRecipes(Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> map) {
    }

    @Override
    public Object createRecipeContext(ReloadableServerResources resources) {
        return ((RecipeManagerAccessor) resources.getRecipeManager()).getContext();
    }
}