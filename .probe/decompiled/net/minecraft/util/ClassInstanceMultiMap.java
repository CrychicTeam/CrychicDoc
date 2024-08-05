package net.minecraft.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ClassInstanceMultiMap<T> extends AbstractCollection<T> {

    private final Map<Class<?>, List<T>> byClass = Maps.newHashMap();

    private final Class<T> baseClass;

    private final List<T> allInstances = Lists.newArrayList();

    public ClassInstanceMultiMap(Class<T> classT0) {
        this.baseClass = classT0;
        this.byClass.put(classT0, this.allInstances);
    }

    public boolean add(T t0) {
        boolean $$1 = false;
        for (Entry<Class<?>, List<T>> $$2 : this.byClass.entrySet()) {
            if (((Class) $$2.getKey()).isInstance(t0)) {
                $$1 |= ((List) $$2.getValue()).add(t0);
            }
        }
        return $$1;
    }

    public boolean remove(Object object0) {
        boolean $$1 = false;
        for (Entry<Class<?>, List<T>> $$2 : this.byClass.entrySet()) {
            if (((Class) $$2.getKey()).isInstance(object0)) {
                List<T> $$3 = (List<T>) $$2.getValue();
                $$1 |= $$3.remove(object0);
            }
        }
        return $$1;
    }

    public boolean contains(Object object0) {
        return this.find(object0.getClass()).contains(object0);
    }

    public <S> Collection<S> find(Class<S> classS0) {
        if (!this.baseClass.isAssignableFrom(classS0)) {
            throw new IllegalArgumentException("Don't know how to search for " + classS0);
        } else {
            List<? extends T> $$1 = (List<? extends T>) this.byClass.computeIfAbsent(classS0, p_13538_ -> (List) this.allInstances.stream().filter(p_13538_::isInstance).collect(Collectors.toList()));
            return Collections.unmodifiableCollection($$1);
        }
    }

    public Iterator<T> iterator() {
        return (Iterator<T>) (this.allInstances.isEmpty() ? Collections.emptyIterator() : Iterators.unmodifiableIterator(this.allInstances.iterator()));
    }

    public List<T> getAllInstances() {
        return ImmutableList.copyOf(this.allInstances);
    }

    public int size() {
        return this.allInstances.size();
    }
}