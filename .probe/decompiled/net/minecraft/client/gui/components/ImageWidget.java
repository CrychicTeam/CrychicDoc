package net.minecraft.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ImageWidget extends AbstractWidget {

    private final ResourceLocation imageLocation;

    public ImageWidget(int int0, int int1, ResourceLocation resourceLocation2) {
        this(0, 0, int0, int1, resourceLocation2);
    }

    public ImageWidget(int int0, int int1, int int2, int int3, ResourceLocation resourceLocation4) {
        super(int0, int1, int2, int3, Component.empty());
        this.imageLocation = resourceLocation4;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        int $$4 = this.m_5711_();
        int $$5 = this.m_93694_();
        guiGraphics0.blit(this.imageLocation, this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, $$4, $$5, $$4, $$5);
    }
}