package net.minecraft.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ImageButton extends Button {

    protected final ResourceLocation resourceLocation;

    protected final int xTexStart;

    protected final int yTexStart;

    protected final int yDiffTex;

    protected final int textureWidth;

    protected final int textureHeight;

    public ImageButton(int int0, int int1, int int2, int int3, int int4, int int5, ResourceLocation resourceLocation6, Button.OnPress buttonOnPress7) {
        this(int0, int1, int2, int3, int4, int5, int3, resourceLocation6, 256, 256, buttonOnPress7);
    }

    public ImageButton(int int0, int int1, int int2, int int3, int int4, int int5, int int6, ResourceLocation resourceLocation7, Button.OnPress buttonOnPress8) {
        this(int0, int1, int2, int3, int4, int5, int6, resourceLocation7, 256, 256, buttonOnPress8);
    }

    public ImageButton(int int0, int int1, int int2, int int3, int int4, int int5, int int6, ResourceLocation resourceLocation7, int int8, int int9, Button.OnPress buttonOnPress10) {
        this(int0, int1, int2, int3, int4, int5, int6, resourceLocation7, int8, int9, buttonOnPress10, CommonComponents.EMPTY);
    }

    public ImageButton(int int0, int int1, int int2, int int3, int int4, int int5, int int6, ResourceLocation resourceLocation7, int int8, int int9, Button.OnPress buttonOnPress10, Component component11) {
        super(int0, int1, int2, int3, component11, buttonOnPress10, f_252438_);
        this.textureWidth = int8;
        this.textureHeight = int9;
        this.xTexStart = int4;
        this.yTexStart = int5;
        this.yDiffTex = int6;
        this.resourceLocation = resourceLocation7;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280322_(guiGraphics0, this.resourceLocation, this.m_252754_(), this.m_252907_(), this.xTexStart, this.yTexStart, this.yDiffTex, this.f_93618_, this.f_93619_, this.textureWidth, this.textureHeight);
    }
}