package dev.architectury.registry.registries;

import com.google.common.base.Suppliers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class DeferredRegister<T> implements Iterable<RegistrySupplier<T>> {

    private final Supplier<RegistrarManager> registriesSupplier;

    private final ResourceKey<Registry<T>> key;

    private final List<DeferredRegister<T>.Entry<T>> entries = new ArrayList();

    private final List<RegistrySupplier<T>> entryView = Collections.unmodifiableList(this.entries);

    private boolean registered = false;

    @Nullable
    private String modId;

    private DeferredRegister(Supplier<RegistrarManager> registriesSupplier, ResourceKey<Registry<T>> key, @Nullable String modId) {
        this.registriesSupplier = (Supplier<RegistrarManager>) Objects.requireNonNull(registriesSupplier);
        this.key = (ResourceKey<Registry<T>>) Objects.requireNonNull(key);
        this.modId = modId;
    }

    public static <T> DeferredRegister<T> create(String modId, ResourceKey<Registry<T>> key) {
        Supplier<RegistrarManager> value = Suppliers.memoize(() -> RegistrarManager.get(modId));
        return new DeferredRegister<>(value, key, (String) Objects.requireNonNull(modId));
    }

    public <R extends T> RegistrySupplier<R> register(String id, Supplier<? extends R> supplier) {
        if (this.modId == null) {
            throw new NullPointerException("You must create the deferred register with a mod id to register entries without the namespace!");
        } else {
            return this.register(new ResourceLocation(this.modId, id), supplier);
        }
    }

    public <R extends T> RegistrySupplier<R> register(ResourceLocation id, Supplier<? extends R> supplier) {
        DeferredRegister<T>.Entry<T> entry = new DeferredRegister.Entry<>(id, (Supplier<T>) supplier);
        this.entries.add(entry);
        if (this.registered) {
            Registrar<T> registrar = this.getRegistrar();
            entry.value = registrar.register(entry.id, entry.supplier);
        }
        return entry;
    }

    public void register() {
        if (this.registered) {
            throw new IllegalStateException("Cannot register a deferred register twice!");
        } else {
            this.registered = true;
            Registrar<T> registrar = this.getRegistrar();
            for (DeferredRegister<T>.Entry<T> entry : this.entries) {
                entry.value = registrar.register(entry.id, entry.supplier);
            }
        }
    }

    public Iterator<RegistrySupplier<T>> iterator() {
        return this.entryView.iterator();
    }

    public RegistrarManager getRegistrarManager() {
        return (RegistrarManager) this.registriesSupplier.get();
    }

    public Registrar<T> getRegistrar() {
        return ((RegistrarManager) this.registriesSupplier.get()).get(this.key);
    }

    private class Entry<R> implements RegistrySupplier<R> {

        private final ResourceLocation id;

        private final Supplier<R> supplier;

        private RegistrySupplier<R> value;

        public Entry(ResourceLocation id, Supplier<R> supplier) {
            this.id = id;
            this.supplier = supplier;
        }

        @Override
        public RegistrarManager getRegistrarManager() {
            return DeferredRegister.this.getRegistrarManager();
        }

        @Override
        public Registrar<R> getRegistrar() {
            return (Registrar<R>) DeferredRegister.this.getRegistrar();
        }

        @Override
        public ResourceLocation getRegistryId() {
            return DeferredRegister.this.key.location();
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public boolean isPresent() {
            return this.value != null && this.value.isPresent();
        }

        public R get() {
            if (this.isPresent()) {
                return (R) this.value.get();
            } else {
                throw new NullPointerException("Registry Object not present: " + this.id);
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.getRegistryId(), this.getId() });
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else {
                return !(obj instanceof RegistrySupplier<?> other) ? false : other.getRegistryId().equals(this.getRegistryId()) && other.getId().equals(this.getId());
            }
        }

        public String toString() {
            return this.getRegistryId().toString() + "@" + this.id.toString();
        }
    }
}