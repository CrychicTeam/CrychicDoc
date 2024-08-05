package mezz.jei.common.config.sorting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import mezz.jei.common.config.sorting.serializers.ISortingSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public abstract class SortingConfig<T> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Path path;

    private final ISortingSerializer<T> serializer;

    @Nullable
    private List<T> sorted;

    public SortingConfig(Path path, ISortingSerializer<T> serializer) {
        this.path = path;
        this.serializer = serializer;
    }

    protected abstract Comparator<T> getDefaultSortOrder();

    private void save(List<T> sorted) {
        try {
            this.serializer.write(this.path, sorted);
        } catch (IOException var3) {
            LOGGER.error("Failed to save to file {}", this.path, var3);
        }
    }

    private Optional<List<T>> loadSortedFromFile() {
        if (Files.exists(this.path, new LinkOption[0])) {
            try {
                List<T> result = this.serializer.read(this.path);
                return Optional.of(result);
            } catch (IOException var2) {
                LOGGER.error("Failed to load from file: {}", this.path, var2);
            }
        }
        return Optional.empty();
    }

    private void load(Collection<T> allValues) {
        Optional<List<T>> previousSorted = this.loadSortedFromFile();
        Comparator<T> defaultOrder = this.getDefaultSortOrder();
        Comparator<T> sortOrder = (Comparator<T>) previousSorted.map(s -> {
            Comparator<T> existingOrder = Comparator.comparingInt(t -> indexOfSort(s.indexOf(t)));
            return existingOrder.thenComparing(defaultOrder);
        }).orElse(defaultOrder);
        this.sorted = allValues.stream().distinct().sorted(sortOrder).toList();
        boolean changed = (Boolean) previousSorted.map(s -> !Objects.equals(s, this.sorted)).orElse(true);
        if (changed) {
            this.save(this.sorted);
        }
    }

    private static int indexOfSort(int index) {
        return index < 0 ? Integer.MAX_VALUE : index;
    }

    private List<T> getSorted(Collection<T> allValues) {
        if (this.sorted == null) {
            this.load(allValues);
        }
        return this.sorted;
    }

    public <V> Comparator<V> getComparator(Collection<T> allValues, Function<V, T> mapping) {
        List<T> sorted = this.getSorted(allValues);
        return Comparator.comparingInt(o -> {
            T value = (T) mapping.apply(o);
            return indexOfSort(sorted.indexOf(value));
        });
    }
}