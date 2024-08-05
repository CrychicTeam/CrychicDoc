package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityKangaroo;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class KangarooAIMelee extends MeleeAttackGoal {

    private final EntityKangaroo kangaroo;

    private BlockPos waterPos;

    private int waterCheckTick = 0;

    private int waterTimeout = 0;

    public KangarooAIMelee(EntityKangaroo kangaroo, double speedIn, boolean useLongMemory) {
        super(kangaroo, speedIn, useLongMemory);
        this.kangaroo = kangaroo;
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void tick() {
        boolean dontSuper = false;
        LivingEntity target = this.kangaroo.m_5448_();
        if (target != null) {
            if (target == this.kangaroo.m_21188_()) {
                if (target.m_20270_(this.kangaroo) < this.kangaroo.m_20205_() + 1.0F && target.m_20069_()) {
                    target.m_20256_(target.m_20184_().add(0.0, -0.09, 0.0));
                    target.m_20301_(target.m_20146_() - 30);
                }
                if (this.waterPos != null && this.kangaroo.m_9236_().getFluidState(this.waterPos).is(FluidTags.WATER)) {
                    this.kangaroo.m_21441_(BlockPathTypes.WATER, 0.0F);
                    this.kangaroo.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
                    double localSpeed = Mth.clamp(this.kangaroo.m_20275_((double) this.waterPos.m_123341_(), (double) this.waterPos.m_123342_(), (double) this.waterPos.m_123343_()) * 0.5, 1.0, 2.3);
                    this.kangaroo.getMoveControl().setWantedPosition((double) this.waterPos.m_123341_(), (double) this.waterPos.m_123342_(), (double) this.waterPos.m_123343_(), localSpeed);
                    if (this.kangaroo.m_20069_()) {
                        this.waterTimeout++;
                    }
                    if (this.waterTimeout < 1400) {
                        dontSuper = true;
                        this.checkAndPerformAttack(target, this.kangaroo.m_20280_(target));
                    }
                    if (this.kangaroo.m_20069_() || this.kangaroo.m_20238_(Vec3.atCenterOf(this.waterPos)) < 10.0) {
                        this.kangaroo.totalMovingProgress = 0.0F;
                    }
                    if (this.kangaroo.m_20238_(Vec3.atCenterOf(this.waterPos)) > 10.0) {
                        this.kangaroo.setVisualFlag(0);
                    }
                    if (this.kangaroo.m_20238_(Vec3.atCenterOf(this.waterPos)) < 3.0 && this.kangaroo.m_20069_()) {
                        this.kangaroo.setStanding(true);
                        this.kangaroo.maxStandTime = 100;
                        this.kangaroo.m_21563_().setLookAt(target, 360.0F, 180.0F);
                        this.kangaroo.setVisualFlag(1);
                    }
                } else {
                    this.kangaroo.setVisualFlag(0);
                    this.waterCheckTick++;
                    this.waterPos = this.generateWaterPos();
                }
            }
            if (!dontSuper) {
                super.tick();
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.waterPos != null && this.kangaroo.m_5448_() != null || super.canContinueToUse();
    }

    @Override
    public void stop() {
        super.stop();
        this.waterCheckTick = 0;
        this.waterTimeout = 0;
        this.waterPos = null;
        this.kangaroo.setVisualFlag(0);
        this.kangaroo.m_21441_(BlockPathTypes.WATER, 8.0F);
        this.kangaroo.m_21441_(BlockPathTypes.WATER_BORDER, 8.0F);
    }

    public BlockPos generateWaterPos() {
        BlockPos blockpos = null;
        RandomSource random = this.kangaroo.m_217043_();
        int range = 15;
        for (int i = 0; i < 15; i++) {
            BlockPos blockpos1 = this.kangaroo.m_20183_().offset(random.nextInt(range) - range / 2, 3, random.nextInt(range) - range / 2);
            while (this.kangaroo.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() > 1) {
                blockpos1 = blockpos1.below();
            }
            if (this.kangaroo.m_9236_().getFluidState(blockpos1).is(FluidTags.WATER)) {
                blockpos = blockpos1;
            }
        }
        return blockpos;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.m_6639_(enemy) + 5.0;
        if (distToEnemySqr <= d0) {
            if (this.kangaroo.m_20069_()) {
                float f1 = this.kangaroo.m_146908_() * (float) (Math.PI / 180.0);
                this.kangaroo.m_20256_(this.kangaroo.m_20184_().add((double) (-Mth.sin(f1) * 0.3F), 0.0, (double) (Mth.cos(f1) * 0.3F)));
                enemy.knockback(1.0, enemy.m_20185_() - this.kangaroo.m_20185_(), enemy.m_20189_() - this.kangaroo.m_20189_());
            }
            this.m_25563_();
            if (this.kangaroo.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                if (this.kangaroo.m_217043_().nextBoolean()) {
                    this.kangaroo.setAnimation(EntityKangaroo.ANIMATION_KICK);
                } else if (!this.kangaroo.m_21205_().isEmpty()) {
                    this.kangaroo.setAnimation(this.kangaroo.m_21526_() ? EntityKangaroo.ANIMATION_PUNCH_L : EntityKangaroo.ANIMATION_PUNCH_R);
                } else {
                    this.kangaroo.setAnimation(this.kangaroo.m_217043_().nextBoolean() ? EntityKangaroo.ANIMATION_PUNCH_R : EntityKangaroo.ANIMATION_PUNCH_L);
                }
            }
        }
    }
}