package net.minecraft.world.entity.ai.goal.target;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;

public class HurtByTargetGoal extends TargetGoal {

    private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

    private static final int ALERT_RANGE_Y = 10;

    private boolean alertSameType;

    private int timestamp;

    private final Class<?>[] toIgnoreDamage;

    @Nullable
    private Class<?>[] toIgnoreAlert;

    public HurtByTargetGoal(PathfinderMob pathfinderMob0, Class<?>... class1) {
        super(pathfinderMob0, true);
        this.toIgnoreDamage = class1;
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        int $$0 = this.f_26135_.m_21213_();
        LivingEntity $$1 = this.f_26135_.m_21188_();
        if ($$0 != this.timestamp && $$1 != null) {
            if ($$1.m_6095_() == EntityType.PLAYER && this.f_26135_.m_9236_().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                return false;
            } else {
                for (Class<?> $$2 : this.toIgnoreDamage) {
                    if ($$2.isAssignableFrom($$1.getClass())) {
                        return false;
                    }
                }
                return this.m_26150_($$1, HURT_BY_TARGETING);
            }
        } else {
            return false;
        }
    }

    public HurtByTargetGoal setAlertOthers(Class<?>... class0) {
        this.alertSameType = true;
        this.toIgnoreAlert = class0;
        return this;
    }

    @Override
    public void start() {
        this.f_26135_.setTarget(this.f_26135_.m_21188_());
        this.f_26137_ = this.f_26135_.getTarget();
        this.timestamp = this.f_26135_.m_21213_();
        this.f_26138_ = 300;
        if (this.alertSameType) {
            this.alertOthers();
        }
        super.start();
    }

    protected void alertOthers() {
        double $$0 = this.m_7623_();
        AABB $$1 = AABB.unitCubeFromLowerCorner(this.f_26135_.m_20182_()).inflate($$0, 10.0, $$0);
        List<? extends Mob> $$2 = this.f_26135_.m_9236_().m_6443_(this.f_26135_.getClass(), $$1, EntitySelector.NO_SPECTATORS);
        Iterator var5 = $$2.iterator();
        while (true) {
            Mob $$3;
            while (true) {
                if (!var5.hasNext()) {
                    return;
                }
                $$3 = (Mob) var5.next();
                if (this.f_26135_ != $$3 && $$3.getTarget() == null && (!(this.f_26135_ instanceof TamableAnimal) || ((TamableAnimal) this.f_26135_).m_269323_() == ((TamableAnimal) $$3).m_269323_()) && !$$3.m_7307_(this.f_26135_.m_21188_())) {
                    if (this.toIgnoreAlert == null) {
                        break;
                    }
                    boolean $$4 = false;
                    for (Class<?> $$5 : this.toIgnoreAlert) {
                        if ($$3.getClass() == $$5) {
                            $$4 = true;
                            break;
                        }
                    }
                    if (!$$4) {
                        break;
                    }
                }
            }
            this.alertOther($$3, this.f_26135_.m_21188_());
        }
    }

    protected void alertOther(Mob mob0, LivingEntity livingEntity1) {
        mob0.setTarget(livingEntity1);
    }
}