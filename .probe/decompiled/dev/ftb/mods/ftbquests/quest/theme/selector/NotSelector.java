package dev.ftb.mods.ftbquests.quest.theme.selector;

import dev.ftb.mods.ftbquests.quest.QuestObjectBase;

public class NotSelector extends ThemeSelector {

    public final ThemeSelector selector;

    public NotSelector(ThemeSelector s) {
        this.selector = s;
    }

    @Override
    public boolean matches(QuestObjectBase object) {
        return !this.selector.matches(object);
    }

    @Override
    public ThemeSelectorType getType() {
        return ThemeSelectorType.NOT;
    }

    @Override
    public int compareTo(ThemeSelector o) {
        return o instanceof NotSelector ? ((NotSelector) o).selector.compareTo(this.selector) : super.compareTo(o);
    }

    public String toString() {
        return "!" + this.selector;
    }

    public int hashCode() {
        return -this.selector.hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else {
            return o instanceof NotSelector ? this.selector.equals(((NotSelector) o).selector) : false;
        }
    }
}