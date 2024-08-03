package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityFroststalker;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class FroststalkerAIMelee extends Goal {

    private final EntityFroststalker froststalker;

    private boolean willJump = false;

    private boolean hasJumped = false;

    private boolean clockwise = false;

    private int pursuitTime = 0;

    private int maxPursuitTime = 0;

    private BlockPos pursuitPos = null;

    private int startingOrbit = 0;

    public FroststalkerAIMelee(EntityFroststalker froststalker) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.froststalker = froststalker;
    }

    @Override
    public boolean canUse() {
        if (this.froststalker.m_5448_() != null && this.froststalker.m_5448_().isAlive()) {
            return !this.froststalker.isValidLeader(this.froststalker.m_5448_()) ? !this.froststalker.isFleeingFire() : this.froststalker.m_21188_() != null && this.froststalker.m_21188_().equals(this.froststalker.m_5448_());
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.froststalker.m_5448_();
        return target != null && !this.froststalker.isValidLeader(target);
    }

    @Override
    public void start() {
        this.willJump = this.froststalker.m_217043_().nextInt(2) == 0;
        this.hasJumped = false;
        this.clockwise = this.froststalker.m_217043_().nextBoolean();
        this.pursuitPos = null;
        this.pursuitTime = 0;
        this.maxPursuitTime = 40 + this.froststalker.m_217043_().nextInt(40);
        this.startingOrbit = this.froststalker.m_217043_().nextInt(360);
        this.froststalker.frostJump();
    }

    @Override
    public void tick() {
        this.froststalker.setBipedal(true);
        this.froststalker.standFor(20);
        LivingEntity target = this.froststalker.m_5448_();
        boolean flag = false;
        if ((this.hasJumped || this.froststalker.isTackling()) && this.froststalker.m_20096_()) {
            this.hasJumped = false;
            this.willJump = false;
            this.froststalker.setTackling(false);
        }
        if (target != null && target.isAlive()) {
            if (this.pursuitTime < this.maxPursuitTime) {
                this.pursuitTime++;
                this.pursuitPos = this.getBlockNearTarget(target);
                float extraSpeed = 0.2F * Math.max(5.0F - this.froststalker.m_20270_(target), 0.0F);
                if (this.pursuitPos != null) {
                    this.froststalker.m_21573_().moveTo((double) this.pursuitPos.m_123341_(), (double) this.pursuitPos.m_123342_(), (double) this.pursuitPos.m_123343_(), (double) (1.0F + extraSpeed));
                } else {
                    this.froststalker.m_21573_().moveTo(target, 1.0);
                }
            } else if (this.willJump && this.pursuitTime == this.maxPursuitTime) {
                this.froststalker.m_21391_(target, 180.0F, 10.0F);
                if (this.froststalker.m_20270_(target) > 10.0F) {
                    this.froststalker.m_21573_().moveTo(target, 1.0);
                } else if (this.froststalker.m_20096_() && this.froststalker.m_142582_(target)) {
                    this.froststalker.setTackling(true);
                    this.hasJumped = true;
                    Vec3 vector3d = this.froststalker.m_20184_();
                    Vec3 vector3d1 = new Vec3(target.m_20185_() - this.froststalker.m_20185_(), 0.0, target.m_20189_() - this.froststalker.m_20189_());
                    if (vector3d1.lengthSqr() > 1.0E-7) {
                        vector3d1 = vector3d1.normalize().scale(0.9).add(vector3d.scale(0.8));
                    }
                    this.froststalker.m_20334_(vector3d1.x, 0.6F, vector3d1.z);
                } else {
                    flag = true;
                }
            } else if (!this.froststalker.isTackling()) {
                this.froststalker.m_21573_().moveTo(target, 1.0);
            }
            if (this.froststalker.isTackling() && this.froststalker.m_20270_(target) <= this.froststalker.m_20205_() + target.m_20205_() + 1.1F && this.froststalker.m_142582_(target)) {
                target.hurt(this.froststalker.m_269291_().mobAttack(this.froststalker), (float) this.froststalker.m_21133_(Attributes.ATTACK_DAMAGE));
                this.start();
            }
            if (!flag && this.froststalker.m_20270_(target) <= this.froststalker.m_20205_() + target.m_20205_() + 1.1F && this.froststalker.m_142582_(target) && this.pursuitTime == this.maxPursuitTime) {
                if (!this.froststalker.isTackling()) {
                    this.froststalker.doHurtTarget(target);
                }
                this.start();
            }
        }
        if (target != null && !this.froststalker.m_20096_()) {
            this.froststalker.m_21391_(target, 180.0F, 10.0F);
            this.froststalker.f_20883_ = this.froststalker.m_146908_();
        }
    }

    public BlockPos getBlockNearTarget(LivingEntity target) {
        float radius = (float) (this.froststalker.m_217043_().nextInt(5) + 3) + target.m_20205_();
        int orbit = (int) ((float) this.startingOrbit + (float) this.pursuitTime / (float) this.maxPursuitTime * 360.0F);
        float angle = (float) (Math.PI / 180.0) * (float) (this.clockwise ? -orbit : orbit);
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos circlePos = AMBlockPos.fromCoords(target.m_20185_() + extraX, target.m_20188_(), target.m_20189_() + extraZ);
        while (!this.froststalker.m_9236_().getBlockState(circlePos).m_60795_() && circlePos.m_123342_() < this.froststalker.m_9236_().m_151558_()) {
            circlePos = circlePos.above();
        }
        while (!this.froststalker.m_9236_().getBlockState(circlePos.below()).m_60634_(this.froststalker.m_9236_(), circlePos.below(), this.froststalker) && circlePos.m_123342_() > 1) {
            circlePos = circlePos.below();
        }
        return this.froststalker.m_21692_(circlePos) > -1.0F ? circlePos : null;
    }

    @Override
    public void stop() {
        this.froststalker.setTackling(false);
    }
}