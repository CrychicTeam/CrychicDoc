package malte0811.ferritecore.ducks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import malte0811.ferritecore.fastmap.FastMap;
import net.minecraft.world.level.block.state.properties.Property;

public interface FastMapStateHolder<S> {

    FastMap<S> getStateMap();

    void setStateMap(FastMap<S> var1);

    int getStateIndex();

    void setStateIndex(int var1);

    ImmutableMap<Property<?>, Comparable<?>> getVanillaPropertyMap();

    void replacePropertyMap(ImmutableMap<Property<?>, Comparable<?>> var1);

    void setNeighborTable(Table<Property<?>, Comparable<?>, S> var1);

    Table<Property<?>, Comparable<?>, S> getNeighborTable();
}