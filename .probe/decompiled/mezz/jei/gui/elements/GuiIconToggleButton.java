package mezz.jei.gui.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.common.gui.TooltipRenderer;
import mezz.jei.common.gui.elements.DrawableBlank;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class GuiIconToggleButton {

    private final IDrawable offIcon;

    private final IDrawable onIcon;

    private final GuiIconButton button;

    private ImmutableRect2i area;

    public GuiIconToggleButton(IDrawable offIcon, IDrawable onIcon, Textures textures) {
        this.offIcon = offIcon;
        this.onIcon = onIcon;
        this.button = new GuiIconButton(new DrawableBlank(0, 0), b -> {
        }, textures);
        this.area = ImmutableRect2i.EMPTY;
    }

    public void updateBounds(ImmutableRect2i area) {
        this.button.m_93674_(area.getWidth());
        this.button.setHeight(area.getHeight());
        this.button.m_252865_(area.getX());
        this.button.m_253211_(area.getY());
        this.area = area;
    }

    public void draw(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (!this.area.isEmpty()) {
            this.button.render(guiGraphics, mouseX, mouseY, partialTicks);
            IDrawable icon = this.isIconToggledOn() ? this.onIcon : this.offIcon;
            icon.draw(guiGraphics, this.button.m_252754_() + 2, this.button.m_252907_() + 2);
        }
    }

    public final boolean isMouseOver(double mouseX, double mouseY) {
        return this.area.contains(mouseX, mouseY);
    }

    public IUserInputHandler createInputHandler() {
        return new GuiIconToggleButton.UserInputHandler();
    }

    public final void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.isMouseOver((double) mouseX, (double) mouseY)) {
            List<Component> tooltip = new ArrayList();
            this.getTooltips(tooltip);
            TooltipRenderer.drawHoveringText(guiGraphics, tooltip, mouseX, mouseY);
        }
    }

    protected abstract void getTooltips(List<Component> var1);

    protected abstract boolean isIconToggledOn();

    protected abstract boolean onMouseClicked(UserInput var1);

    private class UserInputHandler implements IUserInputHandler {

        @Override
        public final Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
            if (GuiIconToggleButton.this.isMouseOver(input.getMouseX(), input.getMouseY())) {
                IUserInputHandler handler = GuiIconToggleButton.this.button.createInputHandler();
                return handler.handleUserInput(screen, input, keyBindings).flatMap(handled -> GuiIconToggleButton.this.onMouseClicked(input) ? Optional.of(this) : Optional.empty());
            } else {
                return Optional.empty();
            }
        }
    }
}