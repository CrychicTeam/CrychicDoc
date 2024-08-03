package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.ColorSelectorPanel;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

public class ColorConfig extends ConfigValue<Color4I> {

    private boolean allowAlphaEdit = false;

    public ColorConfig() {
        this.defaultValue = Icon.empty();
        this.value = Icon.empty();
    }

    public ColorConfig withAlphaEditing() {
        this.allowAlphaEdit = true;
        return this;
    }

    public boolean isAllowAlphaEdit() {
        return this.allowAlphaEdit;
    }

    @Override
    public void onClicked(Widget clicked, MouseButton button, ConfigCallback callback) {
        ColorSelectorPanel.popupAtMouse(clicked.getGui(), this, callback);
    }

    public Component getStringForGUI(@Nullable Color4I v) {
        Component block = Component.literal(" ■").withStyle(Style.EMPTY.withColor(this.allowAlphaEdit ? this.value.rgba() : this.value.rgb()));
        return super.getStringForGUI(v).copy().append(block);
    }
}