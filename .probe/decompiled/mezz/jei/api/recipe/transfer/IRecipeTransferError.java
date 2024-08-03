package mezz.jei.api.recipe.transfer;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphics;

public interface IRecipeTransferError {

    IRecipeTransferError.Type getType();

    default int getButtonHighlightColor() {
        return -2130729728;
    }

    default void showError(GuiGraphics guiGraphics, int mouseX, int mouseY, IRecipeSlotsView recipeSlotsView, int recipeX, int recipeY) {
    }

    public static enum Type {

        INTERNAL(false), USER_FACING(false), COSMETIC(true);

        public final boolean allowsTransfer;

        private Type(boolean allowsTransfer) {
            this.allowsTransfer = allowsTransfer;
        }
    }
}