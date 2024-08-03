package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.CustomNpcs;
import noppes.npcs.controllers.data.TransportCategory;
import noppes.npcs.controllers.data.TransportLocation;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTransporter;
import noppes.npcs.shared.common.util.LogWriter;

public class TransportController {

    private HashMap<Integer, TransportLocation> locations = new HashMap();

    public HashMap<Integer, TransportCategory> categories = new HashMap();

    private int lastUsedID = 0;

    private static TransportController instance;

    public TransportController() {
        instance = this;
        this.loadCategories();
        if (this.categories.isEmpty()) {
            TransportCategory cat = new TransportCategory();
            cat.id = 1;
            cat.title = "Default";
            this.categories.put(cat.id, cat);
        }
    }

    private void loadCategories() {
        File saveDir = CustomNpcs.getLevelSaveDirectory();
        if (saveDir != null) {
            try {
                File file = new File(saveDir, "transport.dat");
                if (!file.exists()) {
                    return;
                }
                this.loadCategories(file);
            } catch (IOException var5) {
                try {
                    File file = new File(saveDir, "transport.dat_old");
                    if (!file.exists()) {
                        return;
                    }
                    this.loadCategories(file);
                } catch (IOException var4) {
                }
            }
        }
    }

    public void loadCategories(File file) throws IOException {
        HashMap<Integer, TransportLocation> locations = new HashMap();
        HashMap<Integer, TransportCategory> categories = new HashMap();
        CompoundTag nbttagcompound1 = NbtIo.readCompressed(new FileInputStream(file));
        this.lastUsedID = nbttagcompound1.getInt("lastID");
        ListTag list = nbttagcompound1.getList("NPCTransportCategories", 10);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                TransportCategory category = new TransportCategory();
                CompoundTag compound = list.getCompound(i);
                category.readNBT(compound);
                for (TransportLocation location : category.locations.values()) {
                    locations.put(location.id, location);
                }
                categories.put(category.id, category);
            }
            this.locations = locations;
            this.categories = categories;
        }
    }

    public CompoundTag getNBT() {
        ListTag list = new ListTag();
        for (TransportCategory category : this.categories.values()) {
            CompoundTag compound = new CompoundTag();
            category.writeNBT(compound);
            list.add(compound);
        }
        CompoundTag nbttagcompound = new CompoundTag();
        nbttagcompound.putInt("lastID", this.lastUsedID);
        nbttagcompound.put("NPCTransportCategories", list);
        return nbttagcompound;
    }

    public void saveCategories() {
        try {
            File saveDir = CustomNpcs.getLevelSaveDirectory();
            File file = new File(saveDir, "transport.dat_new");
            File file1 = new File(saveDir, "transport.dat_old");
            File file2 = new File(saveDir, "transport.dat");
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

    public TransportLocation getTransport(int transportId) {
        return (TransportLocation) this.locations.get(transportId);
    }

    public TransportLocation getTransport(String name) {
        for (TransportLocation loc : this.locations.values()) {
            if (loc.name.equals(name)) {
                return loc;
            }
        }
        return null;
    }

    private int getUniqueIdLocation() {
        if (this.lastUsedID == 0) {
            for (int catid : this.locations.keySet()) {
                if (catid > this.lastUsedID) {
                    this.lastUsedID = catid;
                }
            }
        }
        this.lastUsedID++;
        return this.lastUsedID;
    }

    private int getUniqueIdCategory() {
        int id = 0;
        for (int catid : this.categories.keySet()) {
            if (catid > id) {
                id = catid;
            }
        }
        return id + 1;
    }

    public void setLocation(TransportLocation location) {
        if (this.locations.containsKey(location.id)) {
            for (TransportCategory cat : this.categories.values()) {
                cat.locations.remove(location.id);
            }
        }
        this.locations.put(location.id, location);
        location.category.locations.put(location.id, location);
    }

    public TransportLocation removeLocation(int location) {
        TransportLocation loc = (TransportLocation) this.locations.get(location);
        if (loc == null) {
            return null;
        } else {
            loc.category.locations.remove(location);
            this.locations.remove(location);
            this.saveCategories();
            return loc;
        }
    }

    private boolean containsCategoryName(String name) {
        name = name.toLowerCase();
        for (TransportCategory cat : this.categories.values()) {
            if (cat.title.toLowerCase().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void saveCategory(String name, int id) {
        if (id < 0) {
            id = this.getUniqueIdCategory();
        }
        if (this.categories.containsKey(id)) {
            TransportCategory category = (TransportCategory) this.categories.get(id);
            if (!category.title.equals(name)) {
                while (this.containsCategoryName(name)) {
                    name = name + "_";
                }
                ((TransportCategory) this.categories.get(id)).title = name;
            }
        } else {
            while (this.containsCategoryName(name)) {
                name = name + "_";
            }
            TransportCategory category = new TransportCategory();
            category.id = id;
            category.title = name;
            this.categories.put(id, category);
        }
        this.saveCategories();
    }

    public void removeCategory(int id) {
        if (this.categories.size() != 1) {
            TransportCategory cat = (TransportCategory) this.categories.get(id);
            if (cat != null) {
                for (int i : cat.locations.keySet()) {
                    this.locations.remove(i);
                }
                this.categories.remove(id);
                this.saveCategories();
            }
        }
    }

    public boolean containsLocationName(String name) {
        name = name.toLowerCase();
        for (TransportLocation loc : this.locations.values()) {
            if (loc.name.toLowerCase().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static TransportController getInstance() {
        return instance;
    }

    public TransportLocation saveLocation(int categoryId, CompoundTag compound, ServerPlayer player, EntityNPCInterface npc) {
        TransportCategory category = (TransportCategory) this.categories.get(categoryId);
        if (category != null && npc.role.getType() == 4) {
            RoleTransporter role = (RoleTransporter) npc.role;
            TransportLocation location = new TransportLocation();
            location.readNBT(compound);
            location.category = category;
            if (role.hasTransport()) {
                location.id = role.transportId;
            }
            if (location.id < 0 || !((TransportLocation) this.locations.get(location.id)).name.equals(location.name)) {
                while (this.containsLocationName(location.name)) {
                    location.name = location.name + "_";
                }
            }
            if (location.id < 0) {
                location.id = this.getUniqueIdLocation();
            }
            category.locations.put(location.id, location);
            this.locations.put(location.id, location);
            this.saveCategories();
            return location;
        } else {
            return null;
        }
    }
}