package com.simibubi.create.foundation.gui.element;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.opengl.GL11;

public abstract class StencilElement extends RenderElement {

    @Override
    public void render(GuiGraphics graphics) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        this.transform(ms);
        this.prepareStencil(ms);
        this.renderStencil(graphics);
        this.prepareElement(ms);
        this.renderElement(graphics);
        this.cleanUp(ms);
        ms.popPose();
    }

    protected abstract void renderStencil(GuiGraphics var1);

    protected abstract void renderElement(GuiGraphics var1);

    protected void transform(PoseStack ms) {
        ms.translate(this.x, this.y, this.z);
    }

    protected void prepareStencil(PoseStack ms) {
        GL11.glDisable(2960);
        RenderSystem.stencilMask(-1);
        RenderSystem.clear(1024, Minecraft.ON_OSX);
        GL11.glEnable(2960);
        RenderSystem.stencilOp(7681, 7680, 7680);
        RenderSystem.stencilMask(255);
        RenderSystem.stencilFunc(512, 1, 255);
    }

    protected void prepareElement(PoseStack ms) {
        GL11.glEnable(2960);
        RenderSystem.stencilOp(7680, 7680, 7680);
        RenderSystem.stencilFunc(514, 1, 255);
    }

    protected void cleanUp(PoseStack ms) {
        GL11.glDisable(2960);
    }
}