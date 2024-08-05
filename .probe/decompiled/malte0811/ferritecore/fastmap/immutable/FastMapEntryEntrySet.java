package malte0811.ferritecore.fastmap.immutable;

import com.google.common.collect.FerriteCoreEntrySetAccess;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Map.Entry;
import malte0811.ferritecore.ducks.FastMapStateHolder;
import malte0811.ferritecore.fastmap.FastMap;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FastMapEntryEntrySet extends FerriteCoreEntrySetAccess<Property<?>, Comparable<?>> {

    private final FastMapStateHolder<?> viewedState;

    public FastMapEntryEntrySet(FastMapStateHolder<?> viewedState) {
        this.viewedState = viewedState;
    }

    @NotNull
    public UnmodifiableIterator<Entry<Property<?>, Comparable<?>>> iterator() {
        return new FastMapEntryIterator<Entry<Property<?>, Comparable<?>>>(this.viewedState) {

            protected Entry<Property<?>, Comparable<?>> getEntry(int propertyIndex, FastMap<?> map, int stateIndex) {
                return map.getEntry(propertyIndex, stateIndex);
            }
        };
    }

    public int size() {
        return this.viewedState.getStateMap().numProperties();
    }

    public boolean contains(@Nullable Object object) {
        if (!(object instanceof Entry<?, ?> entry)) {
            return false;
        } else {
            Comparable<?> valueInMap = this.viewedState.getStateMap().getValue(this.viewedState.getStateIndex(), entry.getKey());
            return valueInMap != null && valueInMap.equals(((Entry) object).getValue());
        }
    }

    public boolean isPartialView() {
        return false;
    }
}