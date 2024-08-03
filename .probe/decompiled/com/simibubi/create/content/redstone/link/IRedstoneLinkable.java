package com.simibubi.create.content.redstone.link;

import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.core.BlockPos;

public interface IRedstoneLinkable {

    int getTransmittedStrength();

    void setReceivedStrength(int var1);

    boolean isListening();

    boolean isAlive();

    Couple<RedstoneLinkNetworkHandler.Frequency> getNetworkKey();

    BlockPos getLocation();
}