package de.keksuccino.fancymenu.util.rendering.ui.theme;

import de.keksuccino.fancymenu.events.UIColorThemeChangedEvent;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.fancymenu.util.rendering.ui.theme.themes.UIColorThemes;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UIColorThemeRegistry {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, UIColorTheme> THEMES = new LinkedHashMap();

    private static UIColorTheme activeTheme;

    public static void register(@NotNull UIColorTheme theme) {
        Objects.requireNonNull(theme);
        Objects.requireNonNull(theme.getIdentifier());
        if (THEMES.containsKey(theme.identifier)) {
            LOGGER.warn("[FANCYMENU] UIColorTheme with identifier '" + theme.getIdentifier() + "' already exists! Overriding theme!");
        }
        THEMES.put(theme.getIdentifier(), theme);
    }

    @NotNull
    public static UIColorTheme getActiveTheme() {
        return (UIColorTheme) (activeTheme != null ? activeTheme : UIColorThemes.DARK);
    }

    public static void setActiveTheme(@NotNull String identifier) {
        activeTheme = getTheme(identifier);
        if (activeTheme == null) {
            LOGGER.error("[FANCYMENU] Unable to switch theme! Theme not found: " + identifier);
            LOGGER.error("[FANCYMENU] Falling back to DARK theme!");
            activeTheme = UIColorThemes.DARK;
        }
        EventHandler.INSTANCE.postEvent(new UIColorThemeChangedEvent(getActiveTheme()));
    }

    @Nullable
    public static UIColorTheme getTheme(@NotNull String identifier) {
        Objects.requireNonNull(identifier);
        return (UIColorTheme) THEMES.get(identifier);
    }

    @NotNull
    public static List<UIColorTheme> getThemes() {
        return new ArrayList(THEMES.values());
    }

    public static void clearThemes() {
        THEMES.clear();
        activeTheme = null;
    }
}