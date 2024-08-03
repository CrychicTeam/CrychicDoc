package com.sihenzhang.crockpot.integration.jei.gui;

import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import net.minecraft.client.gui.GuiGraphics;

public class DrawableFramed implements IDrawableAnimated {

    private final IDrawableStatic drawable;

    private final int width;

    private final int height;

    private final ITickTimer tickTimer;

    private final IDrawableAnimated.StartDirection startDirection;

    public DrawableFramed(IDrawableStatic drawable, int ticksPerCycle, int frames, IDrawableAnimated.StartDirection startDirection) {
        boolean inverted = startDirection == IDrawableAnimated.StartDirection.BOTTOM || startDirection == IDrawableAnimated.StartDirection.RIGHT;
        int tickTimerMaxValue;
        int width;
        int height;
        switch(startDirection) {
            case TOP:
            case BOTTOM:
                tickTimerMaxValue = drawable.getHeight();
                width = drawable.getWidth();
                height = drawable.getHeight() / frames;
                break;
            case LEFT:
            case RIGHT:
                tickTimerMaxValue = drawable.getWidth();
                width = drawable.getWidth() / frames;
                height = drawable.getHeight();
                break;
            default:
                throw new IllegalStateException("Unknown startDirection " + startDirection);
        }
        this.drawable = drawable;
        this.width = width;
        this.height = height;
        this.tickTimer = new StepTickTimer(ticksPerCycle, frames, tickTimerMaxValue, !inverted);
        this.startDirection = startDirection;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        int maskLeft = 0;
        int maskRight = 0;
        int maskTop = 0;
        int maskBottom = 0;
        int animationValue = this.tickTimer.getValue();
        int tickerTimerMaxValue = this.tickTimer.getMaxValue();
        int actualWidth = this.drawable.getWidth();
        int actualHeight = this.drawable.getHeight();
        switch(this.startDirection) {
            case TOP:
                maskTop = tickerTimerMaxValue - animationValue - this.height;
                maskBottom = animationValue + actualHeight - tickerTimerMaxValue;
                break;
            case BOTTOM:
                maskTop = actualHeight - animationValue - this.height;
                maskBottom = animationValue;
                break;
            case LEFT:
                maskLeft = tickerTimerMaxValue - animationValue - this.width;
                maskRight = animationValue + actualWidth - tickerTimerMaxValue;
                break;
            case RIGHT:
                maskLeft = actualWidth - animationValue - this.width;
                maskRight = animationValue;
                break;
            default:
                throw new IllegalStateException("Unknown startDirection " + this.startDirection);
        }
        this.drawable.draw(guiGraphics, xOffset - maskLeft, yOffset - maskTop, maskTop, maskBottom, maskLeft, maskRight);
    }
}