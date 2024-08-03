package dev.ftb.mods.ftblibrary.ui;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public abstract class Button extends Widget {

    protected Component title;

    protected Icon icon;

    public Button(Panel panel, Component t, Icon i) {
        super(panel);
        this.setSize(16, 16);
        this.icon = i;
        this.title = t;
    }

    public Button(Panel panel) {
        this(panel, Component.empty(), Icon.empty());
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    public Button setTitle(Component s) {
        this.title = s;
        return this;
    }

    public Button setIcon(Icon i) {
        this.icon = i;
        return this;
    }

    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawButton(graphics, x, y, w, h, this.getWidgetType());
    }

    public void drawIcon(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        this.icon.draw(graphics, x, y, w, h);
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        GuiHelper.setupDrawing();
        int s = h >= 16 ? 16 : 8;
        this.drawBackground(graphics, theme, x, y, w, h);
        this.drawIcon(graphics, theme, x + (w - s) / 2, y + (h - s) / 2, s, s);
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        if (this.isMouseOver()) {
            if (this.getWidgetType() != WidgetType.DISABLED) {
                this.onClicked(button);
            }
            return true;
        } else {
            return false;
        }
    }

    public abstract void onClicked(MouseButton var1);

    @Override
    public Optional<PositionedIngredient> getIngredientUnderMouse() {
        return PositionedIngredient.of(this.icon.getIngredient(), this, true);
    }

    @Override
    public CursorType getCursor() {
        return CursorType.HAND;
    }
}