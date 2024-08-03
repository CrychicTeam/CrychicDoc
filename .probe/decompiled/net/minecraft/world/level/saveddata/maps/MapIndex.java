package net.minecraft.world.level.saveddata.maps;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class MapIndex extends SavedData {

    public static final String FILE_NAME = "idcounts";

    private final Object2IntMap<String> usedAuxIds = new Object2IntOpenHashMap();

    public MapIndex() {
        this.usedAuxIds.defaultReturnValue(-1);
    }

    public static MapIndex load(CompoundTag compoundTag0) {
        MapIndex $$1 = new MapIndex();
        for (String $$2 : compoundTag0.getAllKeys()) {
            if (compoundTag0.contains($$2, 99)) {
                $$1.usedAuxIds.put($$2, compoundTag0.getInt($$2));
            }
        }
        return $$1;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag0) {
        ObjectIterator var2 = this.usedAuxIds.object2IntEntrySet().iterator();
        while (var2.hasNext()) {
            Entry<String> $$1 = (Entry<String>) var2.next();
            compoundTag0.putInt((String) $$1.getKey(), $$1.getIntValue());
        }
        return compoundTag0;
    }

    public int getFreeAuxValueForMap() {
        int $$0 = this.usedAuxIds.getInt("map") + 1;
        this.usedAuxIds.put("map", $$0);
        this.m_77762_();
        return $$0;
    }
}