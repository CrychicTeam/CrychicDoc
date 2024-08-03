package net.minecraftforge.registries;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import org.apache.commons.lang3.Validate;

public class MissingMappingsEvent extends Event {

    private final ResourceKey<? extends Registry<?>> key;

    private final IForgeRegistry<?> registry;

    private final List<MissingMappingsEvent.Mapping<?>> mappings;

    public MissingMappingsEvent(ResourceKey<? extends Registry<?>> key, IForgeRegistry<?> registry, Collection<MissingMappingsEvent.Mapping<?>> missed) {
        this.key = key;
        this.registry = registry;
        this.mappings = List.copyOf(missed);
    }

    public ResourceKey<? extends Registry<?>> getKey() {
        return this.key;
    }

    public IForgeRegistry<?> getRegistry() {
        return this.registry;
    }

    public <T> List<MissingMappingsEvent.Mapping<T>> getMappings(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        return registryKey == this.key ? this.mappings.stream().filter(e -> e.key.getNamespace().equals(namespace)).toList() : List.of();
    }

    public <T> List<MissingMappingsEvent.Mapping<T>> getAllMappings(ResourceKey<? extends Registry<T>> registryKey) {
        return (List<MissingMappingsEvent.Mapping<T>>) (registryKey == this.key ? this.mappings : List.of());
    }

    public static enum Action {

        DEFAULT, IGNORE, WARN, FAIL, REMAP
    }

    public static class Mapping<T> implements Comparable<MissingMappingsEvent.Mapping<T>> {

        private final IForgeRegistry<T> registry;

        private final IForgeRegistry<T> pool;

        final ResourceLocation key;

        final int id;

        MissingMappingsEvent.Action action = MissingMappingsEvent.Action.DEFAULT;

        T target;

        public Mapping(IForgeRegistry<T> registry, IForgeRegistry<T> pool, ResourceLocation key, int id) {
            this.registry = registry;
            this.pool = pool;
            this.key = key;
            this.id = id;
        }

        public void ignore() {
            this.action = MissingMappingsEvent.Action.IGNORE;
        }

        public void warn() {
            this.action = MissingMappingsEvent.Action.WARN;
        }

        public void fail() {
            this.action = MissingMappingsEvent.Action.FAIL;
        }

        public void remap(T target) {
            Validate.notNull(target, "Remap target can not be null", new Object[0]);
            Validate.isTrue(this.pool.getKey(target) != null, String.format(Locale.ENGLISH, "The specified entry %s hasn't been registered in registry yet.", target), new Object[0]);
            this.action = MissingMappingsEvent.Action.REMAP;
            this.target = target;
        }

        public IForgeRegistry<T> getRegistry() {
            return this.registry;
        }

        public ResourceLocation getKey() {
            return this.key;
        }

        public int getId() {
            return this.id;
        }

        public int compareTo(MissingMappingsEvent.Mapping<T> o) {
            int ret = this.registry.getRegistryName().compareNamespaced(o.registry.getRegistryName());
            if (ret == 0) {
                ret = this.key.compareNamespaced(o.key);
            }
            return ret;
        }
    }
}