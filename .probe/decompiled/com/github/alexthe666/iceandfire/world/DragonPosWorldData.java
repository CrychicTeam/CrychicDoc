package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

public class DragonPosWorldData extends SavedData {

    private static final String IDENTIFIER = "iceandfire_dragonPositions";

    protected final Map<UUID, BlockPos> lastDragonPositions = new HashMap();

    private Level world;

    private int tickCounter;

    public DragonPosWorldData() {
    }

    public DragonPosWorldData(Level world) {
        this.world = world;
        this.m_77762_();
    }

    public DragonPosWorldData(CompoundTag compoundTag) {
        this.load(compoundTag);
    }

    public static DragonPosWorldData get(Level world) {
        if (world instanceof ServerLevel) {
            ServerLevel overworld = world.getServer().getLevel(world.dimension());
            DimensionDataStorage storage = overworld.getDataStorage();
            DragonPosWorldData data = storage.computeIfAbsent(DragonPosWorldData::new, DragonPosWorldData::new, "iceandfire_dragonPositions");
            if (data != null) {
                data.world = world;
                data.m_77762_();
            }
            return data;
        } else {
            return null;
        }
    }

    public void addDragon(UUID uuid, BlockPos pos) {
        this.lastDragonPositions.put(uuid, pos);
        this.m_77762_();
    }

    public void removeDragon(UUID uuid) {
        this.lastDragonPositions.remove(uuid);
        this.m_77762_();
    }

    public BlockPos getDragonPos(UUID uuid) {
        return (BlockPos) this.lastDragonPositions.get(uuid);
    }

    public void debug() {
        IceAndFire.LOGGER.warn(this.lastDragonPositions.toString());
    }

    public void tick() {
        this.tickCounter++;
    }

    public DragonPosWorldData load(CompoundTag nbt) {
        this.tickCounter = nbt.getInt("Tick");
        ListTag nbttaglist = nbt.getList("DragonMap", 10);
        this.lastDragonPositions.clear();
        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag CompoundNBT = nbttaglist.getCompound(i);
            UUID uuid = CompoundNBT.getUUID("DragonUUID");
            BlockPos pos = new BlockPos(CompoundNBT.getInt("DragonPosX"), CompoundNBT.getInt("DragonPosY"), CompoundNBT.getInt("DragonPosZ"));
            this.lastDragonPositions.put(uuid, pos);
        }
        return this;
    }

    @NotNull
    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("Tick", this.tickCounter);
        ListTag nbttaglist = new ListTag();
        for (Entry<UUID, BlockPos> pair : this.lastDragonPositions.entrySet()) {
            CompoundTag CompoundNBT = new CompoundTag();
            CompoundNBT.putUUID("DragonUUID", (UUID) pair.getKey());
            CompoundNBT.putInt("DragonPosX", ((BlockPos) pair.getValue()).m_123341_());
            CompoundNBT.putInt("DragonPosY", ((BlockPos) pair.getValue()).m_123342_());
            CompoundNBT.putInt("DragonPosZ", ((BlockPos) pair.getValue()).m_123343_());
            nbttaglist.add(CompoundNBT);
        }
        compound.put("DragonMap", nbttaglist);
        return compound;
    }
}