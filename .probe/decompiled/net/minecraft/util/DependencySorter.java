package net.minecraft.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DependencySorter<K, V extends DependencySorter.Entry<K>> {

    private final Map<K, V> contents = new HashMap();

    public DependencySorter<K, V> addEntry(K k0, V v1) {
        this.contents.put(k0, v1);
        return this;
    }

    private void visitDependenciesAndElement(Multimap<K, K> multimapKK0, Set<K> setK1, K k2, BiConsumer<K, V> biConsumerKV3) {
        if (setK1.add(k2)) {
            multimapKK0.get(k2).forEach(p_285443_ -> this.visitDependenciesAndElement(multimapKK0, setK1, (K) p_285443_, biConsumerKV3));
            V $$4 = (V) this.contents.get(k2);
            if ($$4 != null) {
                biConsumerKV3.accept(k2, $$4);
            }
        }
    }

    private static <K> boolean isCyclic(Multimap<K, K> multimapKK0, K k1, K k2) {
        Collection<K> $$3 = multimapKK0.get(k2);
        return $$3.contains(k1) ? true : $$3.stream().anyMatch(p_284974_ -> isCyclic(multimapKK0, k1, p_284974_));
    }

    private static <K> void addDependencyIfNotCyclic(Multimap<K, K> multimapKK0, K k1, K k2) {
        if (!isCyclic(multimapKK0, k1, k2)) {
            multimapKK0.put(k1, k2);
        }
    }

    public void orderByDependencies(BiConsumer<K, V> biConsumerKV0) {
        Multimap<K, K> $$1 = HashMultimap.create();
        this.contents.forEach((p_285415_, p_285018_) -> p_285018_.visitRequiredDependencies(p_285287_ -> addDependencyIfNotCyclic($$1, p_285415_, p_285287_)));
        this.contents.forEach((p_285462_, p_285526_) -> p_285526_.visitOptionalDependencies(p_285513_ -> addDependencyIfNotCyclic($$1, p_285462_, p_285513_)));
        Set<K> $$2 = new HashSet();
        this.contents.keySet().forEach(p_284996_ -> this.visitDependenciesAndElement($$1, $$2, (K) p_284996_, biConsumerKV0));
    }

    public interface Entry<K> {

        void visitRequiredDependencies(Consumer<K> var1);

        void visitOptionalDependencies(Consumer<K> var1);
    }
}