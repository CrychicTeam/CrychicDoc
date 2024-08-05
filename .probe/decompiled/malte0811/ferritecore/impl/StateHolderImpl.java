package malte0811.ferritecore.impl;

import com.google.common.collect.ImmutableTable;
import java.util.Map;
import malte0811.ferritecore.classloading.FastImmutableMapDefiner;
import malte0811.ferritecore.ducks.FastMapStateHolder;
import malte0811.ferritecore.fastmap.FastMap;
import malte0811.ferritecore.fastmap.table.CrashNeighborTable;
import malte0811.ferritecore.fastmap.table.FastmapNeighborTable;
import malte0811.ferritecore.mixin.config.FerriteConfig;
import net.minecraft.world.level.block.state.properties.Property;

public class StateHolderImpl {

    public static final ThreadLocal<Map<Map<Property<?>, Comparable<?>>, ?>> LAST_STATE_MAP = new ThreadLocal();

    public static final ThreadLocal<FastMap<?>> LAST_FAST_STATE_MAP = new ThreadLocal();

    public static <S> void populateNeighbors(Map<Map<Property<?>, Comparable<?>>, S> states, FastMapStateHolder<S> holder) {
        if (states.size() == 1) {
            holder.setNeighborTable(ImmutableTable.of());
        } else if (holder.getNeighborTable() != null) {
            throw new IllegalStateException();
        } else {
            if (states == LAST_STATE_MAP.get()) {
                holder.setStateMap((FastMap<S>) LAST_FAST_STATE_MAP.get());
            } else {
                LAST_STATE_MAP.set(states);
                FastMap<S> globalTable = new FastMap<>(holder.getVanillaPropertyMap().keySet(), states, FerriteConfig.COMPACT_FAST_MAP.isEnabled());
                holder.setStateMap(globalTable);
                LAST_FAST_STATE_MAP.set(globalTable);
            }
            int index = holder.getStateMap().getIndexOf(holder.getVanillaPropertyMap());
            holder.setStateIndex(index);
            if (FerriteConfig.PROPERTY_MAP.isEnabled()) {
                holder.replacePropertyMap(FastImmutableMapDefiner.makeMap(holder));
            }
            if (FerriteConfig.POPULATE_NEIGHBOR_TABLE.isEnabled()) {
                holder.setNeighborTable(new FastmapNeighborTable<>(holder));
            } else {
                holder.setNeighborTable(CrashNeighborTable.getInstance());
            }
        }
    }
}