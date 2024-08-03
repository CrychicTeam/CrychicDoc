package malte0811.ferritecore.fastmap.table;

import com.google.common.collect.Tables;
import com.google.common.collect.Table.Cell;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import malte0811.ferritecore.ducks.FastMapStateHolder;
import malte0811.ferritecore.fastmap.FastMap;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FastmapNeighborTable<S> extends NeighborTableBase<S> {

    private final FastMapStateHolder<S> owner;

    public FastmapNeighborTable(FastMapStateHolder<S> owner) {
        this.owner = owner;
    }

    public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
        if (columnKey instanceof Comparable && rowKey instanceof Property<?> rowProperty) {
            Comparable<?> valueInState = this.owner.getStateMap().getValue(this.owner.getStateIndex(), (Property<Comparable<?>>) rowProperty);
            return valueInState != null && !valueInState.equals(columnKey) ? rowProperty.getPossibleValues().contains(columnKey) : false;
        } else {
            return false;
        }
    }

    public boolean containsRow(@Nullable Object rowKey) {
        return rowKey instanceof Property<?> rowProperty ? this.owner.getStateMap().getValue(this.owner.getStateIndex(), rowProperty) != null : false;
    }

    public boolean containsColumn(@Nullable Object columnKey) {
        FastMap<S> map = this.owner.getStateMap();
        for (int i = 0; i < map.numProperties(); i++) {
            Entry<Property<?>, Comparable<?>> entry = map.getEntry(i, this.owner.getStateIndex());
            if (!((Comparable) entry.getValue()).equals(columnKey) && ((Property) entry.getKey()).getPossibleValues().contains(columnKey)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsValue(@Nullable Object value) {
        if (value == null) {
            return false;
        } else {
            FastMap<S> map = this.owner.getStateMap();
            for (int propIndex = 0; propIndex < map.numProperties(); propIndex++) {
                if (this.isNeighbor(map.getKey(propIndex).getProperty(), value)) {
                    return true;
                }
            }
            return false;
        }
    }

    private <T extends Comparable<T>> boolean isNeighbor(Property<T> prop, Object potentialNeighbor) {
        FastMap<S> map = this.owner.getStateMap();
        T valueInState = map.getValue(this.owner.getStateIndex(), prop);
        for (T neighborValue : prop.getPossibleValues()) {
            if (!neighborValue.equals(valueInState)) {
                S neighbor = map.with(this.owner.getStateIndex(), prop, valueInState);
                if (potentialNeighbor.equals(neighbor)) {
                    return true;
                }
            }
        }
        return false;
    }

    public S get(@Nullable Object rowKey, @Nullable Object columnKey) {
        return rowKey instanceof Property<?> rowProperty ? this.owner.getStateMap().withUnsafe(this.owner.getStateIndex(), rowProperty, columnKey) : null;
    }

    public boolean isEmpty() {
        return this.owner.getStateMap().isSingleState();
    }

    public int size() {
        int numNeighbors = 0;
        for (int i = 0; i < this.owner.getStateMap().numProperties(); i++) {
            numNeighbors += this.owner.getStateMap().getKey(i).numValues();
        }
        return numNeighbors;
    }

    public Map<Comparable<?>, S> row(@NotNull Property<?> rowKey) {
        Map<Comparable<?>, S> rowMap = new HashMap();
        Comparable<?> contained = this.owner.getStateMap().getValue(this.owner.getStateIndex(), (Property<Comparable<?>>) rowKey);
        for (Comparable<?> val : rowKey.getPossibleValues()) {
            if (!val.equals(contained)) {
                rowMap.put(val, this.owner.getStateMap().withUnsafe(this.owner.getStateIndex(), rowKey, val));
            }
        }
        return rowMap;
    }

    public Map<Property<?>, S> column(@NotNull Comparable<?> columnKey) {
        FastMap<S> map = this.owner.getStateMap();
        int index = this.owner.getStateIndex();
        Map<Property<?>, S> rowMap = new HashMap();
        for (int i = 0; i < map.numProperties(); i++) {
            Property<?> rowKey = map.getKey(i).getProperty();
            Comparable<?> contained = map.getValue(index, (Property<Comparable<?>>) rowKey);
            for (Comparable<?> val : rowKey.getPossibleValues()) {
                if (!val.equals(contained) && val.equals(columnKey)) {
                    rowMap.put(rowKey, map.withUnsafe(index, rowKey, val));
                }
            }
        }
        return rowMap;
    }

    public Set<Cell<Property<?>, Comparable<?>, S>> cellSet() {
        FastMap<S> map = this.owner.getStateMap();
        int index = this.owner.getStateIndex();
        Set<Cell<Property<?>, Comparable<?>, S>> rowMap = new HashSet();
        for (int i = 0; i < map.numProperties(); i++) {
            Property<?> rowKey = map.getKey(i).getProperty();
            Comparable<?> contained = map.getValue(index, (Property<Comparable<?>>) rowKey);
            for (Comparable<?> val : rowKey.getPossibleValues()) {
                if (!val.equals(contained)) {
                    rowMap.add(Tables.immutableCell(rowKey, val, map.withUnsafe(index, rowKey, val)));
                }
            }
        }
        return rowMap;
    }

    public Set<Property<?>> rowKeySet() {
        return this.owner.getVanillaPropertyMap().keySet();
    }

    public Set<Comparable<?>> columnKeySet() {
        FastMap<S> map = this.owner.getStateMap();
        Set<Comparable<?>> rowMap = new HashSet();
        for (int i = 0; i < map.numProperties(); i++) {
            Property<?> rowKey = map.getKey(i).getProperty();
            Comparable<?> contained = map.getValue(this.owner.getStateIndex(), (Property<Comparable<?>>) rowKey);
            for (Comparable<?> val : rowKey.getPossibleValues()) {
                if (!val.equals(contained)) {
                    rowMap.add(val);
                }
            }
        }
        return rowMap;
    }

    public Collection<S> values() {
        FastMap<S> map = this.owner.getStateMap();
        int index = this.owner.getStateIndex();
        Set<S> rowMap = new HashSet();
        for (int i = 0; i < map.numProperties(); i++) {
            Property<?> rowKey = map.getKey(i).getProperty();
            Comparable<?> contained = map.getValue(index, (Property<Comparable<?>>) rowKey);
            for (Comparable<?> val : rowKey.getPossibleValues()) {
                if (!val.equals(contained)) {
                    rowMap.add(map.withUnsafe(index, rowKey, val));
                }
            }
        }
        return rowMap;
    }

    public Map<Property<?>, Map<Comparable<?>, S>> rowMap() {
        FastMap<S> map = this.owner.getStateMap();
        Map<Property<?>, Map<Comparable<?>, S>> rowMap = new HashMap();
        for (int i = 0; i < map.numProperties(); i++) {
            Property<?> rowKey = map.getKey(i).getProperty();
            rowMap.put(rowKey, this.row(rowKey));
        }
        return rowMap;
    }

    public Map<Comparable<?>, Map<Property<?>, S>> columnMap() {
        Map<Property<?>, Map<Comparable<?>, S>> rowMap = this.rowMap();
        Map<Comparable<?>, Map<Property<?>, S>> colMap = new HashMap();
        for (Entry<Property<?>, Map<Comparable<?>, S>> entry : rowMap.entrySet()) {
            for (Entry<Comparable<?>, S> innerEntry : ((Map) entry.getValue()).entrySet()) {
                ((Map) colMap.computeIfAbsent((Comparable) innerEntry.getKey(), $ -> new HashMap())).put((Property) entry.getKey(), innerEntry.getValue());
            }
        }
        return colMap;
    }
}