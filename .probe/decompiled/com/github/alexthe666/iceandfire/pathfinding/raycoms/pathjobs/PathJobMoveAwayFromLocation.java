package com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.MNode;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.Pathfinding;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;

public class PathJobMoveAwayFromLocation extends AbstractPathJob {

    protected final BlockPos avoid;

    protected final int avoidDistance;

    public PathJobMoveAwayFromLocation(Level world, BlockPos start, BlockPos avoid, int avoidDistance, int range, LivingEntity entity) {
        super(world, start, avoid, range, entity);
        this.avoid = new BlockPos(avoid);
        this.avoidDistance = avoidDistance;
    }

    @Nullable
    @Override
    protected Path search() {
        if (Pathfinding.isDebug()) {
            IceAndFire.LOGGER.info(String.format("Pathfinding from [%d,%d,%d] away from [%d,%d,%d]", this.start.m_123341_(), this.start.m_123342_(), this.start.m_123343_(), this.avoid.m_123341_(), this.avoid.m_123342_(), this.avoid.m_123343_()));
        }
        return super.search();
    }

    @Override
    protected double computeHeuristic(BlockPos pos) {
        return -this.avoid.m_123331_(pos);
    }

    @Override
    protected boolean isAtDestination(MNode n) {
        return Math.sqrt(this.avoid.m_123331_(n.pos)) > (double) this.avoidDistance;
    }

    @Override
    protected double getNodeResultScore(MNode n) {
        return -this.avoid.m_123331_(n.pos);
    }
}