package mezz.jei.core.collect;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ImmutableTable.Builder;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public class Table<R, C, V> {

    private final Map<R, Map<C, V>> table;

    private final Function<R, Map<C, V>> rowMappingFunction;

    public static <R, C, V> Table<R, C, V> hashBasedTable() {
        return new Table<>(new HashMap(), HashMap::new);
    }

    public static <R, C, V> Table<R, C, V> identityHashBasedTable() {
        return new Table<>(new IdentityHashMap(), IdentityHashMap::new);
    }

    public Table(Map<R, Map<C, V>> table, Supplier<Map<C, V>> rowSupplier) {
        this.table = table;
        this.rowMappingFunction = k -> (Map) rowSupplier.get();
    }

    @Nullable
    public V get(R row, C col) {
        Map<C, V> rowMap = this.getRow(row);
        return (V) rowMap.get(col);
    }

    public V computeIfAbsent(R row, C col, Supplier<V> valueSupplier) {
        Map<C, V> rowMap = this.getRow(row);
        return (V) rowMap.computeIfAbsent(col, k -> valueSupplier.get());
    }

    @Nullable
    public V put(R row, C col, V val) {
        Map<C, V> rowMap = this.getRow(row);
        return (V) rowMap.put(col, val);
    }

    public Map<C, V> getRow(R row) {
        return (Map<C, V>) this.table.computeIfAbsent(row, this.rowMappingFunction);
    }

    public boolean contains(R row, C col) {
        Map<C, V> map = (Map<C, V>) this.table.get(row);
        return map != null && map.containsKey(col);
    }

    public void clear() {
        this.table.clear();
    }

    public ImmutableTable<R, C, V> toImmutable() {
        Builder<R, C, V> builder = ImmutableTable.builder();
        for (Entry<R, Map<C, V>> entry : this.table.entrySet()) {
            R row = (R) entry.getKey();
            for (Entry<C, V> rowEntry : ((Map) entry.getValue()).entrySet()) {
                C col = (C) rowEntry.getKey();
                V val = (V) rowEntry.getValue();
                builder.put(row, col, val);
            }
        }
        return builder.build();
    }
}