package malte0811.ferritecore.fastmap.immutable;

import com.google.common.collect.UnmodifiableIterator;
import malte0811.ferritecore.ducks.FastMapStateHolder;
import malte0811.ferritecore.fastmap.FastMap;

public abstract class FastMapEntryIterator<T> extends UnmodifiableIterator<T> {

    private final FastMapStateHolder<?> viewedState;

    private int currentIndex = 0;

    public FastMapEntryIterator(FastMapStateHolder<?> viewedState) {
        this.viewedState = viewedState;
    }

    public boolean hasNext() {
        return this.currentIndex < this.viewedState.getStateMap().numProperties();
    }

    public T next() {
        T next = this.getEntry(this.currentIndex, this.viewedState.getStateMap(), this.viewedState.getStateIndex());
        this.currentIndex++;
        return next;
    }

    protected abstract T getEntry(int var1, FastMap<?> var2, int var3);
}