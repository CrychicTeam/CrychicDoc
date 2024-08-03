package com.simibubi.create.foundation.gui.widget;

import com.simibubi.create.AllKeys;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ScrollInput extends AbstractSimiWidget {

    protected Consumer<Integer> onScroll;

    protected int state;

    protected Component title = Lang.translateDirect("gui.scrollInput.defaultTitle");

    protected final Component scrollToModify = Lang.translateDirect("gui.scrollInput.scrollToModify");

    protected final Component shiftScrollsFaster = Lang.translateDirect("gui.scrollInput.shiftScrollsFaster");

    protected Component hint = null;

    protected Label displayLabel;

    protected boolean inverted;

    protected boolean soundPlayed;

    protected Function<Integer, Component> formatter;

    protected int min;

    protected int max;

    protected int shiftStep;

    Function<ScrollValueBehaviour.StepContext, Integer> step;

    public ScrollInput(int xIn, int yIn, int widthIn, int heightIn) {
        super(xIn, yIn, widthIn, heightIn);
        this.state = 0;
        this.min = 0;
        this.max = 1;
        this.shiftStep = 5;
        this.step = this.standardStep();
        this.formatter = i -> Components.literal(String.valueOf(i));
        this.soundPlayed = false;
    }

    public Function<ScrollValueBehaviour.StepContext, Integer> standardStep() {
        return c -> c.shift ? this.shiftStep : 1;
    }

    public ScrollInput inverted() {
        this.inverted = true;
        return this;
    }

    public ScrollInput withRange(int min, int max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public ScrollInput calling(Consumer<Integer> onScroll) {
        this.onScroll = onScroll;
        return this;
    }

    public ScrollInput format(Function<Integer, Component> formatter) {
        this.formatter = formatter;
        return this;
    }

    public ScrollInput removeCallback() {
        this.onScroll = null;
        return this;
    }

    public ScrollInput titled(MutableComponent title) {
        this.title = title;
        this.updateTooltip();
        return this;
    }

    public ScrollInput addHint(MutableComponent hint) {
        this.hint = hint;
        this.updateTooltip();
        return this;
    }

    public ScrollInput withStepFunction(Function<ScrollValueBehaviour.StepContext, Integer> step) {
        this.step = step;
        return this;
    }

    public ScrollInput writingTo(Label label) {
        this.displayLabel = label;
        if (label != null) {
            this.writeToLabel();
        }
        return this;
    }

    @Override
    public void tick() {
        super.tick();
        this.soundPlayed = false;
    }

    public int getState() {
        return this.state;
    }

    public ScrollInput setState(int state) {
        this.state = state;
        this.clampState();
        this.updateTooltip();
        if (this.displayLabel != null) {
            this.writeToLabel();
        }
        return this;
    }

    public ScrollInput withShiftStep(int step) {
        this.shiftStep = step;
        return this;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.inverted) {
            delta *= -1.0;
        }
        ScrollValueBehaviour.StepContext context = new ScrollValueBehaviour.StepContext();
        context.control = AllKeys.ctrlDown();
        context.shift = AllKeys.shiftDown();
        context.currentValue = this.state;
        context.forward = delta > 0.0;
        int priorState = this.state;
        boolean shifted = AllKeys.shiftDown();
        int step = (int) Math.signum(delta) * (Integer) this.step.apply(context);
        this.state += step;
        if (shifted) {
            this.state = this.state - this.state % this.shiftStep;
        }
        this.clampState();
        if (priorState != this.state) {
            if (!this.soundPlayed) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(AllSoundEvents.SCROLL_VALUE.getMainEvent(), 1.5F + 0.1F * (float) (this.state - this.min) / (float) (this.max - this.min)));
            }
            this.soundPlayed = true;
            this.onChanged();
        }
        return priorState != this.state;
    }

    protected void clampState() {
        if (this.state >= this.max) {
            this.state = this.max - 1;
        }
        if (this.state < this.min) {
            this.state = this.min;
        }
    }

    public void onChanged() {
        if (this.displayLabel != null) {
            this.writeToLabel();
        }
        if (this.onScroll != null) {
            this.onScroll.accept(this.state);
        }
        this.updateTooltip();
    }

    protected void writeToLabel() {
        this.displayLabel.text = (Component) this.formatter.apply(this.state);
    }

    @Override
    protected void updateTooltip() {
        this.toolTip.clear();
        if (this.title != null) {
            this.toolTip.add(this.title.plainCopy().withStyle(s -> s.withColor(5476833)));
            if (this.hint != null) {
                this.toolTip.add(this.hint.plainCopy().withStyle(s -> s.withColor(9877472)));
            }
            this.toolTip.add(this.scrollToModify.plainCopy().withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY));
            this.toolTip.add(this.shiftScrollsFaster.plainCopy().withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY));
        }
    }
}