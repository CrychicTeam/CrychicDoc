package mezz.jei.library.gui.ingredients;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.common.util.MathUtil;
import net.minecraft.client.gui.GuiGraphics;

public class RecipeSlots {

    private final List<RecipeSlot> slots = new ArrayList();

    private final IRecipeSlotsView view = new RecipeSlotsView(this.slots);

    public IRecipeSlotsView getView() {
        return this.view;
    }

    public List<RecipeSlot> getSlots() {
        return Collections.unmodifiableList(this.slots);
    }

    public void addSlot(RecipeSlot slot) {
        this.slots.add(slot);
    }

    public void draw(GuiGraphics guiGraphics) {
        for (IRecipeSlotDrawable slot : this.slots) {
            slot.draw(guiGraphics);
        }
    }

    public Optional<RecipeSlot> getHoveredSlot(double recipeMouseX, double recipeMouseY) {
        return this.slots.stream().filter(ingredient -> MathUtil.contains(ingredient.getRect(), recipeMouseX, recipeMouseY)).findFirst();
    }
}