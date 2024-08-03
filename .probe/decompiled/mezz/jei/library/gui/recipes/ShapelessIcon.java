package mezz.jei.library.gui.recipes;

import java.util.List;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.common.gui.elements.HighResolutionDrawable;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.util.ImmutableRect2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ShapelessIcon {

    private final HighResolutionDrawable icon;

    private ImmutableRect2i area;

    public ShapelessIcon(Textures textures) {
        this.icon = textures.getShapelessIcon();
        this.area = ImmutableRect2i.EMPTY;
        this.setPosition(0, 0);
    }

    public IDrawable getIcon() {
        return this.icon;
    }

    public void setPosition(int posX, int posY) {
        this.area = new ImmutableRect2i(posX, posY, this.icon.getWidth(), this.icon.getHeight());
    }

    public void draw(GuiGraphics guiGraphics) {
        this.icon.draw(guiGraphics, this.area.getX(), this.area.getY());
    }

    public List<Component> getTooltipStrings(int mouseX, int mouseY) {
        return this.area.contains((double) mouseX, (double) mouseY) ? List.of(Component.translatable("jei.tooltip.shapeless.recipe")) : List.of();
    }
}