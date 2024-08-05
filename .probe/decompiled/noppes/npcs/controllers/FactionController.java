package noppes.npcs.controllers;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EventHooks;
import noppes.npcs.api.handler.IFactionHandler;
import noppes.npcs.api.handler.data.IFaction;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketSyncRemove;
import noppes.npcs.packets.client.PacketSyncUpdate;
import noppes.npcs.shared.common.util.LogWriter;

public class FactionController implements IFactionHandler {

    public HashMap<Integer, Faction> factionsSync = new HashMap();

    public HashMap<Integer, Faction> factions = new HashMap();

    public static FactionController instance = new FactionController();

    private int lastUsedID = 0;

    public FactionController() {
        instance = this;
        this.factions.put(0, new Faction(0, "Friendly", 56576, 2000));
        this.factions.put(1, new Faction(1, "Neutral", 15916288, 1000));
        this.factions.put(2, new Faction(2, "Aggressive", 14483456, 0));
    }

    public void load() {
        this.factions = new HashMap();
        this.lastUsedID = 0;
        try {
            File saveDir = CustomNpcs.getLevelSaveDirectory();
            if (saveDir == null) {
                return;
            }
            try {
                File file = new File(saveDir, "factions.dat");
                if (file.exists()) {
                    this.loadFactionsFile(file);
                }
            } catch (Exception var9) {
                try {
                    File filex = new File(saveDir, "factions.dat_old");
                    if (filex.exists()) {
                        this.loadFactionsFile(filex);
                    }
                } catch (Exception var8) {
                }
            }
        } finally {
            EventHooks.onGlobalFactionsLoaded(this);
            if (this.factions.isEmpty()) {
                this.factions.put(0, new Faction(0, "Friendly", 56576, 2000));
                this.factions.put(1, new Faction(1, "Neutral", 15916288, 1000));
                this.factions.put(2, new Faction(2, "Aggressive", 14483456, 0));
            }
        }
    }

    private void loadFactionsFile(File file) throws IOException {
        DataInputStream var1x = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))));
        this.loadFactions(var1x);
        var1x.close();
    }

    public void loadFactions(DataInputStream stream) throws IOException {
        HashMap<Integer, Faction> factions = new HashMap();
        CompoundTag nbttagcompound1 = NbtIo.read(stream);
        this.lastUsedID = nbttagcompound1.getInt("lastID");
        ListTag list = nbttagcompound1.getList("NPCFactions", 10);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                CompoundTag nbttagcompound = list.getCompound(i);
                Faction faction = new Faction();
                faction.readNBT(nbttagcompound);
                factions.put(faction.id, faction);
            }
        }
        this.factions = factions;
    }

    public CompoundTag getNBT() {
        ListTag list = new ListTag();
        for (int slot : this.factions.keySet()) {
            Faction faction = (Faction) this.factions.get(slot);
            CompoundTag nbtfactions = new CompoundTag();
            faction.writeNBT(nbtfactions);
            list.add(nbtfactions);
        }
        CompoundTag nbttagcompound = new CompoundTag();
        nbttagcompound.putInt("lastID", this.lastUsedID);
        nbttagcompound.put("NPCFactions", list);
        return nbttagcompound;
    }

    public void saveFactions() {
        try {
            File saveDir = CustomNpcs.getLevelSaveDirectory();
            File file = new File(saveDir, "factions.dat_new");
            File file1 = new File(saveDir, "factions.dat_old");
            File file2 = new File(saveDir, "factions.dat");
            NbtIo.writeCompressed(this.getNBT(), new FileOutputStream(file));
            if (file1.exists()) {
                file1.delete();
            }
            file2.renameTo(file1);
            if (file2.exists()) {
                file2.delete();
            }
            file.renameTo(file2);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception var5) {
            LogWriter.except(var5);
        }
    }

    public Faction getFaction(int faction) {
        return (Faction) this.factions.get(faction);
    }

    public void saveFaction(Faction faction) {
        if (faction.id < 0) {
            faction.id = this.getUnusedId();
            while (this.hasName(faction.name)) {
                faction.name = faction.name + "_";
            }
        } else {
            Faction existing = (Faction) this.factions.get(faction.id);
            if (existing != null && !existing.name.equals(faction.name)) {
                while (this.hasName(faction.name)) {
                    faction.name = faction.name + "_";
                }
            }
        }
        this.factions.remove(faction.id);
        this.factions.put(faction.id, faction);
        Packets.sendAll(new PacketSyncUpdate(faction.id, 1, faction.writeNBT(new CompoundTag())));
        this.saveFactions();
    }

    public int getUnusedId() {
        if (this.lastUsedID == 0) {
            for (int catid : this.factions.keySet()) {
                if (catid > this.lastUsedID) {
                    this.lastUsedID = catid;
                }
            }
        }
        this.lastUsedID++;
        return this.lastUsedID;
    }

    @Override
    public IFaction delete(int id) {
        if (id >= 0 && this.factions.size() > 1) {
            Faction faction = (Faction) this.factions.remove(id);
            if (faction == null) {
                return null;
            } else {
                this.saveFactions();
                faction.id = -1;
                Packets.sendAll(new PacketSyncRemove(id, 1));
                return faction;
            }
        } else {
            return null;
        }
    }

    public int getFirstFactionId() {
        return (Integer) this.factions.keySet().iterator().next();
    }

    public Faction getFirstFaction() {
        return (Faction) this.factions.values().iterator().next();
    }

    public boolean hasName(String newName) {
        if (newName.trim().isEmpty()) {
            return true;
        } else {
            for (Faction faction : this.factions.values()) {
                if (faction.name.equals(newName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public Faction getFactionFromName(String factioname) {
        for (Entry<Integer, Faction> entryfaction : this.factions.entrySet()) {
            if (((Faction) entryfaction.getValue()).name.equalsIgnoreCase(factioname)) {
                return (Faction) entryfaction.getValue();
            }
        }
        return null;
    }

    public String[] getNames() {
        String[] names = new String[this.factions.size()];
        int i = 0;
        for (Faction faction : this.factions.values()) {
            names[i] = faction.name.toLowerCase();
            i++;
        }
        return names;
    }

    @Override
    public List<IFaction> list() {
        return new ArrayList(this.factions.values());
    }

    @Override
    public IFaction create(String name, int color) {
        Faction faction = new Faction();
        while (this.hasName(name)) {
            name = name + "_";
        }
        faction.name = name;
        faction.color = color;
        this.saveFaction(faction);
        return faction;
    }

    @Override
    public IFaction get(int id) {
        return (IFaction) this.factions.get(id);
    }
}