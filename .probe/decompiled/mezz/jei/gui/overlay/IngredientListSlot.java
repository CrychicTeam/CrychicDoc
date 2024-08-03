package mezz.jei.gui.overlay;

import java.util.Optional;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.common.util.ImmutableRect2i;
import org.jetbrains.annotations.Nullable;

public class IngredientListSlot {

    private final ImmutableRect2i area;

    private final int padding;

    private boolean blocked = false;

    @Nullable
    private ElementRenderer<?> ingredientRenderer;

    public IngredientListSlot(int xPosition, int yPosition, int width, int height, int padding) {
        this.area = new ImmutableRect2i(xPosition, yPosition, width, height);
        this.padding = padding;
    }

    public Optional<ElementRenderer<?>> getIngredientRenderer() {
        return Optional.ofNullable(this.ingredientRenderer);
    }

    public Optional<ITypedIngredient<?>> getTypedIngredient() {
        return this.getIngredientRenderer().map(ElementRenderer::getTypedIngredient);
    }

    public void clear() {
        this.ingredientRenderer = null;
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.ingredientRenderer != null && this.area.contains(mouseX, mouseY);
    }

    public void setIngredientRenderer(ElementRenderer<?> ingredientRenderer) {
        this.ingredientRenderer = ingredientRenderer;
        ingredientRenderer.setArea(this.area);
        ingredientRenderer.setPadding(this.padding);
    }

    public ImmutableRect2i getArea() {
        return this.area;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isBlocked() {
        return this.blocked;
    }
}