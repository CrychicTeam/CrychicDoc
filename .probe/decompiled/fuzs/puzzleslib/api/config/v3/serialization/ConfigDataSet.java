package fuzs.puzzleslib.api.config.v3.serialization;

import fuzs.puzzleslib.api.init.v3.RegistryHelper;
import fuzs.puzzleslib.impl.config.serialization.ConfigDataSetImpl;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConfigDataSet<T> extends Collection<T> {

    String CONFIG_DESCRIPTION = "Format for every entry is \"<namespace>:<path>\". Tags are supported, must be in the format of \"#<namespace>:<path>\". Namespace may be omitted to use \"minecraft\" by default. May use asterisk as wildcard parameter via pattern matching, e.g. \"minecraft:*_shulker_box\" to match all shulker boxes no matter of color. Begin an entry with \"!\" to make sure it is excluded, useful e.g. when it has already been matched by another pattern.";

    Map<T, Object[]> toMap();

    Set<T> toSet();

    @Nullable
    Object[] get(T var1);

    <V> V get(T var1, int var2);

    <V> Optional<V> getOptional(T var1, int var2);

    @Deprecated
    boolean add(T var1);

    @Deprecated
    boolean remove(Object var1);

    @Deprecated
    boolean addAll(@NotNull Collection<? extends T> var1);

    @Deprecated
    boolean removeAll(@NotNull Collection<?> var1);

    @Deprecated
    boolean retainAll(@NotNull Collection<?> var1);

    @Deprecated
    void clear();

    static <T> ConfigDataSet<T> from(ResourceKey<? extends Registry<T>> registryKey, String... values) {
        return from(registryKey, Arrays.asList(values));
    }

    static <T> ConfigDataSet<T> from(ResourceKey<? extends Registry<T>> registryKey, List<String> values, Class<?>... types) {
        return from(registryKey, values, (index, value) -> true, types);
    }

    static <T> ConfigDataSet<T> from(ResourceKey<? extends Registry<T>> registryKey, List<String> values, BiPredicate<Integer, Object> filter, Class<?>... types) {
        Registry<T> registry = RegistryHelper.findBuiltInRegistry(registryKey);
        return new ConfigDataSetImpl<>(registry, values, filter, types);
    }

    @SafeVarargs
    static <T> List<String> toString(ResourceKey<? extends Registry<T>> registryKey, T... entries) {
        Registry<? super T> registry = RegistryHelper.findBuiltInRegistry(registryKey);
        return (List<String>) Stream.of(entries).peek(Objects::requireNonNull).map(registry::m_7981_).filter(Objects::nonNull).map(ResourceLocation::toString).collect(Collectors.toList());
    }
}