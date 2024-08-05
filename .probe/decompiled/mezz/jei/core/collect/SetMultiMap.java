package mezz.jei.core.collect;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSetMultimap.Builder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class SetMultiMap<K, V> extends MultiMap<K, V, Set<V>> {

    public SetMultiMap() {
        this(HashSet::new);
    }

    public SetMultiMap(Supplier<Set<V>> collectionSupplier) {
        super(collectionSupplier);
    }

    public SetMultiMap(Map<K, Set<V>> map, Supplier<Set<V>> collectionSupplier) {
        super(map, collectionSupplier);
    }

    public Set<V> get(K key) {
        Set<V> collection = (Set<V>) this.map.get(key);
        return collection != null ? Collections.unmodifiableSet(collection) : Collections.emptySet();
    }

    public ImmutableSetMultimap<K, V> toImmutable() {
        Builder<K, V> builder = ImmutableSetMultimap.builder();
        this.map.forEach(builder::putAll);
        return builder.build();
    }
}