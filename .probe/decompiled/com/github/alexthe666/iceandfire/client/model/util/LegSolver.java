package com.github.alexthe666.iceandfire.client.model.util;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LegSolver {

    public final LegSolver.Leg[] legs;

    public LegSolver(LegSolver.Leg... legs) {
        this.legs = legs;
    }

    public final void update(EntityDragonBase entity, float scale) {
        this.update(entity, entity.f_20883_, scale);
    }

    public final void update(EntityDragonBase entity, float yaw, float scale) {
        double sideTheta = (double) yaw / (180.0 / Math.PI);
        double sideX = (double) (Mth.cos((float) sideTheta) * scale);
        double sideZ = (double) (Mth.sin((float) sideTheta) * scale);
        double forwardTheta = sideTheta + (Math.PI / 2);
        double forwardX = (double) (Mth.cos((float) forwardTheta) * scale);
        double forwardZ = (double) (Mth.sin((float) forwardTheta) * scale);
        for (LegSolver.Leg leg : this.legs) {
            leg.update(entity, sideX, sideZ, forwardX, forwardZ, scale);
        }
    }

    public static final class Leg {

        public final float forward;

        public final float side;

        private final float range;

        private float height;

        private float prevHeight;

        private final boolean isWing;

        public Leg(float forward, float side, float range, boolean isWing) {
            this.forward = forward;
            this.side = side;
            this.range = range;
            this.isWing = isWing;
        }

        public final float getHeight(float delta) {
            return this.prevHeight + (this.height - this.prevHeight) * delta;
        }

        public void update(EntityDragonBase entity, double sideX, double sideZ, double forwardX, double forwardZ, float scale) {
            this.prevHeight = this.height;
            double posY = entity.m_20186_();
            float settledHeight = this.settle(entity, entity.m_20185_() + sideX * (double) this.side + forwardX * (double) this.forward, posY, entity.m_20189_() + sideZ * (double) this.side + forwardZ * (double) this.forward, this.height);
            this.height = Mth.clamp(settledHeight, -this.range * scale, this.range * scale);
        }

        private float settle(EntityDragonBase entity, double x, double y, double z, float height) {
            BlockPos pos = BlockPos.containing(x, y + 0.001, z);
            float dist = this.getDistance(entity.m_9236_(), pos);
            if ((double) (1.0F - dist) < 0.001) {
                dist = this.getDistance(entity.m_9236_(), pos.below()) + (float) y % 1.0F;
            } else {
                dist = (float) ((double) dist - (1.0 - y % 1.0));
            }
            if (entity.m_20096_() && height <= dist) {
                return height == dist ? height : Math.min(height + this.getFallSpeed(), dist);
            } else {
                return height > 0.0F ? Math.max(height - this.getRiseSpeed(), dist) : height;
            }
        }

        private float getDistance(Level world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            VoxelShape aabb = state.m_60812_(world, pos);
            return aabb.isEmpty() ? 1.0F : 1.0F - Math.min((float) aabb.max(Direction.Axis.Y, 0.5, 0.5), 1.0F);
        }

        protected float getFallSpeed() {
            return 0.25F;
        }

        protected float getRiseSpeed() {
            return 0.25F;
        }
    }
}