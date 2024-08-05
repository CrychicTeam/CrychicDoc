package me.jellysquid.mods.lithium.common.util.collections;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.function.Predicate;
import net.minecraft.util.Mth;

public final class Object2BooleanCacheTable<T> {

    private final int mask;

    private final Object2BooleanCacheTable.Node<T>[] nodes;

    private final Predicate<T> operator;

    public Object2BooleanCacheTable(int capacity, Predicate<T> operator) {
        int capacity1 = Mth.smallestEncompassingPowerOfTwo(capacity);
        this.mask = capacity1 - 1;
        this.nodes = new Object2BooleanCacheTable.Node[capacity1];
        this.operator = operator;
    }

    private static <T> int hash(T key) {
        return HashCommon.mix(key.hashCode());
    }

    public boolean get(T key) {
        int idx = hash(key) & this.mask;
        Object2BooleanCacheTable.Node<T> node = this.nodes[idx];
        if (node != null && key.equals(node.key)) {
            return node.value;
        } else {
            boolean test = this.operator.test(key);
            this.nodes[idx] = new Object2BooleanCacheTable.Node<>(key, test);
            return test;
        }
    }

    static class Node<T> {

        final T key;

        final boolean value;

        Node(T key, boolean value) {
            this.key = key;
            this.value = value;
        }
    }
}