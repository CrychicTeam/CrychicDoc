package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class RangedAttackGoal extends Goal {

    private final Mob mob;

    private final RangedAttackMob rangedAttackMob;

    @Nullable
    private LivingEntity target;

    private int attackTime = -1;

    private final double speedModifier;

    private int seeTime;

    private final int attackIntervalMin;

    private final int attackIntervalMax;

    private final float attackRadius;

    private final float attackRadiusSqr;

    public RangedAttackGoal(RangedAttackMob rangedAttackMob0, double double1, int int2, float float3) {
        this(rangedAttackMob0, double1, int2, int2, float3);
    }

    public RangedAttackGoal(RangedAttackMob rangedAttackMob0, double double1, int int2, int int3, float float4) {
        if (!(rangedAttackMob0 instanceof LivingEntity)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        } else {
            this.rangedAttackMob = rangedAttackMob0;
            this.mob = (Mob) rangedAttackMob0;
            this.speedModifier = double1;
            this.attackIntervalMin = int2;
            this.attackIntervalMax = int3;
            this.attackRadius = float4;
            this.attackRadiusSqr = float4 * float4;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity $$0 = this.mob.getTarget();
        if ($$0 != null && $$0.isAlive()) {
            this.target = $$0;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() || this.target.isAlive() && !this.mob.getNavigation().isDone();
    }

    @Override
    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        double $$0 = this.mob.m_20275_(this.target.m_20185_(), this.target.m_20186_(), this.target.m_20189_());
        boolean $$1 = this.mob.getSensing().hasLineOfSight(this.target);
        if ($$1) {
            this.seeTime++;
        } else {
            this.seeTime = 0;
        }
        if (!($$0 > (double) this.attackRadiusSqr) && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
        }
        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (--this.attackTime == 0) {
            if (!$$1) {
                return;
            }
            float $$2 = (float) Math.sqrt($$0) / this.attackRadius;
            float $$3 = Mth.clamp($$2, 0.1F, 1.0F);
            this.rangedAttackMob.performRangedAttack(this.target, $$3);
            this.attackTime = Mth.floor($$2 * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt($$0) / (double) this.attackRadius, (double) this.attackIntervalMin, (double) this.attackIntervalMax));
        }
    }
}