package dev.ftb.mods.ftbquests.quest.theme.selector;

import dev.ftb.mods.ftbquests.quest.QuestObjectBase;

public class IDSelector extends ThemeSelector {

    public final long id;

    public IDSelector(long i) {
        this.id = i;
    }

    @Override
    public boolean matches(QuestObjectBase object) {
        return object.id == this.id;
    }

    @Override
    public ThemeSelectorType getType() {
        return ThemeSelectorType.ID;
    }

    public String toString() {
        return QuestObjectBase.getCodeString(this.id);
    }

    public int hashCode() {
        return Long.hashCode(this.id);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else {
            return o instanceof IDSelector ? this.id == ((IDSelector) o).id : false;
        }
    }
}