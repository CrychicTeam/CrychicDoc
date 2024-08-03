package dev.shadowsoffire.placebo.reload;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public class DynamicHolder<T> implements Supplier<T> {

    public static final ResourceLocation EMPTY = new ResourceLocation("empty", "empty");

    protected final DynamicRegistry<? super T> registry;

    protected final ResourceLocation id;

    @Nullable
    protected T value;

    DynamicHolder(DynamicRegistry<? super T> registry, ResourceLocation id) {
        this.id = id;
        this.registry = registry;
    }

    public boolean isBound() {
        this.bind();
        return this.value != null;
    }

    public T get() {
        this.bind();
        Objects.requireNonNull(this.value, "Trying to access unbound value: " + this.id);
        return this.value;
    }

    public Optional<T> getOptional() {
        return this.isBound() ? Optional.of(this.value()) : Optional.empty();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public boolean is(ResourceLocation id) {
        return this.id.equals(id);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            if (obj instanceof DynamicHolder dh && dh.registry == this.registry && dh.id.equals(this.id)) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.id, this.registry });
    }

    void bind() {
        if (this.value == null) {
            this.value = (T) this.registry.getValue(this.id);
        }
    }

    void unbind() {
        this.value = null;
    }

    @Deprecated(forRemoval = true)
    public T value() {
        return this.get();
    }
}