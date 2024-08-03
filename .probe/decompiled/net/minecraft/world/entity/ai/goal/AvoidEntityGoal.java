package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class AvoidEntityGoal<T extends LivingEntity> extends Goal {

    protected final PathfinderMob mob;

    private final double walkSpeedModifier;

    private final double sprintSpeedModifier;

    @Nullable
    protected T toAvoid;

    protected final float maxDist;

    @Nullable
    protected Path path;

    protected final PathNavigation pathNav;

    protected final Class<T> avoidClass;

    protected final Predicate<LivingEntity> avoidPredicate;

    protected final Predicate<LivingEntity> predicateOnAvoidEntity;

    private final TargetingConditions avoidEntityTargeting;

    public AvoidEntityGoal(PathfinderMob pathfinderMob0, Class<T> classT1, float float2, double double3, double double4) {
        this(pathfinderMob0, classT1, p_25052_ -> true, float2, double3, double4, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
    }

    public AvoidEntityGoal(PathfinderMob pathfinderMob0, Class<T> classT1, Predicate<LivingEntity> predicateLivingEntity2, float float3, double double4, double double5, Predicate<LivingEntity> predicateLivingEntity6) {
        this.mob = pathfinderMob0;
        this.avoidClass = classT1;
        this.avoidPredicate = predicateLivingEntity2;
        this.maxDist = float3;
        this.walkSpeedModifier = double4;
        this.sprintSpeedModifier = double5;
        this.predicateOnAvoidEntity = predicateLivingEntity6;
        this.pathNav = pathfinderMob0.m_21573_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.avoidEntityTargeting = TargetingConditions.forCombat().range((double) float3).selector(predicateLivingEntity6.and(predicateLivingEntity2));
    }

    public AvoidEntityGoal(PathfinderMob pathfinderMob0, Class<T> classT1, float float2, double double3, double double4, Predicate<LivingEntity> predicateLivingEntity5) {
        this(pathfinderMob0, classT1, p_25049_ -> true, float2, double3, double4, predicateLivingEntity5);
    }

    @Override
    public boolean canUse() {
        this.toAvoid = (T) this.mob.m_9236_().m_45982_(this.mob.m_9236_().m_6443_(this.avoidClass, this.mob.m_20191_().inflate((double) this.maxDist, 3.0, (double) this.maxDist), p_148078_ -> true), this.avoidEntityTargeting, this.mob, this.mob.m_20185_(), this.mob.m_20186_(), this.mob.m_20189_());
        if (this.toAvoid == null) {
            return false;
        } else {
            Vec3 $$0 = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.toAvoid.m_20182_());
            if ($$0 == null) {
                return false;
            } else if (this.toAvoid.m_20275_($$0.x, $$0.y, $$0.z) < this.toAvoid.m_20280_(this.mob)) {
                return false;
            } else {
                this.path = this.pathNav.createPath($$0.x, $$0.y, $$0.z, 0);
                return this.path != null;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.pathNav.isDone();
    }

    @Override
    public void start() {
        this.pathNav.moveTo(this.path, this.walkSpeedModifier);
    }

    @Override
    public void stop() {
        this.toAvoid = null;
    }

    @Override
    public void tick() {
        if (this.mob.m_20280_(this.toAvoid) < 49.0) {
            this.mob.m_21573_().setSpeedModifier(this.sprintSpeedModifier);
        } else {
            this.mob.m_21573_().setSpeedModifier(this.walkSpeedModifier);
        }
    }
}