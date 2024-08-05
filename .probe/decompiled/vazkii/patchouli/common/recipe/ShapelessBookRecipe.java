package vazkii.patchouli.common.recipe;

import com.google.common.base.Preconditions;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.PatchouliAPI;

public class ShapelessBookRecipe extends ShapelessRecipe {

    public static final RecipeSerializer<ShapelessBookRecipe> SERIALIZER = new BookRecipeSerializer<>(RecipeSerializer.SHAPELESS_RECIPE, ShapelessBookRecipe::new);

    public ShapelessBookRecipe(ShapelessRecipe compose, @Nullable ResourceLocation outputBook) {
        super(compose.getId(), compose.getGroup(), CraftingBookCategory.MISC, getOutputBook(compose, outputBook), compose.getIngredients());
    }

    private static ItemStack getOutputBook(ShapelessRecipe compose, @Nullable ResourceLocation outputBook) {
        Preconditions.checkArgument(compose.getClass() == ShapelessRecipe.class, "Must be exactly ShapelessRecipe");
        return outputBook != null ? PatchouliAPI.get().getBookStack(outputBook) : compose.getResultItem(RegistryAccess.EMPTY);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}