package me.jellysquid.mods.lithium.common.state;

import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;

public class FastImmutableTableCache<R, C, V> {

    private final ObjectOpenCustomHashSet<R[]> rows = new ObjectOpenCustomHashSet(ObjectArrays.HASH_STRATEGY);

    private final ObjectOpenCustomHashSet<C[]> columns = new ObjectOpenCustomHashSet(ObjectArrays.HASH_STRATEGY);

    private final ObjectOpenCustomHashSet<V[]> values = new ObjectOpenCustomHashSet(ObjectArrays.HASH_STRATEGY);

    private final ObjectOpenCustomHashSet<int[]> indices = new ObjectOpenCustomHashSet(IntArrays.HASH_STRATEGY);

    public synchronized V[] dedupValues(V[] values) {
        return (V[]) ((Object[]) this.values.addOrGet(values));
    }

    public synchronized R[] dedupRows(R[] rows) {
        return (R[]) ((Object[]) this.rows.addOrGet(rows));
    }

    public synchronized C[] dedupColumns(C[] columns) {
        return (C[]) ((Object[]) this.columns.addOrGet(columns));
    }

    public synchronized int[] dedupIndices(int[] ints) {
        return (int[]) this.indices.addOrGet(ints);
    }
}