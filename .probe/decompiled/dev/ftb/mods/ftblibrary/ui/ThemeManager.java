package dev.ftb.mods.ftblibrary.ui;

public enum ThemeManager {

    INSTANCE;

    public Theme getActiveTheme() {
        return NordTheme.THEME;
    }
}