package com.github.alexthe666.citadel.server.world;

import com.github.alexthe666.citadel.server.tick.ServerTickRateTracker;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class CitadelServerData extends SavedData {

    private static Map<MinecraftServer, CitadelServerData> dataMap = new HashMap();

    private static final String IDENTIFIER = "citadel_world_data";

    private MinecraftServer server;

    private ServerTickRateTracker tickRateTracker = null;

    public CitadelServerData(MinecraftServer server) {
        this.server = server;
    }

    public static CitadelServerData get(MinecraftServer server) {
        CitadelServerData fromMap = (CitadelServerData) dataMap.get(server);
        if (fromMap == null) {
            DimensionDataStorage storage = server.getLevel(Level.OVERWORLD).getDataStorage();
            CitadelServerData data = storage.computeIfAbsent(tag -> load(server, tag), () -> new CitadelServerData(server), "citadel_world_data");
            if (data != null) {
                data.m_77762_();
            }
            dataMap.put(server, data);
            return data;
        } else {
            return fromMap;
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        if (this.tickRateTracker != null) {
            tag.put("TickRateTracker", this.tickRateTracker.toTag());
        }
        return tag;
    }

    public static CitadelServerData load(MinecraftServer server, CompoundTag tag) {
        CitadelServerData data = new CitadelServerData(server);
        if (tag.contains("TickRateTracker")) {
            data.tickRateTracker = new ServerTickRateTracker(server, tag.getCompound("TickRateTracker"));
        } else {
            data.tickRateTracker = new ServerTickRateTracker(server);
        }
        return data;
    }

    public ServerTickRateTracker getOrCreateTickRateTracker() {
        if (this.tickRateTracker == null) {
            this.tickRateTracker = new ServerTickRateTracker(this.server);
        }
        return this.tickRateTracker;
    }
}