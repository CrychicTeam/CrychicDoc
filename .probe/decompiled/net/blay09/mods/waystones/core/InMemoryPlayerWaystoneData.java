package net.blay09.mods.waystones.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.blay09.mods.waystones.api.IWaystone;
import net.minecraft.world.entity.player.Player;

public class InMemoryPlayerWaystoneData implements IPlayerWaystoneData {

    private final List<IWaystone> sortedWaystones = new ArrayList();

    private final Map<UUID, IWaystone> waystones = new HashMap();

    private long warpStoneCooldownUntil;

    private long inventoryButtonCooldownUntil;

    @Override
    public void activateWaystone(Player player, IWaystone waystone) {
        this.waystones.put(waystone.getWaystoneUid(), waystone);
        this.sortedWaystones.add(waystone);
    }

    @Override
    public boolean isWaystoneActivated(Player player, IWaystone waystone) {
        return this.waystones.containsKey(waystone.getWaystoneUid());
    }

    @Override
    public void deactivateWaystone(Player player, IWaystone waystone) {
        this.waystones.remove(waystone.getWaystoneUid());
        this.sortedWaystones.remove(waystone);
    }

    @Override
    public long getWarpStoneCooldownUntil(Player player) {
        return this.warpStoneCooldownUntil;
    }

    @Override
    public void setWarpStoneCooldownUntil(Player player, long timeStamp) {
        this.warpStoneCooldownUntil = timeStamp;
    }

    @Override
    public long getInventoryButtonCooldownUntil(Player player) {
        return this.inventoryButtonCooldownUntil;
    }

    @Override
    public void setInventoryButtonCooldownUntil(Player player, long timeStamp) {
        this.inventoryButtonCooldownUntil = timeStamp;
    }

    @Override
    public List<IWaystone> getWaystones(Player player) {
        return this.sortedWaystones;
    }

    @Override
    public void swapWaystoneSorting(Player player, int index, int otherIndex) {
        if (otherIndex == -1) {
            IWaystone waystone = (IWaystone) this.sortedWaystones.remove(index);
            this.sortedWaystones.add(0, waystone);
        } else if (otherIndex == this.sortedWaystones.size()) {
            IWaystone waystone = (IWaystone) this.sortedWaystones.remove(index);
            this.sortedWaystones.add(waystone);
        } else {
            Collections.swap(this.sortedWaystones, index, otherIndex);
        }
    }

    public void setWaystones(List<IWaystone> waystones) {
        this.sortedWaystones.clear();
        this.waystones.clear();
        this.sortedWaystones.addAll(waystones);
        for (IWaystone waystone : waystones) {
            this.waystones.put(waystone.getWaystoneUid(), waystone);
        }
    }
}