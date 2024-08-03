package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class UnderzealotBreakLightGoal extends MoveToBlockGoal {

    private final UnderzealotEntity underzealot;

    public UnderzealotBreakLightGoal(UnderzealotEntity entity, int range) {
        super(entity, 1.0, range, range);
        this.underzealot = entity;
    }

    @Override
    protected int nextStartTick(PathfinderMob mob) {
        return m_186073_(500 + this.underzealot.m_217043_().nextInt(1000));
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.isTargetBlocked(this.f_25602_.getCenter());
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.f_25598_.m_20185_(), this.f_25598_.m_20188_(), this.f_25598_.m_20189_());
        return this.f_25598_.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.f_25598_)).getType() != HitResult.Type.MISS;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.underzealot.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.underzealot.isPackFollower() && !this.underzealot.isCarrying();
    }

    @Override
    public double acceptedDistance() {
        return 2.0;
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos target = this.getMoveToTarget();
        if (target != null) {
            this.underzealot.m_7618_(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(target));
            if (this.m_25625_() && this.underzealot.m_9236_().getBlockState(target).m_204336_(ACTagRegistry.UNDERZEALOT_LIGHT_SOURCES)) {
                if (this.underzealot.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    this.underzealot.setAnimation(UnderzealotEntity.ANIMATION_BREAKTORCH);
                } else if (this.underzealot.getAnimation() == UnderzealotEntity.ANIMATION_BREAKTORCH && this.underzealot.getAnimationTick() == 10) {
                    this.underzealot.m_9236_().m_46961_(target, true);
                }
            }
        }
    }

    @Override
    protected BlockPos getMoveToTarget() {
        return this.f_25602_;
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        return pos != null && worldIn.m_8055_(pos).m_204336_(ACTagRegistry.UNDERZEALOT_LIGHT_SOURCES) && worldIn.m_7146_(pos) > 0;
    }
}