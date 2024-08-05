package com.craisinlord.idas.state;

import com.craisinlord.idas.IDAS;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

public class stateRegion {

    private final String regionKey;

    private final File regionFile;

    private final ConcurrentHashMap<Long, Boolean> stateMap = new ConcurrentHashMap();

    public stateRegion(Path basePath, String regionKey) {
        this.regionKey = regionKey;
        this.regionFile = basePath.resolve(regionKey).toFile();
        this.createRegionFileIfDoesNotExist();
    }

    public synchronized void setCleared(BlockPos pos, boolean isCleared) {
        this.stateMap.put(pos.asLong(), isCleared);
        this.createRegionFileIfDoesNotExist();
        CompoundTag compoundTag = this.readRegionFile();
        compoundTag.putBoolean(pos.toString(), isCleared);
        this.writeRegionFile(compoundTag);
    }

    public synchronized boolean isCleared(BlockPos pos) {
        long posAsLong = pos.asLong();
        if (this.stateMap.containsKey(posAsLong)) {
            return (Boolean) this.stateMap.get(posAsLong);
        } else {
            this.createRegionFileIfDoesNotExist();
            boolean isCleared = false;
            CompoundTag compoundTag = this.readRegionFile();
            if (compoundTag == null) {
                compoundTag = new CompoundTag();
                compoundTag.putBoolean(pos.toString(), isCleared);
                this.writeRegionFile(compoundTag);
            } else if (compoundTag.contains(pos.toString())) {
                isCleared = compoundTag.getBoolean(pos.toString());
            } else {
                compoundTag.putBoolean(pos.toString(), isCleared);
                this.writeRegionFile(compoundTag);
            }
            this.stateMap.put(posAsLong, isCleared);
            return isCleared;
        }
    }

    public synchronized void reset() {
        this.stateMap.clear();
    }

    private void writeRegionFile(CompoundTag compoundTag) {
        try {
            NbtIo.write(compoundTag, this.regionFile);
        } catch (IOException var3) {
            IDAS.LOGGER.error("Encountered error writing data to Labyrinth region file {}", this.regionKey);
            IDAS.LOGGER.error(var3);
        }
    }

    private CompoundTag readRegionFile() {
        try {
            return NbtIo.read(this.regionFile);
        } catch (IOException var2) {
            IDAS.LOGGER.error("Encountered error reading data from Labyrinth region file {}", this.regionKey);
            IDAS.LOGGER.error(var2);
            return new CompoundTag();
        }
    }

    private synchronized void createRegionFileIfDoesNotExist() {
        if (!this.regionFile.exists()) {
            try {
                this.regionFile.createNewFile();
                NbtIo.write(new CompoundTag(), this.regionFile);
            } catch (IOException var2) {
                IDAS.LOGGER.error("Unable to create region file for region {}", this.regionKey);
                IDAS.LOGGER.error(var2);
            }
        }
    }
}