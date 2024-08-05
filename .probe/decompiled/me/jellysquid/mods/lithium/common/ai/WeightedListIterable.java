package me.jellysquid.mods.lithium.common.ai;

import java.util.Iterator;
import net.minecraft.world.entity.ai.behavior.ShufflingList;

public interface WeightedListIterable<U> extends Iterable<U> {

    Iterator<U> iterator();

    static <T> Iterable<? extends T> cast(ShufflingList<T> list) {
        return list;
    }

    public static class ListIterator<U> implements Iterator<U> {

        private final Iterator<ShufflingList.WeightedEntry<? extends U>> inner;

        public ListIterator(Iterator<ShufflingList.WeightedEntry<? extends U>> inner) {
            this.inner = inner;
        }

        public boolean hasNext() {
            return this.inner.hasNext();
        }

        public U next() {
            return (U) ((ShufflingList.WeightedEntry) this.inner.next()).getData();
        }
    }
}