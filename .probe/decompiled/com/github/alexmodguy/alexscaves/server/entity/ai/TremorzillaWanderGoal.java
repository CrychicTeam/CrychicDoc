package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class TremorzillaWanderGoal extends Goal {

    private TremorzillaEntity tremorzilla;

    private double x;

    private double y;

    private double z;

    private boolean tryLandTarget;

    public TremorzillaWanderGoal(TremorzillaEntity tremorzilla) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.tremorzilla = tremorzilla;
    }

    @Override
    public boolean canUse() {
        if (this.tremorzilla.m_217043_().nextInt(40) != 0 && !this.tremorzilla.isTremorzillaSwimming()) {
            return false;
        } else {
            if (this.tremorzilla.isTremorzillaSwimming()) {
                this.tryLandTarget = this.tremorzilla.timeSwimming > 300 || this.tremorzilla.m_217043_().nextFloat() < 0.1F;
            } else {
                this.tryLandTarget = this.tremorzilla.m_217043_().nextFloat() > 0.1F;
            }
            Vec3 target = this.getPosition();
            if (target == null) {
                return false;
            } else {
                this.x = target.x;
                this.y = target.y;
                this.z = target.z;
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.tremorzilla.m_21573_().isDone() && this.tremorzilla.m_20275_(this.x, this.y, this.z) > 8.0;
    }

    @Override
    public void start() {
        this.tremorzilla.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
    }

    public BlockPos findWaterBlock(int range) {
        BlockPos around = this.tremorzilla.m_20183_();
        BlockPos.MutableBlockPos move = new BlockPos.MutableBlockPos();
        move.set(this.tremorzilla.m_20185_(), this.tremorzilla.m_20186_(), this.tremorzilla.m_20189_());
        while (move.m_123342_() < this.tremorzilla.m_9236_().m_151558_() && !this.tremorzilla.m_9236_().getFluidState(move).isEmpty()) {
            move.move(0, 1, 0);
        }
        int surfaceY = move.m_123342_();
        around = around.atY(Math.min(surfaceY - 1, around.m_123342_()));
        for (int i = 0; i < 15; i++) {
            BlockPos blockPos = around.offset(this.tremorzilla.m_217043_().nextInt(range) - range / 2, this.tremorzilla.m_217043_().nextInt(range) - range / 2, this.tremorzilla.m_217043_().nextInt(range) - range / 2);
            if (!this.tremorzilla.m_9236_().getFluidState(blockPos).isEmpty() && !this.isTargetBlocked(Vec3.atCenterOf(blockPos)) && blockPos.m_123342_() > this.tremorzilla.m_9236_().m_141937_() + 1) {
                return blockPos;
            }
        }
        return around;
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.tremorzilla.m_20185_(), this.tremorzilla.m_20188_(), this.tremorzilla.m_20189_());
        return this.tremorzilla.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.tremorzilla)).getType() != HitResult.Type.MISS;
    }

    @Nullable
    protected Vec3 getPosition() {
        if (this.tryLandTarget) {
            Vec3 landTarget = LandRandomPos.getPos(this.tremorzilla, 30, 8);
            if (landTarget != null) {
                return landTarget;
            }
        }
        BlockPos water = this.findWaterBlock(20);
        return water != null ? Vec3.atCenterOf(water) : null;
    }
}