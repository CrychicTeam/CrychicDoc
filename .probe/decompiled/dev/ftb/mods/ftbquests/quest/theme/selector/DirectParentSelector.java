package dev.ftb.mods.ftbquests.quest.theme.selector;

import dev.ftb.mods.ftbquests.quest.QuestObjectBase;

public class DirectParentSelector extends ThemeSelector {

    public final ThemeSelector parent;

    public final ThemeSelector child;

    public DirectParentSelector(ThemeSelector s, ThemeSelector c) {
        this.parent = s;
        this.child = c;
    }

    @Override
    public boolean matches(QuestObjectBase object) {
        if (!this.child.matches(object)) {
            return false;
        } else {
            QuestObjectBase o = object.getQuestFile().getBase(object.getParentID());
            return o != null && this.parent.matches(o);
        }
    }

    @Override
    public ThemeSelectorType getType() {
        return ThemeSelectorType.DIRECT_PARENT;
    }

    public String toString() {
        return this.parent + ">" + this.child;
    }

    public int hashCode() {
        return this.parent.hashCode() * 31 + this.child.hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else {
            return !(o instanceof DirectParentSelector s) ? false : this.parent.equals(s.parent) && this.child.equals(s.child);
        }
    }
}