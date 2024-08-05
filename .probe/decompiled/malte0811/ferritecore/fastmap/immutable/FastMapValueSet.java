package malte0811.ferritecore.fastmap.immutable;

import com.google.common.collect.FerriteCoreImmutableCollectionAccess;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Objects;
import malte0811.ferritecore.ducks.FastMapStateHolder;
import malte0811.ferritecore.fastmap.FastMap;
import org.jetbrains.annotations.Nullable;

public class FastMapValueSet extends FerriteCoreImmutableCollectionAccess<Comparable<?>> {

    private final FastMapStateHolder<?> viewedState;

    public FastMapValueSet(FastMapStateHolder<?> viewedState) {
        this.viewedState = viewedState;
    }

    public UnmodifiableIterator<Comparable<?>> iterator() {
        return new FastMapEntryIterator<Comparable<?>>(this.viewedState) {

            protected Comparable<?> getEntry(int propertyIndex, FastMap<?> map, int stateIndex) {
                return map.getKey(propertyIndex).getValue(stateIndex);
            }
        };
    }

    public int size() {
        return this.viewedState.getStateMap().numProperties();
    }

    public boolean contains(@Nullable Object o) {
        UnmodifiableIterator var2 = this.iterator();
        while (var2.hasNext()) {
            Comparable<?> entry = (Comparable<?>) var2.next();
            if (Objects.equals(entry, o)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPartialView() {
        return false;
    }
}