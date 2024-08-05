package net.minecraftforge.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public interface IForgeFluidState {

    private FluidState self() {
        return (FluidState) this;
    }

    default float getExplosionResistance(BlockGetter level, BlockPos pos, Explosion explosion) {
        return this.self().getType().getExplosionResistance(this.self(), level, pos, explosion);
    }

    default FluidType getFluidType() {
        return this.self().getType().getFluidType();
    }

    default boolean move(LivingEntity entity, Vec3 movementVector, double gravity) {
        return this.self().getType().move(this.self(), entity, movementVector, gravity);
    }

    default boolean canConvertToSource(Level level, BlockPos pos) {
        return this.self().getType().canConvertToSource(this.self(), level, pos);
    }

    default boolean supportsBoating(Boat boat) {
        return this.self().getType().supportsBoating(this.self(), boat);
    }

    default boolean shouldUpdateWhileBoating(Boat boat, Entity rider) {
        return this.self().getType().shouldUpdateWhileBoating(this.self(), boat, rider);
    }

    @Nullable
    default BlockPathTypes getBlockPathType(BlockGetter level, BlockPos pos, @Nullable Mob mob, boolean canFluidLog) {
        return this.self().getType().getBlockPathType(this.self(), level, pos, mob, canFluidLog);
    }

    @Nullable
    default BlockPathTypes getAdjacentBlockPathType(BlockGetter level, BlockPos pos, @Nullable Mob mob, BlockPathTypes originalType) {
        return this.self().getType().getAdjacentBlockPathType(this.self(), level, pos, mob, originalType);
    }

    default boolean canHydrate(BlockGetter getter, BlockPos pos, BlockState source, BlockPos sourcePos) {
        return this.self().getType().canHydrate(this.self(), getter, pos, source, sourcePos);
    }

    default boolean canExtinguish(BlockGetter getter, BlockPos pos) {
        return this.self().getType().canExtinguish(this.self(), getter, pos);
    }
}