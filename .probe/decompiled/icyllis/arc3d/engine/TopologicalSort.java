package icyllis.arc3d.engine;

import java.util.Collection;
import java.util.List;

public final class TopologicalSort {

    public static <T> void topologicalSort(List<T> graph, TopologicalSort.Access<T> access) {
        assert checkAllUnmarked(graph, access);
        int index = 0;
        for (T node : graph) {
            index = dfsVisit(node, access, index);
        }
        assert index == graph.size();
        int i = 0;
        for (int e = graph.size(); i < e; i++) {
            int j = access.getIndex((T) graph.get(i));
            while (j != i) {
                T temp = (T) graph.set(j, graph.get(i));
                graph.set(i, temp);
                j = access.getIndex(temp);
            }
        }
        assert cleanExit(graph, access);
    }

    private static <T> int dfsVisit(T node, TopologicalSort.Access<T> access, int index) {
        if (access.getIndex(node) != -1) {
            return index;
        } else if (access.isTempMarked(node)) {
            throw new IllegalStateException("cyclic dependency: " + node);
        } else {
            Collection<T> edges = access.getIncomingEdges(node);
            if (edges != null && !edges.isEmpty()) {
                access.setTempMarked(node, true);
                for (T edge : edges) {
                    index = dfsVisit(edge, access, index);
                }
                access.setTempMarked(node, false);
            }
            access.setIndex(node, index);
            return index + 1;
        }
    }

    private static <T> boolean checkAllUnmarked(List<T> graph, TopologicalSort.Access<T> access) {
        for (T node : graph) {
            assert access.getIndex(node) == -1;
            assert !access.isTempMarked(node);
        }
        return true;
    }

    private static <T> boolean cleanExit(List<T> graph, TopologicalSort.Access<T> access) {
        int i = 0;
        for (int e = graph.size(); i < e; i++) {
            T node = (T) graph.get(i);
            assert access.getIndex(node) == i;
            assert !access.isTempMarked(node);
        }
        return true;
    }

    public interface Access<T> {

        void setIndex(T var1, int var2);

        int getIndex(T var1);

        void setTempMarked(T var1, boolean var2);

        boolean isTempMarked(T var1);

        Collection<T> getIncomingEdges(T var1);
    }
}