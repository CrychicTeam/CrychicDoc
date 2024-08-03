package com.mna.gui.widgets.guide;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ImageWidget extends AbstractWidget {

    private final ResourceLocation imageTextureLocation;

    private final Consumer<List<Component>> tooltipFunction;

    private final Component caption;

    private final List<Component> tooltip;

    public ImageWidget(int pX, int pY, int pWidth, int pHeight, ResourceLocation imageTextureLocation, List<Component> tooltip, Consumer<List<Component>> tooltipFunction) {
        this(pX, pY, pWidth, pHeight, imageTextureLocation, null, tooltip, tooltipFunction);
    }

    public ImageWidget(int pX, int pY, int pWidth, int pHeight, ResourceLocation imageTextureLocation, Component caption, List<Component> tooltip, Consumer<List<Component>> tooltipFunction) {
        super(pX, pY, pWidth, pHeight, Component.literal(""));
        this.imageTextureLocation = imageTextureLocation;
        this.caption = caption;
        this.tooltip = tooltip;
        this.tooltipFunction = tooltipFunction;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.blit(this.imageTextureLocation, this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
        if (this.caption != null) {
            Minecraft mc = Minecraft.getInstance();
            pGuiGraphics.fillGradient(this.m_252754_(), this.m_252907_() + this.f_93619_ - 9, this.m_252754_() + this.f_93618_, 9, 11141120, 11141120);
            pGuiGraphics.drawString(mc.font, this.caption, this.m_252754_(), this.m_252907_() + this.f_93619_ - 9, 16777215, false);
        }
        if (this.f_93622_ && this.tooltipFunction != null) {
            this.tooltipFunction.accept(this.tooltip);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
    }
}