package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityShoebill;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class ShoebillAIFlightFlee extends Goal {

    private final EntityShoebill bird;

    private BlockPos currentTarget = null;

    private int executionTime = 0;

    public ShoebillAIFlightFlee(EntityShoebill bird) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.bird = bird;
    }

    @Override
    public void stop() {
        this.currentTarget = null;
        this.executionTime = 0;
        this.bird.setFlying(false);
    }

    @Override
    public boolean canContinueToUse() {
        return this.bird.isFlying() && (this.executionTime < 15 || !this.bird.m_20096_());
    }

    @Override
    public boolean canUse() {
        return this.bird.revengeCooldown > 0 && this.bird.m_20096_();
    }

    @Override
    public void start() {
        if (this.bird.m_20096_()) {
            this.bird.setFlying(true);
        }
    }

    @Override
    public void tick() {
        this.executionTime++;
        if (this.currentTarget == null) {
            if (this.bird.revengeCooldown == 0) {
                this.currentTarget = this.getBlockGrounding(this.bird.m_20182_());
            } else {
                this.currentTarget = this.getBlockInViewAway(this.bird.m_20182_());
            }
        }
        if (this.currentTarget != null) {
            this.bird.m_21573_().moveTo((double) ((float) this.currentTarget.m_123341_() + 0.5F), (double) ((float) this.currentTarget.m_123342_() + 0.5F), (double) ((float) this.currentTarget.m_123343_() + 0.5F), 1.0);
            if (this.bird.m_20238_(Vec3.atCenterOf(this.currentTarget)) < 4.0) {
                this.currentTarget = null;
            }
        }
        if (this.bird.revengeCooldown == 0 && (this.bird.m_20069_() || !this.bird.m_9236_().m_46859_(this.bird.m_20183_().below()))) {
            this.stop();
            this.bird.setFlying(false);
        }
    }

    public BlockPos getBlockInViewAway(Vec3 fleePos) {
        float radius = -9.45F - (float) this.bird.m_217043_().nextInt(24);
        float neg = this.bird.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.bird.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.bird.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = AMBlockPos.fromCoords(fleePos.x() + extraX, 0.0, fleePos.z() + extraZ);
        BlockPos ground = this.bird.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, radialPos);
        int distFromGround = (int) this.bird.m_20186_() - ground.m_123342_();
        int flightHeight = 4 + this.bird.m_217043_().nextInt(10);
        BlockPos newPos = radialPos.above(distFromGround > 8 ? flightHeight : (int) this.bird.m_20186_() + this.bird.m_217043_().nextInt(6) + 1);
        return !this.bird.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.bird.m_20238_(Vec3.atCenterOf(newPos)) > 6.0 ? newPos : null;
    }

    public BlockPos getBlockGrounding(Vec3 fleePos) {
        float radius = -9.45F - (float) this.bird.m_217043_().nextInt(24);
        float neg = this.bird.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.bird.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.bird.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = AMBlockPos.fromCoords(fleePos.x() + extraX, 0.0, fleePos.z() + extraZ);
        BlockPos ground = this.bird.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, radialPos);
        return !this.bird.isTargetBlocked(Vec3.atCenterOf(ground.above())) ? ground : null;
    }
}