package snownee.kiwi;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.apache.commons.lang3.ClassUtils;

public class RegistryLookup {

    public final Map<Class<?>, Object> registries = Maps.newConcurrentMap();

    public final Cache<Class<?>, Optional<Object>> cache = CacheBuilder.newBuilder().build();

    public Object findRegistry(Object o) {
        try {
            return ((Optional) this.cache.get(o.getClass(), () -> {
                for (Class<?> clazz = o.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
                    Object registry = this.registries.get(clazz);
                    if (registry != null) {
                        return Optional.of(registry);
                    }
                }
                for (Class<?> clazzx : ClassUtils.getAllInterfaces(o.getClass())) {
                    Object registry = this.registries.get(clazzx);
                    if (registry != null) {
                        return Optional.of(registry);
                    }
                }
                return Optional.empty();
            })).orElse(null);
        } catch (ExecutionException var3) {
            return null;
        }
    }
}