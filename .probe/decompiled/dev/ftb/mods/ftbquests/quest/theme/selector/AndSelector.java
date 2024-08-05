package dev.ftb.mods.ftbquests.quest.theme.selector;

import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import java.util.ArrayList;
import java.util.List;

public class AndSelector extends ThemeSelector {

    public final List<ThemeSelector> selectors = new ArrayList();

    @Override
    public boolean matches(QuestObjectBase object) {
        return this.selectors.stream().allMatch(selector -> selector.matches(object));
    }

    @Override
    public ThemeSelectorType getType() {
        return ThemeSelectorType.AND;
    }

    @Override
    public int compareTo(ThemeSelector o) {
        return o instanceof AndSelector a ? Integer.compare(a.selectors.size(), this.selectors.size()) : super.compareTo(o);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.selectors.size(); i++) {
            if (i > 0) {
                builder.append(" & ");
            }
            builder.append(this.selectors.get(i));
        }
        return builder.toString();
    }

    public int hashCode() {
        return this.selectors.hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else {
            return o instanceof AndSelector ? this.selectors.equals(((AndSelector) o).selectors) : false;
        }
    }
}