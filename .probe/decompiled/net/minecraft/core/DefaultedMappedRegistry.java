package net.minecraft.core;

import com.mojang.serialization.Lifecycle;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

public class DefaultedMappedRegistry<T> extends MappedRegistry<T> implements DefaultedRegistry<T> {

    private final ResourceLocation defaultKey;

    private Holder.Reference<T> defaultValue;

    public DefaultedMappedRegistry(String string0, ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT1, Lifecycle lifecycle2, boolean boolean3) {
        super(resourceKeyExtendsRegistryT1, lifecycle2, boolean3);
        this.defaultKey = new ResourceLocation(string0);
    }

    @Override
    public Holder.Reference<T> registerMapping(int int0, ResourceKey<T> resourceKeyT1, T t2, Lifecycle lifecycle3) {
        Holder.Reference<T> $$4 = super.registerMapping(int0, resourceKeyT1, t2, lifecycle3);
        if (this.defaultKey.equals(resourceKeyT1.location())) {
            this.defaultValue = $$4;
        }
        return $$4;
    }

    @Override
    public int getId(@Nullable T t0) {
        int $$1 = super.getId(t0);
        return $$1 == -1 ? super.getId(this.defaultValue.value()) : $$1;
    }

    @Nonnull
    @Override
    public ResourceLocation getKey(T t0) {
        ResourceLocation $$1 = super.getKey(t0);
        return $$1 == null ? this.defaultKey : $$1;
    }

    @Nonnull
    @Override
    public T get(@Nullable ResourceLocation resourceLocation0) {
        T $$1 = super.get(resourceLocation0);
        return $$1 == null ? this.defaultValue.value() : $$1;
    }

    @Override
    public Optional<T> getOptional(@Nullable ResourceLocation resourceLocation0) {
        return Optional.ofNullable(super.get(resourceLocation0));
    }

    @Nonnull
    @Override
    public T byId(int int0) {
        T $$1 = super.byId(int0);
        return $$1 == null ? this.defaultValue.value() : $$1;
    }

    @Override
    public Optional<Holder.Reference<T>> getRandom(RandomSource randomSource0) {
        return super.getRandom(randomSource0).or(() -> Optional.of(this.defaultValue));
    }

    @Override
    public ResourceLocation getDefaultKey() {
        return this.defaultKey;
    }
}