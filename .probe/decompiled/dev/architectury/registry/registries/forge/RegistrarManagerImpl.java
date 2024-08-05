package dev.architectury.registry.registries.forge;

import com.google.common.base.Objects;
import com.google.common.base.Suppliers;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarBuilder;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.registry.registries.options.DefaultIdRegistrarOption;
import dev.architectury.registry.registries.options.RegistrarOption;
import dev.architectury.registry.registries.options.StandardRegistrarOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegistrarManagerImpl {

    private static final Logger LOGGER = LogManager.getLogger(RegistrarManagerImpl.class);

    private static final Multimap<RegistrarManagerImpl.RegistryEntryId<?>, Consumer<?>> LISTENERS = HashMultimap.create();

    private static void listen(ResourceKey<?> resourceKey, ResourceLocation id, Consumer<?> listener, boolean vanilla) {
        LISTENERS.put(new RegistrarManagerImpl.RegistryEntryId<>(resourceKey, id), listener);
    }

    public static RegistrarManager.RegistryProvider _get(String modId) {
        return new RegistrarManagerImpl.RegistryProviderImpl(modId);
    }

    public static class Data<T> {

        private boolean registered = false;

        private final Map<ResourceLocation, Supplier<? extends T>> objects = new LinkedHashMap();

        public void registerForForge(IForgeRegistry<T> registry, ResourceLocation location, Object[] objectArr, Supplier<? extends T> reference) {
            if (!this.registered) {
                this.objects.put(location, (Supplier) () -> {
                    T valuex = (T) reference.get();
                    objectArr[0] = valuex;
                    return valuex;
                });
            } else {
                ResourceKey<? extends Registry<Object>> resourceKey = ResourceKey.createRegistryKey(registry.getRegistryName());
                T value = (T) reference.get();
                registry.register(location, value);
                objectArr[0] = value;
                RegistrarManagerImpl.RegistryEntryId<?> registryEntryId = new RegistrarManagerImpl.RegistryEntryId<>(resourceKey, location);
                for (Consumer<?> consumer : RegistrarManagerImpl.LISTENERS.get(registryEntryId)) {
                    consumer.accept(value);
                }
                RegistrarManagerImpl.LISTENERS.removeAll(registryEntryId);
            }
        }

        public void register(Registry<T> registry, ResourceLocation location, Supplier<? extends T> reference) {
            if (!this.registered) {
                this.objects.put(location, reference);
            } else {
                T value = (T) reference.get();
                Registry.register(registry, location, value);
                RegistrarManagerImpl.RegistryEntryId<?> registryEntryId = new RegistrarManagerImpl.RegistryEntryId<>(registry.key(), location);
                for (Consumer<?> consumer : RegistrarManagerImpl.LISTENERS.get(registryEntryId)) {
                    consumer.accept(value);
                }
                RegistrarManagerImpl.LISTENERS.removeAll(registryEntryId);
            }
        }
    }

    public static class DelegatedRegistrar<T> implements Registrar<T> {

        private final String modId;

        private final Supplier<Registrar<T>> delegate;

        private final ResourceLocation registryId;

        private List<Runnable> onRegister = new ArrayList();

        public DelegatedRegistrar(String modId, Supplier<Registrar<T>> delegate, ResourceLocation registryId) {
            this.modId = modId;
            this.delegate = delegate;
            this.registryId = registryId;
        }

        public void onRegister() {
            if (this.onRegister != null) {
                for (Runnable runnable : this.onRegister) {
                    runnable.run();
                }
            }
            this.onRegister = null;
        }

        public boolean isReady() {
            return this.onRegister == null;
        }

        @Override
        public RegistrySupplier<T> delegate(ResourceLocation id) {
            return this.isReady() ? ((Registrar) this.delegate.get()).delegate(id) : new RegistrySupplier<T>() {

                @Override
                public RegistrarManager getRegistrarManager() {
                    return RegistrarManager.get(DelegatedRegistrar.this.modId);
                }

                @Override
                public Registrar<T> getRegistrar() {
                    return DelegatedRegistrar.this;
                }

                @Override
                public ResourceLocation getRegistryId() {
                    return DelegatedRegistrar.this.key().location();
                }

                @Override
                public ResourceLocation getId() {
                    return id;
                }

                @Override
                public boolean isPresent() {
                    return DelegatedRegistrar.this.isReady() && ((Registrar) DelegatedRegistrar.this.delegate.get()).contains(id);
                }

                public T get() {
                    return (T) (DelegatedRegistrar.this.isReady() ? ((Registrar) DelegatedRegistrar.this.delegate.get()).get(id) : null);
                }
            };
        }

        @Override
        public <E extends T> RegistrySupplier<E> register(ResourceLocation id, Supplier<E> supplier) {
            if (this.isReady()) {
                return ((Registrar) this.delegate.get()).register(id, supplier);
            } else {
                this.onRegister.add((Runnable) () -> ((Registrar) this.delegate.get()).register(id, supplier));
                return this.delegate(id);
            }
        }

        @Nullable
        @Override
        public ResourceLocation getId(T obj) {
            return !this.isReady() ? null : ((Registrar) this.delegate.get()).getId(obj);
        }

        @Override
        public int getRawId(T obj) {
            return !this.isReady() ? -1 : ((Registrar) this.delegate.get()).getRawId(obj);
        }

        @Override
        public Optional<ResourceKey<T>> getKey(T obj) {
            return !this.isReady() ? Optional.empty() : ((Registrar) this.delegate.get()).getKey(obj);
        }

        @Nullable
        @Override
        public T get(ResourceLocation id) {
            return (T) (!this.isReady() ? null : ((Registrar) this.delegate.get()).get(id));
        }

        @Nullable
        @Override
        public T byRawId(int rawId) {
            return (T) (!this.isReady() ? null : ((Registrar) this.delegate.get()).byRawId(rawId));
        }

        @Override
        public boolean contains(ResourceLocation id) {
            return this.isReady() && ((Registrar) this.delegate.get()).contains(id);
        }

        @Override
        public boolean containsValue(T obj) {
            return this.isReady() && ((Registrar) this.delegate.get()).containsValue(obj);
        }

        @Override
        public Set<ResourceLocation> getIds() {
            return this.isReady() ? ((Registrar) this.delegate.get()).getIds() : Collections.emptySet();
        }

        @Override
        public Set<Entry<ResourceKey<T>, T>> entrySet() {
            return this.isReady() ? ((Registrar) this.delegate.get()).entrySet() : Collections.emptySet();
        }

        @Override
        public ResourceKey<? extends Registry<T>> key() {
            return this.isReady() ? ((Registrar) this.delegate.get()).key() : ResourceKey.createRegistryKey(this.registryId);
        }

        @Override
        public void listen(ResourceLocation id, Consumer<T> callback) {
            if (this.isReady()) {
                ((Registrar) this.delegate.get()).listen(id, callback);
            } else {
                this.onRegister.add((Runnable) () -> ((Registrar) this.delegate.get()).listen(id, callback));
            }
        }

        @NotNull
        public Iterator<T> iterator() {
            return this.isReady() ? ((Registrar) this.delegate.get()).iterator() : Collections.emptyIterator();
        }
    }

    public static class ForgeBackedRegistryImpl<T> implements Registrar<T> {

        private final String modId;

        private IForgeRegistry<T> delegate;

        private Map<ResourceKey<? extends Registry<?>>, RegistrarManagerImpl.Data<?>> registry;

        public ForgeBackedRegistryImpl(String modId, Map<ResourceKey<? extends Registry<?>>, RegistrarManagerImpl.Data<?>> registry, IForgeRegistry<T> delegate) {
            this.modId = modId;
            this.registry = registry;
            this.delegate = delegate;
        }

        @Override
        public RegistrySupplier<T> delegate(ResourceLocation id) {
            final Supplier<T> value = Suppliers.memoize(() -> this.get(id));
            final Registrar<T> registrar = this;
            return new RegistrySupplier<T>() {

                @Override
                public RegistrarManager getRegistrarManager() {
                    return RegistrarManager.get(ForgeBackedRegistryImpl.this.modId);
                }

                @Override
                public Registrar<T> getRegistrar() {
                    return registrar;
                }

                @Override
                public ResourceLocation getRegistryId() {
                    return ForgeBackedRegistryImpl.this.delegate.getRegistryName();
                }

                @Override
                public ResourceLocation getId() {
                    return id;
                }

                @Override
                public boolean isPresent() {
                    return ForgeBackedRegistryImpl.this.contains(id);
                }

                public T get() {
                    return (T) value.get();
                }

                public int hashCode() {
                    return Objects.hashCode(new Object[] { this.getRegistryId(), this.getId() });
                }

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    } else {
                        return !(obj instanceof RegistrySupplier<?> other) ? false : other.getRegistryId().equals(this.getRegistryId()) && other.getId().equals(this.getId());
                    }
                }

                public String toString() {
                    return this.getRegistryId().toString() + "@" + id.toString();
                }
            };
        }

        @Override
        public <E extends T> RegistrySupplier<E> register(ResourceLocation id, Supplier<E> supplier) {
            final Object[] objectArr = new Object[] { null };
            RegistrarManagerImpl.Data<T> data = (RegistrarManagerImpl.Data<T>) this.registry.computeIfAbsent(this.key(), type -> new RegistrarManagerImpl.Data());
            data.registerForForge(this.delegate, id, objectArr, supplier);
            final Registrar<T> registrar = this;
            return new RegistrySupplier<E>() {

                @Override
                public RegistrarManager getRegistrarManager() {
                    return RegistrarManager.get(ForgeBackedRegistryImpl.this.modId);
                }

                @Override
                public Registrar<E> getRegistrar() {
                    return registrar;
                }

                @Override
                public ResourceLocation getRegistryId() {
                    return ForgeBackedRegistryImpl.this.delegate.getRegistryName();
                }

                @Override
                public ResourceLocation getId() {
                    return id;
                }

                @Override
                public boolean isPresent() {
                    return objectArr[0] != null;
                }

                public E get() {
                    E value = (E) objectArr[0];
                    if (value == null) {
                        throw new NullPointerException("Value missing: " + this.getId() + "@" + this.getRegistryId());
                    } else {
                        return value;
                    }
                }

                public int hashCode() {
                    return Objects.hashCode(new Object[] { this.getRegistryId(), this.getId() });
                }

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    } else {
                        return !(obj instanceof RegistrySupplier<?> other) ? false : other.getRegistryId().equals(this.getRegistryId()) && other.getId().equals(this.getId());
                    }
                }

                public String toString() {
                    return this.getRegistryId().toString() + "@" + id.toString();
                }
            };
        }

        @Nullable
        @Override
        public ResourceLocation getId(T obj) {
            return this.delegate.getKey(obj);
        }

        @Override
        public int getRawId(T obj) {
            return ((ForgeRegistry) this.delegate).getID(obj);
        }

        @Override
        public Optional<ResourceKey<T>> getKey(T t) {
            return Optional.ofNullable(this.getId(t)).map(id -> ResourceKey.create(this.key(), id));
        }

        @Nullable
        @Override
        public T get(ResourceLocation id) {
            return this.delegate.getValue(id);
        }

        @Override
        public T byRawId(int rawId) {
            return ((ForgeRegistry) this.delegate).getValue(rawId);
        }

        @Override
        public boolean contains(ResourceLocation resourceLocation) {
            return this.delegate.containsKey(resourceLocation);
        }

        @Override
        public boolean containsValue(T t) {
            return this.delegate.containsValue(t);
        }

        @Override
        public Set<ResourceLocation> getIds() {
            return this.delegate.getKeys();
        }

        @Override
        public Set<Entry<ResourceKey<T>, T>> entrySet() {
            return this.delegate.getEntries();
        }

        @Override
        public ResourceKey<? extends Registry<T>> key() {
            return ResourceKey.createRegistryKey(this.delegate.getRegistryName());
        }

        public Iterator<T> iterator() {
            return this.delegate.iterator();
        }

        @Override
        public void listen(ResourceLocation id, Consumer<T> callback) {
            if (this.contains(id)) {
                callback.accept(this.get(id));
            } else {
                RegistrarManagerImpl.listen(this.key(), id, callback, false);
            }
        }
    }

    public static class RegistryBuilderWrapper<T> implements RegistrarBuilder<T> {

        private final RegistrarManagerImpl.RegistryProviderImpl provider;

        private final RegistryBuilder<?> builder;

        private final ResourceLocation registryId;

        private boolean saveToDisk = false;

        private boolean syncToClients = false;

        public RegistryBuilderWrapper(RegistrarManagerImpl.RegistryProviderImpl provider, RegistryBuilder<?> builder, ResourceLocation registryId) {
            this.provider = provider;
            this.builder = builder;
            this.registryId = registryId;
        }

        @Override
        public Registrar<T> build() {
            if (!this.syncToClients) {
                this.builder.disableSync();
            }
            if (!this.saveToDisk) {
                this.builder.disableSaving();
            }
            if (this.provider.builders == null) {
                throw new IllegalStateException("Cannot create registries when registries are already aggregated!");
            } else {
                Registrar<?>[] registrarRef = new Registrar[1];
                RegistrarManagerImpl.DelegatedRegistrar<T> registrar = new RegistrarManagerImpl.DelegatedRegistrar<>(this.provider.modId, () -> (Registrar) java.util.Objects.requireNonNull(registrarRef[0], "Registry not yet initialized!"), this.registryId);
                RegistrarManagerImpl.RegistryProviderImpl.RegistryBuilderEntry entry = new RegistrarManagerImpl.RegistryProviderImpl.RegistryBuilderEntry(this.builder, forgeRegistry -> {
                    registrarRef[0] = this.provider.get(forgeRegistry);
                    registrar.onRegister();
                });
                this.provider.builders.add(entry);
                RegistrarManagerImpl.RegistryProviderImpl.CUSTOM_REGS.put(registrar.key(), registrar);
                return registrar;
            }
        }

        @Override
        public RegistrarBuilder<T> option(RegistrarOption option) {
            if (option == StandardRegistrarOption.SAVE_TO_DISC) {
                this.saveToDisk = true;
            } else if (option == StandardRegistrarOption.SYNC_TO_CLIENTS) {
                this.syncToClients = true;
            } else if (option instanceof DefaultIdRegistrarOption opt) {
                this.builder.setDefaultKey(opt.defaultId());
            }
            return this;
        }
    }

    public static record RegistryEntryId<T>(ResourceKey<T> registryKey, ResourceLocation id) {

        public String toString() {
            return "Registry Entry [%s / %s]".formatted(this.registryKey.location(), this.id);
        }
    }

    public static class RegistryProviderImpl implements RegistrarManager.RegistryProvider {

        private static final Map<ResourceKey<Registry<?>>, Registrar<?>> CUSTOM_REGS = new HashMap();

        private final String modId;

        private final Supplier<IEventBus> eventBus;

        private final Map<ResourceKey<? extends Registry<?>>, RegistrarManagerImpl.Data<?>> registry = new HashMap();

        private final Multimap<ResourceKey<Registry<?>>, Consumer<Registrar<?>>> listeners = HashMultimap.create();

        @Nullable
        private List<RegistrarManagerImpl.RegistryProviderImpl.RegistryBuilderEntry> builders = new ArrayList();

        public RegistryProviderImpl(String modId) {
            this.modId = modId;
            this.eventBus = Suppliers.memoize(() -> {
                IEventBus eventBus = (IEventBus) EventBuses.getModEventBus(modId).orElseThrow(() -> new IllegalStateException("Can't get event bus for mod '" + modId + "' because it was not registered!"));
                eventBus.register(new RegistrarManagerImpl.RegistryProviderImpl.EventListener());
                return eventBus;
            });
        }

        private void updateEventBus() {
            synchronized (this.eventBus) {
                this.eventBus.get();
            }
        }

        @Override
        public <T> Registrar<T> get(ResourceKey<Registry<T>> registryKey) {
            this.updateEventBus();
            ForgeRegistry<T> registry = RegistryManager.ACTIVE.getRegistry(registryKey.location());
            if (registry == null) {
                Registry<T> ts = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
                if (ts != null) {
                    return this.get(ts);
                } else {
                    Registrar<?> customReg = (Registrar<?>) CUSTOM_REGS.get(registryKey);
                    if (customReg != null) {
                        return (Registrar<T>) customReg;
                    } else {
                        throw new IllegalArgumentException("Registry " + registryKey + " does not exist!");
                    }
                }
            } else {
                return this.get(registry);
            }
        }

        public <T> Registrar<T> get(IForgeRegistry<T> registry) {
            this.updateEventBus();
            return new RegistrarManagerImpl.ForgeBackedRegistryImpl<>(this.modId, this.registry, registry);
        }

        @Override
        public <T> Registrar<T> get(Registry<T> registry) {
            this.updateEventBus();
            return new RegistrarManagerImpl.VanillaBackedRegistryImpl<>(this.modId, this.registry, registry);
        }

        @Override
        public <T> void forRegistry(ResourceKey<Registry<T>> key, Consumer<Registrar<T>> consumer) {
            this.listeners.put(key, consumer);
        }

        @Override
        public <T> RegistrarBuilder<T> builder(Class<T> type, ResourceLocation registryId) {
            return new RegistrarManagerImpl.RegistryBuilderWrapper<>(this, new RegistryBuilder().setName(registryId), registryId);
        }

        public class EventListener {

            @SubscribeEvent
            public void handleEvent(RegisterEvent event) {
                for (Entry<ResourceKey<? extends Registry<?>>, RegistrarManagerImpl.Data<?>> typeDataEntry : RegistryProviderImpl.this.registry.entrySet()) {
                    if (((ResourceKey) typeDataEntry.getKey()).equals(event.getRegistryKey())) {
                        this.registerFor(event, (ResourceKey) typeDataEntry.getKey(), (RegistrarManagerImpl.Data) typeDataEntry.getValue());
                    }
                }
            }

            public <T> void registerFor(RegisterEvent event, ResourceKey<? extends Registry<T>> resourceKey, RegistrarManagerImpl.Data<T> data) {
                event.register(resourceKey, registry -> {
                    data.registered = true;
                    for (Entry<ResourceLocation, Supplier<? extends T>> entry : data.objects.entrySet()) {
                        ResourceLocation location = (ResourceLocation) entry.getKey();
                        T value = (T) ((Supplier) entry.getValue()).get();
                        registry.register(location, value);
                        RegistrarManagerImpl.RegistryEntryId<?> registryEntryId = new RegistrarManagerImpl.RegistryEntryId<>(resourceKey, location);
                        for (Consumer<?> consumer : RegistrarManagerImpl.LISTENERS.get(registryEntryId)) {
                            consumer.accept(value);
                        }
                        RegistrarManagerImpl.LISTENERS.removeAll(registryEntryId);
                    }
                    data.objects.clear();
                    Registrar<Object> archRegistry;
                    if (event.getForgeRegistry() != null) {
                        archRegistry = RegistryProviderImpl.this.get(event.getForgeRegistry());
                    } else {
                        if (event.getVanillaRegistry() == null) {
                            RegistrarManagerImpl.LOGGER.error("Unable to find registry from RegisterEvent as both vanilla and forge registries are null! " + event.getRegistryKey());
                            return;
                        }
                        archRegistry = RegistryProviderImpl.this.get(event.getVanillaRegistry());
                    }
                    for (Entry<ResourceKey<Registry<?>>, Consumer<Registrar<?>>> entry : RegistryProviderImpl.this.listeners.entries()) {
                        if (((ResourceKey) entry.getKey()).location().equals(resourceKey.location())) {
                            ((Consumer) entry.getValue()).accept(archRegistry);
                        }
                    }
                });
            }

            @SubscribeEvent(priority = EventPriority.LOWEST)
            public void handleEventPost(RegisterEvent event) {
                Registrar<Object> archRegistry;
                if (event.getForgeRegistry() != null) {
                    archRegistry = RegistryProviderImpl.this.get(event.getForgeRegistry());
                } else {
                    if (event.getVanillaRegistry() == null) {
                        RegistrarManagerImpl.LOGGER.error("Unable to find registry from RegisterEvent as both vanilla and forge registries are null! " + event.getRegistryKey());
                        return;
                    }
                    archRegistry = RegistryProviderImpl.this.get(event.getVanillaRegistry());
                }
                List<RegistrarManagerImpl.RegistryEntryId<?>> toRemove = new ArrayList();
                for (Entry<RegistrarManagerImpl.RegistryEntryId<?>, Collection<Consumer<?>>> entry : RegistrarManagerImpl.LISTENERS.asMap().entrySet()) {
                    if (((RegistrarManagerImpl.RegistryEntryId) entry.getKey()).registryKey.equals(event.getRegistryKey())) {
                        if (archRegistry.contains(((RegistrarManagerImpl.RegistryEntryId) entry.getKey()).id)) {
                            Object value = archRegistry.get(((RegistrarManagerImpl.RegistryEntryId) entry.getKey()).id);
                            for (Consumer<?> consumer : (Collection) entry.getValue()) {
                                consumer.accept(value);
                            }
                            toRemove.add((RegistrarManagerImpl.RegistryEntryId) entry.getKey());
                        } else {
                            RegistrarManagerImpl.LOGGER.warn("Registry entry listened {} was not realized!", entry.getKey());
                        }
                    }
                }
                for (RegistrarManagerImpl.RegistryEntryId<?> id : toRemove) {
                    RegistrarManagerImpl.LISTENERS.removeAll(id);
                }
            }

            @SubscribeEvent
            public void handleEvent(NewRegistryEvent event) {
                if (RegistryProviderImpl.this.builders != null) {
                    for (RegistrarManagerImpl.RegistryProviderImpl.RegistryBuilderEntry builder : RegistryProviderImpl.this.builders) {
                        event.create(builder.builder(), builder.forgeRegistry());
                    }
                    RegistryProviderImpl.this.builders = null;
                }
            }
        }

        static record RegistryBuilderEntry(RegistryBuilder<?> builder, Consumer<IForgeRegistry<?>> forgeRegistry) {
        }
    }

    public static class VanillaBackedRegistryImpl<T> implements Registrar<T> {

        private final String modId;

        private Registry<T> delegate;

        private Map<ResourceKey<? extends Registry<?>>, RegistrarManagerImpl.Data<?>> registry;

        public VanillaBackedRegistryImpl(String modId, Map<ResourceKey<? extends Registry<?>>, RegistrarManagerImpl.Data<?>> registry, Registry<T> delegate) {
            this.modId = modId;
            this.registry = registry;
            this.delegate = delegate;
        }

        @Override
        public RegistrySupplier<T> delegate(ResourceLocation id) {
            final Supplier<T> value = Suppliers.memoize(() -> this.get(id));
            final Registrar<T> registrar = this;
            return new RegistrySupplier<T>() {

                @Override
                public RegistrarManager getRegistrarManager() {
                    return RegistrarManager.get(VanillaBackedRegistryImpl.this.modId);
                }

                @Override
                public Registrar<T> getRegistrar() {
                    return registrar;
                }

                @Override
                public ResourceLocation getRegistryId() {
                    return VanillaBackedRegistryImpl.this.delegate.key().location();
                }

                @Override
                public ResourceLocation getId() {
                    return id;
                }

                @Override
                public boolean isPresent() {
                    return VanillaBackedRegistryImpl.this.contains(id);
                }

                public T get() {
                    return (T) value.get();
                }

                public int hashCode() {
                    return Objects.hashCode(new Object[] { this.getRegistryId(), this.getId() });
                }

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    } else {
                        return !(obj instanceof RegistrySupplier<?> other) ? false : other.getRegistryId().equals(this.getRegistryId()) && other.getId().equals(this.getId());
                    }
                }

                public String toString() {
                    return this.getRegistryId() + "@" + id.toString();
                }
            };
        }

        @Override
        public <E extends T> RegistrySupplier<E> register(ResourceLocation id, Supplier<E> supplier) {
            RegistrarManagerImpl.Data<T> data = (RegistrarManagerImpl.Data<T>) this.registry.computeIfAbsent(this.key(), type -> new RegistrarManagerImpl.Data());
            data.register(this.delegate, id, supplier);
            return this.delegate(id);
        }

        @Nullable
        @Override
        public ResourceLocation getId(T obj) {
            return this.delegate.getKey(obj);
        }

        @Override
        public int getRawId(T obj) {
            return this.delegate.getId(obj);
        }

        @Override
        public Optional<ResourceKey<T>> getKey(T t) {
            return this.delegate.getResourceKey(t);
        }

        @Nullable
        @Override
        public T get(ResourceLocation id) {
            return this.delegate.get(id);
        }

        @Override
        public T byRawId(int rawId) {
            return (T) this.delegate.m_7942_(rawId);
        }

        @Override
        public boolean contains(ResourceLocation resourceLocation) {
            return this.delegate.keySet().contains(resourceLocation);
        }

        @Override
        public boolean containsValue(T t) {
            return this.delegate.getResourceKey(t).isPresent();
        }

        @Override
        public Set<ResourceLocation> getIds() {
            return this.delegate.keySet();
        }

        @Override
        public Set<Entry<ResourceKey<T>, T>> entrySet() {
            return this.delegate.entrySet();
        }

        @Override
        public ResourceKey<? extends Registry<T>> key() {
            return this.delegate.key();
        }

        public Iterator<T> iterator() {
            return this.delegate.iterator();
        }

        @Override
        public void listen(ResourceLocation id, Consumer<T> callback) {
            if (this.contains(id)) {
                callback.accept(this.get(id));
            } else {
                RegistrarManagerImpl.listen(this.key(), id, callback, true);
            }
        }
    }
}