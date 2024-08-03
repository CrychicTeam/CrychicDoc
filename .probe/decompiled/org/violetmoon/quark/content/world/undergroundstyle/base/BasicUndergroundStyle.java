package org.violetmoon.quark.content.world.undergroundstyle.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BasicUndergroundStyle extends UndergroundStyle {

    public BlockState floorState;

    public BlockState ceilingState;

    public BlockState wallState;

    public final boolean mimicInside;

    public BasicUndergroundStyle(BlockState floorState, BlockState ceilingState, BlockState wallState) {
        this(floorState, ceilingState, wallState, false);
    }

    public BasicUndergroundStyle(BlockState floorState, BlockState ceilingState, BlockState wallState, boolean mimicInside) {
        this.floorState = floorState;
        this.ceilingState = ceilingState;
        this.wallState = wallState;
        this.mimicInside = mimicInside;
    }

    @Override
    public void fillFloor(UndergroundStyleGenerator.Context context, BlockPos pos, BlockState state) {
        if (this.floorState != null) {
            context.world.m_7731_(pos, this.floorState, 2);
        }
    }

    @Override
    public void fillCeiling(UndergroundStyleGenerator.Context context, BlockPos pos, BlockState state) {
        if (this.ceilingState != null) {
            context.world.m_7731_(pos, this.ceilingState, 2);
        }
    }

    @Override
    public void fillWall(UndergroundStyleGenerator.Context context, BlockPos pos, BlockState state) {
        if (this.wallState != null) {
            context.world.m_7731_(pos, this.wallState, 2);
        }
    }

    @Override
    public void fillInside(UndergroundStyleGenerator.Context context, BlockPos pos, BlockState state) {
        if (this.mimicInside) {
            this.fillWall(context, pos, state);
        }
    }
}