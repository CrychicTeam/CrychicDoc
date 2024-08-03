package net.minecraft.core;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.serialization.Lifecycle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class RegistrySetBuilder {

    private final List<RegistrySetBuilder.RegistryStub<?>> entries = new ArrayList();

    static <T> HolderGetter<T> wrapContextLookup(final HolderLookup.RegistryLookup<T> holderLookupRegistryLookupT0) {
        return new RegistrySetBuilder.EmptyTagLookup<T>(holderLookupRegistryLookupT0) {

            @Override
            public Optional<Holder.Reference<T>> get(ResourceKey<T> p_255765_) {
                return holderLookupRegistryLookupT0.m_254902_(p_255765_);
            }
        };
    }

    public <T> RegistrySetBuilder add(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0, Lifecycle lifecycle1, RegistrySetBuilder.RegistryBootstrap<T> registrySetBuilderRegistryBootstrapT2) {
        this.entries.add(new RegistrySetBuilder.RegistryStub<>(resourceKeyExtendsRegistryT0, lifecycle1, registrySetBuilderRegistryBootstrapT2));
        return this;
    }

    public <T> RegistrySetBuilder add(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0, RegistrySetBuilder.RegistryBootstrap<T> registrySetBuilderRegistryBootstrapT1) {
        return this.add(resourceKeyExtendsRegistryT0, Lifecycle.stable(), registrySetBuilderRegistryBootstrapT1);
    }

    private RegistrySetBuilder.BuildState createState(RegistryAccess registryAccess0) {
        RegistrySetBuilder.BuildState $$1 = RegistrySetBuilder.BuildState.create(registryAccess0, this.entries.stream().map(RegistrySetBuilder.RegistryStub::f_254738_));
        this.entries.forEach(p_255629_ -> p_255629_.apply($$1));
        return $$1;
    }

    public HolderLookup.Provider build(RegistryAccess registryAccess0) {
        RegistrySetBuilder.BuildState $$1 = this.createState(registryAccess0);
        Stream<HolderLookup.RegistryLookup<?>> $$2 = registryAccess0.registries().map(p_258195_ -> p_258195_.value().asLookup());
        Stream<HolderLookup.RegistryLookup<?>> $$3 = this.entries.stream().map(p_255700_ -> p_255700_.collectChanges($$1).buildAsLookup());
        HolderLookup.Provider $$4 = HolderLookup.Provider.create(Stream.concat($$2, $$3.peek($$1::m_254987_)));
        $$1.reportRemainingUnreferencedValues();
        $$1.throwOnError();
        return $$4;
    }

    public HolderLookup.Provider buildPatch(RegistryAccess registryAccess0, HolderLookup.Provider holderLookupProvider1) {
        RegistrySetBuilder.BuildState $$2 = this.createState(registryAccess0);
        Map<ResourceKey<? extends Registry<?>>, RegistrySetBuilder.RegistryContents<?>> $$3 = new HashMap();
        $$2.collectReferencedRegistries().forEach(p_272339_ -> $$3.put(p_272339_.key, p_272339_));
        this.entries.stream().map(p_272337_ -> p_272337_.collectChanges($$2)).forEach(p_272341_ -> $$3.put(p_272341_.key, p_272341_));
        Stream<HolderLookup.RegistryLookup<?>> $$4 = registryAccess0.registries().map(p_258194_ -> p_258194_.value().asLookup());
        HolderLookup.Provider $$5 = HolderLookup.Provider.create(Stream.concat($$4, $$3.values().stream().map(RegistrySetBuilder.RegistryContents::m_254889_).peek($$2::m_254987_)));
        $$2.fillMissingHolders(holderLookupProvider1);
        $$2.reportRemainingUnreferencedValues();
        $$2.throwOnError();
        return $$5;
    }

    static record BuildState(RegistrySetBuilder.CompositeOwner f_254680_, RegistrySetBuilder.UniversalLookup f_254749_, Map<ResourceLocation, HolderGetter<?>> f_254690_, Map<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> f_254644_, List<RuntimeException> f_254627_) {

        private final RegistrySetBuilder.CompositeOwner owner;

        private final RegistrySetBuilder.UniversalLookup lookup;

        private final Map<ResourceLocation, HolderGetter<?>> registries;

        private final Map<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> registeredValues;

        private final List<RuntimeException> errors;

        private BuildState(RegistrySetBuilder.CompositeOwner f_254680_, RegistrySetBuilder.UniversalLookup f_254749_, Map<ResourceLocation, HolderGetter<?>> f_254690_, Map<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> f_254644_, List<RuntimeException> f_254627_) {
            this.owner = f_254680_;
            this.lookup = f_254749_;
            this.registries = f_254690_;
            this.registeredValues = f_254644_;
            this.errors = f_254627_;
        }

        public static RegistrySetBuilder.BuildState create(RegistryAccess p_255995_, Stream<ResourceKey<? extends Registry<?>>> p_256495_) {
            RegistrySetBuilder.CompositeOwner $$2 = new RegistrySetBuilder.CompositeOwner();
            List<RuntimeException> $$3 = new ArrayList();
            RegistrySetBuilder.UniversalLookup $$4 = new RegistrySetBuilder.UniversalLookup($$2);
            Builder<ResourceLocation, HolderGetter<?>> $$5 = ImmutableMap.builder();
            p_255995_.registries().forEach(p_258197_ -> $$5.put(p_258197_.key().location(), RegistrySetBuilder.wrapContextLookup(p_258197_.value().asLookup())));
            p_256495_.forEach(p_256603_ -> $$5.put(p_256603_.location(), $$4));
            return new RegistrySetBuilder.BuildState($$2, $$4, $$5.build(), new HashMap(), $$3);
        }

        public <T> BootstapContext<T> bootstapContext() {
            return new BootstapContext<T>() {

                @Override
                public Holder.Reference<T> register(ResourceKey<T> p_256176_, T p_256422_, Lifecycle p_255924_) {
                    RegistrySetBuilder.RegisteredValue<?> $$3 = (RegistrySetBuilder.RegisteredValue<?>) BuildState.this.registeredValues.put(p_256176_, new RegistrySetBuilder.RegisteredValue(p_256422_, p_255924_));
                    if ($$3 != null) {
                        BuildState.this.errors.add(new IllegalStateException("Duplicate registration for " + p_256176_ + ", new=" + p_256422_ + ", old=" + $$3.value));
                    }
                    return BuildState.this.lookup.getOrCreate(p_256176_);
                }

                @Override
                public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> p_255961_) {
                    return (HolderGetter<S>) BuildState.this.registries.getOrDefault(p_255961_.location(), BuildState.this.lookup);
                }
            };
        }

        public void reportRemainingUnreferencedValues() {
            for (ResourceKey<Object> $$0 : this.lookup.holders.keySet()) {
                this.errors.add(new IllegalStateException("Unreferenced key: " + $$0));
            }
            this.registeredValues.forEach((p_256143_, p_256662_) -> this.errors.add(new IllegalStateException("Orpaned value " + p_256662_.value + " for key " + p_256143_)));
        }

        public void throwOnError() {
            if (!this.errors.isEmpty()) {
                IllegalStateException $$0 = new IllegalStateException("Errors during registry creation");
                for (RuntimeException $$1 : this.errors) {
                    $$0.addSuppressed($$1);
                }
                throw $$0;
            }
        }

        public void addOwner(HolderOwner<?> p_256407_) {
            this.owner.add(p_256407_);
        }

        public void fillMissingHolders(HolderLookup.Provider p_255679_) {
            Map<ResourceLocation, Optional<? extends HolderLookup<Object>>> $$1 = new HashMap();
            Iterator<Entry<ResourceKey<Object>, Holder.Reference<Object>>> $$2 = this.lookup.holders.entrySet().iterator();
            while ($$2.hasNext()) {
                Entry<ResourceKey<Object>, Holder.Reference<Object>> $$3 = (Entry<ResourceKey<Object>, Holder.Reference<Object>>) $$2.next();
                ResourceKey<Object> $$4 = (ResourceKey<Object>) $$3.getKey();
                Holder.Reference<Object> $$5 = (Holder.Reference<Object>) $$3.getValue();
                ((Optional) $$1.computeIfAbsent($$4.registry(), p_255896_ -> p_255679_.lookup(ResourceKey.createRegistryKey(p_255896_)))).flatMap(p_256068_ -> p_256068_.m_254902_($$4)).ifPresent(p_256030_ -> {
                    $$5.bindValue(p_256030_.value());
                    $$2.remove();
                });
            }
        }

        public Stream<RegistrySetBuilder.RegistryContents<?>> collectReferencedRegistries() {
            return this.lookup.holders.keySet().stream().map(ResourceKey::m_211136_).distinct().map(p_272342_ -> new RegistrySetBuilder.RegistryContents(ResourceKey.createRegistryKey(p_272342_), Lifecycle.stable(), Map.of()));
        }
    }

    static class CompositeOwner implements HolderOwner<Object> {

        private final Set<HolderOwner<?>> owners = Sets.newIdentityHashSet();

        @Override
        public boolean canSerializeIn(HolderOwner<Object> holderOwnerObject0) {
            return this.owners.contains(holderOwnerObject0);
        }

        public void add(HolderOwner<?> holderOwner0) {
            this.owners.add(holderOwner0);
        }
    }

    abstract static class EmptyTagLookup<T> implements HolderGetter<T> {

        protected final HolderOwner<T> owner;

        protected EmptyTagLookup(HolderOwner<T> holderOwnerT0) {
            this.owner = holderOwnerT0;
        }

        @Override
        public Optional<HolderSet.Named<T>> get(TagKey<T> tagKeyT0) {
            return Optional.of(HolderSet.emptyNamed(this.owner, tagKeyT0));
        }
    }

    static record RegisteredValue<T>(T f_254685_, Lifecycle f_254641_) {

        private final T value;

        private final Lifecycle lifecycle;

        RegisteredValue(T f_254685_, Lifecycle f_254641_) {
            this.value = f_254685_;
            this.lifecycle = f_254641_;
        }
    }

    @FunctionalInterface
    public interface RegistryBootstrap<T> {

        void run(BootstapContext<T> var1);
    }

    static record RegistryContents<T>(ResourceKey<? extends Registry<? extends T>> f_271195_, Lifecycle f_271144_, Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> f_254715_) {

        private final ResourceKey<? extends Registry<? extends T>> key;

        private final Lifecycle lifecycle;

        private final Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> values;

        RegistryContents(ResourceKey<? extends Registry<? extends T>> f_271195_, Lifecycle f_271144_, Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> f_254715_) {
            this.key = f_271195_;
            this.lifecycle = f_271144_;
            this.values = f_254715_;
        }

        public HolderLookup.RegistryLookup<T> buildAsLookup() {
            return new HolderLookup.RegistryLookup<T>() {

                private final Map<ResourceKey<T>, Holder.Reference<T>> entries = (Map<ResourceKey<T>, Holder.Reference<T>>) RegistryContents.this.values.entrySet().stream().collect(Collectors.toUnmodifiableMap(Entry::getKey, p_256193_ -> {
                    RegistrySetBuilder.ValueAndHolder<T> $$1 = (RegistrySetBuilder.ValueAndHolder<T>) p_256193_.getValue();
                    Holder.Reference<T> $$2 = (Holder.Reference<T>) $$1.holder().orElseGet(() -> Holder.Reference.createStandAlone(this, (ResourceKey<T>) p_256193_.getKey()));
                    $$2.bindValue($$1.value().value());
                    return $$2;
                }));

                @Override
                public ResourceKey<? extends Registry<? extends T>> key() {
                    return RegistryContents.this.key;
                }

                @Override
                public Lifecycle registryLifecycle() {
                    return RegistryContents.this.lifecycle;
                }

                @Override
                public Optional<Holder.Reference<T>> get(ResourceKey<T> p_255760_) {
                    return Optional.ofNullable((Holder.Reference) this.entries.get(p_255760_));
                }

                @Override
                public Stream<Holder.Reference<T>> listElements() {
                    return this.entries.values().stream();
                }

                @Override
                public Optional<HolderSet.Named<T>> get(TagKey<T> p_255810_) {
                    return Optional.empty();
                }

                @Override
                public Stream<HolderSet.Named<T>> listTags() {
                    return Stream.empty();
                }
            };
        }
    }

    static record RegistryStub<T>(ResourceKey<? extends Registry<T>> f_254738_, Lifecycle f_254728_, RegistrySetBuilder.RegistryBootstrap<T> f_254689_) {

        private final ResourceKey<? extends Registry<T>> key;

        private final Lifecycle lifecycle;

        private final RegistrySetBuilder.RegistryBootstrap<T> bootstrap;

        RegistryStub(ResourceKey<? extends Registry<T>> f_254738_, Lifecycle f_254728_, RegistrySetBuilder.RegistryBootstrap<T> f_254689_) {
            this.key = f_254738_;
            this.lifecycle = f_254728_;
            this.bootstrap = f_254689_;
        }

        void apply(RegistrySetBuilder.BuildState p_256272_) {
            this.bootstrap.run(p_256272_.bootstapContext());
        }

        public RegistrySetBuilder.RegistryContents<T> collectChanges(RegistrySetBuilder.BuildState p_256416_) {
            Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> $$1 = new HashMap();
            Iterator<Entry<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>>> $$2 = p_256416_.registeredValues.entrySet().iterator();
            while ($$2.hasNext()) {
                Entry<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> $$3 = (Entry<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>>) $$2.next();
                ResourceKey<?> $$4 = (ResourceKey<?>) $$3.getKey();
                if ($$4.isFor(this.key)) {
                    RegistrySetBuilder.RegisteredValue<T> $$6 = (RegistrySetBuilder.RegisteredValue<T>) $$3.getValue();
                    Holder.Reference<T> $$7 = (Holder.Reference<T>) p_256416_.lookup.holders.remove($$4);
                    $$1.put($$4, new RegistrySetBuilder.ValueAndHolder<>($$6, Optional.ofNullable($$7)));
                    $$2.remove();
                }
            }
            return new RegistrySetBuilder.RegistryContents<>(this.key, this.lifecycle, $$1);
        }
    }

    static class UniversalLookup extends RegistrySetBuilder.EmptyTagLookup<Object> {

        final Map<ResourceKey<Object>, Holder.Reference<Object>> holders = new HashMap();

        public UniversalLookup(HolderOwner<Object> holderOwnerObject0) {
            super(holderOwnerObject0);
        }

        @Override
        public Optional<Holder.Reference<Object>> get(ResourceKey<Object> resourceKeyObject0) {
            return Optional.of(this.getOrCreate(resourceKeyObject0));
        }

        <T> Holder.Reference<T> getOrCreate(ResourceKey<T> resourceKeyT0) {
            return (Holder.Reference<T>) this.holders.computeIfAbsent(resourceKeyT0, p_256154_ -> Holder.Reference.createStandAlone(this.f_254742_, p_256154_));
        }
    }

    static record ValueAndHolder<T>(RegistrySetBuilder.RegisteredValue<T> f_254683_, Optional<Holder.Reference<T>> f_254632_) {

        private final RegistrySetBuilder.RegisteredValue<T> value;

        private final Optional<Holder.Reference<T>> holder;

        ValueAndHolder(RegistrySetBuilder.RegisteredValue<T> f_254683_, Optional<Holder.Reference<T>> f_254632_) {
            this.value = f_254683_;
            this.holder = f_254632_;
        }
    }
}