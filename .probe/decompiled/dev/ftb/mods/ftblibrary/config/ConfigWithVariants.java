package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;

public abstract class ConfigWithVariants<T> extends ConfigValue<T> {

    public abstract T getIteration(T var1, boolean var2);

    @Override
    public void onClicked(Widget clickedWidget, MouseButton button, ConfigCallback callback) {
        if (this.value != null && this.getCanEdit()) {
            boolean changed = this.setCurrentValue(this.getIteration(this.value, button.isLeft()));
            callback.save(changed);
        }
    }
}