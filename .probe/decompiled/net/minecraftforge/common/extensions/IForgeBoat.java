package net.minecraftforge.common.extensions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidType;

public interface IForgeBoat {

    private Boat self() {
        return (Boat) this;
    }

    default boolean canBoatInFluid(FluidState state) {
        return state.supportsBoating(this.self());
    }

    default boolean canBoatInFluid(FluidType type) {
        return type.supportsBoating(this.self());
    }

    default boolean shouldUpdateFluidWhileRiding(FluidState state, Entity rider) {
        return state.shouldUpdateWhileBoating(this.self(), rider);
    }
}