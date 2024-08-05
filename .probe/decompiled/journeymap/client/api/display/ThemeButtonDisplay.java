package journeymap.client.api.display;

import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

public interface ThemeButtonDisplay {

    IThemeButton addThemeToggleButton(@NotNull String var1, @NotNull String var2, @NotNull String var3, boolean var4, @NotNull IThemeButton.Action var5);

    IThemeButton addThemeButton(@NotNull String var1, @NotNull String var2, @NotNull String var3, @NotNull IThemeButton.Action var4);

    IThemeButton addThemeToggleButton(@NotNull String var1, @NotNull String var2, boolean var3, @NotNull IThemeButton.Action var4);

    IThemeButton addThemeButton(@NotNull String var1, @NotNull String var2, @NotNull IThemeButton.Action var3);
}