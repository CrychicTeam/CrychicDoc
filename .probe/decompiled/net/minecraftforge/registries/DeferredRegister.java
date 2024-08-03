package net.minecraftforge.registries;

import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeferredRegister<T> {

    private final ResourceKey<? extends Registry<T>> registryKey;

    private final String modid;

    private final boolean optionalRegistry;

    private final Map<RegistryObject<T>, Supplier<? extends T>> entries = new LinkedHashMap();

    private final Set<RegistryObject<T>> entriesView = Collections.unmodifiableSet(this.entries.keySet());

    @Nullable
    private Supplier<RegistryBuilder<?>> registryFactory;

    @Nullable
    private SetMultimap<TagKey<T>, Supplier<T>> optionalTags;

    private boolean seenRegisterEvent = false;

    public static <B> DeferredRegister<B> create(IForgeRegistry<B> reg, String modid) {
        return new DeferredRegister<>(reg, modid);
    }

    public static <B> DeferredRegister<B> create(ResourceKey<? extends Registry<B>> key, String modid) {
        return new DeferredRegister<>(key, modid, false);
    }

    public static <B> DeferredRegister<B> createOptional(ResourceKey<? extends Registry<B>> key, String modid) {
        return new DeferredRegister<>(key, modid, true);
    }

    public static <B> DeferredRegister<B> create(ResourceLocation registryName, String modid) {
        return new DeferredRegister(ResourceKey.createRegistryKey(registryName), modid, false);
    }

    public static <B> DeferredRegister<B> createOptional(ResourceLocation registryName, String modid) {
        return new DeferredRegister(ResourceKey.createRegistryKey(registryName), modid, true);
    }

    private DeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String modid, boolean optionalRegistry) {
        this.registryKey = registryKey;
        this.modid = modid;
        this.optionalRegistry = optionalRegistry;
    }

    private DeferredRegister(IForgeRegistry<T> reg, String modid) {
        this(reg.getRegistryKey(), modid, false);
    }

    public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> sup) {
        if (this.seenRegisterEvent) {
            throw new IllegalStateException("Cannot register new entries to DeferredRegister after RegisterEvent has been fired.");
        } else {
            Objects.requireNonNull(name);
            Objects.requireNonNull(sup);
            ResourceLocation key = new ResourceLocation(this.modid, name);
            if (this.registryKey != null) {
                RegistryObject<I> ret = this.optionalRegistry ? RegistryObject.createOptional(key, this.registryKey, this.modid) : RegistryObject.create(key, this.registryKey, this.modid);
                if (this.entries.putIfAbsent(ret, sup) != null) {
                    throw new IllegalArgumentException("Duplicate registration " + name);
                } else {
                    return ret;
                }
            } else {
                throw new IllegalStateException("Could not create RegistryObject in DeferredRegister");
            }
        }
    }

    public Supplier<IForgeRegistry<T>> makeRegistry(Supplier<RegistryBuilder<T>> sup) {
        return this.makeRegistry(this.registryKey.location(), sup);
    }

    @NotNull
    public TagKey<T> createTagKey(@NotNull String path) {
        Objects.requireNonNull(path);
        return this.createTagKey(new ResourceLocation(this.modid, path));
    }

    @NotNull
    public TagKey<T> createTagKey(@NotNull ResourceLocation location) {
        if (this.registryKey == null) {
            throw new IllegalStateException("The registry name was not set, cannot create a tag key");
        } else {
            Objects.requireNonNull(location);
            return TagKey.create(this.registryKey, location);
        }
    }

    @NotNull
    public TagKey<T> createOptionalTagKey(@NotNull String path, @NotNull Set<? extends Supplier<T>> defaults) {
        Objects.requireNonNull(path);
        return this.createOptionalTagKey(new ResourceLocation(this.modid, path), defaults);
    }

    @NotNull
    public TagKey<T> createOptionalTagKey(@NotNull ResourceLocation location, @NotNull Set<? extends Supplier<T>> defaults) {
        TagKey<T> tagKey = this.createTagKey(location);
        this.addOptionalTagDefaults(tagKey, defaults);
        return tagKey;
    }

    public void addOptionalTagDefaults(@NotNull TagKey<T> name, @NotNull Set<? extends Supplier<T>> defaults) {
        Objects.requireNonNull(defaults);
        if (this.optionalTags == null) {
            this.optionalTags = Multimaps.newSetMultimap(new IdentityHashMap(), HashSet::new);
        }
        this.optionalTags.putAll(name, defaults);
    }

    public void register(IEventBus bus) {
        bus.register(new DeferredRegister.EventDispatcher(this));
        if (this.registryFactory != null) {
            bus.addListener(this::createRegistry);
        }
    }

    public Collection<RegistryObject<T>> getEntries() {
        return this.entriesView;
    }

    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.registryKey;
    }

    @NotNull
    public ResourceLocation getRegistryName() {
        return ((ResourceKey) Objects.requireNonNull(this.registryKey)).location();
    }

    private Supplier<IForgeRegistry<T>> makeRegistry(ResourceLocation registryName, Supplier<RegistryBuilder<T>> sup) {
        if (registryName == null) {
            throw new IllegalStateException("Cannot create a registry without specifying a registry name");
        } else if (RegistryManager.ACTIVE.getRegistry(registryName) == null && this.registryFactory == null) {
            this.registryFactory = () -> ((RegistryBuilder) sup.get()).setName(registryName);
            return new DeferredRegister.RegistryHolder<>(this.registryKey);
        } else {
            throw new IllegalStateException("Cannot create a registry for a type that already exists");
        }
    }

    private void onFill(IForgeRegistry<?> registry) {
        if (this.optionalTags != null) {
            ITagManager<T> tagManager = (ITagManager<T>) registry.tags();
            if (tagManager == null) {
                throw new IllegalStateException("The forge registry " + registry.getRegistryName() + " does not support tags, but optional tags were registered!");
            } else {
                Multimaps.asMap(this.optionalTags).forEach(tagManager::addOptionalTagDefaults);
            }
        }
    }

    private void addEntries(RegisterEvent event) {
        if (event.getRegistryKey().equals(this.registryKey)) {
            this.seenRegisterEvent = true;
            for (Entry<RegistryObject<T>, Supplier<? extends T>> e : this.entries.entrySet()) {
                event.register(this.registryKey, ((RegistryObject) e.getKey()).getId(), () -> ((Supplier) e.getValue()).get());
                ((RegistryObject) e.getKey()).updateReference(event);
            }
        }
    }

    private void createRegistry(NewRegistryEvent event) {
        event.create((RegistryBuilder) this.registryFactory.get(), this::onFill);
    }

    public static class EventDispatcher {

        private final DeferredRegister<?> register;

        public EventDispatcher(DeferredRegister<?> register) {
            this.register = register;
        }

        @SubscribeEvent
        public void handleEvent(RegisterEvent event) {
            this.register.addEntries(event);
        }
    }

    private static class RegistryHolder<V> implements Supplier<IForgeRegistry<V>> {

        private final ResourceKey<? extends Registry<V>> registryKey;

        private IForgeRegistry<V> registry = null;

        private RegistryHolder(ResourceKey<? extends Registry<V>> registryKey) {
            this.registryKey = registryKey;
        }

        public IForgeRegistry<V> get() {
            if (this.registry == null) {
                this.registry = RegistryManager.ACTIVE.getRegistry(this.registryKey);
            }
            return this.registry;
        }
    }
}