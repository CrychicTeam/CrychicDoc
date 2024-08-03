package com.mna.entities.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;

public class RetaliateOnAttackGoal extends TargetGoal {

    private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

    private boolean entityCallsForHelp;

    private int revengeTimerOld;

    private final Class<?>[] excludedReinforcementTypes;

    private Class<?>[] reinforcementTypes;

    public RetaliateOnAttackGoal(Mob creatureIn, Class<?>... excludeReinforcementTypes) {
        super(creatureIn, true);
        this.excludedReinforcementTypes = excludeReinforcementTypes;
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        int i = this.f_26135_.m_21213_();
        LivingEntity livingentity = this.f_26135_.m_21188_();
        if (livingentity == this.f_26135_) {
            this.f_26135_.m_6703_(null);
            return false;
        } else if (i != this.revengeTimerOld && livingentity != null) {
            if (livingentity.m_6095_() == EntityType.PLAYER && this.f_26135_.m_9236_().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                return false;
            } else {
                for (Class<?> oclass : this.excludedReinforcementTypes) {
                    if (oclass.isAssignableFrom(livingentity.getClass())) {
                        return false;
                    }
                }
                return this.m_26150_(livingentity, HURT_BY_TARGETING);
            }
        } else {
            return false;
        }
    }

    public RetaliateOnAttackGoal setCallsForHelp(Class<?>... reinforcementTypes) {
        this.entityCallsForHelp = true;
        this.reinforcementTypes = reinforcementTypes;
        return this;
    }

    @Override
    public void start() {
        this.f_26135_.setTarget(this.f_26135_.m_21188_());
        this.f_26137_ = this.f_26135_.getTarget();
        this.revengeTimerOld = this.f_26135_.m_21213_();
        this.f_26138_ = 300;
        if (this.entityCallsForHelp) {
            this.alertOthers();
        }
        super.start();
    }

    protected void alertOthers() {
        double d0 = this.m_7623_();
        AABB axisalignedbb = AABB.unitCubeFromLowerCorner(this.f_26135_.m_20182_()).inflate(d0, 10.0, d0);
        label56: for (Mob mobentity : this.f_26135_.m_9236_().m_45976_(this.f_26135_.getClass(), axisalignedbb)) {
            if (this.f_26135_ != mobentity && mobentity.getTarget() == null && (!(this.f_26135_ instanceof TamableAnimal) || ((TamableAnimal) this.f_26135_).m_269323_() == ((TamableAnimal) mobentity).m_269323_()) && !mobentity.m_7307_(this.f_26135_.m_21188_())) {
                if (this.reinforcementTypes != null) {
                    boolean flag = false;
                    Class[] var8 = this.reinforcementTypes;
                    int var9 = var8.length;
                    int var10 = 0;
                    while (true) {
                        if (var10 < var9) {
                            Class<?> oclass = var8[var10];
                            if (mobentity.getClass() != oclass) {
                                var10++;
                                continue;
                            }
                            flag = true;
                        }
                        if (!flag) {
                            break;
                        }
                        continue label56;
                    }
                }
                this.setAttackTarget(mobentity, this.f_26135_.m_21188_());
            }
        }
    }

    protected void setAttackTarget(Mob mobIn, LivingEntity targetIn) {
        mobIn.setTarget(targetIn);
    }
}