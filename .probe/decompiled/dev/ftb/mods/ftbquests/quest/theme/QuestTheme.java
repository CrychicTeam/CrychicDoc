package dev.ftb.mods.ftbquests.quest.theme;

import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public class QuestTheme {

    public static QuestTheme instance;

    public static QuestObjectBase currentObject;

    public final List<SelectorProperties> selectors = new ArrayList();

    private final Map<QuestTheme.QuestObjectPropertyKey, Object> cache = new HashMap();

    private final Map<String, Object> defaultCache = new HashMap();

    public SelectorProperties defaults;

    public void clearCache() {
        this.cache.clear();
        this.defaultCache.clear();
    }

    public <T> T get(ThemeProperty<T> property) {
        T cachedValue = (T) this.defaultCache.get(property.getName());
        if (cachedValue != null) {
            return cachedValue;
        } else {
            String value = (String) this.defaults.properties.get(property.getName());
            if (value != null) {
                cachedValue = property.parse(this.replaceVariables(value, 0));
                if (cachedValue != null) {
                    this.defaultCache.put(property.getName(), cachedValue);
                    return cachedValue;
                }
            }
            return property.getDefaultValue();
        }
    }

    public <T> T get(ThemeProperty<T> property, @Nullable QuestObjectBase object) {
        if (object == null) {
            object = currentObject;
        }
        if (object == null) {
            return this.get(property);
        } else {
            QuestTheme.QuestObjectPropertyKey key = new QuestTheme.QuestObjectPropertyKey(property.getName(), object.id);
            T cachedValue = (T) this.cache.get(key);
            if (cachedValue != null) {
                return cachedValue;
            } else {
                QuestObjectBase o = object;
                do {
                    for (SelectorProperties selectorProperties : this.selectors) {
                        if (selectorProperties.selector.matches(o)) {
                            String value = (String) selectorProperties.properties.get(property.getName());
                            if (value != null) {
                                cachedValue = property.parse(this.replaceVariables(value, 0));
                                if (cachedValue != null) {
                                    this.cache.put(key, cachedValue);
                                    return cachedValue;
                                }
                            }
                        }
                    }
                    o = o.getQuestFile().getBase(o.getParentID());
                } while (o != null);
                return this.get(property);
            }
        }
    }

    public String replaceVariables(String value, int iteration) {
        if (iteration >= 30) {
            return value;
        } else {
            String original = value;
            for (String k : this.defaults.properties.keySet()) {
                value = value.replace("{{" + k + "}}", (CharSequence) this.defaults.properties.get(k));
            }
            return original.equals(value) ? value : this.replaceVariables(value, iteration + 1);
        }
    }

    private static class QuestObjectPropertyKey {

        private final String property;

        private final long object;

        private QuestObjectPropertyKey(String p, long o) {
            this.property = p;
            this.object = o;
        }

        public int hashCode() {
            return Long.hashCode((long) this.property.hashCode() * 31L + this.object);
        }

        public boolean equals(Object o) {
            return !(o instanceof QuestTheme.QuestObjectPropertyKey key) ? false : this.object == key.object && this.property.equals(key.property);
        }
    }
}