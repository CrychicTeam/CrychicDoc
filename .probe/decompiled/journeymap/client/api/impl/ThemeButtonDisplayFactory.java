package journeymap.client.api.impl;

import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import journeymap.client.api.display.IThemeButton;
import journeymap.client.api.display.ThemeButtonDisplay;
import journeymap.client.ui.theme.Theme;
import journeymap.client.ui.theme.ThemeButton;

public class ThemeButtonDisplayFactory extends ThemeFactory implements ThemeButtonDisplay {

    private final List<ThemeButton> themeButtonList = new ArrayList();

    public ThemeButtonDisplayFactory(Theme theme) {
        super(theme);
    }

    @Override
    public IThemeButton addThemeToggleButton(@NotNull String labelOn, @NotNull String labelOff, @NotNull String iconName, boolean toggled, @NotNull IThemeButton.Action onPress) {
        ThemeButton button = this.getThemeToggleButton(labelOn, labelOff, iconName, onPress);
        button.setToggled(Boolean.valueOf(toggled));
        this.themeButtonList.add(button);
        return button;
    }

    @Override
    public IThemeButton addThemeButton(@NotNull String labelOn, @NotNull String labelOff, @NotNull String iconName, @NotNull IThemeButton.Action onPress) {
        ThemeButton button = this.getThemeButton(labelOn, labelOff, iconName, onPress);
        this.themeButtonList.add(button);
        return button;
    }

    @Override
    public IThemeButton addThemeToggleButton(@NotNull String label, @NotNull String iconName, boolean toggled, @NotNull IThemeButton.Action onPress) {
        ThemeButton button = this.getThemeToggleButton(label, iconName, onPress);
        button.setToggled(Boolean.valueOf(toggled));
        this.themeButtonList.add(button);
        return button;
    }

    @Override
    public IThemeButton addThemeButton(@NotNull String label, @NotNull String iconName, @NotNull IThemeButton.Action onPress) {
        ThemeButton button = this.getThemeButton(label, iconName, onPress);
        this.themeButtonList.add(button);
        return button;
    }

    public List<ThemeButton> getThemeButtonList() {
        return this.themeButtonList;
    }
}