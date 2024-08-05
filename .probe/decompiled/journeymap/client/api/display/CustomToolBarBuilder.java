package journeymap.client.api.display;

import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

public interface CustomToolBarBuilder {

    IThemeButton getThemeToggleButton(@NotNull String var1, @NotNull String var2, @NotNull String var3, @NotNull IThemeButton.Action var4);

    IThemeButton getThemeButton(@NotNull String var1, @NotNull String var2, @NotNull String var3, @NotNull IThemeButton.Action var4);

    IThemeButton getThemeToggleButton(@NotNull String var1, @NotNull String var2, @NotNull IThemeButton.Action var3);

    IThemeButton getThemeButton(@NotNull String var1, @NotNull String var2, @NotNull IThemeButton.Action var3);

    IThemeToolBar getNewToolbar(IThemeButton... var1);
}