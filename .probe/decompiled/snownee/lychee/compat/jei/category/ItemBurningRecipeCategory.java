package snownee.lychee.compat.jei.category;

import java.util.List;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.client.gui.AllGuiTextures;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.type.LycheeRecipeType;
import snownee.lychee.item_burning.ItemBurningRecipe;

public class ItemBurningRecipeCategory extends ItemAndBlockBaseCategory<LycheeContext, ItemBurningRecipe> {

    public ItemBurningRecipeCategory(LycheeRecipeType<LycheeContext, ItemBurningRecipe> recipeType) {
        super(List.of(recipeType), AllGuiTextures.JEI_DOWN_ARROW);
        this.methodRect.setX(27);
    }

    @Override
    public BlockState getIconBlock(List<ItemBurningRecipe> recipes) {
        return Blocks.FIRE.defaultBlockState();
    }

    @Nullable
    public BlockPredicate getInputBlock(ItemBurningRecipe recipe) {
        return null;
    }

    public BlockState getRenderingBlock(ItemBurningRecipe recipe) {
        return this.getIconBlock(List.of(recipe));
    }
}