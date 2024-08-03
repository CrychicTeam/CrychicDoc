package net.mehvahdjukaar.moonlight.api.events;

import java.util.List;
import java.util.Map;
import net.mehvahdjukaar.moonlight.api.resources.assets.LangBuilder;
import org.jetbrains.annotations.Nullable;

public class AfterLanguageLoadEvent implements SimpleEvent {

    private final Map<String, String> languageLines;

    private final List<String> languageInfo;

    public AfterLanguageLoadEvent(Map<String, String> lines, List<String> info) {
        this.languageInfo = info;
        this.languageLines = lines;
    }

    @Nullable
    public String getEntry(String key) {
        return (String) this.languageLines.get(key);
    }

    public void addEntry(String key, String translation) {
        this.languageLines.computeIfAbsent(key, k -> translation);
    }

    public void addEntries(LangBuilder builder) {
        builder.entries().forEach(this::addEntry);
    }

    public boolean isDefault() {
        return this.languageInfo.stream().anyMatch(l -> l.equals("en_us"));
    }
}