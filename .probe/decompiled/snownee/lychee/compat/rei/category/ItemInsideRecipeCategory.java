package snownee.lychee.compat.rei.category;

import java.util.List;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import snownee.lychee.client.gui.ScreenElement;
import snownee.lychee.compat.rei.display.BaseREIDisplay;
import snownee.lychee.core.ItemShapelessContext;
import snownee.lychee.item_inside.ItemInsideRecipe;
import snownee.lychee.item_inside.ItemInsideRecipeType;
import snownee.lychee.util.ClientProxy;

public class ItemInsideRecipeCategory extends ItemAndBlockBaseCategory<ItemShapelessContext, ItemInsideRecipe, BaseREIDisplay<ItemInsideRecipe>> {

    public ItemInsideRecipeCategory(ItemInsideRecipeType recipeType, ScreenElement mainIcon) {
        super(List.of(recipeType), mainIcon);
        this.infoRect.setPosition(4, 25);
        this.inputBlockRect.setX(80);
        this.methodRect.setX(77);
    }

    @Override
    public List<Widget> setupDisplay(BaseREIDisplay<ItemInsideRecipe> display, Rectangle bounds) {
        List<Widget> widgets = super.setupDisplay(display, bounds);
        if (display.recipe.getTime() > 0) {
            widgets.add(Widgets.createLabel(new Point(bounds.x + this.methodRect.getX() + 10, bounds.y + this.methodRect.getY() - 6), ClientProxy.format("tip.lychee.sec", display.recipe.getTime())).color(-10066330, -4473925).noShadow().centered());
        }
        return widgets;
    }

    @Override
    public int getDisplayWidth(BaseREIDisplay<ItemInsideRecipe> display) {
        return this.getRealWidth();
    }

    @Override
    public int getRealWidth() {
        return 170;
    }
}