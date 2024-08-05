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
import java.util.Map;
import java.util.zip.GZIPInputStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import noppes.npcs.CustomNpcs;
import noppes.npcs.controllers.data.SpawnData;
import noppes.npcs.shared.common.util.LogWriter;

public class SpawnController {

    public HashMap<ResourceLocation, List<SpawnData>> biomes = new HashMap();

    public ArrayList<SpawnData> data = new ArrayList();

    public RandomSource random = RandomSource.create();

    public static SpawnController instance;

    private int lastUsedID = 0;

    public SpawnController() {
        instance = this;
        this.loadData();
    }

    private void loadData() {
        File saveDir = CustomNpcs.getLevelSaveDirectory();
        if (saveDir != null) {
            try {
                File file = new File(saveDir, "spawns.dat");
                if (file.exists()) {
                    this.loadDataFile(file);
                }
            } catch (Exception var5) {
                try {
                    File filex = new File(saveDir, "spawns.dat_old");
                    if (filex.exists()) {
                        this.loadDataFile(filex);
                    }
                } catch (Exception var4) {
                }
            }
        }
    }

    private void loadDataFile(File file) throws IOException {
        DataInputStream var1x = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))));
        this.loadData(var1x);
        var1x.close();
    }

    public void loadData(DataInputStream stream) throws IOException {
        ArrayList<SpawnData> data = new ArrayList();
        CompoundTag nbttagcompound1 = NbtIo.read(stream);
        this.lastUsedID = nbttagcompound1.getInt("lastID");
        ListTag nbtlist = nbttagcompound1.getList("NPCSpawnData", 10);
        if (nbtlist != null) {
            for (int i = 0; i < nbtlist.size(); i++) {
                CompoundTag nbttagcompound = nbtlist.getCompound(i);
                SpawnData spawn = new SpawnData();
                spawn.readNBT(nbttagcompound);
                data.add(spawn);
            }
        }
        this.data = data;
        this.fillBiomeData();
    }

    public CompoundTag getNBT() {
        ListTag list = new ListTag();
        for (SpawnData spawn : this.data) {
            CompoundTag nbtfactions = new CompoundTag();
            spawn.writeNBT(nbtfactions);
            list.add(nbtfactions);
        }
        CompoundTag nbttagcompound = new CompoundTag();
        nbttagcompound.putInt("lastID", this.lastUsedID);
        nbttagcompound.put("NPCSpawnData", list);
        return nbttagcompound;
    }

    public void saveData() {
        try {
            File saveDir = CustomNpcs.getLevelSaveDirectory();
            File file = new File(saveDir, "spawns.dat_new");
            File file1 = new File(saveDir, "spawns.dat_old");
            File file2 = new File(saveDir, "spawns.dat");
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

    public SpawnData getSpawnData(int id) {
        for (SpawnData spawn : this.data) {
            if (spawn.id == id) {
                return spawn;
            }
        }
        return null;
    }

    public void saveSpawnData(SpawnData spawn) {
        if (spawn.id < 0) {
            spawn.id = this.getUnusedId();
        }
        SpawnData original = this.getSpawnData(spawn.id);
        if (original == null) {
            this.data.add(spawn);
        } else {
            original.readNBT(spawn.writeNBT(new CompoundTag()));
        }
        this.fillBiomeData();
        this.saveData();
    }

    private void fillBiomeData() {
        HashMap<ResourceLocation, List<SpawnData>> biomes = new HashMap();
        for (SpawnData spawn : this.data) {
            for (ResourceLocation s : spawn.biomes) {
                List<SpawnData> list = (List<SpawnData>) biomes.get(s);
                if (list == null) {
                    biomes.put(s, list = new ArrayList());
                }
                list.add(spawn);
            }
        }
        this.biomes = biomes;
    }

    public int getUnusedId() {
        this.lastUsedID++;
        return this.lastUsedID;
    }

    public void removeSpawnData(int id) {
        ArrayList<SpawnData> data = new ArrayList();
        for (SpawnData spawn : this.data) {
            if (spawn.id != id) {
                data.add(spawn);
            }
        }
        this.data = data;
        this.fillBiomeData();
        this.saveData();
    }

    public List<SpawnData> getSpawnList(ResourceLocation biome) {
        return (List<SpawnData>) this.biomes.get(biome);
    }

    public SpawnData getRandomSpawnData(ResourceLocation biome) {
        List<SpawnData> list = this.getSpawnList(biome);
        return list != null && !list.isEmpty() ? (SpawnData) WeightedRandom.getRandomItem(this.random, list).orElse(null) : null;
    }

    public boolean hasSpawnList(ResourceLocation biome) {
        return this.biomes.containsKey(biome) && !((List) this.biomes.get(biome)).isEmpty();
    }

    public Map<String, Integer> getScroll() {
        Map<String, Integer> map = new HashMap();
        for (SpawnData spawn : this.data) {
            map.put(spawn.name, spawn.id);
        }
        return map;
    }
}