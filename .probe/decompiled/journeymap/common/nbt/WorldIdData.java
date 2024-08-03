package journeymap.common.nbt;

import java.util.UUID;
import journeymap.common.LoaderHooks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class WorldIdData extends SavedData {

    private static final String DAT_FILE = "WorldUUID";

    private static final String WORLD_ID_KEY = "world_uuid";

    private CompoundTag data;

    public WorldIdData() {
        String worldID = UUID.randomUUID().toString();
        this.data = new CompoundTag();
        LoaderHooks.getServer().getLevel(Level.OVERWORLD).getDataStorage().set("WorldUUID", this);
        this.data.putString("world_uuid", worldID);
        this.m_77762_();
    }

    public static String getWorldId() {
        return get().getNBTWorldID();
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.put("WorldUUID", get().data);
        return compound;
    }

    static WorldIdData get() {
        ServerLevel level = LoaderHooks.getServer().getLevel(Level.OVERWORLD);
        return level.getDataStorage().computeIfAbsent(WorldIdData::load, WorldIdData::new, "WorldUUID");
    }

    private static WorldIdData load(CompoundTag nbt) {
        WorldIdData worldIdData = new WorldIdData();
        worldIdData.data = nbt.getCompound("WorldUUID");
        return worldIdData;
    }

    private String getNBTWorldID() {
        return this.data.contains("world_uuid") ? this.data.getString("world_uuid") : "noWorldIDFound";
    }
}