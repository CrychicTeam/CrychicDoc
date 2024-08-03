package dev.latvian.mods.kubejs.platform;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.util.Lazy;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public interface RecipePlatformHelper {

    Lazy<RecipePlatformHelper> INSTANCE = Lazy.serviceLoader(RecipePlatformHelper.class);

    static RecipePlatformHelper get() {
        return INSTANCE.get();
    }

    @Nullable
    Recipe<?> fromJson(RecipeSerializer<?> var1, ResourceLocation var2, JsonObject var3);

    @Nullable
    JsonObject checkConditions(JsonObject var1);

    Ingredient getCustomIngredient(JsonObject var1);

    void pingNewRecipes(Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> var1);

    boolean processConditions(RecipeManager var1, JsonObject var2);

    Object createRecipeContext(ReloadableServerResources var1);
}