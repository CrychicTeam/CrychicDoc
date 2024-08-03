package com.simibubi.create.foundation.gui.element;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.GuiGraphics;

public class CombinedStencilElement extends StencilElement {

    private StencilElement element1;

    private StencilElement element2;

    private CombinedStencilElement.ElementMode mode;

    private CombinedStencilElement() {
    }

    public static CombinedStencilElement of(@Nonnull StencilElement element1, @Nonnull StencilElement element2) {
        return of(element1, element2, CombinedStencilElement.ElementMode.FIRST);
    }

    public static CombinedStencilElement of(@Nonnull StencilElement element1, @Nonnull StencilElement element2, CombinedStencilElement.ElementMode mode) {
        CombinedStencilElement e = new CombinedStencilElement();
        e.element1 = element1;
        e.element2 = element2;
        e.mode = mode;
        return e;
    }

    public <T extends CombinedStencilElement> T withFirst(StencilElement element) {
        this.element1 = element;
        return (T) this;
    }

    public <T extends CombinedStencilElement> T withSecond(StencilElement element) {
        this.element2 = element;
        return (T) this;
    }

    public <T extends CombinedStencilElement> T withMode(CombinedStencilElement.ElementMode mode) {
        this.mode = mode;
        return (T) this;
    }

    @Override
    protected void renderStencil(GuiGraphics graphics) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        this.element1.transform(ms);
        this.element1.withBounds(this.width, this.height);
        this.element1.renderStencil(graphics);
        ms.popPose();
        ms.pushPose();
        this.element2.transform(ms);
        this.element2.withBounds(this.width, this.height);
        this.element2.renderStencil(graphics);
        ms.popPose();
    }

    @Override
    protected void renderElement(GuiGraphics graphics) {
        if (this.mode.rendersFirst()) {
            this.element1.<StencilElement>withBounds(this.width, this.height).renderElement(graphics);
        }
        if (this.mode.rendersSecond()) {
            this.element2.<StencilElement>withBounds(this.width, this.height).renderElement(graphics);
        }
    }

    public static enum ElementMode {

        FIRST, SECOND, BOTH;

        boolean rendersFirst() {
            return this == FIRST || this == BOTH;
        }

        boolean rendersSecond() {
            return this == SECOND || this == BOTH;
        }
    }
}