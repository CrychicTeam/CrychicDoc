package net.minecraft.util;

import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public final class Graph {

    private Graph() {
    }

    public static <T> boolean depthFirstSearch(Map<T, Set<T>> mapTSetT0, Set<T> setT1, Set<T> setT2, Consumer<T> consumerT3, T t4) {
        if (setT1.contains(t4)) {
            return false;
        } else if (setT2.contains(t4)) {
            return true;
        } else {
            setT2.add(t4);
            for (T $$5 : (Set) mapTSetT0.getOrDefault(t4, ImmutableSet.of())) {
                if (depthFirstSearch(mapTSetT0, setT1, setT2, consumerT3, $$5)) {
                    return true;
                }
            }
            setT2.remove(t4);
            setT1.add(t4);
            consumerT3.accept(t4);
            return false;
        }
    }
}