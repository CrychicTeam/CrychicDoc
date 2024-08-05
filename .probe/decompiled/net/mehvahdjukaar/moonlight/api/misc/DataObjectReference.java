package net.mehvahdjukaar.moonlight.api.misc;

import java.util.Map.Entry;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class DataObjectReference<T> implements Supplier<T> {

    private static final WeakHashSet<DataObjectReference<?>> REFERENCES = new WeakHashSet<>();

    private final ResourceKey<Registry<T>> registryKey;

    private final ResourceKey<T> key;

    @Nullable
    private Holder<T> cache;

    public DataObjectReference(String id, ResourceKey<Registry<T>> registry) {
        this(new ResourceLocation(id), registry);
    }

    public DataObjectReference(ResourceLocation location, ResourceKey<Registry<T>> registry) {
        this.registryKey = registry;
        this.key = ResourceKey.create(this.registryKey, location);
        REFERENCES.add(this);
    }

    public DataObjectReference(ResourceKey<T> key) {
        this.key = key;
        this.registryKey = ResourceKey.createRegistryKey(key.registry());
        REFERENCES.add(this);
    }

    public Holder<T> getHolder() {
        if (this.cache == null) {
            RegistryAccess r = Utils.hackyGetRegistryAccess();
            Registry<T> reg = r.registryOrThrow(this.registryKey);
            try {
                this.cache = reg.getHolderOrThrow(this.key);
            } catch (Exception var4) {
                throw new RuntimeException("Failed to get object from registry: " + this.key + ".\nCalled from " + Thread.currentThread() + ".\nRegistry content was: " + reg.entrySet().stream().map(Entry::getValue).toList(), var4);
            }
        }
        return this.cache;
    }

    @NotNull
    public T get() {
        return this.getHolder().value();
    }

    @Deprecated(forRemoval = true)
    @Nullable
    public T getUnchecked() {
        return this.get();
    }

    public void clearCache() {
        this.cache = null;
    }

    public ResourceLocation getID() {
        return this.key.location();
    }

    @Internal
    public static void onDataReload() {
        REFERENCES.forEach(DataObjectReference::clearCache);
    }
}