package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class LookAtPlayerGoal extends Goal {

    public static final float DEFAULT_PROBABILITY = 0.02F;

    protected final Mob mob;

    @Nullable
    protected Entity lookAt;

    protected final float lookDistance;

    private int lookTime;

    protected final float probability;

    private final boolean onlyHorizontal;

    protected final Class<? extends LivingEntity> lookAtType;

    protected final TargetingConditions lookAtContext;

    public LookAtPlayerGoal(Mob mob0, Class<? extends LivingEntity> classExtendsLivingEntity1, float float2) {
        this(mob0, classExtendsLivingEntity1, float2, 0.02F);
    }

    public LookAtPlayerGoal(Mob mob0, Class<? extends LivingEntity> classExtendsLivingEntity1, float float2, float float3) {
        this(mob0, classExtendsLivingEntity1, float2, float3, false);
    }

    public LookAtPlayerGoal(Mob mob0, Class<? extends LivingEntity> classExtendsLivingEntity1, float float2, float float3, boolean boolean4) {
        this.mob = mob0;
        this.lookAtType = classExtendsLivingEntity1;
        this.lookDistance = float2;
        this.probability = float3;
        this.onlyHorizontal = boolean4;
        this.m_7021_(EnumSet.of(Goal.Flag.LOOK));
        if (classExtendsLivingEntity1 == Player.class) {
            this.lookAtContext = TargetingConditions.forNonCombat().range((double) float2).selector(p_25531_ -> EntitySelector.notRiding(mob0).test(p_25531_));
        } else {
            this.lookAtContext = TargetingConditions.forNonCombat().range((double) float2);
        }
    }

    @Override
    public boolean canUse() {
        if (this.mob.m_217043_().nextFloat() >= this.probability) {
            return false;
        } else {
            if (this.mob.getTarget() != null) {
                this.lookAt = this.mob.getTarget();
            }
            if (this.lookAtType == Player.class) {
                this.lookAt = this.mob.m_9236_().m_45949_(this.lookAtContext, this.mob, this.mob.m_20185_(), this.mob.m_20188_(), this.mob.m_20189_());
            } else {
                this.lookAt = this.mob.m_9236_().m_45982_(this.mob.m_9236_().m_6443_(this.lookAtType, this.mob.m_20191_().inflate((double) this.lookDistance, 3.0, (double) this.lookDistance), p_148124_ -> true), this.lookAtContext, this.mob, this.mob.m_20185_(), this.mob.m_20188_(), this.mob.m_20189_());
            }
            return this.lookAt != null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.lookAt.isAlive()) {
            return false;
        } else {
            return this.mob.m_20280_(this.lookAt) > (double) (this.lookDistance * this.lookDistance) ? false : this.lookTime > 0;
        }
    }

    @Override
    public void start() {
        this.lookTime = this.m_183277_(40 + this.mob.m_217043_().nextInt(40));
    }

    @Override
    public void stop() {
        this.lookAt = null;
    }

    @Override
    public void tick() {
        if (this.lookAt.isAlive()) {
            double $$0 = this.onlyHorizontal ? this.mob.m_20188_() : this.lookAt.getEyeY();
            this.mob.getLookControl().setLookAt(this.lookAt.getX(), $$0, this.lookAt.getZ());
            this.lookTime--;
        }
    }
}