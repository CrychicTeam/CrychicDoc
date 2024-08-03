package dev.latvian.mods.kubejs.server.tag;

import dev.latvian.mods.kubejs.event.EventExceptionHandler;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagLoader;
import net.minecraft.util.ExtraCodecs;

public class TagEventJS extends EventJS {

    public static final EventExceptionHandler TAG_EVENT_HANDLER = (event, container, ex) -> {
        if (ex instanceof IllegalStateException) {
            StackTraceElement[] stacktrace = ex.getStackTrace();
            if (stacktrace.length > 0 && stacktrace[0].toString().contains("dev.latvian.mods.rhino.ScriptRuntime.doTopCall")) {
                Throwable error = ex.getCause() == null ? ex : ex.getCause();
                ConsoleJS.SERVER.error("IllegalStateException was thrown during tag event in script %s:%d, this is most likely due to a concurrency bug in Rhino! While we are working on a fix for this issue, you may manually work around it by reloading the server again (e.g. by using /reload command).".formatted(container.source, container.line), error);
                return null;
            }
        } else if (ex instanceof EmptyTagTargetException) {
            ConsoleJS.SERVER.error(ex.getMessage() + " (at %s:%d)".formatted(container.source, container.line));
            return null;
        }
        return ex;
    };

    public static final String SOURCE = "KubeJS Custom Tags";

    public final RegistryInfo registry;

    public final Registry<?> vanillaRegistry;

    public final Map<ResourceLocation, TagWrapper> tags;

    public int totalAdded;

    public int totalRemoved;

    private Set<ResourceLocation> elementIds;

    public TagEventJS(RegistryInfo ri, Registry<?> vr) {
        this.registry = ri;
        this.vanillaRegistry = vr;
        this.tags = new ConcurrentHashMap();
        this.totalAdded = 0;
        this.totalRemoved = 0;
    }

    public ResourceLocation getType() {
        return this.registry.key.location();
    }

    public TagWrapper get(ResourceLocation id) {
        return (TagWrapper) this.tags.computeIfAbsent(id, this::createTagWrapper);
    }

    protected TagWrapper createTagWrapper(ResourceLocation id) {
        return new TagWrapper(this, id, new ArrayList());
    }

    public TagWrapper add(ResourceLocation tag, Object... filters) {
        return this.get(tag).add(filters);
    }

    public TagWrapper remove(ResourceLocation tag, Object... filters) {
        return this.get(tag).remove(filters);
    }

    public TagWrapper removeAll(ResourceLocation tag) {
        return this.get(tag).removeAll();
    }

    public void removeAllTagsFrom(Object... ids) {
        TagEventFilter filter = TagEventFilter.unwrap(this, ids);
        for (TagWrapper tagWrapper : this.tags.values()) {
            tagWrapper.entries.removeIf(proxy -> filter.testTagOrElementLocation(proxy.entry().elementOrTag()));
        }
    }

    public Set<ResourceLocation> getElementIds() {
        if (this.elementIds == null) {
            this.elementIds = UtilsJS.cast(this.vanillaRegistry.holders().map(Holder.Reference::m_205785_).map(ResourceKey::m_135782_).collect(Collectors.toSet()));
        }
        return this.elementIds;
    }

    void gatherIdsFor(TagWrapper excluded, Collection<ResourceLocation> collection, TagLoader.EntryWithSource entry) {
        ExtraCodecs.TagOrElementLocation id = entry.entry().elementOrTag();
        if (id.tag()) {
            TagWrapper w = (TagWrapper) this.tags.get(id.id());
            if (w != null && w != excluded) {
                for (TagLoader.EntryWithSource proxy : w.entries) {
                    this.gatherIdsFor(excluded, collection, proxy);
                }
            }
        } else {
            ResourceLocation entryId = id.id();
            if (this.getElementIds().contains(entryId)) {
                collection.add(entryId);
            }
        }
    }
}