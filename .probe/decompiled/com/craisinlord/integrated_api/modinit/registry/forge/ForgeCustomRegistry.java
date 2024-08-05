package com.craisinlord.integrated_api.modinit.registry.forge;

import com.craisinlord.integrated_api.modinit.registry.CustomRegistryLookup;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForgeCustomRegistry<T> implements CustomRegistryLookup<T> {

    private final Supplier<IForgeRegistry<T>> registry;

    public ForgeCustomRegistry(Supplier<IForgeRegistry<T>> registry) {
        this.registry = registry;
    }

    @Override
    public boolean containsKey(ResourceLocation id) {
        return ((IForgeRegistry) this.registry.get()).containsKey(id);
    }

    @Override
    public boolean containsValue(T value) {
        return ((IForgeRegistry) this.registry.get()).containsValue(value);
    }

    @Nullable
    @Override
    public T get(ResourceLocation id) {
        return (T) ((IForgeRegistry) this.registry.get()).getValue(id);
    }

    @Nullable
    @Override
    public ResourceLocation getKey(T value) {
        return ((IForgeRegistry) this.registry.get()).getKey(value);
    }

    @Override
    public Collection<T> getValues() {
        return ((IForgeRegistry) this.registry.get()).getValues();
    }

    @Override
    public Collection<ResourceLocation> getKeys() {
        return ((IForgeRegistry) this.registry.get()).getKeys();
    }

    @NotNull
    public Iterator<T> iterator() {
        return ((IForgeRegistry) this.registry.get()).iterator();
    }
}