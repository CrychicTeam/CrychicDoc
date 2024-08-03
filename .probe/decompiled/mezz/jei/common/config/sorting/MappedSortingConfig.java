package mezz.jei.common.config.sorting;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import mezz.jei.common.config.sorting.serializers.ISortingSerializer;

public abstract class MappedSortingConfig<T, V> extends SortingConfig<V> {

    private final Function<T, V> mapping;

    public MappedSortingConfig(Path path, ISortingSerializer<V> serializer, Function<T, V> mapping) {
        super(path, serializer);
        this.mapping = mapping;
    }

    public Comparator<T> getComparator(Collection<T> allValues) {
        Set<V> allMappedValues = (Set<V>) allValues.stream().map(this.mapping).collect(Collectors.toSet());
        return super.getComparator((Collection<T>) allMappedValues, (Function<T, T>) this.mapping);
    }

    public Comparator<T> getComparatorFromMappedValues(Collection<V> allMappedValues) {
        return super.getComparator((Collection<T>) allMappedValues, (Function<T, T>) this.mapping);
    }
}