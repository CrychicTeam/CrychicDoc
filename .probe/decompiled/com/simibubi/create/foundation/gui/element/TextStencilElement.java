package com.simibubi.create.foundation.gui.element;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;

public class TextStencilElement extends DelegatedStencilElement {

    protected Font font;

    protected MutableComponent component;

    protected boolean centerVertically = false;

    protected boolean centerHorizontally = false;

    public TextStencilElement(Font font) {
        this.font = font;
        this.height = 10;
    }

    public TextStencilElement(Font font, String text) {
        this(font);
        this.component = Components.literal(text);
    }

    public TextStencilElement(Font font, MutableComponent component) {
        this(font);
        this.component = component;
    }

    public TextStencilElement withText(String text) {
        this.component = Components.literal(text);
        return this;
    }

    public TextStencilElement withText(MutableComponent component) {
        this.component = component;
        return this;
    }

    public TextStencilElement centered(boolean vertical, boolean horizontal) {
        this.centerVertically = vertical;
        this.centerHorizontally = horizontal;
        return this;
    }

    @Override
    protected void renderStencil(GuiGraphics graphics) {
        float x = 0.0F;
        float y = 0.0F;
        if (this.centerHorizontally) {
            x = (float) this.width / 2.0F - (float) this.font.width(this.component) / 2.0F;
        }
        if (this.centerVertically) {
            y = (float) this.height / 2.0F - (float) (9 - 1) / 2.0F;
        }
        graphics.drawString(this.font, this.component, Math.round(x), Math.round(y), -16777216, false);
    }

    @Override
    protected void renderElement(GuiGraphics graphics) {
        float x = 0.0F;
        float y = 0.0F;
        if (this.centerHorizontally) {
            x = (float) this.width / 2.0F - (float) this.font.width(this.component) / 2.0F;
        }
        if (this.centerVertically) {
            y = (float) this.height / 2.0F - (float) (9 - 1) / 2.0F;
        }
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(x, y, 0.0F);
        this.element.render(graphics, this.font.width(this.component), 9 + 2, this.alpha);
        ms.popPose();
    }

    public MutableComponent getComponent() {
        return this.component;
    }
}