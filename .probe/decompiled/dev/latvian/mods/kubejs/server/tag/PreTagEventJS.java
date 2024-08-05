package dev.latvian.mods.kubejs.server.tag;

import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.bindings.event.ServerEvents;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.ScriptType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class PreTagEventJS extends TagEventJS {

    public final Map<ResourceLocation, PreTagWrapper> tags = new ConcurrentHashMap();

    public final List<Consumer<TagEventJS>> actions = new ArrayList();

    public boolean invalid;

    public static void handle(Map<ResourceKey<?>, PreTagEventJS> tagEventHolders) {
        tagEventHolders.clear();
        if (ServerEvents.TAGS.hasListeners()) {
            for (Object id : ServerEvents.TAGS.findUniqueExtraIds(ScriptType.SERVER)) {
                PreTagEventJS e = new PreTagEventJS(RegistryInfo.of((ResourceKey<? extends Registry<?>>) id));
                try {
                    ServerEvents.TAGS.post(ScriptType.SERVER, id, e);
                } catch (Exception var5) {
                    e.invalid = true;
                    if (DevProperties.get().debugInfo) {
                        KubeJS.LOGGER.warn("Pre Tag event for " + e.registry + " failed:");
                        var5.printStackTrace();
                    }
                }
                if (!e.invalid) {
                    tagEventHolders.put(e.registry.key, e);
                }
            }
        }
    }

    public PreTagEventJS(RegistryInfo registry) {
        super(registry, null);
    }

    @Override
    protected TagWrapper createTagWrapper(ResourceLocation id) {
        return new PreTagWrapper(this, id);
    }

    @Override
    public void removeAllTagsFrom(Object... ignored) {
        this.actions.add(new PreTagEventJS.RemoveAllTagsFromAction(ignored));
    }

    @Override
    public Set<ResourceLocation> getElementIds() {
        return Set.of();
    }

    public static record RemoveAllTagsFromAction(Object[] ignored) implements Consumer<TagEventJS> {

        public void accept(TagEventJS e) {
            e.removeAllTagsFrom(this.ignored);
        }
    }
}