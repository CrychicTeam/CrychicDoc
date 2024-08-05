package eu.midnightdust.lib.util.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TexturedOverlayButtonWidget extends ImageButton {

    public TexturedOverlayButtonWidget(int x, int y, int width, int height, int u, int v, ResourceLocation texture, Button.OnPress pressAction) {
        super(x, y, width, height, u, v, texture, pressAction);
    }

    public TexturedOverlayButtonWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation texture, Button.OnPress pressAction) {
        super(x, y, width, height, u, v, hoveredVOffset, texture, pressAction);
    }

    public TexturedOverlayButtonWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation texture, int textureWidth, int textureHeight, Button.OnPress pressAction) {
        super(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction);
    }

    public TexturedOverlayButtonWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation texture, int textureWidth, int textureHeight, Button.OnPress pressAction, Component text) {
        super(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction, text);
    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        int i = 66;
        if (!this.m_142518_()) {
            i += this.f_94226_ * 2;
        } else if (this.m_198029_()) {
            i += this.f_94226_;
        }
        context.blitNineSliced(f_93617_, this.m_252754_(), this.m_252907_(), this.f_93618_, this.f_93619_, 4, 200, 20, 0, i);
        super.renderWidget(context, mouseX, mouseY, delta);
    }
}