package mezz.jei.common.gui.elements;

import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.common.util.TickTimer;
import net.minecraft.client.gui.GuiGraphics;

public class DrawableAnimated implements IDrawableAnimated {

    private final IDrawableStatic drawable;

    private final ITickTimer tickTimer;

    private final IDrawableAnimated.StartDirection startDirection;

    private static IDrawableAnimated.StartDirection invert(IDrawableAnimated.StartDirection startDirection) {
        return switch(startDirection) {
            case TOP ->
                IDrawableAnimated.StartDirection.BOTTOM;
            case BOTTOM ->
                IDrawableAnimated.StartDirection.TOP;
            case LEFT ->
                IDrawableAnimated.StartDirection.RIGHT;
            case RIGHT ->
                IDrawableAnimated.StartDirection.LEFT;
        };
    }

    public DrawableAnimated(IDrawableStatic drawable, int ticksPerCycle, IDrawableAnimated.StartDirection startDirection, boolean inverted) {
        IDrawableAnimated.StartDirection animationStartDirection;
        if (inverted) {
            animationStartDirection = invert(startDirection);
        } else {
            animationStartDirection = startDirection;
        }
        int tickTimerMaxValue;
        if (animationStartDirection != IDrawableAnimated.StartDirection.TOP && animationStartDirection != IDrawableAnimated.StartDirection.BOTTOM) {
            tickTimerMaxValue = drawable.getWidth();
        } else {
            tickTimerMaxValue = drawable.getHeight();
        }
        this.drawable = drawable;
        this.tickTimer = new TickTimer(ticksPerCycle, tickTimerMaxValue, !inverted);
        this.startDirection = animationStartDirection;
    }

    public DrawableAnimated(IDrawableStatic drawable, ITickTimer tickTimer, IDrawableAnimated.StartDirection startDirection) {
        this.drawable = drawable;
        this.tickTimer = tickTimer;
        this.startDirection = startDirection;
    }

    @Override
    public int getWidth() {
        return this.drawable.getWidth();
    }

    @Override
    public int getHeight() {
        return this.drawable.getHeight();
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        int maskLeft = 0;
        int maskRight = 0;
        int maskTop = 0;
        int maskBottom = 0;
        int animationValue = this.tickTimer.getValue();
        switch(this.startDirection) {
            case TOP:
                maskBottom = animationValue;
                break;
            case BOTTOM:
                maskTop = animationValue;
                break;
            case LEFT:
                maskRight = animationValue;
                break;
            case RIGHT:
                maskLeft = animationValue;
                break;
            default:
                throw new IllegalStateException("Unknown startDirection " + this.startDirection);
        }
        this.drawable.draw(guiGraphics, xOffset, yOffset, maskTop, maskBottom, maskLeft, maskRight);
    }
}