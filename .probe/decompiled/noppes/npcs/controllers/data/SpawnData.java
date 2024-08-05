package noppes.npcs.controllers.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import noppes.npcs.NBTTags;

public class SpawnData implements WeightedEntry {

    public List<ResourceLocation> biomes = new ArrayList();

    public int id = -1;

    public String name = "";

    public Map<Integer, CloneSpawnData> data = new HashMap();

    public boolean liquid = false;

    public int type = 0;

    private Weight weight = Weight.of(10);

    public void readNBT(CompoundTag compound) {
        this.id = compound.getInt("SpawnId");
        this.name = compound.getString("SpawnName");
        this.setWeight(compound.getInt("SpawnWeight"));
        this.biomes = NBTTags.getResourceLocationList(compound.getList("SpawnBiomes", 10));
        this.data = CloneSpawnData.load(compound.getList("SpawnData", 10));
        this.type = compound.getInt("SpawnType");
    }

    public CompoundTag writeNBT(CompoundTag compound) {
        compound.putInt("SpawnId", this.id);
        compound.putString("SpawnName", this.name);
        compound.putInt("SpawnWeight", this.weight.asInt());
        compound.put("SpawnBiomes", NBTTags.nbtResourceLocationList(this.biomes));
        compound.put("SpawnData", CloneSpawnData.save(this.data));
        compound.putInt("SpawnType", this.type);
        return compound;
    }

    public void setWeight(int weight) {
        if (weight == 0) {
            weight = 1;
        }
        this.weight = Weight.of(weight);
    }

    public void setClone(int slot, int tab, String name) {
        this.data.put(slot, new CloneSpawnData(tab, name));
    }

    public CompoundTag getCompound(int slot) {
        CloneSpawnData sd = (CloneSpawnData) this.data.get(slot);
        return sd == null ? null : sd.getCompound();
    }

    @Override
    public Weight getWeight() {
        return this.weight;
    }
}