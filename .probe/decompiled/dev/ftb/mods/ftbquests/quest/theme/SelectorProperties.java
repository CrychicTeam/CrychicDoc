package dev.ftb.mods.ftbquests.quest.theme;

import dev.ftb.mods.ftbquests.quest.theme.selector.ThemeSelector;
import java.util.LinkedHashMap;
import java.util.Map;

public class SelectorProperties implements Comparable<SelectorProperties> {

    public final ThemeSelector selector;

    public final Map<String, String> properties;

    public SelectorProperties(ThemeSelector s) {
        this.selector = s;
        this.properties = new LinkedHashMap();
    }

    public int compareTo(SelectorProperties o) {
        return this.selector.compareTo(o.selector);
    }
}