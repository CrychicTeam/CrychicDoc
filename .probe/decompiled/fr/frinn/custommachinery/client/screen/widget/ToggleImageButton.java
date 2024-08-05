package fr.frinn.custommachinery.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ToggleImageButton extends ImageButton {

    private boolean toggle = false;

    public ToggleImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, ResourceLocation resourceLocation, Button.OnPress onPress) {
        super(x, y, width, height, xTexStart, yTexStart, resourceLocation, onPress);
    }

    public ToggleImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex, ResourceLocation resourceLocation, Button.OnPress onPress) {
        super(x, y, width, height, xTexStart, yTexStart, yDiffTex, resourceLocation, onPress);
    }

    public ToggleImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex, ResourceLocation resourceLocation, int textureWidth, int textureHeight, Button.OnPress onPress) {
        super(x, y, width, height, xTexStart, yTexStart, yDiffTex, resourceLocation, textureWidth, textureHeight, onPress);
    }

    public ToggleImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex, ResourceLocation resourceLocation, int textureWidth, int textureHeight, Button.OnPress onPress, Component message) {
        super(x, y, width, height, xTexStart, yTexStart, yDiffTex, resourceLocation, textureWidth, textureHeight, onPress, message);
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.m_5716_(mouseX, mouseY);
        this.toggle = !this.toggle;
    }

    @Override
    public void renderTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int uOffset, int vOffset, int textureDifference, int width, int height, int textureWidth, int textureHeight) {
        int i = vOffset;
        if (!this.m_142518_()) {
            i = vOffset + textureDifference * 2;
        } else if (this.m_274382_() || this.toggle) {
            i = vOffset + textureDifference;
        }
        RenderSystem.enableDepthTest();
        guiGraphics.blit(texture, x, y, (float) uOffset, (float) i, width, height, textureWidth, textureHeight);
    }
}