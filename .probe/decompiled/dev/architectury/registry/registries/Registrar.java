package dev.architectury.registry.registries;

import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface Registrar<T> extends Iterable<T> {

    RegistrySupplier<T> delegate(ResourceLocation var1);

    default <R extends T> RegistrySupplier<R> wrap(R obj) {
        ResourceLocation id = this.getId((T) obj);
        if (id == null) {
            throw new IllegalArgumentException("Cannot wrap an object without an id: " + obj);
        } else {
            return this.delegate(id);
        }
    }

    <E extends T> RegistrySupplier<E> register(ResourceLocation var1, Supplier<E> var2);

    @Nullable
    ResourceLocation getId(T var1);

    int getRawId(T var1);

    Optional<ResourceKey<T>> getKey(T var1);

    @Nullable
    T get(ResourceLocation var1);

    @Nullable
    T byRawId(int var1);

    boolean contains(ResourceLocation var1);

    boolean containsValue(T var1);

    Set<ResourceLocation> getIds();

    Set<Entry<ResourceKey<T>, T>> entrySet();

    ResourceKey<? extends Registry<T>> key();

    default <R extends T> void listen(RegistrySupplier<R> supplier, Consumer<R> callback) {
        this.listen(supplier.getId(), obj -> callback.accept(obj));
    }

    void listen(ResourceLocation var1, Consumer<T> var2);
}