package malte0811.ferritecore.fastmap.immutable;

import com.google.common.collect.FerriteCoreImmutableMapAccess;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import java.util.Map.Entry;
import malte0811.ferritecore.ducks.FastMapStateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public class FastMapEntryImmutableMap extends FerriteCoreImmutableMapAccess<Property<?>, Comparable<?>> {

    private final FastMapStateHolder<?> viewedState;

    public FastMapEntryImmutableMap(FastMapStateHolder<?> viewedState) {
        this.viewedState = viewedState;
    }

    public int size() {
        return this.viewedState.getStateMap().numProperties();
    }

    public Comparable<?> get(@Nullable Object key) {
        return this.viewedState.getStateMap().getValue(this.viewedState.getStateIndex(), key);
    }

    public ImmutableSet<Entry<Property<?>, Comparable<?>>> createEntrySet() {
        return new FastMapEntryEntrySet(this.viewedState);
    }

    public ImmutableSet<Entry<Property<?>, Comparable<?>>> entrySet() {
        return new FastMapEntryEntrySet(this.viewedState);
    }

    public boolean isPartialView() {
        return false;
    }

    public ImmutableSet<Property<?>> createKeySet() {
        return this.viewedState.getStateMap().getPropertySet();
    }

    public ImmutableCollection<Comparable<?>> createValues() {
        return new FastMapValueSet(this.viewedState);
    }
}