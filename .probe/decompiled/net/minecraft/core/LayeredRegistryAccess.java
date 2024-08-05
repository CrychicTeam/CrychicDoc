package net.minecraft.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;

public class LayeredRegistryAccess<T> {

    private final List<T> keys;

    private final List<RegistryAccess.Frozen> values;

    private final RegistryAccess.Frozen composite;

    public LayeredRegistryAccess(List<T> listT0) {
        this(listT0, Util.make(() -> {
            RegistryAccess.Frozen[] $$1 = new RegistryAccess.Frozen[listT0.size()];
            Arrays.fill($$1, RegistryAccess.EMPTY);
            return Arrays.asList($$1);
        }));
    }

    private LayeredRegistryAccess(List<T> listT0, List<RegistryAccess.Frozen> listRegistryAccessFrozen1) {
        this.keys = List.copyOf(listT0);
        this.values = List.copyOf(listRegistryAccessFrozen1);
        this.composite = new RegistryAccess.ImmutableRegistryAccess(collectRegistries(listRegistryAccessFrozen1.stream())).m_203557_();
    }

    private int getLayerIndexOrThrow(T t0) {
        int $$1 = this.keys.indexOf(t0);
        if ($$1 == -1) {
            throw new IllegalStateException("Can't find " + t0 + " inside " + this.keys);
        } else {
            return $$1;
        }
    }

    public RegistryAccess.Frozen getLayer(T t0) {
        int $$1 = this.getLayerIndexOrThrow(t0);
        return (RegistryAccess.Frozen) this.values.get($$1);
    }

    public RegistryAccess.Frozen getAccessForLoading(T t0) {
        int $$1 = this.getLayerIndexOrThrow(t0);
        return this.getCompositeAccessForLayers(0, $$1);
    }

    public RegistryAccess.Frozen getAccessFrom(T t0) {
        int $$1 = this.getLayerIndexOrThrow(t0);
        return this.getCompositeAccessForLayers($$1, this.values.size());
    }

    private RegistryAccess.Frozen getCompositeAccessForLayers(int int0, int int1) {
        return new RegistryAccess.ImmutableRegistryAccess(collectRegistries(this.values.subList(int0, int1).stream())).m_203557_();
    }

    public LayeredRegistryAccess<T> replaceFrom(T t0, RegistryAccess.Frozen... registryAccessFrozen1) {
        return this.replaceFrom(t0, Arrays.asList(registryAccessFrozen1));
    }

    public LayeredRegistryAccess<T> replaceFrom(T t0, List<RegistryAccess.Frozen> listRegistryAccessFrozen1) {
        int $$2 = this.getLayerIndexOrThrow(t0);
        if (listRegistryAccessFrozen1.size() > this.values.size() - $$2) {
            throw new IllegalStateException("Too many values to replace");
        } else {
            List<RegistryAccess.Frozen> $$3 = new ArrayList();
            for (int $$4 = 0; $$4 < $$2; $$4++) {
                $$3.add((RegistryAccess.Frozen) this.values.get($$4));
            }
            $$3.addAll(listRegistryAccessFrozen1);
            while ($$3.size() < this.values.size()) {
                $$3.add(RegistryAccess.EMPTY);
            }
            return new LayeredRegistryAccess<>(this.keys, $$3);
        }
    }

    public RegistryAccess.Frozen compositeAccess() {
        return this.composite;
    }

    private static Map<ResourceKey<? extends Registry<?>>, Registry<?>> collectRegistries(Stream<? extends RegistryAccess> streamExtendsRegistryAccess0) {
        Map<ResourceKey<? extends Registry<?>>, Registry<?>> $$1 = new HashMap();
        streamExtendsRegistryAccess0.forEach(p_252003_ -> p_252003_.registries().forEach(p_250413_ -> {
            if ($$1.put(p_250413_.key(), p_250413_.value()) != null) {
                throw new IllegalStateException("Duplicated registry " + p_250413_.key());
            }
        }));
        return $$1;
    }
}