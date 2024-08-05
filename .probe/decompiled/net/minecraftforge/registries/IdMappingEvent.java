package net.minecraftforge.registries;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

public class IdMappingEvent extends Event {

    private final Map<ResourceLocation, ImmutableList<IdMappingEvent.ModRemapping>> remaps;

    private final ImmutableSet<ResourceLocation> keys;

    private final boolean isFrozen;

    public IdMappingEvent(Map<ResourceLocation, Map<ResourceLocation, IdMappingEvent.IdRemapping>> remaps, boolean isFrozen) {
        this.isFrozen = isFrozen;
        this.remaps = Maps.newHashMap();
        remaps.forEach((name, rm) -> {
            List<IdMappingEvent.ModRemapping> tmp = Lists.newArrayList();
            rm.forEach((key, value) -> tmp.add(new IdMappingEvent.ModRemapping(name, key, value.currId, value.newId)));
            tmp.sort(Comparator.comparingInt(o -> o.newId));
            this.remaps.put(name, ImmutableList.copyOf(tmp));
        });
        this.keys = ImmutableSet.copyOf(this.remaps.keySet());
    }

    public ImmutableSet<ResourceLocation> getRegistries() {
        return this.keys;
    }

    public ImmutableList<IdMappingEvent.ModRemapping> getRemaps(ResourceLocation registry) {
        return (ImmutableList<IdMappingEvent.ModRemapping>) this.remaps.get(registry);
    }

    public boolean isFrozen() {
        return this.isFrozen;
    }

    public static record IdRemapping(int currId, int newId) {
    }

    public static class ModRemapping {

        public final ResourceLocation registry;

        public final ResourceLocation key;

        public final int oldId;

        public final int newId;

        private ModRemapping(ResourceLocation registry, ResourceLocation key, int oldId, int newId) {
            this.registry = registry;
            this.key = key;
            this.oldId = oldId;
            this.newId = newId;
        }
    }
}