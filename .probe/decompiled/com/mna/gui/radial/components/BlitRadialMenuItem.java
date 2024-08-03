package com.mna.gui.radial.components;

import com.mna.gui.radial.GenericRadialMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BlitRadialMenuItem extends TextRadialMenuItem {

    private final int slot;

    private final ResourceLocation rLoc;

    private final int u;

    private final int v;

    private final int w;

    private final int h;

    private final int tex_w;

    private final int tex_h;

    public int getSlot() {
        return this.slot;
    }

    public ResourceLocation getTexture() {
        return this.rLoc;
    }

    public BlitRadialMenuItem(GenericRadialMenu owner, int slot, ResourceLocation rLoc, int u, int v, int w, int h, int texW, int texH, Component altText) {
        super(owner, altText, Integer.MAX_VALUE);
        this.slot = slot;
        this.rLoc = rLoc;
        this.u = u;
        this.v = v;
        this.w = w;
        this.h = h;
        this.tex_w = texW;
        this.tex_h = texH;
    }

    @Override
    public void draw(DrawingContext context) {
        context.guiGraphics.pose().pushPose();
        context.guiGraphics.pose().translate(-8.0F, -8.0F, context.z);
        RenderSystem.setShaderTexture(0, this.rLoc);
        context.guiGraphics.blit(this.getTexture(), (int) context.x, (int) context.y, (float) this.u, (float) this.v, this.w, this.h, this.tex_w, this.tex_h);
        context.guiGraphics.pose().popPose();
    }

    @Override
    public void drawTooltips(DrawingContext context) {
        if (this.getText() != null) {
            context.guiGraphics.renderTooltip(context.fontRenderer, this.getText(), (int) context.x, (int) context.y);
        }
    }
}