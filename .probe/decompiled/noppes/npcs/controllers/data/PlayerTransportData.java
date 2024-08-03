package noppes.npcs.controllers.data;

import java.util.HashSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class PlayerTransportData {

    public HashSet<Integer> transports = new HashSet();

    public void loadNBTData(CompoundTag compound) {
        HashSet<Integer> dialogsRead = new HashSet();
        if (compound != null) {
            ListTag list = compound.getList("TransportData", 10);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    CompoundTag nbttagcompound = list.getCompound(i);
                    dialogsRead.add(nbttagcompound.getInt("Transport"));
                }
                this.transports = dialogsRead;
            }
        }
    }

    public void saveNBTData(CompoundTag compound) {
        ListTag list = new ListTag();
        for (int dia : this.transports) {
            CompoundTag nbttagcompound = new CompoundTag();
            nbttagcompound.putInt("Transport", dia);
            list.add(nbttagcompound);
        }
        compound.put("TransportData", list);
    }
}