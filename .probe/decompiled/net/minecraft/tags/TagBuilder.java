package net.minecraft.tags;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public class TagBuilder {

    private final List<TagEntry> entries = new ArrayList();

    public static TagBuilder create() {
        return new TagBuilder();
    }

    public List<TagEntry> build() {
        return List.copyOf(this.entries);
    }

    public TagBuilder add(TagEntry tagEntry0) {
        this.entries.add(tagEntry0);
        return this;
    }

    public TagBuilder addElement(ResourceLocation resourceLocation0) {
        return this.add(TagEntry.element(resourceLocation0));
    }

    public TagBuilder addOptionalElement(ResourceLocation resourceLocation0) {
        return this.add(TagEntry.optionalElement(resourceLocation0));
    }

    public TagBuilder addTag(ResourceLocation resourceLocation0) {
        return this.add(TagEntry.tag(resourceLocation0));
    }

    public TagBuilder addOptionalTag(ResourceLocation resourceLocation0) {
        return this.add(TagEntry.optionalTag(resourceLocation0));
    }
}