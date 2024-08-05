package moe.wolfgirl.probejs.lang.snippet.parts;

import org.jetbrains.annotations.Nullable;

public class TabStop extends Enumerable {

    public final String content;

    public TabStop(@Nullable String content) {
        this.content = content;
    }

    @Override
    public String format() {
        return this.content == null ? "$%d".formatted(this.enumeration) : "${%d:%s}".formatted(this.enumeration, this.content.replace("$", "\\$"));
    }
}