package org.embeddedt.modernfix.blockstate;

import com.google.common.collect.UnmodifiableIterator;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;

public class FerriteCorePostProcess {

    private static final boolean willPostProcess;

    private static final MethodHandle theTable;

    private static final MethodHandle toKeyIndex;

    private static final Object2IntMap<?> EMPTY_MAP;

    public static <O, S extends StateHolder<O, S>> void postProcess(StateDefinition<O, S> state) {
        if (willPostProcess) {
            try {
                if (state.getProperties().size() == 0) {
                    UnmodifiableIterator e = state.getPossibleStates().iterator();
                    while (e.hasNext()) {
                        S holder = (S) e.next();
                        Object table = (Object) theTable.invoke(holder);
                        toKeyIndex.invoke(table, EMPTY_MAP);
                    }
                }
            } catch (Throwable var4) {
                var4.printStackTrace();
            }
        }
    }

    static {
        boolean success = true;
        MethodHandle table = null;
        MethodHandle keyIndex = null;
        try {
            Class<?> fastMap = Class.forName("malte0811.ferritecore.fastmap.FastMap");
            Field field = fastMap.getDeclaredField("toKeyIndex");
            field.setAccessible(true);
            keyIndex = MethodHandles.publicLookup().unreflectSetter(field);
            field = StateHolder.class.getDeclaredField("ferritecore_globalTable");
            field.setAccessible(true);
            table = MethodHandles.publicLookup().unreflectGetter(field);
        } catch (RuntimeException | ReflectiveOperationException var5) {
            var5.printStackTrace();
            success = false;
        }
        willPostProcess = success;
        theTable = table;
        toKeyIndex = keyIndex;
        Object2IntArrayMap<?> map = new Object2IntArrayMap();
        map.defaultReturnValue(-1);
        EMPTY_MAP = Object2IntMaps.unmodifiable(map);
    }
}