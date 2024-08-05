package snownee.jade.impl;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import org.jetbrains.annotations.Nullable;
import snownee.jade.util.JadeCodecs;
import snownee.jade.util.JsonConfig;

public class PriorityStore<K, V> {

    private final Object2IntMap<K> priorities = new Object2IntLinkedOpenHashMap();

    private final Function<V, K> keyGetter;

    private final ToIntFunction<V> defaultPriorityGetter;

    @Nullable
    private String configFile;

    @Nullable
    private Codec<K> keyCodec;

    private ImmutableList<K> sortedList = ImmutableList.of();

    private BiFunction<PriorityStore<K, V>, Collection<K>, List<K>> sortingFunction = (store, allKeys) -> allKeys.stream().sorted(Comparator.comparingInt(store::byKey)).toList();

    public PriorityStore(ToIntFunction<V> defaultPriorityGetter, Function<V, K> keyGetter) {
        this.defaultPriorityGetter = defaultPriorityGetter;
        this.keyGetter = keyGetter;
    }

    public void setSortingFunction(BiFunction<PriorityStore<K, V>, Collection<K>, List<K>> sortingFunction) {
        this.sortingFunction = sortingFunction;
    }

    public void configurable(String configFile, Codec<K> keyCodec) {
        this.configFile = configFile;
        this.keyCodec = keyCodec;
    }

    public void put(V provider) {
        Objects.requireNonNull(provider);
        this.put(provider, this.defaultPriorityGetter.applyAsInt(provider));
    }

    public void put(V provider, int priority) {
        Objects.requireNonNull(provider);
        K uid = (K) this.keyGetter.apply(provider);
        Objects.requireNonNull(uid);
        this.priorities.put(uid, priority);
    }

    public void sort(Set<K> extraKeys) {
        Set<K> allKeys = this.priorities.keySet();
        if (!extraKeys.isEmpty()) {
            allKeys = Sets.union(this.priorities.keySet(), extraKeys);
        }
        if (!Strings.isNullOrEmpty(this.configFile)) {
            JsonConfig<Map<K, OptionalInt>> config = new JsonConfig<>(this.configFile, Codec.unboundedMap(this.keyCodec, JadeCodecs.OPTIONAL_INT), null, Map::of);
            Map<K, OptionalInt> map = config.get();
            for (Entry<K, OptionalInt> e : map.entrySet()) {
                if (((OptionalInt) e.getValue()).isPresent()) {
                    this.priorities.put(e.getKey(), ((OptionalInt) e.getValue()).getAsInt());
                }
            }
            new Thread(() -> {
                boolean changed = false;
                TreeMap<K, OptionalInt> newMap = Maps.newTreeMap(Comparator.comparing(Object::toString));
                ObjectIterator var5 = this.priorities.keySet().iterator();
                while (var5.hasNext()) {
                    K id = (K) var5.next();
                    if (!map.containsKey(id)) {
                        newMap.put(id, OptionalInt.empty());
                        changed = true;
                    }
                }
                if (changed) {
                    newMap.putAll(map);
                    config.write(newMap, false);
                }
            }).start();
        }
        this.sortedList = ImmutableList.copyOf((Collection) this.sortingFunction.apply(this, allKeys));
    }

    public int byValue(V value) {
        return this.byKey((K) this.keyGetter.apply(value));
    }

    public int byKey(K id) {
        return this.priorities.getInt(id);
    }

    public ImmutableList<K> getSortedList() {
        return this.sortedList;
    }
}