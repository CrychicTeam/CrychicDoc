package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.GloomothEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GloomothFindLightGoal extends MoveToBlockGoal {

    private final GloomothEntity gloomoth;

    public GloomothFindLightGoal(GloomothEntity entity, int range) {
        super(entity, 1.0, range, range);
        this.gloomoth = entity;
    }

    @Override
    protected int nextStartTick(PathfinderMob mob) {
        return m_186073_(50 + this.gloomoth.m_217043_().nextInt(50));
    }

    @Override
    public boolean canUse() {
        return this.gloomoth.lightPos == null && super.canUse() && !this.isTargetBlocked(this.f_25602_.getCenter());
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.f_25598_.m_20185_(), this.f_25598_.m_20188_(), this.f_25598_.m_20189_());
        return this.f_25598_.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.f_25598_)).getType() != HitResult.Type.MISS;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.gloomoth.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && this.gloomoth.lightPos == null;
    }

    @Override
    public double acceptedDistance() {
        return (double) (this.gloomoth.m_20205_() + 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos target = this.getMoveToTarget();
        if (target != null) {
            this.gloomoth.m_7618_(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(target));
            if (this.m_25625_()) {
                this.gloomoth.lightPos = this.f_25602_;
            }
        }
    }

    @Override
    public void start() {
        this.gloomoth.setFlying(true);
        super.start();
    }

    @Override
    public void stop() {
        super.m_8041_();
    }

    @Override
    protected BlockPos getMoveToTarget() {
        return this.f_25602_;
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        return pos != null && worldIn.m_8055_(pos).m_204336_(ACTagRegistry.GLOOMOTH_LIGHT_SOURCES) && worldIn.m_7146_(pos) > 0 && worldIn instanceof ServerLevel serverLevel ? this.gloomoth.getNearestMothBall(serverLevel, this.f_25602_, 10) == null : false;
    }
}