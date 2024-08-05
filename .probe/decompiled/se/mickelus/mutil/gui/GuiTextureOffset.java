package se.mickelus.mutil.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiTextureOffset extends GuiTexture {

    public GuiTextureOffset(int x, int y, int width, int height, ResourceLocation textureLocation) {
        super(x, y, width + 1, height + 1, textureLocation);
    }

    public GuiTextureOffset(int x, int y, int width, int height, int textureX, int textureY, ResourceLocation textureLocation) {
        super(x, y, width + 1, height + 1, textureX, textureY, textureLocation);
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        this.drawChildren(graphics, refX + this.x, refY + this.y, screenWidth, screenHeight, mouseX, mouseY, opacity * this.opacity);
        graphics.pose().pushPose();
        graphics.pose().translate(0.5F, 0.5F, 0.0F);
        this.drawTexture(graphics, this.textureLocation, refX + this.x, refY + this.y, this.width - 1, this.height - 1, this.textureX, this.textureY, this.color, this.getOpacity() * opacity);
        graphics.pose().popPose();
    }
}