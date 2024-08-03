package com.simibubi.create.compat.computercraft.implementation.peripherals;

import com.simibubi.create.content.kinetics.gauge.StressGaugeBlockEntity;
import dan200.computercraft.api.lua.LuaFunction;
import org.jetbrains.annotations.NotNull;

public class StressGaugePeripheral extends SyncedPeripheral<StressGaugeBlockEntity> {

    public StressGaugePeripheral(StressGaugeBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final float getStress() {
        return this.blockEntity.getNetworkStress();
    }

    @LuaFunction
    public final float getStressCapacity() {
        return this.blockEntity.getNetworkCapacity();
    }

    @NotNull
    public String getType() {
        return "Create_Stressometer";
    }
}