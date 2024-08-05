package dev.ftb.mods.ftbquests.quest.theme.property;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;

public class IconProperty extends ThemeProperty<Icon> {

    public final Icon builtin;

    public IconProperty(String n, Icon b) {
        super(n, Color4I.empty());
        this.builtin = b;
    }

    public IconProperty(String n) {
        this(n, Color4I.empty());
    }

    public Icon parse(String string) {
        return string.equals("builtin") ? this.builtin : Icon.getIcon(string);
    }
}