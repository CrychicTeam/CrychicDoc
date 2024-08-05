package me.lucko.spark.common.util;

import java.util.ArrayList;
import java.util.List;

public class IndexedListBuilder<T> {

    private int i = 0;

    private final List<T> nodes = new ArrayList();

    public int add(T node) {
        this.nodes.add(node);
        return this.i++;
    }

    public List<T> build() {
        return this.nodes;
    }
}