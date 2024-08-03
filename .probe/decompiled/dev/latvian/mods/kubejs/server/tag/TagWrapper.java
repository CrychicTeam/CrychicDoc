package dev.latvian.mods.kubejs.server.tag;

import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagLoader;

public class TagWrapper {

    public final TagEventJS event;

    public final ResourceLocation id;

    public final List<TagLoader.EntryWithSource> entries;

    public TagWrapper(TagEventJS e, ResourceLocation i, List<TagLoader.EntryWithSource> t) {
        this.event = e;
        this.id = i;
        this.entries = t;
    }

    public String toString() {
        return "<%s / #%s>".formatted(this.event.getType(), this.id);
    }

    public TagWrapper add(Object... filters) {
        TagEventFilter filter = TagEventFilter.unwrap(this.event, filters);
        int addedCount = filter.add(this);
        if (addedCount > 0) {
            this.event.totalAdded += addedCount;
            if (ConsoleJS.SERVER.shouldPrintDebug()) {
                ConsoleJS.SERVER.debug("+ %s // %s".formatted(this, filter));
            }
        } else if (DevProperties.get().logSkippedTags) {
            ConsoleJS.SERVER.warn("+ %s // %s [No matches found!]".formatted(this, filter));
        }
        return this;
    }

    public TagWrapper remove(Object... filters) {
        TagEventFilter filter = TagEventFilter.unwrap(this.event, filters);
        int removedCount = filter.remove(this);
        if (removedCount > 0) {
            this.event.totalRemoved += removedCount;
            if (ConsoleJS.SERVER.shouldPrintDebug()) {
                ConsoleJS.SERVER.debug("- %s // %s".formatted(this, filter));
            }
        } else if (DevProperties.get().logSkippedTags) {
            ConsoleJS.SERVER.warn("- %s // %s [No matches found!]".formatted(this, filter));
        }
        return this;
    }

    public TagWrapper removeAll() {
        if (ConsoleJS.SERVER.shouldPrintDebug()) {
            ConsoleJS.SERVER.debug("- %s // (all)".formatted(this));
        }
        if (!this.entries.isEmpty()) {
            this.event.totalRemoved = this.event.totalRemoved + this.entries.size();
            this.entries.clear();
        } else if (DevProperties.get().logSkippedTags) {
            ConsoleJS.SERVER.warn("- %s // (all) [No matches found!]".formatted(this));
        }
        return this;
    }

    public List<ResourceLocation> getObjectIds() {
        LinkedHashSet<ResourceLocation> set = new LinkedHashSet();
        for (TagLoader.EntryWithSource proxy : this.entries) {
            this.event.gatherIdsFor(this, set, proxy);
        }
        return new ArrayList(set);
    }
}