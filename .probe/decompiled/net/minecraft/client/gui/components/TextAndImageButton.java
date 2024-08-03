package net.minecraft.client.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TextAndImageButton extends Button {

    protected final ResourceLocation resourceLocation;

    protected final int xTexStart;

    protected final int yTexStart;

    protected final int yDiffTex;

    protected final int textureWidth;

    protected final int textureHeight;

    private final int xOffset;

    private final int yOffset;

    private final int usedTextureWidth;

    private final int usedTextureHeight;

    TextAndImageButton(Component component0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, int int8, int int9, ResourceLocation resourceLocation10, Button.OnPress buttonOnPress11) {
        super(0, 0, 150, 20, component0, buttonOnPress11, f_252438_);
        this.textureWidth = int8;
        this.textureHeight = int9;
        this.xTexStart = int1;
        this.yTexStart = int2;
        this.yDiffTex = int5;
        this.resourceLocation = resourceLocation10;
        this.xOffset = int3;
        this.yOffset = int4;
        this.usedTextureWidth = int6;
        this.usedTextureHeight = int7;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        super.m_87963_(guiGraphics0, int1, int2, float3);
        this.m_280322_(guiGraphics0, this.resourceLocation, this.getXOffset(), this.getYOffset(), this.xTexStart, this.yTexStart, this.yDiffTex, this.usedTextureWidth, this.usedTextureHeight, this.textureWidth, this.textureHeight);
    }

    @Override
    public void renderString(GuiGraphics guiGraphics0, Font font1, int int2) {
        int $$3 = this.m_252754_() + 2;
        int $$4 = this.m_252754_() + this.m_5711_() - this.usedTextureWidth - 6;
        m_280138_(guiGraphics0, font1, this.m_6035_(), $$3, this.m_252907_(), $$4, this.m_252907_() + this.m_93694_(), int2);
    }

    private int getXOffset() {
        return this.m_252754_() + (this.f_93618_ / 2 - this.usedTextureWidth / 2) + this.xOffset;
    }

    private int getYOffset() {
        return this.m_252907_() + this.yOffset;
    }

    public static TextAndImageButton.Builder builder(Component component0, ResourceLocation resourceLocation1, Button.OnPress buttonOnPress2) {
        return new TextAndImageButton.Builder(component0, resourceLocation1, buttonOnPress2);
    }

    public static class Builder {

        private final Component message;

        private final ResourceLocation resourceLocation;

        private final Button.OnPress onPress;

        private int xTexStart;

        private int yTexStart;

        private int yDiffTex;

        private int usedTextureWidth;

        private int usedTextureHeight;

        private int textureWidth;

        private int textureHeight;

        private int xOffset;

        private int yOffset;

        public Builder(Component component0, ResourceLocation resourceLocation1, Button.OnPress buttonOnPress2) {
            this.message = component0;
            this.resourceLocation = resourceLocation1;
            this.onPress = buttonOnPress2;
        }

        public TextAndImageButton.Builder texStart(int int0, int int1) {
            this.xTexStart = int0;
            this.yTexStart = int1;
            return this;
        }

        public TextAndImageButton.Builder offset(int int0, int int1) {
            this.xOffset = int0;
            this.yOffset = int1;
            return this;
        }

        public TextAndImageButton.Builder yDiffTex(int int0) {
            this.yDiffTex = int0;
            return this;
        }

        public TextAndImageButton.Builder usedTextureSize(int int0, int int1) {
            this.usedTextureWidth = int0;
            this.usedTextureHeight = int1;
            return this;
        }

        public TextAndImageButton.Builder textureSize(int int0, int int1) {
            this.textureWidth = int0;
            this.textureHeight = int1;
            return this;
        }

        public TextAndImageButton build() {
            return new TextAndImageButton(this.message, this.xTexStart, this.yTexStart, this.xOffset, this.yOffset, this.yDiffTex, this.usedTextureWidth, this.usedTextureHeight, this.textureWidth, this.textureHeight, this.resourceLocation, this.onPress);
        }
    }
}