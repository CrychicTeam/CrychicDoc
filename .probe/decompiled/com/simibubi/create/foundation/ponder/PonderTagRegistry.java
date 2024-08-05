package com.simibubi.create.foundation.ponder;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class PonderTagRegistry {

    private final Multimap<ResourceLocation, PonderTag> tags = LinkedHashMultimap.create();

    private final Multimap<PonderChapter, PonderTag> chapterTags = LinkedHashMultimap.create();

    private final List<PonderTag> listedTags = new ArrayList();

    public Set<PonderTag> getTags(ResourceLocation item) {
        return ImmutableSet.copyOf(this.tags.get(item));
    }

    public Set<PonderTag> getTags(PonderChapter chapter) {
        return ImmutableSet.copyOf(this.chapterTags.get(chapter));
    }

    public Set<ResourceLocation> getItems(PonderTag tag) {
        return (Set<ResourceLocation>) this.tags.entries().stream().filter(e -> e.getValue() == tag).map(Entry::getKey).collect(ImmutableSet.toImmutableSet());
    }

    public Set<PonderChapter> getChapters(PonderTag tag) {
        return (Set<PonderChapter>) this.chapterTags.entries().stream().filter(e -> e.getValue() == tag).map(Entry::getKey).collect(ImmutableSet.toImmutableSet());
    }

    public List<PonderTag> getListedTags() {
        return this.listedTags;
    }

    public void listTag(PonderTag tag) {
        this.listedTags.add(tag);
    }

    public void add(PonderTag tag, ResourceLocation item) {
        synchronized (this.tags) {
            this.tags.put(item, tag);
        }
    }

    public void add(PonderTag tag, PonderChapter chapter) {
        synchronized (this.chapterTags) {
            this.chapterTags.put(chapter, tag);
        }
    }

    public PonderTagRegistry.ItemBuilder forItems(ResourceLocation... items) {
        return new PonderTagRegistry.ItemBuilder(items);
    }

    public PonderTagRegistry.TagBuilder forTag(PonderTag tag) {
        return new PonderTagRegistry.TagBuilder(tag);
    }

    public class ItemBuilder {

        private final Collection<ResourceLocation> items;

        private ItemBuilder(ResourceLocation... items) {
            this.items = Arrays.asList(items);
        }

        public PonderTagRegistry.ItemBuilder add(PonderTag tag) {
            this.items.forEach(i -> PonderTagRegistry.this.add(tag, i));
            return this;
        }
    }

    public class TagBuilder {

        private final PonderTag tag;

        private TagBuilder(PonderTag tag) {
            this.tag = tag;
        }

        public PonderTagRegistry.TagBuilder add(ResourceLocation item) {
            PonderTagRegistry.this.add(this.tag, item);
            return this;
        }

        public PonderTagRegistry.TagBuilder add(ItemLike item) {
            return this.add(RegisteredObjects.getKeyOrThrow(item.asItem()));
        }

        public PonderTagRegistry.TagBuilder add(ItemProviderEntry<?> entry) {
            return this.add((ItemLike) entry.get());
        }
    }
}