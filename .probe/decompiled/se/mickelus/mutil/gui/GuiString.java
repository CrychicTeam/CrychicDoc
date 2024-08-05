package se.mickelus.mutil.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

public class GuiString extends GuiElement {

    protected String string;

    protected Font fontRenderer;

    protected int color = -1;

    protected boolean drawShadow = true;

    protected boolean fixedWidth = false;

    public GuiString(int x, int y, String string) {
        super(x, y, 0, 9);
        this.fontRenderer = Minecraft.getInstance().font;
        this.string = string;
        this.width = this.fontRenderer.width(string);
    }

    public GuiString(int x, int y, int width, String string) {
        super(x, y, width, 9);
        this.fixedWidth = true;
        this.fontRenderer = Minecraft.getInstance().font;
        this.string = this.fontRenderer.plainSubstrByWidth(string, width);
    }

    public GuiString(int x, int y, String string, GuiAttachment attachment) {
        this(x, y, string);
        this.attachmentPoint = attachment;
    }

    public GuiString(int x, int y, String string, int color) {
        this(x, y, string);
        this.color = color;
    }

    public GuiString(int x, int y, String string, int color, GuiAttachment attachment) {
        this(x, y, string, attachment);
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setString(String string) {
        if (string != null && !string.equals(this.string)) {
            if (this.fixedWidth) {
                this.string = this.fontRenderer.plainSubstrByWidth(string, this.width);
            } else {
                this.string = string;
                this.width = this.fontRenderer.width(string);
            }
        }
    }

    public GuiString setShadow(boolean shadow) {
        this.drawShadow = shadow;
        return this;
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        this.activeAnimations.removeIf(keyframeAnimation -> !keyframeAnimation.isActive());
        this.activeAnimations.forEach(KeyframeAnimation::preDraw);
        RenderSystem.enableBlend();
        this.drawString(graphics, this.string, refX + this.x, refY + this.y, this.color, opacity * this.getOpacity(), this.drawShadow);
    }

    protected void drawString(GuiGraphics graphics, String text, int x, int y, int color, float opacity, boolean drawShadow) {
        color = colorWithOpacity(color, opacity);
        if ((color & -67108864) != 0) {
            graphics.drawString(this.fontRenderer, text, x, y, color, drawShadow);
        }
    }
}