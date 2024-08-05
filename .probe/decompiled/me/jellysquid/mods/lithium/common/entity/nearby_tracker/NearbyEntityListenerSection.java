package me.jellysquid.mods.lithium.common.entity.nearby_tracker;

import net.minecraft.world.level.entity.EntitySectionStorage;

public interface NearbyEntityListenerSection {

    void addListener(NearbyEntityListener var1);

    void removeListener(EntitySectionStorage<?> var1, NearbyEntityListener var2);
}