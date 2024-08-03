package net.minecraftforge.registries;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RegistryObject<T> implements Supplier<T> {

    @Nullable
    private final ResourceLocation name;

    @Nullable
    private ResourceKey<T> key;

    private final boolean optionalRegistry;

    @Nullable
    private T value;

    @Nullable
    private Holder<T> holder;

    private static final RegistryObject<?> EMPTY = new RegistryObject();

    public static <T, U extends T> RegistryObject<U> create(ResourceLocation name, IForgeRegistry<T> registry) {
        return new RegistryObject<>(name, registry);
    }

    public static <T, U extends T> RegistryObject<U> create(ResourceLocation name, ResourceKey<? extends Registry<T>> registryKey, String modid) {
        return new RegistryObject<>(name, registryKey.location(), modid, false);
    }

    public static <T, U extends T> RegistryObject<U> createOptional(ResourceLocation name, ResourceKey<? extends Registry<T>> registryKey, String modid) {
        return new RegistryObject<>(name, registryKey.location(), modid, true);
    }

    public static <T, U extends T> RegistryObject<U> create(ResourceLocation name, ResourceLocation registryName, String modid) {
        return new RegistryObject<>(name, registryName, modid, false);
    }

    public static <T, U extends T> RegistryObject<U> createOptional(ResourceLocation name, ResourceLocation registryName, String modid) {
        return new RegistryObject<>(name, registryName, modid, true);
    }

    private static <T> RegistryObject<T> empty() {
        return (RegistryObject<T>) EMPTY;
    }

    private RegistryObject() {
        this.name = null;
        this.key = null;
        this.optionalRegistry = false;
    }

    private RegistryObject(ResourceLocation name, IForgeRegistry<?> registry) {
        if (registry == null) {
            throw new IllegalArgumentException("Invalid registry argument, must not be null");
        } else {
            this.name = name;
            this.key = ResourceKey.create(registry.getRegistryKey(), name);
            this.optionalRegistry = false;
            ObjectHolderRegistry.addHandler(pred -> {
                if (pred.test(registry.getRegistryName())) {
                    this.updateReference((IForgeRegistry<? extends T>) registry);
                }
            });
            this.updateReference((IForgeRegistry<? extends T>) registry);
        }
    }

    private RegistryObject(ResourceLocation name, final ResourceLocation registryName, final String modid, boolean optionalRegistry) {
        this.name = name;
        this.key = ResourceKey.create(ResourceKey.createRegistryKey(registryName), name);
        this.optionalRegistry = optionalRegistry;
        final Throwable callerStack = new Throwable("Calling Site from mod: " + modid);
        ObjectHolderRegistry.addHandler(new Consumer<Predicate<ResourceLocation>>() {

            private boolean registryExists = false;

            private boolean invalidRegistry = false;

            public void accept(Predicate<ResourceLocation> pred) {
                if (!this.invalidRegistry) {
                    if (!RegistryObject.this.optionalRegistry && !this.registryExists) {
                        if (!RegistryObject.registryExists(registryName)) {
                            this.invalidRegistry = true;
                            throw new IllegalStateException("Unable to find registry with key " + registryName + " for mod \"" + modid + "\". Check the 'caused by' to see further stack.", callerStack);
                        }
                        this.registryExists = true;
                    }
                    if (pred.test(registryName)) {
                        RegistryObject.this.updateReference(registryName);
                    }
                }
            }
        });
        this.updateReference(registryName);
    }

    @NotNull
    public T get() {
        T ret = this.value;
        Objects.requireNonNull(ret, () -> "Registry Object not present: " + this.name);
        return ret;
    }

    void updateReference(IForgeRegistry<? extends T> registry) {
        if (this.name != null && this.key != null) {
            if (registry.containsKey(this.name)) {
                this.value = (T) registry.getValue(this.name);
                this.holder = (Holder<T>) registry.getHolder(this.name).orElse(null);
            } else {
                this.value = null;
                this.holder = null;
            }
        }
    }

    void updateReference(Registry<? extends T> registry) {
        if (this.name != null && this.key != null) {
            if (registry.containsKey(this.name)) {
                this.value = (T) registry.get(this.name);
                this.holder = (Holder<T>) registry.getHolder(this.key).orElse(null);
            } else {
                this.value = null;
                this.holder = null;
            }
        }
    }

    void updateReference(ResourceLocation registryName) {
        if (this.name != null) {
            IForgeRegistry<? extends T> forgeRegistry = RegistryManager.ACTIVE.getRegistry(registryName);
            if (forgeRegistry != null) {
                this.updateReference(forgeRegistry);
            } else {
                Registry<? extends T> vanillaRegistry = (Registry<? extends T>) BuiltInRegistries.REGISTRY.get(registryName);
                if (vanillaRegistry != null) {
                    this.updateReference(vanillaRegistry);
                } else {
                    this.value = null;
                    this.holder = null;
                }
            }
        }
    }

    void updateReference(RegisterEvent event) {
        IForgeRegistry<? extends T> forgeRegistry = event.getForgeRegistry();
        if (forgeRegistry != null) {
            this.updateReference(forgeRegistry);
        } else {
            Registry<? extends T> vanillaRegistry = event.getVanillaRegistry();
            if (vanillaRegistry != null) {
                this.updateReference(vanillaRegistry);
            } else {
                this.value = null;
            }
        }
    }

    private static boolean registryExists(ResourceLocation registryName) {
        return RegistryManager.ACTIVE.getRegistry(registryName) != null || BuiltInRegistries.REGISTRY.containsKey(registryName);
    }

    public ResourceLocation getId() {
        return this.name;
    }

    @Nullable
    public ResourceKey<T> getKey() {
        return this.key;
    }

    public Stream<T> stream() {
        return this.isPresent() ? Stream.of(this.get()) : Stream.of();
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public void ifPresent(Consumer<? super T> consumer) {
        if (this.isPresent()) {
            consumer.accept(this.get());
        }
    }

    public RegistryObject<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!this.isPresent()) {
            return this;
        } else {
            return predicate.test(this.get()) ? this : empty();
        }
    }

    public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return !this.isPresent() ? Optional.empty() : Optional.ofNullable(mapper.apply(this.get()));
    }

    public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
        Objects.requireNonNull(mapper);
        return !this.isPresent() ? Optional.empty() : (Optional) Objects.requireNonNull((Optional) mapper.apply(this.get()));
    }

    public <U> Supplier<U> lazyMap(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return () -> this.isPresent() ? mapper.apply(this.get()) : null;
    }

    public T orElse(T other) {
        return this.isPresent() ? this.get() : other;
    }

    public T orElseGet(Supplier<? extends T> other) {
        return (T) (this.isPresent() ? this.get() : other.get());
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.isPresent()) {
            return this.get();
        } else {
            throw (Throwable) exceptionSupplier.get();
        }
    }

    @NotNull
    public Optional<Holder<T>> getHolder() {
        return Optional.ofNullable(this.holder);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof RegistryObject ? Objects.equals(((RegistryObject) obj).name, this.name) : false;
        }
    }

    public int hashCode() {
        return Objects.hashCode(this.name);
    }
}