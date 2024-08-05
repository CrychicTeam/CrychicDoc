package com.simibubi.create.content.logistics.depot;

import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityLauncher {

    private int horizontalDistance;

    private int verticalDistance;

    private double yMotion;

    private double xMotion;

    private double totalFlyingTicks;

    public EntityLauncher(int horizontalDistance, int verticalDistance) {
        this.set(horizontalDistance, verticalDistance);
    }

    public void clamp(int max) {
        this.set(Math.min(this.horizontalDistance, max), Mth.sign((double) this.verticalDistance) * Math.min(Math.abs(this.verticalDistance), max));
    }

    public void set(int horizontalDistance, int verticalDistance) {
        this.horizontalDistance = horizontalDistance;
        this.verticalDistance = verticalDistance;
        this.recalculateTrajectory();
    }

    public void applyMotion(Entity entity, Direction facing) {
        Vec3 motionVec = new Vec3(0.0, this.yMotion, this.xMotion);
        motionVec = VecHelper.rotate(motionVec, (double) AngleHelper.horizontalAngle(facing), Direction.Axis.Y);
        entity.setDeltaMovement(motionVec.x * 0.91, motionVec.y * 0.98, motionVec.z * 0.91);
    }

    public int getHorizontalDistance() {
        return this.horizontalDistance;
    }

    public int getVerticalDistance() {
        return this.verticalDistance;
    }

    public double getTotalFlyingTicks() {
        return this.totalFlyingTicks;
    }

    public Vec3 getGlobalPos(double t, Direction d, BlockPos launcher) {
        Vec3 start = new Vec3((double) ((float) launcher.m_123341_() + 0.5F), (double) ((float) launcher.m_123342_() + 0.5F), (double) ((float) launcher.m_123343_() + 0.5F));
        float xt = this.x(t);
        float yt = this.y(t);
        double progress = Mth.clamp(t / this.getTotalFlyingTicks(), 0.0, 1.0);
        double correctionStrength = Math.pow(progress, 3.0);
        Vec3 vec = new Vec3(0.0, (double) yt + (double) ((float) this.verticalDistance - yt) * correctionStrength * 0.5, (double) xt + (double) ((float) this.horizontalDistance - xt) * correctionStrength);
        return VecHelper.rotate(vec, (double) (180.0F + AngleHelper.horizontalAngle(d)), Direction.Axis.Y).add(start);
    }

    public Vec3 getGlobalVelocity(double t, Direction d, BlockPos launcher) {
        return VecHelper.rotate(new Vec3(0.0, (double) this.dy(t), (double) this.dx(t)), (double) (180.0F + AngleHelper.horizontalAngle(d)), Direction.Axis.Y);
    }

    public float x(double t) {
        return (float) (this.xMotion * -10.6033 * (-1.0 + Math.pow(0.91, t)));
    }

    public float y(double t) {
        double f = Math.pow(0.98, t);
        return (float) (this.yMotion * -49.4983 * f + 49.4983 * this.yMotion - 194.033 * f - 3.92 * t + 194.033);
    }

    public float dx(double t) {
        return (float) (this.xMotion * Math.pow(0.91, t));
    }

    public float dy(double t) {
        double f = Math.pow(0.98, t);
        return (float) (this.yMotion * f + (f - 1.0) / -0.020000000000000018 * -0.0784);
    }

    protected void recalculateTrajectory() {
        double xTarget = (double) this.horizontalDistance;
        double yTarget = (double) this.verticalDistance;
        double xError = -7.0E-4 * Math.pow(xTarget + 0.5, 2.0) + 0.484 - Math.min(5.0, yTarget) / 5.0 * Math.min(1.0, 0.076 * xTarget - 0.0014 * xTarget * xTarget);
        double yPeak = Math.max(0.0, yTarget + (xTarget + 0.5) / 8.0) + (double) (xTarget <= 1.0 ? 1 : 4);
        this.yMotion = Math.sqrt(2.0 * yPeak / 13.0) + 0.015 * yPeak;
        double tPeak = Math.log(98.0 / (25.0 * this.yMotion + 98.0)) / (2.0 * Math.log(7.0) - 2.0 * Math.log(5.0) - Math.log(2.0));
        this.totalFlyingTicks = Math.sqrt(yPeak - this.yMotion) * 4.87 + 0.115 * (yPeak - this.yMotion) + tPeak;
        this.xMotion = (xTarget - xError + 0.5) / (-10.6033 * (-1.0 + Math.pow(0.91, this.totalFlyingTicks)));
    }
}