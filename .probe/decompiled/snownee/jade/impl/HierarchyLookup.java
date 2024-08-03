package snownee.jade.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IJadeProvider;
import snownee.jade.util.CommonProxy;

public class HierarchyLookup<T extends IJadeProvider> {

    private final Class<?> baseClass;

    private ListMultimap<Class<?>, T> objects = ArrayListMultimap.create();

    private final Cache<Class<?>, List<T>> resultCache = CacheBuilder.newBuilder().build();

    private final boolean singleton;

    public HierarchyLookup(Class<?> baseClass) {
        this(baseClass, false);
    }

    public HierarchyLookup(Class<?> baseClass, boolean singleton) {
        this.baseClass = baseClass;
        this.singleton = singleton;
    }

    public void register(Class<?> clazz, T provider) {
        if (!CommonProxy.isBlockedUid(provider)) {
            Objects.requireNonNull(clazz);
            Objects.requireNonNull(provider.getUid());
            WailaCommonRegistration.INSTANCE.priorities.put(provider);
            this.objects.put(clazz, provider);
        }
    }

    public List<T> get(Object obj) {
        return obj == null ? List.of() : this.get(obj.getClass());
    }

    public List<T> get(Class<?> clazz) {
        try {
            return (List<T>) this.resultCache.get(clazz, () -> {
                List<T> list = Lists.newArrayList();
                this.getInternal(clazz, list);
                list = ImmutableList.sortedCopyOf(Comparator.comparingInt(WailaCommonRegistration.INSTANCE.priorities::byValue), list);
                return (List) (this.singleton && !list.isEmpty() ? ImmutableList.of((IJadeProvider) list.get(0)) : list);
            });
        } catch (ExecutionException var3) {
            var3.printStackTrace();
            return List.of();
        }
    }

    private void getInternal(Class<?> clazz, List<T> list) {
        if (clazz != this.baseClass && clazz != Object.class) {
            this.getInternal(clazz.getSuperclass(), list);
        }
        list.addAll(this.objects.get(clazz));
    }

    public Multimap<Class<?>, T> getObjects() {
        return this.objects;
    }

    public void invalidate() {
        this.resultCache.invalidateAll();
    }

    public void loadComplete(PriorityStore<ResourceLocation, IJadeProvider> priorityStore) {
        this.objects = ImmutableListMultimap.builder().orderValuesBy(Comparator.comparingInt(priorityStore::byValue)).putAll(this.objects).build();
    }
}