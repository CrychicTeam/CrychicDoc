package com.simibubi.create.compat.computercraft.implementation.peripherals;

import com.simibubi.create.content.kinetics.gauge.SpeedGaugeBlockEntity;
import dan200.computercraft.api.lua.LuaFunction;
import org.jetbrains.annotations.NotNull;

public class SpeedGaugePeripheral extends SyncedPeripheral<SpeedGaugeBlockEntity> {

    public SpeedGaugePeripheral(SpeedGaugeBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final float getSpeed() {
        return this.blockEntity.getSpeed();
    }

    @NotNull
    public String getType() {
        return "Create_Speedometer";
    }
}