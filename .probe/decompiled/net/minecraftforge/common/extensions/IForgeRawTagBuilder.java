package net.minecraftforge.common.extensions;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;

public interface IForgeRawTagBuilder {

    default TagBuilder getRawBuilder() {
        return (TagBuilder) this;
    }

    @Deprecated(forRemoval = true, since = "1.20.1")
    default void serializeTagAdditions(JsonObject tagJson) {
    }

    default TagBuilder remove(TagEntry tagEntry, String source) {
        return this.getRawBuilder().remove(tagEntry);
    }

    default TagBuilder removeElement(ResourceLocation elementID, String source) {
        return this.remove(TagEntry.element(elementID), source);
    }

    default TagBuilder removeTag(ResourceLocation tagID, String source) {
        return this.remove(TagEntry.tag(tagID), source);
    }
}