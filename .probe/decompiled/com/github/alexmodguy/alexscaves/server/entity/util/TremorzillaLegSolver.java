package com.github.alexmodguy.alexscaves.server.entity.util;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TremorzillaLegSolver {

    public final TremorzillaLegSolver.Leg backLeft;

    public final TremorzillaLegSolver.Leg backRight;

    public final TremorzillaLegSolver.Leg[] legs;

    public TremorzillaLegSolver(float forward, float side, float range) {
        this.legs = new TremorzillaLegSolver.Leg[] { new TremorzillaLegSolver.Leg(forward, side, range, false), new TremorzillaLegSolver.Leg(forward, -side, range, false) };
        this.backLeft = this.legs[0];
        this.backRight = this.legs[1];
    }

    public final void update(LivingEntity entity, float scale) {
        this.update(entity, entity.yBodyRot, scale);
    }

    public final void update(LivingEntity entity, float yaw, float scale) {
        double sideTheta = (double) yaw / (180.0 / Math.PI);
        double sideX = Math.cos(sideTheta) * (double) scale;
        double sideZ = Math.sin(sideTheta) * (double) scale;
        double forwardTheta = sideTheta + (Math.PI / 2);
        double forwardX = Math.cos(forwardTheta) * (double) scale;
        double forwardZ = Math.sin(forwardTheta) * (double) scale;
        for (TremorzillaLegSolver.Leg leg : this.legs) {
            leg.update(entity, sideX, sideZ, forwardX, forwardZ, scale);
        }
    }

    public static final class Leg {

        public final float forward;

        public final float side;

        public Vec3 samplePos = Vec3.ZERO;

        private final float range;

        private float height;

        private float prevHeight;

        private boolean isWing;

        public Leg(float forward, float side, float range, boolean isWing) {
            this.forward = forward;
            this.side = side;
            this.range = range;
            this.isWing = isWing;
        }

        public float getHeight(float delta) {
            return this.prevHeight + (this.height - this.prevHeight) * delta;
        }

        public void update(LivingEntity entity, double sideX, double sideZ, double forwardX, double forwardZ, float scale) {
            this.prevHeight = this.height;
            double posY = entity.m_20186_();
            this.samplePos = new Vec3(entity.m_20185_() + sideX * (double) this.side + forwardX * (double) this.forward, posY, entity.m_20189_() + sideZ * (double) this.side + forwardZ * (double) this.forward);
            float settledHeight = this.settle(entity, entity.m_20185_() + sideX * (double) this.side + forwardX * (double) this.forward, posY - 1.0, entity.m_20189_() + sideZ * (double) this.side + forwardZ * (double) this.forward, this.height);
            this.height = Mth.clamp(settledHeight, -this.range * scale, this.range * scale);
        }

        protected float settle(LivingEntity entity, double x, double y, double z, float height) {
            BlockPos pos = new BlockPos((int) Math.floor(x), (int) Math.floor(y + 0.001), (int) Math.floor(z));
            Vec3 vec3 = new Vec3(x, y, z);
            float dist = this.getDistance(entity.m_9236_(), pos, vec3);
            for (float lastDistance = dist; lastDistance == 1.0F; dist += lastDistance) {
                pos = pos.below();
                lastDistance = this.getDistance(entity.m_9236_(), pos, vec3);
            }
            if (entity.m_20096_() && height <= dist) {
                return height == dist ? height : Math.min(height + this.getFallSpeed(), dist);
            } else if (height > 0.0F) {
                return height == dist ? height : Math.max(height - this.getRiseSpeed(), dist);
            } else {
                return height;
            }
        }

        protected float getDistance(Level world, BlockPos pos, Vec3 position) {
            BlockState state = world.getBlockState(pos);
            VoxelShape shape = state.m_60812_(world, pos);
            if (pos.m_123342_() < world.m_141937_()) {
                return 0.0F;
            } else if (shape.isEmpty()) {
                return 1.0F;
            } else {
                Vec3 modIn = new Vec3(position.x % 1.0, position.y, position.z % 1.0);
                Optional<Vec3> closest = shape.closestPointTo(modIn);
                if (closest.isEmpty()) {
                    return 1.0F;
                } else {
                    float closestY = Math.min((float) ((Vec3) closest.get()).y, 1.0F);
                    return position.y < 0.0 ? closestY : 1.0F - closestY;
                }
            }
        }

        protected float getFallSpeed() {
            return 0.15F;
        }

        protected float getRiseSpeed() {
            return 0.15F;
        }
    }
}