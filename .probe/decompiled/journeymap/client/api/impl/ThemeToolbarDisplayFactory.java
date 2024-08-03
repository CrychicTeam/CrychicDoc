package journeymap.client.api.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import journeymap.client.api.display.CustomToolBarBuilder;
import journeymap.client.api.display.IThemeButton;
import journeymap.client.api.display.IThemeToolBar;
import journeymap.client.ui.component.ButtonList;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.theme.Theme;
import journeymap.client.ui.theme.ThemeButton;
import journeymap.client.ui.theme.ThemeToolbar;
import net.minecraft.client.gui.components.Button;

public class ThemeToolbarDisplayFactory extends ThemeFactory implements CustomToolBarBuilder {

    private final List<ThemeToolbar> toolbarList = new ArrayList();

    private final Fullscreen fullscreen;

    public ThemeToolbarDisplayFactory(Theme theme, Fullscreen fullscreen) {
        super(theme);
        this.fullscreen = fullscreen;
    }

    @Override
    public IThemeToolBar getNewToolbar(IThemeButton... themeButtons) {
        List<Button> buttonList = new ArrayList();
        Arrays.stream(themeButtons).forEach(b -> {
            buttonList.add((ThemeButton) b);
            this.fullscreen.addButtonWidget((ThemeButton) b);
        });
        ThemeToolbar themeToolbar = new ThemeToolbar(this.theme, new ButtonList(buttonList));
        this.toolbarList.add(themeToolbar);
        return themeToolbar;
    }

    public List<ThemeToolbar> getToolbarList() {
        return this.toolbarList;
    }
}