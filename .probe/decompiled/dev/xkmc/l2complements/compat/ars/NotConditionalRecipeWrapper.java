package dev.xkmc.l2complements.compat.ars;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public record NotConditionalRecipeWrapper(FinishedRecipe base, String[] modid) implements FinishedRecipe {

    public static Consumer<FinishedRecipe> mod(Consumer<FinishedRecipe> pvd, String... modid) {
        return r -> pvd.accept(new NotConditionalRecipeWrapper(r, modid));
    }

    @Override
    public void serializeRecipeData(JsonObject pJson) {
        this.base.serializeRecipeData(pJson);
    }

    @Override
    public ResourceLocation getId() {
        return this.base.getId();
    }

    @Override
    public RecipeSerializer<?> getType() {
        return this.base.getType();
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        JsonObject ans = this.base.serializeAdvancement();
        if (ans == null) {
            return null;
        } else {
            this.addCondition(ans);
            return ans;
        }
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return this.base.getAdvancementId();
    }

    @Override
    public JsonObject serializeRecipe() {
        JsonObject ans = this.base.serializeRecipe();
        this.addCondition(ans);
        return ans;
    }

    private void addCondition(JsonObject ans) {
        JsonArray conditions = new JsonArray();
        for (String str : this.modid) {
            JsonObject condition = new JsonObject();
            condition.addProperty("type", "forge:mod_loaded");
            condition.addProperty("modid", str);
            JsonObject not = new JsonObject();
            not.addProperty("type", "forge:not");
            not.add("value", condition);
            conditions.add(not);
        }
        ans.add("conditions", conditions);
    }
}