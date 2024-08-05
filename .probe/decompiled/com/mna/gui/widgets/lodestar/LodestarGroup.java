package com.mna.gui.widgets.lodestar;

import com.mna.gui.GuiTextures;
import com.mna.gui.block.GuiLodestarV2;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

public class LodestarGroup extends AbstractWidget {

    private static final int border_thickness = 1;

    private final Minecraft mc;

    private String desiredLabel;

    private String renderedLabel;

    private int labelWidth;

    private boolean labelTruncated = false;

    private final BiConsumer<LodestarGroup, Boolean> clickHandler;

    private double clickX;

    private double clickY;

    private double clickXOffset;

    private double clickYOffset;

    private boolean lowTier;

    public boolean scaling = false;

    public LodestarGroup(int pX, int pY, int pWidth, int pHeight, boolean lowTier, BiConsumer<LodestarGroup, Boolean> clickHandler) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.mc = Minecraft.getInstance();
        this.clickHandler = clickHandler;
        this.lowTier = lowTier;
        this.setLabel("Group");
    }

    @Override
    public void onClick(double mouse_x, double mouse_y) {
        this.clickX = mouse_x;
        this.clickY = mouse_y;
        this.clickXOffset = mouse_x - (double) this.m_252754_();
        this.clickYOffset = mouse_y - (double) this.m_252907_();
        if (this.clickXOffset > (double) (this.f_93618_ - 4) && this.clickYOffset > (double) (this.f_93619_ - 4)) {
            this.scaling = true;
        }
        boolean for_delete = false;
        if (mouse_x >= (double) (this.m_252754_() + this.m_5711_() - 8) && mouse_x <= (double) (this.m_252754_() + this.m_5711_()) && mouse_y >= (double) this.m_252907_() && mouse_y <= (double) (this.m_252907_() + 8)) {
            for_delete = true;
        }
        if (this.clickHandler != null) {
            this.clickHandler.accept(this, for_delete);
        }
    }

    public CompoundTag toCompoundTag(int guiTop, int guiLeft) {
        CompoundTag output = new CompoundTag();
        output.putInt("x", this.m_252754_() - guiLeft);
        output.putInt("y", this.m_252907_() - guiTop);
        output.putInt("w", this.m_5711_());
        output.putInt("h", this.m_93694_());
        output.putString("l", this.desiredLabel);
        return output;
    }

    public static LodestarGroup fromCompound(CompoundTag tag, int guiTop, int guiLeft, boolean lowTier, BiConsumer<LodestarGroup, Boolean> clickHandler) {
        if (tag.contains("x") && tag.contains("y") && tag.contains("w") && tag.contains("h") && tag.contains("l")) {
            int x = tag.getInt("x") + guiLeft;
            int y = tag.getInt("y") + guiTop;
            int w = tag.getInt("w");
            int h = tag.getInt("h");
            String l = tag.getString("l");
            LodestarGroup group = new LodestarGroup(x, y, w, h, lowTier, clickHandler);
            group.setLabel(l);
            return group;
        } else {
            return null;
        }
    }

    public void setLabel(String newLabel) {
        this.desiredLabel = newLabel;
        this.labelTruncated = false;
        int maxLabelWidth = this.f_93618_ - 4;
        for (this.labelWidth = this.mc.font.width(newLabel); this.labelWidth > maxLabelWidth && newLabel.length() >= 5; this.labelTruncated = true) {
            newLabel = newLabel.substring(0, newLabel.length() - 4) + "...";
            this.labelWidth = this.mc.font.width(newLabel);
        }
        this.renderedLabel = newLabel;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.enableBlend();
        pGuiGraphics.fill(this.m_252754_() - 1, this.m_252907_() - 1, this.m_252754_() + this.m_5711_() + 1, this.m_252907_() + this.m_93694_() + 1, this.lowTier ? -14477819 : -14935012);
        pGuiGraphics.fill(this.m_252754_() - 1, this.m_252907_() - 9 - 1 - 1, this.m_252754_() + this.labelWidth + 1 + 2, this.m_252907_(), this.lowTier ? -14477819 : -14935012);
        pGuiGraphics.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.m_5711_(), this.m_252907_() + this.m_93694_(), this.lowTier ? -6780822 : -14013910);
        pGuiGraphics.fill(this.m_252754_() - 1 + 1, this.m_252907_() - 9 - 1, this.m_252754_() + this.labelWidth + 1 + 1, this.m_252907_(), this.lowTier ? -6780822 : -14013910);
        pGuiGraphics.drawString(this.mc.font, this.renderedLabel, this.m_252754_() + 1, this.m_252907_() - 9, -1);
        if (this.labelTruncated && this.isMouseOverLabel((double) ((float) pMouseX / GuiLodestarV2.Zoom), (double) ((float) pMouseY / GuiLodestarV2.Zoom))) {
            this.mc.screen.setTooltipForNextRenderPass(Tooltip.splitTooltip(this.mc, Component.literal(this.desiredLabel)));
        }
        if (this.m_5953_((double) ((float) pMouseX / GuiLodestarV2.Zoom), (double) ((float) pMouseY / GuiLodestarV2.Zoom))) {
            pGuiGraphics.blit(GuiTextures.Blocks.LODESTAR_MAIN, this.m_252754_() + this.m_5711_() - 8, this.m_252907_(), 0, 224, 8, 8);
            pGuiGraphics.fill(this.m_252754_() + this.m_5711_() - 4, this.m_252907_() + this.m_93694_() - 4, this.m_252754_() + this.m_5711_(), this.m_252907_() + this.m_93694_(), -14477819);
        }
    }

    public boolean isMouseOverLabel(double pMouseX, double pMouseY) {
        return this.f_93623_ && this.f_93624_ && pMouseX >= (double) this.m_252754_() && pMouseX <= (double) (this.m_252754_() + this.labelWidth) && pMouseY >= (double) (this.m_252907_() - 9) && pMouseY <= (double) this.m_252907_();
    }

    @Override
    protected boolean clicked(double pMouseX, double pMouseY) {
        return super.clicked(pMouseX, pMouseY) || this.isMouseOverLabel(pMouseX, pMouseY);
    }

    public void resize(int width, int height) {
        this.f_93618_ = Math.max(width, 50);
        this.f_93619_ = Math.max(height, 50);
        this.setLabel(this.desiredLabel);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
    }

    public double getClickX() {
        return this.clickX;
    }

    public double getClickY() {
        return this.clickY;
    }

    public double getClickXOffset() {
        return this.clickXOffset;
    }

    public double getClickYOffset() {
        return this.clickYOffset;
    }

    public String getLabel() {
        return this.desiredLabel;
    }
}