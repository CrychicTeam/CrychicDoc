package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityBunfungus;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;

public class MungusAIAlertBunfungus extends TargetGoal {

    private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

    private static final int ALERT_RANGE_Y = 10;

    private boolean alertSameType;

    private int timestamp;

    private final Class<?>[] toIgnoreDamage;

    @Nullable
    private Class<?>[] toIgnoreAlert;

    public MungusAIAlertBunfungus(PathfinderMob pathfinderMob0, Class<?>... class1) {
        super(pathfinderMob0, true);
        this.toIgnoreDamage = class1;
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        int i = this.f_26135_.m_21213_();
        LivingEntity livingentity = this.f_26135_.m_21188_();
        if (i != this.timestamp && livingentity != null) {
            if (livingentity.m_6095_() == EntityType.PLAYER && this.f_26135_.m_9236_().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                return false;
            } else {
                for (Class<?> oclass : this.toIgnoreDamage) {
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

    @Override
    public void start() {
        this.f_26135_.setTarget(this.f_26135_.m_21188_());
        this.f_26137_ = this.f_26135_.getTarget();
        this.timestamp = this.f_26135_.m_21213_();
        this.f_26138_ = 300;
        this.alertOthers();
        super.start();
    }

    protected void alertOthers() {
        double d0 = this.m_7623_();
        AABB aabb = AABB.unitCubeFromLowerCorner(this.f_26135_.m_20182_()).inflate(d0, 10.0, d0);
        label53: for (EntityBunfungus mob : this.f_26135_.m_9236_().m_6443_(EntityBunfungus.class, aabb, EntitySelector.NO_SPECTATORS)) {
            if (this.f_26135_ != mob && mob.m_5448_() == null && !mob.m_7307_(this.f_26135_.m_21188_()) && mob.defendsMungusAgainst(this.f_26135_.m_21188_())) {
                if (this.toIgnoreAlert != null) {
                    boolean flag = false;
                    Class[] var8 = this.toIgnoreAlert;
                    int var9 = var8.length;
                    int var10 = 0;
                    while (true) {
                        if (var10 < var9) {
                            Class<?> oclass = var8[var10];
                            if (mob.getClass() != oclass) {
                                var10++;
                                continue;
                            }
                            flag = true;
                        }
                        if (!flag) {
                            break;
                        }
                        continue label53;
                    }
                }
                this.alertOther(mob, this.f_26135_.m_21188_());
            }
        }
    }

    protected void alertOther(Mob mob0, LivingEntity livingEntity1) {
        mob0.setTarget(livingEntity1);
    }
}