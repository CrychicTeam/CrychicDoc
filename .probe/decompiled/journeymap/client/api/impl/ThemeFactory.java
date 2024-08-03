package journeymap.client.api.impl;

import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import journeymap.client.api.display.IThemeButton;
import journeymap.client.ui.theme.Theme;
import journeymap.client.ui.theme.ThemeButton;
import journeymap.client.ui.theme.ThemeToggle;

public abstract class ThemeFactory {

    protected final Theme theme;

    protected ThemeFactory(Theme theme) {
        this.theme = theme;
    }

    public ThemeButton getThemeToggleButton(@NotNull String labelOn, @NotNull String labelOff, @NotNull String iconName, @NotNull IThemeButton.Action onPress) {
        return new ThemeToggle(this.theme, labelOn, labelOff, iconName, b -> onPress.doAction((IThemeButton) b));
    }

    public ThemeButton getThemeButton(@NotNull String labelOn, @NotNull String labelOff, @NotNull String iconName, @NotNull IThemeButton.Action onPress) {
        return new ThemeButton(this.theme, labelOn, labelOff, iconName, null, b -> onPress.doAction((IThemeButton) b));
    }

    public ThemeButton getThemeToggleButton(@NotNull String label, @NotNull String iconName, @NotNull IThemeButton.Action onPress) {
        return new ThemeToggle(this.theme, label, iconName, b -> onPress.doAction((IThemeButton) b));
    }

    public ThemeButton getThemeButton(@NotNull String label, @NotNull String iconName, @NotNull IThemeButton.Action onPress) {
        return new ThemeButton(this.theme, label, iconName, b -> onPress.doAction((IThemeButton) b));
    }
}