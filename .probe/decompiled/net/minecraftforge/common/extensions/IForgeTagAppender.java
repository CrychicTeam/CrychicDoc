package net.minecraftforge.common.extensions;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public interface IForgeTagAppender<T> {

    private TagsProvider.TagAppender<T> self() {
        return (TagsProvider.TagAppender<T>) this;
    }

    default TagsProvider.TagAppender<T> addTags(TagKey<T>... values) {
        TagsProvider.TagAppender<T> builder = this.self();
        for (TagKey<T> value : values) {
            builder.addTag(value);
        }
        return builder;
    }

    default TagsProvider.TagAppender<T> addOptionalTag(TagKey<T> value) {
        return this.self().addOptionalTag(value.location());
    }

    default TagsProvider.TagAppender<T> addOptionalTags(TagKey<T>... values) {
        TagsProvider.TagAppender<T> builder = this.self();
        for (TagKey<T> value : values) {
            builder.addOptionalTag(value.location());
        }
        return builder;
    }

    default TagsProvider.TagAppender<T> replace() {
        return this.replace(true);
    }

    default TagsProvider.TagAppender<T> replace(boolean value) {
        this.self().getInternalBuilder().replace(value);
        return this.self();
    }

    default TagsProvider.TagAppender<T> remove(ResourceLocation location) {
        TagsProvider.TagAppender<T> builder = this.self();
        builder.getInternalBuilder().removeElement(location, builder.getModID());
        return builder;
    }

    default TagsProvider.TagAppender<T> remove(ResourceLocation first, ResourceLocation... locations) {
        this.remove(first);
        for (ResourceLocation location : locations) {
            this.remove(location);
        }
        return this.self();
    }

    default TagsProvider.TagAppender<T> remove(ResourceKey<T> resourceKey) {
        this.remove(resourceKey.location());
        return this.self();
    }

    default TagsProvider.TagAppender<T> remove(ResourceKey<T> firstResourceKey, ResourceKey<T>... resourceKeys) {
        this.remove(firstResourceKey.location());
        for (ResourceKey<T> resourceKey : resourceKeys) {
            this.remove(resourceKey.location());
        }
        return this.self();
    }

    default TagsProvider.TagAppender<T> remove(TagKey<T> tag) {
        TagsProvider.TagAppender<T> builder = this.self();
        builder.getInternalBuilder().removeTag(tag.location(), builder.getModID());
        return builder;
    }

    default TagsProvider.TagAppender<T> remove(TagKey<T> first, TagKey<T>... tags) {
        this.remove(first);
        for (TagKey<T> tag : tags) {
            this.remove(tag);
        }
        return this.self();
    }
}