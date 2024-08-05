package net.blay09.mods.waystones.core;

import java.util.List;
import net.blay09.mods.waystones.api.IWaystone;
import net.minecraft.world.entity.player.Player;

public interface IPlayerWaystoneData {

    void activateWaystone(Player var1, IWaystone var2);

    boolean isWaystoneActivated(Player var1, IWaystone var2);

    void deactivateWaystone(Player var1, IWaystone var2);

    long getWarpStoneCooldownUntil(Player var1);

    void setWarpStoneCooldownUntil(Player var1, long var2);

    long getInventoryButtonCooldownUntil(Player var1);

    void setInventoryButtonCooldownUntil(Player var1, long var2);

    List<IWaystone> getWaystones(Player var1);

    void swapWaystoneSorting(Player var1, int var2, int var3);
}