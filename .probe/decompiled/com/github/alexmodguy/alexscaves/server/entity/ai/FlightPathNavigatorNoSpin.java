package com.github.alexmodguy.alexscaves.server.entity.ai;

import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FlightPathNavigatorNoSpin extends FlyingPathNavigation {

    private float distancemodifier = 0.75F;

    public FlightPathNavigatorNoSpin(Mob entitylivingIn, Level worldIn) {
        super(entitylivingIn, worldIn);
    }

    public FlightPathNavigatorNoSpin(Mob entitylivingIn, Level worldIn, float distancemodifier) {
        super(entitylivingIn, worldIn);
        this.distancemodifier = distancemodifier;
    }

    @Override
    protected void followThePath() {
        Vec3 vector3d = this.m_7475_();
        this.f_26505_ = this.f_26494_.m_20205_() * this.distancemodifier;
        Vec3i vector3i = this.f_26496_.getNextNodePos();
        double d0 = Math.abs(this.f_26494_.m_20185_() - ((double) vector3i.getX() + 0.5));
        double d1 = Math.abs(this.f_26494_.m_20186_() - (double) vector3i.getY());
        double d2 = Math.abs(this.f_26494_.m_20189_() - ((double) vector3i.getZ() + 0.5));
        boolean flag = d0 < (double) this.f_26505_ && d2 < (double) this.f_26505_ && d1 < 1.0;
        if (flag || this.m_264193_(this.f_26496_.getNextNode().type) && this.shouldTargetNextNodeInDirection(vector3d)) {
            this.f_26496_.advance();
        }
        this.m_6481_(vector3d);
    }

    private boolean shouldTargetNextNodeInDirection(Vec3 currentPosition) {
        if (this.f_26496_.getNextNodeIndex() + 1 >= this.f_26496_.getNodeCount()) {
            return false;
        } else {
            Vec3 vector3d = Vec3.atBottomCenterOf(this.f_26496_.getNextNodePos());
            if (!currentPosition.closerThan(vector3d, 2.0)) {
                return false;
            } else {
                Vec3 vector3d1 = Vec3.atBottomCenterOf(this.f_26496_.getNodePos(this.f_26496_.getNextNodeIndex() + 1));
                Vec3 vector3d2 = vector3d1.subtract(vector3d);
                Vec3 vector3d3 = currentPosition.subtract(vector3d);
                return vector3d2.dot(vector3d3) > 0.0;
            }
        }
    }
}