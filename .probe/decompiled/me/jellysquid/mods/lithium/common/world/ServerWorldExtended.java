package me.jellysquid.mods.lithium.common.world;

import net.minecraft.world.entity.Mob;

public interface ServerWorldExtended {

    void setNavigationActive(Mob var1);

    void setNavigationInactive(Mob var1);
}