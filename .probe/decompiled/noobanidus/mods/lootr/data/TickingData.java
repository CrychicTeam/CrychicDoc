package noobanidus.mods.lootr.data;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.io.File;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;

public class TickingData extends SavedData {

    private final Object2IntMap<UUID> tickMap = new Object2IntOpenHashMap();

    public TickingData() {
        this.tickMap.defaultReturnValue(-1);
    }

    public static TickingData load(CompoundTag pCompound) {
        TickingData data = new TickingData();
        data.tickMap.clear();
        data.tickMap.defaultReturnValue(-1);
        ListTag decayList = pCompound.getList("result", 10);
        for (int i = 0; i < decayList.size(); i++) {
            CompoundTag thisTag = decayList.getCompound(i);
            data.tickMap.put(thisTag.getUUID("id"), thisTag.getInt("value"));
        }
        return data;
    }

    public boolean isComplete(UUID id) {
        return this.tickMap.getInt(id) == 0 || this.tickMap.getInt(id) == 1;
    }

    public int getValue(UUID id) {
        return this.tickMap.getInt(id);
    }

    public boolean setValue(UUID id, int decayAmount) {
        return this.tickMap.put(id, decayAmount) == -1;
    }

    public int remove(UUID id) {
        return this.tickMap.removeInt(id);
    }

    public boolean tick() {
        if (this.tickMap.isEmpty()) {
            return false;
        } else {
            Object2IntMap<UUID> newMap = new Object2IntOpenHashMap();
            newMap.defaultReturnValue(-1);
            boolean changed = false;
            ObjectIterator var3 = this.tickMap.object2IntEntrySet().iterator();
            while (var3.hasNext()) {
                Entry<UUID> entry = (Entry<UUID>) var3.next();
                int value = entry.getIntValue();
                if (value > 0) {
                    value--;
                    changed = true;
                }
                newMap.put((UUID) entry.getKey(), value);
            }
            if (changed) {
                this.tickMap.clear();
                this.tickMap.putAll(newMap);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public CompoundTag save(CompoundTag pCompound) {
        ListTag decayList = new ListTag();
        ObjectIterator var3 = this.tickMap.object2IntEntrySet().iterator();
        while (var3.hasNext()) {
            Entry<UUID> entry = (Entry<UUID>) var3.next();
            CompoundTag thisTag = new CompoundTag();
            thisTag.putUUID("id", (UUID) entry.getKey());
            thisTag.putInt("value", entry.getIntValue());
            decayList.add(thisTag);
        }
        pCompound.put("result", decayList);
        return pCompound;
    }

    @Override
    public void save(File pFile) {
        if (this.m_77764_()) {
            pFile.getParentFile().mkdirs();
        }
        super.save(pFile);
    }
}