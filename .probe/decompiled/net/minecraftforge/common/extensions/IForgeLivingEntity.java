package net.minecraftforge.common.extensions;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;

public interface IForgeLivingEntity extends IForgeEntity {

    default LivingEntity self() {
        return (LivingEntity) this;
    }

    @Override
    default boolean canSwimInFluidType(FluidType type) {
        return type == ForgeMod.WATER_TYPE.get() ? !this.self().isSensitiveToWater() : IForgeEntity.super.canSwimInFluidType(type);
    }

    default void jumpInFluid(FluidType type) {
        this.self().m_20256_(this.self().m_20184_().add(0.0, 0.04F * this.self().getAttributeValue(ForgeMod.SWIM_SPEED.get()), 0.0));
    }

    default void sinkInFluid(FluidType type) {
        this.self().m_20256_(this.self().m_20184_().add(0.0, -0.04F * this.self().getAttributeValue(ForgeMod.SWIM_SPEED.get()), 0.0));
    }

    default boolean canDrownInFluidType(FluidType type) {
        return type == ForgeMod.WATER_TYPE.get() ? !this.self().canBreatheUnderwater() : type.canDrownIn(this.self());
    }

    default boolean moveInFluid(FluidState state, Vec3 movementVector, double gravity) {
        return state.move(this.self(), movementVector, gravity);
    }
}