package dev.ftb.mods.ftbquests.quest.theme.property;

import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.theme.QuestTheme;
import org.jetbrains.annotations.Nullable;

public abstract class ThemeProperty<T> {

    private final String name;

    private final T defaultValue;

    public ThemeProperty(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return this.name;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    @Nullable
    public abstract T parse(String var1);

    public String toString() {
        return this.name;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public boolean equals(Object o) {
        return o == this || o instanceof ThemeProperty && this.name.equals(o.toString());
    }

    public T get(@Nullable QuestObjectBase object) {
        return QuestTheme.instance.get(this, object);
    }

    public T get() {
        return this.get(null);
    }
}