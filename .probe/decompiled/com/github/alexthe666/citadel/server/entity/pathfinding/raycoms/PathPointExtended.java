package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.pathfinder.Node;

public class PathPointExtended extends Node {

    private boolean onLadder = false;

    private Direction ladderFacing = Direction.DOWN;

    private boolean onRails;

    private boolean railsEntry;

    private boolean railsExit;

    public PathPointExtended(BlockPos pos) {
        super(pos.m_123341_(), pos.m_123342_(), pos.m_123343_());
    }

    public boolean isOnLadder() {
        return this.onLadder;
    }

    public void setOnLadder(boolean onLadder) {
        this.onLadder = onLadder;
    }

    public Direction getLadderFacing() {
        return this.ladderFacing;
    }

    public void setLadderFacing(Direction ladderFacing) {
        this.ladderFacing = ladderFacing;
    }

    public void setOnRails(boolean isOnRails) {
        this.onRails = isOnRails;
    }

    public void setRailsEntry() {
        this.railsEntry = true;
    }

    public void setRailsExit() {
        this.railsExit = true;
    }

    public boolean isOnRails() {
        return this.onRails;
    }

    public boolean isRailsEntry() {
        return this.railsEntry;
    }

    public boolean isRailsExit() {
        return this.railsExit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || this.getClass() != o.getClass()) {
            return false;
        } else if (!super.equals(o)) {
            return false;
        } else {
            PathPointExtended that = (PathPointExtended) o;
            return this.onLadder != that.onLadder ? false : this.ladderFacing == that.ladderFacing;
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (this.onLadder ? 1 : 0);
        return 31 * result + this.ladderFacing.hashCode();
    }
}