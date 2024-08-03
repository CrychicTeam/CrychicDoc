package dev.ftb.mods.ftbquests.quest.theme.selector;

import dev.ftb.mods.ftbquests.quest.QuestObjectBase;

public abstract class ThemeSelector implements Comparable<ThemeSelector> {

    public abstract boolean matches(QuestObjectBase var1);

    public abstract ThemeSelectorType getType();

    public int compareTo(ThemeSelector o) {
        return this.getType().compareTo(o.getType());
    }
}