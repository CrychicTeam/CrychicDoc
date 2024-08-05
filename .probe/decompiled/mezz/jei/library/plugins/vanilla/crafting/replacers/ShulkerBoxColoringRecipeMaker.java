package mezz.jei.library.plugins.vanilla.crafting.replacers;

import java.util.Arrays;
import java.util.List;
import mezz.jei.common.platform.IPlatformIngredientHelper;
import mezz.jei.common.platform.Services;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public final class ShulkerBoxColoringRecipeMaker {

    private static final String group = "jei.shulker.color";

    public static List<CraftingRecipe> createRecipes() {
        ItemStack baseShulkerStack = new ItemStack(Blocks.SHULKER_BOX);
        Ingredient baseShulkerIngredient = Ingredient.of(baseShulkerStack);
        return Arrays.stream(DyeColor.values()).map(color -> createRecipe(color, baseShulkerIngredient)).toList();
    }

    private static CraftingRecipe createRecipe(DyeColor color, Ingredient baseShulkerIngredient) {
        IPlatformIngredientHelper ingredientHelper = Services.PLATFORM.getIngredientHelper();
        Ingredient colorIngredient = ingredientHelper.createShulkerDyeIngredient(color);
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, baseShulkerIngredient, colorIngredient);
        Block coloredShulkerBox = ShulkerBoxBlock.getBlockByColor(color);
        ItemStack output = new ItemStack(coloredShulkerBox);
        ResourceLocation id = new ResourceLocation("minecraft", "jei.shulker.color." + output.getDescriptionId());
        return new ShapelessRecipe(id, "jei.shulker.color", CraftingBookCategory.MISC, output, inputs);
    }

    private ShulkerBoxColoringRecipeMaker() {
    }
}