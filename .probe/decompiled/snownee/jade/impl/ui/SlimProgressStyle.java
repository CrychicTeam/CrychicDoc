package snownee.jade.impl.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IProgressStyle;
import snownee.jade.overlay.DisplayHelper;

public class SlimProgressStyle implements IProgressStyle {

    public int color;

    @Nullable
    public IElement overlay;

    @Override
    public IProgressStyle color(int color, int color2) {
        if (color != color2) {
            throw new UnsupportedOperationException();
        } else {
            this.color = color;
            return this;
        }
    }

    @Override
    public IProgressStyle textColor(int color) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IProgressStyle vertical(boolean vertical) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IProgressStyle overlay(IElement overlay) {
        this.overlay = overlay;
        return this;
    }

    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float w, float h, float progress, Component text) {
        if (this.overlay != null) {
            Vec2 size = new Vec2(w * progress, h);
            this.overlay.size(size);
            this.overlay.render(guiGraphics, x, y - 1.0F, size.x, size.y);
        } else {
            DisplayHelper.INSTANCE.drawGradientProgress(guiGraphics, x, y - 1.0F, w, h, progress, this.color);
        }
    }
}