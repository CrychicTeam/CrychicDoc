package net.minecraft.world.entity.ai.goal.target;

import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class NearestAttackableTargetGoal<T extends LivingEntity> extends TargetGoal {

    private static final int DEFAULT_RANDOM_INTERVAL = 10;

    protected final Class<T> targetType;

    protected final int randomInterval;

    @Nullable
    protected LivingEntity target;

    protected TargetingConditions targetConditions;

    public NearestAttackableTargetGoal(Mob mob0, Class<T> classT1, boolean boolean2) {
        this(mob0, classT1, 10, boolean2, false, null);
    }

    public NearestAttackableTargetGoal(Mob mob0, Class<T> classT1, boolean boolean2, Predicate<LivingEntity> predicateLivingEntity3) {
        this(mob0, classT1, 10, boolean2, false, predicateLivingEntity3);
    }

    public NearestAttackableTargetGoal(Mob mob0, Class<T> classT1, boolean boolean2, boolean boolean3) {
        this(mob0, classT1, 10, boolean2, boolean3, null);
    }

    public NearestAttackableTargetGoal(Mob mob0, Class<T> classT1, int int2, boolean boolean3, boolean boolean4, @Nullable Predicate<LivingEntity> predicateLivingEntity5) {
        super(mob0, boolean3, boolean4);
        this.targetType = classT1;
        this.randomInterval = m_186073_(int2);
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
        this.targetConditions = TargetingConditions.forCombat().range(this.m_7623_()).selector(predicateLivingEntity5);
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

    protected AABB getTargetSearchArea(double double0) {
        return this.f_26135_.m_20191_().inflate(double0, 4.0, double0);
    }

    protected void findTarget() {
        if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {
            this.target = this.f_26135_.m_9236_().m_45982_(this.f_26135_.m_9236_().m_6443_(this.targetType, this.getTargetSearchArea(this.m_7623_()), p_148152_ -> true), this.targetConditions, this.f_26135_, this.f_26135_.m_20185_(), this.f_26135_.m_20188_(), this.f_26135_.m_20189_());
        } else {
            this.target = this.f_26135_.m_9236_().m_45949_(this.targetConditions, this.f_26135_, this.f_26135_.m_20185_(), this.f_26135_.m_20188_(), this.f_26135_.m_20189_());
        }
    }

    @Override
    public void start() {
        this.f_26135_.setTarget(this.target);
        super.start();
    }

    public void setTarget(@Nullable LivingEntity livingEntity0) {
        this.target = livingEntity0;
    }
}