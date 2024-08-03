package com.simibubi.create.foundation.gui.element;

import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.gui.GuiGraphics;

public class DelegatedStencilElement extends StencilElement {

    protected static final DelegatedStencilElement.ElementRenderer EMPTY_RENDERER = (graphics, width, height, alpha) -> {
    };

    protected static final DelegatedStencilElement.ElementRenderer DEFAULT_ELEMENT = (graphics, width, height, alpha) -> UIRenderHelper.angledGradient(graphics, 0.0F, -3, 5, height + 4, width + 6, new Color(-15672048).scaleAlpha(alpha), new Color(-15724323).scaleAlpha(alpha));

    protected DelegatedStencilElement.ElementRenderer stencil;

    protected DelegatedStencilElement.ElementRenderer element;

    public DelegatedStencilElement() {
        this.stencil = EMPTY_RENDERER;
        this.element = DEFAULT_ELEMENT;
    }

    public DelegatedStencilElement(DelegatedStencilElement.ElementRenderer stencil, DelegatedStencilElement.ElementRenderer element) {
        this.stencil = stencil;
        this.element = element;
    }

    public <T extends DelegatedStencilElement> T withStencilRenderer(DelegatedStencilElement.ElementRenderer renderer) {
        this.stencil = renderer;
        return (T) this;
    }

    public <T extends DelegatedStencilElement> T withElementRenderer(DelegatedStencilElement.ElementRenderer renderer) {
        this.element = renderer;
        return (T) this;
    }

    @Override
    protected void renderStencil(GuiGraphics graphics) {
        this.stencil.render(graphics, this.width, this.height, 1.0F);
    }

    @Override
    protected void renderElement(GuiGraphics graphics) {
        this.element.render(graphics, this.width, this.height, this.alpha);
    }

    @FunctionalInterface
    public interface ElementRenderer {

        void render(GuiGraphics var1, int var2, int var3, float var4);
    }
}