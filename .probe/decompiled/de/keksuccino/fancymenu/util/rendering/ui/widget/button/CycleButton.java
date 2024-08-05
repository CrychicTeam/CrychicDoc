package de.keksuccino.fancymenu.util.rendering.ui.widget.button;

import de.keksuccino.fancymenu.util.cycle.ILocalizedValueCycle;
import java.util.Objects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class CycleButton<T> extends ExtendedButton {

    protected final ILocalizedValueCycle<T> cycle;

    protected final CycleButton.CycleButtonClickFeedback<T> clickFeedback;

    public CycleButton(int x, int y, int width, int height, @NotNull ILocalizedValueCycle<T> cycle, @NotNull CycleButton.CycleButtonClickFeedback<T> clickFeedback) {
        super(x, y, width, height, Component.empty(), var1x -> {
        });
        this.cycle = cycle;
        this.clickFeedback = clickFeedback;
        this.setPressAction(var1x -> this.click());
    }

    public void click() {
        this.cycle.next();
        this.clickFeedback.onClick(this.cycle.current(), this);
    }

    @NotNull
    public T getSelectedValue() {
        return this.cycle.current();
    }

    public CycleButton<T> setSelectedValue(@NotNull T value) {
        this.cycle.setCurrentValue((T) Objects.requireNonNull(value));
        return this;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.setLabel(this.cycle.getCycleComponent());
        super.render(graphics, mouseX, mouseY, partial);
    }

    @FunctionalInterface
    public interface CycleButtonClickFeedback<T> {

        void onClick(T var1, CycleButton<T> var2);
    }
}