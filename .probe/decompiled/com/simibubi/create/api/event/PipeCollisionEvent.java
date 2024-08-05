package com.simibubi.create.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

public class PipeCollisionEvent extends Event {

    private final Level level;

    private final BlockPos pos;

    protected final Fluid firstFluid;

    protected final Fluid secondFluid;

    @Nullable
    private BlockState state;

    protected PipeCollisionEvent(Level level, BlockPos pos, Fluid firstFluid, Fluid secondFluid, @Nullable BlockState defaultState) {
        this.level = level;
        this.pos = pos;
        this.firstFluid = firstFluid;
        this.secondFluid = secondFluid;
        this.state = defaultState;
    }

    public Level getLevel() {
        return this.level;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    @Nullable
    public BlockState getState() {
        return this.state;
    }

    public void setState(@Nullable BlockState state) {
        this.state = state;
    }

    public static class Flow extends PipeCollisionEvent {

        public Flow(Level level, BlockPos pos, Fluid firstFluid, Fluid secondFluid, @Nullable BlockState defaultState) {
            super(level, pos, firstFluid, secondFluid, defaultState);
        }

        public Fluid getFirstFluid() {
            return this.firstFluid;
        }

        public Fluid getSecondFluid() {
            return this.secondFluid;
        }
    }

    public static class Spill extends PipeCollisionEvent {

        public Spill(Level level, BlockPos pos, Fluid worldFluid, Fluid pipeFluid, @Nullable BlockState defaultState) {
            super(level, pos, worldFluid, pipeFluid, defaultState);
        }

        public Fluid getWorldFluid() {
            return this.firstFluid;
        }

        public Fluid getPipeFluid() {
            return this.secondFluid;
        }
    }
}