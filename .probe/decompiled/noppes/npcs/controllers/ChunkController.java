package noppes.npcs.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import noppes.npcs.CustomNpcs;

public class ChunkController {

    public static ChunkController instance = new ChunkController();

    private HashMap<Long, List<UUID>> loaded = new HashMap();

    public ChunkController() {
        instance = this;
    }

    public void clear() {
        this.loaded = new HashMap();
    }

    public void unload(ServerLevel world, UUID id, int xChunk, int zChunk) {
        long i = ChunkPos.asLong(xChunk, zChunk);
        List<UUID> list = (List<UUID>) this.loaded.get(i);
        if (list != null) {
            list.remove(id);
            if (list.size() == 0) {
                world.setChunkForced(xChunk, zChunk, false);
                this.loaded.remove(i);
            }
        }
    }

    public void load(ServerLevel world, UUID id, int xChunk, int zChunk) {
        if (this.size() < CustomNpcs.ChuckLoaders) {
            long i = ChunkPos.asLong(xChunk, zChunk);
            List<UUID> list = (List<UUID>) this.loaded.get(i);
            if (list == null) {
                this.loaded.put(i, list = new ArrayList());
            }
            list.add(id);
            if (list.size() == 1) {
                world.setChunkForced(xChunk, zChunk, true);
            }
        }
    }

    public int size() {
        return this.loaded.size();
    }
}