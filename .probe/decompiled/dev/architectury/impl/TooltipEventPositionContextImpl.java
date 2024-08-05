package dev.architectury.impl;

import dev.architectury.event.events.client.ClientTooltipEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class TooltipEventPositionContextImpl implements ClientTooltipEvent.PositionContext {

    private int tooltipX;

    private int tooltipY;

    public TooltipEventPositionContextImpl reset(int tooltipX, int tooltipY) {
        this.tooltipX = tooltipX;
        this.tooltipY = tooltipY;
        return this;
    }

    @Override
    public int getTooltipX() {
        return this.tooltipX;
    }

    @Override
    public void setTooltipX(int x) {
        this.tooltipX = x;
    }

    @Override
    public int getTooltipY() {
        return this.tooltipY;
    }

    @Override
    public void setTooltipY(int y) {
        this.tooltipY = y;
    }
}