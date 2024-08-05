package dev.ftb.mods.ftbquests.quest.theme.selector;

import dev.ftb.mods.ftbquests.quest.QuestObjectBase;

public class AllSelector extends ThemeSelector {

    public static final AllSelector INSTANCE = new AllSelector();

    private AllSelector() {
    }

    @Override
    public boolean matches(QuestObjectBase object) {
        return true;
    }

    @Override
    public ThemeSelectorType getType() {
        return ThemeSelectorType.ALL;
    }

    public String toString() {
        return "*";
    }

    public int hashCode() {
        return 42;
    }

    public boolean equals(Object o) {
        return o == this;
    }
}