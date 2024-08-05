package dev.latvian.mods.kubejs.core;

import com.google.gson.JsonArray;
import dev.latvian.mods.kubejs.bindings.event.ServerEvents;
import dev.latvian.mods.kubejs.item.ingredient.TagContext;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.server.DataExport;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import dev.latvian.mods.kubejs.server.tag.PreTagEventJS;
import dev.latvian.mods.kubejs.server.tag.TagEventFilter;
import dev.latvian.mods.kubejs.server.tag.TagEventJS;
import dev.latvian.mods.kubejs.server.tag.TagWrapper;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagLoader;
import org.jetbrains.annotations.Nullable;

public interface TagLoaderKJS<T> {

    default void kjs$customTags(ServerScriptManager ssm, Map<ResourceLocation, List<TagLoader.EntryWithSource>> map) {
        TagContext.INSTANCE.setValue(TagContext.EMPTY);
        Registry<T> reg = this.kjs$getRegistry();
        if (reg != null) {
            RegistryInfo<?> regInfo = RegistryInfo.of(reg.key());
            if (regInfo.hasDefaultTags || ServerEvents.TAGS.hasListeners(reg.key())) {
                PreTagEventJS preEvent = (PreTagEventJS) ssm.preTagEvents.get(reg.key());
                TagEventJS event = new TagEventJS(regInfo, reg);
                for (Entry<ResourceLocation, List<TagLoader.EntryWithSource>> entry : map.entrySet()) {
                    TagWrapper w = new TagWrapper(event, (ResourceLocation) entry.getKey(), (List<TagLoader.EntryWithSource>) entry.getValue());
                    event.tags.put(w.id, w);
                    if (ConsoleJS.SERVER.shouldPrintDebug()) {
                        ConsoleJS.SERVER.debug("Tags %s/#%s; %d".formatted(regInfo, w.id, w.entries.size()));
                    }
                }
                for (BuilderBase<? extends Object> builder : regInfo.objects.values()) {
                    for (ResourceLocation s : builder.defaultTags) {
                        event.add(s, new TagEventFilter.ID(builder.id));
                    }
                }
                if (preEvent == null) {
                    ServerEvents.TAGS.post(event, regInfo.key, TagEventJS.TAG_EVENT_HANDLER);
                } else {
                    for (Consumer<TagEventJS> a : preEvent.actions) {
                        a.accept(event);
                    }
                }
                map.clear();
                for (Entry<ResourceLocation, TagWrapper> entryx : event.tags.entrySet()) {
                    map.put((ResourceLocation) entryx.getKey(), ((TagWrapper) entryx.getValue()).entries);
                }
                if (event.totalAdded > 0 || event.totalRemoved > 0 || ConsoleJS.SERVER.shouldPrintDebug()) {
                    ConsoleJS.SERVER.info("[%s] Found %d tags, added %d objects, removed %d objects".formatted(regInfo, event.tags.size(), event.totalAdded, event.totalRemoved));
                }
            }
            if (DataExport.export != null) {
                String loc = "tags/" + regInfo + "/";
                for (Entry<ResourceLocation, List<TagLoader.EntryWithSource>> entryx : map.entrySet()) {
                    ArrayList<String> list = new ArrayList();
                    for (TagLoader.EntryWithSource e : (List) entryx.getValue()) {
                        list.add(e.entry().toString());
                    }
                    list.sort(String.CASE_INSENSITIVE_ORDER);
                    JsonArray arr = new JsonArray();
                    for (String e : list) {
                        arr.add(e);
                    }
                    DataExport.export.addJson(loc + entryx.getKey() + ".json", arr);
                }
            }
        }
    }

    void kjs$setRegistry(Registry<T> var1);

    @Nullable
    Registry<T> kjs$getRegistry();
}