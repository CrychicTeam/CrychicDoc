package com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.MNode;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.Pathfinding;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;

public class PathJobMoveToLocation extends AbstractPathJob {

    private static final float DESTINATION_SLACK_NONE = 0.1F;

    private static final float DESTINATION_SLACK_ADJACENT = (float) Math.sqrt(2.0);

    private final BlockPos destination;

    private float destinationSlack = 0.1F;

    public PathJobMoveToLocation(Level world, BlockPos start, BlockPos end, int range, LivingEntity entity) {
        super(world, start, end, range, entity);
        this.destination = new BlockPos(end);
    }

    @Nullable
    @Override
    protected Path search() {
        if (Pathfinding.isDebug()) {
            IceAndFire.LOGGER.info(String.format("Pathfinding from [%d,%d,%d] to [%d,%d,%d]", this.start.m_123341_(), this.start.m_123342_(), this.start.m_123343_(), this.destination.m_123341_(), this.destination.m_123342_(), this.destination.m_123343_()));
        }
        if (this.getGroundHeight(null, this.destination) != this.destination.m_123342_()) {
            this.destinationSlack = DESTINATION_SLACK_ADJACENT;
        }
        return super.search();
    }

    @Override
    protected BlockPos getPathTargetPos(MNode finalMNode) {
        return this.destination;
    }

    @Override
    protected double computeHeuristic(BlockPos pos) {
        return Math.sqrt(this.destination.m_123331_(pos));
    }

    @Override
    protected boolean isAtDestination(MNode n) {
        if (!(this.destinationSlack <= 0.1F)) {
            return n.pos.m_123342_() == this.destination.m_123342_() - 1 ? this.destination.m_123314_(new Vec3i(n.pos.m_123341_(), this.destination.m_123342_(), n.pos.m_123343_()), (double) DESTINATION_SLACK_ADJACENT) : this.destination.m_123314_(n.pos, (double) DESTINATION_SLACK_ADJACENT);
        } else {
            return n.pos.m_123341_() == this.destination.m_123341_() && n.pos.m_123342_() == this.destination.m_123342_() && n.pos.m_123343_() == this.destination.m_123343_();
        }
    }

    @Override
    protected double getNodeResultScore(MNode n) {
        return this.destination.m_123331_(n.pos);
    }
}