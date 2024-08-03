package com.craisinlord.integrated_api.modinit.registry;

import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface CustomRegistryLookup<T> extends Iterable<T> {

    boolean containsKey(ResourceLocation var1);

    boolean containsValue(T var1);

    @Nullable
    T get(ResourceLocation var1);

    @Nullable
    ResourceLocation getKey(T var1);

    Collection<T> getValues();

    Collection<ResourceLocation> getKeys();
}