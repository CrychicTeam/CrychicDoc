package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityHummingbird;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class HummingbirdAIWander extends Goal {

    private final EntityHummingbird fly;

    private final int rangeXZ;

    private final int rangeY;

    private final int chance;

    private final float speed;

    private Vec3 moveToPoint = null;

    public HummingbirdAIWander(EntityHummingbird fly, int rangeXZ, int rangeY, int chance, float speed) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.fly = fly;
        this.rangeXZ = rangeXZ;
        this.rangeY = rangeY;
        this.chance = chance;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        return this.fly.hummingStill > 10 && this.fly.m_217043_().nextInt(this.chance) == 0 && !this.fly.m_21566_().hasWanted();
    }

    @Override
    public void stop() {
        this.moveToPoint = null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.moveToPoint != null && this.fly.m_20238_(this.moveToPoint) > 0.85;
    }

    @Override
    public void start() {
        this.moveToPoint = this.getRandomLocation();
        if (this.moveToPoint != null) {
            this.fly.m_21566_().setWantedPosition(this.moveToPoint.x, this.moveToPoint.y, this.moveToPoint.z, (double) this.speed);
        }
    }

    @Override
    public void tick() {
        if (this.moveToPoint != null) {
            this.fly.m_21566_().setWantedPosition(this.moveToPoint.x, this.moveToPoint.y, this.moveToPoint.z, (double) this.speed);
        }
    }

    @Nullable
    private Vec3 getRandomLocation() {
        RandomSource random = this.fly.m_217043_();
        BlockPos blockpos = null;
        BlockPos origin = this.fly.getFeederPos() == null ? this.fly.m_20183_() : this.fly.getFeederPos();
        for (int i = 0; i < 15; i++) {
            BlockPos blockpos1 = origin.offset(random.nextInt(this.rangeXZ) - this.rangeXZ / 2, 1, random.nextInt(this.rangeXZ) - this.rangeXZ / 2);
            while (this.fly.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() > 0) {
                blockpos1 = blockpos1.below();
            }
            blockpos1 = blockpos1.above(1 + random.nextInt(3));
            if (this.fly.m_9236_().m_46859_(blockpos1.below()) && this.fly.canBlockBeSeen(blockpos1) && this.fly.m_9236_().m_46859_(blockpos1) && !this.fly.m_9236_().m_46859_(blockpos1.below(2))) {
                blockpos = blockpos1;
            }
        }
        return blockpos == null ? null : new Vec3((double) blockpos.m_123341_() + 0.5, (double) blockpos.m_123342_() + 0.5, (double) blockpos.m_123343_() + 0.5);
    }

    public boolean canBlockPosBeSeen(BlockPos pos) {
        double x = (double) ((float) pos.m_123341_() + 0.5F);
        double y = (double) ((float) pos.m_123342_() + 0.5F);
        double z = (double) ((float) pos.m_123343_() + 0.5F);
        HitResult result = this.fly.m_9236_().m_45547_(new ClipContext(new Vec3(this.fly.m_20185_(), this.fly.m_20186_() + (double) this.fly.m_20192_(), this.fly.m_20189_()), new Vec3(x, y, z), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.fly));
        double dist = result.getLocation().distanceToSqr(x, y, z);
        return dist <= 1.0 || result.getType() == HitResult.Type.MISS;
    }
}