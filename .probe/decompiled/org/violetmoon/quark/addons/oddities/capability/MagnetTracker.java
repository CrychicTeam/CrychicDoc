package org.violetmoon.quark.addons.oddities.capability;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.quark.api.IMagnetTracker;

public class MagnetTracker implements IMagnetTracker {

    private final Multimap<BlockPos, Force> forcesActing = HashMultimap.create();

    private final Level world;

    public MagnetTracker(Level world) {
        this.world = world;
    }

    @Override
    public Vec3i getNetForce(BlockPos pos) {
        Vec3i net = Vec3i.ZERO;
        for (Force force : this.forcesActing.get(pos)) {
            net = force.add(net);
        }
        return net;
    }

    @Override
    public void applyForce(BlockPos pos, int magnitude, boolean pushing, Direction dir, int distance, BlockPos origin) {
        this.forcesActing.put(pos, new Force(magnitude, pushing, dir, distance, origin));
    }

    @Override
    public void actOnForces(BlockPos pos) {
        Vec3i net = this.getNetForce(pos);
        if (!net.equals(Vec3i.ZERO)) {
            Direction target = Direction.getNearest((float) net.getX(), (float) net.getY(), (float) net.getZ());
            for (Force force : this.forcesActing.get(pos)) {
                if (force.direction() == target) {
                    BlockState origin = this.world.getBlockState(force.origin());
                    this.world.blockEvent(force.origin(), origin.m_60734_(), force.pushing() ? 0 : 1, force.distance());
                }
            }
        }
    }

    @Override
    public Collection<BlockPos> getTrackedPositions() {
        return this.forcesActing.keySet();
    }

    @Override
    public void clear() {
        this.forcesActing.clear();
    }
}