package dev.latvian.mods.kubejs.recipe.special;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.KubeJSRecipeEventHandler;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeResultCallback;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.List;
import java.util.Map;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ShapedKubeJSRecipe extends ShapedRecipe implements KubeJSCraftingRecipe {

    private final boolean mirror;

    private final List<IngredientAction> ingredientActions;

    private final ModifyRecipeResultCallback modifyResult;

    private final String stage;

    public ShapedKubeJSRecipe(ResourceLocation id, String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result, boolean mirror, List<IngredientAction> ingredientActions, @Nullable ModifyRecipeResultCallback modifyResult, String stage) {
        super(id, group, category, width, height, ingredients, result);
        this.mirror = mirror;
        this.ingredientActions = ingredientActions;
        this.modifyResult = modifyResult;
        this.stage = stage;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer<?>) KubeJSRecipeEventHandler.SHAPED.get();
    }

    @Override
    public List<IngredientAction> kjs$getIngredientActions() {
        return this.ingredientActions;
    }

    @Nullable
    @Override
    public ModifyRecipeResultCallback kjs$getModifyResult() {
        return this.modifyResult;
    }

    @Override
    public String kjs$getStage() {
        return this.stage;
    }

    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        return this.kjs$getRemainingItems(container);
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        return this.kjs$assemble(container, registryAccess);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        for (int i = 0; i <= craftingContainer.getWidth() - this.f_44146_; i++) {
            for (int j = 0; j <= craftingContainer.getHeight() - this.f_44147_; j++) {
                if (this.mirror && this.m_44170_(craftingContainer, i, j, true)) {
                    return true;
                }
                if (this.m_44170_(craftingContainer, i, j, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static class SerializerKJS implements RecipeSerializer<ShapedKubeJSRecipe> {

        private static final RecipeSerializer<ShapedRecipe> SHAPED = UtilsJS.cast(RegistryInfo.RECIPE_SERIALIZER.getValue(new ResourceLocation("crafting_shaped")));

        public ShapedKubeJSRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapedRecipe shapedRecipe = SHAPED.fromJson(id, json);
            boolean mirror = GsonHelper.getAsBoolean(json, "kubejs:mirror", true);
            boolean shrink = GsonHelper.getAsBoolean(json, "kubejs:shrink", true);
            Map<String, Ingredient> key = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            String[] pattern = ShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern"));
            if (shrink) {
                pattern = ShapedRecipe.shrink(pattern);
            }
            int w = pattern[0].length();
            int h = pattern.length;
            NonNullList<Ingredient> ingredients = ShapedRecipe.dissolvePattern(pattern, key, w, h);
            List<IngredientAction> ingredientActions = IngredientAction.parseList(json.get("kubejs:actions"));
            ModifyRecipeResultCallback modifyResult = null;
            if (json.has("kubejs:modify_result")) {
                modifyResult = (ModifyRecipeResultCallback) RecipesEventJS.MODIFY_RESULT_CALLBACKS.get(id);
            }
            String stage = GsonHelper.getAsString(json, "kubejs:stage", "");
            return new ShapedKubeJSRecipe(id, shapedRecipe.getGroup(), shapedRecipe.category(), w, h, ingredients, shapedRecipe.result, mirror, ingredientActions, modifyResult, stage);
        }

        public ShapedKubeJSRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ShapedRecipe shapedRecipe = SHAPED.fromNetwork(id, buf);
            int flags = buf.readByte();
            String group = shapedRecipe.getGroup();
            CraftingBookCategory category = shapedRecipe.category();
            int width = shapedRecipe.getWidth();
            int height = shapedRecipe.getHeight();
            NonNullList<Ingredient> ingredients = shapedRecipe.getIngredients();
            ItemStack result = shapedRecipe.result;
            List<IngredientAction> ingredientActions = (flags & 1) != 0 ? IngredientAction.readList(buf) : List.of();
            String stage = (flags & 2) != 0 ? buf.readUtf() : "";
            boolean mirror = (flags & 4) != 0;
            return new ShapedKubeJSRecipe(id, group, category, width, height, ingredients, result, mirror, ingredientActions, null, stage);
        }

        public void toNetwork(FriendlyByteBuf buf, ShapedKubeJSRecipe r) {
            SHAPED.toNetwork(buf, r);
            int flags = 0;
            if (r.ingredientActions != null && !r.ingredientActions.isEmpty()) {
                flags |= 1;
            }
            if (r.mirror) {
                flags |= 4;
            }
            if (!r.stage.isEmpty()) {
                flags |= 2;
            }
            buf.writeByte(flags);
            if (r.ingredientActions != null && !r.ingredientActions.isEmpty()) {
                IngredientAction.writeList(buf, r.ingredientActions);
            }
            if (!r.stage.isEmpty()) {
                buf.writeUtf(r.stage);
            }
        }
    }
}