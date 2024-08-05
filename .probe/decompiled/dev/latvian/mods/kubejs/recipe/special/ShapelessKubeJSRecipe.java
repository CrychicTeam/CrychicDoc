package dev.latvian.mods.kubejs.recipe.special;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.KubeJSRecipeEventHandler;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeResultCallback;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.Nullable;

public class ShapelessKubeJSRecipe extends ShapelessRecipe implements KubeJSCraftingRecipe {

    private final List<IngredientAction> ingredientActions;

    private final ModifyRecipeResultCallback modifyResult;

    private final String stage;

    public ShapelessKubeJSRecipe(ShapelessRecipe original, List<IngredientAction> ingredientActions, @Nullable ModifyRecipeResultCallback modifyResult, String stage) {
        super(original.getId(), original.getGroup(), original.category(), original.result, original.getIngredients());
        this.ingredientActions = ingredientActions;
        this.modifyResult = modifyResult;
        this.stage = stage;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer<?>) KubeJSRecipeEventHandler.SHAPELESS.get();
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

    public static class SerializerKJS implements RecipeSerializer<ShapelessKubeJSRecipe> {

        private static final RecipeSerializer<ShapelessRecipe> SHAPELESS = UtilsJS.cast(RegistryInfo.RECIPE_SERIALIZER.getValue(new ResourceLocation("crafting_shapeless")));

        public ShapelessKubeJSRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapelessRecipe shapelessRecipe = SHAPELESS.fromJson(id, json);
            List<IngredientAction> ingredientActions = IngredientAction.parseList(json.get("kubejs:actions"));
            ModifyRecipeResultCallback modifyResult = null;
            if (json.has("kubejs:modify_result")) {
                modifyResult = (ModifyRecipeResultCallback) RecipesEventJS.MODIFY_RESULT_CALLBACKS.get(id);
            }
            String stage = GsonHelper.getAsString(json, "kubejs:stage", "");
            return new ShapelessKubeJSRecipe(shapelessRecipe, ingredientActions, modifyResult, stage);
        }

        public ShapelessKubeJSRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ShapelessRecipe shapelessRecipe = SHAPELESS.fromNetwork(id, buf);
            int flags = buf.readByte();
            List<IngredientAction> ingredientActions = (flags & 1) != 0 ? IngredientAction.readList(buf) : List.of();
            String stage = (flags & 2) != 0 ? buf.readUtf() : "";
            return new ShapelessKubeJSRecipe(shapelessRecipe, ingredientActions, null, stage);
        }

        public void toNetwork(FriendlyByteBuf buf, ShapelessKubeJSRecipe r) {
            SHAPELESS.toNetwork(buf, r);
            int flags = 0;
            if (r.ingredientActions != null && !r.ingredientActions.isEmpty()) {
                flags |= 1;
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