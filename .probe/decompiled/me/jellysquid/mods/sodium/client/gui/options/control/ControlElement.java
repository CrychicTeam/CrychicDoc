package me.jellysquid.mods.sodium.client.gui.options.control;

import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.gui.widgets.FlatButtonWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ControlElement<T> extends AbstractWidget {

    protected final Option<T> option;

    protected final Dim2i dim;

    @NotNull
    private FlatButtonWidget.Style style = FlatButtonWidget.Style.defaults();

    public ControlElement(Option<T> option, Dim2i dim) {
        this.option = option;
        this.dim = dim;
    }

    @Override
    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
        String name = this.option.getName().getString();
        if ((this.hovered || this.m_93696_()) && this.font.width(name) > this.dim.width() - this.option.getControl().getMaxWidth()) {
            name = name.substring(0, Math.min(name.length(), 10)) + "...";
        }
        String label;
        if (this.option.isAvailable()) {
            if (this.option.hasChanged()) {
                label = ChatFormatting.ITALIC + name + " *";
            } else {
                label = ChatFormatting.WHITE + name;
            }
        } else {
            label = "" + ChatFormatting.GRAY + ChatFormatting.STRIKETHROUGH + name;
        }
        this.hovered = this.dim.containsCursor((double) mouseX, (double) mouseY);
        this.drawRect(drawContext, this.dim.x(), this.dim.y(), this.dim.getLimitX(), this.dim.getLimitY(), this.hovered ? this.style.bgHovered : this.style.bgDefault);
        this.drawString(drawContext, label, this.dim.x() + 6, this.dim.getCenterY() - 4, this.style.textDefault);
        if (this.m_93696_()) {
            this.drawBorder(drawContext, this.dim.x(), this.dim.y(), this.dim.getLimitX(), this.dim.getLimitY(), -1);
        }
    }

    public Option<T> getOption() {
        return this.option;
    }

    public Dim2i getDimensions() {
        return this.dim;
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent navigation) {
        return !this.option.isAvailable() ? null : super.nextFocusPath(navigation);
    }

    @Override
    public ScreenRectangle getRectangle() {
        return new ScreenRectangle(this.dim.x(), this.dim.y(), this.dim.width(), this.dim.height());
    }

    @Override
    public boolean isMouseOver(double x, double y) {
        return this.dim.containsCursor(x, y);
    }
}