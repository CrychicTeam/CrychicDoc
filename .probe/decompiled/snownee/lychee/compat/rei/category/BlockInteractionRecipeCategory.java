package snownee.lychee.compat.rei.category;

import java.util.List;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.RecipeTypes;
import snownee.lychee.client.gui.AllGuiTextures;
import snownee.lychee.client.gui.ScreenElement;
import snownee.lychee.compat.rei.display.BaseREIDisplay;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.type.BlockKeyRecipeType;
import snownee.lychee.interaction.BlockInteractingRecipe;

public class BlockInteractionRecipeCategory extends ItemAndBlockBaseCategory<LycheeContext, BlockInteractingRecipe, BaseREIDisplay<BlockInteractingRecipe>> {

    public BlockInteractionRecipeCategory(List<BlockKeyRecipeType<LycheeContext, BlockInteractingRecipe>> recipeTypes, ScreenElement mainIcon) {
        super(List.copyOf(recipeTypes), mainIcon);
        this.inputBlockRect.setX(this.inputBlockRect.getX() + 18);
        this.methodRect.setX(this.methodRect.getX() + 18);
        this.infoRect.setX(this.infoRect.getX() + 10);
    }

    @Nullable
    public Component getMethodDescription(BlockInteractingRecipe recipe) {
        return Component.translatable(Util.makeDescriptionId("tip", recipe.getSerializer().getRegistryName()));
    }

    public void drawExtra(BlockInteractingRecipe recipe, GuiGraphics graphics, double mouseX, double mouseY, int centerX) {
        KeyMapping keyMapping = this.getKeyMapping(recipe);
        AllGuiTextures icon;
        if (keyMapping.matchesMouse(0)) {
            icon = AllGuiTextures.LEFT_CLICK;
        } else if (keyMapping.matchesMouse(1)) {
            icon = AllGuiTextures.RIGHT_CLICK;
        } else {
            icon = recipe.getType() == RecipeTypes.BLOCK_CLICKING ? AllGuiTextures.LEFT_CLICK : AllGuiTextures.RIGHT_CLICK;
        }
        icon.render(graphics, 51, 15);
    }

    private KeyMapping getKeyMapping(BlockInteractingRecipe recipe) {
        boolean click = recipe.getType() == RecipeTypes.BLOCK_CLICKING;
        return click ? Minecraft.getInstance().options.keyAttack : Minecraft.getInstance().options.keyUse;
    }

    @Override
    public int getRealWidth() {
        return super.getRealWidth() + 20;
    }
}