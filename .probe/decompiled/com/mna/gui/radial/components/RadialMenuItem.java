package com.mna.gui.radial.components;

import com.mna.gui.radial.GenericRadialMenu;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

public abstract class RadialMenuItem {

    private final GenericRadialMenu owner;

    private Component centralText;

    private boolean visible;

    private boolean hovered;

    protected RadialMenuItem(GenericRadialMenu owner) {
        this.owner = owner;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean newVisible) {
        this.visible = newVisible;
        this.owner.visibilityChanged(this);
    }

    @Nullable
    public Component getCentralText() {
        return this.centralText;
    }

    public void setCentralText(@Nullable Component centralText) {
        this.centralText = centralText;
    }

    public boolean isHovered() {
        return this.hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public abstract void draw(DrawingContext var1);

    public abstract void drawTooltips(DrawingContext var1);

    public boolean onClick() {
        return false;
    }
}