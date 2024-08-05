package com.rekindled.embers.api.power;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public interface IEmberPacketProducer {

    void setTargetPosition(BlockPos var1, Direction var2);

    Direction getEmittingDirection(Direction var1);
}