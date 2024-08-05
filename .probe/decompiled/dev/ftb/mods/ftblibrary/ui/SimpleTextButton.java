package dev.ftb.mods.ftblibrary.ui;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

public abstract class SimpleTextButton extends Button {

    public SimpleTextButton(Panel panel, Component txt, Icon icon) {
        super(panel, txt, icon);
        this.setWidth(panel.getGui().getTheme().getStringWidth(txt) + (this.hasIcon() ? 28 : 8));
        this.setHeight(20);
    }

    public SimpleTextButton setTitle(Component txt) {
        super.setTitle(txt);
        this.setWidth(this.getGui().getTheme().getStringWidth(this.getTitle()) + (this.hasIcon() ? 28 : 8));
        return this;
    }

    public boolean renderTitleInCenter() {
        return false;
    }

    @Override
    public Optional<PositionedIngredient> getIngredientUnderMouse() {
        return PositionedIngredient.of(this.icon.getIngredient(), this);
    }

    public boolean hasIcon() {
        return !this.icon.isEmpty();
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        if (this.getGui().getTheme().getStringWidth(this.getTitle()) + (this.hasIcon() ? 28 : 8) > this.width) {
            list.add(this.getTitle());
        }
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        this.drawBackground(graphics, theme, x, y, w, h);
        int s = h >= 16 ? 16 : 8;
        int off = (h - s) / 2;
        FormattedText title = this.getTitle();
        int textY = y + (h - theme.getFontHeight() + 1) / 2;
        int sw = theme.getStringWidth(title);
        int mw = w - (this.hasIcon() ? off + s : 0) - 6;
        if (sw > mw) {
            sw = mw;
            title = theme.trimStringToWidth(title, mw);
        }
        int textX;
        if (this.renderTitleInCenter()) {
            textX = x + (mw - sw + 6) / 2;
        } else {
            textX = x + 4;
        }
        if (this.hasIcon()) {
            this.drawIcon(graphics, theme, x + off, y + off, s, s);
            textX += off + s;
        }
        theme.drawString(graphics, title, textX, textY, theme.getContentColor(this.getWidgetType()), 2);
    }

    public static SimpleTextButton create(Panel panel, Component txt, Icon icon, Consumer<MouseButton> callback, Component... tooltip) {
        return new SimpleTextButton(panel, txt, icon) {

            @Override
            public void onClicked(MouseButton button) {
                callback.accept(button);
            }

            @Override
            public void addMouseOverText(TooltipList list) {
                for (Component c : tooltip) {
                    list.add(c);
                }
            }
        };
    }

    public static SimpleTextButton accept(Panel panel, Consumer<MouseButton> callback, Component... tooltip) {
        return create(panel, Component.translatable("gui.accept"), Icons.ACCEPT, callback, tooltip);
    }

    public static SimpleTextButton cancel(Panel panel, Consumer<MouseButton> callback, Component... tooltip) {
        return create(panel, Component.translatable("gui.cancel"), Icons.CANCEL, callback, tooltip);
    }
}