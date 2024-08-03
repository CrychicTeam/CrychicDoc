package se.mickelus.mutil.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiTexture extends GuiElement {

    protected ResourceLocation textureLocation;

    protected int textureWidth = 256;

    protected int textureHeight = 256;

    protected int textureX;

    protected int textureY;

    protected int color = 16777215;

    private boolean useDefaultBlending = true;

    public GuiTexture(int x, int y, int width, int height, ResourceLocation textureLocation) {
        this(x, y, width, height, 0, 0, textureLocation);
    }

    public GuiTexture(int x, int y, int width, int height, int textureX, int textureY, ResourceLocation textureLocation) {
        super(x, y, width, height);
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureLocation = textureLocation;
    }

    public GuiTexture setTextureCoordinates(int x, int y) {
        this.textureX = x;
        this.textureY = y;
        return this;
    }

    public GuiTexture setColor(int color) {
        this.color = color;
        return this;
    }

    public GuiTexture setSpriteSize(int width, int height) {
        this.textureWidth = width;
        this.textureHeight = height;
        return this;
    }

    public GuiTexture setUseDefaultBlending(boolean useDefault) {
        this.useDefaultBlending = useDefault;
        return this;
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        this.drawTexture(graphics, this.textureLocation, refX + this.x, refY + this.y, this.width, this.height, this.textureX, this.textureY, this.color, this.getOpacity() * opacity);
    }

    protected void drawTexture(GuiGraphics graphics, ResourceLocation textureLocation, int x, int y, int width, int height, int u, int v, int color, float opacity) {
        graphics.innerBlit(textureLocation, x, x + width, y, y + height, 0, (float) u * 1.0F / (float) this.textureWidth, (float) (u + width) * 1.0F / (float) this.textureWidth, (float) v * 1.0F / (float) this.textureHeight, (float) (v + height) * 1.0F / (float) this.textureHeight, (float) (color >> 16 & 0xFF) / 255.0F, (float) (color >> 8 & 0xFF) / 255.0F, (float) (color & 0xFF) / 255.0F, opacity);
    }
}