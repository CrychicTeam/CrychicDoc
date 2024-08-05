package vazkii.patchouli.common.recipe;

import com.google.common.base.Preconditions;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.PatchouliAPI;

public class ShapedBookRecipe extends ShapedRecipe {

    public static final RecipeSerializer<ShapedBookRecipe> SERIALIZER = new BookRecipeSerializer<>(RecipeSerializer.SHAPED_RECIPE, ShapedBookRecipe::new);

    public ShapedBookRecipe(ShapedRecipe compose, @Nullable ResourceLocation outputBook) {
        super(compose.getId(), compose.getGroup(), CraftingBookCategory.MISC, compose.getWidth(), compose.getHeight(), compose.getIngredients(), getOutputBook(compose, outputBook));
    }

    private static ItemStack getOutputBook(ShapedRecipe compose, @Nullable ResourceLocation outputBook) {
        Preconditions.checkArgument(compose.getClass() == ShapedRecipe.class, "Must be exactly ShapedRecipe");
        return outputBook != null ? PatchouliAPI.get().getBookStack(outputBook) : compose.getResultItem(RegistryAccess.EMPTY);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}