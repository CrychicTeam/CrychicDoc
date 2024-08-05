package icyllis.modernui.widget;

import icyllis.modernui.util.Pools;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap.Entry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class DirectedAcyclicGraph<T> {

    private final Pools.Pool<ArrayList<T>> mListPool = Pools.newSimplePool(10);

    private final Object2ObjectOpenHashMap<T, ArrayList<T>> mGraph = new Object2ObjectOpenHashMap();

    private final ArrayList<T> mSortResult = new ArrayList();

    private final HashSet<T> mSortTmpMarked = new HashSet();

    public void addNode(@Nonnull T node) {
        if (!this.mGraph.containsKey(node)) {
            this.mGraph.put(node, null);
        }
    }

    public boolean contains(@Nonnull T node) {
        return this.mGraph.containsKey(node);
    }

    public void addEdge(@Nonnull T node, @Nonnull T incomingEdge) {
        if (this.mGraph.containsKey(node) && this.mGraph.containsKey(incomingEdge)) {
            ArrayList<T> edges = (ArrayList<T>) this.mGraph.get(node);
            if (edges == null) {
                edges = this.getEmptyList();
                this.mGraph.put(node, edges);
            }
            edges.add(incomingEdge);
        } else {
            throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
        }
    }

    @Nullable
    public List<T> getIncomingEdges(@Nonnull T node) {
        ArrayList<T> result = this.getIncomingEdgesInternal(node);
        return result == null ? null : new ArrayList(result);
    }

    @Nullable
    ArrayList<T> getIncomingEdgesInternal(@Nonnull T node) {
        return (ArrayList<T>) this.mGraph.get(node);
    }

    @Nullable
    public List<T> getOutgoingEdges(@Nonnull T node) {
        ArrayList<T> result = null;
        ObjectIterator var3 = Object2ObjectMaps.fastIterable(this.mGraph).iterator();
        while (var3.hasNext()) {
            Entry<T, ArrayList<T>> entry = (Entry<T, ArrayList<T>>) var3.next();
            ArrayList<T> edges = (ArrayList<T>) entry.getValue();
            if (edges != null && edges.contains(node)) {
                if (result == null) {
                    result = new ArrayList();
                }
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public boolean hasOutgoingEdges(@Nonnull T node) {
        ObjectIterator var2 = this.mGraph.values().iterator();
        while (var2.hasNext()) {
            ArrayList<T> edges = (ArrayList<T>) var2.next();
            if (edges != null && edges.contains(node)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        ObjectIterator var1 = this.mGraph.values().iterator();
        while (var1.hasNext()) {
            ArrayList<T> edges = (ArrayList<T>) var1.next();
            if (edges != null) {
                this.poolList(edges);
            }
        }
        this.mGraph.clear();
    }

    @Nonnull
    public ArrayList<T> getSortedList() {
        this.mSortResult.clear();
        this.mSortTmpMarked.clear();
        ObjectIterator var1 = this.mGraph.keySet().iterator();
        while (var1.hasNext()) {
            T key = (T) var1.next();
            this.dfs(key, this.mSortResult, this.mSortTmpMarked);
        }
        return this.mSortResult;
    }

    private void dfs(T node, @Nonnull ArrayList<T> result, HashSet<T> tmpMarked) {
        if (!result.contains(node)) {
            if (tmpMarked.contains(node)) {
                throw new RuntimeException("This graph contains cyclic dependencies");
            } else {
                tmpMarked.add(node);
                ArrayList<T> edges = (ArrayList<T>) this.mGraph.get(node);
                if (edges != null) {
                    for (T edge : edges) {
                        this.dfs(edge, result, tmpMarked);
                    }
                }
                tmpMarked.remove(node);
                result.add(node);
            }
        }
    }

    int size() {
        return this.mGraph.size();
    }

    @Nonnull
    private ArrayList<T> getEmptyList() {
        ArrayList<T> list = this.mListPool.acquire();
        if (list == null) {
            list = new ArrayList();
        }
        return list;
    }

    private void poolList(@Nonnull ArrayList<T> list) {
        list.clear();
        this.mListPool.release(list);
    }
}