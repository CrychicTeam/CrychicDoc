package io.redspace.ironsspellbooks.entity.mobs.goals;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class AcquireTargetNearLocationGoal<T extends LivingEntity> extends TargetGoal {

    private Vec3 targetSearchPos;

    protected final Class<T> targetType;

    protected final int randomInterval;

    @Nullable
    protected LivingEntity target;

    protected TargetingConditions targetConditions;

    public AcquireTargetNearLocationGoal(Mob pMob, Class<T> pTargetType, int pRandomInterval, boolean pMustSee, boolean pMustReach, Vec3 targetSearchPos, @Nullable Predicate<LivingEntity> pTargetPredicate) {
        super(pMob, pMustSee, pMustReach);
        this.targetType = pTargetType;
        this.randomInterval = m_186073_(pRandomInterval);
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
        this.targetConditions = TargetingConditions.forCombat().range(this.m_7623_()).selector(pTargetPredicate);
        this.targetSearchPos = targetSearchPos;
    }

    @Override
    public boolean canUse() {
        if (this.randomInterval > 0 && this.f_26135_.m_217043_().nextInt(this.randomInterval) != 0) {
            return false;
        } else {
            this.findTarget();
            return this.target != null;
        }
    }

    protected AABB getTargetSearchArea(double pTargetDistance) {
        return this.f_26135_.m_20191_().inflate(pTargetDistance, 4.0, pTargetDistance);
    }

    protected void findTarget() {
        if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {
            AABB targetSearchArea = this.getTargetSearchArea(this.m_7623_());
            List<T> entitiesOfClass = this.f_26135_.m_9236_().m_6443_(this.targetType, targetSearchArea, potentialTarget -> true);
            this.target = this.f_26135_.m_9236_().m_45982_(entitiesOfClass, this.targetConditions, this.f_26135_, this.f_26135_.m_20185_(), this.f_26135_.m_20188_(), this.f_26135_.m_20189_());
        } else {
            this.target = this.f_26135_.m_9236_().m_45949_(this.targetConditions, this.f_26135_, this.f_26135_.m_20185_(), this.f_26135_.m_20188_(), this.f_26135_.m_20189_());
        }
    }

    @Override
    public void start() {
        this.f_26135_.setTarget(this.target);
        super.start();
    }

    public void setTarget(@Nullable LivingEntity pTarget) {
        this.target = pTarget;
    }
}