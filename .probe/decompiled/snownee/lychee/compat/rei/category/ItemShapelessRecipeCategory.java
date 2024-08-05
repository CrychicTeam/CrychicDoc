package snownee.lychee.compat.rei.category;

import java.util.List;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import net.minecraft.client.renderer.Rect2i;
import snownee.lychee.compat.rei.display.BaseREIDisplay;
import snownee.lychee.core.ItemShapelessContext;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;

public abstract class ItemShapelessRecipeCategory<C extends ItemShapelessContext, T extends LycheeRecipe<C>, D extends BaseREIDisplay<T>> extends BaseREICategory<C, T, D> {

    public ItemShapelessRecipeCategory(LycheeRecipeType<C, T> recipeType) {
        super(recipeType);
        this.infoRect = new Rect2i(3, 25, 8, 8);
    }

    @Override
    public int getDisplayWidth(D display) {
        return this.getRealWidth();
    }

    @Override
    public int getRealWidth() {
        return 170;
    }

    @Override
    public List<Widget> setupDisplay(D display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - this.getRealWidth() / 2, bounds.getY() + 4);
        T recipe = display.recipe;
        List<Widget> widgets = super.setupDisplay(display, bounds);
        this.drawInfoBadge(widgets, display, startPoint);
        int xCenter = bounds.getCenterX();
        int y = recipe.m_7527_().size() <= 9 && recipe.showingActionsCount() <= 9 ? 28 : 26;
        this.ingredientGroup(widgets, startPoint, recipe, xCenter - 45 - startPoint.x, y);
        this.actionGroup(widgets, startPoint, recipe, xCenter + 50 - startPoint.x, y);
        this.drawExtra(widgets, display, bounds);
        return widgets;
    }

    public void drawExtra(List<Widget> widgets, D display, Rectangle bounds) {
        Rectangle iconBounds = new Rectangle(bounds.getCenterX() - 8, bounds.y + 19, 24, 24);
        widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, 100.0F);
            this.icon.render(graphics, iconBounds, mouseX, mouseY, delta);
            graphics.pose().popPose();
        }));
    }
}