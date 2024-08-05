package com.simibubi.create.content.trains.station;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

public interface StationMapData {

    boolean toggleStation(LevelAccessor var1, BlockPos var2, StationBlockEntity var3);

    void addStationMarker(StationMarker var1);
}