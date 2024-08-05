package dev.architectury.impl;

import dev.architectury.event.events.client.ClientTooltipEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class TooltipEventColorContextImpl implements ClientTooltipEvent.ColorContext {

    public static final ThreadLocal<TooltipEventColorContextImpl> CONTEXT = ThreadLocal.withInitial(TooltipEventColorContextImpl::new);

    private int backgroundColor;

    private int outlineGradientTopColor;

    private int outlineGradientBottomColor;

    public TooltipEventColorContextImpl reset() {
        this.backgroundColor = -267386864;
        this.outlineGradientTopColor = 1347420415;
        this.outlineGradientBottomColor = 1344798847;
        return this;
    }

    @Override
    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    @Override
    public int getOutlineGradientTopColor() {
        return this.outlineGradientTopColor;
    }

    @Override
    public void setOutlineGradientTopColor(int color) {
        this.outlineGradientTopColor = color;
    }

    @Override
    public int getOutlineGradientBottomColor() {
        return this.outlineGradientBottomColor;
    }

    @Override
    public void setOutlineGradientBottomColor(int color) {
        this.outlineGradientBottomColor = color;
    }
}