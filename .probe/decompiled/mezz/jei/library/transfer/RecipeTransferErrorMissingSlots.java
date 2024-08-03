package mezz.jei.library.transfer;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collection;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class RecipeTransferErrorMissingSlots extends RecipeTransferErrorTooltip {

    private static final int HIGHLIGHT_COLOR = 1727987712;

    private final Collection<IRecipeSlotView> slots;

    public RecipeTransferErrorMissingSlots(Component message, Collection<IRecipeSlotView> slots) {
        super(message);
        this.slots = slots;
    }

    @Override
    public void showError(GuiGraphics guiGraphics, int mouseX, int mouseY, IRecipeSlotsView recipeSlotsView, int recipeX, int recipeY) {
        super.showError(guiGraphics, mouseX, mouseY, recipeSlotsView, recipeX, recipeY);
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate((float) recipeX, (float) recipeY, 0.0F);
        for (IRecipeSlotView slot : this.slots) {
            slot.drawHighlight(guiGraphics, 1727987712);
        }
        poseStack.popPose();
    }
}