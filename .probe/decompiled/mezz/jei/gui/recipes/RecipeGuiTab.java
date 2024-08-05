package mezz.jei.gui.recipes;

import java.util.List;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.input.IUserInputHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public abstract class RecipeGuiTab implements IUserInputHandler {

    public static final int TAB_HEIGHT = 24;

    public static final int TAB_WIDTH = 24;

    private final Textures textures;

    protected final int x;

    protected final int y;

    private final ImmutableRect2i area;

    public RecipeGuiTab(Textures textures, int x, int y) {
        this.textures = textures;
        this.x = x;
        this.y = y;
        this.area = new ImmutableRect2i(x, y, 24, 24);
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.area.contains(mouseX, mouseY);
    }

    public abstract boolean isSelected(IRecipeCategory<?> var1);

    public void draw(boolean selected, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        IDrawable tab = selected ? this.textures.getTabSelected() : this.textures.getTabUnselected();
        tab.draw(guiGraphics, this.x, this.y);
    }

    public abstract List<Component> getTooltip(IModIdHelper var1);
}