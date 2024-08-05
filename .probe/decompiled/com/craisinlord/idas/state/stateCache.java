package com.craisinlord.idas.state;

import com.craisinlord.idas.IDAS;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

public class stateCache {

    public ConcurrentHashMap<String, stateRegion> stateRegionMap = new ConcurrentHashMap();

    private final Path savePath;

    public stateCache(Path dimensionPath) {
        this.savePath = dimensionPath.resolve("idas");
        this.createDirectoryIfDoesNotExist();
    }

    public boolean isCleared(BlockPos pos) {
        String regionKey = this.getRegionKey(pos);
        stateRegion stateRegion = (stateRegion) this.stateRegionMap.computeIfAbsent(regionKey, key -> new stateRegion(this.savePath, key));
        return stateRegion.isCleared(pos);
    }

    public void setCleared(BlockPos pos, boolean isCleared) {
        String regionKey = this.getRegionKey(pos);
        stateRegion stateRegion = (stateRegion) this.stateRegionMap.computeIfAbsent(regionKey, key -> new stateRegion(this.savePath, key));
        stateRegion.setCleared(pos, isCleared);
    }

    private String getRegionKey(BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        return "r." + chunkPos.getRegionX() + "." + chunkPos.getRegionZ() + ".labyrinths";
    }

    private void createDirectoryIfDoesNotExist() {
        try {
            Files.createDirectories(this.savePath);
        } catch (IOException var2) {
            IDAS.LOGGER.error("Unable to create labyrinths save path {}", this.savePath);
            IDAS.LOGGER.error(var2);
        }
    }
}