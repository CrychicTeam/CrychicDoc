package com.illusivesoulworks.polymorph.api.client.widget;

import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class OutputWidget extends AbstractWidget {

    private final ItemStack output;

    private final ResourceLocation resourceLocation;

    private boolean highlighted = false;

    public OutputWidget(IRecipePair recipePair) {
        super(0, 0, 25, 25, Component.empty());
        this.output = recipePair.getOutput();
        this.resourceLocation = recipePair.getResourceLocation();
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        int j = 0;
        if (this.m_252754_() + 25 > mouseX && this.m_252754_() <= mouseX && this.m_252907_() + 25 > mouseY && this.m_252907_() <= mouseY) {
            j += 25;
        }
        PoseStack poseStack = guiGraphics.pose();
        guiGraphics.blit(AbstractRecipesWidget.WIDGETS, this.m_252754_(), this.m_252907_(), 600, this.highlighted ? 41.0F : 16.0F, (float) j, this.f_93618_, this.f_93619_, 256, 256);
        int k = 4;
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 700.0F);
        guiGraphics.renderItem(this.getOutput(), this.m_252754_() + k, this.m_252907_() + k);
        guiGraphics.renderItemDecorations(minecraft.font, this.getOutput(), this.m_252754_() + k, this.m_252907_() + k);
        poseStack.popPose();
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public int getWidth() {
        return 25;
    }

    @Override
    protected void updateWidgetNarration(@Nonnull NarrationElementOutput var1) {
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0 || button == 1;
    }
}