package noppes.npcs.controllers.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import noppes.npcs.controllers.ServerCloneController;

public class CloneSpawnData {

    public int tab;

    public String name;

    private long lastLoaded;

    private CompoundTag compound;

    public CloneSpawnData(int tab, String name) {
        this.name = name;
        this.tab = tab;
    }

    public CompoundTag getCompound() {
        if (this.lastLoaded < ServerCloneController.Instance.lastLoaded) {
            this.compound = ServerCloneController.Instance.getCloneData(null, this.name, this.tab);
            this.lastLoaded = ServerCloneController.Instance.lastLoaded;
        }
        return this.compound;
    }

    public static Map<Integer, CloneSpawnData> load(ListTag list) {
        Map<Integer, CloneSpawnData> data = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            CompoundTag c = list.getCompound(i);
            int tab = c.getInt("tab");
            String name = c.getString("name");
            if (ServerCloneController.Instance == null || ServerCloneController.Instance.hasClone(tab, name)) {
                data.put(c.getInt("slot"), new CloneSpawnData(tab, name));
            }
        }
        return data;
    }

    public static ListTag save(Map<Integer, CloneSpawnData> data) {
        ListTag list = new ListTag();
        for (Entry<Integer, CloneSpawnData> entry : data.entrySet()) {
            if (ServerCloneController.Instance == null || ServerCloneController.Instance.hasClone(((CloneSpawnData) entry.getValue()).tab, ((CloneSpawnData) entry.getValue()).name)) {
                CompoundTag c = new CompoundTag();
                c.putInt("slot", (Integer) entry.getKey());
                c.putInt("tab", ((CloneSpawnData) entry.getValue()).tab);
                c.putString("name", ((CloneSpawnData) entry.getValue()).name);
                list.add(c);
            }
        }
        return list;
    }
}