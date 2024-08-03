package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;

public class PanicGoal extends Goal {

    public static final int WATER_CHECK_DISTANCE_VERTICAL = 1;

    protected final PathfinderMob mob;

    protected final double speedModifier;

    protected double posX;

    protected double posY;

    protected double posZ;

    protected boolean isRunning;

    public PanicGoal(PathfinderMob pathfinderMob0, double double1) {
        this.mob = pathfinderMob0;
        this.speedModifier = double1;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.shouldPanic()) {
            return false;
        } else {
            if (this.mob.m_6060_()) {
                BlockPos $$0 = this.lookForWater(this.mob.m_9236_(), this.mob, 5);
                if ($$0 != null) {
                    this.posX = (double) $$0.m_123341_();
                    this.posY = (double) $$0.m_123342_();
                    this.posZ = (double) $$0.m_123343_();
                    return true;
                }
            }
            return this.findRandomPosition();
        }
    }

    protected boolean shouldPanic() {
        return this.mob.m_21188_() != null || this.mob.m_203117_() || this.mob.m_6060_();
    }

    protected boolean findRandomPosition() {
        Vec3 $$0 = DefaultRandomPos.getPos(this.mob, 5, 4);
        if ($$0 == null) {
            return false;
        } else {
            this.posX = $$0.x;
            this.posY = $$0.y;
            this.posZ = $$0.z;
            return true;
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public void start() {
        this.mob.m_21573_().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.m_21573_().isDone();
    }

    @Nullable
    protected BlockPos lookForWater(BlockGetter blockGetter0, Entity entity1, int int2) {
        BlockPos $$3 = entity1.blockPosition();
        return !blockGetter0.getBlockState($$3).m_60812_(blockGetter0, $$3).isEmpty() ? null : (BlockPos) BlockPos.findClosestMatch(entity1.blockPosition(), int2, 1, p_196649_ -> blockGetter0.getFluidState(p_196649_).is(FluidTags.WATER)).orElse(null);
    }
}