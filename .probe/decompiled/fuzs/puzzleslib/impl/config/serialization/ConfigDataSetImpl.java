package fuzs.puzzleslib.impl.config.serialization;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import fuzs.puzzleslib.api.event.v1.server.TagsUpdatedCallback;
import fuzs.puzzleslib.impl.PuzzlesLib;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ConfigDataSetImpl<T> implements ConfigDataSet<T> {

    private static final Set<Class<?>> SUPPORTED_DATA_TYPES = ImmutableSet.of(boolean.class, Boolean.class, int.class, Integer.class, double.class, Double.class, new Class[] { String.class });

    private final Registry<T> activeRegistry;

    private final List<ConfigDataSetImpl.EntryHolder<?, T>> values = Lists.newArrayList();

    private final BiPredicate<Integer, Object> filter;

    private final int dataSize;

    private Map<T, Object[]> dissolved;

    public ConfigDataSetImpl(Registry<T> registry, List<String> values, BiPredicate<Integer, Object> filter, Class<?>... types) {
        this.activeRegistry = registry;
        this.filter = filter;
        for (Class<?> clazz : types) {
            if (!SUPPORTED_DATA_TYPES.contains(clazz)) {
                throw new IllegalArgumentException("Data type of clazz %s is not supported".formatted(clazz));
            }
        }
        this.dataSize = types.length;
        for (String value : values) {
            this.deserialize(value, types).ifPresent(this.values::add);
        }
        TagsUpdatedCallback.EVENT.register((registryAccess, client) -> this.dissolved = null);
    }

    private static Object deserializeData(Class<?> clazz, String source) throws RuntimeException {
        if (clazz != boolean.class && clazz != Boolean.class) {
            if (clazz == int.class || clazz == Integer.class) {
                return Integer.parseInt(source);
            } else if (clazz == double.class || clazz == Double.class) {
                return Double.parseDouble(source);
            } else if (clazz == String.class) {
                return source;
            } else {
                throw new IllegalArgumentException("Data type of clazz %s is not supported".formatted(clazz));
            }
        } else if (source.equals("true")) {
            return true;
        } else if (source.equals("false")) {
            return false;
        } else {
            throw new IllegalArgumentException("%s is not a boolean value".formatted(source));
        }
    }

    @Override
    public Map<T, Object[]> toMap() {
        return this.dissolve();
    }

    @Override
    public Set<T> toSet() {
        return this.toMap().keySet();
    }

    public Iterator<T> iterator() {
        return this.toSet().iterator();
    }

    public int size() {
        return this.toMap().size();
    }

    public boolean isEmpty() {
        return this.toMap().isEmpty();
    }

    public boolean contains(Object o) {
        return this.toSet().contains(o);
    }

    @NotNull
    public Object[] toArray() {
        return this.toSet().toArray();
    }

    @NotNull
    public <T1> T1[] toArray(@NotNull T1[] a) {
        return (T1[]) this.toSet().toArray(a);
    }

    @Override
    public boolean add(T t) {
        return this.toSet().add(t);
    }

    @Override
    public boolean remove(Object o) {
        return this.toSet().remove(o);
    }

    public boolean containsAll(@NotNull Collection<?> c) {
        return this.toSet().containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return this.toSet().addAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return this.toSet().removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return this.toSet().retainAll(c);
    }

    @Override
    public void clear() {
        this.toMap().clear();
    }

    @Nullable
    @Override
    public Object[] get(T entry) {
        return (Object[]) this.toMap().get(entry);
    }

    @Override
    public <V> V get(T entry, int index) {
        Objects.checkIndex(index, this.dataSize);
        Object[] data = this.get(entry);
        Objects.requireNonNull(data, "data is null");
        return (V) data[index];
    }

    @Override
    public <V> Optional<V> getOptional(T entry, int index) {
        return index >= 0 && index < this.dataSize ? Optional.ofNullable(this.get(entry)).map(data -> data[index]) : Optional.empty();
    }

    public boolean equals(Object o) {
        if (o instanceof ConfigDataSet<?> impl && this.toMap().equals(impl.toMap())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.toMap().hashCode();
    }

    private Map<T, Object[]> dissolve() {
        Map<T, Object[]> dissolved = this.dissolved;
        if (dissolved == null) {
            Map<T, Object[]> entries = Maps.newIdentityHashMap();
            Set<T> toRemove = Sets.newIdentityHashSet();
            for (ConfigDataSetImpl.EntryHolder<?, T> holder : this.values) {
                if (holder instanceof ConfigDataSetImpl.TagEntryHolder) {
                    holder.dissolve(holder.inverted ? (t, objects) -> toRemove.add(t) : entries::put);
                }
            }
            for (ConfigDataSetImpl.EntryHolder<?, T> holderx : this.values) {
                if (holderx instanceof ConfigDataSetImpl.RegistryEntryHolder) {
                    holderx.dissolve(holderx.inverted ? (t, objects) -> toRemove.add(t) : entries::put);
                }
            }
            if (entries.isEmpty() && !toRemove.isEmpty()) {
                entries = (Map<T, Object[]>) this.activeRegistry.stream().collect(Collectors.toMap(Function.identity(), t -> ConfigDataSetImpl.EntryHolder.EMPTY_DATA, (o1, o2) -> o1, Maps::newIdentityHashMap));
            }
            entries.keySet().removeIf(t -> !this.filter.test(0, t) || toRemove.contains(t));
            this.dissolved = dissolved = Collections.unmodifiableMap(entries);
        }
        return dissolved;
    }

    private Optional<ConfigDataSetImpl.EntryHolder<?, T>> deserialize(String source, Class<?>[] types) {
        String[] sources = source.trim().split(",");
        try {
            String newSource = sources[0].trim();
            if (!newSource.startsWith("!")) {
                Object[] data = new Object[types.length];
                for (int i = 0; i < types.length; i++) {
                    if (sources.length - 1 <= i) {
                        throw new IllegalArgumentException("Data index out of bounds, index was %s, but length is %s".formatted(i + 1, sources.length));
                    }
                    data[i] = deserializeData(types[i], sources[i + 1].trim());
                    if (!this.filter.test(i + 1, data[i])) {
                        throw new IllegalStateException("Data %s at index %s from source entry %s does not conform to filter".formatted(data[i], i, source));
                    }
                }
                return Optional.of(this.deserialize(newSource).withData(data));
            } else {
                return Optional.of(this.deserialize(newSource));
            }
        } catch (Exception var7) {
            PuzzlesLib.LOGGER.warn("Unable to parse entry {}", source, var7);
            return Optional.empty();
        }
    }

    private ConfigDataSetImpl.EntryHolder<?, T> deserialize(String source) throws RuntimeException {
        boolean inverted = source.startsWith("!");
        if (inverted) {
            source = source.substring(1);
        }
        boolean tagHolder = source.startsWith("#");
        if (tagHolder) {
            source = source.substring(1);
        }
        if (!source.contains(":")) {
            source = "minecraft:" + source;
        }
        return (ConfigDataSetImpl.EntryHolder<?, T>) (tagHolder ? new ConfigDataSetImpl.TagEntryHolder<>(this.activeRegistry, source, inverted) : new ConfigDataSetImpl.RegistryEntryHolder<>(this.activeRegistry, source, inverted));
    }

    private abstract static class EntryHolder<D, E> {

        public static final Object[] EMPTY_DATA = new Object[0];

        protected final Registry<E> activeRegistry;

        private final String input;

        public final boolean inverted;

        private Object[] data = EMPTY_DATA;

        protected EntryHolder(Registry<E> registry, String input, boolean inverted) {
            this.activeRegistry = registry;
            this.input = input;
            this.inverted = inverted;
        }

        public ConfigDataSetImpl.EntryHolder<D, E> withData(Object[] data) {
            this.data = data;
            return this;
        }

        public final void dissolve(BiConsumer<E, Object[]> builder) {
            this.findRegistryMatches(this.input).stream().flatMap(this::dissolveValue).forEach(value -> builder.accept(value, this.data));
        }

        private Collection<D> findRegistryMatches(String source) {
            Collection<D> matches = Sets.newHashSet();
            if (!source.contains("*")) {
                Optional.ofNullable(ResourceLocation.tryParse(source)).flatMap(this::toValue).ifPresent(matches::add);
            } else {
                String regexSource = source.replace("*", "[a-z0-9/._-]*");
                this.allValues().filter(entry -> ((ResourceLocation) entry.getKey()).toString().matches(regexSource)).map(Entry::getValue).forEach(matches::add);
            }
            if (this.activeRegistry != null && matches.isEmpty()) {
                PuzzlesLib.LOGGER.warn("Unable to parse entry {}: No matches found in registry {}", source, this.activeRegistry.key().location());
            }
            return matches;
        }

        protected abstract Stream<E> dissolveValue(D var1);

        protected abstract Optional<D> toValue(ResourceLocation var1);

        protected abstract Stream<Entry<ResourceLocation, D>> allValues();
    }

    private static class RegistryEntryHolder<E> extends ConfigDataSetImpl.EntryHolder<E, E> {

        RegistryEntryHolder(Registry<E> registry, String source, boolean inverted) {
            super(registry, source, inverted);
        }

        @Override
        protected Stream<E> dissolveValue(E entry) {
            return Stream.of(entry);
        }

        @Override
        protected Optional<E> toValue(ResourceLocation identifier) {
            return !this.activeRegistry.containsKey(identifier) ? Optional.empty() : Optional.of(this.activeRegistry.get(identifier));
        }

        @Override
        protected Stream<Entry<ResourceLocation, E>> allValues() {
            return this.activeRegistry.entrySet().stream().map(entry -> Map.entry(((ResourceKey) entry.getKey()).location(), entry.getValue()));
        }
    }

    private static class TagEntryHolder<E> extends ConfigDataSetImpl.EntryHolder<TagKey<E>, E> {

        TagEntryHolder(Registry<E> registry, String source, boolean inverted) {
            super(registry, source, inverted);
        }

        public Stream<E> dissolveValue(TagKey<E> entry) {
            return StreamSupport.stream(this.activeRegistry.getTagOrEmpty(entry).spliterator(), false).map(Holder::m_203334_);
        }

        @Override
        protected Optional<TagKey<E>> toValue(ResourceLocation identifier) {
            TagKey<E> tag = TagKey.create(this.activeRegistry.key(), identifier);
            return this.activeRegistry.getTag(tag).isEmpty() ? Optional.empty() : Optional.of(tag);
        }

        @Override
        protected Stream<Entry<ResourceLocation, TagKey<E>>> allValues() {
            return this.activeRegistry.getTagNames().map(tagKey -> Map.entry(tagKey.location(), tagKey));
        }
    }
}